package com.procoin.module.logout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.procoin.common.cache.CacheManager;
import com.procoin.common.constant.CommonConst;
import com.procoin.MainApplication;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.http.util.ShareData;

public class LogoutClearUser {
    public LogoutClearUser() {

    }

    public void logoutClearUser(Context context, Class<?> cls) {
        try {
            clearAppDataOnLogout(context);
            MainApplication mainApplication = (MainApplication) (context.getApplicationContext());
//            try {
//                StringBuffer buffer = (StringBuffer) mainApplication.getCacheManager().getCache(CommonConst.MY_SEARCH_STOCK);
//                if (buffer != null && buffer.length() > 0) {
//                    if (mainApplication.getUser() != null) {
//                        TjrHomeSubHttp.getInstance().addSearchStock(buffer.toString(), String.valueOf(mainApplication.getUser().getUserId()));
//                    }
//                    buffer.setLength(0);
//                }
//            } catch (Exception e) {
//            }
            mainApplication.setUser(null);// 注意不能设置为null
            ConfigTjrInfo.getInstance().clearUser();
            mainApplication.stopSubPushServiceAndclearNotification();
            CacheManager.getInstance().removeAll();
            if (cls != null) {
                Intent intent = new Intent(context, cls);
                finishAndSendBroadcase(context, intent);
            }
        } catch (Exception e) {
            Log.d("logoutClearUser","e="+e.getMessage());
            e.printStackTrace();
        }
    }

//    /**
//     * 注销账号,不用去调用网络
//     */
//    public void logoutClearUser(Context context, Intent intent, Class<?> cls) {
//        try {
//            clearAppDataOnLogout(context);
//            MainApplication mainApplication = (MainApplication) (context.getApplicationContext());
//            try {
//                StringBuffer buffer = (StringBuffer) mainApplication.getCacheManager().getCache(CommonConst.MY_SEARCH_STOCK);
//                if (buffer != null && buffer.length() > 0) {
//                    if (mainApplication.getUser() != null) {
//                        TjrHomeSubHttp.getInstance().addSearchStock(buffer.toString(), String.valueOf(mainApplication.getUser().getUserId()));
//                    }
//                    buffer.setLength(0);
//                }
//            } catch (Exception e) {
//            }
//            mainApplication.setUser(null);// 注意不能设置为null
//            mainApplication.stopSubPushServiceAndclearNotification();
//            CacheManager.getInstance().removeAll();
//            // 停止后台服务
//            intent.setClass(context, cls);
//            finishAndSendBroadcase(context, intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 注销时清除数据,都写在这里
     */
    private void clearAppDataOnLogout(Context context) {
        SharedPreferences sharedata = ShareData.getUserSharedPreferences(context);
        ShareData.delUser(sharedata);
        ShareData.delWeibo(sharedata);
        ShareData.delQQ(sharedata);
        ShareData.delWeiXin(sharedata);
//        SquareCacheData.clearSquareCache(context);// 注销用户时清除股友吧的缓存数据
//        HomeCache.clearHomeCache(context);// 注销用户时清除home路牌的缓存数据
//        TradeSharedPreferences.clearTradeParams(context);//注销时清除交易参数数据
//        TradeSharedPreferences.clearAssetBalance(context);//注销时清除交易资产
//        TradeSharedPreferences.clearTradeLoginState(context);//新时代的登录状态以及网页版登录的url
//        HomeCardSharedPreferences.setHomeDataIsReset(context, ((MainApplication) context.getApplicationContext()).getUser().getUserId(), 1);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void finishAndSendBroadcase(Context context, Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(CommonConst.ACTION_BROADCASERECIVER_FINISHONLOGOUT);
            context.sendBroadcast(broadcastIntent);// 该函数用于发送广播
        }
        context.startActivity(intent);
    }
}
