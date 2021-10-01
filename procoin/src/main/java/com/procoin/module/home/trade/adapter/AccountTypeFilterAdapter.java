package com.procoin.module.home.trade.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.module.home.trade.entity.AccountType;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class AccountTypeFilterAdapter extends RecyclerView.Adapter {

    private Context context;
    private Group<AccountType> data;

    private String selectedKeyType = "";

    private onItemclickListen onItemclickListen;

    public void setOnItemclickListen(AccountTypeFilterAdapter.onItemclickListen onItemclickListen) {
        this.onItemclickListen = onItemclickListen;
    }

    public void setData(Group<AccountType> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void clearAllItem() {
        data = null;
        selectedKeyType = "";
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    public AccountTypeFilterAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.account_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvKeyType)
        TextView tvKeyType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final AccountType accountType) {
            tvKeyType.setText(accountType.accountName);
            if (selectedKeyType.equals(accountType.accountName)) {
                tvKeyType.setSelected(true);
            } else {
                tvKeyType.setSelected(false);
            }
            tvKeyType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(accountType.accountName);
                    if (onItemclickListen != null) {
                        onItemclickListen.onItemclick(accountType.accountType);
                    }
                }
            });
        }
    }

    public void reset(){
        selectedKeyType="";
        notifyDataSetChanged();
    }

    public void setSelected(String keyType) {
        if (!this.selectedKeyType.equals(keyType)) {
            this.selectedKeyType = keyType;
            notifyDataSetChanged();
        }

    }


    public interface onItemclickListen {
        public void onItemclick(String accountType);
    }

}
