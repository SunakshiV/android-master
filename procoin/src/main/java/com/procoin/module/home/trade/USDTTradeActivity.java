package com.procoin.module.home.trade;


import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.module.home.trade.dialog.WithDrawDialogFragment;
import com.procoin.module.myhome.PaymentTermActivity;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.fragment.UsdtOrCashTradeHisFragment;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * USDT 充值提现页面
 */
public class USDTTradeActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {

    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    //    @BindView(R.id.llExpand)
//    LinearLayout llExpand;
//    @BindView(R.id.tvRecharge)
//    TextView tvRecharge;
//    @BindView(R.id.tvWithDraw)
//    TextView tvWithDraw;
    //    @BindView(R.id.indicator)
//    TabPageIndicator indicator;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.cdlUSDT)
    CoordinatorLayout cdlUSDT;

    @BindView(R.id.tvUSDTCash)
    TextView tvUSDTCash;
    @BindView(R.id.tvEnableBalance)
    TextView tvEnableBalance;
    @BindView(R.id.tvFreezeBalance)
    TextView tvFreezeBalance;

    @BindView(R.id.tvUSDT)
    TextView tvUSDT;

    @BindView(R.id.tvTab1)
    TextView tvTab1;
    @BindView(R.id.tvTab2)
    TextView tvTab2;

    @BindView(R.id.llUsdt)
    TextView llUsdt;


    @BindView(R.id.tvRechargeCoin)
    TextView tvRechargeCoin;
    @BindView(R.id.tvTakeCoin)
    TextView tvTakeCoin;
    @BindView(R.id.tvBuyCoin)
    TextView tvBuyCoin;




    private MyPagerAdapter adapter;

    private static final String SYMBOL = "USDT";


    private Call<ResponseBody> getTradeInoutHomeCall;

    private Call<ResponseBody> getDefaultReceipt;

    private String noticeMsg="";//凌晨2:00后点击弹出对话框


    @Override
    protected int setLayoutId() {
        return R.layout.usdt_trade;
    }

    @Override
    protected String getActivityTitle() {
        return "USDT资产";
    }

    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //收到一条新信息
                if (vpContent != null && adapter != null) {
                    Object o = adapter.instantiateItem(vpContent, 0);
                    if (o != null && o instanceof UsdtOrCashTradeHisFragment) {
                        UsdtOrCashTradeHisFragment usdtOrCashTradeHisFragment = (UsdtOrCashTradeHisFragment) o;
                        usdtOrCashTradeHisFragment.notifyData();
                    }
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
//        tvRecharge.setOnClickListener(this);
//        tvWithDraw.setOnClickListener(this);
        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        llUsdt.setOnClickListener(this);


        tvRechargeCoin.setOnClickListener(this);
        tvTakeCoin.setOnClickListener(this);
        tvBuyCoin.setOnClickListener(this);

        slideTab(0);

    }

    @Override
    protected void showReConnection() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetTradeInoutHome();
    }

    private void slideTab(int arg0) {
        switch (arg0) {
            case 0:
                tvTab1.setSelected(true);
                tvTab2.setSelected(false);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case 1:
                tvTab1.setSelected(false);
                tvTab2.setSelected(true);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
        }
    }

    private void startGetTradeInoutHome() {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeInoutHomeCall);
        getTradeInoutHomeCall = VHttpServiceManager.getInstance().getVService().tradeInoutHome();
        getTradeInoutHomeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String tolUsdt = resultData.getItem("tolUsdt", String.class);
                    String tolCash = resultData.getItem("tolCash", String.class);

                    String holdUsdt = resultData.getItem("holdUsdt", String.class);
                    String frozenUsdt = resultData.getItem("frozenUsdt", String.class);


                    noticeMsg = resultData.getItem("noticeMsg", String.class);
                    tvUSDT.setText(tolUsdt);
                    tvUSDTCash.setText("≈¥ " + tolCash);

                    tvEnableBalance.setText(holdUsdt);
                    tvFreezeBalance.setText(frozenUsdt);

                }
            }
        });
    }

    private void startReceiptGetDefault() {
        CommonUtil.cancelCall(getDefaultReceipt);
        showProgressDialog();
        getDefaultReceipt = VHttpServiceManager.getInstance().getVService().receiptGetDefault();
        getDefaultReceipt.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    Receipt receipt = resultData.getObject("receipt", Receipt.class);
                    if (receipt != null) {
                        showWithDrawDialogFragment(receipt);
                    } else {
                        showAddReceiptTipsDialog();
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    WithDrawDialogFragment withDrawDialogFragment;

    public void showWithDrawDialogFragment(Receipt receipt) {
        withDrawDialogFragment = WithDrawDialogFragment.newInstance(receipt);
        withDrawDialogFragment.showDialog(getSupportFragmentManager(), "");
    }

    TjrBaseDialog addReceiptTipsDialog;

    private void showAddReceiptTipsDialog() {
        if (addReceiptTipsDialog == null) {
            addReceiptTipsDialog = new TjrBaseDialog(this) {
                @Override
                public void onclickOk() {
                    dismiss();
                    PaymentTermActivity.pageJump(USDTTradeActivity.this, 0);
                }

                @Override
                public void onclickClose() {
                    dismiss();
                }

                @Override
                public void setDownProgress(int progress) {
                }
            };
        }
        addReceiptTipsDialog.setTitleVisibility(View.GONE);
        addReceiptTipsDialog.setMessage("您还未添加收款方式");
        addReceiptTipsDialog.setBtnOkText("添加");
        addReceiptTipsDialog.show();
    }

    TjrBaseDialog noticeDialog;

    private void showNoticeDialogDialog(String tips) {
        noticeDialog = new TjrBaseDialog(this) {
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
        noticeDialog.setTitleVisibility(View.GONE);
        noticeDialog.setBtnColseVisibility(View.GONE);
        noticeDialog.setMessage(tips);
        noticeDialog.setBtnOkText("知道了");
        noticeDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tvRecharge:
//                if(!TextUtils.isEmpty(noticeMsg)){
//                    showNoticeDialogDialog(noticeMsg);
//                    return;
//                }
////                PageJumpUtil.pageJump(this, RechargeUsdtActivity.class);
//                showRechargeUsdtDialogFragment(0);
//                break;
//            case R.id.tvWithDraw:
//                if(!TextUtils.isEmpty(noticeMsg)){
//                    showNoticeDialogDialog(noticeMsg);
//                    return;
//                }
////                PageJumpUtil.pageJump(this, WithdrawUsdtActivity.class);
//                startReceiptGetDefault();
//                break;

            case R.id.tvTab1:
                if (vpContent.getCurrentItem() != 0) {
                    vpContent.setCurrentItem(0);
                }
                break;
            case R.id.tvTab2:
                if (vpContent.getCurrentItem() != 1) {
                    vpContent.setCurrentItem(1);
                }
                break;
            case R.id.llUsdt:
                CommonWebViewActivity.pageJumpCommonWebViewActivity(USDTTradeActivity.this, CommonConst.USDTDETAILS);
                break;

            case R.id.tvRechargeCoin:
//                PageJumpUtil.pageJump(USDTTradeActivity.this,RechargeCoinActivity.class);
                RechargeCoinActivity.pageJump(USDTTradeActivity.this,"USDT");
                break;

            case R.id.tvTakeCoin:
//                PageJumpUtil.pageJump(USDTTradeActivity.this,TakeCoinActivity.class);
                TakeCoinActivity.pageJump(USDTTradeActivity.this,"USDT");
                break;
            case R.id.tvBuyCoin:
                PageJumpUtil.pageJump(USDTTradeActivity.this,LegalTenderBuyCoinActivity.class);
                break;



        }
    }


    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 2;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return position == 0 ? "　出售中　" : "　未上架　";
//        }

        @Override
        public Fragment getItem(int i) {
//            return UsdtOrCashTradeHisFragment.newInstance(SYMBOL,"",0);
            return i == 0 ? UsdtOrCashTradeHisFragment.newInstance(SYMBOL, "", 0, 1) : UsdtOrCashTradeHisFragment.newInstance(SYMBOL, "", 1, 1);

        }
    }

}
