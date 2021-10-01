package com.procoin.module.home.trade;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.adapter.SelectPayWayAdapter;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.myhome.entity.OrderCash;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 充值USDT余额
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class RechargeUsdtActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.etBalance)
    EditText etBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    //    @BindView(R.id.tvOrderOutline)
//    TextView tvOrderOutline;
    @BindView(R.id.tvRandomNum)
    TextView tvRandomNum;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private long money = 0;
    private int randomNum;
    private String realMoney = "0.00";//==money+randomNum

    private double usdtRate;

//    private int recharge;//默认充值金额

    private SelectPayWayAdapter selectPayWayAdapter;


    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> tradeCashOrderBuyCall;
    private Call<ResponseBody> getReceiptsCall;

    private static int AMOUNT_DECIMALCOUNT = 6;//数量的小数点数量


    public static void pageJump(Context context, int recharge) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.RECHARGE, recharge);
        PageJumpUtil.pageJump(context, RechargeUsdtActivity.class, bundle);
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
                money = bundle.getInt(CommonConst.RECHARGE, 0);
            }
        }
        if (money > 0) {
            etBalance.setText(String.valueOf(money));
        }
        etBalance.addTextChangedListener(new TextWatcher() {
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
                realMoney = money + "." + randomNum;
                setAmount();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etBalance.getText().toString())) {
                    CommonUtil.showmessage("请输入充值金额", RechargeUsdtActivity.this);
                    return;
                }
                long receiptId = selectPayWayAdapter.getSelectedReceiptId();
                if (receiptId <= 0) {
                    CommonUtil.showmessage("请选择支付方式", RechargeUsdtActivity.this);
                    return;
                }
                startTradeCashOrder(realMoney, receiptId);
//                SelectPayWayActivity.pageJump(RechargeUsdtActivity.this,String.valueOf(realMoney),"USDT","",1,1);
            }
        });
        immersionBar.keyboardEnable(false).init();

        selectPayWayAdapter = new SelectPayWayAdapter(this);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this, true));
        rv_list.setAdapter(selectPayWayAdapter);

//        selectPayWayAdapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void onItemClickListen(int pos, TaojinluType t) {
//                Receipt receipt = ((Receipt) t);
//                receiptId = receipt.receiptId;
//                for (Receipt r : selectPayWayAdapter.getGroup()) {
//                    receipt.isSelected = r.receiptId == receipt.receiptId;
//                }
//                selectPayWayAdapter.notifyDataSetChanged();
//            }
//        });

        startGetReceipts();

    }

    private void setAmount() {
        if (usdtRate == 0) return;
        if (money > 0) {
            BigDecimal amountBd = new BigDecimal(realMoney).divide(BigDecimal.valueOf(usdtRate), AMOUNT_DECIMALCOUNT, BigDecimal.ROUND_FLOOR);
            tvAmount.setText(amountBd.toPlainString() + "USDT");
        } else {
            tvAmount.setText("0.00USDT");
        }

    }


    private void startGetReceipts() {
        CommonUtil.cancelCall(getReceiptsCall);
        getReceiptsCall = VHttpServiceManager.getInstance().getVService().receiptsForPay();
        getReceiptsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Receipt> group = resultData.getGroup("receiptList", new TypeToken<Group<Receipt>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (group.size() == 1) {
                            selectPayWayAdapter.setDefaultSelectedOnSize1();
                        }
                        selectPayWayAdapter.setGroup(group);
                    }
                }
            }
        });
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
                    tvRandomNum.setText(String.valueOf(randomNum));
                    tvPrice.setText("￥" + StockChartUtil.formatNumber(2, usdtRate));
                    realMoney = money + "." + randomNum;
                    setAmount();
//                    tvUsdtBalance.setText("目前持有USDT:" + holdUsdt);// + "(" + "≈￥" + holdCash + ")");
                }
            }
        });
    }


    private void startTradeCashOrder(String cny, long receiptId) {
        CommonUtil.cancelCall(tradeCashOrderBuyCall);
        showProgressDialog();
        tradeCashOrderBuyCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderBuy(cny, receiptId);
        tradeCashOrderBuyCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, RechargeUsdtActivity.this);
                    OrderCash orderCash = resultData.getObject("order", OrderCash.class);
                    if (orderCash != null) {
//                        String title = "";
//                        if (type == 3) {
//                            title = "现金支付" + symbol + "订单";
//                        } else {
//                            title = "扫码支付" + symbol + "订单";
//                        }
                        setSwipeBackEnable(false);
                        setOverrideExitAniamtion(false);
                        OrderCashInfoActivity.pageJump(RechargeUsdtActivity.this,  orderCash.orderCashId);
                        PageJumpUtil.finishCurr(RechargeUsdtActivity.this);
                        overridePendingTransition(R.anim.login_enter, R.anim.login_out);
                    }
                }

            }
            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }


}
