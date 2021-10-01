package com.procoin.module.myhome.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.myhome.entity.ImageSelectGroup;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.procoin.R;

/**
 * Created by zhengmj on 18-11-21.
 */

public class AlbumSelectAdapter2 extends BaseImageLoaderRecycleAdapter<ImageSelectGroup> {
    private Context context;
    private DisplayImageOptions imageOptions;

    public AlbumSelectAdapter2(Context context) {
        this.context = context;
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_common_mic)
                .showImageOnFail(R.drawable.ic_common_mic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }


    //分开2次循环是为了用notifyItemChanged防止闪烁
    public int setSelected(String path) {

        for (int i = 0, m = getItemCount(); i < m; i++) {
            ImageSelectGroup imageSelectGroup = getItem(i);
            if (imageSelectGroup.isSelected()) {
                imageSelectGroup.setSelected(false);
                notifyItemChanged(i);
                break;
            }
        }
        int index = -1;
        for (int i = 0, m = getItemCount(); i < m; i++) {
            ImageSelectGroup imageSelectGroup = getItem(i);
            if (imageSelectGroup.getPathStr().equals(path)) {
                imageSelectGroup.setSelected(true);
                index = i;
                notifyItemChanged(i);
                break;
            }
        }
        return index;
    }

    public int getIndex(String path) {
        int index = -1;
        for (int i = 0, m = getItemCount(); i < m; i++) {
            ImageSelectGroup imageSelectGroup = getItem(i);
            if (imageSelectGroup.getPathStr().equals(path)) {
                index = i;
                break;
            }
        }
        return index;
    }


    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_album_select, parent, false));
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        final ImageSelectGroup entity = getItem(position);
        Object tag = viewHolder.iv_content.getTag();
        if (tag == null || !tag.equals(entity.getPathStr())) {
            viewHolder.iv_content.setTag(entity.getPathStr());
            displayImage("file://" + entity.getPathStr(), viewHolder.iv_content, imageOptions);
        }

        viewHolder.bg.setSelected(entity.isSelected());
//        viewHolder.iv_content.setImageDrawable(new BitmapDrawable(bitmap));
        viewHolder.flItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemSelectListener != null) {
                    if (!entity.isSelected()) {//已经选中了点击无效
                        onItemSelectListener.onItemSelect(position, entity);

                    }
                }
            }
        });
    }

    private class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView iv_content;
        private View bg;
        private FrameLayout flItem;

        public ViewHolder(View itemView) {
            super(itemView);
            bg = itemView.findViewById(R.id.bg);
            iv_content = itemView.findViewById(R.id.iv_content);
            flItem = itemView.findViewById(R.id.flItem);
        }
    }


    public OnItemSelectListener onItemSelectListener;

    public interface OnItemSelectListener {
        void onItemSelect(int pos, ImageSelectGroup entity);
    }
}
