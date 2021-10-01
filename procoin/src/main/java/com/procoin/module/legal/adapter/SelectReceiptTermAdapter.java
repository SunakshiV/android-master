package com.procoin.module.legal.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.myhome.entity.Receipt;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 *
 * 快捷支付选择收款方式
 * Created by zhengmj on 18-10-26.
 */

public class SelectReceiptTermAdapter extends BaseImageLoaderRecycleAdapter<Receipt> {


    private Context context;

    private OnItemClick onItemClick;


    public SelectReceiptTermAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }


    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.select_receipt_term_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivReceiptLogo)
        ImageView ivReceiptLogo;
        @BindView(R.id.tvWay)
        TextView tvWay;
        @BindView(R.id.tvReceiptDesc)
        TextView tvReceiptDesc;
        @BindView(R.id.llWay)
        LinearLayout llWay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Receipt data, final int pos) {
            displayImageRound(data.receiptLogo, ivReceiptLogo);
            tvWay.setText(data.receiptTypeValue);
            if (!TextUtils.isEmpty(data.receiptDesc)) {
                tvReceiptDesc.setVisibility(View.VISIBLE);
                tvReceiptDesc.setText(data.receiptDesc);
            } else {
                tvReceiptDesc.setVisibility(View.GONE);
            }
            llWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) {
                        onItemClick.onItemClickListen(pos, data);
                    }
                }
            });
        }
    }


}
