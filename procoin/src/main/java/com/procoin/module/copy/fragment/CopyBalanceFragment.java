package com.procoin.module.copy.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.procoin.common.entity.ResultData;
import com.procoin.module.copy.adapter.CopyBalanceAdapter;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.copy.adapter.CopyPieChartAdapter;
import com.procoin.module.copy.entity.CopyBalance;
import com.procoin.module.copy.entity.CopyOrderPieChart;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.PieChartView;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单资金
 * <p>
 * Created by zhengmj on 17-12-7.
 */

public class CopyBalanceFragment extends UserBaseFragment implements View.OnClickListener {

    @BindView(R.id.pieChartView)
    PieChartView pieChartView;
    @BindView(R.id.rvPieChartList)
    RecyclerView rvPieChartList;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.rlTolCost)
    RelativeLayout rlTolCost;
    @BindView(R.id.rlEnableBalance)
    RelativeLayout rlEnableBalance;
    @BindView(R.id.rlNextEnableBalance)
    RelativeLayout rlNextEnableBalance;
    @BindView(R.id.rlProfit)
    RelativeLayout rlProfit;
    @BindView(R.id.ivSort1)
    AppCompatImageView ivSort1;
    @BindView(R.id.ivSort2)
    AppCompatImageView ivSort2;
    @BindView(R.id.ivSort3)
    AppCompatImageView ivSort3;
    @BindView(R.id.ivSort4)
    AppCompatImageView ivSort4;
    private Call<ResponseBody> copyBalanceCall;

    private CopyPieChartAdapter copyPieChartAdapter;

    private CopyBalanceAdapter copyBalanceAdapter;
    private boolean flag;

    private int sortType = 0;

    public static CopyBalanceFragment newInstance() {
        CopyBalanceFragment f = new CopyBalanceFragment();
        return f;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_copy_balance, container, false);
        Log.d("CopyBalanceFragment", "getUserVisibleHint==" + getUserVisibleHint());
        ButterKnife.bind(this, v);

        copyPieChartAdapter = new CopyPieChartAdapter(getActivity());
        rvPieChartList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvPieChartList.setAdapter(copyPieChartAdapter);


        copyBalanceAdapter = new CopyBalanceAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.addItemDecoration(new SimpleRecycleDivider(getActivity()));
        rvList.setAdapter(copyBalanceAdapter);

        rlTolCost.setOnClickListener(this);
        rlEnableBalance.setOnClickListener(this);
        rlNextEnableBalance.setOnClickListener(this);
        rlProfit.setOnClickListener(this);
        return v;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !flag) {
            startCopyBalanceCall();
        }
    }


    private void startCopyBalanceCall() {
        CommonUtil.cancelCall(copyBalanceCall);
        copyBalanceCall = VHttpServiceManager.getInstance().getVService().copyBalance();
        copyBalanceCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    flag = true;
                    Group<CopyBalance> group = resultData.getGroup("copyBalanceList", new TypeToken<Group<CopyBalance>>() {
                    }.getType());
                    copyBalanceAdapter.setGroup(group);


                    String usedBalanceRate = resultData.getItem("usedBalanceRate", String.class);
                    String usableBalanceRate = resultData.getItem("usableBalanceRate", String.class);
                    Group<CopyOrderPieChart> copyOrderPieCharts = new Group<>();
                    CopyOrderPieChart copyOrderPieChart = new CopyOrderPieChart("可用", Float.parseFloat(usableBalanceRate));
                    CopyOrderPieChart copyOrderPieChart2 = new CopyOrderPieChart("已用", Float.parseFloat(usedBalanceRate));
                    copyOrderPieCharts.add(copyOrderPieChart);
                    copyOrderPieCharts.add(copyOrderPieChart2);
                    copyPieChartAdapter.setGroup(copyOrderPieCharts);

                    float[] data = new float[2];
                    data[0] = Float.parseFloat(usableBalanceRate);
                    data[1] = Float.parseFloat(usedBalanceRate);
                    pieChartView.setData(data);
                    pieChartView.startAnimation(1500);


                }
            }
        });
    }


    private void setSortType(int type) {
        if (sortType == 0) {
            sortType = type;
        } else if (sortType == type) {
            sortType = -type;
        } else if (sortType == -type) {
            sortType = 0;
        } else {
            sortType = type;
        }
        if (sortType == 0) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == 1) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_up);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == -1) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_down);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == 2) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_up);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == -2) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_down);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == 3) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_up);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == -3) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_down);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_default);
        } else if (sortType == 4) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_up);
        } else if (sortType == -4) {
            ivSort1.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort2.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort3.setImageResource(R.drawable.ic_svg_sort_default);
            ivSort4.setImageResource(R.drawable.ic_svg_sort_down);
        }
        copyBalanceAdapter.notifyDataSetChangedWithComparator(sortType);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlTolCost:
                setSortType(1);
                break;
            case R.id.rlEnableBalance:
                setSortType(2);
                break;
            case R.id.rlNextEnableBalance:
                setSortType(3);
                break;
            case R.id.rlProfit:
                setSortType(4);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
