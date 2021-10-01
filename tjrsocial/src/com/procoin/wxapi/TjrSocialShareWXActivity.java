//package com.cropyme.wxapi;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.os.Bundle;
//import android.text.TextUtils;
//
//import com.cropyme.http.TaojinluHttp;
//import com.cropyme.http.resource.BaseRemoteResourceManager;
//import com.cropyme.http.util.NotificationsUtil;
//import com.cropyme.social.R;
//import TjrSocialMTAUtil;
//import AbstractBaseActivity;
//import TjrSocialShareConfig;
//import ShareUI;
//import ShareUI.ShareUICallBack;
//import CommonUtil;
//import com.cropyme.task.BaseAsyncTask;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import java.io.File;
//
//public class TjrSocialShareWXActivity extends AbstractBaseActivity {
//    private AddWeiboTask mAddWeiboTask;
//    private String fileName, fileUrl;
//    private String jsonStr; // 分享链接json数据格式
//    // 如报纸组件:{"relType":"paper","relId":"195272"}
//    private Bundle mBundle;// 页面传递进来的
//    private ShareUI shareUI;
//    // private String textContent; 这个是文字
//    private boolean timeline;// 朋友圈还是会话
//    private String texttitle; // 标题
//    private Bitmap icon; // 组件图标
//    private String weiboNormalUrl;// 从后台获取url
//    private String styleType;
//    private String appId;// 分享id
//    private WeChatShare weChatShare;
//    //    private AddWeChatTask addWeChatTask;
//    private BaseRemoteResourceManager mageRemoteResourceManager;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        // TODO Auto-generated method stub
//        super.onCreate(savedInstanceState);
//        mageRemoteResourceManager = new BaseRemoteResourceManager("chat");
//        TjrSocialMTAUtil.trackCustomKVEvent(this, TjrSocialMTAUtil.PROP_CLICKTYPE, TjrSocialMTAUtil.PROPVAlUE_CLICKTYPE + "_wx", TjrSocialMTAUtil.EVENT_MOREOPTIONSBUTTON);
//        if (this.getIntent() != null) mBundle = this.getIntent().getExtras();
//        // 1 为k线角斗场分享过来的，2
//        if (mBundle != null)
//            styleType = mBundle.getString(TjrSocialShareConfig.KEY_EXTRAS_STYLETYPE);
//        shareUI = new ShareUI(this, styleType);
//        shareUI.SetTopText("分享到微信");
//        shareUI.setAt(false);
//        shareUI.setIsface(false);
//        shareUI.setOtherImg(false);
//        // shareUI.setQuestion(true);
//
//        if (mBundle != null) {
//            if (mBundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR))
//                jsonStr = mBundle.getString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR);
//            if (mBundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_TIMELINE))
//                timeline = mBundle.getBoolean(TjrSocialShareConfig.QQ_WECHAT_TIMELINE);// 微信的会话和朋友圈
//            if (mBundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE))
//                texttitle = mBundle.getString(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE);// 微信分享的标题
//            if (mBundle.containsKey(TjrSocialShareConfig.QQ_WECHAT_ICON))
//                icon = CommonUtil.BytesToBitmap(mBundle.getByteArray(TjrSocialShareConfig.QQ_WECHAT_ICON));// 是否发送图标
//            if (mBundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID))
//                appId = mBundle.getString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID);// 发送的Url
//            // if
//            // (mBundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR))
//            // jsonStr =
//            // mBundle.getString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR);
//            shareUI.setQuestion(mBundle.getBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISQUESTION, true));
//
//            // shareUI.setOtherImg(mBundle.getBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISOTHEARIMAGE,
//            // true));
//            shareUI.getData(mBundle);
//            if (!timeline) {
//                shareUI.SetTopText("转发给微信好友");
//            } else {
//                shareUI.SetTopText("转发到微信朋友圈");
//            }
//
//        } else {
//            shareUI.getData(new Bundle());
//        }
//        shareUI.setCallBack(new ShareUICallBack() {
//            @Override
//            public void sendWeibo(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option) {
//
//                if (!TextUtils.isEmpty(styleType)) {
////                    startWeiChatTask("weixin-klinegame", text, bmp);
//                } else {
//                    startAddWeiboTask(weiboType, text, bmp, voteTitle, voteType, option);
//                }
////                switch (styleId) {
////                    case 1:
////                        // CommonUtil.LogLa(2, "shareUI weixin-klinegame start");
////                        startWeiChatTask("weixin-klinegame", text, bmp);
////                        break;
////
////                    default:
////                        // CommonUtil.LogLa(2, "startAddWeiboTask start");
////                        startAddWeiboTask(weiboType, text, bmp, voteTitle, voteType, option);
////                        break;
////                }
//                // 向服务器发送内容，并获取url相应的id值
//
//                // 在发送之前要判断用户是否绑定weibo,如果没有绑定weibo就走绑定流程
//            }
//        });
//        setContentView(shareUI.getView());
//        weChatShare = new WeChatShare(TjrSocialShareWXActivity.this, appId);
//        weChatShare.setTimeline(timeline);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (shareUI != null) shareUI.onActivityResult(requestCode, resultCode, data);
//
//    }
//
//    @Override
//    public void goback() {
//        CommonUtil.pageJump(this, null, true, true);
//    }
//
////    private void startWeiChatTask(String shareType, String content, Bitmap bmp) {
////        CommonUtil.cancelAsyncTask(addWeChatTask);
////        addWeChatTask = (AddWeChatTask) new AddWeChatTask(shareType, content, bmp).executeParams();
////    }
////
////    private class AddWeChatTask extends BaseAsyncTask<Void, Void, String> {
////        private Exception exception;
////        private String content;
////        private Bitmap bmp;
////        private String shareType;
////        private String msg;
////
////        public AddWeChatTask(String shareType, String content, Bitmap bmp) {
////            this.shareType = shareType;
////            this.content = content;
////            this.bmp = bmp;
////        }
////
////        @Override
////        protected void onPreExecute() {
////            super.onPreExecute();
////            showLoadingProgressDialog(getResources().getString(R.string.loading_date_message));
////        }
////
////        @Override
////        protected String doInBackground(Void... params) {
////            try {
////                int isFile = 0;
////                if (bmp != null) {
////                    isFile = 1;
////                    createFileInfo(bmp);
////                }
////                // CommonUtil.LogLa(2,
////                // "weixin-klinegame result is start gethttp 2");
////                // CommonUtil.LogLa(2, "1111file.exists is " + new
////                // File(fileUrl).exists() + " file " + fileUrl + " fileName is "
////                // + fileName);
////                String result = TjrthirdHttp.getInstance().shareWeiXinKline(String.valueOf(shareUI.getUser().getUserId()), shareType, content, fileName, fileUrl, isFile);
////                // CommonUtil.LogLa(2, "weixin-klinegame result is 3 " +
////                // result);
////                if (result != null) {
////
////                    JSONObject json = new JSONObject(result);
////                    if (hasAndNotNull(json, "msg")) {
////                        msg = json.getString("msg");
////                    }
////                    if (hasAndNotNull(json, "success")) {
////                        boolean success = json.getBoolean("success");
////                        if (success) {
////                            if (hasAndNotNull(json, "url")) {
////                                return json.getString("url");
////                            }
////                        }
////                    }
////
////                }
////
////            } catch (Exception e) {
////                this.exception = e;
////                // CommonUtil.LogLa(2, "weixin-klinegame exception");
////                e.printStackTrace();
////            }
////
////            return null;
////        }
////
////        @Override
////        protected void onPostExecute(String result) {
////            super.onPostExecute(result);
////            dismissProgressDialog();
////            if (exception != null) {
////                NotificationsUtil.ToastReasonForFailure(TjrSocialShareWXActivity.this, exception);
////            } else {
////                if (msg != null) {
////                    CommonUtil.showToast(TjrSocialShareWXActivity.this, msg, Gravity.BOTTOM);
////                }
////                CommonUtil.LogLa(2, "AddWeChatTask is " + weChatShare.SendReqURL(result, texttitle, content, icon));
////            }
////
////        }
////    }
//
//    /**
//     * 添加一个weibo到服务器
//     */
//    private void startAddWeiboTask(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option) {
//        CommonUtil.cancelAsyncTask(mAddWeiboTask);
//        mAddWeiboTask = (AddWeiboTask) new AddWeiboTask(weiboType, text, bmp, voteTitle, voteType, option).executeParams();
//    }
//
//    private class AddWeiboTask extends BaseAsyncTask<Void, Void, String> {
//        private Exception exception;
//        private int weiboType;
//        private String text;
//        private Bitmap bmp;
//        private String voteTitle;
//        private int voteType;
//        private String option;
//
//        public AddWeiboTask(int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option) {
//            this.weiboType = weiboType;
//            this.text = text;
//            this.bmp = bmp;
//            this.voteTitle = voteTitle;
//            this.voteType = voteType;
//            this.option = option;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // CommonUtil.LogLa(2, "AddWeiboTask is onPostExecute");
//            showLoadingProgressDialog(getResources().getString(R.string.loading_date_message));
//        }
//
//        @Override
//        protected String doInBackground(Void... params) {
//            try {
//                int cType = 1, isFile = 0;// cType=1纯文字,cType=2图片,cType=3当json
//                if (bmp != null) {
//                    isFile = 1;
//                    cType = 2;
//                    createFileInfo(bmp);// 创建图片
//                }
//                if (jsonStr != null) {
//                    cType = 3;
//                    isFile = 0;
//                }
//                JSONArray contenjson = new JSONArray();
//                if (cType == 3) {
//                    contenjson.put(new JSONObject().put("cType", cType).put("content", jsonStr).put("isFile", isFile));
//                } else if (cType == 2) {
//                    contenjson.put(new JSONObject().put("cType", cType).put("content", fileName).put("isFile", isFile));
//                } else {
//                    contenjson.put(new JSONObject().put("cType", cType).put("content", "").put("isFile", isFile));
//                }
//                String json = TaojinluHttp.getInstance().addWeiboAndOption(weiboType, text, contenjson.toString(), voteTitle, voteType, option, shareUI.getUser().getUserId(), fileName, fileUrl, isFile);
//                // CommonUtil.LogLa(4, "json=" + json);
//                JSONObject jsonObj = new JSONObject(json);
//                if (hasAndNotNull(jsonObj, "success")) {
//                    String suc = jsonObj.getString("success");
//                    if (suc != null && suc.matches("[0-9]+$")) {
//                        weiboNormalUrl = TaojinluHttp.getInstance().getWeiboUrl(suc);
//                    }
//                }
//                return "1";
//            } catch (Exception e) {
//                exception = e;
//                e.printStackTrace();
//                return null;
//            }
//        }
//
//        @Override
//        protected void onPostExecute(String result) {
//            dismissProgressDialog();
//            // CommonUtil.LogLa(2, "AddWeiboTask is onPostExecute");
//            if (exception != null) {
//                NotificationsUtil.ToastReasonForFailure(TjrSocialShareWXActivity.this, exception);
//            } else {
//                CommonUtil.LogLa(2, "AddWeiboTask is " + weChatShare.SendReqURL(weiboNormalUrl, texttitle, text, icon));
//            }
//        }
//    }
//
//    /**
//     * 创建图片
//     *
//     * @param bmp
//     */
//    private void createFileInfo(Bitmap bmp) throws Exception {
//        if (fileName == null) fileName = CommonUtil.getFileName(shareUI.getUser().getUserId());
//        File file = mageRemoteResourceManager.getFile(fileName);
//        fileUrl = file.getPath();
//        mageRemoteResourceManager.writeFile(file, bmp, false);
//        // File file = CommonUtil.GetWeiboFile(fileName);
//        CommonUtil.LogLa(2, "createFileInfo weixin-klinegame file " + file.length() + "   filename=" + fileName + " fileUrl=" + fileUrl);
//        // CommonUtil.writeFile(file, bmp, false);
//    }
//
//}
