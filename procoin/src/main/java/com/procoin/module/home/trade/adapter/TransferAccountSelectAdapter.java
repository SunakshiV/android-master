package com.procoin.module.home.trade.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.AMBaseRecycleAdapter;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.trade.entity.AccountType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class TransferAccountSelectAdapter extends AMBaseRecycleAdapter<AccountType> {


    private OnItemClick onItemClick;
    private Context context;


    public TransferAccountSelectAdapter(Context context) {
        this.context = context;
    }


    public void setOnItemClick(OnItemClick onItemClick){
        this.onItemClick=onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.transfer_account_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TransferAccountSelectAdapter.ViewHolder holder1 = (TransferAccountSelectAdapter.ViewHolder) holder;
        holder1.setData(getItem(position),position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.llItem)
        LinearLayout llItem;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final AccountType data,final int pos) {
            tvName.setText(data.accountName);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClick!=null){
                        onItemClick.onItemClickListen(pos,data);
                    }
                }
            });

        }
    }

}
