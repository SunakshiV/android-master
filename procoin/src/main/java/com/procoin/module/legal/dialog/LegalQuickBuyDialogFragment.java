package com.procoin.module.legal.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.legal.LegalOrderInfoActivity;
import com.procoin.module.legal.adapter.SelectQuickPayWayAdapter;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.module.myhome.entity.AddPaymentTern;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.SimpleSpaceItemDecoration;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 快捷购买弹出框
 */

public class LegalQuickBuyDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rvType)
    RecyclerView rvType;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private SelectQuickPayWayAdapter selectPayWayAdapter;


    private OptionalOrder optionalOrder;
    private String amount = "0.00";
    private Call<ResponseBody> otcCreateOrderCall;

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static LegalQuickBuyDialogFragment newInstance(OptionalOrder optionalOrder, String amount) {
        LegalQuickBuyDialogFragment dialog = new LegalQuickBuyDialogFragment();
        dialog.optionalOrder = optionalOrder;
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
        View v = inflater.inflate(R.layout.legal_quick_buy_dialog, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ENTRUSTAMOUNT)) {
                amount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
        }


        selectPayWayAdapter = new SelectQuickPayWayAdapter(getActivity());
        rvType.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        rvType.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 3, 3, 3, 3));
        rvType.setAdapter(selectPayWayAdapter);
        selectPayWayAdapter.setOnItemclickListen(new SelectQuickPayWayAdapter.onItemclickListen() {
            @Override
            public void onItemclick(AddPaymentTern receipt) {
            }
        });

        if (optionalOrder != null) {
            Group<AddPaymentTern> payWay = new Gson().fromJson(optionalOrder.payWay, new TypeToken<Group<AddPaymentTern>>() {
            }.getType());
            selectPayWayAdapter.setGroup(payWay);
        }

        tvPrice.setText(optionalOrder.price + " CNY/USDT");
        tvAmount.setText(amount + " USDT");

        BigDecimal tolBalanceBD = new BigDecimal(amount).multiply(new BigDecimal(optionalOrder.price)).setScale(2, BigDecimal.ROUND_FLOOR);
        tvMoney.setText("¥ " + tolBalanceBD.toPlainString());

        tvSubmit.setOnClickListener(this);

//        Group<Receipt> selectPayWayGroup=new Group<>();
//        Receipt selectPayWay=null;
//        for (int i = 0; i < 3; i++) {
//             selectPayWay=new Receipt();
//            selectPayWay.bankName="银行卡"+i;
//            selectPayWay.paymentId=i;
//            selectPayWayGroup.add(selectPayWay);
//        }
//        selectPayWayAdapter.setGroup(selectPayWayGroup);
//
//        for (Receipt receipt : selectPayWayAdapter.getGroup()) {
//            if ("1".equals(receipt.paymentId)) {
//                selectPayWayAdapter.setSelected(receipt);
//                break;
//            }
//        }
        return v;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void startOtcCreateOrderCall(String buySell, long adId, String amount, String price, int receiptType) {
        CommonUtil.cancelCall(otcCreateOrderCall);
        ((TJRBaseToolBarActivity) getActivity()).showProgressDialog();
        otcCreateOrderCall = VHttpServiceManager.getInstance().getVService().otcCreateOrder(buySell, adId, amount, price, String.valueOf(receiptType));
        otcCreateOrderCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (getActivity() == null) return;
                ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    long orderId = resultData.getItem("orderId", Long.class);
                    LegalOrderInfoActivity.pageJump(getActivity(), orderId);
                    dismiss();

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                if (getActivity() != null) {
                    ((TJRBaseToolBarActivity) getActivity()).dismissProgressDialog();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:
                if (optionalOrder == null) return;

                int receiptType = selectPayWayAdapter.getSelectReceiptType();
                if (receiptType == -1) {
                    CommonUtil.showmessage("请选择商家收款方式", getActivity());
                    return;
                }
                startOtcCreateOrderCall("buy", optionalOrder.adId, amount, optionalOrder.price, receiptType);
                break;

            default:
                break;
        }
    }


}
