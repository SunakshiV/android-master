package com.procoin.http;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.http.error.TaojinluException;
import com.procoin.http.resource.DiskCache;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.MD5;
import com.procoin.http.common.TJRFilterConf;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.zip.GZIPInputStream;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpApi {
    // private static final String VERSION = "1.1.0";
    // private static final int DEFAULT_MAX_CONNECTIONS = 512;
    // private static final int DEFAULT_SOCKET_TIMEOUT = 30 * 1000;
    // private static final int DEFAULT_MAX_RETRIES = 3;
    private static final int DEFAULT_SOCKET_BUFFER_SIZE = 8192;
    // private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";

    // private static int maxConnections = DEFAULT_MAX_CONNECTIONS;
    // private static int socketTimeout = DEFAULT_SOCKET_TIMEOUT;

    private static final String DEFAULT_CLIENT_VERSION = "redflea.com";
    private static final String CLIENT_VERSION_HEADER = "User-Agent";
    private static final int TIMEOUT = 30; // 设置连接超时及读取超时
//    private static final int TIMEOUT = 2;//测试用超时设置
    private final DefaultHttpClient mHttpClient;

    private final OkHttpClient mOkHttpClient;


//    public static final String API_KEY_FOR_PERVAL = "E419470E5EE64EE9889E85D61F94868F";
//    public static final String API_SECRET_FOR_PERVAL = "915549343E084F1FAA4CEA3E39B52028";

//    public static final String API_KEY_FOR_PERVAL = "7F26EF8E98DD4FC2A1A11FD037F51D04";
//    public static final String API_SECRET_FOR_PERVAL = "CF5DF2E9B5654E9FB0BEEB14171814C2";

    public HttpApi() {
        mHttpClient = HttpApi.createHttpClient();
        mOkHttpClient = HttpApi.createOkHttpClient();
    }

    public void setCookieStore(CookieStore cookieStore) {
        mHttpClient.setCookieStore(cookieStore);
    }


    // public static HttpApi getInstance() {
    // if (instance == null) {
    // synchronized (HttpApi.class) {
    // if (instance == null) instance = new HttpApi();
    // }
    // }
    // return instance;
    // }

    private HttpGet createHttpGet(String url, NameValuePair... nameValuePairs) {
        String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
        HttpGet httpGet = new HttpGet(url + "?" + query);
        CommonUtil.LogLa(4, "-------url-----HttpRequest for: " + url + "?" + query);
        return httpGet;
    }

    private HttpGet createHttpGet(String url) {
        HttpGet httpGet = new HttpGet(url);
        CommonUtil.LogLa(4, "-------url-----HttpRequest for: " + url);
        return httpGet;
    }

    private HttpPost createHttpPost(String url, NameValuePair... nameValuePairs) {
        HttpPost httpPost = new HttpPost(url);
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(stripNulls(nameValuePairs), HTTP.UTF_8));
        } catch (UnsupportedEncodingException e1) {
            throw new IllegalArgumentException("参数设置不正确");
        }
        String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
        CommonUtil.LogLa(4, "-------url-----HttpRequest for: " + url + "?" + query);
        Log.d("request_url", "url=" + url + "?" + query);
        return httpPost;
    }

    /**
     * 构造一个url
     */
    public String createUrl(String specUrl, NameValuePair... nameValuePairs) {
        return specUrl + "?" + URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
    }

    /**
     * 构造一个url,不需要基本的session等东西
     */
    public String createNoBaseKeyUrl(String specUrl, NameValuePair... nameValuePairs) {
        return specUrl + "?" + URLEncodedUtils.format(stripNullsNoHavebaseKey(nameValuePairs), HTTP.UTF_8);
    }

    public HttpURLConnection createHttpURLConnectionPost(URL url, String boundary) throws TaojinluException, IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(TIMEOUT * 1000);
        conn.setRequestMethod("POST");

        conn.setRequestProperty(CLIENT_VERSION_HEADER, DEFAULT_CLIENT_VERSION);
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Charset", "UTF-8");
        conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
        return conn;
    }

    public String doHttpURLConnectionGet(String specUrl, NameValuePair... nameValuePairs) throws TaojinluException, IOException {
        try {
            String query = URLEncodedUtils.format(stripNulls(nameValuePairs), HTTP.UTF_8);
            URL url = new URL(specUrl + "?" + query);
            // String host = Proxy.getDefaultHost(); // 这样可以获取代理接口和端口
            // int post = Proxy.getDefaultPort();
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求头
            // if (host != null && post != -1) {
            // java.net.Proxy proxy = new
            // java.net.Proxy(java.net.Proxy.Type.HTTP, new
            // InetSocketAddress(host, post));
            // conn = (HttpURLConnection) url.openConnection(proxy);
            // } else {
            // conn = (HttpURLConnection) url.openConnection();
            // }
            conn.setUseCaches(false);
            // 设置连接超时时间
            conn.setConnectTimeout(TIMEOUT * 1000);
            // 读取超时
            conn.setReadTimeout(TIMEOUT * 1000);
            conn.setRequestProperty("Accept", "*/*");
            conn.setRequestProperty("Content-Type", "text/html");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            // conn.setRequestProperty("User-Agent",
            // "Mozilla/5.0 (Linux; U; Android 2.2; zh-cn; Desire_A8181 Build/FRF91) AppleWebKit/533.1 (KHTML, likeGecko) Version/4.0 Mobile Safari/533.1");
            InputStreamReader in = new InputStreamReader(conn.getInputStream());
            BufferedReader reader = new BufferedReader(in);// 读字符串用的。
            String inputLine = null;
            StringBuffer result = new StringBuffer();
            // 使用循环来读取获得的数据，把数据都村到result中了
            while (((inputLine = reader.readLine()) != null)) {
                result.append(inputLine);
            }
            reader.close();// 关闭输入流
            // 关闭http连接
            conn.disconnect();
            return result.toString();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    public String doHttpGet(String url, NameValuePair... nameValuePairs) throws TaojinluException, IOException {
        HttpGet httpGet = null;
        if (nameValuePairs != null && nameValuePairs.length > 0) {
            httpGet = createHttpGet(url, nameValuePairs);
        } else {
            httpGet = createHttpGet(url);
        }
        Log.d("uri", "uri==" + httpGet.getURI());
        HttpResponse response = executeHttpRequest(httpGet);
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                try {
                    return checkValidateResponse(EntityUtils.toString(response.getEntity()));
                    // return EntityUtils.toString(response.getEntity());
                } catch (ParseException e) {
                    throw new TaojinluException(e.getMessage());
                }

            case 401:
                response.getEntity().consumeContent();
                throw new TaojinluException(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new TaojinluException(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new TaojinluException(response.getStatusLine().toString());
        }
    }

    public String doHttpPost(String url, NameValuePair... nameValuePairs) throws TaojinluException, IOException {
        HttpPost httpPost = createHttpPost(url, nameValuePairs);
        Log.d("uri", "uri==" + httpPost.getURI());
        CommonUtil.LogLa(2, "uri==" + httpPost.getURI());
        HttpResponse response = executeHttpRequest(httpPost);
        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                try {
                    return checkValidateResponse(EntityUtils.toString(response.getEntity()));
                } catch (ParseException e) {
                    throw new TaojinluException(e.getMessage());
                }
            case 401:
                response.getEntity().consumeContent();
                throw new TaojinluException(response.getStatusLine().toString());

            case 404:
                response.getEntity().consumeContent();
                throw new TaojinluException(response.getStatusLine().toString());

            default:
                response.getEntity().consumeContent();
                throw new TaojinluException(response.getStatusLine().toString());
        }
    }


    public void doOkHttpUpload(String url, List<String> urls, String imType, Callback callback, NameValuePair... nameValuePairs) {
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);

        List<NameValuePair> nameValuePairList = stripNulls(nameValuePairs);
        for (NameValuePair nameValuePair : nameValuePairList) {
            multipartBuilder.addFormDataPart(nameValuePair.getName(), nameValuePair.getValue());
        }
        if(urls!=null&&urls.size()>0&&!TextUtils.isEmpty(imType)){
            for (String u : urls) {
                File file = new File(u);
                Log.d("mOkHttpClient","file=="+u+"  exists=="+file.exists());
                if (file.exists()) {
                    multipartBuilder.addFormDataPart("files", file.getName(), RequestBody.create(MediaType.parse(imType), file));
                }
            }
        }
        Request request = new Request.Builder()
                .url(url)
                .post(multipartBuilder.build())
                .build();
        mOkHttpClient.newCall(request).enqueue(callback);

    }


    /**
     * 专门对私聊下载文件 hash 文件名
     */
    public String doHttpGetForDownLoadFile(DiskCache mResourceCache, String hash, String specUrl, NameValuePair... nameValuePairs) throws TaojinluException, IOException {
        HttpGet httpGet = createHttpGet(specUrl, nameValuePairs);
        httpGet.addHeader("Accept-Encoding", ENCODING_GZIP);
        HttpResponse response = executeHttpRequest(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream is = getUngzippedContent(entity);
        if (is == null) return null;
        if (mResourceCache.store(hash, is)) return hash;
        else return null;
    }

    /**
     * 下载文件文件名
     */
    public String doHttpGetForDownLoadFile(DiskCache mResourceCache, String specUrl) throws TaojinluException, IOException {
        HttpGet httpGet = createHttpGet(specUrl);
        httpGet.addHeader("Accept-Encoding", ENCODING_GZIP);
        HttpResponse response = executeHttpRequest(httpGet);
        HttpEntity entity = response.getEntity();
        InputStream is = getUngzippedContent(entity);
        if (is == null) return null;
        if (mResourceCache.store(specUrl, is)) return specUrl;
        else return null;
    }

    /**
     * 上传文件
     *
     * @param sendUrl
     * @param file
     * @return
     * @throws TaojinluException
     * @throws IOException
     */
    public String uploadFile(String sendUrl, File file, String filename) throws TaojinluException, IOException {
        if (file == null) return null;

        String BOUNDARY = "------------------319831265358979362846";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        int maxBufferSize = 8192;
        FileInputStream fileInputStream = new FileInputStream(file);
        URL url = new URL(sendUrl);
        HttpURLConnection conn = createHttpURLConnectionPost(url, BOUNDARY);
//        conn.addRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        String string =twoHyphens+BOUNDARY+lineEnd;

        dos.writeBytes(twoHyphens + BOUNDARY + lineEnd);  //

//        dos.write(string.getBytes());

        dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\"" + filename + "\"" + lineEnd); //

//        String string2 = "Content-Disposition: form-data; name=\"file\";filename=\"" + filename + "\"" + lineEnd;
//        dos.write(string2.getBytes());

        dos.writeBytes("Content-Type:application/octet-stream" + lineEnd); //

//        String string3 = "Content-Type:application/octet-stream" + lineEnd;

        dos.writeBytes(lineEnd); //

//        dos.write(string3.getBytes());

        int bytesAvailable = fileInputStream.available();
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
        int totalBytesRead = bytesRead;
        while (bytesRead > 0) {
            dos.write(buffer, 0, bufferSize);
            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            totalBytesRead = totalBytesRead + bytesRead;
        }
        dos.writeBytes(lineEnd); //

//        dos.write(lineEnd.getBytes());
//        String string4 = twoHyphens + BOUNDARY + twoHyphens + lineEnd;
        dos.writeBytes(twoHyphens + BOUNDARY + twoHyphens + lineEnd); //
//        dos.write(string4.getBytes());
        fileInputStream.close();
        dos.flush();
        dos.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String responseLine = "";
        while ((responseLine = in.readLine()) != null) {
            response.append(responseLine);
        }
        in.close();
        return checkValidateResponse(response.toString());

    }


    public static SortedMap<String,String> stripMap (SortedMap<String, String> map){

        if (ConfigTjrInfo.getInstance().getVersion() != null) {
            map.put("version", ConfigTjrInfo.getInstance().getVersion());
        }

        if (ConfigTjrInfo.getInstance().getSessionid() != null) {
            map.put("token", ConfigTjrInfo.getInstance().getSessionid());
        }

        if (ConfigTjrInfo.getInstance().getPackageName() != null) {
            map.put("bundleId", ConfigTjrInfo.getInstance().getPackageName());
        }

        map.put("platform", "android");

        map.put("apiKey", VHttpServiceManager.API_KEY);

        StringBuilder api_secret = new StringBuilder();
        for (String value : map.values()) {
            api_secret.append(value);
            Log.d("api_secret", "value==" + value);
        }
        api_secret.append(VHttpServiceManager.API_SECRET);
        Log.d("api_secret", "api_secret==" + api_secret+"  size=="+api_secret.length());
//        map.put("sign",Encryption.MD532(api_secret.toString()).toUpperCase());
        map.put("sign", MD5.getMessageDigest(api_secret.toString().getBytes()));

        return map;
    }

    private List<NameValuePair> stripNulls(NameValuePair... nameValuePairs) {
//        List<NameValuePair> params = new ArrayList<NameValuePair>();
//        for (int i = 0; i < nameValuePairs.length; i++) {
//            NameValuePair param = nameValuePairs[i];
//            if (param.getValue() != null) {
//                params.add(param);
//            }
//        }
//        if (ConfigTjrInfo.getInstance().getVersion() != null)
//            params.add(new BasicNameValuePair("version", ConfigTjrInfo.getInstance().getVersion()));
//        if (ConfigTjrInfo.getInstance().getSessionid() != null)
//            params.add(new BasicNameValuePair("token", ConfigTjrInfo.getInstance().getSessionid()));
//        if (ConfigTjrInfo.getInstance().getPackageName() != null)
//            params.add(new BasicNameValuePair("packageName", ConfigTjrInfo.getInstance().getPackageName()));
//        params.add(new BasicNameValuePair("platform", "android"));


        List<NameValuePair> params = new ArrayList<NameValuePair>();
        SortedMap<String, String> map = new TreeMap<String, String>();
        for (int i = 0; i < nameValuePairs.length; i++) {
            NameValuePair param = nameValuePairs[i];
            if (param.getValue() != null && !"".equals(param.getValue())) {
                params.add(param);
                map.put(param.getName(), param.getValue());
            }
        }

        if (ConfigTjrInfo.getInstance().getVersion() != null) {
            params.add(new BasicNameValuePair("version", ConfigTjrInfo.getInstance().getVersion()));
            map.put("version", ConfigTjrInfo.getInstance().getVersion());
        }

        if (ConfigTjrInfo.getInstance().getSessionid() != null) {
            params.add(new BasicNameValuePair("token", ConfigTjrInfo.getInstance().getSessionid()));
            map.put("token", ConfigTjrInfo.getInstance().getSessionid());
        }

        if (ConfigTjrInfo.getInstance().getPackageName() != null) {
            params.add(new BasicNameValuePair("bundleId", ConfigTjrInfo.getInstance().getPackageName()));
            map.put("bundleId", ConfigTjrInfo.getInstance().getPackageName());
        }

        params.add(new BasicNameValuePair("platform", "android"));
        map.put("platform", "android");

        params.add(new BasicNameValuePair("apiKey", VHttpServiceManager.API_KEY));
        map.put("apiKey", VHttpServiceManager.API_KEY);

        StringBuilder api_secret = new StringBuilder();
        for (String value : map.values()) {
            api_secret.append(value);
        }
        api_secret.append(VHttpServiceManager.API_SECRET);
        Log.d("api_secret", "api_secret==" + api_secret);
//        params.add(new BasicNameValuePair("sign", Encryption.MD532(api_secret.toString()).toUpperCase()));
        params.add(new BasicNameValuePair("sign", MD5.getMessageDigest(api_secret.toString().getBytes())));





        return params;
    }





    private List<NameValuePair> stripNullsNoHavebaseKey(NameValuePair... nameValuePairs) {
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        for (int i = 0; i < nameValuePairs.length; i++) {
            NameValuePair param = nameValuePairs[i];
            if (param.getValue() != null && !"null".equals(param.getValue())) {
                params.add(param);
            }
        }
        return params;
    }

    /**
     * Gets the input stream from a response entity. If the entity is gzipped
     * then this will get a stream over the uncompressed data.
     *
     * @param entity the entity whose content should be read
     * @return the input stream to read from
     * @throws IOException
     */
    public static InputStream getUngzippedContent(HttpEntity entity) throws IOException {
        InputStream responseStream = entity.getContent();
        if (responseStream == null) {
            return responseStream;
        }
        Header header = entity.getContentEncoding();
        if (header == null) {
            return responseStream;
        }
        String contentEncoding = header.getValue();
        if (contentEncoding == null) {
            return responseStream;
        }
        if (contentEncoding.contains(ENCODING_GZIP)) {
            responseStream = new GZIPInputStream(responseStream);
        }
        return responseStream;
    }

    /**
     * execute() an httpRequest catching exceptions and returning null instead.
     *
     * @param httpRequest
     * @return
     * @throws IOException
     */
    public HttpResponse executeHttpRequest(HttpRequestBase httpRequest) throws IOException {
        try {
            mHttpClient.getConnectionManager().closeExpiredConnections();
            // that have been idle longer than 30 sec
            // mHttpClient.getConnectionManager().closeIdleConnections(30,TimeUnit.SECONDS);
            return mHttpClient.execute(httpRequest);
        } catch (IOException e) {
            Log.d("mHttpClient", "///////");
            httpRequest.abort();
            throw e;
        }
    }

    public String checkValidateResponse(String result) {
        CommonUtil.LogLa(2, "checkValidateResponse result is " + result);
        checkValidateResponseJSONObject(result);
        return result;
    }

    public JSONObject checkValidateResponseJSONObject(String result) {
        JSONObject jsonObject = null;
        if (result == null) return jsonObject;
        try {
            jsonObject = JSON.parseObject(result);
            if (jsonObject != null) {
                if (jsonObject.containsKey("success")) {
                    boolean suc = jsonObject.getBooleanValue("success");
                    if (!suc) {
                        if (jsonObject.containsKey("code")) {
                            int code = jsonObject.getIntValue("code");
                            String msg = "请求失败";
                            if (jsonObject.containsKey("msg")) msg = jsonObject.getString("msg");
                            logoutToLoginActity(code, msg);

//							if (code == 40008 || code == 40009) {
//								if (jsonObject.containsKey("msg")) {
//									if (ConfigTjrInfo.getInstance().getContext() != null) {
//										Intent intent = new Intent(TJRFilterConf.INTENT_ACTION_LOGGED_OUT);
//										intent.putExtra(TJRFilterConf.RESPONSE_CODE, code);
//										intent.putExtra(TJRFilterConf.RESPONSE_MSG, jsonObject.getString("msg"));
//										ConfigTjrInfo.getInstance().getContext().sendBroadcast(intent);
//										return null;
//									}
//								}
//							} else if (code == 40024) {
//								if (jsonObject.containsKey("msg")) {
//									if (ConfigTjrInfo.getInstance().getContext() != null) {
//										Intent intent = new Intent(TJRFilterConf.INTENT_ACTION_LOGGED_BINDPHONE);
//										intent.putExtra(TJRFilterConf.RESPONSE_CODE, code);
//										intent.putExtra(TJRFilterConf.RESPONSE_MSG, jsonObject.getString("msg"));
//										ConfigTjrInfo.getInstance().getContext().sendBroadcast(intent);
//										return null;
//									}
//								}
//							}
                        }
                    }
                }
                return jsonObject;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    /**
     * 其他
     * 40040没有淘金豆时就需要跳转
     *
     * @param code
     * @param msg
     */
    public void logoutToLoginActity(int code, String msg) {
        CommonUtil.LogLa(2, "OnLogoutReceiver code is " + code + " msg is " + msg);
        if (code == 40008 || code == 40009) {
            if (ConfigTjrInfo.getInstance().getContext() != null) {
                Intent intent = new Intent(TJRFilterConf.INTENT_ACTION_LOGGED_OUT);
                intent.putExtra(TJRFilterConf.RESPONSE_CODE, code);
                intent.putExtra(TJRFilterConf.RESPONSE_MSG, msg);
                ConfigTjrInfo.getInstance().getContext().sendBroadcast(intent);
            }
        } else if (code == 40040) {
            CommonUtil.LogLa(2, "OnLogoutReceiver code is 1");
            if (ConfigTjrInfo.getInstance().getContext() != null) {
                CommonUtil.LogLa(2, "OnLogoutReceiver code is 2");
                Intent intent = new Intent(TJRFilterConf.INTENT_ACTION_RECHARGE_BEAN);
                intent.putExtra(TJRFilterConf.RESPONSE_CODE, code);
                intent.putExtra(TJRFilterConf.RESPONSE_MSG, msg);
                ConfigTjrInfo.getInstance().getContext().sendBroadcast(intent);
                CommonUtil.LogLa(2, "OnLogoutReceiver code is 3");
            }
        }
    }

    // public static final DefaultHttpClient createHttpClient() {
    // final BasicHttpParams httpParams = new BasicHttpParams();
    // ConnManagerParams.setTimeout(httpParams, socketTimeout);
    // ConnManagerParams.setMaxConnectionsPerRoute(httpParams, new
    // ConnPerRouteBean(maxConnections));
    // ConnManagerParams.setMaxTotalConnections(httpParams,
    // DEFAULT_MAX_CONNECTIONS);
    //
    // HttpConnectionParams.setSoTimeout(httpParams, socketTimeout);
    // HttpConnectionParams.setConnectionTimeout(httpParams, socketTimeout);
    // HttpConnectionParams.setTcpNoDelay(httpParams, true);
    // HttpConnectionParams.setSocketBufferSize(httpParams,
    // DEFAULT_SOCKET_BUFFER_SIZE);
    // HttpConnectionParams.setStaleCheckingEnabled(httpParams, false);
    //
    // HttpProtocolParams.setUseExpectContinue(httpParams, false);
    // HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
    // HttpProtocolParams.setUserAgent(httpParams,
    // String.format("taojinroad-http/%s (http://www.taojinroad.com)",
    // VERSION));
    //
    // final SchemeRegistry schemeRegistry = new SchemeRegistry();
    // schemeRegistry.register(new Scheme("http",
    // PlainSocketFactory.getSocketFactory(), 80));
    // schemeRegistry.register(new Scheme("https",
    // SSLSocketFactory.getSocketFactory(), 443));
    // final ThreadSafeClientConnManager cm = new
    // ThreadSafeClientConnManager(httpParams, schemeRegistry);
    // DefaultHttpClient httpClient = new DefaultHttpClient(cm, httpParams);
    // httpClient.addRequestInterceptor(new HttpRequestInterceptor() {
    // @Override
    // public void process(HttpRequest request, HttpContext context) {
    // if (!request.containsHeader(HEADER_ACCEPT_ENCODING)) {
    // request.addHeader(HEADER_ACCEPT_ENCODING, ENCODING_GZIP);
    // }
    // }
    // });
    //
    // httpClient.addResponseInterceptor(new HttpResponseInterceptor() {
    // @Override
    // public void process(HttpResponse response, HttpContext context) {
    // final HttpEntity entity = response.getEntity();
    // if (entity == null) {
    // return;
    // }
    // final Header encoding = entity.getContentEncoding();
    // if (encoding != null) {
    // for (HeaderElement element : encoding.getElements()) {
    // if (element.getName().equalsIgnoreCase(ENCODING_GZIP)) {
    // response.setEntity(new InflatingEntity(response.getEntity()));
    // break;
    // }
    // }
    // }
    // }
    // });
    // httpClient.setHttpRequestRetryHandler(new
    // RetryHandler(DEFAULT_MAX_RETRIES));
    // httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
    // public long getKeepAliveDuration(HttpResponse response, HttpContext
    // context) {
    // // Honor 'keep-alive' header
    // CommonUtil.LogLa(4, "-------------设置keep-alive------------");
    // HeaderElementIterator it = new
    // BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
    // while (it.hasNext()) {
    // HeaderElement he = it.nextElement();
    // String param = he.getName();
    // String value = he.getValue();
    // if (value != null && param.equalsIgnoreCase("timeout")) {
    // try {
    // return Long.parseLong(value) * 1000;
    // } catch (NumberFormatException ignore) {
    // }
    // }
    // }
    // return 30000;
    // }
    // });
    // return httpClient;
    // }
    //
    // private static class InflatingEntity extends HttpEntityWrapper {
    // public InflatingEntity(HttpEntity wrapped) {
    // super(wrapped);
    // }
    //
    // @Override
    // public InputStream getContent() throws IOException {
    // return new GZIPInputStream(wrappedEntity.getContent());
    // }
    //
    // @Override
    // public long getContentLength() {
    // return -1;
    // }
    // }

    /**
     * Create a thread-safe client. This client does not do redirecting, to
     * allow us to capture correct "error" codes.
     *
     * @return HttpClient
     */
    public static final DefaultHttpClient createHttpClient() {
        // Sets up the http part of the service.
        final SchemeRegistry supportedSchemes = new SchemeRegistry();

        // Register the "http" protocol scheme, it is required
        // by the default operator to look up socket factories.
        final SocketFactory sf = PlainSocketFactory.getSocketFactory();
        final SSLSocketFactory sslf = SSLSocketFactory.getSocketFactory();
        sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        supportedSchemes.register(new Scheme("http", sf, 80));

//        try {
//            SSLSocketFactory sslf = new SSLSocketFactory(SSLContext.getInstance("TLS"), SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Set some client http client parameter defaults.
        final HttpParams httpParams = createHttpParams();
        HttpClientParams.setRedirecting(httpParams, true);

        final ClientConnectionManager ccm = new ThreadSafeClientConnManager(httpParams, supportedSchemes);
        return new DefaultHttpClient(ccm, httpParams);
    }



    public static final OkHttpClient createOkHttpClient(){
        OkHttpClient.Builder builder=new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT * 1000, TimeUnit.MILLISECONDS);
//        builder.writeTimeout(TIMEOUT * 1000, TimeUnit.MILLISECONDS);
//        builder.readTimeout(TIMEOUT * 1000, TimeUnit.MILLISECONDS);
//        builder.setStaleCheckingEnabled(params, false);
//        builder.setConnectionTimeout(params, TIMEOUT * 1000);
//        builder.setSoTimeout(params, TIMEOUT * 1000);
//        builder.setSocketBufferSize(params, DEFAULT_SOCKET_BUFFER_SIZE);
//        builder.setVersion(params, HttpVersion.HTTP_1_1);
        return builder.build();
    }

    /**
     * Create the default HTTP protocol parameters.
     */
    private static final HttpParams createHttpParams() {
        final HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSocketBufferSize(params, DEFAULT_SOCKET_BUFFER_SIZE);

        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        return params;
    }

    /**
     * Create a thread-safe client. This client does not do redirecting, to
     * allow us to capture correct "error" codes.
     *
     * @return HttpClient
     */
    // public static final DefaultHttpClient createHttpClient() {
    // // Sets up the http part of the service.
    // final SchemeRegistry supportedSchemes = new SchemeRegistry();
    // // Register the "http" protocol scheme, it is required
    // // by the default operator to look up socket factories.
    // final SocketFactory sf = PlainSocketFactory.getSocketFactory();
    // supportedSchemes.register(new Scheme("http", sf, 80));
    // supportedSchemes.register(new Scheme("https",
    // SSLSocketFactory.getSocketFactory(), 443));
    // // Set some client http client parameter defaults.
    // final HttpParams httpParams = createHttpParams();
    // HttpClientParams.setRedirecting(httpParams, true);
    // final ClientConnectionManager ccm = new
    // ThreadSafeClientConnManager(httpParams, supportedSchemes);
    // DefaultHttpClient httpClient = new DefaultHttpClient(ccm, httpParams);
    // // 新增test
    // // httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
    // // public long getKeepAliveDuration(HttpResponse response, HttpContext
    // context) {
    // // // Honor 'keep-alive' header
    // // HeaderElementIterator it = new
    // BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
    // // while (it.hasNext()) {
    // // HeaderElement he = it.nextElement();
    // // String param = he.getName();
    // // String value = he.getValue();
    // // if (value != null && param.equalsIgnoreCase("timeout")) {
    // // try {
    // // return Long.parseLong(value) * 1000;
    // // } catch (NumberFormatException ignore) {
    // // }
    // // }
    // // }
    // // HttpHost target = (HttpHost)
    // context.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
    // // if ("www.naughty-server.com".equalsIgnoreCase(target.getHostName())) {
    // // // Keep alive for 5 seconds only
    // // return 5 * 1000;
    // // } else {
    // // // otherwise keep alive for 30 seconds
    // // return 30 * 1000;
    // // }
    // // }
    // // });
    // return httpClient;
    // }

    // /**
    // * Create the default HTTP protocol parameters.
    // */
    // private static final HttpParams createHttpParams() {
    // final HttpParams params = new BasicHttpParams();
    //
    // ConnManagerParams.setMaxTotalConnections(params, 1024);//
    // // Turn off stale checking. Our connections break all the time anyway,
    // // and it's not worth it to pay the penalty of checking every time.
    // HttpConnectionParams.setStaleCheckingEnabled(params, false);
    //
    // HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
    // HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
    // HttpConnectionParams.setTcpNoDelay(params, true);
    // HttpConnectionParams.setSocketBufferSize(params, 8192);
    // // 新增
    // // HttpConnectionParams.setStaleCheckingEnabled(params, true);
    // //
    // //参数设置是否启用旧连接检查，默认是开启的。关闭这个旧连接检查可以提高一点点性能，但是增加了I/O错误的风险（当服务端关闭连接时）。开启这个选项则在每次使用老的连接之前都会检查连接是否可用，这个耗时大概在15-30ms之间
    // // HttpConnectionParams.setLinger(params, -1);
    // // //设置socket延迟关闭时间，值为0表示这个选项是关闭的，值为-1表示使用JRE的默认设置
    // // HttpConnectionParams.setTcpNoDelay(params, true);
    // //
    // //设置是否启用Nagle算法，设置true后禁用Nagle算法，默认为false（即默认启用Nagle算法）。Nagle算法试图通过减少分片的数量来节省带宽。当应用程序希望降低网络延迟并提高性能时，它们可以关闭Nagle算法，这样数据将会更早地发送，但是增加了网络消耗
    // // HttpProtocolParams.setUserAgent(params,
    // //
    // "Mozilla/5.0 (Linux; U; Android 2.2; zh-cn; Desire_A8181 Build/FRF91) AppleWebKit/533.1 (KHTML, likeGecko) Version/4.0 Mobile Safari/533.1");
    // HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    // HttpProtocolParams.setContentCharset(params,
    // HTTP.DEFAULT_CONTENT_CHARSET);
    // HttpProtocolParams.setUseExpectContinue(params, true);
    // return params;
    // }

    // public void setHttpClientProxy(String host, int port, String scheme) {
    // HttpHost proxy = new HttpHost(host, port, scheme);
    // mHttpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY,
    // proxy);
    // }
    //
    // public void removeHttpClientProxy() {
    // mHttpClient.getParams().removeParameter(ConnRoutePNames.DEFAULT_PROXY);
    // }
}
