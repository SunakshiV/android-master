package com.procoin.widgets;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

import com.procoin.R;
import com.procoin.util.InflaterUtils;

/**
 * Created by zhengmj on 18-12-28.
 */

public class EmptyViewUtils {


    public static View showEmptyView( FrameLayout frameLayout,Context context) {
        return showEmptyView(frameLayout, InflaterUtils.inflateView(context, R.layout.include_redz_no_content));
    }

    public static View showEmptyView(FrameLayout frameLayout, View emptyView) {
        if (frameLayout == null || emptyView == null) return null;
        frameLayout.addView(emptyView);
        emptyView.setVisibility(View.VISIBLE);
        return emptyView;
    }

    public static void hideEmptyView(FrameLayout frameLayout, View emptyView) {
        if (frameLayout == null || emptyView == null) return;
        frameLayout.removeView(emptyView);
    }
}
