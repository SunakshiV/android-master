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
import com.procoin.http.base.Group;
import com.procoin.module.home.entity.Position;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomePositionAdapter extends BaseImageLoaderRecycleAdapter<Position> {


    private Context context;

    public HomePositionAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_position_item, parent, false));
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
        @BindView(R.id.tvProfitCash)
        TextView tvProfitCash;
        @BindView(R.id.tvEnableAmount)
        TextView tvEnableAmount;
        @BindView(R.id.tvFrozenAmount)
        TextView tvFrozenAmount;
        @BindView(R.id.tvCostPrice)
        TextView tvCostPrice;
        @BindView(R.id.llItem)
        LinearLayout item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data) {

//            tvSymbol.setText(data.symbol);
//            tvEnableAmount.setText(data.holdAmount);
//            tvFrozenAmount.setText(data.frozenAmount);
//            tvCostPrice.setText(data.costPrice);
//            tvProfitCash.setText(StockChartUtil.formatWithSign(data.profit));
//            if (!TextUtils.isEmpty(data.profit)) {
////                tvProfitRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.profitRate), true) + "%");
//                tvProfitCash.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
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
