package com.procoin.util.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import androidx.annotation.RequiresApi;

/**
 * Created by zhengmj on 18-10-18.
 */

public class BlurBitmapUtil {
    private static final float BITMAP_SCALE = 0.8f;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Drawable drawable, float blurRadius){
        return blurBitmap(context,drawableToBitmap(drawable),blurRadius);
    }
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blurBitmap(Context context, Bitmap bitmap, float blurRadius){
        int width = Math.round(bitmap.getWidth()*BITMAP_SCALE);
        int height = Math.round(bitmap.getHeight()*BITMAP_SCALE);
        Bitmap inputBitmap = Bitmap.createScaledBitmap(bitmap,width,height,false);
        inputBitmap = RGB565toARGB8888(inputBitmap);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);
        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs,inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs,outputBitmap);
        blurScript.setRadius(blurRadius);
        blurScript.setInput(tmpIn);
        blurScript.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }
    public static Bitmap RGB565toARGB8888(Bitmap img){
        int numPixels = img.getWidth()*img.getHeight();
        int[] pixels = new int[numPixels];
        img.getPixels(pixels,0,img.getWidth(),0,0,img.getWidth(),img.getHeight());
        Bitmap result = Bitmap.createBitmap(img.getWidth(),img.getHeight(),Bitmap.Config.ARGB_8888);
        result.setPixels(pixels,0,result.getWidth(),0,0,result.getWidth(),result.getHeight());
        return result;
    }
    public static Bitmap drawableToBitmap(Drawable drawable){
        int h = drawable.getIntrinsicHeight();
        int w = drawable.getIntrinsicWidth();
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(w,h,config);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;
    }
}
