package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.HomeActivity;
import com.procoin.module.home.entity.AccountInfo;
import com.procoin.module.home.entity.UserFollow;
import com.procoin.module.home.trade.adapter.TradeCurrPositionAdapter;
import com.procoin.module.home.trade.dialog.SetFollowMultipleFragment;
import com.procoin.util.MyCallBack;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.CircleImageView;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单账户
 * Created by zhengmj on 19-3-8.
 */

public class HomeFollowAccountFragment extends UserBaseFragment implements View.OnClickListener {


    @BindView(R.id.tvTolAssetsText)
    TextView tvTolAssetsText;
    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.llRiskRateMark)
    LinearLayout llRiskRateMark;
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
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUnBindView)
    TextView tvUnBindView;
    @BindView(R.id.tvBindView)
    TextView tvBindView;
    @BindView(R.id.tvOpenFollowState)
    TextView tvOpenFollowState;
    @BindView(R.id.tvMultiple)
    TextView tvMultiple;
    @BindView(R.id.tvCustom)
    TextView tvCustom;
    @BindView(R.id.tvDigital)
    TextView tvDigital;
    @BindView(R.id.tvStock)
    TextView tvStock;
    @BindView(R.id.toggleView)
    SwitchCompat toggleView;

    private TradeCurrPositionAdapter homeDigitalAdapter;


    private AccountInfo followDigitalAccount;
    private AccountInfo followStockAccount;
    private UserFollow userFollow;

    private int type;//0数字 1期货

    private TjrImageLoaderUtil tjrImageLoaderUtil;


    private Call<ResponseBody> unBindViewCall;
    private Call<ResponseBody> updateForOpenCall;
    private Call<ResponseBody> updateMultipleCall;
    private SetFollowMultipleFragment setFollowMultipleFragment;


    public static HomeFollowAccountFragment newInstance() {
        HomeFollowAccountFragment fragment = new HomeFollowAccountFragment();
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

    public void setData(AccountInfo followDigitalAccount, AccountInfo followStockAccount, UserFollow userFollow) {
        if (rvList == null) return;
        this.followDigitalAccount = followDigitalAccount;
        this.followStockAccount = followStockAccount;
        this.userFollow = userFollow;

        setFollow();
        if (type == 0) {
            setAccountInfo(followDigitalAccount);
        } else if (type == 1) {
            setAccountInfo(followStockAccount);
        }
    }

    private void setFollow() {
        if (userFollow != null) {
            tvMultiple.setText(userFollow.multiple + "倍");

            if (userFollow.dvUid > 0) {
                ivHead.setVisibility(View.VISIBLE);
                tjrImageLoaderUtil.displayImage(userFollow.dvHeadUrl, ivHead);
                tvName.setText(userFollow.dvUserName + " ID:" + userFollow.dvUid);
                tvUnBindView.setVisibility(View.VISIBLE);
                tvBindView.setVisibility(View.GONE);
            } else {
                ivHead.setVisibility(View.GONE);
                tvName.setText("未绑定");
                tvUnBindView.setVisibility(View.GONE);
                tvBindView.setVisibility(View.VISIBLE);
            }
            if (userFollow.isOpen == 1) {
                tvOpenFollowState.setText("已开启");
                toggleView.setChecked(true);
            } else {
                tvOpenFollowState.setText("未开启");
                toggleView.setChecked(false);
            }
        }
    }

    private void setAccountInfo(AccountInfo digitalAccount) {
        if (digitalAccount != null) {
            tvTolAssets.setText(digitalAccount.assets);
            tvTolAssetsCny.setText(digitalAccount.assetsCny);
            tvRiskRate.setText(digitalAccount.riskRate + "%");
            tvEableBail.setText(digitalAccount.eableBail);
            tvProfit.setText(StockChartUtil.formatWithSign(digitalAccount.profit));
            tvProfit.setTextColor(StockChartUtil.getRateTextColor(getActivity(), Double.parseDouble(digitalAccount.profit)));
            tvOpenBail.setText(digitalAccount.openBail);
            tvFrozenBail.setText(digitalAccount.frozenBail);

            if (digitalAccount != null && digitalAccount.openList != null) {
//            rvList.setVisibility(View.VISIBLE);
//            tvNoDataPosition.setVisibility(View.GONE);
                homeDigitalAdapter.setGroup(digitalAccount.openList);
            } else {
//            rvPositionList.setVisibility(View.GONE);
//            tvNoDataPosition.setVisibility(View.VISIBLE);
            }
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_follow_account, container, false);
        ButterKnife.bind(this, view);
        homeDigitalAdapter = new TradeCurrPositionAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvList.addItemDecoration(simpleRecycleDivider);
        rvList.setAdapter(homeDigitalAdapter);


        toggleView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("toggleView2", "onCheckedChanged==" + isChecked);
                if (!buttonView.isPressed()) return;//这句不能少，否则出错
                if (isChecked) {
//                    if (userFollow != null && userFollow.dvUid > 0) {//绑定大v才能开启
                    startUpdateForOpenCall(1);
//                    } else {
//                        CommonUtil.showmessage("请先绑定大V", getActivity());
//                        toggleView.setChecked(false);
//                    }
                } else {
//                    if (userFollow != null && userFollow.dvUid > 0) {//绑定大v才能开启
                    startUpdateForOpenCall(0);
//                    }
                }
            }
        });
//        Log.d("toggleView2","pushDisturbmodeToggle=="+pushDisturbmodeToggle);
//        toggleView.setChecked(pushDisturbmodeToggle);

        tvDigital.setOnClickListener(this);
        tvStock.setOnClickListener(this);
        tvCustom.setOnClickListener(this);
        tvUnBindView.setOnClickListener(this);
        tvBindView.setOnClickListener(this);
        llRiskRateMark.setOnClickListener(this);
        type = 0;
        tvDigital.setSelected(true);
        tvStock.setSelected(false);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();


        return view;
    }

    TjrBaseDialog questionMarkDialog;

    private void showSubmitTipsDialog(String tips) {
        questionMarkDialog = new TjrBaseDialog(getActivity()) {
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
        questionMarkDialog.setTitleVisibility(View.GONE);
        questionMarkDialog.setBtnColseVisibility(View.GONE);
        questionMarkDialog.setMessage("市值小于" + tips + "USDT的币种");
        questionMarkDialog.setBtnOkText("知道了");
        questionMarkDialog.show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        //先设置深色,在当Tab选中的时候在调用immersionBar()方法在设置白色，如果先设置白色一进来就会变成白色，那前面就看不到状态栏
//        mImmersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvDigital:
                if (type == 1) {
                    type = 0;
                    tvTolAssetsText.setText("跟单-数字货币总资产(USDT)");
                    tvDigital.setSelected(true);
                    tvStock.setSelected(false);
                    setAccountInfo(followDigitalAccount);
                }

                break;
            case R.id.tvStock:
                if (type == 0) {
                    type = 1;
                    tvTolAssetsText.setText("跟单-股指期货总资产(USDT)");
                    tvDigital.setSelected(false);
                    tvStock.setSelected(true);
                    setAccountInfo(followStockAccount);
                }
                break;
            case R.id.tvUnBindView:
                if (userFollow != null && userFollow.dvUid > 0) {
                    startUnBindViewCall(userFollow.dvUid);
                }

                break;
            case R.id.tvBindView:
                ((HomeActivity) getActivity()).switchHomeCropyMeFragment();
                break;
            case R.id.tvCustom:
                showSetFollowMultipleFragment();
                break;
            case R.id.llRiskRateMark:
                if (getActivity() != null && getActivity() instanceof HomeActivity) {
                    HomeActivity homeActivity = (HomeActivity) getActivity();
                    if (!TextUtils.isEmpty(homeActivity.riskRateDesc)) {
                        showRiskRateDescDialog(homeActivity.riskRateDesc);
                    }
                }
                break;

        }
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


    private void startUnBindViewCall(long userId) {
        CommonUtil.cancelCall(unBindViewCall);
        unBindViewCall = VHttpServiceManager.getInstance().getVService().unBind(userId);
        unBindViewCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    if (userFollow != null) {
                        userFollow.dvUid = 0;
                        setFollow();
                    }

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private void startUpdateForOpenCall(int isOpen) {
        CommonUtil.cancelCall(updateForOpenCall);
        updateForOpenCall = VHttpServiceManager.getInstance().getVService().updateForOpen(isOpen);
        updateForOpenCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    //TODO
//                    setFollow();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                toggleView.setChecked(!toggleView.isChecked());
            }
        });
    }

    public void showSetFollowMultipleFragment() {
        setFollowMultipleFragment = SetFollowMultipleFragment.newInstance();
        setFollowMultipleFragment.setSetMultipleListen(new SetFollowMultipleFragment.SetMultipleListen() {
            @Override
            public void setMultiple(int multiple) {
                startUpdateMultipleCall(multiple);
            }
        });
        setFollowMultipleFragment.showDialog(getChildFragmentManager(), "");
    }

    private void startUpdateMultipleCall(final int multiple) {
        CommonUtil.cancelCall(updateMultipleCall);
        updateMultipleCall = VHttpServiceManager.getInstance().getVService().updateMultiple(multiple);
        updateMultipleCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    if (userFollow != null) userFollow.multiple = multiple;
                    tvMultiple.setText(userFollow.multiple + "倍");
                    if (setFollowMultipleFragment != null) setFollowMultipleFragment.dismiss();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
