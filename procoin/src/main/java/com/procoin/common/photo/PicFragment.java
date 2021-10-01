package com.procoin.common.photo;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.util.InflaterUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.R;

import uk.co.senab.photoview.PhotoView;

public class PicFragment extends UserBaseFragment {
	private View view;
	private ImageEntity imageEntity;
//	private ImageLoader imageLoader;
//	private DisplayImageOptions options;
	
	private TjrImageLoaderUtil mTjrImageLoaderUtil ;
	
	private int pageType;

	public static PicFragment newInstance(ImageEntity imageEntity, ImageLoader imageLoader, DisplayImageOptions options) {
		PicFragment fragment = new PicFragment();
		fragment.imageEntity = imageEntity;
//		fragment.imageLoader = imageLoader;
//		fragment.options = options;
		return fragment;
	}
	public static PicFragment newInstance(ImageEntity imageEntity) {
		PicFragment fragment = new PicFragment();
		fragment.imageEntity = imageEntity;
		return fragment;
	}
	public static PicFragment newInstance(ImageEntity imageEntity, TjrImageLoaderUtil mTjrImageLoaderUtil, int pageType) {
		PicFragment fragment = new PicFragment();
		fragment.imageEntity = imageEntity;
		fragment.mTjrImageLoaderUtil=mTjrImageLoaderUtil;
		fragment.pageType=pageType;
		return fragment;
	}
@Override
public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
//	mTjrImageLoaderUtil = new TjrImageLoaderUtil();
//	imageLoader = ImageLoader.getInstance();
//	options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).considerExifParams(false).displayer(new FadeInBitmapDisplayer(300)).build();
}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// if(view!=null){//这里重用的话没有了双击事件
		// return view;
		// }
		view = InflaterUtils.inflateView(getActivity(), R.layout.viewpager_photoview_item);
		PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);
		photoView.setOnDoubleTapListener(new TJRDoubleTapListener(photoView));

		final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.progress);
		// imageLoader.displayImage(imageEntity.getMinPic(), photoView);
//		mTjrImageLoaderUtil.displayImage(imageEntity.getMinPic(), photoView);
		Log.d("PicFragment","imageEntity.maxPic=="+imageEntity.maxPic);
		mTjrImageLoaderUtil.displayImageWithListener(imageEntity.maxPic, photoView, new SimpleImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
//				progressBar.setProgress(0);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				progressBar.setVisibility(View.GONE);
			}
		});
//		imageLoader.displayImage(imageEntity.getMaxPic(), photoView, options, new SimpleImageLoadingListener() {
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//				progressBar.setProgress(0);
//				progressBar.setVisibility(View.VISIBLE);
//			}
//
//			@Override
//			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//				progressBar.setVisibility(View.GONE);
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//				progressBar.setVisibility(View.GONE);
//			}
//		}, new ImageLoadingProgressListener() {
//			@Override
//			public void onProgressUpdate(String imageUri, View view, int current, int total) {
//				progressBar.setProgress(Math.round(100.0f * current / total));
//			}
//		});
		return view;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
//		if (view != null) {
//			ViewGroup group = (ViewGroup) view.getParent();
//			group.removeView(view);
//		}
	}
	class TJRDoubleTapListener implements GestureDetector.OnDoubleTapListener {

		private PhotoView photoView;

		public TJRDoubleTapListener(PhotoView photoView) {
			setPhotoViewAttacher(photoView);
		}

		public void setPhotoViewAttacher(PhotoView photoView) {
			this.photoView = photoView;
		}

		@Override
		public boolean onSingleTapConfirmed(MotionEvent e) {
			if(pageType!=0){
				try {
					ActionBar bar=((TJRBaseToolBarActivity)getActivity()).getSupportActionBar();
					if(bar.isShowing()){
						bar.hide();
					}else{
						bar.show();
					}
				} catch (Exception e1) {
				}
			}else{
				if(getActivity()!=null){
					getActivity().finish();
				}
			}
			return false;
		}

		@Override
		public boolean onDoubleTap(MotionEvent ev) {
			try {
				float scale = photoView.getScale();
				float x = ev.getX();
				float y = ev.getY();

				if (scale < photoView.getMediumScale()) {
					photoView.setScale(photoView.getMaximumScale(), x, y, true);
				} else {
					photoView.setScale(photoView.getMinimumScale(), x, y, true);
				}
			} catch (ArrayIndexOutOfBoundsException ex) {
			} catch (IllegalArgumentException ex) {

			}
			return true;
		}

		@Override
		public boolean onDoubleTapEvent(MotionEvent e) {
			return false;
		}
	}
}
