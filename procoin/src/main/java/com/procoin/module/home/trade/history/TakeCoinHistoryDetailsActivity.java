package com.procoin.module.home.trade.history;


import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.home.entity.OrderStateEnum;
import com.procoin.module.home.trade.entity.TakeCoinHistory;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币充币详情
 */
public class TakeCoinHistoryDetailsActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    @BindView(R.id.tvFee)
    TextView tvFee;
    @BindView(R.id.tvTakeCoinAddress)
    TextView tvTakeCoinAddress;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvCancel)
    TextView tvCancel;

    private TakeCoinHistory takeCoinHistory;

    private Call<ResponseBody> tradeCancelCall;

    private TjrBaseDialog cancelTipsDialog;

    public static void pageJump(Context context, TakeCoinHistory takeCoinHistory) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("details", takeCoinHistory);
        PageJumpUtil.pageJump(context, TakeCoinHistoryDetailsActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin_his_details;
    }

    @Override
    protected String getActivityTitle() {
        return "详情";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("details")) {
                takeCoinHistory = bundle.getParcelable("details");
            }
        }
        if (takeCoinHistory != null) {
            tvAmount.setText(takeCoinHistory.amount);
            if (takeCoinHistory.inOut == 1) {
                tvAccountType.setText("充币");
            } else {
                tvAccountType.setText("提币");
            }
            tvFee.setText(takeCoinHistory.fee);
            tvTakeCoinAddress.setText(takeCoinHistory.address);
            tvState.setText(takeCoinHistory.stateDesc);
            tvTime.setText(DateUtils.getStringDateOfString2(takeCoinHistory.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));


            if (takeCoinHistory.state == 0) {
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
    }

    private void showCancelTipsDialog() {
        cancelTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startWithdrawCoinCancel();
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


    private void startWithdrawCoinCancel() {
        if (takeCoinHistory == null) return;
        CommonUtil.cancelCall(tradeCancelCall);
        tradeCancelCall = VHttpServiceManager.getInstance().getVService().withdrawCoinCancel(takeCoinHistory.dwId);
        tradeCancelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, TakeCoinHistoryDetailsActivity.this);
                    takeCoinHistory.state = OrderStateEnum.canceled.getState();
                    finish();
                }
            }

        });
    }


}
