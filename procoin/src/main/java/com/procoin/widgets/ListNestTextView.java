package com.procoin.widgets;

import android.content.Context;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.Layout;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by pqm on 18-9-25.
 *
 * ListView嵌套ListView时会发生测量错误，需要计算子ListView所有item的高度。如果TextView换行的话无法正确计算高度。这个类是用来解决这个问题的。
 */

public class ListNestTextView extends androidx.appcompat.widget.AppCompatTextView {
    private Context context;

    public ListNestTextView(Context context) {
        super(context);
        this.context = context;
    }

    public ListNestTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        Layout layout = getLayout();
        if (layout != null) {
            int height = (int) Math.ceil(getMaxLineHeight(this.getText().toString(), mode))
                    + getCompoundPaddingTop() + getCompoundPaddingBottom();
            int width = getMeasuredWidth();
            setMeasuredDimension(width, height);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private float getMaxLineHeight(String str, int mode) {
        float height = 0.0f;
        float width = getMeasuredWidth();
        float widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        //这里具体this.getPaint()要注意使用，要看你的TextView在什么位置，
        // 这个是拿TextView父控件的Padding的，为了更准确的算出换行
        float pLeft = ((LinearLayout) getParent()).getPaddingLeft();
        float pRight = ((LinearLayout) getParent()).getPaddingRight();
        //检测字符串中是否包含换行符,获得换行的次数，在之后计算高度时加上
        int br = 0;
        if (str.contains("\n"))
            br = str.split("\n").length - 1;
        /**
         *  wrap_content/未指定宽度(MeasureSpec.UNSPECIFIED)，则用屏幕宽度计算
         *  否则就使用View自身宽度计算,并且无需计算Parent的Padding
         */
        int line;
        if (mode == MeasureSpec.UNSPECIFIED)
            line = (int)
                    Math.ceil((this.getPaint().measureText(str) /
                            (widthPixels - getPaddingLeft() - pLeft - pRight - getPaddingRight())));
        else {
            line = (int)
                    Math.ceil((this.getPaint().measureText(str) /
                            (width - getPaddingLeft() - getPaddingRight())));
        }
        float linespace = this.getLineSpacingExtra();
        height = (this.getPaint().getFontMetrics().descent -
                this.getPaint().getFontMetrics().ascent) * (line + br) + (linespace*(line+br));
        return height;
    }
}


