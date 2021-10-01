package com.procoin.module.home.trade.history;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
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
import com.procoin.module.home.trade.adapter.AccountTypeFilterAdapter;
import com.procoin.module.home.trade.adapter.TransferCoinHistoryAdapter;
import com.procoin.module.home.trade.entity.AccountType;
import com.procoin.module.home.trade.entity.TransferHistory;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.widgets.SimpleSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;

/**
 * 划转记录
 */
public class TransferCoinHistoryActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.llParams)
    LinearLayout llParams;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.rvTypeFrom)
    RecyclerView rvTypeFrom;
    @BindView(R.id.rvTypeTo)
    RecyclerView rvTypeTo;
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

    private TransferCoinHistoryAdapter transferCoinHistoryAdapter;
    private Group<TransferHistory> group;
    private Group<AccountType> groupType;
    private int pageSize = 15;
    private int pageNo = 1;


    private Call<ResponseBody> queryTransferListCall;
    private Call<ResponseBody> listAccountTypeCall;

    private AccountTypeFilterAdapter accountTypeFilterAdapterFrom;
    private AccountTypeFilterAdapter accountTypeFilterAdapterTo;

    private String fromAccountType = "";
    private String toAccountType = "";

    @Override
    protected int setLayoutId() {
        return R.layout.coin_transfer_his;
    }

    @Override
    protected String getActivityTitle() {
        return "划转记录";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        llParams.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        hideSelectParams.setOnClickListener(this);

        transferCoinHistoryAdapter = new TransferCoinHistoryAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
        rvList.setAdapter(transferCoinHistoryAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        transferCoinHistoryAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startQueryTransferList();
            }
        });
        accountTypeFilterAdapterFrom = new AccountTypeFilterAdapter(this);
        rvTypeFrom.setLayoutManager(new GridLayoutManager(this, 3));
        rvTypeFrom.addItemDecoration(new SimpleSpaceItemDecoration(this, 0, 10, 5, 5));
        rvTypeFrom.setAdapter(accountTypeFilterAdapterFrom);
        accountTypeFilterAdapterFrom.setOnItemclickListen(new AccountTypeFilterAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String accountType) {
                TransferCoinHistoryActivity.this.fromAccountType = accountType;
            }
        });

        accountTypeFilterAdapterTo = new AccountTypeFilterAdapter(this);
        rvTypeTo.setLayoutManager(new GridLayoutManager(this, 3));
        rvTypeTo.addItemDecoration(new SimpleSpaceItemDecoration(this, 0, 10, 5, 5));
        rvTypeTo.setAdapter(accountTypeFilterAdapterTo);
        accountTypeFilterAdapterTo.setOnItemclickListen(new AccountTypeFilterAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String accountType) {
                TransferCoinHistoryActivity.this.toAccountType = accountType;
            }
        });


        startQueryTransferList();


    }


    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (transferCoinHistoryAdapter != null && transferCoinHistoryAdapter.getRealItemCount() > 0) {
                TransferHistory transferHistory = transferCoinHistoryAdapter.getItem(transferCoinHistoryAdapter.getRealItemCount() - 1);
                if (transferHistory == null) {
                    pageNo = 1;
                }
                startQueryTransferList();
            }
        }
    };

    private void startQueryTransferList() {
        CommonUtil.cancelCall(queryTransferListCall);
        queryTransferListCall = VHttpServiceManager.getInstance().getVService().queryTransferList(fromAccountType, toAccountType, pageNo);
        queryTransferListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<TransferHistory>>() {
                    }.getType());
//                    if (group != null && group.size() > 0) {
                    if (pageNo == 1) {
                        transferCoinHistoryAdapter.setGroup(group);
                    } else {
                        transferCoinHistoryAdapter.addItem(group);
                        transferCoinHistoryAdapter.notifyDataSetChanged();
                    }
//                    }
//                    fl_no_content.setVisibility(transferCoinHistoryAdapter.getRealItemCount() > 0 ? View.GONE : View.VISIBLE);
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (transferCoinHistoryAdapter.getRealItemCount() > 0) {
                        transferCoinHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                transferCoinHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


    public void startListAccountTypeCall() {
        com.procoin.util.CommonUtil.cancelCall(listAccountTypeCall);
        listAccountTypeCall = VHttpServiceManager.getInstance().getVService().listAccountType();
        listAccountTypeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    groupType = resultData.getGroup("accountTypeList", new TypeToken<Group<AccountType>>() {
                    }.getType());
                    showParams();
                    accountTypeFilterAdapterFrom.setData(groupType);
                    accountTypeFilterAdapterTo.setData(groupType);
                }
            }
        });
    }

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
                if (groupType == null || groupType.size() == 0) {
                    startListAccountTypeCall();
                    return;
                }
                showParams();
                break;
            case R.id.tvReset:
                hideSelectParams();
                reset();
                pageNo = 1;
                startQueryTransferList();
                break;
            case R.id.tvQuery:
                hideSelectParams();
                pageNo = 1;
                startQueryTransferList();
                break;
            case R.id.hideSelectParams:
                hideSelectParams();
                break;


        }
    }

    private void reset() {
        fromAccountType = "";
        toAccountType = "";
        accountTypeFilterAdapterTo.reset();
        accountTypeFilterAdapterFrom.reset();
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
