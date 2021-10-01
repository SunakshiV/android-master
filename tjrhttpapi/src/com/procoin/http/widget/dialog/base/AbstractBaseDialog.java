package com.procoin.http.widget.dialog.base;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;
import android.view.WindowManager;

public abstract class AbstractBaseDialog extends Dialog {

	public AbstractBaseDialog(Context context) {
		super(context);
		initBaseTheme();
	}

	/**
	 * 默认的样式，如果需要请自己设置
	 */
	private void initBaseTheme() {
//		this.getWindow().getAttributes().windowAnimations = R.style.Animation_CustomDialog;
		// 没有标题
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		// theme没有全屏幕
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
		// 背景颜色
		this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// 监听dismiss
		this.setCanceledOnTouchOutside(false);
	}

	/**
	 * 确定按钮
	 */
	public abstract void onclickOk();

	/**
	 * 关闭按钮
	 */
	public abstract void onclickClose();

	/**
	 * 设置更新进度条
	 * 
	 * @param progress
	 */
	public abstract void setDownProgress(int progress);
}
