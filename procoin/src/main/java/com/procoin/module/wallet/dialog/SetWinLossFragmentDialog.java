package com.procoin.module.wallet.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.util.CommonUtil;
import com.procoin.util.StockChartUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置止盈止损
 */

public class SetWinLossFragmentDialog extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvStopWinPrice)
    TextView tvStopWinPrice;
    @BindView(R.id.etStopWin)
    EditText etStopWin;
    @BindView(R.id.tvStopLossPrice)
    TextView tvStopLossPrice;
    @BindView(R.id.etStopLoss)
    EditText etStopLoss;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    public double stopWin;// 设置止盈的百分比，如：0.2=20%，0不设置
    public double stopLoss;// 设置止损的百分比，如：0.2=20%，0不设置，止损有限
    public double stopMaxLoss;// 最大止损百分比
    public double openCostPrice;//开仓：成本价
    //    public int multiNum;//杠杆倍数
    public double borrowBalanceValue;
    public double bailBalanceValue;
    public int buySell;//方向
    public int priceDecimals;//小数点数量

    public double interest;//利息

    private SetStopWinLossListen setStopWinLossListen;

    public void setSetStopWinLossListen(SetStopWinLossListen setStopWinLossListen) {
        this.setStopWinLossListen = setStopWinLossListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static SetWinLossFragmentDialog newInstance(int buySell, double openCostPrice, double stopWin, double stopLoss, double stopMaxLoss, double borrowBalanceValue, double bailBalanceValue, double interest, int priceDecimals) {
        SetWinLossFragmentDialog dialog = new SetWinLossFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("buySell", buySell);
        bundle.putDouble("openCostPrice", openCostPrice);
        bundle.putDouble("stopMaxLoss", stopMaxLoss);
        bundle.putDouble("stopWin", stopWin);
        bundle.putDouble("stopLoss", stopLoss);
        bundle.putDouble("borrowBalanceValue", borrowBalanceValue);
        bundle.putDouble("bailBalanceValue", bailBalanceValue);
        bundle.putDouble("interest", interest);
//        bundle.putInt("multiNum", multiNum);
        bundle.putInt("priceDecimals", priceDecimals);
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
        buySell = b.getInt("buySell", 1);
        openCostPrice = b.getDouble("openCostPrice", 0);
        stopMaxLoss = b.getDouble("stopMaxLoss", 0);
        stopWin = b.getDouble("stopWin", 0);
        stopLoss = b.getDouble("stopLoss", 0);
        borrowBalanceValue = b.getDouble("borrowBalanceValue", 0);
        bailBalanceValue = b.getDouble("bailBalanceValue", 0);
//        multiNum = b.getInt("multiNum", 0);
        priceDecimals = b.getInt("priceDecimals", 2);

        interest = b.getDouble("interest", 0);
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
        View v = inflater.inflate(R.layout.set_win_loss_dialog, container, false);
        ButterKnife.bind(this, v);
        tvSubmit.setOnClickListener(this);
        if (stopWin > 0) {
            etStopWin.setText(String.valueOf(stopWin));
        } else {
            etStopWin.setText("");
        }
        tvStopWinPrice.setText(getStopWinPrice(stopWin));
        etStopLoss.setHint("止损不能大于" + stopMaxLoss);
        if (stopLoss > 0) {
            etStopLoss.setText(String.valueOf(stopLoss));
        } else {
            etStopLoss.setText("");
        }
        tvStopLossPrice.setText(getStopLossPrice(stopLoss));

        etStopWin.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > 2) {//最多4位小数
                        s.delete(posDot + 3, posDot + 4);
                    }
                }

                String text = s.toString();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                double stopWinRate = Double.parseDouble(text);
                tvStopWinPrice.setText(getStopWinPrice(stopWinRate));

            }
        });

        etStopLoss.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > 2) {//最多4位小数
                        s.delete(posDot + 3, posDot + 4);
                    }
                }
                String text = s.toString();
                if (TextUtils.isEmpty(text)) {
                    return;
                }
                double stopLossRate = Double.parseDouble(text);
                tvStopLossPrice.setText(getStopLossPrice(stopLossRate));

            }
        });


        return v;
    }


    //止盈价格公式   止盈价格=开盘价*(1+(buysell)*止盈率/(杠杆倍数*100))
    private String getStopWinPrice(double rate) {
        if (rate == 0) return "无设置";
        double m = bailBalanceValue-interest;
        if(m<=0)return "无设置";
        double ret = openCostPrice * (1 + buySell * rate / ((borrowBalanceValue / m) * 100));
        if (ret <= 0) {
            return "无设置";
        }
        return StockChartUtil.formatNumber(priceDecimals, ret);
    }

    //止损价格公式   止盈价格=开盘价*(1+(buysell)*止盈率/(杠杆倍数*100))
    private String getStopLossPrice(double rate) {
        if (rate == 0) return "无设置";
        double m = bailBalanceValue-interest;
        if(m<=0)return "无设置";
        double ret = openCostPrice * (1 - buySell * rate / ((borrowBalanceValue / m) * 100));
        if (ret <= 0) {
            return "无设置";
        }
        return StockChartUtil.formatNumber(priceDecimals, ret);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSubmit:

                String stopWin = etStopWin.getText().toString().trim();
                String stopLoss = etStopLoss.getText().toString().trim();

                if (TextUtils.isEmpty(stopWin)) {
                    stopWin = "0.0";
                }
                if (TextUtils.isEmpty(stopLoss)) {
                    stopLoss = "0.0";
                }
                if (setStopWinLossListen != null) {
                    setStopWinLossListen.goSetStopWinLoss(stopWin, stopLoss);
                }
                break;

            default:
                break;
        }
    }

    public interface SetStopWinLossListen {
        void goSetStopWinLoss(String stopWin, String stopLoss);
    }
}
