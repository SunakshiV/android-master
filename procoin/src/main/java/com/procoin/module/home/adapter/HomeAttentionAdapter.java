package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.module.home.entity.Attention;
import com.procoin.module.myhome.UserHomeActivity;
import com.procoin.util.DateUtils;
import com.procoin.widgets.CircleImageView;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HomeAttentionAdapter extends BaseImageLoaderRecycleAdapter<Attention> {


    private Context context;

    public HomeAttentionAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.home_attention_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivhead)
        CircleImageView ivhead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvDays)
        TextView tvDays;
        @BindView(R.id.tvRenew)
        TextView tvRenew;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.llRenew)
        LinearLayout llRenew;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Attention data) {
            displayImageForHead(data.headUrl, ivhead);
            tvName.setText(data.userName);
            tvDays.setText("已经入驻" + data.days + "天");

            if (data.subIsFee == 0) {
                llRenew.setVisibility(View.GONE);
            } else {
                llRenew.setVisibility(View.VISIBLE);
                if (data.isExpireTime == 0) {
                    tvTime.setText("到期：" + DateUtils.getStringDateOfString2(String.valueOf(data.expireTime), DateUtils.TEMPLATE_yyyyMMdd_divide));
                    tvTime.setTextColor(ContextCompat.getColor(context, R.color.c1d3155));
                } else {
                    tvTime.setText("订阅已过期");
                    tvTime.setTextColor(ContextCompat.getColor(context, R.color.red));
                }
            }


            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(context, data.userId);
                }
            });

        }
    }
}
