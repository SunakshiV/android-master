/**
 * Copyright 2009 Joe LaPenna
 */

package com.procoin.http.util;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class NotificationsUtil {
	// private static final String TAG = "NotificationsUtil";

	public static void ToastReasonForFailure(Context context, Exception exception) {
		try {
			CommonUtil.LogLa(1, "=====context="+context+"  exception="+exception);
			if (exception == null) {
				Toast.makeText(context, "网络请求数据超时,再试一次!", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof SocketTimeoutException) {
				Toast.makeText(context, "网络请求数据超时,再试一次!", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof SocketException) {
				Toast.makeText(context, "网络请求数据超时!", Toast.LENGTH_SHORT).show();
			} else if (exception instanceof IOException) {
				Toast.makeText(context, "网络请求数据超时!", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(context, "网络请求数据超时,再试一次!", Toast.LENGTH_SHORT).show();
			}
			if (exception != null) {
				Log.e("Exception", exception.toString());
			}
		} catch (Exception e) {
			CommonUtil.LogLa(1, "=====context="+context+"  exception="+exception);
		}
	}
}
