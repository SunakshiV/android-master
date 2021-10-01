package com.procoin.module.kbt.app.lightningprediction.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HorizontalAbilityNumAdapter extends RecyclerView.Adapter {


    private ArrayList<String> numData;
    private Context context;


    public void setNumData(String num) {
        if (TextUtils.isEmpty(num)) return;
        numData = new ArrayList<>();
        for (int i = 0, m = num.length(); i < m; i++) {
            numData.add(String.valueOf(num.charAt(i)));
        }
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return numData == null ? 0 : numData.size();
    }


    public HorizontalAbilityNumAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lp_num_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(numData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvNum)
        TextView tvNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(String num) {
            tvNum.setText(num);
        }
    }


}
