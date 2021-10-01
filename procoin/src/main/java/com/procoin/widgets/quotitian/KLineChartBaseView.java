package com.procoin.widgets.quotitian;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

import com.procoin.data.sharedpreferences.StockSharedPreferences;
import com.procoin.util.DateUtils;
import com.procoin.util.StockChartUtil;
import com.procoin.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public abstract class KLineChartBaseView extends View {

    public int chartBgColor = StockChartUtil.CHART_BG;// 图的背景色  StockChartUtil.CHART_BG
    // 这个是保存实心还是空心的变量
//	private final String STOCK_PREFS = "stock_prefs";
//	private final String PAINT_STYLE = "paint_style";
    public Style klinePaintStyle;// 蜡烛的样式 是空心还是实心

    public int klineHeight = 0; // 代表K线图所属的高度
    public int menuHeight = 0; // 代表按钮的高度
    public int middleHeight = 0; // 代表中间macd高度 还有时间的高度
    public int timeTextHeight = 0;//时间文字的高度
    public int footHeight = 0; // 代表成交量高度
    public int totHeight = 0; // 代表整个图所属的总高度
    public int chartWidth = 0; // 代表图所属的宽度 总高度
    private float fontVHeight = 25f;

    public double maxValue; // K线最大值
    public double minValue; // K线最小值

    public double cjslMax;// 成交量数据中的最大值
    public double kdjMax;// KDJ的最大、最小值
    public double kdjMin;
    public double macdMax;// MACD的最大最小值、最小值
    public double macdMin;

    public float candlesWidth;// 蜡烛的宽度
    public float spacing = 4;// 蜡烛与蜡烛间的间距
    public int displayNum;// 屏幕能显示的总天数
    public int startCursor;// 屏幕显示游标开始
    public int endCursor;// 屏幕显示游标结束

    public int defaultLevel = 55;// 默认显示级别
    public int minLevel = 5;// 代表最大级别
    public int maxLevel = 100;// 代表显小级别

    public float spanX; // X坐标跨度值
    public float spanKlineY; // K线的Y坐标跨度值
    public float spanCjslY; // 成交量 Y坐标跨度值
    public float spanKDJY; // KDJY坐标跨度值
    public float spanMACDY; // MACDY坐标跨度值

    private Paint linePaint;
    public Paint textPaint;
    private Paint timePaint;

    private Paint mBitPaint;

    public int priceDecimals = 2;//小数位数,后台返回
    public String tip="";//火币标志

    public KLineChartBaseView(Context context) {
        super(context);
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setAntiAlias(true); // 去掉锯齿
        linePaint.setColor(Color.rgb(99, 99, 99));// 线的颜色
        linePaint.setStrokeWidth(0.0f);//设置为0.5有些机子显示不出来

        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Style.FILL); // 设置paint的 style 为STROKE：实心心
        textPaint.setStrokeWidth(1.0f);
        textPaint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));
        fontVHeight = getFontHeight(textPaint) * 0.67f;// 一行点高度

        timePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        timePaint.setStyle(Style.FILL); // 设置paint的 style 为STROKE：实心心
        timePaint.setStrokeWidth(1.0f);
        timePaint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));


        mBitPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBitPaint.setFilterBitmap(true);
        mBitPaint.setDither(true);

        if (StockSharedPreferences.isPaintStyleFill(context)) {
            klinePaintStyle = Style.FILL;
        } else {
            klinePaintStyle = Style.STROKE;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawColor(chartBgColor); // 背景颜色
        drawHuobiLogo(canvas);
        createKlineChartCoordinate(canvas);
        drawKlineChart(canvas);
    }


    private void drawHuobiLogo(Canvas canvas){

        if("火币".equals(tip)){
            Bitmap bitmap= BitmapFactory.decodeResource(getResources(), R.drawable.ic_huobi_mark_logo);
            Rect mSrcRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            Rect mDestRect = new Rect(30, (int)(klineHeight-bitmap.getHeight()-30), 30+bitmap.getWidth(), (int)(klineHeight-30));
            canvas.drawBitmap(bitmap,mSrcRect,mDestRect,mBitPaint);
        }

    }


    public void clearScreen() {
        maxValue = 0; // K线最大值
        minValue = 0; // K线最小值
        cjslMax = 0;// 成交量数据中的最大值
        kdjMax = 0;// KDJ的最大、最小值
        kdjMin = 0;
        macdMax = 0;// MACD的最大最小值、最小值
        macdMin = 0;
        startCursor = 0;// 屏幕显示游标开始
        endCursor = 0;// 屏幕显示游标结束
        spanX = 0; // X坐标跨度值
        spanKlineY = 0; // K线的Y坐标跨度值
        spanCjslY = 0; // 成交量 Y坐标跨度值
        spanKDJY = 0; // KDJY坐标跨度值
        spanMACDY = 0; // MACDY坐标跨度值
    }

    /**
     * 格式化小数,不4舍5入
     *
     * @param i
     * @param value
     * @return
     */
    public String formatNumber2(final int i, final double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, BigDecimal.ROUND_DOWN);
            return bd.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 画K线图回调
     *
     * @param canvas
     */
    public abstract void drawKlineChart(final Canvas canvas);

    /**
     * 绘制K线图表格及初始化
     */
    public void createKlineChartCoordinate(final Canvas canvas) {
        float stepChartRow = (float) klineHeight / 4; // 计算K线每行的跨度
        for (int i = 0; i <= 4; i++) {
            canvas.drawLine(0.0f, stepChartRow * i, chartWidth, stepChartRow * i, linePaint);
        }
        int rowsVol = 2;
        float stepVolRow = (float) footHeight / rowsVol; // 计算K线每行的跨度
        if (menuHeight > 0) {// 先画顶部的
            canvas.drawLine(0.0f, klineHeight + middleHeight, chartWidth, klineHeight + middleHeight, linePaint);
        }

        for (int i = 0; i <= 2; i++) {//macd 底部 3个文字
//            CommonUtil.LogLa(2, " totHeight is " + totHeight + " footHeight is" + footHeight + " middleHeight is " + middleHeight + " klineHeight is" + klineHeight + " menuHeight is" + menuHeight + " now is " + (klineHeight + middleHeight + menuHeight + stepVolRow * i) + " stepVolRow is " + stepVolRow);
            canvas.drawLine(0.0f, klineHeight + middleHeight + menuHeight + stepVolRow * i + timeTextHeight, chartWidth, klineHeight + middleHeight + menuHeight + stepVolRow * i + timeTextHeight, linePaint);
        }
        // 最大
        drawText(canvas, formatNumber(priceDecimals, maxValue), 0.0f, fontVHeight, StockChartUtil.ZHANG);
        // 平均
        drawText(canvas, formatNumber(priceDecimals, (maxValue + minValue) / 2), 0.0f, stepChartRow * 2 + 8, StockChartUtil.GRAY);
        // 最小
        drawText(canvas, formatNumber(priceDecimals, minValue), 0.0f, klineHeight - 2f, StockChartUtil.DIE);
        calculateDisplayNum();
    }

    /**
     * 画时间
     *
     * @param canvas
     * @param text
     * @param x
     * @param y
     */
    public void drawTextTime(Canvas canvas, String text, float x, float y, Align align) {
        timePaint.setColor(StockChartUtil.M5_COLOR);
        timePaint.setTextAlign(align);
        canvas.drawText(text, x, y, timePaint);
    }

    /**
     * drawTextVol(canvas, "总:0.000", "M5:0.000", "M10:0.000");
     *
     * @param canvas
     * @param param1
     * @param param2
     * @param param3
     */
    public void drawTextVol(Canvas canvas, String param1, String param2, String param3) {
        float x = klineHeight + middleHeight * 3 / 4;
        drawText(canvas, param1, 0.0f, x, StockChartUtil.DIE);
        drawText(canvas, param2, textPaint.measureText(param1) + 5, x, StockChartUtil.M5_COLOR);
        drawText(canvas, param3, textPaint.measureText(param1 + param2) + 10, x, StockChartUtil.YELLOW);
    }

    /**
     * drawTextMACD(canvas, "MACD(12,26,9)", "DIF:0.000", "DEA:0.000",
     * "MACD:0.000");
     *
     * @param canvas
     * @param param1
     * @param param2
     * @param param3
     * @param param4
     */
    public void drawTextMACD(Canvas canvas, String param1, String param2, String param3, double param4) {
        float x = klineHeight + middleHeight * 3 / 4;
        drawText(canvas, param1, 0.0f, x, StockChartUtil.M5_COLOR);
        drawText(canvas, param2, textPaint.measureText(param1), x, StockChartUtil.M5_COLOR);
        drawText(canvas, param3, textPaint.measureText(param1 + param2) + 5, x, StockChartUtil.YELLOW);
        if (param4 <= 0) {
            drawText(canvas, "MACD:" + formatNumber(3, param4), textPaint.measureText(param1 + param2 + param3) + 10, x, StockChartUtil.DIE);
        } else {
            drawText(canvas, "MACD:" + formatNumber(3, param4), textPaint.measureText(param1 + param2 + param3) + 10, x, StockChartUtil.ZHANG);
        }

    }

    /**
     * drawTextMACD(canvas, "KDJ(9,3,3)", "K:0.000", "D:0.000", "J:0.000");
     *
     * @param canvas
     * @param param1
     * @param param2
     * @param param3
     * @param param4
     */
    public void drawTextKDJ(Canvas canvas, String param1, String param2, String param3, String param4) {
        float x = klineHeight + middleHeight * 3 / 4;
        drawText(canvas, param1, 0.0f, x, StockChartUtil.M5_COLOR);
        drawText(canvas, param2, textPaint.measureText(param1), x, StockChartUtil.M5_COLOR);
        drawText(canvas, param3, textPaint.measureText(param1 + param2) + 5, x, StockChartUtil.YELLOW);
        drawText(canvas, param4, textPaint.measureText(param1 + param2 + param3) + 10, x, StockChartUtil.PURPLE);
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
    public void drawText(final Canvas canvas, final String text, final float x, final float y, int color) {
        textPaint.setColor(color);
        canvas.drawText(text, x, y, textPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        chartWidth = measure(widthMeasureSpec);
        totHeight = measure(heightMeasureSpec);
        middleHeight = getFontHeight(textPaint);
        timeTextHeight = getFontHeight(textPaint);
        klineHeight = (totHeight - middleHeight) * 2 / 3;
        footHeight = totHeight - klineHeight - middleHeight - menuHeight - timeTextHeight;//这个25是时间的高度
        calculateDisplayNum();
//        CommonUtil.LogLa(4, "charHeight=" + klineHeight + "  totHeight=" + totHeight + " footHeight=" + footHeight + " menuHeight is " + menuHeight);
        setMeasuredDimension(chartWidth, totHeight);
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
     * 计算当前屏幕以当前蜡烛的宽度,和以当前的蜡烛宽度能显示多少天
     */
    public void calculateDisplayNum() {
        float pie = (defaultLevel - maxLevel / 2.0f) / (maxLevel / 2.0f);
        candlesWidth = (1 + pie) * chartWidth / 80.0f;
        spacing = (1 + pie) * 4;
        spanX = candlesWidth + spacing;
        displayNum = (int) (chartWidth / spanX);
        Log.d("kline", "calculateDisplayNum is " + chartWidth + " displayNum is " + displayNum);
        if (klineHeight != 0) spanKlineY = (float) (maxValue - minValue) / klineHeight;
        if (footHeight != 0) spanCjslY = (float) cjslMax / footHeight;
        if (footHeight != 0) spanMACDY = (float) (macdMax - macdMin) / footHeight;
        if (footHeight != 0) spanKDJY = (float) (kdjMax - kdjMin) / footHeight;
    }

    /**
     * 获取字所占高度
     *
     * @param textPaint
     * @return
     */
    public int getFontHeight(Paint textPaint) {
        FontMetrics fm = textPaint.getFontMetrics();
        return (int) Math.ceil(fm.bottom - fm.top) + 2;
    }

    /**
     *
     * @param dayInt  分钟线的数据是201907070505 其他的是20190707
     * @return
     */
    public String strIntToFormate(String dayInt) {
        try {
            if (dayInt == null) return "";
            if (dayInt.length() == DateUtils.TEMPLATE_yyyyMMdd.length()) {
                return DateUtils.getStringDateOfString(dayInt,DateUtils.TEMPLATE_yyyyMMdd,DateUtils.TEMPLATE_yyyyMMdd_divide);
//                return dayInt.substring(0, 4) + "-" + dayInt.substring(4, 6) + "-" + dayInt.substring(6, 8);
            }else if(dayInt.length() ==DateUtils.TEMPLATE_yyyyMMddHHmm.length()){
                return DateUtils.getStringDateOfString(dayInt,DateUtils.TEMPLATE_yyyyMMddHHmm,DateUtils.TEMPLATE_yyyyMMdd_HHmm);
            }


        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
        return "";
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

    public String formatNumber(final int i, final String value) {
        try {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
            return bd.toPlainString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * json中是否有该字段,并且该字段不为空
     *
     * @param json
     * @param name 字段名
     * @return
     * @throws JSONException
     */
    public boolean hasAndNotNull(JSONObject json, String name) throws JSONException {
        if (json != null && name != null) {
            return json.has(name) && !json.isNull(name) && //
                    json.getString(name) != null && !"".equals(json.getString(name));
        } else return false;
    }

    /**
     * json中是否有该字段,该字段不为空,并且为全数字 Long 类型可以使用
     *
     * @param json
     * @param name 字段名
     * @return
     * @throws JSONException
     */
    public boolean hasNotNullAndIsIntOrLong(JSONObject json, String name) throws JSONException {
        if (json != null && name != null) {
            return json.has(name) && !json.isNull(name) && //
                    json.getString(name) != null && !"".equals(json.getString(name)) && //
                    json.getString(name).matches("[0-9E]+$");
        } else return false;
    }

//    /**
//     * 关闭asyncTask
//     *
//     * @param task
//     */
//    public void cancelAsyncTask(AsyncTask<?, ?, ?> task) {
//        if (task != null && task.getStatus() != AsyncTask.Status.FINISHED) {
//            while (!task.isCancelled() && !task.cancel(true)) {
//                if (task.isCancelled()) break;
//                else task.cancel(true);
//            }
//        }
//    }

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
     * MACD 和 KDJ 的计算纵坐标值
     *
     * @param max   最大值
     * @param spanY 跨度值
     * @param value 当前值
     * @return
     */
    protected float getVolumePixelY(final double max, final double spanY, final double value) {
        if (spanY == 0.0) return 0.0f;
        return (float) ((max - value) / spanY);
    }

    /**
     * 计算成交量纵坐值
     *
     * @param value 当前成交量
     * @return 两位小数
     */
    protected float getVolumeY(final double max, final double spanY, final String value) {
        double result = 0.0f;
        try {
            if (spanY <= 0.0) return 0.0f;
            result = (max - Double.parseDouble(value)) / spanY;
            if (result == 0.0) result = 0.5;// 涨停线
            else if (Double.parseDouble(value) == 0) result = result + 5;// 跌停线
            return Float.parseFloat(formatNumber(2, result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }


}
