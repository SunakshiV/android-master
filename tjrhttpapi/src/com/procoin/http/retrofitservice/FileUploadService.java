package com.procoin.http.retrofitservice;


import com.procoin.http.tjrcpt.VHttpServiceManager;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Url;

/**
 * Created by zhengmj on 18-9-6.
 */

public interface FileUploadService {


    /**
     * 上传图片都可以用这个
     * @param url
     * @param map
     * @param parts
     * @return
     */
    @Multipart
    @POST
    Call<ResponseBody> uploadFiles(
            @Url String url,
            @PartMap Map<String, RequestBody> map,
            @Part() List<MultipartBody.Part> parts);


    /**
     * 上传头像
     * @param url
     * @param part
     * @return
     */
    @Multipart
    @POST
    Call<ResponseBody> uploadFile(
            @Url String url,
            @Part() MultipartBody.Part part);



    //获取图片验证码
    @POST(VHttpServiceManager.DRAG_IMG)
    Call<ResponseBody> getDragImg();

    //获取图片验证码
    @POST(VHttpServiceManager.GETDNS)
    Call<ResponseBody> getDns();


}
