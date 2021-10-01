//package com.coingo.module.kbt;
//
//
//import android.os.Bundle;
//import android.support.v7.widget.LinearLayoutManager;
//import android.view.View;
//import android.widget.TextView;
//
//import TJRBaseToolBarSwipeBackActivity;
//import ResultData;
//import KbtNoticeAdapter;
//import KbtBuyBackDialogFragment;
//import KbtNotice;
//import KbtPool;
//import KbtTrend;
//import MyCallBack;
//import LoadMoreRecycleView;
//import SimpleRecycleDivider;
//import CardHolderChartView;
//import com.coingo.R;
//import Group;
//import VHttpServiceManager;
//import CommonUtil;
//import com.google.gson.reflect.TypeToken;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//
///**
// * kbt回购 （已废弃）
// */
//public class KbtBuyBackActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
//
//
//    @BindView(R.id.tvTime)
//    TextView tvTime;
//    @BindView(R.id.tvPrice)
//    TextView tvPrice;
//    @BindView(R.id.tvPriceCash)
//    TextView tvPriceCash;
////    @BindView(R.id.tvProduceAmount)
////    TextView tvProduceAmount;
////    @BindView(R.id.tvDestroyAmount)
////    TextView tvDestroyAmount;
//    @BindView(R.id.tvHistory)
//    TextView tvHistory;
//    @BindView(R.id.cardHolderChartView)
//    CardHolderChartView cardHolderChartView;
//    @BindView(R.id.rv_list)
//    LoadMoreRecycleView rvList;
//    @BindView(R.id.tvBuyBack)
//    TextView tvBuyBack;
//
//    private String repoAmount;//剩余可回购KBT数量
//    private String holdAmount;//持有KBT数量
//
//    private int pageNo = 1;
//    private int pageSize = 15;
//
//
//    private Call<ResponseBody> getKbtRepoHomeCall;
//    private Call<ResponseBody> getKbtNoticeCall;
//
//    private Call<ResponseBody> kbtRepoCall;
//
//    private KbtNoticeAdapter kbtNoticeAdapter;
//
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.kbt_buy_back;
//    }
//
//    @Override
//    protected String getActivityTitle() {
//        return "KBT回购";
//    }
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        ButterKnife.bind(this);
//        rvList.setLayoutManager(new LinearLayoutManager(this));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));
//        kbtNoticeAdapter = new KbtNoticeAdapter(this);
//        rvList.setAdapter(kbtNoticeAdapter);
//        kbtNoticeAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
//        rvList.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
//        tvBuyBack.setOnClickListener(this);
//        startGetKbtRepoHome();
//        startGetKbtNoticeCall();
//
//    }
//
//    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
//        @Override
//        public void loadMore() {
//            if (kbtNoticeAdapter != null && kbtNoticeAdapter.getRealItemCount() > 0) {
//                startGetKbtNoticeCall();
//            } else {
//                pageNo = 1;
//                startGetKbtNoticeCall();
//            }
//        }
//    };
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//
//    private void startGetKbtRepoHome() {
//        CommonUtil.cancelCall(getKbtRepoHomeCall);
//        getKbtRepoHomeCall = VHttpServiceManager.getInstance().getVService().kbtRepoHome();
//        getKbtRepoHomeCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    repoAmount = resultData.getItem("repoAmount", String.class);
//                    holdAmount = resultData.getItem("holdAmount", String.class);
//
//                    Group<KbtTrend> group = resultData.getGroup("kbtTrendList", new TypeToken<Group<KbtTrend>>() {
//                    }.getType());
//
//                    YYBPool kbtPool = resultData.getObject("kbtPool", YYBPool.class);
//
//                    if (group != null && group.size() > 0) {
//                        cardHolderChartView.setDataForKbt(group);
//                    }
//
//                    if (kbtPool != null) {
////                        tvPrice.setText(kbtPool.repoPrice);
////                        tvPriceCash.setText(kbtPool.repoPriceCny);
////                        tvProduceAmount.setText(kbtPool.produceAmount);
////                        tvDestroyAmount.setText(kbtPool.destroyAmount);
////                        tvTime.setText("回购期：" + DateUtils.getStringDateOfString2(kbtPool.fromTime, DateUtils.TEMPLATE_MM_dd2) + "-" + DateUtils.getStringDateOfString2(kbtPool.toTime, DateUtils.TEMPLATE_MM_dd2));
//                    }
//
//
//                }
//            }
//        });
//    }
//
//
//    private void startGetKbtNoticeCall() {
//        CommonUtil.cancelCall(getKbtNoticeCall);
//        getKbtNoticeCall = VHttpServiceManager.getInstance().getVService().getKbtNotice(pageNo);
//        getKbtNoticeCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                Group<KbtNotice> group = null;
//                if (resultData.isSuccess()) {
//                    pageSize = resultData.getPageSize(pageSize);
//                    group = resultData.getGroup("data", new TypeToken<Group<KbtNotice>>() {
//                    }.getType());
//                    if (group != null && group.size() > 0) {
//                        if (pageNo == 1) {
//                            kbtNoticeAdapter.setGroup(group);
//                        } else {
//                            kbtNoticeAdapter.addItem(group);
//                            kbtNoticeAdapter.notifyDataSetChanged();
//                        }
//                    }
//                    pageNo++;
//                }
//                kbtNoticeAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                kbtNoticeAdapter.onLoadComplete(false, false);
//            }
//        });
//    }
//
//    private void startKbtRepoCall(String amount) {
//        CommonUtil.cancelCall(kbtRepoCall);
//        showProgressDialog();
//        kbtRepoCall = VHttpServiceManager.getInstance().getVService().kbtRepo(amount);
//        kbtRepoCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                dismissProgressDialog();
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, KbtBuyBackActivity.this);
//                    if (kbtBuyBackDialogFragment != null) {
//                        kbtBuyBackDialogFragment.dismiss();
//                    }
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                dismissProgressDialog();
//            }
//        });
//    }
//
//    KbtBuyBackDialogFragment kbtBuyBackDialogFragment;
//
//    private void showKbtBuyBackDialogFragment() {
//        kbtBuyBackDialogFragment = KbtBuyBackDialogFragment.newInstance(repoAmount, holdAmount);
//        kbtBuyBackDialogFragment.setOnShareDialogCallBack(new KbtBuyBackDialogFragment.OnShareDialogCallBack() {
//            @Override
//            public void onDialogDismiss() {
//                kbtBuyBackDialogFragment.dismiss();
//            }
//
//            @Override
//            public void onSubmit(double kbt) {
//                startKbtRepoCall(String.valueOf(kbt));
//            }
//        });
//        kbtBuyBackDialogFragment.showDialog(getSupportFragmentManager(), "");
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tvBuyBack:
//                showKbtBuyBackDialogFragment();
//                break;
//
//
//        }
//    }
//
//}
