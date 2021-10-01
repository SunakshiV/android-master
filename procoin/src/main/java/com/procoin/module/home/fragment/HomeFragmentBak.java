package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.NormalShareData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.adapter.HomeFollowAdapter;
import com.procoin.module.home.adapter.HomePositionAdapter;
import com.procoin.module.home.entity.HomeCopyOrder;
import com.procoin.module.home.entity.Position;
import com.procoin.module.home.trade.USDTTradeActivity;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.util.TjrMinuteTaskPool;
import com.procoin.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-3-8.
 */

public class HomeFragmentBak extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.rvFollowList)
    RecyclerView rvFollowList;
    @BindView(R.id.rvPositionList)
    RecyclerView rvPositionList;
    //    @BindView(R.id.ll_bar)
//    LinearLayout ll_bar;
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.tvTodayProfit)
    TextView tvTodayProfit;
    @BindView(R.id.tvUsdtBalance)
    TextView tvUsdtBalance;
    @BindView(R.id.tvRecharge)
    TextView tvRecharge;
    @BindView(R.id.tvCopyOrder)
    TextView tvCopyOrder;
    @BindView(R.id.tvPosition)
    TextView tvPosition;
    @BindView(R.id.tvNoDataCopyOrder)
    TextView tvNoDataCopyOrder;
    @BindView(R.id.llCopyOrder)
    LinearLayout llCopyOrder;
    @BindView(R.id.tvNoDataPosition)
    TextView tvNoDataPosition;
    @BindView(R.id.llHoldPostion)
    LinearLayout llHoldPostion;
    @BindView(R.id.llHide)
    LinearLayout llHide;
    @BindView(R.id.cbSign)
    CheckBox cbSign;
    @BindView(R.id.ivQuestionMark)
    ImageView ivQuestionMark;

    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private HomeFollowAdapter homeFollowAdapter;
    private HomePositionAdapter homePositionAdapter;

    private boolean isRun = false;//定时器是否在跑
    private TjrMinuteTaskPool tjrMinuteTaskPool;

//    private String openCopyUrl;//当openCopy==0的时候跳到网页，带这个url


//    private RadarView mRdv;
//    private SeekBar mSeekBar1;
//    private SeekBar mSeekBar2;
//    private SeekBar mSeekBar3;
//    private SeekBar mSeekBar4;
//    private SeekBar mSeekBar5;
//    private SeekBar mSeekBar6;
//
//    private int[] ids={R.id.seekBar1,R.id.seekBar2,R.id.seekBar3,R.id.seekBar4,R.id.seekBar5,R.id.seekBar6};

    private Group<Position> groupPostion;

    public static HomeFragmentBak newInstance() {
        HomeFragmentBak fragment = new HomeFragmentBak();
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeAccountFragment", "onResume   getUserVisibleHint()==" + getUserVisibleHint());
        if (getUserVisibleHint()) {
            startGetHomeAccount();
        } else {
            closeTimer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
        if (getUser() != null && cbSign != null)
            cbSign.setChecked(NormalShareData.getIsHideSmallFlag(getActivity(), getUser().getUserId()));
        if (isVisibleToUser) {
            startGetHomeAccount();
        } else {
            closeTimer();
        }
    }

    @Override
    public void onPause() {
        closeTimer();
        super.onPause();
        Log.d("setUserVisibleHint", "onPause=======");

    }

    @Override
    public void onDestroy() {
        closeTimer();
        releaseTimer();
        super.onDestroy();
    }


    private void startTimer() {
//        calculateTradeIncomeRunnable();//启动前也要先计算一次
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTime(getActivity(), task);

    }

    private void closeTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.closeTime();
    }

    private void releaseTimer() {
        isRun = false;
        if (tjrMinuteTaskPool != null) tjrMinuteTaskPool.release();
    }


    private Runnable task = new Runnable() {
        public void run() {
            try {
                startGetHomeAccount();
            } catch (Exception e) {
                CommonUtil.LogLa(2, "Exception is " + e.getMessage());
            }
        }
    };


    public void immersionbar() {
//        if (mImmersionBar != null && ll_bar != null) {
//            mImmersionBar
//                    .titleBar(ll_bar)
//                    .statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA)
//                    .init();
//        }
        if (mImmersionBar == null) return;
        mImmersionBar
//                .titleBar(ll_bar)
                .statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA)
                .init();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_bak, container, false);
//        ll_bar = view.findViewById(R.id.ll_bar);
//        swipeRefreshLayout = view.findViewById(R.id.swiperefreshlayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//            }
//        });
//        rv_list = view.findViewById(R.id.rv_list);
//        adapter = new ActivityAdapter(getActivity());
//        adapter.setOnSignMethodCallback(new ActivityAdapter.OnSignMethodCallback() {
//            @Override
//            public void onSignClick() {
//                doSign();
//            }
//        });
//        rv_list.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rv_list.setAdapter(adapter);

//        mRdv = (RadarView) view.findViewById(R.id.rdv);
//        mSeekBar1 = (SeekBar) view.findViewById(R.id.seekBar1);
//        mSeekBar2 = (SeekBar) view.findViewById(R.id.seekBar2);
//        mSeekBar3 = (SeekBar) view.findViewById(R.id.seekBar3);
//        mSeekBar4 = (SeekBar) view.findViewById(R.id.seekBar4);
//        mSeekBar5 = (SeekBar)view. findViewById(R.id.seekBar5);
//        mSeekBar6 = (SeekBar) view.findViewById(R.id.seekBar6);
//
//        for (int i = 0; i < mRdv.getData().size(); i++) {
//            double value=mRdv.getData().get(i);
//            ((SeekBar)view.findViewById(ids[i])).setProgress((int) value);
//        }
//
//        mSeekBar1.setOnSeekBarChangeListener(this);
//        mSeekBar2.setOnSeekBarChangeListener(this);
//        mSeekBar3.setOnSeekBarChangeListener(this);
//        mSeekBar4.setOnSeekBarChangeListener(this);
//        mSeekBar5.setOnSeekBarChangeListener(this);
//        mSeekBar6.setOnSeekBarChangeListener(this);
        ButterKnife.bind(this, view);

        tjrMinuteTaskPool = new TjrMinuteTaskPool();

        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);

        homeFollowAdapter = new HomeFollowAdapter(getActivity());
        rvFollowList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvFollowList.addItemDecoration(simpleRecycleDivider);
        rvFollowList.setAdapter(homeFollowAdapter);

//        Group<Order> group = new Group<>();
//        for (int i = 0; i < 4; i++) {
//            group.add(new Order());
//        }
//        homeFollowAdapter.setGroup(group);


        homePositionAdapter = new HomePositionAdapter(getActivity());
        rvPositionList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 15, 15);
//        simpleRecycleDivider.setShowLastDivider(true);
        rvPositionList.addItemDecoration(simpleRecycleDivider);
        rvPositionList.setAdapter(homePositionAdapter);
//        Group<Position> group2 = new Group<>();
//        for (int i = 0; i < 4; i++) {
//            group2.add(new Position());
//        }
//        homePositionAdapter.setGroup(group2);

        tvRecharge.setOnClickListener(this);
        tvCopyOrder.setOnClickListener(this);
        tvPosition.setOnClickListener(this);

        cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (getUser() != null) {
                    NormalShareData.saveIsHideSmall(getActivity(), getUser().getUserId(), isChecked);
                }
                if (homePositionAdapter != null)
                    homePositionAdapter.setGroupIsHide(groupPostion, isChecked);
            }
        });
        ivQuestionMark.setOnClickListener(this);
        if (getUser() != null)
            cbSign.setChecked(NormalShareData.getIsHideSmallFlag(getActivity(), getUser().getUserId()));
        slide(0);

        return view;
    }

    private void slide(int pos) {
        scrollView.fullScroll(NestedScrollView.FOCUS_UP);
        switch (pos) {
            case 0:
                tvCopyOrder.setSelected(true);
                tvPosition.setSelected(false);
                tvCopyOrder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                llCopyOrder.setVisibility(View.VISIBLE);
                llHoldPostion.setVisibility(View.GONE);
                llHide.setVisibility(View.INVISIBLE);
                break;
            case 1:
                tvCopyOrder.setSelected(false);
                tvPosition.setSelected(true);
                tvCopyOrder.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvPosition.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                llCopyOrder.setVisibility(View.GONE);
                llHoldPostion.setVisibility(View.VISIBLE);
                llHide.setVisibility(View.VISIBLE);
                break;
        }
    }

    TjrBaseDialog questionMarkDialog;

    private void showSubmitTipsDialog(String tips) {
        questionMarkDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage("市值小于" + tips + "USDT的币种");
        questionMarkDialog.setBtnOkText("知道了");
        questionMarkDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRecharge:
//                RechargeUsdtActivity.pageJump(getActivity(), 0);
                PageJumpUtil.pageJump(getActivity(), USDTTradeActivity.class);
                break;
            case R.id.tvCopyOrder:
                slide(0);
                break;
            case R.id.tvPosition:
                slide(1);
                break;
            case R.id.ivQuestionMark:
                if (!minMarketBalance.equals("0")) showSubmitTipsDialog(minMarketBalance);
                break;

        }
    }

    //    @Override
//    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//        for (int i = 0; i < ids.length; i++) {
//            if(ids[i]==seekBar.getId()){
//                List<Double> data = mRdv.getData();
//                data.set(i, (double) progress);
//                mRdv.setData(data);
//            }
//        }
//    }
//
//    @Override
//    public void onStartTrackingTouch(SeekBar seekBar) {
//
//    }
//
//    @Override
//    public void onStopTrackingTouch(SeekBar seekBar) {
//
//    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        immersionbar();
        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private Call<ResponseBody> getHomeAccountCall;

    private String tolAssets;
    private String tolAssetsCny;
    private String todayProfit;
    private String usdtBalance;
    private String minMarketBalance = "0";
//    private String todayProfitRate;
//    private String tolProfit;
//    private String tolProfitRate;


    private void startGetHomeAccount() {
        CommonUtil.cancelCall(getHomeAccountCall);
        getHomeAccountCall = VHttpServiceManager.getInstance().getVService().homeAccount();
        getHomeAccountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    tolAssets = resultData.getItem("tolAssets", String.class);
                    tolAssetsCny = resultData.getItem("tolAssetsCny", String.class);
                    todayProfit = resultData.getItem("todayProfit", String.class);
                    usdtBalance = resultData.getItem("usdtBalance", String.class);
                    minMarketBalance = resultData.getItem("minMarketBalance", String.class);

                    tvTolAssets.setText(tolAssets);
                    tvTolAssetsCny.setText(tolAssetsCny);
                    tvTodayProfit.setText(StockChartUtil.formatWithSign(todayProfit));
                    tvUsdtBalance.setText(usdtBalance);

//                    openCopyUrl = resultData.getItem("openCopyUrl", String.class);
//                    openCopy = resultData.getItem("openCopy", Integer.class);
//                    if(openCopy==0){
//                        tvCropyme.setText("开通CROPYME");
//                    }else{
//                        tvCropyme.setText("进入我的CROPYME");
//                    }

//                    todayProfitRate = resultData.getItem("todayProfitRate", String.class);
//                    tolProfit = resultData.getItem("tolProfit", String.class);
//                    tolProfitRate = resultData.getItem("tolProfitRate", String.class);
//                    tvTodayProfitRate.setText(todayProfitRate);
//                    tvTolProfit.setText(tolProfit);

                    groupPostion = resultData.getGroup("holdList", new TypeToken<Group<Position>>() {
                    }.getType());
                    if (groupPostion != null && groupPostion.size() > 0) {
                        rvPositionList.setVisibility(View.VISIBLE);
                        tvNoDataPosition.setVisibility(View.GONE);
                        homePositionAdapter.setGroupIsHide(groupPostion, cbSign.isChecked());
                    } else {
                        rvPositionList.setVisibility(View.GONE);
                        tvNoDataPosition.setVisibility(View.VISIBLE);
                    }
                    Group<HomeCopyOrder> groupFollow = resultData.getGroup("orderList", new TypeToken<Group<HomeCopyOrder>>() {
                    }.getType());
                    if (groupFollow != null && groupFollow.size() > 0) {
                        rvFollowList.setVisibility(View.VISIBLE);
                        tvNoDataCopyOrder.setVisibility(View.GONE);
                        homeFollowAdapter.setGroup(groupFollow);
                    } else {
                        rvFollowList.setVisibility(View.GONE);
                        tvNoDataCopyOrder.setVisibility(View.VISIBLE);
                    }
                    int size = (groupPostion == null ? 0 : groupPostion.size()) + (groupFollow == null ? 0 : groupFollow.size());
                    if (!isRun && size > 0) startTimer();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

}
