//package com.cropyme.task;
//
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//import CommonUtil;
//import MyCallBack;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//
///**
// * Created by zhengmj on 17-9-27.
// */
//
//public class GetWxauthShareUrlTask {
//    Call<ResponseBody> call;
//    String share_type;
//    String params;
//
//    public String title;
//    public String content;
//    public String logo;
//    public String share_url;
//    private MyCallBack callBack;
//
//    public GetWxauthShareUrlTask(String share_type, String params, MyCallBack myCallBack) {
//        this.share_type = share_type;
//        this.params = params;
//        callBack = myCallBack;
//        startGetInfo(share_type, params);
//    }
//
//    private void startGetInfo(String share_type, String params) {
//        CommonUtil.cancelCall(call);
//        call = RedzHttpServiceManager.getInstance().getShareService().getWxauthShareUrl(share_type, params);
//        if (callBack != null)
//            call.enqueue(callBack);
//    }
////    @Override
////    protected Boolean doInBackground(Void... p) {
////        try {
////            String result = BeebarHttp.getInstance().getWxauthShareUrl(share_type, params);
////            Log.d("result", "result==" + result);
////            if (!TextUtils.isEmpty(result)) {
////                boolean ret=false;
////                JSONObject jsonObject = new JSONObject(result);
////                if (JsonParserUtils.hasAndNotNull(jsonObject, "success")) {
////                    if (ret=jsonObject.getBoolean("success")) {
////                        JSONObject jsondata=jsonObject.getJSONObject("data");
////                        if(JsonParserUtils.hasAndNotNull(jsondata,"share_url")){
////                            share_url=jsondata.getString("share_url");
////                        }
////                        if(JsonParserUtils.hasAndNotNull(jsondata,"title")){
////                            title=jsondata.getString("title");
////                        }
////                        if(JsonParserUtils.hasAndNotNull(jsondata,"content")){
////                            content=jsondata.getString("content");
////                        }
////                        if(JsonParserUtils.hasAndNotNull(jsondata,"logo")){
////                            logo=jsondata.getString("logo");
////                        }
////
////                    }
////                }
////                return ret;
////            }
////
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return false;
////    }
//}
