package com.procoin.module.logout;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.procoin.http.common.TJRFilterConf;

public class OnLogoutReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("onReceive","ConfigTjrInfo intent getAction == "+intent.getAction());
//        CommonUtil.LogLa(2, "OnLogoutReceiver is " + intent.getAction());
        if (intent.getAction().equals(TJRFilterConf.INTENT_ACTION_LOGGED_OUT)) {
            Log.d("onReceive","ConfigTjrInfo onReceive");
            intent.setClass(context, ReLoginRemindActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            return;
        }
    }

}