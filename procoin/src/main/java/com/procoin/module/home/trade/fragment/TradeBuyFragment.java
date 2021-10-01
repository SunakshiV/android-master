package com.procoin.module.home.trade.fragment;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.home.trade.entity.CheckOut;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.seekbar.BubbleSeekBar;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.trade.TradeActivity;
import com.procoin.util.StockChartUtil;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 买入
 * Created by zhengmj on 19-3-8.
 */

public class TradeBuyFragment extends UserBaseFragment implements View.OnClickListener {

    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.tvPriceCash)
    TextView tvPriceCash;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvUnitSymbol)
    TextView tvUnitSymbol;
    @BindView(R.id.tvOriginSymbol)
    TextView tvOriginSymbol;
    @BindView(R.id.etEnableAmount)
    TextView etEnableAmount;
    @BindView(R.id.tvTolBalance)
    TextView tvTolBalance;
    @BindView(R.id.tvTolBalanceCash)
    TextView tvTolBalanceCash;
    @BindView(R.id.sdbSeekbar)
    BubbleSeekBar sdbSeekbar;
    @BindView(R.id.tvMaxAmount)
    TextView tvMaxAmount;
    @BindView(R.id.tvMinAmount)
    TextView tvMinAmount;
    @BindView(R.id.tvBuy)
    TextView tvBuy;

    private Handler handler = new Handler();
    private CheckOut checkOut;
    private Call<ResponseBody> getTradeCheckOutCall;
    private String symbol;
    private boolean setDefaultFlag = false;

    private  int priceDecimals = 6;//价格的小数点数量
    private  int amountDecimals = 4;//数量的小数点数量

    private TjrBaseDialog submitTipsDialog;
    private Call<ResponseBody> getTradeOrderCall;

    public static TradeBuyFragment newInstance(String symbol) {
        TradeBuyFragment fragment = new TradeBuyFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setDecimals(int priceDecimals,int amountDecimals) {
        this.priceDecimals = priceDecimals;
        this.amountDecimals=amountDecimals;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;
        symbol = b.getString(CommonConst.SYMBOL, "");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TradeActivity", "TradeBuyFragment setUserVisibleHint==" + isVisibleToUser + " getActivity()==" + getActivity());
        if (getActivity() == null) return;
        if (isVisibleToUser) {
//            ((TradeActivity) getActivity()).hideCopy();
            startGetTradeCheckOut(etPrice.getText().toString(), etAmount.getText().toString());
        }
    }

    ValueAnimator textSizeAnimator;

    public void onClickPrice(String price) {
        if (etPrice != null) {
            etPrice.setText(price);
            etPrice.setSelection(etPrice.getText().length());
            if (textSizeAnimator == null) {
                textSizeAnimator = ValueAnimator.ofFloat(14f, 15f, 16f, 17f, 18f, 17f, 16f, 15f, 14f);
                textSizeAnimator.setDuration(400);
                textSizeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                textSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    public void onAnimationUpdate(ValueAnimator animation) {
                        etPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, (Float) animation.getAnimatedValue());
                    }
                });
            }
            textSizeAnimator.start();
        }
    }

    private void startGetTradeCheckOut(String price, String amount) {
        CommonUtil.cancelCall(getTradeCheckOutCall);
        getTradeCheckOutCall = VHttpServiceManager.getInstance().getVService().tradeCheckOut(symbol, price, amount, checkOut == null ? "" : checkOut.myCoin, 1);
        getTradeCheckOutCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    checkOut = resultData.getObject(CheckOut.class);
                    setData();
                }
            }
        });
    }


    /**
     * 设置默认价格，只设置一次就好
     */
    public void setDefaultLast(double last, String lastCny) {
        if (getActivity() == null) return;
        if (setDefaultFlag) return;
        if (etPrice != null && TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            etPrice.setText(StockChartUtil.formatNumber(last));
            tvPriceCash.setText(lastCny);
            setDefaultFlag = true;
        }
    }

    private void setTolBalance(String balance){
        if(balance.length()>=13){
            tvTolBalance.setText(balance + "\n"+ com.procoin.util.CommonUtil.getUnitSymbol(symbol));
        }else{
            tvTolBalance.setText(balance + com.procoin.util.CommonUtil.getUnitSymbol(symbol));
        }
    }

    private void setData() {
        if (checkOut != null) {
            tvPriceCash.setText(checkOut.priceCny);
            etEnableAmount.setText("可用 " + checkOut.myCoin + com.procoin.util.CommonUtil.getUnitSymbol(symbol));
            tvMinAmount.setText(checkOut.minAmount);
            tvMaxAmount.setText(checkOut.maxAmount + com.procoin.util.CommonUtil.getOriginSymbol(symbol));
            setTolBalance(checkOut.balance);
            tvTolBalanceCash.setText(checkOut.balanceCny);
//            if (Double.parseDouble(checkOut.balance) != 0) {
//                int progress = (int) (Double.parseDouble(checkOut.amount) / Double.parseDouble(checkOut.maxAmount) * 100);
//                if (progress > 100) progress = 100;
            if (Double.parseDouble(checkOut.maxAmount) != 0) {
                BigDecimal progressBd = new BigDecimal(checkOut.amount).divide(new BigDecimal(checkOut.maxAmount), 8, BigDecimal.ROUND_FLOOR).multiply(BigDecimal.valueOf(100));
                sdbSeekbar.setProgress(Math.min(progressBd.floatValue(), 100));
            } else {
                sdbSeekbar.setProgress(0);
            }
//            }

            if (checkOut.openCopy == 1) {
                ((TradeActivity) getActivity()).showCopy(checkOut.copy);
            } else {
                ((TradeActivity) getActivity()).hideCopy();
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trade_buy, container, false);
        ButterKnife.bind(this, view);
        tvOriginSymbol.setText(com.procoin.util.CommonUtil.getOriginSymbol(symbol));
        tvUnitSymbol.setText(com.procoin.util.CommonUtil.getUnitSymbol(symbol));
        sdbSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
            @Override
            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.d("sdbSeekbar", "onProgressChanged    progress==" + progress + "  progressFloat==" + progressFloat + "  fromUser==" + fromUser);
                if (!fromUser) return;
                if (checkOut == null) return;
                BigDecimal amount = BigDecimal.valueOf(progressFloat).divide(BigDecimal.valueOf(100)).multiply(new BigDecimal(checkOut.maxAmount)).setScale(amountDecimals, BigDecimal.ROUND_FLOOR);//算出数量
                String a=amount.toPlainString();
                etAmount.setText(a);
                etAmount.setSelection(a.length());

                String price=etPrice.getText().toString().trim();
                if(!TextUtils.isEmpty(price)&&!TextUtils.isEmpty(a)&&checkOut!=null){
                    BigDecimal tolBalanceBD=new BigDecimal(price).multiply(new BigDecimal(a)).setScale(8,BigDecimal.ROUND_FLOOR);
                    BigDecimal balanceCashBD=tolBalanceBD.multiply(new BigDecimal(checkOut.usdtRate)).setScale(2,BigDecimal.ROUND_FLOOR);
                    setTolBalance(tolBalanceBD.toPlainString());
                    tvTolBalanceCash.setText("≈¥"+balanceCashBD.toPlainString());
                }


            }


            //如果设置bsb_auto_adjust_section_mark=false,那么这个方法是最终的结果
            @Override
            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
                Log.d("sdbSeekbar", "getProgressOnActionUp    progress==" + progress + "  progressFloat==" + progressFloat);

            }

            //如果设置bsb_auto_adjust_section_mark=true,那么这个方法是最终的结果
            @Override
            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
                Log.d("sdbSeekbar", "getProgressOnFinally    progress==" + progress + "  progressFloat==" + progressFloat + " fromUser==" + fromUser);


//                BigDecimal bigDecimalBalance = BigDecimal.valueOf(progressFloat).divide(BigDecimal.valueOf(100)).multiply(new BigDecimal(checkOut.myCoin)).setScale(BALANCE_DECIMALCOUNT);//算出交易额
//                Log.d("TradeBuyFragment", "balance==" + bigDecimalBalance.toPlainString());
////                tvTolBalance.setText(bigDecimalBalance.toPlainString() + "USDT");//这个不显示，显示有误差，直接用后台的
//                double price = 0.0;
//                String etPriceText = etPrice.getText().toString().trim();
//                if (!TextUtils.isEmpty(etPriceText)) {
//                    price = Double.parseDouble(etPriceText);
//                }
//                if (price > 0) {
////                    double amount = balance / price;//算出数量
//                    BigDecimal bigDecimalAmount = bigDecimalBalance.divide(BigDecimal.valueOf(price),AMOUNT_DECIMALCOUNT,BigDecimal.ROUND_FLOOR);
//                    Log.d("TradeBuyFragment", "amount==" + bigDecimalAmount.toPlainString());
//                    etAmount.setText(bigDecimalAmount.toPlainString());
//                }


            }
        });
        etPrice.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > priceDecimals) {//最多4位小数
                        s.delete(posDot + (priceDecimals + 1), posDot + (priceDecimals + 2));
                    }
                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }
        });

        etAmount.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > amountDecimals) {
                        s.delete(posDot + (amountDecimals + 1), posDot + (amountDecimals + 2));
                    }
                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }
        });
        tvBuy.setOnClickListener(this);
        return view;
    }


    /**
     * 当撤单的时候要清空checkOut，然后重新调用startGetTradeCheckOut
     */
    public void onCancelOrder() {
        checkOut = null;
        if (handler != null) {
            handler.post(runnable);
        }
    }


    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            String price = etPrice.getText().toString();
            String moneyOrAmount = etAmount.getText().toString();
            startGetTradeCheckOut(price, moneyOrAmount);
        }
    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startGetTradeCheckOut("", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuy:
                String amount = etAmount.getText().toString();
                String price = etPrice.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    com.procoin.util.CommonUtil.showmessage("请输入买入价格", getActivity());
                    return;
                }
                if (TextUtils.isEmpty(amount)) {
                    com.procoin.util.CommonUtil.showmessage("请输入买入数量", getActivity());
                    return;
                }
//                    if (Double.parseDouble(moneyOrAmount) > Double.parseDouble(holdCash)) {
//                        CommonUtil.showmessage("余额不足", EntrusTransactiontActivity.this);
//                        return;
//                    }
//                String tips = "您将以" + price + com.procoin.util.CommonUtil.getUnitSymbol(symbol)+"价格委托买入" + amount + com.procoin.util.CommonUtil.getOriginSymbol(symbol);
                String tips = "是否确定下单？";
                showSubmitTipsDialog(tips, price, amount, checkOut.balance);
                break;
        }
    }

    private void showSubmitTipsDialog(String tips, final String moneyOrAmount, final String price, final String balance) {
        submitTipsDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                dismiss();
                startGetTradeOrder(moneyOrAmount, price, balance, "");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        submitTipsDialog.setTvTitle("买入概要");
        submitTipsDialog.setMessage(tips);
        submitTipsDialog.setBtnOkText("确定");
        submitTipsDialog.show();
    }


    private void startGetTradeOrder(final String price, final String amount, final String balance, String payPass) {
        CommonUtil.cancelCall(getTradeOrderCall);
        ((TJRBaseToolBarActivity) getActivity()).showProgressDialog();
        getTradeOrderCall = VHttpServiceManager.getInstance().getVService().tradeOrder(symbol, price, amount, balance, 1, payPass);
        getTradeOrderCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    etPrice.setText("");
                    etAmount.setText("");
                    sdbSeekbar.setProgress(0.0f);
                    checkOut = null;//下单成功要清掉checkOut，不然可用数量不会减少
                    ((TradeActivity) getActivity()).startGetTradeOrderList();
                    startGetTradeCheckOut("", "");
                    com.procoin.util.CommonUtil.showmessage(resultData.msg, getActivity());
//                    Order order = resultData.getObject("order", Order.class);
//                    if (order != null) {
//                        ((TJRBaseToolBarActivity) getActivity()).getApplicationContext().order = order;
//                        PageJumpUtil.pageJump(getActivity(), OrderEntrustnfoActivity.class);
//                    }

                }
            }

            @Override
            protected void handleSuccessFalse(ResultData resultData) {
                super.handleSuccessFalse(resultData);
//                if (resultData.code == 40080) {//买币时余额不足，跳转现金支付
//                    int recharge = resultData.getItem("recharge", Integer.class);//当返回40080的时候entrustAmount要用后台返回的，因为小数点的问题
//                    showGoRechargeDialog(resultData.msg, recharge);
//                }
            }


            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startGetTradeOrder(price, amount, balance, pwString);
            }
        });
    }

//
//    TjrBaseDialog goRechargeDialog;
//
//    private void showGoRechargeDialog(final String msg, final int recharge) {
//        goRechargeDialog = new TjrBaseDialog(getActivity()) {
//            @Override
//            public void onclickOk() {
//                dismiss();
//                RechargeUsdtActivity.pageJump(getActivity(), recharge);
//            }
//
//            @Override
//            public void onclickClose() {
//                dismiss();
//            }
//
//            @Override
//            public void setDownProgress(int progress) {
//
//            }
//        };
//        goRechargeDialog.setMessage(msg);
//        goRechargeDialog.setBtnOkText("去充值");
//        goRechargeDialog.setTitleVisibility(View.VISIBLE);
//        goRechargeDialog.setTvTitle("温馨提示");
//        goRechargeDialog.show();
//    }

}
