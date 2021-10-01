package com.procoin.widgets.quotitian.sym;

import android.animation.ValueAnimator;
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
import android.text.Layout;
import android.text.StaticLayout;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;

import com.procoin.common.constant.CommonConst;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.RxAsyncTask;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.quotitian.entity.LineTimeEntity;
import com.procoin.widgets.quotitian.entity.LineXYEntity;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.widgets.quotitian.entity.StockDayData;
import com.procoin.widgets.quotitian.entity.jsonparser.StarProDataParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 分时图(股指期货)
 * Created by taojinroad on 16/8/24.
 */
public class MinuteStockTimeLineChart extends BaseStockTimeLineView {
    private static final Object LOCK_OBJECT = new Object();
    private final List<LineTimeEntity> lineResultList = new ArrayList<>();
    private LineTimeEntity midLineTimeEntity, lastLineTimeEntity;

    private volatile List<LineXYEntity> lineXYList = new ArrayList<LineXYEntity>();// 保存当前要显示线数据的坐标信息

    private Paint paint, volpaint, linePaint, dottedinePaint; //

    private Paint shadePaint;//阴影

    private Handler handler = new Handler();
    private static final long DEPLAY_MILLTIME = 200;// 延迟多少时间进行刷新

    private boolean isShowCircle;

    private GestureDetector gestureDetector;// 手势
    private int pointer;// 接触点数
    private float distanceDown = 0;// 按下时两指间的距离
    private float distanceUp = 0;// 离开时两指间的距离
    //竖线
    private volatile boolean isShowWhiteLine;// 是否显示白线
    public int vLinePosition = 0;
    private float vLineX = 0, vLineY = 0;// 白竖线的X,y坐标
    private float left = 0, top = 0, right = 0, bottom = 0;// 白线背景框

    private KlineChartDataListener klineChartDataListener;
    private KLineChartShowWhiteLine kLineChartShowWhiteLine;// 显示白线的回调

    private RectF rectF = new RectF();// 设置个长方形

    private float maxWaveRadius = 30;//扩散半径
    private long waveTime = 2000;//一个涟漪扩散的时间
    private int waveRate = 1;//涟漪的个数
    //...get/set方法略
    private Paint circlePaint;
    private float[] waveList;//涟漪队列

    private Path path = new Path();

    public interface KlineChartDataListener {
        void returnWhiteLineIndex(boolean isWhiteLine, final StockDayData stockDayData);
    }

    // TODO K线白线显示请求监听
    public interface KLineChartShowWhiteLine {
        void showWhiteLine(boolean show);
    }

    public void setKlineChartDataListener(KlineChartDataListener klineChartDataListener) {
        this.klineChartDataListener = klineChartDataListener;
    }

    public void setkLineChartShowWhiteLine(KLineChartShowWhiteLine kLineChartShowWhiteLine) {
        this.kLineChartShowWhiteLine = kLineChartShowWhiteLine;
    }

    public MinuteStockTimeLineChart(Context context) {
        super(context);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);/* 去锯齿 */
        paint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(lineColor);
        linePaint.setAntiAlias(true);/* 去锯齿 */
        linePaint.setStrokeWidth(2f);
        volpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        volpaint.setAntiAlias(true);/* 去锯齿 */


        shadePaint = new Paint();
        shadePaint.setStyle(Paint.Style.FILL);
//        shadePaint.setAntiAlias(true);
        dottedinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        dottedinePaint.setColor(lineColor);
        dottedinePaint.setStyle(Paint.Style.STROKE);
        dottedinePaint.setAntiAlias(true);
        dottedinePaint.setStrokeWidth(2f);
        dottedinePaint.setPathEffect(new DashPathEffect(new float[]{10f, 10f}, 0));

        //最开始不透明且扩散距离为0
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
        gestureDetector = new GestureDetector(new GestureListener());
    }

    @Override
    public void drawShareTime(Canvas canvas) {
        if (spanLineY <= 0 || spanVolY <= 0) return;
        synchronized (LOCK_OBJECT) {
            // 画线
            drawToLine(canvas);
            // 画十字线
            drawCrossLine(canvas);
        }
    }

    /**
     * 画分时线
     *
     * @param canvas
     */
    private void drawToLine(final Canvas canvas) {
        LineXYEntity preXY = null;
        Log.d("minuteTimeline", "lineXYList==" + (lineXYList == null ? "null" : lineXYList.size()));
        for (LineXYEntity xy : lineXYList) {
            if (xy.coordinateX >= 0 && xy.coordinateX <= chartWidth) {
                if (preXY != null) {
                    canvas.drawLine(preXY.coordinateX, preXY.lineY + stepMarginTop, xy.coordinateX, xy.lineY + stepMarginTop, linePaint);
                }
            }
            drawCjsl(canvas, xy);// 底部成效图
            preXY = xy;
        }
        if (preXY == null) return;
        //画渐变
        drawShade(canvas);
        //绘制扩散的圆
        if (isShowCircle) {
            canvas.drawCircle(preXY.coordinateX, preXY.lineY + stepMarginTop, 10, linePaint);//根据进度计算扩散半径
            for (Float waveRadius : waveList) {
                circlePaint.setColor(Color.argb((int) (255 * (1 - waveRadius)), 255, 143, 1));//根据进度产生一个argb颜色
                canvas.drawCircle(preXY.coordinateX, preXY.lineY + stepMarginTop, waveRadius * maxWaveRadius, circlePaint);//根据进度计算扩散半径
            }
        }
        //绘制当前价
        if(lastLineTimeEntity != null){
            String p = formatNumberAuto(lastLineTimeEntity.price);
            Path path = new Path();
            float priceWidth = fontPaint.measureText(p) + 15 * 2;
            float priceHeight = getFontHeight(fontPaint);
            if ((preXY.coordinateX + priceWidth + 20) > chartWidth) {
                preXY = new LineXYEntity(lastLineTimeEntity.time, lastLineTimeEntity.price, lastLineTimeEntity.amount, lastLineTimeEntity.balance);
                preXY.lineY = getCharPixelY(maxPriceValue, spanLineY, lastLineTimeEntity.price);//当前值
                preXY.coordinateX = chartWidth;
                path.moveTo(0, preXY.lineY + stepMarginTop);
                path.lineTo(stepColumn * 3 - priceWidth, preXY.lineY + stepMarginTop);
                canvas.drawPath(path, dottedinePaint);
                path.reset();
                path.moveTo(stepColumn * 3, preXY.lineY + stepMarginTop);
                path.lineTo(chartWidth, preXY.lineY + stepMarginTop);
                canvas.drawPath(path, dottedinePaint);

//                gridPaint.setStyle(Paint.Style.STROKE);// 设置填满
                canvas.drawRect(stepColumn * 3 - priceWidth, preXY.lineY + stepMarginTop - priceHeight / 2-5, stepColumn * 3, preXY.lineY + stepMarginTop + priceHeight / 2+5, gridPaint);

                StaticLayout layout = new StaticLayout(p, fontPaint, (int) priceWidth, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
                canvas.save();
                canvas.translate(stepColumn * 3- layout.getWidth(), preXY.lineY + stepMarginTop - layout.getHeight() / 2);//从20，20开始画
                layout.draw(canvas);
                canvas.restore();



//            drawText(canvas, p,stepColumn*4-priceWidth,preXY.lineY + stepMarginTop+priceHeight/2,fontColor,Paint.Align.LEFT);
            } else {
                path.moveTo(preXY.coordinateX + 15, preXY.lineY + stepMarginTop);
                path.lineTo(chartWidth - fontPaint.measureText(p) - 15, preXY.lineY + stepMarginTop);
                drawText(canvas, p, chartWidth - 10f, preXY.lineY + stepMarginTop + getFontHeight(fontPaint) / 3, lineColor, Paint.Align.RIGHT);
                canvas.drawPath(path, dottedinePaint);//用canvas.drawLine画虚线是有问题的，要开启硬件加速才可以，这里用drawPath代替
            }
        }

    }

    //画渐变
    private void drawShade(Canvas canvas) {
//        LineXYEntity preXY = null;
        path.reset();
        LinearGradient gradient = null;
        float firstPointX = 0, firstPointY = 0;
        for (int i = 0, m = lineXYList.size(); i < m; i++) {
            LineXYEntity xy = lineXYList.get(i);
//            if (xy.coordinateX >= 0 && xy.coordinateX <= chartWidth) {
            if (i == 0) {
                firstPointX = xy.coordinateX;
                firstPointY = xy.lineY + stepMarginTop;
                path.moveTo(xy.coordinateX, xy.lineY + stepMarginTop);
            } else {
                path.lineTo(xy.coordinateX, xy.lineY + stepMarginTop);
                if (i == lineXYList.size() - 1) {
                    path.lineTo(xy.coordinateX, lineChartHeight + stepMarginTop);
                    path.lineTo(firstPointX, lineChartHeight + stepMarginTop);
                    path.close();
                }
            }
//                }
        }
        int[] colors = {shadeStartColor, shadeMidColor, shadeEndColor};
        float[] position = {0f, 0.5f, 1.0f};
        gradient = new LinearGradient(
                firstPointX,
                firstPointY,
                firstPointX,
                lineChartHeight + stepMarginTop,
                colors,
                position,
                Shader.TileMode.CLAMP);
        shadePaint.setShader(gradient);
        canvas.drawPath(path, shadePaint);

    }

    /**
     * 画十字线 新版改成蓝色
     *
     * @param canvas
     */
    private void drawCrossLine(final Canvas canvas) {
        if (isShowWhiteLine && midLineTimeEntity != null) {
            String time = DateUtils.getStringDateOfString2(String.valueOf(midLineTimeEntity.time), DateUtils.TEMPLATE_HHmm);
            float timeWidth = fontPaint.measureText(time) / 2;
            float textHeight = getFontHeight(fontPaint);

            String price = formatNumberAuto(midLineTimeEntity.price);
            float priceWidth = fontPaint.measureText(price)+15*2;

            float klineHeight = chartHeight - stepMarginBottom;
            // x线
            canvas.drawLine(vLineX, 0, vLineX, klineHeight, gridPaint);
            // xy交接点
            gridPaint.setStyle(Paint.Style.FILL);// 设置填满
            canvas.drawCircle(vLineX, vLineY + stepMarginTop, 10, gridPaint);//根据进度计算扩散半径
//            gridPaint.setStyle(Paint.Style.STROKE);// 设置填满
            //时间
            canvas.drawRect(vLineX - timeWidth, chartHeight - stepMarginBottom + textHeight / 4, vLineX + timeWidth, chartHeight - stepMarginBottom + textHeight * 5 / 4, gridPaint);
            drawText(canvas, time, vLineX - timeWidth, chartHeight - stepMarginBottom + textHeight, fontColor, Paint.Align.LEFT);
            //右边十字线的价格
            CommonUtil.LogLa(3, "====vLineX=" + vLineX);
            if (vLineX > chartWidth / 2) {
                // y线
                canvas.drawLine(priceWidth , vLineY + stepMarginTop, chartWidth, vLineY + stepMarginTop, gridPaint);
                canvas.drawRect(0, vLineY + stepMarginTop - textHeight / 2 - 5, priceWidth , vLineY + stepMarginTop + textHeight / 2 + 5, gridPaint);
                drawText(canvas, price, priceWidth/2, vLineY + stepMarginTop + textHeight / 4, fontColor, Paint.Align.CENTER);
            } else {
                // y线
                canvas.drawLine(0, vLineY + stepMarginTop, chartWidth - priceWidth - 10, vLineY + stepMarginTop, gridPaint);
                canvas.drawRect(chartWidth - priceWidth , vLineY + stepMarginTop - textHeight / 2 - 5, chartWidth , vLineY + stepMarginTop + textHeight / 2 + 5, gridPaint);
                drawText(canvas, price, chartWidth - priceWidth/2 , vLineY + stepMarginTop + textHeight / 4, fontColor, Paint.Align.CENTER);
            }
        }
    }

    /**
     * 画成交量图
     *
     * @param canvas
     */
    private void drawCjsl(Canvas canvas, LineXYEntity xy) {
        volpaint.setStrokeWidth(1);// 空心矩形边框线宽
        volpaint.setStyle(Paint.Style.FILL);// 实心
        volpaint.setColor(volColor);/* 设定paint的颜色 */
        canvas.drawRect(xy.coordinateX, chartHeight - volChartHeight - stepMarginBottom + xy.volY, xy.coordinateX + spanX-1, chartHeight - stepMarginBottom, volpaint);
//        if (xy.isShowTime) {
//            drawText(canvas, DateUtils.getStringDateOfString2(String.valueOf(xy.time), DateUtils.TEMPLATE_HHmm), xy.coordinateX, chartHeight - stepMarginBottom / 2, fontColor, Paint.Align.CENTER);
//        }
    }


    float x;

    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        pointer = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_2_DOWN: // 两手第一次距离
                getParent().requestDisallowInterceptTouchEvent(true);
                distanceDown = (float) Math.hypot(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
                break;
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
//                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE: // 拖动白线一起移动
                if (isShowWhiteLine) {
                    calculateLinePosition(event.getX(), event.getY());// 移动竖线
                } else {
                    if (Math.abs(event.getX() - x) > 30) {//解决滑动冲突问题
                        getParent().requestDisallowInterceptTouchEvent(true);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
            default:
                // 把白线消失
                if (isShowWhiteLine) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                    isShowWhiteLine = false;
                    vLinePosition = 0;
                    calculateLinePosition(event.getX(), event.getY());// 移动竖线
                }
                break;
        }
        if (isShowWhiteLine) return true;
        return gestureDetector.onTouchEvent(event);
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (checkOnTouchRect(e.getY()) == 1) {
                callOnClick();
            }
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO 长按时出现白线
            if (checkOnTouchRect(e.getY()) == 2) return;// 触模到成交量的区域不出现白线
            CommonUtil.LogLa(3, "====onLongPress=" + e.getAction());
            getParent().requestDisallowInterceptTouchEvent(true);
            isShowWhiteLine = true;
            calculateLinePosition(e.getX(), e.getY());
            if (kLineChartShowWhiteLine != null)
                kLineChartShowWhiteLine.showWhiteLine(isShowWhiteLine);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            if (pointer == 2) {// 两个手指交给这里处理
                distanceUp = (float) Math.hypot(e2.getX(0) - e2.getX(1), e2.getY(0) - e2.getY(1));
                if (distanceDown <= 0) distanceDown = distanceUp;
                if (distanceUp > distanceDown) {// 放大
                    if (defaultLevel < maxLevel) {
                        defaultLevel++;
                        // 更新坐标和K线图
                        refreshDisplayCursor();
                        calculationCoordinate();
                        postInvalidateDelayed(DEPLAY_MILLTIME);
                    }
                } else {// 缩小
                    if (defaultLevel > minLevel) {
                        defaultLevel--;
                        // 更新坐标和K线图
                        refreshDisplayCursor();
                        calculationCoordinate();
                        postInvalidateDelayed(DEPLAY_MILLTIME);
                    }
                }
                distanceDown = distanceUp;
                return true;
            } else {
                if (!isShowWhiteLine) {
                    calculateMovePosition(distanceX);
                    return true;
                }
            }
            return false;
        }

    }

    /**
     * 计算竖线位置
     */
    private void calculateLinePosition(float x, float y) {
        synchronized (LOCK_OBJECT) {
            CommonUtil.LogLa(2, "calculateLinePosition x=" + x + "   y=" + y);
            int size = lineXYList.size();
            if (size == 0) return;
            float rightMaxX;
            float leftMaxX;
            leftMaxX = lineXYList.get(0).coordinateX;
            rightMaxX = lineXYList.get(size - 1).coordinateX;
            if (x < leftMaxX) x = leftMaxX;
            else if (x > rightMaxX) x = rightMaxX;
            int n = (int) (x / spanX);
//            int n = (int) (x / (candlesWidth + spacing));
            if (n >= size) n = size - 1;
            vLinePosition = n;
            CommonUtil.LogLa(2, "calculateLinePosition vLinePosition=" + vLinePosition);
            if (vLinePosition >= 0 && vLinePosition < size) {
                vLineY = lineXYList.get(vLinePosition).lineY;
                vLineX = lineXYList.get(vLinePosition).coordinateX;
            }
            reSetMidByVolMacdKdjValue();
            invalidate();
        }
    }

    /**
     * 设置中间macd,cjsl,kdj的值
     */
    private void reSetMidByVolMacdKdjValue() {
        int size = size = lineResultList.size();
        if (size == 0) return;
        if (!isShowWhiteLine) {// 白线消失
            if (size > 0) midLineTimeEntity = lineResultList.get(size - 1);
        } else {// 白线出现
            int index = startCursor + vLinePosition;
            if (index > size) midLineTimeEntity = lineResultList.get(size - 1);
            else if (index < 0) midLineTimeEntity = lineResultList.get(0);
            else midLineTimeEntity = lineResultList.get(index);
        }
    }


    /**
     * 检测用户点击所在哪个区块
     *
     * @param y
     * @return
     */
    private int checkOnTouchRect(float y) {
        if (y < lineChartHeight) return 1;// 点击在K线图
        return 0;
    }


    /**
     * 计算移动位置
     *
     * @param distanceX X方向的位移
     */
    private void calculateMovePosition(float distanceX) {
        synchronized (LOCK_OBJECT) {
            int size = lineResultList.size();
            if (size <= displayNum) return;
            int n = (int) (distanceX / spanX);
            if (n == 0) return;
            startCursor += n;
            refreshDisplayCursor();
            calculationCoordinate();
            invalidate();
        }
    }

    /**
     * 获取分时线历史数据
     *
     * @param symbol
     * @param progressBar
     */
    public synchronized void startGetDataTask(String symbol, ProgressBar progressBar) {
        new QuoteDataTask(symbol, progressBar).execute();
    }

    private class QuoteDataTask extends RxAsyncTask<String, Void, Boolean> {
        private String symbol;
        private ProgressBar progressBar;

        public QuoteDataTask(String symbol, ProgressBar progressBar) {
            this.symbol = symbol;
            this.progressBar = progressBar;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CommonUtil.LogLa(2, "QuoteDataTask onPreExecute");
            if (progressBar != null) progressBar.setVisibility(VISIBLE);
        }

        @Override
        protected Boolean call(String... strings) {
            try {
                String resultM = CropymelHttpSocket.getInstance().sendProdCodeMinuteLine(symbol);
                //解析数据
                parserData(resultM);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onCompleted() {
            super.onCompleted();
            CommonUtil.LogLa(2, "QuoteDataTask onCompleted");
            if (progressBar != null) progressBar.setVisibility(GONE);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    postInvalidateDelayed(DEPLAY_MILLTIME);
                }
            }, 100);
        }

        @Override
        protected void onResult(Boolean resulte) {
            super.onResult(resulte);
        }

        @Override
        protected void onError(Throwable e) {
            super.onError(e);
        }

        protected void parserData(final String jsonsStr) throws Exception {
            Log.d("minuteTimeline", "jsonsStr==" + jsonsStr);
            synchronized (LOCK_OBJECT) {
                JSONObject jsonObject = new JSONObject(jsonsStr);
                //当前最新数据
                StarProDataParser starProDataParser = new StarProDataParser();
                if (JsonParserUtils.hasAndNotNull(jsonObject, symbol)) {
                    StarProData starProData = starProDataParser.parse(jsonObject.getJSONObject(symbol));
                    if (starProData != null){
                        yesCloseValue=starProData.yesClose;
                        priceDecimals = starProData.priceDecimals;
                    }
                }
                if (JsonParserUtils.hasAndNotNull(jsonObject, "min_" + symbol)) {
                    lineResultList.clear();
                    parserMinuteDataHisList(jsonObject.getString("min_" + symbol));
                    sortListAsc();
                    int lineSize = lineResultList.size();
                    if (lineSize > 0)
                        midLineTimeEntity = lineResultList.get(lineSize - 1);
                    startCursor = lineSize - displayNum + shiftNum;
                    isShowCircle = true;
                    refreshDisplayCursor();
                }
                calculationCoordinate();
                postInvalidate();


            }
        }
    }

    /**
     * 计算分析K线换算坐标
     */
    private void calculationCoordinate() {
        try {
            synchronized (LOCK_OBJECT) {
                List<LineTimeEntity> list = calculationAllMaxAndMin();// calculationMaxAndMin在calculateDisplayNum之前调用
                if (list == null) return;
                calculateDisplayNum();// calculateDisplayNum要在calculationMaxAndMin后
                lineXYList.clear();
                LineXYEntity xy;
                float coordinateX = 0;
                int drawTextTimeCount = 0;
                for (LineTimeEntity data : list) {
                    xy = new LineXYEntity(data.time, data.price, data.amount, data.balance);
                    xy.time = data.time;
                    xy.coordinateX = coordinateX;
                    xy.lineY = getCharPixelY(maxPriceValue, spanLineY, data.price);//当前值
                    if ((int) ((coordinateX + spanX) / stepColumn) == drawTextTimeCount) {
                        xy.isShowTime = true;
                        drawTextTimeCount++;
                    }
                    // amount
                    xy.volY = getVolumeY(maxVolValue, spanVolY, data.amount);
                    lineXYList.add(xy);// 将转换后的结果缓存
                    coordinateX += spanX;// X坐标自增到下一条位置
//                    Log.d("calculationCoordinate","lineY=="+xy.lineY+"  volY=="+xy.volY+"  maxPriceValue=="+maxPriceValue+"  spanLineY=="+spanLineY+"  data.proce=="+data.price+"  data.amount=="+data.amount);
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 计算最大值和最小值,只有一次调用
     */
    private List<LineTimeEntity> calculationAllMaxAndMin() {
        synchronized (LOCK_OBJECT) {
            boolean isLineFirst = true;
            int size = lineResultList.size();
            if (size == 0) return null;
            if (startCursor > endCursor) return null;
            List<LineTimeEntity> list = lineResultList.subList(startCursor, endCursor);
            if (list == null) return null;
            lastLineTimeEntity = lineResultList.get(size-1);
            maxPriceValue = 0;
            minPriceValue = 0;
            maxVolValue = 0;
            for (LineTimeEntity data : list) {
                // 第一根
                if (isLineFirst) {
                    maxPriceValue = lastLineTimeEntity.price;
                    minPriceValue = lastLineTimeEntity.price;
                    isLineFirst = false;
                }
                maxPriceValue = Math.max(maxPriceValue, data.price);
                minPriceValue = Math.min(minPriceValue, data.price);
                maxVolValue = Math.max(maxVolValue, data.amount);
            }
//            maxPriceValue = maxPriceValue + maxPriceValue * 0.0012;
//            minPriceValue = minPriceValue - minPriceValue * 0.0012;
            maxVolValue = maxVolValue + maxVolValue * 0.0012;
            // 这里比较数据
            if (yesCloseValue > 0) {
                double maxValue = Math.abs(maxPriceValue - yesCloseValue);
                double minValue = Math.abs(yesCloseValue - minPriceValue);
                if (maxValue > minValue) {
                    minPriceValue = yesCloseValue - maxValue;
                } else {
                    maxPriceValue = yesCloseValue + minValue;
                }
            }
            return list;
        }
    }

    /**
     * @param content
     * @return
     * @throws Exception
     */
    public void parserMinuteDataHisList(final String content) throws Exception {
        // 时间1,当前价2,成交量3,交易额4;
//        final String regex = "([0-9]+),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*);";
        final String regex = "([0-9]+),([0-9]*[.]??[0-9]*),([0-9]*[.]??[0-9]*);"; // 交易额不要了
        final Pattern pa2 = Pattern.compile(regex, Pattern.DOTALL); // 总
        final Matcher ma2 = pa2.matcher(content);
        while (ma2.find()) {
            if (ma2.groupCount() == 3) {
                if (ma2.group(1).matches(CommonConst.INTMATCHES) && ma2.group(2).matches(CommonConst.FLOATMATCHES) && ma2.group(3).matches(CommonConst.FLOATMATCHES)) {
                    lineResultList.add(new LineTimeEntity(Long.parseLong(ma2.group(1)), Double.parseDouble(ma2.group(2)), Double.parseDouble(ma2.group(3))));
                }
            }
        }
    }

    /**
     * 升序排列
     */
    private void sortListAsc() {
        // 升序计算K线
        Collections.sort(lineResultList, new Comparator<LineTimeEntity>() {
            @Override
            public int compare(LineTimeEntity s1, LineTimeEntity s2) {
                return (int) (s1.time - s2.time);
            }
        });
    }


    /**
     * 初始化K线游标
     */
    private void refreshDisplayCursor() {
        calculateDisplayNum();
        int size = lineResultList.size();
        if (size == 0) return;
        if (displayNum >= size) {
            endCursor = size;
            startCursor = 0;
        } else {
            if (startCursor + displayNum - size > shiftNum) {
                startCursor = size - displayNum + shiftNum;
            }
            endCursor = startCursor + displayNum;
            if (endCursor >= size) {
                endCursor = size;
            }
            if (startCursor < 0) {
                startCursor = 0;
            }
            if (endCursor < size) isShowCircle = false;
            else isShowCircle = true;
        }
    }

    /**
     * 解析json
     */
    public void parserJsonStock(final LineTimeEntity lineTimeEntity) {
        synchronized (LOCK_OBJECT) {
            if (lineTimeEntity == null) return;
            Log.d("MinuteTimeLineChart", "lineTimeEntity==" + lineTimeEntity.toString());
            int size = lineResultList.size();
            if (size > 0) {
                LineTimeEntity lineTime = lineResultList.get(size - 1);
                Log.d("MinuteTimeLineChart", "lineTime.time==" + lineTime.time + "  lineTimeEntity.time==" + lineTimeEntity.time);
                if (lineTimeEntity.time >= lineTime.time) {
                    if (lineTime.time == lineTimeEntity.time) {
                        lineTime.price = lineTimeEntity.price;
                        lineTime.amount = lineTimeEntity.amount;
                        lineTime.balance = lineTimeEntity.balance;
                    } else {
                        lineResultList.add(lineTimeEntity);
                        startCursor++;
                    }
                    refreshDisplayCursor();
                    calculationCoordinate();
                    postInvalidate();
//                    postInvalidateDelayed(DEPLAY_MILLTIME);
                }
            }
        }
    }

}
