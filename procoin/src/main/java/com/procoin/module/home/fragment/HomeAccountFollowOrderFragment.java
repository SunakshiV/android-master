package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.module.home.adapter.HomeFollowAdapter;
import com.procoin.module.home.entity.HomeCopyOrder;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟单
 * Created by zhengmj on 19-3-8.
 */

public class HomeAccountFollowOrderFragment extends UserBaseFragment {
    @BindView(R.id.tvNoDataCopyOrder)
    TextView tvNoDataCopyOrder;
    @BindView(R.id.rvFollowList)
    RecyclerView rvFollowList;

    private HomeFollowAdapter homeFollowAdapter;


    public static HomeAccountFollowOrderFragment newInstance() {
        HomeAccountFollowOrderFragment fragment = new HomeAccountFollowOrderFragment();
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() == null) return;
        if (isVisibleToUser) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
        }
    }

    public void setData(Group<HomeCopyOrder> groupFollow) {
        if(rvFollowList==null)return;
        if (groupFollow != null && groupFollow.size() > 0) {
            rvFollowList.setVisibility(View.VISIBLE);
            tvNoDataCopyOrder.setVisibility(View.GONE);
            homeFollowAdapter.setGroup(groupFollow);
        } else {
            rvFollowList.setVisibility(View.GONE);
            tvNoDataCopyOrder.setVisibility(View.VISIBLE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_account_follow, container, false);
        ButterKnife.bind(this, view);

        homeFollowAdapter = new HomeFollowAdapter(getActivity());
        rvFollowList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvFollowList.addItemDecoration(simpleRecycleDivider);
        rvFollowList.setAdapter(homeFollowAdapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
