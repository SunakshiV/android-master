package com.procoin.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by zhengmj on 17-5-18.
 */

public class BitmapUtil {

    /**
     * 按尺寸压缩图片
     *
     * @param filePath
     * @param adjustOritation 是否根据图片的方向，如果是手机或者相机拍的照片，有个角度的概念，如果不处理，显示的时候会歪过来
     * @return
     * @throws Exception
     */
    public static Bitmap getSmallBitmap(String filePath, boolean adjustOritation) throws Exception {
        Bitmap bm = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(filePath), null, options);
            // int inSampleSize = 1;
            // int reqHeight = 800;
            // int reqWidth = 480;
            // int height = options.outHeight;
            // int width = options.outWidth;
            // if (height > reqHeight || width > reqWidth) {
            // final int heightRatio = Math.round((float) height / (float)
            // reqHeight);
            // final int widthRatio = Math.round((float) width / (float)
            // reqWidth);
            // inSampleSize = heightRatio < widthRatio ? widthRatio :
            // heightRatio;
            // }
            options.inSampleSize = computeSampleSize(options, -1, 1280 * 1280);
            // options.inSampleSize = inSampleSize;
            Log.d("inSampleSize", "inSampleSize==" + options.inSampleSize);
            options.inJustDecodeBounds = false;
            bm = loadBitmapByOritation(filePath, options, adjustOritation);
            // if(adjustOritation){
            // bm=loadBitmap(filePath, adjustOritation);
            // }else{
            // bm = BitmapFactory.decodeStream(new FileInputStream(filePath),
            // null, options);
            // }
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

    // 这个就是压缩图片
    public static byte[] compressImage(Bitmap image) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] resultbtye = null;
        try {
            image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
            int options = 80;
            // 这句log可能会内存溢出
            CommonUtil.LogLa(2, "compressImage 压缩位图 大小" + baos.toByteArray().length / 1024);
            while (baos.toByteArray().length / 1024 > 50 && options > 1) { // 循环判断如果压缩后图片是否大于50kb,大于继续压缩
                baos.reset();// 重置baos即清空baos
                image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
                options -= 10;// 每次都减少10
                if (options <= 0) {
                    options -= 1;
                }
            }
            resultbtye = baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            CommonUtil.LogLa(2, "compressImage 压缩位图 Exception" + e.getMessage());
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            CommonUtil.LogLa(2, "compressImage 压缩位图 OutOfMemoryError" + e.getMessage());
        } finally {
            if (baos != null) baos.close();
        }
        return resultbtye;
    }
}
