package com.procoin.module.circle;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.circle.entity.CircleMemberUser;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.circle.adapter.CircleMemberAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * 管理圈成员  圈成员列表 都是这个页面
 *
 * @author zhengmj
 */
public class CircleAllMembersActivity extends TJRBaseToolBarSwipeBackActivity {
    @BindView(R.id.fl_no_content)
    FrameLayout fl_no_content;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swiperefreshlayout;
    private CircleMemberAdapter circleMemberListAdapter;


    private String circleId;
    private String order = "asc";
    private int pageSize = 15;
    private int role = -1;
    private Group<CircleMemberUser> group;
    private Group<CircleMemberUser> searchResultGroup;

    private int pageNo = 1;

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview;
    }

    @Override
    protected String getActivityTitle() {
        return "所有成员";
    }

    public static void pageJumpThis(Context context, String circleId,int role) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CIRCLEID, circleId);
        bundle.putInt(CommonConst.ROLE, role);
        PageJumpUtil.pageJump(context, CircleAllMembersActivity.class, bundle);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            circleId = getIntent().getExtras().getString(CommonConst.CIRCLEID);
            role = getIntent().getExtras().getInt(CommonConst.ROLE, -1);
            if (TextUtils.isEmpty(circleId)) {
                CommonUtil.showmessage("参数错误", this);
                finish();
                return;
            }
        }
        ButterKnife.bind(this);
//        setContentView(R.layout.simple_recycleview_2);
//        if(type!=1){
//            pullToRefreshListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    CircleMemberUser item = (CircleMemberUser) parent.getItemAtPosition(position);
//                    if(type==2){
//                        if(item.userId==user.getUserId()){
//                            CommonUtil.showmessage("不能打赏给自己",ManagerCircleMembersActivity.this);
//                            return;
//                        }
//                        Intent intent=new Intent();
//                        intent.putExtra(CommonConst.USERNAME,item.name);
//                        intent.putExtra(CommonConst.USERID,item.userId);
//                        setResult(0x456, intent);
//                        PageJumpUtil.finishCurr(ManagerCircleMembersActivity.this);
//
//                    }else if(type==3){
//                        if(item.userId==user.getUserId()){
//                            CommonUtil.showmessage("不能@自己",ManagerCircleMembersActivity.this);
//                            return;
//                        }
//                        Intent intent=new Intent();
//                        intent.putExtra(CommonConst.USERNAME,item.name);
//                        intent.putExtra(CommonConst.USERID,item.userId);
//                        setResult(0x567, intent);
//                        PageJumpUtil.finishCurr(ManagerCircleMembersActivity.this);
//                    }else{
//                        if (role == -1) return;
//                        String[] values = null;
//                        int dialogType = 0;
//                        if (role == 5) {//管理员
//                            if (item.role == 0) {
//                                values = new String[]{"移除出圈子", "查看资料","取消"};
//                                dialogType = 0;
//                            }else {
//                                values = new String[]{ "查看资料","取消"};
//                                dialogType = 1;
//                            }
//                        } else if (role == 10) {
//                            if (item.role == 0) {
//                                values = new String[]{"移除出圈子", "设为管理员","查看资料", "取消"};
//                                dialogType = 2;
//                            } else if (item.role == 5) {
//                                values = new String[]{"移除出圈子", "取消管理员", "查看资料","取消"};
//                                dialogType = 3;
//                            }
//                        }else {
//                            values = new String[]{ "查看资料","取消"};
//                            dialogType = 1;
//                        }
//                        if (values != null) {
//                            new AlertDialog.Builder(ManagerCircleMembersActivity.this).setTitle("操作").setCancelable(true).setItems(values, new OnDialogItemClickListen(dialogType, item.userId, position - 1)).create().show();
//                        }
//                    }
//                }
//            });
//        }

        circleMemberListAdapter = new CircleMemberAdapter(this,circleId,role,getUserIdLong());
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.dividerColor)));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this));
        rvList.setAdapter(circleMemberListAdapter);
        rvList.setRecycleViewLoadMoreCallBack(callBack);
        circleMemberListAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swiperefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startGetMemberList(circleId);
            }
        });
        startGetMemberList(circleId);
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (circleMemberListAdapter != null && circleMemberListAdapter.getRealItemCount() > 0) {
                CircleMemberUser circleMemberUser = circleMemberListAdapter.getItem(circleMemberListAdapter.getRealItemCount() - 1);
                if (circleMemberUser == null) {
                    pageNo = 1;
                }
                startGetMemberList(circleId);
            }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.circle_manager_member_menu, menu);
        // Retrieve the SearchView and plug it into SearchManager
        final SearchView searchView = (SearchView) MenuItemCompat
                .getActionView(menu.findItem(R.id.action_search));
        searchView.setQueryHint("搜索成员");
        SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d("setOnQueryTextListener", "onQueryTextSubmit   s==" + s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d("setOnQueryTextListener", "onQueryTextChange   s==" + s);
                if (TextUtils.isEmpty(s)) {
                    circleMemberListAdapter.setGroup(group);
                } else {
                    searchResultGroup = searchUser(s);
                    circleMemberListAdapter.setGroup(searchResultGroup);
                }
                return false;
            }
        });

        return true;
    }

    public Group<CircleMemberUser> searchUser(String s) {
        Group<CircleMemberUser> searchResult = null;
        if (group != null && group.size() > 0) {
            searchResult = new Group<CircleMemberUser>();
            for (int i = 0; i < group.size(); i++) {
                CircleMemberUser user = group.get(i);
                if (user != null && user.userName.contains(s)) {
                    searchResult.add(user);
                }
            }
        }
        return searchResult;
    }



    Call<ResponseBody> getMemberListCall;

    private void startGetMemberList(String circleId) {
        com.procoin.http.util.CommonUtil.cancelCall(getMemberListCall);
        getMemberListCall = VHttpServiceManager.getInstance().getVService().getMemberList(circleId, pageNo);
        getMemberListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<CircleMemberUser>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            circleMemberListAdapter.setGroup(group);
                            mActionBar.setTitle("所有成员("+group.size()+")");//现在后台是全部返回的
                        } else {
                            circleMemberListAdapter.addItem(group);
                            circleMemberListAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                    swiperefreshlayout.setRefreshing(false);
                    if (circleMemberListAdapter.getRealItemCount() > 0) {
                        circleMemberListAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
                    }
                }
            }
            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swiperefreshlayout.setRefreshing(false);
                circleMemberListAdapter.onLoadComplete(false,false);
            }
        });
    }


}
