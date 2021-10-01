package com.procoin.module.home.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.module.home.OrderEntrustnfoActivity;
import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.error.TaojinluException;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.entity.Order;
import com.procoin.util.CommonUtil;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.TjrMinuteTaskPool;
import com.procoin.widgets.quotitian.StarRunTimeManager;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.widgets.quotitian.entity.jsonparser.StarProDataParser;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 委托交易
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class EntrusTransactiontSimpleActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    //    @BindView(R.id.tvTitle)
//    TextView tvTitle;
//    @BindView(R.id.tvPrice)
//    TextView tvPrice;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.etMoneyOrAmount)
    EditText etMoneyOrAmount;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.tvMoneyOrAmountText)
    TextView tvMoneyOrAmountText;
    @BindView(R.id.tvBalanceText)
    TextView tvBalanceText;
    //    @BindView(R.id.tvOrderOutline)
//    TextView tvOrderOutline;
    @BindView(R.id.tvBuyAmount)
    TextView tvBuyAmount;
    @BindView(R.id.tvSellMoney)
    TextView tvSellMoney;
    @BindView(R.id.tvSellMoneyUsdt)
    TextView tvSellMoneyUsdt;
    @BindView(R.id.tvSwitch)
    TextView tvSwitch;
    @BindView(R.id.ivQuestionMark)
    ImageView ivQuestionMark;
    @BindView(R.id.tvMarkHint)
    TextView tvMarkHint;

    @BindView(R.id.tvFollowAmount)
    TextView tvFollowAmount;
    @BindView(R.id.tvFollowMoney)
    TextView tvFollowMoney;


    private StarRunTimeManager starRunTimeManager;
    private TjrMinuteTaskPool tjrMinuteTaskPool;
    private Handler handler = new Handler();
    private StarProData starProData;

    private String symbol = "";//币种

    private int buySell;//1：买，-1：卖
    private int decimalCount = 2;//小数点数量,金额小数点2位，数量小数点是4位.

    private String holdCash = "0.00";//持有现金余额
    private String holdUsdt = "0.00";//持有USDT
    private String holdCoin = "0.00";//持有当前币种的数量

    private Bundle bundle;


    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> getTradeOrderCall;

    public static void pageJump(Context context, String symbol, int buySell) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.BUYSELL, buySell);
        PageJumpUtil.pageJump(context, EntrusTransactiontActivity.class, bundle);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.entrust_simple;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recharge_usdt);
        ButterKnife.bind(this);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }
            if (bundle.containsKey(CommonConst.BUYSELL)) {
                buySell = bundle.getInt(CommonConst.BUYSELL, 1);
            }
        }


        starRunTimeManager = new StarRunTimeManager();
        tjrMinuteTaskPool = new TjrMinuteTaskPool();
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());

        etMoneyOrAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > decimalCount) {//最多2位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                handler.removeCallbacks(runnable);
                if (isNotNullOrZero()) {
                    handler.postDelayed(runnable, 500);
                } else {
                    resetTolAmount();
                }
            }
        });

        if (buySell == 1) {
            mActionBar.setTitle("委托买入" + symbol);
            tvMoneyOrAmountText.setText("买入金额(¥)");
            tvBalanceText.setText("可用USDT:≈¥0.00");
//            etMoney.setTextColor(ContextCompat.getColor(this, R.color.ce2214e));
            etMoneyOrAmount.setHint("输入买入金额");
            decimalCount = 2;
            tvBuyAmount.setVisibility(View.VISIBLE);
            tvSellMoney.setVisibility(View.GONE);
            tvSellMoneyUsdt.setVisibility(View.GONE);
            tvSubmit.setBackgroundResource(R.drawable.selector_rect_solid_corner0_e2214e);

            tvFollowAmount.setText("跟单者跟随预计买入" + symbol + "数量：0" );
            tvFollowMoney.setText("跟单者跟随买入总金额：¥ 0" );

            tvBuyAmount.setText("我预计买入数量:0.00");
        } else {
            mActionBar.setTitle("委托卖出" + symbol);
            tvMoneyOrAmountText.setText("卖出数量");
            tvBalanceText.setText("目前持有" + symbol + "数量:" + 0);
//            etMoney.setTextColor(ContextCompat.getColor(this, R.color.c00ad88));
            etMoneyOrAmount.setHint("输入卖出数量");
            decimalCount = 4;

            tvBuyAmount.setVisibility(View.GONE);
            tvSellMoney.setVisibility(View.VISIBLE);
            tvSellMoneyUsdt.setVisibility(View.VISIBLE);
            tvSubmit.setBackgroundResource(R.drawable.selector_rect_solid_corner0_00ad88);

            tvFollowAmount.setText("跟单者跟随卖出" + symbol + "数量：0" );
            tvFollowMoney.setText("跟单者跟随预计总金额：¥ 0");

            tvSellMoney.setText("我预计金额:¥ 0" );
            tvSellMoneyUsdt.setText("≈USDT：0" );
        }

        tvSwitch.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        ivQuestionMark.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        tvMarkHint.setVisibility(View.VISIBLE);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        tvMarkHint.setVisibility(View.GONE);
                }
                return true;
            }
        });



    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String moneyOrAmount = etMoneyOrAmount.getText().toString();
            startGetTradeCheckOut(moneyOrAmount);
        }
    };


    private boolean isNotNullOrZero() {//当输入框都有值得时候返回true
        String moneyOrAmount = etMoneyOrAmount.getText().toString();
        return !TextUtils.isEmpty(moneyOrAmount) && Double.parseDouble(moneyOrAmount) > 0;

    }

    private void startGetTradeCheckOut(String entrustAmount) {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeCheckOut(symbol, "",entrustAmount, "", buySell);
        getTradeConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String copyAmount = resultData.getItem("copyAmount", String.class);// 跟单委托卖出或预计总数量
                    String copyCash = resultData.getItem("copyCash", String.class);// 跟单委托买入或预计总金额

                    String predictMyBalance = resultData.getItem("predictMyBalance", String.class);// 预计数量或金额
                    String predictMyUsdt = resultData.getItem("predictMyUsdt", String.class);// 预计USDT数量

                    if (buySell == 1) {
                        tvFollowAmount.setText("跟单者跟随预计买入" + symbol + "数量：" + copyAmount);
                        tvFollowMoney.setText("跟单者跟随买入总金额：¥" + copyCash);
                        tvBuyAmount.setText("我预计买入数量:" + predictMyBalance);
                    } else {
                        tvFollowAmount.setText("跟单者跟随卖出" + symbol + "数量：" + copyAmount);
                        tvFollowMoney.setText("跟单者跟随预计总金额：¥" + copyCash);
                        tvSellMoney.setText("我预计金额:¥" + predictMyBalance);
                        tvSellMoneyUsdt.setText("≈USDT：" + predictMyUsdt);
                    }


                }
            }
        });
    }

    private void resetTolAmount() {//重置
        if(buySell==1){
            tvFollowAmount.setText("跟单者跟随预计买入" + symbol + "数量：0" );
            tvFollowMoney.setText("跟单者跟随买入总金额：¥ 0" );

            tvBuyAmount.setText("我预计买入数量:0.00");
        }else{
            tvFollowAmount.setText("跟单者跟随卖出" + symbol + "数量：0" );
            tvFollowMoney.setText("跟单者跟随预计总金额：¥ 0");

            tvSellMoney.setText("我预计金额:¥ 0" );
            tvSellMoneyUsdt.setText("≈USDT：0" );
        }

    }

    private void setAmount() {
        String money = etMoneyOrAmount.getText().toString().trim();
        if (TextUtils.isEmpty(money)) {
            if (buySell == 1) {
                tvBuyAmount.setText("预计数量:0");
            } else {
//                tvSellMoney.setText();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetTradeConfig();
        CommonUtil.LogLa(2, "OLStarHomeActivity onResume");
        starRunTimeManager.onResume();
        if (tjrMinuteTaskPool != null)
            tjrMinuteTaskPool.startTime(getApplicationContext(), scheduledTask);
    }

    @Override
    protected void onPause() {
        super.onPause();
        starRunTimeManager.onPause();
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

    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try { //当前价格
                String procText = CropymelHttpSocket.getInstance().sendProdCode(symbol, 5);//5
                Log.d("190", "procText == " + procText);
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

    private void updateUI() {
        if (starProData == null) return;
//        tvPrice.setText("当前价:¥" + StockChartUtil.formatNumber(2, starProData.last));
//        tvPrice.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));
//        tvPriceRate.setText(StockChartUtil.formatNumWithSign(2, starProData.rate, true) + "%");
//        tvPriceRate.setTextColor(StockChartUtil.getRateTextColorWithBlackBg(this, starProData.rate));
    }


    private synchronized void pareserProCodeJsons(String json) throws TaojinluException, JSONException {
        if (json == null) return;
        JSONObject jsonObject = new JSONObject(json);
        if (JsonParserUtils.hasAndNotNull(jsonObject, "isRun")) {
            MainApplication.isRun = jsonObject.getBoolean("isRun");
        }
        if (JsonParserUtils.hasAndNotNull(jsonObject, "quote")) {
            starProData = new StarProDataParser().parse(jsonObject.getJSONObject("quote"));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
//                SelectPayWayActivity.pageJump(EntrusTransactiontSimpleActivity.this, etMoneyOrAmount.getText().toString());
                String moneyOrAmount = etMoneyOrAmount.getText().toString();
                String tips = "";
                if (buySell == 1) {
                    if (TextUtils.isEmpty(moneyOrAmount)) {
                        CommonUtil.showmessage("请输入买入金额", EntrusTransactiontSimpleActivity.this);
                        return;
                    }
//                    if (Double.parseDouble(moneyOrAmount) > Double.parseDouble(holdCash)) {
//                        CommonUtil.showmessage("余额不足", EntrusTransactiontSimpleActivity.this);
//                        return;
//                    }
                    tips = "确定买入？";
                } else {
                    if (TextUtils.isEmpty(moneyOrAmount)) {
                        CommonUtil.showmessage("请输入卖出数量", EntrusTransactiontSimpleActivity.this);
                        return;
                    }

                    if (Double.parseDouble(moneyOrAmount) > Double.parseDouble(holdCoin)) {
                        CommonUtil.showmessage("持币数量不足", EntrusTransactiontSimpleActivity.this);
                        return;
                    }
                    tips = "确定卖出？";
                }
                showSubmitTipsDialog(tips, moneyOrAmount);
                break;
            case R.id.tvSwitch:
                if (bundle == null) bundle = getIntent().getExtras();
                setSwipeBackEnable(false);
                setOverrideExitAniamtion(false);
//                PageJumpUtil.pageJump(this, EntrusTransactiontSimpleActivity.class, bundle);
                Intent intent = new Intent();
                intent.setClass(this, EntrusTransactiontActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                PageJumpUtil.finishCurr(this);
                overridePendingTransition(R.anim.enter_left_2_right, R.anim.out_2_right);
                break;
        }
    }

    private void startGetTradeOrder(final String entrustAmount) {
        showProgressDialog();
        com.procoin.http.util.CommonUtil.cancelCall(getTradeOrderCall);
//        getTradeOrderCall = VHttpServiceManager.getInstance().getVService().tradeOrder(symbol, "",entrustAmount,  "",buySell);
        getTradeOrderCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, EntrusTransactiontSimpleActivity.this);
                    Order order = resultData.getObject("order", Order.class);
                    if (order != null) {
                        getApplicationContext().order = order;
                        PageJumpUtil.pageJump(EntrusTransactiontSimpleActivity.this, OrderEntrustnfoActivity.class);
                    }
                }
            }

            @Override
            protected void handleSuccessFalse(ResultData resultData) {
                super.handleSuccessFalse(resultData);
                if (resultData.code == 40080) {//买币时余额不足，跳转现金支付
                    String amount = resultData.getItem("entrustAmount", String.class);//当返回40080的时候entrustAmount要用后台返回的，因为小数点的问题
                    SelectPayWayActivity.pageJump(EntrusTransactiontSimpleActivity.this, amount, symbol, "", 0, buySell);
                }


            }
            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void startGetTradeConfig() {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig(symbol,"");
        getTradeConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {

                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    holdCash = resultData.getItem("holdCash", String.class);
                    holdCoin = resultData.getItem("holdCoin", String.class);

//                    usdtRate = resultData.getItem("usdtRate", Double.class);
//                    randomNum = resultData.getItem("randomNum", Integer.class);
//                    holdUsdt = resultData.getItem("holdUsdt", String.class);
//                    tvRandomNum.setText(String.valueOf(randomNum));
//                    tvPrice.setText("￥" + StockChartUtil.formatNumber(2, usdtRate));
//                    tvUsdtBalance.setText("目前持有USDT:" + holdUsdt + "(" + "≈￥" + holdCash + ")");
                    if (buySell == 1) {
                        holdCash = resultData.getItem("holdCash", String.class);
                        tvBalanceText.setText("可用USDT:" + holdUsdt + "  ≈¥" + holdCash);
                    } else {
                        holdCoin = resultData.getItem("holdCoin", String.class);
                        tvBalanceText.setText("持有" + symbol + ":" + holdCoin);
                    }
                }
            }
        });
    }


    TjrBaseDialog submitTipsDialog;

    private void showSubmitTipsDialog(String tips, final String moneyOrAmount) {
        submitTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startGetTradeOrder(moneyOrAmount);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        submitTipsDialog.setTvTitle("订单概要");
        submitTipsDialog.setMessage(tips);
        submitTipsDialog.setBtnOkText("确定");
        submitTipsDialog.show();
    }

}
