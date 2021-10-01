package com.procoin.module.guide;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

public class LayoutListener implements OnGlobalLayoutListener {

	private ViewTreeObserver vtos;
	private CallGlobalLayoutOver callOver;
	private View view;

	public LayoutListener(View view, CallGlobalLayoutOver callOver) {
		this.view = view;
		this.callOver = callOver;
		setOnGlobalLayoutListener();
	}

	public void setOnGlobalLayoutListener() {
		if (view == null) return;
		vtos = view.getViewTreeObserver();
		vtos.addOnGlobalLayoutListener(this);// 为其添加监听器
	}

	public void removeGlobalOnLayoutListener() {
		if (view == null) return;
		vtos = view.getViewTreeObserver();
		vtos.removeGlobalOnLayoutListener(this);
	}

	@Override
	public void onGlobalLayout() {
		if (callOver != null) callOver.globalLayoutOver();
	}

	public interface CallGlobalLayoutOver {
		void globalLayoutOver();
	}

}
