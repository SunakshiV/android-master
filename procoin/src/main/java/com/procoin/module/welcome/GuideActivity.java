package com.procoin.module.welcome;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.module.home.HomeActivity;
import com.procoin.module.welcome.fragment.BaseWelComeFragment;
import com.procoin.module.welcome.fragment.DiyGuideFragment1;
import com.procoin.module.welcome.fragment.DiyGuideFragment2;
import com.procoin.module.welcome.fragment.DiyGuideFragment3;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.indicator.CirclePageIndicator;
import com.procoin.R;


/**
 * 这个类是欢迎的介绍类
 *
 * @author zhengmj
 */
public class GuideActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    private final int PAGERNUM = 3;// 暂时只有3个
    private ViewPager viewPager;
    private CirclePageIndicator mIndicator;
    private int oldPos;
    private boolean left;
    private boolean right;

    private Bundle bundle;
    private int pageJumpType;
//    private TextView tvJump;//

    private TextView btnOk;


    @Override
    protected int setLayoutId() {
        return R.layout.welcome_guide_activity;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSwipeBackEnable(false);
        setOverrideExitAniamtion(false);
        bundle = this.getIntent().getExtras();
        if (bundle != null) {
            pageJumpType = bundle.getInt(CommonConst.JUMPTYPE, 0);
        }
//        mActionBar.hide();
//        setContentView(R.layout.welcome_guide_activity);
//        tvJump = (TextView) findViewById(R.id.tvJump);
        viewPager = (ViewPager) findViewById(R.id.pager);
        final WelComePagerAdapter mAdapter = new WelComePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
        mIndicator.setPageColor(ContextCompat.getColor(this, R.color.guide_page_color));// 底部全部
        mIndicator.setFillColor(ContextCompat.getColor(this, R.color.guide_fill_color));// 选中
        mIndicator.setSnap(true);

        mIndicator.setViewPager(viewPager);
        mIndicator.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    default:
                        if (oldPos < arg0) {
                            // 往右边滑动
                            right = true;
                            left = false;
                        } else if (oldPos > arg0) {
                            // 往左边滑动
                            left = true;
                            right = false;

                        }
                        BaseWelComeFragment f = (BaseWelComeFragment) mAdapter.instantiateItem(viewPager, arg0);
                        f.changeView(left, right);

                        left = right = false;
                        oldPos = arg0;
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

        btnOk = (TextView) findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
//        tvJump.setOnClickListener(this);
    }

    public void jumptoPage() {
        if (pageJumpType == 0) {
            PageJumpUtil.finishCurr(GuideActivity.this);
        } else {
            PageJumpUtil.pageJump(GuideActivity.this, HomeActivity.class, null);
            PageJumpUtil.finishCurr(GuideActivity.this);
            overridePendingTransition(R.anim.alpha_from0_to1, R.anim.alpha_from1_to0);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOk:
                jumptoPage();
                break;
        }
    }

    class WelComePagerAdapter extends FragmentPagerAdapter {

        public WelComePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public BaseWelComeFragment instantiateItem(ViewGroup container, int position) {
            BaseWelComeFragment fragment = (BaseWelComeFragment) super.instantiateItem(container, position);
            return fragment;
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0:
                    return DiyGuideFragment1.newInstance();
                case 1:
                    return DiyGuideFragment2.newInstance();
                default:
                    return DiyGuideFragment3.newInstance();
            }
        }

        @Override
        public int getCount() {
            return PAGERNUM;
        }

    }
}
