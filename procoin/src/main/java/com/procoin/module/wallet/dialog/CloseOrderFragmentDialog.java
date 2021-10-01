package com.procoin.module.wallet.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.TextUtils;
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
 * 普通平仓
 */

public class CloseOrderFragmentDialog extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvHoldAmount)
    TextView tvHoldAmount;


    private String holdHand="0";

    private SetAmountListen setAmountListen;


    public void setSetAmountListen(SetAmountListen setAmountListen) {
        this.setAmountListen = setAmountListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static CloseOrderFragmentDialog newInstance(String holdHand) {
        CloseOrderFragmentDialog dialog = new CloseOrderFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("holdHand", holdHand);
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
        holdHand = b.getString("holdHand", "0");
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
        View v = inflater.inflate(R.layout.close_order_dialog, container, false);
        ButterKnife.bind(this, v);
        tvSubmit.setOnClickListener(this);
        tvHoldAmount.setText("持仓手数: "+holdHand);

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

                String amount = etAmount.getText().toString().trim();

                if (TextUtils.isEmpty(amount)) {
                    amount = "0";
                }
                colseKeybord();
                if (setAmountListen != null) {
                    setAmountListen.setAmount(amount);
                }
                break;

            default:
                break;
        }
    }

    public interface SetAmountListen {
        void setAmount(String amount);
    }

    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
