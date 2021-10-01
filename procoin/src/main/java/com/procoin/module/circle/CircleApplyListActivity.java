package com.procoin.module.circle;

import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.widget.FrameLayout;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.module.circle.adapter.CircleApplyListAdapter;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.module.circle.entity.CircleApply;
import com.procoin.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * 圈子申请列表
 *
 * @author zhengmj
 */
public class CircleApplyListActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private CircleApplyListAdapter circleApplyListAdapter;

    private String circleId;
    private int pageNo = 1;
    private int pageSize = 15;

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }

    @Override
    protected String getActivityTitle() {
        return "成员申请";
    }

    public static void pageJumpThis(Context context, String circleId) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CIRCLEID, circleId);
        PageJumpUtil.pageJump(context, CircleApplyListActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            circleId = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            if (TextUtils.isEmpty(circleId)) {
                CommonUtil.showmessage("参数错误", this);
                finish();
                return;
            }

        }
        ButterKnife.bind(this);
        circleApplyListAdapter = new CircleApplyListAdapter(this, getApplicationContext().getUser().getUserName());
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this));
        rvList.setAdapter(circleApplyListAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        circleApplyListAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startFindApplies(circleId);
            }
        });
        startFindApplies(circleId);
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (circleApplyListAdapter != null && circleApplyListAdapter.getRealItemCount() > 0) {
                CircleApply circleApply = circleApplyListAdapter.getItem(circleApplyListAdapter.getRealItemCount() - 1);
                if (circleApply == null) {
                    pageNo = 1;
                }
                startFindApplies(circleId);
            }
        }
    };


    Call<ResponseBody> findAppliesCall;

    private void startFindApplies(final String circleId) {
        com.procoin.http.util.CommonUtil.cancelCall(findAppliesCall);
        findAppliesCall = VHttpServiceManager.getInstance().getVService().findApplies(circleId, pageNo);
        findAppliesCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    Group<CircleApply> group = resultData.getGroup("data", new TypeToken<Group<CircleApply>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            circleApplyListAdapter.setGroup(group);
                        } else {
                            circleApplyListAdapter.addItem(group);
                            circleApplyListAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (circleApplyListAdapter.getRealItemCount() > 0) {
                        circleApplyListAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                    CircleSharedPreferences.saveApplyCount(CircleApplyListActivity.this,circleId,getUserIdLong(),0);//成员申请消息清零
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                circleApplyListAdapter.onLoadComplete(false, false);
            }
        });
    }

}
