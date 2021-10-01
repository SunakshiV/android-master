package com.procoin.module.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.TjrMinuteTaskPool;
import com.procoin.widgets.quotitian.StarDetailPriceView_N;
import com.procoin.widgets.quotitian.StarRunTimeManager;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.error.TaojinluException;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.module.home.trade.TradeActivity;
import com.procoin.module.home.trade.dialog.BuyDialogFragment;
import com.procoin.social.TjrSocialMTAUtil;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.RxAsyncTask;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.quotitian.StarKlineChart;
import com.procoin.widgets.quotitian.StarMinuteLineChart;
import com.procoin.widgets.quotitian.entity.StockDayData;
import com.procoin.widgets.quotitian.entity.jsonparser.StarProDataParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 行情
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class MarketActivity2 extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.weipan_chart)
    FrameLayout weipanChart; //存放:分时图+条形图
    @BindView(R.id.weipan_chart_pro)
    ProgressBar weipanChartPro;//进度
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPriceCash)
    TextView tvPriceCash;

    @BindView(R.id.tvPriceRate)
    TextView tvPriceRate;
    @BindView(R.id.sdpDetailPriceList)
    StarDetailPriceView_N sdpDetailPriceList;
    //    @BindView(R.id.llMarkMenu)
//    LinearLayoutMarkMenu llMarkMenu;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private final int DEFAULTKLINEREQENUMTYPE = -8741;//今日价格
    private final int DEFAULTKLINEREHOURQENUMTYPE = -8742;//小时价格
    @BindView(R.id.tvMinute)
    TextView tvMinute;
    @BindView(R.id.tvKlineDay)
    TextView tvKlineDay;
    @BindView(R.id.tvKlineWeek)
    TextView tvKlineWeek;
    @BindView(R.id.tvKlineMonth)
    TextView tvKlineMonth;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvJrkp)
    TextView tvJrkp;
    @BindView(R.id.tvZjcj)
    TextView tvZjcj;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvZgcj)
    TextView tvZgcj;
    @BindView(R.id.tvZdcj)
    TextView tvZdcj;
    @BindView(R.id.tvVol)
    TextView tvVol;
    @BindView(R.id.llTop)
    LinearLayout llTop;
    @BindView(R.id.tvM5)
    TextView tvM5;
    @BindView(R.id.tvM10)
    TextView tvM10;
    @BindView(R.id.tvM20)
    TextView tvM20;
    @BindView(R.id.llKlineData)
    LinearLayout llKlineData;

    private int klineReqEnumType = DEFAULTKLINEREQENUMTYPE;

    //    private StarMinuteHourLineChart minuteHourLineChart;//小时价格分时图,只是分时图,不包括右边的3档,属于starMinuteVIew里面的
    private StarMinuteLineChart minuteLineChart;//今日价格分时图
    private StarKlineChart goodsKlineChart;//K线
    private StarProData starProData;

    private StarRunTimeManager starRunTimeManager;
    private TjrMinuteTaskPool tjrMinuteTaskPool;
    private Handler handler = new Handler();

    private BuyDialogFragment buyDialogFragment;


    private String symbol = "";//币种
    public int decimal = 6; // 小数保留位数


    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        PageJumpUtil.pageJump(context, MarketActivity2.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.market;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.market);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }
        if (TextUtils.isEmpty(symbol)) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        mActionBar.setTitle(symbol);
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);

        tvMinute.setOnClickListener(this);
        tvKlineDay.setOnClickListener(this);
        tvKlineWeek.setOnClickListener(this);
        tvKlineMonth.setOnClickListener(this);

        tvMinute.setSelected(true);

        starRunTimeManager = new StarRunTimeManager();
        tjrMinuteTaskPool = new TjrMinuteTaskPool();

//        minuteHourLineChart = new StarMinuteHourLineChart(this);
//        minuteHourLineChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TjrSocialMTAUtil.trackCustomKVEvent(getApplicationContext(), "交易页横屏点击次数", "小时图", TjrSocialMTAUtil.MTATradeLandscapeClick);
//                Bundle bundle = new Bundle();
//                bundle.putString(CommonConst.PARAMS, OLStarHomeLandActivity.parms2JsonStr(proCode, klineReqEnumType));
//                PageJumpUtil.pageJumpResult(OLStarHomeActivity.this, OLStarHomeLandActivity.class, bundle);
//            }
//        });
//        minuteHourLineChart.mReGetHisListen = new StarMinuteHourLineChart.ReGetHisListen() {
//            @Override
//            public void reGetHisListen() {
//                CommonUtil.LogLa(2, "minuteHourLineChart is StarMinuteLineChart");
//                if (minuteHourLineChart.getVisibility() == View.VISIBLE)
//                    new QuoteDataTask(DEFAULTKLINEREHOURQENUMTYPE, true).execute();
//            }
//        };


        minuteLineChart = new StarMinuteLineChart(this);
        minuteLineChart.isMaxSize = false;
        minuteLineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                TjrSocialMTAUtil.trackCustomKVEvent(getApplicationContext(), "交易页横屏点击次数", "全天图", TjrSocialMTAUtil.MTATradeLandscapeClick);
//                Bundle bundle = new Bundle();
//                bundle.putString(CommonConst.PARAMS, OLStarHomeLandActivity.parms2JsonStr(proCode, klineReqEnumType));
//                PageJumpUtil.pageJumpResult(OLStarHomeActivity.this, OLStarHomeLandActivity.class, bundle);
                MarkLandActivity.pageJumpForResult(MarketActivity2.this, symbol, klineReqEnumType);
            }
        });

        minuteLineChart.mReGetHisListen = new StarMinuteLineChart.ReGetHisListen() {
            @Override
            public void reGetHisListen() {
                CommonUtil.LogLa(2, "minuteLineChart is StarMinuteLineChart");
                if (minuteLineChart.getVisibility() == View.VISIBLE)
                    new QuoteDataTask(DEFAULTKLINEREQENUMTYPE, true).execute();
            }
        };

        goodsKlineChart = new StarKlineChart(this);
        goodsKlineChart.setKlineRequestListener(new StarKlineChart.KlineRequestListener() {
            @Override
            public void requestKlineStart() {
                weipanChartPro.setVisibility(View.VISIBLE);
            }

            @Override
            public void requestKlineComplete() {
                weipanChartPro.setVisibility(View.GONE);
            }
        });

        goodsKlineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TjrSocialMTAUtil.trackCustomKVEvent(getApplicationContext(), "交易页横屏点击次数", "K线图_" + klineReqEnumType, TjrSocialMTAUtil.MTATradeLandscapeClick);
//                Bundle bundle = new Bundle();
//                bundle.putString(CommonConst.PARAMS, OLStarHomeLandActivity.parms2JsonStr(proCode, klineReqEnumType));
//                PageJumpUtil.pageJumpResult(OLStarHomeActivity.this, OLStarHomeLandActivity.class, bundle);
                MarkLandActivity.pageJumpForResult(MarketActivity2.this, symbol, klineReqEnumType);
            }
        });
        //        //这个增加监听白线显示或消失
        goodsKlineChart.setKlineChartDataListener(new StarKlineChart.KlineChartDataListener() {
            @Override
            public void returnWhiteLineIndex(boolean isWhiteLine, final boolean isLeft,final StockDayData stockDayData) {
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            llKlineData.setVisibility(View.GONE);
                        }
                    });
                }
            }
        });
        weipanChart.addView(minuteLineChart);
//        llMarkMenu.setOnMenuItemClick(new LinearLayoutMarkMenu.OnMenuItemClick() {
//            @Override
//            public void onFsClick() {
//                setChartByType(DEFAULTKLINEREQENUMTYPE);
//            }
//
//            @Override
//            public void onDayKlineClick() {
//                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
//
//            }
//
//            @Override
//            public void onWeekKlineClick() {
//                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
//            }
//
//            @Override
//            public void onMonthKlineClick() {
//                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
//
//            }
//
//            @Override
//            public void onMenuOpen() {
//
//            }
//
//            @Override
//            public void onMenuClose() {
//
//            }
//        });

//        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
//            @Override
//            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
//                llMarkMenu.closeMenu("");
//            }
//        });

        refreshChart();//k线数据就刷一次就好
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
    }


    private void showAllData(StockDayData stockDayData) {
        if (stockDayData != null) {
            int color = StockChartUtil.getRateTextColor(this,stockDayData.getRate());
            tvZjcj.setTextColor(color);
            tvRate.setTextColor(color);
            color = StockChartUtil.getRateColor(stockDayData.getJrkp(), stockDayData.getZrsp());
            tvJrkp.setTextColor(color);
            color = StockChartUtil.getRateColor(stockDayData.getZgcj(), stockDayData.getZrsp());
            tvZgcj.setTextColor(color);
            color = StockChartUtil.getRateColor(stockDayData.getZdcj(), stockDayData.getZrsp());
            tvZdcj.setTextColor(color);
            tvZjcj.setText(StockChartUtil.formatNumber(decimal, stockDayData.getZjcj()));
            tvJrkp.setText(StockChartUtil.formatNumber(decimal, stockDayData.getJrkp()));
            tvRate.setText(StockChartUtil.formatNumWithSign(2, stockDayData.getRate(), true) + "%");
            tvZgcj.setText(StockChartUtil.formatNumber(decimal, stockDayData.getZgcj()));
            tvZdcj.setText(StockChartUtil.formatNumber(decimal, stockDayData.getZdcj()));
//            tvCjsl.setText(StockChartUtil.formatVolume(stockDayData.getCjsl()));
//            if (tvKlineMin5.isSelected() || tvKlineMin15.isSelected() || tvKlineMin30.isSelected() || tvKlineMin60.isSelected()) {
//                tvDate.setText(VeDate.getDateToMMddHHmm(VeDate.strLongToDate("20" + stockDayData.getDate() + "00")));
//            } else {
            tvDate.setText(goodsKlineChart.strIntToFormate(String.valueOf(stockDayData.getDate())));
//            }
            tvM5.setText(StockChartUtil.formatNumber(decimal, stockDayData.getM5()));
            tvM10.setText(StockChartUtil.formatNumber(decimal, stockDayData.getM10()));
            tvM20.setText(StockChartUtil.formatNumber(decimal, stockDayData.getM30()));
//            tvVol.setText(StockChartUtil.formatMoney(stockDayData.getCjsl(), decimal, decimal));
            tvVol.setText(stockDayData.getCjsl());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeActivity onResume");
//        starRunTimeManager.onResume();
        if (tjrMinuteTaskPool != null)
            tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
//            tjrMinuteTaskPool.startTime(getApplicationContext(), scheduledTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        starRunTimeManager.onPause();
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();

    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonUtil.LogLa(2, "OLStarHomeActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtil.LogLa(2, "OLStarHomeActivity onDestroy");
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }
//           case R.id.tvMinute:
//    setChartByType(DEFAULTKLINEREQENUMTYPE);
//                break;
//            case R.id.tvKlineDay:
//    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
//                break;
//            case R.id.tvKlineWeek:
//    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
//                break;
//            case R.id.tvKlineMonth:
//    setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
//                break;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8741) {//横屏回来的参数
            setChartByType(data.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, klineReqEnumType));
            if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) { //今日价格
//                llMarkMenu.setTvCurrText("分时");
                setChartByType(DEFAULTKLINEREQENUMTYPE);
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute()) {
//                llMarkMenu.setTvCurrText("日k");
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute()) {
//                llMarkMenu.setTvCurrText("周k");
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute()) {
//                llMarkMenu.setTvCurrText("月k");
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
            }
        }
    }

    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try { //当前价格
                long startTime = System.currentTimeMillis();
                Log.d("190", "start Time == " + startTime);
                String procText = CropymelHttpSocket.getInstance().sendProdCode(symbol, 5);//5
                Log.d("190", " Time == " + (System.currentTimeMillis() - startTime));
                Log.d("190", "procText == " + procText);
//                String resultM = CropymelHttpSocket.getInstance().sendProdCodeMinuteLine(symbol);
//                Log.d("190", "resultM == " + resultM);
                CommonUtil.LogLa(2, " procText is " + procText);
                pareserProCodeJsons(procText);
                try {
//                    minuteLineChart.parserJsonStock(starProData);
//                    minuteHourLineChart.parserJsonStock(starProData);
                } catch (Exception e) {
                    CommonUtil.LogLa(2, "Exception e is " + e.getMessage());
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (starProData != null) {
                            updateUI();
//                            autoShowTradeDialog();//防止
                        }
                    }
                });
            } catch (Exception e) {
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };

    private void refreshChart() {//只是刷新当前的行情图用这个
        Log.d("kline", "startGetStockKlineTask33333");
        if (!TextUtils.isEmpty(symbol)) {
            if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {//今日价格
                if (!minuteLineChart.isInit) {
                    new QuoteDataTask(klineReqEnumType, true).execute();
                }
            } else if (klineReqEnumType == DEFAULTKLINEREHOURQENUMTYPE) {//小时价格
//                if (!minuteHourLineChart.isInit) {
//                    new QuoteDataTask(klineReqEnumType, true).execute();
//                }
            } else {
                Log.d("kline", "startGetStockKlineTask44444");
                //获取历史线
                goodsKlineChart.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("kline", "startGetStockKlineTask、、、、、、、");
                        goodsKlineChart.startGetStockKlineTask(CropymelHttpSocket.KlineReqEnum.getKlineReqEnumByMinute(klineReqEnumType), symbol, "0");
                    }
                });
//            }
            }
        }

    }

    private class QuoteDataTask extends RxAsyncTask<String, Void, String> {
        private int klineType;
        private boolean showProDialog;

        public QuoteDataTask(int klineType, boolean showProDialog) {
            this.klineType = klineType;
            this.showProDialog = showProDialog;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtil.LogLa(2, "QuoteDataTask onPreExecute");
            if (showProDialog) weipanChartPro.setVisibility(View.VISIBLE);
        }

        @Override
        protected String call(String... strings) {
            CommonUtil.LogLa(2, "QuoteDataTask call");
            try {
                String resultM = null;
                if (klineType == DEFAULTKLINEREQENUMTYPE) {
                    resultM = CropymelHttpSocket.getInstance().sendProdCodeMinuteLine(symbol);
                } else if (klineType == DEFAULTKLINEREHOURQENUMTYPE) {//小时价格分时
//                        resultM = CropymelHttpSocket.getInstance().minuteGetHour(symbol);
                }
                CommonUtil.LogLa(2, "QuoteDataTask resultM==" + resultM);
//                pareserProCodeJsons(resultM);
                return resultM;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected void onCompleted() {
            super.onCompleted();
            CommonUtil.LogLa(2, "QuoteDataTask onCompleted");
            if (showProDialog) weipanChartPro.setVisibility(View.GONE);
        }

        @Override
        protected void onResult(String resulte) {
            super.onResult(resulte);
            CommonUtil.LogLa(2, "QuoteDataTask onResult" + resulte);
            if (!TextUtils.isEmpty(resulte)) {
                if (klineType == DEFAULTKLINEREQENUMTYPE) {
                    minuteLineChart.startHisDateTask(resulte, starProData);
                } else if (klineType == DEFAULTKLINEREHOURQENUMTYPE) {
//                    minuteHourLineChart.startHisDateTask(starProData, resulte);
                }
            }
//            updateUI();
        }

        @Override
        protected void onError(Throwable e) {
            super.onError(e);
            CommonUtil.LogLa(2, "QuoteDataTask onError");
        }
    }


    private synchronized void pareserProCodeJsons(String json) throws TaojinluException, JSONException {
        if (json == null) return;
        JSONObject jsonObject = new JSONObject(json);
        if (JsonParserUtils.hasAndNotNull(jsonObject, "isRun")) {
            MainApplication.isRun = jsonObject.getBoolean("isRun");
        }
        if (JsonParserUtils.hasAndNotNull(jsonObject, symbol)) {
            starProData = new StarProDataParser().parse(jsonObject.getJSONObject(symbol));
        }
    }

    private void updateUI() {
        if (starProData == null) return;
//        tvPrice.setText(StockChartUtil.formatNumber(2, starProData.last));
        tvPrice.setText(String.valueOf(starProData.last));
        tvPriceCash.setText(starProData.lastCny);
        tvPrice.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));

        tvPriceRate.setText(StockChartUtil.formatNumWithSign(2, starProData.rate, true) + "%");
        tvPriceRate.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));

        sdpDetailPriceList.updateDateDetail(starProData.buys, false, starProData.sells, true);//卖需要倒序
    }

    private void setChartByType(int klineReqEnumType) {//切换行情图添用这个(加对应的画图view,并且获取数据画图)
        Log.d("kline", "startGetStockKlineTask11111");
        if (this.klineReqEnumType != klineReqEnumType) {
            this.klineReqEnumType = klineReqEnumType;
            weipanChart.removeAllViews();
            minuteLineChart.setVisibility(View.INVISIBLE);
//            minuteHourLineChart.setVisibility(View.INVISIBLE);
            if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {//今日价格
                minuteLineChart.setVisibility(View.VISIBLE);
                weipanChart.addView(minuteLineChart, 0);
                minuteLineChart.postInvalidate();
                goodsKlineChart.clearAllData();
                updateMenuSelect(null, DEFAULTKLINEREQENUMTYPE);
            } else if (klineReqEnumType == DEFAULTKLINEREHOURQENUMTYPE) {//小时价格
//                weipanChart.addView(minuteHourLineChart, 0);
//                minuteHourLineChart.setVisibility(View.VISIBLE);
//                minuteHourLineChart.postInvalidate();
//                goodsKlineChart.clearAllData();
//                updateMenuSelect(null, DEFAULTKLINEREHOURQENUMTYPE);
            } else {//k线
                Log.d("kline", "startGetStockKlineTask22222");
                weipanChart.addView(goodsKlineChart, 0);
                updateMenuSelect(CropymelHttpSocket.KlineReqEnum.getKlineReqEnumByMinute(klineReqEnumType), 0);
            }
        }
        refreshChart();//添加完成之后刷新,这个放到外面就是说比如你当前是5分钟k线,当你再次点击5分钟K线就帮你刷新一次
    }

    private void updateMenuSelect(CropymelHttpSocket.KlineReqEnum klineReqEnum, int mintueType) {
        tvMinute.setSelected(false); //分时图
        tvKlineDay.setSelected(false);// 日图
        tvKlineWeek.setSelected(false);
        tvKlineMonth.setSelected(false);
        if (klineReqEnum == null) {//
            klineReqEnumType = mintueType;
            if (klineReqEnumType == DEFAULTKLINEREHOURQENUMTYPE) {
//                tvMinuteHour.setSelected(true);
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
        }
    }


    private void showOLStarHomeDialogFragment() {
        buyDialogFragment = BuyDialogFragment.newInstance();
//        buyDialogFragment.setOnDialogCallbackListener(new OnDialogCallbackListener() {
//            @Override
//            public void onSwitchCallback(short trade_opp_type, short item_type) {
//                tradeDialog.dismiss();
//                showOlStarHomePickDialogFragment(trade_opp_type, item_type);
//                SysShareData.setSharedDate(OLStarHomeActivity.this, CommonConst.ISOPENSYSPICKUP, "0");
//            }
//
//            @Override
//            public void onDismiss() {
//                tradeDialog.dismiss();
//            }
//        });
        buyDialogFragment.showDialog(getSupportFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuy:
                TradeActivity.pageJump(this, symbol, 1);
//                EntrusTransactiontActivity.pageJump(MarketActivity.this, symbol, 1);
                break;
            case R.id.tvSell:
                TradeActivity.pageJump(this, symbol, -1);
//                EntrusTransactiontActivity.pageJump(MarketActivity.this, symbol, -1);
                break;
            case R.id.tvMinute:
                setChartByType(DEFAULTKLINEREQENUMTYPE);
                break;
            case R.id.tvKlineDay:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
                break;
            case R.id.tvKlineWeek:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
                break;
            case R.id.tvKlineMonth:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
                break;
        }
    }

}
