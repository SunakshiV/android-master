package com.procoin.common.base.adapter;


import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.widgets.LoadMoreRecycleView;
import com.procoin.http.base.TaojinluType;
import com.procoin.R;
import com.procoin.module.pulltorefresh.AutoLoadMoreLayout;

/**
 * Created by zhengmj on 17-10-30.
 */

public abstract class BaseLoadMoreImageLoaderRecycleAdapter<T extends TaojinluType> extends BaseImageLoaderRecycleAdapter {

    protected LoadMoreRecycleView.RecycleViewLoadMoreCallBack recycleViewLoadMoreCallBack;
    protected AutoLoadMoreLayout.FootMode mode = AutoLoadMoreLayout.FootMode.INITIALISE;
    protected boolean dividerEnable = false;
    protected boolean showHeader;
    public final static int FOOTVIEWTYPE = 10;

    private Context context;
    protected LayoutInflater layoutInflater;

    private int footTextColor = Color.BLACK;

    private String footText;//如果不为空,就相当于写死了,不会在根据foot的状态,适用于有些adapter有2种场景,一种有foot一种不需要foot,如果不需要foot就设置成别的text


    public void setFootText(String footText) {
        this.footText = footText;
    }

    public void setFootTextColor(int footTextColor) {
        this.footTextColor = footTextColor;
    }

    public BaseLoadMoreImageLoaderRecycleAdapter(Context context) {
        this(context, R.drawable.ic_common_mic);
    }

    public BaseLoadMoreImageLoaderRecycleAdapter(Context context, int defaultRes) {
        super(defaultRes);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }
    public void setDividerEnable(boolean dividerEnable) {
        this.dividerEnable = dividerEnable;
    }

    /**
     * 这里是用于加载失败后,可点击重新加载,所以传入了recycleViewLoadMoreCallBack
     *
     * @param recycleViewLoadMoreCallBack
     */
    public void setRecycleViewLoadMoreCallBack(LoadMoreRecycleView.RecycleViewLoadMoreCallBack recycleViewLoadMoreCallBack) {
        this.recycleViewLoadMoreCallBack = recycleViewLoadMoreCallBack;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTVIEWTYPE) {
            View footView = layoutInflater.inflate(R.layout.load_foot, parent, false);
            TextView tvText = (TextView) footView.findViewById(R.id.tvText);
            tvText.setTextColor(footTextColor);
            return new FootViewHolder(footView);
        } else {
            return onCreateViewHolderWithoutFoot(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        if (mViewHolder instanceof DynmicAdapter.TextViewBaseHolder) {
//            DynmicAdapter.TextViewBaseHolder baseViewHolder = (DynmicAdapter.TextViewBaseHolder) mViewHolder;
//            baseViewHolder.setData(getItem(position));
//        } else {
//            FootViewHolder footViewHolder = (FootViewHolder) mViewHolder;
//            footViewHolder.setState();
//        }
        if (holder instanceof BaseLoadMoreImageLoaderRecycleAdapter.FootViewHolder) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            footViewHolder.setState(context, mode, dividerEnable, recycleViewLoadMoreCallBack,footText);
        }
        else {
            onBindViewViewHolderWithoutFoot(holder, position);
        }
    }
    @Override
    public int getItemCount() {
        return super.getItemCount()+ 1;
    }

    public int getRealItemCount() {
        return super.getItemCount();
    }

    public boolean isComplete() {
        return mode == AutoLoadMoreLayout.FootMode.COMPLETE;
    }

    public boolean isLoading() {
        return mode == AutoLoadMoreLayout.FootMode.LOADING;
    }

    @Override
    public int getItemViewType(int position) {
        Log.d("getItemViewType", "position==" + position + "   getItemCount()==" + getItemCount());
                if (position == getItemCount() - 1) {
                    return FOOTVIEWTYPE;
                } else {
                    return getItemType(position);
                }
    }

    public T getLastItem() {
        return (T) super.getItem(getItemCount() - 2);
    }

    @Override
    public T getItem(int position) {
        return (T) super.getItem(position);
    }

    public void setOnloading() {
        mode = AutoLoadMoreLayout.FootMode.LOADING;
        notifyDataSetChanged();
    }

    public void onLoadComplete(Boolean isSuccess, Boolean isComplete) {
        if (!isSuccess) {
            mode = AutoLoadMoreLayout.FootMode.FAIL;
        } else {
            if (getRealItemCount() == 0) {
                mode = AutoLoadMoreLayout.FootMode.NODATA;
            } else {
                mode = isComplete ? AutoLoadMoreLayout.FootMode.COMPLETE : AutoLoadMoreLayout.FootMode.NORMAL;
            }

        }
//
        notifyDataSetChanged();
    }

    public class FootViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout foot_load;
        private ProgressBar pb;
        private TextView tvText;
        private View viewFootDivider;

        public FootViewHolder(View convertView) {
            super(convertView);
            foot_load = (LinearLayout) convertView.findViewById(R.id.foot_load);
            pb = (ProgressBar) convertView.findViewById(R.id.pb);
            tvText = (TextView) convertView.findViewById(R.id.tvText);
            viewFootDivider = convertView.findViewById(R.id.ViewFootDivider);
        }


        public void setState(Context context, AutoLoadMoreLayout.FootMode mode, boolean dividerEnable, final LoadMoreRecycleView.RecycleViewLoadMoreCallBack recycleViewLoadMoreCallBack,String footText) {
            viewFootDivider.setVisibility(dividerEnable ? View.VISIBLE : View.GONE);
            if (null != tvText) {
                if(!TextUtils.isEmpty(footText)){
                    pb.setVisibility(View.GONE);
                    tvText.setText(footText);
                    foot_load.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (recycleViewLoadMoreCallBack != null) {
                                recycleViewLoadMoreCallBack.loadMore();
                            }
                        }
                    });
                    return;//footText不为空没必要在执行下去了
                }else{
                    tvText.setText(mode.getResText() == 0 ? "" : context
                            .getString(mode.getResText()));
                }
            }
            Log.d("FootViewHolder","mode=="+mode.getResText());
            switch (mode) {
                case INITIALISE:
                    foot_load.setVisibility(View.GONE);
                    break;
                case LOADING:
                    foot_load.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.VISIBLE);
                    Log.d("FootViewHolder","//////////////");
                    break;
                case NORMAL:
                case FAIL:
                case COMPLETE:
                case NODATA:
                    foot_load.setVisibility(View.VISIBLE);
                    pb.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }

            if (mode == AutoLoadMoreLayout.FootMode.FAIL) {
                foot_load.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (recycleViewLoadMoreCallBack != null) {
                            setOnloading();
                            recycleViewLoadMoreCallBack.loadMore();
                        }
                    }
                });
            } else {
                foot_load.setOnClickListener(null);
            }
        }
    }


    protected abstract int getItemType(int position);


    protected abstract RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType);

    protected abstract void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position);
}
