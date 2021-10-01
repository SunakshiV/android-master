package com.procoin.widgets;

/**
 * Created by zhengmj on 18-8-2.
 */

public interface OnItemMoveListener {
    void onItemMove(int fromPosition, int toPosition);

    void onItemDissmiss(int position);

    void onFinish();
}
