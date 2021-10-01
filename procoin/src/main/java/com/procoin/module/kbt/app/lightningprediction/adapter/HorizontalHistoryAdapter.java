package com.procoin.module.kbt.app.lightningprediction.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.procoin.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HorizontalHistoryAdapter extends RecyclerView.Adapter {


    private List<Integer> numData;
    private Context context;


    public void setNumData(List<Integer> numData) {
        this.numData = numData;
        notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return numData == null ? 0 : numData.size();
    }


    public HorizontalHistoryAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lp_ability_his_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(numData.get(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivNum)
        ImageView ivNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(int num) {
            if (num == 1) {//-1不超，1超过
                ivNum.setImageResource(R.drawable.shape_point_lp_history_green);
            } else if (num == -1) {
                ivNum.setImageResource(R.drawable.shape_point_lp_history_red);
            } else {
                ivNum.setImageResource(R.drawable.shape_point_lp_history_gray);
            }
        }
    }


}
