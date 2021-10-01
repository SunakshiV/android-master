package com.procoin.widgets;

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.widget.RecyclerView;
import android.view.View;

import com.procoin.util.DensityUtil;

/**
 * Created by zhengmj on 19-9-10.
 */

public class SimpleSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private Context context;

    private int left;
    private int right;
    private int top;
    private int bottom;

    public SimpleSpaceItemDecoration(Context context, int left, int right, int top, int bottom) {

        this.context = context;

        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = DensityUtil.dip2px(context, left);
        outRect.right = DensityUtil.dip2px(context, right);
        outRect.top = DensityUtil.dip2px(context, top);
        outRect.bottom = DensityUtil.dip2px(context, bottom);

    }
}
