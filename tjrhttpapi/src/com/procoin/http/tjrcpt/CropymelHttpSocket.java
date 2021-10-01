package com.procoin.http.tjrcpt;

import android.util.Log;

import com.procoin.http.HttpApi;
import com.procoin.nsk.Protocol;
import com.procoin.nsk.TjrStarNSKManager;

import org.apache.http.message.BasicNameValuePair;

public class CropymelHttpSocket {
    private static final String STOCKURLROOT = "/";
    private static final String STOCKURLREAL = "/real";//5秒 实时刷分时
    private static final String STOCKURLMINUTE_LINE_GET = "/minuteLine/get";
    private static final String STOCKURLMINUTE_5DAY_LINE_GET = "/minute5DayLine/get";
    private static final String STOCKURLMINUTE_LINE_GETHOUR = "/minute_line/get_hour";

    private static final String STOCKURLMARKET_HOT_GET = "/market_hot/get";
    private static final String STOCKURREAL_LIMIT = "/real/limit"; //5秒 实时刷我的自选之类的
    private static final String STOCKUKLINE_GETDAY = "/kline/day";
    private static final String STOCKUKLINE_GETDAY_TO_LINE = "/kline_day/to_line";
    private static final String STOCKUKLINE_WEEK = "/kline/week";
    private static final String STOCKUKLINE_MONTH = "/kline/month";
    private static final String STOCKUKLINE_15MIN = "/kline/15min";
    private static final String STOCKUKLINE_HOUR = "/kline/hour";
    private static final String STOCKUKLINE_4HOUR = "/kline/4hour";


    private static final String OLSTAR_SEARCH = "/search/get";

    private static final String STOCKUKLINE_GETMINUTE = "/kline/get_minute";
    private static final String STOCKUKLINE_GET_1_MINUTE = "/kline/min";//1分钟

    private static final String OLSTAR_DETAILBS = "/detailbs/get";

    private static final String MARKETDATA = "/marketData";

    private static final String FAST = "/fast";


//    private static final String DLG_HOME_REFRESH_ENTRUSTS = "/dlg_home/refresh_entrusts";


    private static CropymelHttpSocket instance;
    private static TjrStarNSKManager tjrNSKManager = TjrStarNSKManager.getInstance();

    public enum KlineReqEnum {
        KLINE_1(1, STOCKUKLINE_GET_1_MINUTE), // 1分钟线
        KLINE_5(5, STOCKUKLINE_GETMINUTE), // 5分钟线
        KLINE_15(15, STOCKUKLINE_GETMINUTE), // 15分钟线
        KLINE_30(30, STOCKUKLINE_GETMINUTE), // 30分钟线
        KLINE_60(60, STOCKUKLINE_GETMINUTE), //
        KLINE_240(240, STOCKUKLINE_4HOUR), //
        KLINE_DAY(0, STOCKUKLINE_GETDAY),
        KLINE_WEEK(-1, STOCKUKLINE_WEEK),
        KLINE_MONTH(-2, STOCKUKLINE_MONTH);

        private final int minute;
        private final String reqDo;

        // 构造方法
        private KlineReqEnum(int minute, String reqDo) {
            this.minute = minute;
            this.reqDo = reqDo;
        }

        public int minute() {
            return minute;
        }

        public static KlineReqEnum getKlineReqEnumByMinute(int minute) {
            for (KlineReqEnum klineReqEnum : values()) {
                if (klineReqEnum.minute == minute) {
                    return klineReqEnum;
                }
            }
            return KLINE_5;

        }

        public String reqDo() {
            return reqDo;
        }

    }

    private final HttpApi mHttpApi;

    private CropymelHttpSocket() {
        mHttpApi = new HttpApi();
//        mApiStockRealUrl = "http://" + TjrBaseApi.stockHomeUri.uri() + ":9999/";
    }


    public static CropymelHttpSocket getInstance() {
        if (instance == null) {
            synchronized (CropymelHttpSocket.class) {
                if (instance == null) instance = new CropymelHttpSocket();
            }
        }
        return instance;
    }

    public void clearPool() {
        try {
            tjrNSKManager.clearPool();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public void initConnect() {
        try {
            tjrNSKManager.sendCommand(Protocol.PING);
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

//    /**
//     * 分时k线 /kline/get_minute?prod_code=WH1568&minute=5&type=v&day=0
//     */
//    public String klineMinute(String prod_code, KlineReqEnum klineReqEnum, String type, String day) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(klineReqEnum.reqDo(), //
//                    new BasicNameValuePair("type", type),//
//                    new BasicNameValuePair("day", day),//
//                    new BasicNameValuePair("minute", String.valueOf(klineReqEnum.minute())),//
//                    new BasicNameValuePair("prod_code", prod_code)));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return null;
//        }
//    }

    /**
     * 1小时线url:/minute_line/get_hour?prod_code=WH1568
     */
    public String minuteGetHour(String prod_code) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLMINUTE_LINE_GETHOUR,
                    new BasicNameValuePair("hour", String.valueOf(4)),
                    new BasicNameValuePair("avg", String.valueOf(1)),
                    new BasicNameValuePair("prod_code", prod_code)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    /**
     * 日K线url /kline/get_day?prod_code=WH1568&type=v&day=0
     */
    public String klineGetDay(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_GETDAY,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    /**
     * 日K线 to line url /kline_day/to_line?prod_code=00001
     */
    public String klineGetDayToLine(String prod_code) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_GETDAY_TO_LINE,//
                    new BasicNameValuePair("prod_code", prod_code)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    public String klineGetWeek(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_WEEK,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }


    public String klineGetMonth(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_MONTH,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    public String klineGet15Min(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_15MIN,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    public String klineGet1Min(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_GET_1_MINUTE,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }


    public String klineGetHour(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_HOUR,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    public String klineGet4Hour(String symbol, String type, String timestamp) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKUKLINE_4HOUR,//
                    new BasicNameValuePair("symbol", symbol), //
                    new BasicNameValuePair("timestamp", timestamp),
                    new BasicNameValuePair("type", type)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }




    /**
     * 快照行情少量数据url:/real/limit?prod_code=WH1568,WH1538  用于 自选卡 和持仓
     */
    public String getRealLimit(String prodCode) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURREAL_LIMIT, new BasicNameValuePair("prod_code", prodCode)));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
            return null;
        }
    }


    /**
     * 刷新我用券买的列表
     * @param userId
     * @return
     * @throws Exception
     */
//    public String refreshEntrusts(String userId) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(DLG_HOME_REFRESH_ENTRUSTS, new BasicNameValuePair("userId", userId)));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
////            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//            return null;
//        }
//    }


    /**
     * 行情快照
     *
     * @param bs_num==5,就拿5档
     */

    public String sendProdCode(String symbol, int bs_num) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLREAL,
                    new BasicNameValuePair("symbol", symbol),
                    new BasicNameValuePair("depth", String.valueOf(bs_num))));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
            return null;
        }
    }

    public String sendProdCode(String prod_code) throws Exception {
        return sendProdCode(prod_code, 5);
    }

    /**
     * 分时线的数据
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    public String sendProdCodeMinuteLine(String symbol) throws Exception {
        try {
            //分时线:/minute_line/get?prod_code=WH000001
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLMINUTE_LINE_GET,
                    new BasicNameValuePair("avg", String.valueOf(1)),  //添加avg参数,获取成交额
                    new BasicNameValuePair("symbol", symbol)));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("QuoteDataTask", "e==" + e);
            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
            return null;
        }
    }


    /**
     * 5日分时图
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    public String sendProdCode5DayMinuteLine(String symbol) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLMINUTE_5DAY_LINE_GET,
//                    new BasicNameValuePair("avg", String.valueOf(1)),  //添加avg参数,获取成交额
                    new BasicNameValuePair("symbol", symbol)));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("QuoteDataTask", "e==" + e);
            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
            return null;
        }
    }


    public String getMarketHot(int rateNum, int marketNum) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLMARKET_HOT_GET, new BasicNameValuePair("rateNum", String.valueOf(rateNum)), new BasicNameValuePair("marketNum", String.valueOf(marketNum))));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
            return null;
        }
    }

    private final String STOCKURLMARKET_MAIN_GET = "/market_home/get";

    public String getMarketMain(int mainNum, int potentialNum) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLMARKET_MAIN_GET, new BasicNameValuePair("mainNum", String.valueOf(mainNum)), new BasicNameValuePair("potentialNum", String.valueOf(potentialNum))));
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
            return null;
        }
    }

    /**
     * 网红卡搜索
     * /search/get.do
     *
     * @param key_value
     * @return
     * @throws Exception
     */
    public String searchOLStarCard(String userId, String key_value) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(OLSTAR_SEARCH, new BasicNameValuePair("user_id", userId),
                    new BasicNameValuePair("key_value", key_value)));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 交易详情
     *
     * @return
     * @throws Exception
     */
    public String getTradeDetails(String prod_code) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(OLSTAR_DETAILBS,
                    new BasicNameValuePair("prod_code", prod_code)));
        } catch (Exception e) {
            return null;
        }
    }


    public String sendPing() throws Exception {
        try {
            return tjrNSKManager.sendCommand("");
        } catch (Exception e) {
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }
    /**
     * 首页行情列表刷新
     *
     * @param symbols   自选才需要填 其他填空
     * @param sortField 1：symbol,2：price,3：rate
     * @param sortType
     * @param tab       optional自选、digital数字货币、stock股指期货
     * @return
     * @throws Exception
     */
    public String marketData(String symbols, int sortField, int sortType, String tab) throws Exception {
        return marketData(symbols, sortField, sortType, tab, 1);
    }

    /**
     * 首页行情列表刷新
     *
     * @param symbols   自选才需要填 其他填空
     * @param sortField 1：symbol,2：price,3：rate
     * @param sortType
     * @param tab       optional自选、digital数字货币、stock股指期货
     * @param page      分页
     * @return
     * @throws Exception
     */
    public String marketData(String symbols, int sortField, int sortType, String tab, int page) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(MARKETDATA,
                    new BasicNameValuePair("symbols", symbols),
                    new BasicNameValuePair("sortField", sortField == 1 ? "symbol" : sortField == 2 ? "price" : "rate"),
                    new BasicNameValuePair("sortType", String.valueOf(sortType)),
                    new BasicNameValuePair("tab", tab),
                    new BasicNameValuePair("page", String.valueOf(page))

            ));
        } catch (Exception e) {
            Log.d("marketData", "e==" + e);
            e.printStackTrace();
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }

    /**
     * 我的自选(暂时不用到)
     * socket获取快速行情数据(如自选,多个加逗号隔开)
     *
     * @return
     * @throws Exception
     */
    public String optional(String symbols) throws Exception {
        try {
            return tjrNSKManager.sendCommand(mHttpApi.createUrl(FAST,
                    new BasicNameValuePair("symbols", symbols)

            ));
        } catch (Exception e) {
            Log.d("marketData", "e==" + e);
            e.printStackTrace();
            if (e instanceof InterruptedException) return null;
            return null;
        }
    }


}
