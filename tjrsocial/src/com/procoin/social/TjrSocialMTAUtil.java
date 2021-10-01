package com.procoin.social;

import android.app.Application;
import android.content.Context;

import com.tencent.mta.track.StatisticsDataAPI;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;

import java.util.Properties;

public class TjrSocialMTAUtil {
    private static final String MTA_APPKEY = "AH2GY867QHLL";
    public static final String EVENT_MOREOPTIONSBUTTON = "MoreOptionsButton";
//    // 手机登录模型:
//    public static final String MTAPHONEONCLICK = "MTAPhoneOnClick"; // (4-1)点击Phone登录事件

    public static final String PROP_CLICKTYPE = "ClickType";
    public static final String PROPVAlUE_CLICKTYPE = "ClickType_Android";
    public static final String MTAONCLICKWELCOME = "MTAOnClickWelcome"; //welcome分享
//    public static final String MTAHOTSPOTTOCLICK = "MTAHotspotToClick"; // 头条里面点击组件

    // 新版
    //手机登录模型:
    public static final String MTAPhoneOnClick = "MTAPhoneOnClick";                 //点击登录事件
    public static final String MTAPhoneVarUser = "MTAPhoneVarUser";               //登录验证用户信息
    public static final String MTAPhoneLoginToHome = "MTAPhoneLoginToHome";              //登录主页页面

    //手机获取验证码模型:
    public static final String MTAPhoneRegToPhonePage = "MTAPhoneRegToPhonePage";          //进入手机注册页面
    public static final String MTAPhoneRegReqCode = "MTAPhoneRegReqCode";              //注册点击验证码按钮
    public static final String MTAPhoneRegReqSucCode = "MTAPhoneRegReqSucCode";           //注册获取验证码成功

    //手机注册模型:
    public static final String MTAPhoneRegToUserPage = "MTAPhoneRegToUserPage";           //进入个人信息注册页面
    public static final String MTAPhoneRegOnClickHome = "MTAPhoneRegOnClickHome";          //注册点击下一步按钮
    public static final String MTAPhoneRegToHome = "MTAPhoneRegToHome"; //注册成功进入主页

    //开机页点击事件:
    public static final String MTAOnClickWelcome = "MTAOnClickWelcome"; //开机页点击

    //主页tap点击次数:
    public static final String MTAHomeTabClick = "MTAHomeTabClick"; //主页tab点击次数

    /**
     * 市场页面
     **/
    public static final String MTAMarketRecommendTabClick = "MTAMarketRecommendTabClick";     //推荐卡片点击
    public static final String MTAMarketRateTabClick = "MTAMarketRateTabClick"; //人气榜点击
    public static final String MTAMarketValueTabClick = "MTAMarketValueTabClick"; //身价榜点击
    public static final String MTAMarketKillCellClick = "MTAMarketKillCellClick"; //秒杀列表点击
    public static final String MTAMarketRateMoreClick = "MTAMarketRateMoreClick"; //人气榜更多点击
    public static final String MTAMarketValueMoreClick = "MTAMarketValueMoreClick"; //身价榜更多点击
    public static final String MTAMarketSearchClick = "MTAMarketSearchClick"; //市场页搜索点击

    /**
     * 关注页面
     **/
    public static final String MTAStockCellClick = "MTAStockCellClick"; //关注列表点击
    public static final String MTAStockRefreshClick = "MTAStockRefreshClick"; //关注页刷新点击
    public static final String MTAStockSearchClick = "MTAStockSearchClick"; //关注页搜索点击

    /**
     * 新闻页面
     **/
    public static final String MTANewsCellClick = "MTANewsCellClick"; //新闻列表点击
    public static final String MTANewsMoreClick = "MTANewsMoreClick"; //新闻更多点击
    public static final String MTANewsSearchClick = "MTANewsSearchClick"; //新闻页搜索点击

    /**
     * 账户页面
     **/
    public static final String MTAAccountCellClick = "MTAAccountCellClick";   //账户做多列表点击
    public static final String MTAAccountCellDownClick = "MTAAccountCellDownClick"; //账户做空列表点击

    public static final String MTAAccountRefreshClick = "MTAAccountRefreshClick"; //账户页刷新点击
    public static final String MTAAccountMsgClick = "MTAAccountMsgClick"; //账户页消息点击
    public static final String MTAAccountSettingClick = "MTAAccountSettingClick"; //账户页设置点击
    public static final String MTAAccountMyClick = "MTAAccountMyClick"; //账户页我的点击
    public static final String MTAAccountOrderClick = "MTAAccountOrderClick"; //账户页订单点击
    public static final String MTAAccountDoneClick = "MTAAccountDoneClick"; //账户页成交点击
    public static final String MTAAccountExchangeClick = "MTAAccountExchangeClick"; //账户页提货兑换点击
    public static final String MTAAccountNewCardClick = "MTAAccountNewCardClick";         //账户页新卡特权点击
    public static final String MTAAccountBankClick = "MTAAccountBankClick";             //账户页转入转出点击

    /**
     * 买卖页面
     **/
    public static final String MTATradeTopScrollTab = "MTATradeTopScrollTab";             //交易页顶部滚动次数
    public static final String MTATradeLineTypeClick = "MTATradeLineTypeClick";           //交易页行情图选择点击
    public static final String MTATradeLandscapeClick = "MTATradeLandscapeClick";          //交易页横屏点击次数
    public static final String MTATradeNewsInfoChangeClick = "MTATradeNewsInfoChangeClick";     //交易页新闻公告切换点击
    public static final String MTATradeRadioClick = "MTATradeRadioClick";              //交易页直播点击
    public static final String MTATradeKLineIndexTypeClick = "MTATradeKLineIndexTypeClick";     //交易页K线图指标点击
    public static final String MTATradeRefreshClick = "MTATradeRefreshClick";            //交易页刷新点击
    public static final String MTATradeLineDetailClick = "MTATradeLineDetailClick";         //交易页3档展示点击

    public static final String MTAWEIXINWEBSHARE = "MTAWeiXinWebShare"; //网页分享


    /**
     * 活动金
     */
    public static final String MTATradeTicketOpenUpClick = "MTATradeTicketOpenUpClick";  //本金券框买卡点击
    public static final String MTATradeTicketOpenDownClick = "MTATradeTicketOpenDownClick"; //本金券框借卡点击
//    public static final String MTAInvateWXClick = "MTAInvateWXClick"; //点击微信邀请好友
//    public static final String MTAInvateWXCircleClick = "MTAInvateWXCircleClick"; //点击微信朋友圈邀请好友


    /**
     * 买卖卡明细
     */

    public static final String MTAUpDetailMarketClick = "MTAUpDetailMarketClick"; //买卡明细行情点击
    public static final String MTAUpDetailOpenUpClick = "MTAUpDetailOpenUpClick"; //买卡明细买卡点击
    public static final String MTAUpDetailCloseUpClick = "MTAUpDetailCloseUpClick";  //买卡明细卖卡点击

    public static final String MTADownDetailMarketClick = "MTADownDetailMarketClick"; //卖卡明细行情点击
    public static final String MTADownDetailOpenDownClick = "MTADownDetailOpenDownClick"; //卖卡明细借卡点击
    public static final String MTADownDetailCloseDownClick = "MTADownDetailCloseDownClick";  //卖卡明细还卡点击

    /**
     * 分享
     *
     */

    public static final String MTAWeiXinShareClick = "MTAWeiXinShareClick";  //分享统一用这个


    /**
     * 持仓多空切换
     *
     */

    public static final String MTAAccountUDChangeClick = "MTAAccountUDChangeClick";  //账户页多空列表切换点击







    /**
     * 根据不同的模式，建议设置的开关状态，可根据实际情况调整，仅供参考。
     *
     * @param isDebugMode 根据调试或发布条件，配置对应的MTA配置
     */
    public static void initMTAConfig(boolean isDebugMode, Context context) {
//        java.util.UUID.randomUUID();
        // android.os.Debug.startMethodTracing("MTA");
        if (isDebugMode) { // 调试时建议设置的开关状态
            // 查看MTA日志及上报数据内容
            StatConfig.setDebugEnable(true);
            // 禁用MTA对app未处理异常的捕获，方便开发者调试时，及时获知详细错误信息。
            // StatConfig.setAutoExceptionCaught(true);
            // StatConfig.setEnableSmartReporting(false);
            // Thread.setDefaultUncaughtExceptionHandler(new
            // UncaughtExceptionHandler() {
            //
            // @Override
            // public void uncaughtException(Thread thread, Throwable ex) {
            // logger.error("setDefaultUncaughtExceptionHandler");
            // }
            // });
            // 调试时，使用实时发送
            // StatConfig.setStatSendStrategy(StatReportStrategy.BATCH);

            // // 是否按顺序上报
            // StatConfig.setReportEventsByOrder(false);
            // // 缓存在内存的buffer日志数量,达到这个数量时会被写入db
            // StatConfig.setNumEventsCachedInMemory(30);
            // // 缓存在内存的buffer定期写入的周期
            // StatConfig.setFlushDBSpaceMS(10 * 1000);
            // // 如果用户退出后台，记得调用以下接口，将buffer写入db
            // StatService.flushDataToDB(getApplicationContext());

            // StatConfig.setEnableSmartReporting(false);
            // StatConfig.setSendPeriodMinutes(1);
            // StatConfig.setStatSendStrategy(StatReportStrategy.PERIOD);
        } else { // 发布时，建议设置的开关状态，请确保以下开关是否设置合理
            // 禁止MTA打印日志
            StatConfig.setDebugEnable(false);

            // 选择默认的上报策略
            // StatConfig.setStatSendStrategy(StatReportStrategy.APP_LAUNCH);
            // StatConfig.setStatSendStrategy(StatReportStrategy.ONLY_WIFI);
        }
        StatisticsDataAPI.instance(context.getApplicationContext());
        StatService.setContext(context.getApplicationContext());
        // 注册Activity生命周期监控，自动统计时长
        StatService.registerActivityLifecycleCallbacks((Application)context.getApplicationContext());

        // 根据情况，决定是否开启MTA对app未处理异常的捕获
        StatConfig.setAutoExceptionCaught(true);

        try {
            StatService.startStatService(context.getApplicationContext(), MTA_APPKEY, com.tencent.stat.common.StatConstants.VERSION);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void onResume(Context context) {
        try {
            StatService.onResume(context);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void onPause(Context context) {
        try {
            StatService.onPause(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackCustomKVEvent(Context context, String namevalue, String eventName) {
        try {
            Properties prop = new Properties();
            prop.setProperty("name", namevalue);
            StatService.trackCustomKVEvent(context, eventName, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void trackCustomKVEvent(Context context, String name, String namevalue, String eventName) {
        try {
            Properties prop = new Properties();
            prop.setProperty(name, namevalue);
            StatService.trackCustomKVEvent(context, eventName, prop);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
