package com.procoin.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhengmj on 16-1-20.
 */
public class StockSharedPreferences {
    private final static String STOCK_PREFS = "stock_prefs";

    /**
     * 实心 1 空心为0
     *
     * @param isFill
     */
    public static void savePaintStyleFill(Context context, boolean isFill) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(STOCK_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            if (isFill) {
                editor.putInt("paint_style", 1);
            } else {
                editor.putInt("paint_style", 0);
            }
            editor.commit();
        }
    }

    /**
     * 是空心还是实心
     *
     * @return
     */
    public static boolean isPaintStyleFill(Context context) {
        int fill = 0;
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(STOCK_PREFS, Context.MODE_PRIVATE);
            fill = prefs.getInt("paint_style", 0);
        }
        return fill == 1;
    }


    /**
     * 默认是1秒
     */
    public static void saveSelectSpeed(Context context, int selectSpeed) {
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(STOCK_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("select_speed", selectSpeed);
            editor.commit();
        }
    }

    /**
     * 默认是5秒
     */
    public static int getSelectSpeed(Context context) {
        int fill = 1;
        if (context != null) {
            SharedPreferences prefs = context.getSharedPreferences(STOCK_PREFS, Context.MODE_PRIVATE);
            fill = prefs.getInt("select_speed", 1);//1秒刷新
        }
        return fill;
    }
}
