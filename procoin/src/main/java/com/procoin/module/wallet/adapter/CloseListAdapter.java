package com.procoin.module.wallet.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.module.wallet.entity.CloseDetails;
import com.procoin.util.DateUtils;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CloseListAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<CloseDetails> {



    private Context context;

    public CloseListAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.close_details_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvProfit)
        TextView tvProfit;
        @BindView(R.id.tvCloseCostPrice)
        TextView tvCloseCostPrice;
        @BindView(R.id.tvCloseDealAmount)
        TextView tvCloseDealAmount;
        @BindView(R.id.tvCloseFeePrice)
        TextView tvCloseFeePrice;
        @BindView(R.id.tvCloseTime)
        TextView tvCloseTime;
        @BindView(R.id.tvProfitShare)
        TextView tvProfitShare;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


        public void setData(final CloseDetails data) {
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
            if (!TextUtils.isEmpty(data.profit)) {
                tvProfit.setTextColor(StockChartUtil.getRateTextColor(context, Double.parseDouble(data.profit)));
            }
            tvCloseCostPrice.setText(data.closePrice);
            tvCloseDealAmount.setText(data.closeHand);
            tvCloseFeePrice.setText(data.closeFee);
            tvCloseTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.closeTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            tvProfitShare.setText(data.profitShare);

        }
    }
}
