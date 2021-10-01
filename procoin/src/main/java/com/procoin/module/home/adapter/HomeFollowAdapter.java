package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.module.copy.CropyOrderInfoActivity;
import com.procoin.module.myhome.UserHomeActivity;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.home.entity.HomeCopyOrder;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeFollowAdapter extends BaseImageLoaderRecycleAdapter<HomeCopyOrder> {


    private Context context;

    public HomeFollowAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_follow_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivHead)
        CircleImageView ivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvBalance)
        TextView tvBalance;
        @BindView(R.id.tvProfit)
        TextView tvProfit;
//        @BindView(R.id.tvProfitRate)
//        TextView tvProfitRate;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final HomeCopyOrder data) {

            displayImageForHead(data.copyHeadUrl, ivHead);
            tvName.setText(data.copyName);
            tvBalance.setText(data.tolBalance);
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
            if (!TextUtils.isEmpty(data.profit)) {
//                tvProfitRate.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(data.profitRate), true) + "%");
                tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
            }
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CropyOrderInfoActivity.pageJump(context, data.orderId);
                }
            });
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(context,data.copyUid);
                }
            });


        }
    }
}
