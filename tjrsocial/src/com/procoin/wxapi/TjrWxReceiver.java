//package com.procoin.wxapi;
//
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.os.Bundle;
//
//import com.tencent.mm.opensdk.modelmsg.ShowMessageFromWX;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//
///**
// * Created by zhengmj on 17-4-17.
// */
//
//public abstract class TjrWxReceiver extends BroadcastReceiver implements IWXAPIEventHandler {
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        if (WXEntryActivity.INTENT_ACTION_WX_RESP_OK.equals(intent.getAction())) {
//
//            Bundle wxBundle = intent.getExtras();
//            if (wxBundle != null) {
//                ShowMessageFromWX.Resp wxResp = new ShowMessageFromWX.Resp();
//                wxResp.fromBundle(wxBundle);
//                onResp(wxResp);
//            }
//        }
//    }
//
//    public void register(Activity activity) {
//
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(WXEntryActivity.INTENT_ACTION_WX_RESP_OK);
//        activity.registerReceiver(this, intentFilter);
//    }
//
//}
