package com.procoin.widgets;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by zhengmj on 18-12-7.
 */

public class HomeTabLayout extends FrameLayout {

    private HomeTabCallBack callBack;

    public void setHomeTabCallBack(HomeTabCallBack callBack){
        this.callBack = callBack;
    }

    public interface HomeTabCallBack{
        void onTabTouch(MotionEvent motionEvent);
    }

    public HomeTabLayout(@NonNull Context context) {
        super(context);
    }

    public HomeTabLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("200","HomeTabLayout -> Parent :\nx == "+event.getX()+"\ny== "+event.getY()+"\nRawX == "+event.getRawX()+"\nRawY == "+event.getRawY());

        if (callBack!=null)callBack.onTabTouch(event);
        return true;
    }
}
