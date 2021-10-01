package com.procoin.module.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.SearchActivity;
import com.procoin.module.home.adapter.HomeAttentionAdapter;
import com.procoin.module.home.entity.Attention;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 关注
 * Created by zhengmj on 19-3-8.
 */

public class HomeAttentionFragment extends UserBaseImmersionBarFragment {
    @BindView(R.id.ll_bar)
    LinearLayout ll_bar;
//    @BindView(R.id.ivSearch)
//    LinearLayout ivSearch;

    @BindView(R.id.llText)
    LinearLayout llText;
    @BindView(R.id.llSearch)
    LinearLayout llSearch;

    @BindView(R.id.rvAttentionList)
    RecyclerView rvAttentionList;
//    @BindView(R.id.tvGradeText)
//    TextView tvGradeText;
    @BindView(R.id.llAttentionNodata)
    LinearLayout llNodata;
    private HomeAttentionAdapter homeAttentionAdapter;

    private Call<ResponseBody> followCall;

    public static HomeAttentionFragment newInstance() {
        HomeAttentionFragment fragment = new HomeAttentionFragment();
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getActivity() == null) return;
        if (isVisibleToUser) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetFollow();
                }
            }, 500);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            startGetFollow();
        }
    }

    private void startGetFollow() {
        CommonUtil.cancelCall(followCall);
        followCall = VHttpServiceManager.getInstance().getVService().follow();
        followCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Attention> group = resultData.getGroup(new TypeToken<Group<Attention>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        rvAttentionList.setVisibility(View.VISIBLE);
                        llText.setVisibility(View.VISIBLE);
                        llNodata.setVisibility(View.GONE);
                        homeAttentionAdapter.setGroup(group);
                    } else {
                        rvAttentionList.setVisibility(View.GONE);
                        llText.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    public void immersionbar() {
        if (mImmersionBar != null && ll_bar != null) {
            mImmersionBar
                    .titleBar(ll_bar)
                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
                    .navigationBarColor(R.color.white)
                    .init();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_attention, container, false);
        ButterKnife.bind(this, view);

        homeAttentionAdapter = new HomeAttentionAdapter(getActivity());
        rvAttentionList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvAttentionList.addItemDecoration(new SimpleRecycleDivider(getActivity(), true));
        rvAttentionList.setAdapter(homeAttentionAdapter);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.pageJump(getActivity(), SearchActivity.class);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).navigationBarColor(R.color.white).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
