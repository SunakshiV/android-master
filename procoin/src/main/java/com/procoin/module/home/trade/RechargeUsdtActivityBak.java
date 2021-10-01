package com.procoin.module.home.trade;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 充值USDT余额
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class RechargeUsdtActivityBak extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvUsdtBalance)
    TextView tvUsdtBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
//    @BindView(R.id.tvOrderOutline)
//    TextView tvOrderOutline;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvRandomNum)
    TextView tvRandomNum;


    private long money = 0;
    private double usdtRate;
    private int randomNum;
    private String holdUsdt = "0.00000000";
    private String holdCash = "0.00";
    private double realMoney = 0.00;//==money+randomNum


    private int recharge;


    private Call<ResponseBody> getTradeConfigCall;


    public static void pageJump(Context context,int recharge) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.RECHARGE, recharge);
        PageJumpUtil.pageJump(context, RechargeUsdtActivityBak.class, bundle);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.recharge_usdt;
    }

    @Override
    protected String getActivityTitle() {
        return "充值USDT";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.recharge_usdt);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.RECHARGE)) {
                recharge = bundle.getInt(CommonConst.RECHARGE, 0);
            }
        }


        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    money = Long.parseLong(s.toString());
                } else {
                    money = 0;
                }
                realMoney = Double.parseDouble(money + "." + randomNum);

                tvAmount.setText(StockChartUtil.formatNumber(8, realMoney / usdtRate));

//                if (money > 0) {
//                    tvAmount.setText(StockChartUtil.formatNumber(8, realMoney / usdtRate));
//                } else {
//                    tvAmount.setText("0.00");
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etMoney.getText().toString())) {
                    CommonUtil.showmessage("请输入充值金额", RechargeUsdtActivityBak.this);
                    return;
                }
                SelectPayWayActivity.pageJump(RechargeUsdtActivityBak.this,String.valueOf(realMoney),"USDT","",1,1);
            }
        });
        immersionBar.keyboardEnable(false).init();

    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetTradeConfig();
    }

    private void startGetTradeConfig() {
        CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("","");
        getTradeConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    usdtRate = resultData.getItem("usdtRate", Double.class);
                    randomNum = resultData.getItem("randomNum", Integer.class);
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    holdCash = resultData.getItem("holdCash", String.class);
                    tvRandomNum.setText(String.valueOf(randomNum));
                    tvPrice.setText("￥" + StockChartUtil.formatNumber(2, usdtRate));
                    tvUsdtBalance.setText("目前持有USDT:" + holdUsdt);// + "(" + "≈￥" + holdCash + ")");
                }
            }
        });
    }


}
