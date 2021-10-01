package com.procoin.module.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.home.adapter.MasterRankAdapter;
import com.procoin.module.home.entity.CropymeAd;
import com.procoin.module.home.entity.CropymeAdType;
import com.procoin.module.home.entity.SubUser;
import com.procoin.util.CommonUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.CycleGalleryViewPager;
import com.procoin.widgets.indicator.CircleIndicator;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jzvd.JzvdStd;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-3-8.
 */

public class HomeCropyMeFragmentBak extends UserBaseImmersionBarFragment implements View.OnClickListener {
    @BindView(R.id.hicvp)
    CycleGalleryViewPager hicvp;
    @BindView(R.id.indicator)
    CircleIndicator indicator;
    @BindView(R.id.llHicvp)
    LinearLayout llHicvp;
//    @BindView(R.id.tvSearch)
//    TextView tvSearch;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    MasterRankAdapter masterRankAdapter;


    private Handler handler = new Handler();
    private static final int BANNERNITERVAL = 5000;//banner自动播放
    private boolean visibile;

    private HomeAdPagerAdapter pagerAdapter;
    private Group<CropymeAd> groupHomeAd = null;

    private Call<ResponseBody> cropymeCall;

    public static HomeCropyMeFragmentBak newInstance() {
        HomeCropyMeFragmentBak fragment = new HomeCropyMeFragmentBak();
        return fragment;
    }


    public void immersionbar() {
//        if (mImmersionBar != null && ll_bar != null) {
//            mImmersionBar
//                    .titleBar(ll_bar)
//                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
//                    .init();
//        }

        mImmersionBar
                .statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA)
                .init();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            startBannerAutoScroll();
        }
        Log.d("onresumeTest", "onResume/////////=" + getClass() + "  getUserVisibleHint==" + getUserVisibleHint());
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("CommunityFragment", "onPause/////////");
        closeTimer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeTimer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("setUserVisibleHint", "isVisibleToUser==" + isVisibleToUser + "      " + getClass());
        visibile = isVisibleToUser;
        if (isVisibleToUser) {
            if (hicvp != null) {
                startBannerAutoScroll();
            }
            startGetCropyme();
        } else {
            closeTimer();
        }
    }

    private void startBannerAutoScroll() {
        if (groupHomeAd != null && groupHomeAd.size() > 1) {
            if (handler != null) {
                handler.removeCallbacks(bannerRunnable);
                handler.postDelayed(bannerRunnable, BANNERNITERVAL);
            }
        } else {
            if (handler != null) {
                handler.removeCallbacks(bannerRunnable);
            }
        }
    }

    private Runnable bannerRunnable = new Runnable() {
        @Override
        public void run() {
            if (hicvp != null && !hicvp.isBeingDraging()) {
                hicvp.setNextItem();
            }
            handler.postDelayed(this, BANNERNITERVAL);

        }
    };

    private void closeTimer() {
        if (handler != null) handler.removeCallbacks(bannerRunnable);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_cropyme, container, false);
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
        ButterKnife.bind(this, view);

//        tvSearch.setOnClickListener(this);

        hicvp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentItem(position % pagerAdapter.getCount());
                startBannerAutoScroll();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        masterRankAdapter = new MasterRankAdapter(getActivity(),getUserId());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setAdapter(masterRankAdapter);
//        rvList.setRecycleViewLoadMoreCallBack(callBack);
//        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity()));
//        masterRankAdapter.setRecycleViewLoadMoreCallBack(callBack);

//        Group<Receipt> receipts = new Group<>();
//        Receipt receipt = null;
//        for (int i = 0; i < 10; i++) {
//            receipt = new Receipt();
//            receipts.add(receipt);
//        }
//
//        masterRankAdapter.setGroup(receipts);

        startGetCropyme();
        return view;
    }

//    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
//        @Override
//        public void loadMore() {
//            if (masterRankAdapter != null && masterRankAdapter.getRealItemCount() > 0) {
//                MyMessage myAnswerEntity=masterRankAdapter.getItem(masterRankAdapter.getItemCount()-2);
//                if(myAnswerEntity!=null){
//                    startGetMyMsgTask();
//                }
//            }
//        }
//    };

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
//            case R.id.tvSearch:
//                PageJumpUtil.pageJump(getActivity(), SearchActivity.class);
//                break;
        }
    }


    class HomeAdPagerAdapter extends PagerAdapter {

        TjrImageLoaderUtil displayImageRound;
        List<CropymeAd> olstarCardList;


        public HomeAdPagerAdapter() {
//            super(getChildFragmentManager());
            displayImageRound = new TjrImageLoaderUtil(R.drawable.ic_common_mic);
        }

        public void setOlstarCardList(List<CropymeAd> olstarCardList) {
            this.olstarCardList = olstarCardList;
//            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return olstarCardList == null ? 0 : olstarCardList.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.market_recommend_item);
            Log.d("instantiateItem", "instantiateItem.......");
            ImageView ivHead = (ImageView) view.findViewById(R.id.ivHead);
//            ivHead.setradis(4, 4, 4, 4);
            ImageView ivCenterPlay = (ImageView) view.findViewById(R.id.ivCenterPlay);
            final CropymeAd item = groupHomeAd.get(position);
            ivCenterPlay.setVisibility(CropymeAdType.isVideo(item.type) ? View.VISIBLE : View.GONE);
            displayImageRound.displayImage(item.imageUrl, ivHead);
            view.setOnClickListener(new OnOlstarCardClickListen(item, position));
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }


        @Override
        public float getPageWidth(int position) {
            return 1f;
        }
    }

    class OnOlstarCardClickListen implements View.OnClickListener {
        CropymeAd item;
        int position;

        public OnOlstarCardClickListen(CropymeAd item, int position) {
            this.item = item;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (hicvp.getCurrentItem() == position) {
//                TjrSocialMTAUtil.trackCustomKVEvent(getActivity(), "推荐卡片点击", "market_recommend_tab_" + String.valueOf(position + 1), TjrSocialMTAUtil.MTAMarketRecommendTabClick);
//                jump2OlstarHomeActivity(item);
                if (CropymeAdType.isVideo(item.type)) {
//                    Bundle bundle = new Bundle();
//                    bundle.putString("previewImgUrl", item.imageUrl);
//                    bundle.putString("videoUrl", item.videoUrl);
//                    PageJumpUtil.pageJump(getActivity(), VideoFullScreenActivity.class, bundle);
                    JzvdStd.startFullscreen(getActivity(), JzvdStd.class, item.videoUrl, "");
                } else {
                    PageJumpUtil.jumpByPkg(getActivity(), item.pkg, item.cls, item.params);
                }
            } else {
                if (hicvp.getCurrentItem() == pagerAdapter.getCount() - 1) {
                    if (position == hicvp.getCurrentItem() - 1) {
                        hicvp.setPrivItem();
                    } else {
                        hicvp.setNextItem();
                    }
                } else if (hicvp.getCurrentItem() == 0) {
                    if (position == hicvp.getCurrentItem() + 1) {
                        hicvp.setNextItem();
                    } else {
                        hicvp.setPrivItem();
                    }
                } else {
                    hicvp.setCurrentItem(position);
                }
            }

        }
    }

    private void startGetCropyme() {
        CommonUtil.cancelCall(cropymeCall);
        cropymeCall = VHttpServiceManager.getInstance().getVService().cropyme();
        cropymeCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<SubUser> group = resultData.getGroup("scoreRank", new TypeToken<Group<SubUser>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        masterRankAdapter.setGroup(group);
                    }

                    groupHomeAd= resultData.getGroup("banner", new TypeToken<Group<CropymeAd>>() {
                    }.getType());

                    if (groupHomeAd != null && groupHomeAd.size() > 0) {
                        llHicvp.setVisibility(View.VISIBLE);
                        pagerAdapter = new HomeAdPagerAdapter();
                        pagerAdapter.setOlstarCardList(groupHomeAd);
                        hicvp.setAdapter(pagerAdapter);
                        hicvp.setNarrowFactor(0.9f);
                        indicator.setCount(groupHomeAd.size());
                        indicator.setCurrentItem(0);
                        startBannerAutoScroll();
                    } else {
                        llHicvp.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }
}
