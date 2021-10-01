package com.procoin.common.base.util;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by zhengmj on 19-4-17.
 */

public class ResizeBase {
    private Context context;
    private float designerDensity = 2;//干部的设计图是按 1 dp = 2px 的比例来标的
    private float density;//手机本来的本来像素密度
    private float scale;


    public ResizeBase(Context context){
        this.context = context;
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (manager!=null){
            manager.getDefaultDisplay().getMetrics(metrics);
            density = metrics.density;
            scale = density/designerDensity;
        }
    }

    public ViewGroup generateAdaptationView(@LayoutRes int layoutRes){
        ViewGroup layout = (ViewGroup) LayoutInflater.from(context).inflate(layoutRes,null,false);
        LinearLayout.LayoutParams outSideParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(outSideParams);
        setAdaptationSize(layout);
        return layout;
    }
    private void setAdaptationSize(ViewGroup layout){
        int childCount = layout.getChildCount();
        for (int i = 0;i<childCount;i++){
            View child =layout.getChildAt(i);
            if (child instanceof ViewGroup){
                setAdaptationSize((ViewGroup) child);
            }else {
                adjustLayoutParams(child,child.getLayoutParams());
            }
        }
    }
    private void adjustLayoutParams(View child,ViewGroup.LayoutParams layoutParams){
        layoutParams.width = resizeValue(layoutParams.width);
        layoutParams.height = resizeValue(layoutParams.height);
        child.setPadding(resizeValue(child.getPaddingLeft()),resizeValue(child.getPaddingTop()),resizeValue(child.getPaddingRight()),resizeValue(child.getPaddingBottom()));
        if (layoutParams instanceof ViewGroup.MarginLayoutParams){
            ((ViewGroup.MarginLayoutParams)layoutParams).topMargin = resizeValue(((ViewGroup.MarginLayoutParams)layoutParams).topMargin);
            ((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin = resizeValue(((ViewGroup.MarginLayoutParams)layoutParams).bottomMargin);
            ((ViewGroup.MarginLayoutParams)layoutParams).leftMargin = resizeValue(((ViewGroup.MarginLayoutParams)layoutParams).leftMargin);
            ((ViewGroup.MarginLayoutParams)layoutParams).rightMargin = resizeValue(((ViewGroup.MarginLayoutParams)layoutParams).rightMargin);
            child.setLayoutParams(layoutParams);
            return;
        }
        child.setLayoutParams(layoutParams);
    }
    private int resizeValue(int value){
        int resizValue = 0;
        resizValue = (int)((float)value * scale);
        return resizValue;
    }
}
