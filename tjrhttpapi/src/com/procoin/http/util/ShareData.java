package com.procoin.http.util;

import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.procoin.http.model.User;
import com.procoin.http.common.CommonConst;
import com.procoin.http.common.TJrLoginTypeEnum;

public class ShareData {


    /**
     * 获取user的信息
     *
     * @param
     * @return
     */
    public static SharedPreferences getUserSharedPreferences(Context context) {
        return context.getSharedPreferences(CommonConst.SHARED_PREFERENCE_USER, Context.MODE_PRIVATE);// 表示当前文件可以被其他应用读取，
    }

    /**
     * 保存一般内容
     *
     * @param sharedata
     * @param map
     */
    public static void save(SharedPreferences sharedata, Map<String, String> map) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (map != null && map.size() > 0) {
            for (String key : map.keySet()) {
                if (key != null && map.get(key) != null) {
                    editor.putString(key, map.get(key).toString());
                }
            }
        }
        // 一定要提交
        editor.commit();
    }

    /**
     * 保存用户
     *
     * @param sharedata
     * @param account
     * @param password
     * @param loginType
     */
    public static void saveUser(SharedPreferences sharedata, String account, String password, String loginType, String sessionid, User user) {
        Log.d("successLoginTo", "1111111111111");
        if (sharedata == null) {
            Log.d("487", "sharedata == null");
            return;
        }
        Editor editor = sharedata.edit();
        if (account != null) editor.putString(CommonConst.USERACCOUNT, account);
        if (password != null) editor.putString(CommonConst.PASSWORD, password);
        if (loginType != null) editor.putString(CommonConst.LOGIN_TYPE, loginType);
        if (sessionid != null) editor.putString(CommonConst.TJR_SESSIONID, sessionid);
        Log.d("successLoginTo", "22222");
        if (user != null && user.getUserId() > 0) {

            Log.d("successLoginTo", "user.getUserId()==" + user.getUserId());

            editor.putLong(CommonConst.SHARED_PREFERENCE_USERID, user.getUserId());
            editor.putLong("synTime", user.getSynTime());
            editor.putInt("synVersion", user.getSynVersion());
            editor.putInt("sex", user.getSex());
            editor.putInt("isLock", user.getIsLock());
            editor.putInt("passErrNum", user.getPassErrNum());
            editor.putInt("otcCertify", user.getOtcCertify());
            editor.putInt("otcIssue", user.getOtcIssue());
            editor.putInt("idCertify", user.getIdCertify());

            editor.putString("synSign", user.getSynSign());
            editor.putString("phone", user.getPhone());

            editor.putLong("roomId",user.getRoomId());
            editor.putString("userName", user.getUserName());
            editor.putString("headUrl", user.getHeadUrl());
            editor.putString("bgUrl",user.getBgUrl());
            editor.putString("userPass", user.getUserPass());
            editor.putString("birthday", user.getBirthday());
            editor.putString("describes", user.getDescribes());
            editor.putString("createTime", user.getCreateTime());
            editor.putString("lastIp", user.getLastIp());
            editor.putString("lastLogin", user.getLastLogin());
            editor.putString("regPlatform", user.getRegPlatform());
            editor.putString("payPass", user.getPayPass());
            editor.putString("userRealName", user.getUserRealName());

//            if (user.getUserName() != null)editor.putString(CommonConst.SHARED_PREFERENCE_NAME, user.getUserName());
//            if (user.getHeadUrl() != null)editor.putString(CommonConst.SHARED_PREFERENCE_HEADURL, user.getHeadUrl());
//                editor.putString(CommonConst.SHARED_PREFERENCE_SEX, user.getSex());// 性别，0代表女性，1代表男性
//            if (user.getSelfDescription() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_SELFDESCRIPTION, user.getSelfDescription());// 自我描述
//            if (user.getPurview() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_PURVIEW, user.getPurview());// 1代表仅有自己可以见,2代表好友可见,3代表所有人可见
//            if (user.getBirthday() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_BIRTHDAY, user.getBirthday());// 生日
//            // 格式2011-2-1
//            if (user.getProfession() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_PROFESSION, user.getProfession());// 职业
//            if (user.getAddress() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_ADDRESS, user.getAddress());// 地址
//            editor.putInt(CommonConst.SHARED_PREFERENCE_STOCKAGE, user.getStockAge());// 股龄
//            if (user.getInvestmentStyle() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_INVESTMENTSTYLE, user.getInvestmentStyle());// 投资风格
//            // 1代表短线激进型,2代表中线波段型,3代表长线稳健型
//            editor.putInt(CommonConst.SHARED_PREFERENCE_ISVIP, user.getIsVip());// 是不是vip
//            if (user.getVipDesc() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_VIPDESC, user.getVipDesc());// vip描述
//
//            if (user.getBind_prod_code() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_GETBIND_PROD_CODE, user.getBind_prod_code());// vip描述
//
//            editor.putInt(CommonConst.SHARED_PREFERENCE_IS_SHOW_DY, user.getIs_show_dy());//
//
//            editor.putInt(CommonConst.SHARED_PREFERENCE_IDCERTIFY, user.getIdCertify());// 认证状态
//
//            if (user.getUserRealName() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_USERREALNAME, user.getUserRealName());// 真实姓名
//            if (user.getVerify_msg() != null)
//                editor.putString(CommonConst.SHARED_PREFERENCE_VERIFY_MSG, user.getVerify_msg());//认证状态字符串
        }
        Log.d("successLoginTo", "44444");
        // 一定要提交
        editor.commit();
        if (TJrLoginTypeEnum.qq.type().equals(loginType)) {//CommonConst.LOGIN_TYPE_QQ
            saveQQ(sharedata, account, password);
        } else if (TJrLoginTypeEnum.sinawb.type().equals(loginType)) {//CommonConst.LOGIN_TYPE_SINAWB
            saveWeibo(sharedata, account, password);
        } else if (TJrLoginTypeEnum.weixin.type().equals(loginType)) {//  CommonConst.LOGIN_TYPE_WEIXIN
            saveWeiXin(sharedata, account, password, null);
        }
    }

    /**
     * 保存用户
     *
     * @param sharedata
     */
    public static User getUser(SharedPreferences sharedata) {

        Log.d("successLoginTo", "555");
        if (sharedata == null) return null;
        Log.d("successLoginTo", "666");
        User user = new User();
        user.setUserId(sharedata.getLong(CommonConst.SHARED_PREFERENCE_USERID, 0));
        user.setSynTime(sharedata.getLong("synTime", 0));

        user.setSynVersion(sharedata.getInt("synVersion", 0));
        user.setSex(sharedata.getInt("sex", 0));
        user.setIsLock(sharedata.getInt("isLock", 0));
        user.setPassErrNum(sharedata.getInt("passErrNum", 0));
        user.setOtcCertify(sharedata.getInt("otcCertify", 0));
        user.setOtcIssue(sharedata.getInt("otcIssue", 0));
        user.setIdCertify(sharedata.getInt("idCertify", 0));

        user.setSynSign(sharedata.getString("synSign", ""));
        user.setPhone(sharedata.getString("phone", ""));

        user.setUserName(sharedata.getString("userName", ""));
        user.setHeadUrl(sharedata.getString("headUrl", ""));
        user.setBgUrl(sharedata.getString("bgUrl",""));
        user.setUserPass(sharedata.getString("userPass", ""));
        user.setBirthday(sharedata.getString("birthday", ""));
        user.setDescribes(sharedata.getString("describes", ""));
        user.setCreateTime(sharedata.getString("createTime", ""));
        user.setLastIp(sharedata.getString("lastIp", ""));
        user.setLastLogin(sharedata.getString("lastLogin", ""));
        user.setRegPlatform(sharedata.getString("regPlatform", ""));
        user.setPayPass(sharedata.getString("payPass", ""));
        user.setUserRealName(sharedata.getString("userRealName", ""));
        user.setRoomId(sharedata.getLong("roomId",0));

//        user.setUserAccount(sharedata.getString(CommonConst.USERACCOUNT, ""));
//        String uid = sharedata.getString(CommonConst.SHARED_PREFERENCE_USERID, "");
//        Log.d("successLoginTo", "uid==" + uid);
//        if (uid.matches("[0-9]+$")) user.setUserId(Long.parseLong(uid));
//        else user.setUserId(null);
//        user.setName(sharedata.getString(CommonConst.SHARED_PREFERENCE_NAME, ""));
//        user.setHeadurl(sharedata.getString(CommonConst.SHARED_PREFERENCE_HEADURL, ""));
//        user.setSex(sharedata.getString(CommonConst.SHARED_PREFERENCE_SEX, "1"));
//        user.setSelfDescription(sharedata.getString(CommonConst.SHARED_PREFERENCE_SELFDESCRIPTION, ""));
//        user.setPurview(sharedata.getString(CommonConst.SHARED_PREFERENCE_PURVIEW, "2"));// 1代表仅有自己可以见,2代表好友可见,3代表所有人可见
//        user.setBirthday(sharedata.getString(CommonConst.SHARED_PREFERENCE_BIRTHDAY, ""));
//        user.setProfession(sharedata.getString(CommonConst.SHARED_PREFERENCE_PROFESSION, ""));
//        user.setAddress(sharedata.getString(CommonConst.SHARED_PREFERENCE_ADDRESS, ""));
//        user.setStockAge(sharedata.getInt(CommonConst.SHARED_PREFERENCE_STOCKAGE, 0));
//        user.setInvestmentStyle(sharedata.getString(CommonConst.SHARED_PREFERENCE_INVESTMENTSTYLE, "1"));
//        user.setIsVip(sharedata.getInt(CommonConst.SHARED_PREFERENCE_ISVIP, 0));
//        user.setBind_prod_code(sharedata.getString(CommonConst.SHARED_PREFERENCE_GETBIND_PROD_CODE, ""));
//        user.setIs_show_dy(sharedata.getInt(CommonConst.SHARED_PREFERENCE_IS_SHOW_DY, 0));
//
//        if (user.getIsVip() == 1) {// 如果是vip就查出来desc
//            user.setVipDesc(sharedata.getString(CommonConst.SHARED_PREFERENCE_VIPDESC, ""));
//        }
        if (user.getUserId() > 0) {
            Log.d("successLoginTo", "777");
            return user;
        } else {
            Log.d("successLoginTo", "8888");
            return null;
        }
    }

    /**
     * 获取用户登录sessionid
     *
     * @param sharedata
     * @return
     */
    public static String getSessionid(SharedPreferences sharedata) {
        return sharedata.getString(CommonConst.TJR_SESSIONID, "");
    }


    /**
     * 保存微信
     *
     * @param sharedata
     * @param openId
     * @param wexinToken
     */
    public static void saveWeiXin(SharedPreferences sharedata, String openId, String wexinToken, String wexinRefreshToken) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (openId != null) editor.putString(CommonConst.WEIXIN_OPENID, openId);
        if (wexinToken != null) editor.putString(CommonConst.WEIXIN_TOKEN, wexinToken);
        if (wexinRefreshToken != null)
            editor.putString(CommonConst.WEIXIN_REFRESH_TOKEN, wexinRefreshToken);
        // 一定要提交
        editor.commit();
    }

    public static void saveWeiXin(SharedPreferences sharedata, String openId, String wexinToken, String wexinRefreshToken, String screenName) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (openId != null) editor.putString(CommonConst.WEIXIN_OPENID, openId);
        if (wexinToken != null) editor.putString(CommonConst.WEIXIN_TOKEN, wexinToken);
        if (screenName != null) editor.putString(CommonConst.WEIXIN_SCREENNAME, screenName);
        if (wexinRefreshToken != null)
            editor.putString(CommonConst.WEIXIN_REFRESH_TOKEN, wexinRefreshToken);
        // 一定要提交
        editor.commit();
    }

    /**
     * 保存QQ昵称
     *
     * @param sharedata
     * @param screenName 昵称
     */
    public static void saveWeiXinScreenName(SharedPreferences sharedata, String screenName) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (screenName != null) editor.putString(CommonConst.WEIXIN_SCREENNAME, screenName);
        // 一定要提交
        editor.commit();
    }

    public static void saveQQ(SharedPreferences sharedata, String openId, String qqToken) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (openId != null) editor.putString(CommonConst.QQ_OPENID, openId);
        if (qqToken != null) editor.putString(CommonConst.QQ_TOKEN, qqToken);
        // 一定要提交
        editor.commit();

    }

    public static void saveQQ(SharedPreferences sharedata, String openId, String qqToken, String screenName) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (openId != null) editor.putString(CommonConst.QQ_OPENID, openId);
        if (qqToken != null) editor.putString(CommonConst.QQ_TOKEN, qqToken);
        if (screenName != null) editor.putString(CommonConst.QQ_SCREENNAME, screenName);
        // 一定要提交
        editor.commit();
    }

    /**
     * 保存微博信息
     *
     * @param sharedata
     * @param weiboUID
     * @param weiboToken
     */

    public static void saveWeibo(SharedPreferences sharedata, String weiboUID, String weiboToken) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (weiboUID != null) editor.putString(CommonConst.WEIBO_UID, weiboUID);
        if (weiboToken != null) editor.putString(CommonConst.WEIBO_TOKEN, weiboToken);
        // 一定要提交
        editor.commit();
    }

    public static void saveWeibo(SharedPreferences sharedata, String weiboUID, String weiboToken, String screenName) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (weiboUID != null) editor.putString(CommonConst.WEIBO_UID, weiboUID);
        if (weiboToken != null) editor.putString(CommonConst.WEIBO_TOKEN, weiboToken);
        if (screenName != null) editor.putString(CommonConst.WEIBO_SCREENNAME, screenName);
        // 一定要提交
        editor.commit();
    }

    /**
     * 用户登录情况
     *
     * @param isFirstLogin
     */
    public static void saveIsFirstLogin(Context context, int isFirstLogin) {
        SharedPreferences sharedata = getUserSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putInt(CommonConst.ISFIRSTLOGIN, isFirstLogin);
        // 一定要提交
        editor.commit();
    }

    /**
     * 获取用户登录情况
     *
     * @return
     */
    public static int getIsFirstLogin(Context context) {
        SharedPreferences sharedata = getUserSharedPreferences(context);
        if (sharedata != null) return sharedata.getInt(CommonConst.ISFIRSTLOGIN, 1);
        return 1;
    }

    /**
     * 获取微电台登录情况
     *
     * @param sharedata
     * @return
     */
    public static int getIsTjr(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getInt(CommonConst.ISTJR, 1);
        return 1;
    }

    /**
     * 用户微电台登录情况
     *
     * @param sharedata
     * @param
     */
    public static void saveIsTjr(SharedPreferences sharedata, int isTjr) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putInt(CommonConst.ISTJR, isTjr);
        // 一定要提交
        editor.commit();
    }

    // /**
    // * 获取Contact情况
    // *
    // * @param sharedata
    // * @return
    // */
    // public static String getUpdateContactTime(SharedPreferences sharedata) {
    // if (sharedata != null) return
    // sharedata.getString(CommonConst.UPDATECONTACTTIME, "");
    // return "";
    // }
    //
    // /**
    // * 用户Contact情况
    // *
    // * @param sharedata
    // * @param isFirstLogin
    // */
    // public static void saveUpdateContactTime(SharedPreferences sharedata,
    // String time) {
    // if (sharedata == null) return;
    // Editor editor = sharedata.edit();
    // editor.putString(CommonConst.UPDATECONTACTTIME, time);
    // // 一定要提交
    // editor.commit();
    // }

    /**
     * 用户ContactId记录上传情况，可以用来保证增量上传
     *
     * @param sharedata
     * @return
     */
    public static String getUpdateContactID(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.UPDATECONTACTID, "0");
        return "0";
    }

    /**
     * 用户ContactId记录上传情况，可以用来保证增量上传
     *
     * @param sharedata
     * @param
     */
    public static void saveUpdateContactId(SharedPreferences sharedata, String contactId) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.UPDATECONTACTID, contactId);
        // 一定要提交
        editor.commit();
    }

    /**
     * 保存微博昵称
     *
     * @param sharedata
     * @param screenName 昵称
     */
    public static void saveWeiboScreenName(SharedPreferences sharedata, String screenName) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (screenName != null) editor.putString(CommonConst.WEIBO_SCREENNAME, screenName);
        // 一定要提交
        editor.commit();
    }

    /**
     * 保存QQ昵称
     *
     * @param sharedata
     * @param screenName 昵称
     */
    public static void saveQQScreenName(SharedPreferences sharedata, String screenName) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        if (screenName != null) editor.putString(CommonConst.QQ_SCREENNAME, screenName);
        // 一定要提交
        editor.commit();
    }

    /**
     * 删除微博信息
     *
     * @param sharedata
     */
    public static void delWeibo(SharedPreferences sharedata) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.WEIBO_UID, "");
        editor.putString(CommonConst.WEIBO_TOKEN, "");
        editor.putString(CommonConst.WEIBO_SCREENNAME, "");
        editor.commit();
    }

    /**
     * 删除QQ信息
     *
     * @param sharedata
     */
    public static void delWeiXin(SharedPreferences sharedata) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.WEIXIN_OPENID, "");
        editor.putString(CommonConst.WEIXIN_TOKEN, "");
        editor.putString(CommonConst.WEIXIN_REFRESH_TOKEN, "");
        editor.commit();
    }

    /**
     * 删除QQ信息
     *
     * @param sharedata
     */
    public static void delQQ(SharedPreferences sharedata) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.QQ_OPENID, "");
        editor.putString(CommonConst.QQ_TOKEN, "");
        editor.putString(CommonConst.QQ_SCREENNAME, "");
        editor.commit();
    }

    public static void delUser(SharedPreferences sharedata) {
        if (sharedata == null) return;
        int count = getIsCreateIcon(sharedata);
        String version = getVersionIntrod(sharedata);
        Editor editor = sharedata.edit();
        editor.clear();
        // editor.putString(CommonConst.USERACCOUNT, "");
        // editor.putString(CommonConst.PASSWORD, "");
        // editor.putString(CommonConst.LOGIN_TYPE, "");
        // editor.putString(CommonConst.WEIBO_SCREENNAME, "");
        // editor.putString(CommonConst.WEIBO_UID, "");
        // editor.putString(CommonConst.WEIBO_TOKEN, "");
        // editor.putString(CommonConst.SHARED_PREFERENCE_USERID, "");
        // editor.putString(CommonConst.SHARED_PREFERENCE_NAME, "");
        // editor.putString(CommonConst.SHARED_PREFERENCE_HEADURL, "");
        // 一定要提交
        editor.commit();
        saveIsCreateIcon(sharedata, count);
        saveVersionIntrod(sharedata, version);
    }

    /**
     * 获取用户登录类型
     *
     * @param sharedata
     * @return
     */
    public static String getLoginType(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.LOGIN_TYPE, "");
        return null;
    }

    /**
     * 获取当前用户登录账号
     *
     * @param sharedata
     * @return
     */
    public static String getNowUserAccount(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.USERACCOUNT, "");
        return null;
    }

    /**
     * 获取weibo token类型
     *
     * @param sharedata
     * @return
     */
    public static String getWeiboTocken(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.WEIBO_TOKEN, "");
        return null;
    }

    /**
     * 获取weibo uid类型
     *
     * @param sharedata
     * @return
     */
    public static String getWeiboUid(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.WEIBO_UID, "");
        return null;
    }

    /**
     * 获取qq token类型
     *
     * @param sharedata
     * @return
     */
    public static String getQQTocken(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.QQ_TOKEN, "");
        return null;
    }

    /**
     * 获取qq uid类型
     *
     * @param sharedata
     * @return
     */
    public static String getQQUid(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.QQ_OPENID, "");
        return null;
    }

    /**
     * 获取weibo 昵称
     *
     * @param sharedata
     * @return
     */
    public static String getWeiboScreenName(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.WEIBO_SCREENNAME, "");
        return null;
    }

    /**
     * 获取QQ 昵称
     *
     * @param sharedata
     * @return
     */
    public static String getQQScreenName(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.QQ_SCREENNAME, "");
        return null;
    }

    /**
     * 获取QQ 昵称
     *
     * @param sharedata
     * @return
     */
    public static String getWeiXinScreenName(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.WEIXIN_SCREENNAME, "");
        return null;
    }

    /**
     * 获取代表是否创建桌面图标，０代表创建，1代表不创建
     *
     * @param sharedata
     * @return
     */
    public static int getIsCreateIcon(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getInt(CommonConst.SHARED_PREFERENCE_COUNT, 0);
        return 0;
    }

    /**
     * 生成代表是否创建桌面图标，０代表创建，1代表不创建
     *
     * @param sharedata
     * @return
     */
    public static void saveIsCreateIcon(SharedPreferences sharedata, int count) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putInt(CommonConst.SHARED_PREFERENCE_COUNT, count);
        editor.commit();
    }

    /**
     * 获取是否显示版本介绍所图
     *
     * @param sharedata
     * @return
     */
    public static String getVersionIntrod(SharedPreferences sharedata) {
        if (sharedata != null)
            return sharedata.getString(CommonConst.SHARED_PREFERENCE_VERSION, "");
        return "";
    }

    /**
     * 生成是否显示版本介绍所图
     *
     * @param sharedata
     * @return
     */
    public static void saveVersionIntrod(SharedPreferences sharedata, String version) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.SHARED_PREFERENCE_VERSION, version);
        editor.commit();
    }

    /**
     * 获取是否显示版本介绍所图
     *
     * @param sharedata
     * @return
     */
    public static String getIsOpen(SharedPreferences sharedata) {
        if (sharedata != null)
            return sharedata.getString(CommonConst.SHARED_PREFERENCE_ISOPEN, "1");
        return "1";
    }

    /**
     * 生成是否显示版本介绍所图
     *
     * @param sharedata
     * @return
     */
    public static void saveIsOpen(SharedPreferences sharedata) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.SHARED_PREFERENCE_ISOPEN, "2");
        editor.commit();
    }

    /**
     * 获取用户刷新好友圈最后一次时间
     *
     * @param sharedata
     * @return
     */
    public static String getRefreshFriendsCircleLastTime(SharedPreferences sharedata) {
        if (sharedata != null)
            return sharedata.getString(CommonConst.REFRESH_CIRCLE_LAST_TIME, "0");
        return null;
    }

    /**
     * 保存用户最后刷新好友圈最后一次时间
     *
     * @param sharedata
     * @param msgTime
     */
    public static void saveRefreshFriendsCircleLastTime(SharedPreferences sharedata, String msgTime) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.REFRESH_CIRCLE_LAST_TIME, msgTime);
        editor.commit();
    }

    /**
     * 获取公告的时间
     *
     * @return
     */
    public static String getPlacardTime(Context context) {
        SharedPreferences sharedata = getUserSharedPreferences(context);
        if (sharedata != null)
            return sharedata.getString(CommonConst.SHARED_PREFERENCE_PLACARDTIME, "0");
        return "0";
    }

    /**
     * 获取公告的时间
     *
     * @return
     */
    public static void savePlacardTime(Context context, String placardTime) {
        SharedPreferences sharedata = getUserSharedPreferences(context);
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.SHARED_PREFERENCE_PLACARDTIME, placardTime);
        editor.commit();
    }

    /**
     * 定时更新好友的时间
     *
     * @param sharedata
     * @return
     */
    public static String getFrTime(SharedPreferences sharedata) {
        if (sharedata != null) return sharedata.getString(CommonConst.SHARED_PREFERENCE_FRTIME, "");
        return "";
    }

    /**
     * 定时获取好友时间
     *
     * @param sharedata
     * @return
     */
    public static void saveFrTime(SharedPreferences sharedata, String frTime) {
        if (sharedata == null) return;
        Editor editor = sharedata.edit();
        editor.putString(CommonConst.SHARED_PREFERENCE_FRTIME, frTime);
        editor.commit();
    }

//    /**
//     * 获取代表是否创建桌面图标，０代表创建，1代表不创建
//     *
//     * @param sharedata
//     * @return
//     */
//    public static int getIsSreachFriendNew(SharedPreferences sharedata) {
//        if (sharedata != null)
//            return sharedata.getInt(CommonConst.SHARED_PREFERENCE_FIRST_SREACHFRIEND, 1);
//        return 1;
//    }
//
//    /**
//     * 生成代表是否创建桌面图标，０代表创建，1代表不创建
//     *
//     * @param sharedata
//     * @return
//     */
//    public static void saveIsSreachFriendNew(SharedPreferences sharedata, int newCount) {
//        if (sharedata == null) return;
//        Editor editor = sharedata.edit();
//        editor.putInt(CommonConst.SHARED_PREFERENCE_FIRST_SREACHFRIEND, newCount);
//        editor.commit();
//    }

//    /**
//     * 获取代表是否创建桌面图标，０代表创建，1代表不创建
//     * 现在用来主页的消息
//     *
//     * @param sharedata
//     * @return
//     */
//    public static int getIsSreachApp(SharedPreferences sharedata) {
//        if (sharedata != null)
//            return sharedata.getInt(CommonConst.SHARED_PREFERENCE_FIRST_SREACHAPP, 1);
//        return 1;
//    }
//
//    /**
//     * 生成代表是否创建桌面图标，０代表创建，1代表不创建
//     *
//     * @param sharedata
//     * @return
//     */
//    public static void saveIsSreachApp(SharedPreferences sharedata, int newCount) {
//        if (sharedata == null) return;
//        Editor editor = sharedata.edit();
//        editor.putInt(CommonConst.SHARED_PREFERENCE_FIRST_SREACHAPP, newCount);
//        editor.commit();
//    }

}
