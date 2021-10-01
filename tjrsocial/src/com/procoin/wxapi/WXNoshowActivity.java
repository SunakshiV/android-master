//package com.procoin.wxapi;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//
//import com.procoin.social.common.TjrSocialShareConfig;
//import com.procoin.social.util.CommonUtil;
//import com.procoin.social.baseui.AbstractBaseActivity;
//
///**
// * 這個類是給不需要显示页面的时候进行调用的，直接发送， 目前只有新聞類型就是只有網頁連接的
// *
// * @author zhengmj
// */
//public class WXNoshowActivity extends AbstractBaseActivity {
//
//    private String textContent; // 这个是文字
//    private boolean timeline;// 朋友圈还是会话
//    private String texttitle; // 标题
//    private Bitmap icon; // 组件图标
//    private WeChatShare weChatShare;
//    private String wechatUrl;// 發送的網頁地址
//    private String appId;//分享的id
//
//    @Override
//    public void goback() {
//        CommonUtil.pageJump(this, null, true, true);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // if (this.getIntent() != null) {
//        getData(this.getIntent().getExtras());
//        // } else {
//        // goback();
//        // }
//    }
//
//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        getData(intent.getExtras());
//    }
//
//    /**
//     * @param mbundle
//     */
//    public void getData(Bundle mbundle) {
//        if (mbundle != null) {
//            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT))
//                textContent = mbundle.getString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT);
//            if (mbundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_TIMELINE))
//                timeline = mbundle.getBoolean(TjrSocialShareConfig.QQ_WECHAT_TIMELINE);// 微信的会话和朋友圈
//            if (mbundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE))
//                texttitle = mbundle.getString(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE);// 微信分享的标题
//            if (mbundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_ICON))
//                icon = CommonUtil.BytesToBitmap(mbundle.getByteArray(TjrSocialShareConfig.QQ_WECHAT_ICON));// 是否发送图标
//            if (mbundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_SENDURL))
//                wechatUrl = mbundle.getString(TjrSocialShareConfig.QQ_WECHAT_SENDURL);// 发送的Url
//            if (mbundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID))
//                appId = mbundle.getString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID);// 发送的Url
//
//        }
//        if (weChatShare == null) {
//            weChatShare = new WeChatShare(this, appId);
//        }
//        // textContent = "content";
//        // texttitle = "title";
//        // icon = BitmapFactory.decodeResource(this.getResources(),
//        // R.drawable.ic_launcher);
//        // wechatUrl = "http://www.taojinroad.com";
//        weChatShare.setTimeline(timeline);
//        CommonUtil.LogLa(2, "WXNoshowActivity timeline" + timeline);
//        CommonUtil.LogLa(2, " is" + weChatShare.SendReqURL(wechatUrl, texttitle, textContent, icon));
//        goback();
//    }
//}
