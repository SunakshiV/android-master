package com.procoin.module.myhome.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.procoin.module.myhome.entity.ImageSelectGroup;
import com.procoin.util.InflaterUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.procoin.http.base.baseadapter.BaseImageLoaderAdapter;
import com.procoin.R;


public class MyhomeSelectAdapter extends BaseImageLoaderAdapter<ImageSelectGroup> {

    private Context context;
    protected DisplayImageOptions imageOptions;

    private boolean showCarema;

//    private final int MAX_ITEMS_NUM = 12;//

    public MyhomeSelectAdapter(Context context,boolean showCarema) {
        super();
        this.context = context;
        this.showCarema=showCarema;
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


    @Override
    public int getCount() {
        return super.getCount() + (showCarema?1:0);
    }

    @Override
    public ImageSelectGroup getItem(int position) {
        return super.getItem(position-(showCarema?1:0));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder;
        if (convertView == null) {
//            switch (getItemViewType(position)) {
//                case 1:
//            convertView = InflaterUtils.inflateView(context, R.layout.myhome_selectimage_item_camera);
//                    holder = new ViewCameraHolder(convertView);
//                    convertView.setTag(holder);
//                    break;
//
//                default:
            convertView = InflaterUtils.inflateView(context, R.layout.myhome_selectimage_item);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
//                    break;
//            }

        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        holder.setValue(position);

        return convertView;
    }

    class ViewHolder extends BaseViewHolder {

        ImageView ivView;

        private ViewHolder(View view) {
            ivView = (ImageView) view.findViewById(R.id.ivView);
        }

        @Override
        public void setValue(int position) {
            if (showCarema&&position == 0) {
                ivView.setImageResource(R.drawable.ic_myhome_selectcamera);
//                displayImage("drawable://" + R.drawable.ic_myhome_selectcamera, ivView);
            } else {
                ImageSelectGroup item = getItem(position);
                displayImage("file://" + item.getPathStr(), ivView, imageOptions);
            }


        }
    }

//    class ViewCameraHolder extends BaseViewHolder {
//
//        private ViewCameraHolder(View view) {
    // ivView = (ImageView) view.findViewById(R.id.ivView);
//        }

//        @Override
//        public void setValue(int position) {
    // ImageSelectGroup item = getItem(position);
    // if (item != null) {
    // ImageLoader.getInstance().displayImage(item.getPathStr(), ivView,
    // imageOptions);
    // }

//        }
//    }

//    @Override
//    public int getItemViewType(int position) {
//        int type = ((ImageSelectGroup) getItem(position)).getType();
//        return type <= (MAX_ITEMS_NUM - 1) ? type : MAX_ITEMS_NUM - 1;
//    }

//    @Override
//    public int getViewTypeCount() {
//        return MAX_ITEMS_NUM;
//    }


    private abstract class BaseViewHolder {

        public abstract void setValue(int position);

    }


}
