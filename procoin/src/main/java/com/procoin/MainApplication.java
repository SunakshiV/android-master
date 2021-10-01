package com.procoin;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import androidx.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.procoin.common.cache.CacheManager;
import com.procoin.data.db.TaoJinLuDatabase;
import com.procoin.module.home.entity.Order;
import com.procoin.module.home.entity.SubUser;
import com.procoin.module.home.trade.entity.AccountType;
import com.procoin.module.myhome.entity.ImageSelectGroup;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.module.welcome.LoadResActivity;
import com.procoin.subpush.ReceivedManager;
import com.procoin.http.base.Group;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.http.model.User;
import com.procoin.http.resource.BaseRemoteResourceManager;
import com.procoin.http.resource.NullDiskCache;
import com.procoin.http.util.MD5;
import com.procoin.http.util.ShareData;
import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.stat.common.Util;

import java.util.Map;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

public class MainApplication extends AbstractBaseApplication {
    public static boolean isConnected = false;//在第一次进入APP时，如果网络有问题，则会显示noWeb页面，如果中途网络有问题，除了独立的Activity，Fragment不会显示noWeb页面
    private static final String KEY_DEX2_SHA1 = "KEY_DEX2_SHA1";
    private volatile User user;
    public volatile static boolean isRun = true; // 代表要不要关闭定时器
    public volatile static int couts = 0; // 线程标志计数器，
    private static Object lock = new Object();
    private volatile boolean isSDCard; // 是否有sd card
    // 组件信息,3.bingphone 进入到账户页面需要刷新,4 ,更新已经订阅,5.创建报纸
    private int[] guide;// 新手指南数据`
    private volatile static CacheManager cacheManager;
    private BaseRemoteResourceManager mRemoteResourceChatManager; // 专门对私聊处理
    private BaseRemoteResourceManager mDCIMRemoteResourceManager;
    private BaseRemoteResourceManager mVedioResourceManager;


    private final static String DCIMIMAGE = "dcim_image";// 相册文件夹

    public volatile boolean isShowKline;//是否显示k线分时 0 不显 1 显
    public String deviceId;

    private TaoJinLuDatabase mDb;

    public volatile long msgCount;// 我的消息数量

    public volatile boolean optionalFlag;// 自选列表发生变化需要刷新

//    public volatile int isShowFastBuy;// 1代表显示快捷购买 0代表隐藏
//    public volatile int isShowDefault;// 1代表学分可以点击 0代表学分不能点击并且箭头隐藏


        public volatile String coinSubState;// 认购状态，不为null代表需要显示



    public volatile Group<ImageSelectGroup> allGroups;//当前所有的,用完记得清除
    public volatile Group<ImageSelectGroup> selectedGroup;//已选中的group,用完记得清除

    public volatile Order order;//订单详情
    public volatile OrderCash orderCash;//提现

    public volatile SubUser subUser;//雷达图详情数据

    public volatile Group<AccountType> accountTypeGroup;

    @Override
    public void onCreate() {
        if (quickStart()) {
            return;//没加载完就不让进去
        }
        super.onCreate();

        //上线关掉联调模式，在初始化时调用.不可链接可视化埋点
//        StatisticsDataAPI.instance(this, DebugMode.DEBUG_OFF);
        //通过联调模式来埋点，在初始化时调用，可链接可视化埋点
//        StatisticsDataAPI.instance(this, DebugMode.DEBUG_AND_TRACK);

        //MTA可视化新版初始化
        StatisticsDataAPI.instance(this);


//        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJavaCrashHandlerStatus(true);
//        StatCrashReporter.getStatCrashReporter(getApplicationContext()).setJniNativeCrashStatus(true);
//
//        StatCrashReporter.getStatCrashReporter(getApplicationContext()).addCrashCallback(
//                new StatCrashCallback() {
//                    @Override
//                    public void onJniNativeCrash(String nativeCrashStacks) { // native crash happened
//                        // do something
//                    }
//                    @Override
//                    public void onJavaCrash(Thread thread, Throwable ex) {// java crash happened
//                        // do something
//                    }
//                });

    }

    @Override
    protected void initTjrConfig() {
        // TODO Auto-generated method stub
//        mContext = this.getApplicationContext();
//        if (BuildConfig.DEBUG) {
//            LayoutCast.init(this);
//        }
//        LeakCanary.install(this);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
//            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                    .detectDiskReads()
//                    .detectDiskWrites()
//                    .detectNetwork()   // or .detectAll() for all detectable problems
//                    .penaltyLog()
//                    .build());
//
//            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                    .detectLeakedSqlLiteObjects()
//                    .detectLeakedClosableObjects()
//                    .detectActivityLeaks()
//                    .penaltyLog()
//                    .penaltyDeath()
//                    .build());
//        }

        SharedPreferences sp = ShareData.getUserSharedPreferences(getApplicationContext());
        user = ShareData.getUser(sp);
        Log.d("487", "MainApplication user == null " + (user == null));
        Log.d("user", "user==" + (user != null ? String.valueOf(user.getUserId()) : "null)"));
        deviceId = LoadTjrUUId();

        ConfigTjrInfo.getInstance().initAndroidInfo(getApplicationContext(), getmVersion(), getPackageName(), ShareData.getSessionid(sp), user == null ? "" : String.valueOf(user.getUserId()));
        mDb = TaoJinLuDatabase.getInstance(this);
//        guide = mDb.getGuideValue();
//        TjrSocialMTAUtil.initMTAConfig(true, this);
        initCacheManager(); // 初始化一个单例内存管理器
        loadResourceManagers();
        new MediaCardStateBroadcastReceiver().register();

//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//                .detectCustomSlowCalls() //API等级11，使用StrictMode.noteSlowCode
//                .detectDiskReads()
//                .detectDiskWrites()
//                .detectNetwork()   // or .detectAll() for all detectable problems
//                .detectAll()
//                .penaltyDialog() //弹出违规提示对话框
//                .penaltyLog() //在Logcat 中打印违规异常信息
//                .penaltyFlashScreen() //API等级11
//                .build());
//        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//                .detectLeakedSqlLiteObjects()
//                .detectLeakedClosableObjects() //API等级11
//                .penaltyLog()
//                .penaltyDeath()
//                .build());

//        BlockCanary.install(this, new AppBlockCanaryContext()).start();


        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("QbSdk", " onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(),  cb);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        CommonUtil.LogLa(2, "App attachBaseContext ");
        if (!quickStart() && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {//>=5.0的系统默认对dex进行oat优化
            if (needWait(base)) {
                waitForDexopt(base);
            }
//            CommonUtil.LogLa(2, " MultiDex.install");
            MultiDex.install(this);
        } else {
            return;
        }
    }

    public boolean quickStart() {
        if (getCurProcessName(this) != null && getCurProcessName(this).contains(":minipreval")) {
//            CommonUtil.LogLa(2, ":minitjr start!");
            return true;
        }
        return false;
    }

    //neead wait for dexopt ?
    private boolean needWait(Context context) {
        String flag = get2thDexSHA1(context);
//        CommonUtil.LogLa(2, "dex2-sha1 " + flag);
//         TODO: 15-12-3  这里需要修改下文件名字 getmVersion()
        SharedPreferences sp = context.getSharedPreferences(
                getmVersion(), MODE_MULTI_PROCESS);
        String saveValue = sp.getString(KEY_DEX2_SHA1, "novalue");
        return !saveValue.equals(flag);
    }

    /**
     * Get classes.dex file signature
     *
     * @param context
     * @return
     */
    private String get2thDexSHA1(Context context) {
        ApplicationInfo ai = context.getApplicationInfo();
        String source = ai.sourceDir;
        try {
            JarFile jar = new JarFile(source);
            Manifest mf = jar.getManifest();
            Map<String, java.util.jar.Attributes> map = mf.getEntries();
//            CommonUtil.LogLa(2, "map is " + map.toString());
            java.util.jar.Attributes a = map.get("classes2.dex");
            if (a != null) {
                return a.getValue("SHA1-Digest");
            } else {
                return "novalue";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // optDex finish
    public void installFinish(Context context) {
        // TODO: 15-12-3 这里需要修改下文件名字
//        long startWait = System.currentTimeMillis();
//        CommonUtil.LogLa(2, "installFinish startWait ms :" + startWait);
        SharedPreferences sp = context.getSharedPreferences(
                getmVersion(), MODE_MULTI_PROCESS);
        sp.edit().putString(KEY_DEX2_SHA1, get2thDexSHA1(context)).commit();
    }

    public static String getCurProcessName(Context context) {
        try {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context
                    .getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                    .getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
        } catch (Exception e) {
            // ignore
        }
        return null;
    }

    public void waitForDexopt(Context base) {
        Intent intent = new Intent();
        ComponentName componentName = new
                ComponentName(getPackageName(), LoadResActivity.class.getName());
        intent.setComponent(componentName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        base.startActivity(intent);
        long startWait = System.currentTimeMillis();
//        CommonUtil.LogLa(2, "waitForDexopt startWait ms :" + startWait);
        long waitTime = 10 * 1000;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB_MR1) {
            waitTime = 20 * 1000;//实测发现某些场景下有些2.3版本有可能10s都不能完成optdex
        }
        while (needWait(base)) {
            try {
                long nowWait = System.currentTimeMillis() - startWait;
//                CommonUtil.LogLa(2, "wait ms :" + nowWait);
                if (nowWait >= waitTime) {
                    return;
                }
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public BaseRemoteResourceManager getmVedioResourceManager() {
        return mVedioResourceManager;
    }

    public BaseRemoteResourceManager getmDCIMRemoteResourceManager() {
        return mDCIMRemoteResourceManager;
    }


    public BaseRemoteResourceManager getRemoteResourceChatManager() {
        return mRemoteResourceChatManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }


    /**
     * 单例化一个CacheManager
     *
     * @return CacheManager对象
     */
    private void initCacheManager() {
        if (cacheManager == null) {
            synchronized (lock) {
                if (cacheManager == null) {
                    cacheManager = new CacheManager();
                }
            }
        }
    }


    public User getUser() {
        return user;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setUser(User user) {
        this.user = user;
        ConfigTjrInfo.getInstance().setUserId(user == null ? "" : String.valueOf(user.getUserId()));
    }

    public boolean isRun() {
        synchronized (lock) {
            return isRun;
        }
    }

    public static void setRun(boolean isRun) {
        synchronized (lock) {
            MainApplication.isRun = isRun;
            if (!isRun) {
                couts = 1;
            } else {
                couts = 0;
            }
        }
    }


    /**
     * 这个是启动socket的服务
     */
    public void startSubPushService() {
        Log.d("154", "startSubPushService");
        if (getUser() != null && getUser().getUserId() != 0) {
            Log.d("154", "startSubPushService success");
            try {
                ReceivedManager.getInstance(getApplicationContext()).initRunService(getApplicationContext(), getUser().getUserId(), ConfigTjrInfo.getInstance().getSessionid(), ConfigTjrInfo.getInstance().getVersion());
            } catch (Exception e) {
                Log.d("154", "has Error e == " + e);
            }
        }
        if (getUser() == null) {
            Log.d("154", "getUser() == null");
        } else {
//            if (getUser().getUserId() == null){
//                Log.d("154","getUser().getUserId() == null");
//            }else {
            if (getUser().getUserId() == 0) {
                Log.d("154", "getUser().getUserId() == null");
            }
//            }
        }

    }


    /**
     * 停止服务
     */
    public void stopSubPushServiceAndclearNotification() {
        // 清除所有通知
        ReceivedManager.getInstance(getApplicationContext()).stopRunService(getApplicationContext());
        ConfigTjrInfo.getInstance().clear();
        clearNotification(0);
    }

    /**
     * 清除所有淘金路push
     *
     * @param id
     */
    public void clearNotification(int id) {
        NotificationManager notiManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (notiManager != null) {
            if (id <= 0) notiManager.cancelAll();
            else notiManager.cancel(id);
        }
    }


    /**
     * Set up resource managers on the application depending on SD card state.
     */
    private class MediaCardStateBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_MEDIA_UNMOUNTED.equals(intent.getAction())) {
                Toast.makeText(context, "你已经卸载SD卡!图片,话音、图像将无法使用.", Toast.LENGTH_SHORT).show();
//                getRemoteResourceManager().shutdown();
                getRemoteResourceChatManager().shutdown();
                getmDCIMRemoteResourceManager().shutdown();
                getmVedioResourceManager().shutdown();
                loadResourceManagers();
            } else if (Intent.ACTION_MEDIA_MOUNTED.equals(intent.getAction())) {
                loadResourceManagers();
            }
        }

        public void register() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
            intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);
            intentFilter.addDataScheme("file");
            registerReceiver(this, intentFilter);
        }
    }
    //这里注意因为application初始化的时候就调用这句了，用来创建目录，但这里还没有申请权限可能会创建失败，所以，首页申请的权限之后，在调用一次
    public void loadResourceManagers() {
        try {
//            mRemoteResourceManager = new RemoteResourceManager("chat");
            mRemoteResourceChatManager = new BaseRemoteResourceManager("chat");
            mDCIMRemoteResourceManager = new BaseRemoteResourceManager(DCIMIMAGE);
            mVedioResourceManager = new BaseRemoteResourceManager("video");
            mDCIMRemoteResourceManager.removeNoMedia();
            mVedioResourceManager.removeNoMedia();
            isSDCard = true;
        } catch (IllegalStateException e) {
            mRemoteResourceChatManager = new BaseRemoteResourceManager(new NullDiskCache());
            isSDCard = false;
        }

    }

    public boolean isSDCard() {
        return isSDCard;
    }

    public String LoadTjrUUId() {//        deviceId = ((TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();有些手机返回null
        try {
            final String tmDevice, tmSerial, androidId, wifiMac;
            tmDevice = Util.getDeviceID(this);
            tmSerial = Util.getSimOperator(this);
            androidId = android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            wifiMac = Util.getWifiMacAddress(this);
            if (!TextUtils.isEmpty(tmDevice) || !TextUtils.isEmpty(tmSerial) || !TextUtils.isEmpty(androidId) || !TextUtils.isEmpty(wifiMac)) {//只要有一个不为空，就ok
                return MD5.getMessageDigest(tmDevice + tmSerial + androidId + wifiMac);
            }
            //        CommonUtil.LogLa(2, "uuid is " + uuid);
        } catch (Exception e) {

        }
        return "-1";//反正不能返回空，所以先返回-1，以便到时候可以查到有多少手机不能获取到deviceId
    }

    public TaoJinLuDatabase getmDb() {
        return mDb;
    }



    /**
     * @param name
     * @param headUrl
     * @return 是否有修改过user信息
     */
    public boolean updateUserNameAndHeadUrl(String name, String headUrl) {
        if (TextUtils.isEmpty(name) && TextUtils.isEmpty(headUrl)) return false;
        if (user != null) {
            if (name.equals(user.getUserName()) && headUrl.equals(user.getHeadUrl())) return false;
            user.setUserName(name);
            user.setHeadUrl(headUrl);
            ShareData.saveUser(ShareData.getUserSharedPreferences(this), null, null, null, null, user);
            return true;
        }
        return false;
    }
}
