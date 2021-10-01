package com.procoin.http.util;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;



import com.procoin.http.TjrBaseApi;
import com.procoin.http.resource.BaseRemoteResourceManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.ResponseBody;
import retrofit2.Call;

public class CommonUtil {
    private static String TAG = "ddd";
    private static Toast toast;

    /**
     * 打出log
     *
     * @param type 0代表verbase 1代表debug 2代表info 3代表warn 4代表error
     * @param msg  要打印的message
     */
    public static void LogLa(int type, String msg) {
        if (!TjrBaseApi.isLog) return;
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
     * 判断是否安装qq或者qq空间
     *
     * @return
     */
    public static boolean isInstallQQ(Context context) {
        try {
            if (CommonUtil.checkApkExist(context, "com.tencent.mobileqq") != null) return true;// QQ
            else if (CommonUtil.checkApkExist(context, "com.qzone") != null) return true;// QQ空间
            return false;
        } catch (Exception e) {
            return false;
        }
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
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
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

    public static void cancelCall(Call<ResponseBody> call) {
        if (call != null) call.cancel();

    }

    /**
     * 自定义位置显示Toast
     *
     * @param context
     * @param msg      Toast要显示的信息
     * @param gravity  Toast显示位置（Gravity）
   //  * @param xOffsetØ x偏移量
     */
    public static void showToast(Context context, String msg, int gravity) {
        Toast toast = Toast.makeText(context, msg + "", Toast.LENGTH_SHORT);
        toast.setGravity(gravity, 0, 0);// 设置toast位子；
        toast.show();
    }

    /**
     * 自定义位置显示Toast
     *
     * @param context
     * @param msg      Toast要显示的信息
     * @param gravity  Toast显示位置（Gravity）
  //   * @param xOffsetØ x偏移量
     */
    public static void showToast(Context context, String msg, int gravity, int x, int y) {
        Toast toast = Toast.makeText(context, msg + "", Toast.LENGTH_SHORT);
        toast.setGravity(gravity, x, y);// 设置toast位子；
        toast.show();
    }

    /**
     * 打印Toast
     *
     * @param value
     */
    public static void showmessage(String value, Context conext) {
        if (toast == null) {
            toast = Toast.makeText(conext, value, Toast.LENGTH_SHORT);

        } else {
            toast.setText(value);
        }
        toast.show();
    }

    public static File saveBitmapToFile(BaseRemoteResourceManager baseRemoteResourceManager, Bitmap bitmap) throws Exception {
        if (bitmap == null) return null;
        String fileName2 = System.currentTimeMillis() + ".jpg";
        File file2 = baseRemoteResourceManager.getFile(fileName2);
        baseRemoteResourceManager.writeFile(file2, bitmap, true);
        if (file2 != null && file2.exists()) {
            return file2;
        }
        return null;
    }



    public static File saveBitmapToFile(BaseRemoteResourceManager remoteResourceManagerTalkie, Bitmap bitmap, boolean reyle, String userId) throws Exception {
        if (bitmap == null || remoteResourceManagerTalkie == null) return null;
        String fileName2 = System.currentTimeMillis() + "" + userId + ".jpg";
        File file2 = remoteResourceManagerTalkie.getFile(fileName2);
        Log.d("absolutePath", "555555");
        remoteResourceManagerTalkie.writeFile(file2, bitmap, reyle);
        Log.d("absolutePath", "666666");
        if (file2 != null && file2.exists()) {
            return file2;
        }
        return null;
    }


    public static Bitmap getSmallBitmap(String filePath, boolean adjustOritation) throws Exception {
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(filePath), null, options);
            options.inSampleSize = computeSampleSize(options, -1, 1280 * 1280);
            // options.inSampleSize = inSampleSize;
            Log.d("inSampleSize", "inSampleSize==" + options.inSampleSize);
            options.inJustDecodeBounds = false;
            bm = loadBitmapByOritation(filePath, options, adjustOritation);
        } catch (OutOfMemoryError e) {
        }
        return bm;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);
        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }
        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;
        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }
        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }

    }

    /**
     * 从给定的路径加载图片，并指定是否自动旋转方向
     *
     * @throws FileNotFoundException
     */
    public static Bitmap loadBitmapByOritation(String imgpath, BitmapFactory.Options ops, boolean adjustOritation) throws FileNotFoundException {
        Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(imgpath), null, ops);
        if (!adjustOritation) {
            return bm;
        } else {
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                // 计算旋转角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            Log.d("digree", "digree==" + digree);
            if (digree != 0) {
                // 旋转图片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            }
            return bm;
        }
    }

    public static Bitmap BytesToBitmap(byte[] photoBtye) {

        Bitmap photo = null;
        if (photoBtye == null) return photo;
        try {
            photo = BitmapFactory.decodeByteArray(photoBtye, 0, photoBtye.length);
            CommonUtil.LogLa(2, " BytesToBitmap " + photoBtye.length / 1024);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return photo;
    }


    /**
     * 这个方法是用来判断输入字符长度，一个中文算一个字符，2个英文算一个字符，2个数字算一个字符
     *
     * @param str
     * @param maxLength
     * @return 返回true代表已经超标
     */
    public static boolean getGbkStringLength(String str, int maxLength) {
        if (TextUtils.isEmpty(str)) return true;
        try {
            return str.getBytes("GBK").length / 2 > maxLength;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return true;
    }




    /**
     * 隐藏键盘
     *
     * @param activity
     */
/*    public static void hideSoftKeyBoard(AppCompatActivity activity) {
        View localView = activity.getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (localView != null && imm != null) {
            imm.hideSoftInputFromWindow(localView.getWindowToken(), 2);
        }
    }

    public static void hideSoftKeyBoard(AppCompatActivity activity, AlertDialog mAlertDialog) {
        if (mAlertDialog != null && mAlertDialog.getCurrentFocus() != null && mAlertDialog.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mAlertDialog.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static void hideSoftKeyBoard(AppCompatActivity activity, Dialog mAlertDialog) {
        if (mAlertDialog != null && mAlertDialog.getCurrentFocus() != null && mAlertDialog.getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mAlertDialog.getCurrentFocus().getWindowToken(), 0);
        }
    }*/


    /**
     * view转bitmap
     */
    public static Bitmap viewConversionBitmap(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

}
