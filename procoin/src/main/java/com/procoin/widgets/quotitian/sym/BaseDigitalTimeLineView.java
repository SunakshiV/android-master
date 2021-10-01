package com.procoin.widgets.quotitian.sym;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

import com.procoin.util.StockChartUtil;
import com.procoin.R;

import java.math.BigDecimal;

public abstract class BaseDigitalTimeLineView extends View {

    public int fontColor = StockChartUtil.GRAY; // GRAY 图表左中间的字体颜色

    public int shadeStartColor = Color.argb(24, 255, 143, 1);//阴影渐变开始颜色
    public int shadeMidColor = Color.argb(16, 255, 143, 1);//阴影渐变开始颜色
    public int shadeEndColor = Color.argb(8, 255, 143, 1);//阴影渐变结束颜色

    public int lineColor = Color.rgb(255, 143, 1);//分时线的颜色
    public int lineCrossColor = Color.rgb(255, 255, 255);//十字线的颜色
    public int volColor = Color.rgb(255, 143, 1);//成交量的颜色

    private int fontSize = 8; // GRAY 图表左中间的字体颜色

    public Paint gridPaint;// 画表格的线
    public TextPaint fontPaint;// 字体颜色
    public Paint timePaint;
    public Paint mBitPaint;//

    public int tolChartWidth;
    public int chartWidth = 0; // 图的宽度
    public int chartHeight = 0;//整个图的高度（lineChartHeight+volChartHeight）
    public float lineChartHeight = 0; // 分时图的高度
    public float volChartHeight = 0; // 成交量图高度

    public float stepMarginTop, stepMarginBottom, stepColumn, stepRow;

    public int displayNum = 100;// 屏幕能显示的总条数
    public int shiftNum = 20; //屏幕能显示的位移数
    public int startCursor;// 屏幕显示游标开始
    public int endCursor;// 屏幕显示游标结束

    public int defaultLevel = 35;// 默认显示级别
    public int minLevel = 5;// 代表最大级别
    public int maxLevel = 100;// 代表显小级别
    public float candlesWidth;// 蜡烛的宽度
    public float spacing = 4;// 蜡烛与蜡烛间的间距


    public float spanX; // X坐标跨度值
    public float spanLineY; // K线的Y坐标跨度值
    public float spanVolY; // 成交量 Y坐标跨度值

    public double maxPriceValue; // 线最大值
    public double minPriceValue; // 线最小值
    public double maxVolValue;// 成交量数据中的最大值

    public int priceDecimals = 2;//小数位数

    public String tip = "";//火币标志


    public BaseDigitalTimeLineView(Context context) {
        super(context);
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setAntiAlias(true); // 去掉锯齿
        gridPaint.setColor(Color.rgb(91, 91, 91));// 线的颜色
        gridPaint.setStrokeWidth(1.0f);

        fontPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setAntiAlias(true); // 去掉锯齿
        fontPaint.setTextSize(StockChartUtil.pxToSp(getResources(), fontSize));
        fontPaint.setStyle(Paint.Style.FILL); // 设置paint的 style 为STROKE：实心心
        fontPaint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        fontPaint.setStrokeWidth(0.01f);

        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint.setStyle(Paint.Style.FILL); // 设置paint的 style 为STROKE：实心心
        timePaint.setStrokeWidth(1.0f);
        timePaint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));

        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);

    }

    /**
     * 回调画分时图
     *
     * @param canvas
     */
    public abstract void drawShareTime(Canvas canvas);


    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(StockChartUtil.CHART_BG); // 背景颜色
        drawHuobiLogo(canvas);
        createDivider(canvas);//画表格线
        calculateDisplayNum();
        drawShareTime(canvas);

    }

    private void drawHuobiLogo(Canvas canvas) {
        if ("火币".equals(tip)) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_huobi_mark_logo);
            Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect mDestRect = new Rect(30, (int) (lineChartHeight + stepMarginTop - bitmap.getHeight() - 30), 30 + bitmap.getWidth(), (int) (lineChartHeight + stepMarginTop - 30));
            canvas.drawBitmap(bitmap, mSrcRect, mDestRect, mBitPaint);
        }
    }

    /**
     * 画表格
     *
     * @param canvas
     */
    private void createDivider(final Canvas canvas) {
        stepMarginTop = chartHeight / 12;
        stepMarginBottom = chartHeight / 20;
        lineChartHeight = chartHeight - stepMarginTop - stepMarginBottom;
        stepRow = (float) (chartHeight - stepMarginTop - stepMarginBottom) / 5; // 计算每行的跨度
        volChartHeight = stepRow;//成交量的图高度
        lineChartHeight = lineChartHeight - volChartHeight;//分时线的高度
        //画竖线
        stepColumn = chartWidth / 5.0f; // 计算每列的跨度
        for (int j = 0; j < 6; j++) { // 画columns列线
            canvas.drawLine(stepColumn * j, 0.0f, stepColumn * j, chartHeight - stepMarginBottom, gridPaint);
//            drawText(canvas, "15:56", stepColumn * j, chartHeight - stepMarginBottom / 2, fontColor, Paint.Align.CENTER);
        }

        boolean drawRate = true;
        if (maxPriceValue == 0 || minPriceValue == 0) {//都为0就不用花比例了
            drawRate = false;
        }
        double middPriceValue = (maxPriceValue - minPriceValue) / 2 + minPriceValue;
        String rateText = formatNumberAuto((maxPriceValue - middPriceValue) / middPriceValue * 100) + "%";
        String rateHalfText = formatNumberAuto((maxPriceValue - middPriceValue) / 2 / middPriceValue * 100) + "%";
        //画横线
        canvas.drawLine(0, 0, chartWidth, 0, gridPaint);//第1条线
        canvas.drawLine(0, stepMarginTop, chartWidth, stepMarginTop, gridPaint);//第2条线
        if (drawRate)
            drawText(canvas, rateText, 10, stepMarginTop - 10f, fontColor, Paint.Align.LEFT);//比例
        drawText(canvas, formatNumberAuto(maxPriceValue), chartWidth - 10f, stepMarginTop - 10f, fontColor, Paint.Align.RIGHT);
        double stepPrice = (maxPriceValue - minPriceValue) / 4;
        for (int i = 1; i < 7; i++) {
            if (i == 2) {
                gridPaint.setStrokeWidth(3.5f);
                canvas.drawLine(0, stepRow * i + stepMarginTop, chartWidth, stepRow * i + stepMarginTop, gridPaint);
                gridPaint.setStrokeWidth(1.0f);
            } else {
                canvas.drawLine(0, stepRow * i + stepMarginTop, chartWidth, stepRow * i + stepMarginTop, gridPaint);
            }
            if (i == 4) {
                drawText(canvas, formatNumberAuto(minPriceValue), chartWidth - 10f, stepRow * i + stepMarginTop - 10f, fontColor, Paint.Align.RIGHT);
                if (drawRate)
                    drawText(canvas, "-" + rateText, 10, stepRow * i + stepMarginTop - 10f, fontColor, Paint.Align.LEFT);//比例
            } else if (i < 4) {
                drawText(canvas, formatNumberAuto(maxPriceValue - stepPrice * i), chartWidth - 10f, stepRow * i + stepMarginTop - 10f, fontColor, Paint.Align.RIGHT);
                if (drawRate) {
                    if (i == 1) {
                        drawText(canvas, rateHalfText, 10, stepRow * i + stepMarginTop - 10f, fontColor, Paint.Align.LEFT);//比例
                    } else if (i == 3) {
                        drawText(canvas, "-" + rateHalfText, 10, stepRow * i + stepMarginTop - 10f, fontColor, Paint.Align.LEFT);//比例
                    }
                }
            }
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

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        chartWidth = measure(widthMeasureSpec);
        tolChartWidth = chartWidth;
        chartHeight = measure(heightMeasureSpec);
        calculateDisplayNum();
        setMeasuredDimension(chartWidth, chartHeight);
    }

    private int measure(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        int result = 500;
        if (specMode == MeasureSpec.EXACTLY) {// 精确尺寸
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {// 最大可获得的空间
            result = specSize;
        }
        return result;
    }

    /**
     * 计算当前屏幕以当前点的宽度,和以当前的点宽度能显示多少根
     */

    public void calculateDisplayNum() {
        float pie = (defaultLevel - maxLevel / 2.0f) / (maxLevel / 2.0f);
        candlesWidth = (1 + pie) * chartWidth / 200.0f;
        spacing = (1 + pie) * 4;
        spanX = candlesWidth + spacing;
        displayNum = (int) (chartWidth / spanX);
        shiftNum = displayNum / 5;
        if (lineChartHeight != 0)
            spanLineY = (float) (maxPriceValue - minPriceValue) / lineChartHeight;
        if (volChartHeight != 0) spanVolY = (float) maxVolValue / volChartHeight;
    }

    /**
     * 计算纵坐值
     *
     * @param // 高度值
     * @param // 每个跨度值
     * @param // 时间
     * @return 两位小数
     */
    protected float getCharPixelY(final double max, final double spanY, final double value) {
        if (spanY == 0.0) return 0.0f;
        if (value <= 0) return -1;
        return (float) ((max - value) / spanY);
    }

    /**
     * 计算成交量纵坐值
     *
     * @param value 当前成交量
     * @return 两位小数
     */
    protected float getVolumeY(final double max, final double spanY, final double value) {
        double result = 0.0f;
        try {
            if (spanY <= 0.0) return 0.0f;
            result = (max - value) / spanY;
            if (result == 0.0) result = 0.5;// 涨停线
            else if (value == 0) result = result + 5;// 跌停线
            return Float.parseFloat(formatNumber(4, result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
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

    public String formatNumberAuto(final double value) {
        try {
            return formatNumber(priceDecimals, value);
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
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        return fontMetrics.descent - fontMetrics.ascent;
    }

    /**
     * 画时间
     *
     * @param canvas
     * @param text
     * @param x
     * @param y
     */
    public void drawTextTime(Canvas canvas, String text, float x, float y, Paint.Align align) {
        timePaint.setColor(StockChartUtil.M5_COLOR);
        timePaint.setTextAlign(align);
        canvas.drawText(text, x, y, timePaint);
    }
}
