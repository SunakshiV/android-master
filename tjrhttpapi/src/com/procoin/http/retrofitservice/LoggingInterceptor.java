package com.procoin.http.retrofitservice;

import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.Connection;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

//暂时不用这个

public class LoggingInterceptor implements Interceptor {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {

            Request request=chain.request();
            long requestTime = System.currentTimeMillis();//请求发起的时间
            HttpUrl httpUrl=request.url();
            List<String> strings= httpUrl.encodedPathSegments();

            Log.d("LoggingInterceptor","strings=="+strings);

            if (request.body() instanceof FormBody) {
                FormBody formBody=(FormBody)request.body();
                Log.d("LoggingInterceptor","formBody.size()=="+formBody.size());
                for (int i = 0; i <formBody.size() ; i++) {
                    Log.d("LoggingInterceptor",formBody.name(i)+"=="+formBody.value(i));
                }

            }
            Connection requestConnection=chain.connection();
            Headers requestHeaders=request.headers();

            Log.d("LoggingInterceptor","url=="+httpUrl);
            Log.d("LoggingInterceptor","requestConnection=="+requestConnection);
            Log.d("LoggingInterceptor","requestHeaders=="+requestHeaders);

            Log.d("LoggingInterceptor","requestHeaders.size=="+requestHeaders.size());

            long responseTime = System.currentTimeMillis();//收到响应的时间
            okhttp3.Response response = chain.proceed(chain.request());
            ResponseBody responseBody = response.peekBody(1024 * 1024);
            HttpUrl responseUrl=response.request().url();
//            String content = response.body().string();
            Headers responseHeaders=response.headers();
            long delayTime=responseTime-requestTime;


            Log.d("LoggingInterceptor","responseBody=="+responseBody);
            Log.d("LoggingInterceptor","responseUrl=="+responseUrl);
//            Log.d("LoggingInterceptor","content=="+content);
            Log.d("LoggingInterceptor","responseHeaders=="+responseHeaders);
            Log.d("LoggingInterceptor","delayTime=="+delayTime);

            return response;
        }
    }