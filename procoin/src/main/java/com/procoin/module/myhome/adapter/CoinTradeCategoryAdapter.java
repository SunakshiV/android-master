package com.procoin.module.myhome.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.AMBaseRecycleAdapter;
import com.procoin.module.myhome.entity.CoinTradeCount;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CoinTradeCategoryAdapter extends AMBaseRecycleAdapter<CoinTradeCount> {


    private Context context;


    public CoinTradeCategoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.coin_trade_category_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvCoinName)
        TextView tvCoinName;
        @BindView(R.id.tvCount)
        TextView tvCount;
        @BindView(R.id.tvProfit)
        TextView tvProfit;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(CoinTradeCount data, int pos) {
            tvCoinName.setText(data.symbol);
            tvCount.setText(data.num);
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));

//            if (pos % 2 == 0) {
//                llItem.setBackgroundResource(R.color.cf4f5f6);
//            }else{
//                llItem.setBackgroundResource(R.color.white);
//
//            }
        }
    }


}
