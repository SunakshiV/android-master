package com.procoin.module.dialog;

import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;

public abstract class TjrUpdateDialog extends AbstractBaseDialog {
    private TextView tvTitle; // 标题
    private TextView tvMessage;// 内容
    private TextView btnOk;
    private TextView btnClose;
    private ImageView ivRocket;


    public TjrUpdateDialog(Context context) {
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

        this.setContentView(R.layout.update_dialog);

        tvTitle = (TextView) this.findViewById(R.id.tvTitle);
        tvMessage = (TextView) this.findViewById(R.id.tvMessage);
        tvMessage.setMovementMethod(ScrollingMovementMethod.getInstance());
        // tvMessage.setText(text.getText());
        btnOk = (TextView) this.findViewById(R.id.btnOk);
        btnClose = (TextView) this.findViewById(R.id.btnClose);

        ivRocket = (ImageView) this.findViewById(R.id.ivRocket);
        btnClose.setOnClickListener(onClick);
        btnOk.setOnClickListener(onClick);

        ivRocket.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                int width = ivRocket.getWidth();
                if (width > 0) {
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) ivRocket.getLayoutParams();
                    lp.height = width * 591 / 1006;
                    ivRocket.setLayoutParams(lp);
                }
                ivRocket.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
    }

    public void setCloseVisibility(int v) {
        if (btnClose != null) btnClose.setVisibility(v);
    }


    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            TjrUpdateDialog.this.dismiss();
            if (v.getId() == R.id.btnOk) {
                onclickOk();
            } else if (v.getId() == R.id.btnClose) {
                onclickClose();
            }
        }

    }

}
