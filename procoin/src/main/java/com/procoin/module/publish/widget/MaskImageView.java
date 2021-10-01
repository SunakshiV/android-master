package com.procoin.module.publish.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MaskImageView extends androidx.appcompat.widget.AppCompatImageView {

    public MaskImageView(Context context) {
        super(context);
    }

    public MaskImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MaskImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        Drawable d = createStateDrawable(getContext(), new BitmapDrawable(getContext().getResources(), bm));
        setImageDrawable(d);
    }

    public StateListDrawable createStateDrawable(Context context, Drawable normal) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(View.PRESSED_ENABLED_STATE_SET, createPressDrawable(normal));
        drawable.addState(View.ENABLED_STATE_SET, normal);
        drawable.addState(View.EMPTY_STATE_SET, normal);
        return drawable;
    }

    public Drawable createPressDrawable(Drawable d) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ((BitmapDrawable) d).getBitmap().compress(Bitmap.CompressFormat.JPEG, 100, baos);
        int option = 100;
        while (baos.toByteArray().length/1024>10){
            baos.reset();
            if (option>=10)option -= 10;
            Log.d("200","baos length == "+(baos.toByteArray().length/1024)+" option == "+option);
            ((BitmapDrawable) d).getBitmap().compress(Bitmap.CompressFormat.JPEG, option, baos);
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        Bitmap bitmap = BitmapFactory.decodeStream(isBm,null,null);
//        Bitmap bitmap = ((BitmapDrawable) d).getBitmap().copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColor(0x60000000);
        RectF rect = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        new Canvas(bitmap).drawRoundRect(rect, 4, 4, paint);
        return new BitmapDrawable(getContext().getResources(), bitmap);
    }

}
