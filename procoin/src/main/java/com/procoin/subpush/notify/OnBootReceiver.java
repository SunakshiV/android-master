package com.procoin.subpush.notify;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;


public class OnBootReceiver extends BroadcastReceiver {
    private static final int PERIOD = 300000; // 5 minutes

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SubPushService", "OnBootReceiver....");
        AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(context, OnAlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, i, 0);
        //开始时间
        long firstime = SystemClock.elapsedRealtime() + 60000;
        mgr.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime, PERIOD, pi);
    }
}