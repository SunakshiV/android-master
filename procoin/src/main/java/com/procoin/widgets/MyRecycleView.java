package com.procoin.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

/**
 * Created by zhengmj on 17-6-19.
 */

public class MyRecycleView extends RecyclerView {
    public MyRecycleView(Context context) {
        super(context);
        setScrollListen();
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setScrollListen();
    }

    public MyRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setScrollListen();
    }

    private void setScrollListen() {
//        addOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                Log.d("RecyclerView", "newState==" + newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                Log.d("RecyclerView", "dx==" + dx + "    dy==" + dy);
//            }
//        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        ViewParent parent = this;
        while (!((parent = parent.getParent()) instanceof ViewPager)) ;// 循环查找viewPager
        parent.requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

//    public boolean isSlideToBottom() {
//        if (computeHorizontalScrollExtent() + computeHorizontalScrollOffset()
//                >= computeHorizontalScrollRange())
//            return true;
//        return false;
//    }
//
//
//    private boolean canScrollHorizontal(int direction){
//        int offset=computeHorizontalScrollOffset();
//        int range=computeHorizontalScrollRange();
//        if(range==0)return false;
//        if(direction<0){
//            return offset>0;
//        }else{
//            return offset<range-1;
//        }
//    }
}
