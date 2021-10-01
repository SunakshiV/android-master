package com.procoin.module.legal.adapter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.http.base.Group;
import com.procoin.module.myhome.entity.AddPaymentTern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 快捷购买支付方式
 * Created by zhengmj on 18-10-26.
 */

public class SelectQuickPayWayAdapter extends BaseImageLoaderRecycleAdapter<AddPaymentTern> {


    private Context context;

    private onItemclickListen onItemclickListen;

    private AddPaymentTern currSelectPayWay;


    public void setOnItemclickListen(onItemclickListen onItemclickListen) {
        this.onItemclickListen = onItemclickListen;
    }


    public void setSelected(AddPaymentTern selectPayWay) {
        if (currSelectPayWay == null || currSelectPayWay.paymentId != (selectPayWay.paymentId)) {
            this.currSelectPayWay = selectPayWay;
            notifyDataSetChanged();
        }
    }

    @Override
    public void setGroup(Group<AddPaymentTern> g) {
        if (g != null && g.size() > 0) {//默认第一个选中
            currSelectPayWay = g.get(0);
        }
        super.setGroup(g);
    }

    public SelectQuickPayWayAdapter(Context context) {
        this.context = context;
    }

    public int getSelectReceiptType() {
        if (currSelectPayWay != null) {
            return currSelectPayWay.receiptType;
        }

        return -1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.select_pay_way_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ivImg)
        ImageView ivImg;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvRecommend)
        TextView tvRecommend;
        @BindView(R.id.tvBestPrice)
        TextView tvBestPrice;
        @BindView(R.id.ivSelected)
        AppCompatImageView ivSelected;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final AddPaymentTern payWay, int pos) {
            displayImage(payWay.receiptLogo, ivImg);
            tvName.setText(payWay.receiptTypeValue);
            if (currSelectPayWay != null && currSelectPayWay.paymentId == payWay.paymentId) {
                llItem.setSelected(true);
                ivSelected.setVisibility(View.VISIBLE);
            } else {
                llItem.setSelected(false);
                ivSelected.setVisibility(View.INVISIBLE);
            }
            if (pos == 0) {
                tvRecommend.setVisibility(View.VISIBLE);
                tvBestPrice.setVisibility(View.VISIBLE);
            } else {
                tvRecommend.setVisibility(View.GONE);
                tvBestPrice.setVisibility(View.GONE);
            }
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(payWay);
                    if (onItemclickListen != null) {
                        onItemclickListen.onItemclick(payWay);
                    }
                }
            });
        }
    }


    public interface onItemclickListen {
        public void onItemclick(AddPaymentTern product);
    }

}
