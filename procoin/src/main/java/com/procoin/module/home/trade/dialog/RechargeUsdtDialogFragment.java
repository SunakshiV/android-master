package com.procoin.module.home.trade.dialog;

import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.StockChartUtil;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 充值USDT
 */

public class RechargeUsdtDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.etBalance)
    EditText etBalance;
    @BindView(R.id.tvRandomNum)
    TextView tvRandomNum;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private long money = 0;
    private int randomNum;
    private String realMoney = "0.00";//==money+randomNum

    private double usdtRate;

    private static int AMOUNT_DECIMALCOUNT = 6;//数量的小数点数量

    private TJRBaseToolBarActivity mActivity;
    private Call<ResponseBody> getTradeConfigCall;

    private RechargeListen rechargeListen;




    public void setRechargeListen(RechargeListen rechargeListen) {
        this.rechargeListen = rechargeListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static RechargeUsdtDialogFragment newInstance(int recharge) {
        RechargeUsdtDialogFragment dialog = new RechargeUsdtDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.RECHARGE, recharge);
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
        View v = inflater.inflate(R.layout.recharge_usdt_dialog, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.RECHARGE)) {
                money = bundle.getInt(CommonConst.RECHARGE, 0);
            }
        }
        if (money > 0) {
            etBalance.setText(String.valueOf(money));
        }
        etBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s.toString())) {
                    money = Long.parseLong(s.toString());
                } else {
                    money = 0;
                }
                realMoney = money + "." + randomNum;
                setAmount();


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usdtRate == 0) return;
                if (TextUtils.isEmpty(etBalance.getText().toString())) {
                    com.procoin.http.util.CommonUtil.showmessage("请输入购买金额", getActivity());
                    return;
                }
                colseKeybord();
                if (rechargeListen != null) {
                    rechargeListen.goSelectPayWay(realMoney);
                }
//                long receiptId = selectPayWayAdapter.getSelectedReceiptId();
//                if (receiptId <= 0) {
//                    com.cropyme.http.util.CommonUtil.showmessage("请选择支付方式", getActivity());
//                    return;
//                }
//                startTradeCashOrder(realMoney, receiptId);
            }
        });

        startGetTradeConfig();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    private void startGetTradeConfig() {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("","");
        getTradeConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (!isAdded()) {
                    return;//不加这句有些手机会报错
                }
                if (resultData.isSuccess()) {
                    Log.d("startGetTradeConfig", "tvRandomNum==" + tvRandomNum + "  isDetached" + isAdded() + " isDetached" + isDetached());
                    usdtRate = resultData.getItem("usdtRate", Double.class);
                    randomNum = resultData.getItem("randomNum", Integer.class);
                    tvRandomNum.setText(String.valueOf(randomNum));
                    tvPrice.setText("购买单价: ￥" + StockChartUtil.formatNumber(2, usdtRate));
                    realMoney = money + "." + randomNum;
                    setAmount();
//                    tvUsdtBalance.setText("目前持有USDT:" + holdUsdt);// + "(" + "≈￥" + holdCash + ")");
                }
            }
        });
    }


    private void setAmount() {
        if (usdtRate == 0) return;
        if (money > 0) {
            BigDecimal amountBd = new BigDecimal(realMoney).divide(BigDecimal.valueOf(usdtRate), AMOUNT_DECIMALCOUNT, BigDecimal.ROUND_FLOOR);
            tvAmount.setText("购买数量: " + amountBd.toPlainString() + "USDT");
        } else {
            tvAmount.setText("购买数量： 0.00USDT");
        }

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
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public interface RechargeListen {
        void goSelectPayWay(String cny);
    }
}
