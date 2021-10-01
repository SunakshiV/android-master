package com.procoin.module.copy;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.module.copy.fragment.CopyBalanceFragment;
import com.procoin.module.copy.fragment.HoldCostFragment;
import com.procoin.module.copy.fragment.HoldMarketValueFragment;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单用户数据
 */
public class CropyUserDataActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.cdlUSDT)
    CoordinatorLayout cdlUSDT;

    @BindView(R.id.tvCropyTolBalance)
    TextView tvCropyTolBalance;
    @BindView(R.id.tvTolShareMoney)
    TextView tvTolShareMoney;
    @BindView(R.id.tvCurrShareMoney)
    TextView tvCurrShareMoney;

    @BindView(R.id.tvCost)
    TextView tvCost;
    @BindView(R.id.tvAssets)
    TextView tvAssets;
    @BindView(R.id.tvMarket)
    TextView tvMarket;


    private MyPagerAdapter adapter;


    private Call<ResponseBody> copyDataHomeCall;


    @Override
    protected int setLayoutId() {
        return R.layout.cropy_user_data;
    }

    @Override
    protected String getActivityTitle() {
        return "跟单用户数据";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
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
        vpContent.setOffscreenPageLimit(adapter.getCount() - 1);
        tvCost.setOnClickListener(this);
        tvAssets.setOnClickListener(this);
        tvMarket.setOnClickListener(this);
        slideTab(0);

    }


    @Override
    protected void onResume() {
        super.onResume();
        startCopyDataHome();
    }

    private void slideTab(int arg0) {
        switch (arg0) {
            case 0:
                tvCost.setSelected(true);
                tvAssets.setSelected(false);
                tvMarket.setSelected(false);

                tvCost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvAssets.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvMarket.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case 1:
                tvCost.setSelected(false);
                tvAssets.setSelected(true);
                tvMarket.setSelected(false);

                tvCost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvAssets.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvMarket.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case 2:
                tvCost.setSelected(false);
                tvAssets.setSelected(false);
                tvMarket.setSelected(true);

                tvCost.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvAssets.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvMarket.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
        }
    }

    private void startCopyDataHome() {
        CommonUtil.cancelCall(copyDataHomeCall);
        copyDataHomeCall = VHttpServiceManager.getInstance().getVService().copyDataHome();
        copyDataHomeCall.enqueue(new MyCallBack(this) {

            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String totalProfitShare = resultData.getItem("totalProfitShare", String.class);
                    String totalCopyBalance = resultData.getItem("totalCopyBalance", String.class);
                    String predictProfitShare = resultData.getItem("predictProfitShare", String.class);

                    tvCropyTolBalance.setText(totalCopyBalance);
                    tvTolShareMoney.setText(totalProfitShare);
                    tvCurrShareMoney.setText(predictProfitShare);

                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCost:
                if (vpContent.getCurrentItem() != 0) {
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.tvAssets:
                if (vpContent.getCurrentItem() != 1) {
                    vpContent.setCurrentItem(1);
                }
                break;
            case R.id.tvMarket:
                if (vpContent.getCurrentItem() != 2) {
                    vpContent.setCurrentItem(2);
                }
                break;


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
            return i == 0 ? HoldCostFragment.newInstance() : i == 1 ? CopyBalanceFragment.newInstance() : HoldMarketValueFragment.newInstance();

        }
    }

}
