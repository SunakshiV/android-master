package com.procoin.social.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.social.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CommonUtil {

	private static String TAG = "ddd";

	public static Class<?> getClass(String calssName) {
		try {
			return Class.forName(calssName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 页面跳转
	 * 
	 * @param
	// *            当前页面
	 * @param cls
	 *            要跳转到的页面
	 * @param back
	 *            是否是返回操作
	 * @param finish
	 *            是否finish当前页面
	 * @return true表示跳转成功，false表示跳转不成功
	 */
	public static boolean pageJump(AppCompatActivity activity, Class<?> cls, boolean back, boolean finish) {
		try {
			if (activity != null && cls != null) {
				Intent intent = new Intent();
				intent.setClass(activity, cls);
				// 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);// 进入动画
			}
			if (activity != null) {
				if (finish) activity.finish();
				// if (back)
				// activity.overridePendingTransition(android.R.anim.slide_in_left,
				// android.R.anim.slide_out_right);// 返回动画
				//第一个参数为activity进入时的动画，第二个参数为activity退出时的动画
				if (back) activity.overridePendingTransition(R.anim.out_right_to_left, R.anim.out_left_to_right);// 返回动画
				return true;
			}
		} catch (Exception e) {
			Log.e("Exception", "pageJump error!  " + e.toString());
		}
		return false;
	}

	/**
	 * 页面跳转,带参数
	 * 
	 * @param activity
	 *            当前页面
	 * @param cls
	 *            要跳转到的页面
	 * @param back
	 *            是否是返回操作
	 * @param finish
	 *            是否finish当前页面
	 * @param bundle
	 *            参数
	 * @return true表示跳转成功，false表示跳转不成功
	 */
	public static boolean pageJumpToData(AppCompatActivity activity, Class<?> cls, boolean back, boolean finish, Bundle bundle) {
		try {
			if (activity != null && cls != null) {
				Intent intent = new Intent();
				intent.setClass(activity, cls);
				// 在A窗口打开B窗口时,在Intent中直接加入标志，这样开启B时将会清除该进程空间的所有Activity
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				if (bundle != null) intent.putExtras(bundle);
				activity.startActivity(intent);
				activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);// 进入动画
			}
			if (activity != null) {
				if (finish) activity.finish();
				// if (back)
				// activity.overridePendingTransition(android.R.anim.slide_in_left,
				// android.R.anim.slide_out_right);// 返回动画
				if (back) activity.overridePendingTransition(R.anim.out_right_to_left, R.anim.out_left_to_right);// 返回动画
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.e("Exception", "pageJump error!  " + e.toString());
		}
		return false;
	}

	/**
	 * 自定义位置显示Toast
	 * 
	 * @param context
	 * @param msg
	 *            Toast要显示的信息
	 * @param gravity
	 *            Toast显示位置（Gravity）
//	 * @param xOffsetØ
	 *            x偏移量
	 */
	public static void showToast(Context context, String msg, int gravity) {
		Toast toast = Toast.makeText(context, msg + "", Toast.LENGTH_LONG);
		toast.setGravity(gravity, 0, 0);// 设置toast位子；
		toast.show();
	}

	/**
	 * 自定义位置显示Toast
	 * 
	 * @param context
	 * @param msg
	 *            Toast要显示的信息
	 * @param gravity
	 *            Toast显示位置（Gravity）
	// * @param xOffsetØ
	 *            x偏移量
	 */
	public static void showToast(Context context, String msg, int gravity, int x, int y) {
		Toast toast = Toast.makeText(context, msg + "", Toast.LENGTH_SHORT);
		toast.setGravity(gravity, x, y);// 设置toast位子；
		toast.show();
	}

	/**
	 * 打出log
	 * 
	 * @param type
	 *            0代表verbase 1代表debug 2代表info 3代表warn 4代表error
	 * @param msg
	 *            要打印的message
	 */
	public static void LogLa(int type, String msg) {
		switch (type) {
		case 0:
			Log.v(TAG, "ygq " + msg + "");
			break;
		case 1:
			Log.d(TAG, "ygq " + msg + "");
			break;
		case 2:
			Log.i(TAG, "ygq " + msg + "");
			break;
		case 3:
			Log.w(TAG, "ygq " + msg + "");
			break;
		case 4:
			Log.e(TAG, "ygq " + msg + "");
			break;
		default:
			Log.v(TAG, "ygq " + msg + "");
			break;
		}
	}

	/**
	 * 上传图片时重新命名文件
	 * 
	 * @param userId
	 * @return
	 */
	public static String getFileName(Long userId) {
		return VeDate.getyyyyMMddHHmmss(VeDate.getNow()) + "_" + userId + ".png";
	}

	/**
	 * 把一个View的对象转换成bitmap
	 * 
	 * @param v
	 * @return
	 */
	public static Bitmap getViewBitmap(View v) {
		v.setDrawingCacheEnabled(true);
		v.clearFocus();
		v.setPressed(false);
		// 能画缓存就返回false
		boolean willNotCache = v.willNotCacheDrawing();
		v.setWillNotCacheDrawing(false);
		int color = v.getDrawingCacheBackgroundColor();
		v.setDrawingCacheBackgroundColor(0);
		if (color != 0) v.destroyDrawingCache();
		v.buildDrawingCache();
		Bitmap cacheBitmap = v.getDrawingCache();
		if (cacheBitmap == null) return null;
		Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);
		v.destroyDrawingCache();
		v.setWillNotCacheDrawing(willNotCache);
		v.setDrawingCacheBackgroundColor(color);
		v.setDrawingCacheEnabled(false);
		if (cacheBitmap != null && !cacheBitmap.isRecycled()) cacheBitmap.recycle();
		return bitmap;
	}

	/**
	 * 创建文件
	 * 
	 * @param bitmap
	 * @param bitmapFile
	 */
	public static void createimage(Bitmap bitmap, File bitmapFile) {
		FileOutputStream bitmapWtriter = null;
		try {
			if (bitmap != null) {
				if (bitmap.getHeight() > 576 || bitmap.getWidth() > 480) bitmap = Bitmap.createScaledBitmap(bitmap, 480, 576, false);
				bitmapWtriter = new FileOutputStream(bitmapFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapWtriter);
				if (!bitmap.isRecycled()) bitmap.recycle();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bitmapWtriter != null) bitmapWtriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 创建文件
	 * 
	 * @param bitmap
	 * @param bitmapFile
	 */
	public static void createimageUnrecycle(Bitmap bitmap, File bitmapFile) {
		FileOutputStream bitmapWtriter = null;
		try {
			if (bitmap != null) {
				// if (bitmap.getHeight() > 576 || bitmap.getWidth() > 480)
				// bitmap = Bitmap.createScaledBitmap(bitmap, 480, 576, false);
				bitmapWtriter = new FileOutputStream(bitmapFile);
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bitmapWtriter);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bitmapWtriter != null) bitmapWtriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void recycleBitmap(ImageView view) {
		if (view != null) {
			try {
				BitmapDrawable bd1 = (BitmapDrawable) view.getDrawable();
				if (bd1 != null) {
					view.setImageBitmap(null);
					view.setImageDrawable(null);
					Bitmap bt1 = bd1.getBitmap();
					if (bt1 != null && !bt1.isRecycled()) {
						bt1.recycle();
					}
				}
			} catch (ClassCastException e) {
				Log.i("Exception", "ClassCastException  " + e.toString());
			} catch (RuntimeException e) {
				Log.i("Exception", "ClassCastException  " + e.toString());
			}
		}

	}

	public static Bitmap decodeFile(int id, AppCompatActivity context) throws FileNotFoundException {
		BitmapFactory.Options o = new BitmapFactory.Options();
		o.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(context.getResources(), id, o);
		final int REQUIRED_SIZE = 60;
		int width_tmp = o.outWidth, height_tmp = o.outHeight;
		int scale = 1;
		while (true) {
			if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) break;
			width_tmp /= 2;
			height_tmp /= 2;
			scale *= 2;
		}
		BitmapFactory.Options o2 = new BitmapFactory.Options();
		o2.inSampleSize = scale;
		return BitmapFactory.decodeResource(context.getResources(), id, o2);
	}

	public static Bitmap getAssBitmap(Context context, int position, String path) {
		String str = path + "/" + position + ".png.bm";
		SoftReference<Bitmap> soft = null;
		AssetManager assetManager = context.getAssets();
		try {
			InputStream is = assetManager.open(str);
			soft = new SoftReference<Bitmap>(BitmapFactory.decodeStream(is));
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (soft != null && soft.get() != null && !soft.get().isRecycled()) return soft.get();
		return null;
	}

	public static String getStockAge(int age) {
		switch (age) {
		case 1:
			return "小学生（半年以内）";
		case 2:
			return "中学生（半年-2年）";
		case 3:
			return "大学生（2-10年）";
		case 4:
			return "老股灰（10年以上）";
		default:
			return "保密";
		}
	}

	public static String getInvestmentStyle(String investmentStyle) {
		if ("2".equals(investmentStyle)) return "中线波段型";
		else if ("3".equals(investmentStyle)) return "长线稳健型";
		else return "短线激进型";
	}

	/**
	 * 停止播放录音
	 * 
	 * @param mp
	 */
	public static void unpaly(MediaPlayer mp) {
		if (mp != null) mp.reset();
	}

	public static void paly(MediaPlayer mp, String path) {
		try {
			mp.reset();
			mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mp.setDataSource(path);
			mp.prepare();
			mp.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断一个股票是否在一串股票中
	 * 
	 * @param fullcode
	 *            要判断的那支股票
	 * @param allFullcodes
	 *            一串股票
	 * @return 在那串股票中则返回true,反之返回false
	 */
	public static boolean isInTheAllFullcodes(String fullcode, String allFullcodes) {
		if (allFullcodes != null && fullcode != null) {
			String regex = "[szh0-9,]*(" + fullcode + ")[szh0-9,]*";
			Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
			Matcher ma = pa.matcher(allFullcodes);
			while (ma.find()) {
				if (ma.groupCount() > 0 && fullcode.equals(ma.group(1))) return true;
			}
		}
		return false;
	}

	/**
	 * 涨幅颜色
	 * 
	 * @param rate
	 * @return
	 */
	public static int getRateColor(double rate) {
		if (rate > 0) return Color.RED;
		else if (rate == 0 || rate == -100d) return Color.WHITE;
		else return Color.GREEN;
	}

	/**
	 * 关闭asyncTask
	 * 
	 * @param task
	 */
	public static void cancelAsyncTask(AsyncTask<?, ?, ?> task) {
		if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
			while (!task.isCancelled() && !task.cancel(true)) {
				if (task.isCancelled()) break;
				else task.cancel(true);
			}
		}
	}
	public static void cancelCall(Call<ResponseBody> call){
		if(call!=null)call.cancel();
	}

	/**
	 * 根据包名判断程序是否安装,并返回其他
	 * 
	 * @param context
	 * @param packageName
	 * @return
	 */
	public static ApplicationInfo checkApkExist(Context context, String packageName) {
		if (packageName == null || "".equals(packageName)) return null;
		try {
			ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
			return info;
		} catch (NameNotFoundException e) {
			return null;
		}
	}

	/**
	 * 隱藏鍵盤
	 * 
	 * @param context
	 * @param et
	 */
	public static void hidekeybroad(Context context, EditText et) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
	}

	/**
	 * 加载页面
	 * 
	 * @param resource
	// * @param content
	 * @return
	 */
	public static View inflateView(int resource, Context context) {
		return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
	}

	/**
	 * 把资源文件转为 Bitmap
	 * 
	 * @param context
	 * @param resid
	 * @return
	 */
	public static Bitmap readBitmap(Context context, int resid) {
		BitmapFactory.Options opt = new BitmapFactory.Options();
		opt.inPreferredConfig = Bitmap.Config.RGB_565;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		// 获取资源图片
		InputStream input = context.getResources().openRawResource(resid);
		return BitmapFactory.decodeStream(input, null, opt);
	}

	/**
	 * 获取结果的
	 * 
	 * @param activity
	 * @param cls
	 * @param intent
	 * @param back
	 * @return
	 */
	public static boolean pageJumpResult(AppCompatActivity activity, Class<?> cls, Intent intent, boolean back) {
		try {
			if (activity != null && cls != null && intent != null) {
				intent.setClass(activity, cls);
				activity.startActivityForResult(intent, 0x123);
			}
			if (activity != null) {
				if (back) {
					// 返回动画
					activity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				}
				return true;
			}
		} catch (Exception e) {
			Log.e("Exception", "pageJump error!  " + e.toString());
		}
		return false;
	}

	/**
	 * 这个方法是用来返回 bitmap的btye字节
	 * 
	 * @param bitmap
	 * @return
	 */
	public static byte[] bitmapToBytes(Bitmap bitmap) {
		if (bitmap == null) {
			return null;
		}
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		// 将Bitmap压缩成PNG编码，质量为100%存储
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);// 除了PNG还有很多常见格式，如jpeg等。
		return os.toByteArray();
	}

	public static Bitmap BytesToBitmap(byte[] photoBtye) {

		Bitmap photo = null;
		if (photoBtye == null) return photo;
		try {
			photo = BitmapFactory.decodeByteArray(photoBtye, 0, photoBtye.length);
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
		}
		return photo;
	}

	/**
	 * 如果内容中包含有[tjr_*] 就是chat新增的类型
	 * 
	 * @param txt
	 * @return
	 */
	public static String findMatcherStr(String txt) {
		if (txt != null) {
			String re = "(.*?\\[tjr_.*?\\]=)"; // Square Braces 1
			Pattern p = Pattern.compile(re, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(txt);
			if (m.find()) {
				String sbraces1 = m.group(1);
				if (txt.startsWith(sbraces1)) return sbraces1;
			}
		}
		return null;
	}

	public static String makeLogTag(Class<?> cls) {
		return "taojin_" + cls.getSimpleName();
	}

	/**
	 * 获取压缩过的图片
	 * 
	 * @param srcPath
	 * @return
	 */
	public static Bitmap getCompressImage(String srcPath) {
		if (srcPath == null) return null;
		try {
			BitmapFactory.Options newOpts = new BitmapFactory.Options();
			// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
			newOpts.inJustDecodeBounds = true;
			Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空

			newOpts.inJustDecodeBounds = false;
			int w = newOpts.outWidth;
			int h = newOpts.outHeight;
			// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
			float hh = 800f;// 这里设置高度为800f
			float ww = 480f;// 这里设置宽度为480f
			// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
			int be = 1;// be=1表示不缩放
			if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
				be = (int) (newOpts.outWidth / ww);
			} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
				be = (int) (newOpts.outHeight / hh);
			}
			if (be <= 0) be = 1;
			newOpts.inSampleSize = be;// 设置缩放比例
			// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
			bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
			return compressImage(bitmap);// 压缩好比例大小后再进行质量压缩
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	public static File GetWeiboFile(String name) throws IOException {
		final String WEIBOPATH = android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/taojin/tjrshare/";
		File dir = new File(WEIBOPATH);
		if (!dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(WEIBOPATH + name);
		if (!file.exists()) {
			file.createNewFile();
		}
		return file;
	}

	/**
	 * 使用的方法压缩图片
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap compressImage(Bitmap image) {
		if (image == null) return null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 60;
			CommonUtil.LogLa(2, "image``` " + baos.toByteArray().length / 1024);
			while (baos.toByteArray().length / 1024 > 100 && options > 1) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
				if (options <= 10) {
					options -= 1;
				}
			}
			CommonUtil.LogLa(2, "==之后```` " + baos.toByteArray().length / 1024);
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}


	/**
	 * 微信分享的logo不能超过32k
	 *
	 * @param image
	 * @return
	 */
	public static Bitmap compressImageWechatShare(Bitmap image) {
		if (image == null) return null;
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
			int options = 60;
			CommonUtil.LogLa(2, "image``` " + baos.toByteArray().length / 1024);
			while (baos.toByteArray().length / 1024 > 25 && options > 1) { // 循环判断如果压缩后图片是否大于30kb,大于继续压缩
				baos.reset();// 重置baos即清空baos
				image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
				options -= 10;// 每次都减少10
				if (options <= 10) {
					options -= 1;
				}
			}
			CommonUtil.LogLa(2, "==之后```` " + baos.toByteArray().length / 1024);
			ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
			Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
			return bitmap;
		} catch (OutOfMemoryError e) {
			return null;
		}
	}

	public static boolean isSdCard() {
		if (!Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return false;
		}
		return true;
	}

	public static void writeFile(File file, Bitmap bitmap, boolean recycle) throws IOException {
		if (bitmap == null || file == null) {
			return;
		}
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos); // PNG
		} catch (IOException ioe) {

		} finally {
			try {
				if (fos != null) {
					if (recycle) bitmap.recycle();
					fos.flush();
					fos.close();
				}
				// bitmap.recycle();
			} catch (IOException e) {
			}
		}
	}

}
