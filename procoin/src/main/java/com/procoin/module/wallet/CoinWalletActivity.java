package com.procoin.module.wallet;


import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.entity.Store;
import com.procoin.module.wallet.adapter.WalletHistoryAdapter;
import com.procoin.module.wallet.entity.StoreHistory;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 存币宝
 */
public class CoinWalletActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


//    @BindView(R.id.tvEquityLevel)
//    TextView tvEquityLevel;

    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;
    @BindView(R.id.tvAmountTip)
    TextView tvAmountTip;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvProfitTip)
    TextView tvProfitTip;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.tvIntoWallet)
    TextView tvIntoWallet;
    @BindView(R.id.tvRolloutWallet)
    TextView tvRolloutWallet;
    @BindView(R.id.ivQuestionMark)
    ImageView ivQuestionMark;

    private int pageNo = 1;
    private int pageSize = 15;

    private Call<ResponseBody> getKbtAssetCall;

    private WalletHistoryAdapter walletHistoryAdapter;

    private String symbol = "";//币种


    @Override
    protected int setLayoutId() {
        return R.layout.coin_wallet;
    }

    @Override
    protected String getActivityTitle() {
        return "存币宝";
    }

    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        PageJumpUtil.pageJump(context, CoinWalletActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
//        tvKbtRules.setOnClickListener(this);
//        tvBuyBack.setOnClickListener(this);
//        tvAssignment.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }

        mActionBar.setTitle(symbol + "存币宝");
//        tvKbtTolBalanceText.setText(symbol + "总数量");

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));

        walletHistoryAdapter = new WalletHistoryAdapter(this);
        rvList.setAdapter(walletHistoryAdapter);
        walletHistoryAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        rvList.setRecycleViewLoadMoreCallBack(loadMoreCallBack);

        tvIntoWallet.setOnClickListener(this);
        tvRolloutWallet.setOnClickListener(this);
        ivQuestionMark.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        startGetPrybarStoreAssetCall();
    }


    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (walletHistoryAdapter != null && walletHistoryAdapter.getRealItemCount() > 0) {
                startGetPrybarStoreAssetCall();
            } else {
                pageNo = 1;
                startGetPrybarStoreAssetCall();
            }
        }
    };


    private void startGetPrybarStoreAssetCall() {
        CommonUtil.cancelCall(getKbtAssetCall);
        Log.d("KBTHomeActivity", "symbol==" + symbol);
        getKbtAssetCall = VHttpServiceManager.getInstance().getVService().prybarStoreAsset(symbol, pageNo);
        getKbtAssetCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<StoreHistory> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    Store store = resultData.getObject("storeResult", Store.class);
                    if (store != null) {
                        tvAmountTip.setText(store.amountTip);
                        tvAmount.setText(store.amount);
                        tvProfitTip.setText(store.profitTip);
                        tvProfit.setText(store.profit);
                    }
//
                    group = resultData.getGroup("recordList", new TypeToken<Group<StoreHistory>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            walletHistoryAdapter.setGroup(group);
                        } else {
                            walletHistoryAdapter.addItem(group);
                            walletHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                walletHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                walletHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvIntoWallet:
                IntoWalletActivity.pageJump(this, symbol);
                break;
            case R.id.tvRolloutWallet:
                RollOutWalletActivity.pageJump(this, symbol);
                break;
            case R.id.ivQuestionMark:
                CommonWebViewActivity.pageJumpCommonWebViewActivity(this, CommonConst.PASSGEDETAIL_27);
                break;

        }
    }

}
