package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.util.DateUtils;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.module.home.trade.OrderCashInfoActivity;
import com.procoin.module.home.trade.WithDrawInfoActivity;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class UsdtTradeHisAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<OrderCash> {


    private Context context;
    private long myUserId;

    public UsdtTradeHisAdapter(Context context, long myUserId) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
        this.myUserId = myUserId;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.usdt_trade_his_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvOrientation)
        TextView tvOrientation;

        @BindView(R.id.tvOrientationText)
        TextView tvOrientationText;

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvMoney)
        TextView tvMoney;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.ivArrow)
        ImageView ivArrow;
        @BindView(R.id.viewPoint)
        View viewPoint;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final OrderCash data) {
            if (data.buySell == 1) {
                if (data.symbol.equals("USDT")) {
                    tvOrientation.setText("充值");
                    tvOrientationText.setText("充值金额(元)");
                } else {
                    tvOrientation.setText("买入");
                    tvOrientationText.setText("买入总额(元)");
                }
                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_die_color));
//                tvMoney.setText(data.balanceCash);
            } else {
                if (data.symbol.equals("USDT")) {
                    tvOrientation.setText("提现");
                    tvOrientationText.setText("提现金额(元)");
                } else {
                    tvOrientation.setText("卖出");
                    tvOrientationText.setText("卖出金额(元)");
                }

                tvOrientation.setTextColor(ContextCompat.getColor(context, R.color.quotation_zhang_color));

//                tvMoney.setText(data.amountCash);
            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.buySell == 1) {
                        OrderCashInfoActivity.pageJump(context, data.orderCashId);
                    } else {
                        WithDrawInfoActivity.pageJump(context, data.orderCashId);
                    }

                }
            });
            tvMoney.setText(data.balanceCny);
            tvSymbol.setText(data.symbol);
            tvState.setText(data.stateDesc);
            tvAmount.setText(data.amount);
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));

            String chatTopic = CommonUtil.getChatTop(myUserId, data.handleUid, data.orderCashId);
            int chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(context, chatTopic, myUserId);
            viewPoint.setVisibility(chatCount > 0 ? View.VISIBLE : View.GONE);


        }
    }
}
