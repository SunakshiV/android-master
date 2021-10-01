package com.procoin.social.util;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * 这个类是chat键盘打开和关闭
 * 
 * @author zhengmj
 * 
 */
public class ResizeLayout extends RelativeLayout {

	int count = 0;
	int count1 = 0;
	int count2 = 0;
	private static final int SOFTKEYPAD_MIN_HEIGHT = 50;
	private Handler uiHandler = new Handler();
	private showOrHideViewListen listen;

	public void setListen(showOrHideViewListen listen) {
		this.listen = listen;
	}

	public ResizeLayout(Context context) {
		super(context);
	}

	public ResizeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onSizeChanged(int w, final int h, int oldw, final int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		uiHandler.post(new Runnable() {

			@Override
			public void run() {
				if (listen != null) {
					if (oldh - h > SOFTKEYPAD_MIN_HEIGHT) {
						listen.showOrHideView(false);// 隐藏
					} else {
						listen.showOrHideView(true);
					}
				}

			}
		});
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public interface showOrHideViewListen {
		public void showOrHideView(boolean show);
	}

}
