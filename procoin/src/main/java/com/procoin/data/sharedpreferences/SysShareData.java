package com.procoin.data.sharedpreferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.procoin.http.base.Group;
import com.procoin.module.home.entity.Market;
import com.procoin.module.home.entity.SubUser;

import java.util.Map;

/**
 * 这个文件是用来保存状态， 用来防止用户注销后被清理掉
 *
 * @author zhengmj
 */
public class SysShareData {

    private final static String TJRSYSDATA = "coingosys";
    private final static String TJRVERSION = "coingoversion";

    private final static String TOKASEARCHHISTORY = "searchhistory";//搜索历史专用

    private final static String SEARCHCOINHISTORY = "searchCoinHistory";//搜索币种历史专用
    private final static String DYNAMIC_COUNT = "dynamic";//搜索历史专用
    private final static String SAVE_DYNMIC_CONTENT = "savedynamic";//保存最新的那一页动态

    /**
     * 获取user的信息
     *
     * @param
     * @return
     */
    public static SharedPreferences getSysSharedPreferences(Context context) {
        if (context == null) return null;
        return context.getSharedPreferences(TJRSYSDATA, Context.MODE_PRIVATE);// 表示当前文件可以被其他应用读取， Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE
    }


    /**
     * @return
     */
    public static String getVersion(Context context) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getString(TJRVERSION, "");
        return "";
    }

    /**
     * @param
     */
    public static void saveVersion(Context context, String version) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(TJRVERSION, version);
        // 一定要提交
        editor.commit();
    }


    /**
     * 获取内容情况
     *
     * @return
     */
    public static String getSharedDate(Context context, String key) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata != null) return sharedata.getString(key, "");
        return "";
    }

    /**
     * 获取内容情况
     *
     * @return
     */
    public static void setSharedDate(Context context, String key, String value) {
        SharedPreferences sharedata = getSysSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(key, value);
        // 一定要提交
        editor.commit();
    }


    //保存动态消息（int count,String headUrl,String tips）
    public static void saveSocialNews(Context context, int count, String headUrl, String tips, String userId) {
        SharedPreferences shareData = context.getSharedPreferences(DYNAMIC_COUNT + userId, Context.MODE_PRIVATE);
        if (shareData == null) return;
        Editor editor = shareData.edit();
        editor.putString(userId, count + "," + headUrl + "," + tips);
        editor.commit();
    }

    //获得动态消息
    public static Map<String, String> getSocialNews(Context context, String userId) {
        SharedPreferences shareData = context.getSharedPreferences(DYNAMIC_COUNT + userId, Context.MODE_PRIVATE);
        return (Map<String, String>) shareData.getAll();
    }

    //清除动态消息
    public static void cleanSocialNews(Context context, String userId) {
        SharedPreferences shareData = context.getSharedPreferences(DYNAMIC_COUNT + userId, Context.MODE_PRIVATE);
        Editor editor = shareData.edit();
        editor.clear();
        editor.commit();
    }

    //保存第一页动态
    public static void saveLastestDynamicContent(Context context, String userId, String json) {
        SharedPreferences shareData = context.getSharedPreferences(SAVE_DYNMIC_CONTENT + userId, Context.MODE_PRIVATE);
        if (shareData == null) return;
        Editor editor = shareData.edit();
        editor.putString(userId, json);
        editor.commit();
    }

    //获取第一页动态
    public static String getLastestDynamicContent(Context context, String userId) {
        SharedPreferences shareData = context.getSharedPreferences(SAVE_DYNMIC_CONTENT + userId, Context.MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) shareData.getAll();
        if (map != null && map.size() == 1) {
            for (Map.Entry<String, String> e : map.entrySet()) {
                String json = e.getValue();
                return json;
            }
        }
        return null;
    }

    public static void cleanLocalDynamic(Context context, String userId) {
        SharedPreferences shareData = context.getSharedPreferences(SAVE_DYNMIC_CONTENT + userId, Context.MODE_PRIVATE);
        Editor editor = shareData.edit();
        editor.clear();
        editor.commit();
    }


    //保存搜索历史 (用于搜索建议接口)
    public static void appendSearchHistory(Context context, SubUser e, String userId) {
        if (e == null) return;
        SharedPreferences sharedata = context.getSharedPreferences(TOKASEARCHHISTORY + userId, Context.MODE_PRIVATE);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(String.valueOf(e.userId), e.userId + "," + e.userName + "," + e.headUrl + "," + System.currentTimeMillis());
        editor.commit();
    }

    //    //获取搜索历史
    public static Group getSearchHistory(Context context, String userId) {
        SharedPreferences sharedata = context.getSharedPreferences(TOKASEARCHHISTORY + userId, Context.MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) sharedata.getAll();
        Group group = null;
        if (map.size() > 0) {
            group = new Group();
            for (Map.Entry<String, String> e : map.entrySet()) {
                try {
                    String[] content = e.getValue().split(",");
                    group.add(new SubUser(Long.parseLong(content[0]), content[1], content[2], Long.parseLong(content[3])));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return group;
    }

    //清楚搜索历史
    public static void cleanSearchHistory(Context context, String userId) {
        SharedPreferences sharedata = context.getSharedPreferences(TOKASEARCHHISTORY + userId, Context.MODE_PRIVATE);
        Editor editor = sharedata.edit();
        editor.clear();
        editor.commit();
    }


    //保存搜索币种历史
    public static void appendSearchCoinHistory(Context context, Market e, String userId) {
        if (e == null) return;
        SharedPreferences sharedata = context.getSharedPreferences(SEARCHCOINHISTORY + userId, Context.MODE_PRIVATE);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(String.valueOf(e.symbol), e.symbol + "," + e.name + "," + System.currentTimeMillis());
        editor.commit();
    }

    //    //获取搜索历史
    public static Group getSearchCoinHistory(Context context, String userId) {
        SharedPreferences sharedata = context.getSharedPreferences(SEARCHCOINHISTORY + userId, Context.MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) sharedata.getAll();
        Group group = null;
        if (map.size() > 0) {
            group = new Group();
            for (Map.Entry<String, String> e : map.entrySet()) {
                try {
                    String[] content = e.getValue().split(",");
                    if (content.length == 2) {
                        group.add(new Market(content[0], Long.parseLong(content[1])));
                    } else if (content.length == 3) {//新增加的name
                        group.add(new Market(content[0], content[1], Long.parseLong(content[2])));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        return group;
    }

    //清楚搜索历史
    public static void cleanSearchCoinHistory(Context context, String userId) {
        SharedPreferences sharedata = context.getSharedPreferences(SEARCHCOINHISTORY + userId, Context.MODE_PRIVATE);
        Editor editor = sharedata.edit();
        editor.clear();
        editor.commit();
    }


}
