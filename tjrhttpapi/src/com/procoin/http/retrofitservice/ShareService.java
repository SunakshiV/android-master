//package com.cropyme.http.retrofitservice;
//
//
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.http.Field;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.POST;
//
///**
// * Created by zhengmj on 18-9-25.
// */
//
//public interface ShareService {
//    @FormUrlEncoded
//    @POST(RedzHttpServiceManager.PERVALSYS_WXAUTH_GET_SHARE)
//    Call<ResponseBody> getWxauthShareUrl(@Field("share_type") String share_type, @Field("params")String params);
//}
