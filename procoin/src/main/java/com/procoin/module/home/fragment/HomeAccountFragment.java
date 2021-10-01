package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.entity.AccountInfo;
import com.procoin.module.home.entity.Position;
import com.procoin.module.home.entity.UserFollow;
import com.procoin.module.home.trade.RechargeCoinActivity;
import com.procoin.module.home.trade.TakeCoinActivity;
import com.procoin.module.home.trade.TransferCoinActivity;
import com.procoin.module.home.trade.USDTTradeActivity;
import com.procoin.module.legal.LegalMoneyActivity;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.TjrMinuteTaskPool;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-3-8.
 */

public class HomeAccountFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;


    @BindView(R.id.vp_content)
    ViewPager vp_content;
    @BindView(R.id.llRechargeCoin)
    TextView llRechargeCoin;
    @BindView(R.id.llWithDrawCoin)
    TextView llWithDrawCoin;
    @BindView(R.id.llTransfer)
    TextView llTransfer;
    @BindView(R.id.llLegalMoney)
    TextView llLegalMoney;

    @BindView(R.id.tvDigitalAccount)
    TextView tvDigitalAccount;
    @BindView(R.id.tvStockAccount)
    TextView tvStockAccount;
//    @BindView(R.id.tvFollowAccount)
//    TextView tvFollowAccount;
    @BindView(R.id.tvBalanceAccount)
    TextView tvBalanceAccount;




    private boolean isRun = false;//定时器是否在跑
    private TjrMinuteTaskPool tjrMinuteTaskPool;


    private Group<Position> groupPostion;


    private MyPagerAdapter adapter;

    public static HomeAccountFragment newInstance() {
        HomeAccountFragment fragment = new HomeAccountFragment();
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
//        if (getUser() != null && cbSign != null)
//            cbSign.setChecked(NormalShareData.getIsHideSmallFlag(getActivity(), getUser().getUserId()));
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
                .navigationBarColor(R.color.white)
                .init();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        tjrMinuteTaskPool = new TjrMinuteTaskPool();


        tvDigitalAccount.setOnClickListener(this);
        tvStockAccount.setOnClickListener(this);
//        tvFollowAccount.setOnClickListener(this);
        tvBalanceAccount.setOnClickListener(this);

        llRechargeCoin.setOnClickListener(this);
        llWithDrawCoin.setOnClickListener(this);
        llTransfer.setOnClickListener(this);
        llLegalMoney.setOnClickListener(this);


//        cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (getUser() != null) {
//                    NormalShareData.saveIsHideSmall(getActivity(), getUser().getUserId(), isChecked);
//                }
//                setHold(groupPostion, isChecked);
////                if (homePositionAdapter != null)
////                    homePositionAdapter.setGroupIsHide(groupPostion, isChecked);
//            }
//        });
//        ivQuestionMark.setOnClickListener(this);
//        if (getUser() != null)
//            cbSign.setChecked(NormalShareData.getIsHideSmallFlag(getActivity(), getUser().getUserId()));


        adapter = new MyPagerAdapter(getChildFragmentManager());
        vp_content.setAdapter(adapter);
        vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slide(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vp_content.setOffscreenPageLimit(adapter.getCount() - 1);
        slide(0);
        return view;
    }

    private void slide(int pos) {
//        scrollView.fullScroll(NestedScrollView.FOCUS_UP);
        switch (pos) {
            case 0:
                tvDigitalAccount.setSelected(true);
                tvStockAccount.setSelected(false);
//                tvFollowAccount.setSelected(false);
                tvBalanceAccount.setSelected(false);

                break;
            case 1:
                tvDigitalAccount.setSelected(false);
                tvStockAccount.setSelected(true);
//                tvFollowAccount.setSelected(false);
                tvBalanceAccount.setSelected(false);
//                llHide.setVisibility(View.INVISIBLE);
                break;
//            case 2:
//                tvDigitalAccount.setSelected(false);
//                tvStockAccount.setSelected(false);
////                tvFollowAccount.setSelected(true);
//                tvBalanceAccount.setSelected(false);
////                llHide.setVisibility(View.VISIBLE);
//                break;
            case 2:
                tvDigitalAccount.setSelected(false);
                tvStockAccount.setSelected(false);
//                tvFollowAccount.setSelected(false);
                tvBalanceAccount.setSelected(true);
//                llHide.setVisibility(View.INVISIBLE);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvRecharge:
//                RechargeUsdtActivity.pageJump(getActivity(), 0);
                PageJumpUtil.pageJump(getActivity(), USDTTradeActivity.class);
                break;
            case R.id.tvDigitalAccount:
//                slide(0);
                if (vp_content.getCurrentItem() != 0) {
                    vp_content.setCurrentItem(0);
                }
                break;
            case R.id.tvStockAccount:
//                slide(0);
                if (vp_content.getCurrentItem() != 1) {
                    vp_content.setCurrentItem(1);
                }
                break;
//            case R.id.tvFollowAccount:
////                slide(1);
//                if (vp_content.getCurrentItem() != 2) {
//                    vp_content.setCurrentItem(2);
//                }
//                break;
            case R.id.tvBalanceAccount:
                if (vp_content.getCurrentItem() != 2) {
                    vp_content.setCurrentItem(2);
                }
                break;

            case R.id.llRechargeCoin:
                PageJumpUtil.pageJump(getActivity(), RechargeCoinActivity.class);
//                RechargeCoinActivity.pageJump(getActivity(),"USDT");
                break;
            case R.id.llWithDrawCoin:
                PageJumpUtil.pageJump(getActivity(), TakeCoinActivity.class);
                break;
            case R.id.llTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;
            case R.id.llLegalMoney:
                PageJumpUtil.pageJump(getActivity(), LegalMoneyActivity.class);
                break;

//            case R.id.ivQuestionMark:
//                if (!minMarketBalance.equals("0")) showSubmitTipsDialog(minMarketBalance);
//                break;

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        immersionbar();
        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).navigationBarColor(R.color.white).init();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    private Call<ResponseBody> getHomeAccountCall;

    private String tolAssets;
    private String tolAssetsCny;


    private void startGetHomeAccount() {
        CommonUtil.cancelCall(getHomeAccountCall);
        getHomeAccountCall = VHttpServiceManager.getInstance().getVService().homeAccount();
        getHomeAccountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    tolAssets = resultData.getItem("tolAssets", String.class);
                    tolAssetsCny = resultData.getItem("tolAssetsCny", String.class);
                    tvTolAssets.setText(tolAssets);
                    tvTolAssetsCny.setText(tolAssetsCny);


                    AccountInfo digitalAccount = resultData.getObject("digitalAccount", AccountInfo.class);
                    AccountInfo stockAccount = resultData.getObject("stockAccount", AccountInfo.class);
                    AccountInfo followDigitalAccount = resultData.getObject("followDigitalAccount", AccountInfo.class);
                    AccountInfo followStockAccount = resultData.getObject("followStockAccount", AccountInfo.class);
                    AccountInfo balanceAccount = resultData.getObject("balanceAccount", AccountInfo.class);
                    UserFollow userFollow = resultData.getObject("userFollow", UserFollow.class);


                    if (vp_content.getCurrentItem() == 0) {
                        setDitigalData(digitalAccount,0);
                        String s = digitalAccount.openList == null ? "null" : (digitalAccount.openList.size() + "");
//                    com.procoin.util.CommonUtil.showmessage(s,getActivity());
                        Log.d("digitalAccount", "digitalAccount.openList11111==" + s);
                    } else {
                        setDitigalData(stockAccount,1);
                        String s = stockAccount.openList == null ? "null" : (stockAccount.openList.size() + "");
//                    com.procoin.util.CommonUtil.showmessage(s,getActivity());
                        Log.d("digitalAccount", "stockAccount.openList11111==" + s);
                    }
//                    setFollow(followDigitalAccount, followStockAccount, userFollow);
                    setBalance(balanceAccount);

                    if (!isRun) startTimer();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private void setDitigalData(AccountInfo accountInfo, int pos) {
        if (adapter != null && vp_content != null) {
            Object object = adapter.instantiateItem(vp_content, pos);
            if (object != null && object instanceof HomeDigitalAccountFragment) {
                HomeDigitalAccountFragment homeDigitalAccountFragment = (HomeDigitalAccountFragment) object;
                homeDigitalAccountFragment.setData(accountInfo);
            }

        }
    }


//    private void setFollow(AccountInfo followDigitalAccount, AccountInfo followStockAccount, UserFollow userFollow) {
//        if (adapter != null && vp_content != null) {
//            Object object = adapter.instantiateItem(vp_content, 2);
//            if (object != null && object instanceof HomeFollowAccountFragment) {
//                HomeFollowAccountFragment homeFollowAccountFragment = (HomeFollowAccountFragment) object;
//                homeFollowAccountFragment.setData(followDigitalAccount, followStockAccount, userFollow);
//            }
//
//        }
//
//    }


    private void setBalance(AccountInfo balanceAccount) {
        if (adapter != null && vp_content != null) {
            Object object = adapter.instantiateItem(vp_content, 2);
            if (object != null && object instanceof HomeBalanceAccountFragment) {
                Log.d("setCoinWallet", "////////");
                HomeBalanceAccountFragment homeBalanceAccountFragment = (HomeBalanceAccountFragment) object;
                homeBalanceAccountFragment.setData(balanceAccount);
            }

        }

    }

    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case 0:
                    return HomeDigitalAccountFragment.newInstance(0);
                case 1:
                    return HomeDigitalAccountFragment.newInstance(1);
//                case 2:
//                    return HomeFollowAccountFragment.newInstance();
                default:
                case 2:
                    return HomeBalanceAccountFragment.newInstance();


            }

        }
    }
}
