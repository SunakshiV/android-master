package com.procoin.subpush.notify;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.procoin.subpush.Consts;

public class OnNotifyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		if (!intent.getAction().equals(Consts.ACTION_NOTIFY_MESSAGE)) {
			return;
		}
		String json = intent.getStringExtra(Consts.NOTIFY_MODEL_JSON);
		Log.d("SubPushService", "OnNotifyReceiver....json:" + json + "  Application:" + context.getApplicationContext());
		if(json != null){
		}
	}

}