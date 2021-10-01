package com.procoin.widgets.transactionpassword;

import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.module.myhome.SettingPayPasswordActivity;
import com.procoin.util.PageJumpUtil;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;
import com.procoin.R;

/**
 * 已经废弃，请使用FixAuthPasswordDialog2
 */
public class FixAuthPasswordDialog extends AbstractBaseDialog {
    private TextView tvForget;
    private TextView tvMsg;
    private ImageView btn_cancel;
    private GridPasswordView gpv_normal;
    private Context context;
    public PassWordFinish callBack;

    public FixAuthPasswordDialog(Context context) {
        super(context);
        initTheme(context);

    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onclickOk() {

    }

    @Override
    public void onclickClose() {

    }

    @Override
    public void setDownProgress(int progress) {

    }

    private void initTheme(Context context) {
        this.context = context;
        OnClick onclick = new OnClick();
        this.setContentView(R.layout.pay_auth_password_dialog);
        gpv_normal = (GridPasswordView) findViewById(R.id.gpv_normal);
        gpv_normal.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {

            }

            @Override
            public void onInputFinish(String psw) {
                gpv_normal.clearPassword();
                tvMsg.setText("");
                if (callBack != null) {
                    callBack.checkPassWordFinsh(psw);
                }
            }
        });
        tvForget = (TextView) findViewById(R.id.tvForget);
        tvForget.setOnClickListener(onclick);
        btn_cancel = (ImageView) findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(onclick);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tvForget:
                    PageJumpUtil.pageJump(context, SettingPayPasswordActivity.class);
                    break;
                case R.id.btn_cancel:
                    dismiss();
//                    cancel();

                    break;
            }
        }
    }


    public interface PassWordFinish {
        void checkPassWordFinsh(String strpw);
    }

}
