package com.procoin.module.copy;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.appcompat.widget.LinearLayoutManager;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.copy.adapter.CopyOrderDetailsListAdapter;
import com.procoin.module.copy.entity.CopyOrder;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;

/**
 * 交易明细
 */

public class CropyOrderDetailListActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    @BindView(R.id.ivArrow)
    ImageView ivArrow;
    @BindView(R.id.llParams)
    LinearLayout llParams;
    @BindView(R.id.rv_list)
    LoadMoreRecycleView listViewAutoLoadMore;
    @BindView(R.id.swiperefreshlayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.etCoin)
    EditText etCoin;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvReset)
    TextView tvReset;
    @BindView(R.id.tvQuery)
    TextView tvQuery;
    @BindView(R.id.llSelectParamsAnim)
    LinearLayout llSelectParamsAnim;
    @BindView(R.id.hideSelectParams)
    View hideSelectParams;
    @BindView(R.id.llSelectParams)
    LinearLayout llSelectParams;


    private CopyOrderDetailsListAdapter copyOrderDetailsListAdapter;
    private int pageSize = 20;
    private int pageNo = 1;
    private long orderId;

    private String symbol;
    private String buySell = "";//1买入 -1卖出

    private Call<ResponseBody> copyTradeListCall;

    public static void pageJump(Context context, long orderId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERID, orderId);
        PageJumpUtil.pageJumpToData(context, CropyOrderDetailListActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.copy_order_details;
    }

    @Override
    protected String getActivityTitle() {
        return "交易明细";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle b = null;
        if ((b = getIntent().getExtras()) != null) {
            if (b.containsKey(CommonConst.ORDERID)) {
                orderId = b.getLong(CommonConst.ORDERID, 0);
            } else {
                CommonUtil.showmessage("参数错误", this);
                finish();
            }
        }
        copyOrderDetailsListAdapter = new CopyOrderDetailsListAdapter(this);
        listViewAutoLoadMore.setLayoutManager(new LinearLayoutManager(this));
        listViewAutoLoadMore.setAdapter(copyOrderDetailsListAdapter);
        listViewAutoLoadMore.setRecycleViewLoadMoreCallBack(callBack);
        listViewAutoLoadMore.addItemDecoration(new SimpleRecycleDivider(this));
        copyOrderDetailsListAdapter.setRecycleViewLoadMoreCallBack(callBack);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNo = 1;
                startCopyTradeList(symbol, buySell);
            }
        });
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(true);//前面都已经判断过了,不知道为何这里有时候会报null,所以在判断一下
                startCopyTradeList(symbol, buySell);
            }
        }, 500);

        llParams.setOnClickListener(this);
        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvReset.setOnClickListener(this);
        tvQuery.setOnClickListener(this);
        hideSelectParams.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    LoadMoreRecycleView.RecycleViewLoadMoreCallBack callBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (copyOrderDetailsListAdapter != null && copyOrderDetailsListAdapter.getRealItemCount() > 0) {
                CopyOrder copyOrder = copyOrderDetailsListAdapter.getItem(copyOrderDetailsListAdapter.getItemCount() - 2);
                if (copyOrder != null) {
                    startCopyTradeList(symbol, buySell);

                }
            }
        }
    };

    private void startCopyTradeList(String symbol, String buySell) {
        CommonUtil.cancelCall(copyTradeListCall);
        copyTradeListCall = VHttpServiceManager.getInstance().getVService().copyTradeList(symbol, buySell, orderId, pageNo);
        copyTradeListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                Group<CopyOrder> group = null;
                if (resultData.isSuccess()) {
                    pageSize = resultData.getPageSize(pageSize);
                    group = resultData.getGroup("data", new TypeToken<Group<CopyOrder>>() {
                    }.getType());
                    if (pageNo == 1) {
                        copyOrderDetailsListAdapter.setGroup(group);
                    } else {
                        if (group != null && group.size() > 0) {
                            copyOrderDetailsListAdapter.addItem(group);
                            copyOrderDetailsListAdapter.notifyDataSetChanged();
                        }
                    }
                    pageNo++;
                }
                swipeRefreshLayout.setRefreshing(false);
                copyOrderDetailsListAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swipeRefreshLayout.setRefreshing(false);
                copyOrderDetailsListAdapter.onLoadComplete(false, false);
            }
        });
    }

    private ObjectAnimator objectAnimatorShow;
    private ObjectAnimator objectAnimatorShowArrow;
    private ObjectAnimator objectAnimatorHide;
    private ObjectAnimator objectAnimatorHideArrow;
    private FastOutSlowInInterpolator fastOutSlowInInterpolator;


    private void showSelectParams() {
        llSelectParams.setVisibility(View.VISIBLE);

        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorShow == null) {
            objectAnimatorShow = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", -llSelectParamsAnim.getHeight(), 0);
            objectAnimatorShow.setDuration(300);
            objectAnimatorShow.setInterpolator(fastOutSlowInInterpolator);
        }

        if (objectAnimatorShowArrow == null) {
            objectAnimatorShowArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", 0.0f, -45.0f, -90.0f, -180.0f);
            objectAnimatorShowArrow.setDuration(300);
            objectAnimatorShowArrow.setInterpolator(fastOutSlowInInterpolator);
        }
        objectAnimatorShow.start();
        objectAnimatorShowArrow.start();

    }

    private void hideSelectParams() {
        closeKeyBoard();
        if (fastOutSlowInInterpolator == null)
            fastOutSlowInInterpolator = new FastOutSlowInInterpolator();
        if (objectAnimatorHide == null) {
            objectAnimatorHide = ObjectAnimator.ofFloat(llSelectParamsAnim, "translationY", 0, -llSelectParamsAnim.getHeight());
            objectAnimatorHide.setDuration(300);
            objectAnimatorHide.setInterpolator(fastOutSlowInInterpolator);
            objectAnimatorHide.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    llSelectParams.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }


        if (objectAnimatorHideArrow == null) {
            objectAnimatorHideArrow = ObjectAnimator.ofFloat(ivArrow, "rotation", -180.0f, -90.0f, -45.0f, 0.0f);
            objectAnimatorHideArrow.setDuration(300);
            objectAnimatorHideArrow.setInterpolator(fastOutSlowInInterpolator);

        }
        objectAnimatorHide.start();
        objectAnimatorHideArrow.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llParams:
                if (llSelectParams.getVisibility() == View.INVISIBLE || llSelectParams.getVisibility() == View.GONE) {
                    showSelectParams();
                } else {
                    hideSelectParams();
                }
                break;
            case R.id.tvBuy:
                updateBuySell("1");
                break;
            case R.id.tvSell:
                updateBuySell("-1");
                break;
            case R.id.tvReset:
                hideSelectParams();
                reset();
                break;
            case R.id.tvQuery:
                String symbol = etCoin.getText().toString();
//                if (TextUtils.isEmpty(symbol)) {
//                    CommonUtil.showmessage("请选择币种", this);
//                    return;
//                }
                hideSelectParams();
                this.symbol = symbol;
                pageNo = 1;
                startCopyTradeList(etCoin.getText().toString(), buySell);
                break;
            case R.id.hideSelectParams:
                hideSelectParams();
                break;


        }
    }

    private void updateBuySell(String buySell) {
        if ("".equals(this.buySell)) {
            this.buySell = buySell;
        } else if (buySell.equals(this.buySell)) {
            this.buySell = "";
        } else {
            this.buySell = buySell;
        }
        setState();

    }

    private void setState() {
        if ("".equals(this.buySell)) {
            tvBuy.setSelected(false);
            tvSell.setSelected(false);
        } else if ("1".equals(this.buySell)) {
            tvBuy.setSelected(true);
            tvSell.setSelected(false);
        } else if ("-1".equals(this.buySell)) {
            tvBuy.setSelected(false);
            tvSell.setSelected(true);
        }
    }

    private void reset() {
        this.buySell = "";
        symbol = "";
        etCoin.setText("");
        pageNo=1;
        setState();
        startCopyTradeList(symbol, buySell);
    }

    private void closeKeyBoard() {
        if (etCoin == null || etCoin.getWindowToken() == null) return;
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etCoin.getWindowToken(), 0);
    }
}
