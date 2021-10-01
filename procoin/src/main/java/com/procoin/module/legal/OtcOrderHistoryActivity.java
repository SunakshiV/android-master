package com.procoin.module.legal;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.legal.adapter.OtcOrderHistoryAdapter;
import com.procoin.module.legal.entity.OtcOrderHistory;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 历史记录
 */
public class OtcOrderHistoryActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.llParams)
    LinearLayout llParams;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvQuery)
    TextView tvQuery;
    @BindView(R.id.llSelectParamsAnim)
    LinearLayout llSelectParamsAnim;
    @BindView(R.id.hideSelectParams)
    View hideSelectParams;
    @BindView(R.id.llSelectParams)
    LinearLayout llSelectParams;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvAlreadyTrade)
    TextView tvAlreadyTrade;
    @BindView(R.id.tvNotPay)
    TextView tvNotPay;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;

    private OtcOrderHistoryAdapter orderHistoryAdapter;
    private Group<OtcOrderHistory> group;
    //    private Group<AccountType> groupType;
    private int pageSize = 15;
    private int pageNo = 1;


    private Call<ResponseBody> queryTransferListCall;
    private Call<ResponseBody> listAccountTypeCall;


    private String buySell = "";
//    private String state = "";

    @Override
    protected int setLayoutId() {
        return R.layout.otc_order_his;
    }

    @Override
    protected String getActivityTitle() {
        return "订单记录";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        llParams.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        hideSelectParams.setOnClickListener(this);

        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);

        tvAlreadyTrade.setOnClickListener(this);
        tvNotPay.setOnClickListener(this);
        tvCancel.setOnClickListener(this);


        orderHistoryAdapter = new OtcOrderHistoryAdapter(this, getUserIdLong());
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
        rvList.setAdapter(orderHistoryAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        orderHistoryAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startOtcFindOrderList();
            }
        });

        startOtcFindOrderList();


    }


    @Override
    protected void onResume() {
        super.onResume();
        if (orderHistoryAdapter != null && orderHistoryAdapter.getRealItemCount() > 0) {
            orderHistoryAdapter.notifyDataSetChanged();
        }
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (orderHistoryAdapter != null && orderHistoryAdapter.getRealItemCount() > 0) {
                OtcOrderHistory otcOrderHistory = orderHistoryAdapter.getItem(orderHistoryAdapter.getRealItemCount() - 1);
                if (otcOrderHistory == null) {
                    pageNo = 1;
                }
                startOtcFindOrderList();
            }
        }
    };

    private void startOtcFindOrderList() {
        CommonUtil.cancelCall(queryTransferListCall);
        queryTransferListCall = VHttpServiceManager.getInstance().getVService().otcFindOrderList(buySell, buySell, pageNo);
        queryTransferListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<OtcOrderHistory>>() {
                    }.getType());
//                    if (group != null && group.size() > 0) {
                    if (pageNo == 1) {
                        orderHistoryAdapter.setGroup(group);
                        llNodata.setVisibility((group!=null&&group.size()>0)?View.GONE:View.VISIBLE);
                    } else {
                        orderHistoryAdapter.addItem(group);
                        orderHistoryAdapter.notifyDataSetChanged();
                    }
//                    }
//                    fl_no_content.setVisibility(transferCoinHistoryAdapter.getRealItemCount() > 0 ? View.GONE : View.VISIBLE);
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (orderHistoryAdapter.getRealItemCount() > 0) {
                        orderHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                orderHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


//    public void startListAccountTypeCall() {
//        com.procoin.util.CommonUtil.cancelCall(listAccountTypeCall);
//        listAccountTypeCall = VHttpServiceManager.getInstance().getVService().listAccountType();
//        listAccountTypeCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    groupType = resultData.getGroup("accountTypeList", new TypeToken<Group<AccountType>>() {
//                    }.getType());
//                    showParams();
//                }
//            }
//        });
//    }

    private void showParams() {
        if (llSelectParams.getVisibility() == View.INVISIBLE || llSelectParams.getVisibility() == View.GONE) {
            showSelectParams();
        } else {
            hideSelectParams();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.llParams:
//                if (groupType == null || groupType.size() == 0) {
//                    startListAccountTypeCall();
//                    return;
//                }
                showParams();
                break;
            case R.id.tvReset:
                hideSelectParams();
                reset();
                pageNo = 1;
                startOtcFindOrderList();
                break;
            case R.id.tvQuery:
                hideSelectParams();
                pageNo = 1;
                startOtcFindOrderList();
                break;
            case R.id.hideSelectParams:
                hideSelectParams();
                break;
            case R.id.tvBuy:
                tvBuy.setSelected(true);
                tvSell.setSelected(false);
                buySell = "buy";
                break;
            case R.id.tvSell:
                tvBuy.setSelected(false);
                tvSell.setSelected(true);
                buySell = "sell";
                break;
            case R.id.tvAlreadyTrade:
                tvAlreadyTrade.setSelected(true);
                tvNotPay.setSelected(false);
                tvCancel.setSelected(false);
                break;
            case R.id.tvNotPay:
                tvAlreadyTrade.setSelected(false);
                tvNotPay.setSelected(true);
                tvCancel.setSelected(false);
                break;
            case R.id.tvCancel:
                tvAlreadyTrade.setSelected(false);
                tvNotPay.setSelected(false);
                tvCancel.setSelected(true);
                break;


        }
    }


    private void reset() {
        tvBuy.setSelected(false);
        tvSell.setSelected(false);
        buySell = "";
//        state = "";
    }


    private ObjectAnimator objectAnimatorShow;
    private ObjectAnimator objectAnimatorShowArrow;
    private ObjectAnimator objectAnimatorHide;
    private ObjectAnimator objectAnimatorHideArrow;
    private FastOutSlowInInterpolator fastOutSlowInInterpolator;


    private void showSelectParams() {
        llSelectParams.setVisibility(View.VISIBLE);

        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorShow == null) {
            objectAnimatorShow = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", -llSelectParamsAnim.getHeight(), 0);
            objectAnimatorShow.setDuration(300);
            objectAnimatorShow.setInterpolator(fastOutSlowInInterpolator);
        }

        if (objectAnimatorShowArrow == null) {
            objectAnimatorShowArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", 0.0f, -45.0f, -90.0f, -180.0f);
            objectAnimatorShowArrow.setDuration(300);
            objectAnimatorShowArrow.setInterpolator(fastOutSlowInInterpolator);
        }
        objectAnimatorShow.start();
        objectAnimatorShowArrow.start();

    }

    private void hideSelectParams() {
        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorHide == null) {
            objectAnimatorHide = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", 0, -llSelectParamsAnim.getHeight());
            objectAnimatorHide.setDuration(300);
            objectAnimatorHide.setInterpolator(fastOutSlowInInterpolator);
            objectAnimatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    llSelectParams.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


        if (objectAnimatorHideArrow == null) {
            objectAnimatorHideArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", -180.0f, -90.0f, -45.0f, 0.0f);
            objectAnimatorHideArrow.setDuration(300);
            objectAnimatorHideArrow.setInterpolator(fastOutSlowInInterpolator);

        }
        objectAnimatorHide.start();
        objectAnimatorHideArrow.start();
    }

}
