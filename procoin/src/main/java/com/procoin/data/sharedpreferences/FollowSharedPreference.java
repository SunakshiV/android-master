//package com.tjr.bee.data.sharedpreferences;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONArray;
//import FollowEntity;
//
//import java.util.ArrayList;
//import java.util.Iterator;
//
///**
// * 保存"关注"列表
// */
//
//public class FollowSharedPreference {
//    private final static String MY_COLLECTION = "my_collection";//存放我关注的网红
//
//    /**
//     * 保存指定userId 的关注
//     *
//     * @param context
//     * @param mUserId
//     * @param list 允许为 null
//     */
//    public static void saveFollowStars(Context context, String mUserId, ArrayList<FollowEntity> list) {
//        if (TextUtils.isEmpty(mUserId)) return;
//
//        SharedPreferences sp = context.getSharedPreferences(MY_COLLECTION, Context.MODE_PRIVATE);
//        StringBuilder key = new StringBuilder();
//        key.append(MY_COLLECTION).append(mUserId);
//        sp.edit().putString(key.toString(), JSON.toJSONString(list)).commit();
//    }
//
//    /**
//     * 获取指定userId 的关注
//     *
//     * @param context
//     * @param mUserId 指定userId
//     * @return
//     */
//    public static ArrayList<FollowEntity> getFollowStars(Context context, String mUserId) {
//        if (TextUtils.isEmpty(mUserId)) return null;
//
//        SharedPreferences sp = context.getSharedPreferences(MY_COLLECTION, Context.MODE_PRIVATE);
//        StringBuilder key = new StringBuilder();
//        key.append(MY_COLLECTION).append(mUserId);
//
//        return (ArrayList<FollowEntity>) JSONArray.parseArray(sp.getString(key.toString(), null), FollowEntity.class);
//    }
//
//    /**
//     * 商品是否保存在本地,即是否已关注
//     * @param context
//     * @param mUserId
//     * @param proCode 商品代码
//     * @return
//     */
//    public static boolean isMyFollow(Context context, String mUserId, String proCode) {
//        if (TextUtils.isEmpty(mUserId)  || TextUtils.isEmpty(proCode)) return false;
//
//        ArrayList<FollowEntity> list = FollowSharedPreference.getFollowStars(context, mUserId);
//        if (list != null) {
//            for (FollowEntity e : list) {
//                if (proCode.equals(e.prod_code)) return true;
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * 添加 or 删除 关注
//     *
//     * @param context
//     * @param mUserId 用户id
//     * @param isAdd   添加 or 删除
//     * @param proCode 添加 or 删除的商品代码
//     * @return
//     */
//    public static boolean updateFollowStars(Context context, String mUserId, boolean isAdd, String proCode) {
//        if (TextUtils.isEmpty(mUserId) || TextUtils.isEmpty(proCode)) return false;
//
//        ArrayList<FollowEntity> list = FollowSharedPreference.getFollowStars(context, mUserId);
//        if (isAdd) {
//            FollowEntity e = new FollowEntity();
//            e.prod_code = proCode;
//            if (null == list) {
//                list = new ArrayList<FollowEntity>();
//                list.add(e);
//            } else {
//                boolean hasIn = false;//是否已存在于列表中
//                for (FollowEntity m : list) {
//                    if (e.prod_code.equals(m.prod_code)) {
//                        hasIn = true;
//                        break;
//                    }
//                }
//                if (!hasIn) list.add(e);
//            }
//        } else {
//            if (null != list && 0 != list.size()) {
//                Iterator it = list.iterator();
//                while (it.hasNext()) {
//                    if (proCode.equals(((FollowEntity) it.next()).prod_code))
//                        it.remove();
//                }
//            }
//        }
//        FollowSharedPreference.saveFollowStars(context, mUserId, list);
//
//        return true;
//    }
//
//    /**
//     * 清除指定 userId 的数据
//     *
//     * @param context
//     * @param mUserId 指定userId
//     */
//    public static void remove(Context context, long mUserId) {
//        if (0 == mUserId) return;
//
//        SharedPreferences sp = context.getSharedPreferences(MY_COLLECTION, Context.MODE_PRIVATE);
//        StringBuilder key = new StringBuilder();
//        key.append(MY_COLLECTION).append(mUserId);
//        sp.edit().remove(key.toString()).commit();
//    }
//
//    /**
//     * 清除 MY_COLLECTION 中所有数据
//     */
//    public static void clearAll(Context context) {
//        SharedPreferences sp = context.getSharedPreferences(MY_COLLECTION, Context.MODE_PRIVATE);
//        sp.edit().clear().commit();
//    }
//}
