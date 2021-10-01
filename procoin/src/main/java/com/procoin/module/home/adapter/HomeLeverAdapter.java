package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.home.entity.Prybar;
import com.procoin.module.wallet.LeverInfoActivity;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeLeverAdapter extends BaseImageLoaderRecycleAdapter<Prybar> {


    private Context context;

    public HomeLeverAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_lever_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvProfitRate)
        TextView tvProfitRate;
        @BindView(R.id.tvOrientation)
        TextView tvOrientation;
        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.tvMultiNum)
        TextView tvMultiNum;
        @BindView(R.id.llItem)
        LinearLayout llItem;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Prybar data) {
            tvSymbol.setText(data.symbol);
            tvOrientation.setText(data.buySellStr);
            tvMultiNum.setText(data.multiNum + "X");
            tvProfitRate.setText(data.rate + "%");
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
            if (!TextUtils.isEmpty(data.profit)) {
                tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
            }
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LeverInfoActivity.pageJump(context,data.orderId);
                }
            });


        }
    }
}
