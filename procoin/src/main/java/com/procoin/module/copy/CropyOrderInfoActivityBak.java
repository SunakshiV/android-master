package com.procoin.module.copy;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.copy.adapter.CopyPositionAdapter;
import com.procoin.module.myhome.UserHomeActivity;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.CircleImageView;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.copy.adapter.CopyOrderDetailsListAdapter;
import com.procoin.module.copy.entity.CopyOrder;
import com.procoin.module.copy.entity.CopyOrderDetail;
import com.procoin.social.util.AvoidMultiClick;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.LoadMoreRecycleView;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.ImmersionBar;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 产品详情
 */

public class CropyOrderInfoActivityBak extends TJRBaseToolBarSwipeBackActivity implements DialogInterface.OnDismissListener {

    @BindView(R.id.tvToolbarTitle)
    TextView tvToolbarTitle;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
//    @BindView(R.id.tvProfit)
//    TextView tvProfit;
    @BindView(R.id.tvProfitBalance)
    TextView tvProfitBalance;
    @BindView(R.id.tvCopyOrderBalance)
    TextView tvCopyOrderBalance;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvAppendBalance)
    TextView tvAppendBalance;
    //    @BindView(R.id.iv_share)
//    ImageView ivShare;
    @BindView(R.id.llHead)
    LinearLayout llHead;
    @BindView(R.id.ivhead)
    CircleImageView ivhead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvFollowCount)
    TextView tvFollowCount;
    @BindView(R.id.tvGrade)
    TextView tvGrade;

    @BindView(R.id.tvNoHold)
    TextView tvNoHold;
    @BindView(R.id.llHold)
    LinearLayout llHold;

    @BindView(R.id.rvPositionList)
    RecyclerView rvPositionList;
    @BindView(R.id.tvMaxBalance)
    TextView tvMaxBalance;
    @BindView(R.id.tvStopWin)
    TextView tvStopWin;
    @BindView(R.id.tvStopLoss)
    TextView tvStopLoss;
    @BindView(R.id.tvModify)
    TextView tvModify;
    @BindView(R.id.tvStopOrder)
    TextView tvStopOrder;
    @BindView(R.id.rvTradeHis)
    LoadMoreRecycleView rvTradeHis;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;


    //下面是跟单设置弹框View
    private TextView tvSetStopWin;
    private TextView tvSetStopLoss;
    //    private BubbleSeekBar bsbSeekbar ;
    private SeekBar sbStopWin;
    private SeekBar sbStopLoss;
    private TextView tvSubmit;
    private TextView tvClose;
    private EditText etMaxMoney;
    private LinearLayout llTop;
    private TextView tvMaxCny;


    //下面是追加金额弹框View
    private LinearLayout llAppendTop;
    private TextView tvAppendClose;
    private EditText etAppendBalance;
    private TextView tvBtnAppend;
    private TextView tvBalanceText;
    private TextView tvTolCny;


    private Handler handler = new Handler();
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ActionBar mActionBar;
    private Toolbar toolbar;
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private Onclick onclick;
    private boolean showBlackFlag;

    private long orderId;
    private Call<ResponseBody> copyOrderDetailCall;
    private Call<ResponseBody> copyUpdateOptionCall;
    private Call<ResponseBody> copyAppendOrderCashCall;
    private Call<ResponseBody> copyCloseorderCall;
    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> copyTradeListCall;


    private CopyPositionAdapter copyPositionAdapter;
    private CopyOrderDetailsListAdapter copyOrderDetailsListAdapter;

    private static final int decimalCount = 4;//小数点数量,金额小数点2位，数量小数点是4位.

    private CopyOrderDetail copyOrderDetail;

    private double atMaxCash;
    private double appendBalance;




    private String holdUsdt = "0.00";//持有USDT
    private double usdtRate = 0.00;//usdt市价


    private int pageNo = 1;
    private int pageSize = 15;


    public static void pageJump(Context context, long orderId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERID, orderId);
        PageJumpUtil.pageJumpToData(context, CropyOrderInfoActivityBak.class, bundle);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

    }

//    @Override
//    protected void initImmersionBar() {
//        super.initImmersionBar();
//    }

    @Override
    protected int setLayoutId() {
        return R.layout.cropy_order_info_bak;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        setContentView(R.layout.product_details);
        Bundle b = null;
        if ((b = getIntent().getExtras()) != null) {
            if (b.containsKey(CommonConst.ORDERID)) {
                orderId = b.getLong(CommonConst.ORDERID, 0);
            } else {
                CommonUtil.showmessage("参数错误", this);
                finish();
            }
        }
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(0);

//        immersionBar
//                .keyboardEnable(false)
//                .titleBar(toolbar, false)
//                .statusBarDarkFont(true)
//                .flymeOSStatusBarFontColor(R.color.black)
//                .init();
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
        mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_svg_back_white);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitleEnabled(false);
        tvToolbarTitle.setTextColor(ContextCompat.getColor(this, R.color.white));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("onOffsetChanged", "verticalOffset==" + verticalOffset);
                int alpha = Math.min(Math.abs(verticalOffset) / 1, 255);
                toolbar.getBackground().setAlpha(alpha);
                Log.d("onOffsetChanged", "alpha==" + alpha);
                if (alpha >= 150) {
                    if (!showBlackFlag) {
                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_common_back_gray);
                        tvToolbarTitle.setTextColor(ContextCompat.getColor(CropyOrderInfoActivityBak.this, R.color.black));
                        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.black).init();
//                        iv_share.setImageResource(R.drawable.ic_redz_new_share_black);
                        showBlackFlag = true;
                    }

                } else {
                    if (showBlackFlag) {
                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_svg_back_white);
                        tvToolbarTitle.setTextColor(ContextCompat.getColor(CropyOrderInfoActivityBak.this, R.color.white));
//                        iv_share.setImageResource(R.drawable.ic_redz_new_share_white);
                        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
                        showBlackFlag = false;
                    }

                }
            }
        });

        onclick = new Onclick();


        copyPositionAdapter = new CopyPositionAdapter(this);
        rvPositionList.setLayoutManager(new LinearLayoutManager(this));
        rvPositionList.addItemDecoration(new SimpleRecycleDivider(this, 0, 0, ContextCompat.getColor(this, R.color.transparent), 8));
        rvPositionList.setAdapter(copyPositionAdapter);


        copyOrderDetailsListAdapter = new CopyOrderDetailsListAdapter(this);
        rvTradeHis.setLayoutManager(new LinearLayoutManager(this));
        rvTradeHis.addItemDecoration(new SimpleRecycleDivider(this, 15, 15, false));
        rvTradeHis.setAdapter(copyOrderDetailsListAdapter);

        copyOrderDetailsListAdapter.setRecycleViewLoadMoreCallBack(loadMoreCallBack);
        rvTradeHis.setRecycleViewLoadMoreCallBack(loadMoreCallBack);


        tvModify.setOnClickListener(onclick);
        tvAppendBalance.setOnClickListener(onclick);
        tvStopOrder.setOnClickListener(onclick);



        startCopyOrderDetail();
        startGetTradeConfig();
        startCopyTradeList();


    }


    LoadMoreRecycleView.RecycleViewLoadMoreCallBack loadMoreCallBack = new LoadMoreRecycleView.RecycleViewLoadMoreCallBack() {
        @Override
        public void loadMore() {
            if (copyOrderDetailsListAdapter != null && copyOrderDetailsListAdapter.getRealItemCount() > 0) {
                startCopyTradeList();
            } else {
                pageNo = 1;
                startCopyTradeList();
            }
        }
    };


    private void startCopyTradeList() {
        CommonUtil.cancelCall(copyTradeListCall);
        copyTradeListCall = VHttpServiceManager.getInstance().getVService().copyTradeList("", "", orderId, pageNo);
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
                copyOrderDetailsListAdapter.onLoadComplete(resultData.isSuccess(), group == null || group.size() < pageSize);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                copyOrderDetailsListAdapter.onLoadComplete(false, false);
            }
        });
    }


    private void startCopyOrderDetail() {
        CommonUtil.cancelCall(copyOrderDetailCall);
        copyOrderDetailCall = VHttpServiceManager.getInstance().getVService().copyOrderDetail(orderId);
        copyOrderDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    copyOrderDetail = resultData.getObject("orderDetail", CopyOrderDetail.class);
                    if (copyOrderDetail != null) {
                        setData();
                    }
//                    Group<CopyOrder> trades = resultData.getGroup("trades", new TypeToken<Group<CopyOrder>>() {
//                    }.getType());
//                    if (trades != null && trades.size() > 0) {
//                        copyOrderDetailsListAdapter.setGroup(trades);
//                    }
                }
            }
        });
    }

    private void startGetTradeConfig() {
        CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("","");
        getTradeConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    usdtRate = resultData.getItem("usdtRate", Double.class);
                }
            }
        });
    }

    private void setData() {
        if (copyOrderDetail != null) {
//            tvProfitBalance.setText(copyOrderDetail.profitCash);
            tvProfitBalance.setText(StockChartUtil.formatNumWithSign(4, Double.parseDouble(copyOrderDetail.profitCash), true));
            tvProfitBalance.setTextColor(StockChartUtil.getRateTextColor(this,Double.parseDouble(copyOrderDetail.profitCash)));
//            tvProfit.setText(StockChartUtil.formatNumWithSign(2, Double.parseDouble(copyOrderDetail.profitRate), true) + "%");
//            tvProfit.setBackgroundResource(StockChartUtil.getRateBg(Double.parseDouble(copyOrderDetail.profitRate)));

            tvCopyOrderBalance.setText(copyOrderDetail.tolBalance);
//            tvHoldMarketValue.setText(copyOrderDetail.holdMarketValue);
            tvBalance.setText(copyOrderDetail.balance);

            tjrImageLoaderUtil.displayImageForHead(copyOrderDetail.copyHeadUrl, ivhead);
            llHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(CropyOrderInfoActivityBak.this,copyOrderDetail.copyUid);
                }
            });
            tvName.setText(copyOrderDetail.copyName);
            tvFollowCount.setText(copyOrderDetail.fansCount + "个关注");
            tvGrade.setText(copyOrderDetail.score);

            if (copyOrderDetail.holdList != null && copyOrderDetail.holdList.size() > 0) {
                tvNoHold.setVisibility(View.GONE);
                llHold.setVisibility(View.VISIBLE);
                rvPositionList.setVisibility(View.VISIBLE);
                copyPositionAdapter.setGroup(copyOrderDetail.holdList);
            } else {
                tvNoHold.setVisibility(View.VISIBLE);
                llHold.setVisibility(View.GONE);
                rvPositionList.setVisibility(View.GONE);
            }
            tvMaxBalance.setText("每笔跟单金额上限：" + copyOrderDetail.maxCopyBalance + "USDT");
            tvStopWin.setText("止盈：" + copyOrderDetail.stopWinValue);
            tvStopLoss.setText("止损：" + copyOrderDetail.stopLossValue);
            setBtnState();


        }
    }

    private void setBtnState() {
        if (copyOrderDetail == null) return;
        if (copyOrderDetail.isDone == 0) {
            tvStopOrder.setText("停止跟单");
            tvStopOrder.setEnabled(true);
            tvAppendBalance.setEnabled(true);
            tvModify.setEnabled(true);

        } else if (copyOrderDetail.isDone == 1) {
            tvStopOrder.setText("正在停止跟单..." + copyOrderDetail.doneDegree + "%");
            tvStopOrder.setEnabled(false);
            tvAppendBalance.setEnabled(false);
            tvModify.setEnabled(false);
        } else {
            tvStopOrder.setText("已停止跟单");
            tvStopOrder.setEnabled(false);
            tvAppendBalance.setEnabled(false);
            tvModify.setEnabled(false);
        }
    }

    private void startCopyUpdateOption(double atMaxCash, double stopWin, double stopLoss) {
        CommonUtil.cancelCall(copyUpdateOptionCall);
        copyUpdateOptionCall = VHttpServiceManager.getInstance().getVService().copyUpdateOption(orderId, String.valueOf(atMaxCash), stopWin, stopLoss);
        copyUpdateOptionCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CropyOrderInfoActivityBak.this);
                    if (mAlertDialog != null) {
                        CommonUtil.hideSoftKeyBoard(CropyOrderInfoActivityBak.this, mAlertDialog);
                        mAlertDialog.dismiss();
                    }
                    startCopyOrderDetail();
                }
            }
        });
    }

    private void startCopyAppendOrderCash(double cash) {
        CommonUtil.cancelCall(copyAppendOrderCashCall);
        copyAppendOrderCashCall = VHttpServiceManager.getInstance().getVService().copyAppendOrderCash(orderId, String.valueOf(cash));
        copyAppendOrderCashCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CropyOrderInfoActivityBak.this);
                    if (mAlertDialog2 != null) {
                        CommonUtil.hideSoftKeyBoard(CropyOrderInfoActivityBak.this, mAlertDialog2);
                        mAlertDialog2.dismiss();
                    }
                    startCopyOrderDetail();
                    startGetTradeConfig();
                }
            }
        });
    }


    private void startCopyCloseorder() {
        CommonUtil.cancelCall(copyCloseorderCall);
        copyCloseorderCall = VHttpServiceManager.getInstance().getVService().copyCloseorder(orderId);
        copyCloseorderCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CropyOrderInfoActivityBak.this);
                    //重新刷新
                    if (copyOrderDetail != null) {
                        copyOrderDetail.isDone = 1;
                        setBtnState();
                    }
                    startCopyOrderDetail();
                }
            }
        });
    }

    private AlertDialog mAlertDialog;

    private void showCopySettingDialog() {

        //弹出Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        mAlertDialog = builder.create();
        mAlertDialog.setOnDismissListener(this);
        mAlertDialog.show();
        View dialogView = initDialogView();
        mAlertDialog.setContentView(dialogView);
        Window mDialogWindow = mAlertDialog.getWindow();
        if (mDialogWindow != null) {
            //解决无法弹出输入法的问题
            mDialogWindow.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        //计算屏幕宽高
        mDialogWindow.setGravity(Gravity.TOP);
        mDialogWindow.setWindowAnimations(R.style.TopAnimation);
        mDialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//widthAndHeight[1] / 2
        ImmersionBar.with(this, mAlertDialog, getClass().getSimpleName())
                .titleBar(llTop)
//                .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                .navigationBarColor(R.color.btn4)
                .init();
//        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
    }


    private View initDialogView() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_copy_setting, null);
//        bsbSeekbar=(BubbleSeekBar) dialogView.findViewById(R.id.sdbSeekbar);
//        bsbSeekbar.setCustomSectionTextArray(new BubbleSeekBar.CustomSectionTextArray() {
//            @NonNull
//            @Override
//            public SparseArray<String> onCustomize(int sectionCount, @NonNull SparseArray<String> array) {
//                array.clear();
//                array.put(0, "0");
//                array.put(1, "25%");
//                array.put(2, "50%");
//                array.put(3, "75%");
//                array.put(4, "100%");
//                return array;
//            }
//        });
//        bsbSeekbar.setOnProgressChangedListener(new BubbleSeekBar.OnProgressChangedListener() {
//            @Override
//            public void onProgressChanged(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//                tvSetStopWin.setText("止盈 " + progress + "%");
//            }
//
//            @Override
//            public void getProgressOnActionUp(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat) {
//
//            }
//
//            @Override
//            public void getProgressOnFinally(BubbleSeekBar bubbleSeekBar, int progress, float progressFloat, boolean fromUser) {
//
//            }
//        });
        llTop = dialogView.findViewById(R.id.llTop);
        tvClose = dialogView.findViewById(R.id.tvClose);
        tvMaxCny = dialogView.findViewById(R.id.tvMaxCny);
        etMaxMoney = dialogView.findViewById(R.id.etMaxMoney);
        etMaxMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > decimalCount) {//最多2位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                atMaxCash = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    atMaxCash = Double.parseDouble(s.toString());
                } else {
                    atMaxCash = 0;
                }
                BigDecimal tolCnyBd = BigDecimal.valueOf(usdtRate).multiply(BigDecimal.valueOf(atMaxCash)).setScale(2, BigDecimal.ROUND_FLOOR);
                tvMaxCny.setText("≈¥ " + tolCnyBd.toPlainString());

            }
        });
        tvSetStopWin = dialogView.findViewById(R.id.tvStopWin);
        tvSetStopLoss = dialogView.findViewById(R.id.tvStopLoss);
        sbStopWin = dialogView.findViewById(R.id.sbStopWin);
        sbStopLoss = dialogView.findViewById(R.id.sbStopLoss);
        tvSubmit = dialogView.findViewById(R.id.tvSubmit);

        sbStopWin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvSetStopWin.setText("止盈: " + progress + "%");
                if (progress == 0) {
                    tvSetStopWin.setText("止盈: " + "无设置");
                } else {
                    tvSetStopWin.setText("止盈: " + progress + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbStopLoss.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                tvSetStopLoss.setText("止损: " + progress + "%");
                if (progress == 0) {
                    tvSetStopLoss.setText("止损: " + "无设置");
                } else {
                    tvSetStopLoss.setText("止损: " + progress + "%");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        if (copyOrderDetail != null) {
            etMaxMoney.setText(String.valueOf(copyOrderDetail.maxCopyBalance));
            tvSetStopWin.setText("止盈: " + copyOrderDetail.stopWinValue);
            tvSetStopLoss.setText("止损: " + copyOrderDetail.stopLossValue);
            sbStopWin.setProgress((int) (copyOrderDetail.stopWin * 100));
//            bsbSeekbar.setProgress((int) (copyOrderDetail.stopWin * 100));
            sbStopLoss.setProgress((int) (copyOrderDetail.stopLoss * 100));

        }


        tvClose.setOnClickListener(onclick);
        tvSubmit.setOnClickListener(onclick);
        return dialogView;
    }


    private AlertDialog mAlertDialog2;
    private Window mDialogWindow2;

    private void showAppendBalanceDialog() {

        //弹出Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialog);
        mAlertDialog2 = builder.create();
        mAlertDialog2.setOnDismissListener(this);
        mAlertDialog2.show();
        View dialogView = initDialogView2();
        mAlertDialog2.setContentView(dialogView);
        mDialogWindow2 = mAlertDialog2.getWindow();
        if (mDialogWindow2 != null) {
            //解决无法弹出输入法的问题
            mDialogWindow2.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }
        //计算屏幕宽高
        mDialogWindow2.setGravity(Gravity.TOP);
        mDialogWindow2.setWindowAnimations(R.style.TopAnimation);
        mDialogWindow2.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);//widthAndHeight[1] / 2
        ImmersionBar.with(this, mAlertDialog2, getClass().getSimpleName())
                .titleBar(llAppendTop)
//                .navigationBarWithKitkatEnable(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
//                .navigationBarColor(R.color.btn4)
                .init();
        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
    }


    private View initDialogView2() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_append_balance, null);
        llAppendTop = dialogView.findViewById(R.id.llAppendTop);
        tvAppendClose = dialogView.findViewById(R.id.tvAppendClose);
        tvBalanceText = dialogView.findViewById(R.id.tvBalanceText);
        tvTolCny = dialogView.findViewById(R.id.tvTolCny);
        etAppendBalance = dialogView.findViewById(R.id.etAppendBalance);
        if (appendBalance != 0) {
            etAppendBalance.append(String.valueOf(appendBalance));
            BigDecimal tolMaxBd = BigDecimal.valueOf(usdtRate).multiply(BigDecimal.valueOf(appendBalance)).setScale(2, BigDecimal.ROUND_FLOOR);
            tvTolCny.setText("≈¥ " + tolMaxBd.toPlainString());
        }
        etAppendBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > decimalCount) {//最多2位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                appendBalance = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    appendBalance = Double.parseDouble(s.toString());
                } else {
                    appendBalance = 0;
                }
                BigDecimal tolMaxBd = BigDecimal.valueOf(usdtRate).multiply(BigDecimal.valueOf(appendBalance)).setScale(2, BigDecimal.ROUND_FLOOR);
                tvTolCny.setText("≈¥ " + tolMaxBd.toPlainString());

            }
        });
        tvBtnAppend = dialogView.findViewById(R.id.tvBtnAppend);
        tvAppendClose.setOnClickListener(onclick);
        tvBtnAppend.setOnClickListener(onclick);

        tvBalanceText.setText("可用USDT:" + holdUsdt);

        return dialogView;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (mAlertDialog != null && dialog == mAlertDialog) {
            ImmersionBar.with(this, mAlertDialog, getClass().getSimpleName()).destroy();
//        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.black).init();
        } else if (mAlertDialog2 != null && dialog == mAlertDialog2) {
            ImmersionBar.with(this, mAlertDialog2, getClass().getSimpleName()).destroy();
            immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.black).init();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        CommonUtil.LogLa(2, "OLStarHomeActivity onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class Onclick extends AvoidMultiClick {
        @Override
        public void click(View v) {
            switch (v.getId()) {
                case R.id.tvModify://修改dialog
                    if (copyOrderDetail == null) return;
                    showCopySettingDialog();
                    break;
                case R.id.tvAppendBalance://追加金额dialog
                    showAppendBalanceDialog();
                    break;
                case R.id.tvClose:
                    if (mAlertDialog != null) {
                        CommonUtil.hideSoftKeyBoard(CropyOrderInfoActivityBak.this, mAlertDialog);
                        mAlertDialog.dismiss();
                    }
                    break;
                case R.id.tvAppendClose:
                    if (mAlertDialog2 != null) {
                        CommonUtil.hideSoftKeyBoard(CropyOrderInfoActivityBak.this, mAlertDialog2);
                        mAlertDialog2.dismiss();
                    }
                    break;
                case R.id.tvSubmit:
                    if(copyOrderDetail==null){
                        return;
                    }
                    if (atMaxCash == 0) {
                        com.procoin.util.CommonUtil.showmessage("请输入最大金额", CropyOrderInfoActivityBak.this);
                        return;
                    }
                    if(atMaxCash>Double.parseDouble(copyOrderDetail.tolBalance)){
                        com.procoin.util.CommonUtil.showmessage("最大金额不能大于跟单总金额", CropyOrderInfoActivityBak.this);
                        return;
                    }
                    startCopyUpdateOption(atMaxCash, (double) sbStopWin.getProgress() / 100, (double) sbStopLoss.getProgress() / 100);
                    break;

                case R.id.tvBtnAppend:
                    if (appendBalance == 0) {
                        com.procoin.util.CommonUtil.showmessage("请输入追加金额", CropyOrderInfoActivityBak.this);
                        return;
                    }
                    CommonUtil.hideSoftKeyBoard(CropyOrderInfoActivityBak.this, mAlertDialog2);
                    startCopyAppendOrderCash(appendBalance);
                    break;
                case R.id.tvStopOrder:
                    showStopOrderDialog();
                    break;

            }
        }
    }

    TjrBaseDialog stopOrderDialog;

    private void showStopOrderDialog() {
        stopOrderDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startCopyCloseorder();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        stopOrderDialog.setTvTitle("温馨提示");
        stopOrderDialog.setMessage("停止跟单将以市价卖出您所有的跟单持仓");
        stopOrderDialog.setBtnOkText("确定");
        stopOrderDialog.show();
    }


    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.clear();
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        }
        return super.onOptionsItemSelected(item);
    }


}
