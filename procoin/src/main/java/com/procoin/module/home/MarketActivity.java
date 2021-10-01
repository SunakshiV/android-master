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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.core.widget.NestedScrollView;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.NormalShareData;
import com.procoin.http.base.Group;
import com.procoin.http.error.TaojinluException;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.entity.CoinDesc;
import com.procoin.module.home.trade.TradeActivity;
import com.procoin.module.home.trade.TradeLeverActivity;
import com.procoin.module.home.trade.adapter.DealListAdapter;
import com.procoin.module.home.trade.dialog.BuyDialogFragment;
import com.procoin.module.home.trade.entity.MarkDeal;
import com.procoin.module.login.LoginActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.util.TjrMinuteTaskPool;
import com.procoin.widgets.quotitian.StarDetailPriceView_N;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 行情
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class MarketActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


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
    @BindView(R.id.tvCurrAmt)
    TextView tvCurrAmt;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvOpenMarketStr)
    TextView tvOpenMarketStr;


    @BindView(R.id.tvPriceRate)
    TextView tvPriceRate;
    @BindView(R.id.sdpDetailPriceList)
    StarDetailPriceView_N sdpDetailPriceList;
    //    @BindView(R.id.llMarkMenu)
//    LinearLayoutMarkMenu llMarkMenu;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private final int DEFAULTKLINEREQENUMTYPE = -8741;//今日价格
    //    private final int DEFAULTKLINEREHOURQENUMTYPE = -8742;//小时价格
    private final int DEFAULTKLINEREDAYFINEENUMTYPE = -8743;//5日
    @BindView(R.id.tvMinute)
    TextView tvMinute;
    @BindView(R.id.tvMinuteDay5)
    TextView tvMinuteDay5;
    @BindView(R.id.tvKlineDay)
    TextView tvKlineDay;
    @BindView(R.id.tvKlineWeek)
    TextView tvKlineWeek;
    //    @BindView(R.id.tvKlineMonth)
//    TextView tvKlineMonth;
    @BindView(R.id.tvKline15Minutes)
    TextView tvKline15Minutes;
    @BindView(R.id.tvKlineOneHour)
    TextView tvKlineOneHour;
    @BindView(R.id.tvKline1Minutes)
    TextView tvKline1Minutes;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvJrkp)
    TextView tvJrkp;
    @BindView(R.id.tvZjcj)
    TextView tvZjcj;
    @BindView(R.id.tvAmt)
    TextView tvAmt;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvZgcj)
    TextView tvZgcj;
    @BindView(R.id.tvZdcj)
    TextView tvZdcj;
    @BindView(R.id.tvVol)
    TextView tvVol;
    @BindView(R.id.llMaData)
    LinearLayout llMaData;
    @BindView(R.id.tvM5)
    TextView tvM5;
    @BindView(R.id.tvM10)
    TextView tvM10;
    @BindView(R.id.tvM30)
    TextView tvM30;
    @BindView(R.id.llKlineData)
    LinearLayout llKlineData;
    @BindView(R.id.llFullScreen)
    LinearLayout llFullScreen;
    @BindView(R.id.llOptional)
    LinearLayout llOptional;
    @BindView(R.id.ivOptional)
    ImageView ivOptional;

    @BindView(R.id.tvCoinDesc)
    TextView tvCoinDesc;
    @BindView(R.id.tvMaxPrice)
    TextView tvMaxPrice;
    @BindView(R.id.tvMinPrice)
    TextView tvMinPrice;
    @BindView(R.id.tv24Vol)
    TextView tv24Vol;

    @BindView(R.id.tvDeep)
    TextView tvDeep;
    @BindView(R.id.tvTrade)
    TextView tvTrade;
    @BindView(R.id.tvDesc)
    TextView tvDesc;

    @BindView(R.id.llCoinDesc)
    LinearLayout llCoinDesc;


    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.tvIssueDate)
    TextView tvIssueDate;
    @BindView(R.id.tvIssueAmount)
    TextView tvIssueAmount;
    @BindView(R.id.tvCirculateAmount)
    TextView tvCirculateAmount;
    @BindView(R.id.tvCrowdfundPrice)
    TextView tvCrowdfundPrice;
    @BindView(R.id.tvWhitePaperUrl)
    TextView tvWhitePaperUrl;
    @BindView(R.id.tvOfficialWebUrl)
    TextView tvOfficialWebUrl;
    @BindView(R.id.tvBlockUrl)
    TextView tvBlockUrl;
    @BindView(R.id.llWhitePaperUrl)
    LinearLayout llWhitePaperUrl;
    @BindView(R.id.llOfficialWebUrl)
    LinearLayout llOfficialWebUrl;
    @BindView(R.id.llBlockUrl)
    LinearLayout llBlockUrl;
    @BindView(R.id.llDealList)
    LinearLayout llDealList;
    @BindView(R.id.rvDealList)
    RecyclerView rvDealList;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.llUpAciton)
    LinearLayout llUpAciton;

    private int klineReqEnumType = DEFAULTKLINEREQENUMTYPE;

    //    private StarMinuteHourLineChart minuteHourLineChart;//小时价格分时图,只是分时图,不包括右边的3档,属于starMinuteVIew里面的
    private MinuteDigitalTimeLineChart minuteLineChart;//今日价格分时图(数字货币)
    private MinuteStockTimeLineChart stockTimeLineChart;//今日价格分时图(股指期货)
    private MinuteTimeLineDay5Chart minuteTimeLineDay5Chart;//分时图5日
    private StarKlineChart goodsKlineChart;//K线
    private StarProData starProData;

    private StarProDataParser starProDataParser;
    private LineTimeEntityParser lineTimeEntityParser;
    private LineTimeEntity lineTimeEntity;//分时图最新数据

    private StarRunTimeManager starRunTimeManager;
    private TjrMinuteTaskPool tjrMinuteTaskPool;
    private Handler handler = new Handler();

    private BuyDialogFragment buyDialogFragment;


    private String symbol = "";//币种
    private int isLever = 1;//1代表是杠杆
    private String marketType = "";
//    public int decimal = 6; // 小数保留位数

    private Call<ResponseBody> getCoinInfoCall;
    private Call<ResponseBody> optionalAddCall;
    private Call<ResponseBody> optionalDelCall;

    private Call<ResponseBody> isOptionalCall;

    private CoinDesc coinDesc;

    private int isOptional = -1;//是否是自选 1是自选 0不是
    private boolean optionalFlag;//正在请求falg

    private DealListAdapter dealListAdapter;


    public static void pageJump(Context context, String symbol) {
        pageJump(context, symbol, 1);
    }

    public static void pageJump(Context context, String symbol, int isLever) {
        pageJump(context, symbol, isLever, false);
    }

    public static void pageJump(Context context, String symbol, int isLever, boolean isClearTop) {
        Bundle bundle = new Bundle();
//        if (!TextUtils.isEmpty(symbol) && !symbol.contains("/")) {
//            symbol = symbol + "/USDT";
//        }
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.ISLEVER, isLever);
//        bundle.putString(CommonConst.ACCOUNTTYPE, accountType);
        if (!isClearTop) {
            PageJumpUtil.pageJump(context, MarketActivity.class, bundle);
        } else {
            Intent intent = new Intent();
            intent.setClass(context, MarketActivity.class);
            if (bundle != null) intent.putExtras(bundle);
            context.startActivity(intent);
            if (context instanceof AppCompatActivity) {
                ((AppCompatActivity) context).overridePendingTransition(R.anim.perval_right_to_left, 0);
            }
        }

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
            if (bundle.containsKey(CommonConst.ISLEVER)) {
                isLever = bundle.getInt(CommonConst.ISLEVER, 1);
            }

//            if (bundle.containsKey(CommonConst.ACCOUNTTYPE)) {
//                accountType = bundle.getString(CommonConst.ACCOUNTTYPE, "");
//            }
        }
        if (TextUtils.isEmpty(symbol)) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }

        if (isLever == 1) {
//            llOptional.setVisibility(View.GONE);
            tvBuy.setText("看涨(做多)");
            tvSell.setText("看跌(做空)");
        } else {
//            llOptional.setVisibility(View.VISIBLE);
            tvBuy.setText("买入");
            tvSell.setText("卖出");
        }
//        mActionBar.setTitle(symbol);
        tvSubTitle.setText(symbol);
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);

        tvMinute.setOnClickListener(this);
        tvMinuteDay5.setOnClickListener(this);
        tvKlineDay.setOnClickListener(this);
        tvKlineWeek.setOnClickListener(this);
//        tvKlineMonth.setOnClickListener(this);
        llFullScreen.setOnClickListener(this);
        llOptional.setOnClickListener(this);
        tvKline15Minutes.setOnClickListener(this);
        tvKlineOneHour.setOnClickListener(this);
        tvKline1Minutes.setOnClickListener(this);
        tvMinute.setSelected(true);


        tvDeep.setOnClickListener(this);
        tvTrade.setOnClickListener(this);
        tvDesc.setOnClickListener(this);

        tvDeep.setSelected(true);
        tvDesc.setSelected(false);

        sdpDetailPriceList.setVisibility(View.VISIBLE);
        llCoinDesc.setVisibility(View.GONE);

//        llWhitePaperUrl.setOnClickListener(this);//水清说不要点击事件
//        llOfficialWebUrl.setOnClickListener(this);
//        llBlockUrl.setOnClickListener(this);


        starRunTimeManager = new StarRunTimeManager();
        tjrMinuteTaskPool = new TjrMinuteTaskPool();
        starProDataParser = new StarProDataParser();
        lineTimeEntityParser = new LineTimeEntityParser();
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


        minuteTimeLineDay5Chart = new MinuteTimeLineDay5Chart(this);

        goodsKlineChart = new StarKlineChart(this, true);
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

//        goodsKlineChart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                TjrSocialMTAUtil.trackCustomKVEvent(getApplicationContext(), "交易页横屏点击次数", "K线图_" + klineReqEnumType, TjrSocialMTAUtil.MTATradeLandscapeClick);
////                Bundle bundle = new Bundle();
////                bundle.putString(CommonConst.PARAMS, OLStarHomeLandActivity.parms2JsonStr(proCode, klineReqEnumType));
////                PageJumpUtil.pageJumpResult(OLStarHomeActivity.this, OLStarHomeLandActivity.class, bundle);
//                MarkLandActivity.pageJumpForResult(MarketActivity.this, symbol, klineReqEnumType);
//            }
//        });
        //        //这个增加监听白线显示或消失
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

        dealListAdapter = new DealListAdapter(this);
        rvDealList.setLayoutManager(new LinearLayoutManager(this));
        rvDealList.setAdapter(dealListAdapter);
//        weipanChart.addView(minuteLineChart);
//        refreshChart();//k线数据就刷一次就好
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
        klineReqEnumType = NormalShareData.getKlineType(MarketActivity.this);
        setChartByType(klineReqEnumType, true);
        startGetCoinInfo();//
        startIsOptionalCall();
    }


    private void startGetCoinInfo() {
        CommonUtil.cancelCall(getCoinInfoCall);
        getCoinInfoCall = VHttpServiceManager.getInstance().getVService().getCoinInfo(symbol);
        getCoinInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    coinDesc = resultData.getObject("coin", CoinDesc.class);
                    if (coinDesc != null) {
//                        mActionBar.setTitle(coinDesc.name + "(" + coinDesc.symbol + ")");
                        tvTitle.setText(coinDesc.name);
                        tvCoinName.setText(TextUtils.isEmpty(coinDesc.name) ? "----" : coinDesc.name);
                        tvIssueDate.setText(TextUtils.isEmpty(coinDesc.issueDate) ? "----" : coinDesc.issueDate);
                        tvIssueAmount.setText(TextUtils.isEmpty(coinDesc.issueAmount) ? "----" : coinDesc.issueAmount);
                        tvCirculateAmount.setText(TextUtils.isEmpty(coinDesc.circulateAmount) ? "----" : coinDesc.circulateAmount);

                        tvCrowdfundPrice.setText(TextUtils.isEmpty(coinDesc.crowdfundPrice) ? "----" : coinDesc.crowdfundPrice);
                        tvWhitePaperUrl.setText(TextUtils.isEmpty(coinDesc.whitePaperUrl) ? "----" : coinDesc.whitePaperUrl);
                        tvOfficialWebUrl.setText(TextUtils.isEmpty(coinDesc.officialWebUrl) ? "----" : coinDesc.officialWebUrl);
                        tvBlockUrl.setText(TextUtils.isEmpty(coinDesc.blockUrl) ? "----" : coinDesc.blockUrl);

                        tvCoinDesc.setText(coinDesc.desc);

                        llUpAciton.setVisibility(coinDesc.isTrade==1?View.VISIBLE:View.GONE);

                    }
                }
            }

        });
    }


    private void showAllData(StockDayData stockDayData) {
        if (goodsKlineChart == null) return;
        if (stockDayData != null) {
            int color = StockChartUtil.getRateTextColor(this, stockDayData.getRate());
            tvRate.setTextColor(color);
            tvAmt.setTextColor(color);
//            tvZjcj.setTextColor(color);
//            tvRate.setTextColor(color);
//            color = StockChartUtil.getRateColor(stockDayData.getJrkp(), stockDayData.getZrsp());
//            tvJrkp.setTextColor(color);
//            color = StockChartUtil.getRateColor(stockDayData.getZgcj(), stockDayData.getZrsp());
//            tvZgcj.setTextColor(color);
//            color = StockChartUtil.getRateColor(stockDayData.getZdcj(), stockDayData.getZrsp());
//            tvZdcj.setTextColor(color);

//            tvZjcj.setText(String.valueOf(stockDayData.getZjcj()));
//            tvJrkp.setText(String.valueOf(stockDayData.getJrkp()));
//            tvRate.setText(StockChartUtil.formatNumWithSign(2, stockDayData.getRate(), true) + "%");
//            tvZgcj.setText(String.valueOf(stockDayData.getZgcj()));
//            tvZdcj.setText(String.valueOf(stockDayData.getZdcj()));
//            tvDate.setText(goodsKlineChart.strIntToFormate(String.valueOf(stockDayData.getDate())));
//            tvM5.setText(String.valueOf(stockDayData.getM5()));
//            tvM10.setText(String.valueOf(stockDayData.getM10()));
//            tvM20.setText(String.valueOf(stockDayData.getM20()));
//            tvVol.setText(String.valueOf(stockDayData.getCjsl()));

            tvZjcj.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getZjcj()));
            tvJrkp.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getJrkp()));
            tvRate.setText(StockChartUtil.formatNumWithSign(2, stockDayData.getRate(), true) + "%");
            tvAmt.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getAmt()));
            tvZgcj.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getZgcj()));
            tvZdcj.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getZdcj()));
            tvDate.setText(goodsKlineChart.strIntToFormate(String.valueOf(stockDayData.getDate())));
            setMaData(stockDayData);
//            tvVol.setText(StockChartUtil.formatMoney(stockDayData.getCjsl(), goodsKlineChart.decimals, goodsKlineChart.decimals));
            tvVol.setText(stockDayData.getCjsl());


        }
    }

    private void setMaData(StockDayData stockDayData) {
        if (stockDayData == null) return;
        tvM5.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getM5()));
        tvM10.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getM10()));
        tvM30.setText(StockChartUtil.formatNumber(goodsKlineChart.priceDecimals, stockDayData.getM30()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeActivity onResume");
        if (tjrMinuteTaskPool != null)
            tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());
//            tjrMinuteTaskPool.startTime(getApplicationContext(), scheduledTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();

    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonUtil.LogLa(2, "OLStarHomeActivity onStop");
    }

    @Override
    protected void onDestroy() {
        NormalShareData.saveKlineType(this, klineReqEnumType);
        CommonUtil.LogLa(2, "OLStarHomeActivity onDestroy");
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 8741) {//横屏回来的参数
            setChartByType(data.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, klineReqEnumType));
            if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) { //今日价格
//                llMarkMenu.setTvCurrText("分时");
                setChartByType(DEFAULTKLINEREQENUMTYPE);
            }
            if (klineReqEnumType == DEFAULTKLINEREDAYFINEENUMTYPE) { //今日价格
//                llMarkMenu.setTvCurrText("分时");
                setChartByType(DEFAULTKLINEREDAYFINEENUMTYPE);
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute()) {
//                llMarkMenu.setTvCurrText("日k");
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute()) {
//                llMarkMenu.setTvCurrText("周k");
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute()) {
//                llMarkMenu.setTvCurrText("月k");
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_15.minute()) {
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_15.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_60.minute()) {
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_60.minute());
            } else if (klineReqEnumType == CropymelHttpSocket.KlineReqEnum.KLINE_1.minute()) {
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_1.minute());
            }
        }
    }

    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try { //当前价格
                long startTime = System.currentTimeMillis();
                String procText = CropymelHttpSocket.getInstance().sendProdCode(symbol, 20);//5
//                String resultM = CropymelHttpSocket.getInstance().sendProdCodeMinuteLine(symbol);
//                Log.d("190", "resultM == " + resultM);
                CommonUtil.LogLa(2, " procText is " + procText);
                pareserProCodeJsons(procText);

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {//因为分时图没有marketType出不来，所以当获得marketType后在设置一遍，不然出不来
                            Log.d("marketType", "stockTimeLineChart==" + stockTimeLineChart + "  minuteLineChart==" + minuteLineChart);
                            if (stockTimeLineChart == null && minuteLineChart == null) {
                                setChartByType(klineReqEnumType, true);
                            }
                        }
                        if (starProData != null) {
                            updateUI();
                        }
                        if (marketTypeIsStock()) {
                            if (stockTimeLineChart != null)
                                stockTimeLineChart.parserJsonStock(lineTimeEntity);
                        } else if (marketTypeIsDigital()) {
                            if (minuteLineChart != null)
                                minuteLineChart.parserJsonStock(lineTimeEntity);
                        }
                        minuteTimeLineDay5Chart.parserJsonStock(lineTimeEntity);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                CommonUtil.LogLa(2, " e " + e.getMessage());
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };

    private void refreshChart() {//只是刷新当前的行情图用这个
        Log.d("kline", "startGetStockKlineTask33333");
        if (!TextUtils.isEmpty(symbol)) {
            if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {//分时图
                if (marketTypeIsStock()) {
                    if (stockTimeLineChart != null)
                        stockTimeLineChart.startGetDataTask(symbol, weipanChartPro);
                } else if (marketTypeIsDigital()) {
                    if (minuteLineChart != null)
                        minuteLineChart.startGetDataTask(symbol, weipanChartPro);
                }
            } else if (klineReqEnumType == DEFAULTKLINEREDAYFINEENUMTYPE) {//5日分时图
                minuteTimeLineDay5Chart.startGetDataTask(symbol, weipanChartPro);
            } else {
                Log.d("kline", "startGetStockKlineTask44444");
                //获取历史线
                goodsKlineChart.reset(CropymelHttpSocket.KlineReqEnum.getKlineReqEnumByMinute(klineReqEnumType));
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

    private synchronized void pareserProCodeJsons(String json) throws TaojinluException, JSONException {
        if (json == null) return;
        JSONObject jsonObject = new JSONObject(json);
        if (JsonParserUtils.hasAndNotNull(jsonObject, "isRun")) {
            MainApplication.isRun = jsonObject.getBoolean("isRun");
        }
        if (JsonParserUtils.hasAndNotNull(jsonObject, symbol)) {
            long t = System.currentTimeMillis();
            starProData = starProDataParser.parse(jsonObject.getJSONObject(symbol));
            if (starProData != null) marketType = starProData.marketType;
            Log.d("marketType", "marketType==" + marketType);
        }
        lineTimeEntity = lineTimeEntityParser.parse(jsonObject, "min_" + symbol);
        if (starProData != null && lineTimeEntity != null)
            lineTimeEntity.price = starProData.last;//有时候2个价格不一样，就导致看起来不同步

//        Log.d("pareserProCodeJsons", "starProData.last==" + starProData.last+"  lineTimeEntity.price=="+lineTimeEntity.price);
    }

    private void updateUI() {
        if (starProData == null) return;


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
//        tvPrice.setText(StockChartUtil.formatNumber(2, starProData.last));
        tvPrice.setText(StockChartUtil.formatNumber(starProData.priceDecimals, starProData.last));
        tvPrice.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));
        tvCurrAmt.setText(StockChartUtil.formatWithSign(starProData.amt));
        tvCurrAmt.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));
        tvUnit.setText(starProData.currency);
        Log.d("updateUI", "starProData.last==" + starProData.last + "  starProData.amt==" + starProData.amt + "  starProData.rate==" + starProData.rate);

        if (!TextUtils.isEmpty(starProData.openMarketStr)) {
            tvOpenMarketStr.setVisibility(View.VISIBLE);
            tvOpenMarketStr.setText(starProData.openMarketStr);
        } else {
            tvOpenMarketStr.setVisibility(View.GONE);
        }


//        tvPrice.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));

//        tvPriceRate.setText(StockChartUtil.getRateTextArrow(this, starProData.rate) + starProData.amt + "(" + StockChartUtil.formatNumWithSign(2, starProData.rate, true) + "%)");
        tvPriceRate.setText(StockChartUtil.formatNumWithSign(2, starProData.rate, true) + "%");
        tvPriceRate.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));


        tvMaxPrice.setText(starProData.high);
        tvMinPrice.setText(starProData.low);
        tv24Vol.setText(starProData.amount);

        sdpDetailPriceList.updateDateDetail(starProData.buys, false, starProData.sells, true);//卖需要倒序

        Group<MarkDeal> dealList = starProData.dealList;
        if (dealList != null) {
            for (MarkDeal markDeal : dealList) {
                Log.d("dealList", markDeal.toString());
            }
        }
        dealListAdapter.setGroup(starProData.dealList);
    }

    private boolean marketTypeIsStock() {
        return "stock".equals(marketType);
    }

    private boolean marketTypeIsDigital() {
        return "digital".equals(marketType);
    }

    private void setChartByType(int klineReqEnumType) {
        setChartByType(klineReqEnumType, false);
    }


    private void setChartByType(int klineReqEnumType, boolean first) {//切换行情图添用这个(加对应的画图view,并且获取数据画图)
        Log.d("kline", "startGetStockKlineTask11111");
//        if (this.klineReqEnumType != klineReqEnumType) {
        this.klineReqEnumType = klineReqEnumType;
        weipanChart.removeAllViews();
        if (minuteLineChart != null) minuteLineChart.setVisibility(View.INVISIBLE);
        if (stockTimeLineChart != null) stockTimeLineChart.setVisibility(View.INVISIBLE);
        minuteTimeLineDay5Chart.setVisibility(View.INVISIBLE);
//            minuteHourLineChart.setVisibility(View.INVISIBLE);
        if (klineReqEnumType == DEFAULTKLINEREQENUMTYPE) {//今日价格
            if (marketTypeIsStock()) {
                llMaData.setVisibility(View.GONE);
                if (stockTimeLineChart == null) {
                    stockTimeLineChart = new MinuteStockTimeLineChart(this);
                }
                stockTimeLineChart.setVisibility(View.VISIBLE);
                weipanChart.addView(stockTimeLineChart, 0);
//                stockTimeLineChart.postInvalidate();
                goodsKlineChart.clearAllData();
                updateMenuSelect(null, DEFAULTKLINEREQENUMTYPE);
            } else if (marketTypeIsDigital()) {
                llMaData.setVisibility(View.GONE);
                if (minuteLineChart == null) {
                    minuteLineChart = new MinuteDigitalTimeLineChart(this);
                }
                minuteLineChart.setVisibility(View.VISIBLE);
                weipanChart.addView(minuteLineChart, 0);
//                minuteLineChart.postInvalidate();
                goodsKlineChart.clearAllData();
                updateMenuSelect(null, DEFAULTKLINEREQENUMTYPE);
            } else {
            }

        } else if (klineReqEnumType == DEFAULTKLINEREDAYFINEENUMTYPE) {//5日分时图
            llMaData.setVisibility(View.GONE);
            minuteTimeLineDay5Chart.setVisibility(View.VISIBLE);
            weipanChart.addView(minuteTimeLineDay5Chart, 0);
//            minuteTimeLineDay5Chart.postInvalidate();
            goodsKlineChart.clearAllData();
            updateMenuSelect(null, DEFAULTKLINEREDAYFINEENUMTYPE);
        } else {//k线
            llMaData.setVisibility(View.VISIBLE);
            Log.d("kline", "startGetStockKlineTask22222");
            weipanChart.addView(goodsKlineChart, 0);
            updateMenuSelect(CropymelHttpSocket.KlineReqEnum.getKlineReqEnumByMinute(klineReqEnumType), 0);
        }
        if (first) {//第一次让他延迟500毫秒刷新，否则可能会出现y坐标画图为0，然后闪一下就好了，造成体验不好
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refreshChart();//添加完成之后刷新,这个放到外面就是说比如你当前是5分钟k线,当你再次点击5分钟K线就帮你刷新一次
                }
            }, 500);
        } else {
            refreshChart();//添加完成之后刷新,这个放到外面就是说比如你当前是5分钟k线,当你再次点击5分钟K线就帮你刷新一次
        }


    }

    private void updateMenuSelect(CropymelHttpSocket.KlineReqEnum klineReqEnum, int mintueType) {
        tvMinute.setSelected(false); //分时图
        tvMinuteDay5.setSelected(false); //分时图
        tvKlineDay.setSelected(false);// 日图
        tvKlineWeek.setSelected(false);
//        tvKlineMonth.setSelected(false);
        tvKline15Minutes.setSelected(false);
        tvKlineOneHour.setSelected(false);
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
            case KLINE_15:
                tvKline15Minutes.setSelected(true);
                break;
            case KLINE_60:
                tvKlineOneHour.setSelected(true);
                break;
            case KLINE_1:
                tvKline1Minutes.setSelected(true);
                break;

//            case KLINE_MONTH:
//                tvKlineMonth.setSelected(true);
//                break;
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
                if (isLogin()) {
                    if (isLever == 0) {
                        TradeActivity.pageJump(this, symbol, 1);
                    } else if (isLever == 1) {
                        TradeLeverActivity.pageJump(this, symbol, 1);
                    }
//                EntrusTransactiontActivity.pageJump(MarketActivity.this, symbol, 1);
                } else {
                    LoginActivity.login(MarketActivity.this);
                }
                break;
            case R.id.tvSell:
                if (isLogin()) {
                    if (isLever == 0) {
                        TradeActivity.pageJump(this, symbol, -1);
                    } else if (isLever == 1) {
                        TradeLeverActivity.pageJump(this, symbol, -1);
                    }

//                EntrusTransactiontActivity.pageJump(MarketActivity.this, symbol, -1);
                } else {
                    LoginActivity.login(MarketActivity.this);
                }
                break;
            case R.id.tvMinute:
                setChartByType(DEFAULTKLINEREQENUMTYPE);
                break;
            case R.id.tvMinuteDay5:
                setChartByType(DEFAULTKLINEREDAYFINEENUMTYPE);
                break;
            case R.id.tvKlineDay:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_DAY.minute());
                break;
            case R.id.tvKlineWeek:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_WEEK.minute());
                break;
//            case R.id.tvKlineMonth:
//                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_MONTH.minute());
//                break;

            case R.id.tvKline15Minutes:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_15.minute());
                break;
            case R.id.tvKlineOneHour:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_60.minute());
                break;
            case R.id.tvKline1Minutes:
                setChartByType(CropymelHttpSocket.KlineReqEnum.KLINE_1.minute());
                break;
            case R.id.llFullScreen:
                MarkLandActivity.pageJumpForResult(MarketActivity.this, symbol, klineReqEnumType);
                break;
            case R.id.llOptional:
                if (isLogin()) {
                    if (isOptional == -1) return;
                    if (isOptional == 0) {
                        startOptionalAddCall();
                    } else {
                        startOptionalDelCall();
                    }
                } else {
                    LoginActivity.login(MarketActivity.this);
                }

                break;

            case R.id.tvDeep:
                sdpDetailPriceList.setVisibility(View.VISIBLE);
                llDealList.setVisibility(View.GONE);
                llCoinDesc.setVisibility(View.GONE);

                tvDeep.setSelected(true);
                tvTrade.setSelected(false);
                tvDesc.setSelected(false);
                break;
            case R.id.tvTrade:
                sdpDetailPriceList.setVisibility(View.GONE);
                llDealList.setVisibility(View.VISIBLE);
                llCoinDesc.setVisibility(View.GONE);

                tvDeep.setSelected(false);
                tvTrade.setSelected(true);
                tvDesc.setSelected(false);
                break;
            case R.id.tvDesc:
                sdpDetailPriceList.setVisibility(View.GONE);
                llDealList.setVisibility(View.GONE);
                llCoinDesc.setVisibility(View.VISIBLE);

                tvDeep.setSelected(false);
                tvTrade.setSelected(false);
                tvDesc.setSelected(true);
                break;
//            case R.id.llWhitePaperUrl:
//                if (coinDesc != null && !TextUtils.isEmpty(coinDesc.whitePaperUrl)) {
//                    CommonWebViewActivity.pageJumpCommonWebViewActivity(MarketActivity.this, coinDesc.whitePaperUrl);
//                }
//                break;
//            case R.id.llOfficialWebUrl:
//                if (coinDesc != null && !TextUtils.isEmpty(coinDesc.officialWebUrl)) {
//                    CommonWebViewActivity.pageJumpCommonWebViewActivity(MarketActivity.this, coinDesc.officialWebUrl);
//                }
//                break;
//            case R.id.llBlockUrl:
//                if (coinDesc != null && !TextUtils.isEmpty(coinDesc.blockUrl)) {
//                    CommonWebViewActivity.pageJumpCommonWebViewActivity(MarketActivity.this, coinDesc.blockUrl);
//                }
//                break;


        }
    }

    private void startOptionalAddCall() {
        if (optionalFlag) {
            return;
        }
        optionalFlag = true;
        CommonUtil.cancelCall(optionalAddCall);
        optionalAddCall = VHttpServiceManager.getInstance().getVService().optionalAdd(symbol);
        optionalAddCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                optionalFlag = false;
                if (resultData.isSuccess()) {
                    getApplicationContext().optionalFlag = true;
                    CommonUtil.showmessage(resultData.msg, MarketActivity.this);
                    isOptional = 1;
                    setIsOptional();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                optionalFlag = false;

            }
        });
    }

    private void startOptionalDelCall() {
        if (optionalFlag) {
            return;
        }
        optionalFlag = true;
        CommonUtil.cancelCall(optionalDelCall);
        optionalDelCall = VHttpServiceManager.getInstance().getVService().optionalDel(symbol);
        optionalDelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                optionalFlag = false;
                if (resultData.isSuccess()) {
                    getApplicationContext().optionalFlag = true;
                    CommonUtil.showmessage(resultData.msg, MarketActivity.this);
                    isOptional = 0;
                    setIsOptional();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                optionalFlag = false;

            }

        });
    }

    private void startIsOptionalCall() {
        CommonUtil.cancelCall(isOptionalCall);
        isOptionalCall = VHttpServiceManager.getInstance().getVService().isOptional(symbol);
        isOptionalCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    isOptional = resultData.getItem("isOptional", Integer.class);
                    setIsOptional();
                }
            }

        });
    }

    private void setIsOptional() {
        if (isOptional == 0) {
            ivOptional.setImageResource(R.drawable.ic_optional_unselected);
        } else {
            ivOptional.setImageResource(R.drawable.ic_optional_selected);
        }

    }

}
