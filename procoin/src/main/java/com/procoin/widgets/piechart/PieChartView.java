///* ===========================================================
// * AFreeChart : a free chart library for Android(tm) platform.
// *              (based on JFreeChart and JCommon)
// * ===========================================================
// *
// * (C) Copyright 2010, by ICOMSYSTECH Co.,Ltd.
// * (C) Copyright 2000-2008, by Object Refinery Limited and Contributors.
// *
// * Project Info:
// *    AFreeChart: http://code.google.com/p/afreechart/
// *    JFreeChart: http://www.jfree.org/jfreechart/index.html
// *    JCommon   : http://www.jfree.org/jcommon/index.html
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU Lesser General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU Lesser General Public License for more details.
// *
// * You should have received a copy of the GNU Lesser General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// *
// * [Android is a trademark of Google Inc.]
// *
// * -----------------
// * PieChartDemo01View.java
// * -----------------
// * (C) Copyright 2010, 2011, by ICOMSYSTECH Co.,Ltd.
// *
// * Original Author:  Niwano Masayoshi (for ICOMSYSTECH Co.,Ltd);
// * Contributor(s):   -;
// *
// * Changes
// * -------
// * 19-Nov-2010 : Version 0.0.1 (NM);
// */
//
//package com.tjr.bee.widgets.piechart;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.graphics.Typeface;
//import android.support.v4.content.ContextCompat;
//import android.util.AttributeSet;
//
//import com.tjr.bee.R;
//import StockChartUtil;
//
//import org.afree.chart.AFreeChart;
//import org.afree.chart.ChartFactory;
//import org.afree.chart.plot.PiePlot;
//import org.afree.data.general.DefaultPieDataset;
//import org.afree.data.general.PieDataset;
//import org.afree.graphics.PaintType;
//import org.afree.graphics.SolidColor;
//import org.afree.graphics.geom.Font;
//import org.afree.ui.RectangleInsets;
//
//import java.util.HashMap;
//import java.util.Map;
//
//public class PieChartView extends DemoView {
//    private DefaultPieDataset dataset = new DefaultPieDataset();
//    private AFreeChart chart;
//    private double tol, stol;
//    private int index;
//    private Map<Integer, PaintType> map;
//    private int size = 1;
//    private Context context;
//
//
//    public PieChartView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        init(context);
//    }
//
//    public PieChartView(Context context) {
//        super(context);
//        init(context);
//    }
//
//
//    private void init(Context context) {
//        this.context = context;
//        map = new HashMap<Integer, PaintType>();
//
////        for (int i = 0, m = CardHolderFragment.colors.length; i < m; i++) {
////            map.put(i, new SolidColor(Color.parseColor(CardHolderFragment.colors[i])));
////        }
////        map.put(0, new SolidColor(Color.parseColor("#ff4444")));
////        map.put(1, new SolidColor(Color.parseColor("#299999")));
////        map.put(2, new SolidColor(Color.parseColor("#36cc36")));
////        map.put(3, new SolidColor(Color.parseColor("#ff9944")));
////        map.put(4, new SolidColor(Color.parseColor("#128512")));
////        map.put(5, new SolidColor(Color.parseColor("#0d6363")));
////        map.put(6, new SolidColor(Color.parseColor("#a65716")));
////        map.put(7, new SolidColor(Color.parseColor("#ff9898")));
////        map.put(8, new SolidColor(Color.parseColor("#7acccc")));
////        map.put(9, new SolidColor(Color.parseColor("#a61616")));
//        size = map.size();
//        chart = createChart(dataset);
//        chart.setBackgroundPaintType(new SolidColor(ContextCompat.getColor(context, R.color.blackColor)));//这个是上面有一条线的颜色
//        chart.setBorderVisible(false);
//        setChart(chart);
//    }
//
//    /**
//     * 设置饼状图数据
//     * Creates a sample dataset.
//     *
//     * @return a sample dataset.
//     */
////    public void setChartdata(Group<HoldCardStruct> holdCardStructs) {
////        try {
////            if (chart != null) {
////                tol = 0;
////                if (holdCardStructs != null) {
////                    for (HoldCardStruct holdCardStruct : holdCardStructs) {
////                        tol = tol + holdCardStruct.amount;
////                    }
////                }
////                if (tol == 0) return;
////                PiePlot plot = (PiePlot) chart.getPlot();
////                dataset.clear();
////                index = 0;
////                for (HoldCardStruct holdCardStruct : holdCardStructs) {
////                    dataset.setValue(holdCardStruct.title  + StockChartUtil.formatNumWithSign(2, holdCardStruct.amount / tol * 100, false) + "%", holdCardStruct.amount);
////                    plot.setSectionPaintType(dataset.getKey(index), map.get(index % size));
////                    index++;
////                }
////                plot.setDataset(dataset);
////                setChart(chart);
////            }
////        } catch (Exception e) {
////            // TODO: handle exception
////        }
////    }
//
//    /**
//     * Creates a chart.
//     *
//     * @param dataset the dataset.
//     * @return a chart.
//     */
//    private AFreeChart createChart(PieDataset dataset) {
//
//        AFreeChart chart = ChartFactory.createPieChart(
//                "", // chart title
//                dataset, // data
//                false, // include legend
//                false,
//                false);
//        PiePlot plot = (PiePlot) chart.getPlot();
//        plot.setLabelFont(new Font("SansSerif", Typeface.NORMAL, 25));
//        plot.setCircular(true);
//        plot.setLabelGap(0.05);
//        plot.setLabelBackgroundPaintType(new SolidColor(Color.WHITE));//文字背景有颜色
//        plot.setLabelLinkPaintType(new SolidColor(Color.WHITE));//线的颜色
//        plot.setBackgroundPaintType(new SolidColor(ContextCompat.getColor(context, R.color.blackColor)));//背景色
////        plot.setOutlinePaintType(new SolidColor(ContextCompat.getColor(context, R.color.blackColor)));//
//        plot.setOutlineVisible(false);
//        plot.setInsets(new RectangleInsets(0, 0, 0, 0));
////        plot.setForegroundAlpha(0);
//        return chart;
//
//    }
//}
