//package com.cropyme.http.tjrcpt;
//
//import java.io.File;
//import java.io.IOException;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import HttpApi;
//import TjrBaseApi;
//import TaojinluException;
//
//public class TjrthirdHttp {
//
//    private static TjrthirdHttp instance;
//    private static final String DATATYPE = ".do";
//    private static final String URL_API_WEIXIN_SHARE = "/weixinShare"; // 私聊
//    private final String mApiTjrthirdHttp;
//    private final HttpApi mHttpApi;
//
//    private TjrthirdHttp() {
//        mHttpApi = new HttpApi();
//        mApiTjrthirdHttp = TjrBaseApi.mApiTjrthirdUri.uri();
//    }
//
//    public static TjrthirdHttp getInstance() {
//        if (instance == null) {
//            synchronized (TjrthirdHttp.class) {
//                if (instance == null) instance = new TjrthirdHttp();
//            }
//        }
//        return instance;
//    }
//
//    private String tjrthirdHttpApi(String url) {
//        return mApiTjrthirdHttp + url + DATATYPE;
//    }
//
//    /**
//     * @param userId
//     * @param shareType weixin-klinegame
//     * @param content
//     * @param fileName
//     * @param fileurl
//     * @param isFile
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String shareWeiXinKline(String userId, String shareType, String content, String params, String fileName, String fileurl, int isFile) throws TaojinluException, IOException {
//        if (isFile == 0) {
//            return mHttpApi.doHttpPost(tjrthirdHttpApi(URL_API_WEIXIN_SHARE)//
//                    , new BasicNameValuePair("method", "shareWeiXin"),//
//                    new BasicNameValuePair("content", content),//
//                    new BasicNameValuePair("shareType", shareType),//
//                    new BasicNameValuePair("userId", String.valueOf(userId)),//
//                    new BasicNameValuePair("params", params),//
//                    new BasicNameValuePair("isFile", String.valueOf(isFile))//
//            );
//        } else if (isFile == 1) {
//            // 当有文件的时候
//            String sendUrl = mHttpApi.createUrl(tjrthirdHttpApi(URL_API_WEIXIN_SHARE), //
//                    new BasicNameValuePair("method", "shareWeiXin"),//
//                    new BasicNameValuePair("content", content),//
//                    new BasicNameValuePair("shareType", shareType),//
//                    new BasicNameValuePair("isFile", String.valueOf(isFile)),//
//                    new BasicNameValuePair("params", params),//
//                    new BasicNameValuePair("userId", String.valueOf(userId)));
//            File file = new File(fileurl);
//            return mHttpApi.uploadFile(sendUrl, file, fileName);
//        }
//        return null;
//    }
//
//    public String shareWeiXin(String shareType, String content, String params, String userId) throws TaojinluException, IOException {
//        return mHttpApi.doHttpPost(tjrthirdHttpApi(URL_API_WEIXIN_SHARE)//
//                , new BasicNameValuePair("method", "shareWeiXin"),//
//                new BasicNameValuePair("content", content),//
//                new BasicNameValuePair("shareType", shareType),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("isFile", String.valueOf("0")),//
//                new BasicNameValuePair("params", params)//
//        );
//    }
//
//}
