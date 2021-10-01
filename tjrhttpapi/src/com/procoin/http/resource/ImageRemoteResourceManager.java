package com.procoin.http.resource;

import java.io.File;

import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;


/**
 * 专门对所有图片作一个文件imag操作
 * 
 * @author Administrator
 * 
 */
public class ImageRemoteResourceManager {
	
	public ImageRemoteResourceManager(Context context) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
        .cacheInMemory(true) // default
        .cacheOnDisk(true) // default
        .build();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
		        .threadPoolSize(5) // default
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default
		        .tasksProcessingOrder(QueueProcessingType.LIFO)
		        .denyCacheImageMultipleSizesInMemory()
		        .diskCacheFileNameGenerator(new Md5FileNameGenerator()) // default
		        .defaultDisplayImageOptions(options) // default
		        .memoryCache(new WeakMemoryCache())
				.imageDownloader(
						new BaseImageDownloader(context,
								5 * 1000, 30 * 1000))// connectTimeout 超时时间
		        .build();
		ImageLoader.getInstance().init(config);
	}
	
	public ImageLoader imageLoader(){
		return ImageLoader.getInstance();
	}
	
	@SuppressWarnings("deprecation")
	public File getImageFile(String imageUri){
		return ImageLoader.getInstance().getDiskCache().get(imageUri);
	}
	
}
