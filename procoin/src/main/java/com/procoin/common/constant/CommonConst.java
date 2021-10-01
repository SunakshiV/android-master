package com.procoin.common.constant;

public final class CommonConst {
    public static final boolean ISSHOWNEWVER = false;//需要新版本提示
    // 页面跳转的参数开始
    public static final String PKG = "pkg";
    public static final String CLS = "cls";
    public static final String APP_NAME = "PROCOIN";
    public static final String PKG_HOME = "com.procoin";
    // 保存用户信息使用的字段开始
    public static final String IS_LOGIN = "is_login";// 代表是否从输入密码登录的
    public static final String USERACCOUNT = "userAccount";
    public static final String PASSWORD = "password";
    public static final String C_CODE = "c_code";
    public static final String USERID = "userId";

    public static final String USERNAME = "userName";


    public static final String LOGIN_TYPE = "mbEncry";//
    // 保存用户信息使用的字段结束
    public static final String KEY_EXTRAS_USER_INFO = "user_info";// 代表在activity中传递用户关键字
    public static final String KEY_EXTRAS_TYPE = "type";// 代表type类型

    public static final String INTMATCHES = "[-0-9E]+$";// int型匹配
    public static final String FLOATMATCHES = "[-]?[0-9.E]+$";// 小数型匹配


    public static final String TITLE = "title";
    public static final String URLS = "urls";


    // 缓存我查过的股票记录
    public static final String MY_SEARCH_STOCK = "my_search_stock";
    // 消息类型参数
    // 与的消息跳转参数有关关键字
    public static final String MSG_PARAMS = "params";// params

    public static final String MYINFO = "myinfo";
    public static final String KEY_EXTRAS_CLS = "cls"; // 包名和类名
    public static final String KEY_EXTRAS_PKG = "pkg";

    public static final String ACTION_BROADCASERECIVER_FINISHONLOGOUT = "com.procoin.broadcaseReciver.finishOnLogout"; // 当用户注销时候，需要发送一条广播来关闭一些页面

    public static final String KEY_EXTRAS_BITMAP = "bitmap";

    public static final String PARAMS = "params"; // 消息传的

    // 朋友圈
    public static final String PICSTRING = "picString";
    public static final String SINGLEPICSTRING = "singlePicString";
    public static final String JUMPTYPE = "jumpType";
    public static final String TAUSERID = "taUserId";

    public static final String DEFAULTPOS = "defaultPos";
    public static final String PAGETYPE = "pageType";

    public static final String POS = "pos";
    public static final String CIRCLECHATMARK = "circleChatMark";


    public static final String CIRCLEID = "circleId";
    public static final String CIRCLEINFO = "circleInfo";
    public static final String CIRCLENAME = "circleName";
    public static final String CIRCLEJOINMODE = "circleJoinMode";

    public static final String BALANCE = "balance";

    public static final String PUSH_PRIVATE_CHAT = "push_private_chat";//私聊客服
    public static final String PUSH_CIRCLE = "push_circle";//这个是以前的圈子(暂时不用)
    public static final String PUSH_LP = "lp";//你关注的人发了文正
    public static final String PUSH_QA = "qa";//邀请你提问
    public static final String PUSH_RE = "re";//评论回复


    public static final String AT_MATCHES = "@\\((.*?)\\)\\「(.*?)」";//@功能的正则表达式

    public static final String WEB_MATCHES = "((http[s]{0,1}|ftp)://[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)|(www.[a-zA-Z0-9\\.\\-]+\\.([a-zA-Z]{2,4})(:\\d+)?(/[a-zA-Z0-9\\.\\-~!@#$%^&*+?:_/=<>]*)?)";//网页


    public static final String WEIPAN_ACTION_NOTIFICATION_SUB_TRADE = "weipan_action_notification_sub_trade";//是否要跟单，提醒

    public static final String CLEAR_PRIVATE_CHAT = "clear_private_chat";//清除私聊聊天记录
    public static final String CLEAR_CIRCLE_CHAT = "clear_circle_chat";//清除圈子聊天记录



    public static final String HEAD = "head";
    public static final String BODY = "body";

    public static final String CHAT_TOPIC = "chatTopic";// 私聊
    public static final String CHAT_TOPIC2 = "chat_topic";// 私聊

    public static final String CHAT_NAME = "chatName";// 私聊名字
    public static final String CHAT_HEADURL = "chatHeadUrl";// 私聊头像
    public static final String ISPRIVATECHAT = "isPrivateChat";// 是否私聊


    //下面是新加的

    public static final String ADDRESS = "address";//地址
    public static final String ADDRID = "addr_id";//地址id

    public static final String BUTTONTEXT = "buttonText";

    public static final String VIDEOOUTPUTPATH = "videoOutputPath";

    public static final String NOTIFICATION_SETTING = "notification_setting_";

    public static final float STATUSBAR_ALPHA = 0.2f;//原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度


    public static final String KEY_EXTRAS_ARTICLEID = "articleId";

    public static final String SHOWCAREMA = "showCarema";

    public static final String PID = "PROJECT_ID";
    public static final String UName = "USER_NAME";
    public static final String UIcon = "USER_ICON";

    public static final String SYMBOL = "symbol";
    public static final String BUYSELL = "buySell";

    public static final String STATE = "state";
    public static final String ISDONE = "isDone";

    public static final String RECHARGE = "recharge";
    public static final String RECEIPT = "receipt";




    public static final String PROJECTTYPENAME = "projectTypeName";

    public static final String PROJECTTYPEID = "projectTypeId";

    public static final String PROJECTTYPESUB = "projectTypeSub";

    public static final String FOREXAMPLE = "forExample";




    public static final String PROJECTID = "projectId";

    public static final String ISEDIT = "isEdit";

    public static final String TOKAENTITY = "tokaEntity";
    public static final String ALREADYCREATETOKA = "alreadyCreateToka";

    public static final String IDCERTIFY = "idCertify";


    public static final String ENTRUST_BS = "entrust_bs";

    public static final String ONSALE = "onSale";
    public static final String MAXPICCOUNT = "maxPicCount";
    public static final String ISPREVIEW = "isPreView";

    public static final String CLASSNAME = "className";
    public static final String NEEDCUT = "needCut";

    public static final String ARTWORK = "artwork";


    public static final String PICLIST = "picList";
    public static final String HOLDAMOUNT = "holdAmount";

    public static final String PRIVACY_PROTOCOL = "http://api.xincp11.com/procoin/article/#/passgeDetail?article_id=54";//隐私条款
    public static final String USER_PROTOCOL = "http://api.xincp11.com/procoin/article/#/passgeDetail?article_id=48";//用户协议
    public static final String PASSGEDETAIL_26 = "http://api.xincp11.com/procoin/article/#/passgeDetail?article_id=49";//交易服务协议
    public static final String PASSGEDETAIL_28 = "http://api.xincp11.com/procoin/article/#/passgeDetail?article_id=51";//交易规则说明
    public static final String ABOUTCROPYME = "http://api.xincp11.com/procoin/article/#/passgeDetail?article_id=60";//关于我们

    public static final String SERVICE_PROTOCOL = "http://api.byy.one/coingo/html/service-protocol.html";//服务协议
    public static final String LIABILITY_PROTOCOL = "http://api.byy.one/coingo/html/liability-protocol.html";//风险提示
    public static final String USDTDETAILS = "http://api.byy.one/coingo/html/usdt-des.html";//什么是USDT
    public static final String FOLLOWRULE = "http://api.byy.one/coingo/html/follow-rule.html";//跟单规则
    public static final String PASSGEDETAIL_25 = "http://api.byy.one/coingo/article/#/passgeDetail?article_id=25";//数字资产借贷服务合同
    public static final String PASSGEDETAIL_27 = "http://api.byy.one/coingo/article/#/passgeDetail?article_id=27";//存币宝说明
    public static final String PASSGEDETAIL_29 = "http://api.xincp11.com/procoin/article/#/passgeDetail?article_id=51";//如何交易



//    public static final String CIRCLERULE = "http://www.caixuevip.com.cropyme/html/ruleDoc.html";//圈主规则

    public static final String WEBHASBACK = "webHasBack";//网页是否有返回按钮

    /**
     * 只保留数字
     */
    public static final String ONLYDIGITAL = "\\D+";


    public static final String ROLE = "role";

    public static final String TARGETUID = "targetUid";

    public static final String RECEIPTTYPE = "receiptType";
    public static final String ENTRUSTAMOUNT = "entrustAmount";

    public static final String UNITPRICE = "unitPrice";
    public static final String ISLIMIT = "isLimit";


    public static final String ORDERCASHID = "orderCashId";
    public static final String ORDERID = "orderId";

    public static final String SHOWUSERID = "showUserId";
    public static final String SHOWPAYMENTID = "showPaymentId";


    public static final String SERVERHOST = "serverHost";
    public static final String SERVERHTTP = "serverHttp";

    public static final String TIPS = "tips";

    public static final String STATEMENTURL = "statementUrl";
    public static final String RULESURL = "rulesUrl";


    public static final String ISLEVER = "isLever";

    public static final String ACCOUNTTYPE = "accountType";

    public static final String ADID = "adId";



}
