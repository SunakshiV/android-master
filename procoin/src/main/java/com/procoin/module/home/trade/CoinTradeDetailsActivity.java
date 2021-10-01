package com.procoin.module.home.trade;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.entity.Deal;
import com.procoin.module.home.entity.Order;
import com.procoin.module.home.entity.OrderStateEnum;
import com.procoin.module.home.trade.adapter.CoinTradeDetailsAdapter;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易详情
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class CoinTradeDetailsActivity extends TJRBaseActionBarSwipeBackObserverActivity {


    @BindView(R.id.tvOrientation)
    TextView tvOrientation;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvEntrustAmountText)
    TextView tvEntrustAmountText;
    @BindView(R.id.tvEntrustAmount)
    TextView tvEntrustAmount;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDealAvgPrice)
    TextView tvDealAvgPrice;
    @BindView(R.id.tvDealAmountText)
    TextView tvDealAmountText;
    @BindView(R.id.tvDealAmount)
    TextView tvDealAmount;
    @BindView(R.id.tvDealBalance)
    TextView tvDealBalance;
    @BindView(R.id.tvTolFee)
    TextView tvTolFee;
    @BindView(R.id.llCopyFencheng)
    LinearLayout llCopyFencheng;
    @BindView(R.id.llServiceFee)
    LinearLayout llServiceFee;
    @BindView(R.id.tvCopyFencheng)
    TextView tvCopyFencheng;
    @BindView(R.id.tvServiceFee)
    TextView tvServiceFee;
    @BindView(R.id.tvMyEntrustAmountText)
    TextView tvMyEntrustAmountText;
    @BindView(R.id.tvMyEntrustAmount)
    TextView tvMyEntrustAmount;
    @BindView(R.id.tvMyDealAmountText)
    TextView tvMyDealAmountText;
    @BindView(R.id.tvMyDealAmountt)
    TextView tvMyDealAmountt;
    @BindView(R.id.tvMyFee)
    TextView tvMyFee;
    @BindView(R.id.tvMyDealBalance)
    TextView tvMyDealBalance;
    @BindView(R.id.tvCopyEntrustAmountText)
    TextView tvCopyEntrustAmountText;
    @BindView(R.id.tvCopyEntrustAmount)
    TextView tvCopyEntrustAmount;
    @BindView(R.id.tvCopyDealAmountText)
    TextView tvCopyDealAmountText;
    @BindView(R.id.tvCopyDealAmount)
    TextView tvCopyDealAmount;
    @BindView(R.id.tvCopyFee)
    TextView tvCopyFee;
    @BindView(R.id.tvCopyDealBalance)
    TextView tvCopyDealBalance;
    @BindView(R.id.llOpenCopy)
    LinearLayout llOpenCopy;
    @BindView(R.id.rvDetailsList)
    RecyclerView rvDetailsList;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    @BindView(R.id.tvTolFeeText)
    TextView tvTolFeeText;
    @BindView(R.id.tvMyFeeText)
    TextView tvMyFeeText;
    @BindView(R.id.tvCopyFeeText)
    TextView tvCopyFeeText;

    @BindView(R.id.llProfit)
    LinearLayout llProfit;
    @BindView(R.id.tvProfit)
    TextView tvProfit;

    @BindView(R.id.tvPriceText)
    TextView tvPriceText;
    @BindView(R.id.tvDealAvgPriceText)
    TextView tvDealAvgPriceText;
    @BindView(R.id.tvDealBalanceText)
    TextView tvDealBalanceText;
    @BindView(R.id.tvMyDealBalanceText)
    TextView tvMyDealBalanceText;
    @BindView(R.id.tvCopyFenchengText)
    TextView tvCopyFenchengText;
    @BindView(R.id.tvServiceFeeText)
    TextView tvServiceFeeText;
    @BindView(R.id.tvCopyDealBalanceText)
    TextView tvCopyDealBalanceText;


    private long orderId;
    private int pos = -1;

    private Call<ResponseBody> getTradeOrderDetailCall;

    private Order order;
    private Group<Deal> dealGroup;

    private CoinTradeDetailsAdapter coinTradeDetailsAdapter;

    private Call<ResponseBody> tradeCancelCall;
    private TjrBaseDialog cancelTipsDialog;


    public static void pageJump(Context context, long orderId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERID, orderId);
        PageJumpUtil.pageJump(context, CoinTradeDetailsActivity.class, bundle);
    }

    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case refresh_entrust_order_api:
                startGetTradeOrderDetail();
                break;
        }
    }

    @Override
    protected int setLayoutId() {
        return R.layout.coin_trade_details;
    }

    @Override
    protected String getActivityTitle() {
        return "成交明细";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERID)) {
                orderId = bundle.getLong(CommonConst.ORDERID, 0l);
            }
            if (bundle.containsKey(CommonConst.POS)) {
                pos = bundle.getInt(CommonConst.POS, -1);
            }
        }

        coinTradeDetailsAdapter = new CoinTradeDetailsAdapter(this);
        rvDetailsList.setLayoutManager(new LinearLayoutManager(this));
        rvDetailsList.addItemDecoration(new SimpleRecycleDivider(this, true));
        rvDetailsList.setAdapter(coinTradeDetailsAdapter);

        startGetTradeOrderDetail();

    }


    private void startGetTradeOrderDetail() {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeOrderDetailCall);
        getTradeOrderDetailCall = VHttpServiceManager.getInstance().getVService().tradeOrderDetail(orderId);
        getTradeOrderDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    order = resultData.getObject("order", Order.class);
                    dealGroup = resultData.getGroup("dealList", new TypeToken<Group<Deal>>() {
                    }.getType());
                    setData();
                }
            }
        });
    }

    private void setData() {
        if (order != null) {
            if (order.buySell == 1) {
                tvOrientation.setText("买入");
                tvOrientation.setTextColor(ContextCompat.getColor(this, R.color.quotation_zhang_color));
                llProfit.setVisibility(View.GONE);
//                llCopyFencheng.setVisibility(View.INVISIBLE);
//                llServiceFee.setVisibility(View.INVISIBLE);

            } else {
                tvOrientation.setText("卖出");
                tvOrientation.setTextColor(ContextCompat.getColor(this, R.color.quotation_die_color));

                llProfit.setVisibility(View.VISIBLE);
                tvProfit.setText(order.profit);
//                if(order.openCopy==1){
//                    llCopyFencheng.setVisibility(View.VISIBLE);
//                    llServiceFee.setVisibility(View.VISIBLE);
//                    tvCopyFencheng.setText(order.profitShare);
//                    tvServiceFee.setText(order.serviceShare);
//                }else{
//                    llCopyFencheng.setVisibility(View.INVISIBLE);
//                    llServiceFee.setVisibility(View.INVISIBLE);
//                }
            }
            tvSymbol.setText(order.symbol);
            tvState.setText(order.stateDesc);


            String originSymbol = CommonUtil.getOriginSymbol(order.symbol);
            String unitSymbol = CommonUtil.getUnitSymbol(order.symbol);

//            @BindView(R.id.tvServiceFeeText)
//            TextView tvServiceFeeText;
//            @BindView(R.id.tvCopyDealBalanceText)
//            TextView tvCopyDealBalanceText;

            //全部
            tvPriceText.setText("委托价格(" + unitSymbol + ")");
            tvPrice.setText(order.price);

            tvEntrustAmountText.setText("委托数量(" + originSymbol + ")");
            tvEntrustAmount.setText(order.tolAmount);

            tvTime.setText(DateUtils.getStringDateOfString2(order.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));

            tvDealAvgPriceText.setText("成交均价(" + unitSymbol + ")");
            tvDealAvgPrice.setText(order.dealAvgPrice);

            tvDealAmountText.setText("成交数量(" + originSymbol + ")");
            tvDealAmount.setText(order.dealTolAmount);

            tvDealBalanceText.setText("成交额(" + unitSymbol + ")");
            tvDealBalance.setText(order.dealTolBalance);

            if (order.buySell == 1) {//买入手续费是币，卖出手续费是usdt
                tvTolFeeText.setText("手续费(" + originSymbol + ")");
            } else {
                tvTolFeeText.setText("手续费(" + unitSymbol + ")");
            }
            Log.d("tvTolFee", "order.dealTolFee==" + order.dealTolFee);
            tvTolFee.setText(String.valueOf(order.dealTolFee));

            if (order.openCopy == 0) {
                llOpenCopy.setVisibility(View.GONE);
            } else {
                llOpenCopy.setVisibility(View.VISIBLE);
                tvCopyFenchengText.setText("跟单分成(" + unitSymbol + ")");
                tvCopyFencheng.setText(order.profitShare);
                tvServiceFeeText.setText("技术服务费(" + unitSymbol + ")");
                tvServiceFee.setText(order.serviceShare);

                //我的
                tvMyEntrustAmountText.setText("委托数量(" + originSymbol + ")");
                tvMyEntrustAmount.setText(order.amount);
                tvMyDealAmountText.setText("成交数量(" + originSymbol + ")");
                tvMyDealAmountt.setText(order.dealAmount);
                if (order.buySell == 1) {//买入手续费是币，卖出手续费是usdt
                    tvMyFeeText.setText("手续费(" + originSymbol + ")");
                } else {
                    tvMyFeeText.setText("手续费(" + unitSymbol + ")");
                }
                tvMyFee.setText(order.dealFee);
                tvMyDealBalanceText.setText("成交额(" + unitSymbol + ")");
                tvMyDealBalance.setText(order.dealBalance);

                //跟单
                tvCopyEntrustAmountText.setText("委托数量(" + originSymbol + ")");
                tvCopyEntrustAmount.setText(order.copyAmount);

                tvCopyDealAmountText.setText("成交数量(" + originSymbol + ")");
                tvCopyDealAmount.setText(order.copyDealAmount);

                if (order.buySell == 1) {//买入手续费是币，卖出手续费是usdt
                    tvCopyFeeText.setText("手续费(" + originSymbol + ")");
                } else {
                    tvCopyFeeText.setText("手续费(" + unitSymbol + ")");
                }
                tvCopyFee.setText(order.copyDealFee);
                tvCopyDealBalanceText.setText("成交额(" + unitSymbol + ")");
                tvCopyDealBalance.setText(order.copyDealBalance);
            }

            if (OrderStateEnum.isCancelEnable(order.state)) {
                tvCancel.setVisibility(View.VISIBLE);
                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showCancelTipsDialog();
                    }
                });
            } else {
                tvCancel.setVisibility(View.GONE);
            }
        }

        if (dealGroup != null && dealGroup.size() > 0) {
            coinTradeDetailsAdapter.setGroup(dealGroup);
        }
    }


    private void showCancelTipsDialog() {
        cancelTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startTradeCancel();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        cancelTipsDialog.setMessage("确定撤销订单");
        cancelTipsDialog.setBtnOkText("撤销");
        cancelTipsDialog.setTitleVisibility(View.GONE);
        cancelTipsDialog.show();
    }


    private void startTradeCancel() {
        if (order == null) {
            finish();
            return;
        }
        showProgressDialog();
        CommonUtil.cancelCall(tradeCancelCall);
        tradeCancelCall = VHttpServiceManager.getInstance().getVService().tradeCancel(order.orderId);
        tradeCancelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CoinTradeDetailsActivity.this);
                    if (pos >= 0) {
                        Intent intent = new Intent();
                        intent.putExtra(CommonConst.POS, pos);
                        setResult(0x123, intent);
                    }
                    finish();

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
