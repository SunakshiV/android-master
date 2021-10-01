package com.procoin.social.weibo;

import com.procoin.social.baseui.AbstractBaseActivity;

public class CopyOfTjrSocialShareWeiboActivity extends AbstractBaseActivity {

	@Override
	public void goback() {
		// TODO Auto-generated method stub
		
	}
//	private final static int WEIBO_SEND_OK = 0; // //向weibo发送数据成功
//	private final static int WEIBO_SEND = 1; // //向weibo发送数据
//	private final static int WEIBO_GET_SHORT_URL = 2; // 从weibo获取短链接
//	private final static int WEIBO_AUTH = 3; // weibo重新授权
//	private final static int WEIBO_ERROR = 4; // weibo发生错误
//	private final static int WEIBO_AUTH_SHOWMSG = 5; // weibo提示
//	private final static int WEIBO_AUTH_SHOWBIND = 6; // weibo绑定提示
//	private AlertDialog alertDialog, bindAlertDialog; // 弹出对话框
//	private boolean isBind;// 是不是要綁定，需要爲true
//	private SharedPreferences sharedata;
//	private AddWeiboTask mAddWeiboTask;
//	private BindWeiboTask mBindWeiboTask; // 绑定weibo
//	private String weiboTokenStr, weiboUidStr, screenName;
//	private String fileName, fileUrl;
//	private String weiboShortUrl;// 从weibo获取短链接url
//	private String weiboNormalUrl;// 从后台获取url
//	private String jsonStr; // 分享链接json数据格式
//							// 如报纸组件:{"relType":"paper","relId":"195272"}
//	private Bundle mBundle;// 页面传递进来的
//	private boolean isneedUrl;// 是不是需要增加详细页的Url 默认是true，
//								// 当需要http://www.taojinroad.com 就不需要
//
//	private SendWeiboListen sendWeiboListen;
//	private WeiboShortUrlListen shortUrlListen;
//	public static Oauth2AccessToken accessToken;
//	private Weibo mWeibo;
//	private StatusesAPI statuesApi;
//	private ShortUrlAPI shortUrlAPI;
//	private UsersAPI mUsersAPI;
//	/**
//	 * SsoHandler 仅当sdk支持sso时有效，
//	 */
//	private SsoHandler mSsoHandler;
//	private ShareUI shareUI;
//	private int styleId;
//	private Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			switch (msg.what) {
//			case WEIBO_SEND_OK: // 向weibo发送数据成功返回
//				dismissProgressDialog();
//				CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "分享成功", Gravity.CENTER);
//				if (isBind) {
//					showBindAcountDalog();
//				} else {
//					goback();
//				}
//				break;
//			case WEIBO_SEND: // 向weibo发送数据
//				statuesApi = new StatusesAPI(accessToken);
//				if (weiboShortUrl == null) weiboShortUrl = "";
//				String Content = shareUI.getEdWeibo().getText().toString() + weiboShortUrl + " （分享自 @淘金路金融社交 ）";
//				if (fileName != null) {
//					String Imageurl = TaojinluHttp.getInstance().getWeiboImageUrl(fileName.replace(".bm", ""));
//					statuesApi.uploadUrlText(Content, Imageurl, null, null, sendWeiboListen);
//				} else {
//					statuesApi.update(Content, null, null, sendWeiboListen);// 不发图片
//				}
//				break;
//			case WEIBO_GET_SHORT_URL:// 从weibo获取短链接
//				shortUrlAPI = new ShortUrlAPI(accessToken);
//				shortUrlAPI.shorten(new String[] { weiboNormalUrl }, shortUrlListen);
//				break;
//			case WEIBO_AUTH:// weibo重新授权
//				if (mSsoHandler == null) {
//					mSsoHandler = new SsoHandler(CopyOfTjrSocialShareWeiboActivity.this, mWeibo);
//				}
//				mSsoHandler.authorize(new AuthDialogListener());
//				break;
//			case WEIBO_ERROR://
//				dismissProgressDialog();
//				CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "发送失败", Gravity.BOTTOM);
//				break;
//			case WEIBO_AUTH_SHOWMSG:// 提示weibo内容
//				showDalog();
//				break;
//			case WEIBO_AUTH_SHOWBIND:// 提示绑定weibo
//				showBindAcountDalog();
//				break;
//			default:
//				break;
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		isneedUrl = true;
//		if (this.getIntent() != null) mBundle = this.getIntent().getExtras();
//		if (mBundle != null) {
//			styleId = mBundle.getInt(TjrSocialShareConfig.KEY_EXTRAS_STYLEID, 0);
//			if (mBundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_ISNEEDURL)) isneedUrl = mBundle.getBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISNEEDURL);
//		}
//		shareUI = new ShareUI(this, styleId);
//		shareUI.setAt(true);
//		shareUI.setIsface(true);
//		shareUI.setOtherImg(true);
//
//		mWeibo = Weibo.getInstance(TjrSocialShareConfig.APP_WEIBO_KEY, TjrSocialShareConfig.REDIRECT_WEIBO_URL);
//		sharedata = ShareData.getUserSharedPreferencesForSocial(CopyOfTjrSocialShareWeiboActivity.this);
//		weiboTokenStr = "";
//		weiboUidStr = "";
//		screenName = "";
//		if (sharedata != null) {
//			weiboTokenStr = ShareData.getWeiboTocken(sharedata);
//			weiboUidStr = ShareData.getWeiboUid(sharedata);
//			screenName = ShareData.getWeiboScreenName(sharedata);
//		}
//		sendWeiboListen = new SendWeiboListen();
//		shortUrlListen = new WeiboShortUrlListen();
//		accessToken = new Oauth2AccessToken();
//		accessToken.setToken(weiboTokenStr);
//
//		if (mBundle != null) {
//			if (mBundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR)) {
//				jsonStr = mBundle.getString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR);
//				if (jsonStr != null) shareUI.setOtherImg(false);
//			}
//			shareUI.getData(mBundle);
//			// 组件分享json数据格式
//		} else {
//			shareUI.getData(new Bundle());
//		}
//		shareUI.setCallBack(new ShareUICallBack() {
//			@Override
//			public void sendWeibo(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option) {
//				CommonUtil.LogLa(2, " weiboType=" + weiboType + " \n bmp=" + bmp + " \n text=" + text + " \n voteTitle=" + voteTitle + " \n voteType=" + voteType + " \n option=" + option);
//				// 向服务器发送内容，并获取url相应的id值
//				if (text == null || text.length() == 0) {
//					CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "发送内容不能为空", Gravity.BOTTOM);
//					return;
//				}
//				startAddWeiboTask(weiboType, text, bmp, voteTitle, voteType, option);
//				// 在发送之前要判断用户是否绑定weibo,如果没有绑定weibo就走绑定流程
//			}
//		});
//		setContentView(shareUI.getView());
//	}
//
//	class AuthDialogListener implements WeiboAuthListener {
//
//		@Override
//		public void onComplete(Bundle values) {
//			String code = values.getString("code");
//			if (code != null) {
//				Toast.makeText(CopyOfTjrSocialShareWeiboActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
//				return;
//			}
//			weiboTokenStr = values.getString("access_token");
//			weiboUidStr = values.getString("uid");
//			accessToken.setToken(weiboTokenStr);
//			screenName = values.getString("userName");
//			getShortUrlorSend();
//			// TODO 是不是需要申请连接
//			// if (isneedUrl) {
//			// handler.sendEmptyMessage(WEIBO_GET_SHORT_URL);// 获取短链接
//			// } else {
//			// handler.sendEmptyMessage(WEIBO_SEND);// 发送weibo内容
//			// }
//
//			// handler.sendEmptyMessage(WEIBO_AUTH_SHOWMSG);
//			// ShareData.saveWeibo(sharedata, weiboUidStr, weiboTokenStr);
//			// handler.sendEmptyMessage(WEIBO_GET_SHORT_URL);// 获取短链接
//			// if (isBind) startBindWeiboTask(screenName);
//			// isBind = false;
//		}
//
//		@Override
//		public void onError(WeiboDialogError e) {
//			Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(), Toast.LENGTH_LONG).show();
//		}
//
//		@Override
//		public void onCancel() {
//			Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
//		}
//
//		@Override
//		public void onWeiboException(WeiboException e) {
//			Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
//		}
//
//	}
//
//	class SendWeiboListen implements RequestListener {
//
//		@Override
//		public void onComplete(String arg0) {
//			CommonUtil.LogLa(2, " SendWeiboListen onComplete======" + arg0);
//			handler.sendEmptyMessage(WEIBO_SEND_OK);// 发送成功
//		}
//
//		@Override
//		public void onError(WeiboException arg0) {
//			CommonUtil.LogLa(2, " SendWeiboListen WeiboException======" + arg0.getMessage());
//			handler.sendEmptyMessage(WEIBO_ERROR);// 发送失败
//		}
//
//		@Override
//		public void onIOException(IOException arg0) {
//			CommonUtil.LogLa(2, " SendWeiboListen IOException======" + arg0.getMessage());
//			handler.sendEmptyMessage(WEIBO_ERROR);// 发送失败
//		}
//	}
//
//	class WeiboShortUrlListen implements RequestListener {
//
//		@Override
//		public void onComplete(String arg0) {
//			try {
//				if (arg0 != null) {
//					JSONObject jsonObject = new JSONObject(arg0);
//					if (hasAndNotNull(jsonObject, "urls")) {
//						JSONArray array = jsonObject.getJSONArray("urls");
//						int m = array.length();
//						if (m > 0) {
//							for (int i = 0; i < m; i++) {
//								JSONObject ob = array.getJSONObject(i);
//								if (hasAndNotNull(ob, "url_short")) {
//									weiboShortUrl = " " + ob.getString("url_short");// 加个空格避免
//								}
//							}
//						}
//					}
//				}
//				handler.sendEmptyMessage(WEIBO_SEND);// 发送weibo内容
//			} catch (Exception e) {
//				handler.sendEmptyMessage(WEIBO_ERROR);
//			}
//		}
//
//		@Override
//		public void onError(WeiboException arg0) {
//			CommonUtil.LogLa(2, " WeiboShortUrlListen WeiboException======" + arg0.getMessage());
//			handler.sendEmptyMessage(WEIBO_AUTH);
//		}
//
//		@Override
//		public void onIOException(IOException arg0) {
//			CommonUtil.LogLa(2, " WeiboShortUrlListen IOException======" + arg0.getMessage());
//			handler.sendEmptyMessage(WEIBO_ERROR);// 发送失败
//		}
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		if (shareUI != null) shareUI.onActivityResult(requestCode, resultCode, data);
//		// sso 授权回调
//		if (mSsoHandler != null) {
//			mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
//		}
//	}
//
//	@Override
//	public void goback() {
//		CommonUtil.pageJump(this, null, true, true);
//	}
//
//	/**
//	 * 添加一个weibo到服务器
//	 */
//	private void startAddWeiboTask(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option) {
//		CommonUtil.cancelAsyncTask(mAddWeiboTask);
//		mAddWeiboTask = (AddWeiboTask) new AddWeiboTask(weiboType, text, bmp, voteTitle, voteType, option).executeParams();
//	}
//
//	private class AddWeiboTask extends AsyncTask<Void, Void, String> {
//		private Exception exception;
//		private int weiboType;
//		private String text;
//		private Bitmap bmp;
//		private String voteTitle;
//		private int voteType;
//		private String option;
//
//		public AddWeiboTask(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option) {
//			this.weiboType = weiboType;
//			this.text = text;
//			this.bmp = bmp;
//			this.voteTitle = voteTitle;
//			this.voteType = voteType;
//			this.option = option;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			showLoadingProgressDialog(getResources().getString(R.string.loading_date_message));
//		}
//
//		@Override
//		protected String doInBackground(Void... params) {
//			try {
//				int cType = 1, isFile = 0;// cType=1纯文字,cType=2图片,cType=3当json
//				if (bmp != null) {
//					isFile = 1;
//					cType = 2;
//					createFileInfo(bmp);// 创建图片
//				}
//				if (jsonStr != null) {
//					cType = 3;
//					isFile = 0;
//				}
//				JSONArray contenjson = new JSONArray();
//				if (cType == 3) {// 目前是只要單個
//					contenjson.put(new JSONObject().put("cType", cType).put("content", jsonStr).put("isFile", isFile));
//				} else if (cType == 2) {
//					contenjson.put(new JSONObject().put("cType", cType).put("content", fileName).put("isFile", isFile));
//				} else {
//					contenjson.put(new JSONObject().put("cType", cType).put("content", "").put("isFile", isFile));
//				}
//				String jsonStr = TaojinluHttp.getInstance().addWeiboAndOptio(weiboType, text, contenjson.toString(), voteTitle, voteType, option, shareUI.getUser().getUserId(), fileName, fileUrl, isFile);
//				if ("".equals(weiboTokenStr)) {// 从后台获取其相关用户绑定账号
//					String socialStr = TaojinluHttp.getInstance().getAccounts(String.valueOf(shareUI.getUser().getUserId()));
//					parserSocialJSON(socialStr);
//				}
//				CommonUtil.LogLa(4, "jsonStr=" + jsonStr);
//				JSONObject json = new JSONObject(jsonStr);
//				if (hasAndNotNull(json, "success")) {
//					String suc = json.getString("success");
//					if (suc != null && suc.matches("[0-9]+$")) {
//						weiboNormalUrl = TaojinluHttp.getInstance().getWeiboUrl(suc);
//						if ("".equals(weiboTokenStr)) { // 说明该用户还没有weibo授权
//							isBind = true;// 代表要走绑定流程
//							handler.sendEmptyMessage(WEIBO_AUTH);// 并进行授权
//						} else {
//							getShortUrlorSend();
//							// TODO 是不是需要申请连接
//							// if (isneedUrl) {
//							// handler.sendEmptyMessage(WEIBO_GET_SHORT_URL);//
//							// 获取短链接
//							// } else {
//							// handler.sendEmptyMessage(WEIBO_SEND);// 发送weibo内容
//							// }
//						}
//						return "1";
//					} else {
//						return null;
//					}
//				} else {
//					return null;
//				}
//			} catch (Exception e) {
//				exception = e;
//				return null;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (exception != null) {
//				NotificationsUtil.ToastReasonForFailure(CopyOfTjrSocialShareWeiboActivity.this, exception);
//				dismissProgressDialog();
//			} else if (result == null) {
//				handler.sendEmptyMessage(WEIBO_ERROR);// 失败
//				dismissProgressDialog();
//			}
//		}
//
//	}
//
//	/**
//	 * 绑定weibo,并获取微博昵称screen_name
//	 */
//	public void startBindWeiboTask(String screenName) {
//		try {
//			if (screenName != null) {
//				if (screenName != null && !"".equals(screenName)) ShareData.saveWeiboScreenName(sharedata, screenName);
//				sendBindWeiboInfoTask(screenName);
//			} else {
//				mUsersAPI = new UsersAPI(accessToken);
//				mUsersAPI.show(Long.valueOf(weiboUidStr), new ShowUserReq());
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//
//	class ShowUserReq implements RequestListener {
//
//		@Override
//		public void onComplete(String arg0) {
//			try {
//				JSONObject data = new JSONObject(arg0);
//				screenName = data.getString("screen_name");
//				if (screenName != null && !"".equals(screenName)) ShareData.saveWeiboScreenName(sharedata, screenName);
//				sendBindWeiboInfoTask(screenName);
//			} catch (JSONException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//
//		@Override
//		public void onError(WeiboException arg0) {
//			handler.sendEmptyMessage(WEIBO_ERROR);// 失败
//		}
//
//		@Override
//		public void onIOException(IOException arg0) {
//			handler.sendEmptyMessage(WEIBO_ERROR);// 失败
//		}
//	}
//
//	public void sendBindWeiboInfoTask(String screenName) {
//		CommonUtil.cancelAsyncTask(mBindWeiboTask);
//		mBindWeiboTask = (BindWeiboTask) new BindWeiboTask().executeParams(screenName);
//	}
//
//	private class BindWeiboTask extends AsyncTask<String, Void, String> {
//
//		@Override
//		protected String doInBackground(String... params) {
//			try {
//				return TaojinluHttp.getInstance().bindAccount(String.valueOf(shareUI.getUser().getUserId()), CommonConst.LOGIN_TYPE_SINAWB, weiboUidStr, weiboTokenStr, "", params[0]);
//			} catch (Exception e) {
//				return null;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			// TODO Auto-generated method stub
//			super.onPostExecute(result);
//			try {
//				if (result != null) {
//					JSONObject jsonObject = new JSONObject(result);
//					String msg = "绑定失败!";
//					if (hasAndNotNull(jsonObject, "retCode")) {
//						if (hasAndNotNull(jsonObject, "message")) msg = jsonObject.getString("message");
//						int code = jsonObject.getInt("retCode");
//						if (code == -3 || code == -2 || code == -4) {
//							CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, msg, Gravity.BOTTOM);
//						} else {
//							CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "绑定失败!", Gravity.BOTTOM);
//						}
//					} else {
//						if (hasAndNotNull(jsonObject, "success")) {
//							boolean suc = jsonObject.getBoolean("success");
//							if (suc) {
//								if (weiboTokenStr != null && weiboUidStr != null && weiboUidStr.length() > 0) ShareData.saveWeibo(sharedata, weiboUidStr, weiboTokenStr);
//								CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "绑定成功!", Gravity.BOTTOM);
//							} else {
//								CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "绑定失败!", Gravity.BOTTOM);
//							}
//						} else {
//							CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "绑定失败!", Gravity.BOTTOM);
//						}
//					}
//				} else {
//					CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "绑定失败!", Gravity.BOTTOM);
//				}
//			} catch (Exception e) {
//				CommonUtil.showToast(CopyOfTjrSocialShareWeiboActivity.this, "绑定失败!", Gravity.BOTTOM);
//			}
//			dismissProgressDialog();
//			goback();
//		}
//	}
//
//	/**
//	 * 解析用户绑定weibo信息
//	 * 
//	 * @param result
//	 */
//	private void parserSocialJSON(String result) {
//		try {
//			JSONObject json = new JSONObject(result);
//			if (hasAndNotNull(json, "accessToken")) {
//				weiboTokenStr = json.getString("accessToken");
//				accessToken.setToken(weiboTokenStr);
//			}
//			if (hasAndNotNull(json, "sinawb")) {
//				weiboUidStr = json.getString("sinawb");
//			}
//			if (weiboTokenStr != null && weiboUidStr != null && weiboUidStr.length() > 0) ShareData.saveWeibo(sharedata, weiboUidStr, weiboTokenStr);
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}
//
//	/**
//	 * 创建图片
//	 * 
//	 * @param bmp
//	 */
//	private void createFileInfo(Bitmap bmp) throws Exception {
//		if (fileName == null) fileName = CommonUtil.getFileName(shareUI.getUser().getUserId());
//		File file = CommonUtil.GetWeiboFile(fileName);
//		fileUrl = file.getPath();
//		CommonUtil.LogLa(2, "    filename=" + fileName + " fileUrl=" + fileUrl);
//		CommonUtil.writeFile(file, bmp, false);
//	}
//
//	/**
//	 * 弹出发送weibo
//	 * 
//	 * @param taUserId
//	 */
//	public void showDalog() {
//		// 对话框
//		if (alertDialog == null) {
//			// 文本输入框
//			alertDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage(Html.fromHtml("是否分享到<font color=\"#2593d0\">" + screenName + "</font>微博?")).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface v, int btn) {
//					// isBind = true;
//					// handler.sendEmptyMessage(WEIBO_AUTH);
//					getShortUrlorSend();
//					// handler.sendEmptyMessage(WEIBO_GET_SHORT_URL);// 获取短链接
//				}
//			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface v, int btn) {
//					v.cancel(); // 关闭当前的对话框.
//					weiboTokenStr = "";
//					weiboUidStr = "";
//					dismissProgressDialog();
//				}
//			}).create();
//		}
//		alertDialog.show();
//	}
//
//	/**
//	 * 弹出绑定weibo
//	 * 
//	 * @param taUserId
//	 */
//	public void showBindAcountDalog() {
//		// 对话框
//		if (bindAlertDialog == null) {
//			// 文本输入框
//			if(screenName == null)screenName="该";
//			bindAlertDialog = new AlertDialog.Builder(this).setTitle("提示").setMessage(Html.fromHtml("是否把<font color=\"#2593d0\">" + screenName + "</font>微博绑定到本账号?")).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface v, int btn) {
//					isBind = false;
//					showLoadingProgressDialog(getResources().getString(R.string.loading_date_message));
//					startBindWeiboTask(screenName);
//				}
//			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				public void onClick(DialogInterface v, int btn) {
//					v.cancel(); // 关闭当前的对话框.
//					weiboTokenStr = "";
//					weiboUidStr = "";
//					goback();
//					//getShortUrlorSend();
//				}
//			}).create();
//		}
//		bindAlertDialog.show();
//	}
//	/**
//	 * 申请连接或者发送微薄
//	 */
//	private void getShortUrlorSend() {
//		// TODO 是不是需要申请连接
//		if (isneedUrl) {
//			handler.sendEmptyMessage(WEIBO_GET_SHORT_URL);// 获取短链接
//		} else {
//			handler.sendEmptyMessage(WEIBO_SEND);// 发送weibo内容
//		}
//	}

}
