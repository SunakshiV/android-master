package com.procoin.http.retrofitservice;

import android.text.TextUtils;
import android.util.Log;

import com.procoin.http.TjrBaseApi;
import com.procoin.http.util.MD5;
import com.procoin.http.common.ConfigTjrInfo;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * 添加公共参数的拦截器
 */
public class PublicParameterInterceptor implements Interceptor {


    @Override
    public okhttp3.Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Log.d("ParameterInterceptor", "method==" + request.method() + "=====================");
        if (request.method().equals("GET")) {
            SortedMap<String, String> map = new TreeMap<String, String>();
            HttpUrl httpUrl = request.url();
//            HttpUrl.Builder builder=new HttpUrl.Builder();//get不能用这种方式
            Set<String> names = httpUrl.queryParameterNames();
            boolean hasUserId = false;//这里判断一下如果是user_id就不添加了,否则会重复，主要是为了以前的接口有些没有把user_id去掉
            for (String name : names) {
                String value = httpUrl.queryParameter(name);
                map.put(name, value);
                if (name.equals("user_id")) {
                    hasUserId = true;
                }
            }
            HttpUrl.Builder builder = httpUrl.newBuilder();
            builder.addQueryParameter("version", ConfigTjrInfo.getInstance().getVersion());
            if (!hasUserId)
                builder.addQueryParameter("userId", ConfigTjrInfo.getInstance().getUserId());
            builder.addQueryParameter("token", TextUtils.isEmpty(ConfigTjrInfo.getInstance().getSessionid()) ? "" : ConfigTjrInfo.getInstance().getSessionid());
            builder.addQueryParameter("bundleId", ConfigTjrInfo.getInstance().getPackageName());
            builder.addQueryParameter("platform", "android");
            builder.addQueryParameter("apiKey", VHttpServiceManager.API_KEY);

            map.put("version", ConfigTjrInfo.getInstance().getVersion());
            map.put("userId", ConfigTjrInfo.getInstance().getUserId());
            map.put("token", TextUtils.isEmpty(ConfigTjrInfo.getInstance().getSessionid()) ? "" : ConfigTjrInfo.getInstance().getSessionid());
            map.put("bundleId", ConfigTjrInfo.getInstance().getPackageName());
            map.put("platform", "android");
            map.put("apiKey", VHttpServiceManager.API_KEY);

            StringBuilder api_secret = new StringBuilder();
            for (String value : map.values()) {
                api_secret.append(value);
            }
            api_secret.append(VHttpServiceManager.API_SECRET);
            Log.d("ParameterInterceptor", "GET    api_secret==" + api_secret);
            HttpUrl newHttpUrl = builder.addQueryParameter("sign", MD5.getMessageDigest(api_secret.toString())).build();
            request = request.newBuilder().url(newHttpUrl).build();

            if (TjrBaseApi.isLog) {
                StringBuilder sb = new StringBuilder();
                sb.append(newHttpUrl).append("?");

                Set<String> log_names = newHttpUrl.queryParameterNames();
                Iterator<String> log_it = log_names.iterator();
                while (log_it.hasNext()) {
                    String name = log_it.next();
                    String value = newHttpUrl.queryParameter(name);
                    sb.append(name).append("=").append(value);
                }
                Log.d("url", "url==" + sb.toString().substring(0, sb.length() - 2));
            }
        } else if (request.method().equals("POST")) {
            Log.d("ParameterInterceptor", "request.body()==" + request.body());
            if (request.body() instanceof MultipartBody) {//文件上传
                MultipartBody.Builder builder = new MultipartBody.Builder();
                SortedMap<String, String> map = new TreeMap<String, String>();

                builder.addFormDataPart("version", ConfigTjrInfo.getInstance().getVersion());
                builder.addFormDataPart("userId", ConfigTjrInfo.getInstance().getUserId());
                builder.addFormDataPart("token", TextUtils.isEmpty(ConfigTjrInfo.getInstance().getSessionid()) ? "" : ConfigTjrInfo.getInstance().getSessionid());
                builder.addFormDataPart("bundleId", ConfigTjrInfo.getInstance().getPackageName());
                builder.addFormDataPart("platform", "android");
                builder.addFormDataPart("apiKey", VHttpServiceManager.API_KEY);

                map.put("version", ConfigTjrInfo.getInstance().getVersion());
                map.put("userId", ConfigTjrInfo.getInstance().getUserId());
                map.put("token", TextUtils.isEmpty(ConfigTjrInfo.getInstance().getSessionid()) ? "" : ConfigTjrInfo.getInstance().getSessionid());
                map.put("bundleId", ConfigTjrInfo.getInstance().getPackageName());
                map.put("platform", "android");
                map.put("apiKey", VHttpServiceManager.API_KEY);

                MultipartBody multipartBody = (MultipartBody) request.body();
                List<MultipartBody.Part> parts = multipartBody.parts();
                Log.d("ParameterInterceptor", "MultipartBody======part.size==" + parts.size());
                for (MultipartBody.Part part : parts) {
                    if (part.body().contentType().type().equals("text")) {
                        Headers headers = part.headers();
                        for (int i = 0; i < headers.names().size(); i++) {
                            Log.d("ParameterInterceptor", "headers======value=" + headers.value(i));
                            String value = headers.value(i);//valueform-data; name="article_type"
                            String replaceValue = "form-data; name=";//这段在MultipartBody.Part源码中看到，为了强行获取key
                            if (value.contains(replaceValue)) {
                                String key = value.replace(replaceValue, "").replaceAll("\"", "");
                                Log.d("ParameterInterceptor", "MultipartBody======key=" + key);
                                map.put(key, bodyToString(part.body()));
                                break;
                            }
                        }
                    }
                    builder.addPart(part);
//                    Log.d("ParameterInterceptor", "MultipartBody======" + bodyToString(part.body()));
//                    Log.d("ParameterInterceptor", "MultipartBody======type==" + part.body().contentType().type());
                }

                StringBuilder api_secret = new StringBuilder();
                for (String value : map.values()) {
                    api_secret.append(value);
                }
                api_secret.append(VHttpServiceManager.API_SECRET);
//                builder.addFormDataPart("sign", Encryption.MD532(api_secret.toString()).toUpperCase());
                builder.addFormDataPart("sign", MD5.getMessageDigest(api_secret.toString()));
                request = request.newBuilder().post(builder.build()).build();
            } else {
                //这里特别注意:当service里面的接口不包含任何参数的时候，是不需要添加@FormUrlEncoded注释的，所以request.body()获取的并不是FormBody而是RequestBody，所以要放else里面
//                 if (request.body() instanceof FormBody || request.body() instanceof RequestBody) {
                FormBody formBody = null;
                if (request.body() instanceof FormBody) {
                    formBody = (FormBody) request.body();
                }
                FormBody.Builder builder = new FormBody.Builder();
                SortedMap<String, String> map = new TreeMap<String, String>();

                if (formBody != null) {
                    for (int i = 0, m = formBody.size(); i < m; i++) {
                        if (!formBody.name(i).equals("user_id")) {//这里判断一下如果是user_id就不添加了，下面统一添加，否则会重复，主要是为了以前的接口有些没有把user_id去掉
                            builder.addEncoded(formBody.encodedName(i), formBody.encodedValue(i));
                        }
                        //map里面的value确保没有被encoded，否则被Rsa加密过得参数就会有问题
                        map.put(formBody.name(i), formBody.value(i));
//                        Log.d("ParameterInterceptor", "formBody.name(i)==" + formBody.name(i) + "   " + formBody.encodedName(i));
//                        Log.d("ParameterInterceptor", formBody.name(i).equals("user_id") + "   " + formBody.encodedName(i).equals("user_id"));
                    }
                }
                builder.add("version", ConfigTjrInfo.getInstance().getVersion())
                        .add("userId", ConfigTjrInfo.getInstance().getUserId())
                        .add("token", TextUtils.isEmpty(ConfigTjrInfo.getInstance().getSessionid()) ? "" : ConfigTjrInfo.getInstance().getSessionid())
                        .add("bundleId", ConfigTjrInfo.getInstance().getPackageName())
                        .add("platform", "android")
                        .add("apiKey", VHttpServiceManager.API_KEY)
                        .build();

                map.put("version", ConfigTjrInfo.getInstance().getVersion());
                map.put("userId", ConfigTjrInfo.getInstance().getUserId());
                map.put("token", TextUtils.isEmpty(ConfigTjrInfo.getInstance().getSessionid()) ? "" : ConfigTjrInfo.getInstance().getSessionid());
                map.put("bundleId", ConfigTjrInfo.getInstance().getPackageName());
                map.put("platform", "android");
                map.put("apiKey", VHttpServiceManager.API_KEY);

                StringBuilder api_secret = new StringBuilder();

                for (String value : map.keySet()) {
                    Log.d("ParameterInterceptor", value +"="+map.get(value)+ "\n");
                }

                for (String value : map.values()) {
                    api_secret.append(value);
                }
                api_secret.append(VHttpServiceManager.API_SECRET);
                Log.d("ParameterInterceptor", "POST    api_secret==" + api_secret + "   size==" + api_secret.length());
//                builder.add("sign", Encryption.MD532(api_secret.toString()).toUpperCase());
                builder.add("sign", MD5.getMessageDigest(api_secret.toString()));
                formBody = builder.build();

                Log.d("ParameterInterceptor", "formBody.size==" + formBody.size());
                request = request.newBuilder().post(formBody).build();


                if (TjrBaseApi.isLog) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(request.url()).append("?");
                    for (int i = 0, m = formBody.size(); i < m; i++) {
                        sb.append(formBody.encodedName(i));
                        sb.append("=");
                        sb.append(formBody.encodedValue(i));
                        if (i < m-1) sb.append("&");
                    }
                    Log.d("ddd", "url==" + sb.toString());
                }
//                 }
            }
        }

        return chain.proceed(request);
    }

    private String bodyToString(RequestBody request) {
        try {
            RequestBody copy = request;
            Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "";
        }
    }
}