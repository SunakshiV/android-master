package com.procoin;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.LocaleList;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.procoin.http.model.Components;
import com.procoin.http.model.Placard;
import com.procoin.http.resource.ImageRemoteResourceManager;
import com.procoin.updatedialog.DownAndNoticeDialogManager;
import com.procoin.updatedialog.DownLoadDialogManager;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;

public abstract class AbstractBaseApplication extends Application {
    private String packageName = "com.procoin";
    public String mVersion; // app当前版本号
    public String mVersionName; // app当前版本名
    public Components components;
    public Placard placard;
    //    private getAppInfoTask mgetAppInfoTask;
    public boolean isNewVersion; // false代表没有，true代表有更新
    private DownLoadDialogManager mUpdateManager;
    private Call<ResponseBody> callGetAppInfoTask;// 获取通知下载更新的类
    //    private GetClientInfoTask mGetClientInfoTask;// 获取通知下载更新的类
    private DownAndNoticeDialogManager downAndNoticeDiglogManage;
    private volatile ImageRemoteResourceManager imageRemoteResourceManager;

    /**
     * 自Android7.0系统起，由LocaleList管理语言
     系统可设置多个语言列表，根据优先级来选定语言。那么，正常情况下，若应用语言跟随系统，则直接LocaleList.getDefault().get(0)则可拿到系统当前语言。
     可是，若应用通过configuration.setLocale(locale)设置语言后(源码实际是new LocaleList(locale))，该locale会被塞进系统语言列表的首位，
     此时系统当前语言并不是首位的语言。因此，若应用再次选择跟随系统后，拿到语言列表首位的语言就不是系统当前的语言。
     解决办法：进入App后，可在Application里，通过LocaleList.getDefault()获取系统语言列表集合，保存在内存中；之后，若应用设置语言跟随系统后，则直接从保存的语言列表集合获取首位语言，进行设置
     当系统改变了语言后会回调onConfigurationChanged，在这个方法里重新赋值
     */
    public Locale currLocale;

    public Locale getCurrLocale() {
        return currLocale;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        // 初始化android uid
        imageRemoteResourceManager = new ImageRemoteResourceManager(getApplicationContext());
        isNewVersion = false;
        packageName = getPackageName();
//        Log.d("test", "packageName=" + packageName);
        mVersion = getVersionString(this);
        // 为cmwap用户设置代理上网
        new NetworkReceiver().register();
        String type = getNetworkType();
        if (null != type && type.equalsIgnoreCase("cmwap")) {
//			CommonUtil.showToast(this.getApplicationContext(), "您当前正在使用cmwap网络上网.", Gravity.BOTTOM);
            // HttpApi.getInstance().setHttpClientProxy("10.0.0.172", 80,
            // "http");
        } else {
            // HttpApi.getInstance().removeHttpClientProxy();
        }
        initTjrConfig();
        currLocale = getLocale();
//        onLanguageChange();
        checkLanguage(this, null);




    }

    abstract protected void initTjrConfig();

    public String getmVersion() {
        return mVersion;
    }

    public String getmVersionName() {
        return mVersionName;
    }

    public boolean isNewVersion() {
        return isNewVersion;
    }

    public ImageRemoteResourceManager imageRemoteResourceManager() {
        return imageRemoteResourceManager;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(checkLanguage(base, null));
    }

    /**
     * @param newConfig newConfig
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//当系统改变语言之后会调用这个方法，直接用newConfig里面的语言
            currLocale = newConfig.getLocales().get(0);
        } else {
            currLocale = newConfig.locale;
        }
        Log.d("getLocaleByLanguage", "onConfigurationChanged  currLocale==" + currLocale.getLanguage());
        checkLanguage(this, currLocale);
        ZXingLibrary.initDisplayOpinion(this);
    }

    private Locale getLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = LocaleList.getDefault().get(0);
        } else {
            locale = Locale.getDefault();
        }
        return locale;
    }

    public Context checkLanguage(Context context, Locale locale) {
        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        if (locale == null) {
            String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "auto");
            if (language.equals(Locale.ENGLISH.getLanguage())) {
                locale = Locale.ENGLISH;
            } else if (language.equals(Locale.SIMPLIFIED_CHINESE.getLanguage())) {
                locale = Locale.SIMPLIFIED_CHINESE;
            } else {
                locale = getLocale();
            }
        }
        if (locale == null) return context;
        // 设置语言，版本做兼容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale);
            context = context.createConfigurationContext(configuration);
        } else {
            configuration.locale = locale;
            resources.updateConfiguration(configuration, displayMetrics);
        }
        return context;
    }


    /**
     * 获取当前app的版本号
     *
     * @param context
     * @return
     */
    private String getVersionString(Context context) {
        // Get a version int for the app.
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            mVersionName = pi.versionName;
            return String.valueOf(pi.versionCode);
        } catch (NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 弹出下载
     *
     * @param activity
     */
    public void showDownLoadAppWithDialog(AppCompatActivity activity) {
        if (components != null) {
            if (mVersion != null && mVersion.matches("[0-9]+$")) {
                if ((components.version > Integer.parseInt(mVersion)) && activity != null) {
                    if (mUpdateManager == null)
                        mUpdateManager = new DownLoadDialogManager(activity, components);
                    else mUpdateManager.setUpdateManager(activity, components);
                    mUpdateManager.checkUpdateDialog();
                }
            }
        }
    }

    public String getNetworkType() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            return activeNetInfo.getExtraInfo(); // 接入点名称: 此名称可被用户任意更改 如: cmwap,
            // cmnet,internet ...
        } else {
            return null;
        }
    }

    public class NetworkReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context ctx, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    // HttpApi.getInstance().removeHttpClientProxy();
                    String netType = info.getExtraInfo();
                    String typeName = info.getTypeName();
                    boolean connected = info.isConnected();
                    if (typeName != null && typeName.equalsIgnoreCase("wifi")) {
                        if (connected) {
//							CommonUtil.showToast(ctx, "您当前正在使用 wifi 网络", Gravity.BOTTOM);
                        } else {
//							CommonUtil.showToast(ctx, "您当前网络不可用", Gravity.BOTTOM);
                        }
                    } else if (typeName != null && typeName.equalsIgnoreCase("mobile")) {
                        if (connected) {
                            if (netType != null && netType.equalsIgnoreCase("cmwap")) {
//								CommonUtil.showToast(ctx, "您当前正在使用 cmwap 网络上网.", Gravity.BOTTOM);
                                // HttpApi.getInstance().setHttpClientProxy("10.0.0.172",
                                // 80, "http");
                            } else {
//								CommonUtil.showToast(ctx, "您当前正在使用 手机 网络", Gravity.BOTTOM);
                            }
                        } else {
//							CommonUtil.showToast(ctx, "您当前网络不可用", Gravity.BOTTOM);
                        }
                    }
                }
            }
        }

        public void register() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(this, intentFilter);
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (downAndNoticeDiglogManage != null)
            downAndNoticeDiglogManage.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

//    public void startGetClientInfo(final Activity rActivity, String plareTime) {
//        CommonUtil.cancelCall(callGetAppInfoTask);
//        callGetAppInfoTask = RedzHttpServiceManager.getInstance().getRedzService().homeGet(plareTime);
//        callGetAppInfoTask.enqueue(new MyCallBack(rActivity) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    Integer msgCount = resultData.getItem("msgCount", Integer.class);
//                    if (msgCount == null) msgCount = 0;//这里一定要判断否则会报错
//                    Components componentsinfo = resultData.getObject("version", Components.class);
//                    Placard placard = resultData.getObject("notice", Placard.class);
//
//                    if (componentsinfo != null) {
//                        components = componentsinfo;
//                        if (mVersion != null && mVersion.matches("[0-9]+$")) {
//                            if ((componentsinfo.version > Integer.parseInt(mVersion)) && rActivity != null) {
//                                if (downAndNoticeDiglogManage == null) {
//                                    downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
//                                } else {
//                                    downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
//                                }
//                                downAndNoticeDiglogManage.checkUpdateDialog();
//                                isNewVersion = true;
//                            } else {
//                                if (downAndNoticeDiglogManage == null) {
//                                    downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
//                                } else {
//                                    downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
//                                }
//                                downAndNoticeDiglogManage.checkNoticeDialog();// 检查
//                                isNewVersion = false;
//                            }
//                        }
//                    } else if (placard != null) {// 单独只有公告
//                        if (downAndNoticeDiglogManage == null) {
//                            downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
//                        } else {
//                            downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
//                        }
//                        downAndNoticeDiglogManage.checkNoticeDialog();
//                        isNewVersion = false;
//                    }
//
//                }
//            }
//        });
////        CommonUtil.cancelAsyncTask(mGetClientInfoTask);
////        mGetClientInfoTask = (GetClientInfoTask) new GetClientInfoTask(plareTime,user_id) {
////
////            @Override
////            public void getReslute(Components componentsinfo, Placard placard, Activity rActivity,int msg_count) {
////                if (componentsinfo != null) {
////                    components = componentsinfo;
////                    if (mVersion != null && mVersion.matches("[0-9]+$")) {
////                        if ((componentsinfo.version > Integer.parseInt(mVersion)) && rActivity != null) {
////                            if (downAndNoticeDiglogManage == null) {
////                                downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
////                            } else {
////                                downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
////                            }
////                            downAndNoticeDiglogManage.checkUpdateDialog();
////                            isNewVersion = true;
////                        } else {
////                            if (downAndNoticeDiglogManage == null) {
////                                downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
////                            } else {
////                                downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
////                            }
////                            downAndNoticeDiglogManage.checkNoticeDialog();// 检查
////                            isNewVersion = false;
////                        }
////                    }
////                } else if (placard != null) {// 单独只有公告
////                    if (downAndNoticeDiglogManage == null) {
////                        downAndNoticeDiglogManage = new DownAndNoticeDialogManager(rActivity, componentsinfo, placard);
////                    } else {
////                        downAndNoticeDiglogManage.setUpdateManager(rActivity, componentsinfo, placard);
////                    }
////                    downAndNoticeDiglogManage.checkNoticeDialog();
////                    isNewVersion = false;
////                }
////            }
////        }.executeParams(activity);
//    }


}
