package com.procoin.http.widget.dialog.ui;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.procoin.http.R;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;

public abstract class TjrBaseDialog extends AbstractBaseDialog {
    private TextView tvTitle; // 标题
    private TextView tvMessage;// 内容
    private TextView btnOk;
    private TextView btnClose;



    public TjrBaseDialog(Context context) {
        super(context);
        initTheme(context);
    }

    public void setTvTitle(CharSequence str) {
        tvTitle.setText(str);
    }

    public void setMessage(CharSequence str) {
        tvMessage.setText(str);
    }

    /**
     * ok按钮
     *
     * @param str
     */
    public void setBtnOkText(CharSequence str) {
        btnOk.setText(str);
    }

    /**
     * 关闭按钮
     *
     * @param str
     */
    public void setBtnColseText(CharSequence str) {
        btnClose.setText(str);
    }

    public void setBtnColseVisibility(int intVis) {
        btnClose.setVisibility(intVis);
    }

    public void setTitleVisibility(int intVis) {
        tvTitle.setVisibility(intVis);
    }

    private void initTheme(Context context) {
        OnClick onClick = new OnClick();
        this.setContentView(R.layout.base_abstract_dialog);
        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvMessage = (TextView) this.findViewById(R.id.tvMessage);
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        // tvMessage.setText(text.getText());
        btnOk = (TextView) this.findViewById(R.id.btnOk);
        btnClose = (TextView) this.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(onClick);
        btnOk.setOnClickListener(onClick);
    }

    public void setCloseVisibility(int v) {
        if (btnClose != null) btnClose.setVisibility(v);
    }


    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TjrBaseDialog.this.dismiss();
            if (v.getId() == R.id.btnOk) {
                onclickOk();
            } else if (v.getId() == R.id.btnClose) {
                onclickClose();
            }
        }

    }

}
