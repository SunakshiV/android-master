//package com.tjr.bee.updatedialog;
//
//import android.app.Activity;
//import android.app.Application;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//
//import Components;
//import Placard;
//import ImageRemoteResourceManager;
//import CommonUtil;
////import com.cropyme.http.widget.dialog.DownAndNoticeDialogManager;
////import com.cropyme.http.widget.dialog.DownLoadDialogManager;
////import com.cropyme.http.widget.dialog.task.GetClientInfoTask;
//
//public abstract class AbstractBaseApplication extends Application {
//    private String packageName = "com.cropyme";
//    private String mVersion; // app当前版本号
//    private String mVersionName; // app当前版本名
//    private Components components;
//    //    private getAppInfoTask mgetAppInfoTask;
//    private boolean isNewVersion; // false代表没有，true代表有更新
//    private DownLoadDialogManager mUpdateManager;
//    private GetClientInfoTask mGetClientInfoTask;// 获取通知下载更新的类
//    private DownAndNoticeDialogManager downAndNoticeDiglogManage;
//    private volatile ImageRemoteResourceManager imageRemoteResourceManager;
//    private int msg_count;
//    protected int getMsg_count(){
//        return msg_count;
//    }
//    @Override
//    public void onCreate() {
//        // TODO Auto-generated method stub
//        super.onCreate();
//        // 初始化android uid
//        imageRemoteResourceManager = new ImageRemoteResourceManager(getApplicationContext());
//        isNewVersion = false;
//        packageName = getPackageName();
////        Log.d("test", "packageName=" + packageName);
//        mVersion = getVersionString(this);
//        // 为cmwap用户设置代理上网
//        new NetworkReceiver().register();
//        String type = getNetworkType();
//        if (null != type && type.equalsIgnoreCase("cmwap")) {
////			CommonUtil.showToast(this.getApplicationContext(), "您当前正在使用cmwap网络上网.", Gravity.BOTTOM);
//            // HttpApi.getInstance().setHttpClientProxy("10.0.0.172", 80,
//            // "http");
//        } else {
//            // HttpApi.getInstance().removeHttpClientProxy();
//        }
//        initTjrConfig();
//    }
//
//    abstract protected void initTjrConfig();
//
//    public String getmVersion() {
//        return mVersion;
//    }
//
//    public String getmVersionName() {
//        return mVersionName;
//    }
//
//    public boolean isNewVersion() {
//        return isNewVersion;
//    }
//
//    public ImageRemoteResourceManager imageRemoteResourceManager() {
//        return imageRemoteResourceManager;
//    }
//
//    /**
//     * 获取当前app的版本号
//     *
//     * @param context
//     * @return
//     */
//    private String getVersionString(Context context) {
//        // Get a version int for the app.
//        try {
//            PackageManager pm = context.getPackageManager();
//            PackageInfo pi = pm.getPackageInfo(packageName, 0);
//            mVersionName = pi.versionName;
//            return String.valueOf(pi.versionCode);
//        } catch (NameNotFoundException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    /**
//     * 弹出下载
//     *
//     * @param activity
//     */
//    public void showDownLoadAppWithDialog(Activity activity) {
//        if (components != null) {
//            if (mVersion != null && mVersion.matches("[0-9]+$")) {
//                if ((components.version > Integer.parseInt(mVersion)) && activity != null) {
//                    if (mUpdateManager == null)
//                        mUpdateManager = new DownLoadDialogManager(activity, components);
//                    else mUpdateManager.setUpdateManager(activity, components);
//                    mUpdateManager.checkUpdateDialog();
//                }
//            }
//        }
//    }
//
//    public String getNetworkType() {
//        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
//        if (activeNetInfo != null) {
//            return activeNetInfo.getExtraInfo(); // 接入点名称: 此名称可被用户任意更改 如: cmwap,
//            // cmnet,internet ...
//        } else {
//            return null;
//        }
//    }
//
//    public class NetworkReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context ctx, Intent intent) {
//            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
//                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
//                if (info != null) {
//                    // HttpApi.getInstance().removeHttpClientProxy();
//                    String netType = info.getExtraInfo();
//                    String typeName = info.getTypeName();
//                    boolean connected = info.isConnected();
//                    if (typeName != null && typeName.equalsIgnoreCase("wifi")) {
//                        if (connected) {
////							CommonUtil.showToast(ctx, "您当前正在使用 wifi 网络", Gravity.BOTTOM);
//                        } else {
////							CommonUtil.showToast(ctx, "您当前网络不可用", Gravity.BOTTOM);
//                        }
//                    } else if (typeName != null && typeName.equalsIgnoreCase("mobile")) {
//                        if (connected) {
//                            if (netType != null && netType.equalsIgnoreCase("cmwap")) {
////								CommonUtil.showToast(ctx, "您当前正在使用 cmwap 网络上网.", Gravity.BOTTOM);
//                                // HttpApi.getInstance().setHttpClientProxy("10.0.0.172",
//                                // 80, "http");
//                            } else {
////								CommonUtil.showToast(ctx, "您当前正在使用 手机 网络", Gravity.BOTTOM);
//                            }
//                        } else {
////							CommonUtil.showToast(ctx, "您当前网络不可用", Gravity.BOTTOM);
//                        }
//                    }
//                }
//            }
//        }
//
//        public void register() {
//            IntentFilter intentFilter = new IntentFilter();
//            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
//            registerReceiver(this, intentFilter);
//        }
//    }
//
//    public void startGetClientInfo(Activity activity, String plareTime,String user_id) {
//        CommonUtil.cancelAsyncTask(mGetClientInfoTask);
//        mGetClientInfoTask = (GetClientInfoTask) new GetClientInfoTask(plareTime,user_id) {
//
//            @Override
//            public void getReslute(Components componentsinfo, Placard placard, Activity rActivity,int msg_count) {
//               AbstractBaseApplication.this.msg_count = msg_count;
//                if (componentsinfo != null) {
//                    components = componentsinfo;
//                    if (mVersion != null && mVersion.matches("[0-9]+$")) {
//                        if ((componentsinfo.version > Integer.parseInt(mVersion)) && rActivity != null) {
//                            if (downAndNoticeDiglogManage == null) {
//                                downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
//                            } else {
//                                downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
//                            }
//                            downAndNoticeDiglogManage.checkUpdateDialog();
//                            isNewVersion = true;
//                        } else {
//                            if (downAndNoticeDiglogManage == null) {
//                                downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
//                            } else {
//                                downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
//                            }
//                            downAndNoticeDiglogManage.checkNoticeDialog();// 检查
//                            isNewVersion = false;
//                        }
//                    }
//                } else if (placard != null) {// 单独只有公告
//                    if (downAndNoticeDiglogManage == null) {
//                        downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
//                    } else {
//                        downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
//                    }
//                    downAndNoticeDiglogManage.checkNoticeDialog();
//                    isNewVersion = false;
//                }
//            }
//        }.executeParams(activity);
//    }
//}
