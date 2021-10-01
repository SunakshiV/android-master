package com.procoin.module.home.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.module.home.HomeActivity;
import com.procoin.module.kbt.entity.KbtTrend;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.trade.TradeActivity;
import com.procoin.module.kbt.adapter.KbtNoticeAdapter;
import com.procoin.module.kbt.dialog.KbtBuyBackDialogFragment;
import com.procoin.module.kbt.entity.KbtNotice;
import com.procoin.module.kbt.entity.KbtPool;
import com.procoin.module.login.LoginActivity;
import com.procoin.util.MyCallBack;
import com.procoin.util.StockChartUtil;
import com.procoin.util.VeDate;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.widgets.piechart.CardHolderChartView2;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 *
 * 币种认购页面(已废弃 见 HomeCoinSubBuyActivity)
 * Created by zhengmj on 19-3-8.
 */

public class HomeCoinSubBuyFragment extends UserBaseFragment implements View.OnClickListener {

    //    @BindView(R.id.tvTime)
//    TextView tvTime;
//    @BindView(R.id.tvKbtRules)
//    TextView tvKbtRules;


    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvPriceCash)
    TextView tvPriceCash;
    @BindView(R.id.tvTotalAmount)
    TextView tvTotalAmount;
    @BindView(R.id.tvTotalAmountText)
    TextView tvTotalAmountText;

//    @BindView(R.id.tvMyAmount)
//    TextView tvMyAmount;
//    @BindView(R.id.tvMyAmountText)
//    TextView tvMyAmountText;

    @BindView(R.id.tvPbText)
    TextView tvPbText;
    @BindView(R.id.pb)
    ProgressBar pb;

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

    @BindView(R.id.tvTimeHint)
    TextView tvTimeHint;
    @BindView(R.id.tvTime)
    TextView tvTime;


    private String repoAmount;//剩余可回购KBT数量
    private String holdAmount;//持有KBT数量
    private String myEquityLevel;//权限等级
    private String myEquityTip;//
    private String subUrl;//

    private long countDownTimestamp = 0;
    private String timeTips = "";
    private String btnText = "";
    private boolean isOpenBuy;
    private boolean isOpenTrade;


//    private String kbtDescribe = "";//kbt说明

    private KbtPool kbtPool;

    private int pageNo = 1;
    private int pageSize = 15;


    private Call<ResponseBody> getKbtRepoHomeCall;
    private Call<ResponseBody> getKbtNoticeCall;

    private Call<ResponseBody> kbtRepoCall;

    private KbtNoticeAdapter kbtNoticeAdapter;


    public static HomeCoinSubBuyFragment newInstance() {
        HomeCoinSubBuyFragment fragment = new HomeCoinSubBuyFragment();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "HomeCoinSubBuyFragment onResume   getUserVisibleHint==" + getUserVisibleHint() + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetKbtRepoHome();
            pageNo = 1;
            startGetKbtNoticeCall();
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
        View view = inflater.inflate(R.layout.kbt_buy_back, container, false);
        ButterKnife.bind(this, view);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity(), 15, 15));
        kbtNoticeAdapter = new KbtNoticeAdapter(getActivity());
        rvList.setAdapter(kbtNoticeAdapter);
        kbtNoticeAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        rvList.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        tvBuyBack.setOnClickListener(this);
        tvAssignment.setOnClickListener(this);
//        tvKbtRules.setOnClickListener(this);
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
        getKbtNoticeCall = VHttpServiceManager.getInstance().getVService().getCoinNotice(pageNo);
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
        getKbtRepoHomeCall = VHttpServiceManager.getInstance().getVService().repoHome();
        getKbtRepoHomeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    repoAmount = resultData.getItem("repoAmount", String.class);
                    holdAmount = resultData.getItem("holdAmount", String.class);

                    myEquityLevel = resultData.getItem("myEquityLevel", String.class);
                    myEquityTip = resultData.getItem("myEquityTip", String.class);
                    subUrl = resultData.getItem("subUrl", String.class);

                    countDownTimestamp = resultData.getItem("countDownTimestamp", Long.class);
                    btnText = resultData.getItem("btnText", String.class);
                    timeTips = resultData.getItem("timeTips", String.class);


                    isOpenBuy = resultData.getItem("isOpenBuy", Boolean.class);
                    isOpenTrade = resultData.getItem("isOpenTrade", Boolean.class);

                    String coinSubState = resultData.getItem("coinSubState", String.class);
                    if (getActivity() != null && getActivity() instanceof HomeActivity) {
                        ((HomeActivity) getActivity()).setShowCoin(coinSubState);
                    }

                    startCountDownTime();
                    tvBuyBack.setText(btnText);
                    tvTimeHint.setText(timeTips + ": ");


//                    kbtDescribe = resultData.getItem("kbtDescribe", String.class);


                    Group<KbtTrend> group = resultData.getGroup("kbtTrendList", new TypeToken<Group<KbtTrend>>() {
                    }.getType());

                    kbtPool = resultData.getObject("coinSubBuy", KbtPool.class);

                    if (group != null && group.size() > 0) {
                        cardHolderChartView.setDataForKbt(group);
                    }

                    if (kbtPool != null) {

                        tvSymbol.setText(TextUtils.isEmpty(kbtPool.symbol) ? "暂无" : kbtPool.symbol);
                        tvPrice.setText(kbtPool.price);
                        tvPriceCash.setText(kbtPool.priceCny);
                        tvTotalAmount.setText(kbtPool.totalAmount);
                        tvTotalAmountText.setText("本轮认购" + (TextUtils.isEmpty(kbtPool.symbol) ? "" : kbtPool.symbol) + "总数量");

//                        tvMyAmount.setText(kbtPool.mySubAmount);
//                        tvMyAmountText.setText("我累计认购" + kbtPool.symbol + "数量");

                        if (Double.parseDouble(kbtPool.totalAmount) > 0) {
                            double p = Double.parseDouble(kbtPool.produceAmount) / (int) Double.parseDouble(kbtPool.totalAmount) * 100;
                            tvPbText.setText("本轮认购进度：" + StockChartUtil.formatNumber(2, p) + "%");
                            pb.setMax((int) Double.parseDouble(kbtPool.totalAmount));
                            pb.setProgress((int) Double.parseDouble(kbtPool.produceAmount));
                        }
                    }

                    if (kbtBuyBackDialogFragment != null)//这里主要更新USDT数量，避免充值完回来没有变化
                        kbtBuyBackDialogFragment.updateUSDT(holdAmount);


                }
            }
        });
    }

    private CountDownTimer timer;

    private void startCountDownTime() {
//        Long ss = VeDate.strLongToDate(String.valueOf(orderCash.expireTime)).getTime() - VeDate.strLongToDate(String.valueOf(orderCash.createTime)).getTime();
        Long ss = countDownTimestamp - System.currentTimeMillis() / 1000;
        Log.d("startCountDownTime", "countDownTimestamp==" + countDownTimestamp);
        Log.d("startCountDownTime", "System.currentTimeMillis()==" + System.currentTimeMillis());
        Log.d("startCountDownTime", "ss==" + ss);
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (ss > 0) {
//            llTime.setVisibility(View.VISIBLE);
            timer = new CountDownTimer(ss * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
//                    Log.d("startCountDownTime", "time0==" + time[0]+"time1==" + time[1]+"time2==" + time[2]+"time3==" + time[3]);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[0] + " 天 " + time[1] + " 时 " + time[2] + " 分 " + time[3] + " 秒");
                    }
                }

                public void onFinish() {
                    tvTime.setText("0 天 0 时 00 分 00 秒");
                }
            };
            timer.start();
        } else {
            tvTime.setText("0 天 0 时 00 分 00 秒");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onDestroyView() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();
    }


    private void startKbtRepoCall(String amount) {
        if (kbtPool == null) return;
        CommonUtil.cancelCall(kbtRepoCall);
        ((TJRBaseToolBarActivity) getActivity()).showProgressDialog();
        kbtRepoCall = VHttpServiceManager.getInstance().getVService().repo(kbtPool.subId, kbtPool.symbol, amount);
        kbtRepoCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    com.procoin.util.CommonUtil.showmessage(resultData.msg, getActivity());
                    if (kbtBuyBackDialogFragment != null) {
                        kbtBuyBackDialogFragment.dismiss();
                    }
                    startGetKbtRepoHome();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
            }
        });
    }

    KbtBuyBackDialogFragment kbtBuyBackDialogFragment;

    private void showKbtBuyBackDialogFragment() {
        kbtBuyBackDialogFragment = KbtBuyBackDialogFragment.newInstance(kbtPool, holdAmount, myEquityLevel, myEquityTip, subUrl);
        kbtBuyBackDialogFragment.setOnShareDialogCallBack(new KbtBuyBackDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                kbtBuyBackDialogFragment.dismiss();
            }

            @Override
            public void onSubmit(String amount) {
                startKbtRepoCall(amount);
            }
        });
        kbtBuyBackDialogFragment.showDialog(getChildFragmentManager(), "");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuyBack:
                if (((TJRBaseToolBarActivity) getActivity()).isLogin()) {
                    if (kbtPool == null) return;
                    if (!isOpenBuy) {
                        CommonUtil.showmessage("暂未开放", getActivity());
                        return;
                    }
                    if (TextUtils.isEmpty(kbtPool.symbol) && kbtPool.subId == 0) {
                        CommonUtil.showmessage("当前暂无认购", getActivity());
                        return;
                    }
                    showKbtBuyBackDialogFragment();
                } else {
                    LoginActivity.login((TJRBaseToolBarActivity) getActivity());
                }
                break;
//            case R.id.tvKbtRules:
//                if (!TextUtils.isEmpty(kbtDescribe)) {
//                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), kbtDescribe);
//                }
//                break;
            case R.id.tvAssignment:
                if (!isOpenTrade) {
                    CommonUtil.showmessage("暂未开放", getActivity());
                    return;
                }
                if (kbtPool != null)
                    TradeActivity.pageJump(getActivity(), kbtPool.symbol, -1);
                break;


        }


    }
}
