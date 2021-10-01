package com.procoin.module.copy.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.entity.ResultData;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.util.DensityUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.widgets.piechart.HoldCostChartView;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.copy.adapter.HoldCoinSymbolAdapter;
import com.procoin.module.copy.adapter.HoldCostAdapter;
import com.procoin.module.copy.entity.Distribute;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 持币成本
 * <p>
 * Created by zhengmj on 17-12-7.
 */

public class HoldCostFragment extends UserBaseFragment {

    @BindView(R.id.rvCoin)
    RecyclerView rvCoin;
    @BindView(R.id.tvTolBalance)
    TextView tvTolBalance;
    @BindView(R.id.tvAvgCostPrice)
    TextView tvAvgCostPrice;
    @BindView(R.id.tvProfitRate)
    TextView tvProfitRate;
    @BindView(R.id.rvData)
    RecyclerView rvData;

    @BindView(R.id.holdCostView)
    LinearLayout holdCostView;

    private Call<ResponseBody> copyHoldCostCall;


    private HoldCostAdapter holdCostAdapter;

    private HoldCoinSymbolAdapter holdCoinSymbolAdapter;

    private HoldCostChartView holdCostChartView;

    public static HoldCostFragment newInstance() {
        HoldCostFragment f = new HoldCostFragment();
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hold_cost, container, false);
        Log.d("HoldCostFragment", "getUserVisibleHint==" + getUserVisibleHint());
        startCopyHoldCostCall("");
        ButterKnife.bind(this, v);

        holdCostAdapter = new HoldCostAdapter(getActivity());
        rvData.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvData.addItemDecoration(new SimpleRecycleDivider(getActivity()));
        rvData.setAdapter(holdCostAdapter);

        holdCostChartView = new HoldCostChartView(getActivity());
//        holdCostChartView.setBackgroundColor(Color.GRAY);
        holdCostView.addView(holdCostChartView);

        holdCoinSymbolAdapter = new HoldCoinSymbolAdapter(getActivity());
        rvCoin.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rvCoin.addItemDecoration(new SpaceItemDecoration());
        rvCoin.setAdapter(holdCoinSymbolAdapter);
        holdCoinSymbolAdapter.setOnItemclickListen(new HoldCoinSymbolAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String symbol) {
                resetValue(symbol);
                startCopyHoldCostCall(symbol);
            }
        });
        return v;
    }

    private void resetValue(String symbol) {
        tvTolBalance.setText("总数量: 0.00" + " " + (symbol == null ? "" : symbol));
        tvAvgCostPrice.setText("平均成本价: 0.00");
        tvProfitRate.setText("盈利比例: " + 0.00 + "%");
        holdCostAdapter.clearAllItem();
    }


    private void startCopyHoldCostCall(String symbol) {
        CommonUtil.cancelCall(copyHoldCostCall);
        copyHoldCostCall = VHttpServiceManager.getInstance().getVService().copyHoldCost(symbol);
        copyHoldCostCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String totalAmount = resultData.getItem("totalAmount", String.class);
                    String profitRate = resultData.getItem("profitRate", String.class);
                    String symbol = resultData.getItem("symbol", String.class);
                    String avgCostPrice = resultData.getItem("avgCostPrice", String.class);
                    String price = resultData.getItem("price", String.class);

                    String[] symbolList = resultData.getStringArray("symbolList");
                    if (symbolList != null && symbolList.length > 0) {

                        holdCoinSymbolAdapter.setData(symbolList);
                        holdCoinSymbolAdapter.setSelected(symbol);
                    }

                    Group<Distribute> group = resultData.getGroup("distributeList", new TypeToken<Group<Distribute>>() {
                    }.getType());
                    holdCostAdapter.setGroup(group);

                    Group<Distribute> group2 = resultData.getGroup("chartDistributeList", new TypeToken<Group<Distribute>>() {
                    }.getType());
                    holdCostChartView.setData(group2, price, avgCostPrice);

                    tvTolBalance.setText("总数量: " + totalAmount + " " + (symbol == null ? "" : symbol));
                    tvAvgCostPrice.setText("平均成本价: " + avgCostPrice);
                    tvProfitRate.setText("盈利比例: " + profitRate + "%");


                }
            }
        });
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = DensityUtil.dip2px(getActivity(), 5);
            outRect.right = DensityUtil.dip2px(getActivity(), 5);
            outRect.top = DensityUtil.dip2px(getActivity(), 5);
            outRect.bottom = DensityUtil.dip2px(getActivity(), 5);

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
