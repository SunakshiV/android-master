package com.procoin.social.keep;


/**
 * 该类用于保存Oauth2AccessToken到sharepreference，并提供读取功能
 * @author xiaowei6@staff.sina.com.cn
 *
 */
public class AccessTokenKeeper {
//    private static final String PREFERENCES_NAME = "com_weibo_sdk_android";
//
//    private static final String KEY_UID           = "uid";
//    private static final String KEY_ACCESS_TOKEN  = "access_token";
//    private static final String KEY_EXPIRES_IN    = "expires_in";
//    
//    /**
//     * 保存 Token 对象到 SharedPreferences。
//     * 
//     * @param context 应用程序上下文环境
//     * @param token   Token 对象
//     */
//    public static void writeAccessToken(Context context, Oauth2AccessToken token) {
//        if (null == context || null == token) {
//            return;
//        }
//        
//        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
//        Editor editor = pref.edit();
//        editor.putString(KEY_UID, token.getUid());
//        editor.putString(KEY_ACCESS_TOKEN, token.getToken());
//        editor.putLong(KEY_EXPIRES_IN, token.getExpiresTime());
//        editor.commit();
//    }
//
//    /**
//     * 从 SharedPreferences 读取 Token 信息。
//     * 
//     * @param context 应用程序上下文环境
//     * 
//     * @return 返回 Token 对象
//     */
//    public static Oauth2AccessToken readAccessToken(Context context) {
//        if (null == context) {
//            return null;
//        }
//        
//        Oauth2AccessToken token = new Oauth2AccessToken();
//        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
//        token.setUid(pref.getString(KEY_UID, ""));
//        token.setToken(pref.getString(KEY_ACCESS_TOKEN, ""));
//        token.setExpiresTime(pref.getLong(KEY_EXPIRES_IN, 0));
//        return token;
//    }
//
//    /**
//     * 清空 SharedPreferences 中 Token信息。
//     * 
//     * @param context 应用程序上下文环境
//     */
//    public static void clear(Context context) {
//        if (null == context) {
//            return;
//        }
//        
//        SharedPreferences pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_APPEND);
//        Editor editor = pref.edit();
//        editor.clear();
//        editor.commit();
//    }
}
