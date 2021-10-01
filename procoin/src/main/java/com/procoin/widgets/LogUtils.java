package com.procoin.widgets;

import android.util.Log;

import com.procoin.http.TjrBaseApi;

/**
 * Created by zhengmj on 18-10-19.
 */

public class LogUtils {

    public static void d(String tag, String msg) {
        if (TjrBaseApi.isLog) Log.d(tag, msg);
    }

    public static void i(String tag, String msg) {
        if (TjrBaseApi.isLog) Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (TjrBaseApi.isLog) Log.e(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (TjrBaseApi.isLog) Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (TjrBaseApi.isLog) Log.w(tag, msg);
    }
}
