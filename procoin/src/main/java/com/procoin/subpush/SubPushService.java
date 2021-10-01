package com.procoin.subpush;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.procoin.subpush.connect.SubPushConnect;
import com.procoin.subpush.connect.listen.ConnectListen;
import com.procoin.data.sharedpreferences.NormalShareData;
import com.procoin.http.TjrBaseApi;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.http.model.DnsInfo;
import com.procoin.http.tjrcpt.SubPushHttp;
import com.procoin.http.util.ShareData;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SubPushService extends Service implements ConnectListen {
    private static Object lock = new Object();
    private volatile Channel mChannel;
    private volatile long userId;
    private volatile String sessionid;
    private volatile String version;
    private volatile boolean isSuccess;
    private final RemoteCallbackList<IReceivedCallback> mCallbacks = new RemoteCallbackList<IReceivedCallback>();
    private volatile int reqdoOkCount;//由于还没有绑定service，获取不到监听，所以先记录一下，等绑定好了之后就判断这个
    private volatile boolean isBindViewer;
    private final BlockingQueue<String> msgQueue = new ArrayBlockingQueue<String>(2000);

    private final IRemoteService.Stub mBindViewer = new IRemoteService.Stub() {

        @Override
        public int send(String reqUrl) throws RemoteException {
            // TODO 前端发送请求
            return sendText(reqUrl);
        }

        @Override
        public void registerReceivedCallback(IReceivedCallback cb) throws RemoteException {
            synchronized (lock) {
                mCallbacks.register(cb);
                isBindViewer = true;
                if (reqdoOkCount > 0) {
                    notifyReceivedJson(Consts.REQDO_OK);
                }
                reqdoOkCount = 0;
            }
        }

        @Override
        public void unregisterReceivedCallback(IReceivedCallback cb) throws RemoteException {
            mCallbacks.unregister(cb);
        }
    };

    @Override
    public void onCreate() {
        Log.d("SubPushService", "SubPushService onCreate....pid=" + android.os.Process.myPid());

//        SharedPreferences sharedata = ShareData.getUserSharedPreferencesForSocial(this);
        SharedPreferences sharedata = ShareData.getUserSharedPreferences(this.getApplicationContext());
        userId = sharedata.getLong("userId", 0);
//        if (String.valueOf(uid).matches("[0-9]+$")) userId = Long.parseLong(uid);
//        else userId = 0;
        sessionid = sharedata.getString("sessionid", "");

        final DnsInfo dnsInfo = NormalShareData.getDnsInfo(this.getApplicationContext());
        Log.d("SubPushService", "SubPushService onCreate....sessionid=" + sessionid + " userId is " + userId+"   socketHost is "+ (dnsInfo != null?dnsInfo.pushSocket:TjrBaseApi.mApiSubPushUrl.uri()));
        startRunMsgTask();
        startForeground(0, new Notification());
        ThreadManager.submit(new Runnable() {
            @Override
            public void run() {
//                Log.d("SubPushService", "ThreadManager....run userId:" + userId + " sessionid=" + sessionid);
//				PowerManager.WakeLock wakeLock = WakeLockWrapper.getWakeLockInstance(SubPushService.this, SubPushService.class.getSimpleName());
//				wakeLock.acquire();
//				try {
//					SubPushConnect.getInstance().initConfigureBootstrap(SubPushService.this, SubPushService.this);
//				} finally {
//					wakeLock.release();
//				}
                SubPushConnect.getInstance().initConfigureBootstrap(SubPushService.this, dnsInfo != null?dnsInfo.pushSocket:TjrBaseApi.mApiSubPushUrl.uri(),SubPushService.this);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SubPushService", "SubPushService onBindView");
        return mBindViewer;
    }

    @Override
    public void unbindService(ServiceConnection conn) {
        Log.d("SubPushService", "SubPushService unbindService");
        isBindViewer = false;
        super.unbindService(conn);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SubPushService", "onStartCommand.... sessionid=" + sessionid + " myPid=" + android.os.Process.myPid());
        if (intent != null) {
            userId = intent.getLongExtra("userId", 0);
            sessionid = intent.getStringExtra("sessionid");
            version = intent.getStringExtra("version");
            Log.d("SubPushService", "onStartCommand....getAction=" + intent.getAction() + " flags=" + flags + " startId=" + startId + " userId=" + userId + " sessionid=" + sessionid);
        }
        return Service.START_STICKY;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        // TODO Auto-generated method stub
        Log.d("SubPushService", "onTaskRemoved....");
        reStartWakeupService();
//        CircleSharedPreferences.saveCircleSpChatRoomid(this, "0");
//		String circleNum2=CircleSharedPreferences.getCircleSpChatRoomid(this);
//		Log.d("onTaskRemoved", "circleNum1=="+circleNum1+"   circleNum2=="+circleNum2);
    }

    @Override
    public void onDestroy() {
        Log.d("SubPushService", "onDestroy..1..sessionid=" + sessionid);
        // 取消掉所有的回调
        mCallbacks.kill();
        sessionid = null;
        userId = 0;
        version = null;
        isBindViewer = false;
        SubPushConnect.getInstance().shutBootstrap();
        stopForeground(true);
        Log.d("SubPushService", "onDestroy..2..sessionid=" + sessionid);
    }

    /**
     * 回调数据
     *
     * @param json
     */
    private void notifyReceivedJson(String json) {
//		final int len = mCallbacks.beginBroadcast();
//		Log.d("SubPushService", "notifyReceivedJson....  len:" + len+ " myPid=" + android.os.Process.myPid());
//		for (int i = 0; i < len; i++) {
//			try {
//				mCallbacks.getBroadcastItem(i).onReceivedJson(json);
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
//		mCallbacks.finishBroadcast();
        msgQueue.add(json);
    }

    @Override
    public void channel(Channel channel) {
        // TODO Auto-generated method stub
        Log.d("SubPushService", "设置channel:" + channel);
        mChannel = channel;
    }

    public boolean isConnected() {
        return mChannel != null && mChannel.isBound() && mChannel.isOpen() && mChannel.isConnected();
    }

    private int sendText(String reqUrl) {
        Log.d("SubPushService", "    isSuccess==" + isSuccess + "   isConnected():" + isConnected()+ "     sendText:" + reqUrl );
        if (!isSuccess) return -1;
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


    @Override
    public void messageReceived(String json) {
        // TODO Auto-generated method stub
        Log.d("SubPushService", "messageReceived....json:" + json);
        if (Consts.PONG.equals(json)) return;
        if (Consts.REQDO_CONNECT.equals(json)) {
            isSuccess = true;
            Log.d("SubPushService", "messageReceived..REQDO_CONNECT..json:" + json + "  userId:" + userId + " sessionid=" + sessionid);
            if (userId > 0) {// sessionid 改 token
//                sendText("/connect?user_id=" + userId + "&sessionid=" + sessionid + "&packageName=com.tjr.perval&platform=android&version=" + version);
                ConfigTjrInfo.getInstance().setSessionid(sessionid);
                ConfigTjrInfo.getInstance().setUserId(String.valueOf(userId));
                Log.d("SubPushService", "messageReceived..REQDO_CONNECT..url is :" + SubPushHttp.getInstance().connectUrl(userId));
                sendText(SubPushHttp.getInstance().connectUrl(userId));
            }
        } else if (Consts.REQDO_OK.equals(json)) {
            Log.d("SubPushService", "messageReceived..REQDO_OK..json:" + json);
            synchronized (lock) {
                isSuccess = true;
                reqdoOkCount++;
                Log.d("SubPushService", "messageReceived..REQDO_OK..json:" + json + "  userId:" + userId + "  reqdoOkCount=" + reqdoOkCount + "  isBindViewer=" + isBindViewer + " sessionid=" + sessionid);
            }
        }
        if (isBindViewer) {
            notifyReceivedJson(json);
        } else {
            reStartWakeupService();
        }
    }

    private void reStartWakeupService() {
        Intent intent = new Intent(Consts.ACTION_SUBPUSH_RESTART);
        sendBroadcast(intent);
    }

    @SuppressWarnings("unchecked")
    private void startRunMsgTask() {
        ThreadManager.submit(new Runnable() {
            @Override
            public void run() {
                for (; ; ) {
                    try {
                        String json = msgQueue.take();
                        if (json != null) {
                            final int len = mCallbacks.beginBroadcast();
                            Log.d("SubPushService", "notifyReceivedJson....  len:" + len + " myPid=" + android.os.Process.myPid());
                            for (int i = 0; i < len; i++) {
                                try {
                                    mCallbacks.getBroadcastItem(i).onReceivedJson(json);
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                }
                            }
                            mCallbacks.finishBroadcast();
                        }
                    } catch (Exception e) {

                    }
                }
            }
        });
    }
    /**
     * 判断某个服务是否正在运行的方法
     *
     * @param mContext
     * @param serviceName
     *            是包名+服务的类名（例如：com.example.apklock.AppService）
     * @return true代表正在运行，false代表服务没有正在运行
     */
    // public static boolean isServiceWork(Context mContext, String serviceName)
    // {
    // boolean isWork = false;
    // ActivityManager myAM = (ActivityManager) mContext
    // .getSystemService(Context.ACTIVITY_SERVICE);
    // List<RunningServiceInfo> myList =
    // myAM.getRunningServices(Integer.MAX_VALUE);
    // if (myList.size() <= 0) {
    // return false;
    // }
    // for (int i = 0; i < myList.size(); i++) {
    // String mName = myList.get(i).service.getClassName().toString();
    // if (mName.equals(serviceName)) {
    // isWork = true;
    // break;
    // }
    // }
    // return isWork;
}
