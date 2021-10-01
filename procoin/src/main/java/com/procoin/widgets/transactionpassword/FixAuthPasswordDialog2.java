package com.procoin.widgets.transactionpassword;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.module.myhome.SettingPayPasswordActivity;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;

public class FixAuthPasswordDialog2 extends DialogFragment {
    private TextView tvForget;
    private TextView tvMsg;
    private ImageView btn_cancel;
    private GridPasswordView gpv_normal;
    private PassWordFinish callBack;
    public Context context;

    public void setCallBack(PassWordFinish callBack) {
        this.callBack = callBack;
    }

    public static FixAuthPasswordDialog2 newInstance(Context context) {
        FixAuthPasswordDialog2 fixAuthPasswordDialog2 = new FixAuthPasswordDialog2();
        fixAuthPasswordDialog2.context = context;
        return fixAuthPasswordDialog2;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        setCancelable(false);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pay_auth_password_dialog, container, false);
        OnClick onclick = new OnClick();
        gpv_normal = (GridPasswordView) view.findViewById(R.id.gpv_normal);
        gpv_normal.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                Log.d("GridPasswordView","psw=="+psw);

            }

            @Override
            public void onInputFinish(final String psw) {
                gpv_normal.getHandler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        gpv_normal.clearPassword();
                        tvMsg.setText("");
                        colseKeybord();
                        dismiss();
                        if (callBack != null) {
                            callBack.checkPassWordFinsh(psw);
                        }
                    }
                },300);

            }
        });
        tvForget = (TextView) view.findViewById(R.id.tvForget);
        tvForget.setOnClickListener(onclick);
        btn_cancel = (ImageView) view.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(onclick);
        tvMsg = (TextView) view.findViewById(R.id.tvMsg);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        return view;
    }


    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvForget:
                    PageJumpUtil.pageJump(context, SettingPayPasswordActivity.class);
                    break;
                case R.id.btn_cancel:
                    colseKeybord();
                    dismiss();
//                    cancel();

                    break;
            }
        }
    }

    private void colseKeybord() {
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public interface PassWordFinish {
        void checkPassWordFinsh(String strpw);
    }

}
