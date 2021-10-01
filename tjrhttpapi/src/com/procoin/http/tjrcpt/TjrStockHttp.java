//package com.cropyme.http.tjrcpt;
//
//import java.io.IOException;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import com.cropyme.http.TjrBaseApi;
//import com.cropyme.http.common.CommonConst;
//import com.cropyme.nsk.Protocol;
//import com.cropyme.nsk.TjrNSKManager;
//import com.cropyme.http.HttpApi;
//import com.cropyme.http.error.TaojinluException;
//
//public class TjrStockHttp {
//    private static final String STOCKURLROOT = "/";
//    private static TjrStockHttp instance;
//    private static TjrNSKManager tjrNSKManager = TjrNSKManager.getInstance();
//
//    private final String mApiStockRealUrl; // 主要对行情
//    private final String[] KLINE_CYCLE_METHOD = {"get5Mindata", "get10Mindata", "get15Mindata", "get60Mindata", "findDateKlineBybAnda", "findWeekDateKlineByNum", "findMonthDateKlineByNum"};// K线的周期方法,0代表5分钟线,1代表15分钟线,2代表30分钟线,3代表60分钟线,4代表日线,5代表周线，6代表月线
//
//    public enum KlineMethod {
//        KLINE_5MIN("find5MinDataKlineList"), KLINE_10MIN("find10MinDataKlineList"), KLINE_15MIN("find15MinDataKlineList"), KLINE_30MIN("find30MinDataKlineList"), KLINE_60MIN("find60MinDataKlineList"), KLINE_DAY("findDayDateKlineList"), KLINE_WEEK("findWeekDateKlineList"), KLINE_MONTH("findMonthDateKlineList");
//
//        private final String methodName;
//
//        // 构造方法
//        private KlineMethod(String methodName) {
//            this.methodName = methodName;
//        }
//
//        public String methodName() {
//            return methodName;
//        }
//    }
//
//    private final HttpApi mHttpApi;
//
//    private TjrStockHttp() {
//        mHttpApi = new HttpApi();
//        mApiStockRealUrl = "http://" + TjrBaseApi.stockHomeUri.uri() + ":9999/";
//    }
//
//
//    public static TjrStockHttp getInstance() {
//        if (instance == null) {
//            synchronized (TjrStockHttp.class) {
//                if (instance == null) instance = new TjrStockHttp();
//            }
//        }
//        return instance;
//    }
//
//    public void clearPool() {
//        try {
//            tjrNSKManager.clearPool();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    public void initConnect() {
//        try {
//            tjrNSKManager.sendCommand(Protocol.PING);
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    /**
//     * 获取当前个股实时数据
//     *
//     * @param fdms
//     * @return
//     * @throws Exception
//     */
//    public String sendDefaultStock(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    public String sendDefaultStockHTTP(String fdms) throws Exception {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("fullcodes", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//    }
//
//    /**
//     * 获取个股分时线历史数据
//     *
//     * @param fdms
//     * @return
//     * @throws Exception
//     */
//    public String sendDefaultStockMinDataHis(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("getMinuteDataHis", fdms)));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("getMinuteDataHis", fdms));
//        }
//    }
//
//    public String sendDefaultStockMinDataHisHTTP(String fdms) throws Exception {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("getMinuteDataHis", fdms));
//    }
//
//    /**
//     * 获取指数均线历史数据
//     *
//     * @param fdms
//     * @return
//     * @throws Exception
//     */
//    public String sendDefaultIndexMaResultHis(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("getMaResultHis", fdms)));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("getMaResultHis", fdms));
//        }
//    }
//
//    public String sendDefaultIndexMaResultHisHTTP(String fdms) throws Exception {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("getMaResultHis", fdms));
//    }
//
//    /**
//     * 获取主页自选股及上证分时线数据
//     *
//     * @param fdms   自选股+上证s_sh000001
//     * @param minFdm 上证sh000001
//     * @return
//     */
//    public String sendHomeAll(String fdms, String minuDateFdm) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findHomeAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("minuDateFdm", minuDateFdm), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findHomeAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("minuDateFdm", minuDateFdm), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 获取主页自选股及上证分时线数据
//     *
//     * @param fdms 自选股+上证s_sh000001
//     * @return
//     */
//    public String sendHomeStock(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findHomeStock"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findHomeStock"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 获取指数详细页面数据
//     *
//     * @param fdms
//     * @return
//     */
//    public String sendIndexDetail(String fdms, String mvFdm) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findIndexDetail"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("getMarketValue", mvFdm), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findIndexDetail"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("getMarketValue", mvFdm), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 指数当前实时数据详细+上涨，平盘，下跌+指数分时历史数据
//     *
//     * @param fdms
//     * @param macode sh01,sz01,sz05,sz06
//     * @return
//     */
//    public String sendIndexShareAll(String fdms, String macode) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findIndexShareAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findIndexShareAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 指数当前实时数据详细+上涨，平盘，下跌
//     *
//     * @param fdms
//     * @param macode sh01,sz01,sz05,sz06
//     * @return
//     */
//    public String sendIndexShare(String fdms, String macode) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findIndexShare"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findIndexShare"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    public String sendIndexShareHttp(String fdms, String macode) throws Exception {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findIndexShare"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair(CommonConst.ISRUN, ""));
//    }
//
//    /**
//     * 指数横屏异动指标股+当前实时数据+均线实时数据
//     *
//     * @param fdms
//     * @param macode sh01,sz01,sz05,sz06
//     * @return
//     */
//    public String sendIndexLandscape(String fdms, String macode, String num) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findIndexLandscape"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair("num", num), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findIndexLandscape"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("macode", macode), new BasicNameValuePair("num", num), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 热点涨幅榜，涨速榜，领涨板块
//     *
//     * @return
//     * @throws Exception
//     */
//    public String sendIndexAHot(String rateNum, String rate5mNum, String ratebkNum) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findIndexAHot"), new BasicNameValuePair("rateNum", rateNum),// 涨幅榜
//                    new BasicNameValuePair("rate5mNum", rate5mNum),// 涨速榜
//                    new BasicNameValuePair("ratebkNum", ratebkNum),// 领涨板块
//                    new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findIndexAHot"), new BasicNameValuePair("rateNum", rateNum),// 涨幅榜
//                    new BasicNameValuePair("rate5mNum", rate5mNum),// 涨速榜
//                    new BasicNameValuePair("ratebkNum", ratebkNum),// 领涨板块
//                    new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 指点里面股票排行
//     */
//    public String sendRankListData(String changeRateType, String field, String sort, int startNum, int endNum) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("getHotSpot", changeRateType + "," + field + "," + sort + "," + startNum + "," + endNum),//
//                    new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                    new BasicNameValuePair("getHotSpot", changeRateType + "," + field + "," + sort + "," + startNum + "," + endNum),//
//                    new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 板块详细
//     */
//    public String sendPlateDetails(String plateId, String field, String sort, int startNum, int endNum, int plateType) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("get5MinPlate", plateId + "," + field + "," + sort + "," + startNum + "," + endNum + "," + plateType), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                    new BasicNameValuePair("get5MinPlate", plateId + "," + field + "," + sort + "," + startNum + "," + endNum + "," + plateType), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 个股当前分时页面实时数据+分时历史数据
//     *
//     * @param fdms
//     * @return
//     * @throws Exception
//     */
//    public String sendStockShareAll(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findStockShareAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findStockShareAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    public String sendStockShareAllHTTP(String fdms) throws Exception {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findStockShareAll"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//    }
//
//    /**
//     * 获取我自选股列表
//     *
//     * @param fdms
//     * @param p
//     * @throws Exception
//     */
//    public String sendMyStockList(String fdms, String p) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findMyStockList"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("p", p), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findMyStockList"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair("p", p), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 个股当前分时页面实时数据
//     *
//     * @param fdms
//     * @throws Exception
//     */
//    public String sendStockShare(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findStockShare"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findStockShare"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 横屏个股当前实时数据
//     *
//     * @param fdms
//     * @throws Exception
//     */
//    public String sendStockLandscape(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findStockLandscape"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findStockLandscape"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * 个股当前明细实时数据
//     *
//     * @param fdms
//     * @throws Exception
//     */
//    public String sendStockDetail(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair("method", "findStockDetail"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, "")));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("method", "findStockDetail"), new BasicNameValuePair("fdms", fdms), new BasicNameValuePair(CommonConst.ISRUN, ""));
//        }
//    }
//
//    /**
//     * K线数据
//     *
//     * @param fdms  股票代码
//     * @param cycle 周期 0代表5分钟线,1代表15分钟线,2代表30分钟线,3代表60分钟线,4代表日线,5代表周线，6代表月线
//     * @param num   请求K线条数
//     * @param date  请求开始日期(20121231)
//     * @param bAnda 从请求日期往今天还是往过去拿数据(a代表往前(今天)拿,b代表往后(过去)拿)
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String sendKLineData(String fdms, int cycle, String num, String date, String bAnda) throws Exception {
//        try {
//            // return
//            // tjrNSKManager.sendCommand(tjrStockHttpApi.sendKLineDataSK(fdms,
//            // cycle, num, date, bAnda));
//            if (cycle < 4) {
//                return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, new BasicNameValuePair(KLINE_CYCLE_METHOD[cycle], fdms + "," + num)));
//            } else {
//                return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, //
//                        new BasicNameValuePair("method", KLINE_CYCLE_METHOD[cycle]), //
//                        new BasicNameValuePair("fdms", fdms), //
//                        new BasicNameValuePair("num", num),//
//                        new BasicNameValuePair("date", date),//
//                        new BasicNameValuePair("bAnda", bAnda)));
//            }
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            // return tjrStockHttpApi.sendKLineDataHTTP(fdms, cycle, num, date,
//            // bAnda);
//            if (cycle < 4) {
//                return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair(KLINE_CYCLE_METHOD[cycle], fdms + "," + num));
//            } else {
//                return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                        new BasicNameValuePair("method", KLINE_CYCLE_METHOD[cycle]), //
//                        new BasicNameValuePair("fdms", fdms), //
//                        new BasicNameValuePair("num", num),//
//                        new BasicNameValuePair("date", date),//
//                        new BasicNameValuePair("bAnda", bAnda));
//            }
//        }
//    }
//
//    /**
//     * 获取K线数据 socket and http
//     *
//     * @param fdms    此参数不能为空
//     * @param //cycle   此参数不能为空
//     * @param dAndx   dAndx=d代表大于，dAndx=x代表小于,dAndx=dd代表大于或等于，dAndx=xd代表小于等于,想对于日期比较
//     *                ，此参数可以为空，默认xd
//     * @param num     根数,此参数可以为空，默认120
//     * @param date    日期 如date=201140620 此参数可以为空，默认今天
//     * @param //isKline 是否要K线数据,isKline=0不要K线数据,isKline=1要加入K线数据,此参数可以为空，默认1
//     * @param //join    加入额外数据,join=cjsl,join=macd,join=kdj,此参数可以为空,为空不加入此数据
//     * @return
//     */
//    public String sendKLineBySKOrHTTP(String fdms, KlineMethod klineMethod, String dAndx, String num, String date) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT, //
//                    new BasicNameValuePair("method", klineMethod.methodName), //
//                    new BasicNameValuePair("fdms", fdms), //
//                    new BasicNameValuePair("dAndx", dAndx),//
//                    new BasicNameValuePair("num", num),//
//                    new BasicNameValuePair("date", date)));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                    new BasicNameValuePair("method", klineMethod.methodName), //
//                    new BasicNameValuePair("fdms", fdms), //
//                    new BasicNameValuePair("dAndx", dAndx),//
//                    new BasicNameValuePair("num", num),//
//                    new BasicNameValuePair("date", date));
//        }
//    }
//
//    /**
//     * 获取K线数据 http
//     *
//     * @param fdms    此参数不能为空
//     * @param cycle   此参数不能为空
//     * @param dAndx   dAndx=d代表大于，dAndx=x代表小于,dAndx=dd代表大于或等于，dAndx=xd代表小于等于,想对于日期比较
//     *                ，此参数可以为空，默认xd
//     * @param num     根数,此参数可以为空，默认120
//     * @param date    日期 如date=201140620 此参数可以为空，默认今天
//     * @param isKline 是否要K线数据,isKline=0不要K线数据,isKline=1要加入K线数据,此参数可以为空，默认1
//     * @param join    加入额外数据,join=cjsl,join=macd,join=kdj,此参数可以为空,为空不加入此数据
//     * @return
//     */
//    public String sendKLineByHTTP(String fdms, KlineMethod klineMethod, String dAndx, String num, String date) throws Exception {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                new BasicNameValuePair("method", klineMethod.methodName), //
//                new BasicNameValuePair("fdms", fdms), //
//                new BasicNameValuePair("dAndx", dAndx),//
//                new BasicNameValuePair("num", num),//
//                new BasicNameValuePair("date", date));
//    }
//
//    /**
//     * 股本信息 获取总股本、流通股本、最近四个季度净利润
//     *
//     * @param fdms
//     * @return
//     * @throws Exception
//     */
//    public String sendStockCapital(String fdms) throws Exception {
//        try {
//            return tjrNSKManager.sendCommand(mHttpApi.createUrl(STOCKURLROOT,//
//                    new BasicNameValuePair("getCapital", fdms)));
//        } catch (Exception e) {
//            if (e instanceof InterruptedException) return null;
//            return mHttpApi.doHttpGet(mApiStockRealUrl,//
//                    new BasicNameValuePair("getCapital", fdms));
//        }
//    }
//
//    /**
//     * 更新键盘数据
//     * "http://share.taojinroad.com:9999/?method=updateAllStockAndPlate&time=" +
//     * time;
//     *
//     * @param time
//     * @return
//     */
//    public String updateAllStockAndPlate(String time) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                new BasicNameValuePair("method", "updateAllStockAndPlate"), new BasicNameValuePair("time", time));
//    }
//
//    /**
//     * 好友的指数分时线
//     *
//     * @param params
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String getFriendIndexLineHis(String params) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, //
//                new BasicNameValuePair("getIndexLine", params));
//    }
//
//    public String getIsRun() throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiStockRealUrl, new BasicNameValuePair("isRun", ""));
//    }
//
//}
