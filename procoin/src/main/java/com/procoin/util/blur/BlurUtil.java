package com.procoin.util.blur;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import androidx.core.view.ViewCompat;
import android.view.View;

import com.procoin.util.BitmapUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhengmj on 16-8-16.
 */
public class BlurUtil {


    public static void blur(final Bitmap bkg, final View view) {
        if (bkg == null || view == null) return;

        final ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler();

        synchronized (BlurUtil.class) {
            singleThreadPool.execute(new Runnable() {

                public void run() {
                    if (bkg == null || view == null) return;
                    try {
                        float radius = 16;// 这个越大越模糊128
                        Bitmap fastBitmap = FastBlur.doBlur(bkg, (int) radius, false);
                        Bitmap endbkg = BitmapUtil.BytesToBitmap(BitmapUtil.compressImage(fastBitmap));
                        final BitmapDrawable drawable = new BitmapDrawable(endbkg);
                        handler.post(new Runnable() {

                            public void run() {
                                if (drawable == null || view == null) return;
                                ViewCompat.setBackground(view, drawable);

                            }
                        });
                        if (fastBitmap != null && !fastBitmap.isRecycled()) {
                            fastBitmap.recycle();
                        }

                    } catch (Exception e) {
                    } catch (OutOfMemoryError e) {
                    } finally {
                        if (singleThreadPool != null) {
                            singleThreadPool.shutdown();
                        }

                    }
                }
            });
        }
    }


}
