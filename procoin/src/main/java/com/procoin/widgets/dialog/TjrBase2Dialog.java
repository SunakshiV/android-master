package com.procoin.widgets.dialog;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.http.widget.dialog.base.AbstractBaseDialog;
import com.procoin.R;


public abstract class TjrBase2Dialog extends AbstractBaseDialog {
    public EditText tvEdit;
    public TextView tvTitle;
    public TextView btnOk;
    public TextView btnClose;

    public String name;

    public TjrBase2Dialog(Context context, String name) {
        super(context);
        this.name = name;
        initTheme(context);
    }

    public void setTvTitle(CharSequence str) {
        tvTitle.setText(str);
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
    public void setBtnCloseText(CharSequence str) {
        btnClose.setText(str);
    }

    public void setBtnCloseVisibility(int intVis) {
        btnClose.setVisibility(intVis);
    }


    public void setMaxLines(int lines) {
        tvEdit.setMaxLines(lines);
    }

    public void setInputType(int type) {
        tvEdit.setInputType(type);
    }

    public void setHint(CharSequence str) {
        tvEdit.setHint(str);
    }

    /**
     * aaaa
     *
     * @param context
     */
    private void initTheme(final Context context) {
        OnClick onClick = new OnClick();
        this.setContentView(R.layout.base2_abstart_dialog);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvEdit = (EditText) findViewById(R.id.tvEdit);
        tvEdit.setText(name);
        tvEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                Log.d("154","s == "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btnClose = (TextView) findViewById(R.id.btnClose);
        btnOk = (TextView) findViewById(R.id.btnOk);

        btnClose.setOnClickListener(onClick);
        btnOk.setOnClickListener(onClick);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(tvEdit, 0);
                tvEdit.setSelection(tvEdit.getText().length());
            }
        }, 500);

    }


    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v.getId() == com.procoin.http.R.id.btnOk) {
                onclickOk();
            } else if (v.getId() == com.procoin.http.R.id.btnClose) {
                onclickClose();
            }
        }

    }
}
