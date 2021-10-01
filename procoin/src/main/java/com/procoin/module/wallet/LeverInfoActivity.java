package com.procoin.module.wallet;


import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.MarketActivity;
import com.procoin.module.wallet.adapter.CloseListAdapter;
import com.procoin.module.wallet.dialog.CloseOrderFragmentDialog;
import com.procoin.module.wallet.dialog.SetLossFragmentDialog;
import com.procoin.module.wallet.dialog.SetWinFragmentDialog;
import com.procoin.module.wallet.entity.OrderInfo;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.util.TjrMinuteTaskPool;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 杠杆详情页面
 */
public class LeverInfoActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvProfitRate)
    TextView tvProfitRate;
    @BindView(R.id.tvMargin)
    TextView tvMargin;
    @BindView(R.id.tvOrientation)
    TextView tvOrientation;
    @BindView(R.id.tvCostPrice)
    TextView tvCostPrice;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvOpenState)
    TextView tvOpenState;
    @BindView(R.id.tvUpdateWin)
    TextView tvUpdateWin;
    @BindView(R.id.tvUpdateLoss)
    TextView tvUpdateLoss;
    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvLastPrice)
    TextView tvLastPrice;
    @BindView(R.id.ivMark)
    ImageView ivMark;
    @BindView(R.id.tvCloseOrder)
    TextView tvCloseOrder;
    @BindView(R.id.tvSetWin)
    TextView tvSetWin;
    @BindView(R.id.tvSetLoss)
    TextView tvSetLoss;
    @BindView(R.id.llLastPrice)
    LinearLayout llLastPrice;
    @BindView(R.id.tvOpenDealAmount)
    TextView tvOpenDealAmount;
    @BindView(R.id.rvCloseList)
    RecyclerView rvCloseList;
    @BindView(R.id.tvCloseOrderCommon)
    TextView tvCloseOrderCommon;
//    @BindView(R.id.tvCloseCostPrice)
//    TextView tvCloseCostPrice;
//    @BindView(R.id.tvCloseFeePrice)
//    TextView tvCloseFeePrice;
//    @BindView(R.id.tvCloseTime)
//    TextView tvCloseTime;
//    @BindView(R.id.llClose)
//    LinearLayout llClose;
//    @BindView(R.id.tvCloseDealAmount)
//    TextView tvCloseDealAmount;
//    @BindView(R.id.tvCloseDealBalance)
//    TextView tvCloseDealBalance;

    private Call<ResponseBody> getPrybarDetailCall;
    private Call<ResponseBody> updateWinCall;
    private Call<ResponseBody> updateLossCall;
    private Call<ResponseBody> prybarCloseOrderCall;
    private SetWinFragmentDialog setWinFragmentDialog;
    private SetLossFragmentDialog setLossFragmentDialog;
    private CloseOrderFragmentDialog closeOrderFragmentDialog;

    private TjrMinuteTaskPool tjrMinuteTaskPool;

    private long orderId;
    private OrderInfo orderInfo;

    private CloseListAdapter closeListAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.lever_info;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    public static void pageJump(Context context, long orderId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERID, orderId);
        PageJumpUtil.pageJump(context, LeverInfoActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERID)) {
                orderId = bundle.getLong(CommonConst.ORDERID, 0);
            }

        }
        rvCloseList.setLayoutManager(new LinearLayoutManager(this));
        rvCloseList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0));
        closeListAdapter = new CloseListAdapter(this);
        rvCloseList.setAdapter(closeListAdapter);
        tvSetWin.setOnClickListener(this);
        tvSetLoss.setOnClickListener(this);
        tjrMinuteTaskPool = new TjrMinuteTaskPool();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetPrybarDetailCall();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        closeTimer();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseTimer();
        closeTimer();
    }

    boolean isRun;

    private void startTimer() {
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTimeWithoutRun(scheduledTask, getApplicationContext());

    }

    private void closeTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private void releaseTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
    }

    private Runnable scheduledTask = new Runnable() {
        public void run() {
            try {
                startGetPrybarDetailCall();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!MainApplication.isRun) {
                tjrMinuteTaskPool.closeTime();
            }
        }
    };


    private void startGetPrybarDetailCall() {
        CommonUtil.cancelCall(getPrybarDetailCall);
        getPrybarDetailCall = VHttpServiceManager.getInstance().getVService().prybarDetail(orderId);
        getPrybarDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    orderInfo = resultData.getObject("order", OrderInfo.class);
                    setData();
                }
            }
        });
    }


    private void startUpdateWinCall(final String stopWin, String payPass) {
        CommonUtil.cancelCall(updateWinCall);
        updateWinCall = VHttpServiceManager.getInstance().getVService().updateWinPrice(orderId, stopWin, payPass);
        updateWinCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (setWinFragmentDialog != null) setWinFragmentDialog.dismiss();
                    com.procoin.util.CommonUtil.showmessage(resultData.msg, LeverInfoActivity.this);
                    startGetPrybarDetailCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startUpdateWinCall(stopWin, pwString);
            }
        });
    }

    private void startUpdateLossCall(final String stopLoss, String payPass) {
        CommonUtil.cancelCall(updateLossCall);
        updateLossCall = VHttpServiceManager.getInstance().getVService().updateLossPrice(orderId, stopLoss, payPass);
        updateLossCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (setLossFragmentDialog != null) setLossFragmentDialog.dismiss();
                    com.procoin.util.CommonUtil.showmessage(resultData.msg, LeverInfoActivity.this);
                    startGetPrybarDetailCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startUpdateLossCall(stopLoss, pwString);
            }
        });
    }

    private void startPrybarCloseOrderCall(final String closeHand, String payPass) {
        CommonUtil.cancelCall(prybarCloseOrderCall);
        prybarCloseOrderCall = VHttpServiceManager.getInstance().getVService().prybarCloseOrder(orderId, closeHand, payPass);
        prybarCloseOrderCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (closeOrderFragmentDialog != null) closeOrderFragmentDialog.dismiss();
                    com.procoin.util.CommonUtil.showmessage(resultData.msg, LeverInfoActivity.this);
                    startGetPrybarDetailCall();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startPrybarCloseOrderCall(closeHand, pwString);
            }
        });
    }


//    TjrBaseDialog updateWinLossDialog;
//
//    private void showUpdateWinLossDialog(String msg, final String stopWin, final String stopLoss) {
//        updateWinLossDialog = new TjrBaseDialog(this) {
//            @Override
//            public void onclickOk() {
//                dismiss();
//                startUpdateWinLossCall(stopWin, stopLoss);
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
//        updateWinLossDialog.setMessage(msg);
//        updateWinLossDialog.setBtnOkText("确定");
//        updateWinLossDialog.setBtnColseText("取消");
//        updateWinLossDialog.setTitleVisibility(View.GONE);
//        updateWinLossDialog.show();
//    }

    private void setData() {
        if (orderInfo != null) {
            closeListAdapter.setGroup(orderInfo.closeDetails);

            tvProfit.setText(StockChartUtil.formatWithSign(orderInfo.profit));
            tvProfitRate.setText(StockChartUtil.formatWithSign(orderInfo.profitRate) + "%");
            int color = StockChartUtil.getRateTextColor(this, Double.parseDouble(orderInfo.profit));
            tvProfit.setTextColor(color);
            tvProfitRate.setTextColor(color);
            mActionBar.setTitle(orderInfo.symbol);
            tvMargin.setText(orderInfo.openBail);
            tvOrientation.setText(orderInfo.buySellValue);
            tvOrientation.setTextColor(StockChartUtil.getRateTextColor(this, orderInfo.buySell.equals("buy") ? 1 : -1));
            tvCostPrice.setText(orderInfo.openPrice);
            tvFee.setText(orderInfo.openFee);
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(orderInfo.openTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));

            tvSymbol.setText(orderInfo.symbol + "现价");
            tvOpenDealAmount.setText(orderInfo.openHand);
//            tvOpenDealBalance.setText(orderInfo.openDealBalance);

            tvUpdateWin.setText(orderInfo.stopWinPrice.equals("0") ? "无设置" : orderInfo.stopWinPrice);
            tvUpdateLoss.setText(orderInfo.stopLossPrice.equals("0") ? "无设置" : orderInfo.stopLossPrice);
            tvOpenState.setText(orderInfo.nowStateDesc);
            if (orderInfo.closeDone == 1) {
                llLastPrice.setVisibility(View.GONE);

                tvCloseOrder.setEnabled(false);
                tvCloseOrderCommon.setEnabled(false);

                tvSetWin.setVisibility(View.GONE);
                tvSetLoss.setVisibility(View.GONE);

//                llClose.setVisibility(View.VISIBLE);
//                tvCloseCostPrice.setText(orderInfo.closePrice);
//                tvCloseFeePrice.setText(orderInfo.closeFee);
//                tvCloseTime.setText(DateUtils.getStringDateOfString2(String.valueOf(orderInfo.closeTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            } else {
                llLastPrice.setVisibility(View.VISIBLE);
                llLastPrice.setOnClickListener(this);
                tvLastPrice.setText(orderInfo.last + "   " + StockChartUtil.formatWithSign(orderInfo.rate) + "%");
                tvLastPrice.setTextColor(StockChartUtil.getRateTextColor(this, Double.parseDouble(orderInfo.rate)));

                tvSetWin.setVisibility(View.VISIBLE);
                tvSetLoss.setVisibility(View.VISIBLE);

//                llClose.setVisibility(View.GONE);


                if (Double.parseDouble(orderInfo.rate) >= 0) {
                    ivMark.setImageResource(R.drawable.ic_mark_red);
                } else {
                    ivMark.setImageResource(R.drawable.ic_mark_green);
                }
                if (orderInfo.openDone == 1) {
                    tvCloseOrder.setEnabled(true);
                    tvCloseOrderCommon.setEnabled(true);
                    tvCloseOrder.setOnClickListener(this);
                    tvCloseOrderCommon.setOnClickListener(this);

                    tvSetWin.setVisibility(View.VISIBLE);
                    tvSetLoss.setVisibility(View.VISIBLE);

                } else {
                    tvCloseOrder.setEnabled(false);
                    tvCloseOrderCommon.setEnabled(false);

                    tvSetWin.setVisibility(View.GONE);
                    tvSetLoss.setVisibility(View.GONE);

                }

                if (!isRun) {
                    startTimer();
                }
            }
        }
    }

    TjrBaseDialog closeOrderDialog;

    private void showCloseOrderDialog() {
        closeOrderDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startPrybarCloseOrderCall("0", "");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        closeOrderDialog.setMessage("是否以当前市价平仓" + orderInfo.openHand + "？");
        closeOrderDialog.setBtnOkText("确定");
        closeOrderDialog.setBtnColseText("取消");
        closeOrderDialog.setTitleVisibility(View.GONE);
        closeOrderDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSetWin:
                if (orderInfo == null) return;
                setWinFragmentDialog = SetWinFragmentDialog.newInstance(orderInfo.stopWinPrice, orderInfo.priceDecimals);
                setWinFragmentDialog.setSetStopWinListen(new SetWinFragmentDialog.SetStopWinListen() {
                    @Override
                    public void goSetStopWin(String stopWin) {
                        startUpdateWinCall(stopWin, "");
                    }
                });
                setWinFragmentDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.tvSetLoss:
                if (orderInfo == null) return;
                setLossFragmentDialog = SetLossFragmentDialog.newInstance(orderInfo.stopLossPrice, orderInfo.priceDecimals);
                setLossFragmentDialog.setSetStopLossListen(new SetLossFragmentDialog.SetStopLossListen() {
                    @Override
                    public void goSetStopLoss(String stopLoss) {
                        startUpdateLossCall(stopLoss, "");
                    }
                });
                setLossFragmentDialog.show(getSupportFragmentManager(), "");
                break;

            case R.id.tvCloseOrder:
                if (orderInfo == null) return;
                showCloseOrderDialog();
                break;
            case R.id.tvCloseOrderCommon:
                if (orderInfo == null) return;
                closeOrderFragmentDialog = CloseOrderFragmentDialog.newInstance(orderInfo.openHand);
                closeOrderFragmentDialog.setSetAmountListen(new CloseOrderFragmentDialog.SetAmountListen() {
                    @Override
                    public void setAmount(String amount) {
                        if (TextUtils.isEmpty(amount)) return;
                        startPrybarCloseOrderCall(amount, "");
                    }
                });
                closeOrderFragmentDialog.show(getSupportFragmentManager(), "");
                break;
            case R.id.llLastPrice:
                if (orderInfo != null)
                    MarketActivity.pageJump(LeverInfoActivity.this, orderInfo.symbol, 1, true);
                break;

        }
    }

}
