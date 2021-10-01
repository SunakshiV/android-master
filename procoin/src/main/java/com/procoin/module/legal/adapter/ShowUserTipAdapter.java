package com.procoin.module.legal.adapter;

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

public class ShowUserTipAdapter extends RecyclerView.Adapter {

    private Context context;

    private String[] data;

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }


    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public ShowUserTipAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.show_user_tip_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTips)
        TextView tvTips;
//        @BindView(R.id.viewGap)
//        View viewGap;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(String data) {
            tvTips.setText(data);
        }
    }


}
