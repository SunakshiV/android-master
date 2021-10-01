/*
package com.procoin.social.qq;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.procoin.social.common.TjrSocialShareConfig;
import com.procoin.http.base.TaojinluType;
import com.procoin.social.R;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.open.utils.HttpUtils;
import com.tencent.tauth.IRequestListener;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

*/
/**
 * Created by zhengmj on 18-8-10.
 *//*


public class QQ implements TaojinluType{
    private Context context;
    private  Tencent mTencent;
    private UserInfo mInfo;
    private OnMessageCallback callback;

    private static boolean isServerSideLogin = false;

    public QQ(Context context,OnMessageCallback callback){
        mTencent = Tencent.createInstance(TjrSocialShareConfig.APPID_QQ_KEY,context);
        this.callback = callback;
        this.context = context;
    }
    public UserInfo getmInfo(){
        if (mInfo == null) mInfo = new UserInfo(context,mTencent.getQQToken());
        return mInfo;
    }
//    public String getInfo(){
//        return info;
//    }
    IUiListener loginListener = new BaseUiListener() {
        @Override
        protected void doComplete(JSONObject values) {
            Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
//            Log.d("155","values == "+values);
            initOpenidAndToken(values);
            updateUserInfo();
        }
    };
    public  void initOpenidAndToken(JSONObject jsonObject) {
        try {
            if (callback!=null) callback.callback(jsonObject);
            String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
            String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
            String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
            if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
                    && !TextUtils.isEmpty(openId)) {
                mTencent.setAccessToken(token, expires);
                mTencent.setOpenId(openId);
            }
        } catch(Exception e) {
        }
    }
    public class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
//            Log.d("155","qq onComplete");
            if (null == response) {
//                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                return;
            }
            JSONObject jsonResponse = (JSONObject) response;
            if (null != jsonResponse && jsonResponse.length() == 0) {
//                Util.showResultDialog(MainActivity.this, "返回为空", "登录失败");
                return;
            }
//            Util.showResultDialog(MainActivity.this, response.toString(), "登录成功");
            // 有奖分享处理
//            handlePrizeShare();
            doComplete((JSONObject)response);
        }

        protected void doComplete(JSONObject values) {
//            Log.d("155","qq doComplete");
        }

        @Override
        public void onError(UiError e) {
//            Log.d("155","qq onError");
//            Util.toastMessage(MainActivity.this, "onError: " + e.errorDetail);
//            Util.dismissDialog();
        }

        @Override
        public void onCancel() {
//            Log.d("155","qq onCancel");
//            Util.toastMessage(MainActivity.this, "onCancel: ");
//            Util.dismissDialog();
//            if (isServerSideLogin) {
//                isServerSideLogin = false;
//            }
        }
    }
    public class BaseApiListener implements IRequestListener{

        @Override
        public void onComplete(JSONObject jsonObject) {
            if (null != jsonObject && jsonObject.length() == 0) {
                return;
            }
            doComplete(jsonObject);
        }
        protected void doComplete(JSONObject values){

        }
        @Override
        public void onIOException(IOException e) {

        }

        @Override
        public void onMalformedURLException(MalformedURLException e) {

        }

        @Override
        public void onJSONException(JSONException e) {

        }

        @Override
        public void onConnectTimeoutException(ConnectTimeoutException e) {

        }

        @Override
        public void onSocketTimeoutException(SocketTimeoutException e) {

        }

        @Override
        public void onNetworkUnavailableException(HttpUtils.NetworkUnavailableException e) {

        }

        @Override
        public void onHttpStatusException(HttpUtils.HttpStatusException e) {

        }

        @Override
        public void onUnknowException(Exception e) {

        }
    }
//     Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            if (msg.what == 0) {
//                JSONObject response = (JSONObject) msg.obj;
////                if (callback!=null) callback.callback(response);
////                if (response.has("nickname")) {
////                    try {
////                        mUserInfo.setVisibility(android.view.View.VISIBLE);
////                        mUserInfo.setText(response.getString("nickname"));
////                    } catch (JSONException e) {
////                        e.printStackTrace();
////                    }
////                }
//            }
////            else if(msg.what == 1){
////                Bitmap bitmap = (Bitmap)msg.obj;
////                mUserLogo.setImageBitmap(bitmap);
////                mUserLogo.setVisibility(android.view.View.VISIBLE);
////            }
//        }
//
//    };
    private void updateUserInfo() {
        if (mTencent != null && mTencent.isSessionValid()) {
            IUiListener listener = new IUiListener() {

                @Override
                public void onError(UiError e) {

                }

                @Override
                public void onComplete(final Object response) {
//                    Log.d("155","response == "+response.toString());
                    Message msg = new Message();
                    msg.obj = response;
                    msg.what = 0;
//                    mHandler.sendMessage(msg);
                    new Thread(){

                        @Override
                        public void run() {
                            JSONObject json = (JSONObject)response;
                            if(json.has("figureurl")){
//                                Bitmap bitmap = null;
//                                try {
//                                    bitmap = Util.getbitmap(json.getString("figureurl_qq_2"));
//                                } catch (JSONException e) {
//
//                                }
//                                Message msg = new Message();
//                                msg.obj = bitmap;
//                                msg.what = 1;
//                                mHandler.sendMessage(msg);
                            }
                        }

                    }.start();
                }

                @Override
                public void onCancel() {

                }
            };
            mInfo = new UserInfo(context, mTencent.getQQToken());
            mInfo.getUserInfo(listener);

        }
    }
    public void share(Bundle bundle,IUiListener listener){
        mTencent.shareToQQ((Activity) context,bundle,listener);
    }
    public void login(){
        if (!mTencent.isSessionValid()) {
//            Log.d("155","result == "+(mTencent.login((Activity) context, "all", loginListener)));
            mTencent.login((Activity) context, "all", loginListener);
            isServerSideLogin = false;
        }
        else {
//            if (isServerSideLogin) { // Server-Side 模式的登陆, 先退出，再进行SSO登陆
//                mTencent.logout(context);
//                mTencent.login((Activity) context, "all", loginListener);
//                isServerSideLogin = false;
//                Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
//                return;
//            }
            mTencent.logout(context);
            mTencent.login((Activity) context, "all", loginListener);
//            updateUserInfo();
//            updateLoginButton();
        }
    }
    public ShareBuilder getShareBuilder(){
        return new ShareBuilder();
    }
    public  class ShareBuilder{
        private Bundle params;
        private String targetUrl;
        private int type;
        private ShareBuilder(){
            params = new Bundle();
            params.putString(QQShare.SHARE_TO_QQ_APP_NAME,context.getString(R.string.app_name));
            params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        }
        public ShareBuilder setTitle(String title){
            if (title.length()>30){
                title = title.substring(0,30);
            }
            params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
            return this;
        }
        public ShareBuilder setContent(String content){
            if (content.length()>40){
                content = content.substring(0,40);
            }
            params.putString(QQShare.SHARE_TO_QQ_SUMMARY, content);
            return this;
        }
        public ShareBuilder setTargetUrl(String url){
            targetUrl = url;
            params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, url);
            return this;
        }
        public ShareBuilder setImgUrl(String url){
            params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, url);
            return this;
        }
        public ShareBuilder isShareToZone(boolean toZone){
            if (toZone){
                type = QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN;
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
            }else {
                type = QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE;
                params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
            }
            return this;
        }
        public void share(@NonNull IUiListener listener){
            try {
                Log.d("154","isMainThread == "+(Looper.getMainLooper() == Looper.myLooper()));
                if (TextUtils.isEmpty(targetUrl)) throw new RuntimeException("TargetUrl is needed.");
                if (type == 0) throw new RuntimeException("Please specify a share type.");
                mTencent.shareToQQ((Activity) context,params,listener);
            }catch (RuntimeException e){
//                Noti
            }
        }
    }
    public interface OnMessageCallback{
        void callback(JSONObject result);
    }

}
*/
