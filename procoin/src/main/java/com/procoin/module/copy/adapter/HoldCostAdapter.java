package com.procoin.module.copy.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.R;
import com.procoin.module.copy.entity.Distribute;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 持币成本
 * Created by zhengmj on 18-10-26.
 */

public class HoldCostAdapter extends BaseImageLoaderRecycleAdapter<Distribute> {


    private Context context;

    public HoldCostAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.hold_cost_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvCostPrice)
        TextView tvCostPrice;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvProfitRate)
        TextView tvProfitRate;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Distribute data) {
            if (!TextUtils.isEmpty(data.fromPirce) && !TextUtils.isEmpty(data.toPirce)) {
                tvCostPrice.setText(data.fromPirce + "~" + data.toPirce);
            } else {
                tvCostPrice.setText(data.price);
            }
            tvAmount.setText(data.amount);
            tvProfitRate.setText(data.rate + "%");
        }
    }
}
