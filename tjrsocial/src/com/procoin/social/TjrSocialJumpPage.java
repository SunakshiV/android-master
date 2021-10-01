package com.procoin.social;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.social.common.TjrSocialShareConfig;

public class TjrSocialJumpPage {

    /**
     * 跳到行情页面
     *
     * @param activity
     * @param jc
     * @param fdm
     */
    public static void jumpToStock(AppCompatActivity activity, String jc, String fdm) {
        Intent mIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString(TjrSocialShareConfig.STOCKNAME, jc);
        bundle.putString(TjrSocialShareConfig.FULLCODE, fdm);
        ComponentName comp = new ComponentName("com.coingo", "com.coingo.quotation.stock.StockActivity");//
        mIntent.putExtras(bundle);
        mIntent.setComponent(comp);
        mIntent.setAction("android.intent.action.VIEW");
        activity.startActivity(mIntent);
        activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);// 进入动画
    }

    /**
     * 跳到行情页面
     *
     * @param activity
     * @param jc
     * @param fdm
     */
    public static void jumpToIndex(AppCompatActivity activity, String jc, String fdm) {
        try {
            Intent mIntent = new Intent();
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(jc))
                bundle.putString(TjrSocialShareConfig.STOCKNAME, jc);
            if (!TextUtils.isEmpty(fdm))
                bundle.putString(TjrSocialShareConfig.FULLCODE, fdm);
            ComponentName comp = new ComponentName("com.coingo", "com.coingo.quotation.index.IndexActivity");//
            // mIntent.putExtras(bundle);
            mIntent.setComponent(comp);
            mIntent.setAction("android.intent.action.VIEW");
            activity.startActivity(mIntent);
            activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);// 进入动画
        } catch (Exception e) {
        }
    }

    /**
     * 行情页面分享到股友吧的固定的股票代码
     *
     * @param activity
     * @param jc
     * @param fdm
     */
    public static String gyqStockStr(String jc, String fdm) {
        if (fdm == null || jc == null) {
            return "$" + jc + "(" + fdm + ")$";
        } else {
            return "$" + jc + "(" + fdm.toUpperCase() + ")$";
        }

    }

}
