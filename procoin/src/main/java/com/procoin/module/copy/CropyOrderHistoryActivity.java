package com.procoin.module.copy;


import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.LinearLayoutManager;
import android.view.View;
import android.widget.FrameLayout;

import com.procoin.common.entity.ResultData;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.copy.adapter.CopyOrderHistoryAdapter;
import com.procoin.module.copy.entity.CopyOrderHistory;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单记录
 */
public class CropyOrderHistoryActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;

    private CopyOrderHistoryAdapter copyOrderHistoryAdapter;

    private int pageSize = 15;
    private int pageNo = 1;

    private Call<ResponseBody> withdrawCoinListCall;
    private Group<CopyOrderHistory> group;


    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }

    @Override
    protected String getActivityTitle() {
        return "跟单记录";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        copyOrderHistoryAdapter = new CopyOrderHistoryAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
        rvList.setAdapter(copyOrderHistoryAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        copyOrderHistoryAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startGetCopyHistoryList();
            }
        });

        startGetCopyHistoryList();
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (copyOrderHistoryAdapter != null && copyOrderHistoryAdapter.getRealItemCount() > 0) {
                CopyOrderHistory copyOrderHistory = copyOrderHistoryAdapter.getItem(copyOrderHistoryAdapter.getRealItemCount() - 1);
                if (copyOrderHistory == null) {
                    pageNo = 1;
                }
                startGetCopyHistoryList();
            }
        }
    };

    private void startGetCopyHistoryList() {
        CommonUtil.cancelCall(withdrawCoinListCall);
        withdrawCoinListCall = VHttpServiceManager.getInstance().getVService().copyHistoryList(pageNo);
        withdrawCoinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<CopyOrderHistory>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            copyOrderHistoryAdapter.setGroup(group);
                        } else {
                            copyOrderHistoryAdapter.addItem(group);
                            copyOrderHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                    fl_no_content.setVisibility(copyOrderHistoryAdapter.getRealItemCount() > 0 ? View.GONE : View.VISIBLE);
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (copyOrderHistoryAdapter.getRealItemCount() > 0) {
                        copyOrderHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                copyOrderHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


}
