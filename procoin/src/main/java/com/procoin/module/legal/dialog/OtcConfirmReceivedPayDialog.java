package com.procoin.module.legal.dialog;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;
import com.procoin.util.CommonUtil;

/**
 * 我确认已收到付款
 * Created by zhengmj on 19-7-26.
 */

public abstract class OtcConfirmReceivedPayDialog extends AbstractBaseDialog implements View.OnClickListener {


    private CheckBox cbSign;
    private TextView btnClose;
    private TextView btnOk;

    private Context context;

    public OtcConfirmReceivedPayDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.order_pay_confirm_received_dialog);
        cbSign = findViewById(R.id.cbSign);
        btnClose = findViewById(R.id.btnClose);
        btnOk = findViewById(R.id.btnOk);
        btnOk.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                onclickClose();
                break;
            case R.id.btnOk:
                if (!cbSign.isChecked()) {
                    CommonUtil.showmessage("请确定已登录收款账户，并核对收款无误", context);
                    return;
                }
                dismiss();
                onclickOk();
                break;
        }
    }

    @Override
    public void onclickClose() {
        dismiss();
    }

    @Override
    public void setDownProgress(int progress) {

    }
}
