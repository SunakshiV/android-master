package com.procoin.http.util;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.procoin.http.R;
import com.procoin.http.base.baseadapter.AnimateImageDefaultListener;

public class TjrImageLoaderUtil {
    protected DisplayImageOptions options, optionsRound,optionsForHead,optionsRoundForHead;
    protected ImageLoader imageLoader;
    protected ImageLoadingListener animateFirstListener = new AnimateImageDefaultListener();


    private static final int CORNERRADIUS=4;//圆角默认半径

    public TjrImageLoaderUtil() {
        this(R.drawable.ic_common_mic,R.drawable.ic_default_head,CORNERRADIUS);
    }
    public TjrImageLoaderUtil(int defaultRes) {
        this(defaultRes,R.drawable.ic_default_head,CORNERRADIUS);
    }
    public TjrImageLoaderUtil(int defaultRes,int cornerRadius) {
        this(defaultRes,R.drawable.ic_default_head,cornerRadius);
    }
    /**
     *
     * @param defaultRes  普通默认图像
     * @param defaultResForHead  头像默认图像
     * @param cornerRadius //圆角默认半径
     */
    public TjrImageLoaderUtil(int defaultRes,int defaultResForHead,int cornerRadius) {
        init(defaultRes,defaultResForHead,cornerRadius);
    }

    /**
     * 是否要不要圆角图
     *
     * @param defaultRes
     */
    private void init(int defaultRes,int defaultResForHead,int cornerRadius) {
        imageLoader = ImageLoader.getInstance();
        // 圆角,(CenterCrop会失效)
        optionsRound = new DisplayImageOptions.Builder().showImageForEmptyUri(defaultRes).showImageOnFail(defaultRes).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(cornerRadius)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.NONE) // default
                // Bitmap.Config.ARGB_8888
//				.showImageOnLoading(defaultRes)																																																									// 默认8888很容易爆内存溢出
                .build();
        // 不圆角
        options = new DisplayImageOptions.Builder().showImageForEmptyUri(defaultRes).showImageOnFail(defaultRes).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565) // default
                // Bitmap.Config.ARGB_8888
                // 默认8888很容易爆内存溢出
//                .showImageOnLoading(defaultRes)
                .build();


        // 圆角头像(CenterCrop会失效)
        optionsRoundForHead = new DisplayImageOptions.Builder().showImageForEmptyUri(defaultResForHead).showImageOnFail(defaultResForHead).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).displayer(new RoundedBitmapDisplayer(cornerRadius)).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.NONE) // default
                // Bitmap.Config.ARGB_8888
//				.showImageOnLoading(defaultRes)																																																											// 默认8888很容易爆内存溢出
                .build();

        // 不圆角头像
        optionsForHead = new DisplayImageOptions.Builder().showImageForEmptyUri(defaultResForHead).showImageOnFail(defaultResForHead).cacheInMemory(true).cacheOnDisk(true).considerExifParams(true).bitmapConfig(Bitmap.Config.RGB_565) // default
                // Bitmap.Config.ARGB_8888
                // 默认8888很容易爆内存溢出
//                .showImageOnLoading(defaultResForHead)
                .build();


    }
    public Drawable getDrawable(String url){
        return new BitmapDrawable(imageLoader.loadImageSync(url));
    }
//    /**
//     * 加v的ImageView 设置图片
//     */
//    public void displayAddVImage(AddVImageView ivHead, String headurl, int isVip, String userId) {
//        ivHead.setImageByLoader(imageLoader, options, animateFirstListener, headurl, isVip, userId);
//    }
//
//    /**
//     * 加v的ImageView 设置图片  主要用于有些页面默认的背景图片不一样
//     */
//    public void displayAddVImage(AddVImageView ivHead, String headurl, int isVip, String userId, DisplayImageOptions customOptions) {
//        ivHead.setImageByLoader(imageLoader, customOptions, animateFirstListener, headurl, isVip, userId);
//    }
//    /**
//     * 加v的ImageView 设置图片并且是圆角
//     */
//    public void displayAddVImageRound(AddVImageView ivHead, String headurl, int isVip, String userId) {
//        ivHead.setImageByLoader(imageLoader, optionsRound, animateFirstListener, headurl, isVip, userId);
//    }

    /**
     * 设置图片用这个并且是圆角
     */
    public void displayImageRound(String url, ImageView iv) {
        imageLoader.displayImage(url, iv, optionsRound, animateFirstListener);
    }

    /**
     * 设置图片用这个并且不是圆角
     */
    public void displayImage(String url, ImageView iv) {
        imageLoader.displayImage(url, iv, options, animateFirstListener);
    }


    /**
     * 设置图片用这个并且是圆角(头像专用)
     */
    public void displayImageRoundForHead(String url, ImageView iv) {
        imageLoader.displayImage(url, iv, optionsRoundForHead, animateFirstListener);
    }
    /**
     * 设置图片用这个并且不是圆角(头像专用)
     */
    public void displayImageForHead(String url, ImageView iv) {
        imageLoader.displayImage(url, iv, optionsForHead, animateFirstListener);
    }





    /**
     * 设置图片用这个并且不是圆角  主要用于有些页面默认的背景图片不一样
     */
    public void displayImage(String url, ImageView iv, DisplayImageOptions customOptions) {
        imageLoader.displayImage(url, iv, customOptions, animateFirstListener);
    }


    public void displayImageWithListener(String url, ImageView iv, ImageLoadingListener listener) {
        imageLoader.displayImage(url, iv, options, listener);
    }

    public void displayImageWithListener(String url, ImageView iv, DisplayImageOptions customOptions, ImageLoadingListener listener) {
        imageLoader.displayImage(url, iv, customOptions, listener);
    }

    public void destory() {
        imageLoader.destroy();
    }

}
