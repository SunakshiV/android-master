package com.procoin.module.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.home.entity.Order;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 委托订单详情  废弃
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class OrderEntrustnfoActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.ivSuccess)
    ImageView ivSuccess;
    @BindView(R.id.tvCoinName)
    TextView tvCoinName;
    @BindView(R.id.tvFollowMoney)
    TextView tvFollowMoney;
    @BindView(R.id.tvMyMoney)
    TextView tvMyMoney;

    @BindView(R.id.tvTolMoney)
    TextView tvTolMoney;
    @BindView(R.id.tvBuyPrice)
    TextView tvBuyPrice;
    @BindView(R.id.tvBuyAmount)
    TextView tvBuyAmount;
    @BindView(R.id.tvOrderState)
    TextView tvOrderState;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.tvClose)
    TextView tvClose;

    @BindView(R.id.llBuy)
    LinearLayout llBuy;
    @BindView(R.id.tvFollowAmount)
    TextView tvFollowAmount;
    @BindView(R.id.tvMyAmount)
    TextView tvMyAmount;
    @BindView(R.id.tvTolAmount)
    TextView tvTolAmount;
    @BindView(R.id.tvSellPrice)
    TextView tvSellPrice;
    @BindView(R.id.tvSellMoney)
    TextView tvSellMoney;
    @BindView(R.id.llSell)
    LinearLayout llSell;


    private Order order;

    @Override
    protected int setLayoutId() {
        return R.layout.order_entrust;
    }

    @Override
    protected String getActivityTitle() {
        return "委托订单详情";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.language_setting);
        ButterKnife.bind(this);
        order = getApplicationContext().order;
        if (order == null) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        if (order.buySell == 1) {
            llBuy.setVisibility(View.VISIBLE);
            llSell.setVisibility(View.GONE);

            ivSuccess.setImageResource(R.drawable.ic_success);

            tvMyMoney.setText(order.balance);
            tvFollowMoney.setText(order.copyBalance);
            tvTolMoney.setText(order.tolBalance);

            tvBuyPrice.setText(order.price);
//            tvBuyAmount.setText(order.predictBalance);
        } else {
            llBuy.setVisibility(View.GONE);
            llSell.setVisibility(View.VISIBLE);

            ivSuccess.setImageResource(R.drawable.ic_success);

            tvFollowAmount.setText(order.copyBalance);
            tvMyAmount.setText(order.amount);
            tvTolAmount.setText(order.tolAmount);

            tvSellPrice.setText(order.price);
//            tvSellMoney.setText(order.predictBalance);

        }

        tvCoinName.setText(order.symbol);
        tvOrderState.setText(order.stateDesc);
        tvOrderNo.setText(String.valueOf(order.orderId));
        tvOrderTime.setText(DateUtils.getStringDateOfString2(String.valueOf(order.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
        tvClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.finishCurr(OrderEntrustnfoActivity.this);
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            order = getApplicationContext().order = null;
        }
    }
}
