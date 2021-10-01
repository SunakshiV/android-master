package com.procoin.module.legal.adapter;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.legal.AddAdActivity;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.module.myhome.entity.AddPaymentTern;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 自选列表
 * Created by zhengmj on 18-10-26.
 */

public class OptionalListAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<OptionalOrder> {


    private Context context;

    private OnItemClick onItemClick;
    private String buySell = "buy";//买卖方向
    private boolean isMyAd;//我的广告页面
    private Gson gson = new Gson();


    public OptionalListAdapter(Context context, OnItemClick onItemClick, String buySell, boolean isMyAd) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
        this.onItemClick = onItemClick;
        this.buySell = buySell;
        this.isMyAd = isMyAd;
    }


    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.optional_list_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.ivHead)
        CircleImageView ivHead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvState)
        TextView tvState;
        @BindView(R.id.tvOrderCount)
        TextView tvOrderCount;
        @BindView(R.id.tvRate)
        TextView tvRate;
        @BindView(R.id.tvNum)
        TextView tvNum;
        @BindView(R.id.tvLimit)
        TextView tvLimit;
        @BindView(R.id.tvPrice)
        TextView tvPrice;
        @BindView(R.id.tvWay)
        RecyclerView tvWay;
        @BindView(R.id.tvBuySell)
        TextView tvBuySell;
        @BindView(R.id.tvType)
        TextView tvType;
        @BindView(R.id.tvDelete)
        TextView tvDelete;
        @BindView(R.id.tvOnLine)
        TextView tvOnLine;
        @BindView(R.id.tvEdit)
        TextView tvEdit;
        @BindView(R.id.llEdit)
        LinearLayout llEdit;


        PayWayAdapter payWayAdapter;

        MyOnclick myOnclick;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final OptionalOrder data, final int pos) {
            if (isMyAd) {
                tvState.setVisibility(View.VISIBLE);
                tvBuySell.setVisibility(View.GONE);
                llEdit.setVisibility(View.VISIBLE);
                if ("buy".equals(data.buySell)) {
                    tvType.setText("类型  " + "购买");
                } else {
                    tvType.setText("类型  " + "出售");
                }
                if (data.isOnline == 0) {
                    tvOnLine.setText("上架");
                    tvState.setText("已下架");
                    tvState.setBackgroundResource(R.drawable.shape_offline);
                } else {
                    tvOnLine.setText("下架");
                    if ("buy".equals(data.buySell)) {
                        tvState.setText("购买中");
                        tvState.setBackgroundResource(R.drawable.shape_online_buy);
                    } else {
                        tvState.setText("出售中");
                        tvState.setBackgroundResource(R.drawable.shape_online_sell);
                    }
                }
            } else {
                tvState.setVisibility(View.GONE);
                tvBuySell.setVisibility(View.VISIBLE);
                llEdit.setVisibility(View.GONE);
                if ("buy".equals(buySell)) {
                    tvBuySell.setText("购买");
                } else {
                    tvBuySell.setText("出售");
                }
            }
            tvName.setText(data.userName);
            displayImage(data.userLogo, ivHead);
            tvOrderCount.setText(String.valueOf(data.orderNum));
            tvRate.setText(data.limitRate + "%");

            tvNum.setText("数量  " + data.amount + " USDT");
            tvLimit.setText("限额  ¥" + data.minCny + "-" + "¥" + data.maxCny);
            tvPrice.setText("¥" + data.price);

            payWayAdapter = new PayWayAdapter(context);
            tvWay.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
//            tvWay.addItemDecoration(new SimpleSpaceItemDecoration(context, 0, 5, 0, 0));
            tvWay.setAdapter(payWayAdapter);
            final Group<AddPaymentTern> groupMarket = gson.fromJson(data.payWay, new TypeToken<Group<AddPaymentTern>>() {
            }.getType());
            payWayAdapter.setGroup(groupMarket);

            myOnclick = new MyOnclick(pos, data);
            tvBuySell.setOnClickListener(myOnclick);
            tvDelete.setOnClickListener(myOnclick);
            tvOnLine.setOnClickListener(myOnclick);
            tvEdit.setOnClickListener(myOnclick);
        }
    }

    TjrBaseDialog delDialog;

    private void showDelDialog(final OptionalOrder entity, final int pos) {
        delDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                startOtcDelMyAd(entity, pos, "");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delDialog.setMessage("确定删除");
        delDialog.setBtnOkText("删除");
        delDialog.setBtnColseText("取消");
        delDialog.setTitleVisibility(View.GONE);
        delDialog.show();
    }

    Call<ResponseBody> otcDelMyAdCall;

    private void startOtcDelMyAd(final OptionalOrder optionalOrder, final int pos, String payPass) {
        CommonUtil.cancelCall(otcDelMyAdCall);
        otcDelMyAdCall = VHttpServiceManager.getInstance().getVService().otcDelMyAd(optionalOrder.adId);
        otcDelMyAdCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    removeItem(pos);
                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startOtcDelMyAd(optionalOrder, pos, pwString);
            }

        });
    }


    TjrBaseDialog onLineDialog;

    private void showOnLineDialog(final OptionalOrder entity) {
        onLineDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                startOtcSetOnline(entity, entity.isOnline == 0 ? 1 : 0, "");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        onLineDialog.setMessage(entity.isOnline == 0 ? "是否确定上架?" : "是否确定下架?");
        onLineDialog.setBtnOkText("确定");
        onLineDialog.setBtnColseText("取消");
        onLineDialog.setTitleVisibility(View.GONE);
        onLineDialog.show();
    }

    private Call<ResponseBody> otcSetOnlineCall;

    private void startOtcSetOnline(final OptionalOrder optionalOrder, final int online, String payPass) {
        CommonUtil.cancelCall(otcSetOnlineCall);
        otcSetOnlineCall = VHttpServiceManager.getInstance().getVService().otcSetOnline(optionalOrder.adId, online);
        otcSetOnlineCall.enqueue(new MyCallBack(context) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, context);
                    optionalOrder.isOnline = online;
                    notifyDataSetChanged();

                }
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startOtcDelMyAd(optionalOrder, online, pwString);
            }

        });
    }

    private class MyOnclick implements View.OnClickListener {
        int pos;
        OptionalOrder optionalOrder;

        public MyOnclick(int pos, OptionalOrder optionalOrder) {
            this.pos = pos;
            this.optionalOrder = optionalOrder;
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tvBuySell:
                    if (onItemClick != null) {
                        onItemClick.onItemClickListen(pos, optionalOrder);
                    }
                    break;
                case R.id.tvDelete:
                    showDelDialog(optionalOrder, pos);
                    break;
                case R.id.tvOnLine:
                    showOnLineDialog(optionalOrder);
                    break;
                case R.id.tvEdit:
                    AddAdActivity.pageJump(context, optionalOrder.adId);
                    break;
            }
        }
    }

}
