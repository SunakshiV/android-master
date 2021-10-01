package com.procoin.widgets.quotitian;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.procoin.common.entity.ResultData;
import com.procoin.common.entity.jsonparser.ResultDataParser;
import com.procoin.http.tjrcpt.CropymelHttpSocket;
import com.procoin.http.util.NotificationsUtil;
import com.procoin.social.TjrSocialMTAUtil;
import com.procoin.task.BaseAsyncTask;
import com.procoin.util.CommonUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.util.VeDate;
import com.procoin.widgets.quotitian.entity.StockCoordinate;
import com.procoin.widgets.quotitian.entity.StockDayData;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by taojinroad on 16/8/24.
 */
public class StarKlineToLineChart extends KLineChartBaseView {
    private Context mContext;
    private final double N13_2 = 2 / 13.0;
    private final double N13_11 = 11 / 13.0;
    private final double N27_2 = 2 / 27.0;
    private final double N27_25 = 25 / 27.0;
    private final double P3_1 = 1 / 3.0;
    private final double P3_2 = 2 / 3.0;
    private static final long DEPLAY_MILLTIME = 500;// 延迟多少时间进行刷新
    private static final Object LOCK_OBJECT = new Object();
    private volatile int midflag;// 0代表vol，1代表macd,2代表dea
    private GetStockKlineTask mGetStockKlineTask;
    private GetStockHisKlineTask mGetStockHisKlineTask;
    private volatile List<StockCoordinate> coordinateList = new ArrayList<StockCoordinate>();// 保存当前要显示K线数据的坐标信息
    private final List<StockDayData> klineResultList = new ArrayList<StockDayData>();// 没有除权数组,最原始K线数据
    private String fdm;
    private String jc;
    private CropymelHttpSocket.KlineReqEnum klineReqEnum;
    private int cxNum; // 代表后台返回数据多余根数
    private Paint paint, volpaint, linePaint;
    private volatile boolean isOpenVol; // 是否要开启底部画线
    private volatile boolean isStartTask;// 是否正在请求数据
    private StockDayData midStockDayData;
    private int pointer;// 接触点数
    private GestureDetector gestureDetector;// 手势
    // private boolean canOnTouchKLine = true;// K线是否可以拖动,双击等OnTouch操作
    public boolean canScrollKLine = true;// 是否可以拖动K线
    private float distanceDown = 0;// 按下时两指间的距离
    private float distanceUp = 0;// 离开时两指间的距离
    private volatile boolean isShowWhiteLine;// 是否显示白线
    // private final int MINSPACE = 1;// 放大缩小时双指最小移动距离
    /**
     * 竖线位置所在游标
     */
    public int vLinePosition = 0;
    private float vLineX = 0, vLineY = 0;// 白竖线的X,y坐标
    private float left = 0, top = 0, right = 0, bottom = 0;// 白线背景框

    private KlineChartDataListener klineChartDataListener;
    private KlineRequestListener klineRequestListener;
    private KLineChartShowWhiteLine kLineChartShowWhiteLine;// 显示白线的回调
    private CompleteMsgKLine completeMsgKLine;
    private boolean canOnClickVol = true;// 代表是否可以点击

    private RectF rectF = new RectF();// 设置个长方形

    private ResultDataParser resultDataParser;

    public String screenType = "v";//屏幕类型 v是竖屏，h是横屏
    //    private String user_id;//接口的数据
    private boolean isEnd;//是否已经最后面了
    private Handler handler = new Handler();

    private String strStartDate = ""; //macd 左右的日期
    private String strEndDate = "";//macd 左右的日期

    public StarKlineToLineChart(Context context) {
        super(context);
        init(context, false);
    }

    public StockDayData getMidStockDayData() {
        return midStockDayData;
    }

    /**
     * new 一个可以拖动的K线图
     *
     * @param context
     * @param canScrollKLine
     */
    public StarKlineToLineChart(Context context, boolean canScrollKLine) {
        super(context);
        init(context, canScrollKLine);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context, boolean canScrollKLine) {
        this.mContext = context;
        this.canScrollKLine = canScrollKLine;
//        chartBgColor = Color.WHITE;
        gestureDetector = new GestureDetector(new GestureListener());
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setAntiAlias(true);/* 去锯齿 */
        paint.setTextSize(StockChartUtil.pxToSp(getResources(), StockChartUtil.DEFUALT_FONT_SIZE));
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setAntiAlias(true);/* 去锯齿 */
        linePaint.setStrokeWidth(2f);
        volpaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        volpaint.setAntiAlias(true);/* 去锯齿 */
        resultDataParser = new ResultDataParser();
    }

    public void setKlineChartDataListener(KlineChartDataListener klineChartDataListener) {
        this.klineChartDataListener = klineChartDataListener;
    }

    public void setCompleteMsgKLine(CompleteMsgKLine completeMsgKLine) {
        this.completeMsgKLine = completeMsgKLine;
    }

    public void setkLineChartShowWhiteLine(KLineChartShowWhiteLine kLineChartShowWhiteLine) {
        this.kLineChartShowWhiteLine = kLineChartShowWhiteLine;
    }

    public void setCanOnClickVol(boolean canOnClickVol) {
        this.canOnClickVol = canOnClickVol;
    }

    public void setKlineRequestListener(KlineRequestListener klineRequestListener) {
        this.klineRequestListener = klineRequestListener;
    }

    // TODO K线白线显示回调白线所在的一根数据
    public interface KlineChartDataListener {
        void returnWhiteLineIndex(boolean isWhiteLine, final StockDayData stockDayData);
    }

    // TODO K线白线显示请求监听
    public interface KLineChartShowWhiteLine {
        void showWhiteLine(boolean show);
    }

    // TODO K线数据网络请求监听
    public interface KlineRequestListener {
        void requestKlineStart();

        void requestKlineComplete();
    }


    @Override
    public void drawKlineChart(final Canvas canvas) {
        synchronized (LOCK_OBJECT) {
            // 画K线
            drawCandlesToLine(canvas);
            // 画白线
            drawWhiteLine(canvas);
        }
        if (completeMsgKLine != null) completeMsgKLine.refreshMsgKLine(true, false);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
//        CommonUtil.LogLa(3, "onTouchEvent  event.getAction()" + event.getAction());
        pointer = event.getPointerCount();
        switch (event.getAction()) {
            case MotionEvent.ACTION_POINTER_2_DOWN: // 两手第一次距离
                getParent().requestDisallowInterceptTouchEvent(true);
                if (kLineChartShowWhiteLine != null) kLineChartShowWhiteLine.showWhiteLine(true);
                distanceDown = (float) Math.hypot(event.getX(0) - event.getX(1), event.getY(0) - event.getY(1));
                break;
            case MotionEvent.ACTION_MOVE: // 拖动白线一起移动
                if (isShowWhiteLine) {
                    calculateLinePosition(event.getX(), event.getY());// 移动竖线
                }
                break;
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
//            com.taojin.http.util.CommonUtil.LogLa(2, "GoodsKlineChart onSingleTapConfirmed");
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
            int c = checkOnTouchRect(e.getY());
            if (c == 2) {// 触模到成交量的区域
//                if (canOnClickVol) startGetCjslMacdKdjTask();
            }
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
            CommonUtil.LogLa(3, "onScroll   pointer=" + pointer + " distanceX is " + distanceX + " distanceY is " + distanceY);
            if (pointer == 2) {// 两个手指交给这里处理
                if (isShowWhiteLine) return true;
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
                    Log.d("kline", "canScrollKLine is " + canScrollKLine);
                    if (canScrollKLine) {
                        calculateMovePosition(distanceX);// 手指拖动时,左右移动K线图
                        Log.d("kline", "displayNum is " + displayNum + " startCursor is " + startCursor);
                        if (!isEnd && !isStartTask && klineResultList.size() > 0 && startCursor <= 19 && distanceX < 0) {
                            CommonUtil.cancelAsyncTask(mGetStockHisKlineTask);
                            String date = "";
                            switch (klineReqEnum) {
                                case KLINE_5:
                                case KLINE_15:
                                case KLINE_30:
                                case KLINE_60:
                                    date = "20" + klineResultList.get(0).getDate() + "00";
                                    break;
                                default:
                                    date = String.valueOf(klineResultList.get(0).getDate());
                                    break;
                            }

                            mGetStockHisKlineTask = (GetStockHisKlineTask) new GetStockHisKlineTask(date).executeParams();
                        }
                        return true;
                    }
                }

            }
            return false;
        }
    }

    /**
     * 计算移动位置
     *
     * @param distanceX X方向的位移
     */
    private void calculateMovePosition(float distanceX) {
        synchronized (LOCK_OBJECT) {
            int size = klineResultList.size();
            if (size <= displayNum) return;
            int n = (int) (distanceX / (candlesWidth + spacing));
            if (n == 0) return;
            startCursor += n;
            refreshDisplayCursor();
            calculationCoordinate();
            invalidate();
        }
    }

    /**
     * 设置中间macd,cjsl,kdj的值
     */
    private void reSetMidByVolMacdKdjValue() {
        int size = size = klineResultList.size();
        if (size == 0) return;
        if (!isShowWhiteLine) {// 白线消失
            if (size > 0) midStockDayData = klineResultList.get(size - 1);
        } else {// 白线出现
            int index = startCursor + vLinePosition;
            if (index > size) midStockDayData = klineResultList.get(size - 1);
            else if (index < 0) midStockDayData = klineResultList.get(0);
            else midStockDayData = klineResultList.get(index);
        }
    }

    /**
     * 检测用户点击所在哪个区块
     *
     * @param y
     * @return
     */
    private int checkOnTouchRect(float y) {
        if (y < klineHeight) return 1;// 点击在K线图
        else if (y > (klineHeight + middleHeight + menuHeight) && y < totHeight) return 2;// 点击在K成交量
        return 0;
    }

    /**
     * 计算竖线位置
     */
    private void calculateLinePosition(float x, float y) {
        synchronized (LOCK_OBJECT) {
            int size = coordinateList.size();
            if (size == 0) return;
            float rightMaxX;
            float leftMaxX;
            leftMaxX = coordinateList.get(0).getCoordinateX();
            rightMaxX = coordinateList.get(size - 1).getCoordinateX();
            if (x < leftMaxX) x = leftMaxX;
            else if (x > rightMaxX) x = rightMaxX;
            int n = (int) (x / (candlesWidth + spacing));
            if (n >= size) n = size - 1;
            vLinePosition = n;
            vLineX = n * (candlesWidth + spacing) + candlesWidth / 2;
            if (vLinePosition >= 0 && vLinePosition < size) {
                vLineY = coordinateList.get(vLinePosition).getZjcjY();
            } else {
                vLineY = y;
            }

            reSetMidByVolMacdKdjValue();
            invalidate();
        }
    }

    /**
     * 画白线 新版改成蓝色
     *
     * @param canvas
     */
    private void drawWhiteLine(final Canvas canvas) {
        if (isShowWhiteLine) {
            paint.setColor(StockChartUtil.BULE);
            paint.setStrokeWidth(2.0f);
            // x线
            canvas.drawLine(vLineX, 0, vLineX, klineHeight, paint);
            canvas.drawLine(vLineX, klineHeight + middleHeight, vLineX, totHeight, paint);
            // y线
            if (vLineY < 0) vLineY = 0;
            else if (vLineY > klineHeight) {
                vLineY = klineHeight;
            }
            canvas.drawLine(0, vLineY, chartWidth, vLineY, paint);
            // 圆角左边长方形
            if (midStockDayData != null) {
                paint.setColor(StockChartUtil.KLINE_WHITELINE_BG);//
                paint.setStyle(Paint.Style.FILL);// 设置填满
                String v = formatNumber(priceDecimals, maxValue - vLineY * spanKlineY);
                int vhset = getFontHeight(paint) / 4;
                left = 0;
                top = vLineY - vhset - 5;
                if (top < 0) {
                    top = 0;
                    vLineY = vhset + 5;
                }
                right = paint.measureText(v) + 7;
                bottom = vLineY + vhset + 5;
                if (bottom > klineHeight) {
                    bottom = klineHeight;
                    vLineY = klineHeight - vhset - 5;
                    top = vLineY - vhset - 5;
                }
                // 画时间
                if ((right * 3) > vLineX) {
                    if (right > vLineX) vLineX = right;
                    left = chartWidth - right;
                    top = vLineY - vhset - 5;
                    right = chartWidth;
                    bottom = vLineY + vhset + 5;
                } else {
                    float endTimex = chartWidth - right;
                    if (vLineX > endTimex) vLineX = endTimex;
                }
                drawTextTime(canvas, strIntToFormate(String.valueOf(midStockDayData.getDate())), vLineX, totHeight - footHeight + 18, Paint.Align.CENTER);
                // 画背景框+数字
                rectF.set(left, top, right, bottom);
                canvas.drawRoundRect(rectF, 4, 4, paint);
                paint.setColor(Color.WHITE);
                canvas.drawText(v, left + 4, vLineY + vhset, paint);
            }
        }
    }

    /**
     * 画蜡烛
     *
     * @param canvas
     */
    private void drawCandlesToLine(final Canvas canvas) {
        StockCoordinate pastKC = null;
        for (StockCoordinate kc : coordinateList) {
            if (kc.getCoordinateX() >= 0 && kc.getCoordinateX() <= chartWidth) {
//                if (kc.getAmt() > 0) {// 坐标比较,真实价大的坐标小,所以真实价的收盘大于开盘,坐标比的话就是小于
//                    // 收盘价大于开盘价
//                    paint.setColor(StockChartUtil.ZHANG);// 颜色
//                    paint.setStrokeWidth(2);// 空心矩形边框线宽
//                    paint.setStyle(klinePaintStyle);// 原来是 空心 Paint.Style.STROKE
//                    canvas.drawLine(kc.getCoordinateX(), kc.getZgcjY(), kc.getCoordinateX(), kc.getZjcjY(), paint);
//                    canvas.drawLine(kc.getCoordinateX(), kc.getJrkpY(), kc.getCoordinateX(), kc.getZdcjY(), paint);
//                    if (kc.getZjcjY() < kc.getJrkpY()) {
//                        canvas.drawRect(kc.getCoordinateX() - candlesWidth / 2, kc.getZjcjY(), kc.getCoordinateX() + candlesWidth / 2, kc.getJrkpY(), paint);
//                    } else {
//                        canvas.drawRect(kc.getCoordinateX() - candlesWidth / 2, kc.getJrkpY(), kc.getCoordinateX() + candlesWidth / 2, kc.getZjcjY(), paint);
//                    }
//                } else if (kc.getAmt() < 0) {// 坐标比较,真实价大的坐标小,所以真实价的收盘小于开盘,坐标比的话就是大于
//                    // 收盘价小于开盘价
//                    paint.setColor(StockChartUtil.DIE);// 颜色
//                    paint.setStrokeWidth(2);
//                    canvas.drawLine(kc.getCoordinateX(), kc.getZgcjY(), kc.getCoordinateX(), kc.getJrkpY(), paint);
//                    canvas.drawLine(kc.getCoordinateX(), kc.getZjcjY(), kc.getCoordinateX(), kc.getZdcjY(), paint);
//                    if (Math.abs(kc.getJrkpY() - kc.getZjcjY()) > 2)
//                        paint.setStyle(Paint.Style.FILL);// 实心
//                    else paint.setStyle(klinePaintStyle);// 空心
//                    // Paint.Style.STROKE
//                    if (kc.getZjcjY() < kc.getJrkpY()) {
//                        canvas.drawRect(kc.getCoordinateX() - candlesWidth / 2, kc.getZjcjY(), kc.getCoordinateX() + candlesWidth / 2, kc.getJrkpY(), paint);
//                    } else {
//                        canvas.drawRect(kc.getCoordinateX() - candlesWidth / 2, kc.getJrkpY(), kc.getCoordinateX() + candlesWidth / 2, kc.getZjcjY(), paint);
//                    }
//                } else {// 当开盘价和收盘价不同
//                    paint.setColor(StockChartUtil.ZHANG);
//                    paint.setStrokeWidth(2);
//                    canvas.drawLine(kc.getCoordinateX(), kc.getZgcjY(), kc.getCoordinateX(), kc.getZdcjY(), paint);
//                    canvas.drawLine((kc.getCoordinateX() - candlesWidth / 2), kc.getZjcjY(), kc.getCoordinateX() + candlesWidth / 2, kc.getZjcjY(), paint);
//                }
                if (pastKC != null) {
                    canvas.drawLine(pastKC.getCoordinateX(), pastKC.getZjcjY(), kc.getCoordinateX(), kc.getZjcjY(), linePaint);
                }
//                if (pastKC != null) drawCandlesM(canvas, pastKC, kc);
//                if (midflag == 0) drawCjsl(canvas, pastKC, kc);// 底部成效图
//                else if (midflag == 1) drawMACD(canvas, pastKC, kc);// 底部MACD成效图
//                else if (midflag == 2) drawKDJ(canvas, pastKC, kc);// 底部KDJ成效图
                drawCjsl(canvas, pastKC, kc);// 底部成效图
                pastKC = kc;
            }
        }
        if (midStockDayData != null) {
            if (midflag == 0)
                drawTextVol(canvas, "总:" + midStockDayData.getCjsl() , "M5:" + midStockDayData.getVm5(), "M10:" +midStockDayData.getVm10());
            else if (midflag == 1)
                drawTextMACD(canvas, "MACD(12,26,9)", "DIF:" + formatNumber(3, midStockDayData.getDif()), "DEA:" + formatNumber(3, midStockDayData.getDea()), midStockDayData.getBar());
            else if (midflag == 2)
                drawTextKDJ(canvas, "KDJ(9,3,3)", "K:" + formatNumber(3, midStockDayData.getKdjk()), "D:" + formatNumber(3, midStockDayData.getKdjd()), "J:" + formatNumber(3, midStockDayData.getKdjj()));
            else drawTextVol(canvas, "总:0.000", "M5:0.000", "M10:0.000");
        } else {
            drawTextVol(canvas, "总:0.000", "M5:0.000", "M10:0.000");
        }
        if (!isShowWhiteLine) {
            int size = coordinateList.size();
            if (size > 0) {//这个是2macd上的2个日期
                if (klineReqEnum == CropymelHttpSocket.KlineReqEnum.KLINE_DAY || klineReqEnum == CropymelHttpSocket.KlineReqEnum.KLINE_WEEK) {
                    strStartDate = strIntToFormate(String.valueOf(coordinateList.get(0).getDate()));
                    strEndDate = strIntToFormate(String.valueOf(coordinateList.get(size - 1).getDate()));
                } else {
                    strStartDate = VeDate.getDateToMMddHHmm(VeDate.strLongToDate("20" + coordinateList.get(0).getDate() + "00"));
                    strEndDate = VeDate.getDateToMMddHHmm(VeDate.strLongToDate("20" + coordinateList.get(size - 1).getDate() + "00"));
                }
                float dateHeight = totHeight - footHeight-timeTextHeight/2  + (menuHeight == 0 ? 0 : (menuHeight / 2));
                drawTextTime(canvas, strStartDate, coordinateList.get(0).getCoordinateX(), dateHeight, Paint.Align.LEFT);
                float minTimeX = coordinateList.get(size - 1).getCoordinateX();
                Log.d("StarKlineChart","minTimeX=="+minTimeX);
                float textWidth=paint.measureText(strStartDate);
                Log.d("StarKlineChart","textWidth=="+textWidth);
                if (minTimeX < 450) minTimeX = 450f;
                Log.d("StarKlineChart","minTimeX  end=="+minTimeX);
                if (size >= 2)
                    drawTextTime(canvas, strEndDate, minTimeX, dateHeight, Paint.Align.RIGHT);
            }
        }

        if (klineChartDataListener != null) {
            if (midStockDayData != null) {
                midStockDayData.setJc(jc);
                midStockDayData.setFdm(fdm);
            }
            klineChartDataListener.returnWhiteLineIndex(isShowWhiteLine, midStockDayData);
        }
    }

    /**
     * 画K线图中的M5,M10,M20线
     *
     * @param canvas
     * @param kc
     */
    private void drawCandlesM(Canvas canvas, StockCoordinate pastKC, StockCoordinate kc) {
        paint.setStrokeWidth(1.5f);
        // M5
        if (pastKC.getM5Y() <= klineHeight && kc.getM5Y() <= klineHeight && pastKC.getM5Y() >= 0 && kc.getM5Y() >= 0) {
            paint.setColor(StockChartUtil.M5_COLOR);// 白色
            canvas.drawLine(pastKC.getCoordinateX(), pastKC.getM5Y(), kc.getCoordinateX(), kc.getM5Y(), paint);
        }
        // M10
        if (pastKC.getM10Y() <= klineHeight && kc.getM10Y() <= klineHeight && pastKC.getM10Y() >= 0 && kc.getM10Y() >= 0) {
            paint.setColor(StockChartUtil.YELLOW);// 黄色
            canvas.drawLine(pastKC.getCoordinateX(), pastKC.getM10Y(), kc.getCoordinateX(), kc.getM10Y(), paint);
        }
        // M20
        if (pastKC.getM30Y() <= klineHeight && kc.getM30Y() <= klineHeight && pastKC.getM30Y() >= 0 && kc.getM30Y() >= 0) {
            paint.setColor(StockChartUtil.PURPLE);// 紫色
            canvas.drawLine(pastKC.getCoordinateX(), pastKC.getM30Y(), kc.getCoordinateX(), kc.getM30Y(), paint);
        }
    }

    /**
     * 画成交量图
     *
     * @param canvas
     */
    private void drawCjsl(Canvas canvas, StockCoordinate pastKC, StockCoordinate kc) {
        if (midflag != 0 || !isOpenVol) return;
        volpaint.setStrokeWidth(2);// 空心矩形边框线宽
//        if (kc.getAmt() < 0) {// 开盘价大于收盘价
//            if ((kc.getCoordinateX() - candlesWidth / 2) != (kc.getCoordinateX() + candlesWidth / 2))
//                volpaint.setStyle(Paint.Style.FILL);// 实心
//            else volpaint.setStyle(klinePaintStyle);// 空心 Paint.Style.STROKE
//            volpaint.setColor(StockChartUtil.DIE);/* 设定paint的颜色 */
//        } else {
//            volpaint.setColor(StockChartUtil.ZHANG);/* 设定paint的颜色 */
//            volpaint.setStyle(klinePaintStyle);// 空心 Paint.Style.STROKE
//        }
        volpaint.setStyle(Paint.Style.FILL);// 实心
        volpaint.setColor(StockChartUtil.BULE);/* 设定paint的颜色 */
        canvas.drawRect(kc.getCoordinateX() - candlesWidth / 2, totHeight - footHeight + kc.getCjslY(), kc.getCoordinateX() + candlesWidth / 2, totHeight, volpaint);
        if (pastKC != null) {
            volpaint.setStrokeWidth(1.5f);
            if (pastKC.getVM5Y() <= footHeight) {// M5
                volpaint.setColor(StockChartUtil.M5_COLOR);
                canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getVM5Y(), kc.getCoordinateX(), totHeight - footHeight + kc.getVM5Y(), volpaint);
            }
            if (pastKC.getVM10Y() <= footHeight) {// M10
                volpaint.setColor(StockChartUtil.YELLOW);
                canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getVM10Y(), kc.getCoordinateX(), totHeight - footHeight + kc.getVM10Y(), volpaint);
            }
        }
    }

    /**
     * 画MACD
     *
     * @param canvas
     */
    private void drawMACD(Canvas canvas, StockCoordinate pastKC, StockCoordinate kc) {
        if (midflag != 1 || !isOpenVol) return;
        if (pastKC != null) {
            // dif
            volpaint.setStrokeWidth(1.5f);
            volpaint.setColor(StockChartUtil.M5_COLOR);// Color.WHITE
            canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getDifY(), kc.getCoordinateX(), totHeight - footHeight + kc.getDifY(), volpaint);
            // dea
            volpaint.setColor(StockChartUtil.YELLOW);
            canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getDeaY(), kc.getCoordinateX(), totHeight - footHeight + kc.getDeaY(), volpaint);
        }
        // bar
        volpaint.setColor(kc.getBarY() <= footHeight / 2 ? StockChartUtil.ZHANG : StockChartUtil.DIE);
        volpaint.setStrokeWidth(3.0f);
        canvas.drawLine(kc.getCoordinateX(), totHeight - footHeight + kc.getBarY(), kc.getCoordinateX(), totHeight - footHeight + footHeight / 2, volpaint);
    }

    /**
     * 画KDJ
     *
     * @param canvas
     */
    private void drawKDJ(Canvas canvas, StockCoordinate pastKC, StockCoordinate kc) {
        if (midflag != 2 || !isOpenVol) return;
        volpaint.setStrokeWidth(2.0f);
        if (pastKC != null) {
            // KDJ中的K线
            volpaint.setColor(StockChartUtil.M5_COLOR);// 白 改成灰
            canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getKdjKY(), kc.getCoordinateX(), totHeight - footHeight + kc.getKdjKY(), volpaint);
            // KDJ中的D线
            volpaint.setColor(StockChartUtil.YELLOW);// 黄
            canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getKdjDY(), kc.getCoordinateX(), totHeight - footHeight + kc.getKdjDY(), volpaint);
            // KDJ中的J线
            volpaint.setColor(StockChartUtil.PURPLE);// 紫
            canvas.drawLine(pastKC.getCoordinateX(), totHeight - footHeight + pastKC.getKdjJY(), kc.getCoordinateX(), totHeight - footHeight + kc.getKdjJY(), volpaint);
        }
    }

    /**
     * 设置点击底部事件，就是切换macd,cjsl,kdj
     */
    private void startGetCjslMacdKdjTask() {
        synchronized (LOCK_OBJECT) {
            if (fdm == null || !isOpenVol) return;
            if (klineResultList.size() == 0) return;
            String chType = "";
            if (midflag == 0) {
                midflag = 1;
                chType = "macd";
            } else if (midflag == 1) {
                midflag = 2;
                chType = "kdj";
            } else if (midflag == 2) {
                midflag = 0;
                chType = "vol";
            }
            TjrSocialMTAUtil.trackCustomKVEvent(getContext(), "交易页K线图指标点击", chType, TjrSocialMTAUtil.MTATradeKLineIndexTypeClick);
            calculationCoordinate();
            postInvalidateDelayed(DEPLAY_MILLTIME);
            return;
        }
    }

    //清除所有数据
    public void clearAllData() {
        klineResultList.clear();
        coordinateList.clear();// 保存当前要显示K线数据的坐标信息
        invalidate();
    }

    /**
     * 请求K线数据
     *
     * @param klineReqEnum
     * @param fdm
     * @param dateTime
     * @return
     */
    public synchronized boolean startGetStockKlineTask(CropymelHttpSocket.KlineReqEnum klineReqEnum, String fdm, String dateTime) {
        if (fdm == null) return false;
        if (isStartTask) return false;
        isEnd = false;//看是不是拖动到最后
        midStockDayData = null;
        isOpenVol = false;// 关闭底部画图功能
        isShowWhiteLine = false;
        synchronized (LOCK_OBJECT) {
            klineResultList.clear();
            coordinateList.clear();// 保存当前要显示K线数据的坐标信息
            invalidate();
        }
        this.klineReqEnum = klineReqEnum;
//        midflag = 0;
        this.fdm = fdm;
        if (kLineChartShowWhiteLine != null) kLineChartShowWhiteLine.showWhiteLine(false);
        CommonUtil.cancelAsyncTask(mGetStockKlineTask);
        mGetStockKlineTask = (GetStockKlineTask) new GetStockKlineTask(dateTime).executeParams();
        return true;
    }

    public void cancelAsyncRunTask() {
        CommonUtil.cancelAsyncTask(mGetStockKlineTask);
    }

    /**
     * 获取K线数据历史数据
     *
     * @author luke
     */
    private class GetStockKlineTask extends BaseAsyncTask<String, Void, String> {
        private Exception mReason;
        private final String dateTime;


        public GetStockKlineTask(String dateTime) {
            this.dateTime = dateTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isStartTask = true;
            if (klineRequestListener != null) klineRequestListener.requestKlineStart();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //WeipanHttp.getInstance().weipanKline(projectName, klineReqEnum, user_id, fdm, dateTime, token, mobile, mars_cid, screenType);
                String result = null;
                if (klineReqEnum == CropymelHttpSocket.KlineReqEnum.KLINE_DAY ) {
                    result = CropymelHttpSocket.getInstance().klineGetDay(fdm, screenType, dateTime);
                }else if(klineReqEnum == CropymelHttpSocket.KlineReqEnum.KLINE_WEEK){
                    result = CropymelHttpSocket.getInstance().klineGetWeek(fdm, screenType, dateTime);
                } else {
                    result = CropymelHttpSocket.getInstance().klineGetHour(fdm,  screenType, dateTime);
                }
                Log.d("kline", "GetStockKlineTask is " + result);
                parserJsonStockKline(result);

                return "1";
            } catch (Exception e) {
                mReason = e;
                Log.d("kline", "Exception is " + e.getMessage());
                fdm = null;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Log.d("onexception", "GetStockKlineTask/////");
                NotificationsUtil.ToastReasonForFailure(mContext, mReason);
            }
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (klineRequestListener != null) klineRequestListener.requestKlineComplete();
                    postInvalidateDelayed(DEPLAY_MILLTIME);
                }
            }, 500);
            isStartTask = false;
        }

        protected void parserJsonStockKline(final String jsonsStr) throws Exception {
            synchronized (LOCK_OBJECT) {
                JSONObject json = new JSONObject(jsonsStr);
                if (hasNotNullAndIsIntOrLong(json, "cxNum")) {// K线数据,必要先有K线数据才能得出下面数据
                    cxNum = json.getInt("cxNum");
                }
                if (hasAndNotNull(json, "isEnd")) {//月线只有那么多
                    isEnd = json.getBoolean("isEnd");
                }
                if (hasAndNotNull(json, "kline")) {// K线数据
                    klineResultList.clear();
                    parserKline(json.getString("kline"), null);
                    sortKlineListAsc(klineResultList);
                    int klineSize = klineResultList.size();
                    if (klineSize > 0)
                        midStockDayData = klineResultList.get(klineResultList.size() - 1);
                    if (displayNum <= klineSize) {
                        startCursor = klineSize - displayNum;
                    } else {
                        startCursor = 0;
                    }

                    for (StockDayData item : klineResultList) {
                        //1日期,2开盘,3最高,4最低,5收盘(最近成交),成交量
                        Log.d("kline", " 日期 is " + item.getDate() + " 2开盘 " + item.getJrkp() + " 最高 " + item.getZgcj() + " 最低 " + item.getZdcj() + " 收盘(最新价) is " + item.getZjcj() + " 成交量 is " + item.getCjsl());
                    }

                    Log.d("kline", "GetStockKlineTask is " + klineResultList.size() + "startCursor is " + startCursor + " displayNum is " + displayNum + " spanX is " + spanX);
                    refreshDisplayCursor();
                }
                calculationCoordinate();
                isOpenVol = true;// 打开底部画图功能
            }
        }
    }


    /**
     * 获取K线数据更久历史数据
     *
     * @author luke
     */
    private class GetStockHisKlineTask extends BaseAsyncTask<String, Void, String> {
        private Exception mReason;
        //        private final String user_id;
        private final String dateTime;

        public GetStockHisKlineTask(String dateTime) {
//            this.user_id = user_id;
            this.dateTime = dateTime;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isStartTask = true;
            if (klineRequestListener != null) klineRequestListener.requestKlineStart();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                String result = null;
                if (klineReqEnum == CropymelHttpSocket.KlineReqEnum.KLINE_DAY|| klineReqEnum == CropymelHttpSocket.KlineReqEnum.KLINE_WEEK) {
                    result = CropymelHttpSocket.getInstance().klineGetDay(fdm, screenType, "0");
                } else {
                    result = CropymelHttpSocket.getInstance().klineGetHour(fdm,  screenType, "0");
                }
//                WeipanHttp.getInstance().weipanKline("", klineReqEnum, user_id, fdm, dateTime, screenType);
                parserJsonHisStockKline(result);
                return "1";
            } catch (Exception e) {
                mReason = e;
                fdm = null;
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Log.d("onexception", "GetStockKlineTask/////");
                NotificationsUtil.ToastReasonForFailure(mContext, mReason);
            }
            if (klineRequestListener != null) klineRequestListener.requestKlineComplete();
            postInvalidateDelayed(DEPLAY_MILLTIME);
            isStartTask = false;
        }

        protected void parserJsonHisStockKline(final String jsonsStr) throws Exception {
            synchronized (LOCK_OBJECT) {
                ResultData resultData = resultDataParser.parse(new JSONObject(jsonsStr));
                if (resultData.isSuccess()) {
                    JSONObject json = resultData.returnJSONObject();
//                    if (hasAndNotNull(json, "jc")) {// K线数据
//                        setJc(json.getString("jc"));
//                    }
//                    if (hasAndNotNull(json, "fdm")) {// K线数据,必要先有K线数据才能得出下面数据
//                        setFdm(json.getString("fdm"));
//                    }
//                    if (hasNotNullAndIsIntOrLong(json, "cxNum")) {// K线数据,必要先有K线数据才能得出下面数据
//                        cxNum = json.getInt("cxNum");
//                    }
                    if (hasAndNotNull(json, "isEnd")) {// K线数据
                        isEnd = json.getBoolean("isEnd");
                    }

                    if (hasAndNotNull(json, "kline")) {// K线数据
//                        klineResultList.clear();
                        Log.d("kline", "parserJsonHisStockKline is " + klineResultList.size());
                        int oldListSize = klineResultList.size();
                        parserKline(json.getString("kline"), null);
                        sortKlineListAsc(klineResultList);
                        Log.d("kline", "sortKlineListAsc is " + klineResultList.size());
                        if (klineResultList.size() > 0)
                            midStockDayData = klineResultList.get(klineResultList.size() - 1);
//                        startCursor = klineResultList.size() - displayNum;
                        if (oldListSize < klineResultList.size()) {//如果解析后大于旧的就要计算开始的游标
                            startCursor = klineResultList.size() - (oldListSize - startCursor);
                        }
                        Log.d("kline", "startCursor is " + startCursor);


                        refreshDisplayCursor();
                    }
                    calculationCoordinate();
                    isOpenVol = true;// 打开底部画图功能
                }
            }
        }
    }


    /**
     * 初始化K线游标
     */
    private void refreshDisplayCursor() {
        calculateDisplayNum();
        int size = klineResultList.size();
        if (size == 0) return;
        int limitIndex = cxNum;
        if (limitIndex < 0) limitIndex = 0;
        if (displayNum >= size) {
            endCursor = size;
            startCursor = limitIndex;
        } else {
            if (startCursor < limitIndex) startCursor = limitIndex;
            endCursor = startCursor + displayNum;
            if (size == 0 || startCursor >= size || endCursor < startCursor) return;
            if (endCursor > size) {
                endCursor = size;
                startCursor = endCursor - displayNum;
                if (startCursor < limitIndex) startCursor = limitIndex;
                if (size == 0 || startCursor >= size || endCursor < startCursor) return;
            }
        }
    }

    /**
     * 计算分析K线换算坐标
     */
    private void calculationCoordinate() {
        try {
            synchronized (LOCK_OBJECT) {
                List<StockDayData> list = calculationAllMaxAndMin();// calculationMaxAndMin在calculateDisplayNum之前调用
                if (list == null) return;
                calculateDisplayNum();// calculateDisplayNum要在calculationMaxAndMin后
                coordinateList.clear();
                StockCoordinate coordinate;
                float coordinateX = candlesWidth / 2;
                for (StockDayData data : list) {
                    coordinate = new StockCoordinate();
                    coordinate.setDate(data.getDate());
                    coordinate.setCoordinateX(coordinateX);
                    coordinate.setZgcjY(getCharPixelY(maxValue, spanKlineY, data.getZgcj()));// 最大值
                    coordinate.setZdcjY(getCharPixelY(maxValue, spanKlineY, data.getZdcj()));// 最小值
                    coordinate.setJrkpY(getCharPixelY(maxValue, spanKlineY, data.getJrkp()));// 开盘
                    coordinate.setZjcjY(getCharPixelY(maxValue, spanKlineY, data.getZjcj()));// 收盘
                    coordinate.setM5Y(getCharPixelY(maxValue, spanKlineY, data.getM5()));// M5
                    coordinate.setM10Y(getCharPixelY(maxValue, spanKlineY, data.getM10()));// M10
                    coordinate.setM30Y(getCharPixelY(maxValue, spanKlineY, data.getM30()));// M20
                    coordinate.setAmt(data.getZjcj(), data.getJrkp());
                    if (midflag == 1) {// macd 日期,dif,dea,bar
                        // dif
                        coordinate.setDifY(getVolumePixelY(macdMax, spanMACDY, data.getDif()));
                        // dea
                        coordinate.setDeaY(getVolumePixelY(macdMax, spanMACDY, data.getDea()));
                        // bar
                        coordinate.setBarY(getVolumePixelY(macdMax, spanMACDY, data.getBar()));
                    } else if (midflag == 2) {// kdj 日期,k,d,j
                        // K
                        coordinate.setKdjKY(getVolumePixelY(kdjMax, spanKDJY, data.getKdjk()));
                        // D
                        coordinate.setKdjDY(getVolumePixelY(kdjMax, spanKDJY, data.getKdjd()));
                        // J
                        coordinate.setKdjJY(getVolumePixelY(kdjMax, spanKDJY, data.getKdjj()));
                    } else if (midflag == 0) {// cjsl 日期,成交量,vm5,vm20
                        // cjsl
                        coordinate.setCjslY(getVolumeY(cjslMax, spanCjslY, data.getCjsl()));
                        // VM5
                        coordinate.setVM5Y(getVolumePixelY(cjslMax, spanCjslY, Double.parseDouble(data.getVm5())));
                        // VM10
                        coordinate.setVM10Y(getVolumePixelY(cjslMax, spanCjslY, Double.parseDouble(data.getVm10())));
                    }
                    coordinateList.add(coordinate);// 将转换后的结果缓存
                    coordinateX += spanX;// X坐标自增到下一条位置
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * 计算最大值和最小值,只有一次调用
     */
    private List<StockDayData> calculationAllMaxAndMin() {
        synchronized (LOCK_OBJECT) {
            boolean isKlineFirst = true;
            int size = klineResultList.size();
            if (size == 0) return null;
            if (startCursor > endCursor) return null;
            List<StockDayData> list = klineResultList.subList(startCursor, endCursor);
            if (list == null) return null;
            double kMax = Integer.MIN_VALUE, dMax = Integer.MIN_VALUE, jMax = Integer.MIN_VALUE, kMin = Integer.MAX_VALUE, dMin = Integer.MAX_VALUE, jMin = Integer.MAX_VALUE;
            double difMax = Double.MIN_VALUE, deaMax = Double.MIN_VALUE, barMax = Double.MIN_VALUE, difMin = Double.MAX_VALUE, deaMin = Double.MAX_VALUE, barMin = Double.MAX_VALUE;
            maxValue = 0;
            minValue = 0;
            cjslMax = 0;
            for (StockDayData data : list) {
                // 对K线
                if (isKlineFirst) {
                    maxValue = data.getZgcj();
                    if (data.getZdcj() > 0) minValue = data.getZdcj();
                    isKlineFirst = !(maxValue > 0 && minValue > 0);
//                    CommonUtil.LogLa(2, "isKlineFirst start maxValue is " + maxValue + " minValue is " + minValue);
//
//                    CommonUtil.LogLa(2, "isKlineFirst end maxValue is " + maxValue + " minValue is " + minValue);
                }
                if (data.getZgcj() > 0) maxValue = Math.max(maxValue, data.getZgcj());
                if (data.getZdcj() > 0) minValue = Math.min(minValue, data.getZdcj());
                if (data.getM5() > 0) {
                    maxValue = Math.max(maxValue, data.getM5());
                    minValue = Math.min(minValue, data.getM5());
                }
                if (data.getM10() > 0) {
                    maxValue = Math.max(maxValue, data.getM10());
                    minValue = Math.min(minValue, data.getM10());
                }
                if (data.getM30() > 0) {
                    maxValue = Math.max(maxValue, data.getM30());
                    minValue = Math.min(minValue, data.getM30());
                }
                // 对成交量
                if (midflag == 0) {
                    cjslMax = Math.max(cjslMax, Double.parseDouble(data.getCjsl()));
                    cjslMax = (long) Math.max(cjslMax, Double.parseDouble(data.getVm5()));
                    cjslMax = (long) Math.max(cjslMax, Double.parseDouble(data.getVm10()));
                } else if (midflag == 1) {
                    // MACD最大值
                    difMax = Math.max(difMax, data.getDif());
                    deaMax = Math.max(deaMax, data.getDea());
                    barMax = Math.max(barMax, data.getBar());
                    // MACD最小值
                    difMin = Math.min(difMin, data.getDif());
                    deaMin = Math.min(deaMin, data.getDea());
                    barMin = Math.min(barMin, data.getBar());
                } else if (midflag == 2) {
                    // kdj最大值
                    kMax = Math.max(kMax, data.getKdjk());
                    dMax = Math.max(dMax, data.getKdjd());
                    jMax = Math.max(jMax, data.getKdjj());
                    // kdj最小值
                    kMin = Math.min(kMin, data.getKdjk());
                    dMin = Math.min(dMin, data.getKdjd());
                    jMin = Math.min(jMin, data.getKdjj());
                }
            }
            if (maxValue == minValue) {
                maxValue = maxValue + maxValue * 0.01;
                minValue = minValue - minValue * 0.01;
            }
            if (midflag == 1) {
                macdMax = Math.max(difMax, deaMax);
                macdMax = Math.max(macdMax, barMax);
                macdMin = Math.min(difMin, deaMin);
                macdMin = Math.min(macdMin, barMin);
                // MACD的最大 最小值和0之间的跨度一样
                macdMax = Math.max(macdMax, Math.abs(macdMin));
                macdMin = macdMax * -1;
            } else if (midflag == 2) {
                kdjMax = Math.max(kMax, dMax);
                kdjMax = Math.max(kdjMax, jMax);
                kdjMin = Math.min(kMin, dMin);
                kdjMin = Math.min(kdjMin, jMin);
            }
            return list;
        }
    }

    private void parserKline(String result, StockDayData oldData) throws Exception {
        try {
            // 1日期,2开盘,3最高,4最低,5收盘(最近成交),成交量
            final String regex = "(\\d{8,14}),([-]??[0-9]*[.]??[0-9E-]*),([-]??[0-9]*[.]??[0-9E-]*),([-]??[0-9]*[.]??[0-9E-]*),([-]??[0-9]*[.]??[0-9E-]*),([-]??[0-9]*[.]??[0-9E-]*);"; // 总
            final Pattern pa = Pattern.compile(regex, Pattern.DOTALL); // 总
            final Matcher ma = pa.matcher(result);
            String time;
            while (ma.find()) {
                if (ma.groupCount() >= 5) {
                    StockDayData data = new StockDayData(Double.parseDouble(ma.group(2)), Double.parseDouble(ma.group(5)), Double.parseDouble(ma.group(3)), Double.parseDouble(ma.group(4)), ma.group(6));
                    time = ma.group(1);
                    if (time.length() >= 12) {
                        data.setDate(Integer.parseInt(time.substring(2, 12)));
                    } else {
                        data.setDate(Integer.parseInt(time));
                    }
                    if (oldData != null) {
                        if (data.getDate() == oldData.getDate()) {
                            data.setEma12(oldData.getEma12());
                            data.setEma26(oldData.getEma26());
                            data.setDif(oldData.getDif());
                            data.setDea(oldData.getDea());
                            data.setBar(oldData.getBar());
                            data.setKdjk(oldData.getKdjk());
                            data.setKdjd(oldData.getKdjd());
                            data.setKdjj(oldData.getKdjj());
                            data.setM5(oldData.getM5());
                            data.setM10(oldData.getM10());
                            data.setM30(oldData.getM30());
                            data.setVm5(oldData.getVm5());
                            data.setVm10(oldData.getVm10());
                            data.setIsHas(true);
                        }
                    }
                    klineResultList.add(data);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private void sortKlineListAsc(List<StockDayData> dayDatas) {
        // 升序计算K线
        Collections.sort(dayDatas, new Comparator<StockDayData>() {
            @Override
            public int compare(StockDayData s1, StockDayData s2) {
                return (int)(s1.getDate() - s2.getDate());
            }
        });
        StockDayData pastData = null;
        double rate = 0.00, amt = 0.00;
        LinkedList<StockDayData> rsv9List = new LinkedList<StockDayData>();
        LinkedList<StockDayData> mv5List = new LinkedList<StockDayData>();
        LinkedList<StockDayData> mv10List = new LinkedList<StockDayData>();
        LinkedList<StockDayData> m30List = new LinkedList<StockDayData>();
        double ema12 = 0, ema26 = 0, dif = 0, dea = 0, bar = 0, max9Value = 0, min9Value = 0, k = 0, d = 0, rsv = 0, j = 0, m5 = 0, vm5 = 0, m10 = 0, vm10 = 0, m20 = 0;
        double tolm5 = 0, tolmv5 = 0, tolm10 = 0, tolmv10 = 0, tolm20 = 0;
        for (StockDayData dayData : dayDatas) {
            if (pastData != null) {
                if (pastData.getZjcj() > 0) {
                    if (dayData.getJrkp() <= 0 || dayData.getZjcj() <= 0) {
                        dayData.setJrkp(pastData.getZjcj());
                        dayData.setZjcj(pastData.getZjcj());
                        dayData.setZgcj(pastData.getZjcj());
                        dayData.setZdcj(pastData.getZjcj());
                    }
                    amt = dayData.getZjcj() - pastData.getZjcj(); // 最新成交价-昨日收盘价
                    rate = amt * 100 / pastData.getZjcj(); // (最新成交价-昨日收盘价)/昨日收盘价
                    if (rate <= -100) {
                        rate = 0;
                        amt = 0;
                    }
                    dayData.setRate(rate);
                    dayData.setAmt(amt);
                    dayData.setZrsp(pastData.getZjcj());
                }
            }
            // 计算M5,MV5
            if (mv5List.size() == 4) {
                mv5List.add(dayData);
                tolm5 = 0;
                tolmv5 = 0;
                for (StockDayData sd : mv5List) {
                    tolm5 = tolm5 + sd.getZjcj();
                    tolmv5 = tolmv5 + Double.parseDouble(sd.getCjsl());
                }
                m5 = tolm5 / 5.0;
                vm5 = tolmv5 / 5.0;
                dayData.setM5(formatNumberToDouble(4, m5));
                dayData.setVm5(formatNumberToString(4, vm5));
                mv5List.removeFirst();
            } else {
                mv5List.add(dayData);
            }
            // 计算M10,MV10
            if (mv10List.size() == 9) {
                mv10List.add(dayData);
                tolm10 = 0;
                tolmv10 = 0;
                for (StockDayData sd : mv10List) {
                    tolm10 = tolm10 + sd.getZjcj();
                    tolmv10 = tolmv10 + Double.parseDouble(sd.getCjsl());
                }
                m10 = tolm10 / 10;
                vm10 = tolmv10 / 10;
                dayData.setM10(formatNumberToDouble(4, m10));
                dayData.setVm10(formatNumberToString(4, vm10));
                mv10List.removeFirst();
            } else {
                mv10List.add(dayData);
            }
            // M20
            if (m30List.size() == 29) {
                m30List.add(dayData);
                tolm20 = 0;
                for (StockDayData sd : m30List) {
                    tolm20 = tolm20 + sd.getZjcj();
                }
                m20 = tolm20 / 30;
                dayData.setM30(formatNumberToDouble(4, m20));
                m30List.removeFirst();
            } else {
                m30List.add(dayData);
            }
            rsv9List.add(dayData);
            if (!dayData.getIsHas()) {
                // 计算MACD
                if (pastData == null) {
                    dayData.setEma12(dayData.getZjcj());
                    dayData.setEma26(dayData.getZjcj());
                } else {
                    ema12 = N13_2 * dayData.getZjcj() + pastData.getEma12() * N13_11;
                    ema26 = N27_2 * dayData.getZjcj() + pastData.getEma26() * N27_25;
                    dif = ema12 - ema26;
                    dea = dif * 0.2 + pastData.getDea() * 0.8;
                    bar = 2 * (dif - dea);
                    dayData.setEma12(formatNumberToDouble(4, ema12));
                    dayData.setEma26(formatNumberToDouble(4, ema26));
                    dayData.setDif(formatNumberToDouble(4, dif));
                    dayData.setDea(formatNumberToDouble(4, dea));
                    dayData.setBar(formatNumberToDouble(4, bar));
                }
                // 计算KDJ
                if (pastData == null) {
                    if (dayData.getZgcj() == dayData.getZdcj()) rsv = 50;
                    else
                        rsv = (dayData.getZjcj() - dayData.getZdcj()) / (dayData.getZgcj() - dayData.getZdcj()) * 100;
                    dayData.setKdjk(formatNumberToDouble(4, rsv));
                    dayData.setKdjd(formatNumberToDouble(4, rsv));
                    dayData.setKdjj(formatNumberToDouble(4, rsv));
                } else {
                    max9Value = Double.MIN_VALUE;
                    min9Value = Double.MAX_VALUE;
                    for (StockDayData data : rsv9List) {
                        max9Value = Math.max(max9Value, data.getZgcj());
                        min9Value = Math.min(min9Value, data.getZdcj());
                    }
                    if (max9Value != min9Value) {
                        rsv = (dayData.getZjcj() - min9Value) / (max9Value - min9Value) * 100;
                    } else {
                        rsv = 50;
                    }
                    k = P3_2 * pastData.getKdjk() + P3_1 * rsv;
                    d = P3_2 * pastData.getKdjd() + P3_1 * k;
                    j = 3 * k - 2 * d;
                    dayData.setKdjk(formatNumberToDouble(4, k));
                    dayData.setKdjd(formatNumberToDouble(4, d));
                    dayData.setKdjj(formatNumberToDouble(4, j));
                }
            }
            if (rsv9List.size() == 9) rsv9List.removeFirst();
            pastData = dayData;
        }
    }

//    public String getFdm() {
//        return fdm;
//    }
//
//    public void setFdm(String fdm) {
//        this.fdm = fdm;
//    }
//
//    public String getJc() {
//        return jc;
//    }
//
//    public void setJc(String jc) {
//        this.jc = jc;
//    }

    public int getMidflag() {
        return midflag;
    }

    public void setMidflag(int midflag) {
        this.midflag = midflag;
    }

    /**
     * 格式化小数,4舍5入
     *
     * @param i
     * @param value
     * @return
     */
    private double formatNumberToDouble(final int i, final double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
            return Double.parseDouble(bd.toString());
        } catch (Exception e) {

        }
        return 0;
    }

    private String formatNumberToString(final int i, final double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
            return bd.toPlainString();
        } catch (Exception e) {

        }
        return "0";
    }
}
