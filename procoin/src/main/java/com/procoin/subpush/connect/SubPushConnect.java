package com.procoin.subpush.connect;

import android.content.Context;
import android.util.Log;

import com.procoin.subpush.Consts;
import com.procoin.subpush.connect.listen.ConnectListen;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.DelimiterBasedFrameDecoder;
import org.jboss.netty.handler.codec.frame.Delimiters;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.handler.timeout.IdleStateHandler;
import org.jboss.netty.handler.timeout.ReadTimeoutException;
import org.jboss.netty.util.CharsetUtil;
import org.jboss.netty.util.HashedWheelTimer;
import org.jboss.netty.util.Timeout;
import org.jboss.netty.util.Timer;
import org.jboss.netty.util.TimerTask;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SubPushConnect {
    private static final int PORT = 9779;  ////port 8686
    // Sleep 5 seconds before a reconnection attempt.
    public static final int RECONNECT_DELAY = 5;
    // Reconnect when the server sends nothing for 10 seconds.
    public static final int READ_TIMEOUT = 240;
    // write when the client sends nothing for 300 seconds.
    public static final int WRITE_TIMEOUT = 120;
    private ConnectListen mConnectListen;
    private Context mContext;
    private volatile boolean isRunConnect;

    private volatile static SubPushConnect instance;
    private volatile Timer timer;
    private volatile ClientBootstrap bootstrap;
    private volatile Channel mChannel;

    public SubPushConnect() {
        isRunConnect = false;
    }

    public static SubPushConnect getInstance() {
        if (instance == null) {
            synchronized (SubPushConnect.class) {
                if (instance == null) instance = new SubPushConnect();
            }
        }
        return instance;
    }

    public synchronized void initConfigureBootstrap(Context mContext, String socketHost, ConnectListen mConnectListen) {
        if (isRunConnect) return;
        this.mContext = mContext;
        this.mConnectListen = mConnectListen;
        timer = new HashedWheelTimer();
        // Configure the client.
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
        // Configure the pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            // 心跳包
            final ChannelHandler idleStateHandler = new IdleStateHandler(timer, READ_TIMEOUT, WRITE_TIMEOUT, 0);
            final ChannelHandler heartbeatHandler = new HeartbeatHandler();
            // private final ChannelHandler timeoutHandler = new
            // ReadTimeoutHandler(timer, READ_TIMEOUT);
            private final ChannelHandler uptimeHandler = new UptimeClientHandler(bootstrap, timer);

            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(idleStateHandler, heartbeatHandler, new DelimiterBasedFrameDecoder(8192 * 8192, Delimiters.lineDelimiter()), new StringDecoder(CharsetUtil.UTF_8), new StringEncoder(CharsetUtil.UTF_8), uptimeHandler);
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);
        bootstrap.setOption("reuseAddress", true);
        bootstrap.setOption("remoteAddress", new InetSocketAddress(socketHost, PORT));
        Log.d("ReceivedManager", String.format("[initConfigureBootstrap SERVER IS] %s%n", socketHost));
        mChannel = bootstrap.connect().awaitUninterruptibly().getChannel();
        mConnectListen.channel(mChannel);
        isRunConnect = true;
    }

    public synchronized void shutBootstrap() {
        try {
            isRunConnect = false;
            if (timer != null) timer.stop();
            if (mChannel != null) mChannel.close().awaitUninterruptibly();
            if (bootstrap != null) bootstrap.releaseExternalResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class UptimeClientHandler extends SimpleChannelUpstreamHandler {

        final ClientBootstrap bootstrap;
        private final Timer timer;
        private long startTime = -1;

        public UptimeClientHandler(ClientBootstrap bootstrap, Timer timer) {
            this.bootstrap = bootstrap;
            this.timer = timer;
        }

        InetSocketAddress getRemoteAddress() {
            return (InetSocketAddress) bootstrap.getOption("remoteAddress");
        }

        @Override
        public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
//			println("Disconnected from: " + getRemoteAddress());
            mConnectListen.messageReceived(Consts.CONNECTION_ERROR);
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
			println("Sleeping for: " + RECONNECT_DELAY + 's');
            timer.newTimeout(new TimerTask() {
                public void run(Timeout timeout) {
					println("Reconnecting to: " + getRemoteAddress());
//					PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(mContext, SubPushService.class.getSimpleName());
//					wakeLock.acquire();
//					try{
//						mConnectListen.channel(bootstrap.connect().awaitUninterruptibly().getChannel());
//					}finally{
//						wakeLock.release();
//					}
                    mChannel = bootstrap.connect().awaitUninterruptibly().getChannel();
                    mConnectListen.channel(mChannel);
                }
            }, RECONNECT_DELAY, TimeUnit.SECONDS);
        }

        @Override
        public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
            if (startTime < 0) {
                startTime = System.currentTimeMillis();
            }
			println("Connected to: " + getRemoteAddress());
        }

        @Override
        public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
			println("收到:"+e.getMessage());
            mConnectListen.messageReceived((String) e.getMessage());
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
            Throwable cause = e.getCause();
//			Log.e("SubPushService", "exceptionCaught---------------------"+cause.getMessage());
            if (cause instanceof ConnectException) {
                startTime = -1;
//				println("Failed to connect: " + cause.getMessage());
            }
            if (cause instanceof ReadTimeoutException) {
                // The connection was OK but there was no traffic for last
                // period.
//				println("Disconnecting due to no inbound traffic");
            } else {
                cause.printStackTrace();
            }
            mConnectListen.messageReceived(Consts.CONNECTION_ERROR);
            ctx.getChannel().close();
        }

        private void println(String msg) {
            if (startTime < 0) {
                Log.d("ReceivedManager", String.format("[SERVER IS DOWN] %s%n", msg));
            } else {
                Log.d("ReceivedManager", String.format("[UPTIME: %5ds] %s%n", (System.currentTimeMillis() - startTime) / 1000, msg));
            }
        }
    }

}
