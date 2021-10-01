package com.procoin.widgets.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.procoin.http.base.Group;
import com.procoin.module.copy.entity.Distribute;
import com.procoin.util.StockChartUtil;


public class HoldCostChartView extends View {

    private TextPaint mTextPaint;

    private Paint mColumnPaint;

    private Paint mPointPaint;

    private Paint mLinePaint;

    private Paint mParallelPaint;//平行线

    private Paint mCostPricePaint;//平均成本价线
    private Paint pricePaint;//现价线
    private TextPaint priceTextPaint;
    private Paint priceBgPaint;


    private float textHeight;//文字高度
    private int maxTextWidth;//最大文字宽度

    private int mWidth;
    private int mHeigth;

    private static final int textSize = 10;//单位已经是sp
    private static final int textColor = Color.parseColor("#808080");
    private static final int parallelColor = Color.parseColor("#e7e7e7");
    private static final int avgCostPriceLinelColor = Color.parseColor("#ff8f01");
    private static final int priceLineColor = Color.parseColor("#ff8f01");
    private static final int priceBgColor = Color.parseColor("#ff8f01");
    private static final int priceTextColor = Color.parseColor("#ffffff");


    private static final int pointColor = Color.parseColor("#808080");
    private static final int pointRadius = 8;

    private static final int lineColor = Color.parseColor("#e6e6e6");


    private static final int columnColor = Color.parseColor("#ff5757");

    private static final int columnGap = 30;//线与文字的左右间距
    private static final int bottomHeight = 30;//与底部的距离
    private static final int topHeight = 30;//与顶部的距离
    private static final int rectHeight = 6;//矩形高度
    private static final double maxLineWidthMult = 0.8;//最长的一根线为lineMaxWidth的多少倍


    private Group<Distribute> group;//实际数量
    private String price = "0.0";//现价
    private String avgCostPrice = "0.0";//平均成本价
    private static final int maxCountScale = 7;//左边多少个刻度
    private int lineMaxWidth;//线的最大宽度
    private int realMaxlineWidth;//最长的线为lineMaxWidth的maxLineWidthMult倍数


    private double maxPrice;//这里不能默认0，因为最大也有可能是负数
    private double minPrice;//同理

    private double avgPriceScale;//
    private double spanX, spanY;


    public int priceDecimals = 2;//小数位数


    public HoldCostChartView(Context context) {
        super(context);
//        mTextPaint = new TextPaint();
//        mTextPaint.setTextSize(DensityUtil.sp2px(getContext(), textSize));
//        mTextPaint.setColor(textColor);
//        mTextPaint.setAntiAlias(true);

        mTextPaint = new TextPaint();
        mTextPaint.setColor(textColor);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setTextSize(StockChartUtil.pxToSp(getResources(), textSize));
        mTextPaint.setStrokeWidth(1);
        mTextPaint.setAntiAlias(true);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;
//        Log.d("HoldCostChartView", "textHeight==" + textHeight);
//        textHeight = mTextPaint.descent() - mTextPaint.ascent();

        mParallelPaint = new Paint();
        mParallelPaint.setColor(parallelColor);
        mParallelPaint.setAntiAlias(true);


        mCostPricePaint = new Paint();
        mCostPricePaint.setColor(avgCostPriceLinelColor);
        mCostPricePaint.setAntiAlias(true);

        pricePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        pricePaint.setColor(priceLineColor);
        pricePaint.setStyle(Paint.Style.STROKE);
        pricePaint.setAntiAlias(true);
        pricePaint.setStrokeWidth(2f);
        pricePaint.setPathEffect(new DashPathEffect(new float[]{10f, 10f}, 0));


        priceTextPaint = new TextPaint();
        priceTextPaint.setColor(priceTextColor);
        priceTextPaint.setTextAlign(Paint.Align.LEFT);
        priceTextPaint.setTextSize(StockChartUtil.pxToSp(getResources(), textSize));
        priceTextPaint.setStrokeWidth(1);
        priceTextPaint.setAntiAlias(true);


        priceBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        priceBgPaint.setAntiAlias(true); // 去掉锯齿
        priceBgPaint.setColor(priceBgColor);// 线的颜色
        priceBgPaint.setStrokeWidth(1.0f);


        mColumnPaint = new Paint();
        mColumnPaint.setAntiAlias(true);
        mColumnPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mColumnPaint.setColor(columnColor);  //颜色为红色


        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);//实心矩形框
        mPointPaint.setColor(pointColor);


        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setColor(lineColor);

//        mLinePaint.setAlpha(128);
//        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(2f);

    }

    boolean isClear;

    public void clearData() {
        isClear = true;
        group = null;
        maxPrice = 0;
        minPrice = 0;
        invalidate();
    }

    public void setData(Group<Distribute> group, String price, String avgCostPrice) {
        if (group == null || group.size() == 0) return;

        Distribute distribute = null;
        maxPrice = 0;
        minPrice = 0;
        double maxAmount = 0;
//        double maxAmountRate = 0;
        for (int i = 0, m = group.size(); i < m; i++) {
            distribute = group.get(i);
            if (i == 0) {
                maxPrice = Double.parseDouble(distribute.price);
                minPrice = maxPrice * 0.8;//防止只有一项数据的时候相同
                maxAmount = Double.parseDouble(distribute.amount);
//                maxAmountRate = Double.parseDouble(distribute.rate);
            } else {
                maxPrice = Math.max(maxPrice, Double.parseDouble(distribute.price));
                minPrice = Math.min(minPrice, Double.parseDouble(distribute.price));
                maxAmount = Math.max(maxAmount, Double.parseDouble(distribute.amount));
//                maxAmountRate = Math.max(maxAmountRate, Double.parseDouble(distribute.rate));
            }
        }
//        Log.d("CardHolderChartView", "maxNum==" + maxPrice + "  minNum==" + minPrice + "  maxAmountRate=" + maxAmountRate);

        maxTextWidth = Math.max((int) mTextPaint.measureText(String.valueOf(maxPrice)), (int) mTextPaint.measureText(String.valueOf(minPrice)));
        lineMaxWidth = mWidth - maxTextWidth - columnGap;//这个columnGap是线与文字的间距
        realMaxlineWidth = (int) (lineMaxWidth * maxLineWidthMult);
        Log.d("HoldCostChartView", "mWidth==" + mWidth + "  maxTextWidth==" + maxTextWidth + "  lineMaxWidth==" + lineMaxWidth + "   realMaxlineWidth==" + realMaxlineWidth);

//        if (maxAmountRate == 0) maxAmountRate = 1;
//        maxAmount = maxAmount * 100 / maxAmountRate;
//        spanX = maxAmount / mWidth;
        spanX = maxAmount / realMaxlineWidth;
        spanY = (maxPrice - minPrice) / getRealHeight();
        avgPriceScale = (maxPrice - minPrice) / (maxCountScale - 1);


        setVisibility(VISIBLE);
        //计算坐标
        for (Distribute d : group) {
            d.x = (float) (Double.parseDouble(d.amount) / spanX);
            d.y = (float) ((maxPrice - Double.parseDouble(d.price)) / spanY + topHeight);
//            Log.d("CardHolderChartView", "======" + d.toString());
        }
        this.group = group;
        this.price = price;
        this.avgCostPrice = avgCostPrice;
        postInvalidate();
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
            isClear = false;
            return;
        }
        if (group == null || group.size() == 0) return;
        //画刻度
        String price;
        float y = getRealHeight() * 1.0f / (maxCountScale - 1);
        for (int i = 0; i < maxCountScale; i++) {
            price = StockChartUtil.formatNumber(priceDecimals, maxPrice - avgPriceScale * i);
            Log.d("HoldCostChartView", "画刻度 price==" + price + "  maxPrice==" + maxPrice + "  avgPriceScale==" + avgPriceScale);
//            StaticLayout layout = new StaticLayout(price, mTextPaint, maxTextWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
//            Log.d("HoldCostChartView", "textHeight==" + layout.getHeight());
//            canvas.save();
//            canvas.translate(mWidth , y * i + topHeight + layout.getHeight() / 2);
//            layout.draw(canvas);
//            canvas.restore();
            canvas.drawText(price, mWidth - mTextPaint.measureText(price), y * i + topHeight + textHeight / 2 - 5, mTextPaint);
            canvas.drawLine(0, y * i + topHeight, lineMaxWidth, y * i + topHeight, mParallelPaint);//画平行线
        }

        drawToChart(canvas);
        drawCurrPrice(canvas);
        drawAvgCostPrice(canvas);
    }

    //画平均成本价
    private void drawAvgCostPrice(Canvas canvas) {
        if (!TextUtils.isEmpty(avgCostPrice)) {
            float y = (float) ((maxPrice - Double.parseDouble(avgCostPrice)) / spanY + topHeight);
            canvas.drawLine(0, y, lineMaxWidth, y, mCostPricePaint);
        }

    }


    //画现价
    private void drawCurrPrice(Canvas canvas) {
        if (!TextUtils.isEmpty(price)) {
            float y = 0;
            if (Double.parseDouble(price) > maxPrice) {
                y = topHeight / 2;
            } else if (Double.parseDouble(price) < minPrice) {
                y = mHeigth - bottomHeight / 2;
            } else {
                y = (float) ((maxPrice - Double.parseDouble(price)) / spanY + topHeight);
                Log.d("CardHolderChartView", "drawCurrPrice   y==" + y + "  mHeigth==" + mHeigth + " topHeight==" + topHeight + " aa==" + (maxPrice - Double.parseDouble(price)) / spanY);
            }
//            float y = (float) ((maxPrice - Double.parseDouble(price)) / spanY + topHeight);
//            Log.d("CardHolderChartView", "drawCurrPrice   y==" + y + "  mHeigth==" + mHeigth+" topHeight=="+topHeight+" aa=="+(maxPrice - Double.parseDouble(price)) / spanY );
//            if (y >= mHeigth) {
//                y = mHeigth - bottomHeight/2 ;
//            }
//            if (y < 0) {
//                y = topHeight / 2;
//            }
//            canvas.drawLine(0, y, lineMaxWidth, y, dottedinePaint);
            Path path = new Path();
//            path.moveTo(0, y);
//            path.lineTo(lineMaxWidth, y);
//            canvas.drawPath(path, pricePaint);//用canvas.drawLine画虚线是有问题的，要开启硬件加速才可以，这里用drawPath代替
//
//            canvas.drawText(price, mWidth - mTextPaint.measureText(price), y + textHeight / 2 - 5, priceTextPaint);

            float textWidth = mTextPaint.measureText(price) + 10;
            int gap = 40;//现价距离右边的距离
            path.moveTo(0, y);
            path.lineTo(lineMaxWidth - textWidth - gap, y);
            canvas.drawPath(path, pricePaint);
            path.reset();
            path.moveTo(lineMaxWidth - gap, y);
            path.lineTo(lineMaxWidth, y);
            canvas.drawPath(path, pricePaint);

//                gridPaint.setStyle(Paint.Style.STROKE);// 设置填满
            canvas.drawRect(lineMaxWidth - textWidth - gap, y - textHeight / 2, lineMaxWidth - gap, y + textHeight / 2, priceBgPaint);
            canvas.drawText(price, lineMaxWidth - textWidth - gap + 5, y + textHeight / 2 - 5, priceTextPaint);

//            StaticLayout layout = new StaticLayout(p, fontPaint, (int) priceWidth, Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
//            canvas.save();
//            canvas.translate(stepColumn * 4 - layout.getWidth() / 2 - 15, preXY.lineY + stepMarginTop - layout.getHeight() / 2);//从20，20开始画
//            layout.draw(canvas);
//            canvas.restore();
        }

    }

    //画矩形
    private void drawToChart(final Canvas canvas) {
        if (group == null) return;
        for (Distribute d : group) {
            Log.d("HoldCostChartView", "d==" + d);
            if (Double.parseDouble(d.price) > Double.parseDouble(price)) {
                mColumnPaint.setColor(StockChartUtil.getRateTextColor(getContext(), -1));
            } else {
                mColumnPaint.setColor(StockChartUtil.getRateTextColor(getContext(), 1));
            }
            canvas.drawRect(0, d.y - rectHeight / 2, d.x, d.y + rectHeight / 2, mColumnPaint);
        }
    }

    private int getRealHeight() {
        return mHeigth - topHeight - bottomHeight;
    }

}
