package com.procoin.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.procoin.util.StockChartUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhengmj on 19-4-30.
 */

public class RadarView extends View {

    //数据个数
    private int count = 5;
    //网格最大半径
    private float radius;
    //中心X
    private int centerX;
    //中心Y
    private int centerY;
    //雷达区画笔
    private Paint mainPaint;
    //文本画笔
    private TextPaint textPaint;
    //数据区画笔
    private Paint valuePaint;
    //标题文字
    private List<String> titles;
    //标题数据，为了方便换行还看一些
    private List<String> titlesValue;
    //各维度分值
    private List<Double> data;
    //数据最大值
    private float maxValue = 100;
    private float angle;

    private static final int RADAR_COLOR = Color.GRAY;//雷达网格颜色，包括线的颜色
//    private static final int RADAR_STREAK_COLOR_1 = Color.parseColor("#FFF6F6F6");//雷达条纹颜色,2种颜色循环
//    private static final int RADAR_STREAK_COLOR_2 = Color.parseColor("#FFEAEAEA");//雷达条纹颜色2种颜色循环

    private static final int[] RADAR_STREAK_COLOR = {
            Color.parseColor("#0d3d3a50"),
            Color.parseColor("#0d3d3a50"),
            Color.parseColor("#0d3d3a50"),
            Color.parseColor("#0d3d3a50")};
    private static final int RADAR_TEXT_COLOR = Color.GRAY;//雷达字体颜色
    private static final int RADAR_COVER_COLOR = Color.parseColor("#6175ae");//雷达覆盖区域颜色
    private static final int RADAR_LINE_COLOR = Color.parseColor("#ff8f01");//雷达覆盖区域连线颜色


    public RadarView(Context context) {
        this(context, null);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mainPaint = new Paint();
        mainPaint.setColor(RADAR_COLOR);
        mainPaint.setAntiAlias(true);
        mainPaint.setStrokeWidth(1);
        mainPaint.setStyle(Paint.Style.STROKE);

        textPaint = new TextPaint();
        textPaint.setColor(RADAR_TEXT_COLOR);
        textPaint.setTextAlign(Paint.Align.LEFT);
//        textPaint.setTextSize(30);
        textPaint.setTextSize(StockChartUtil.pxToSp(getResources(), 10));
        textPaint.setStrokeWidth(1);
        textPaint.setAntiAlias(true);

        valuePaint = new Paint();
        valuePaint.setColor(RADAR_COVER_COLOR);
        valuePaint.setAntiAlias(true);
        valuePaint.setStrokeWidth(5);
        valuePaint.setStyle(Paint.Style.FILL);

        titles = new ArrayList<>(count);
        titles.add("跟单盈利额\n");//加'\n'是为了防止换行时，如果是负数，就会把前面一行的最后一个字带过来
        titles.add("盈利能力\n");
        titles.add("跟单收益率\n");
        titles.add("跟单胜率\n");
        titles.add("人气指数\n");

        titlesValue = new ArrayList<>(count);
        titlesValue.add("0.00");
        titlesValue.add("0.00%");
        titlesValue.add("0.00%");
        titlesValue.add("0.00%");
        titlesValue.add("0");


//        titles.add("F");

        data = new ArrayList<>();
        data.add(0.00);
        data.add(0.00);
        data.add(0.00);
        data.add(0.00);
        data.add(0.00);
//        data.add(66.0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        radius = Math.min(w, h) / 2 * 0.7f;
        centerX = w / 2;
        centerY = h / 2;
        //一旦size发生改变，重新绘制
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawPolygon(canvas);
//        drawLines(canvas);//绘制直线
        drawTitle(canvas);
        drawRegion(canvas);
    }

    /**
     * 绘制多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        Path path = new Path();
        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
        //中心与相邻两个内角相连的夹角角度
        angle = (float) (2 * Math.PI / count);
        Log.d("Radar", "angle==" + angle);
        //每个蛛丝之间的间距
        float r = radius / (count - 1);
        for (int i = count - 1; i > 0; i--) {//从外往里画，方便绘制条纹效果
            //当前半径
            float curR = r * i;
            path.reset();
            for (int j = 0; j < count; j++) {

//                if (j == 0) {//这个是从右边平行位置开始画
//                    path.moveTo(centerX + curR, centerY);
//                } else {
//                    Log.d("Radar","curR * Math.cos(angle * j))=="+curR * Math.cos(angle * j));
//                    //对于直角三角形sin(x)是对边比斜边，cos(x)是底边比斜边，tan(x)是对边比底边
//                    //因此可以推导出:底边(x坐标)=斜边(半径)*cos(夹角角度)
//                    //               对边(y坐标)=斜边(半径)*sin(夹角角度)
//                    float x = (float) (centerX + curR * Math.cos(angle * j));
//                    float y = (float) (centerY + curR * Math.sin(angle * j));
//                    path.lineTo(x, y);
//                }
                float x = (float) (centerX + curR * Math.sin(angle * j));
                float y = (float) (centerY - curR * Math.cos(angle * j));

                if (j == 0) {
                    path.moveTo(x, y);
                } else {
                    Log.d("Radar", "curR * Math.cos(angle * j))==" + curR * Math.cos(angle * j));
                    path.lineTo(x, y);
                }
            }
            path.close();
            mainPaint.setColor(RADAR_COLOR);
            mainPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, mainPaint);

            mainPaint.setColor(RADAR_STREAK_COLOR[i - 1]);

//            if (i == count - 1) flag = false;//回复默认值
//            //填充覆盖区域
//            if (!flag) {
//                mainPaint.setColor(RADAR_STREAK_COLOR_1);
//            } else {
//                mainPaint.setColor(RADAR_STREAK_COLOR_2);
//            }
//            flag = !flag;

            mainPaint.setStyle(Paint.Style.FILL);
            canvas.drawPath(path, mainPaint);
        }
    }

    boolean flag = false;

    /**
     * 绘制多边形
     *
     * @param canvas
     */
//    private void drawPolygon(Canvas canvas) {
//        Path path = new Path();
//        //1度=1*PI/180   360度=2*PI   那么我们每旋转一次的角度为2*PI/内角个数
//        //中心与相邻两个内角相连的夹角角度
//        angle = (float) (2 * Math.PI / count);
//        //每个蛛丝之间的间距
//        float r = radius / (count - 1);
//        for (int i = 0; i < count; i++) {
//            //当前半径
//            float curR = r * i;
//            path.reset();
//            for (int j = 0; j < count; j++) {
//                if (j == 0) {
//                    path.moveTo(centerX + curR, centerY);
//                } else {
//                    //对于直角三角形sin(x)是对边比斜边，cos(x)是底边比斜边，tan(x)是对边比底边
//                    //因此可以推导出:底边(x坐标)=斜边(半径)*cos(夹角角度)
//                    //               对边(y坐标)=斜边(半径)*sin(夹角角度)
//                    float x = (float) (centerX + curR * Math.cos(angle * j));
//                    float y = (float) (centerY + curR * Math.sin(angle * j));
//                    path.lineTo(x, y);
//                }
//            }
//            path.close();
//            canvas.drawPath(path, mainPaint);
//        }
//    }

    /**
     * 绘制直线
     */
    private void drawLines(Canvas canvas) {
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            path.reset();
            path.moveTo(centerX, centerY);
            //这个是从右边平行位置画
//            float x = (float) (centerX + radius * Math.cos(angle * i));
//            float y = (float) (centerY + radius * Math.sin(angle * i));
            float x = (float) (centerX + radius * Math.sin(angle * i));
            float y = (float) (centerY - radius * Math.cos(angle * i));
            path.lineTo(x, y);
            mainPaint.setColor(RADAR_COLOR);
            mainPaint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, mainPaint);
        }
    }

    /**
     * 绘制标题文字
     *
     * @param canvas
     */
    private void drawTitle(Canvas canvas) {
        if (count != titles.size()) {
            return;
        }
        //相关知识点:http://mikewang.blog.51cto.com/3826268/871765/
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        //绘制文字时不让文字和雷达图形交叉,加大绘制半径
        float textRadius = radius;// + fontHeight;
        double pi = Math.PI;
        for (int i = 0; i < count; i++) {
//            float x = (float) (centerX + radius * Math.cos(angle * i));
//            float y = (float) (centerY + textRadius * Math.sin(angle * i));
            float x = (float) (centerX + radius * Math.sin(angle * i));
            float y = (float) (centerY - radius * Math.cos(angle * i));
            //当前绘制标题所在顶点角度
            float degrees = angle * i;
            Log.d("drawTitle", "degrees==" + degrees + " title==" + titles.get(i));
            //从右下角开始顺时针画起,与真实坐标系相反

//            String value=CommonUtil.fromHtml(titles.get(i)+"<span style=\"font-weight:bold;\">" + titlesValue.get(i) + "</span>");
//            StaticLayout layout = new StaticLayout(value, textPaint, (int) textPaint.measureText(titles.get(i)),
                    StaticLayout layout = new StaticLayout(titles.get(i)+titlesValue.get(i), textPaint, (int)textPaint.measureText(titles.get(i)+50),
                    Layout.Alignment.ALIGN_CENTER, 1.0F, 0.0F, true);
            Log.d("StaticLayout", "layout==" + layout.getWidth());
            float disX = 0;
            float disY = 0;
            if (degrees == 0) {//
                disX = x - layout.getWidth() / 2;
                disY = y - layout.getHeight() - 10;
            } else if (degrees < pi / 2) {//第1象限
                disX = x + 10;
                disY = y - layout.getHeight() / 2;
            } else if (degrees >= pi / 2 && degrees < pi) {//第4象限
                disX = x - layout.getWidth() / 2;
                disY = y + 10;
            } else if (degrees >= pi && degrees < 3 * pi / 2) {//第3象限
                disX = x - layout.getWidth() / 2;
                disY = y + 10;
            } else if (degrees >= 3 * pi / 2 && degrees <= 2 * pi) {//第2象限
                disX = x - layout.getWidth() - 10;
                disY = y - layout.getHeight() / 2;
            }
//            if (degrees == 0) {//
//                disX = x+15;
//                disY=y-layout.getHeight()/2;
//            } else if (degrees < pi / 2) {//第四象限
//                disX = x-layout.getWidth()/2;
//                disY=y+15;
//            } else if (degrees >= pi / 2 && degrees < pi) {//第三象限
//                disX = x-layout.getWidth()-15;
//                disY=y-layout.getHeight()/2;
//            } else if (degrees >= pi && degrees < 3 * pi / 2) {//第二象限
//                disX = x-layout.getWidth()-10;
//                disY=y-layout.getHeight()/2;
//            } else if (degrees >= 3 * pi / 2 && degrees <= 2 * pi) {//第一象限
//                disX = x-layout.getWidth()/2;
//                disY=y-layout.getHeight()-10;
//            }
            canvas.save();
            canvas.translate(disX, disY);//从20，20开始画
            layout.draw(canvas);
            canvas.restore();
//            float textWidth = textPaint.measureText(titles.get(i));
//            if (degrees == 0) {
////                canvas.drawText(titles.get(i), x + 10, y + fontHeight / 2, textPaint);
//            } else if (degrees < pi / 2) {//第四象限
////                float dis=textPaint.measureText(titles.get(i))/(Math.max(titles.get(i).length()-1,1));
////                float dis=0;
//                canvas.drawText(titles.get(i), x - dis / 2, y + fontHeight , textPaint);
//                Log.d("drawTitle", "444==title==" + titles.get(i));
//            } else if (degrees >= pi / 2 && degrees < pi) {//第三象限
////                float dis=textPaint.measureText(titles.get(i))/(Math.max(titles.get(i).length()-1,1));
////                float dis=0;
//                canvas.drawText(titles.get(i), x - dis - 10, y + fontHeight / 2, textPaint);
//                Log.d("drawTitle", "333==title==" + titles.get(i));
//            } else if (degrees >= pi && degrees < 3 * pi / 2) {//第二象限
////                float dis=textPaint.measureText(titles.get(i))/(titles.get(i).length());
////                float dis=0;
//                canvas.drawText(titles.get(i), x - dis - 10, y, textPaint);
//                Log.d("drawTitle", "222==title==" + titles.get(i));
//            } else if (degrees >= 3 * pi / 2 && degrees <= 2 * pi) {//第一象限
//                canvas.drawText(titles.get(i), x - dis / 2, y - fontHeight / 2, textPaint);
//                Log.d("drawTitle", "111==title==" + titles.get(i));
//            }
        }

    }

    /**
     * 绘制覆盖区域
     */
    private void drawRegion(Canvas canvas) {
//        valuePaint.setAlpha(85);
        Path path = new Path();
        for (int i = 0; i < count; i++) {
            //计算该数值与最大值比例
            Double perCenter = data.get(i) / maxValue;
            //小圆点所在位置距离圆心的距离
            double perRadius = perCenter * radius;
//            float x = (float) (centerX + perRadius * Math.cos(angle * i));
//            float y = (float) (centerY + perRadius * Math.sin(angle * i));
            float x = (float) (centerX + perRadius * Math.sin(angle * i));
            float y = (float) (centerY - perRadius * Math.cos(angle * i));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
            //绘制小圆点
//            canvas.drawCircle(x, y, 10, valuePaint);
        }
        //闭合覆盖区域
        path.close();

        //填充覆盖区域
        valuePaint.setStyle(Paint.Style.FILL);
        valuePaint.setColor(RADAR_COVER_COLOR);
        valuePaint.setAlpha(128);
        canvas.drawPath(path, valuePaint);

//        valuePaint.setStyle(Paint.Style.STROKE);
//        valuePaint.setColor(RADAR_LINE_COLOR);
//        //绘制覆盖区域外的连线
//        canvas.drawPath(path, valuePaint);

    }

    //设置数值种类
    public void setCount(int count) {
        this.count = count;
        postInvalidate();
    }

    //设置蜘蛛网颜色
    public void setMainPaint(Paint mainPaint) {
        this.mainPaint = mainPaint;
        postInvalidate();
    }

    //设置标题颜色
    public void setTextPaint(TextPaint textPaint) {
        this.textPaint = textPaint;
    }

    //设置标题
    public void setTitles(ArrayList<String> titles, ArrayList<String> titlesValue) {
        this.titles = titles;
        this.titlesValue = titlesValue;
        postInvalidate();
    }

    //设置覆盖局域颜色
    public void setValuePaint(Paint valuePaint) {
        this.valuePaint = valuePaint;
        postInvalidate();
    }

    //设置数值
    public void setData(List<Double> data) {
        this.data = data;
        postInvalidate();
    }

    public List<Double> getData() {
        return data;
    }

    //设置最大数值
    public void setMaxValue(float maxValue) {
        this.maxValue = maxValue;
    }
}
