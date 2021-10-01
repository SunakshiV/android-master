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
import com.procoin.util.JsonParserUtils;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.quotitian.entity.MinTimeEntity;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.http.util.NotificationsUtil;
import com.procoin.task.BaseAsyncTask;
import com.procoin.util.VeDate;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by taojinroad on 16/8/24.
 */
public class StarMinuteHourLineChart extends ShareTimeView {

    private final int LASTSPACESIZE = 10;//这里是后面空出来的空间
    private final int ORANGECOLOR = Color.rgb(99, 99, 99);
    //    private StockRateAndAmtDomainParser stockRateAndAmtParser;
//    private ResultDataParser resultDataParser;
    public double upPosiAvePri;  //多的持仓均价
    public double downPosiAvePri;//空的持仓均价
    private Context context;
    public volatile boolean isGet = true,isInit = false;
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
    public final LinkedList<Float> minEventlist = new LinkedList<Float>();
    private MinTimeEntity minTimeEntity;
    private final Handler mHandler = new Handler();

//    public short[][] shortsTime;

    public BaseRequestListener listener;//加载框
    public ReGetHisListen mReGetHisListen;
    private Runnable drawTask = new Runnable() {
        @Override
        public void run() {
            // Auto-generated method stub
            postInvalidate();
        }
    };
    private GoodsMinHisTask mGoodsMinHisTask;
    private Runnable updateSizeTask;

    @SuppressWarnings("deprecation")
    public StarMinuteHourLineChart(Context context) {
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


        updateSpanXByMaxPoint(240 + LASTSPACESIZE);//一个小时是3600 所以是3700 空出来100
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
        if (minTimeList.size() > 0) {
            try {
                int lastItemTime = minTimeList.getLast().time % 2400;
                if (isMaxSize) {//String dateString, String parserFormat, String format
                    drawText(canvas, hhmm2Date(String.format("%04d", lastItemTime)), 0, texty, Color.parseColor("#a1a1a1"), true);//时间字体yanse BC000000
                    drawText(canvas, getGoldTimeFormat(lastItemTime), (spanX * maxPoint), texty, Color.parseColor("#a1a1a1"), false);
                } else {
                    drawGoldTimeText(canvas, hhmm2Date(String.format("%04d", lastItemTime)), 0, texty, Color.parseColor("#a1a1a1"), true);
                    drawGoldTimeText(canvas, getGoldTimeFormat(lastItemTime), (spanX * maxPoint), texty, Color.parseColor("#a1a1a1"), false);
                }
            } catch (Exception e) {
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
                int n = (int) ((x / chartWidth) * minTimeList.size());//图上第几个点
//                int n = (int) (x / spanX);//图上第几个点
                if (n >= size) n = size - 1;
                vLinePosition = n;
            }
            CommonUtil.LogLa(2, "vLinePosition is " + vLinePosition);
            if (vLinePosition >= 0 && vLinePosition < size) {
                minTimeEntity = minTimeList.get(vLinePosition);
                vLineX = minTimeEntity.x;
                vLineY = minTimeEntity.minY;
                Log.d("refreshDraw","4444");
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
                Log.d("drawWhiteLine","minTimeEntity.timeFormat=="+minTimeEntity.timeFormat);
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
            if (entity.minY <= chartHeight && entity.minY >= 0) {
                if (entity.x != 0 && preminX != 0 && entity.minY != 0) {
                    if (isMoveToMin) {
//                        com.taojin.util.CommonUtil.LogLa(2, "preminX is " + preminX + " preminY is " + preminY + " " + spanX);
                        if (preminX > Float.parseFloat(formatNumber(2, spanX))) {
                            minLinePath.moveTo(-2, preminY);
                            minLinePath.lineTo(preminX, preminY);
                        } else {
                            minLinePath.moveTo(preminX, preminY);
                        }
                        // minLinePath.moveTo(preminX, preminY);
                        isMoveToMin = false;
                    } else {
                        minLinePath.lineTo(entity.x, entity.minY);
                    }

                }

                if (preminX == 0 && preminY == 0) {//第一次的时候要平移过去
                    rectPath.lineTo(entity.x, chartHeight / 2);
                }
                rectPath.lineTo(entity.x, entity.minY);
                preminX = entity.x;
                preminY = entity.minY;

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
        double rate = 1;//涨幅需要和昨日收盘价比较最新交易价
        if (minTimeList != null) {
            if (size > 0) {
                rate = minTimeList.get(size - 1).rate;
            }
        }
        int rectColor = StockChartUtil.getRateAlphaColorNoWhite(rate);
        int ractColor = StockChartUtil.getRateColorNoWhite(rate);
        updateLinearGradient(new LinearGradient(0, 0, 0, chartHeight, rectColor, rectColor, Shader.TileMode.MIRROR));
        canvas.drawPath(rectPath, rectPaint);
        minLinePaint.setColor(ractColor);
        canvas.drawPath(minLinePath, minLinePaint);
        // 画最后一个圆点
        paint.setColor(ractColor);//
        if (preminY == 0) preminY = chartHeight / 2 - 10;
        canvas.drawCircle(preminX, preminY, 3, paint);
        drawavgAll(canvas);//画持仓线


//        // TODO 增加了这里加入 渐变
//        int size = minTimeList.size();
//        double rate = 0;//涨幅需要和昨日收盘价比较最新交易价
//        int ractColor = StockChartUtil.getRateColorNoWhite(rate);
////        minLinePaint.setColor(ractColor);
//        canvas.drawPath(minLinePath, minLinePaint);
//        // 画最后一个圆点
//        paint.setColor(ractColor);//
//        if (preminY == 0) preminY = chartHeight / 2 - 10;
//        canvas.drawCircle(preminX, preminY, 3, paint);
////        drawavgAll(canvas);//画持仓线


    }

    /**
     * 交易时间
     *
     * @param currData
     */
    public void startHisDateTask(StarProData currData, String resultJson) {
        CommonUtil.cancelAsyncTask(mGoodsMinHisTask);
        mGoodsMinHisTask = (GoodsMinHisTask) new GoodsMinHisTask(currData, resultJson).executeParams();
    }


    /**
     * 获取黄金的行情数据
     */
    private class GoodsMinHisTask extends BaseAsyncTask<String, Void, String> {
        private Exception mReason;
        private StarProData currData;  //当前信息
        private BaseRequestListener listener;
        private int finalMaxPoint;
        private String result;

        public GoodsMinHisTask(StarProData currData, String result) {
            isGet = true;
            this.currData = currData;
            this.result = result;
            this.listener = listener;
        }

        @Override
        protected void onPreExecute() {
            clearAllDate();

            if (listener != null) listener.requestStart();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                synchronized (LOCK_OBJECT) {
                    isGet = true;
                    yesValue = (float) currData.preClose;
//                    maxValue = (float) currData.high;
//                    minValue = (float) currData.low;
                    if (currData != null) {
                        if (!TextUtils.isEmpty(result)) {
                            JSONObject jsonObject = new JSONObject(result);
                            Log.d("drawWhiteLine","result=="+result);
                            minTimeList.clear();
                            if (JsonParserUtils.hasAndNotNull(jsonObject, "hhmm_count")) {
                                finalMaxPoint = jsonObject.getInt("hhmm_count") + LASTSPACESIZE;
                            }
                            if (JsonParserUtils.hasAndNotNull(jsonObject, "min_" + currData.symbol)) {
                                List<MinTimeEntity> bakList = parserGoodsMinuteDataHisSetList(jsonObject.getString("min_" + currData.symbol));
                                if (bakList != null) minTimeList.addAll(bakList);
                            }
                            calculateMaxValueHome();
                            sortListAsc();
                            //计算最大的交易量
                            calculateVolMaxValueFromList();
                            // 换算坐标
//                            reSetMinTimeAllXY();//不能放这里,放这里的话分时图一进来可能会错乱
                        }
                    }
                    isGet = false;
                    isInit = true;
                }
                return "1";
            } catch (Exception e) {
                mReason = e;
                return null;
            }
        }



        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                NotificationsUtil.ToastReasonForFailure(context, mReason);
            }
            if (finalMaxPoint > LASTSPACESIZE && maxPoint != finalMaxPoint) {
                requestLayout();
            }
            Log.d("refreshDraw","11111");
            reSetMinTimeAllXY();
            refreshDraw();
            if (listener != null) listener.requestComplete(result);
        }

        /**
         * 计算成交量相差值最大值
         */
        private void calculateVolMaxValueFromList() {
            double max;
            boolean isset = false;
            for (MinTimeEntity entity : minTimeList) {
                if (!isset) {
//                    maxAmtOrVolValue = entity.vol;
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

        /**
         * @param content
         * @return
         * @throws Exception
         */
        public List<MinTimeEntity> parserGoodsMinuteDataHisSetList(final String content) throws Exception {
            // 时间0, 当前价1,成交量2,成交额;
//            final String regex2 = "([0-9]+),([0-9]*[.]??[0-9]*),([0-9]+);"; // 总
            final String regex2 = "([0-9]+),([0-9]*[.]??[0-9]*),([0-9]+),([0-9]*[.]??[0-9]*);"; // 总、
            final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
            final Matcher ma2 = pa2.matcher(content);
            List<MinTimeEntity> bakList = new ArrayList<MinTimeEntity>();
            maxValue = yesValue;
            minValue = yesValue;
            while (ma2.find()) {
                if (ma2.groupCount() == 4) {
                    if (ma2.group(1).matches(CommonConst.INTMATCHES) && ma2.group(2).matches(CommonConst.FLOATMATCHES) && ma2.group(3).matches(CommonConst.INTMATCHES) && ma2.group(4).matches(CommonConst.FLOATMATCHES)) {
                        MinTimeEntity item = new MinTimeEntity();
                        item.time = Integer.parseInt(ma2.group(1));
                        item.zjcj = Float.parseFloat(ma2.group(2));
                        minValue = Math.min(minValue,item.zjcj);
                        maxValue = Math.max(maxValue,item.zjcj);
                        item.vol = Float.parseFloat(ma2.group(3));
                        double cjje = Double.parseDouble(ma2.group(4));
                        if (item.vol != 0) item.ma = (float) (cjje / item.vol);
                        bakList.add(item);
                    }
                }
            }
            return bakList;
        }

    }

//    @Override
//    public void calculateMaxValueHome() {
//        if (maxValue != 0 && minValue != 0) {
//            if (Math.round(maxValue - minValue) <= 0.01f) {
//                maxValue = maxValue + 0.01f;
//                minValue = minValue - 0.01f;
//            }
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
        int lasttime = 0;//最后的时间
        if (minTimeList.size() > 0) {
            lasttime = hhmmssToSecond(minTimeList.getLast().time);
        }
        Log.d("minTimeList","minTimeList.size()=="+minTimeList.size());
        for (int i = 0; i < minTimeList.size(); i++) {
            MinTimeEntity entity = minTimeList.get(i);
//            CommonUtil.LogLa(2, "screenwidth is " + chartWidth + " lasttime is " + lasttime + " now is " + hhmmssToSecond(entity.time) + " pos is " + ((maxPoint - 100) - (lasttime - hhmmssToSecond(entity.time))) + "entity.x " + (spanX * ((maxPoint - 10) - (lasttime - hhmmssToSecond(entity.time)))) + " entity.time is " + entity.time);
            entity.x = Float.parseFloat(formatNumber(2, spanX * ((maxPoint - LASTSPACESIZE) - (lasttime - hhmmssToSecond(entity.time)))));
            // 计算分时Y坐标
            entity.minY = getCharPixelY(maxValue, spanY, entity.zjcj);

            // 计算黄线Y坐标
            entity.maY = StockChartUtil.getCharPixelY(maxValue, spanY, entity.ma);
            // 计算成交量Y坐标
            if (i == 0) {
                preVol = entity.vol; //第一个点可以过滤
            }
            entity.volOffset = entity.vol - preVol;//交易量是现在这个点-前一个点
            entity.volY = StockChartUtil.getCharVolumeY(maxAmtOrVolValue, spanAmtOrVolY, entity.volOffset);
            if (entity.vol > 0) preVol = entity.vol;

            // 计算时间格式化，rate
            if (yesValue != 0) {
                entity.rate = formatNumberFloat(2, (entity.zjcj - yesValue) * 100 / yesValue);
            }
            Log.d("minTimeList","entity.time=="+entity.time+"   i=="+i);
            entity.timeFormat = getGoldTimeFormat(entity.time);
            Log.d("minTimeList","entity.timeFormat=="+entity.timeFormat+"   i=="+i);
        }
//        for (MinTimeEntity entity : minTimeList) {
//            entity.x = getCharPixelX(spanX, entity.time);
//            // 计算分时Y坐标
//            entity.minY = getCharPixelY(maxValue, spanY, entity.zjcj);
//            // 计算时间格式化，rate
//            if (yesValue != 0) {
//                entity.rate = formatNumberFloat(2, (entity.zjcj - yesValue) * 100 / yesValue);
//            }
//            entity.timeFormat = getGoldTimeFormat(entity.time);
//        }
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

    //
    private String getGoldTimeFormat(int longtime) {
//        time = time % 1440; //一天的距离
//        int hour = time / 60;
//        int numter = time % 60;
//        return String.format("%02d", hour) + ":" + String.format("%02d", numter);
        String str = "";
        try {
            int h = longtime / 100;
            if (h >= 24) h = h % 24;
//            int m = (longtime % 10000) / 100;
            int m = longtime % 100;
            StringBuffer timeStr = new StringBuffer();

            timeStr.append(h >= 10 ? String.valueOf(h) : "0" + Math.max(0, h));
            timeStr.append(":" + (m >= 10 ? String.valueOf(m) : "0" + Math.max(0, m)));
//            timeStr.append(":" + (s >= 10 ? String.valueOf(s) : "0" + Math.max(0, s)));
            str = timeStr.toString();
            timeStr.setLength(0);
        } catch (Exception e) {

        }

        Log.d("drawWhiteLine","getGoldTimeFormat=="+str);
        return str;
    }

    /**
     * 把时分秒 变成累加模式的秒数
     * 现在换算成分,因为是5小时
     */

    private int hhmmssToSecond(int time) {
//        int hour = time / 10000;
//        int numter = time % 10000 / 100;
//        int second = time % 100;
//        return ((hour * 3600) + (numter * 60) + second);
        int hour = time / 100;
        int numter = time % 100;
        return (hour * 60) + numter;

    }


    /**
     * HHmm 转换成  累加秒模式
     *
     * @param time
     * @return
     */
    private short timeformatshort(int time) {
        int hour = time / 100;
        int numter = time % 100;
        return (short) ((hour * 60) + numter);
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

    //持仓线
    private void drawavgAll(Canvas canvas) {
        //这里是持仓线

        float upy = -1;
        float downy = -1;
        if (minValue <= (float) upPosiAvePri && (float) upPosiAvePri <= maxValue) {
            upy = getCharPixelY(maxValue, spanY, (float) upPosiAvePri);
        }
        if (minValue <= (float) downPosiAvePri && (float) downPosiAvePri <= maxValue) {
            downy = getCharPixelY(maxValue, spanY, (float) downPosiAvePri);
        }

        boolean isNeedUp = upy <= chartHeight && upy >= 0;
        boolean isNeedDown = downy < chartHeight && downy >= 0;

        if (!isNeedUp && !isNeedDown) {
            return;//不需要画
        }

        float avgfontheight = getFontHeight(getFontPaint()) * 2 / 3;
        float textupy = upy;
        float textdowny = downy;
        if (upy < avgfontheight) {
            textupy = upy + avgfontheight;
        }
        if (downy < avgfontheight) {
            textdowny = downy + avgfontheight;
        }

        String upavgText = "买涨:" + StockChartUtil.formatNumber(decimal, upPosiAvePri);
        String downavgText = "买跌:" + StockChartUtil.formatNumber(decimal, downPosiAvePri);
        float tmpupx = 0;
        float tmpdownx = 0;
        if (minTimeList.size() < maxPoint / 2) {
            tmpupx = chartWidth - getFontPaint().measureText(upavgText);
            tmpdownx = chartWidth - getFontPaint().measureText(downavgText);
        }
        if (upPosiAvePri > 0 && downPosiAvePri > 0) {//兩個數也都有
            if (Math.abs(upy - downy) <= avgfontheight) {//如果2个重叠
                if (upPosiAvePri <= downPosiAvePri) {
                    if (upy < avgfontheight) {
                        drawavgitem(canvas, tmpupx, upy, textupy, upavgText, true);
                        drawavgitem(canvas, tmpdownx, downy, textupy + avgfontheight + 4f, downavgText, false);
                    } else {
                        drawavgitem(canvas, tmpupx, upy, upy, upavgText, true);
                        drawavgitem(canvas, tmpdownx, downy, downy + avgfontheight + 4f, downavgText, false);
                    }
                } else {
                    if (downy < avgfontheight) {
                        drawavgitem(canvas, tmpdownx, downy, textdowny, downavgText, false);
                        drawavgitem(canvas, tmpupx, upy, textdowny + avgfontheight + 4f, upavgText, true);
                    } else {
                        drawavgitem(canvas, tmpdownx, downy, downy, downavgText, false);
                        drawavgitem(canvas, tmpupx, upy, upy + avgfontheight + 4f, upavgText, true);
                    }
                }
            } else {
                drawavgitem(canvas, tmpupx, upy, textupy, upavgText, true);
                drawavgitem(canvas, tmpdownx, downy, textdowny, downavgText, false);
            }

        } else if (upPosiAvePri > 0) {
            drawavgitem(canvas, tmpupx, upy, textupy, upavgText, true);

        } else if (downPosiAvePri > 0) {
            drawavgitem(canvas, tmpdownx, downy, textdowny, downavgText, false);
        }
    }


    private void drawavgitem(Canvas canvas, float linex, float liney, float texty, String text, boolean isBuy) {
        int unit = 2;
        float avgfontheight = getFontHeight(getFontPaint()) * 3 / 4;
        int color = Color.parseColor("#ec6338");

        if (!isBuy) {
//            effectPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 1));
            color = Color.parseColor("#37d270");
        } else {
//            effectPaint.setPathEffect(new DashPathEffect(new float[]{4, 4}, 5));
        }
        Path upPath = new Path();
        upPath.moveTo(0, liney);
        upPath.lineTo(chartWidth, liney);
        effectPaint.setColor(color);
        canvas.drawPath(upPath, effectPaint);

        rectavgPaint.setColor(color);
        RectF effRectF = new RectF();
        if (linex < chartWidth / 2) {
            effRectF.set(unit, texty - avgfontheight - unit, getFontPaint().measureText(text) + (unit * 2), texty - unit);
            drawText(canvas, text, linex + unit, texty - (unit * 2), color, true);
        } else {
            effRectF.set(chartWidth - getFontPaint().measureText(text) - (unit * 2), texty - avgfontheight - unit, chartWidth - unit, texty - unit);
            drawText(canvas, text, linex - unit, texty - (unit * 2), color, true);
        }

        canvas.drawRoundRect(effRectF, 4, 4, rectavgPaint);
    }


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
     * 升序排列
     */
    private void sortListAsc() {
        // 升序计算K线
        Collections.sort(minTimeList, new Comparator<MinTimeEntity>() {
            @Override
            public int compare(MinTimeEntity s1, MinTimeEntity s2) {
//                maxValue = Math.max(maxValue, s1.zjcj);
//                maxValue = Math.max(maxValue, s2.zjcj);
//                if (minValue == 0) {
//                    minValue = s1.zjcj;
//                }
//                minValue = Math.min(minValue, s1.zjcj);
//                minValue = Math.min(minValue, s2.zjcj);
                return s1.time - s2.time;
            }
        });
    }

    private String hhmm2Date(String str) {
        if (TextUtils.isEmpty(str)) return null;
        SimpleDateFormat formatter = new SimpleDateFormat("HHmm");
        try {
            Calendar c = Calendar.getInstance();
            c.setTime(formatter.parse(str));
            c.add(Calendar.HOUR, Math.min(-1, -(int) (maxPoint / 60)));//当前时间减去一个小时
            return VeDate.getDateToHHmm(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
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
//                maxValue = (float) currPrice.high;
//                minValue = (float) currPrice.low;
                int hhmm = getStringHHMMToInt(stryyyyMMddHHmmssToDate(currPrice.time));
                MinTimeEntity nowTimeEntity = new MinTimeEntity(hhmm, (float) currPrice.last);
                minValue = Math.min(minValue,nowTimeEntity.zjcj);
                maxValue = Math.max(maxValue,nowTimeEntity.zjcj);

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
                Iterator it = minTimeList.iterator();
                while (it.hasNext()) {
                    MinTimeEntity tempobj = (MinTimeEntity) it.next();
                    if (tempobj.time > hhmm || tempobj.time < hhmm - 100 * ((int) maxPoint / 60)) {//清除少一个小时的数据  tempobj.time < hhmmss - 10000
                        it.remove();
//                        com.tjr.perval.util.CommonUtil.LogLa(2, "minTime minTimeList clear " + tempobj.time + " 计算" + (hhmm - 100 * (maxPoint / 60)));
                    }
                }


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
                    Log.d("refreshDraw","2222");
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
                CommonUtil.LogLa(2, "小时图 isLostTime minTimeEntityTime is " + minTimeEntity.time + " nowItem is " + nowItem.time);
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
//    /**
//     * 解析json
//     */
//    public void parserJsonStock(final StarProData currPrice) throws Exception {
//        synchronized (LOCK_OBJECT) {
//            if (isGet) return;
//            if (currPrice != null) {
//                int size = minTimeList.size();
//                int hhmm = getStringHHMMToInt(stryyyyMMddHHmmssToDate(currPrice.date + currPrice.time)); //Integer.parseInt(currPrice.time);
//                MinTimeEntity minTimeEntity = null;
//                if (size > 0) {
//                    minTimeEntity = minTimeList.get(size - 1);
//                }
//                com.tjr.perval.util.CommonUtil.LogLa(2, "minTime paserjsonStock is " + hhmm + " last is " + (float) currPrice.last + " amount is " + (float) currPrice.amount);
//                if (minTimeEntity != null) {
////                    Log.d("pieview", "doInBackground  minTimeEntity.time=" + minTimeEntity.time + "  hhmm=" + hhmm);
//                    if (Math.abs(minTimeEntity.time - hhmm) >= 5) {
//                        if (mReGetHisListen != null)
//                            mReGetHisListen.reGetHisListen();
////                        parCount = 0;//重置
//                        return;
//                    }
//                    if (minTimeEntity.time == hhmm) {
//                        minTimeEntity.zjcj = (float) currPrice.last;
//                        minTimeEntity.vol = (float) currPrice.amount;
//                    } else {
//                        //如果获取到开盘5点50以上的数据，并且数据里面包含0点以后的数据，就重新刷新列表
//                        MinTimeEntity item = new MinTimeEntity(hhmm, (float) currPrice.last);
//                        item.vol = (float) currPrice.amount;
//                        minTimeList.add(item);
//                    }
//                } else {
//                    MinTimeEntity item = new MinTimeEntity(hhmm, (float) currPrice.last);
//                    item.vol = (float) currPrice.amount;
//                    minTimeList.add(item);
////                    minTimeList.add(new MinTimeEntity(hhmm, (float) currPrice.last, (float) currPrice.amount));
//                }
//                Iterator it = minTimeList.iterator();
////                com.tjr.perval.util.CommonUtil.LogLa(2, "minTime minTimeList 计算 " + (hhmm - 100 * ((int) maxPoint / 60)));
//                while (it.hasNext()) {
//                    MinTimeEntity tempobj = (MinTimeEntity) it.next();
//                    if (tempobj.time > hhmm || tempobj.time < hhmm - 100 * ((int) maxPoint / 60)) {//清除少一个小时的数据  tempobj.time < hhmmss - 10000
//                        it.remove();
//                        com.tjr.perval.util.CommonUtil.LogLa(2, "minTime minTimeList clear " + tempobj.time + " 计算" + (hhmm - 100 * (maxPoint / 60)));
//                    }
//                }
//
//                sortListAsc();
//                //计算最大的交易量
//                if (minTimeList.size() > 0) {
//                    float perVol = minTimeList.getFirst().vol;
//                    for (MinTimeEntity item : minTimeList) {
//                        maxAmtOrVolValue = Math.max(maxAmtOrVolValue, item.vol - perVol);
//                        if (item.vol > 0) perVol = item.vol;
//                    }
//                }
//                if (maxValue != 0 && minValue != 0) {
//                    if (maxValue - minValue < 0.01f) {
//                        maxValue = maxValue + 0.01f;
//                        minValue = minValue - 0.01f;
//                    }
//                    yesValue = (maxValue + minValue) / 2;
//                }
//                calculateMaxValueHome();
//                reSetMinTimeAllXY();
//                refreshDraw();
//            }
//        }
//}

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

    private class UpDateSizeTask implements Runnable {

        public void run() {
            try {
                synchronized (LOCK_OBJECT) {
                    calculateMaxValueHome();
                    reSetMinTimeAllXY();
                }
            } catch (Exception e) {
            } finally {
                Log.d("refreshDraw","3333");
                refreshDraw();
            }
        }
    }

}
