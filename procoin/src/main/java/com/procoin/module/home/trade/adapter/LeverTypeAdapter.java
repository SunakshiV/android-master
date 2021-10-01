package com.procoin.module.home.trade.adapter;

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

public class LeverTypeAdapter extends RecyclerView.Adapter {

    private Context context;
    private String[] data;

    private String selectedKeyType = "";

    private onItemclickListen onItemclickListen;

    public void setOnItemclickListen(LeverTypeAdapter.onItemclickListen onItemclickListen) {
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


    public LeverTypeAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.key_type_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position], position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvKeyType)
        TextView tvKeyType;
//        @BindView(R.id.viewGap)
//        View viewGap;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String keyType, int pos) {
            tvKeyType.setText(keyType);
            if (selectedKeyType.equals(keyType)) {
                tvKeyType.setSelected(true);
            } else {
                tvKeyType.setSelected(false);
            }
//            if (pos == getItemCount() - 1) {
//                viewGap.setVisibility(View.GONE);
//            } else {
//                viewGap.setVisibility(View.VISIBLE);
//            }
            tvKeyType.setOnClickListener(new View.OnClickListener() {
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

    public void clearSelected() {
        this.selectedKeyType = "";
        notifyDataSetChanged();
    }


    public interface onItemclickListen {
        public void onItemclick(String symbol);
    }

}
