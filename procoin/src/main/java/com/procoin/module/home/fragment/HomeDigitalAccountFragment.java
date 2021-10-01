package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.HomeActivity;
import com.procoin.module.home.entity.AccountInfo;
import com.procoin.module.home.trade.adapter.TradeCurrPositionAdapter;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 数字货币账户
 * Created by zhengmj on 19-3-8.
 */

public class HomeDigitalAccountFragment extends UserBaseFragment {


    @BindView(R.id.tvTolAssetsText)
    TextView tvTolAssetsText;
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.tvRiskRateMark)
    TextView tvRiskRateMark;
    @BindView(R.id.tvRiskRate)
    TextView tvRiskRate;
    @BindView(R.id.tvEableBail)
    TextView tvEableBail;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvOpenBail)
    TextView tvOpenBail;
    @BindView(R.id.tvFrozenBail)
    TextView tvFrozenBail;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.llRiskRateDesc)
    LinearLayout llRiskRateDesc;


    private TradeCurrPositionAdapter homeDigitalAdapter;


    private AccountInfo accountInfo;

    private int type = 0;//0数字账户 1股指账户


    public static HomeDigitalAccountFragment newInstance(int type) {
        HomeDigitalAccountFragment fragment = new HomeDigitalAccountFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
        }
    }

    public void setData(AccountInfo digitalAccount) {
        if (rvList == null) return;
        this.accountInfo = digitalAccount;
        if (accountInfo != null) {
            tvTolAssets.setText(accountInfo.assets);
            tvTolAssetsCny.setText(accountInfo.assetsCny);
            tvRiskRate.setText(accountInfo.riskRate + "%");
            tvEableBail.setText(accountInfo.eableBail);
            tvProfit.setText(StockChartUtil.formatWithSign(accountInfo.profit));
            tvProfit.setTextColor(StockChartUtil.getRateTextColor(getActivity(), Double.parseDouble(accountInfo.profit)));
            tvOpenBail.setText(accountInfo.openBail);
            tvFrozenBail.setText(accountInfo.frozenBail);
//            String s = accountInfo.openList == null ? "null" : (accountInfo.openList.size() + "");
//            CommonUtil.showmessage(s, getActivity());
            if (accountInfo != null && accountInfo.openList != null) {
                homeDigitalAdapter.setGroup(accountInfo.openList);
            }
        }


    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_ditital_account, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        if (bundle.containsKey("type")) {
            type = bundle.getInt("type", 0);
        }
        homeDigitalAdapter = new TradeCurrPositionAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvList.addItemDecoration(simpleRecycleDivider);
        rvList.setAdapter(homeDigitalAdapter);

        llRiskRateDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null && getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    if (!TextUtils.isEmpty(homeActivity.riskRateDesc)) {
                        showRiskRateDescDialog(homeActivity.riskRateDesc);
                    }
                }
            }
        });

        if (type == 0) {
            tvTolAssetsText.setText("数字货币总资产(USDT)");
        } else if (type == 1) {
            tvTolAssetsText.setText("股指期货总资产(USDT)");
        }

        return view;
    }

    TjrBaseDialog showRiskRateDescDialog;

    private void showRiskRateDescDialog(String tips) {
        showRiskRateDescDialog = new TjrBaseDialog(getActivity()) {
            @Override
            public void onclickOk() {
                dismiss();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        showRiskRateDescDialog.setTitleVisibility(View.GONE);
        showRiskRateDescDialog.setBtnColseVisibility(View.GONE);
        showRiskRateDescDialog.setMessage(tips);
        showRiskRateDescDialog.setBtnOkText("知道了");
        showRiskRateDescDialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
