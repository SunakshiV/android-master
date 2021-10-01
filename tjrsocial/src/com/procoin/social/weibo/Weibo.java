//package com.procoin.social.weibo;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//
//import com.sina.weibo.sdk.WbSdk;
//import com.sina.weibo.sdk.api.ImageObject;
//import com.sina.weibo.sdk.api.TextObject;
//import com.sina.weibo.sdk.api.WeiboMultiMessage;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.auth.WbAuthListener;
//import com.sina.weibo.sdk.auth.sso.SsoHandler;
//import com.sina.weibo.sdk.share.WbShareHandler;
//import com.procoin.http.error.TaojinluException;
//import com.procoin.social.common.TjrSocialShareConfig;
//
//import java.io.IOException;
//
///**
// * Created by zhengmj on 18-8-10.
// */
//
//public class Weibo {
//
//    private static final String GETWEIBOUSERINFO = "https://api.weibo.com/2/users/show.json";//获取微博用户信息
//    /**
//     //     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
//     //     */
////    private Oauth2AccessToken mAccessToken;
//    /**
//     * 注意：SsoHandler 仅当 SDK 支持 SSO 时有效
//     */
//    private SsoHandler mSsoHandler;
//    private WbShareHandler shareHandler;
//    private Activity activity;
//
//    public Weibo(Activity activity) {
//        WbSdk.install(activity, new AuthInfo(activity, TjrSocialShareConfig.WEIBO_APP_KEY, TjrSocialShareConfig.REDIRECT_URL, TjrSocialShareConfig.WEIBO_SCOPE));
//        mSsoHandler = new SsoHandler(activity);
//        shareHandler = new WbShareHandler(activity);
//        shareHandler.registerApp();
//    }
//
//    public SsoHandler getmSsoHandler() {
//        return mSsoHandler;
//    }
//
//    public WbShareHandler getShareHandler() {
//        return shareHandler;
//    }
//
//    /**
//     * 授权
//     *
//     * @param wbAuthListener
//     */
//    public void authorize(WbAuthListener wbAuthListener) {
//        mSsoHandler.authorize(wbAuthListener);
//    }
//
//    /**
//     * 获取微博用户信息
//     *
//     * @param access_token
//     * @param uid
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
////    public String getWeiboUserInfo(String access_token, String uid) throws TaojinluException, IOException {
////        return BeebarHttp.getInstance().getWeiboUserInfo(GETWEIBOUSERINFO, access_token, uid);
////    }
//
//
//    /**
//     *
//     * 注意:demo里面是说分享成功后回调onNewIntent,结果并没有，而是回调了onActivityResult，可能新版已经改成回调onActivityResult(demo里面用的是4.1，我们用的是4.3)
//     *
//     * 分享普通文本
//     *
//     * @param shareText
//     * @param title
//     * @param actionUrl  响应的url
//     */
//    public void shareText(String shareText, String title, String actionUrl,Bitmap bitmap) {
//
//        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
//        TextObject textObject = new TextObject();
//        textObject.text = shareText;
//        textObject.title = title;
//        textObject.actionUrl = actionUrl;
//        weiboMessage.textObject = textObject;
//        if(bitmap!=null){
//            ImageObject imageObject = new ImageObject();
//            imageObject.setImageObject(bitmap);
//            weiboMessage.imageObject=imageObject;
//        }
//        shareHandler.shareMessage(weiboMessage, false);
//    }
//
//
//
//}
