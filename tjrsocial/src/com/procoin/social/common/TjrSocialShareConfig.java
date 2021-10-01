package com.procoin.social.common;

public class TjrSocialShareConfig {
    public static final String SOCIAL_WEIBO = "weibo";
    public static final String SOCIAL_WEIXIN = "weixin";

    // 应用的key 请到官方申请正式的appkey替换APP_KEY
    // weibo
    public static final String WEIBO_APP_KEY = "293945019";// 正式版本
    public static final String WEIBO_APP_SECRET = "0990dde3e8917d513b12f29681f0af1e";//
    /**
     *
     *  注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响，
     * 但是没有定义将无法使用 SDK 认证登录。
     * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
     */
    public static final String REDIRECT_URL = "http://www.qulianr.com";

    /**
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
     * <p/>
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
     * <p/>
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     * <p/>
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write," + "friendships_groups_read,friendships_groups_write,statuses_to_me_read," + "follow_app_official_microblog," + "invitation_write";

    // 替换为开发者REDIRECT_URL
    public static final String REDIRECT_WEIBO_URL = "http://www.taojinroad.com/download.html";
    public static final String REDIRECT_WEIBO_MICRADIO_URL = "http://www.taojinroad.com/download.html";
    // weixin
    public static final String APPID_WECHAT_KEY = "wx177a8b2f3e6c995c";//wx3deb49bd2aa840a5
//    public static final String APPID_WECHAT_MIRRADIO_KEY = "wxb8fde8dcdb80782e";
    // QQ
    public static final String APPID_QQ_KEY = "1107229202"; // QQ
    public static final String APPID_QQ_MIRRADIO_KEY = "100493486"; // 微电台QQkey
    public static final String APPID_QQ_CALLBACK = "tencentauth://auth.qq.com";
    public static final String APPID_QQ_SCOPE = "get_user_info,get_user_profile,add_share,add_topic,list_album,upload_pic,add_album";// 授权范围

    // 跳转需要的类型
    public static final String KEY_EXTRAS_SOCIAL_APPID = "social_appId";// 分享的appId
    public static final String KEY_EXTRAS_SOCIAL_PACKAPE = "social_pakeage";
    public static final String KEY_EXTRAS_BITMAP = "bitmap";
    public static final String KEY_EXTRAS_PHOTO_PATH = "photo_path";
    public static final String KEY_EXTRAS_JSONSTR = "jsonStr";
    public static final String KEY_EXTRAS_KEY_CONTENT = "weibo_content";
    public static final String KEY_EXTRAS_TOACTIVITY = "gotoActivity";
    public static final String KEY_EXTRAS_QUESTION = "weibo_question";
    public static final String KEY_EXTRAS_QUESTION_ARRAY = "weibo_question_array";
    public static final String KEY_EXTRAS_USER = "theuser";
    public static final String KEY_EXTRAS_ISQUESTION = "isQuestion";
    public static final String KEY_EXTRAS_ISNEEDURL = "isneedUrl";

    public static final String KEY_EXTRAS_ISOTHEARIMAGE = "isOtherImage";

    public static final String KEY_EXTRAS_USERID = "userId";
    public static final String KEY_EXTRAS_USERNAME = "name";
    public static final String KEY_EXTRAS_USERHEADURL = "headUrl";
    //    public static final String KEY_EXTRAS_STYLEID = "styleId";// 樣式id
    public static final String KEY_EXTRAS_STYLETYPE = "styleType";// 樣式id
    public static final String KEY_EXTRAS_CLS = "cls"; // 包名和类名
    public static final String KEY_EXTRAS_PKG = "pkg";
    public static final String KEY_EXTRAS_PVIEW = "pview";
    public static final String KEY_EXTRAS_COMMONBUNDLE = "commonBundle";// 跳入聊天使用的bundle
    public static final String KEY_EXTRAS_CHATMYTOPIC_STRING = "chatMytopic_string";// 代表在activity中跳转到私聊

    public static final String KEY_EXTRAS_TYPE = "type";// 代表type类型 0--3
    public static final String KEY_EXTRAS_RESULTTYPE = "resultType";// 代表resulttype类型

    // 微信分享
    public static final String QQ_WECHAT_TIMELINE = "Wechat_timeline";
    public static final String QQ_WECHAT_WEBTITLE = "Wechat_title";
    public static final String QQ_WECHAT_ICON = "Wechat_the_icon";
    public static final String QQ_WECHAT_SENDURL = "Wechat_the_sendurl";
//	// 登錄的類型
//	public static final String MB = "mb";// 手機登錄同一个mbEncry
//	public static final String SINAWB = "sinawb";// sinawb登錄
//	public static final String QQ = "qq";// QQ登錄
//	public static final String WEIXIN = "weixin";// 微信登錄

    public static final String ARTICLE = "article";
    public static final String STYPE = "stype";
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String PARAMS = "params";
    public static final String ICONURL = "iconUrl";

    // 页面常用跳转，行情是每个组件都要的，所以都提炼到这里来
    public static String STOCKNAME = "stockName";
    public static final String FULLCODE = "fullcode";

    //股友吧还是好友圈
    public static final String SQUAREORFRIENDCIRCLE = "squareOrFriendCircle";//

    public static final String APPENDTEXT = "appendText";//
}
