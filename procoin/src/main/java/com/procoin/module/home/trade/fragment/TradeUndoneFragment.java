package com.procoin.module.home.trade.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.entity.Order;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.home.trade.CoinTradeDetailsActivity;
import com.procoin.module.home.trade.adapter.TradeUndoneAdapter;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.google.gson.reflect.TypeToken;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 币币交易记录（未完成）
 * Created by zhengmj on 17-12-7.
 */

public class TradeUndoneFragment extends UserBaseFragment {
    private boolean hasOnpause = false;
    private int pageNo = 1;
    private int pageSize = 15;
    private LoadMoreRecycleView listViewAutoLoadMore;
    private Call<ResponseBody> realCall;
    private TradeUndoneAdapter coinTradeEntrustAdapter;

    private String state = "";//state: null:全部，0：待付款，1：已标记付款，2：已完成，-10：已失效
    private String symbol = "";
    private String buySell;//1买 -1卖  null全部
//    private int isDone;//(0：未完成 ，1：历史记录)

    public static TradeUndoneFragment newInstance(String symbol, String state, int isDone, String buySell) {
        TradeUndoneFragment f = new TradeUndoneFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.STATE, state);
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putString(CommonConst.BUYSELL, buySell);
        bundle.putInt(CommonConst.ISDONE, isDone);
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
        buySell = b.getString(CommonConst.BUYSELL, "");
//        isDone = b.getInt(CommonConst.ISDONE, 0);

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
        if (resultCode == 0x123) {
            if (data != null) {
                int pos = data.getIntExtra(CommonConst.POS, -1);
                if (pos >= 0) {
                    coinTradeEntrustAdapter.removeItem(pos);

//                    Order order = coinTradeEntrustAdapter.getItem(pos);
//                    order.state = OrderStateEnum.canceled.getState();
//                    coinTradeEntrustAdapter.notifyDataSetChanged();
                }
            }
        }


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
        coinTradeEntrustAdapter = new TradeUndoneAdapter(getActivity());
        listViewAutoLoadMore.setAdapter(coinTradeEntrustAdapter);
        coinTradeEntrustAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        coinTradeEntrustAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
//                CoinTradeDetailsActivity.pageJump(context,data.orderId);
                Order order = (Order) t;
                Bundle bundle = new Bundle();
                bundle.putLong(CommonConst.ORDERID, order.orderId);
                bundle.putInt(CommonConst.POS, pos);
                PageJumpUtil.pageJumpResult(TradeUndoneFragment.this, CoinTradeDetailsActivity.class, bundle);
            }
        });
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
        realCall = VHttpServiceManager.getInstance().getVService().tradeOrderList(symbol, state, 0, buySell, pageNo);
        realCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<Order> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<Order>>() {
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
