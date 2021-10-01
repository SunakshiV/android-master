package com.procoin.social;

import com.procoin.social.baseui.AbstractBaseActivity;
//import com.cropyme.stock.klinepage.StockKLinePage;

public class TjrsocialActivity extends AbstractBaseActivity {
    /**
     * Called when the activity is first created.
     */
//
//	private TextView mTokenText;
//
//	/** 微博 Web 授权类，提供登陆等功能 */
//	@SuppressWarnings("unused")
//	private WeiboAuth mWeiboAuth;
//	/** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能 */
//	private Oauth2AccessToken mAccessToken;
//
//	/** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
//	private SsoHandler mSsoHandler;
//	private StockKLinePage iPage;
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.main);
//		LinearLayout l = (LinearLayout) findViewById(R.id.ssid);
//		// IndexKLinePage in = new IndexKLinePage(this);
//		 iPage = new StockKLinePage(this);
////		IndexKLinePage iPage = new IndexKLinePage(this);
//		// StockShareTimePage iPage = new StockShareTimePage(this);
//		 l.addView(iPage.getView());
//		iPage.startGetDataTask("sz002340");
//
////		ChatStockmenu menChatStockmenu = new ChatStockmenu(this);
////		menChatStockmenu.setType(3);
////		l.addView(menChatStockmenu.showView());
//
//		// // 创建微博实例
//		// mWeiboAuth = new WeiboAuth(this, TjrSocialShareConfig.APP_WEIBO_KEY,
//		// TjrSocialShareConfig.REDIRECT_WEIBO_URL,
//		// TjrSocialShareConfig.WEIBO_SCOPE);
//		// // 触发sso测试button
//		// findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
//		// @Override
//		// public void onClick(View v) {
//		// // User user = new User();
//		// // user.setUserId(7l);
//		// // user.setName("吕林青");
//		// //
//		// TjrSocialShare.getInstance().shareToWeiboWithCanvas(TjrsocialActivity.this,
//		// "aaaaaaaa", TjrsocialActivity.this.getWindow().getDecorView(), null,
//		// user, false);
//		// mSsoHandler = new SsoHandler(TjrsocialActivity.this, mWeiboAuth);
//		// mSsoHandler.authorize(new AuthListener());
//		//
//		// }
//		// });
//		// // 从 SharedPreferences 中读取上次已保存好 AccessToken 等信息，
//		// // 第一次启动本应用，AccessToken 不可用
//		// // mAccessToken = AccessTokenKeeper.readAccessToken(this);
//		// if (mAccessToken.isSessionValid()) {
//		// updateTokenView(true);
//		// }
//	}
//
//	@Override
//	public void onWindowFocusChanged(boolean hasFocus) {
//		// TODO Auto-generated method stub
//		super.onWindowFocusChanged(hasFocus);
//		if(iPage  != null)iPage.updateBtnCycleXY();//更新除权坐标位置
//	}
//
//	/**
//	 * 显示当前 Token 信息。
//	 *
//	 * @param hasExisted
//	 *            配置文件中是否已存在 token 信息并且合法
//	 */
//	private void updateTokenView(boolean hasExisted) {
//		String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new java.util.Date(mAccessToken.getExpiresTime()));
//		String format = "Token：%1$s \n有效期：%2$s";
//		mTokenText.setText(String.format(format, mAccessToken.getToken(), date));
//
//		String message = String.format(format, mAccessToken.getToken(), date);
//		if (hasExisted) {
//			message = "Token 仍在有效期内，无需再次登录。" + "\n" + message;
//		}
//		mTokenText.setText(message);
//	}
//
//	/**
//	 * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用
//	 * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
//	 * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
//	 * SharedPreferences 中。
//	 */
//	class AuthListener implements WeiboAuthListener {
//
//		@Override
//		public void onComplete(Bundle values) {
//			// 从 Bundle 中解析 Token
//			mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//			if (mAccessToken.isSessionValid()) {
//				// 显示 Token
//				updateTokenView(false);
//
//				// 保存 Token 到 SharedPreferences
//				// AccessTokenKeeper.writeAccessToken(TjrsocialActivity.this,
//				// mAccessToken);
//				Toast.makeText(TjrsocialActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
//			} else {
//				// 当您注册的应用程序签名不正确时，就会收到 Code，请确保签名正确
//				String code = values.getString("code");
//				String message = "授权失败";
//				if (!TextUtils.isEmpty(code)) {
//					message = message + "\nObtained the code: " + code;
//				}
//				Toast.makeText(TjrsocialActivity.this, message, Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onCancel() {
//			Toast.makeText(TjrsocialActivity.this, "取消授权", Toast.LENGTH_LONG).show();
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			Toast.makeText(TjrsocialActivity.this, "授权失败", Toast.LENGTH_LONG).show();
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//
//		// SSO 授权回调
//		// 重要：发起 SSO 登陆的 Activity 必须重写 onActivityResult
//		if (mSsoHandler != null) {
//			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//		}
//	}
//
    @Override
    public void goback() {
        // TODO Auto-generated method stub

    }

}