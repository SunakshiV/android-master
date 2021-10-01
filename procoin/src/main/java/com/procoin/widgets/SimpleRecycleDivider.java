package com.procoin.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.procoin.util.DensityUtil;
import com.procoin.R;

/**
 * Created by zhengmj on 17-7-24.
 */

public class SimpleRecycleDivider extends RecyclerView.ItemDecoration {
    private int dividerHeight = 0;
    private int left;//首页列表用到,分割线左边的距离
    private int right;
    private Paint dividerPaint;
    private static final float DEFAULT_DIVIDERHEIGHT = 0.5f;//单位px
    private boolean showLastDivider;//是否显示最后一项分割线,有加载更多就不需要

    public SimpleRecycleDivider(Context context) {
        this(context, 0);
    }

    public SimpleRecycleDivider(Context context, boolean showLastDivider) {
        this(context, 0);
        this.showLastDivider = showLastDivider;
    }

    public SimpleRecycleDivider(Context context, int left,boolean showLastDivider) {
        this(context, left);
        this.showLastDivider = showLastDivider;
    }

    public SimpleRecycleDivider(Context context, int left) {
        this(context, left, 0);
    }

//    public SimpleRecycleDivider(Context context, int left, int dividerColor) {
//        this(context, left, 0, dividerColor);
//    }

    public SimpleRecycleDivider(Context context, int left, int right) {
        this(context, left, right,false);
    }

    public SimpleRecycleDivider(Context context, int left, int right,boolean showLastDivider) {
        this(context, left, right, ContextCompat.getColor(context, R.color.dividerColor), DEFAULT_DIVIDERHEIGHT);
        this.showLastDivider=showLastDivider;
    }


    public SimpleRecycleDivider(Context context, int left, int right, int dividerColor) {
        this(context, left, right, dividerColor, DEFAULT_DIVIDERHEIGHT);
    }

    public void setShowLastDivider(boolean showLastDivider) {
        this.showLastDivider = showLastDivider;
    }

    public SimpleRecycleDivider(Context context, int left, int right, int dividerColor, float dividerHeight) {
        dividerPaint = new Paint();
        dividerPaint.setColor(dividerColor);
        this.left = DensityUtil.dip2px(context, left);
        this.right = DensityUtil.dip2px(context, right);
        this.dividerHeight = DensityUtil.dip2px(context, dividerHeight);
        Log.d("SimpleRecycleDivider", "this.dividerHeight==" + this.dividerHeight + "right==" + right);
//        dividerHeight = context.getResources().getDimensionPixelSize(R.dimen.divider_height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        outRect.bottom = dividerHeight;
        outRect.set(0, 0, 0, dividerHeight);
//        Log.d("SimpleRecycleDivider", "this.dividerHeight==" + dividerHeight);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int childCount = parent.getChildCount();
//        int left = parent.getPaddingLeft();
        int r = parent.getWidth() - right;//右边在这里计算
        for (int i = 0; i < (showLastDivider ? childCount : childCount - 1); i++) {
            View view = parent.getChildAt(i);
            float top = view.getBottom();
            float bottom = view.getBottom() + dividerHeight;
            c.drawRect(left, top, r, bottom, dividerPaint);
        }
    }


}
