package com.procoin.widgets.quotitian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Shader;
import android.view.View;

import com.procoin.util.StockChartUtil;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class ShareTimeView extends View {
    public int chartBgColor = StockChartUtil.CHART_BG;// 图的背景色
    public int fontLeftTopColor = StockChartUtil.ZHANG; // 图表左上方的字体颜色
    private int fontLeftMidColor = StockChartUtil.GRAY; // GRAY 图表左中间的字体颜色
    public int fontLeftBottomColor = StockChartUtil.DIE; // 图表左下方的字体颜色

    public float chartScale = (float) 2 / 3; //行情图的比例
    public int decimal = 6; // 小数保留位数
    private int columns = 4; // 默认4列
    public int rows = 4; // 默认4行
    public float fontVHeight = 25f;
    private boolean hasTBSide;// 　是否保留上下在两条钱（现在是是否画 田字格）
    private boolean hasLRSide;// 是否保留最左右两边的竖线,hasLRSide=true代表要两边，hasLRSide=false就不要两边
    private boolean isStock; // isStock=true代表是个股，否则是指数股票
    private boolean isAmtOrVol; // isAmtOrVol是否画成交额或成交量图

    private boolean isShowLeftTop; // 是否显示左边上角的值
    private boolean isShowLeftMid; // 是否显示左边中间的值
    private boolean isShowLeftBottom; // 是否显示左边下角的值
    private boolean isShowRight; // 是否显示右边上下角的值

    public int chartHeight = 0; // 代表分时图所属的高度
    public int chartWidth = 0; // 代表图的宽度
    public int totHeight = 0; // 代表整个画布的高度
    public int danMuHeight = 0; // 代表弹幕按钮的高度
    //	public int volHeight = 0; // 代表交易量高度
    private float stepColumn; // 每列的跨度，
    private float stepRow; // 行的跨度
    private double rate;
    public float maxPoint = 240.0f;//最大份数
    public float spanX; // X坐标跨度值
    public double spanY; // Y坐标跨度值
    public double maxValue; // 最大值
    public double yesValue; // 昨收值
    public double minValue; // 最小值
    public double spanAmtOrVolY; // 成交额或成交量Y坐标跨度值
    public double maxAmtOrVolValue; // 当前成交额或成交量最大值
    public double preAmtOrVolValue; // 前一次成交额或成交量
    public double maxRate, minRate;

    private Paint gridPaint;// 画表格的线专用横线
    private Paint gridMidPaint; // 画表格的线专用的中间和底部用，美工专门要突出
    private Paint fontPaint;// 字体颜色
    // private Paint effectLinePaint;//画表格的虚线专用
    // private PathEffect effect;//虚线
    // private Path effectPath;

    public Paint rectPaint;// 半透明区域
    public Path rectPath;


//    public String jc="";
//    public boolean showJc;//新版首页用到,左上角上面价格后面加一个jc

    public ShareTimeView(Context context) {
        super(context);
        gridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridPaint.setAntiAlias(true); // 去掉锯齿
        gridPaint.setColor(Color.rgb(91, 91, 91));// 线的颜色
        gridPaint.setStrokeWidth(1.0f);

        gridMidPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        gridMidPaint.setAntiAlias(true); // 去掉锯齿
        gridMidPaint.setColor(Color.rgb(212, 215, 217));// 线的颜色

        gridMidPaint.setStrokeWidth(1.0f);
        // effectLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // effectLinePaint.setAntiAlias(true); // 去掉锯齿
        // effectLinePaint.setStyle(Paint.Style.STROKE);//低版本时要设置画虚线
        // effectLinePaint.setColor(Color.rgb(75, 75, 75));//
        // effectLinePaint.setStrokeWidth(1.4f);
        // effect = new DashPathEffect(new float[]{5,5},1);
        // effectLinePaint.setPathEffect(effect);
        // effectPath = new Path();
        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setAntiAlias(true); // 去掉锯齿
        fontPaint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));
        fontPaint.setStyle(Paint.Style.FILL); // 设置paint的 style 为STROKE：实心心
        fontPaint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        fontPaint.setStrokeWidth(0.5f);
        fontVHeight = getFontHeight(fontPaint) * 0.67f;// 一行点高度

        rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 半透明区域
        rectPaint.setAntiAlias(true); // 去掉锯齿
        rectPaint.setStyle(Paint.Style.FILL);
        rectPaint.setStrokeWidth(0f);
        LinearGradient gradient = new LinearGradient(0, 0, 0, 0, Color.argb(0, 0, 0, 0), Color.argb(0, 0, 0, 0), Shader.TileMode.MIRROR);
        rectPaint.setShader(gradient);
        rectPath = new Path();// 画半透明的区域
        // getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    public void initDate() {
        maxValue = 0.0f; // 最大值
        yesValue = 0.0f; // 昨收值
        minValue = 0.0f; // 最小值
//        jc="";
        spanY = 0.0f; // Y坐标跨度值
        spanAmtOrVolY = 0.0f; // 成交额或成交量Y坐标跨度值
        maxAmtOrVolValue = 0.0f; // 当前成交额或成交量最大值
        preAmtOrVolValue = 0.0f; // 前一次成交额或成交量
        maxRate = 0.0f;
        minRate = 0.0f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 初始化
        canvas.drawColor(chartBgColor); // 背景颜色
//        canvas.drawRGB(51,51,51);
        drawBgItem(canvas);//背景item
        if (hasTBSide) createShareTimeCoordinate(canvas);
        else createShareTimeCoordinateHome(canvas);
        if (isShowLeftTop) drawTextOnLeftTop(canvas); // 画左上角的值
        if (isShowLeftMid) drawTextOnLeftMid(canvas);// 画中间值
        if (isShowLeftBottom) drawTextOnLeftBottom(canvas);// 画左下角值
        if (isShowRight) drawTextOnRightTopAndBottom(canvas); // 画右上角右下角百分值
        drawShareTime(canvas);
    }

    /**
     * 背景item画图
     *
     * @param canvas
     */
    public void drawBgItem(Canvas canvas) {

    }

    /**
     * 计算最大值和最小值
     */
    public void calculateMaxValue() {
        if (yesValue == 0 || minValue == 0 || maxValue == 0 || minValue > 999999 || maxValue > 999999) {
            minValue = yesValue - yesValue * 0.01f;
            maxValue = yesValue + yesValue * 0.01f;
        } else {
            maxRate = Math.abs(maxValue - yesValue) / yesValue;
            minRate = Math.abs(yesValue - minValue) / yesValue;
            //
            //
            //

            if (maxRate <= 0.01 && minRate <= 0.01) {
                minValue = yesValue - yesValue * 0.01f;
                maxValue = yesValue + yesValue * 0.01f;
            } else {
                if (maxRate >= minRate) {
                    maxValue = yesValue + yesValue * maxRate;
                    minValue = yesValue - yesValue * maxRate;
                } else {
                    minValue = yesValue - yesValue * minRate;
                    maxValue = yesValue + yesValue * minRate;
                }
            }
        }
    }

    /**
     * 计算最大值和最小值
     */
    public void calculateMaxValueHome() {
        if (yesValue == 0 || minValue == 0 || maxValue == 0 || minValue > 999999 || maxValue > 999999) {
            minValue = yesValue - yesValue * 0.01f;
            maxValue = yesValue + yesValue * 0.01f;
        } else {
            maxRate = Math.abs(maxValue - yesValue) / yesValue;
            minRate = Math.abs(yesValue - minValue) / yesValue;
            if (maxRate >= minRate) {
                maxValue = yesValue + yesValue * maxRate;
                minValue = yesValue - yesValue * maxRate;
            } else {
                minValue = yesValue - yesValue * minRate;
                maxValue = yesValue + yesValue * minRate;
            }
        }
    }

    /**
     * 画分时图
     *
     * @param canvas
     */
    public abstract void drawShareTime(Canvas canvas);

    /**
     * 更新线性渐变位置
     */
    public void updateLinearGradient(LinearGradient gradient) {
        if (gradient == null)
            gradient = new LinearGradient(0, chartHeight, 0, 0, StockChartUtil.TIME_COLOR_1, StockChartUtil.TIME_COLOR_2, Shader.TileMode.MIRROR);
        rectPaint.setShader(gradient);
    }

    /**
     * 更新线性渐变位置
     */
    public void updateLinearGradient() {
        updateLinearGradient(null);
    }

    /**
     * 绘制分时图表格
     *
     * @param canvas 当前画布
     *               //@param columns 列数
     *               //@param rows    行数
     *               //@param lrSide  是否除掉两边 true代表不要两边，false就要两边
     */
    private void createShareTimeCoordinate(final Canvas canvas) {
        if (rows > 0) stepRow = (float) chartHeight / rows; // 计算每行的跨度
        if (columns > 0) stepColumn = (float) chartWidth / columns; // 计算每列的跨度
        if (!hasLRSide) { // 去掉两边
            if (isAmtOrVol) {
                canvas.drawLine(0.5f, 0.5f, chartWidth, 0.5f, gridPaint);
                canvas.drawLine(0.5f, chartHeight, chartWidth, chartHeight, gridPaint);
                if (danMuHeight > 0)
                    canvas.drawLine(0.5f, chartHeight + danMuHeight, chartWidth, chartHeight + danMuHeight, gridPaint);// 交易量顶部交易线
                canvas.drawLine(0.5f, totHeight, chartWidth, totHeight, gridPaint);
            } else {
                canvas.drawLine(0.5f, 0.5f, chartWidth, 0.5f, gridPaint);
                canvas.drawLine(0.5f, chartHeight, chartWidth, chartHeight, gridPaint);
            }
        } else {
            if (isAmtOrVol) {
                canvas.drawRect(0.5f, 0.5f, chartWidth, totHeight, gridPaint);
            } else {
                canvas.drawRect(0.5f, 0.5f, chartWidth, chartHeight, gridPaint);
            }
        }
        for (int j = 1; j < columns; j++) { // 画columns列线
            if (isAmtOrVol) {
                if (danMuHeight > 0) {
                    canvas.drawLine(stepColumn * j, 0.0f, stepColumn * j, chartHeight, gridPaint);
                    canvas.drawLine(stepColumn * j, chartHeight + danMuHeight, stepColumn * j, totHeight, gridPaint);
                } else {
                    canvas.drawLine(stepColumn * j, 0.0f, stepColumn * j, totHeight, gridPaint);
                }
            } else {
                canvas.drawLine(stepColumn * j, 0.0f, stepColumn * j, chartHeight, gridPaint);
            }
        }
        // 交易量横线
        if (isAmtOrVol) {
            canvas.drawLine(0.5f, totHeight - (totHeight - chartHeight - danMuHeight) / 2, chartWidth, totHeight - (totHeight - chartHeight - danMuHeight) / 2, gridPaint);// gridPaint

            // effectPath.reset();
            // effectPath.moveTo(0.5f, totHeight - (totHeight - chartHeight) /
            // 2);
            // effectPath.lineTo(chartWidth, totHeight - (totHeight -
            // chartHeight) / 2);
            // canvas.drawPath(effectPath, effectLinePaint);
        }
        // 画分时上面3个横线
        for (int i = 1; i < rows; i++) {
            if (i % 2 == 0) {
                canvas.drawLine(0.0f, stepRow * i, chartWidth, stepRow * i, gridMidPaint);
            } else {
                canvas.drawLine(0.0f, stepRow * i, chartWidth, stepRow * i, gridPaint);
            }
        }
    }

    private void createShareTimeCoordinateHome(final Canvas canvas) {
        if (rows > 0) stepRow = (float) chartHeight / rows; // 计算每行的跨度
        // 画横线
        for (int i = 1; i < rows; i++) {
            canvas.drawLine(0.0f, stepRow * i, chartWidth, stepRow * i, gridPaint);
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
    public void drawText(final Canvas canvas, final String text, final float x, final float y, int color, boolean isLeft) {
        fontPaint.setColor(color);
        if (isLeft) {
            fontPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            fontPaint.setTextAlign(Paint.Align.RIGHT);
        }
        canvas.drawText(text, x, y, fontPaint);
        fontPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    /**
     * 画左上角的值
     *
     * @param
     */
    public void drawTextOnLeftTop(Canvas canvas) {
        if (isStock) {
            drawText(canvas, formatNumber(decimal, maxValue), 0.0f, fontVHeight, fontLeftTopColor, true);
        } else {
//            CommonUtil.LogLa(2, "getFontPaint start" + getFontPaint().measureText(formatNumber(0, maxValue)) + " " + maxValue);
            drawText(canvas, formatNumber(0, maxValue), 0.0f, fontVHeight, fontLeftTopColor, true);
        }
    }

    /**
     * 画左边中间值的值
     *
     * @param //topValue
     */
    public void drawTextOnLeftMid(Canvas canvas) {
        if (isStock) {
            drawText(canvas, formatNumber(decimal, yesValue), 0.0f, chartHeight / 2 + 7, fontLeftMidColor, true);// 画中间值
        } else {
            drawText(canvas, formatNumber(0, yesValue), 0.0f, chartHeight / 2 + 7, fontLeftMidColor, true);// 画中间值
        }
    }

    /**
     * 画左边左下角值
     *
     * @param //topValue
     */
    public void drawTextOnLeftBottom(Canvas canvas) {
        if (isStock) {
            drawText(canvas, formatNumber(decimal, minValue), 0.0f, chartHeight - 1.5f, fontLeftBottomColor, true);// 画左下角值
        } else {
            drawText(canvas, formatNumber(0, minValue), 0.0f, chartHeight - 1.5f, fontLeftBottomColor, true);// 画左下角值
        }
    }

    /**
     * 画右边上角和下角的值
     *
     * @param //topValue
     */
    public void drawTextOnRightTopAndBottom(Canvas canvas) {
        if (yesValue == 0.0) {
            drawText(canvas, "1.00%", chartWidth, fontVHeight, StockChartUtil.ZHANG, false);
            drawText(canvas, "1.00%", chartWidth, chartHeight - 1.5f, StockChartUtil.DIE, false);// 画左下角值
        } else {
            rate = Math.abs(maxValue - yesValue) * 100 / yesValue;
            drawText(canvas, formatNumber(2, rate) + "%", chartWidth, fontVHeight, StockChartUtil.ZHANG, false);
            drawText(canvas, formatNumber(2, rate) + "%", chartWidth, chartHeight - 1.5f, StockChartUtil.DIE, false);// 画左下角值
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        chartWidth = measure(widthMeasureSpec);
        totHeight = measure(heightMeasureSpec);
        if (isAmtOrVol) {
            chartHeight = (int) (totHeight * chartScale);
        } else {
            chartHeight = totHeight;
        }
        spanX = (float) chartWidth / maxPoint;
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

    public void setDecimal(int decimal) {
        this.decimal = decimal;
    }

    public void updateSpanXByMaxPoint(float maxPoint) {
        this.maxPoint = maxPoint;
        spanX = chartWidth / maxPoint;
    }

    public void setHasLRSide(boolean hasLRSide) {
        this.hasLRSide = hasLRSide;
    }

    public void setIsStock(boolean isStock) {
        this.isStock = isStock;
    }

    public boolean isStock() {
        return isStock;
    }

    public void setIsAmtOrVol(boolean isAmtOrVol) {
        this.isAmtOrVol = isAmtOrVol;
    }

    public void setIsShowLeftTop(boolean isShowLeftTop) {
        this.isShowLeftTop = isShowLeftTop;
    }

    public void setIsShowLeftMid(boolean isShowLeftMid) {
        this.isShowLeftMid = isShowLeftMid;
    }

    public void setIsShowLeftBottom(boolean isShowLeftBottom) {
        this.isShowLeftBottom = isShowLeftBottom;
    }

    public void setIsShowRight(boolean isShowRight) {
        this.isShowRight = isShowRight;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public void setHasTBSide(boolean hasTBSide) {
        this.hasTBSide = hasTBSide;
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
            return bd.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    public float formatNumberFloat(final int i, final double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
            return bd.floatValue();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String formatTime(int time) {
        try {
            if (time > 1500) time = 1500;
            StringBuffer buf = new StringBuffer(String.valueOf(time));
            if (buf.length() == 3) {
                buf.insert(0, "0").insert(2, ":");
            } else if (buf.length() == 4) {
                buf.insert(2, ":");
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 22小时
     *
     * @param time
     * @return
     */
    public String formatTime22(int time) {
        try {
            if (time > 2400) time = time - 2400;
            StringBuffer buf = new StringBuffer(String.valueOf(time));
            if (buf.length() == 1) {
                buf.insert(0, "000").insert(2, ":");
            } else if (buf.length() == 2) {
                buf.insert(0, "00").insert(2, ":");
            } else if (buf.length() == 3) {
                buf.insert(0, "0").insert(2, ":");
            } else if (buf.length() == 4) {
                buf.insert(2, ":");
            }
            return buf.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将yyyyMMddHHmmss格式字符串转换为时间
     *
     * @param strDate
     * @return
     */
    public Date stryyyyMMddHHmmssToDate(String strDate) {
        try {

            SimpleDateFormat formatter;
            if (strDate.length() == 6) {
                formatter = new SimpleDateFormat("HHmmss");
            } else {
                formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            }
            ParsePosition pos = new ParsePosition(0);
            return formatter.parse(strDate, pos);
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }
    }

    public int getStringHHMMToInt(Date date) {
        if (date == null) return -1;
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String dateString = formatter.format(date);
        return Integer.parseInt(dateString);
    }

    /**
     * 得到现在手机时间
     *
     * @return
     */
    public Date getNow() {
        Date currentTime = new Date();
        return currentTime;
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

    public void setChartBgColor(int chartBgColor) {
        this.chartBgColor = chartBgColor;
    }

    public Paint getGridPaint() {
        return gridPaint;
    }

    public Paint getFontPaint() {
        return fontPaint;
    }

    public void setFontLeftTopColor(int fontLeftTopColor) {
        this.fontLeftTopColor = fontLeftTopColor;
    }

    public void setFontLeftMidColor(int fontLeftMidColor) {
        this.fontLeftMidColor = fontLeftMidColor;
    }

    public void setFontLeftBottomColor(int fontLeftBottomColor) {
        this.fontLeftBottomColor = fontLeftBottomColor;
    }
}
