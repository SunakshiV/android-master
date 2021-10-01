package com.procoin.module.home.trade.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.entity.Position;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.home.trade.adapter.TradeUndoneLeverAdapter;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单记录-杠杆（未完成）（跟TradeUndoneLeverFragment比，只是接口不一样）
 * Created by zhengmj on 17-12-7.
 */

public class TradeUndoneLeverFollowFragment extends UserBaseFragment {
    private boolean hasOnpause = false;
    private int pageNo = 1;
    private int pageSize = 15;
    private LoadMoreRecycleView listViewAutoLoadMore;
    private Call<ResponseBody> realCall;
    private TradeUndoneLeverAdapter coinTradeEntrustAdapter;

    private String symbol = "";
    private String accountType = "";

    public static TradeUndoneLeverFollowFragment newInstance(String symbol, String accountType) {
        TradeUndoneLeverFollowFragment f = new TradeUndoneLeverFollowFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(CommonConst.STATE, state);
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putString(CommonConst.ACCOUNTTYPE, accountType);
//        bundle.putInt(CommonConst.ISDONE, isDone);
        f.setArguments(bundle);
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;
        symbol = b.getString(CommonConst.SYMBOL, "USDT");
        accountType = b.getString(CommonConst.ACCOUNTTYPE, "");

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 0x123) {
//            if (data != null) {
//                int pos = data.getIntExtra(CommonConst.POS, -1);
//                if (pos >= 0) {
//                    coinTradeEntrustAdapter.removeItem(pos);
//                }
//            }
//        }


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
        coinTradeEntrustAdapter = new TradeUndoneLeverAdapter(getActivity());
        listViewAutoLoadMore.setAdapter(coinTradeEntrustAdapter);
        coinTradeEntrustAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
//        coinTradeEntrustAdapter.setOnItemClick(new OnItemClick() {
//            @Override
//            public void onItemClickListen(int pos, TaojinluType t) {
//                Position order = (Position) t;
//                LeverInfoActivity.pageJump(getActivity(),order.orderId);
//            }
//        });
        refresh();
        return v;
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (coinTradeEntrustAdapter != null && coinTradeEntrustAdapter.getRealItemCount() > 0) {
                startGetTradeOrderList();
            } else {
                pageNo = 1;
                startGetTradeOrderList();
            }
        }
    };


    public void refresh() {
        pageNo = 1;
        if (listViewAutoLoadMore != null) {
            listViewAutoLoadMore.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startGetTradeOrderList();
                }
            }, 500);
        }
    }


    private void startGetTradeOrderList() {
        CommonUtil.cancelCall(realCall);
        realCall = VHttpServiceManager.getInstance().getVService().queryFollowList(symbol, accountType, 0, "", pageNo, "");
        realCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Position> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<Position>>() {
                    }.getType());
                    if (pageNo == 1) {
                        coinTradeEntrustAdapter.setGroup(group);
                    } else {
                        if (group != null && group.size() > 0) {
                            coinTradeEntrustAdapter.addItem(group);
                            coinTradeEntrustAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                coinTradeEntrustAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                coinTradeEntrustAdapter.onLoadComplete(false, false);
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
