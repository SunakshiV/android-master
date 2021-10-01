package com.procoin.social.weibo;

import com.procoin.social.baseui.AbstractBaseActivity;

//public class CopyOfTjrSocialShareWeiboAtActivity extends AbstractBaseActivity implements RequestListener, AuthWeiboCompleteCallBack {
public class CopyOfTjrSocialShareWeiboAtActivity extends AbstractBaseActivity {

	@Override
	public void goback() {
		// TODO Auto-generated method stub
		
	}
//	private View view;
//	private User user;
//	private AuthWeibo authWeibo;
//	private Onclick onClick;
//	private Bundle bundle;
//	private ListView listview;
//	private Group<WeiboAtName> group;
//	private DialogListViewAdapter dialogAdapter;
//	private WeiboAtNameJason parser;
//	private WeiboWatch weiwatch;
//	private SearchListener searchListener;
//	private FriendshipsAPI friendsApi;
//	private SearchAPI searchAPI;
//	private Intent intent;
//	private ProgressBar progressBarWait;
//	private Handler hand = new Handler() {
//		@SuppressWarnings("unchecked")
//		public void handleMessage(android.os.Message msg) {
//			switch (msg.what) {
//			case 0:
//				dialogAdapter.clearAllItem();
//				dialogAdapter.setGroup(((Group<WeiboAtName>) group.clone()));
//				progressBarWait.setVisibility(View.GONE);
//				break;
//			case 1:// 重新授权
//				if (!CopyOfTjrSocialShareWeiboAtActivity.this.isFinishing()) {
//					authWeibo.authorizeToWeibo(true);
//				}
//				break;
//			case 2:
//				CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "网络错误", Gravity.BOTTOM);
//				break;
//			case 3:// 获取好友
//				if (authWeibo.getWeiboUid() != null && authWeibo.getWeiboUid().matches("[0-9]+$")) {
//					friendsApi.friends(Long.valueOf(authWeibo.getWeiboUid()), 100, 0, false, CopyOfTjrSocialShareWeiboAtActivity.this);
//					progressBarWait.setVisibility(View.VISIBLE);
//				} else {
//					authWeibo.startGetAccountsWeiboTask();
//				}
//				break;
//			}
//		};
//	};
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		bundle = getIntent().getExtras();
//		if (bundle != null) {
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERID)) {
//				user = new User();
//				user.setUserId(bundle.getLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, 0l));
//			}
//			if (user == null || user.getUserId() == 0) {
//				CommonUtil.showToast(this, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//				goback();
//				return;
//			}
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERNAME)) {
//				user.setName(bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME));
//			}
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL)) {
//				user.setHeadurl(bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL));
//			}
//		}
//		setContentView(showView());
//		authWeibo = new AuthWeibo(this, user, this);
//		searchListener = new SearchListener();
//		friendsApi = new FriendshipsAPI(authWeibo.getAccessToken());
//		searchAPI = new SearchAPI(authWeibo.getAccessToken());
//		if (authWeibo.getWeiboUid() == null || authWeibo.getAccessToken() == null || "".equals(authWeibo.getAccessToken()) || "".equals(authWeibo.getWeiboUid())) {
//			authWeibo.startGetAccountsWeiboTask();
//		} else {
//			hand.sendEmptyMessage(3);
//		}
//	}
//
//	/**
//	 * weibo授权成功回调接口
//	 */
//	@Override
//	public void authWeiboComplete(String weiboUid, String weiboToken, String screenName) {
//		// TODO Auto-generated method stub
//		friendsApi = new FriendshipsAPI(authWeibo.getAccessToken());
//		searchAPI = new SearchAPI(authWeibo.getAccessToken());
//		hand.sendEmptyMessage(3);
//	}
//
//	@Override
//	public void getAccountsComplete(String weiboUid, String weiboToken) {
//		// TODO Auto-generated method stub
//		friendsApi = new FriendshipsAPI(authWeibo.getAccessToken());
//		searchAPI = new SearchAPI(authWeibo.getAccessToken());
//		hand.sendEmptyMessage(3);
//	}
//
//	private View showView() {
//		if (view == null) {
//			view = this.inflateView(R.layout.tjr_social_at_activity);
//			progressBarWait = (ProgressBar) view.findViewById(R.id.progressBarWait);
//			listview = (ListView) view.findViewById(R.id.lvchoice);
//			dialogAdapter = new DialogListViewAdapter(this);
//			group = new Group<WeiboAtName>();
//			parser = new WeiboAtNameJason();
//			onClick = new Onclick();
//			intent = new Intent();
//			EditText aut = (EditText) view.findViewById(R.id.edAtweibo);
//			weiwatch = new WeiboWatch();
//			aut.addTextChangedListener(weiwatch);
//			listview.setAdapter(dialogAdapter);
//			listview.setOnItemClickListener(new OnItemClickListener() {
//
//				@Override
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
//					intent.putExtra("friends_name", group.get(arg2).getScreen_name() + " ");
//					CopyOfTjrSocialShareWeiboAtActivity.this.setResult(RESULT_OK, intent);
//					goback();
//				}
//			});
//			Button btnblack = (Button) view.findViewById(R.id.btnBack);
//			btnblack.setOnClickListener(onClick);
//		}
//		return view;
//	}
//
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		// sso 授权回调
//		if (authWeibo != null) {
//			authWeibo.getmSsoHandler().authorizeCallBack(requestCode, resultCode, data);
//		}
//	}
//
//	class Onclick implements View.OnClickListener {
//
//		@Override
//		public void onClick(View v) {
//			if (v.getId() == R.id.btnBack) {
//				goback();
//			}
//
//		}
//	}
//
//	class WeiboWatch implements TextWatcher {
//
//		@Override
//		public void afterTextChanged(Editable s) {
//
//		}
//
//		@Override
//		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//		}
//
//		@Override
//		public void onTextChanged(CharSequence s, int start, int before, int count) {
//			if (!s.toString().equals("")) {
//				searchAPI.atUsers(s.toString(), 20, WeiboAPI.FRIEND_TYPE.ATTENTIONS, WeiboAPI.RANGE.ALL, searchListener);
//			} else {
//				hand.sendEmptyMessage(3);
//			}
//		}
//
//	}
//
//	class SearchListener implements RequestListener {
//		WeiboAtName a = new WeiboAtName();
//
//		public SearchListener() {
//			a.setScreen_name("无联系人");
//		}
//
//		@Override
//		public void onComplete(String arg0) {
//			CommonUtil.LogLa(2, "AtWeicoRL==" + arg0);
//			JSONArray jsonarry;
//			try {
//				jsonarry = new JSONArray(arg0);
//				if (group != null) group.clear();
//				if (!jsonarry.isNull(0)) {
//					for (int i = 0; i < jsonarry.length(); i++) {
//						JSONObject hes = jsonarry.getJSONObject(i);
//						group.add(parser.parse(hes));
//					}
//
//				} else {
//					group.add(a);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//			hand.sendEmptyMessage(0);
//		}
//
//		@Override
//		public void onError(WeiboException arg0) {
//			CommonUtil.LogLa(2, CommonUtil.makeLogTag(CopyOfTjrSocialShareWeiboAtActivity.class) + " WeiboException=" + arg0.getMessage());
//			JSONObject jason;
//			if (!CopyOfTjrSocialShareWeiboAtActivity.this.isFinishing()) {
//				try {
//					jason = new JSONObject(arg0.getMessage());
//					if (hasAndNotNull(jason, "error")) {
//						hand.sendEmptyMessage(1);
//					} else {
//						hand.sendEmptyMessage(2);
//					}
//				} catch (JSONException e1) {
//					e1.printStackTrace();
//					CommonUtil.LogLa(2, "IOException=" + arg0.getMessage());
//					hand.sendEmptyMessage(2);
//				}
//			}
//
//		}
//
//		@Override
//		public void onIOException(IOException arg0) {
//			CommonUtil.LogLa(2, "IOException=" + arg0.getMessage());
//
//		}
//
//	}
//
//	@Override
//	public void goback() {
//		this.finish();
//	}
//
//	@Override
//	public void onComplete(String arg0) {
//		CommonUtil.LogLa(2, "FriendsWeico=" + arg0);
//		JSONObject json;
//		try {
//			json = new JSONObject(arg0);
//			if (group != null) group.clear();
//			if (hasAndNotNull(json, "users")) {
//				JSONArray jason = new JSONArray(json.getString("users"));
//				for (int i = 0; i < jason.length(); i++) {
//					JSONObject hes = jason.getJSONObject(i);
//					group.add(parser.parse(hes));
//				}
//			}
//		} catch (JSONException e) {
//			e.printStackTrace();
//		}
//		hand.sendEmptyMessage(0);
//	}
//
//	@Override
//	public void onError(WeiboException arg0) {
//		CommonUtil.LogLa(2, "WeiboException=" + arg0.getMessage());
//		JSONObject jason;
//		try {
//			jason = new JSONObject(arg0.getMessage());
//			if (hasAndNotNull(jason, "error")) {
//				hand.sendEmptyMessage(1);
//			} else {
//				CommonUtil.LogLa(2, "JSONException=" + arg0.getMessage());
//				hand.sendEmptyMessage(2);
//			}
//		} catch (JSONException e1) {
//			e1.printStackTrace();
//			CommonUtil.LogLa(2, "JSONException=" + e1.getMessage());
//			hand.sendEmptyMessage(2);
//		}
//	}
//
//	@Override
//	public void onIOException(IOException arg0) {
//		CommonUtil.LogLa(2, "IOException=" + arg0.getMessage());
//	}
//
//	@Override
//	public void bindWeiboAccountsComplete(String weiboUid, String weiboToken, String screenName, String json) {
//		// TODO Auto-generated method stub
//
//		try {
//			if (json != null) {
//				JSONObject jsonObject = new JSONObject(json);
//				String msg = "绑定失败!";
//				if (hasAndNotNull(jsonObject, "retCode")) {
//					if (hasAndNotNull(jsonObject, "message")) msg = jsonObject.getString("message");
//					int code = jsonObject.getInt("retCode");
//					if (code == -3 || code == -2 || code == -4) {
//						CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, msg, Gravity.BOTTOM);
//					} else {
//						CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//					}
//				} else {
//					if (hasAndNotNull(jsonObject, "success")) {
//						boolean suc = jsonObject.getBoolean("success");
//						if (suc) {
//							CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "绑定成功!", Gravity.BOTTOM);
//						} else {
//							CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//						}
//					} else {
//						CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//					}
//				}
//			} else {
//				CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//			}
//		} catch (Exception e) {
//			CommonUtil.showToast(CopyOfTjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//		}
//		authWeiboComplete(weiboUid, weiboToken, screenName);
//	}
//
//	@Override
//	public void authWeiboError() {
//		// TODO Auto-generated method stub
//
//	}

}
