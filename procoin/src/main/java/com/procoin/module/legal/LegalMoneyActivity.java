package com.procoin.module.legal;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.module.legal.fragment.LegalMoneyOptionalFragment;
import com.procoin.module.legal.fragment.LegalMoneyQuickFragment;
import com.procoin.widgets.NoScrollViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 法币购买
 * <p>
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class LegalMoneyActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    @BindView(R.id.llWayBg)
    LinearLayout llWayBg;

    @BindView(R.id.tvWay1)
    TextView tvWay1;
    @BindView(R.id.tvWay2)
    TextView tvWay2;
    @BindView(R.id.flPager)
    NoScrollViewPager flPager;


    private WayPageAdapter homePageAdapter;

    private Call<ResponseBody> memberRecordListCall;


    @Override
    protected int setLayoutId() {
        return R.layout.legal_money;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
//        mActionBar.setBackgroundDrawable(null);
        tvWay1.setOnClickListener(this);
        tvWay2.setOnClickListener(this);

        flPager = (NoScrollViewPager) findViewById(R.id.flPager);
        homePageAdapter = new WayPageAdapter(getSupportFragmentManager());
        flPager.setAdapter(homePageAdapter);
        flPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
//

        flPager.setOffscreenPageLimit(homePageAdapter.getCount() - 1);

        slideTab(0);
    }

    public void slideTab(int pos) {
        switch (pos) {
            case 0:
                tvWay1.setSelected(true);
                tvWay2.setSelected(false);
                llWayBg.setBackgroundResource(R.drawable.ic_legal_money_0);
                break;

            case 1:
                tvWay1.setSelected(false);
                tvWay2.setSelected(true);
                llWayBg.setBackgroundResource(R.drawable.ic_legal_money_1);
                break;
        }
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(homePageAdapter!=null&&flPager!=null){
            Object object=homePageAdapter.instantiateItem(flPager,0);
            if (object != null && object instanceof LegalMoneyQuickFragment) {
                LegalMoneyQuickFragment legalMoneyQuickFragment = (LegalMoneyQuickFragment) object;
                legalMoneyQuickFragment.refresh();
            }
        }
    }

//
//    PopupWindow pop;
//    TextView tvAll, tvIn, tvOut;
//
//    private void showPopupMenu(View parent) {
//        if (pop == null) {
//            View view = InflaterUtils.inflateView(this, R.layout.points_details_menu);
//
//            tvAll = view.findViewById(R.id.tvAll);
//            tvIn = view.findViewById(R.id.tvIn);
//            tvOut = view.findViewById(R.id.tvOut);
//
//            tvAll.setOnClickListener(this);
//            tvIn.setOnClickListener(this);
//            tvOut.setOnClickListener(this);
//
//            pop = new PopupWindow(view, DensityUtil.dip2px(this, 140), ViewGroup.LayoutParams.WRAP_CONTENT);//
//
//            pop.setOutsideTouchable(false);
//            pop.setFocusable(false);//
//            pop.setOutsideTouchable(true);
//            pop.setFocusable(true);
//            pop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.transparent)));
//            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                }
//            });
//
//        }
//        if (pop != null && !pop.isShowing()) {
////            pop.showAsDropDown(parent);
//            pop.showAsDropDown(parent, 0, 0);
////            pop.showAtLocation(parent,Gravity.CENTER,50,50);
//        }
//    }

//    private void dissPop() {
//        if (pop != null & pop.isShowing()) {
//            pop.dismiss();
//        }
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvWay1:
                if (flPager.getCurrentItem() != 0) {
                    flPager.setCurrentItem(0);
                }
                break;
            case R.id.tvWay2:
                if (flPager.getCurrentItem() != 1) {
                    flPager.setCurrentItem(1);
                }
                break;
        }
    }



    private class WayPageAdapter extends FragmentPagerAdapter {


        public WayPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
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

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return LegalMoneyQuickFragment.newInstance();
                default:
                    return LegalMoneyOptionalFragment.newInstance();
            }


        }
    }


}
