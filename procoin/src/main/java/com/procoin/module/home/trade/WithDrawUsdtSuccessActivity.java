package com.procoin.module.home.trade;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * usdt提现成功页面(已废弃)
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class WithDrawUsdtSuccessActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.ivSuccess)
    ImageView ivSuccess;
    @BindView(R.id.tvWithDrawCount)
    TextView tvWithDrawCount;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvAccountName)
    TextView tvAccountName;
    @BindView(R.id.tvOrderState)
    TextView tvOrderState;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.tvClose)
    TextView tvClose;


    private OrderCash orderCash;

    @Override
    protected int setLayoutId() {
        return R.layout.with_draw_success;
    }

    @Override
    protected String getActivityTitle() {
        return "提现订单详情";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);

        orderCash = getApplicationContext().orderCash;
        if (orderCash == null) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }


        tvWithDrawCount.setText(orderCash.amount);

        if (orderCash.receipt != null) {
            tvAccountType.setText(orderCash.receipt.receiptTypeValue + "账号");
            tvAccount.setText(orderCash.receipt.receiptNo);
            tvAccountName.setText(orderCash.receipt.receiptName);

        }
        tvOrderState.setText(orderCash.stateDesc);
        tvOrderNo.setText(String.valueOf(orderCash.orderCashId));
        tvOrderTime.setText(DateUtils.getStringDateOfString2(String.valueOf(orderCash.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));

        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.finishCurr(WithDrawUsdtSuccessActivity.this);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            orderCash = getApplicationContext().orderCash = null;
        }
    }

}
