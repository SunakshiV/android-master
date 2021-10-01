package com.procoin.nsk;

import android.util.Log;

import com.procoin.http.TjrBaseApi;
import com.procoin.http.util.MD5;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.nsk.exceptions.TjrNSKConnectionException;
import com.procoin.nsk.exceptions.TjrNSKDataException;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelEvent;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.oio.OioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.queue.BlockingReadTimeoutException;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.internal.DeadLockProofWorker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


public class ConnectionNetty {
	private String host;
	private int port = Protocol.DEFAULT_PORT;
	private volatile Channel channel;
	private ClientBootstrap bootstrap;
	private volatile TjrSocketClientHandler handler;
	private int timeout = Protocol.DEFAULT_TIMEOUT;
    private int soTimeout = Protocol.DEFAULT_SOTIMEOUT;
    private final AtomicLong transferredMessages = new AtomicLong();
	public ConnectionNetty(final String host) {
		super();
		this.host = host;
	}

	public ConnectionNetty(final String host, final int port) {
		super();
		this.host = host;
		this.port = port;
	}
	
	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public int getSoTimeout() {
		return soTimeout;
	}

	public void setSoTimeout(int soTimeout) {
		this.soTimeout = soTimeout;
	}

	public String getHost() {
		return host;
	}

	public void connect() {
		if (!isConnected()) {
			bootstrap = new ClientBootstrap(new OioClientSocketChannelFactory(Executors.newCachedThreadPool()));
			bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
				public ChannelPipeline getPipeline() throws Exception {
					ChannelPipeline pipeline = Channels.pipeline();
					handler = new TjrSocketClientHandler();
					pipeline.addLast("framer", new DelimiterBasedFrameDecoder(8192*10, Delimiters.lineDelimiter()));
					pipeline.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
					pipeline.addLast("encoder", new StringEncoder(CharsetUtil.UTF_8));
					pipeline.addLast("handler", handler);
					return pipeline;
				}
			});
			bootstrap.setOption("tcpNoDelay", true);
			bootstrap.setOption("keepAlive", true);
			bootstrap.setOption("reuseAddress", true);
			bootstrap.setOption("connectTimeoutMillis", timeout);
			// Start the connection attempt.
			if(TjrBaseApi.isDebug)Log.d("Protocol", "---------------connect-----------host="+host+"  port="+port);
			ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
			channel = future.awaitUninterruptibly().getChannel();
			if (!future.isSuccess()) {
				bootstrap.releaseExternalResources();
				throw new TjrNSKConnectionException(future.getCause());
			}
		}
	}

	public boolean isConnected() {
		return channel != null && channel.isBound() && channel.isOpen() && channel.isConnected();
	}

	public void disconnect() {
		try {
			if (channel != null) channel.close().awaitUninterruptibly();
			// Shut down all thread pools to exit.
			if (bootstrap != null) bootstrap.releaseExternalResources();
		} catch (Exception e) {
			throw new TjrNSKConnectionException(e);
		}
	}

	/**
	 * 这里方法里要同步
	 * @param cmd
	 * @return
	 */
	protected String sendCommand(final String cmd){
		connect();
		long msgId = transferredMessages.incrementAndGet();
		String msgIdSign = MD5.getMessageDigest(msgId + VHttpServiceManager.API_SECRET);
		String rex = msgId+Protocol.DOLLAR_BYTE;
		if("".equals(cmd)){
			channel.write("/?msgId="+msgId+"\r\n");
		}else{
			channel.write(cmd+"&msgId="+msgId+"&msgIdSign="+msgIdSign+"\r\n");
		}
		if(TjrBaseApi.isDebug)Log.d("Protocol", "--------------------------rex="+rex);
		for (;;) {
			try {
				String message = handler.read(soTimeout, TimeUnit.MILLISECONDS);
				if(message == null){
					disconnect();
					throw new TjrNSKDataException("data read timeout");
				}else {
					if(message.startsWith(rex)){
//						if(Protocol.DEBUG)Log.d("Protocol", "--3--sendCommand...result="+message);
						return message.substring(rex.length());
					}
				}
			} catch (BlockingReadTimeoutException e) {
				// TODO Auto-generated catch block
				if(TjrBaseApi.isDebug)Log.d("Protocol", "--------BlockingReadTimeoutException...e="+e.getMessage());
				disconnect();
				throw new TjrNSKConnectionException(e);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				if(TjrBaseApi.isDebug)Log.d("Protocol", "--------IOException...e="+e.getMessage());
				disconnect();
				throw new TjrNSKConnectionException(e);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				if(TjrBaseApi.isDebug)Log.d("Protocol", "--------InterruptedException...e="+e.getMessage());
//				disconnect();
				throw new TjrNSKConnectionException(e);
			}
		}
    }
	
	
	
	public class TjrSocketClientHandler extends SimpleChannelUpstreamHandler {
		private final BlockingQueue<ChannelEvent> queue;
		private volatile boolean closed;
		
		public TjrSocketClientHandler() {
			queue = new LinkedBlockingQueue<ChannelEvent>();
		}
		
		/**
		 * Returns the queue which stores the received messages. The default
		 * implementation returns the queue which was specified in the
		 * constructor.
		 */
		protected BlockingQueue<ChannelEvent> getQueue() {
			return queue;
		}

		/**
		 * Returns {@code true} if and only if the {@link Channel} associated
		 * with this handler has been closed.
		 * 
		 * @throws IllegalStateException
		 *             if this handler was not added to a
		 *             {@link ChannelPipeline} yet
		 */
		public boolean isClosed() {
			return closed;
		}

		/**
		 * Waits until a new message is received or the associated
		 * {@link Channel} is closed.
		 * 
		 * @return the received message or {@code null} if the associated
		 *         {@link Channel} has been closed
		 * @throws IOException
		 *             if failed to receive a new message
		 * @throws InterruptedException
		 *             if the operation has been interrupted
		 */
		public String read() throws IOException, InterruptedException {
			ChannelEvent e = readEvent();
			if (e == null) {
				return null;
			}

			if (e instanceof MessageEvent) {
				return getMessage((MessageEvent) e);
			} else if (e instanceof ExceptionEvent) {
				throw (IOException) new IOException().initCause(((ExceptionEvent) e).getCause());
			} else {
				throw new IllegalStateException();
			}
		}

		/**
		 * Waits until a new message is received or the associated
		 * {@link Channel} is closed.
		 * 
		 * @param timeout
		 *            the amount time to wait until a new message is received.
		 *            If no message is received within the timeout,
		 *            {@link BlockingReadTimeoutException} is thrown.
		 * @param unit
		 *            the unit of {@code timeout}
		 * 
		 * @return the received message or {@code null} if the associated
		 *         {@link Channel} has been closed
		 * @throws BlockingReadTimeoutException
		 *             if no message was received within the specified timeout
		 * @throws IOException
		 *             if failed to receive a new message
		 * @throws InterruptedException
		 *             if the operation has been interrupted
		 */
		public String read(long timeout, TimeUnit unit) throws IOException, InterruptedException,BlockingReadTimeoutException {
			ChannelEvent e = readEvent(timeout, unit);
			if (e == null) {
				return null;
			}

			if (e instanceof MessageEvent) {
				return getMessage((MessageEvent) e);
			} else if (e instanceof ExceptionEvent) {
				throw (IOException) new IOException().initCause(((ExceptionEvent) e).getCause());
			} else {
				throw new IllegalStateException();
			}
		}

		/**
		 * Waits until a new {@link ChannelEvent} is received or the associated
		 * {@link Channel} is closed.
		 * 
		 * @return a {@link MessageEvent} or an {@link ExceptionEvent}.
		 *         {@code null} if the associated {@link Channel} has been
		 *         closed
		 * @throws InterruptedException
		 *             if the operation has been interrupted
		 */
		public ChannelEvent readEvent() throws InterruptedException {
			detectDeadLock();
			if (isClosed()) {
				if (getQueue().isEmpty()) {
					return null;
				}
			}

			ChannelEvent e = getQueue().take();
			if (e instanceof ChannelStateEvent) {
				// channelClosed has been triggered.
				assert closed;
				return null;
			} else {
				return e;
			}
		}

		/**
		 * Waits until a new {@link ChannelEvent} is received or the associated
		 * {@link Channel} is closed.
		 * 
		 * @param timeout
		 *            the amount time to wait until a new {@link ChannelEvent}
		 *            is received. If no message is received within the timeout,
		 *            {@link BlockingReadTimeoutException} is thrown.
		 * @param unit
		 *            the unit of {@code timeout}
		 * 
		 * @return a {@link MessageEvent} or an {@link ExceptionEvent}.
		 *         {@code null} if the associated {@link Channel} has been
		 *         closed
		 * @throws BlockingReadTimeoutException
		 *             if no event was received within the specified timeout
		 * @throws InterruptedException
		 *             if the operation has been interrupted
		 */
		public ChannelEvent readEvent(long timeout, TimeUnit unit) throws InterruptedException, BlockingReadTimeoutException {
			detectDeadLock();
			if (isClosed()) {
				if (getQueue().isEmpty()) {
					return null;
				}
			}

			ChannelEvent e = getQueue().poll(timeout, unit);
			if (e == null) {
				throw new BlockingReadTimeoutException();
			} else if (e instanceof ChannelStateEvent) {
				// channelClosed has been triggered.
				assert closed;
				return null;
			} else {
				return e;
			}
		}

		private void detectDeadLock() {
			if (DeadLockProofWorker.PARENT.get() != null) {
				throw new IllegalStateException("read*(...) in I/O thread causes a dead lock or " + "sudden performance drop. Implement a state machine or " + "call read*() from a different thread.");
			}
		}
		
		private String getMessage(MessageEvent e) {
			return (String) e.getMessage();
		}
		
		@Override
		public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception{
			closed = true;
		}
		
		@Override
		public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
			// Print out the line received from the server.
//			if(Protocol.DEBUG)Log.d("Protocol", "=========收到服务器messageReceived：="+e.getMessage()+ "  size="+getQueue().size());
			getQueue().put(e);
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
			if(TjrBaseApi.isDebug)Log.d("Protocol", "=========收到服务器exceptionCaught：="+e.getCause()+ "  size="+getQueue().size());
			getQueue().put(e);
		}

	}
}
