package com.procoin.common.base;

import android.os.Bundle;
import androidx.annotation.LayoutRes;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.procoin.common.base.util.ResizeBase;
import com.procoin.R;

/**
 * Created by maxim on 19-4-17.
 * 这是 自动屏幕适配 Activity的基类
 */

public abstract class AutoResizeActivity extends TJRBaseToolBarSwipeBackActivity {
    private LinearLayout ll_container;
    private ResizeBase resizeBase;

    @Override
    protected int setLayoutId() {
        return R.layout.resize_container;
    }
    protected void resizeContent(@LayoutRes int layoutId){
        ViewGroup viewGroup = resizeBase.generateAdaptationView(layoutId);
        ll_container.addView(viewGroup);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ll_container = (LinearLayout) findViewById(R.id.ll_container);
        resizeBase = new ResizeBase(this);
    }
}
