package com.procoin.module.home.trade.dialog;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.util.CommonUtil;

/**
 * Created by zhengmj on 19-7-26.
 */

public abstract class RechargeOrTransferDialog extends AbstractBaseDialog implements View.OnClickListener {

    private TextView btnGoRecharge;
    private TextView tvTitle; // 标题
    private TextView tvMessage;// 内容
    private TextView btnGoTransfer;
    private TextView btnClose;

    private Context context;

    public RechargeOrTransferDialog(Context context) {
        super(context);
        this.context = context;
        setContentView(R.layout.charge_or_transfer_dialog);
        tvTitle = this.findViewById(R.id.tvTitle);
        tvMessage = this.findViewById(R.id.tvMessage);
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        btnGoTransfer =this.findViewById(R.id.btnGoTransfer);
        btnGoRecharge = findViewById(R.id.btnGoRecharge);
        btnClose =  this.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(this);
        btnGoTransfer.setOnClickListener(this);
        btnGoRecharge.setOnClickListener(this);
    }
    public void setTvTitle(CharSequence str) {
        tvTitle.setText(str);
    }

    public void setMessage(CharSequence str) {
        tvMessage.setText(str);
    }
    public abstract void onclickRecharge();

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                onclickClose();
                break;
            case R.id.btnGoRecharge:
                dismiss();
                onclickRecharge();
                break;
            case R.id.btnGoTransfer:
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
