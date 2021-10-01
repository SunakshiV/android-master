//package com.cropyme.social.task;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.view.Gravity;
//
//import BaseRemoteResourceManager;
//import com.cropyme.http.tjrcpt.TjrthirdHttp;
//import NotificationsUtil;
//import ParserJsonUtils;
//import CommonUtil;
//import BaseAsyncTask;
//
//import org.json.JSONObject;
//
//import java.io.File;
//
//public abstract class ThirdShareWXTask extends BaseAsyncTask<Void, Void, String> {
//    private Exception exception;
//    private String content;
//    private String paramstr;
//    private Activity activity;
//    private long userId;
//    private String shareType;
//    private String msg;
////    private TjrProgressDialog tjrProgressDialog;
//    private int isFile;
//    private Bitmap viewBitmap;
//    private String fileName, fileUrl;
//    private BaseRemoteResourceManager mageRemoteResourceManager;
//
//    public ThirdShareWXTask(Activity activity, String shareType, String content, String params, long userId, Bitmap viewBitmap) {
//        this.paramstr = params;
//        this.userId = userId;
//        this.content = content;
//        this.shareType = shareType;
//        this.activity = activity;
//        this.viewBitmap = viewBitmap;
////        tjrProgressDialog = new TjrProgressDialog(activity);
//        mageRemoteResourceManager = new BaseRemoteResourceManager("chat");
//    }
//
//    @Override
//    protected void onPreExecute() {
////        if (activity != null && !activity.isFinishing()) tjrProgressDialog.showProgressDialog();
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        try {
//            if (viewBitmap != null) {
//                isFile = 1;
//                createFileInfo(viewBitmap);// 创建图片
//            }
//
////            if (paramstr != null) {
////                cType = 3;
////                isFile = 0;
////            }
////            JSONArray contenjson = new JSONArray();
////            if (cType == 3) {//
////                contenjson.put(new JSONObject().put("cType", cType).put("content", paramstr).put("isFile", isFile));
////                paramstr = contenjson.toString();
////            }
//
//            String result = TjrthirdHttp.getInstance().shareWeiXinKline(String.valueOf(userId), shareType, content, paramstr, fileName, fileUrl, isFile);
//            CommonUtil.LogLa(2, "TjrthirdHttp result is " + result);
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
//            CommonUtil.LogLa(2, "TjrthirdHttp result Exception is " + e.getMessage());
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        CommonUtil.LogLa(2, "TjrthirdHttp onPostExecute is " + result);
////        tjrProgressDialog.dismissProgressDialog();
//        if (exception != null) {
//            CommonUtil.LogLa(2, "TjrthirdHttp exception is " + exception);
//            NotificationsUtil.ToastReasonForFailure(activity, exception);
//        } else {
//            if (msg != null) {
//                CommonUtil.LogLa(2, "TjrthirdHttp msg is " + msg);
//                CommonUtil.showToast(activity, msg, Gravity.BOTTOM);
//            }
//            sendUrl(result);
////            if (weChatShare != null) {
////                weChatShare.setTimeline(isTimeline);
////                CommonUtil.LogLa(2, "TjrthirdHttp SendReqURL is " + weChatShare.SendReqURL(result, texttitle, shareContent, bitmap));
////            }
//        }
//    }
//
//    /**
//     * 创建图片
//     *
//     * @param bmp
//     */
//    private void createFileInfo(Bitmap bmp) throws Exception {
//        if (fileName == null) fileName = CommonUtil.getFileName(userId);
//        File file = mageRemoteResourceManager.getFile(fileName);
//        fileUrl = file.getPath();
//        mageRemoteResourceManager.writeFile(file, bmp, false);
//        // File file = CommonUtil.GetWeiboFile(fileName);
//        CommonUtil.LogLa(2, "createFileInfo weixin-klinegame file " + file.length() + "   filename=" + fileName + " fileUrl=" + fileUrl);
//        // CommonUtil.writeFile(file, bmp, false);
//    }
//
//    public abstract void sendUrl(String url);
//
//}