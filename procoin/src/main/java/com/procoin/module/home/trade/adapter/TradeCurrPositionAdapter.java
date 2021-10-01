package com.procoin.module.home.trade.adapter;

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
import com.procoin.http.base.Group;
import com.procoin.module.home.entity.Position;
import com.procoin.module.wallet.LeverInfoActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class TradeCurrPositionAdapter extends BaseImageLoaderRecycleAdapter<Position> {

    private Context context;

    public TradeCurrPositionAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_position_item, parent, false));
    }


    @Override
    public void setGroup(Group<Position> g) {

        super.setGroup(g);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvBuySell)
        TextView tvBuySell;
        @BindView(R.id.tvProfitCash)
        TextView tvProfitCash;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvOpenPrice)
        TextView tvOpenPrice;
        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data) {

            tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
            tvBuySell.setText( "•" + data.buySellValue);
            tvProfitCash.setText(StockChartUtil.formatWithSign(data.profitRate) + "%");
            tvAmount.setText(data.openHand);
            tvOpenPrice.setText(String.valueOf(data.openPrice));
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));

            if (!TextUtils.isEmpty(data.profit)) {
                int color=StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit));
                tvProfitCash.setTextColor(color);
                tvProfit.setTextColor(color);
            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.symbol.equals("USDT")) {
                        return;
                    }
                    LeverInfoActivity.pageJump(context, data.orderId);
                }
            });
        }
    }
}
