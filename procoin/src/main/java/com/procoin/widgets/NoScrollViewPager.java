package com.procoin.widgets;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class NoScrollViewPager extends ViewPager {
//    private boolean noScroll = false;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub  
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

//    public void setNoScroll(boolean noScroll) {
//        this.noScroll = noScroll;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
//        if (arg0.getAction() == MotionEvent.ACTION)
//        float pX = 0;
//        float pY = 0;
//        if (arg0.getAction() == MotionEvent.ACTION_DOWN){
//            pX = arg0.getX();
//            pY = arg0.getY();
//        }
//        if (arg0.getAction() == MotionEvent.ACTION_MOVE){
//            float nX = arg0.getX();
//            float nY = arg0.getY();
//            Log.d("154","pX == "+pX+" nX == "+nX);
//            if (nX<=pX||nX>=pX){
//                return true;
//            }
//        }
        return false;
    }

}