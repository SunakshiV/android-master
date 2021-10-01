package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.module.myhome.entity.Receipt;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.home.OnItemClick;
import com.procoin.widgets.CircleImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class SelectPayWayAdapter extends BaseImageLoaderRecycleAdapter<Receipt> {


    private Context context;

    private OnItemClick onItemClick;

    private List<Integer> list = new ArrayList<>();


    public SelectPayWayAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

    //当只有一种支付方式的时候，默认选中
    public void setDefaultSelectedOnSize1(){
        list.clear();
        list.add(0);
    }

    public long getSelectedReceiptId() {
        if (list == null || list.size() != 1) return 0l;
        return getGroup().get(list.get(0)).paymentId;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.add_payment_tern_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivReceiptLogo)
        CircleImageView ivReceiptLogo;
        @BindView(R.id.tvWay)
        TextView tvWay;
        //        @BindView(R.id.tvDesc)
//        TextView tvDesc;
        @BindView(R.id.cb)
        CheckBox cb;
        @BindView(R.id.llWay)
        LinearLayout llWay;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Receipt data, final int pos) {
            displayImage(data.receiptLogo, ivReceiptLogo);
            tvWay.setText(data.receiptTypeValue);
            if (list.contains(pos)) {
                cb.setChecked(true);
            } else {
                cb.setChecked(false);
            }
//            if (!TextUtils.isEmpty(data.receiptDesc)) {
//                tvDesc.setVisibility(View.VISIBLE);
//                tvDesc.setText(data.receiptDesc);
//            } else {
//                tvDesc.setVisibility(View.GONE);
//            }
            llWay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.contains(pos)) {
                        cb.setClickable(false);
                    } else {
                        list.clear();
                        list.add(pos);
                        cb.setChecked(true);
                    }
                    notifyDataSetChanged();

//                    if (onItemClick != null) {
//                        onItemClick.onItemClickListen(pos, data);
//                    }
                }
            });
        }
    }


}
