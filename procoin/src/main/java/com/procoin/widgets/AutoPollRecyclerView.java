package com.procoin.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * Created by zhengmj on 18-5-22.
 */
public class AutoPollRecyclerView extends RecyclerView {
    private static final long TIME_AUTO_POLL = 5;
    private boolean running;
    private boolean canRun;
    AutoPollTask autoPollTask;
    static class AutoPollTask implements Runnable{
        private final WeakReference<AutoPollRecyclerView> mReference;
        public AutoPollTask(AutoPollRecyclerView reference){
            this.mReference = new WeakReference<AutoPollRecyclerView>(reference);
        }
        @Override
        public void run() {
            AutoPollRecyclerView recyclerView = mReference.get();
            if (recyclerView != null && recyclerView.running && recyclerView.canRun){
                recyclerView.scrollBy(2,2);
                recyclerView.postDelayed(recyclerView.autoPollTask,recyclerView.TIME_AUTO_POLL);
            }
        }
    }
    public void start(){
        if (running){
            stop();
            canRun = true;
            running = true;
            postDelayed(autoPollTask,TIME_AUTO_POLL);
        }
    }
    public void stop(){
        running = false;
        removeCallbacks(autoPollTask);
    }
    public AutoPollRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (running){
                    stop();
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                if (canRun){
                    start();
                }
                break;
        }
        return super.onTouchEvent(e);
    }
}