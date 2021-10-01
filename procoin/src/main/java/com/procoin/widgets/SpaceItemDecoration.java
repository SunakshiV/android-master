package com.procoin.widgets;

import android.content.Context;
import android.graphics.Rect;
import androidx.appcompat.widget.RecyclerView;
import android.view.View;

import com.procoin.util.DensityUtil;


/**
 * 九宫格布局用到  设置item之间的间距
 */
public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    private Context context;
    private float space;

    public SpaceItemDecoration(Context context, float space) {
        this.context = context;
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.left = DensityUtil.dip2px(context, space);
        outRect.right = DensityUtil.dip2px(context, space);
        outRect.top = DensityUtil.dip2px(context, space);
        outRect.bottom = DensityUtil.dip2px(context, space);

    }
}