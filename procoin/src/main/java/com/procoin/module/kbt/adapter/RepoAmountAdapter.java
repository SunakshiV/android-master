package com.procoin.module.kbt.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class RepoAmountAdapter extends RecyclerView.Adapter {

    private Context context;
    private String[] data;

    private String selectedKeyType = "";

    private onItemclickListen onItemclickListen;

    public void setOnItemclickListen(RepoAmountAdapter.onItemclickListen onItemclickListen) {
        this.onItemclickListen = onItemclickListen;
    }

    public void setData(String[] data) {
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
        return data == null ? 0 : data.length;
    }


    public RepoAmountAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.repo_amount_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvAmountType)
        TextView tvAmountType;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String keyType) {
            tvAmountType.setText(keyType);
            if (selectedKeyType.equals(keyType)) {
                tvAmountType.setSelected(true);
            } else {
                tvAmountType.setSelected(false);
            }
            tvAmountType.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(keyType);
                    if (onItemclickListen != null) {
                        onItemclickListen.onItemclick(keyType);
                    }
                }
            });
        }
    }


    public void setSelected(String keyType) {
        if (!this.selectedKeyType.equals(keyType)) {
            this.selectedKeyType = keyType;
            notifyDataSetChanged();
        }

    }


    public interface onItemclickListen {
        void onItemclick(String amountType);
    }

}
