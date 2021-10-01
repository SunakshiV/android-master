package com.procoin.http.base.baseadapter;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.procoin.http.R;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.util.TjrImageLoaderUtil;

/**
 * 
 * @author 有用到ImageLoader的adapter
 *
 * @param <T>
 */
public abstract class BaseImageLoaderAdapter<T extends TaojinluType> extends AMBaseAdapter<T>{
	protected TjrImageLoaderUtil mTjrImageLoaderUtil;
	private static final int CORNERRADIUS=8;//圆角默认半径

	public BaseImageLoaderAdapter(){
		this(R.drawable.ic_common_mic,R.drawable.ic_default_head,CORNERRADIUS);
	}
	
	public BaseImageLoaderAdapter(int defaultRes){
		this(defaultRes,R.drawable.ic_default_head,CORNERRADIUS);
	}

	public BaseImageLoaderAdapter(int defaultRes,int defaultResForHead,int cornerRadius){
		mTjrImageLoaderUtil = new TjrImageLoaderUtil(defaultRes,defaultResForHead,cornerRadius);
	}
//	/**
//	 * 加v的ImageView 设置图片用这个并且是圆角
//	 */
//	public void displayAddVImage(AddVImageView ivHead, String headurl,int isVip,String userId){
//		mTjrImageLoaderUtil.displayAddVImage(ivHead, headurl, isVip, userId);
//	}
//
//	/**
//	 * 加v的ImageView 设置图片用这个并且是圆角
//	 */
//	public void displayAddVImage(AddVImageView ivHead, String headurl,int isVip,String userId,DisplayImageOptions customOptions){
//		mTjrImageLoaderUtil.displayAddVImage(ivHead, headurl, isVip, userId, customOptions);
//	}


	/**
	 * 设置图片用这个并且是圆角
	 */
	public void displayImageRound(String url,ImageView iv){
		mTjrImageLoaderUtil.displayImageRound(url, iv);
	}
	
	/**
	 * 设置图片用这个并且不是圆角
	 */
	public void displayImage(String url,ImageView iv){
		mTjrImageLoaderUtil.displayImage(url, iv);
	}


	/**
	 * 设置图片用这个并且是圆角(头像专用)
	 */
	public void displayImageRoundForHead(String url,ImageView iv){
		mTjrImageLoaderUtil.displayImageRoundForHead(url, iv);
	}

	/**
	 * 设置图片用这个并且不是圆角(头像专用)
	 */
	public void displayImageForHead(String url,ImageView iv){
		mTjrImageLoaderUtil.displayImageForHead(url, iv);
	}
	/**
	 * 设置图片用这个并且不是圆角
	 */
	public void displayImage(String url,ImageView iv,DisplayImageOptions customOptions){
		mTjrImageLoaderUtil.displayImage(url, iv, customOptions);
	}



	public void displayImageWithListener(String url, ImageView iv, ImageLoadingListener listener) {
		mTjrImageLoaderUtil.displayImageWithListener(url, iv,  listener);
	}

	public void displayImageWithListener(String url, ImageView iv, DisplayImageOptions customOptions,ImageLoadingListener listener) {
		mTjrImageLoaderUtil.displayImageWithListener(url, iv, customOptions, listener);
	}



}
