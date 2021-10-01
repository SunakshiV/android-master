//package com.cropyme.social.task;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.view.Gravity;
//
//import com.cropyme.http.tjrcpt.TjrthirdHttp;
//import NotificationsUtil;
//import ParserJsonUtils;
//import CommonUtil;
//import BaseAsyncTask;
//import WeChatShare;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//public class OldThirdShareWXTask extends BaseAsyncTask<Void, Void, String> {
//    private Exception exception;
//    private String texttitle;
//    private String content;
//    private String paramstr;
//    private Activity activity;
//    private long userId;
//    private String shareType;
//    private String msg;
//    private WeChatShare weChatShare;
//    private Bitmap bitmap;
//    public boolean isTimeline;// 是不是朋友圈
////    private TjrProgressDialog tjrProgressDialog;
//    private int cType;
//    private int isFile;
//
//    public OldThirdShareWXTask(Activity activity, String shareType, String texttitle, String content, String params, long userId, Bitmap bitmap) {
//        this.paramstr = params;
//        this.userId = userId;
//        this.content = content;
//        this.shareType = shareType;
//        this.texttitle = texttitle;
//        this.bitmap = bitmap;
//        this.activity = activity;
//        weChatShare = new WeChatShare(activity);
//        weChatShare.setTimeline(isTimeline);
////        tjrProgressDialog = new TjrProgressDialog(activity);
//    }
//
//    @Override
//    protected void onPreExecute() {
//        // activity.showLoadingProgressDialog(activity.getResources().getString(R.string.loading_date_message));
////        if (activity != null && !activity.isFinishing()) tjrProgressDialog.showProgressDialog();
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        try {
//            if (paramstr != null) {
//                cType = 3;
//                isFile = 0;
//            }
//            JSONArray contenjson = new JSONArray();
//            if (cType == 3) {//
//                contenjson.put(new JSONObject().put("cType", cType).put("content", paramstr).put("isFile", isFile));
//                paramstr = contenjson.toString();
//            }
//            CommonUtil.LogLa(2, "contenjson is " + paramstr);
//            String result = TjrthirdHttp.getInstance().shareWeiXin(shareType, content, paramstr, String.valueOf(userId));
//            if (result != null) {
//                JSONObject json = new JSONObject(result);
//                if (ParserJsonUtils.hasAndNotNull(json, "msg")) {
//                    msg = json.getString("msg");
//                }
//                if (ParserJsonUtils.hasAndNotNull(json, "success")) {
//                    boolean success = json.getBoolean("success");
//                    if (success) {
//                        if (ParserJsonUtils.hasAndNotNull(json, "url")) {
//                            return json.getString("url");
//                        }
//                    }
//                }
//
//            }
//            return "1";
//        } catch (Exception e) {
//            exception = e;
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
////        tjrProgressDialog.dismissProgressDialog();
//        if (exception != null) {
//            NotificationsUtil.ToastReasonForFailure(activity, exception);
//        } else {
//            if (weChatShare != null) {
//                if (msg != null) {
//                    CommonUtil.showToast(activity, msg, Gravity.BOTTOM);
//                }
//                weChatShare.setTimeline(isTimeline);
//                weChatShare.SendReqURL(result, texttitle, content, bitmap);
//            }
//        }
//    }
//
//}