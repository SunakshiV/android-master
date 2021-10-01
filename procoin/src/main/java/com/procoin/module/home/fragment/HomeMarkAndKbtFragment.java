package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.R;
import com.procoin.module.home.HomeActivity;
import com.procoin.module.home.OptionalDragSortActivity;
import com.procoin.module.home.SearchCoinActivity;
import com.procoin.module.login.LoginActivity;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 19-3-8.
 */

public class HomeMarkAndKbtFragment extends UserBaseImmersionBarFragment implements View.OnClickListener {

    @BindView(R.id.ll_bar)
    LinearLayout ll_bar;


    @BindView(R.id.tvOptional)
    TextView tvOptional;
    @BindView(R.id.tvFutures)
    TextView tvFutures;
    @BindView(R.id.tvDigital)
    TextView tvDigital;
    @BindView(R.id.tvHS)
    TextView tvHS;
    @BindView(R.id.tvHK)
    TextView tvHK;

//    @BindView(R.id.tvMarket)
//    TextView tvMarket;
//    @BindView(R.id.tvBuyBack)
//    TextView tvBuyBack;

    @BindView(R.id.ivSearch)
    LinearLayout ivSearch;
    @BindView(R.id.ivOptionalSort)
    LinearLayout ivOptionalSort;


    @BindView(R.id.viewPager)
    ViewPager viewPager;

    HomeMarkPageAdapter homeMarkPageAdapter;

    public static HomeMarkAndKbtFragment newInstance() {
        HomeMarkAndKbtFragment fragment = new HomeMarkAndKbtFragment();
        return fragment;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (homeMarkPageAdapter != null && viewPager != null) {
            for (int i = 0; i < homeMarkPageAdapter.getCount(); i++) {
                Fragment fragment = (Fragment) homeMarkPageAdapter.instantiateItem(viewPager, i);
                fragment.setUserVisibleHint(isVisibleToUser && viewPager.getCurrentItem() == i);
            }
        }

        Log.d("HomeMarkAndKbtFragment", "setUserVisibleHint   isVisibleToUser==" + isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "onResume   getUserVisibleHint()==" + getUserVisibleHint());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("setUserVisibleHint", "onPause=======");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    public void immersionbar() {
        Log.d("HomeMarket", "mImmersionBar==" + mImmersionBar + " ll_bar==" + ll_bar);
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
        View view = inflater.inflate(R.layout.fragment_home_mark_and_kbt, container, false);
        ButterKnife.bind(this, view);

        homeMarkPageAdapter = new HomeMarkPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(homeMarkPageAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                Log.d("HomeMarkAndKbtFragment", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        viewPager.setOffscreenPageLimit(homeMarkPageAdapter.getCount() - 1);
        tvOptional.setOnClickListener(this);
        tvFutures.setOnClickListener(this);
        tvDigital.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        ivOptionalSort.setOnClickListener(this);
        tvHS.setOnClickListener(this);
        tvHK.setOnClickListener(this);

        viewPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                viewPager.setCurrentItem(0, false);
                slideTab(0);
            }
        }, 300);

        return view;
    }

    public void setKbtSelected() {
        if (viewPager != null) {
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    viewPager.setCurrentItem(1, false);
                    slideTab(1);
                }
            }, 300);
        }

    }

    public void slideTab(int pos) {
        switch (pos) {
            case 0:
                tvOptional.setSelected(true);
                tvHS.setSelected(false);
                tvHK.setSelected(false);
                tvFutures.setSelected(false);
                tvDigital.setSelected(false);
                ivOptionalSort.setVisibility(View.VISIBLE);
                break;

            case 1:
                tvOptional.setSelected(false);
                tvHS.setSelected(true);
                tvHK.setSelected(false);
                tvFutures.setSelected(false);
                tvDigital.setSelected(false);
                ivOptionalSort.setVisibility(View.GONE);
                break;
            case 2:
                tvOptional.setSelected(false);
                tvHS.setSelected(false);
                tvHK.setSelected(true);
                tvFutures.setSelected(false);
                tvDigital.setSelected(false);
                ivOptionalSort.setVisibility(View.GONE);
                break;
            case 3:
                tvOptional.setSelected(false);
                tvHS.setSelected(false);
                tvHK.setSelected(false);
                tvFutures.setSelected(true);
                tvDigital.setSelected(false);
                ivOptionalSort.setVisibility(View.GONE);
                break;
            case 4:
                tvOptional.setSelected(false);
                tvHS.setSelected(false);
                tvHK.setSelected(false);
                tvFutures.setSelected(false);
                tvDigital.setSelected(true);
                ivOptionalSort.setVisibility(View.GONE);
                break;

        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        immersionbar();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tvOptional:
                if (viewPager.getCurrentItem() != 0) {
                    viewPager.setCurrentItem(0);
                }
                break;
            case R.id.tvHS:
                if (viewPager.getCurrentItem() != 1) {
                    viewPager.setCurrentItem(1);
                }
                break;
            case R.id.tvHK:
                if (viewPager.getCurrentItem() != 2) {
                    viewPager.setCurrentItem(2);
                }
                break;
            case R.id.tvFutures:
                if (viewPager.getCurrentItem() != 3) {
                    viewPager.setCurrentItem(3);
                }
                break;
            case R.id.tvDigital:
                if (viewPager.getCurrentItem() != 4) {
                    viewPager.setCurrentItem(4);
                }
                break;
            case R.id.ivSearch:
                PageJumpUtil.pageJump(getActivity(), SearchCoinActivity.class);
                break;
            case R.id.ivOptionalSort:
                if (((HomeActivity) getActivity()).isLogin()) {
                    PageJumpUtil.pageJump(getActivity(), OptionalDragSortActivity.class);
                } else {
                    LoginActivity.login((HomeActivity) getActivity());
                }

                break;
        }

    }


    private class HomeMarkPageAdapter extends FragmentPagerAdapter {


        public HomeMarkPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public void notifyDataSetChanged() {
//            slideTab(0);
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return FragmentStatePagerAdapter.POSITION_NONE;
        }

        //optional自选、digital数字货币、stock股指期货
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return HomeMarkOptionalFragment.newInstance("optional");
                case 1:
                    return HomeStockMarkFragment.newInstance("shsz", 1);
                case 2:
                    return HomeStockMarkFragment.newInstance("hk", 1);
                case 3:
                    return HomeStockDigitalMarkFragment.newInstance("stock", 1);
                case 4:
                    return HomeStockDigitalMarkFragment.newInstance("digital", 1);
                default:
                    return HomeMarkOptionalFragment.newInstance("optional");
            }


        }
    }
}
