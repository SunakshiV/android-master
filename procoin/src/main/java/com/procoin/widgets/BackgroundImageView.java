package com.procoin.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;

import com.procoin.R;

/**
 * Created by zhengmj on 19-1-28.
 */

public class BackgroundImageView extends androidx.appcompat.widget.AppCompatImageView {
    private Paint paint;
    private Path path;
    private float yDistance = 0.5f;//调整白色区域在整个控件中的占比(百分比),默认为50%
    private int arcDistance = 100;//弧形顶部与底部的距离，默认为100(像素)
    private int sectorColor=Color.WHITE;//弧形颜色，默认白色
    public BackgroundImageView(Context context) {
        super(context);
        init(context,null);
    }
    public BackgroundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }
    private void init(Context context,AttributeSet attrs){
        if (attrs != null){
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.BackgroundImageView);
            yDistance = array.getFloat(R.styleable.BackgroundImageView_blockHeight,0.5f);
            yDistance = 1-yDistance;
            arcDistance = array.getInt(R.styleable.BackgroundImageView_arcHeight,100);
            sectorColor = array.getColor(R.styleable.BackgroundImageView_sectorColor, Color.WHITE);
            array.recycle();
        }
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(sectorColor);
        path = new Path();
    }
    public void setArcDistance(int distance){
        if (distance<=0)return;
        arcDistance = distance;
        invalidate();
    }
    public void setBlockHeight(float percent){
        yDistance = 1-percent;
        if (percent<=0||percent>=0.75f){
            yDistance = 0.5f;
        }
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        super.onDraw(canvas);
        path.moveTo(0,height*yDistance);
        path.quadTo(width/2,height*yDistance+arcDistance,width,height*yDistance);
        path.lineTo(width,height+1);//不加1可能下面出现一条黑线，可能是由getHeight()返回int类型而这里是float类型，产生了误差
        path.lineTo(0,height+1);
        path.close();
        path.setFillType(Path.FillType.WINDING);
//        int saveCount = canvas.saveLayer(0,0,getWidth(),getHeight(),null,Canvas.ALL_SAVE_FLAG);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY));
        canvas.drawPath(path,paint);
//        canvas.restoreToCount(saveCount);
//        paint.setXfermode(null);
    }
}
