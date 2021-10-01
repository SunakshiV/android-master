package com.procoin.module.kbt.app.lightningprediction;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.procoin.module.kbt.app.lightningprediction.entity.KbtTicket;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.subpush.connect.HeartbeatHandler;
import com.procoin.util.JsonParserUtils;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.SubPushHttp;
import com.procoin.module.kbt.app.lightningprediction.entity.Comment;
import com.procoin.module.kbt.app.lightningprediction.entity.PreGame;
import com.procoin.subpush.Consts;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
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
import org.json.JSONObject;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class GameSeverConnect extends Thread {
    private static final int PORT = 20018;  ////port
    // Sleep 5 seconds before a reconnection attempt.
    public static final int RECONNECT_DELAY = 1;
    // Reconnect when the server sends nothing for 10 seconds.
    public static final int READ_TIMEOUT = 10;
    // write when the client sends nothing for 300 seconds.
    public static final int WRITE_TIMEOUT = 60;

    private final ClientBootstrap bootstrap;
    private volatile Channel mChannel;
    private volatile GameSeverConnect instance;
    private volatile boolean isRunConnect;

    private final Timer timer;
    private final String host;
    private final long userId;
    private final Handler messageHandler;


    private final Gson gson = new Gson();

    public GameSeverConnect(long userId, String host, Handler messageHandler) {
        this.userId = userId;
        this.host = host;
        this.messageHandler = messageHandler;
        this.isRunConnect = false;
        timer = new HashedWheelTimer();
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
    }

    public void shutBootstrap() {
        try {
            isRunConnect = false;
            if (timer != null) timer.stop();
            if (mChannel != null) mChannel.close().awaitUninterruptibly();
            bootstrap.releaseExternalResources();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        if (!isRunConnect) {
            // Configure the pipeline factory.
            bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
                // 心跳包
                final ChannelHandler idleStateHandler = new IdleStateHandler(timer, READ_TIMEOUT, WRITE_TIMEOUT, 0);
                final ChannelHandler heartbeatHandler = new HeartbeatHandler();
                private final ChannelHandler uptimeHandler = new UptimeClientHandler(bootstrap, timer);

                public ChannelPipeline getPipeline() throws Exception {
                    return Channels.pipeline(idleStateHandler, heartbeatHandler, new DelimiterBasedFrameDecoder(8192 * 8192, Delimiters.lineDelimiter()), new StringDecoder(CharsetUtil.UTF_8), new StringEncoder(CharsetUtil.UTF_8), uptimeHandler);
                }
            });
            bootstrap.setOption("tcpNoDelay", true);
            bootstrap.setOption("keepAlive", true);
            bootstrap.setOption("reuseAddress", true);
            bootstrap.setOption("remoteAddress", new InetSocketAddress(host, PORT));
            Log.d("LPHomeActivity", String.format("[initConfigureBootstrap SERVER IS] %s%n", host));
            try{
                mChannel = bootstrap.connect().awaitUninterruptibly().getChannel();
                isRunConnect = true;
            }catch (Exception e){
                e.printStackTrace();
                Log.d("LPHomeActivity", "Exception=="+e.toString());
            }

        }
    }

    public int sendText(String reqUrl) {
        println("sendText    isRunConnect==" + isRunConnect + "   isConnected():" + isConnected() + "     sendText:" + reqUrl);
        if (isConnected()) {
            ChannelFuture channelFuture = mChannel.write(reqUrl + Consts.R_N).awaitUninterruptibly();
            if (!channelFuture.isSuccess()) {
                channelFuture.getChannel().close();
                return -1;
            }
        } else {
            if (mChannel != null) {
                mChannel.close();
                return -1;
            }
        }
        return 1;
    }

    public boolean isConnected() {
        return mChannel != null && mChannel.isBound() && mChannel.isOpen() && mChannel.isConnected();
    }

    private void println(String msg) {
        Log.d("LPHomeActivity", String.format("[SERVER IS  %s%n", msg));
    }

    class UptimeClientHandler extends SimpleChannelUpstreamHandler {

        private final ClientBootstrap bootstrap;
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
            println("Disconnected from: " + getRemoteAddress());
        }

        @Override
        public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) {
            println("Sleeping for: " + RECONNECT_DELAY + 's');
            timer.newTimeout(new TimerTask() {
                public void run(Timeout timeout) {
                    println("Reconnecting to: " + getRemoteAddress());
                    mChannel = bootstrap.connect().awaitUninterruptibly().getChannel();
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
            println("收到:" + e.getMessage());
//                mConnectListen.messageReceived((String) e.getMessage());
            //这里解析数据
            String json = (String) e.getMessage();
            if (Consts.PONG.equals(json)) return;
            if (Consts.REQDO_LOGIN.equals(json)) {
                sendText(SubPushHttp.getInstance().predictLogin(userId));
            } else if (Consts.REQDO_OK.equals(json)) {

            } else {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (JsonParserUtils.hasNotNullAndIsIntOrLong(jsonObject, "type")) {
                        int type = jsonObject.getInt("type");
                        boolean success = jsonObject.getBoolean("success");
                        String receive = null;// 接收的数据，是字符串类型
                        if (JsonParserUtils.hasAndNotNull(jsonObject, "receive")) {
                            receive = jsonObject.getString("receive");
                        }
                        if (!success) {//false 代表发送失败  {"receive":"{\"code\":40000,\"msg\":\"请求失败,请重新请求\",\"success\":false}","success":false,"type":214}
                            if (!TextUtils.isEmpty(receive)) {
                                Message message = new Message();
                                message.obj = new ReceiveModel(type, receive);
                                messageHandler.sendMessage(message);
                            }
                        } else {
                            if (!TextUtils.isEmpty(receive)) {
                                Object obj = null;
                                switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(type)) {
                                    case game_predict_main:
                                        obj = gson.fromJson(receive, PreGame.class);
                                        break;
                                    case game_predict_user:// {"receive":"{\"abilityValue\":0,\"holdAmount\":0}","success":true,"type":20001}
                                        break;
                                    case game_predict_tickets://{"receive":"[{\"name\":\"新手入场券\",\"price\":100,\"type\":0},{\"name\":\"职业入场券\",\"price\":300,\"type\":0},{\"name\":\"大师入场券\",\"price\":500,\"type\":0}]","success":true,"type":20002}
                                        obj = gson.fromJson(receive, new TypeToken<Group<KbtTicket>>() {
                                        }.getType());
                                        break;
                                    case game_predict_chat://{"receive":"{\"headUrl\":\"http://192.168.1.66:16678/cropyme/user/image/20190626174233963047533.png\",\"say\":\"很好玩哟\"}","success":true,"type":20003}
                                        obj = gson.fromJson(receive, Comment.class);
                                        break;
                                }
                                Message message = new Message();
                                message.obj = new ReceiveModel(type, obj != null ? obj : receive);
                                messageHandler.sendMessage(message);


                            }


                        }
                    }
                } catch (Exception ex) {
                    println("异常:" + ex.getMessage());
                }
            }

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
            Throwable cause = e.getCause();
            println("exceptionCaught异常:" + cause.getMessage());
            if (cause instanceof ConnectException) {
                startTime = -1;
                println("Failed to connect: " + cause.getMessage());
            }
            if (cause instanceof ReadTimeoutException) {
                println("Disconnecting due to no inbound traffic");
            } else {
                println("exceptionCaught异常:" + cause);
                cause.printStackTrace();
            }
            ctx.getChannel().close();
        }

    }
}