package com.procoin.module.copy.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.copy.entity.CopyOrderPieChart;
import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 持币市值
 * Created by zhengmj on 18-10-26.
 */

public class CopyHoldMarketValueAdapter extends BaseImageLoaderRecycleAdapter<CopyOrderPieChart> {


    private Context context;

    public CopyHoldMarketValueAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.copy_hold_market_value_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvBalance)
        TextView tvBalance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CopyOrderPieChart data) {
            tvSymbol.setText(data.symbol);
            tvAmount.setText(data.amount);
            tvBalance.setText(data.balance);
        }
    }
}
