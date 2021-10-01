package com.procoin.widgets.quotitian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.interfaces.BaseRequestListener;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.quotitian.entity.MinTimeEntity;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.util.RxAsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by taojinroad on 16/8/24.
 */
public class StarMinuteLineChart extends ShareTimeView {

//    private final int ORANGECOLOR = Color.parseColor("#ff6b1b");
    private final int ORANGECOLOR = Color.rgb(99, 99, 99);
    public short[][] shortsTime; //开仓时间数组
    public double upPosiAvePri;  //多的开仓均价
    public double downPosiAvePri;//空的开仓均价
    private Context context;
    public volatile boolean isInit;//是否初始化过
    public volatile boolean isGet;
    public boolean isMaxSize = true;//显示大的时间字体
    public boolean isShowButtomTime;// 显示底部的时间

    private Path effectBgPath;
    private Paint effectPaint; //画虚线的
    protected Paint minLinePaint;
    public Path minLinePath;

    protected Paint maLinePaint; //画ma线
    private Path maLinePath; //ma路径
    protected Paint volLinePaint;
    private Paint timefontPaint;//时间的画笔

    private Paint rectavgPaint;// 画rect

    protected float preminX, preminY, premaX, premaY;
    protected boolean isMoveToMin;
    protected boolean isMoveToMa;
    private static final Object LOCK_OBJECT = new Object();
    /**
     * 坚线
     */
    protected Paint paint;
    private GestureDetector gestureDetector;// 手势
    private volatile boolean isShowWhiteLine;// 是否显示白线
    public int vLinePosition = 0;
    private float vLineX = 0, vLineY = 0;// 白竖线的X,y坐标
    private float left = 0, top = 0, right = 0, bottom = 0;// 白线背景框
    private RectF rectF = new RectF();// 设置个长方形

    public final LinkedList<MinTimeEntity> minTimeList = new LinkedList<MinTimeEntity>();
    private MinTimeEntity minTimeEntity;
    private final Handler mHandler = new Handler();
//    private int parCount;//

    public BaseRequestListener listener;//加载框
    public ReGetHisListen mReGetHisListen;
    private Runnable drawTask = new Runnable() {
        @Override
        public void run() {
            postInvalidate();
        }
    };
    private Runnable dateTask;//更新行情的runable
    private Runnable updateSizeTask;
    private Thread thread;

    @SuppressWarnings("deprecation")
    public StarMinuteLineChart(Context context) {
        super(context);
        isShowButtomTime = true;
        rows = 0;//8列
        setDecimal(6);
        setIsStock(true);
        setIsShowLeftTop(true);
        setIsShowLeftMid(true);
        setIsShowLeftBottom(true);
        setIsShowRight(false);
        setIsAmtOrVol(true);
        setHasTBSide(false);
        this.context = context;
        gestureDetector = new GestureDetector(new GestureListener());
        init();
    }

    private void init() {
        chartScale = (float) 23 / 30;//行情图的高度
//        chartBgColor = Color.WHITE;
//        chartBgColor = Color.parseColor("#f7f7fc");
        rectavgPaint = new Paint();
        rectavgPaint.setColor(Color.parseColor("#00a1f2"));
        rectavgPaint.setStyle(Paint.Style.STROKE);
        rectavgPaint.setStrokeWidth(1.5f);
        effectBgPath = new Path();
        //虚线的
        effectPaint = new Paint();
        effectPaint.setColor(ORANGECOLOR);//中间虚线颜色
        effectPaint.setStyle(Paint.Style.STROKE);
        effectPaint.setStrokeWidth(1.5f);
        effectPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 1));


        timefontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        timefontPaint.setAntiAlias(true); // 去掉锯齿
        timefontPaint.setTextSize(StockChartUtil.pxToSp(getResources(), 12));
        timefontPaint.setStyle(Paint.Style.FILL); // 设置paint的 style 为STROKE：实心心
        timefontPaint.setFlags(Paint.FAKE_BOLD_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        timefontPaint.setStrokeWidth(0.5f);

//        stockRateAndAmtParser = new StockRateAndAmtDomainParser();
//        resultDataParser = new ResultDataParser();
        minLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 分时线
        minLinePaint.setAntiAlias(true); // 去掉锯齿
        minLinePaint.setStyle(Paint.Style.STROKE);
        minLinePaint.setStrokeWidth(1.5f);
        minLinePaint.setColor(ORANGECOLOR);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);/* 去锯齿 */
        paint.setStrokeWidth(1.0f);
        paint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));
        paint.setTextAlign(Paint.Align.CENTER);
        minLinePath = new Path();// 分时线

        maLinePath = new Path();// 黄线
        //ma画笔
        maLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 均线
        maLinePaint.setStyle(Paint.Style.STROKE);
        maLinePaint.setAntiAlias(true); // 去掉锯齿
        maLinePaint.setStrokeWidth(1.0f);
        maLinePaint.setColor(StockChartUtil.YELLOW);

        //成交量画笔
        volLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 成交量线
        volLinePaint.setAntiAlias(true); // 去掉锯齿
        volLinePaint.setStrokeWidth(1.5f);
        volLinePaint.setColor(Color.rgb(0, 161, 242));//成交量颜色

        updateSpanXByMaxPoint(4800);
    }

    @Override
    public void drawBgItem(Canvas canvas) {

        //画水平的虚线

        //绘制中间的虚线
        effectBgPath.reset();
        effectBgPath.moveTo(0, chartHeight / 2);
        effectBgPath.lineTo(chartWidth, chartHeight / 2);
        effectPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 1));
        effectPaint.setColor(ORANGECOLOR);
        canvas.drawPath(effectBgPath, effectPaint);

        //这个是底部的线
        canvas.drawLine(0.5f, chartHeight, chartWidth, chartHeight, getGridPaint());

        float texty = chartHeight + (fontVHeight);
        if (!isMaxSize) {
            texty = chartHeight + (getFontHeight(timefontPaint) * 0.67f);
        }

//        canvas.drawLine(0.5f, chartHeight + (danMuHeight) + 1.5f, chartWidth, chartHeight + (danMuHeight) + 1.5f, getGridPaint());
        if (shortsTime != null) {
            int maxP = 0;
            for (int i = 0; i < shortsTime.length; i++) {
                short[] shortitem = shortsTime[i];
                if (i == shortsTime.length - 1) {
                    if (isMaxSize) {
                        drawText(canvas, getGoldTimeFormat(shortitem[0]), (spanX * maxP), texty, Color.parseColor("#a1a1a1"), true);//BC000000
                        drawText(canvas, getGoldTimeFormat(shortitem[1]), (spanX * maxPoint), texty, Color.parseColor("#a1a1a1"), false);
                    } else {
                        drawGoldTimeText(canvas, getGoldTimeFormat(shortitem[0]), (spanX * maxP), texty, Color.parseColor("#a1a1a1"), true);
                        drawGoldTimeText(canvas, getGoldTimeFormat(shortitem[1]), (spanX * maxPoint), texty, Color.parseColor("#a1a1a1"), false);
                    }
                } else {
                    if (isMaxSize) {
                        drawText(canvas, getGoldTimeFormat(shortitem[0]), (spanX * maxP), texty, Color.parseColor("#a1a1a1"), true);
                    } else {
                        drawGoldTimeText(canvas, getGoldTimeFormat(shortitem[0]), (spanX * maxP), texty, Color.parseColor("#a1a1a1"), true);
                    }
                    maxP = maxP + (shortitem[1] - shortitem[0]);
                    if (isMaxSize) {
                        drawText(canvas, getGoldTimeFormat(shortitem[1]) + "\\", (spanX * maxP), texty, Color.parseColor("#a1a1a1"), false);
                    } else {
                        drawGoldTimeText(canvas, getGoldTimeFormat(shortitem[1]) + "\\", (spanX * maxP), texty, Color.parseColor("#a1a1a1"), false);
                    }
                    canvas.drawLine((spanX * maxP), 0.5f, (spanX * maxP), chartHeight, getGridPaint());
                }

            }
        }
    }


    @Override
    public void drawShareTime(Canvas canvas) {
        if (!isGet) {
            synchronized (LOCK_OBJECT) {
//            createIndexTimeendLineAndTime(canvas);  //画
                drawMinTimeChart(canvas);
                // 画十字白线
                drawWhiteLine(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE: // 拖动白线一起移动
                if (isShowWhiteLine) {
                    calculateLinePosition(event.getX(), event.getY(), isShowWhiteLine);// 移动竖线
                    return true;
                }
                break;
            default:
                // 把白线消失
                if (isShowWhiteLine) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    vLinePosition = 0;
                    calculateLinePosition(event.getX(), event.getY(), false);// 移动竖线
                }
                break;
        }
        if (isShowWhiteLine) return true;
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
//            com.taojin.http.util.CommonUtil.LogLa(2, "GoodsMinuteLineChart onSingleTapConfirmed");
            callOnClick();
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
//            com.taojin.http.util.CommonUtil.LogLa(2, "GoodsMinuteLineChart onSingleTapUp");
            return super.onSingleTapUp(e);
        }


        @Override
        public boolean onDown(MotionEvent e) {
//            com.taojin.http.util.CommonUtil.LogLa(2, "GoodsMinuteLineChart onDown");
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
//            com.taojin.http.util.CommonUtil.LogLa(2, "GoodsMinuteLineChart onLongPress");
            // 长按时出现白线
            calculateLinePosition(e.getX(), e.getY(), true);
            getParent().requestDisallowInterceptTouchEvent(true);// 不拦截
        }
    }

    /**
     * 计算竖线位置
     */
    private void calculateLinePosition(float x, float y, boolean isShow) {
        synchronized (LOCK_OBJECT) {
            isShowWhiteLine = isShow;
            if (spanX == 0) return;
            int size = minTimeList.size();
            if (size == 0) return;
            float leftMaxX = minTimeList.get(0).x;
            float rightMaxX = minTimeList.get(size - 1).x;
            if (x < leftMaxX) {
                x = leftMaxX;
                vLinePosition = 0;
                CommonUtil.LogLa(2, "leftMaxX is " + leftMaxX + " x is " + x);
            } else if (x > rightMaxX) {
                x = rightMaxX;
                vLinePosition = size - 1;
                CommonUtil.LogLa(2, "rightMaxX is " + rightMaxX + " x is " + x);
            } else {
                int n = 0;
                for (int i = 0; i < minTimeList.size(); i++) {
                    MinTimeEntity item = minTimeList.get(i);
                    float span = Math.abs(x - item.x);
                    if (span <= spanX && span >= spanX / 2) {
                        CommonUtil.LogLa(2, "x is " + x + " item.x is " + item.x + " spanX is " + spanX);
                        n = i;
                        break;
                    }
                }
                if (n >= size) n = size - 1;
                vLinePosition = n;
            }
            CommonUtil.LogLa(2, "vLinePosition is " + vLinePosition);
            if (vLinePosition >= 0 && vLinePosition < size) {
                minTimeEntity = minTimeList.get(vLinePosition);
                vLineX = minTimeEntity.x;
                vLineY = minTimeEntity.minY;
                refreshDraw();
            }
        }
    }

    public void clearAllDate() {
        synchronized (LOCK_OBJECT) {
            minTimeList.clear();
            initDate();
            refreshDraw();
        }
    }

    /**
     * 刷新画布
     */
    public void refreshDraw() {
        mHandler.post(drawTask);
    }

    /**
     * 画白线
     *
     * @param canvas
     */
    private void drawWhiteLine(final Canvas canvas) {
        if (isShowWhiteLine) {
            paint.setColor(StockChartUtil.BULE);
            // 坚线
            canvas.drawLine(vLineX, 0, vLineX, totHeight, paint);
            // 横线
            canvas.drawLine(0, vLineY, chartWidth, vLineY, paint);
            // 画小圆点
            paint.setColor(Color.RED);
            canvas.drawCircle(vLineX, vLineY, 2.5f, paint);
            // 圆角边长方形
            if (minTimeEntity != null) {
                String vol = StockChartUtil.formatMoney(minTimeEntity.volOffset, 0, 2);// 成交量int
                String showZjcj = formatNumber(decimal, minTimeEntity.zjcj);
                int vhset = getFontHeight(paint);// 一行点高度
                float maxW = Math.max(paint.measureText(String.valueOf(minTimeEntity.timeFormat)), paint.measureText(showZjcj));
                maxW = Math.max(maxW, paint.measureText(String.valueOf(minTimeEntity.rate) + "%"));
                maxW = Math.max(maxW, paint.measureText(vol));
                paint.setColor(StockChartUtil.MINTIME_WHITELINE_BG);//
                paint.setStyle(Paint.Style.FILL);// 设置填满
                int rectX = 0;
                Paint.Align align = Paint.Align.LEFT;
                if (vLineX < 150) {
                    align = Paint.Align.RIGHT;
                    rectX = chartWidth;
                    left = chartWidth - maxW;
                    right = chartWidth;
                } else {
                    left = 0;
                    right = maxW;
                }
                top = vLineY - 2 * vhset;
                bottom = vLineY + 2 * vhset + vhset / 2;
                float tmpvLineY = vLineY;
//                float tmpvLineX = vLineX;
                if (top < 0) {
                    tmpvLineY = 2 * vhset;
                    top = 0;
                    bottom = tmpvLineY + 2 * vhset + vhset / 2;
                }
                if (bottom > chartHeight) {
                    tmpvLineY = chartHeight - 2 * vhset - vhset / 2;
                    bottom = chartHeight;
                    top = chartHeight - 4 * vhset - vhset / 2;
                }
                rectF.set(left, top, right, bottom);
                canvas.drawRoundRect(rectF, 4, 4, paint);
                // 添加信息
                paint.setColor(Color.WHITE);
                paint.setTextAlign(align);
                canvas.drawText(minTimeEntity.timeFormat, rectX, tmpvLineY - vhset, paint);
//                paint.setColor(StockChartUtil.getRateColor(minTimeEntity.rate));
                canvas.drawText(showZjcj, rectX, tmpvLineY, paint);
                canvas.drawText(String.valueOf(minTimeEntity.rate) + "%", rectX, tmpvLineY + vhset, paint);
                paint.setColor(StockChartUtil.CJSL_BG);
                canvas.drawText(vol, rectX, tmpvLineY + 2 * vhset, paint);
            }
        }
    }

    /**
     * 画分时线，黄线，成交量
     *
     * @param canvas
     */
    protected void drawMinTimeChart(final Canvas canvas) {
        float saclesize = (float) (totHeight - (chartHeight + danMuHeight)) / (totHeight - chartHeight);//有弹幕高度的时候,在这里需要
        preminX = 0.0f;
        preminY = 0.0f;
        premaX = 0.0f;
        premaY = 0.0f;
        isMoveToMin = true;
        isMoveToMa = true;
        rectPath.reset();
        minLinePath.reset();
        maLinePath.reset(); //黄线

        rectPath.moveTo(-2, chartHeight);
        rectPath.lineTo(-2, chartHeight / 2);
        for (MinTimeEntity entity : minTimeList) {
            // 分时Y坐标
            if (entity == null) return;
            if (entity.minY <= chartHeight && entity.minY >= 0) {
                if (preminX == 0 && preminY == 0) {//第一次的时候要平移过去
                    rectPath.lineTo(entity.x, chartHeight / 2);
                }
                preminX = entity.x;
                preminY = entity.minY;
                if (entity.x != 0 && entity.minY != 0) {
                    if (isMoveToMin) {
                        if (entity.x > Float.parseFloat(formatNumber(2, spanX))) {
//                            CommonUtil.LogLa(2, "isMoveToMin preminX>spanX is x " + entity.x + " y is " + entity.minY + "time is " + entity.time);
                            minLinePath.moveTo(-2, chartHeight / 2);
                            minLinePath.lineTo(entity.x, chartHeight / 2);
                            minLinePath.lineTo(entity.x, entity.minY);
                        } else {
//                            CommonUtil.LogLa(2, "isMoveToMin maLinePaint is x " + entity.x + " y is " + entity.minY + "time is " + entity.time);
                            minLinePath.moveTo(entity.x, entity.minY);
                        }
                        // minLinePath.moveTo(preminX, preminY);
                        isMoveToMin = false;

                    } else {
//                        CommonUtil.LogLa(2, "false maLinePaint is x " + entity.x + " y is " + entity.minY + "time is " + entity.time);
                        minLinePath.lineTo(entity.x, entity.minY);
                    }
                }
                rectPath.lineTo(entity.x, entity.minY);
            }
            // 黄线Y坐标
            if (entity.maY <= chartHeight && entity.maY >= 0) {
                if (entity.x != 0 && premaX != 0 && entity.maY != 0) {
                    if (isMoveToMa) {
                        maLinePath.moveTo(premaX, premaY);
                        isMoveToMa = false;
                    } else {
                        maLinePath.lineTo(entity.x, entity.maY);
                    }
                }
                premaX = entity.x;
                premaY = entity.maY;
            }
            // 成交量Y坐标
            if (entity.volY != 0)
                canvas.drawLine(entity.x, chartHeight + danMuHeight + (entity.volY * saclesize), entity.x, totHeight, volLinePaint);
        }

        rectPath.lineTo(preminX, chartHeight);

        // 这里是画ma线
        canvas.drawPath(maLinePath, maLinePaint);


        // TODO 增加了这里加入 渐变
        int size = minTimeList.size();
//        double endzjcj = 0;
        double rate = 1;//涨幅需要和昨日收盘价比较最新交易价
        if (minTimeList != null) {
            if (size > 0) {
                rate = minTimeList.get(size - 1).rate;
            }
        }
        int rectColor = StockChartUtil.getRateAlphaColorNoWhite(rate);
        updateLinearGradient(new LinearGradient(0, 0, 0, chartHeight, rectColor, rectColor, Shader.TileMode.MIRROR));
        canvas.drawPath(rectPath, rectPaint);
        int ractColor = StockChartUtil.getRateColorNoWhite(rate);
        minLinePaint.setColor(ractColor);
        canvas.drawPath(minLinePath, minLinePaint);
        // 画最后一个圆点
        paint.setColor(ractColor);//
        if (preminY == 0) preminY = chartHeight / 2 - 10;
        canvas.drawCircle(preminX, preminY, 3, paint);
//        drawavgAll(canvas);//画持仓线


    }

    public void startHisDateTask(final String jsonStr, StarProData starProData) {
        dateTask = new DateTask(jsonStr, starProData);
        if (thread != null) {
            try {
                thread.interrupt();
            } catch (Exception e) {

            } finally {
                thread = null;
            }
        }
        thread = new Thread(dateTask);
        thread.start();

    }

    /**
     * 解析json
     */
    public void parserJsonStock(final StarProData currPrice) throws Exception {
        synchronized (LOCK_OBJECT) {
//            CommonUtil.LogLa(2, "parserJsonStock is " + isGet + " currPrice is " + currPrice);
            if (isGet) return;
            if (currPrice != null) {
                yesValue = (float) currPrice.preClose;

                maxValue = Double.parseDouble(currPrice.high);
                minValue = Double.parseDouble(currPrice.low);
                int hhmm = getStringHHMMToInt(stryyyyMMddHHmmssToDate(currPrice.time));
                MinTimeEntity nowTimeEntity = new MinTimeEntity(hhmm, (float) currPrice.last);
                nowTimeEntity.vol = Float.parseFloat(currPrice.amount);
                float cjsl = Float.parseFloat(currPrice.amount);
                float cjje = (float) currPrice.balance;
                if (cjsl != 0) nowTimeEntity.ma = cjje / cjsl;
                if (isLostTime(nowTimeEntity)) {
                    if (mReGetHisListen != null)
                        mReGetHisListen.reGetHisListen();
                    return;
                }
                //
//                sortListAsc();
                //计算最大的交易量
//                int size = minTimeList.size();
//                float perVol = 0;
//                if (size >= 2) {
//                    perVol = minTimeList.get(size - 2).vol;
//                }

                //更新成交量
                maxAmtOrVolValue = Math.max(maxAmtOrVolValue, nowTimeEntity.vol - preAmtOrVolValue);
                //更新最大最小
                calculateMaxValueHome();
                reSetMinTimeAllXY();
                if (getVisibility() == VISIBLE) {
                    refreshDraw();
                }
            }
        }
    }


    /**
     * 如果超过5分钟那就要重新刷历史数据
     *
     * @param nowItem
     * @return
     */
    private boolean isLostTime(MinTimeEntity nowItem) {
        int size = minTimeList.size();
        MinTimeEntity minTimeEntity = null;
        if (size > 0) {
            minTimeEntity = minTimeList.get(size - 1);
        }
        if (minTimeEntity != null) {
            if (Math.abs(minTimeEntity.time - nowItem.time) >= 2) {
                CommonUtil.LogLa(2, "分时 isLostTime minTimeEntityTime is " + minTimeEntity.time + " nowItem is " + nowItem.time);
                return true;
            }
            if (minTimeEntity.time == nowItem.time) {
                minTimeEntity.zjcj = nowItem.zjcj;
                minTimeEntity.vol = nowItem.vol;
            } else {
                //如果获取到开盘5点50以上的数据，并且数据里面包含0点以后的数据，就重新刷新列表
                minTimeList.add(nowItem);
            }
        } else {
            minTimeList.add(nowItem);
        }
        return false;
    }


    /**
     *
     */
    public void startUpdateSizeTask() {

        if (updateSizeTask != null) {
            mHandler.removeCallbacks(updateSizeTask);
            updateSizeTask = null;
        }
        if (updateSizeTask == null) {
            updateSizeTask = new UpDateSizeTask();
        }
        mHandler.post(updateSizeTask);
    }


//    @Override
//    public void calculateMaxValueHome() {
//        if (maxValue != 0 && minValue != 0) {
//            if (Math.round(maxValue - minValue) <= 0.01f) {
//                maxValue = maxValue + 0.01f;
//                minValue = minValue - 0.01f;
//            }
////            yesValue = (maxValue + minValue) / 2;
//        }
//        if (yesValue == 0 || minValue == 0 || maxValue == 0 || minValue > 999999 || maxValue > 999999 || maxValue == minValue) {
//            minValue = yesValue - 1f;
//            maxValue = yesValue + 1f;
//        }
//    }

    /**
     * 重新计算所有坐标,前提list最好已按升序排序
     */
    private void reSetMinTimeAllXY() {
        if (chartHeight != 0) spanY = (maxValue - minValue) / chartHeight;
        if (totHeight != chartHeight)
            spanAmtOrVolY = maxAmtOrVolValue / (totHeight - chartHeight); // 每次差度
        double preVol = 0;
//        CommonUtil.LogLa(2, "reSetMinTimeAllXY max is " + maxPoint + " spanX is " + spanX + " 屏幕 宽度" + chartWidth);
        for (int i = 0; i < minTimeList.size(); i++) {
            MinTimeEntity entity = minTimeList.get(i);
//            CommonUtil.LogLa(2, "MinTimeEntity is " + entity.toString());
            entity.x = getTimePixeX(hhmmToshort(entity.time), spanX);
            // 计算分时Y坐标
            entity.minY = getCharPixelY(maxValue, spanY, entity.zjcj);

            // 计算黄线Y坐标
            entity.maY = StockChartUtil.getCharPixelY(maxValue, spanY, entity.ma);
            // 计算成交量Y坐标
            if (i == 0) {
                preVol = entity.vol; //第一个点可以过滤
            }
            entity.volOffset = entity.vol - preVol;
            entity.volY = StockChartUtil.getCharVolumeY(maxAmtOrVolValue, spanAmtOrVolY, entity.volOffset);
            if (entity.vol > 0) preVol = entity.vol;
            // 计算时间格式化，rate
            if (yesValue != 0) {
                entity.rate = formatNumberFloat(2, (entity.zjcj - yesValue) * 100 / yesValue);
            }
            entity.timeFormat = formatTime22(entity.time);
        }
    }

    /**
     * 根据时间换算当前的X坐标
     *
     * @param posTime 转换成已经从零分累加的时间
     * @param spanX
     * @return
     */
    public float getTimePixeX(final short posTime, final float spanX) {
        int pos = 0;
        try {
            if (shortsTime != null) {
                for (int i = 0; i < shortsTime.length; i++) {
                    short[] shortitem = shortsTime[i];
                    if (shortitem[0] <= posTime && shortitem[1] >= posTime) {
                        pos = pos + (posTime - shortitem[0]);
                        break;
                    } else {
                        pos = pos + (shortitem[1] - shortitem[0]);
                    }
                }
            }
//            com.tjr.perval.util.CommonUtil.LogLa(2, " pos is " + pos + " time is " + posTime + " result is " + Float.parseFloat(formatNumber(2, spanX * pos)));
            return Float.parseFloat(formatNumber(2, spanX * pos));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }


    /**
     * @param max
     * @param spanY
     * @param value
     * @return
     */
    public float getCharPixelY(final double max, final double spanY, final double value) {
        double result = 0.0f;
        try {
            if (spanY == 0.0) return 0.0f;
            result = (max - value) / spanY;
            if (result == 0.0) result = 2.0;// 涨停线
            else if (result >= max / spanY) result = max / spanY - 2;// 跌停线
            return Float.parseFloat(formatNumber(2, result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isShowButtomTime) {
            chartHeight = chartHeight - (int) (fontVHeight * 2);//底部字体的高度
        }
    }

    public interface ReGetHisListen {
        public void reGetHisListen();
    }

    private String getGoldTimeFormat(int time) {
        time = time % 1440; //一天的距离
        int hour = time / 60;
        int numter = time % 60;
        return String.format("%02d", hour) + ":" + String.format("%02d", numter);
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
    public void drawGoldTimeText(final Canvas canvas, final String text, final float x, final float y, int color, boolean isLeft) {
        timefontPaint.setColor(color);
        if (isLeft) {
            timefontPaint.setTextAlign(Paint.Align.LEFT);
        } else {
            timefontPaint.setTextAlign(Paint.Align.RIGHT);
        }
        canvas.drawText(text, x, y, timefontPaint);
        timefontPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

//    //持仓线
//    private void drawavgAll(Canvas canvas) {
//        //这里是持仓线
//        float upy = -1;
//        float downy = -1;
//        if (minValue <= (float) upPosiAvePri && (float) upPosiAvePri <= maxValue) {
//            upy = getCharPixelY(maxValue, spanY, (float) upPosiAvePri);
//        }
//        if (minValue <= (float) downPosiAvePri && (float) downPosiAvePri <= maxValue) {
//            downy = getCharPixelY(maxValue, spanY, (float) downPosiAvePri);
//        }
//
//        boolean isNeedUp = upy <= chartHeight && upy >= 0;
//        boolean isNeedDown = downy < chartHeight && downy >= 0;
//
//        if (!isNeedUp && !isNeedDown) {
//            return;//不需要画
//        }
//
//        float avgfontheight = getFontHeight(getFontPaint()) * 2 / 3;
//        float textupy = upy;
//        float textdowny = downy;
//        if (upy < avgfontheight) {
//            textupy = upy + avgfontheight;
//        }
//        if (downy < avgfontheight) {
//            textdowny = downy + avgfontheight;
//        }
//
//        String upavgText = "买涨:" + StockChartUtil.formatNumber(decimal, upPosiAvePri);
//        String downavgText = "买跌:" + StockChartUtil.formatNumber(decimal, downPosiAvePri);
//        float tmpupx = 0;
//        float tmpdownx = 0;
//        if (minTimeList.size() < maxPoint / 2) {
//            tmpupx = chartWidth - getFontPaint().measureText(upavgText);
//            tmpdownx = chartWidth - getFontPaint().measureText(downavgText);
//        }
//        if (upPosiAvePri > 0 && downPosiAvePri > 0) {//兩個數也都有
//            if (Math.abs(upy - downy) <= avgfontheight) {//如果2个重叠
//                if (upPosiAvePri <= downPosiAvePri) {
//                    if (upy < avgfontheight) {
//                        drawavgitem(canvas, tmpupx, upy, textupy, upavgText, true);
//                        drawavgitem(canvas, tmpdownx, downy, textupy + avgfontheight + 4f, downavgText, false);
//                    } else {
//                        drawavgitem(canvas, tmpupx, upy, upy, upavgText, true);
//                        drawavgitem(canvas, tmpdownx, downy, downy + avgfontheight + 4f, downavgText, false);
//                    }
//                } else {
//                    if (downy < avgfontheight) {
//                        drawavgitem(canvas, tmpdownx, downy, textdowny, downavgText, false);
//                        drawavgitem(canvas, tmpupx, upy, textdowny + avgfontheight + 4f, upavgText, true);
//                    } else {
//                        drawavgitem(canvas, tmpdownx, downy, downy, downavgText, false);
//                        drawavgitem(canvas, tmpupx, upy, upy + avgfontheight + 4f, upavgText, true);
//                    }
//                }
//            } else {
//                drawavgitem(canvas, tmpupx, upy, textupy, upavgText, true);
//                drawavgitem(canvas, tmpdownx, downy, textdowny, downavgText, false);
//            }
//
//        } else if (upPosiAvePri > 0) {
//            drawavgitem(canvas, tmpupx, upy, textupy, upavgText, true);
//
//        } else if (downPosiAvePri > 0) {
//            drawavgitem(canvas, tmpdownx, downy, textdowny, downavgText, false);
//        }
//    }
//
//
//    private void drawavgitem(Canvas canvas, float linex, float liney, float texty, String text, boolean isBuy) {
//        int unit = 2;
//        float avgfontheight = getFontHeight(getFontPaint()) * 3 / 4;
//        int color = Color.parseColor("#ec6338");
//
//        if (!isBuy) {
//            color = Color.parseColor("#37d270");
//        } else {
//        }
//        Path upPath = new Path();
//        upPath.moveTo(0, liney);
//        upPath.lineTo(chartWidth, liney);
//        effectPaint.setColor(color);
//        canvas.drawPath(upPath, effectPaint);
//
//        rectavgPaint.setColor(color);
//        RectF effRectF = new RectF();
//        if (linex < chartWidth / 2) {
//            effRectF.set(unit, texty - avgfontheight - unit, getFontPaint().measureText(text) + (unit * 2), texty - unit);
//            drawText(canvas, text, linex + unit, texty - (unit * 2), color, true);
//        } else {
//            effRectF.set(chartWidth - getFontPaint().measureText(text) - (unit * 2), texty - avgfontheight - unit, chartWidth - unit, texty - unit);
//            drawText(canvas, text, linex - unit, texty - (unit * 2), color, true);
//        }
//
//        canvas.drawRoundRect(effRectF, 4, 4, rectavgPaint);
//    }


    /**
     * 获取现在时分秒时间合并成int
     *
     * @param date 格式:HHmm
     * @return int
     */
    public int getStringHHMMToInt(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        String dateString = formatter.format(date);
        return Integer.parseInt(dateString);
    }

    /**
     * HHmm 转换成0累加模式
     *
     * @param time
     * @return
     */
    private short hhmmToshort(int time) {
        int hour = time / 100;
        int numter = time % 100;
        return (short) ((hour * 60) + numter);
    }

    private class UpDateSizeTask implements Runnable {

        public void run() {
            try {
                synchronized (LOCK_OBJECT) {
                    calculateMaxValueHome();
                    reSetMinTimeAllXY();
                }
            } catch (Exception e) {
            } finally {
                refreshDraw();
            }
        }
    }


    private class DateTask implements Runnable {
        private String jsonStr;
        private StarProData starProData;

        public DateTask(String jsonStr, StarProData starProData) {
            this.jsonStr = jsonStr;
            this.starProData = starProData;

        }

        public void run() {
            long time1 = System.currentTimeMillis();
            isGet = true;
            try {
                initDate();//清空历史
                Log.d("MinTimeEntity","jsonStr=="+jsonStr);
                if (!TextUtils.isEmpty(jsonStr)) {


                    isInit = true;
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    yesValue = (double) starProData.preClose;
                    maxValue = Double.parseDouble(starProData.high);
                    minValue = Double.parseDouble(starProData.low);

                    int taskMaxPoint = 0;
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "quote_hhmm")) {
                        JSONArray qArray = jsonObject.getJSONArray("quote_hhmm");
                        CommonUtil.LogLa(2, "itemTime qArray.length() " + qArray.length());
                        if (qArray.length() % 2 == 0) {//是偶数才能进入
                            shortsTime = new short[qArray.length() / 2][2];
                            short firstTime = Short.parseShort(qArray.getString(0));
                            for (int i = 0; i < qArray.length(); i++) {
                                short itemTime = Short.parseShort(qArray.getString(i));
                                if (itemTime < firstTime) {
                                    itemTime = (short) (2400 + itemTime);
                                }
                                if (i % 2 == 0) {//
                                    shortsTime[i / 2][0] = hhmmToshort(itemTime);
                                    CommonUtil.LogLa(2, "0 itemTime is " + itemTime + " 计算后 " + shortsTime[i / 2][0]);
                                } else {//
                                    shortsTime[i / 2][1] = hhmmToshort(itemTime);
                                    CommonUtil.LogLa(2, "1 itemTime is " + itemTime + " 计算后 " + shortsTime[i / 2][1]);
                                    //计算最大值
//                                    taskMaxPoint = taskMaxPoint + (shortsTime[i / 2][1] - hhmmToshort(shortsTime[i / 2][0]));
                                }
                            }

                            for (int i = 0; i < shortsTime.length; i++) {
                                short[] shortitem = shortsTime[i];
                                taskMaxPoint = taskMaxPoint + (shortitem[1] - shortitem[0]);
                            }

                            CommonUtil.LogLa(2, "taskMaxPoint " + taskMaxPoint);
                            if (maxPoint != taskMaxPoint) {
                                maxPoint = taskMaxPoint;
                                spanX = (float) chartWidth / maxPoint;//重新计算下span
                            }
                        }
                    }
                    Log.d("MinTimeEntity","==========");

                    synchronized (LOCK_OBJECT) {
                        minTimeList.clear();
                        calculateMaxValueHome();
                        Log.d("MinTimeEntity","jsonObject=="+jsonObject.toString());
                        if (JsonParserUtils.hasAndNotNull(jsonObject, "min_" + starProData.symbol)) {
                            List<MinTimeEntity> bakList = parserGoodsMinuteDataHisSetList(jsonObject.getString("min_" + starProData.symbol));
                            if (bakList != null) minTimeList.addAll(bakList);
                        }
                        sortListAsc();
                        //计算最大的交易量
                        calculateVolMaxValueFromList();
                        reSetMinTimeAllXY();
                    }
                }
            } catch (Exception e) {
//                CommonUtil.LogLa(2, "Exception is " + e.getMessage());
            } finally {
                isGet = false;
                refreshDraw();
            }
        }
    }

    /**
     * 计算成交量相差值最大值
     */
    private void calculateVolMaxValueFromList() {
        double max;
        boolean isset = false;
        for (MinTimeEntity entity : minTimeList) {
            if (!isset) {
                maxAmtOrVolValue = entity.vol;
                preAmtOrVolValue = entity.vol;
                isset = true;
                continue;
            }
            if (entity.vol > 0) {
                max = entity.vol - preAmtOrVolValue;
                if (maxAmtOrVolValue < max) {
                    maxAmtOrVolValue = max;
                }
                preAmtOrVolValue = entity.vol;
            }
        }
    }

    private class HisDataTask extends RxAsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Boolean call(String... strings) {


            return true;
        }

    }


    /**
     * @param content
     * @return
     * @throws Exception
     */
    public List<MinTimeEntity> parserGoodsMinuteDataHisSetList(final String content) throws Exception {
        // 时间(HHmm)1,当前价2,成交量3;
        Log.d("MinTimeEntity","content=="+content);
//        final String regex2 = "([0-9]+),([0-9]*[.]??[0-9]*),([0-9]+),([0-9]*[.]??[0-9]*);"; // 总
        final String regex2 = "([0-9]+),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*);"; // 总

//        Log.d("MinTimeEntity","1111111");
//        Log.d("MinTimeEntity","2222222222");
//        Log.d("MinTimeEntity","3333333");
//        Log.d("MinTimeEntity","44444444");
//        Log.d("MinTimeEntity","555555");
        final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
        final Matcher ma2 = pa2.matcher(content);
        List<MinTimeEntity> bakList = new ArrayList<MinTimeEntity>();
        short firstTime = 0;
        if (shortsTime != null) {
            firstTime = shortsTime[0][0];
        }
        int time=0;
        while (ma2.find()) {
            if (ma2.groupCount() == 4) {
                if (ma2.group(1).matches(CommonConst.INTMATCHES) && ma2.group(2).matches(CommonConst.FLOATMATCHES) && ma2.group(3).matches(CommonConst.FLOATMATCHES) && ma2.group(4).matches(CommonConst.FLOATMATCHES)) {
                    MinTimeEntity item = new MinTimeEntity();
                    time=Integer.parseInt(DateUtils.getStringDateOfString2(ma2.group(1),DateUtils.TEMPLATE_HHmm2));
                    Log.d("MinTimeEntity","time=="+time);
//                    time = Integer.parseInt(ma2.group(1));
                    if (time < firstTime) time = time + 2400;//现货行情22小时,多加24小时
                    item.time = time;
                    item.zjcj = Float.parseFloat(ma2.group(2));
                    item.vol = Double.parseDouble(ma2.group(3));
                    double cjje = Double.parseDouble(ma2.group(4));
                    if (item.vol != 0) item.ma = (double) (cjje / item.vol);
                    bakList.add(item);
                }
            }
        }
        return bakList;
    }

    /**
     * 升序排列
     */
    private void sortListAsc() {
        // 升序计算K线
        Collections.sort(minTimeList, new Comparator<MinTimeEntity>() {
            @Override
            public int compare(MinTimeEntity s1, MinTimeEntity s2) {
                return s1.time - s2.time;
            }
        });
    }

}
