package com.procoin.widgets;

import android.content.Context;
import android.util.Log;

import com.procoin.util.DensityUtil;

/**
 * Created by zhengmj on 18-10-12.
 *
 * 这个类是用来根据视频（或图片）本身的尺寸来获得更合理的尺寸用来显示
 */

public class VideoOrImageWidthHeightUtils {

    private static final int MAXWIDTH = 200;//图片最大宽度单位dip
    private static final int MAXHEIGHT = 230;//图片最大高度单位dip
    private static final int MINWIDTHHEIGHT = 200;//当图片的高等于宽时候，为了防止有些图片太小，设置一个最小值
    private static final int MINWIDTH = 100;//当图片的宽大于高时，为了防止有些图片太小，设置一个最小值
    private static final int MINHEIGHT = 100;//当图片的高等于宽时候，为了防止有些图片太小，设置一个最小值

    private int maxWidth;
    private int maxHeight;
    private int minWidthHeight;
    private int minWidth;
    private int minHeight;


    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setMinWidthHeight(int minWidthHeight) {
        this.minWidthHeight = minWidthHeight;
    }

    public void setMinWidth(int minWidth) {
        this.minWidth = minWidth;
    }

    public void setMinHeight(int minHeight) {
        this.minHeight = minHeight;
    }

    public VideoOrImageWidthHeightUtils(Context context){
        maxWidth = DensityUtil.dip2px(context, MAXWIDTH);
        maxHeight = DensityUtil.dip2px(context, MAXHEIGHT);
        minWidthHeight = DensityUtil.dip2px(context, MINWIDTHHEIGHT);
        minWidth = DensityUtil.dip2px(context, MINWIDTH);
        minHeight = DensityUtil.dip2px(context, MINHEIGHT);
    }


    public int[] getNewWidthHeight(int imgWidth, int imgHeight) {
        if(imgWidth==0||imgHeight==0)return new int[]{0,0};
        int imageViewWidth = 0;
        int imageViewHeight = 0;
        Log.d("bubbleImage", "maxWidth=" + maxWidth + " maxHeight=" + maxHeight + " imgWidth=" + imgWidth + " imgHeight=" + imgHeight);
        if (imgWidth != 0 && imgHeight != 0) {
            if (imgWidth > imgHeight) {//图片的宽大于高 ，就以图片的宽为准
                if (imgWidth < maxWidth) {//当图片的宽小于最大宽时，控件的宽等于图片的宽，空间的高等于图片的高
                    if (imgHeight < minHeight) {//防止有些图片很宽，但是很矮，这个时候要根据最小高度，并且根据理想比例（自己定）算出高度，这个时候图片的现实模式要设置为
                        imageViewHeight = minHeight;
                        imageViewWidth = Math.min(imageViewHeight * imgWidth / imgHeight, maxWidth);
                    } else {
                        imageViewWidth = imgWidth;
                        imageViewHeight = imgHeight;
                    }
                } else if (imgWidth > maxWidth) {//当图片的宽大于最大宽时，控件的宽等于规定的最大宽，空间的高按比例算出来
                    imageViewWidth = maxWidth;
                    imageViewHeight = imgHeight * imageViewWidth / imgWidth;
                    if (imageViewHeight < minHeight) {//防止有些图片很宽，但是很矮，这个时候要根据最小高度，并且根据理想比例（自己定）算出高度，这个时候图片的现实模式要设置为
                        imageViewHeight = minHeight;
                        imageViewWidth = Math.min(imageViewHeight * imgWidth / imgHeight, maxWidth);//重新计算宽度
                    }


                } else {//图片的宽等于高
                    if (imgWidth < minWidthHeight) {
                        imageViewWidth = imageViewHeight = minWidthHeight;
                    } else {
                        imageViewWidth = imageViewHeight = Math.min(imgWidth, maxWidth);
                    }
                }
            } else if (imgWidth < imgHeight) {
                if (imgHeight < maxHeight) {
                    if (imgWidth < minWidth) {
                        imageViewWidth = minWidth;
                        imageViewHeight = Math.min(imgHeight * imageViewWidth / imgWidth, maxHeight);

                    } else {
                        imageViewHeight = imgHeight;
                        imageViewWidth = imgWidth;
                    }

                } else if (imgHeight > maxHeight) {
                    imageViewHeight = maxHeight;
                    imageViewWidth = imgWidth * maxHeight / imgHeight;
                    if (imageViewWidth < minWidth) {
                        imageViewWidth = minWidth;
                        imageViewHeight = Math.min(imgHeight * imageViewWidth / imgWidth, maxHeight);
                    }

                } else {
                    if (imgWidth < minWidthHeight) {
                        imageViewWidth = imageViewHeight = minWidthHeight;
                    } else {
                        imageViewWidth = imageViewHeight = Math.min(imgWidth, maxWidth);
                    }
                }

            } else {
                if (imgWidth < minWidthHeight) {
                    imageViewWidth = imageViewHeight = minWidthHeight;
                } else {
                    imageViewWidth = imageViewHeight = Math.min(imgWidth, maxWidth);
                }
            }
        }
        return new int[]{imageViewWidth,imageViewHeight};
//        Log.d("bubbleImage", "imageViewWidth=" + imageViewWidth + " imageViewHeight=" + imageViewHeight);
//        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) jzVideoPlayerStandard.getLayoutParams();
//        lp.height = imageViewHeight;
//        lp.width = imageViewWidth;
//        jzVideoPlayerStandard.setLayoutParams(lp);
    }
}
