package com.procoin.module.legal.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class AppealListAdapter extends RecyclerView.Adapter {


    private Context context;

    private String[] data;

    private int currSelected = -1;


    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public String getCurSelected() {
        if (data != null && currSelected != -1) {
            return data[currSelected];
        }
        return "";
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }


    public AppealListAdapter(Context context) {
        this.context = context;
    }

    public void setCurrSelected(int currSelected) {
        this.currSelected = currSelected;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.appeal_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position], position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTip)
        TextView tvTip;
        @BindView(R.id.cbCheck)
        CheckBox cbCheck;
        @BindView(R.id.llWay)
        LinearLayout llWay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String data, final int position) {
            tvTip.setText(data);
            cbCheck.setChecked(position == currSelected);
            llWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currSelected = position;
                    notifyDataSetChanged();
                }
            });
        }
    }

}
