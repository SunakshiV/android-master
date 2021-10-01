package com.taojin.swipback.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.taojin.swipback.R;
import com.taojin.swipback.SwipeBackLayout;
import com.taojin.swipback.SwipeBackLayout.OnPageScrollEndCallBack;
import com.taojin.swipback.Utils;

/**
 * @author Yrom
 */
public class SwipeBackActivityHelper {
    private Activity mActivity;

    private SwipeBackLayout mSwipeBackLayout;
    private InputMethodManager im;


    public SwipeBackActivityHelper(Activity activity) {
        mActivity = activity;
    }

    @SuppressWarnings("deprecation")
    public void onActivityCreate() {
        mActivity.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//android.R.color.transparent
        mActivity.getWindow().getDecorView().setBackgroundDrawable(null);
        mSwipeBackLayout = (SwipeBackLayout) LayoutInflater.from(mActivity).inflate(R.layout.swipeback_layout, null);
        im = (InputMethodManager)mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
        mSwipeBackLayout.addSwipeListener(new SwipeBackLayout.SwipeListener() {
            @Override
            public void onScrollStateChange(int state, float scrollPercent) {
                Log.d("addSwipeListener","onScrollStateChange......scrollPercent=="+scrollPercent+" state=="+state);
            }

            @Override
            public void onEdgeTouch(int edgeFlag) {
                Utils.convertActivityToTranslucent(mActivity);
                try{
                    if(im!=null&&mActivity!=null)im.hideSoftInputFromWindow(mActivity.getCurrentFocus().getWindowToken(), 0);
                }catch (Exception e){
                }
                Log.d("addSwipeListener", "onEdgeTouch......");
            }

            @Override
            public void onScrollOverThreshold() {
                Log.d("addSwipeListener","onScrollOverThreshold......");

            }
        });
    }
    
    public void setOnPageScrollEndCallBack(OnPageScrollEndCallBack onPageScrollEndCallBack){
    	mSwipeBackLayout.setOnPageScrollEndCallBack(onPageScrollEndCallBack);
    }

    public void onPostCreate() {
        mSwipeBackLayout.attachToActivity(mActivity);
    }

    public View findViewById(int id) {
        if (mSwipeBackLayout != null) {
            return mSwipeBackLayout.findViewById(id);
        }
        return null;
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return mSwipeBackLayout;
    }
}
