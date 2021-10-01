package com.procoin.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by zhengmj on 19-2-28.
 */

public class InterceptChildLayout extends LinearLayout {

    public InterceptChildLayout(Context context) {
        super(context);
    }

    public InterceptChildLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN){
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }
}
