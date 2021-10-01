//package com.cropyme.http.retrofitservice;
//
//
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//
//import java.util.Map;
//
//import io.reactivex.Observable;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//import retrofit2.http.Field;
//import retrofit2.http.FieldMap;
//import retrofit2.http.FormUrlEncoded;
//import retrofit2.http.GET;
//import retrofit2.http.POST;
//import retrofit2.http.Query;
//import retrofit2.http.Url;
//
///**
// * Created by zhengmj on 18-9-6.
// */
///*
//
//     */
//public interface WxService {
//
//    @GET(RedzHttpServiceManager.GET_WECHAT_TOKEN)
//    Call<ResponseBody> getWechatAuthor(@Query("appid") String appid, @Query("secret") String secret, @Query("code") String code, @Query("grant_type") String grant_type);
//    @GET(RedzHttpServiceManager.GET_WECHAT_INFO)
//    Call<ResponseBody> getWechatInfo(@Query("access_token")String access_token,@Query("openid")String openid);
//}
