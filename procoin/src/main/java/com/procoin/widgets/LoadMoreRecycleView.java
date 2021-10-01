package com.procoin.widgets;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;

/**
 * Created by zhengmj on 17-6-19.
 */

public class LoadMoreRecycleView extends RecyclerView {
    private RecycleViewLoadMoreCallBack recycleViewLoadMoreCallBack;

//    private BaseLoadMoreImageLoaderRecycleAdapter baseLoadMoreImageLoaderRecycleAdapter;

    public LoadMoreRecycleView(Context context) {
        this(context, null);
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setRecycleViewLoadMoreCallBack(RecycleViewLoadMoreCallBack recycleViewLoadMoreCallBack) {
        this.recycleViewLoadMoreCallBack = recycleViewLoadMoreCallBack;
    }

//    public void setLoadMoreAdapter(BaseLoadMoreImageLoaderRecycleAdapter baseLoadMoreImageLoaderRecycleAdapter) {
//        this.baseLoadMoreImageLoaderRecycleAdapter = baseLoadMoreImageLoaderRecycleAdapter;
//        setAdapter(baseLoadMoreImageLoaderRecycleAdapter);
//    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
    }

    @Override
    public void onScrolled(int dx, int dy) {
        super.onScrolled(dx, dy);

        int endCompletelyPosition = 0;
        if (getLayoutManager() instanceof LinearLayoutManager) {
            endCompletelyPosition = ((LinearLayoutManager) getLayoutManager()).findLastCompletelyVisibleItemPosition();
        }
        if (getLayoutManager() instanceof GridLayoutManager) {
            endCompletelyPosition = ((GridLayoutManager) getLayoutManager()).findLastCompletelyVisibleItemPosition();
        }
        if (getAdapter() instanceof BaseLoadMoreImageLoaderRecycleAdapter) {
            BaseLoadMoreImageLoaderRecycleAdapter baseLoadMoreImageLoaderRecycleAdapter = (BaseLoadMoreImageLoaderRecycleAdapter) getAdapter();
            if (!baseLoadMoreImageLoaderRecycleAdapter.isComplete()&&!baseLoadMoreImageLoaderRecycleAdapter.isLoading()) {
                Log.d("onScrolled","endCompletelyPosition=="+endCompletelyPosition+"   "+(baseLoadMoreImageLoaderRecycleAdapter.getRealItemCount()-1));
                if (baseLoadMoreImageLoaderRecycleAdapter.getRealItemCount()>0&&endCompletelyPosition > baseLoadMoreImageLoaderRecycleAdapter.getRealItemCount()-1 ) {//如果判>=可能会调用多次loadMore
                    if (recycleViewLoadMoreCallBack != null) {
                        Log.d("onScrolled","loadMore。。。。。。。");
                        baseLoadMoreImageLoaderRecycleAdapter.setOnloading();
                        recycleViewLoadMoreCallBack.loadMore();

                    }
                }
            }else {
                Log.d("onScrolled","!baseLoadMoreImageLoaderRecycleAdapter.isComplete()&&!baseLoadMoreImageLoaderRecycleAdapter.isLoading() is false");
            }
        }

    }


    public interface RecycleViewLoadMoreCallBack {
        void loadMore();
    }

}
