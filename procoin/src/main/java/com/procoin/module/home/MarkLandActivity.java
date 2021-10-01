package com.procoin.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.interfaces.BaseRequestListener;
import com.procoin.http.error.TaojinluException;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.social.util.AvoidMultiClick;
import com.procoin.util.CommonUtil;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.util.TjrMinuteTaskPool;
import com.procoin.widgets.quotitian.StarKlineChart;
import com.procoin.widgets.quotitian.StarRunTimeManager;
import com.procoin.widgets.quotitian.entity.LineTimeEntity;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.widgets.quotitian.entity.StockDayData;
import com.procoin.widgets.quotitian.entity.jsonparser.LineTimeEntityParser;
import com.procoin.widgets.quotitian.entity.jsonparser.StarProDataParser;
import com.procoin.widgets.quotitian.sym.MinuteDigitalTimeLineChart;
import com.procoin.widgets.quotitian.sym.MinuteStockTimeLineChart;
import com.procoin.widgets.quotitian.sym.MinuteTimeLineDay5Chart;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 微盘的横屏页面
 * Created by zhengmj on 16-8-29.
 */
public class MarkLandActivity extends TJRBaseToolBarActivity implements BaseRequestListener {

    private final int DEFAULTKLINEREQENUMTYPE = -8741;//分时的类型
    private final int DEFAULTKLINEREDAYFINEENUMTYPE = -8743;//5日

    private HorizontalScrollView hsmenu; //
    private TextView tvSymbol;
    private TextView tvMinute;   //分时图
    private TextView tvMinuteDay5;
    private TextView tvKlineDay;// 日图
    private TextView tvKlineWeek;//周图
    private TextView tvKlineMonth;//月图
    private TextView  tvKline15Minutes;
    private TextView tvKlineHour;//
    private TextView tvKline1Minutes;//
    private LinearLayout llKlineData;

    private TextView tvJrkp;
    private TextView tvZjcj;
    private TextView tvRate;
    private TextView tvAmt;
    private TextView tvZgcj;
    private TextView tvZdcj;
    private TextView tvDate;
    private TextView tvM5;
    private TextView tvM10;
    private TextView tvM30;
    private TextView tvVol;
    private LinearLayout llMaData;

    private TextView tvMaxPrice;
    private TextView tvMinPrice;
    private TextView tv24Vol;

    private StarProData starProData;

    private StarProDataParser starProDataParser;
    private LineTimeEntityParser lineTimeEntityParser;
    private LineTimeEntity lineTimeEntity;//分时图最新数据

    private String symbol = "";//公共代码
    private StarRunTimeManager starRunTimeManager;

    private MinuteDigitalTimeLineChart minuteLineChart;//今日价格分时图(数字货币)
    private MinuteStockTimeLineChart stockTimeLineChart;//今日价格分时图(股指期货)
    private MinuteTimeLineDay5Chart minuteTimeLineDay5Chart;//分时图5日

    private StarKlineChart goodsKlineChart;

    private FrameLayout weipan_chart;//图表view
    private TjrMinuteTaskPool tjrMinuteTaskPool;

    private int klineReqEnumType = CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute();

    private ImageView ivKlineClose;//关闭

    private static Handler handler = new Handler();
    private String marketType = "";

    private ProgressBar weipan_chart_pro;//行情的加载框



    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, klineReqEnumType);
        setResult(8741, intent);
        super.onBackPressed();
    }


    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try { //当前价格
                String procText = CropymelHttpSocket.getInstance().sendProdCode(symbol);//5
                android.util.Log.d("OLStarHomeLandActivity", "procText==" + procText);
                pareserProCodeJsons(procText);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(klineReqEnumType == DEFAULTKLINEREQENUMTYPE){//因为分时图没有marketType出不来，所以当获得marketType后在设置一遍，不然出不来
                            Log.d("marketType","stockTimeLineChart=="+stockTimeLineChart+"  minuteLineChart=="+minuteLineChart);
                            if(stockTimeLineChart==null&&minuteLineChart==null){
                                setChartByType(klineReqEnumType,true);
                            }
                        }

                        if (starProData != null) {
                            updateUI();
                        }
                        if(marketTypeIsStock()){
                            if(stockTimeLineChart!=null)stockTimeLineChart.parserJsonStock(lineTimeEntity);
                        }else if(marketTypeIsDigital()){
                            if(minuteLineChart!=null)minuteLineChart.parserJsonStock(lineTimeEntity);
                        }
                        minuteTimeLineDay5Chart.parserJsonStock(lineTimeEntity);
                    }
                });
            } catch (Exception e) {
//                com.taojin.http.util.CommonUtil.LogLa(2, "Exception is " + e.getMessage());
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };

    @Override
    protected int setLayoutId() {
        return R.layout.mark_land_activity;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    public static void pageJumpForResult(Context context, String symbol, int klineReqEnumType) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, klineReqEnumType);
        PageJumpUtil.pageJumpResult((AppCompatActivity) context, MarkLandActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            parserParamsBack(bundle, new ParamsBack() {
                @Override
                public void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception {
                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.SYMBOL)) {
                        bundle.putString(CommonConst.SYMBOL, jsonObject.getString(CommonConst.SYMBOL));
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.KEY_EXTRAS_TYPE)) {
                        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, jsonObject.getInt(CommonConst.KEY_EXTRAS_TYPE));
                        CommonUtil.LogLa(2, "jsonObject klineReqEnumType is " + jsonObject.getInt(CommonConst.KEY_EXTRAS_TYPE));
                    }
                }
            });
            if (bundle.containsKey(CommonConst.KEY_EXTRAS_TYPE)) {
                klineReqEnumType = bundle.getInt(CommonConst.KEY_EXTRAS_TYPE, CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
                CommonUtil.LogLa(2, "bundle.containsKey klineReqEnumType is " + klineReqEnumType);
            }

            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL);
            }
        }
        CommonUtil.LogLa(2, "klineReqEnumType is " + klineReqEnumType);
        if (TextUtils.isEmpty(symbol)) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        immersionBar.statusBarView(R.id.statusBar).statusBarDarkFont(false).init();
        starProDataParser = new StarProDataParser();
        lineTimeEntityParser = new LineTimeEntityParser();
        Onclick onclick = new Onclick();

        llKlineData = (LinearLayout) findViewById(R.id.llKlineData);
        hsmenu = (HorizontalScrollView) findViewById(R.id.hsmenu);
        tvZjcj = (TextView) findViewById(R.id.tvZjcj);
        tvZgcj = (TextView) findViewById(R.id.tvZgcj);
        tvZdcj = (TextView) findViewById(R.id.tvZdcj);
        tvJrkp = (TextView) findViewById(R.id.tvJrkp);
        tvRate = (TextView) findViewById(R.id.tvRate);
        tvAmt = (TextView) findViewById(R.id.tvAmt);

        llMaData = (LinearLayout) findViewById(R.id.llMaData);
        tvM5 = (TextView) findViewById(R.id.tvM5);
        tvM10 = (TextView) findViewById(R.id.tvM10);
        tvM30 = (TextView) findViewById(R.id.tvM30);
        tvDate = (TextView) findViewById(R.id.tvDate);
        tvVol = (TextView) findViewById(R.id.tvVol);

        tvMaxPrice = (TextView) findViewById(R.id.tvMaxPrice);
        tvMinPrice = (TextView) findViewById(R.id.tvMinPrice);
        tv24Vol = (TextView) findViewById(R.id.tv24Vol);

        tvSymbol= (TextView) findViewById(R.id.tvSymbol);
        tvSymbol.setText(symbol);

        tvMinute = (TextView) findViewById(R.id.tvMinute); //分时图
        tvMinuteDay5= (TextView) findViewById(R.id.tvMinuteDay5); //分时图
//        tvKlineMin = (TextView) findViewById(R.id.tvKlineMin); // 分钟图
        tvKline15Minutes= (TextView) findViewById(R.id.tvKline15Minutes);
        tvKlineHour = (TextView) findViewById(R.id.tvKlineHour);
        tvKline1Minutes= (TextView) findViewById(R.id.tvKline1Minutes);
        tvKlineDay = (TextView) findViewById(R.id.tvKlineDay); // 日图
        tvKlineWeek = (TextView) findViewById(R.id.tvKlineWeek);//周图
        tvKlineMonth = (TextView) findViewById(R.id.tvKlineMonth);//月图
        weipan_chart = (FrameLayout) findViewById(R.id.weipan_chart);
        ivKlineClose = (ImageView) findViewById(R.id.ivKlineClose);
        weipan_chart_pro = (ProgressBar) findViewById(R.id.weipan_chart_pro);//加载框

        tvMinute.setOnClickListener(onclick);
        tvMinuteDay5.setOnClickListener(onclick);
        tvKlineDay.setOnClickListener(onclick);
        tvKlineWeek.setOnClickListener(onclick);
        tvKlineMonth.setOnClickListener(onclick);
        tvKline15Minutes.setOnClickListener(onclick);
        tvKlineHour.setOnClickListener(onclick);
        tvKline1Minutes.setOnClickListener(onclick);
        ivKlineClose.setOnClickListener(onclick);

        android.util.Log.d("OLStarHomeLandActivity", "klineReqEnumType == DEFAULTKLINEREQENUMTYPE");

        minuteTimeLineDay5Chart = new MinuteTimeLineDay5Chart(this);
        goodsKlineChart = new StarKlineChart(this, true);
        goodsKlineChart.screenType = "h";//横屏
        goodsKlineChart.setKlineChartDataListener(new StarKlineChart.KlineChartDataListener() {
            @Override
            public void returnWhiteLineIndex(boolean isWhiteLine, final boolean isLeft, final StockDayData stockDayData) {
                if (isWhiteLine) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            llKlineData.setVisibility(View.VISIBLE);
                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llKlineData.getLayoutParams();
                            if (isLeft) {
                                lp.gravity = Gravity.RIGHT;
                            } else {
                                lp.gravity = Gravity.LEFT;
                            }
                            llKlineData.setLayoutParams(lp);
                            showAllData(stockDayData);
                        }
                    });
                } else {
//                    llKlineData.setVisibility(View.GONE);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setMaData(stockDayData);
                            llKlineData.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });

        //这个是增加
        goodsKlineChart.setKlineRequestListener(new StarKlineChart.KlineRequestListener() {
            @Override
            public void requestKlineStart() {
                weipan_chart_pro.setVisibility(View.VISIBLE);
            }

            @Override
            public void requestKlineComplete() {
                weipan_chart_pro.setVisibility(View.GONE);
                showAllData(goodsKlineChart.getMidStockDayData());
            }
        });

//        if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {
//            weipan_chart.addView(minuteLineChart);
//        } else {
//            weipan_chart.addView(goodsKlineChart);
//        }
        tjrMinuteTaskPool = new TjrMinuteTaskPool();
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
        setChartByType(klineReqEnumType,true);//k线数据就刷一次就好
        CommonUtil.LogLa(2, "OLStarHomeLandActivity onCreate");
    }


    private class Onclick extends AvoidMultiClick {

        @Override
        public void click(View v) {
            switch (v.getId()) {
                case R.id.ivKlineClose:
                    onBackPressed();
                    break;
                case R.id.tvMinute:
                    setChartByType(DEFAULTKLINEREQENUMTYPE);
                    break;
                case R.id.tvMinuteDay5:
                    setChartByType(DEFAULTKLINEREDAYFINEENUMTYPE);
                    break;
                case R.id.tvKlineDay:
                    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
                    CommonUtil.LogLa(2, "tvKlineDay onclick ");
                    break;
                case R.id.tvKlineWeek:
                    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
                    break;
                case R.id.tvKlineMonth:
                    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
                    break;
                case R.id.tvKline15Minutes:
                    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_15.minute());
                    break;
                case R.id.tvKlineHour:
                    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_60.minute());
                    break;
                case R.id.tvKline1Minutes:
                    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_1.minute());
                    break;


            }
        }
    }


    private void updateMenuSelect(CropymelHttpSocket.KlineReqEnum klineReqEnum, int mintueType) {
        tvMinute.setSelected(false); //分时图
        tvMinuteDay5.setSelected(false); //分时图
        tvKlineDay.setSelected(false);// 日图
        tvKlineWeek.setSelected(false);
        tvKlineMonth.setSelected(false);
        tvKline15Minutes.setSelected(false);
        tvKlineHour.setSelected(false);
        tvKline1Minutes.setSelected(false);
        if (klineReqEnum == null) {//
            klineReqEnumType = mintueType;
            if (klineReqEnumType == DEFAULTKLINEREDAYFINEENUMTYPE) {
                tvMinuteDay5.setSelected(true);
            } else if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {
                tvMinute.setSelected(true);
            }
            return;
        }
        klineReqEnumType = klineReqEnum.minute();
        CommonUtil.LogLa(2, "klineReqEnum is  " + klineReqEnum.minute());
        switch (klineReqEnum) {
            case KLINE_DAY:
                tvKlineDay.setSelected(true);
//                CommonUtil.LogLa(2, "tvKlineDay is  " + tvKlineDay.isSelected());
                break;
            case KLINE_WEEK:
                tvKlineWeek.setSelected(true);
                break;
            case KLINE_MONTH:
                tvKlineMonth.setSelected(true);
                break;
            case KLINE_15:
                tvKline15Minutes.setSelected(true);
                break;
            case KLINE_60:
                tvKlineHour.setSelected(true);
                break;
            case KLINE_1:
                tvKline1Minutes.setSelected(true);
                break;


        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeLandActivity onResume");
//        starRunTimeManager.onResume();
        if (tjrMinuteTaskPool != null) {
            tjrMinuteTaskPool.startTime(this, scheduledTask);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtil.LogLa(2, "OLStarHomeLandActivity onPause");
//        starRunTimeManager.onPause();
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
        if (isFinishing()) {
            handler.removeCallbacksAndMessages(null);
            if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
//            if (udpateBalanceReceive != null) unregisterReceiver(udpateBalanceReceive);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtil.LogLa(2, "OLStarHomeLandActivity onDestroy");
    }


    @Override
    public void requestStart() {
        showProgressDialog("");
    }

    @Override
    public void requestComplete(Object result) {
        dismissProgressDialog();
    }
    private boolean marketTypeIsStock(){
        return "stock".equals(marketType);
    }

    private boolean marketTypeIsDigital(){
        return "digital".equals(marketType);
    }
    private void setChartByType(int klineReqEnumType) {
        setChartByType(klineReqEnumType,false);
    }
    private void setChartByType(int klineReqEnumType,boolean isFirst) {//切换行情图添用这个(加对应的画图view,并且获取数据画图)
        CommonUtil.LogLa(2, "this.klineReqEnumType is " + this.klineReqEnumType + " klineReqEnumType is " + klineReqEnumType);
        if (this.klineReqEnumType != klineReqEnumType) {
            this.klineReqEnumType = klineReqEnumType;
        }
        weipan_chart.removeAllViews();
        if (minuteLineChart != null) minuteLineChart.setVisibility(View.INVISIBLE);
        if (stockTimeLineChart != null) stockTimeLineChart.setVisibility(View.INVISIBLE);
        minuteTimeLineDay5Chart.setVisibility(View.INVISIBLE);
        if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {

            if (marketTypeIsStock()) {
                llMaData.setVisibility(View.GONE);
                if(stockTimeLineChart==null){
                    stockTimeLineChart=new MinuteStockTimeLineChart(this);
                }
                stockTimeLineChart.setVisibility(View.VISIBLE);
                weipan_chart.addView(stockTimeLineChart, 0);
                stockTimeLineChart.postInvalidate();
                goodsKlineChart.clearAllData();
                updateMenuSelect(null, DEFAULTKLINEREQENUMTYPE);
            } else if (marketTypeIsDigital()) {
                llMaData.setVisibility(View.GONE);
                if(minuteLineChart==null){
                    minuteLineChart = new MinuteDigitalTimeLineChart(this);
                }
                minuteLineChart.setVisibility(View.VISIBLE);
                weipan_chart.addView(minuteLineChart, 0);
                minuteLineChart.postInvalidate();
                goodsKlineChart.clearAllData();
                updateMenuSelect(null, DEFAULTKLINEREQENUMTYPE);
            }


        } else if (klineReqEnumType == DEFAULTKLINEREDAYFINEENUMTYPE) {
            llMaData.setVisibility(View.GONE);
            minuteTimeLineDay5Chart.setVisibility(View.VISIBLE);
            weipan_chart.addView(minuteTimeLineDay5Chart, 0);
            minuteTimeLineDay5Chart.postInvalidate();
            goodsKlineChart.clearAllData();
            updateMenuSelect(null, DEFAULTKLINEREDAYFINEENUMTYPE);
        } else {
            llMaData.setVisibility(View.VISIBLE);
            weipan_chart.addView(goodsKlineChart, 0);
            updateMenuSelect(CropymelHttpSocket.KlineReqEnum.getKlineReqEnumByMinute(klineReqEnumType), 0);
        }
        refreshChart();//添加完成之后刷新,这个放到外面就是说比如你当前是5分钟k线,当你再次点击5分钟K线就帮你刷新一次
    }

    private void refreshChart() {//只是刷新当前的行情图用这个
        if (!TextUtils.isEmpty(symbol)) {
            if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {
                if(marketTypeIsStock()){
                    if(stockTimeLineChart!=null)stockTimeLineChart.startGetDataTask(symbol, weipan_chart_pro);
                }else if(marketTypeIsDigital()){
                    if(minuteLineChart!=null)minuteLineChart.startGetDataTask(symbol, weipan_chart_pro);
                }
            }else if (klineReqEnumType == DEFAULTKLINEREDAYFINEENUMTYPE) {//5日分时图
                minuteTimeLineDay5Chart.startGetDataTask(symbol, weipan_chart_pro);
            }else {
                //获取历史线
                goodsKlineChart.post(new Runnable() {
                    @Override
                    public void run() {
                        goodsKlineChart.startGetStockKlineTask(CropymelHttpSocket.KlineReqEnum.getKlineReqEnumByMinute(klineReqEnumType), symbol, "0");
                    }
                });

            }
        }
    }

    /**
     * @param stockDayData
     * @param ,,
     */
    private void showAllData(StockDayData stockDayData) {
        if (goodsKlineChart == null) return;
        if (stockDayData != null) {
            int color = StockChartUtil.getRateTextColor(this, stockDayData.getRate());
            tvRate.setTextColor(color);
            tvAmt.setTextColor(color);

            tvZjcj.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getZjcj()));
            tvJrkp.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getJrkp()));
            tvRate.setText(StockChartUtil.formatNumWithSign(2, stockDayData.getRate(), true) + "%");
            tvAmt.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getAmt()));
            tvZgcj.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getZgcj()));
            tvZdcj.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getZdcj()));
            tvDate.setText(goodsKlineChart.strIntToFormate(String.valueOf(stockDayData.getDate())));
            setMaData(stockDayData);
            tvVol.setText(stockDayData.getCjsl());

        }
    }


    private void setMaData(StockDayData stockDayData) {
        if (stockDayData == null) return;
        tvM5.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getM5()));
        tvM10.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getM10()));
        tvM30.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getM30()));
    }

    private synchronized void pareserProCodeJsons(String json) throws TaojinluException, JSONException {
        if (json == null) return;
        JSONObject jsonObject = new JSONObject(json);
        if (JsonParserUtils.hasAndNotNull(jsonObject, "isRun")) {
            MainApplication.isRun = jsonObject.getBoolean("isRun");
        }
        if (JsonParserUtils.hasAndNotNull(jsonObject, symbol)) {
            starProData = starProDataParser.parse(jsonObject.getJSONObject(symbol));
            marketType = starProData.marketType;
        }
        lineTimeEntity = lineTimeEntityParser.parse(jsonObject, "min_" + symbol);
    }


    public void updateUI() {

        tvMaxPrice.setText(starProData.high);
        tvMinPrice.setText(starProData.low);
        tv24Vol.setText(starProData.amount);

        if (minuteLineChart != null) {
            minuteLineChart.priceDecimals = starProData.priceDecimals;
            minuteLineChart.tip = starProData.tip;
        }
        if (stockTimeLineChart != null) {
            stockTimeLineChart.priceDecimals = starProData.priceDecimals;
            stockTimeLineChart.tip = starProData.tip;
            stockTimeLineChart.resetDisplayNum(starProData.prefixType);
        }

        if (minuteTimeLineDay5Chart != null) {
            minuteTimeLineDay5Chart.priceDecimals = starProData.priceDecimals;
            minuteTimeLineDay5Chart.tip = starProData.tip;
        }

        if (goodsKlineChart != null) {
            goodsKlineChart.priceDecimals = starProData.priceDecimals;
            goodsKlineChart.tip = starProData.tip;
        }
    }


}
