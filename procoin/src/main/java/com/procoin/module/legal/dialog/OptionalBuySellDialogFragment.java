package com.procoin.module.legal.dialog;

import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.trade.TransferCoinActivity;
import com.procoin.module.legal.LegalOrderInfoActivity;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;

/**
 * 自选区的买卖
 */

public class OptionalBuySellDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvLimit)
    TextView tvLimit;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;


    private OptionalOrder optionalOrder;

    private CountDownTimer timer;
    private Call<ResponseBody> outHoldAmountCall, otcCreateOrderCall;


    private String amount = "0.00";//数量
    private String holdAmount = "0.00";//可用usdt
    private int amount_decimalcount = 4;


    /**
     * 非摘单 入参
     *
     * @return
     */
    public static OptionalBuySellDialogFragment newInstance(OptionalOrder optionalOrder) {
        OptionalBuySellDialogFragment dialog = new OptionalBuySellDialogFragment();
        dialog.optionalOrder = optionalOrder;
//        Bundle bundle = new Bundle();
//        bundle.putString(CommonConst.BUYSELL, buySell);
//        dialog.setArguments(bundle);
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
        View v = inflater.inflate(R.layout.optional_buy_sell_dialog, container, false);
        ButterKnife.bind(this, v);
        Log.d("onCreateView","11111111111111");

        llTransfer.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        if ("buy".equals(optionalOrder.buySell)) {
            llTransfer.setVisibility(View.VISIBLE);
            tvTitle.setText("出售USDT");
            tvAll.setText("全部出售");
            tvPayWay.setText("实收款");
            tvBalance.setVisibility(View.VISIBLE);
            startOutHoldAmountCall();
        } else {
            llTransfer.setVisibility(View.GONE);
            tvTitle.setText("购买USDT");
            tvAll.setText("全部买入");
            tvPayWay.setText("实付款");
            tvBalance.setVisibility(View.GONE);

        }
        Log.d("onCreateView","2222222222222");
        tvLimit.setText("限额 ¥" + optionalOrder.minCny + "-¥" + optionalOrder.maxCny);
        tvPrice.setText(optionalOrder.price + " CNY/USDT");
//        tvAmount.setText(optionalOrder.price+" USDT");
        tvCancel.setText(optionalOrder.timeLimit + "s后自动取消");
        tvSubmit.setOnClickListener(this);

        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s)) return;
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > amount_decimalcount) {
                        s.delete(posDot + (amount_decimalcount + 1), posDot + (amount_decimalcount + 2));
                    }
                }
                amount = s.toString();
                tvAmount.setText(amount + " USDT");
                BigDecimal tolBalanceBD = new BigDecimal(s.toString()).multiply(new BigDecimal(optionalOrder.price)).setScale(2, BigDecimal.ROUND_FLOOR);
                tvMoney.setText("¥ " + tolBalanceBD.toPlainString());
            }
        });
        Log.d("onCreateView","33333333333");
        startCountDownTime();
        Log.d("onCreateView","44444444");

        return v;
    }


    public void startOutHoldAmountCall() {
        CommonUtil.cancelCall(outHoldAmountCall);
        outHoldAmountCall = VHttpServiceManager.getInstance().getVService().outHoldAmount("balance");
        outHoldAmountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdAmount = resultData.getItem("holdAmount", String.class);
                    tvBalance.setText("余额 " + holdAmount + " USDT");
                }
            }
        });
    }


    private void startCountDownTime() {
        if (optionalOrder == null) return;
//        Long ss = VeDate.strLongToDate(String.valueOf(orderCash.expireTime)).getTime() - VeDate.strLongToDate(String.valueOf(orderCash.createTime)).getTime();
        Log.d("startCountDownTime", "optionalOrder.timeLimit ==" + optionalOrder.timeLimit);
        if (optionalOrder.timeLimit > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(optionalOrder.timeLimit * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    Log.d("startCountDownTime", "millisUntilFinished ==" + millisUntilFinished);
                    tvCancel.setText((millisUntilFinished / 1000) + "s后自动取消");
                }

                public void onFinish() {
                    CommonUtil.showmessage("操作超时", getActivity());
                    dismiss();

                }
            };
            timer.start();
        }
    }




    @Override
    public void onDestroyView() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        super.onDestroyView();

    }

    public void startOtcCreateOrderCall(String buySell, long adId, String amount, String price) {
        CommonUtil.cancelCall(otcCreateOrderCall);
        ((TJRBaseToolBarActivity)getActivity()).showProgressDialog();
        otcCreateOrderCall = VHttpServiceManager.getInstance().getVService().otcCreateOrder(buySell, adId, amount, price, "");
        otcCreateOrderCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (getActivity() == null) return;
                ((TJRBaseToolBarActivity)getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    long orderId = resultData.getItem("orderId", Long.class);
                    LegalOrderInfoActivity.pageJump(getActivity(),orderId);
                    dismiss();
                }
            }
            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                if(getActivity()!=null){
                    ((TJRBaseToolBarActivity)getActivity()).dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llTransfer:
                TransferCoinActivity.pageJump(getActivity(), "");
                break;
            case R.id.tvAll:
                if (optionalOrder == null) return;
                if ("buy".equals(optionalOrder.buySell)) {
                    etAmount.setText(holdAmount);
                } else {
                    etAmount.setText(optionalOrder.amount);
                }
                etAmount.setSelection(etAmount.getText().toString().length());
                break;
            case R.id.tvSubmit:
                if (optionalOrder == null) return;
                String buySell = "buy";//这里buySell要倒过来
                if ("buy".equals(optionalOrder.buySell)) {
                    buySell = "sell";
                } else {
                    buySell = "buy";
                }
                startOtcCreateOrderCall(buySell, optionalOrder.adId, amount, optionalOrder.price);
                break;
            case R.id.tvCancel:
                dismiss();
                break;
            default:
                break;
        }
    }


}
