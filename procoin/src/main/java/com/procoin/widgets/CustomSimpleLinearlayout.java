package com.procoin.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class CustomSimpleLinearlayout extends LinearLayout {
    private View view;
    private int maxChildCount = 5;

    private ListAdapter adapter;//是时候需要判断数据有没有改变没改变就不用重新设置了

    public ListAdapter geAdapter() {
        return adapter;
    }

    public void setMaxChildCount(int maxChildCount) {
        this.maxChildCount = maxChildCount;
    }

    public CustomSimpleLinearlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomSimpleLinearlayout(Context context) {
        super(context);
    }

    public void setAdapter(ListAdapter adapter) {
        if (adapter == null) return;
        this.adapter = adapter;
        removeAllViews();
        Log.d("CustomSimple", "adapterCount==" + adapter.getCount());
        for (int i = 0, m = adapter.getCount(); i < Math.min(m, maxChildCount); i++) {
            view = adapter.getView(i, null, null);
            this.addView(view);
        }
    }

    //加入所有item项
    public void setAdapterFullSize(ListAdapter adapter) {
        if (adapter == null) return;
        this.adapter = adapter;
        removeAllViews();
        for (int i = 0, m = adapter.getCount(); i < m; i++) {
            view = adapter.getView(i, null, null);
            this.addView(view);
        }
    }

}
