package com.procoin.module.wallet.dialog;

import android.content.Context;
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
import com.procoin.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 设置止盈
 */

public class SetWinFragmentDialog extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.etStopWin)
    EditText etStopWin;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    public String stopWinPrice = "";// 设置止盈
    public int priceDecimals;//小数点数量


    private SetStopWinListen setStopWinListen;

    public void setSetStopWinListen(SetStopWinListen setStopWinListen) {
        this.setStopWinListen = setStopWinListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static SetWinFragmentDialog newInstance(String stopWinPrice, int priceDecimals) {
        SetWinFragmentDialog dialog = new SetWinFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("stopWinPrice", stopWinPrice);
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
        stopWinPrice = b.getString("stopWinPrice", "");
        priceDecimals = b.getInt("priceDecimals", 2);
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
        View v = inflater.inflate(R.layout.set_win_dialog, container, false);
        ButterKnife.bind(this, v);
        tvSubmit.setOnClickListener(this);
        if (!stopWinPrice.equals("0")) etStopWin.setText(stopWinPrice);
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
                    if (s.length() - 1 - posDot > priceDecimals) {//最多4位小数
                        s.delete(posDot + (priceDecimals + 1), posDot + (priceDecimals + 2));
                    }
                }
            }
        });


        return v;
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

                if (TextUtils.isEmpty(stopWin)) {
                    stopWin = "0.0";
                }
                colseKeybord();
                if (setStopWinListen != null) {
                    setStopWinListen.goSetStopWin(stopWin);
                }
                break;

            default:
                break;
        }
    }

    public interface SetStopWinListen {
        void goSetStopWin(String stopWin);
    }

    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
