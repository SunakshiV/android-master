package com.procoin.widgets.quotitian;

import com.procoin.MainApplication;

/**
 * 这个类完全就是用来一个小时如果需要setRun为true 防止没有刷新
 * Created by zhengmj on 17-5-29.
 */

public class StarRunTimeManager {
    private final long maxTime = 600000;
    private long time; //保存时间

    public void onResume() {
        if ((System.currentTimeMillis() - time) >= maxTime) MainApplication.setRun(true);
    }

    public void onPause() {
        time = System.currentTimeMillis();
    }


}
