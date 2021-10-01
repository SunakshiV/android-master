package com.procoin.module.kbt.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.util.DateUtils;
import com.procoin.util.StockChartUtil;
import com.procoin.R;
import com.procoin.module.kbt.entity.KbtGetHistory;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class KbtGetHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<KbtGetHistory> {


    private Context context;

    public KbtGetHistoryAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.kbt_get_history_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvGetWay)
        TextView tvGetWay;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvKBTBalance)
        TextView tvKBTBalance;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final KbtGetHistory data) {
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            tvKBTBalance.setText(StockChartUtil.formatWithSign(data.amount));
            tvGetWay.setText(data.tradeTypeDesc);

        }
    }
}
