//package com.procoin.wxapi;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.Gravity;
//
//import com.procoin.social.baseui.AbstractBaseActivity;
//import com.procoin.social.util.CommonUtil;
//import com.tencent.mm.opensdk.constants.ConstantsAPI;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//
///**
// * wx一定要起这个页面，不然你不能收到成功或失败的返回，
// *
// * @author zhengmj
// *
// */
//public class WXPayEntryActivity extends AbstractBaseActivity implements IWXAPIEventHandler {
//	public static final String INTENT_ACTION_PAY = "com.coingo.intent.action.INTENT_ACTION_PAY_OK";
//	private WeChatShare wechat;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		wechat = new WeChatShare(this);
//		getData(getIntent());
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		setIntent(intent);
//		getData(intent);
//	}
//
//	public void getData(Intent intent) {
//		wechat.getWXAPI().handleIntent(intent, this);
//	}
//
//	/**
//	 * 向微信向我们的回调
//	 */
//	@Override
//	public void onReq(BaseReq arg0) {
//		// TODO Auto-generated method stub
//		// CommonUtil.showToast(this, "sonReq", Gravity.CENTER);
//
//	}
//
//	@Override
//	public void onResp(BaseResp arg0) {
//		CommonUtil.LogLa(2, "------------WXEntryActivity----------------onResp----------");
////		Log.d("155","Pay onResp");
//		if (arg0.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
//			String result = null;
//			switch (arg0.errCode) {
//			case BaseResp.ErrCode.ERR_OK:
//				// if (arg0 instanceof SendAuth.Resp) {
//				// SendAuth.Resp resp = (SendAuth.Resp) arg0;
//				Intent intent = new Intent();
//				// intent.putExtra(WX_CODE, resp.code);
//				intent.setAction(INTENT_ACTION_PAY);
//				sendBroadcast(intent);// 广播成功支付
//				// }
//				result = "支付成功";
//				break;
//			case BaseResp.ErrCode.ERR_USER_CANCEL:
//				result = "支付取消";
//				break;
//			case BaseResp.ErrCode.ERR_AUTH_DENIED:
//				result = "支付被拒绝";
//				break;
//			default:
//				result = "未知错误";
//				break;
//			}
//			CommonUtil.showToast(this, result, Gravity.BOTTOM);
//		}
//		finish();
//	}
//
//	@Override
//	public void goback() {
//		CommonUtil.pageJump(this, null, true, true);
//	}
//}
