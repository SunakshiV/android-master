package com.procoin.module.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.procoin.module.home.entity.SubUser;
import com.procoin.widgets.RadarView;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.home.entity.RadarChart;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 雷达图说明
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class RadarDetailsActivity extends TJRBaseToolBarSwipeBackActivity {


    @BindView(R.id.rdv)
    RadarView rdv;
    @BindView(R.id.tvTolIncomeScore)
    TextView tvTolIncomeScore;
    @BindView(R.id.tvCopyRateScore)
    TextView tvCopyRateScore;
    @BindView(R.id.tvProfitShareScore)
    TextView tvProfitShareScore;
    @BindView(R.id.tvCopyNumScore)
    TextView tvCopyNumScore;
    @BindView(R.id.tvCopyBalanceScore)
    TextView tvCopyBalanceScore;

    public volatile SubUser subUser;//雷达图数据

    @Override
    protected int setLayoutId() {
        return R.layout.radar_details;
    }

    @Override
    protected String getActivityTitle() {
        return "雷达图说明";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        subUser = getApplicationContext().subUser;

//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);

        if (subUser != null) {

            List<Double> data = new ArrayList<>();
            data.add(subUser.radarFollowBalanceWeight);
            data.add(subUser.radarProfitRateWeight);
            data.add(subUser.radarFollowProfitRateWeight);
            data.add(subUser.radarFollowWinRateWeight);
            data.add(subUser.radarFollowNumWeight);
            rdv.setData(data);

            ArrayList<String> titles = new ArrayList<>();

            titles.add("跟单盈利额\n");
            titles.add("盈利能力\n");
            titles.add("跟单收益率\n");
            titles.add("跟单胜率\n");
            titles.add("人气指数\n");

            ArrayList<String> titleValues = new ArrayList<>();
            titleValues.add(String.valueOf(subUser.radarFollowBalance));
            titleValues.add(subUser.radarProfitRate + "%");
            titleValues.add(subUser.radarFollowProfitRate + "%");
            titleValues.add(subUser.radarFollowWinRate + "%");
            titleValues.add(String.valueOf(subUser.radarFollowNum));

            rdv.setTitles(titles, titleValues);

            tvCopyBalanceScore.setText("跟单盈利额(" + subUser.radarFollowBalance + ")");
            tvTolIncomeScore.setText("盈利能力(" + subUser.radarProfitRate + "%)");
            tvCopyRateScore.setText("跟单收益率(" + subUser.radarFollowProfitRate + "%)");
            tvProfitShareScore.setText("跟单胜率(" + subUser.radarFollowWinRate + "%)");
            tvCopyNumScore.setText("人气指数(" + subUser.radarFollowNum + ")");


        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            subUser = getApplicationContext().subUser = null;
        }
    }
}
