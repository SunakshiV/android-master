package com.procoin.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.social.util.CommonUtil;
import com.procoin.util.VeDate;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * 
 * 主要是很多页面都需要同样的image展示
 * 
 *
 * 
 */

public class ViewPagerPhotoViewUI extends PhotoViewPager {

	public final static String IMAGEURLS = "imageUrls";
	public final static String IMGSRC = "imgSrc";
	private PhotoViewPager mViewPager;
	private List<String> imgUrls = new ArrayList<String>();
	private String imgSrc;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private Bundle mBundle;
	private View headbar;
	private TranslateAnimation mShowAction;
	private TranslateAnimation mHiddenAction;
	private View view;
	private AppCompatActivity context;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				mViewPager.setAdapter(new SamplePagerAdapter());
				for (int i = 0; i < imgUrls.size(); i++) {
					if (imgUrls.get(i) != null) {
						if (imgSrc.equals(imageLoader.getDiskCache()
								.get(imgUrls.get(i)).getPath())) {
							mViewPager.setCurrentItem(i);
						}
					}

				}
				break;

			default:
				break;
			}

		}

	};
	
	public ViewPagerPhotoViewUI(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ViewPagerPhotoViewUI(Context context) {
		super(context);
	}

	// public ViewPagerPhotoViewUI(Activity context, Bundle mBundle) {
	// this.context = context;
	// this.mBundle = mBundle;
	// if (this.mBundle != null) {
	// if (this.mBundle.containsKey(IMAGEURLS)) {
	// imgUrls.addAll(mBundle.getStringArrayList(IMAGEURLS));
	// if (imgUrls.size() == 0) {
	// }
	// }
	// if (mBundle.containsKey(IMGSRC)) {
	// imgSrc = mBundle.getString(IMGSRC);
	// }
	// }
	// }

	public String getImgSrc() {
		return imgSrc;
	}

	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}

	public boolean saveToSdcard() {
		// TODO remove 东西
		File file = null;
		try {
			String uri = imgUrls.get(mViewPager.getCurrentItem());
			Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri);
			if (bitmap == null) {
				CommonUtil.showToast(context, "没有获取到图片", Gravity.BOTTOM);
				return false;
			}

			String fileName = VeDate.getyyyyMMddHHmmss(VeDate.getNow())
					+ ".png";
			file = ((MainApplication) context.getApplicationContext())
					.getmDCIMRemoteResourceManager().getFile(fileName);
			((MainApplication) context.getApplicationContext())
					.getmDCIMRemoteResourceManager().writeFile(file, bitmap,
							false);
		} catch (Exception e) {
			CommonUtil.showToast(context, "没有获取到图片", Gravity.BOTTOM);
			return false;
		}
		CommonUtil.showToast(context, "保存图片到" + file.getParent(),
				Gravity.BOTTOM);
		return true;
	}

	public int getCount() {
		return imgUrls.size();
	}

	public void setheadBar(View headbar) {
		this.headbar = headbar;
	}

	public View showView() {
		if (view == null) {
			view = CommonUtil
					.inflateView(R.layout.viewpager_photoview, context);
		}
		mViewPager = (PhotoViewPager) view.findViewById(R.id.view_pager);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder().cacheInMemory(false)
				.cacheOnDisk(true).considerExifParams(false)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		mShowAction.setDuration(500);
		mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,
				0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f);
		mHiddenAction.setDuration(500);
		handler.sendEmptyMessageDelayed(0, 200);
		return view;
	}

	public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
		mViewPager.setOnPageChangeListener(listener);
		listener.onPageSelected(mViewPager.getCurrentItem());
	}

	private class SamplePagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imgUrls.size();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			View view = CommonUtil.inflateView(
					R.layout.viewpager_photoview_item, context);

			PhotoView photoView = (PhotoView) view.findViewById(R.id.iv_photo);

			photoView
					.setOnDoubleTapListener(new TJRDoubleTapListener(photoView));

			final ProgressBar progressBar = (ProgressBar) view
					.findViewById(R.id.progress);
			imageLoader.displayImage(imgUrls.get(position), photoView, options,
					new SimpleImageLoadingListener() {
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							progressBar.setProgress(0);
							progressBar.setVisibility(VISIBLE);
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							progressBar.setVisibility(GONE);
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							progressBar.setVisibility(GONE);
						}
					}, new ImageLoadingProgressListener() {
						@Override
						public void onProgressUpdate(String imageUri,
								View view, int current, int total) {
							progressBar.setProgress(Math.round(100.0f * current
									/ total));
						}
					});
			container.addView(view, ViewPager.LayoutParams.FILL_PARENT,
					ViewPager.LayoutParams.FILL_PARENT);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

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
			if (headbar != null) {
				if (headbar.getVisibility() == VISIBLE) {
					headbar.startAnimation(mHiddenAction);
					headbar.setVisibility(GONE);
				} else {
					headbar.startAnimation(mShowAction);
					headbar.setVisibility(VISIBLE);
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
