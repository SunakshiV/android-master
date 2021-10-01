package com.procoin.http.widget.view;

import java.io.IOException;
import java.util.Set;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.procoin.http.R;
import com.procoin.http.common.CommonConst;
import com.procoin.http.resource.BaseRemoteResourceManager;

public class AddVImageView extends RelativeLayout {
	private long userId;
	private Context context;
	private Matrix m;
	private Bitmap oldBm;
	private Bitmap resizedBitmap;
	
	private ImageView iv_head ;
	private ImageView iv_v;

	public AddVImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context,attrs);
	}

	public AddVImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context,attrs);
	}

	public AddVImageView(Context context) {
		super(context);
		init(context,null);
	}

	private void init(Context context,AttributeSet attrs) {
		this.context = context;
	}

	/**
	 * 
	 * @param mRrm
	 * @param headurl
	 * @param mLanchFatcher
	 *            ==null 为单个imageView !=null 为listView 设置ImageView
	 * @param isVip
	 *            是否是vip
	 * @param userId
	 *            不需要默认的点击事件 传null
	 */
	@Deprecated
	public void setImage(BaseRemoteResourceManager mRrm, String headurl,
			Set<Object> mLanchFatcher, int isVip, String userId) {
		setImage(mRrm, headurl, mLanchFatcher, isVip, userId, 0.0f);
	}
	
	private Bitmap getSacleBitmap(float scale, int resource) {
		if (scale == 0.0f) {
			if (oldBm == null)
				oldBm = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.ic_authen);
			return oldBm;
		} else {
			if (m != null && oldBm != null && resizedBitmap != null) {
				return resizedBitmap;
			} else {
				m = new Matrix();
				m.postScale(scale, scale);
				if (oldBm == null)
					oldBm = BitmapFactory.decodeResource(
							context.getResources(), R.drawable.ic_authen);
				resizedBitmap = Bitmap.createBitmap(oldBm, 0, 0,
						oldBm.getWidth(), oldBm.getHeight(), m, true);
			}
		}
		return resizedBitmap;
	}

	/**
	 * @param scale
	 *            缩小至原来的多少倍
	 */
	@Deprecated
	public void setImage(BaseRemoteResourceManager mRrm, String headurl,
			Set<Object> mLanchFatcher,  int isVip, String userId,
			float scale) {
		
		if(iv_head==null){
			iv_head=(ImageView) findViewById(R.id.iv_head);
			iv_head.setScaleType(ScaleType.FIT_CENTER);
		}
		if(iv_v==null){
			iv_v=(ImageView) findViewById(R.id.iv_v);
		}
		setListImageView(mRrm, headurl, iv_head, mLanchFatcher);
		if (isVip == 1) {
			iv_v.setVisibility(View.VISIBLE);
			iv_v.setImageBitmap(getSacleBitmap(scale, R.drawable.ic_authen));
		} else {
			iv_v.setVisibility(View.GONE);
		}
		if (userId != null && userId.matches("[0-9E]+$")) {
			this.userId = Long.parseLong(userId);
			if (this.userId > 0) {
				setClickable(true);
				setOnClickListener(new JumpToUserHomeClick(this.userId));
			}else {
				setOnClickListener(null);
				setClickable(false);
			}
		} else {
			setOnClickListener(null);
			setClickable(false);
		}
	}
	/**
	 *  用 ImageLoader  加载图片用这个
	 */
	public void setImageByLoader(ImageLoader loader,DisplayImageOptions optons, ImageLoadingListener animateFirstListener ,String headurl,
			int isVip, String userId) {
		setImage(loader, optons,animateFirstListener,headurl,  isVip, userId, 0.0f);
	}
	/**
	 *  用 ImageLoader  加载图片用这个 
	 */
	public void setImage(ImageLoader loader,DisplayImageOptions optons, ImageLoadingListener animateFirstListener ,String headurl,
			int isVip, String userId,float scale) {
		if(iv_head==null){
			iv_head=(ImageView) findViewById(R.id.iv_head);
			iv_head.setScaleType(ScaleType.FIT_CENTER);
		}
		if(iv_v==null){
			iv_v=(ImageView) findViewById(R.id.iv_v);
		}
		loader.displayImage(headurl, iv_head, optons, animateFirstListener);
		if (isVip == 1) {
			iv_v.setVisibility(View.VISIBLE);
			iv_v.setImageBitmap(getSacleBitmap(scale, R.drawable.ic_authen));
		} else {
			iv_v.setVisibility(View.GONE);
		}
		if (userId != null && userId.matches("[0-9E]+$")) {
			this.userId = Long.parseLong(userId);
			if (this.userId > 0) {
				setClickable(true);
				setOnClickListener(new JumpToUserHomeClick(this.userId));
			}else {
				setOnClickListener(null);
				setClickable(false);
			}
		} else {
			setOnClickListener(null);
			setClickable(false);
		}
	}

	/**
	 * k线角斗场 随机切换图片的时候用到
	 * 
	 * @param imageLevelSrcResource
	 */
	public void setImageLevelSrc(int imageLevelSrcResource) {
		if(iv_head==null){
			iv_head=(ImageView) findViewById(R.id.iv_head);
			iv_head.setScaleType(ScaleType.FIT_CENTER);
		}
		iv_head.setImageResource(imageLevelSrcResource);
	}

	public void setImageLevel(int level) {
		if(iv_head==null){
			iv_head=(ImageView) findViewById(R.id.iv_head);
			iv_head.setScaleType(ScaleType.FIT_CENTER);
		}
		iv_head.setImageLevel(level);
	}

	public void setImageDrawable(Drawable d) {
		if(iv_head==null){
			iv_head=(ImageView) findViewById(R.id.iv_head);
			iv_head.setScaleType(ScaleType.FIT_CENTER);
		}
		iv_head.setImageDrawable(d);
	}

	public void setImageResource(int res) {
		setImageDrawable(getResources().getDrawable(res));
	}

	public void setImageBitmap(Bitmap bm) {
		if (bm == null){
			setImageDrawable(null);
		}else{
			setImageDrawable(new BitmapDrawable(getResources(), bm));
		}
	}

	@SuppressWarnings("unused")
	private int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				res.getDisplayMetrics());
	}

	/**
	 * 根据包名判断程序是否安装,并返回其他
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	private ApplicationInfo checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName))
			return null;
		try {
			ApplicationInfo info = context.getPackageManager()
					.getApplicationInfo(packageName,
							PackageManager.GET_UNINSTALLED_PACKAGES);
			return info;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * 
	 * @param mRrm
	 * @param headurl
	 * @param ivPhoto
	 */
	@Deprecated
	private void setListImageView(BaseRemoteResourceManager mRrm,
			String headurl, ImageView ivPhoto, Set<Object> mLaunchedPhotoFetches) {
		ivPhoto.setImageResource(R.drawable.ic_default_head);
		if (headurl == null || headurl.length() <= 0)
			return;
		if (!headurl.startsWith("http"))
			return;
		Uri uriPhoto = Uri.parse(headurl);
		try {
			Bitmap bitmap = mRrm.get(uriPhoto);
			if (bitmap != null)
				ivPhoto.setImageBitmap(bitmap);
		} catch (IOException e) {
			if (!mLaunchedPhotoFetches.contains(headurl)) {
				mLaunchedPhotoFetches.add(headurl);
				mRrm.request(uriPhoto);
			}
		}
	}

	class JumpToUserHomeClick implements View.OnClickListener {
		private long userId;
//		private int isVip;

		private JumpToUserHomeClick(long userId) {
			this.userId = userId;
//			this.isVip = isVip;
		}

		@Override
		public void onClick(View v) {
//			CommonUtil.showToast(context, userId+"", Gravity.CENTER);
			// 如果使用了userId，优先使用id
			try {
				ApplicationInfo info = checkApkExist(context, "com.cropyme");
				if (info != null) {
					Bundle bundle = new Bundle();
					// bundle.putInt(CommonConst.ISAddV, isVip);
					bundle.putLong(CommonConst.SHARED_PREFERENCE_USERID, userId);
					Intent mIntent = new Intent();
					mIntent.putExtras(bundle);
					ComponentName comp = new ComponentName("com.coingo",
							"com.coingo.friend.FriendHomeActivity");
					mIntent.setComponent(comp);
					mIntent.setAction("android.intent.action.VIEW");
					context.startActivity(mIntent);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}
}
