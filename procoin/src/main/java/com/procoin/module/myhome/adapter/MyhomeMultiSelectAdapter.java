package com.procoin.module.myhome.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.myhome.entity.ImageSelectGroup;
import com.procoin.widgets.layout.SquareLayout;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.procoin.R;


public class MyhomeMultiSelectAdapter extends BaseImageLoaderRecycleAdapter<ImageSelectGroup> {

    private Context context;
    protected DisplayImageOptions imageOptions;

    private boolean showCarema;
    private int maxPicCount;
    private CallBack callBack;

    private ColorMatrixColorFilter darker;

    private ColorMatrixColorFilter re;

//    private final int MAX_ITEMS_NUM = 12;//

    public MyhomeMultiSelectAdapter(Context context, boolean showCarema, int maxPicCount) {
        super();
        this.context = context;
        this.showCarema = showCarema;
        this.maxPicCount = maxPicCount;
        imageOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.drawable.ic_common_mic)
                .showImageOnFail(R.drawable.ic_common_mic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(false)
                .resetViewBeforeLoading(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        int brightness = -80;
        ColorMatrix matrix1 = new ColorMatrix();//用来把颜色变暗
        matrix1.set(new float[]{1, 0, 0, 0, brightness, 0, 1, 0, 0, brightness, 0, 0, 1, 0, brightness, 0, 0, 0, 1, 0});
        darker = new ColorMatrixColorFilter(matrix1);

        int brightness2 = 0;
        ColorMatrix matrix = new ColorMatrix();//用来把颜色恢复
        matrix.set(new float[]{1, 0, 0, 0, brightness2, 0, 1, 0, 0, brightness2, 0, 0, 1, 0, brightness2, 0, 0, 0, 1, 0});
        re = new ColorMatrixColorFilter(matrix);
    }

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.myhome_multiselectimage_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setValue(position);

    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (showCarema ? 1 : 0);
    }


    @Override
    public ImageSelectGroup getItem(int position) {
        return super.getItem(position - (showCarema ? 1 : 0));
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        BaseViewHolder holder;
//        if (convertView == null) {
//            convertView = InflaterUtils.inflateView(context, R.layout.myhome_multiselectimage_item);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
////                    break;
////            }
//
//        } else {
//            holder = (BaseViewHolder) convertView.getTag();
//        }
//        holder.setValue(position);
//
//        return convertView;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SquareLayout flView;
        ImageView ivView;
        ImageView ivSelect;

        FrameLayout flSelect;

        public ViewHolder(View view) {
            super(view);
            ivView = (ImageView) view.findViewById(R.id.ivView);
            ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
            flView = (SquareLayout) view.findViewById(R.id.flView);
            flSelect = (FrameLayout) view.findViewById(R.id.flSelect);
        }

        public void setValue(final int position) {
            final ImageSelectGroup item = getItem(position);
            View.OnClickListener onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    switch (v.getId()) {
                        case R.id.flView:
                            if (callBack != null) {
                                if (showCarema && position == 0) {
                                    callBack.onCapture();

                                } else {
                                    callBack.onItemClick(position, item);

                                }
                            }
                            break;
                        case R.id.flSelect:
                            if (callBack != null) {
                                callBack.onSelectClick(position, item);
                            }
                            break;
                    }
                }
            };
            if (showCarema && position == 0) {
                ivView.setImageResource(R.drawable.ic_myhome_selectcamera);
                flSelect.setVisibility(View.GONE);
//                displayImage("drawable://" + R.drawable.ic_myhome_selectcamera, ivView);
            } else {
                displayImage("file://" + item.getPathStr(), ivView, imageOptions);
//                if (maxPicCount == 1) {
//                    ivSelect.setVisibility(View.GONE);
//                } else {
                flSelect.setVisibility(View.VISIBLE);
                if (item.isCheck()) {
                    ivSelect.setImageResource(R.drawable.ic_redz_photo_selected);
                    ivView.setColorFilter(darker);
                } else {
                    ivSelect.setImageResource(R.drawable.ic_redz_photo_unselect);
                    ivView.setColorFilter(re);
                }
//                }
                flSelect.setOnClickListener(onClickListener);
            }
            flView.setOnClickListener(onClickListener);
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

    public interface CallBack {
        void onSelectClick(int pos, ImageSelectGroup item);

        void onItemClick(int pos, ImageSelectGroup item);

        void onCapture();
    }
}
