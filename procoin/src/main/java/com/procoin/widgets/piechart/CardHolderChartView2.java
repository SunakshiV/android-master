package com.procoin.widgets.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.procoin.module.kbt.entity.KbtTrend;
import com.procoin.module.myhome.entity.Trend;
import com.procoin.util.DateUtils;
import com.procoin.util.DensityUtil;
import com.procoin.util.LinearEquationInTwoUnknowns;
import com.procoin.util.StockChartUtil;
import com.procoin.http.base.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * 这个刻度是小数点的  kbt回购走势图用到
 */
public class CardHolderChartView2 extends View {
    private Context context;

    private Paint mTextPaint;

    private Paint mColumnPaint;

    private Paint mPointPaint;

    private Paint mLinePaint;

    private Paint mParallelPaint;//平行线


    private int leftTextWidth;//圆柱与左边的间距140
    private int rightWidth;//圆柱与右边的间距120
    private static final int bottomHeight = 50;//圆柱与底部的距离
    private static final int topHeight = 50;//圆柱与顶部的距离


    private float columnWidth;//圆柱宽
    private int columnSize;//圆柱数量
    private float spacing;//圆柱间距
    private static final float multiple = 3f;//圆柱的宽是间距的多少倍

    private int textHeight;//文字高度
    private int maxTextWidth;//最大文字宽度


    private int mWidth;
    private int mHeigth;

    private static final int textSize = 10;//单位已经是sp
    private static final int textColor = Color.parseColor("#808080");
    private static final int parallelColor = Color.parseColor("#e7e7e7");

    private static final int pointColor = Color.parseColor("#808080");
    private static final int pointRadius = 8;

    private static final int lineColor = Color.parseColor("#ff8f01");

    private static final int columnColor = Color.parseColor("#ff5757");

    private static final int columnGap = 30;//左右边文字的左右间距


    private String[] date;//日期
    private String[] num;//实际数量
    private double[] count;//左边刻度

    private boolean isMinus;//刻度是否出现负数


    private static final int maxCountScale = 7;//左边多少个刻度


    private static final int maxDateCount = 5;//显示日期数量，日期太多显示不完会重复


    public CardHolderChartView2(Context context) {
        super(context);

        init(context);
    }

    public CardHolderChartView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        //创建初始化
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        mTextPaint = new Paint();
        mTextPaint.setTextSize(DensityUtil.sp2px(getContext(), textSize));
        mTextPaint.setColor(textColor);
        mTextPaint.setAntiAlias(true);
        textHeight = (int) (mTextPaint.descent() - mTextPaint.ascent());

        mParallelPaint = new Paint();
        mParallelPaint.setColor(parallelColor);
        mParallelPaint.setAntiAlias(true);

        mColumnPaint = new Paint();
        mColumnPaint.setAntiAlias(true);
        mColumnPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mColumnPaint.setColor(columnColor);  //颜色为红色


        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mPointPaint.setColor(pointColor);


        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(lineColor);

//        mLinePaint.setAlpha(128);
//        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(2f);

    }

    boolean isClear;

    public void clearData() {
        isClear = true;
        date = null;
        num = null;
        count = null;
        invalidate();
//        if(canvas==null)return;
////        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
////        canvas.restore();
////        invalidate();
//        Paint paint = new Paint();
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//        canvas.drawPaint(paint);
//        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//        invalidate();
//        if(mTextPaint!=null){
//            mTextPaint.reset();
//        }
//        if(mColumnPaint!=null){
//            mColumnPaint.reset();
//        }
//        if(mPointPaint!=null){
//            mPointPaint.reset();
//        }
//        if(mLinePaint!=null){
//            mLinePaint.reset();
//        }
//        if(mParallelPaint!=null){
//            mParallelPaint.reset();
//        }
    }


    public void setDataForKbt(Group<KbtTrend> group) {
        if (group != null && group.size() > 0) {
            Group<Trend> groupTrend = new Group<>();
            Trend trend = null;
            for (KbtTrend kbtTrend : group) {
                trend = new Trend();
                trend.dayTime = kbtTrend.dayTime;
                trend.num = kbtTrend.price;
                groupTrend.add(trend);
            }
            setData(groupTrend);
        }
    }

    public void setData(Group<Trend> group) {
        if (group == null || group.size() == 0) return;
        columnSize = Math.max(group.size(), 5);//如果柱子不够5根也按5根算,避免如果只有2根,会出现太粗

        date = new String[group.size()];
        num = new String[group.size()];

        Trend trend = null;
        double maxNum = -999999;//这里不能默认0，因为最大也有可能是负数
        double minNum = 999999;//同理
        for (int i = 0, m = group.size(); i < m; i++) {
            trend = group.get(i);
            date[i] = DateUtils.getStringDateOfString2(String.valueOf(trend.dayTime), DateUtils.TEMPLATE_yyyyMMdd);
            num[i] = trend.num;
            maxNum = Math.max(maxNum, Double.parseDouble(trend.num));
            minNum = Math.min(minNum, Double.parseDouble(trend.num));
        }

        Log.d("CardHolderChartView", "maxNum==" + maxNum + "  minNum==" + minNum + "  mWidth==" + mWidth + "  mHeigth==" + mHeigth);

//        if (maxNum > 0) {
//            count = new int[maxCountScale];
//            int countScale = (int) (maxNum / (maxCountScale - 1)) + 1;
//            Log.d("CardHolderChartView", "countScale==11==" + countScale);
////            countScale = getRealCountScale(countScale);
////            Log.d("CardHolderChartView", "countScale==22==" + countScale);
//            for (int i = 0, m = maxCountScale; i < m; i++) {
//                count[i] = countScale * i;
//            }
//        }

        count = new double[maxCountScale];
        double countScale = 0;
        if (minNum >= 0) {
            isMinus = false;
            countScale = (double) ((maxNum - minNum) / (maxCountScale - 1));//+ 0.1
            for (int i = 0, m = maxCountScale; i < m; i++) {
                count[i] = countScale * i + minNum;
            }
        } else {//有负数情况出现
            isMinus = true;
//            int a=(int)((Math.abs(maxNum) + Math.abs(minNum)) / (maxCountScale - 1));
//            Log.d("CardHolderChartView", "a==" + a);
            countScale = (double) (((Math.abs(maxNum) + Math.abs(minNum)) / (maxCountScale - 1)) * 2);//+ 0.1
            int midIndex = getMidIndex();
            for (int i = 0; i < maxCountScale; i++) {
                if (i < midIndex) {
                    count[i] = -countScale * (midIndex - i);
                } else if (i > midIndex) {
                    count[i] = countScale * (i - midIndex);
                } else {
                    count[i] = 0;
                }
            }
            Log.d("CardHolderChartView", "countScale==" + countScale);


        }

        Log.d("CardHolderChartView", "count==" + count);

        maxTextWidth = Math.max((int) mTextPaint.measureText(String.valueOf(maxNum)), (int) mTextPaint.measureText(String.valueOf(minNum)));
        leftTextWidth = maxTextWidth + columnGap * 2;//确定左边数量的最大宽度,取最大值就好
        rightWidth = columnGap * 2;//确定右边价格的最大宽度,取最大值就好


        setVisibility(VISIBLE);
        postInvalidate();
    }


    private int getMidIndex() {//获取中间索引
        if (maxCountScale % 2 == 0) {
            return maxCountScale / 2 - 1;
        } else {
            return maxCountScale / 2;
        }
    }

    /**
     * 取智能整数   例如:213取250  260取300
     *
     * @param countScale
     * @return
     */
    private int getRealCountScale(int countScale) {//1666
        String scale = String.valueOf(countScale);
        int length = scale.length();//4
        if (length >= 3) {
            int base = (int) Math.pow(10, length - 1);//1000
            if (countScale % base == 0) return countScale;//如果是整100或者整1000就直接返回
            int realEndNum = 0;
            int endNum = Integer.parseInt(scale.substring(1, length));//666
            if (endNum <= base / 2) {//213取250  260取300
                realEndNum = base / 2;
            } else {
                realEndNum = base;
            }
            return Integer.parseInt(scale.substring(0, 1)) * base + realEndNum;
        }
        return countScale;

//        if (scale.length() == 2) {
//            return Integer.parseInt(scale.substring(0, 1)) * 10 + 10;
//        } else if (scale.length() == 3) {
//            return Integer.parseInt(scale.substring(0, 1)) * 100 + 100;
//        } else if (scale.length() == 4) {
//            return Integer.parseInt(scale.substring(0, 1)) * 1000 + 1000;
//        } else if (scale.length() == 5) {
//            return Integer.parseInt(scale.substring(0, 1)) * 10000 + 10000;
//        } else if (scale.length() == 6) {
//            return Integer.parseInt(scale.substring(0, 1)) * 100000 + 100000;
//        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeigth = h;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isClear) {
//            canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
//            Paint paint = new Paint();
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
//            canvas.drawPaint(paint);
//            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
//            setBackgroundColor(Color.WHITE);
            isClear = false;
            return;
        }
        if (date == null) return;

        //例如 圆柱的宽是间隙的1.2倍,圆柱数量为5,总宽=100;
        //x-1.2y=0
        //5x+4y=100
        float[] xy = LinearEquationInTwoUnknowns.getXY(new LinearEquationInTwoUnknowns(1.0f, -multiple, 0), new LinearEquationInTwoUnknowns(columnSize, columnSize - 1, mWidth - leftTextWidth - rightWidth));
        columnWidth = xy[0];
        spacing = xy[1];

        //画左边的刻度
        for (int i = 0, m = count.length; i < m; i++) {
            float x = (float) (leftTextWidth - maxTextWidth - columnGap);
            float y = (mHeigth - bottomHeight - topHeight) / (count.length - 1) * (count.length - (i + 1)) + topHeight;//+ textHeight / 2
            canvas.drawText(StockChartUtil.formatNumber(8, count[i]), x, y - textHeight / 2, mTextPaint);
            canvas.drawLine(x, y, mWidth, y, mParallelPaint);//画平行线
        }

//        //画右边边的刻度
//        for (int i = 0, m = price.length; i < m; i++) {
//            canvas.drawText(StockChartUtil.formatNumWithSign(2, price[i], false), (float) (mWidth - rightTextWidth+columnGap ), (mHeigth - bottomHeight - topHeight) / (price.length - 1) * (price.length - (i + 1)) + textHeight / 2 + topHeight, mTextPaint);
//        }
//        画日期
//        for (int i = 0, m = date.length; i < m; i++) {
//            String time = date[i];
//            canvas.drawText(time, (float) (leftTextWidth + columnWidth * (i + 0.5) + spacing * i - mTextPaint.measureText(time) / 2), mHeigth, mTextPaint);
//        }
//        画日期,只画第一根和最后一根
        if (date.length > 0)
            canvas.drawText(date[0], (float) (leftTextWidth + columnWidth * (0 + 0.5) + spacing * 0 - mTextPaint.measureText(date[0]) / 2), mHeigth, mTextPaint);
        if (date.length > 1)
            canvas.drawText(date[date.length - 1], (float) (leftTextWidth + columnWidth * (date.length - 1 + 0.5) + spacing * (date.length - 1) - mTextPaint.measureText(date[date.length - 1]) / 2), mHeigth, mTextPaint);


//        //画柱子 和值
//        int maxCount = count[count.length - 1];
//        for (int i = 0, m = realCount.length; i < m; i++) {
//            float left = leftTextWidth + columnWidth * i + spacing * i;
//            int right = mHeigth - (mHeigth - bottomHeight - topHeight) * realCount[i] / maxCount - bottomHeight;
//            canvas.drawRect(new Rect(
//                    (int)left,
//                    right,
//                    (int)(leftTextWidth + columnWidth * i + spacing * i + columnWidth),
//                    mHeigth - bottomHeight), mColumnPaint);
//
//            float textWidth = mTextPaint.measureText(String.valueOf(realCount[i]));
//
//            canvas.drawText(String.valueOf(realCount[i]), left + (columnWidth - textWidth) / 2, mHeigth - bottomHeight - 10, mTextPaint);
//        }


//        画点和文字
        List<Point> pointList = new ArrayList<>();
        double maxPoint = count[count.length - 1];
        double minPoint = count[0];
        float fx = 0, fy = 0;
        for (int i = 0, m = num.length; i < m; i++) {
            fx = (float) (leftTextWidth + columnWidth * (i + 0.5) + spacing * i);
            if (isMinus) {
                if (maxCountScale % 2 == 0) {
                    /**
                     *
                     * 当maxCountScale==5 上面占2下面占2  即 5 2 2
                     * 当maxCountScale==6 上面占3下面占2  即 6 3 2   3/5
                     * 当maxCountScale==7 上面占3下面占3  即 7 3 3
                     * 当maxCountScale==8 上面占4下面占3  即 8 4 3
                     *
                     * 所以当maxCountScale % 2 == 0时候 即上面部分 总高度*(maxCountScale/2)/(maxCountScale-1)  下面部分 总高度*(maxCountScale/2-1)/(maxCountScale-1)
                     *
                     */
                    fy = (float) ((mHeigth - bottomHeight - topHeight) * (maxCountScale / 2) / (maxCountScale - 1) - (mHeigth - bottomHeight - topHeight) * (maxCountScale / 2) / (maxCountScale - 1) * Double.parseDouble(num[i]) / maxPoint + topHeight);
                } else {
                    fy = (float) ((mHeigth - bottomHeight - topHeight) / 2 - (mHeigth - bottomHeight - topHeight) / 2 * Double.parseDouble(num[i]) / maxPoint + topHeight);
                }
            } else {
                fy = (float) (mHeigth - (mHeigth - bottomHeight - topHeight) / (maxPoint-minPoint) * (Double.parseDouble(num[i]) - minPoint) - bottomHeight);
            }

            Log.d("CardHolderChartView", "fy==" + fy);
//            canvas.drawCircle(fx, fy, pointRadius, mPointPaint);
//            canvas.drawText(String.valueOf(num[i]), fx + 12, fy + textHeight / 2, mTextPaint);//这里画线和点的话被覆盖了，所以放到后面画
            pointList.add(new Point((int) fx, (int) fy));
        }

        //连线
        drawLine(canvas, pointList);
//        drawScrollLine(canvas, pointList);

        //画文字和点
//        drawTextAndCircle(canvas, pointList);
    }

    //画线和点
    private void drawTextAndCircle(Canvas canvas, List<Point> mListPoint) {
        if (mListPoint == null) return;
        for (int i = 0; i < mListPoint.size(); i++) {
            canvas.drawCircle(mListPoint.get(i).x, mListPoint.get(i).y, pointRadius, mPointPaint);
            canvas.drawText(String.valueOf(num[i]), mListPoint.get(i).x + 12, mListPoint.get(i).y + textHeight / 2, mTextPaint);
        }
    }

    //画折线
    private void drawLine(Canvas canvas, List<Point> mListPoint) {
        if (mListPoint == null || mListPoint.size() == 0) return;
        Path path = new Path();
        LinearGradient gradient = null;
        for (int i = 0; i < mListPoint.size(); i++) {
            Point point = mListPoint.get(i);
            if (i == 0) {
                path.moveTo(point.x, point.y);
            } else {
                path.lineTo(point.x, point.y);
//                if (i == mListPoint.size() - 1) {
//                    path.lineTo(point.x, mHeigth - bottomHeight);
//                    path.lineTo(mListPoint.get(0).x, mHeigth - bottomHeight);
//                    path.close();
//                }

            }
        }
//        gradient = new LinearGradient(
//                mListPoint.get(0).x,
//                mListPoint.get(0).y,
//                mListPoint.get(0).x,
//                mHeigth - bottomHeight,
//                Color.argb(255, 255, 143, 1),
//                Color.argb(255, 255, 143, 1),
//                Shader.TileMode.CLAMP);
//        mLinePaint.setShader(gradient);
        canvas.drawPath(path, mLinePaint);
    }


    //画曲线
    private void drawScrollLine(Canvas canvas, List<Point> mListPoint) {
        if (mListPoint == null || mListPoint.size() == 0) return;
        if (mListPoint.size() == 1) {//如果size为1，无法画曲线
            drawLine(canvas, mListPoint);
            return;
        }

        Point pStart = new Point();
        Point pEnd = new Point();
        Path path = new Path();

        for (int i = 0; i < mListPoint.size() - 1; i++) {
            pStart = mListPoint.get(i);
            pEnd = mListPoint.get(i + 1);
            Point point3 = new Point();
            Point point4 = new Point();
            float wd = (pStart.x + pEnd.x) / 2;
            point3.x = wd;
            point3.y = pStart.y;
            point4.x = wd;
            point4.y = pEnd.y;
            if (i == 0) {
                path.moveTo(pStart.x, pStart.y);
            }
            path.cubicTo(point3.x, point3.y, point4.x, point4.y, pEnd.x, pEnd.y);
        }
        path.lineTo(pEnd.x, mHeigth - bottomHeight);
        path.lineTo(mListPoint.get(0).x, mHeigth - bottomHeight);
        path.close();
        LinearGradient gradient = new LinearGradient(
                mListPoint.get(0).x,
                mListPoint.get(0).y,
                mListPoint.get(0).x,
                mHeigth - bottomHeight,
//                Color.argb(200, 86, 84, 101),
//                Color.argb(50, 224, 224, 226),
                Color.argb(150, 255, 143, 1),
                Color.argb(50, 255, 143, 1),
                Shader.TileMode.CLAMP);
        mLinePaint.setShader(gradient);

        canvas.drawPath(path, mLinePaint);


    }


    class Point {
        public float x;
        public float y;

        public Point() {
        }

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

}
