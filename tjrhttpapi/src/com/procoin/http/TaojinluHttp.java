package com.procoin.http;

import com.procoin.http.error.TaojinluException;
import com.procoin.http.resource.DiskCache;

import java.io.IOException;

public class TaojinluHttp {
    public static final String TAOJINROAD_API_DOMAIN = "api.xincp11.com";
    private static TaojinluHttp instance;
    /*
     * api_gfan || api_suhang_reg || api_helper_reg 小助手|| api_gpkhw_reg ||
     * api_lcj_reg || api_xtlc_reg || api_stock1_reg || api_stock2_reg ||
     * api_baidu_reg ||api_360_reg ||taojinroad 淘金路 ||api_yingyongbao_reg 应用宝
     */
    public static final String API_CALL_TYPE = "taojinroad"; // 区别接口调用类型
    public static final String API_CALL_FILE = "channel.cfg"; // 区别接口调用类型保存的文件，保存在asset目录下
    // tjrchannel=xxx
    private static final String DATATYPE = ".do";
    private static final String JSPTYPE = ".jsp";
    private static final String URL_API_USER_ADDCONTACT = "/user/addcontact"; // 上传通信录接口
    private static final String URL_API_USER_FINDKNOWFRIENDS = "/user/findknowfriends"; // 查找好友在淘金路上
    private static final String URL_API_ACCURATE_FIND = "/accurate/find"; // 查找接口
//    private static final String URL_API_USER_MSGOPP = "/user/msgOpp";// 发送好友动态
    private static final String URL_API_USER_ADDFRIEND = "/user/addfriend"; // 加好友接口
    private static final String URL_API_USER_LOGIN = "/user/login";
    private static final String URL_API_USER_MOBILELOGIN = "/user/mobilelogin";// 登录
    private static final String URL_API_USER_FRIEND = "/user/friend";
    private static final String URL_API_USER_MBREGIST = "/user/mbregist"; // 手机端注册
    private static final String URL_API_STOCK_GETMYSTOCK = "/stock/mystock";
    private static final String URL_API_STOCK = "/stock";
    private static final String URL_API_COMPANY = "/company";
    private static final String URL_API_USER = "/user";
    private static final String URL_API_INVITE_SENDINVITE = "/invite/sendinvite"; // 手机发送邀请接口
    private static final String URL_API_COMPONENTS = "/components";
    private static final String URL_API_COMPONENTS_NEWSPAPER = "/component/newsPaper"; // 报纸
    private static final String URL_API_COMPONENTS_NEWSPAPEROPP = "/component/newsPaperOpp"; // 报纸
    // private static final String URL_API_COMPONENTS_STOCKPK =
    // "/component/stockPK"; // 战股擂台
    private static final String URL_API_COMPONENTS_HELPPICK = "/component/helpPick"; // 帮忙挑股
    private static final String URL_API_STOCK_BAR = "/component/stockbar"; // 股吧
    private static final String URL_API_QASERVLET = "/QAServlet"; // 语音问答
    private static final String URL_API_USER_OTHERACCOUNT = "/user/otherAccount"; // 其他账号
    private static final String URL_API_WEIBO = "/weibo";
    private static final String URL_API_MILLIONAIRE = "/millionaire";
    private static final String URL_API_KLINE_ARENA = "/kline/arena";
    private static final String URL_API_WEIBO_DETAIL = "/weibo-detail";
    private static final String URL_API_FAVORITES = "/favorites"; // 收藏夹
    private static final String URL_API_NEWS_NEWSRECORD = "/news/newsRecord"; // 意见反馈
    // private static final String URL_API_TALKIEEDIT = "/talkieEdit";// 说一说
    // private static final String URL_API_TALKIESEARCH = "/talkieSearch";// 说一说
    // private static final String URL_API_TALKIEPUBLIC = "/talkiePublic";//
    // 股友圈广场
    private static final String URL_API_SQUAREEEDIT = "/squareEdit";// 广场
    private static final String URL_API_SQUARESEARCH = "/squareSearch";// 广场

    private static final String URL_API_SQUAREFAVOR = "/squareFavor";// 股友吧收藏

    private static final String URL_API_SQUARETOPIC = "/squareTopic";// 话题

    private static final String URL_API_FRIENDSSAY = "/friendsSay";// 好友圈
    private static final String URL_API_FRIENDSREVIEW = "/friendsReview";// 好友圈评论
    private static final String URL_API_FRIENDSGOOD = "/friendsGood";// 好友圈赞
    private static final String URL_API_TAGUSER = "/tagUser";//用户标签

    private final String URL_API_NEWSSEARCH = "/newsSearch"; // 新闻搜索
    private final String URL_API_NEWSEDIT = "/newsEdit"; // 修改

    // getDateKlineByNum
    private final String[] KLINE_CYCLE_METHOD = {"get5Mindata", "get10Mindata", "get15Mindata", "get60Mindata", "findDateKlineBybAnda", "findWeekDateKlineByNum", "findMonthDateKlineByNum"};// K线的周期方法,0代表5分钟线,1代表15分钟线,2代表30分钟线,3代表60分钟线,4代表日线,5代表周线，6代表月线

//    private final String mApiBaseUrl; // 主要对用户关系系统url
    private final HttpApi mHttpApi;

    public TaojinluHttp() {
        mHttpApi = new HttpApi();
//        mApiBaseUrl = TjrBaseApi.mApiBaseUri.uri();
    }

    public static TaojinluHttp getInstance() {
        if (instance == null) {
            synchronized (TaojinluHttp.class) {
                if (instance == null) instance = new TaojinluHttp();
            }
        }
        return instance;
    }


    public HttpApi getHttpApi() {
        return mHttpApi;
    }


//    private String fullUrl(String url) {
//        return mApiBaseUrl + url + DATATYPE;
//    }
//    @Deprecated
//    public String getUserInfoJson(String userAccount, String password, String type, String apiCallType) throws TaojinluException, IOException {
//        return getHttpApi().doHttpPost(fullUrl(URL_API_USER_LOGIN), //
//                new BasicNameValuePair("userAccount", userAccount), //
//                new BasicNameValuePair("password", MD5.getMessageDigest(password)), //
//                new BasicNameValuePair("apiCallType", apiCallType), //
//                new BasicNameValuePair("type", type));
//    }




    public String downloadVoice(DiskCache mResourceCache, String url) throws TaojinluException, IOException {
        return getHttpApi().doHttpGetForDownLoadFile(mResourceCache, url);
    }



}
