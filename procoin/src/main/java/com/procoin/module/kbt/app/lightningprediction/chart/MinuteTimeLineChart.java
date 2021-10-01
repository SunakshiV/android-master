package com.procoin.module.kbt.app.lightningprediction.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.procoin.module.kbt.app.lightningprediction.entity.Minute;
import com.procoin.util.DateUtils;
import com.procoin.module.kbt.app.lightningprediction.entity.PreGame;
import com.procoin.util.StockChartUtil;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.TreeMap;


/**
 * Created by taojinroad on 16/8/24.
 */
public class MinuteTimeLineChart extends View {
    private final int CHART_BG = Color.argb(77, 0, 0, 0); //
    private final int lineColor = Color.rgb(255, 143, 1);//分时线的颜色
    private final int priceColor = Color.parseColor("#66ffffff");

    private int chartWidth = 0; // 图的宽度
    private int chartHeight = 0;//图的高度
    private int chartTolHeight = 0;//图的总高度

    private int tolIndex;//画图最大点数
    private int stopIndex;//图的中线
    private String startTime, midTime, endTime;

    public double stepMaxPrice; // 价格绝对值最大值范围
    public double maxPriceValue, minPriceValue;// 价格绝对值最大值
    public double votePriceValue; // 中间值

    private Paint gridPaint, linePaint;// 画表格的线
    private TextPaint fontPaint;// 字体颜色
    private int fontSize = 8; // GRAY 图表左中间的字体颜色

    private float stepX, stepY;
    private Minute minutePre;

    private float maxWaveRadius = 30;//扩散半径
    private long waveTime = 2000;//一个涟漪扩散的时间
    private int waveRate = 1;//涟漪的个数
    private Paint circlePaint;
    private float[] waveList;//涟漪队列

    private Path path = new Path();
    private DashPathEffect pathEffect = new DashPathEffect(new float[]{10f, 10f}, 0);

    private final TreeMap<Integer, Minute> minuteDatas = new TreeMap<>(new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            if(o1 > o2)return 1;
            else if(o1 < o2)return -1;
            return 0;
        }
    });

    public MinuteTimeLineChart(Context context) {
        super(context);
        init();
    }

    public MinuteTimeLineChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MinuteTimeLineChart(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setAntiAlias(true); // 去掉锯齿
        gridPaint.setColor(Color.rgb(91, 91, 91));// 线的颜色
        gridPaint.setStyle(Paint.Style.STROKE);
        gridPaint.setStrokeWidth(1.0f);

        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(true);/* 去锯齿 */
        linePaint.setStrokeWidth(2f);

        fontPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setAntiAlias(true); // 去掉锯齿
        fontPaint.setTextSize(StockChartUtil.pxToSp(getResources(), fontSize));
        fontPaint.setStyle(Paint.Style.FILL); // 设置paint的 style 为STROKE：实心心
        fontPaint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        fontPaint.setStrokeWidth(0.01f);


        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        waveList = new float[waveRate];
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(waveTime);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                for (int i = 0; i < waveList.length; i++) {
                    float v = value - i * 1.0f / waveRate;
                    if (v < 0 && waveList[i] > 0) {
                        v += 1;
                    }
                    waveList[i] = v;
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(CHART_BG); // 背景颜色
        drawLine(canvas);//画分时线
    }

    private void drawLine(final Canvas canvas) {
        //画中间线
        String textFont = formatNumber(2, votePriceValue);
        canvas.drawLine(fontPaint.measureText(textFont) + 8f, chartHeight / 2.0f, chartWidth, chartHeight / 2.0f, gridPaint);
        //画竖线(虚线)
        gridPaint.setPathEffect(pathEffect);
        path.reset();
        path.moveTo(stopIndex * stepX, 0);
        path.lineTo(stopIndex * stepX, chartHeight);
        canvas.drawPath(path, gridPaint);
//        canvas.drawLine(stopIndex * stepX, 0, stopIndex * stepX, chartHeight, gridPaint);
        //画中间值votePriceValue
        drawText(canvas, textFont, 0, chartHeight / 2.0f + getFontHeight(fontPaint) / 3, priceColor, Paint.Align.LEFT);
        //画底部时间
        drawText(canvas, DateUtils.getStringDateOfString2(startTime, DateUtils.TEMPLATE_HHmmss), 0, chartTolHeight - 4f, priceColor, Paint.Align.LEFT);
        drawText(canvas, DateUtils.getStringDateOfString2(midTime, DateUtils.TEMPLATE_HHmmss), stopIndex * stepX, chartTolHeight - 4f, priceColor, Paint.Align.CENTER);
        drawText(canvas, DateUtils.getStringDateOfString2(endTime, DateUtils.TEMPLATE_HHmmss), chartWidth, chartTolHeight - 4f, priceColor, Paint.Align.RIGHT);

        minutePre = null;
        for (Minute minute : minuteDatas.values()) {
            minute.lindX = minute.index * stepX;
            minute.lineY = getCharY(minute.price);
            if (minutePre != null) {
                canvas.drawLine(minutePre.lindX, minutePre.lineY, minute.lindX, minute.lineY, linePaint);
            }else{
                canvas.drawLine(0, getCharY(votePriceValue), minute.lindX, minute.lineY, linePaint);
            }
            minutePre = minute;
        }
        if (minutePre == null) return;
        canvas.drawCircle(minutePre.lindX, minutePre.lineY, 8, linePaint);//根据进度计算扩散半径
        for (Float waveRadius : waveList) {
            circlePaint.setColor(Color.argb((int) (255 * (1 - waveRadius)), 255, 143, 1));//根据进度产生一个argb颜色
            canvas.drawCircle(minutePre.lindX, minutePre.lineY, waveRadius * maxWaveRadius, circlePaint);//根据进度计算扩散半径
        }
        //画当前价
        textFont = formatNumber(2, minutePre.price);
        if (chartWidth - minutePre.lindX > (fontPaint.measureText(textFont) + 10)) {
            drawText(canvas, textFont, minutePre.lindX + 10f, minutePre.lineY + getFontHeight(fontPaint) / 3, lineColor, Paint.Align.LEFT);
        } else {
            drawText(canvas, textFont, chartWidth - 20f, minutePre.lineY + getFontHeight(fontPaint) / 3, lineColor, Paint.Align.RIGHT);
        }
    }

    /**
     * 在x,y坐标中画上text值
     *
     * @param canvas
     * @param text
     * @param x
     * @param y
     * @param color  颜色值
     */
    public void drawText(final Canvas canvas, final String text, final float x, final float y, int color, Paint.Align align) {
        fontPaint.setColor(color);
        fontPaint.setTextAlign(align);
        canvas.drawText(text, x, y, fontPaint);
        fontPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 刷新数据
     *
     * @param preGame
     */
    public void refreshData(PreGame preGame) {
        if (preGame == null) return;
        tolIndex = preGame.tolIndex;
        stopIndex = preGame.stopIndex;
        votePriceValue = preGame.votePrice;
        startTime = preGame.startTime;
        midTime = preGame.midTime;
        endTime = preGame.endTime;
        //清除图表重新绘画
        if (preGame.clearChart == 1) {
            minuteDatas.clear();
            stepMaxPrice = 0;
        }
        if (preGame.minutes != null) {
            for (Minute minute : preGame.minutes) {
                minuteDatas.put(minute.index, minute);
                stepMaxPrice = Math.max(stepMaxPrice, Math.abs(minute.price - votePriceValue));
            }
        }
        maxPriceValue = votePriceValue + stepMaxPrice * 1.1;
        minPriceValue = votePriceValue - (maxPriceValue - votePriceValue);
        //每根占x像素
        stepX = chartWidth * 1f / tolIndex;
        stepY = (float) (maxPriceValue - minPriceValue) / chartHeight;
        invalidate();
    }

    protected float getCharY(final double value) {
        if (stepY == 0.0) return 0.0f;
        if (value <= 0) return -1;
        return (float) ((maxPriceValue - value) / stepY);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        chartWidth = measure(widthMeasureSpec);
        chartTolHeight = measure(heightMeasureSpec);
        chartHeight = chartTolHeight - 30;
        setMeasuredDimension(chartWidth, chartTolHeight);
    }

    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 200;
        if (specMode == MeasureSpec.EXACTLY) {// 精确尺寸
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {// 最大可获得的空间
            result = specSize;
        }
        return result;
    }

    /**
     * 格式化小数,4舍5入
     *
     * @param i
     * @param value
     * @return
     */
    public String formatNumber(final int i, final double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
            return bd.toPlainString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 获取字所占高度
     *
     * @param textPaint
     * @return
     */
    public float getFontHeight(Paint textPaint) {
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }
}
