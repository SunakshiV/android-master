package com.procoin.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by zhengmj on 16-1-20.
 */
public class PrivateChatSharedPreferences {

    private static final String CIRCLE_PRIVATE_CHAT_NEWS_FILENAME = "circle_private_chat_news";//保存私聊消息数量的文件名
    private static final String CIRCLE_PRIVATE_CHAT_HISTORY = "circle_private_chat_history";//保存私聊记录


    /**
     * 更新私聊消息数量
     *
     * @param context
     */
    public static void updatePriChatRecordNum(Context context, String chatTopic, long userId, int chatNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_PRIVATE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int singleNum = sp.getInt(chatTopic + userId, 0);
        int allNum = sp.getInt("allNum" + userId, 0);   //私聊未读消息总数,跟单条消息一起更新减少IO
        editor.putInt(chatTopic + userId, singleNum + chatNum);// cricleParty+圈号作为主键
        editor.putInt("allNum" + userId, allNum + chatNum);// cricleParty+圈号作为主键
        editor.commit();
    }

    /**
     * 清除私聊消息数量
     *
     * @param context
     * @param
     */
    public static void clearPriChatRecordNum(Context context, String chatTopic, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_PRIVATE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int singleNum = sp.getInt(chatTopic + userId, 0);
        int allNum = sp.getInt("allNum" + userId, 0);   //私聊未读消息总数,跟单条消息一起更新减少IO
        editor.putInt(chatTopic + userId, 0);
        editor.putInt("allNum" + userId, allNum - singleNum);
        editor.commit();
    }

    /**
     * 获取私聊数量
     *
     * @param context
     * @param chatTopic
     * @param userId
     * @return
     */
    public static int getPriChatRecordNum(Context context, String chatTopic, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_PRIVATE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt(chatTopic + userId, 0);
    }


    /**
     * 获取总的私聊数量
     *
     * @param context
     * @param userId
     * @return
     */
    public static int getAllPriChatRecordNum(Context context, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_PRIVATE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt("allNum" + userId, 0);
    }


//    /**
//     * 当收到一条私聊消息，就记录到本地(如果不存在的话)
//     *
//     * @param context
//     */
//    public static void addChatRoom(Context context, long myUserId, long toUserId, String toName, String toHeadurl, String createTime) {
//        if (myUserId == toUserId) return;
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_PRIVATE_CHAT_HISTORY + myUserId, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(String.valueOf(CommonUtil.getChatTop(myUserId, toUserId)), toName + "_" + toHeadurl + "_" + createTime);
//        editor.commit();
//    }
//
//    public static Group<ChatRoom> getAllChatRoom(Context context, long userId) {
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_PRIVATE_CHAT_HISTORY + userId, Context.MODE_PRIVATE);
//        Map<String, ?> map = sp.getAll();
//        Group<ChatRoom> chatRooms = new Group<>();
//        ChatRoom chatRoom = null;
//        for (Map.Entry<String, ?> kvEntry : map.entrySet()) {
//            String value = (String) kvEntry.getValue();
//            if (!TextUtils.isEmpty(value)) {
//                String[] v = value.split("_");
//                if (v.length == 3) {//名字_头像_时间
//                    chatRoom = new ChatRoom(kvEntry.getKey(), v[0], v[1], v[2]);
//                    chatRooms.add(chatRoom);
//                }
//            }
//        }
//        return chatRooms;
//    }

}
