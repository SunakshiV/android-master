package com.procoin.social.util;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Rect;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class ImageViewUtil {

	/**
	 * 把一个View的对象转换成bitmap
	 */
	public static Bitmap createViewBitmap(View v) {
		try {
			v.setDrawingCacheEnabled(true);
			v.clearFocus();
			v.setPressed(false);
			// 能画缓存就返回false
			boolean willNotCache = v.willNotCacheDrawing();
			v.setWillNotCacheDrawing(false);
			int color = v.getDrawingCacheBackgroundColor();
			v.setDrawingCacheBackgroundColor(0);
			if (color != 0) {
				v.destroyDrawingCache();
			}
			v.buildDrawingCache();
			Bitmap cacheBitmap = v.getDrawingCache();
			if (cacheBitmap == null) return null;
			Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

			// Restore the view
			v.destroyDrawingCache();
			v.setWillNotCacheDrawing(willNotCache);
			v.setDrawingCacheBackgroundColor(color);
			v.setDrawingCacheEnabled(false);

			if (cacheBitmap != null && !cacheBitmap.isRecycled()) {
				cacheBitmap.recycle();
			}
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			CommonUtil.LogLa(2, "ex=" + e.toString());
		} catch (OutOfMemoryError e) {
			CommonUtil.LogLa(2, "ex=" + e.toString());
		}
		return null;
	}

	/***
	 * 增加了actionbar后需要去除标题栏，还有就是
	 * 
	 * @param activity
	 * @param v
	 * @return
	 */
	public static Bitmap createDecorViewBitmap(AppCompatActivity activity, View v) {
		if (v == null || activity == null) return null;
		try {
			v.setDrawingCacheEnabled(true);
			v.clearFocus();
			v.setPressed(false);
			// 能画缓存就返回false
			boolean willNotCache = v.willNotCacheDrawing();
			v.setWillNotCacheDrawing(false);
			int color = v.getDrawingCacheBackgroundColor();
			v.setDrawingCacheBackgroundColor(0);
			if (color != 0) {
				v.destroyDrawingCache();
			}
			v.buildDrawingCache();
			Bitmap cacheBitmap = v.getDrawingCache();
			if (cacheBitmap == null) return null;
			Bitmap bitmap = null;
			if (v.equals(activity.getWindow().getDecorView())) {
				Rect frame = new Rect();
				// decorView是window中的最顶层view，可以从window中获取到decorView，然后decorView有个getWindowVisibleDisplayFrame方法可以获取到程序显示的区域，包括标题栏，但不包括状态栏。
				activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
				// 第二步 得到状态栏的高度
				int statusHeight = frame.top;
				// 第四步 创建新的Bitmap对象 并截取除了状态栏的其他区域
				bitmap = Bitmap.createBitmap(cacheBitmap, 0, statusHeight, cacheBitmap.getWidth(), cacheBitmap.getHeight() - statusHeight);
			} else {
				bitmap = Bitmap.createBitmap(cacheBitmap);
			}

			// Restore the view
			v.destroyDrawingCache();
			v.setWillNotCacheDrawing(willNotCache);
			v.setDrawingCacheBackgroundColor(color);
			v.setDrawingCacheEnabled(false);

			if (cacheBitmap != null && !cacheBitmap.isRecycled()) {
				cacheBitmap.recycle();
			}
			return bitmap;
		} catch (Exception e) {
			// TODO: handle exception
			CommonUtil.LogLa(2, "ex=" + e.toString());
		} catch (OutOfMemoryError e) {
			CommonUtil.LogLa(2, "ex=" + e.toString());
		}
		return null;
	}
	
	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}
		
		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
}
