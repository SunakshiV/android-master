package com.procoin.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.util.DensityUtil;

import java.util.ArrayList;

/**
 * Created by zhengmj on 19-3-2.
 */

public class TjrPopMenu extends PopupWindow{
    private Context context;
    private TjrPopLayout tjrPopLayout;
    private MenuLayout menuLayout;
    private View trigger;
    private OnMenuItemClickListener onMenuItemClickListner;
    private int x;
    private int y;

    public interface OnMenuItemClickListener{
        void onMenuItemClick(int position);
    }
    public void setOnMenuItemClickLIstner(OnMenuItemClickListener onMenuItemClickLIstner){
        this.onMenuItemClickListner = onMenuItemClickLIstner;
    }
    public TjrPopMenu(Context context){
        super(context);
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        setOutsideTouchable(true);
        this.context = context;
        tjrPopLayout = new TjrPopLayout(context);
        menuLayout = new MenuLayout(context);
    }
    public void setMenu(ArrayList<String> list){
        menuLayout.setTitleList(list);
        menuLayout.setPadding(DensityUtil.dip2px(context,3),DensityUtil.dip2px(context,3),DensityUtil.dip2px(context,3),DensityUtil.dip2px(context,3));
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (menuLayout.getParent() == null)tjrPopLayout.addView(menuLayout,layoutParams);
        setContentView(tjrPopLayout);
    }
    public void setTrigger(View view){
        tjrPopLayout.setTrigger(view);
        trigger = view;
    }
    public void setTouchCoordinate(int x, int y){
        this.x = x;
        this.y = y;
        getContentView().measure(0,0);
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int[] locations = new int[2];
        trigger.getLocationOnScreen(locations);
        int triggerX = locations[0];
        Log.d("120"," a == "+(triggerX + x + getContentView().getMeasuredWidth()/2)+" b == "+ displayMetrics.widthPixels +" x == "+x+triggerX+" w/2 == "+getContentView().getMeasuredWidth()/2);
        if (triggerX + x + getContentView().getMeasuredWidth()/2 > displayMetrics.widthPixels){
            int delta = triggerX + x + getContentView().getMeasuredWidth()/2 - displayMetrics.widthPixels;
            tjrPopLayout.setAdjustArrow(delta);
        }else {
            if (triggerX + x - getContentView().getMeasuredWidth()/2 < 0){
                int delta = triggerX + x - getContentView().getMeasuredWidth()/2;
                tjrPopLayout.setAdjustArrow(delta);
            }
            tjrPopLayout.setAdjustArrow(0);
        }

    }
    public void show(){
        getContentView().measure(0,0);
        showAsDropDown(trigger,x-getContentView().getMeasuredWidth()/2,-trigger.getHeight()+y-getContentView().getMeasuredHeight());

    }
    private class MenuLayout extends LinearLayout{
        private int dividerWidth = 2;
        private ArrayList<String> titleList;
        private void setTitleList(ArrayList<String> titleList){
            this.titleList = titleList;
            if (titleList!=null&&titleList.size()>0){
                int childCount = getChildCount();
                if (childCount!=0){
                    removeAllViews();
                }
                LinearLayout.LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                for (int i = 0;i<titleList.size();i++){
                    final int index = i;
                    String s = titleList.get(i);
                    TextView tv = new TextView(context);
                    tv.setTextSize(12);
                    tv.setText(s);
                    tv.setTextColor(Color.WHITE);
//                    tv.setGravity(Gravity.CENTER);
                    tv.setPadding(24,
                            6,
                           24,
                            6);
                    tv.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (onMenuItemClickListner!=null){
                                onMenuItemClickListner.onMenuItemClick(index);
                                dismiss();
                            }
                        }
                    });
                    addView(tv,layoutParams);
                }
            }
        }
        public MenuLayout(Context context) {
            super(context);
            setOrientation(HORIZONTAL);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int childCount = getChildCount();
            for (int i = 0;i<childCount;i++){
                View child = getChildAt(i);
                measureChild(child,widthMeasureSpec,heightMeasureSpec);
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
//            int childCount = getChildCount();
//            Log.d("120","layout count == "+childCount);
//            for (int i = 0;i<childCount;i++){
//                View child = getChildAt(i);
//                child.layout(i*child.getMeasuredWidth()+i*dividerWidth,t,l+i*dividerWidth+i+child.getMeasuredWidth(),b);
//            }
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            int childCount = getChildCount();
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            for (int i = 0;i<childCount;i++){
                View child = getChildAt(i);
                int childWidth = child.getMeasuredWidth();
                int childHeight = child.getMeasuredHeight();
                if (i!=childCount-1){
                    canvas.drawRect(child.getRight(),getPaddingTop()+child.getPaddingTop(),child.getRight()+dividerWidth,childHeight,paint);
                }
            }
            super.dispatchDraw(canvas);
        }

        public MenuLayout(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            setOrientation(HORIZONTAL);
        }

    }
}
