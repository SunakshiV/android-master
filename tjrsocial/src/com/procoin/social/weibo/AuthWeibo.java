//package com.cropyme.social.weibo;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.ProgressDialog;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Html;
//import android.util.Log;
//import android.view.Gravity;
//import android.widget.Toast;
//
////import com.sina.weibo.sdk.auth.Oauth2AccessToken;
////import com.sina.weibo.sdk.auth.WeiboAuth;
////import com.sina.weibo.sdk.auth.WeiboAuthListener;
////import com.sina.weibo.sdk.auth.sso.SsoHandler;
////import com.sina.weibo.sdk.exception.WeiboException;
////import com.sina.weibo.sdk.net.RequestListener;
////import com.sina.weibo.sdk.openapi.legacy.UsersAPI;
//import TaojinluHttp;
//import CommonConst;
//import TJrLoginTypeEnum;
//import com.cropyme.http.edcode.pay.AndroidRSA;
//import User;
//import com.cropyme.http.tjrcpt.TjrUserHttp;
//import ShareData;
//import com.cropyme.social.R;
//import TjrSocialShareConfig;
//import CommonUtil;
//import BaseAsyncTask;
//
//public class AuthWeibo {
//    private AuthWeiboCompleteCallBack callBack;
//    private Activity mActivity;
//    private BindWeiboTask mBindWeiboTask; // 绑定weibo
//    private GetAccountsWeiboTask mGetAccountsWeiboTask;
//    private SharedPreferences sharedata;
//    private String weiboToken;
//    private String weiboUid;
//    private String screenName;
//    private AlertDialog alertDialog; // 弹出对话框
//
//    /**
//     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
//     */
//    private Oauth2AccessToken mAccessToken;
//    /**
//     * 微博 Web 授权类，提供登陆等功能
//     */
//    private WeiboAuth mWeiboAuth;
//    /**
//     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
//     */
//    private SsoHandler mSsoHandler;
//    private UsersAPI mUsersAPI;
//    private User user;
//    private ProgressDialog progressDialog;
//    private Handler handler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case 0://
//                    dismissProgressDialog();
//                    CommonUtil.showToast(mActivity, "发送失败", Gravity.BOTTOM);
//                    break;
//                case -1:// 提示绑定
//                    showDalog();
//                    break;
//                case -2:// 直接绑定
//                    showLoadingProgressDialog(mActivity.getResources().getString(R.string.loading_date_message));
//                    startBindWeiboTask(screenName);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        ;
//    };
//
//    public AuthWeibo(Activity activity, User user, AuthWeiboCompleteCallBack callBack) {
//        this.mActivity = activity;
//        this.callBack = callBack;
//        this.user = user;
//        // 创建微博实例
//        mWeiboAuth = new WeiboAuth(activity, TjrSocialShareConfig.APP_WEIBO_KEY, TjrSocialShareConfig.REDIRECT_WEIBO_URL, TjrSocialShareConfig.WEIBO_SCOPE);
//        // 通过单点登录 (SSO) 获取 Token
//        mSsoHandler = new SsoHandler(activity, mWeiboAuth);
//        sharedata = ShareData.getUserSharedPreferencesForSocial(mActivity);
//        weiboToken = "";
//        weiboUid = "";
//        if (sharedata != null) {
//            weiboToken = ShareData.getWeiboTocken(sharedata);
//            weiboUid = ShareData.getWeiboUid(sharedata);
//        }
//        mAccessToken = new Oauth2AccessToken();
//        mAccessToken.setUid(weiboUid);
//        mAccessToken.setToken(weiboToken);
//    }
//
//    public AuthWeibo(Activity activity, String appId, String mainPackage, User user, AuthWeiboCompleteCallBack callBack) {
//        this.mActivity = activity;
//        this.callBack = callBack;
//        this.user = user;
//        // 创建微博实例
//        if (appId == null) appId = TjrSocialShareConfig.APP_WEIBO_KEY;
//        if (mainPackage == null) mainPackage = "com.coingo";//没有时默认
//        mWeiboAuth = new WeiboAuth(activity, appId, TjrSocialShareConfig.REDIRECT_WEIBO_URL, TjrSocialShareConfig.WEIBO_SCOPE);
//        // 通过单点登录 (SSO) 获取 Token
//        mSsoHandler = new SsoHandler(activity, mWeiboAuth);
//        sharedata = ShareData.getUserSharedPreferencesForSocial(mActivity, mainPackage);
//        weiboToken = "";
//        weiboUid = "";
//        if (sharedata != null) {
//            weiboToken = ShareData.getWeiboTocken(sharedata);
//            weiboUid = ShareData.getWeiboUid(sharedata);
//        }
//        mAccessToken = new Oauth2AccessToken();
//        mAccessToken.setUid(weiboUid);
//        mAccessToken.setToken(weiboToken);
//    }
//
//    /**
//     * 开始授权验证,要不要弹出提示框
//     */
//    public void authorizeToWeibo(boolean isAlert) {
//        mSsoHandler.authorize(new AuthListener(isAlert));
//    }
//
//    /**
//     * 不弹出绑定提示框
//     */
//    public void bindWeibo() {
//        // 对话框
//        authorizeToWeibo(false);
//    }
//
//    /**
//     * 微博认证授权回调类。 1. SSO 授权时，需要在 {@link #} 中调用
//     * {@link SsoHandler#authorizeCallBack} 后， 该回调才会被执行。 2. 非 SSO
//     * 授权时，当授权结束后，该回调就会被执行。 当授权成功后，请保存该 access_token、expires_in、uid 等信息到
//     * SharedPreferences 中。
//     */
//    class AuthListener implements WeiboAuthListener {
//        private boolean isAlert;
//
//        public AuthListener(boolean isAlert) {
//            this.isAlert = isAlert;
//        }
//
//        @Override
//        public void onComplete(Bundle values) {
//            // 从 Bundle 中解析 Token
//            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
//            if (mAccessToken.isSessionValid()) {
//                // 保存 Token 到 SharedPreferences
//                weiboToken = mAccessToken.getToken();
//                weiboUid = mAccessToken.getUid();
//                if (values.containsKey("userName")) {
//                    screenName = values.getString("userName");
//                }
//                if (isAlert) {
//                    handler.sendEmptyMessage(-1);
//                } else {
//                    handler.sendEmptyMessage(-2);
//                }
//            } else {
//                Toast.makeText(mActivity, "授权失败", Toast.LENGTH_LONG).show();
//                if (callBack != null) callBack.authWeiboError();
//            }
//        }
//
//        @Override
//        public void onCancel() {
//            Toast.makeText(mActivity, "授权取消", Toast.LENGTH_LONG).show();
//            if (callBack != null) callBack.authWeiboError();
//        }
//
//        @Override
//        public void onWeiboException(WeiboException e) {
//            Toast.makeText(mActivity, "授权失败" + e.getMessage(), Toast.LENGTH_LONG).show();
//            if (callBack != null) callBack.authWeiboError();
//        }
//    }
//
//    /**
//     * weibo授权成功回调
//     */
//    public interface AuthWeiboCompleteCallBack {
//        public void authWeiboComplete(String weiboUid, String weiboToken, String screenName);// weibo授权成功回调
//
//        public void getAccountsComplete(String weiboUid, String weiboToken);
//
//        public void bindWeiboAccountsComplete(String weiboUid, String weiboToken, String screenName, String json);// 绑定weibo成功回调
//
//        public void authWeiboError();// 绑定weibo异常
//    }
//
//    /**
//     * 获取weibo绑定账号
//     */
//    public void startGetAccountsWeiboTask() {
//        CommonUtil.cancelAsyncTask(mGetAccountsWeiboTask);
//        mGetAccountsWeiboTask = (GetAccountsWeiboTask) new GetAccountsWeiboTask().executeParams();
//    }
//
//    private class GetAccountsWeiboTask extends BaseAsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            showLoadingProgressDialog(mActivity.getResources().getString(R.string.loading_date_message));
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                parserSocialJSON(TaojinluHttp.getInstance().getAccounts(String.valueOf(user.getUserId())));
//                return "1";
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            dismissProgressDialog();
//            if (result != null) {
//                if (weiboToken == null || "".equals(weiboToken) || "".equals(weiboUid)) {
//                    authorizeToWeibo(true);
//                } else {
//                    callBack.getAccountsComplete(weiboUid, weiboToken);
//                }
//            }
//        }
//
//        /**
//         * 解析用户绑定weibo信息
//         *
//         * @param result
//         */
//        private void parserSocialJSON(String result) {
//            try {
//                JSONObject json = new JSONObject(result);
//                if (hasAndNotNull(json, "accessToken")) {
//                    weiboToken = json.getString("accessToken");
//                    mAccessToken.setToken(weiboToken);
//                }
//                if (hasAndNotNull(json, "sinawb")) {
//                    weiboUid = json.getString("sinawb");
//                }
//                if (weiboToken != null && weiboUid != null && weiboUid.length() > 0)
//                    ShareData.saveWeibo(sharedata, weiboUid, weiboToken);
//            } catch (Exception e) {
//                // TODO: handle exception
//            }
//        }
//    }
//
//    /**
//     * 绑定weibo,并获取微博昵称screen_name
//     */
//    public void startBindWeiboTask(String screenName) {
//        try {
//            if (screenName != null) {
//                if (screenName != null && !"".equals(screenName))
//                    ShareData.saveWeiboScreenName(sharedata, screenName);
//                sendBindWeiboInfoTask(screenName);
//            } else {
//                mUsersAPI = new UsersAPI(mAccessToken);
//                mUsersAPI.show(Long.valueOf(weiboUid), new ShowUserReq());
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    class ShowUserReq implements RequestListener {
//
//        @Override
//        public void onComplete(String arg0) {
//            try {
//                JSONObject data = new JSONObject(arg0);
//                screenName = data.getString("screen_name");
//                if (screenName != null && !"".equals(screenName))
//                    ShareData.saveWeiboScreenName(sharedata, screenName);
//                sendBindWeiboInfoTask(screenName);
//            } catch (JSONException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onError(WeiboException arg0) {
//            handler.sendEmptyMessage(0);// 失败
//        }
//
//        @Override
//        public void onIOException(IOException arg0) {
//            handler.sendEmptyMessage(0);// 失败
//        }
//
//        @Override
//        public void onComplete4binary(ByteArrayOutputStream responseOS) {
//            // TODO Auto-generated method stub
//
//        }
//    }
//
//    public void sendBindWeiboInfoTask(String screenName) {
//        CommonUtil.cancelAsyncTask(mBindWeiboTask);
//        mBindWeiboTask = (BindWeiboTask) new BindWeiboTask(screenName).executeParams();
//    }
//
//    private class BindWeiboTask extends BaseAsyncTask<String, Void, String> {
//        private String screenName;
//
//        public BindWeiboTask(String screenName) {
//            this.screenName = screenName;
//        }
//
//        @Override
//        protected String doInBackground(String... params) {
//            try {
//                //TaojinluHttp.getInstance().bindAccount(String.valueOf(user.getUserId()), CommonConst.LOGIN_TYPE_SINAWB, weiboUid, weiboToken, "", screenName)
////                JSONObject signObj = new JSONObject();
////                signObj.put("account", weiboUid);
////                signObj.put("type", CommonConst.LOGIN_TYPE_SINAWB);
//                return TjrUserHttp.getInstance().addBindAccount(String.valueOf(user.getUserId()), TJrLoginTypeEnum.sinawb.type(), null, weiboUid, weiboToken, screenName);
//            } catch (Exception e) {
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            // TODO Auto-generated method stub
//            super.onPostExecute(result);
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                if (hasAndNotNull(jsonObject, "success")) {
//                    boolean suc = jsonObject.getBoolean("success");
//                    if (suc) ShareData.saveWeibo(sharedata, weiboUid, weiboToken);
//                }
//            } catch (Exception e) {
//            }
//            callBack.bindWeiboAccountsComplete(weiboUid, weiboToken, screenName, result);
//            dismissProgressDialog();
//        }
//    }
//
//    public Oauth2AccessToken getmAccessToken() {
//        return mAccessToken;
//    }
//
//    public void setmAccessToken(Oauth2AccessToken mAccessToken) {
//        this.mAccessToken = mAccessToken;
//    }
//
//    public SsoHandler getmSsoHandler() {
//        return mSsoHandler;
//    }
//
//    public String getWeiboUid() {
//        return weiboUid;
//    }
//
//    /**
//     * 弹出对话框
//     *
//     * @param
//     */
//    public void showDalog() {
//        // 对话框
//        if (alertDialog == null) {
//            // 文本输入框
//            if (screenName == null) screenName = "该";
//            alertDialog = new AlertDialog.Builder(mActivity).setTitle("提示").setMessage(Html.fromHtml("是否把<font color=\"#2593d0\">" + screenName + "</font>微博绑定到本账号?")).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface v, int btn) {
//                    showLoadingProgressDialog(mActivity.getResources().getString(R.string.loading_date_message));
//                    startBindWeiboTask(screenName);
//                }
//            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface v, int btn) {
//                    v.cancel(); // 关闭当前的对话框.
//                    callBack.authWeiboComplete(weiboUid, weiboToken, screenName);
//                    dismissProgressDialog();
//                }
//            }).create();
//        }
//        alertDialog.show();
//    }
//
//    /**
//     * json中是否有该字段,并且该字段不为空
//     *
//     * @param json
//     * @param name 字段名
//     * @return
//     * @throws JSONException
//     */
//    private boolean hasAndNotNull(JSONObject json, String name) throws JSONException {
//        if (json != null && name != null) {
//            return json.has(name) && !json.isNull(name) && //
//                    json.getString(name) != null && !"".equals(json.getString(name));
//        } else return false;
//    }
//
//    private void showLoadingProgressDialog(CharSequence message) {
//        try {
//            if (progressDialog == null) {
//                progressDialog = new ProgressDialog(mActivity);
//                progressDialog.setIndeterminate(true);
//                progressDialog.setOwnerActivity(mActivity);
//            }
//            progressDialog.setMessage(message);
//            progressDialog.show();
//        } catch (Exception e) {
//            Log.e("Exception", "showProgressDialog  " + e.toString());
//        }
//    }
//
//    private void dismissProgressDialog() {
//        if (progressDialog != null) {
//            progressDialog.dismiss();
//        }
//    }
//}
