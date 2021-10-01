package com.procoin.http.common;

public class CommonConst {
	public static final boolean DEBUG = false;
	public static final String ISRUN = "isRun";
//	public static final String LOGIN_TYPE_MB = "mb";// 手機登錄同一个mbEncry
//	public static final String LOGIN_TYPE_SINAWB = "sinawb";// sinawb登錄
//	public static final String LOGIN_TYPE_QQ = "qq";// QQ登錄
//	public static final String LOGIN_TYPE_WEIXIN = "weixin";// 微信登錄

	// 保存用户信息使用的字段开始
	public static final String SHARED_PREFERENCE_DNSINFO = "dnsinfo";
	public static final String SHARED_PREFERENCE_PLACARDTIME = "placardTime"; // 通知时间
	public static final String SHARED_PREFERENCE_USER = "user";
	public static final String SHARED_PREFERENCE_TJRAPIKEY = "tjrApiKey";
	public static final String SHARED_PREFERENCE_USERID = "userId";// 跳进好友主页也使用了这个变量
	public static final String SHARED_PREFERENCE_NAME = "name";// 名字
	public static final String SHARED_PREFERENCE_HEADURL = "headurl";// 头像
	public static final String SHARED_PREFERENCE_SEX = "sex";// 性别，0代表女性，1代表男性
	public static final String SHARED_PREFERENCE_SELFDESCRIPTION = "selfDescription";// 自我描述
	public static final String SHARED_PREFERENCE_PURVIEW = "purview";// 1代表仅有自己可以见,2代表好友可见,3代表所有人可见
	public static final String SHARED_PREFERENCE_BIRTHDAY = "birthday";// 生日
																		// 格式2011-2-1
	public static final String SHARED_PREFERENCE_PROFESSION = "profession";// 职业
	public static final String SHARED_PREFERENCE_ADDRESS = "address";// 地址
	public static final String SHARED_PREFERENCE_STOCKAGE = "stockAge";// 股龄
	public static final String SHARED_PREFERENCE_ISVIP = "isVip";// 股龄
	public static final String SHARED_PREFERENCE_VIPDESC = "ipDesc";// 股龄
	public static final String SHARED_PREFERENCE_INVESTMENTSTYLE = "investmentStyle";// 投资风格
																						// 1代表短线激进型,2代表中线波段型,3代表长线稳健型
	public static final String SHARED_PREFERENCE_FRTIME = "frtime";
	public static final String SHARED_PREFERENCE_FIRST_SREACHFRIEND = "first_sreachfriend";
	public static final String SHARED_PREFERENCE_FIRST_SREACHAPP = "first_sreachapp";
	public static final String LOGIN_TYPE = "mbEncry";// 只是代表一个key键，代表是加密方式
	public static final String USERACCOUNT = "userAccount";
	public static final String PASSWORD = "password";
	public static final String USERID = "userId";
	public static final String TJR_SESSIONID = "sessionid";
	public static final String ISFIRSTLOGIN = "isFirstLogin";
	public static final String ISTJR = "isTjr";
	public static final String UPDATECONTACTTIME = "updateContactTime";
	public static final String UPDATECONTACTID = "updateContactId";
	public static final String SHARED_PREFERENCE_COUNT = "count";// 代表是否创建桌面图标，０代表创建，1代表不创建
	public static final String SHARED_PREFERENCE_VERSION = "version";// 区别于是否显示版本介绍所图
	public static final String SHARED_PREFERENCE_ISOPEN = "isopen";// 区别于是否显示

	public static final String SHARED_PREFERENCE_GETBIND_PROD_CODE = "bindProdCode";//
	public static final String SHARED_PREFERENCE_IS_SHOW_DY = "isShowDy";//

	public static final String SHARED_PREFERENCE_IDCERTIFY = "idCertify";//
//	public static final String SHARED_PREFERENCE_VERIFY_MSG = "verify_msg";//
	public static final String SHARED_PREFERENCE_USERREALNAME = "userRealName";//

	public static final String WEIBO_UID = "weibo_uid";
	public static final String WEIBO_TOKEN = "weibo_token";
	public static final String WEIBO_SCREENNAME = "weibo_screenName";// 昵称
	public static final String QQ_OPENID = "qq_uid";
	public static final String QQ_TOKEN = "qq_token";
	public static final String QQ_SCREENNAME = "qq_screenName";// 昵称
	public static final String WEIXIN_OPENID = "weixin_uid";
	public static final String WEIXIN_TOKEN = "weixin_token";
	public static final String WEIXIN_REFRESH_TOKEN = "weixin_refresh_token";
	public static final String WEIXIN_SCREENNAME = "weixin_screenName";// 昵称
	public static final String REFRESH_CIRCLE_LAST_TIME = "refresh_circle_last_time";// 好友圈用户刷新列表最后一次时间

	public static final String ISAddV = "isAddV";// 是否加v
	public static final String CHAT_VOICE = "[tjr_voice]=";// 语音

}
