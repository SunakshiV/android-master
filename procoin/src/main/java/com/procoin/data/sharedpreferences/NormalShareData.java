package com.procoin.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.procoin.http.TjrBaseApi;
import com.procoin.http.model.DnsInfo;

/**
 * 这个文件是用来保存普通数据(flag之类的)
 */
public class NormalShareData {


    private final static String NORMALFLAG = "normalFlag";
    private final static String EDITUSERFLAG = "editUserFlag";

    private final static String STOPWIN = "stopWin";

    private final static String HIDEFLAG = "hideFLAG";

    private final static String HIDEFLAG2 = "hideFLAG2";

    private final static String COINSUBROUND = "coinSubRound";

    private final static String KLINETYPE = "klineType";

    /**
     * @param
     * @return
     */
    public static SharedPreferences getSysSharedPreferences(Context context) {
        if (context == null) return null;
        return context.getSharedPreferences(NORMALFLAG, Context.MODE_PRIVATE);// 表示当前文件可以被其他应用读取， Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE
    }


    /**
     * @return
     */
    public static int getUserEditFlag(Context context, long userId) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getInt(EDITUSERFLAG + "_" + userId, 0);
        return 0;
    }

    /**
     * @param editFlag 点过传1
     */
    public static void saveUserEditFlag(Context context, long userId, int editFlag) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putInt(EDITUSERFLAG + "_" + userId, editFlag);
        // 一定要提交
        editor.commit();
    }


    /**
     * @return
     */
    public static int getStopWinFlag(Context context, long userId) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getInt(STOPWIN + "_" + userId, 0);
        return 0;
    }

    /**
     * @param editFlag 点过传1
     */
    public static void saveStopWinFlag(Context context, long userId, int editFlag) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putInt(STOPWIN + "_" + userId, editFlag);
        // 一定要提交
        editor.commit();
    }


    /**
     * 是否隐藏小额币种(持仓)
     *
     * @param context
     * @param userId
     * @param isHide
     */
    public static void saveIsHideSmall(Context context, long userId, boolean isHide) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putBoolean(HIDEFLAG + "_" + userId, isHide);
        // 一定要提交
        editor.commit();
    }

    /**
     * 获取是否隐藏小额币种(持仓)（默认隐藏）
     *
     * @param context
     * @param userId
     * @return
     */
    public static boolean getIsHideSmallFlag(Context context, long userId) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getBoolean(HIDEFLAG + "_" + userId, false);
        return false;
    }


    /**
     * 是否隐藏小额币种(跟单)
     *
     * @param context
     * @param userId
     * @param isHide
     */
    public static void saveIsHideSmall2(Context context, long userId, boolean isHide) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putBoolean(HIDEFLAG2 + "_" + userId, isHide);
        // 一定要提交
        editor.commit();
    }

    /**
     * 获取是否隐藏小额币种(跟单)（默认隐藏）
     *
     * @param context
     * @param userId
     * @return
     */
    public static boolean getIsHideSmallFlag2(Context context, long userId) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getBoolean(HIDEFLAG2 + "_" + userId, false);
        return false;
    }


    /**
     * @return DNS
     */
    public static DnsInfo getDnsInfo(Context context) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) {
            String quoteSocket = sharedata.getString("quoteSocket", TjrBaseApi.stockHomeUri.uri());
            String pushSocket = sharedata.getString("pushSocket", TjrBaseApi.mApiSubPushUrl.uri());
            String api = sharedata.getString("api", TjrBaseApi.mApiCropymeBaseUri.uri());
//            String predictSocket = sharedata.getString("predictSocket", TjrBaseApi.gamePredictSeverUri.uri());
            return new DnsInfo(quoteSocket, pushSocket, api);
        }
        return new DnsInfo(TjrBaseApi.stockHomeUri.uri(), TjrBaseApi.mApiSubPushUrl.uri(), TjrBaseApi.mApiCropymeBaseUri.uri());
    }

    /**
     * save dns
     */
    public static void saveDnsInfo(Context context, String quoteSocket, String pushSocket, String api) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString("quoteSocket", quoteSocket);
        editor.putString("pushSocket", pushSocket);
        editor.putString("api", api);
//        editor.putString("predictSocket", predictSocket);
        // 一定要提交
        editor.commit();
    }


    /**
     * 新币种第一次主动显示到认购
     *
     * @param context
     * @param coinSubRound
     */
    public static void saveCoinSubRound(Context context, String coinSubRound) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(COINSUBROUND, coinSubRound);
        // 一定要提交
        editor.commit();
    }

    public static String getCoinSubRound(Context context) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getString(COINSUBROUND, "");
        return "";
    }


    /**
     * 记录上一次打开的K先类型
     * @param context
     * @param klineReqEnumType
     */
    public static void saveKlineType(Context context, int  klineReqEnumType) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putInt(KLINETYPE, klineReqEnumType);
        // 一定要提交
        editor.commit();
    }

    public static int getKlineType(Context context) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getInt(KLINETYPE, -8741);//默认分时图
        return -8741;
    }


}
