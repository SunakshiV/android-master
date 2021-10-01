package com.procoin.nsk;

//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.Channel;
//import io.netty.channel.ChannelHandler.Sharable;
//import io.netty.channel.ChannelHandlerContext;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.ChannelOption;
//import io.netty.channel.EventLoopGroup;
//import io.netty.channel.SimpleChannelInboundHandler;
//import io.netty.channel.oio.OioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.oio.OioSocketChannel;
//import io.netty.handler.codec.DelimiterBasedFrameDecoder;
//import io.netty.handler.codec.Delimiters;
//import io.netty.handler.codec.string.StringDecoder;
//import io.netty.handler.codec.string.StringEncoder;
//import io.netty.util.CharsetUtil;
//
//import java.io.IOException;
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.TimeUnit;
//import java.util.concurrent.TimeoutException;
//import java.util.concurrent.atomic.AtomicLong;
//
//import android.util.Log;
//
//import TjrBaseApi;
//import TjrNSKConnectionException;
//import TjrNSKDataException;

public class ConnectionNIO {
//	private String host;
//	private int port = Protocol.DEFAULT_PORT;
//	private volatile Channel channel;
//	private volatile TjrSocketClientHandler handler;
//	private EventLoopGroup group;
//	private int timeout = Protocol.DEFAULT_TIMEOUT;
//	private int soTimeout = Protocol.DEFAULT_SOTIMEOUT;
//	private final AtomicLong transferredMessages = new AtomicLong();
//
//	public ConnectionNIO(final String host) {
//		super();
//		this.host = host;
//	}
//
//	public ConnectionNIO(final String host, final int port) {
//		this(host);
//		this.port = port;
//	}
//
//	public int getTimeout() {
//		return timeout;
//	}
//
//	public void setTimeout(int timeout) {
//		this.timeout = timeout;
//	}
//
//	public int getSoTimeout() {
//		return soTimeout;
//	}
//
//	public void setSoTimeout(int soTimeout) {
//		this.soTimeout = soTimeout;
//	}
//
//	public void connect() {
//		if (!isConnected()) {
//			group = new OioEventLoopGroup();
//			try {
//				Bootstrap b = new Bootstrap();
//				b.group(group).channel(OioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).option(ChannelOption.SO_KEEPALIVE, true).option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout).handler(new ChannelInitializer<SocketChannel>() {
//					@Override
//					public void initChannel(SocketChannel ch) throws Exception {
//						handler = new TjrSocketClientHandler();
//						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(8192 * 2, Delimiters.lineDelimiter()), new StringDecoder(CharsetUtil.UTF_8), new StringEncoder(CharsetUtil.UTF_8), handler);
//					}
//				});
//				// Start the connection attempt.
//				channel = b.connect(host, port).sync().channel();
//			} catch (Exception e) {
//				group.shutdownGracefully();
//				throw new TjrNSKConnectionException(e);
//			}
//		}
//	}
//
//	public boolean isConnected() {
//		return channel != null && channel.isActive() && channel.isOpen() && channel.isRegistered();
//	}
//
//	public void disconnect() {
//		try {
//			if (channel != null) channel.closeFuture().sync();
//			// Shut down all thread pools to exit.
//			if (group != null) group.shutdownGracefully();
//		} catch (Exception e) {
//			throw new TjrNSKConnectionException(e);
//		}
//	}
//
//	/**
//	 * 这里方法里要同步
//	 * 
//	 * @param cmd
//	 * @return
//	 */
//	protected String sendCommand(final String cmd) {
//		connect();
//		long msgId = transferredMessages.incrementAndGet();
//		String rex = msgId + Protocol.DOLLAR_BYTE;
//		if ("".equals(cmd)) {
//			channel.writeAndFlush("/?msgId=" + msgId + "\r\n");
//		} else {
//			channel.writeAndFlush(cmd + "&msgId=" + msgId + "\r\n");
//		}
//		if (TjrBaseApi.isDebug) Log.d("Protocol", "--------------------------rex=" + rex);
//		for (;;) {
//			try {
//				String message = handler.read(soTimeout, TimeUnit.MILLISECONDS);
//				if (message == null) {
//					disconnect();
//					throw new TjrNSKDataException("data read timeout");
//				} else {
//					if (message.startsWith(rex)) {
//						// if(Protocol.DEBUG)Log.d("Protocol",
//						// "--3--sendCommand...result="+message);
//						return message.substring(rex.length());
//					}
//				}
//			} catch (TimeoutException e) {
//				// TODO Auto-generated catch block
//				if (TjrBaseApi.isDebug) Log.d("Protocol", "--------BlockingReadTimeoutException...e=" + e.getMessage());
//				disconnect();
//				throw new TjrNSKConnectionException(e);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				if (TjrBaseApi.isDebug) Log.d("Protocol", "--------IOException...e=" + e.getMessage());
//				disconnect();
//				throw new TjrNSKConnectionException(e);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				if (TjrBaseApi.isDebug) Log.d("Protocol", "--------InterruptedException...e=" + e.getMessage());
//				// disconnect();
//				throw new TjrNSKConnectionException(e);
//			}
//		}
//	}
//
//	@Sharable
//	public class TjrSocketClientHandler extends SimpleChannelInboundHandler<String> {
//		private volatile boolean closed;
//		private final BlockingQueue<String> answer = new LinkedBlockingQueue<String>();
//
//		public String read(long timeout, TimeUnit unit) throws IOException, InterruptedException, TimeoutException {
//			if (closed) {
//				if (answer.isEmpty()) {
//					return null;
//				}
//			}
//			String msg = answer.poll(timeout, unit);
//			if (msg == null) {
//				throw new TimeoutException("connection timeout");
//			}
//			return msg;
//		}
//
//		@Override
//		public void channelRegistered(ChannelHandlerContext ctx) {
//			closed = false;
//		}
//
//		@Override
//		public void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
//			// if(Protocol.DEBUG)Log.d("Protocol",
//			// "=========收到服务器messageReceived：="+msg);
//			answer.put(msg);
//		}
//
//		@Override
//		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//			if (TjrBaseApi.isDebug) Log.d("Protocol", "=========收到服务器exceptionCaught：=" + cause.getMessage());
//			closed = true;
//			ctx.close();
//		}
//
//	}

}
