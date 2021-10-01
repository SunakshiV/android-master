package com.procoin.http.tjrcpt;

import com.procoin.http.TjrBaseApi;
import com.procoin.http.HttpApi;
import com.procoin.http.error.TaojinluException;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.IOException;

/**
 * Created by zhengmj on 17-7-13.
 */

public class PrivateChatHttp {
    private static final String URL_API_PRIVATE_CHAT_CREATE_CHAT_TOPIC = "/private_chat/create_chat_topic";

    private static PrivateChatHttp instance;
    private final String mApiTjrCircle;
    private final HttpApi mHttpApi;


    private PrivateChatHttp() {
        mHttpApi = new HttpApi();
//        mApiTjrCircle = TjrBaseApi.mApiPervalUri.uri();
        mApiTjrCircle = TjrBaseApi.mApiCropymeBaseUri.uri();
    }

    public static PrivateChatHttp getInstance() {
        if (instance == null) {
            synchronized (PrivateChatHttp.class) {
                if (instance == null) instance = new PrivateChatHttp();
            }
        }
        return instance;
    }

    private String tjrfundUrl(String url) {
        return mApiTjrCircle + url + ".do";
    }


    public String createChatTopic(long userId, long taUserId) throws TaojinluException, IOException {
        return mHttpApi.doHttpPost(tjrfundUrl(URL_API_PRIVATE_CHAT_CREATE_CHAT_TOPIC),
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("taUserId", String.valueOf(taUserId)),
                new BasicNameValuePair("method", "createChatTopic")
        );
    }

    /**
     * 私聊发送文字
     *
     * @param userId
     * @param chatTopic
     * @param say
     * @return
     */
    public String privateChatsendText(Long userId, String chatTopic, String say, String verify, String type) {
        return mHttpApi.createUrl("/chat/send",//
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("chatTopic", chatTopic),
                new BasicNameValuePair("verify", verify),
                new BasicNameValuePair("say", say),
                new BasicNameValuePair("type", type)

        );
    }

    /**
     * 私聊发送图片
     *
     * @param userId
     * @param chatTopic
     * @param fileUrl   图片路径
     * @param imgWidth  图片宽
     * @param imgHeight 图片高
     * @param verifi
     * @param type
     * @return
     */
    public String privateChatsendImg(Long userId, String chatTopic, String fileUrl, int imgHeight, int imgWidth, String verify, String type) {
        return mHttpApi.createUrl("/chat/send",//
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("chatTopic", chatTopic),
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
     * @param chatTopic
     * @param fileUrl
     * @param second    语音时长 单位秒
     * @param verifi
     * @param type
     * @return
     */
    public String privateChatsendVoice(Long userId, String chatTopic, String fileUrl, int second, String verify, String type) {
        return mHttpApi.createUrl("/chat/send",//
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("chatTopic", chatTopic),
                new BasicNameValuePair("verify", verify),
                new BasicNameValuePair("fileUrl", fileUrl),
                new BasicNameValuePair("second", String.valueOf(second)),
                new BasicNameValuePair("type", type)

        );
    }

//    /**
//     * 圈子发送文字
//     *
//     * @param userId
//     * @param circleNum
//     * @param say
//     * @param verifi
//     * @return
//     */
//    public String send(Long userId, String circleNum, String say, String verifi) {
//
//        return mHttpApi.createUrl(SOCKET_API_CHAT,//
//                new BasicNameValuePair("method", "send"), new BasicNameValuePair("userId", String.valueOf(userId)),
//                new BasicNameValuePair("circleNum", circleNum), new BasicNameValuePair("verifi", verifi), new
//                        BasicNameValuePair("say", say));
//    }

    /**
     * 私聊,发图片或者语音
     *
     * @param userId
     * @param chatTopic
     * @param type      img voice
     * @param second    语音时长
     * @param picLength
     * @param picWidth
     * @param verifi
     * @param file
     * @param fileName
     * @return
     * @throws TaojinluException
     * @throws IOException
     */
    public String privateChatsend(long userId, String chatTopic, String type, int second, int picLength, int picWidth, String
            verify, File file, String fileName) throws TaojinluException, IOException {
//		Log.d("t", "..updateAllUserInfo..type=" + type + "  bitmapFile=" + file + "  url=" + fullUrl(URL_API_USER));
        //long user_id, String chat_topic, String type, String pic_length, String pic_width, String second, String verifi
        String sendUrl = mHttpApi.createUrl(tjrfundUrl("/private_chat/send_File"),
                new BasicNameValuePair("userId", String.valueOf(userId)),
                new BasicNameValuePair("chatTopic", chatTopic),
                new BasicNameValuePair("type", type),
                new BasicNameValuePair("second", String.valueOf(second)),
                new BasicNameValuePair("picLength", String.valueOf(picLength)),
                new BasicNameValuePair("picWidth", String.valueOf(picWidth)),
                new BasicNameValuePair("verify", verify));
        return mHttpApi.uploadFile(sendUrl, file, fileName);
    }

    /**
     * 圈子发送个股
     *
     * @param userId
     * @param circleNum
     * @param say
     * @param verifi
     * @return
     */
    public String sendFdm(Long userId, String circleNum, String say, String verify) {

        return mHttpApi.createUrl("/chat/send_fdm",//
                new BasicNameValuePair("userId", String.valueOf(userId)), new BasicNameValuePair("chatTopic", circleNum), new BasicNameValuePair("verify", verify), new BasicNameValuePair("say", say));
    }


}
