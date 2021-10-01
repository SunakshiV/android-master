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
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.entity.Position;
import com.procoin.module.home.trade.TradeLeverActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易未完成记录-杠杆
 * Created by zhengmj on 18-10-26.
 */

public class TradeUndoneLeverAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Position> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TradeUndoneLeverAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_undone_lever_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {

        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvBuySell)
        TextView tvBuySell;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvCancel)
        TextView tvCancel;
        @BindView(R.id.tvHand)
        TextView tvHand;
        @BindView(R.id.tvOpenPrice)
        TextView tvOpenPrice;
        @BindView(R.id.tvOpenBail)
        TextView tvOpenBail;
        @BindView(R.id.llItem)
        LinearLayout llItem;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Position data, final int pos) {
            tvSymbol.setText(CommonUtil.getOriginSymbol(data.symbol));
            tvBuySell.setText("•" + data.buySellValue);
            tvTime.setText(DateUtils.getStringDateOfString2(String.valueOf(data.openTime), DateUtils.TEMPLATE_yyyyMMdd_HHmm));
            tvHand.setText(data.openHand);
            tvOpenPrice.setText(data.price);
            tvOpenBail.setText(data.openBail);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showCancelTipsDialog(data, pos);
                }
            });


        }


    }

    private void showCancelTipsDialog(final Position data, final int pos) {
        cancelTipsDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                startOrderCancel(data, pos, "");


            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        cancelTipsDialog.setMessage("确定撤销订单");
        cancelTipsDialog.setBtnOkText("撤销");
        cancelTipsDialog.setTitleVisibility(View.GONE);
        cancelTipsDialog.show();
    }


    private void startOrderCancel(final Position data, final int pos, String payPass) {
        CommonUtil.cancelCall(tradeCancelCall);
        tradeCancelCall = VHttpServiceManager.getInstance().getVService().proOrderCancel(data.orderId, payPass);
        tradeCancelCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    removeItem(pos);
                    if (context instanceof TradeLeverActivity)//交易页面需要刷新checkout
                        ((TradeLeverActivity) context).onCancelOrder();
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startOrderCancel(data, pos, pwString);
            }


        });
    }

}
