package com.procoin.module.kbt;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import androidx.appcompat.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.kbt.adapter.KbtGetHistoryAdapter;
import com.procoin.module.kbt.entity.KbtGetHistory;
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
 * KBT页面(不只是KBT，看传进来的symbol)
 */
public class KBTHomeActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    //    @BindView(R.id.tvKbtRules)
//    TextView tvKbtRules;
    @BindView(R.id.tvKbtTolBalance)
    TextView tvKbtTolBalance;

    @BindView(R.id.tvCash)
    TextView tvCash;

    @BindView(R.id.tvEquityLevel)
    TextView tvEquityLevel;



//    @BindView(R.id.tvKbtTolBalanceText)
//    TextView tvKbtTolBalanceText;


    //    @BindView(R.id.tvBuyBack)
//    TextView tvBuyBack;
//    @BindView(R.id.tvAssignment)
//    TextView tvAssignment;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView rvList;


    @BindView(R.id.tvEnableBalance)
    TextView tvEnableBalance;
    @BindView(R.id.tvFreezeBalance)
    TextView tvFreezeBalance;
    @BindView(R.id.tvLockBalance)
    TextView tvLockBalance;


    private int pageNo = 1;
    private int pageSize = 15;

    private Call<ResponseBody> getKbtAssetCall;

    private KbtGetHistoryAdapter kbtGetHistoryAdapter;

    private String symbol = "";//币种


    @Override
    protected int setLayoutId() {
        return R.layout.kbt_home;
    }

    @Override
    protected String getActivityTitle() {
        return "资产";
    }

    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        PageJumpUtil.pageJump(context, KBTHomeActivity.class, bundle);
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

        mActionBar.setTitle(symbol + "资产");
//        tvKbtTolBalanceText.setText(symbol + "总数量");

        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.addItemDecoration(new SimpleRecycleDivider(this, 15, 15));

        kbtGetHistoryAdapter = new KbtGetHistoryAdapter(this);
        rvList.setAdapter(kbtGetHistoryAdapter);
        kbtGetHistoryAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        rvList.setRecycleViewLoadMoreCallBack(loadMoreCallBack);

    }

    @Override
    protected void onResume() {
        super.onResume();

        startGetKbtAssetCall();
    }


    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (kbtGetHistoryAdapter != null && kbtGetHistoryAdapter.getRealItemCount() > 0) {
                startGetKbtAssetCall();
            } else {
                pageNo = 1;
                startGetKbtAssetCall();
            }
        }
    };


    private void startGetKbtAssetCall() {
        CommonUtil.cancelCall(getKbtAssetCall);
        Log.d("KBTHomeActivity", "symbol==" + symbol);
        getKbtAssetCall = VHttpServiceManager.getInstance().getVService().getCoinAsset(symbol, pageNo);
        getKbtAssetCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<KbtGetHistory> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    String totalAmount = resultData.getItem("totalAmount", String.class);
                    String totalCny = resultData.getItem("totalCny", String.class);
                    String holdAmount = resultData.getItem("holdAmount", String.class);
                    String frozenAmount = resultData.getItem("frozenAmount", String.class);
                    String lockAmount = resultData.getItem("lockAmount", String.class);

                    String equityLevel = resultData.getItem("equityLevel", String.class);

                    if (!TextUtils.isEmpty(equityLevel)) {
                        tvEquityLevel.setVisibility(View.VISIBLE);
                        tvEquityLevel.setText(equityLevel);
                    } else {
                        tvEquityLevel.setVisibility(View.GONE);
                    }


                    tvKbtTolBalance.setText(totalAmount);
                    tvEnableBalance.setText(holdAmount);
                    tvFreezeBalance.setText(frozenAmount);
                    tvLockBalance.setText(lockAmount);
                    if (!TextUtils.isEmpty(totalCny)) {
                        tvCash.setText(totalCny);
                        tvCash.setVisibility(View.VISIBLE);
                    } else {
                        tvCash.setVisibility(View.GONE);
                    }

                    group = resultData.getGroup("acquireRecordList", new TypeToken<Group<KbtGetHistory>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (pageNo == 1) {
                            kbtGetHistoryAdapter.setGroup(group);
                        } else {
                            kbtGetHistoryAdapter.addItem(group);
                            kbtGetHistoryAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                kbtGetHistoryAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                kbtGetHistoryAdapter.onLoadComplete(false, false);
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvKbtRules:
//                CommonWebViewActivity.pageJumpCommonWebViewActivity(this, "http://www.baidu.com");
//                break;
//            case R.id.tvBuyBack:
//                setResult(0x234);
//                PageJumpUtil.finishCurr(KBTHomeActivity.this);
////                PageJumpUtil.pageJump(this, KbtBuyBackActivity.class);
//                break;
//            case R.id.tvAssignment:
//                TradeActivity.pageJump(this, "KBT", -1);
//                break;

        }
    }

}
