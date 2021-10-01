package com.procoin.widgets.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * * 高确定了，让宽==高
 */
public class SquareLayout2 extends FrameLayout {
    public SquareLayout2(Context context) {
        super(context);
    }

    public SquareLayout2(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public SquareLayout2(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(resolveSize(0, widthMeasureSpec), resolveSize(0, heightMeasureSpec));
        int childHeightSize = getMeasuredHeight();
        // height is equal to width
        widthMeasureSpec = heightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }
}