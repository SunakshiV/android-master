package com.procoin.subpush;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class SubPushActivity extends AppCompatActivity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("SubPushActivity", "....onCreate..........");
		Intent service = new Intent(this, SubPushService.class);
		startService(service);
//		if (android.os.Build.VERSION.SDK_INT >= 21) {
//			finishAndRemoveTask();
//		} else {
//			finish();
//		}
	}

	public static void exitApplication(Context context) {
		Intent intent = new Intent(context, SubPushActivity.class);
	    intent.setAction(Intent.ACTION_VIEW);
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
	    context.startActivity(intent);
	}
}
