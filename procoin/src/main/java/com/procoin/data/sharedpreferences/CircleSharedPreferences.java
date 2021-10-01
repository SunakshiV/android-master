package com.procoin.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.procoin.subpush.notify.TPushSettingManager;

/**
 * Created by zhengmj on 16-1-20.
 */
public class CircleSharedPreferences {

    private static final String CIRCLE_SP_CHAT_ROOMID = "circle_chat_roomid";//临时保存当前聊天的页面圈号

    private static final String CIRCLE_SP_PARTY_FILENAME = "circle_party";//保存聚会的文件名

    private static final String CIRCLE_CHAT_NEWS_FILENAME = "circle_chat_news";//保存私聊消息数量的文件名

    private static final String CIRCLE_PRIVATE_CHAT_NEWS_FILENAME = "circle_private_chat_news";//保存私聊消息数量的文件名

    private static final String CIRCLE_CHAT_NAME_FILENAME = "circle_chat_name";//保存圈聊名字的文件名

    private static final String CIRCLE_SETTING_FILENAME = "circle_setting";//保存圈聊名字的文件名

    private static final String CIRCLE_SP_GAME_FILENAME = "circle_game";//保存圈比赛的文件名

    private static final String CIRCLE_CHAT_AT_FILENAME = "circle_chat_at_name";//有人@我

    private static final String CIRCLE_CHAT_APPLY = "circle_chat_apply";//成员申请数量



    /**
     * 获取最新进入的是哪个圈子
     *
     * @param context
     * @return
     */
    public static String getCircleSpChatRoomid(Context context) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SP_CHAT_ROOMID, Context.MODE_PRIVATE);
        return sp.getString("chat_roomid", "0");
    }

    /**
     * 保存最新进入的是哪个圈子以便push过来的时候可以判断 聊天页面退出或者onTaskRemoved 的时候要清零
     */
    public static void saveCircleSpChatRoomid(Context context, String circleNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SP_CHAT_ROOMID, Context.MODE_PRIVATE);
        sp.edit().putString("chat_roomid", circleNum).commit();//
    }

    /**
     * 获取圈子聚会的缓存
     *
     * @param context
     * @return
     */
    public static String getCircleParty(Context context, String circleNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SP_PARTY_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(circleNum, null);
    }

    /**
     * 保存圈子聚会
     */
    public static void saveCircleParty(Context context, String circleNum, String value) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SP_PARTY_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putString(circleNum, value).commit();// cricleParty+圈号作为主键
    }

    /**
     * 更新私聊的消息数量
     *
     * @param context
     */
    public static void updateChatRecordNum(Context context, String circleNum, long userId, int chatNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        int n = sp.getInt(circleNum + userId, -1);
        if (chatNum > 0 && n == -1) {
            sp.edit().putInt(circleNum + userId, 0).commit();// cricleParty+圈号作为主键
            return;//批量返回数据时，如果本地没有私聊数量，则不添加 ，不然的话用户每次卸载重装之后，会出现很多new
        }
        sp.edit().putInt(circleNum + userId, Math.max(0, chatNum + n)).commit();// cricleParty+圈号作为主键
    }

    /**
     * @param context
     * @param
     */
    public static void clearChatRecordNum(Context context, String circleNum, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putInt(circleNum + userId, 0).commit();// cricleParty+圈号作为主键
    }

    //获取私聊数量   key=圈号+userId
    public static int getChatRecordNum(Context context, String circleNum, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_NEWS_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt(circleNum + userId, 0);
    }

    /**
     * 获取圈子的聊天名字
     *
     * @param context
     * @param circleNum
     * @return
     */
    public static String getCircleChatName(Context context, String circleNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_NAME_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(circleNum, "");
    }

    /**
     * 保存圈子的聊天名字
     *
     * @param context
     * @param circleNum
     * @return
     */
    public static void saveCircleChatName(Context context, String circleNum, String value) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_NAME_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putString(circleNum, value).commit();
    }
//    /**
//     * 保存聚会提醒
//     * @param context
//     * @param userId
//     * @param value
//     */
//    public static void saveCircleSettingPartyRemind(Context context,long userId, String circleNum, boolean value){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//        sp.edit().putBoolean("party_" + circleNum+"_"+userId, value).commit();
//        TPushSettingManager.getInstance().savePartyReminde(userId, circleNum, value);
//    }
//
//    /**
//     * 获取聚会提醒
//     * @param context
//     * @param userId
//     * @return
//     */
//    public static boolean getCircleSettingPartyRemind(Context context,long userId, String circleNum){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//       return sp.getBoolean("party_" + circleNum+"_"+userId, true);
//    }
//    /**
//     * 保存私聊提醒
//     * @param context
//     * @param userId
//     * @param value
//     */
//    public static void saveCircleSettingChatRemind(Context context,long userId, String circleNum, boolean value){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//        sp.edit().putBoolean("chat_" + circleNum+"_"+userId,value).commit();
//        TPushSettingManager.getInstance().saveChatReminde(userId,circleNum,value);
//    }
//    /**
//     * 获取私聊提醒
//     * @param context
//     * @param userId
//     */
//    public static boolean getCircleSettingChatRemind(Context context,long userId, String circleNum){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//       return sp.getBoolean("chat_" + circleNum+"_"+userId,true);
//    }

//    /**
//     * 保存资讯提醒
//     * @param context
//     * @param userId
//     * @param value
//     */
//    public static void saveCircleSettingInfoRemind(Context context,long userId, String circleNum, boolean value){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//        sp.edit().putBoolean("info_" + circleNum+"_"+userId, value).commit();
//        TPushSettingManager.getInstance().saveInfoReminde(userId, circleNum, value);
//    }
//    /**
//     * 获取资讯提醒
//     * @param context
//     * @param userId
//     */
//    public static boolean getCircleSettingInfoRemind(Context context,long userId, String circleNum){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//        return sp.getBoolean("info_" + circleNum+"_"+userId,true);
//    }
//    /**
//     * 获取比赛提醒
//     * @param context
//     * @param userId
//     */
//    public static boolean getCircleSettingGameRemind(Context context,long userId, String circleNum){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//        return sp.getBoolean("game_" + circleNum+"_"+userId,true);
//    }
//    /**
//     * 保存比赛提醒
//     * @param context
//     * @param userId
//     * @param value
//     */
//    public static void saveCircleSettingGameRemind(Context context,long userId, String circleNum,boolean value){
//        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
//        sp.edit().putBoolean("game_" + circleNum+"_"+userId, value).commit();
//        TPushSettingManager.getInstance().saveGameReminde(userId, circleNum, value);
//    }

    /**
     * 获取圈子提醒  包括所有  返回true代表已经关闭圈子提醒
     *
     * @param context
     * @param userId
     */
    public static boolean getCircleSettingRemind(Context context, long userId, String circleNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
        return sp.getBoolean("circle_" + circleNum + "_" + userId, false);
    }

    /**
     * 圈子提醒  包括所有
     *
     * @param context
     * @param userId
     * @param value
     */
    public static void saveCircleSettingRemind(Context context, long userId, String circleNum, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SETTING_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean("circle_" + circleNum + "_" + userId, value).commit();
        TPushSettingManager.getInstance().saveCircleReminde(userId, circleNum, value);
    }

    /**
     * 获取圈子比赛的缓存
     *
     * @param context
     * @param circleNum
     * @return
     */
    public static String getCircleGame(Context context, String circleNum) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SP_GAME_FILENAME, Context.MODE_PRIVATE);
        return sp.getString(circleNum, null);
    }

    /**
     * 保存圈子比赛
     */
    public static void saveCircleGame(Context context, String circleNum, String value) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_SP_GAME_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putString(circleNum, value).commit();// cricleNum+userId作为主键
    }

    /**
     * 有人@我
     */
    public static void saveCircleAt(Context context, String circleNum, long userId, int value) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_AT_FILENAME, Context.MODE_PRIVATE);
        sp.edit().putInt(circleNum + "_" + userId, value).commit();// cricleNum+userId作为主键
    }

    /**
     * 获取有人@我
     */
    public static int getCircleAt(Context context, String circleNum, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_AT_FILENAME, Context.MODE_PRIVATE);
        return sp.getInt(circleNum + "_" + userId, 0);
    }


    /**
     * 保存成员申请数
     */
    public static void saveApplyCount(Context context, String circleNum, long userId, int value) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_APPLY, Context.MODE_PRIVATE);
        sp.edit().putInt(circleNum + "_" + userId, value).commit();// cricleNum+userId作为主键
    }


    /**
     * 获取成员申请数
     */
    public static int getApplyCount(Context context, String circleNum, long userId) {
        SharedPreferences sp = context.getSharedPreferences(CIRCLE_CHAT_APPLY, Context.MODE_PRIVATE);
        return sp.getInt(circleNum + "_" + userId, 0);
    }


}
