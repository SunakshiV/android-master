/*
package com.procoin.social.qq;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;

import com.procoin.http.common.TJrLoginTypeEnum;
import com.procoin.http.model.User;
import com.procoin.social.common.TjrSocialShareConfig;
import com.procoin.social.util.CommonUtil;
import com.procoin.task.BaseAsyncTask;
import com.tencent.connect.UserInfo;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class AuthQQ {
    private AuthQQCompleteCallBack callBack;
    private User user;
    private Activity mActivity;
    private Tencent mTencent;
    private String qqToken;
    private String qqUid;
    private String nickname;
//    private SharedPreferences sharedata;
    private BindQQInfoTask mBindQQInfoTask;
    private int loginCount;
    private ProgressDialog progressDialog;
    private AlertDialog alertDialog; // 弹出对话框
    private UserInfo mInfo = null;//新SDK获取用户信息的类
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0://
                    dismissProgressDialog();
                    CommonUtil.showToast(mActivity, "发送失败", Gravity.BOTTOM);
                    break;
                case 1://
                    dismissProgressDialog();
                    showLoadingProgressDialog("请求中,请稍等...");
                    startgetQQnameAsync();
                    break;
                case -1:// 直接绑定QQ
                    // showDalog();
                    sendBindQQInfoTask(nickname);
                    break;
                case -2://
                    dismissProgressDialog();
                    callBack.authQQError();
                    break;
                default:
                    break;
            }
        }

        ;
    };

    public AuthQQ(Activity activity, User user, AuthQQCompleteCallBack callBack) {
        this.mActivity = activity;
        this.user = user;
        loginCount = 0;
        this.callBack = callBack;
//        sharedata = ShareData.getUserSharedPreferencesForSocial(mActivity);
        mTencent = Tencent.createInstance(TjrSocialShareConfig.APPID_QQ_KEY, mActivity);
    }
    public void initUserInfo(){
        mInfo = new UserInfo(mActivity,mTencent.getQQToken());
    }
    public UserInfo getmInfo(){
        return mInfo;
    }
    */
/**
     * @param activity
     * @param user
     * @param appId
     * @param mainPackage
     * @param callBack
     *//*

    public AuthQQ(Activity activity, String appId, String mainPackage, User user, AuthQQCompleteCallBack callBack) {
        this.mActivity = activity;
        this.user = user;
        loginCount = 0;
        this.callBack = callBack;
        if (appId == null) appId = TjrSocialShareConfig.APPID_QQ_KEY;
        if (mainPackage == null) mainPackage = "com.cropyme";// 没有时默认
//        sharedata = ShareData.getUserSharedPreferencesForSocial(mActivity, mainPackage);
        mTencent = Tencent.createInstance(appId, mActivity);
    }

    */
/**
     * QQ授权成功回调
     *//*

    public interface AuthQQCompleteCallBack {
        public void authQQComplete(String qqUid, String qqToken, String screenName);// QQ授权成功回调

        public void bindQQAccountsComplete(String qqUid, String qqToken, String screenName, String json);// 绑定QQ成功回调

        public void authQQError();// 绑定QQ异常
    }

    public void authorizeToQQ() {
        if (!mTencent.isSessionValid()) {
            IUiListener listener = new BaseUiListener() {
                protected void doComplete(JSONObject values) {
                    try {
                        qqToken = values.getString("access_token");
                        qqUid = values.getString("openid");
//                        ShareData.saveQQ(sharedata, qqUid, qqToken);
                        loginCount++;
                        handler.sendEmptyMessage(1);
                    } catch (Exception e) {
                        handler.sendEmptyMessage(-2);
                    }
                }
            };
            mTencent.login(mActivity, "all", listener);
        } else {
            qqToken = mTencent.getAccessToken();
            qqUid = mTencent.getOpenId();
//            ShareData.saveQQ(sharedata, qqUid, qqToken);
            loginCount++;
            handler.sendEmptyMessage(1);
        }
    }

    private class BaseUiListener implements IUiListener {

//        @Override
//        public void onComplete(JSONObject response) {//最新的sdk已经不支持这个方法了,代替的方法在下一个
//            doComplete(response);
//        }
        @Override
        public void onComplete(Object o) { //根据最新的Demo改的
            doComplete((JSONObject) o);
        }
        protected void doComplete(JSONObject values) {

        }


        @Override
        public void onError(UiError e) {
            handler.sendEmptyMessage(-2);
            CommonUtil.showToast(mActivity, "腾讯QQ认证失败!", Gravity.BOTTOM);
        }

        @Override
        public void onCancel() {
            handler.sendEmptyMessage(-2);
            CommonUtil.showToast(mActivity, "腾讯QQ认证取消!", Gravity.BOTTOM);
        }
    }

    private void sendBindQQInfoTask(String screenName) {
        if (qqUid == null || qqToken == null) {
            handler.sendEmptyMessage(-2);
            CommonUtil.showToast(mActivity, "腾讯QQ认证失败!", Gravity.BOTTOM);
            return;
        }
        CommonUtil.cancelAsyncTask(mBindQQInfoTask);
        mBindQQInfoTask = (BindQQInfoTask) new BindQQInfoTask(screenName).executeParams();
    }

    private class BindQQInfoTask extends BaseAsyncTask<String, Void, String> {
        private String screenName;

        public BindQQInfoTask(String screenName) {
            this.screenName = screenName;
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //TaojinluHttp.getInstance().bindAccount(String.valueOf(user.getUserId()), CommonConst.LOGIN_TYPE_QQ, qqUid, qqToken, "", screenName)
                JSONObject signObj = new JSONObject();
                signObj.put("account", qqUid);
                signObj.put("type", TJrLoginTypeEnum.qq.type());//CommonConst.LOGIN_TYPE_QQ
//                return TjrUserHttp.getInstance().addBindAccount(String.valueOf(user.getUserId()), TJrLoginTypeEnum.qq.type(), null, qqUid, qqToken, screenName);
                return "";
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            callBack.bindQQAccountsComplete(qqUid, qqToken, screenName, result);
            dismissProgressDialog();
        }
    }

    */
/**
     * 异步取
     *//*

    public void startgetQQnameAsyncHandler() {
        handler.sendEmptyMessage(1);
    }

    private void startgetQQnameAsync() {
//        mTencent.requestAsync(Constants.GRAPH_USER_INFO, null, Constants.HTTP_GET, new BaseApiListener("get_user_info"), null);//在新SDK里已经不用这个了
//        mInfo.getUserInfo(new BaseUiListener("get_simple_userinfo"));
    }

    private class BaseApiListener implements IRequestListener {//其实也没啥改动，就是接口的参数变少了，内容还是一样
        private String mScope = "all";

        public BaseApiListener(String scope) {
            mScope = scope;
        }

//        @Override
//        public void onComplete(final JSONObject response, Object state) {
//            doComplete(response, state);
//        }
        @Override
        public void onComplete(JSONObject jsonObject) {
            doComplete(jsonObject);
        }


        protected void doComplete(JSONObject response) {
            try {
                int ret = response.getInt("ret");
                if (ret == 100030) {
                    if (loginCount < 2) {
                        Runnable r = new Runnable() {
                            public void run() {
                                mTencent.reAuth(mActivity, mScope, new BaseUiListener());
                            }
                        };
                        mActivity.runOnUiThread(r);
                    }
                } else {
                    nickname = response.getString("nickname");
//                    ShareData.saveQQScreenName(sharedata, nickname);
                    // sendBindQQInfoTask(nickname);
                    handler.sendEmptyMessage(-1);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                handler.sendEmptyMessage(-2);
            }

        }

//        @Override
//        public void onIOException(final IOException e, Object state) {
//            // showResult("IRequestListener.onIOException:", e.getMessage());
//            handler.sendEmptyMessage(-2);
//        }
        @Override
        public void onIOException(IOException e) {
            handler.sendEmptyMessage(-2);
        }

        @Override
        public void onMalformedURLException(final MalformedURLException e) {
            // showResult("IRequestListener.onMalformedURLException",
            // e.toString());
            handler.sendEmptyMessage(-2);
        }

        @Override
        public void onJSONException(final JSONException e) {
            // showResult("IRequestListener.onJSONException:", e.getMessage());
            handler.sendEmptyMessage(-2);
        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException arg0) {
            // showResult("IRequestListener.onConnectTimeoutException:",
            // arg0.getMessage());
            handler.sendEmptyMessage(-2);

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException arg0) {
            // showResult("IRequestListener.SocketTimeoutException:",
            // arg0.getMessage());
            handler.sendEmptyMessage(-2);
        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {
            handler.sendEmptyMessage(-2);
        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e) {
            handler.sendEmptyMessage(-2);
        }

        @Override
        public void onUnknowException(Exception arg0) {
            handler.sendEmptyMessage(-2);
        }

//        @Override
//        public void onHttpStatusException(HttpStatusException arg0, Object arg1) {
//            // showResult("IRequestListener.HttpStatusException:",
//            // arg0.getMessage());
//            handler.sendEmptyMessage(-2);
//        }

//        @Override
//        public void onNetworkUnavailableException(NetworkUnavailableException arg0, Object arg1) {
//            // showResult("IRequestListener.onNetworkUnavailableException:",
//            // arg0.getMessage());
//            handler.sendEmptyMessage(-2);
//        }

    }

    public Tencent getmTencent() {
        return mTencent;
    }

    */
/**
     * 弹出对话框
     *
     * @param
     *//*

    public void showDalog() {
        // 对话框
        if (alertDialog == null) {
            // 文本输入框
            if (nickname == null) nickname = "该";
            alertDialog = new AlertDialog.Builder(mActivity).setTitle("提示").setMessage(Html.fromHtml("是否把<font color=\"#2593d0\">" + nickname + "</font>QQ绑定到本账号?")).setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface v, int btn) {
                    showLoadingProgressDialog("请求中,请稍等...");
                    sendBindQQInfoTask(nickname);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface v, int btn) {
                    v.cancel(); // 关闭当前的对话框.
                    callBack.authQQComplete(qqUid, qqToken, nickname);
                    dismissProgressDialog();
                }
            }).create();
        }
        alertDialog.show();
    }

    private void showLoadingProgressDialog(CharSequence message) {
        try {
            if (progressDialog == null) {
                progressDialog = new ProgressDialog(mActivity);
                progressDialog.setIndeterminate(true);
                progressDialog.setOwnerActivity(mActivity);
            }
            progressDialog.setMessage(message);
            progressDialog.show();
        } catch (Exception e) {
            Log.e("Exception", "showProgressDialog  " + e.toString());
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    public String getQqToken() {
        return qqToken;
    }

    public void setQqToken(String qqToken) {
        this.qqToken = qqToken;
    }

    public String getQqUid() {
        return qqUid;
    }

    public void setQqUid(String qqUid) {
        this.qqUid = qqUid;
    }

}
*/
