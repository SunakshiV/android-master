package com.procoin.module.copy.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.copy.adapter.CopyHoldMarketValueAdapter;
import com.procoin.module.copy.adapter.CopyPieChartAdapter;
import com.procoin.module.copy.entity.CopyOrderPieChart;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.PieChartView;
import com.procoin.widgets.SimpleRecycleDivider;
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

public class HoldMarketValueFragment extends UserBaseFragment {

    @BindView(R.id.tvTolMarket)
    TextView tvTolMarket;
    @BindView(R.id.tvTolMarketCny)
    TextView tvTolMarketCny;
    @BindView(R.id.pieChartView)
    PieChartView pieChartView;
    @BindView(R.id.rvPieChartList)
    RecyclerView rvPieChartList;
    @BindView(R.id.rv_list)
    RecyclerView rvList;

    private boolean flag;

    private Call<ResponseBody> holdMarketValueCall;

    private CopyPieChartAdapter copyPieChartAdapter;

    private CopyHoldMarketValueAdapter copyHoldMarketValueAdapter;

    public static HoldMarketValueFragment newInstance() {
        HoldMarketValueFragment f = new HoldMarketValueFragment();
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !flag) {
            startHoldMarketValueCall();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_hold_market_value, container, false);
        Log.d("HoldMarketValueFragment", "getUserVisibleHint==" + getUserVisibleHint());
        ButterKnife.bind(this, v);
        copyPieChartAdapter = new CopyPieChartAdapter(getActivity());
        rvPieChartList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPieChartList.setAdapter(copyPieChartAdapter);


        copyHoldMarketValueAdapter = new CopyHoldMarketValueAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity()));
        rvList.setAdapter(copyHoldMarketValueAdapter);


        return v;
    }


    private void startHoldMarketValueCall() {
        CommonUtil.cancelCall(holdMarketValueCall);
        holdMarketValueCall = VHttpServiceManager.getInstance().getVService().holdMarketValue();
        holdMarketValueCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    flag=true;
                    Group<CopyOrderPieChart> group = resultData.getGroup("holdList", new TypeToken<Group<CopyOrderPieChart>>() {
                    }.getType());
                    copyHoldMarketValueAdapter.setGroup(group);

                    Group<CopyOrderPieChart> group2 = resultData.getGroup("chartList", new TypeToken<Group<CopyOrderPieChart>>() {
                    }.getType());

                    if (group2 != null && group2.size() > 0) {
                        float[] data = new float[group2.size()];
                        for (int i = 0, m = group2.size(); i < m; i++) {
                            data[i] = group2.get(i).rate;
                        }
                        Log.d("CropyOrderInfo", "data==" + data.toString());
                        pieChartView.setData(data);
                        pieChartView.startAnimation(1500);

                        copyPieChartAdapter.setGroup(group2);

                    }

                    String totalMarketValue = resultData.getItem("totalMarketValue", String.class);
                    String totalMarketValueCny = resultData.getItem("totalMarketValueCny", String.class);

                    tvTolMarket.setText("总市值： " + totalMarketValue + " USDT");
                    tvTolMarketCny.setText(totalMarketValueCny);
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
