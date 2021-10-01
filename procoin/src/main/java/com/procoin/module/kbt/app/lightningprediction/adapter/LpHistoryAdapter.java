package com.procoin.module.kbt.app.lightningprediction.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.module.kbt.app.lightningprediction.entity.LpHistory;
import com.procoin.util.DateUtils;
import com.procoin.util.StockChartUtil;
import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 预测历史记录
 * Created by zhengmj on 18-10-26.
 */

public class LpHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<LpHistory> {


    private Context context;


    public LpHistoryAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lp_history_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvAbility)
        TextView tvAbility;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final LpHistory data) {

            tvTitle.setText(data.amount + "YYB预测" + (data.myVote == 1 ? "超过" : "不超过"));
            tvTime.setText(DateUtils.getStringDateOfString2(data.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));
            tvAbility.setText(StockChartUtil.formatWithSign(data.receive) + "YYB");
            tvState.setText(data.resultDesc);

        }
    }

}
