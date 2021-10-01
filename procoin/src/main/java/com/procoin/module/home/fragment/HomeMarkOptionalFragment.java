package com.procoin.module.home.fragment;

import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.adapter.HomeMarketAdapter;
import com.procoin.module.home.entity.Market;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.TjrMinuteTaskPool;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 我的自选
 * Created by zhengmj on 19-3-8.
 */

public class HomeMarkOptionalFragment extends UserBaseFragment implements View.OnClickListener {
    @BindView(R.id.rvOptionalList)
    RecyclerView rvOptionalList;
    @BindView(R.id.llOptionalNodata)
    LinearLayout llOptionalNodata;

    @BindView(R.id.ivSort1)
    AppCompatImageView ivSort1;
    @BindView(R.id.llSort1)
    LinearLayout llSort1;
    @BindView(R.id.ivSort2)
    AppCompatImageView ivSort2;
    @BindView(R.id.llSort2)
    LinearLayout llSort2;
    @BindView(R.id.ivSort3)
    AppCompatImageView ivSort3;
    @BindView(R.id.llSort3)
    LinearLayout llSort3;

    private HomeMarketAdapter homeMarketAdapter;

    private Call<ResponseBody> getHomeOptionalCall;

    private String symbols = "";

    private Gson gsonMark = new Gson();
    private boolean isRun = false;//定时器是否在跑
    private TjrMinuteTaskPool tjrMinuteTaskPool;


    private Handler handler = new Handler();

    private String tab = "optional";//optional自选、digital数字货币、stock股指期货

    //排序用到
    private int sortField = 0;//1：symbol,2：price,3：rate
    private int sortType = 0;//0默认 1 倒序 2正序

    public static HomeMarkOptionalFragment newInstance(String tab) {
        HomeMarkOptionalFragment fragment = new HomeMarkOptionalFragment();
        Bundle bundle = new Bundle();
        bundle.putString("tab", tab);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("HomeMarkAndKbtFragment", "HomeMarkFragment  onResume   getUserVisibleHint==" + getUserVisibleHint() + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
//            startGetMarket();
            startTimer();
        } else {
            closeTimer();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeMarkAndKbtFragment", "HomeMarkFragment setUserVisibleHint   isVisibleToUser==" + isVisibleToUser + "  getParentFragment().getUserVisibleHint()==" + (getParentFragment() == null ? "null" : getParentFragment().getUserVisibleHint()));
        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
//            startGetMarket();
            startTimer();
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
        if (TextUtils.isEmpty(symbols) || getOptionalFlag()) {
            startGetOptional();
            return;
        }
        if (tjrMinuteTaskPool == null) {
            tjrMinuteTaskPool = new TjrMinuteTaskPool();
        }
        isRun = true;
        tjrMinuteTaskPool.startTime(getActivity(), task);
    }

    private boolean getOptionalFlag() {
        return ((MainApplication) (getActivity().getApplicationContext())).optionalFlag;
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
//                startGetMarket();
                if (!TextUtils.isEmpty(symbols)) {
                    String result = CropymelHttpSocket.getInstance().marketData(symbols, sortField, sortType, tab);
                    setData(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("marketData", "Exception is==" + e);
            }
        }
    };

    private void setData(String result) throws Exception {
        Log.d("marketData", "symbols==" + symbols + "   result==" + result);
        if (!TextUtils.isEmpty(result)) {
            JSONObject jsonObject = new JSONObject(result);
            if (JsonParserUtils.hasAndNotNull(jsonObject, "quotes")) {
                final Group<Market> groupMarket = gsonMark.fromJson(jsonObject.getString("quotes"), new TypeToken<Group<Market>>() {
                }.getType());
                Log.d("marketData", "groupMarket==" + (groupMarket==null?"null":groupMarket.size()) );
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (groupMarket != null && groupMarket.size() > 0) {
                            homeMarketAdapter.setGroup(groupMarket);
                        }
                        int size = (groupMarket == null ? 0 : groupMarket.size());
                        if (!isRun && size > 0) startTimer();
                    }
                });

            }
        }
    }

    public void immersionbar() {
//        Log.d("HomeMarket", "mImmersionBar==" + mImmersionBar + " ll_bar==" + ll_bar);
//        if (mImmersionBar != null && ll_bar != null) {
//            mImmersionBar
//                    .titleBar(ll_bar)
//                    .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
//                    .init();
//        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_optional, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("tab")) {
            tab = bundle.getString("tab");
            Log.d("MarkOptionalFragment", "tab==" + tab);
        }
        tjrMinuteTaskPool = new TjrMinuteTaskPool();

        homeMarketAdapter = new HomeMarketAdapter(getActivity(), 1);
        rvOptionalList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvMarketList.addItemDecoration(new SimpleRecycleDivider(getActivity(), true));
        rvOptionalList.setAdapter(homeMarketAdapter);
        llSort1.setOnClickListener(this);
        llSort2.setOnClickListener(this);
        llSort3.setOnClickListener(this);
//        startGetOptional();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void startGetOptional() {
        CommonUtil.cancelCall(getHomeOptionalCall);
        getHomeOptionalCall = VHttpServiceManager.getInstance().getVService().optionalCoinFind();
        getHomeOptionalCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    ((MainApplication) (getActivity().getApplicationContext())).optionalFlag = false;
                    symbols = "";
                    String[] symbolArrays = resultData.getStringArray("symbols");
                    StringBuilder stringBuilder = null;
                    for (String s : symbolArrays) {
                        if (stringBuilder == null) {
                            stringBuilder = new StringBuilder(s);
                        } else {
                            stringBuilder.append(",");
                            stringBuilder.append(s);
                        }
                    }
                    if (stringBuilder != null) {
                        symbols = stringBuilder.toString();
                    }
//                    symbols = resultData.getItem("symbols", String.class);
                    if (!TextUtils.isEmpty(symbols)) {
                        rvOptionalList.setVisibility(View.VISIBLE);
                        llOptionalNodata.setVisibility(View.GONE);
                        startTimer();
                    } else {
                        rvOptionalList.setVisibility(View.GONE);
                        llOptionalNodata.setVisibility(View.VISIBLE);
                    }
//                    CommonUtil.showmessage(symbols,getActivity());
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llSort1:
                if (sortField != 1) {
                    sortField = 1;
                    sortType = 2;//默认正序
                } else {
                    skipSortType();
                }
                setIconAndSort();

                break;
            case R.id.llSort2:
                if (sortField != 2) {
                    sortField = 2;
                    sortType = 1;//默认倒序
                } else {
                    skipSortType();
                }
                setIconAndSort();
                break;
            case R.id.llSort3:
                if (sortField != 3) {
                    sortField = 3;
                    sortType = 1;//默认倒序
                } else {
                    skipSortType();
                }
                setIconAndSort();
                break;
        }
    }


    private void skipSortType() {
        if (sortType == 1) {
            sortType = 2;
        } else if (sortType == 2) {
            sortType = 0;
        } else {
            sortType = 1;
        }
    }

    private void setIconAndSort() {

        if (sortType == 0) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
        } else {
            if (sortField == 1) {
                ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
                ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
                if (sortType == 1) {
                    ivSort1.setImageResource(R.drawable.ic_svg_sort_down);
                } else {
                    ivSort1.setImageResource(R.drawable.ic_svg_sort_up);
                }
            } else if (sortField == 2) {
                ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
                ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
                if (sortType == 1) {
                    ivSort2.setImageResource(R.drawable.ic_svg_sort_down);
                } else {
                    ivSort2.setImageResource(R.drawable.ic_svg_sort_up);
                }
            } else {
                ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
                ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
                if (sortType == 1) {

                    ivSort3.setImageResource(R.drawable.ic_svg_sort_down);
                } else {
                    ivSort3.setImageResource(R.drawable.ic_svg_sort_up);
                }
            }
        }
//        handler.post(task);
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> e) throws Exception {
                String result = CropymelHttpSocket.getInstance().marketData(symbols, sortField, sortType, tab);
                if (!TextUtils.isEmpty(result)) {
                    e.onNext(result);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String result) throws Exception {
                        setData(result);
                    }
                });

    }



}
