package com.procoin.module.home.trade.dialog;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.trade.WithDrawInfoActivity;
import com.procoin.module.myhome.PaymentTermActivity;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 充值USDT
 */

public class WithDrawDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.ivReceiptLogo)
    ImageView ivReceiptLogo;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvDefault)
    TextView tvDefault;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.rlSelectAccount)
    LinearLayout rlSelectAccount;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvUsdtBalance)
    TextView tvUsdtBalance;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;


    private TJRBaseToolBarActivity mActivity;
    private double usdtRateWithdraw;
    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> getInoutCreateCall;
    private String holdUsdt = "0.0000000";
    private String holdCash = "0.00";

    private double amount;

    private static final int decimalCount = 6;//提现小数点数量

    private Receipt receipt;
    private TjrImageLoaderUtil tjrImageLoaderUtil;

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static WithDrawDialogFragment newInstance(Receipt receipt) {
        WithDrawDialogFragment dialog = new WithDrawDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonConst.RECEIPT, receipt);
        dialog.setArguments(bundle);
        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreate ");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        mActivity = (TJRBaseToolBarActivity) getActivity();
        //入参处理
        Bundle b = getArguments();
        if (null == b) return;
        receipt = b.getParcelable(CommonConst.RECEIPT);
    }

    @Override
    public void onStart() {
        CommonUtil.LogLa(2, "OLStarHomeDialogFragment                      ---> onStart ");
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.withdraw_usdt_dialog, container, false);
        ButterKnife.bind(this, v);
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        tvAll.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        rlSelectAccount.setOnClickListener(this);
        setReceiptData();

        etAmount.addTextChangedListener(new TextWatcher() {
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
                amount = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    amount = Double.parseDouble(s.toString());
                } else {
                    amount = 0;
                }

                if (amount > 0) {
                    tvMoney.setText("￥" + StockChartUtil.formatNumber(2, amount * usdtRateWithdraw));
//                    tvOrderOutline.setVisibility(View.VISIBLE);
//                    tvOrderOutline.setText("您正在进行当日有效有损限价单,以￥" + StockChartUtil.formatNumber(2, usdtRate) + "的价格卖出USDT总数量" + StockChartUtil.formatNumber(8, amount));
                } else {
                    tvMoney.setText("￥0.00");
//                    tvOrderOutline.setVisibility(View.GONE);
                }

            }
        });
        startGetTradeConfig();
        return v;
    }

    public void setReceiptData() {
        if (receipt != null && receipt.paymentId > 0) {
            ivReceiptLogo.setVisibility(View.VISIBLE);
            llAccount.setVisibility(View.VISIBLE);
            tjrImageLoaderUtil.displayImage(receipt.receiptLogo, ivReceiptLogo);
            tvAccount.setText(receipt.receiptNo);
            tvName.setText(receipt.receiptName);
            if (receipt.isDefault == 1) {
                tvDefault.setVisibility(View.VISIBLE);
            } else {
                tvDefault.setVisibility(View.GONE);
            }
            if (receipt.receiptType == 1 || receipt.receiptType == 2) {
                ivPayWay.setVisibility(View.VISIBLE);
                tjrImageLoaderUtil.displayImage(receipt.qrCode, ivPayWay);
            } else {
                ivPayWay.setVisibility(View.GONE);
            }
        }
    }


    private void startGetTradeConfig() {
        CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("", decimalCount);
        getTradeConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (!isAdded()) {
                    return;//不加这句有些手机会报错
                }
                if (resultData.isSuccess()) {
                    usdtRateWithdraw = resultData.getItem("usdtRateWithdraw", Double.class);
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    holdCash = resultData.getItem("holdCash", String.class);
                    tvPrice.setText("市价: ￥" + StockChartUtil.formatNumber(2, usdtRateWithdraw));
                    tvUsdtBalance.setText("目前持有USDT:" + holdUsdt);// + "(" + "≈￥" + holdCash + ")");

                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x789 && data != null) {
            receipt = data.getParcelableExtra("Receipt");
            setReceiptData();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rlSelectAccount:
                Bundle bundle = new Bundle();
                bundle.putInt(CommonConst.JUMPTYPE, 1);
                Intent intent = new Intent();
                intent.setClassName(getActivity().getPackageName(), PaymentTermActivity.class.getName());
                intent.putExtras(bundle);
                PageJumpUtil.pageJumpResult(WithDrawDialogFragment.this, intent, 0x123);
//                PageJumpUtil.pageJumpResult(getActivity(), PaymentTermActivity.class, bundle);
                break;
            case R.id.tvSubmit:
                if (usdtRateWithdraw == 0) return;
                if (receipt == null || receipt.paymentId == 0) {
                    CommonUtil.showmessage("请添加收款方式", getActivity());
                    return;
                }
//                String amount = etAmount.getText().toString();
                if (amount <= 0) {
                    CommonUtil.showmessage("请输入提现数量", getActivity());
                    return;
                }
                if (amount > Double.parseDouble(holdUsdt)) {
                    CommonUtil.showmessage("USDT余额不足", getActivity());
                    return;
                }
                colseKeybord();
                startTradeCashOrder(String.valueOf(amount), receipt.paymentId, "");
                break;
            case R.id.tvAll:
                if (usdtRateWithdraw == 0) return;
                etAmount.setText(holdUsdt);
                etAmount.setSelection(etAmount.getText().length());
                break;
            default:
                break;
        }
    }


    private void startTradeCashOrder(final String entrustAmount, final long receiptId, String payPass) {
        com.procoin.http.util.CommonUtil.cancelCall(getInoutCreateCall);
        mActivity.showProgressDialog();
        getInoutCreateCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderSell(entrustAmount, receiptId, payPass);
        getInoutCreateCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                mActivity.dismissProgressDialog();
                if (resultData.isSuccess()) {
                    com.procoin.http.util.CommonUtil.showmessage(resultData.msg, getActivity());
                    OrderCash orderCash = resultData.getObject("order", OrderCash.class);
//                    mActivity.getApplicationContext().orderCash = orderCash;
                    if (orderCash != null) {
                        WithDrawInfoActivity.pageJump(mActivity,orderCash.orderCashId);
//                        PageJumpUtil.pageJump(mActivity, WithDrawUsdtSuccessActivity.class);
                        dismiss();
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                PageJumpUtil.finishCurr(mActivity);
//
//                            }
//                        }, 500);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                mActivity.dismissProgressDialog();
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startTradeCashOrder(entrustAmount, receiptId, pwString);
            }
        });
    }

    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
