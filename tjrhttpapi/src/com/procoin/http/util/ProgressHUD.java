package com.procoin.http.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;

import com.procoin.http.R;

public class ProgressHUD extends Dialog {
	private static final int messageRes = R.string.loading_date_message;

	public ProgressHUD(Context context) {
		super(context);
	}

	public ProgressHUD(Context context, int theme) {
		super(context, theme);
	}

	public void onWindowFocusChanged(boolean hasFocus) {
            ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
            AnimationDrawable spinner = (AnimationDrawable) imageView
                    .getBackground();
            spinner.start();
	}

//	public void setMessage(CharSequence message) {
//		if (message != null && message.length() > 0) {
//			// findViewById(R.id.progress_hud_message).setVisibility(View.VISIBLE);
//			TextView txt = (TextView) findViewById(R.id.progress_hud_message);
//			txt.setVisibility(View.VISIBLE);
//			txt.setText(message);
//			txt.invalidate();
//		}
//	}

	public static ProgressHUD show(Context context) {
		return show(context, "", true, true, null);
	}
	public static ProgressHUD show (Context context,boolean disableFocus,CharSequence message,
									boolean indeterminate, boolean cancelable,
									OnCancelListener cancelListener){
		ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
		dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_hud);
		if (disableFocus){
			dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
		}
//		TextView txt = (TextView) dialog
//				.findViewById(R.id.progress_hud_message);
//		if (TextUtils.isEmpty(message)) {//统一显示的文字
//			txt.setText(context.getString(messageRes));
//		} else {
//			txt.setText(message);
//		}
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}
	public static ProgressHUD show(Context context, CharSequence message,
			boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
		ProgressHUD dialog = new ProgressHUD(context, R.style.ProgressHUD);
		dialog.setTitle("");
		dialog.setContentView(R.layout.progress_hud);
//		TextView txt = (TextView) dialog
//				.findViewById(R.id.progress_hud_message);
//		if (TextUtils.isEmpty(message)) {//统一显示的文字
//			txt.setText(context.getString(messageRes));
//		} else {
//			txt.setText(message);
//		}
		dialog.setCancelable(cancelable);
		dialog.setOnCancelListener(cancelListener);
		dialog.setCanceledOnTouchOutside(false);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
		lp.dimAmount = 0.2f;
		dialog.getWindow().setAttributes(lp);
		// dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
		dialog.show();
		return dialog;
	}
}
