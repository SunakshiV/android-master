package com.procoin.widgets.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;


/**
 * 宽确定了，让高==宽
 */
public class SquareLayout extends FrameLayout {
	public SquareLayout(Context context) {
		super(context);
	}

	public SquareLayout(Context context, AttributeSet attr) {
		super(context, attr);
	}

	public SquareLayout(Context context, AttributeSet attr, int defStyle) {
		super(context, attr, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		setMeasuredDimension(resolveSize(0, widthMeasureSpec), resolveSize(0, heightMeasureSpec));
		int childWidthSize = getMeasuredWidth();
		// height is equal to width
		heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}
}