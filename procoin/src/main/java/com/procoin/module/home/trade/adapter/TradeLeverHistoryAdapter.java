package com.procoin.module.home.trade.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.entity.Position;
import com.procoin.module.wallet.LeverInfoActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易历史记录-杠杆
 * Created by zhengmj on 18-10-26.
 */

public class TradeLeverHistoryAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Position> {


    private Context context;
    private Call<ResponseBody> tradeCancelCall;

    private OnItemClick onItemClick;
    private TjrBaseDialog cancelTipsDialog;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public TradeLeverHistoryAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.trade_lever_history_item, parent, false));
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
        @BindView(R.id.ivArrow)
        ImageView ivArrow;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvHand)
        TextView tvHand;
        @BindView(R.id.tvOpenPrice)
        TextView tvOpenPrice;
        @BindView(R.id.tvOpenBail)
        TextView tvOpenBail;
        @BindView(R.id.tvOpenPriceText)
        TextView tvOpenPriceText;
        @BindView(R.id.tvOpenBailText)
        TextView tvOpenBailText;
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
            tvState.setText(data.nowStateDesc);

            if (data.closeState.equals("canceled")) {
                ivArrow.setVisibility(View.GONE);
                tvOpenPriceText.setText("委托价");
                tvOpenBailText.setText("开仓保证金");
                tvOpenPrice.setText(data.price);
                tvOpenBail.setText(data.openBail);
                llItem.setOnClickListener(null);
            } else {
                ivArrow.setVisibility(View.VISIBLE);
                tvOpenPriceText.setText("开仓价");
                tvOpenBailText.setText("盈利(USDT)");
                tvOpenPrice.setText(data.openPrice);
                tvOpenBail.setText(StockChartUtil.formatWithSign(data.profit));
                llItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LeverInfoActivity.pageJump(context, data.orderId);
                    }
                });
            }


        }


    }


}
