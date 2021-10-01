//package com.procoin.wxapi;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//
//import com.procoin.social.baseui.AbstractBaseActivity;
//import com.procoin.social.util.CommonUtil;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//
///**
// * wx一定要起这个页面，不然你不能收到成功或失败的返回，
// *
// * @author zhengmj
// */
//public class WXEntryActivity extends AbstractBaseActivity implements IWXAPIEventHandler {
//    public static final String INTENT_ACTION_WX_AUTH_LOGGED_IN = "com.coingo.intent.action.WX_AUTH_LOGGED_IN";
//    public static final String INTENT_ACTION_WX_RESP_OK = "com.coingo.intent.action.WX_RESP_OK";
//    public static final String WX_CODE = "wx_code";
//    private WeChatShare wechat;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        wechat = new WeChatShare(this);
//        getData(getIntent());
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//
//        super.onNewIntent(intent);
//        setIntent(intent);
//        getData(intent);
//    }
//
//    public void getData(Intent intent) {
//        wechat.getWXAPI().handleIntent(intent, this);
////        System.out.println("getData intent.getExtras() " + intent.getExtras());
////        System.out.println("getData intent " + intent);
//    }
//
//    /**
//     * 向微信向我们的回调
//     */
//    @Override
//    public void onReq(BaseReq arg0) {
//        Log.d("onResp","onReq");
//        // TODO Auto-generated method stub
////        Log.d("155","onReq openId == "+arg0.openId);
////        Log.d("155","onReq transaction == "+arg0.transaction);
////        System.out.println("getData sonReq " + arg0);
////        tjrReq = (ShowMessageFromWX.Req) arg0;
//
//    }
//
//    @Override
//    public void onResp(BaseResp arg0) {
//        Log.d("onResp","onResp");
////        System.out.println("getData onResp " + arg0);
////        CommonUtil.LogLa(2, "------------WXEntryActivity----------------onResp----------");
//        String result = null;
//
//        Log.d("onResp","onResp errCode == "+arg0.errCode+"  arg0.errStr=="+arg0.errStr);
//        Log.d("onResp","errCode == "+arg0.errCode);
//        switch (arg0.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
////                Bundle wxBundle = new Bundle();
////                if (arg0 instanceof SendMessageToWX.Resp) {
////                    Log.d("onResp","arg0 instanceof SendMessageToWX.Resp");
////                    SendMessageToWX.Resp wxResp = (SendMessageToWX.Resp) arg0;
////                    if (wxResp != null) {
////                        wxResp.toBundle(wxBundle);
////                        Intent intent = new Intent();
////                        intent.putExtras(wxBundle);
////                        intent.setAction(INTENT_ACTION_WX_RESP_OK);
////                        sendBroadcast(intent);// 广播一个微信授权验证信息
////                    }
////                }
//                result = "分享成功";
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                result = "发送取消";
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                result = "发送被拒绝";
//                break;
//            case BaseResp.ErrCode.ERR_UNSUPPORT:
//                result = "不支持错误";
//                break;
//            default:
//                result = "未知错误";
//                break;
//
//
//        }
//        if (arg0 instanceof SendAuth.Resp) {
////            Log.d("155","Excute 广播一个微信授权验证信息");
//            SendAuth.Resp resp = (SendAuth.Resp) arg0;
//            Intent intent = new Intent();
//            // TODO 原来 resp.token
////            Log.d("155","resp.code == "+resp.code);
//            intent.putExtra(WX_CODE, resp.code);
//            intent.setAction(INTENT_ACTION_WX_AUTH_LOGGED_IN);
//            sendBroadcast(intent);// 广播一个微信授权验证信息
//        }
//        if(!TextUtils.isEmpty(result)) com.procoin.http.util.CommonUtil.showmessage(result,WXEntryActivity.this);
////        CommonUtil.showToast(this, result, Gravity.BOTTOM);
//        finish();
//        overridePendingTransition(0,0);
//    }
//
//    @Override
//    public void goback() {
//        CommonUtil.pageJump(this, null, true, true);
//    }
//}
