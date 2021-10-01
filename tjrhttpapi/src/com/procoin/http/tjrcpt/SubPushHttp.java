package com.procoin.http.tjrcpt;

import com.procoin.http.HttpApi;

import org.apache.http.message.BasicNameValuePair;

public class SubPushHttp {
    private static SubPushHttp instance;
    private static final String DATATYPE = ".do";
    private static final String SOCKET_API_CONNECT = "/connect";
    private static final String SOCKET_API_DATA_COMMAND = "/data/command";
    /**
     * 以下是闪电预测
     */
    private static final String PREDICT_LOGIN = "/login";
    private static final String PREDICT_USER = "/user";
    private static final String PREDICT_TICKETS = "/tickets";
    private static final String PREDICT_VOTE = "/vote";
    private static final String PREDICT_CHAT = "/chat";

    private final HttpApi mHttpApi;

    private SubPushHttp() {
        mHttpApi = new HttpApi();
    }

    public HttpApi getHttpApi() {
        return mHttpApi;
    }

    public static SubPushHttp getInstance() {
        if (instance == null) {
            synchronized (SubPushHttp.class) {
                if (instance == null) instance = new SubPushHttp();
            }
        }
        return instance;
    }


    public String connectUrl(long userId) {
        return mHttpApi.createUrl(SOCKET_API_CONNECT,
                new BasicNameValuePair("userId", String.valueOf(userId)));
    }

    public String connectGetDataUrl(Long userId) {
        return mHttpApi.createUrl(SOCKET_API_DATA_COMMAND,//
                new BasicNameValuePair("userId", String.valueOf(userId)));
    }


    /**
     * 闪电预测链接
     * @param userId
     * @return
     */
    public String predictLogin(long userId) {
        return mHttpApi.createUrl(PREDICT_LOGIN,
                new BasicNameValuePair("userId", String.valueOf(userId)));
    }

    /**
     * 闪电预测投票
     *
     * @param userId
     * @param price 券值的大小
     * @param vote 1或-1
     * @return
     */
    public String predictVote(long userId,String price,int vote) {
        return mHttpApi.createUrl(PREDICT_VOTE,
                new BasicNameValuePair("userId", String.valueOf(userId)),new BasicNameValuePair("price", price),new BasicNameValuePair("vote", String.valueOf(vote)));
    }

    /**
     * 闪电预测聊天
     * @param userId
     * @return
     */
    public String predictChat(long userId, String say) {
        return mHttpApi.createUrl(PREDICT_CHAT,
                new BasicNameValuePair("userId", String.valueOf(userId)),new BasicNameValuePair("say", say));
    }
}



