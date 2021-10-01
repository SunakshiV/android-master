package com.procoin.util;

import android.content.Context;
import android.os.Handler;

import com.procoin.data.sharedpreferences.StockSharedPreferences;
import com.procoin.MainApplication;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhengmj on 17-2-24.
 * 这个类是因为太多的类需要时使用定时器,但是很多地方都是启动停止定时器
 */

public class TjrMinuteTaskPool {
    private ScheduledFuture<?> beeperHandle;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);//定时器
    private Handler handler=new Handler();

//    synchronized public final void submit(final Runnable task) {
//        if (!executorService.isTerminated() && !executorService.isShutdown() && task != null) {
//            executorService.submit(task);
//        }
//    }

    /**
     * 开启定时器: 开市才执行,即 MainApplication.isRun==true
     */
    synchronized public void startTime(final Runnable command, long initalDelay, long period, TimeUnit unit) {
        if (scheduler.isShutdown()) return;
        if (MainApplication.isRun) {
            if (null != beeperHandle && !beeperHandle.isCancelled()) beeperHandle.cancel(true);
            beeperHandle = scheduler.scheduleAtFixedRate(command, initalDelay, period, unit);
        }
    }
    synchronized public void startTime(final Runnable command, long initalDelay, Context context) {
        if (null == context) return;
        this.startTime(command, initalDelay, StockSharedPreferences.getSelectSpeed(context), TimeUnit.SECONDS);
    }
    synchronized public void startTime(Context context, final Runnable command) {
        this.startTime(command, 0, context);
    }

    /**
     * 开启定时器: 无需考虑是否开市
     */
    synchronized public void startTimeWithoutRun(final Runnable command, long initalDelay, long period, TimeUnit unit) {
        if (scheduler.isShutdown()) return;
        if (null != beeperHandle && !beeperHandle.isCancelled()) beeperHandle.cancel(true);
        beeperHandle = scheduler.scheduleAtFixedRate(command, initalDelay, period, unit);
    }
    synchronized public void startTimeWithoutRun(final Runnable command, long initalDelay, Context context) {
        if (null == context) return;
        this.startTimeWithoutRun(command, initalDelay,
                StockSharedPreferences.getSelectSpeed(context), TimeUnit.SECONDS);
    }
    synchronized public void startTimeWithoutRun(final Runnable command, Context context) {
        this.startTimeWithoutRun(command, 0, context);
    }

    /**
     * 关闭定时器
     */
    synchronized public void closeTime() {
        if (beeperHandle != null) {
            beeperHandle.cancel(true);
//            beeperHandle = null;
        }
    }

    /**
     * 页面如果销毁 这个方法需要释放
     */
    synchronized public void release() {
        scheduler.shutdown();
    }
}
