package com.procoin.module.wallet;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 转入存币宝
 */
public class IntoWalletActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvSymbol1)
    TextView tvSymbol1;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvSymbol2)
    TextView tvSymbol2;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;
    @BindView(R.id.tvIntoWallet)
    TextView tvIntoWallet;

    private String symbol = "";//币种

    private String storeSymbol;
    private String amount;
    private String minInAmount;


    private Call<ResponseBody> getPrybarStoreConfigCall;

    private Call<ResponseBody> getPrybarStoreCreateInCall;

    @Override
    protected int setLayoutId() {
        return R.layout.into_wallet;
    }

    @Override
    protected String getActivityTitle() {
        return "转入";
    }

    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        PageJumpUtil.pageJump(context, IntoWalletActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
//        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
//        tvKbtRules.setOnClickListener(this);
//        tvBuyBack.setOnClickListener(this);
//        tvAssignment.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }


        }
        mActionBar.setTitle("转入" + symbol);
        tvAll.setOnClickListener(this);
        tvIntoWallet.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetPrybarStoreAssetConfig();

    }


    private void startGetPrybarStoreAssetConfig() {
        CommonUtil.cancelCall(getPrybarStoreConfigCall);
        getPrybarStoreConfigCall = VHttpServiceManager.getInstance().getVService().prybarStoreConfig(symbol, 1);
        getPrybarStoreConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {

                    storeSymbol = resultData.getItem("storeSymbol", String.class);
                    amount = resultData.getItem("amount", String.class);
                    minInAmount = resultData.getItem("minInAmount", String.class);


                    tvSymbol1.setText("转入" + storeSymbol + "数量到存币宝");
                    tvSymbol2.setText(storeSymbol);
                    etAmount.setHint("请最低转入" + minInAmount);
                    tvEnableAmount.setText("可用数量：" + amount + " " + storeSymbol);
                }
            }
        });
    }

    private void startGetPrybarStoreAssetCreateIn(String amount) {
        CommonUtil.cancelCall(getPrybarStoreCreateInCall);
        getPrybarStoreCreateInCall = VHttpServiceManager.getInstance().getVService().prybarStoreCreateIn(symbol, amount);
        getPrybarStoreCreateInCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    showSuccessDialog(resultData.msg);
                }
            }
        });
    }
    TjrBaseDialog delDialog;

    private void showSuccessDialog(String msg) {
        delDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.finishCurr(IntoWalletActivity.this);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delDialog.setMessage(msg);
        delDialog.setBtnOkText("确定");
        delDialog.setBtnColseVisibility(View.GONE);
        delDialog.setTitleVisibility(View.GONE);
        delDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAll:
                if (!TextUtils.isEmpty(amount)) {
                    etAmount.setText(amount);
                    etAmount.setSelection(amount.length());
                }
                break;
            case R.id.tvIntoWallet:
                String amountText = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amountText)) {
                    com.procoin.util.CommonUtil.showmessage("请输入转入数量", this);
                    return;
                }
                if (Double.parseDouble(amountText) > Double.parseDouble(amount)) {
                    CommonUtil.showmessage("可用数量不足", this);
                    return;
                }
                startGetPrybarStoreAssetCreateIn(amountText);
                break;


        }
    }

}
