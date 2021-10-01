package com.procoin.widgets;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

/**
 * Created by zhengmj on 19-2-27.
 */

public class TjrPopupView extends PopupWindow {
    private TjrPopLayout layout;
    private View trigger;
    private Context context;
    public TjrPopupView(Context context,View view){
        super(context);
        this.context = context;
        setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setFocusable(true);
        setOutsideTouchable(true);
        layout = new TjrPopLayout(context);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.addView(view,layoutParams);
        setContentView(layout);
    }
    public void setTrigger(View view){
        layout.setTrigger(view);
        trigger = view;
    }
//    public void show(){
//        getContentView().measure(0,0);
//        if (trigger!=null){
//
//            int[] locations = new int[2];
//            trigger.getLocationOnScreen(locations);
//            //arrow终点在popup里的位置
//            int popWidth = getContentView().getMeasuredWidth();
//            int arrow = popWidth/2;
//            int x = locations[0];//triggle的屏幕上的x
//            int point = (trigger.getRight() - trigger.getLeft())/2;
//            int distance = arrow - point;//pop中点到trigger中点的距离
//            if (distance>x){
//                Log.d("120","1");
//                //预计移动到中心所超出的部分
//                int deltaDistance = distance - x;
//                layout.setAdjustArrow(distance);
//                showAsDropDown(trigger,-(deltaDistance),-getContentView().getMeasuredHeight()*2);
//            }else {
//                Log.d("120","2");
//                WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//                int width = wm.getDefaultDisplay().getWidth();
//                if (width - arrow < point){//如果pop中点到屏幕右边的距离大于trigger中点到屏幕右边的距离
//                    int delta = point - (width - popWidth);//则获取pop中点与trigger中点的距离
//                    layout.setAdjustArrow(-delta);//向右为负
//
//                }
//                showAsDropDown(trigger,-(distance),-getContentView().getMeasuredHeight()*2);
//            }
//        }
//    }
    public void show(){
        getContentView().measure(0,0);
        if (trigger!=null){
            int popWidth = getContentView().getMeasuredWidth();//获取弹出框的目的宽度
            int arrow = popWidth/2;//弹出框箭头的默认位置（弹出框的中点）
            int triggerMiddle = (trigger.getRight() - trigger.getLeft())/2;//trigger的中点
            int ideallyXOffset = arrow - triggerMiddle;//pop的理想左偏移距离 = pop的中点距离 - trigger的中点距离
            if (triggerMiddle + trigger.getLeft() < ideallyXOffset){//如果triggle中点到屏幕左边的距离小于pop理想左移距离
                int deltaDistance = ideallyXOffset - trigger.getLeft();//arrow需要移动的距离 = 理想距离 - (t中点 + tLeft) + t中点
                layout.setAdjustArrow(deltaDistance);
            }
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            int screenWidth = wm.getDefaultDisplay().getWidth();
            if (screenWidth - arrow < triggerMiddle + trigger.getLeft()){
                //pop的中点与屏幕左边的距离比trigger中点到屏幕的距离还要小，arrow需要向右移动
                int deltaDistance = (triggerMiddle + trigger.getLeft()) - (screenWidth - arrow);
                layout.setAdjustArrow(-deltaDistance);
            }
            showAsDropDown(trigger,-ideallyXOffset,-getContentView().getMeasuredHeight()*2);
        }
    }
}
