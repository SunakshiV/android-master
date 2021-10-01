//package com.cropyme.social.task;
//
//import android.app.Activity;
//import android.text.TextUtils;
//import android.view.Gravity;
//
//import com.cropyme.http.tjrcpt.CircleHttp;
//import NotificationsUtil;
//import ParserJsonUtils;
//import CommonUtil;
//import BaseAsyncTask;
//
//import org.json.JSONObject;
//
///**
// * Created by kechenng on 16-8-19.
// */
//public abstract class ShareToCircleTask extends BaseAsyncTask<Void, Void, Boolean> {
//
//    private long userId;
//    private String title;
//    private String content;
//    private String params;
//    private String shareType;
//    private String msg;
//    private Exception e;
//    private String result;
//    private String shareContent;//要分享的内容字段
//    private Activity activity;
//
//    public ShareToCircleTask(Activity activity, long userId, String title, String content, String params, String shareType) {
//        this.activity = activity;
//        this.userId = userId;
//        this.title = title;
//        this.content = content;
//        this.params = params;
//        this.shareType = shareType;
//    }
//
//    @Override
//    protected Boolean doInBackground(Void... params) {
//        try {
//            result = CircleHttp.getInstance().getShareTypeByInfo(userId, title, content, this.params, shareType);
//            CommonUtil.LogLa(2, "  userId === " + userId + "  title === " + title + "  content === " + content + "  params === " + this.params + "  shareType === " + shareType
//                    + "\n" + "result === " + result);
////            result === {"code":20000,"msg":"请求成功","shareContent":"{\"cls\":\"com.cropyme.quotation.stock.f10.F10DetailsActivity\",\"content\":\"原标题：险资三季末持仓曝光：新进107股增持56股随着上市公司三季报逐步披露，险资2016年三季末持\",\"logo\":\"http://share.taojinroad.com:9997/app/image/info_share.png\",\"params\":\"{\\\"title\\\":\\\"险资三季末持仓曝光：新进107股 增持56股\\\",\\\"fullCode\\\":\\\"sz000506\\\",\\\"stockName\\\":\\\"中润资源\\\",\\\"urls\\\":\\\"http:\\\\/\\\\/info.taojinroad.com:8090\\\\/company\\\\/news\\\\/2016-10-27\\\\/sz0005068526241.html\\\",\\\"brief\\\":\\\"原标题：险资三季末持仓曝光：新进107股增持56股随着上市公司三季报逐步披露，险资2016年三季末持仓情况曝光。证券时报股市大数据新媒体“数据宝”统计，截至10\\\",\\\"f10TypeNumber\\\":0}\",\"pkg\":\"com.cropyme\",\"pview\":\"F10DetailsViewController\",\"time\":20161028171732,\"title\":\"险资三季末持仓曝光：新进107股 增持56股\"}","success":true}
//
//            if (!TextUtils.isEmpty(result)) {
//                JSONObject jaAll = new JSONObject(result);
//                if (ParserJsonUtils.hasAndNotNull(jaAll, "msg")) {
//                    msg = jaAll.getString("msg");
//                }
//                if (ParserJsonUtils.hasAndNotNull(jaAll, "success")) {
//                    if (jaAll.getBoolean("success")) {
//                        if (ParserJsonUtils.hasAndNotNull(jaAll, "shareContent")) {
//                            shareContent = jaAll.getString("shareContent");
//
//                            CommonUtil.LogLa(2, "  shareContent === " + shareContent);
////                            shareContent === {"cls":"com.cropyme.quotation.stock.f10.F10DetailsActivity","content":"原标题：险资三季末持仓曝光：新进107股增持56股随着上市公司三季报逐步披露，险资2016年三季末持","logo":"http://share.taojinroad.com:9997/app/image/info_share.png","params":"{\"title\":\"险资三季末持仓曝光：新进107股 增持56股\",\"fullCode\":\"sz000506\",\"stockName\":\"中润资源\",\"urls\":\"http:\\/\\/info.taojinroad.com:8090\\/company\\/news\\/2016-10-27\\/sz0005068526241.html\",\"brief\":\"原标题：险资三季末持仓曝光：新进107股增持56股随着上市公司三季报逐步披露，险资2016年三季末持仓情况曝光。证券时报股市大数据新媒体“数据宝”统计，截至10\",\"f10TypeNumber\":0}","pkg":"com.cropyme","pview":"F10DetailsViewController","time":20161028173334,"title":"险资三季末持仓曝光：新进107股 增持56股"}
//                        }
//                        return true;
//                    }
//                }
//            }
//        } catch (Exception e1) {
//            this.e = e1;
//            e1.printStackTrace();
//        }
//        return false;
//    }
//
//    @Override
//    protected void onPostExecute(Boolean aBoolean) {
//        if (aBoolean) {
//            getContentFinish(shareContent);
//        } else {
//            if (!TextUtils.isEmpty(msg)) {
//                CommonUtil.showToast(activity, msg, Gravity.CENTER);
//            }
//            if (e != null) {
//                NotificationsUtil.ToastReasonForFailure(activity, e);
//            }
//        }
//    }
//
//    public abstract void getContentFinish(String shareContent);
//
//}
