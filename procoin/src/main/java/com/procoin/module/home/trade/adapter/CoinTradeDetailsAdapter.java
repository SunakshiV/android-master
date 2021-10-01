package com.procoin.module.home.trade.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.common.base.adapter.AMBaseRecycleAdapter;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.R;
import com.procoin.module.home.entity.Deal;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CoinTradeDetailsAdapter extends AMBaseRecycleAdapter<Deal> {


    private Context context;


    public CoinTradeDetailsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvDealPrice)
        TextView tvDealPrice;
        @BindView(R.id.tvDealAmountText)
        TextView tvDealAmountText;
        @BindView(R.id.tvDealAmount)
        TextView tvDealAmount;
        @BindView(R.id.tvFeeText)
        TextView tvFeeText;
        @BindView(R.id.tvFee)
        TextView tvFee;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Deal data) {

            String originSymbol = CommonUtil.getOriginSymbol(data.symbol);
            String unitSymbol = CommonUtil.getUnitSymbol(data.symbol);

            tvTime.setText(DateUtils.getStringDateOfString2(data.timestamp, DateUtils.TEMPLATE_HHMMMMDD));
            tvDealPrice.setText(data.dealPrice);
            tvDealAmountText.setText("成交量(" + originSymbol + ")");
            tvDealAmount.setText(data.dealAmount);
            tvFee.setText(data.dealFee);
            if (data.buySell == 1) {//买入手续费是币，卖出手续费是usdt
                tvFeeText.setText("手续费(" + originSymbol + ")");
            } else {
                tvFeeText.setText("手续费(" + unitSymbol + ")");
            }
        }
    }


}
