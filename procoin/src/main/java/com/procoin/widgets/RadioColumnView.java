package com.procoin.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.procoin.util.DensityUtil;

/**
 * Created by zhengmj on 19-2-18.
 */

public class RadioColumnView extends SurfaceView implements SurfaceHolder.Callback,Runnable{
    private Paint frontRectPaint;
    private Paint backRectPaint;
    private int space;//间隔
    private int width;
    private int columnNum;
    private SurfaceHolder holder;
    private Canvas canvas;
    private boolean isDrawing;
    private Rect[] rects;
    public RadioColumnView(Context context) {
        super(context);
        init(context);
    }

    public RadioColumnView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context){
        width = DensityUtil.dip2px(context,1);
        space = DensityUtil.dip2px(context,0.5f);
        columnNum = getWidth()/(width + space);

        frontRectPaint = new Paint();
        frontRectPaint.setColor(Color.WHITE);
        frontRectPaint.setStyle(Paint.Style.FILL);
        frontRectPaint.setAntiAlias(true);

        backRectPaint = new Paint();
        backRectPaint.setColor(Color.parseColor("#33ffc90c"));
        backRectPaint.setStyle(Paint.Style.FILL);
        backRectPaint.setAntiAlias(true);

        holder = getHolder();
        holder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDrawing = true;
        new Thread(this).start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDrawing = false;
    }

    @Override
    public void run() {
        while (isDrawing)draw();
    }
    private void draw(){
        try{
            canvas = holder.lockCanvas();
        }catch (Exception e){

        }finally {
            if (canvas!=null) holder.unlockCanvasAndPost(canvas);
        }
    }

}
