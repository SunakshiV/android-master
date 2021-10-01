package com.procoin.widgets;

import android.graphics.Canvas;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.helper.ItemTouchHelper;
import android.util.Log;

/**
 * Created by zhengmj on 18-12-26.
 * 在LoadMoreRecyclerView的基础上对事件拦截作出优化（主要用于新消息提示的滑动冲突）
 */

public class SocialTouchHepler extends ItemTouchHelper.Callback {
    private AdapterTouchCallback touchCallback;
    private boolean hasAd = false;
    private boolean hasBanner = false;
    private DismissCallback callback;
    private int previousDirection;//用户触摸时滑动的方向，8右滑，4左滑
    private float previousX;
    public void setOnDismissCallback(DismissCallback callback){
        this.callback = callback;
    }
    public interface DismissCallback{
        void onDismiss();
    }
    public void setHasAd(boolean b){
        hasAd = b;
    }
    public void setHasBanner(boolean b){
        hasBanner = b;
    }
    public SocialTouchHepler(AdapterTouchCallback touchCallback){
        this.touchCallback = touchCallback;
    }
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        if (hasBanner){
            int position = viewHolder.getAdapterPosition();
            if (hasAd){
                if (position == 1)return makeMovementFlags(0,swipeFlags);
            }else {
                if (position == 0)return makeMovementFlags(0,swipeFlags);
            }
        }else {
            return makeMovementFlags(0,0);
        }
        return makeMovementFlags(0,0);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //松手的时候才会调用这个
        if (touchCallback!=null){
            Log.d("200","itemTouchHelper direction == "+direction);
            touchCallback.onItemSwipe(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        Log.d("200","itemTouchHelper dX: "+dX+" dy: "+dY+" actionState: "+actionState+" isActive == "+isCurrentlyActive);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        float alpha = 100/Math.abs(0 - dX);
        viewHolder.itemView.setAlpha(alpha);
        if (isCurrentlyActive){
            //用户的手正在点击view
            if (dX>previousX){
                previousDirection = 8;
            }else if (dX<previousX){
                previousDirection = 4;
            }
        }else {
            //用户的手松开了view
            if (previousDirection == 8 && previousX<dX){
                //用户右滑，view继续向右运动
                return;
            }
            if (previousDirection == 4 && previousX>dX){
                //用户左滑，view继续向左运动
                return;
            }
            if (callback!=null&&alpha<=0.6f)callback.onDismiss();
        }
        previousX = dX;
    }
}
