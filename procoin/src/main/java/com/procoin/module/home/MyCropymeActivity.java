//package com.coingo.module.home;
//
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.TextUtils;
//import android.util.Log;
//import android.util.TypedValue;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.LinearLayout;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//
//import ResultData;
//import CopyPositionAdapter;
//import CoinTradeCategoryAdapter;
//import TrendFragment;
//import CommonUtil;
//import MyCallBack;
//import CircleImageView;
//import RadarView;
//import com.coingo.R;
//import TJRBaseToolBarSwipeBackActivity;
//import Group;
//import VHttpServiceManager;
//import TjrImageLoaderUtil;
//import Position;
//import SubUser;
//import CoinTradeCount;
//import InflaterUtils;
//import PageJumpUtil;
//import com.google.gson.reflect.TypeToken;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//
///**
// * 我的操作台 (已废弃，现在都跳到UserHomeActivity)
// * <p>
// * Created by zhengmj on 18-10-10.
// */
//
//public class MyCropymeActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
//
//
//    @BindView(R.id.ivhead)
//    CircleImageView ivhead;
//    @BindView(R.id.tvName)
//    TextView tvName;
//    @BindView(R.id.tvUserBrief)
//    TextView tvUserBrief;
//    @BindView(R.id.tvGrade)
//    TextView tvGrade;
//    @BindView(R.id.tvProfit)
//    TextView tvProfit;
//    @BindView(R.id.rdv)
//    RadarView rdv;
//    @BindView(R.id.tvNoHold)
//    TextView tvNoHold;
//    @BindView(R.id.llHold)
//    LinearLayout llHold;
//    @BindView(R.id.rvPositionList)
//    RecyclerView rvPositionList;
//    @BindView(R.id.tvTab1)
//    TextView tvTab1;
//    @BindView(R.id.tvTab2)
//    TextView tvTab2;
//    @BindView(R.id.tvTab3)
//    TextView tvTab3;
//    @BindView(R.id.tvTab4)
//    TextView tvTab4;
//    @BindView(R.id.vp_content)
//    ViewPager vpContent;
//    @BindView(R.id.rv_list)
//    RecyclerView rvList;
//    @BindView(R.id.llRadar)
//    LinearLayout llRadar;
//
//    @BindView(R.id.llProfitMark)
//    LinearLayout llProfitMark;
//
//
//
//    private Call<ResponseBody> personalConsoleCall;
//    private SubUser subUser;
//
//    private CoinTradeCategoryAdapter coinTradeCategoryAdapter;
//    private CopyPositionAdapter copyPositionAdapter;
//    private TjrImageLoaderUtil tjrImageLoaderUtil;
//
//    private MyPagerAdapter adapter;
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.my_cropyme;
//    }
//
//    @Override
//    protected String getActivityTitle() {
//        return "我的CORPYME";
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        setContentView(R.layout.my_cropyme);
//        ButterKnife.bind(this);
//
//        tjrImageLoaderUtil = new TjrImageLoaderUtil();
//
//        coinTradeCategoryAdapter = new CoinTradeCategoryAdapter(this);
//        rvList.setLayoutManager(new LinearLayoutManager(this));
////        rvAttentionList.addItemDecoration(new SimpleRecycleDivider(getActivity(), true));
//        rvList.setAdapter(coinTradeCategoryAdapter);
//
//
//        copyPositionAdapter = new CopyPositionAdapter(this);
//        rvPositionList.setLayoutManager(new LinearLayoutManager(this));
//        rvPositionList.setAdapter(copyPositionAdapter);
//
//        adapter = new MyPagerAdapter(getSupportFragmentManager());
//        vpContent.setAdapter(adapter);
//        vpContent.setOffscreenPageLimit(adapter.getCount() - 1);
//        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int i, float v, int i1) {
//
//            }
//
//            @Override
//            public void onPageSelected(int i) {
//                Log.d("slide", "onPageSelected  i==" + i);
//                slideTab(i);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int i) {
//
//            }
//        });
//
//        tvTab1.setOnClickListener(this);
//        tvTab2.setOnClickListener(this);
//        tvTab3.setOnClickListener(this);
//        tvTab4.setOnClickListener(this);
//
//        llRadar.setOnClickListener(this);
//
//        llProfitMark.setOnClickListener(this);
////        llProfitMark.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View v, MotionEvent event) {
////                switch (event.getAction()) {
////                    case MotionEvent.ACTION_DOWN:
////                        showPopupMenu(llProfitMark,getScreenWidth()*2/3, ViewGroup.LayoutParams.WRAP_CONTENT);
////                        break;
////                    case MotionEvent.ACTION_UP:
////                    case MotionEvent.ACTION_CANCEL:
////                        dimissPop();
////                }
////                return true;
////            }
////        });
//
//        slideTab(0);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tvTab1:
//                if (vpContent.getCurrentItem() != 0) {
//                    vpContent.setCurrentItem(0, false);
//                }
//                break;
//            case R.id.tvTab2:
//                if (vpContent.getCurrentItem() != 1) {
//                    vpContent.setCurrentItem(1, false);
//                }
//                break;
//            case R.id.tvTab3:
//                if (vpContent.getCurrentItem() != 2) {
//                    vpContent.setCurrentItem(2, false);
//                }
//                break;
//            case R.id.tvTab4:
//                if (vpContent.getCurrentItem() != 3) {
//                    vpContent.setCurrentItem(3, false);
//                }
//                break;
//            case R.id.llRadar:
//                if (subUser != null && subUser.radarChart != null) {
//                    getApplicationContext().radarChart = subUser.radarChart;
//                }
//                PageJumpUtil.pageJump(MyCropymeActivity.this, RadarDetailsActivity.class);
//                break;
//            case R.id.llProfitMark:
//                showPopupMenu(llProfitMark,getScreenWidth()/2, ViewGroup.LayoutParams.WRAP_CONTENT);
//                break;
//        }
//    }
//    PopupWindow pop;
//
//    private void showPopupMenu(View parent,int width, int height) {
//        if (pop == null) {
//            pop = new PopupWindow(InflaterUtils.inflateView(this,R.layout.profit_mark), width, height);//
////            pop.setOutsideTouchable(false);
////            pop.setFocusable(false);// 如果不加这个，Grid不会响应ItemClick
//            pop.setOutsideTouchable(true);
//            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));// 特别留意这个东东
//            pop.setFocusable(true);// 如果不加这个，Grid不会响应ItemClick
////            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
////                @Override
////                public void onDismiss() {
////                }
////            });
//        }
//        if (pop != null && !pop.isShowing()) {
////            pop.showAsDropDown(parent);
//            pop.showAsDropDown(parent,0,50);
////            pop.showAtLocation(parent,Gravity.CENTER,50,50);
//        }
//    }
//    private void dimissPop(){
//        if(pop!=null&&pop.isShowing()){
//            pop.dismiss();
//        }
//    }
//
//
//    private void slideTab(int arg0) {
////        currentTab = arg0;
//        switch (arg0) {
//            case 0:
//                tvTab1.setSelected(true);
//                tvTab2.setSelected(false);
//                tvTab3.setSelected(false);
//                tvTab4.setSelected(false);
//                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                break;
//            case 1:
//                tvTab1.setSelected(false);
//                tvTab2.setSelected(true);
//                tvTab3.setSelected(false);
//                tvTab4.setSelected(false);
//                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                break;
//            case 2:
//                tvTab1.setSelected(false);
//                tvTab2.setSelected(false);
//                tvTab3.setSelected(true);
//                tvTab4.setSelected(false);
//                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                tvTab4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                break;
//            case 3:
//                tvTab1.setSelected(false);
//                tvTab2.setSelected(false);
//                tvTab3.setSelected(false);
//                tvTab4.setSelected(true);
//                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
//                tvTab4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
//                break;
//        }
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        startPersonalHome();
//    }
//
//    private void startPersonalHome() {
//        CommonUtil.cancelCall(personalConsoleCall);
//        personalConsoleCall = VHttpServiceManager.getInstance().getVService().personalConsole();
//        personalConsoleCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//
//                    subUser = resultData.getObject("userRadar", SubUser.class);
//                    String tolProfit = resultData.getItem("tolProfit", String.class);
//                    setData(tolProfit);
//
//                    Group<CoinTradeCount> group = resultData.getGroup("trades", new TypeToken<Group<CoinTradeCount>>() {
//                    }.getType());
//                    coinTradeCategoryAdapter.setGroup(group);
//
//                    Group<Position> groupHold = resultData.getGroup("holdList", new TypeToken<Group<Position>>() {
//                    }.getType());
//                    if (groupHold != null && groupHold.size() > 0) {
//                        tvNoHold.setVisibility(View.GONE);
//                        llHold.setVisibility(View.VISIBLE);
//                        rvPositionList.setVisibility(View.VISIBLE);
//                        copyPositionAdapter.setGroup(groupHold);
//                    } else {
//                        tvNoHold.setVisibility(View.VISIBLE);
//                        llHold.setVisibility(View.GONE);
//                        rvPositionList.setVisibility(View.GONE);
//                    }
//
//                }
//
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//            }
//        });
//    }
//
//    private void setData(String tolProfit) {
//        tvProfit.setText(tolProfit);
//
//        if (subUser != null) {
//
//
//            if (getApplicationContext().getUser() != null) {
//                tvName.setText(getApplicationContext().getUser().userName);
//                tjrImageLoaderUtil.displayImageForHead(getApplicationContext().getUser().headUrl, ivhead);
//                tvUserBrief.setText(getApplicationContext().getUser().describes);
//            }
//            tvGrade.setText(subUser.score);
//            if (subUser.radarChart != null) {
//
//                if(subUser.radarChart.scoreData!=null){
//                    List<Double> data = new ArrayList<>();
//                    data.add(Double.parseDouble(TextUtils.isEmpty(subUser.radarChart.scoreData.copyBalanceScore)?"0.00":subUser.radarChart.scoreData.copyBalanceScore));
//                    data.add(Double.parseDouble(TextUtils.isEmpty(subUser.radarChart.scoreData.tolIncomeScore)?"0.00":subUser.radarChart.scoreData.tolIncomeScore));
//                    data.add(Double.parseDouble(TextUtils.isEmpty(subUser.radarChart.scoreData.copyRateScore)?"0.00":subUser.radarChart.scoreData.copyRateScore));
//                    data.add(Double.parseDouble(TextUtils.isEmpty(subUser.radarChart.scoreData.profitShareScore)?"0.00":subUser.radarChart.scoreData.profitShareScore));
//                    data.add(Double.parseDouble(TextUtils.isEmpty(subUser.radarChart.scoreData.copyNumScore)?"0.00":subUser.radarChart.scoreData.copyNumScore));
//                    rdv.setData(data);
//                }
//
////                List<Double> data = new ArrayList<>();
////                data.add(Double.parseDouble(subUser.radarChart.scoreData.tolIncomeScore));
////                data.add(Double.parseDouble(subUser.radarChart.scoreData.copyRateScore));
////                data.add(Double.parseDouble(subUser.radarChart.scoreData.profitShareScore));
////                data.add(Double.parseDouble(subUser.radarChart.scoreData.copyNumScore));
////                data.add(Double.parseDouble(subUser.radarChart.scoreData.copyBalanceScore));
////                rdv.setData(data);
//
//                ArrayList<String> titles = new ArrayList<>();
//                titles.add("跟单盈利额\n");
//                titles.add("盈利能力\n");
//                titles.add("跟单收益率\n" );
//                titles.add("跟单胜率\n" );
//                titles.add("人气指数\n");
//
//                ArrayList<String> titleValues = new ArrayList<>();
//                titleValues.add(subUser.radarChart.copyBalance );
//                titleValues.add( subUser.radarChart.tolIncomeRate + "%");
//                titleValues.add( subUser.radarChart.copyRate + "%");
//                titleValues.add( subUser.radarChart.profitShare + "%");
//                titleValues.add( String.valueOf(subUser.radarChart.copyNum));
//                rdv.setTitles(titles,titleValues);
//
//            }
//
//        }
//    }
//
//
//    class MyPagerAdapter extends FragmentPagerAdapter {
//        public MyPagerAdapter(FragmentManager fm) {
//            super(fm);
//        }
//
//        @Override
//        public int getCount() {
//            return 4;
//        }
//
//        //        @Override
////        public CharSequence getPageTitle(int position) {
////            return position == 0 ? "　出售中　" : "　未上架　";
////        }
//        @Override
//        public Fragment getItem(int i) {
//            return i == 0 ? TrendFragment.newInstance(getUserIdLong(), 4) : TrendFragment.newInstance(getUserIdLong(), i);
//
//        }
//    }
//
//}
