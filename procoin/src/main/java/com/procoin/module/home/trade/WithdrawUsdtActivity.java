package com.procoin.module.home.trade;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.myhome.PaymentTermActivity;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.widgets.CircleImageView;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提现USDT余额
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class WithdrawUsdtActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvUsdtBalance)
    TextView tvUsdtBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvNext)
    TextView tvNext;

    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.ivReceiptLogo)
    CircleImageView ivReceiptLogo;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;

    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.rlSelectAccount)
    RelativeLayout rlSelectAccount;

    private double usdtRateWithdraw;
    private String holdUsdt = "0.0000000";
    private String holdCash = "0.00";

    private double amount;

    private Receipt receipt;

    private Call<ResponseBody> getTradeConfigCall;
    Call<ResponseBody> getInoutCreateCall;
    Call<ResponseBody> getDefaultReceipt;

    private TjrImageLoaderUtil tjrImageLoaderUtil;

    @Override
    protected int setLayoutId() {
        return R.layout.withdraw_usdt;
    }

    @Override
    protected String getActivityTitle() {
        return "USDT提现";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.withdraw_usdt);
        ButterKnife.bind(this);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        immersionBar.keyboardEnable(false).init();

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
                    if (s.length() - 1 - posDot > 8) {//最多2位小数
                        s.delete(posDot + 9, posDot + 10);
                    }
                }
                amount = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    amount = Double.parseDouble(s.toString());
                } else {
                    amount = 0;
                }

                if (amount > 0) {
                    tvMoney.setText("￥" + StockChartUtil.formatNumber(2, amount * usdtRateWithdraw));
//                    tvOrderOutline.setVisibility(View.VISIBLE);
//                    tvOrderOutline.setText("您正在进行当日有效有损限价单,以￥" + StockChartUtil.formatNumber(2, usdtRate) + "的价格卖出USDT总数量" + StockChartUtil.formatNumber(8, amount));
                } else {
                    tvMoney.setText("￥0.00");
//                    tvOrderOutline.setVisibility(View.GONE);
                }

            }
        });

        tvNext.setOnClickListener(this);
        rlSelectAccount.setOnClickListener(this);

        startReceiptGetDefault();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetTradeConfig();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x789 && data != null) {
            receipt = data.getParcelableExtra("Receipt");
            setData();
        }
    }

    private void setData() {
        if (receipt != null && receipt.paymentId > 0) {
            ivReceiptLogo.setVisibility(View.VISIBLE);
            tvType.setVisibility(View.VISIBLE);
            llAccount.setVisibility(View.VISIBLE);
            tvAdd.setVisibility(View.INVISIBLE);

            tjrImageLoaderUtil.displayImage(receipt.receiptLogo, ivReceiptLogo);
            tvType.setText(receipt.receiptTypeValue);

            tvAccount.setText(receipt.receiptNo);
            tvName.setText(receipt.receiptName);

        } else {
            ivReceiptLogo.setVisibility(View.INVISIBLE);
            tvType.setVisibility(View.INVISIBLE);
            llAccount.setVisibility(View.INVISIBLE);
            tvAdd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlSelectAccount:
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConst.JUMPTYPE, 1);
                PageJumpUtil.pageJumpResult(WithdrawUsdtActivity.this, PaymentTermActivity.class, bundle);
                break;
            case R.id.tvNext:
                if (receipt == null || receipt.paymentId == 0) {
                    CommonUtil.showmessage("请添加收款方式", WithdrawUsdtActivity.this);
                    return;
                }
//                String amount = etAmount.getText().toString();
                if (amount <= 0) {
                    CommonUtil.showmessage("请输入提现数量", WithdrawUsdtActivity.this);
                    return;
                }
                if (amount > Double.parseDouble(holdUsdt)) {
                    CommonUtil.showmessage("USDT余额不足", WithdrawUsdtActivity.this);
                    return;
                }
                startTradeCashOrder(String.valueOf(amount), receipt.paymentId, "");
                break;
        }
    }

    private void startGetTradeConfig() {
        CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("","");
        getTradeConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    usdtRateWithdraw = resultData.getItem("usdtRateWithdraw", Double.class);
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    holdCash = resultData.getItem("holdCash", String.class);
                    tvPrice.setText("￥" + StockChartUtil.formatNumber(2, usdtRateWithdraw));
                    tvUsdtBalance.setText("目前持有USDT:" + holdUsdt);// + "(" + "≈￥" + holdCash + ")");
                }
            }
        });
    }

    private void startReceiptGetDefault() {
        CommonUtil.cancelCall(getDefaultReceipt);
        getDefaultReceipt = VHttpServiceManager.getInstance().getVService().receiptGetDefault();
        getDefaultReceipt.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    receipt = resultData.getObject("receipt", Receipt.class);
                    setData();
                }
            }
        });
    }


    private void startTradeCashOrder(final String entrustAmount, final long receiptId, String payPass) {
        com.procoin.http.util.CommonUtil.cancelCall(getInoutCreateCall);
        showProgressDialog();
        getInoutCreateCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderSell(entrustAmount, receiptId, payPass);
        getInoutCreateCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    com.procoin.http.util.CommonUtil.showmessage(resultData.msg, WithdrawUsdtActivity.this);
                    OrderCash orderCash = resultData.getObject("order", OrderCash.class);
                    if(orderCash!=null){
                        WithDrawInfoActivity.pageJump(WithdrawUsdtActivity.this,orderCash.orderCashId);
                    }
//                    getApplicationContext().orderCash = orderCash;
//                    if (orderCash != null) {
//                        PageJumpUtil.pageJump(WithdrawUsdtActivity.this, WithDrawUsdtSuccessActivity.class);
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                PageJumpUtil.finishCurr(WithdrawUsdtActivity.this);
//
//                            }
//                        }, 500);
//                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startTradeCashOrder(entrustAmount, receiptId, pwString);
            }
        });
    }
}
