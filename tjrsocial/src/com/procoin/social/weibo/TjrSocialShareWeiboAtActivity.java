//package com.cropyme.social.weibo;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.ProgressBar;
//
//import com.sina.weibo.sdk.exception.WeiboException;
//import com.sina.weibo.sdk.net.RequestListener;
//import com.sina.weibo.sdk.openapi.legacy.FriendshipsAPI;
//import com.sina.weibo.sdk.openapi.legacy.SearchAPI;
//import com.sina.weibo.sdk.openapi.legacy.WeiboAPI;
//import Group;
//import User;
//import com.cropyme.social.R;
//import AbstractBaseActivity;
//import TjrSocialShareConfig;
//import WeiboAtName;
//import WeiboAtNameJason;
//import DialogListViewAdapter;
//import CommonUtil;
//import com.cropyme.social.weibo.AuthWeibo.AuthWeiboCompleteCallBack;
//
//public class TjrSocialShareWeiboAtActivity extends AbstractBaseActivity implements RequestListener, AuthWeiboCompleteCallBack {
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
//	private String sharePackage; // 分享的組件包名
//	private String appId; // 分享的Id
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
//				if (!TjrSocialShareWeiboAtActivity.this.isFinishing()) {
//					authWeibo.authorizeToWeibo(true);
//				}
//				break;
//			case 2:
//				CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "网络错误", Gravity.BOTTOM);
//				break;
//			case 3:// 获取好友
//				if (authWeibo.getWeiboUid() != null && authWeibo.getWeiboUid().matches("[0-9]+$")) {
//					friendsApi.friends(Long.valueOf(authWeibo.getWeiboUid()), 100, 0, false, TjrSocialShareWeiboAtActivity.this);
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
//			user = new User();
//			user.setUserId(0l);
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERID)) {
//				user.setUserId(bundle.getLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, 0l));
//			}
//			// if (user == null || user.getUserId() == 0) {
//			// CommonUtil.showToast(this, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//			// goback();
//			// return;
//			// }
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERNAME)) {
//				user.setName(bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME));
//			}
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL)) {
//				user.setHeadurl(bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL));
//			}
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID)) appId = bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID);// 发送的Url
//			if (bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_PACKAPE)) sharePackage = bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_PACKAPE);// 发送的Url
//		}
//		setContentView(showView());
//		authWeibo = new AuthWeibo(this, appId, sharePackage, user, this);
//		searchListener = new SearchListener();
//		friendsApi = new FriendshipsAPI(authWeibo.getmAccessToken());
//		searchAPI = new SearchAPI(authWeibo.getmAccessToken());
//		if (authWeibo.getWeiboUid() == null || authWeibo.getmAccessToken() == null || "".equals(authWeibo.getmAccessToken()) || "".equals(authWeibo.getWeiboUid())) {
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
//		friendsApi = new FriendshipsAPI(authWeibo.getmAccessToken());
//		searchAPI = new SearchAPI(authWeibo.getmAccessToken());
//		hand.sendEmptyMessage(3);
//	}
//
//	@Override
//	public void getAccountsComplete(String weiboUid, String weiboToken) {
//		// TODO Auto-generated method stub
//		friendsApi = new FriendshipsAPI(authWeibo.getmAccessToken());
//		searchAPI = new SearchAPI(authWeibo.getmAccessToken());
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
//					intent.putExtra("friends_name", group.get(arg2).screen_name + " ");
//					TjrSocialShareWeiboAtActivity.this.setResult(RESULT_OK, intent);
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
//			a.screen_name = "无联系人";
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
//			CommonUtil.LogLa(2, CommonUtil.makeLogTag(TjrSocialShareWeiboAtActivity.class) + " WeiboException=" + arg0.getMessage());
//			JSONObject jason;
//			if (!TjrSocialShareWeiboAtActivity.this.isFinishing()) {
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
//		@Override
//		public void onComplete4binary(ByteArrayOutputStream responseOS) {
//			// TODO Auto-generated method stub
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
//						CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, msg, Gravity.BOTTOM);
//					} else {
//						CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//					}
//				} else {
//					if (hasAndNotNull(jsonObject, "success")) {
//						boolean suc = jsonObject.getBoolean("success");
//						if (suc) {
//							CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "绑定成功!", Gravity.BOTTOM);
//						} else {
//							CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//						}
//					} else {
//						CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//					}
//				}
//			} else {
//				CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//			}
//		} catch (Exception e) {
//			CommonUtil.showToast(TjrSocialShareWeiboAtActivity.this, "绑定失败!", Gravity.BOTTOM);
//		}
//		authWeiboComplete(weiboUid, weiboToken, screenName);
//	}
//
//	@Override
//	public void authWeiboError() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onComplete4binary(ByteArrayOutputStream responseOS) {
//		// TODO Auto-generated method stub
//
//	}
//
//}
