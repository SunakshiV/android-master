package com.procoin.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public class GvItemFrameLayout extends FrameLayout {

	public GvItemFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public GvItemFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public GvItemFrameLayout(Context context) {
		super(context);
	}
	   @SuppressWarnings("unused")
	    @Override
	    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	        setMeasuredDimension(getDefaultSize(0, widthMeasureSpec), getDefaultSize(0, heightMeasureSpec));
	 
	        int childWidthSize = getMeasuredWidth();
	        int childHeightSize = getMeasuredHeight();
	        //高度和宽度一样
	        heightMeasureSpec = widthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidthSize, MeasureSpec.EXACTLY);
	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    }
	
}
