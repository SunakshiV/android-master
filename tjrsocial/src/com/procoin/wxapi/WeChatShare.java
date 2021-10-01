//package com.procoin.wxapi;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.util.Log;
//import android.view.Gravity;
//
//import com.procoin.social.TjrSocialMTAUtil;
//import com.procoin.social.common.TjrSocialShareConfig;
//import com.procoin.social.util.CommonUtil;
//import com.procoin.http.error.TaojinluException;
////import com.cropyme.social.R;
//import com.procoin.social.util.ImageViewUtil;
//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
//import com.tencent.mm.opensdk.modelmsg.WXImageObject;
//import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
//import com.tencent.mm.opensdk.modelmsg.WXTextObject;
//import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
//import com.tencent.mm.opensdk.modelpay.PayReq;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//
///**
// * weibotype 1 纯文字 2 文件 ，文章 ，文字 3 文字 + 2类型 + 投票 ctype 1 文字 2 文件 3 报纸 retype
// * paper 报纸 stock f10 timeline 时间线
// *
// * @author zhengmj
// */
//public class WeChatShare {
//
//    private final String wxAppSecret = "bfffb666c72eb208cd5d43f311ffd067\n";//667980edfb01fbbf7dd248239d1a4b70
//    private final String wxAuthUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
//    private final String wxUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
//    private final String wxState = "bee_wx_auth_login";
//    private final int TIMELINE_SUPPORTED_VERSION = 0x21020001;
//    private Activity activity;
//    private boolean isTimeline;//
//    private IWXAPI api;
//    private static final int THUMB_SIZE = 100;// 图片的大小
//    private String appId;
//    public WeChatShare(Activity activity) {
//        this.activity = activity;
//        appId = TjrSocialShareConfig.APPID_WECHAT_KEY;
//        api = WXAPIFactory.createWXAPI(activity, TjrSocialShareConfig.APPID_WECHAT_KEY, true);
////        Log.d("155","api.registerApp(TjrSocialShareConfig.APPID_WECHAT_KEY) == "+api.registerApp(TjrSocialShareConfig.APPID_WECHAT_KEY));
////        api.registerApp(TjrSocialShareConfig.APPID_WECHAT_KEY);
//    }
//
//    public WeChatShare(Activity activity, String appId) {
//        this.activity = activity;
//        if (appId == null) appId = TjrSocialShareConfig.APPID_WECHAT_KEY;
//        api = WXAPIFactory.createWXAPI(activity, appId, true);// 这个是原来做错的，第二个参数没用，防止原来的程序错误，注册app到微信
//        api.registerApp(appId);
//    }
//
//    public boolean isTimeline() {
//        return isTimeline;
//    }
//
//    /**
//     * @param isTimeline true 代表发送朋友圈 ，false 发送到会话
//     */
//    public void setTimeline(boolean isTimeline) {
//        this.isTimeline = isTimeline;
//    }
//
//    /**
//     * 微信授权进行支付
//     *
//     * @return
//     */
//    public boolean sendAuthPay(String prepayId, String packageValue, String partnerid, String nonceStr, String timestamp, String sign) {
//        if (!isSupportAPI()) return false;
//        if (api != null) {
//            final PayReq req = new PayReq();
//            req.appId = appId;
//            req.nonceStr = nonceStr;
//            req.packageValue = packageValue;// "Sign=WXPay"
//            req.partnerId = partnerid;
//            req.prepayId = prepayId;
//            req.timeStamp = timestamp;
//            req.sign = sign;
//            api.sendReq(req);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * 微信授权请求
//     *
//     * @return
//     */
//    public boolean sendAuthReq() {
////        Log.d("155","Excute sendAuthReq");
//        if (!isSupportAPI()) return false;
//        if (api != null) {
//            final SendAuth.Req req = new SendAuth.Req();
//            req.scope = "snsapi_userinfo";
//            req.state = wxState;
//            return api.sendReq(req);
////            return true;
//        }
//        return false;
//    }
//    public String getAuthUrl(){
//        return wxAuthUrl;
//    }
//    public String getAppId(){
//        return appId;
//    }
//    public String getAppSercret(){
//        return wxAppSecret;
//    }
//    public String getGrantType(){
//        return "authorization_code";
//    }
//    /**
//     * 微信登录授权获取 access_token，openid
//     *
//     * @param code
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
////    public String sendAccessTokenUrl(String code) throws TaojinluException, IOException {
////        Log.d("155","Excute sendAccessTokenUrl");
////        Call<ResponseBody> call = BeebarHttpServiceManager.getInstance().getBeebarService().getWechatAuthor(wxAuthUrl,appId,wxAppSecret,code,"authorization_code");
////        return TaojinluHttp.getInstance().sendWXAccessTokenUrl(wxAuthUrl, appId, wxAppSecret, code);
////    }
//
//    /**
//     * 获取用户信息
//     *
//     * @param access_token
//     * @param openid
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
////    public String getWeiXinUserInfo(String access_token, String openid) throws TaojinluException, IOException {
////        Log.d("155","getWeiXinUserInfo");
////        return TaojinluHttp.getInstance().getWXUserInfo(wxUserInfoUrl, access_token, openid);
////    }
//
//    /**
//     * 微信登录授权
//     *
//     * @param text
//     * @return
//     */
//    public boolean SendReqtext(String text) {
//        if (!isSupportAPI()) return false;
//        if (text != null && api != null) {
//            WXTextObject textob = new WXTextObject();
//            textob.text = text;
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = textob;
//            msg.description = text;
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("taojin_text");
//            req.message = msg;
//            req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//
//			/*
//             * 重复了一次 WXTextObject textObj = new WXTextObject(); textObj.text =
//			 * text; // 调用api接口发送数据到微信 api.sendReq(req);
//			 */
//            return true;
//        }
//        return false;
//    }
//
//    public IWXAPI getWXAPI() {
//        return api;
//    }
//
//    public boolean SendReqImage(String filepath) {
//        if (!isSupportAPI()) return false;
//        if (filepath != null && api != null) {
//            String path = filepath;
//            File file = new File(path);
//            if (!file.exists()) {
////                String tip = activity.getString(R.string.send_img_file_not_exist);
////                Toast.makeText(activity, tip + " path = " + path, Toast.LENGTH_LONG).show();
//                return false;
//            }
//
//            WXImageObject imgObj = new WXImageObject();
//            imgObj.setImagePath(path);
//
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = imgObj;
//
//            Bitmap bmp = BitmapFactory.decodeFile(path);
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//            bmp.recycle();
//            msg.thumbData = ImageViewUtil.bmpToByteArray(thumbBmp, true);
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("taojin_img");
//            req.message = msg;
//            req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//            return true;
//        }
//        return false;
//    }
//
//    public boolean SendReqBitmap(Bitmap bmp) {
//        if (!isSupportAPI()) return false;
//        if (bmp != null && api != null) {
//            WXImageObject imgObj = new WXImageObject(bmp);
//
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = imgObj;
//
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//            // bmp.recycle();
//            msg.thumbData = ImageViewUtil.bmpToByteArray(thumbBmp, true); // 设置缩略图
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("taojin_img");
//            req.message = msg;
//            req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//            return true;
//        }
//        return false;
//    }
//
//    /**
//     * @param URL         文章的URL
//     * @param title       文章的标题
//     * @param description 文章的描述
//     * @param thumb       相关的图片
//     * @return
//     */
//    public boolean SendReqURL(String URL, String title, String description, Bitmap thumb) {
//        if (!isSupportAPI()) return false;
//        if (URL != null && api != null) {
//            WXWebpageObject webpage = new WXWebpageObject();
//            webpage.webpageUrl = URL;
//            WXMediaMessage msg = new WXMediaMessage(webpage);
//            msg.title = title;
//            msg.description = description;
//            if (thumb != null) {
//                Log.d("wechatshare", "thumb=="+thumb.getRowBytes());
//                Bitmap thumbBmp = Bitmap.createScaledBitmap(CommonUtil.compressImageWechatShare(thumb), THUMB_SIZE, THUMB_SIZE, true);
//                Log.d("wechatshare", "thumbBmp=="+thumbBmp.getRowBytes());
//                msg.thumbData = ImageViewUtil.bmpToByteArray(thumbBmp, true);
//                Log.d("wechatshare", "msg.thumbData=="+msg.thumbData.length);
//            }
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction(URL);//"webpage"
//            req.message = msg;
//            req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//            //微信分享MTA统计统一放在这里
//            if(isTimeline){
//                TjrSocialMTAUtil.trackCustomKVEvent(activity, TjrSocialMTAUtil.PROP_CLICKTYPE,"发送到朋友圈", TjrSocialMTAUtil.MTAWeiXinShareClick);
//            }else{
//                TjrSocialMTAUtil.trackCustomKVEvent(activity, TjrSocialMTAUtil.PROP_CLICKTYPE,"发送到微信会话", TjrSocialMTAUtil.MTAWeiXinShareClick);
//            }
//
//            return true;
//        }
//        return false;
//    }
//
//    public boolean SendReqImageURL(String url) throws MalformedURLException, IOException {
//        if (!isSupportAPI()) return false;
//        if (url != null && api != null) {
//            WXImageObject imgObj = new WXImageObject();
////            imgObj.imageUrl = url;
//
//            WXMediaMessage msg = new WXMediaMessage();
//            msg.mediaObject = imgObj;
//
//            Bitmap bmp = BitmapFactory.decodeStream(new URL(url).openStream());
//            Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//            bmp.recycle();
//            msg.thumbData = ImageViewUtil.bmpToByteArray(thumbBmp, true);
//
//            SendMessageToWX.Req req = new SendMessageToWX.Req();
//            req.transaction = buildTransaction("img");
//            req.message = msg;
//            req.scene = isTimeline ? SendMessageToWX.Req.WXSceneTimeline : SendMessageToWX.Req.WXSceneSession;
//            api.sendReq(req);
//            return true;
//        }
//        return false;
//    }
//
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type;// System.currentTimeMillis()
//    }
//
//    /**
//     * 判断微信是否符合要求
//     *
//     * @return
//     */
//    public boolean isSupportAPI() {
//
//        if (!api.isWXAppInstalled()) {
//            CommonUtil.showToast(activity, "没有安装微信", Gravity.BOTTOM);
//            return false;
//        }
////        if (Build.VERSION.SDK_INT<api.getWXAppSupportAPI()){
////            Log.d("154","wxSupportApi == "+api.getWXAppSupportAPI());
////            CommonUtil.showToast(activity, "该版本不支持微信分享，请更新", Gravity.BOTTOM);
////            return false;
////        }
////        if (!api.isWXAppSupportAPI()) {
////
////            CommonUtil.showToast(activity, "该版本不支持微信分享，请更新", Gravity.BOTTOM);
////            return false;
////        }
//
//        int wxSdkVersion = api.getWXAppSupportAPI();
//        if (isTimeline) {
//            if (wxSdkVersion < TIMELINE_SUPPORTED_VERSION) {
//                CommonUtil.showToast(activity, "该版本不支持微信分享到好友圈，请更新", Gravity.BOTTOM);
//                return false;
//            }
//        }
//        CommonUtil.LogLa(2, "isSupportAPI " + api.isWXAppInstalled() + " " + (wxSdkVersion >= TIMELINE_SUPPORTED_VERSION));
//        return true;
//    }
////    public ShareBuilder getShareBuilder(){
////        return new ShareBuilder();
////    }
////    public class ShareBuilder{
////        private WXWebpageObject wxWebpageObject;
////        private ShareBuilder(){
////            wxWebpageObject = new WXWebpageObject();
////        }
////        private ShareBuilder setUrl(String url){
////            wxWebpageObject.webpageUrl = url;
////            return this;
////        }
////        private
////    }
//}
