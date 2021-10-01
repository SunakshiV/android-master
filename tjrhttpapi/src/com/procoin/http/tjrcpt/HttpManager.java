package com.procoin.http.tjrcpt;

import android.util.Log;

import com.procoin.http.TjrBaseApi;
import com.procoin.http.retrofitservice.FileUploadService;
import com.procoin.http.retrofitservice.PostService;
import com.procoin.http.retrofitservice.PublicParameterInterceptor;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by zhengmj on 19-4-17.
 */

public class HttpManager {
    private static HttpManager instance;

    private final String mApiImredzBaseUri;
    private final String mApiImredzFileBaseUri;
//    private final String mApiImTokaShare;

    private PostService postService;
    private FileUploadService fileUploadService;

    private HttpManager() {
        mApiImredzBaseUri = TjrBaseApi.mApiCropymeBaseUri.uri();
        mApiImredzFileBaseUri = TjrBaseApi.mApiCropymeBaseUploadFile.uri();
//        mApiImTokaShare = TjrBaseApi.mApiImTokaShare.uri();
    }

    public static HttpManager getInstance(){
        if (instance == null) {
            synchronized (HttpManager.class) {
                if (instance == null) instance = new HttpManager();
            }
        }
        return instance;
    }
    public PostService postService(){
        if (postService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.connectTimeout(VHttpServiceManager.TIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(VHttpServiceManager.TIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(VHttpServiceManager.TIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连
            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mApiImredzBaseUri)
                    .build();
            postService = retrofit.create(PostService.class);
        }
        return postService;
    }
    public FileUploadService uploadService() {
        if (fileUploadService == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.addInterceptor(new PublicParameterInterceptor());
            if (TjrBaseApi.isLog) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.d("url_info", "url_info=" + message);
                    }
                });
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(httpLoggingInterceptor);//这里是日志监听
            }
            builder.connectTimeout(VHttpServiceManager.UPLOADTIMEOUT, TimeUnit.SECONDS);
            builder.readTimeout(VHttpServiceManager.UPLOADTIMEOUT, TimeUnit.SECONDS);
            builder.writeTimeout(VHttpServiceManager.UPLOADTIMEOUT, TimeUnit.SECONDS);
            builder.retryOnConnectionFailure(true);//错误重连

            Retrofit retrofit = new Retrofit.Builder()
                    .client(builder.build())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(mApiImredzFileBaseUri)
                    .build();
            fileUploadService = retrofit.create(FileUploadService.class);

        }
        return fileUploadService;
    }
}
