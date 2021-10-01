package com.procoin.module.home.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.entity.AccountInfo;
import com.procoin.module.home.trade.adapter.TakeCoinHistoryAdapter;
import com.procoin.module.home.trade.entity.TakeCoinHistory;
import com.procoin.module.home.trade.history.TakeCoinHistoryActivity;
import com.procoin.module.home.trade.history.TakeCoinHistoryDetailsActivity;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 余额账户
 * Created by zhengmj on 19-3-8.
 */

public class HomeBalanceAccountFragment extends UserBaseFragment {


    @BindView(R.id.tvTolAssets)
    TextView tvTolAssets;
    @BindView(R.id.tvTolAssetsCny)
    TextView tvTolAssetsCny;
    @BindView(R.id.rvList)
    RecyclerView rvList;
    @BindView(R.id.tvEableBalance)
    TextView tvEableBalance;
    @BindView(R.id.tvFrozenBalance)
    TextView tvFrozenBalance;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    @BindView(R.id.tvAll)
    TextView tvAll;


    private TakeCoinHistoryAdapter takeCoinHistoryAdapter;


    private AccountInfo balanceAccount;

    private int pageSize = 15;
    private int pageNo = 1;
    private Call<ResponseBody> withdrawCoinListCall;
    private Group<TakeCoinHistory> group;


    public static HomeBalanceAccountFragment newInstance() {
        HomeBalanceAccountFragment fragment = new HomeBalanceAccountFragment();
        return fragment;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("HomeAccountFragment", "setUserVisibleHint==isVisibleToUser " + isVisibleToUser);
        if (isVisibleToUser && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetwithdrawCoinList();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint() && getParentFragment() != null && getParentFragment().getUserVisibleHint()) {
            startGetwithdrawCoinList();
        }
    }

    //    @BindView(R.id.tvEableBalance)
//    TextView tvEableBalance;
//    @BindView(R.id.tvFrozenBalance)
//    TextView tvFrozenBalance;
    public void setData(AccountInfo balanceAccount) {
        if (rvList == null) return;
        this.balanceAccount = balanceAccount;
        if (balanceAccount != null) {
            tvTolAssets.setText(balanceAccount.assets);
            tvTolAssetsCny.setText(balanceAccount.assetsCny);
            tvEableBalance.setText(balanceAccount.holdAmount);
            tvFrozenBalance.setText(balanceAccount.frozenAmount);
        }

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_balance_account, container, false);
        ButterKnife.bind(this, view);
        takeCoinHistoryAdapter = new TakeCoinHistoryAdapter(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        SimpleRecycleDivider simpleRecycleDivider = new SimpleRecycleDivider(getActivity(), 0, 0, ContextCompat.getColor(getActivity(), R.color.pageBackground), 10);
        simpleRecycleDivider.setShowLastDivider(false);
        rvList.addItemDecoration(simpleRecycleDivider);
        rvList.setAdapter(takeCoinHistoryAdapter);
        takeCoinHistoryAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                TakeCoinHistory takeCoinHistory = (TakeCoinHistory) t;
                TakeCoinHistoryDetailsActivity.pageJump(getActivity(), takeCoinHistory);
            }
        });
        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.pageJump(getActivity(), TakeCoinHistoryActivity.class);
            }
        });


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
    public void onDestroyView() {
        super.onDestroyView();
    }


    private void startGetwithdrawCoinList() {
        CommonUtil.cancelCall(withdrawCoinListCall);
        withdrawCoinListCall = VHttpServiceManager.getInstance().getVService().withdrawCoinList(pageNo);
        withdrawCoinListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<TakeCoinHistory>>() {
                    }.getType());
                    takeCoinHistoryAdapter.setGroup(group);
                    if (group != null && group.size() > 0) {
                        rvList.setVisibility(View.VISIBLE);
                        tvNoData.setVisibility(View.GONE);
                    } else {
                        rvList.setVisibility(View.GONE);
                        tvNoData.setVisibility(View.VISIBLE);
                    }
//                    if (takeCoinHistoryAdapter.getRealItemCount() > 0) {
//                        takeCoinHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
//                    }
                }
            }

        });
    }
}
