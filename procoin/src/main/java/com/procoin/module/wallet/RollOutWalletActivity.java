package com.procoin.module.wallet;


import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.home.entity.Store;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 转出存币宝
 */
public class RollOutWalletActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvAmountSymbol)
    TextView tvAmountSymbol;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.llSymbol)
    LinearLayout llSymbol;
//    @BindView(R.id.cbSign)
//    CheckBox cbSign;
    @BindView(R.id.tvProfitSymbol)
    TextView tvProfitSymbol;
    @BindView(R.id.tvProfit)
    TextView tvProfit;
    @BindView(R.id.llProfit)
    LinearLayout llProfit;
//    @BindView(R.id.cbSign2)
//    CheckBox cbSign2;
    @BindView(R.id.tvTargetSymbol)
    TextView tvTargetSymbol;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvCurrSymbol)
    TextView tvCurrSymbol;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;
    @BindView(R.id.tvFrozenAmount)
    TextView tvFrozenAmount;
    @BindView(R.id.tvRollOutWallet)
    TextView tvRollOutWallet;
    @BindView(R.id.ivSeclected1)
    ImageView ivSeclected1;
    @BindView(R.id.ivSeclected2)
    ImageView ivSeclected2;

    private String symbol = "";//币种

    private Call<ResponseBody> getPrybarStoreConfigCall;
    private Call<ResponseBody> getPrybarStoreCreateOutCall;
    private Store store;

    private int type = 0;//0转出币，1转出收益


    @Override
    protected int setLayoutId() {
        return R.layout.roll_out_wallet;
    }

    @Override
    protected String getActivityTitle() {
        return "转出";
    }

    public static void pageJump(Context context, String symbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        PageJumpUtil.pageJump(context, RollOutWalletActivity.class, bundle);
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
        mActionBar.setTitle("转出" + symbol);

        llSymbol.setOnClickListener(this);
        llProfit.setOnClickListener(this);
        tvRollOutWallet.setOnClickListener(this);
        tvAll.setOnClickListener(this);

        ivSeclected1.setVisibility(View.VISIBLE);
        ivSeclected2.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetPrybarStoreAssetConfig();

    }


    private void startGetPrybarStoreAssetConfig() {
        CommonUtil.cancelCall(getPrybarStoreConfigCall);
        getPrybarStoreConfigCall = VHttpServiceManager.getInstance().getVService().prybarStoreConfig(symbol, -1);
        getPrybarStoreConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    store = resultData.getObject("storeResult", Store.class);
                    setStore();
                }
            }
        });
    }


    private void setStore() {
        if (store != null) {
            tvAmountSymbol.setText(store.amountSymbol + "数量");
            tvAmount.setText(store.amount);

            tvProfitSymbol.setText(store.profitSymbol + "收益");
            tvProfit.setText(store.profit);

            if (type == 0) {
                tvTargetSymbol.setText("转出" + store.amountSymbol + "数量到持仓");
                etAmount.setHint("请输入转出" + store.amountSymbol + "数量");
                tvCurrSymbol.setText(store.amountSymbol);
                tvEnableAmount.setText("可转出数量" + store.holdAmount + " " + store.amountSymbol);
                tvFrozenAmount.setVisibility(View.VISIBLE);
                tvFrozenAmount.setText("冻结数量" + store.frozenAmount + " " + store.amountSymbol);

            } else {
                tvTargetSymbol.setText("转出" + store.profitSymbol + "数量到持仓");
                etAmount.setHint("请输入转出" + store.profitSymbol + "数量");
                tvCurrSymbol.setText(store.profitSymbol);
                tvEnableAmount.setText("可转出数量" + store.profit + " " + store.profitSymbol);
                tvFrozenAmount.setVisibility(View.INVISIBLE);
            }


        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSymbol:
                ivSeclected1.setVisibility(View.VISIBLE);
                ivSeclected2.setVisibility(View.GONE);
                type = 0;
                etAmount.setText("");
                setStore();
                break;
            case R.id.llProfit:
                ivSeclected1.setVisibility(View.GONE);
                ivSeclected2.setVisibility(View.VISIBLE);
                type = 1;
                etAmount.setText("");
                setStore();
                break;
            case R.id.tvAll:
                if (store == null) return;
                if (type == 0) {
                    etAmount.setText(store.holdAmount);
                    etAmount.setSelection(store.holdAmount.length());
                } else {
                    etAmount.setText(store.profit);
                    etAmount.setSelection(store.profit.length());
                }
                break;
            case R.id.tvRollOutWallet:
                if (store == null) return;
                String amountText = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amountText)) {
                    com.procoin.util.CommonUtil.showmessage("请输入转出数量", this);
                    return;
                }
                if (type == 0) {
                    if (Double.parseDouble(amountText) > Double.parseDouble(store.holdAmount)) {
                        CommonUtil.showmessage("可转出数量不足", this);
                        return;
                    }
                } else {
                    if (Double.parseDouble(amountText) > Double.parseDouble(store.profit)) {
                        CommonUtil.showmessage("可转出数量不足", this);
                        return;
                    }
                }
                startGetPrybarStoreAssetCreateOut(amountText);
                break;


        }
    }


    private void startGetPrybarStoreAssetCreateOut(String amount) {
        CommonUtil.cancelCall(getPrybarStoreCreateOutCall);
        getPrybarStoreCreateOutCall = VHttpServiceManager.getInstance().getVService().prybarStoreCreateOut(symbol, amount, type);
        getPrybarStoreCreateOutCall.enqueue(new MyCallBack(this) {
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
                PageJumpUtil.finishCurr(RollOutWalletActivity.this);
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

}
