package com.procoin.module.myhome.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.entity.TimeTypeEnum;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.myhome.entity.Trend;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.piechart.CardHolderChartView;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 17-12-7.
 */

public class TrendFragment extends UserBaseFragment implements View.OnClickListener {
    @BindView(R.id.tvMonth1)
    TextView tvMonth1;
    @BindView(R.id.tvMonth3)
    TextView tvMonth3;
    @BindView(R.id.tvMonth6)
    TextView tvMonth6;
    @BindView(R.id.tvMonth12)
    TextView tvMonth12;
    @BindView(R.id.cardHolderChartView)
    CardHolderChartView cardHolderChartView;
    private boolean hasOnpause = false;
    Call<ResponseBody> personalTrendNumCall;

    private long targetUid;
    private int type = 1;//1 业绩走势  2跟单人气  3交易次数  4累计盈亏
    private TimeTypeEnum timeTypeEnum = TimeTypeEnum.MONTH;

    public static TrendFragment newInstance(long targetUid, int type) {
        TrendFragment f = new TrendFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.TARGETUID, targetUid);
        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;

        targetUid = b.getLong(CommonConst.TARGETUID, 0);
        type = b.getInt(CommonConst.KEY_EXTRAS_TYPE, 1);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TrendFragment", "setUserVisibleHint    isVisibleToUser=="+isVisibleToUser+ "    type==" + type+"  cardHolderChartView=="+cardHolderChartView);
        if (isVisibleToUser && cardHolderChartView != null) {
            startPersonalTrendNum();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.yeji_trend, container, false);
        ButterKnife.bind(this, v);
        tvMonth1.setOnClickListener(this);
        tvMonth3.setOnClickListener(this);
        tvMonth6.setOnClickListener(this);
        tvMonth12.setOnClickListener(this);
        tvMonth1.setSelected(true);
        tvMonth3.setSelected(false);
        tvMonth6.setSelected(false);
        tvMonth12.setSelected(false);
        Log.d("TrendFragment", "onCreateView==");

        return v;
    }


    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("TrendFragment", "getUserVisibleHint==" + getUserVisibleHint() + "    type==" + type);
        if (getUserVisibleHint()) {
            startPersonalTrendNum();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvMonth1:
                timeTypeEnum = TimeTypeEnum.MONTH;
                tvMonth1.setSelected(true);
                tvMonth3.setSelected(false);
                tvMonth6.setSelected(false);
                tvMonth12.setSelected(false);
                startPersonalTrendNum();
                break;
            case R.id.tvMonth3:
                timeTypeEnum = TimeTypeEnum.MONTH_3;
                tvMonth1.setSelected(false);
                tvMonth3.setSelected(true);
                tvMonth6.setSelected(false);
                tvMonth12.setSelected(false);
                startPersonalTrendNum();
                break;
            case R.id.tvMonth6:
                timeTypeEnum = TimeTypeEnum.MONTH_6;
                tvMonth1.setSelected(false);
                tvMonth3.setSelected(false);
                tvMonth6.setSelected(true);
                tvMonth12.setSelected(false);
                startPersonalTrendNum();
                break;
            case R.id.tvMonth12:
                timeTypeEnum = TimeTypeEnum.YEAR;
                tvMonth1.setSelected(false);
                tvMonth3.setSelected(false);
                tvMonth6.setSelected(false);
                tvMonth12.setSelected(true);
                startPersonalTrendNum();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void startPersonalTrendNum() {
        cardHolderChartView.clearData();
        CommonUtil.cancelCall(personalTrendNumCall);
//        if(type==0){
//            personalTrendNumCall = VHttpServiceManager.getInstance().getVService().personalProfit();
//        }else{
            personalTrendNumCall = VHttpServiceManager.getInstance().getVService().trendChart(targetUid, timeTypeEnum.getTimeType(), type);
//        }
        personalTrendNumCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Trend> group = resultData.getGroup("trendData", new TypeToken<Group<Trend>>() {
                    }.getType());
                    if (group != null) {
                        cardHolderChartView.setData(group);
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
