package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.module.home.MarketActivity;
import com.procoin.module.home.entity.YYBPool;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.piechart.CardHolderChartView2;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.kbt.adapter.KbtNoticeAdapter;
import com.procoin.module.kbt.entity.KbtNotice;
import com.procoin.module.kbt.entity.KbtTrend;
import com.procoin.module.login.LoginActivity;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * YYB回购
 * Created by zhengmj on 19-3-8.
 */

public class HomeYYBFragment extends UserBaseFragment implements View.OnClickListener {

    //    @BindView(R.id.tvTime)
//    TextView tvTime;
    @BindView(R.id.tvKbtRules)
    TextView tvKbtRules;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPriceCash)
    TextView tvPriceCash;
    @BindView(R.id.tvProduceAmount)
    TextView tvProduceAmount;
    @BindView(R.id.tvDestroyAmount)
    TextView tvDestroyAmount;
    @BindView(R.id.tvHistory)
    TextView tvHistory;
    @BindView(R.id.cardHolderChartView)
    CardHolderChartView2 cardHolderChartView;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.tvBuyBack)
    TextView tvBuyBack;
    @BindView(R.id.tvAssignment)
    TextView tvAssignment;


    private String repoAmount;//剩余可回购KBT数量
    private String holdAmount;//持有KBT数量

    private String yybDescribe = "";//kbt说明

    private int pageNo = 1;
    private int pageSize = 15;


    private Call<ResponseBody> getKbtRepoHomeCall;
    private Call<ResponseBody> getKbtNoticeCall;
    private Call<ResponseBody> kbtRepoCall;

    private KbtNoticeAdapter kbtNoticeAdapter;


    public static HomeYYBFragment newInstance() {
        HomeYYBFragment fragment = new HomeYYBFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "HomeCoinSubBuyFragment onResume   getUserVisibleHint==" + getUserVisibleHint() + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
        } else {
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeMarkAndKbtFragment", "HomeCoinSubBuyFragment setUserVisibleHint   isVisibleToUser==" + isVisibleToUser + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetKbtRepoHome();
            pageNo = 1;
            startGetKbtNoticeCall();
        } else {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.yyb_buy_back, container, false);
        ButterKnife.bind(this, view);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity(), 15, 15));
        kbtNoticeAdapter = new KbtNoticeAdapter(getActivity());
        rvList.setAdapter(kbtNoticeAdapter);
        kbtNoticeAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        rvList.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        tvBuyBack.setOnClickListener(this);
        tvAssignment.setOnClickListener(this);
        tvKbtRules.setOnClickListener(this);
//        startGetKbtRepoHome();
//        startGetKbtNoticeCall();
        return view;
    }


    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (kbtNoticeAdapter != null && kbtNoticeAdapter.getRealItemCount() > 0) {
                startGetKbtNoticeCall();
            } else {
                pageNo = 1;
                startGetKbtNoticeCall();
            }
        }
    };


    private void startGetKbtNoticeCall() {
        CommonUtil.cancelCall(getKbtNoticeCall);
        getKbtNoticeCall = VHttpServiceManager.getInstance().getVService().getYYBNotice(pageNo);
        getKbtNoticeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<KbtNotice> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<KbtNotice>>() {
                    }.getType());
                    Log.d("HomeCoinSubBuyFragment", "group==" + (group == null ? "null" : group.size()));
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            kbtNoticeAdapter.setGroup(group);
                        } else {
                            kbtNoticeAdapter.addItem(group);
                            kbtNoticeAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                kbtNoticeAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                kbtNoticeAdapter.onLoadComplete(false, false);
            }
        });
    }

    private void startGetKbtRepoHome() {
        CommonUtil.cancelCall(getKbtRepoHomeCall);
        getKbtRepoHomeCall = VHttpServiceManager.getInstance().getVService().yybRepoHome();
        getKbtRepoHomeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    repoAmount = resultData.getItem("repoAmount", String.class);
                    holdAmount = resultData.getItem("holdAmount", String.class);
                    yybDescribe = resultData.getItem("yybDescribe", String.class);

                    Group<KbtTrend> group = resultData.getGroup("yybTrendList", new TypeToken<Group<KbtTrend>>() {
                    }.getType());
                    YYBPool yybPool = resultData.getObject("yybPool", YYBPool.class);

                    if (group != null && group.size() > 0) {
                        cardHolderChartView.setDataForKbt(group);
                    }

                    if (yybPool != null) {
                        tvPrice.setText(yybPool.repoPrice);
                        tvPriceCash.setText(yybPool.repoPriceCny);
                        tvProduceAmount.setText(yybPool.produceAmount);
                        tvDestroyAmount.setText(yybPool.destroyAmount);
//                        tvTime.setText("回购期：" + DateUtils.getStringDateOfString2(kbtPool.fromTime, DateUtils.TEMPLATE_MM_dd2) + "-" + DateUtils.getStringDateOfString2(kbtPool.toTime, DateUtils.TEMPLATE_MM_dd2));
                    }


                }
            }
        });
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void startKbtRepoCall(String amount) {
        CommonUtil.cancelCall(kbtRepoCall);
        ((TJRBaseToolBarActivity) getActivity()).showProgressDialog();
        kbtRepoCall = VHttpServiceManager.getInstance().getVService().yybRepo(amount);
        kbtRepoCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    com.procoin.util.CommonUtil.showmessage(resultData.msg, getActivity());
                    if (yybBuyBackDialogFragment != null) {
                        yybBuyBackDialogFragment.dismiss();
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
            }
        });
    }

    YYBBuyBackDialogFragment yybBuyBackDialogFragment;

    private void showYYBBuyBackDialogFragment() {
        yybBuyBackDialogFragment = YYBBuyBackDialogFragment.newInstance(repoAmount, holdAmount);
        yybBuyBackDialogFragment.setOnShareDialogCallBack(new YYBBuyBackDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                yybBuyBackDialogFragment.dismiss();
            }

            @Override
            public void onSubmit(double kbt) {
                startKbtRepoCall(String.valueOf(kbt));
            }
        });
        yybBuyBackDialogFragment.showDialog(getChildFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyBack:
                if (((TJRBaseToolBarActivity) getActivity()).isLogin()) {
                    showYYBBuyBackDialogFragment();
                } else {
                    LoginActivity.login((TJRBaseToolBarActivity) getActivity());
                }
                break;
            case R.id.tvKbtRules:
                if (!TextUtils.isEmpty(yybDescribe)) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), yybDescribe);
                }
                break;
            case R.id.tvAssignment:
//                if (((TJRBaseToolBarActivity) getActivity()).isLogin()) {
//                    TradeActivity.pageJump(getActivity(), "YYB", -1);
//                } else {
//                    LoginActivity.login((TJRBaseToolBarActivity) getActivity());
//                }
                MarketActivity.pageJump(getActivity(), "YYB");
                break;


        }


    }
}
