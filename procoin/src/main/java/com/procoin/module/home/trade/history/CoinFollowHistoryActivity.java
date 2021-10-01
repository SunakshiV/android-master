package com.procoin.module.home.trade.history;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.home.trade.fragment.TradeHistoryLeverFollowFragment;
import com.procoin.module.home.trade.fragment.TradeUndoneLeverFollowFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 跟单历史记录
 */
public class CoinFollowHistoryActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.vp_content)
    ViewPager vpContent;

    @BindView(R.id.tvTab1)
    TextView tvTab1;
    @BindView(R.id.tvTab2)
    TextView tvTab2;
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.llParams)
    LinearLayout llParams;
    @BindView(R.id.etCoin)
    EditText etCoin;
    //    @BindView(R.id.tvAllDeal)
//    TextView tvAllDeal;
//    @BindView(R.id.tvPartDeal)
//    TextView tvPartDeal;
//    @BindView(R.id.tvCancel)
//    TextView tvCancel;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvQuery)
    TextView tvQuery;
    @BindView(R.id.llSelectParamsAnim)
    LinearLayout llSelectParamsAnim;
    @BindView(R.id.hideSelectParams)
    View hideSelectParams;
    @BindView(R.id.llSelectParams)
    LinearLayout llSelectParams;
    @BindView(R.id.tvAlreadyTrade)
    TextView tvAlreadyTrade;
    @BindView(R.id.tvAlreadyCancel)
    TextView tvAlreadyCancel;
    @BindView(R.id.tvDigital)
    TextView tvDigital;
    @BindView(R.id.tvStock)
    TextView tvStock;


    private MyPagerAdapter adapter;

    private String accountType = "";

//    public static void pageJump(Context context, String accountType) {
//        Bundle bundle = new Bundle();
//        bundle.putString(CommonConst.ACCOUNTTYPE, accountType);
//        PageJumpUtil.pageJump(context, CoinFollowHistoryActivity.class, bundle);
//    }


//    @Override
//    protected void handlerMsg(ReceiveModel model) {
//        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
//            case refresh_entrust_order_api:
//                if (adapter != null && vpContent != null) {
//                    TradeUndoneLeverFragment tradeUndoneFragment = (TradeUndoneLeverFragment) adapter.instantiateItem(vpContent, 0);
//                    tradeUndoneFragment.refresh();
//                }
//                break;
//        }
//    }

    @Override
    protected int setLayoutId() {
        return R.layout.coin_follow_his_lever;
    }

    @Override
    protected String getActivityTitle() {
        return "跟单记录";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        Bundle bundle = getIntent().getExtras();
//        if (bundle != null) {
//            if (bundle.containsKey(CommonConst.ACCOUNTTYPE)) {
//                accountType = bundle.getString(CommonConst.ACCOUNTTYPE, "");
//            }
//
//        }
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);

        llParams.setOnClickListener(this);
        tvAlreadyTrade.setOnClickListener(this);
        tvAlreadyCancel.setOnClickListener(this);
        tvDigital.setOnClickListener(this);
        tvStock.setOnClickListener(this);
//        tvCancel.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        hideSelectParams.setOnClickListener(this);

        vpContent.setCurrentItem(1);
        slideTab(1);
        llParams.setVisibility(View.VISIBLE);

    }

    private void slideTab(int arg0) {
//        currentTab = arg0;
        switch (arg0) {
            case 0:
                tvTab1.setSelected(true);
                tvTab2.setSelected(false);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                llParams.setVisibility(View.GONE);

                break;
            case 1:
                tvTab1.setSelected(false);
                tvTab2.setSelected(true);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                llParams.setVisibility(View.VISIBLE);
                break;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTab1:
                if (vpContent.getCurrentItem() != 0) {
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.tvTab2:
                if (vpContent.getCurrentItem() != 1) {
                    vpContent.setCurrentItem(1);
                }
                break;

            case R.id.llParams:
                if (llSelectParams.getVisibility() == View.INVISIBLE || llSelectParams.getVisibility() == View.GONE) {
                    showSelectParams();
                } else {
                    hideSelectParams();
                }
                break;
            case R.id.tvAlreadyTrade:
                updateOrderState("filled");
                break;
            case R.id.tvAlreadyCancel:
                updateOrderState("canceled");
                break;
            case R.id.tvDigital:
                updateAccountType("followdigital");
                break;
            case R.id.tvStock:
                updateAccountType("followstock");
                break;

            case R.id.tvReset:
                hideSelectParams();
                reset();
                if (vpContent.getCurrentItem() == 1) {
                    TradeHistoryLeverFollowFragment tradeHistoryLeverFollowFragment = (TradeHistoryLeverFollowFragment) adapter.instantiateItem(vpContent, 1);
                    tradeHistoryLeverFollowFragment.reset();
                }
                break;
            case R.id.tvQuery:
                String symbol = etCoin.getText().toString();
//                if (TextUtils.isEmpty(symbol)) {
//                    CommonUtil.showmessage("请选择币种", this);
//                    return;
//                }
                hideSelectParams();
                this.symbol = symbol;
                if (vpContent.getCurrentItem() == 1) {
                    TradeHistoryLeverFollowFragment tradeHistoryLeverFollowFragment = (TradeHistoryLeverFollowFragment) adapter.instantiateItem(vpContent, 1);
                    tradeHistoryLeverFollowFragment.filter(this.symbol, orderState, accountType);
                }
                break;
            case R.id.hideSelectParams:
                hideSelectParams();
                break;


        }
    }

    private void reset() {
        this.orderState = "";
        this.accountType = "";
        symbol = "";
        etCoin.setText("");
        setOrderStateBtnState();
    }


    private void updateOrderState(String orderState) {
        if (!orderState.equals(this.orderState)) {
            this.orderState = orderState;
        } else {
            this.orderState = "";
        }
        setOrderStateBtnState();
    }

    private void setOrderStateBtnState() {
        if (orderState.equals("filled")) {
            tvAlreadyTrade.setSelected(true);
            tvAlreadyCancel.setSelected(false);
        } else if (orderState.equals("canceled")) {
            tvAlreadyTrade.setSelected(false);
            tvAlreadyCancel.setSelected(true);
        } else {
            tvAlreadyTrade.setSelected(false);
            tvAlreadyCancel.setSelected(false);
        }
    }

    private void updateAccountType(String accountType) {
        if (!accountType.equals(this.accountType)) {
            this.accountType = accountType;
        } else {
            this.accountType = "";
        }
        setAccountTypeState();
    }

    private void setAccountTypeState() {
        if (accountType.equals("followdigital")) {
            tvDigital.setSelected(true);
            tvStock.setSelected(false);
        } else if (accountType.equals("followstock")) {
            tvDigital.setSelected(false);
            tvStock.setSelected(true);
        } else {
            tvDigital.setSelected(false);
            tvStock.setSelected(false);
        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return position == 0 ? "　出售中　" : "　未上架　";
//        }

        @Override
        public Fragment getItem(int i) {
//            return UsdtOrCashTradeHisFragment.newInstance(SYMBOL,"",0);
            return i == 0 ? TradeUndoneLeverFollowFragment.newInstance("", accountType) : TradeHistoryLeverFollowFragment.newInstance("", accountType);

        }
    }


    private ObjectAnimator objectAnimatorShow;
    private ObjectAnimator objectAnimatorShowArrow;
    private ObjectAnimator objectAnimatorHide;
    private ObjectAnimator objectAnimatorHideArrow;
    private FastOutSlowInInterpolator fastOutSlowInInterpolator;

    private String symbol;
    private String orderState = "";//


    private void showSelectParams() {
        llSelectParams.setVisibility(View.VISIBLE);

        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorShow == null) {
            objectAnimatorShow = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", -llSelectParamsAnim.getHeight(), 0);
            objectAnimatorShow.setDuration(300);
            objectAnimatorShow.setInterpolator(fastOutSlowInInterpolator);
        }

        if (objectAnimatorShowArrow == null) {
            objectAnimatorShowArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", 0.0f, -45.0f, -90.0f, -180.0f);
            objectAnimatorShowArrow.setDuration(300);
            objectAnimatorShowArrow.setInterpolator(fastOutSlowInInterpolator);
        }
        objectAnimatorShow.start();
        objectAnimatorShowArrow.start();

    }

    private void hideSelectParams() {
        closeKeyBoard();
        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorHide == null) {
            objectAnimatorHide = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", 0, -llSelectParamsAnim.getHeight());
            objectAnimatorHide.setDuration(300);
            objectAnimatorHide.setInterpolator(fastOutSlowInInterpolator);
            objectAnimatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    llSelectParams.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


        if (objectAnimatorHideArrow == null) {
            objectAnimatorHideArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", -180.0f, -90.0f, -45.0f, 0.0f);
            objectAnimatorHideArrow.setDuration(300);
            objectAnimatorHideArrow.setInterpolator(fastOutSlowInInterpolator);

        }
        objectAnimatorHide.start();
        objectAnimatorHideArrow.start();
    }


    private void closeKeyBoard() {
        if (etCoin == null || etCoin.getWindowToken() == null) return;
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCoin.getWindowToken(), 0);
    }

}
