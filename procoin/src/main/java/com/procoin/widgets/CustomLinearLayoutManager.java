package com.procoin.widgets;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutManager;

/**
 * recycleView 用到,用来代替以前ListView的head添加ListView,那么head的lsitView 就要禁止滚动,以前我们会用到AppListView,但是有些手机在刷新的时候会闪烁,所以现在改用RecycleView
 */
public class CustomLinearLayoutManager extends LinearLayoutManager {
    private boolean isScrollVerticalEnabled = true;
    private boolean isScrollHorizontallyEnabled = true;

    public CustomLinearLayoutManager(Context context) {
        super(context);
    }


    public void setScrollVerticalEnabled(boolean scrollVerticalEnabled) {
        isScrollVerticalEnabled = scrollVerticalEnabled;
    }

    public void setScrollHorizontallyEnabled(boolean scrollHorizontallyEnabled) {
        isScrollHorizontallyEnabled = scrollHorizontallyEnabled;
    }

    @Override
    public boolean canScrollVertically() {
        //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
        return isScrollVerticalEnabled && super.canScrollVertically();
    }

    @Override
    public boolean canScrollHorizontally() {
        return isScrollHorizontallyEnabled && super.canScrollHorizontally();
    }
}