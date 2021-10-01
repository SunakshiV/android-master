package com.procoin.widgets;

/**
 * Created by zhengmj on 18-8-2.
 */

public interface OnDragVHListener {
    /**
     * Item被选中时触发
     */
    void onItemSelected();


    /**
     * Item在拖拽结束/滑动结束后触发
     */
    void onItemFinish();
}
