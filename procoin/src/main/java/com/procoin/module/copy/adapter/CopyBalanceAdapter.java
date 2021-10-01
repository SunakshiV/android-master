package com.procoin.module.copy.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.copy.entity.CopyBalance;
import com.procoin.util.StockChartUtil;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟单资金
 * Created by zhengmj on 18-10-26.
 */

public class CopyBalanceAdapter extends BaseImageLoaderRecycleAdapter<CopyBalance> {


    private Context context;

    public CopyBalanceAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }

//    class MyComparator implements Comparator<CopyBalance> {
//        private int sortType = 0;
//
//        public MyComparator(int sortType) {
//            this.sortType = sortType;
//
//        }
//
//        @Override
//        public int compare(CopyBalance o1, CopyBalance o2) {
//            return 0;
//        }
//    }

    private int sortType = 0;
    Comparator comparator = new Comparator<CopyBalance>() {
        @Override
        public int compare(CopyBalance lhs, CopyBalance rhs) {
            double result = 0;
            double num;
            double num2;
            if (sortType == 0) {
                num = lhs.totalBalance;
                num2 = rhs.totalBalance;
                result = num2 - num;
            } else if (sortType == 1) {
                num = lhs.totalBalance;
                num2 = rhs.totalBalance;
                result = num - num2;
            } else if (sortType == -1) {
                num = lhs.totalBalance;
                num2 = rhs.totalBalance;
                result = num2 - num;
            } else if (sortType == 2) {
                num = lhs.usableBalance;
                num2 = rhs.usableBalance;
                result = num - num2;
            } else if (sortType == -2) {
                num = lhs.usableBalance;
                num2 = rhs.usableBalance;
                result = num2 - num;
            } else if (sortType == 3) {
                num = lhs.nextUsableBalance;
                num2 = rhs.nextUsableBalance;
                result = num - num2;
            } else if (sortType == -3) {
                num = lhs.nextUsableBalance;
                num2 = rhs.nextUsableBalance;
                result = num2 - num;
            } else if (sortType == 4) {
                num = lhs.profit;
                num2 = rhs.profit;
                result = num - num2;
            } else if (sortType == -4) {
                num = lhs.profit;
                num2 = rhs.profit;
                result = num2 - num;
            }
            if (result == 0) {
                return 0;
            } else if (result > 0) {
                return 1;
            } else {
                return -1;
            }

        }
    };


    public void notifyDataSetChangedWithComparator(int sortType) {
        this.sortType = sortType;
        if (getItemCount() > 1) {
            Collections.sort(group, comparator);
        }
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.copy_balance_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUserName)
        TextView tvUserName;
        @BindView(R.id.tvTolCost)
        TextView tvTolCost;
        @BindView(R.id.tvEnableBalance)
        TextView tvEnableBalance;
        @BindView(R.id.tvNextBalance)
        TextView tvNextBalance;
        @BindView(R.id.tvProfit)
        TextView tvProfit;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CopyBalance data) {
            tvUserName.setText(data.userName);
            tvTolCost.setText(String.valueOf(data.totalBalance));
            tvEnableBalance.setText(String.valueOf(data.usableBalance));
            tvNextBalance.setText(String.valueOf(data.nextUsableBalance));
            tvProfit.setText(StockChartUtil.formatWithSign(data.profit));
        }
    }
}
