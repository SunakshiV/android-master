package com.procoin.module.legal.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.legal.LegalOrderInfoActivity;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 确认出售
 */

public class ConfirmSellDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvWay)
    TextView tvWay;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private OptionalOrder optionalOrder;
    private Receipt receipt;
    private String amount="0.00";

    private Call<ResponseBody> otcCreateOrderCall;


    /**
     * 非摘单 入参
     *
     * @return
     */
    public static ConfirmSellDialogFragment newInstance(OptionalOrder optionalOrder, Receipt receipt, String amount) {
        ConfirmSellDialogFragment dialog = new ConfirmSellDialogFragment();
        dialog.optionalOrder = optionalOrder;
        dialog.receipt = receipt;
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.ENTRUSTAMOUNT, amount);
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
        //入参处理
        Bundle b = getArguments();
        if (null == b) return;

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
        View v = inflater.inflate(R.layout.confirm_sell_dialog, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ENTRUSTAMOUNT)) {
                amount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
        }
        if (receipt != null && optionalOrder != null) {
            tvWay.setText(receipt.receiptTypeValue);
            tvPrice.setText(optionalOrder.price + " CNY/USDT");
            tvAmount.setText(amount + " USDT");
            BigDecimal tolBalanceBD = new BigDecimal(amount).multiply(new BigDecimal(optionalOrder.price)).setScale(2, BigDecimal.ROUND_FLOOR);
            tvMoney.setText("¥ " + tolBalanceBD.toPlainString());
        }
        tvSubmit.setOnClickListener(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->            onResume   isVisible = " + getUserVisibleHint());
//        if(etBalance!=null){
//            etBalance.post(new Runnable(){
//                @Override
//                public void run()
//                {
//                    etBalance.requestFocus();
//                    InputMethodManager imm =
//                            (InputMethodManager)etBalance.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null)
//                        imm.showSoftInput(etBalance, InputMethodManager.SHOW_IMPLICIT);
//                }
//            });
//        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

//        InputMethodManager imm =
//                (InputMethodManager)etBalance.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        Log.d("onDismiss","imm.isActive()=="+imm.isActive()+"  imm.isActive(etBalance)=="+imm.isActive(etBalance));
//        if (imm.isActive())
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        super.onDismiss(dialog);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->           setUserVisibleHint  isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void startOtcCreateOrderCall(String buySell, long adId, String amount, String price, int receiptType) {
        CommonUtil.cancelCall(otcCreateOrderCall);
        ((TJRBaseToolBarActivity)getActivity()).showProgressDialog();
        otcCreateOrderCall = VHttpServiceManager.getInstance().getVService().otcCreateOrder(buySell, adId, amount, price, String.valueOf(receiptType));
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
            case R.id.tvSubmit:
                if (receipt != null && optionalOrder != null) {
                    startOtcCreateOrderCall("sell",optionalOrder.adId,amount,optionalOrder.price,receipt.receiptType);
                }
                break;

            default:
                break;
        }
    }



}
