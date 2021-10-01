package com.procoin.module.legal.adapter;

import android.content.Context;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.http.base.Group;
import com.procoin.module.myhome.AddReceiptTermActivity;
import com.procoin.module.myhome.entity.Receipt;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 可以多选
 * Created by zhengmj on 18-10-26.
 */

public class MultiSelectQuickPayWayAdapter extends BaseImageLoaderRecycleAdapter<Receipt> {


    private Context context;
    private Set<Long> selectSet = new HashSet<>();


    public String getSelectSet() {
        if (selectSet.size() == 0) {
            return "";
        } else {
            StringBuilder sb = null;
            for (Long aLong : selectSet) {
                if (sb == null) {
                    sb = new StringBuilder(String.valueOf(aLong));
                } else {
                    sb.append(",");
                    sb.append(String.valueOf(aLong));
                }

            }
            return sb.toString();
        }
    }


    @Override
    public void setGroup(Group<Receipt> g) {
//        if (g != null && g.size() > 0) {
//            selectSet.add(g.get(0).paymentId);
//        }
        super.setGroup(g);


    }


    @Override
    public int getItemCount() {
        int size = group == null ? 0 : group.size();
        if (size > 2) {
            return size;
        } else {
            return size + 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int size = group == null ? 0 : group.size();
        if (size == 0) {
            return 1;
        } else if (size > 2) {
            return 0;
        } else {
            if (position == size) {
                return 1;
            } else {
                return 0;
            }
        }

    }

    public void addSelectPayway(long receiptId) {
        selectSet.add(receiptId);

    }

    public MultiSelectQuickPayWayAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 0) {
            return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.select_pay_way_item, parent, false));
        } else {
            return new ViewHolderAdd(LayoutInflater.from(context).inflate(R.layout.select_way_pay_add_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        if (type == 0) {
            ViewHolder holder1 = (ViewHolder) holder;
            holder1.setData(getItem(position));
        } else {
            ViewHolderAdd viewHolderAdd = (ViewHolderAdd) holder;
            viewHolderAdd.flAddPayMent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddReceiptTermActivity.pageJump(context, 1);
                }
            });
        }
    }

    public class ViewHolderAdd extends RecyclerView.ViewHolder {
        @BindView(R.id.flAddPayMent)
        FrameLayout flAddPayMent;

        public ViewHolderAdd(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

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

        public void setData(final Receipt payWay) {
            tvName.setText(payWay.receiptTypeValue);
            tvRecommend.setVisibility(View.GONE);
            tvBestPrice.setVisibility(View.GONE);
            displayImage(payWay.receiptLogo, ivImg);

            if (selectSet.contains(payWay.paymentId)) {
                llItem.setSelected(true);
                ivSelected.setVisibility(View.VISIBLE);
            } else {
                llItem.setSelected(false);
                ivSelected.setVisibility(View.INVISIBLE);
            }
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    setSelected(payWay);
                    if (selectSet.contains(payWay.paymentId)) {
                        selectSet.remove(payWay.paymentId);
                    } else {
                        selectSet.add(payWay.paymentId);
                    }
                    notifyDataSetChanged();
                }

            });
        }
    }


}
