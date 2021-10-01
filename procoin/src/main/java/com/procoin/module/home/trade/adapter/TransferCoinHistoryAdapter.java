package com.procoin.module.home.trade.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.module.home.trade.entity.TransferHistory;
import com.procoin.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 划转历史记录
 * Created by zhengmj on 18-10-26.
 */

public class TransferCoinHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<TransferHistory> {


    private Context context;


    public TransferCoinHistoryAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.transfer_coin_history_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvTime)
        TextView tvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        public void setData(final TransferHistory data, final int pos) {
            tvAmount.setText(data.amount);
            tvType.setText(data.typeValue);
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));
        }
    }


}
