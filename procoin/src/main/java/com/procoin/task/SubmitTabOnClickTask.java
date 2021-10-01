//package com.cropyme.task;
//
//import com.cropyme.http.home.TjrHomeSubHttp;
//import com.cropyme.task.BaseAsyncTask;
//
///**
// * Created by zhengmj on 17-3-22.
// */
//
//public class SubmitTabOnClickTask extends BaseAsyncTask<Void, Void, String> {
//
//    private String modelId;
//    private int type;//国际要闻是0 ， 推广是1,welcome 网页广告3 ,webview 关闭的时候要提交url 4
//    private long userId;
//
//    public SubmitTabOnClickTask(long userId, String modelId, int type) {
//        this.modelId = modelId;
//        this.type = type;
//        this.userId = userId;
//    }
//
//    @Override
//    protected String doInBackground(Void... params) {
//        try {
//            String result = TjrHomeSubHttp.getInstance().submitTabTwoOnClick(String.valueOf(userId), modelId, type);
//        } catch (Exception e) {
//        }
//
//        return null;
//    }
//}
