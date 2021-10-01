package com.procoin.http.tjrcpt;

import com.procoin.http.TjrBaseApi;
import com.procoin.http.HttpApi;

import org.apache.http.message.BasicNameValuePair;

/**
 * 这个是圈子发送用到
 * Created by zhengmj on 17-7-13.
 */

public class CircleChatHttp {

    private static CircleChatHttp instance;
    private final String mApiTjrCircle;
    private final HttpApi mHttpApi;


    private CircleChatHttp() {
        mHttpApi = new HttpApi();
        mApiTjrCircle = TjrBaseApi.mApiCropymeBaseUri.uri();
    }

    public static CircleChatHttp getInstance() {
        if (instance == null) {
            synchronized (CircleChatHttp.class) {
                if (instance == null) instance = new CircleChatHttp();
            }
        }
        return instance;
    }

    private String tjrfundUrl(String url) {
        return mApiTjrCircle + url + ".do";
    }


    /**
     * 圈子发送文字
     *
     * @param userId
     * @param circleId
     * @param say
     * @param verify
     * @return
     */
    public String circleChatsendText(Long userId, String circleId, String say, String verify, String type) {
        return mHttpApi.createUrl("/circle/send",//
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("circleId", circleId),
                new BasicNameValuePair("verify", verify),
                new BasicNameValuePair("say", say),
                new BasicNameValuePair("type", type)

        );
    }

    /**
     * 圈子发送图片
     *
     * @param userId
     * @param circleId
     * @param fileUrl   图片路径
     * @param imgWidth  图片宽
     * @param imgHeight 图片高
     * @param verify
     * @param type
     * @return
     */
    public String circleChatsendImg(Long userId, String circleId, String fileUrl, int imgHeight, int imgWidth, String verify, String type) {
        return mHttpApi.createUrl("/circle/send",//
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("circleId", circleId),
                new BasicNameValuePair("verify", verify),
                new BasicNameValuePair("fileUrl", fileUrl),
                new BasicNameValuePair("imgWidth", String.valueOf(imgWidth)),
                new BasicNameValuePair("imgHeight", String.valueOf(imgHeight)),
                new BasicNameValuePair("type", type)

        );
    }

    /**
     * 私聊发送语音
     *
     * @param userId
     * @param fileUrl
     * @param second  语音时长 单位秒
     * @param verify,
     * @param type
     * @return
     */
    public String circleChatsendVoice(Long userId, String circleId, String fileUrl, int second, String verify, String type) {
        return mHttpApi.createUrl("/circle/send",//
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("circleId", circleId),
                new BasicNameValuePair("verify", verify),
                new BasicNameValuePair("fileUrl", fileUrl),
                new BasicNameValuePair("second", String.valueOf(second)),
                new BasicNameValuePair("type", type)
        );
    }

    /**
     * 同步圈子mark 后台返回我的圈子列表
     * @param userId
     * @param circleIdAndSynMark
     * @return
     */
    public String sendSynjoin(Long userId, String circleIdAndSynMark) {
        return mHttpApi.createUrl("/circle/synjoin",//
                new BasicNameValuePair("circleIdAndSynMark", circleIdAndSynMark),
                new BasicNameValuePair("userId", String.valueOf(userId))
        );
    }

    /**
     * 进入圈子调用
     * @param userId
     * @param circleId
     * @return
     */
    public String circleInto(Long userId, String circleId) {
        return mHttpApi.createUrl("/circle/into",//
                new BasicNameValuePair("circleId", circleId),
                new BasicNameValuePair("userId", String.valueOf(userId))
        );
    }

}
