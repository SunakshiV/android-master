package com.procoin.module.copy.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.http.base.Group;
import com.procoin.module.home.entity.Position;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟单持仓（被动）
 * Created by zhengmj on 18-10-26.
 */

public class CopyPositionAdapter extends BaseImageLoaderRecycleAdapter<Position> {


    private Context context;

    public CopyPositionAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    public void setGroupIsHide(Group<Position> group, boolean isHide) {
        if (isHide) {//隐藏小额币种
            if (group != null && group.size() > 0) {
                Group<Position> group1 = new Group<>();
                for (Position position : group) {
                    if (position.hide.equals("0")) {
                        group1.add(position);
                    }
                }
                setGroup(group1);
            }
        } else {
            setGroup(group);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.copy_position_item, parent, false));
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

        @BindView(R.id.tvAvgPrice)
        TextView tvAvgPrice;

        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.tvStateDesc)
        TextView tvStateDesc;


        @BindView(R.id.llItem)
        LinearLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data) {
//            tvSymbol.setText(data.symbol);
//            tvAvgPrice.setText(data.costPrice);
//            tvAmount.setText(data.amount);
//            if (!TextUtils.isEmpty(data.stateDesc)) {
//                tvStateDesc.setVisibility(View.VISIBLE);
//                tvStateDesc.setText(data.stateDesc);
//            } else {
//                tvStateDesc.setVisibility(View.GONE);
//            }
//            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
//            if (!TextUtils.isEmpty(data.profit)) {
////                tvProfitRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.profitRate), true) + "%");
////                tvProfit.setText(data.profit);
//                tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
//            }
//
//            item.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (data.symbol.equals("USDT")) {
//                        return;
//                    }
//                    MarketActivity.pageJump(context, data.symbol);
//                }
//            });
        }
    }
}
