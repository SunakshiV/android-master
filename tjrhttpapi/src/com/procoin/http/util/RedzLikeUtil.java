//package com.cropyme.http.util;
//
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//
///**
// * Created by zhengmj on 18-11-5.
// */
//
//public class RedzLikeUtil {
//    private long dynamicId;
//    Call<ResponseBody> operaCall;
//    public RedzLikeUtil(long dynamicId){
//        this.dynamicId = dynamicId;
//    }
//    public void getResult(Callback<ResponseBody> callback){
//        CommonUtil.cancelCall(operaCall);
//        operaCall = RedzHttpServiceManager.getInstance().getRedzService().operaLike(dynamicId);
//        operaCall.enqueue(callback);
//    }
//}
