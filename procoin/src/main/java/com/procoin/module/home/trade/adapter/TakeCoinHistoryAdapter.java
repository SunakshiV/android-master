package com.procoin.module.home.trade.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.trade.entity.TakeCoinHistory;
import com.procoin.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 提币历史记录
 * Created by zhengmj on 18-10-26.
 */

public class TakeCoinHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<TakeCoinHistory> {


    private Context context;
    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TakeCoinHistoryAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.take_coin_history_item2, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.tvInOut)
        TextView tvInOut;
        @BindView(R.id.tvAmount)
        TextView tvAmount;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final TakeCoinHistory data, final int pos) {

            if (data.inOut == 1) {
                tvInOut.setText("充币");
            } else {
                tvInOut.setText("提币");
            }
            tvState.setText(data.stateDesc);
            tvAmount.setText(data.amount);
            tvTime.setText(DateUtils.getStringDateOfString2(data.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClick != null) onItemClick.onItemClickListen(pos, data);
                }
            });
//            if (data.state == 0) {
//                tvCancel.setVisibility(View.VISIBLE);
//                tvCancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showCancelTipsDialog(data, pos);
//                    }
//                });
//            } else {
//                tvCancel.setVisibility(View.GONE);
//            }

        }
    }
//
//    private void showCancelTipsDialog(final TakeCoinHistory data, final int pos) {
//        cancelTipsDialog = new TjrBaseDialog(context) {
//            @Override
//            public void onclickOk() {
//                dismiss();
//                startWithdrawCoinCancel(data, pos);
//            }
//
//            @Override
//            public void onclickClose() {
//                dismiss();
//            }
//
//            @Override
//            public void setDownProgress(int progress) {
//
//            }
//        };
//        cancelTipsDialog.setMessage("确定撤销订单");
//        cancelTipsDialog.setBtnOkText("撤销");
//        cancelTipsDialog.setTitleVisibility(View.GONE);
//        cancelTipsDialog.show();
//    }


//    private void startWithdrawCoinCancel(final TakeCoinHistory data, final int pos) {
//        CommonUtil.cancelCall(tradeCancelCall);
//        tradeCancelCall = VHttpServiceManager.getInstance().getVService().withdrawCoinCancel(data.dwId);
//        tradeCancelCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, context);
//                    data.state = OrderStateEnum.canceled.getState();
//                    notifyItemChanged(pos);
////                    removeItem(pos);
//                }
//            }
//
//        });
//    }
}
