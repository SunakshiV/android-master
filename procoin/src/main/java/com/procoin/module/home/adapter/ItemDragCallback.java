package com.procoin.module.home.adapter;

import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.StaggeredGridLayoutManager;
import androidx.appcompat.widget.helper.ItemTouchHelper;

import com.procoin.widgets.OnDragVHListener;
import com.procoin.widgets.OnItemMoveListener;

/**
 * 我的toka 排序
 */
public class ItemDragCallback extends ItemTouchHelper.Callback {


    private RecyclerView recyclerView;
    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        this.recyclerView=recyclerView;
        int dragFlags;
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        } else {
            dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        }
        // 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
        int swipeFlags = 0;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        // 不同Type之间不可移动
        if (viewHolder.getItemViewType() != target.getItemViewType()) {
            return false;
        }

        if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
            OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
            listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
            OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
            listener.onItemDissmiss(viewHolder.getAdapterPosition());
        }
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        // 不在闲置状态
        if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
            if (viewHolder instanceof OnDragVHListener) {
                OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
                itemViewHolder.onItemSelected();
            }
        }
        super.onSelectedChanged(viewHolder, actionState);
    }

    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof OnDragVHListener) {
            OnDragVHListener itemViewHolder = (OnDragVHListener) viewHolder;
            itemViewHolder.onItemFinish();
        }
        if (recyclerView.getAdapter() instanceof OnItemMoveListener) {
            OnItemMoveListener listener = ((OnItemMoveListener) recyclerView.getAdapter());
            listener.onFinish();
        }
        super.clearView(recyclerView, viewHolder);

    }


    @Override
    public boolean isLongPressDragEnabled() {
        // 不支持长按拖拽功能 手动控制
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled() {
        // 不支持滑动功能
        return false;
    }
}