package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.home.entity.Store;
import com.procoin.module.wallet.CoinWalletActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeCoinWalletAdapter extends BaseImageLoaderRecycleAdapter<Store> {



    private Context context;

    public HomeCoinWalletAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_wallet_item, parent, false));
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
        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.tvProfitTip)
        TextView tvProfitTip;
        @BindView(R.id.llItem)
        LinearLayout llItem;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Store data) {
            tvSymbol.setText(data.amountTip);
            tvAmount.setText(data.amount);
            tvProfitTip.setText(data.profitTip);
            tvProfit.setText(data.profit);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CoinWalletActivity.pageJump(context,data.amountSymbol);
                }
            });

//            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
//            if (!TextUtils.isEmpty(data.profit)) {
//                tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
//            }


        }
    }
}
