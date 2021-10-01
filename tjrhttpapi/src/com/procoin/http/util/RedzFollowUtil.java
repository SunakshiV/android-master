//package com.cropyme.http.util;
//
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.Callback;
//
///**
// * Created by zhengmj on 18-10-19.
// */
//
//public class RedzFollowUtil {
//    private boolean doAdd;//true -> add,false -> delete;
//    private long uid;
//    private Call<ResponseBody> addCall;
//    private Call<ResponseBody> delCall;
//
//    public RedzFollowUtil(boolean doAdd,long uid){
//        this.doAdd = doAdd;
//        this.uid = uid;
//    }
//    public void getResult(Callback<ResponseBody>callback){
//        if (doAdd){
//            addFollow(callback);
//        }else {
//            delFollow(callback);
//        }
//    }
//    private void addFollow(Callback<ResponseBody>callback){
//        CommonUtil.cancelCall(addCall);
//        addCall = RedzHttpServiceManager.getInstance().getRedzService().addFollow(uid);
//        addCall.enqueue(callback);
//    }
//    private void delFollow(Callback<ResponseBody>callback){
//        CommonUtil.cancelCall(delCall);
//        delCall = RedzHttpServiceManager.getInstance().getRedzService().delFollow(uid);
//        delCall.enqueue(callback);
//    }
//}
