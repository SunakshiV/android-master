package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.adapter.UsdtTradeHisAdapter;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 17-12-7.
 */

public class UsdtOrCashTradeHisFragment extends UserBaseFragment {
    private boolean hasOnpause = false;
    private int pageNo = 1;
    private int pageSize = 15;
    private LoadMoreRecycleView listViewAutoLoadMore;
    private Call<ResponseBody> realCall;
    private UsdtTradeHisAdapter usdtTradeHisAdapter;

    private String state = "";//state: 0：待付款，1：已标记付款，null:全部
    private String symbol = "";
    private int isDone;//(0：未完成 ，1：历史记录)

    private int type;//1:充值提现， 2：买币(用于过滤USDT记录)

    public static UsdtOrCashTradeHisFragment newInstance(String symbol, String state, int isDone, int type) {
        UsdtOrCashTradeHisFragment f = new UsdtOrCashTradeHisFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.STATE, state);
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.ISDONE, isDone);
        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;
        state = b.getString(CommonConst.STATE, "");
        symbol = b.getString(CommonConst.SYMBOL, "USDT");
        isDone = b.getInt(CommonConst.ISDONE, 0);
        type = b.getInt(CommonConst.KEY_EXTRAS_TYPE, 1);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser && listViewAutoLoadMore != null && onSale == 1) {
//            MainApplication mainApplication = ((MainApplication) getActivity().getApplicationContext());
//            if (mainApplication.projectAdd) {//上架
//                mainApplication.projectAdd = false;
//                pageNo = 1;
//                startGetMyUserProjectList();
//            }
//        }
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void notifyData(){
        if(usdtTradeHisAdapter!=null)usdtTradeHisAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_usdt_trade_his, container, false);
        listViewAutoLoadMore = (LoadMoreRecycleView) v.findViewById(R.id.rv_list);
//        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swiperefreshlayout);
        listViewAutoLoadMore.setLayoutManager(new LinearLayoutManager(getActivity()));
        listViewAutoLoadMore.addItemDecoration(new SimpleRecycleDivider(getActivity(), 15, 15));

//        listViewAutoLoadMore.setAdapter(adapter);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                startfindEntrustbslist();
//            }
//        });
        usdtTradeHisAdapter = new UsdtTradeHisAdapter(getActivity(), getUser() == null ? 0 : getUser().getUserId());
        listViewAutoLoadMore.setAdapter(usdtTradeHisAdapter);
        usdtTradeHisAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(loadMoreCallBack);

        return v;
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (usdtTradeHisAdapter != null && usdtTradeHisAdapter.getRealItemCount() > 0) {
                startGetTradeHistoryList();
            } else {
                pageNo = 1;
                startGetTradeHistoryList();
            }
        }
    };


    public void refresh() {
        pageNo = 1;
        if (listViewAutoLoadMore != null) {
            listViewAutoLoadMore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetTradeHistoryList();
                }
            }, 500);
        }
    }


    private void startGetTradeHistoryList() {
        CommonUtil.cancelCall(realCall);
        realCall = VHttpServiceManager.getInstance().getVService().tradeHistory(symbol, state, isDone, pageNo, type);
        realCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<OrderCash> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<OrderCash>>() {}.getType());
//                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            usdtTradeHisAdapter.setGroup(group);
                        } else {
                            usdtTradeHisAdapter.addItem(group);
                            usdtTradeHisAdapter.notifyDataSetChanged();
                        }
//                    }
                    pageNo++;
                }
                usdtTradeHisAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                usdtTradeHisAdapter.onLoadComplete(false, false);
            }
        });
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }


}
