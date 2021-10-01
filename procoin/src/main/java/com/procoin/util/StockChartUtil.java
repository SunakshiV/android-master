package com.procoin.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.util.TypedValue;

import com.procoin.widgets.quotitian.entity.StockDomain;
import com.procoin.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Formatter;

public class StockChartUtil {
    public static final int DELAYMILLIS = 500;
    public static final int DEFUALT_FONT_SIZE = 12;// 行情默认字体大小
    // 新版本行情使用的颜色
    public static final int CHART_BG = Color.rgb(15, 24, 38); // rgb(242, 245, 247);//

    // 行情背景色

    // public static final int CHART_LINE_BG = Color.rgb(50, 50, 50);//没发现有用到
    // public static final int RED_SIGN = Color.rgb(231, 111, 111);// 新版路牌 红色
    // 没发现有用到
    // public static final int GREEN_SIGN = Color.rgb(103, 206, 90);// 新版路牌 绿色
    // 没发现有用到

    public static final int ALPHA_ZHANG = Color.argb(80, 0, 173, 136);// 红色
    public static final int ALPHA_DIE = Color.argb(80, 226, 33, 78);// 红色
    public static final int ZHANG = Color.rgb(226, 33, 78);// K线蜡烛柱的红色 cc1414
    public static final int DIE = Color.rgb(0, 173, 136);// K线蜡烛柱的绿色
    public static final int GRAY = Color.rgb(177, 177, 177);// 白色 变成灰色
    public static final int TIME_COLOR_1 = Color.argb(130, 36, 36, 36);// 画半透明的区域色
    public static final int TIME_COLOR_2 = Color.argb(78, 231, 231, 231);// 画半透明的区域色

    public static final int MINLINK_COLOR = Color.rgb(13, 13, 13);
    public static final int M5_COLOR = Color.rgb(53, 125, 173);
    public static final int YELLOW = Color.rgb(255, 196, 62);// 黄色 cffc43e
    public static final int PURPLE = Color.rgb(176, 128, 206);// 紫色
    // public static final int BULE = Color.rgb(67, 191, 175);// 蓝色
    public static final int BULE = Color.rgb(0, 161, 242);// 蓝色
    public static final int BLACK = Color.rgb(0, 0, 0);// 黑色

    public static final int KLINE_WHITELINE_BG = Color.rgb(40, 205, 215);// K线白线框背景色
    public static final int MINTIME_WHITELINE_BG = Color.rgb(100, 100, 100);// 分时线白线框背景色

    public static final int GREY = Color.rgb(128, 120, 120);// 灰色

    public static final int CJSL_BG = Color.rgb(255, 247, 153);// 成交量色


    public static int getRateColorNoWhite(double rate) {
        if (rate >= 0) return ZHANG;
        else return DIE;
    }

    public static int getRateAlphaColorNoWhite(double rate) {
        if (rate >= 0) return ALPHA_ZHANG;
        else return ALPHA_DIE;
    }

    /**
     * 涨幅颜色
     *
     * @param now       当前价格
     * @param yesterday 昨天价格
     * @return
     */
    public static int getRateColor(double now, double yesterday) {
        if (now == yesterday || now == 0) return GRAY;
        else return now > yesterday ? ZHANG : DIE;
    }

    /**
     * 计算涨幅
     *
     * @param stockDomain
     * @return
     */
    public static double calculationRate(StockDomain stockDomain) {
        if (stockDomain == null) return 0;
        if (stockDomain.zrsp == 0) return 0;
        if (stockDomain.zjcj != 0.0 && stockDomain.cjsl > 0)
            return (stockDomain.zjcj - stockDomain.zrsp) * 100 / stockDomain.zrsp; // (最新成交价-昨日收盘价)/昨日收盘价
        return 0;
    }

    /**
     * 格式化金额，金额的换算，formatAmount 和formatVolume 大数字有问题
     *
     * @param amount
     * @return
     */
    public static String formatMoney(double amount, int mini, int maxi) {
        if (amount >= 1E12) return formatNumber(maxi, BigDecimal.ROUND_DOWN, amount / 1E12) + "万亿";
        else if (amount >= 1E8)
            return formatNumber(maxi, BigDecimal.ROUND_DOWN, amount / 1E8) + "亿";
        else if (amount >= 1E4)
            return formatNumber(maxi, BigDecimal.ROUND_DOWN, amount / 1E4) + "万";
        else return formatNumber(mini, amount);
    }


    /**
     * 格式化小数,4舍5入
     *
     * @param i
     * @param bigFlag // BigDecimal.ROUND_HALF_UP 4舍5入    BigDecimal.ROUND_DOWN 直接删除多余的小数位，如2.35会变成2.3
     * @param value
     * @return
     */
    public static String formatNumber(final int i, final int bigFlag, final double value) {
        try {
            if ("".equals(value) || "nan".equals(String.valueOf(value).toLowerCase())) return "0";
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, bigFlag);
            return bd.toPlainString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 格式化小数,4舍5入
     *
     * @param i
     * @param value
     * @return
     */
    public static String formatNumber(final int i, final double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
            return bd.toPlainString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 防止科学计数法
     *
     * @param value
     * @return
     */
    public static String formatNumber(double value) {
        try {
            BigDecimal bd = new BigDecimal(Double.toString(value));
            return bd.toPlainString();
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 格式化成交量 超过亿以亿手为单位,超过万以万手为单位
     *
     * @param volume 成交量
     * @return
     */
    public static String formatVolume(double volume) {
        if (volume >= 1E10) return formatNumber(2, volume / 1E10) + "亿";
        else if (volume >= 1E6) return formatNumber(2, volume / 1E6) + "万";
        else return String.valueOf((long) (volume / 100));
    }

    /**
     * 格式化成交金额 超过亿以亿为单位,超过万以万为单位
     *
     * @param amount 成交金额
     * @return
     */
    public static String formatAmount(double amount) {
        if (amount >= 1E12) return formatNumber(2, amount / 1E12) + "万亿";
        else if (amount >= 1E8) return formatNumber(2, amount / 1E8) + "亿";
        else if (amount >= 1E4) return formatNumber(2, amount / 1E4) + "万";
        else return formatNumber(2, amount);
    }

    /**
     * 计算横坐值
     *
     * @param time  930
     * @param spanX 每个跨度值
     * @param time  时间
     * @return
     */
    public static float getCharPixelX(final float spanX, final String time) {
        float result = 0.0f;
        try {
            if (time == null || time.length() < 3) return result;
            int h = Integer.parseInt(time.substring(0, time.length() - 2));
            int m = Integer.parseInt(time.substring(time.length() - 2, time.length()));
            if (h == 9) result = (m - 30) * spanX; // 9:30-9:59
            else if (h == 10) result = (m + 30) * spanX;// 10:00-10:59
            else if (h == 11) result = ((m < 30 ? m : 30) + 90) * spanX;// 11:00-11:29
            else if (h == 12) result = (30 + 90) * spanX;// 11:30-12:59归1300
            else if (h == 13) result = (m + 120) * spanX;
            else if (h == 14) result = (m + 180) * spanX;
            else result = 240 * spanX;
            return Float.parseFloat(formatNumber(1, result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Float.parseFloat(formatNumber(1, result));
    }

    /**
     * 计算纵坐值
     * <p>
     * //@param height 高度值
     * //@param span   每个跨度值
     * // @param time   时间
     *
     * @return 两位小数
     */
    public static float getCharPixelY(final double max, final double spanY, final double value) {
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

    /**
     * 计算成交量纵坐值,在跌停线不同
     * <p>
     * //@param height 高度值
     *
     * @param spanY 每个跨度值
     * @param value 当前成交量
     * @return 两位小数
     */
    public static float getCharVolumeY(final double max, final double spanY, final double value) {
        double result = 0.0f;
        try {
            if (spanY <= 0.0) return 0.0f;
            if (value <= 0.0) return 0.0f;
            if (value == max) return 0.1f;
            result = (max - value) / spanY;
            return Float.parseFloat(formatNumber(2, result));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0f;
    }

    public static String formatNumWithSign(int d, double val, boolean isSign) {
        Formatter f = null;
        try {
            f = new Formatter();
            if (isSign) return f.format("%+1." + d + "f", val).toString().trim();
            else return f.format("%1." + d + "f", val).toString().trim();
        } catch (Exception e) {
            return "----";
        } finally {
            if (f != null) f.close();
        }
    }

    public static String formatWithSign(double val) {
        try {
            if (val >= 0) {
                return "+" + val;
            } else {
                return val + "";
            }
        } catch (Exception e) {
            return val + "";
        }
    }

    /**
     * 这里不调回formatWithSign(double val)是因为可能产生科学计数法
     *
     * @param val
     * @return
     */
    public static String formatWithSign(String val) {
        try {
            if (Double.parseDouble(val) >= 0) {
                return "+" + val;
            } else {
                return val;
            }
        } catch (Exception e) {
            return val;
        }
    }

//    public static String formatNumberWithFdm(final String fdm, final double value) {
//        try {
//            int i = 2;
//            if (fdm != null) {
//                String dm = fdm.replaceAll(ONLYDIGITAL, "");
//                if (dm != null) {
//                    if (dm.startsWith("9") || dm.startsWith("5")) i = 3;
//                }
//            }
//            BigDecimal bd = new BigDecimal(Double.toString(value));
//            bd = bd.setScale(i, BigDecimal.ROUND_HALF_UP);
//            return bd.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "0";
//        }
//    }
//
//    public static String replaceForDm(String fdm) {
//        if (fdm != null) {
//            String dm = fdm.replaceAll(ONLYDIGITAL, "");
//            return dm;
//        }
//        return "";
//    }

//    /**
//     * 把时间int转为格式化日期yyyy-MM-dd
//     *
//     * @param dataint 日期
//     * @param style   样式 (比如.,或-,或/之类的)
//     * @param head    是否保年份的前两个数字(如2011,是否保留20)
//     * @return yyyy样式MM样式dd
//     */
//    public static String getStringIntToDate(String dataint, String style, boolean head) {
//        if (dataint == null) return null;
//        try {
//            String year;
//            if (head) year = dataint.substring(0, 4);
//            else year = dataint.substring(2, 4);
//            String month = dataint.substring(4, 6);
//            String day = dataint.substring(6, 8);
//            String ymd = year + style + month + style + day;
//            return ymd;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static String getStringIntToDate(String dataint) {
//        if (dataint == null) return null;
//        try {
//            String month = dataint.substring(2, 4);
//            String day = dataint.substring(4, 6);
//            String hour = dataint.substring(6, 8);
//            String minute = dataint.substring(8, 10);
//            String ymd = month + "-" + day + " " + hour + ":" + minute;
//            return ymd;
//        } catch (Exception e) {
//            return null;
//        }
//    }
//
//    public static int formatDecimalWithFdm(final String fdm) {
//        int i = 2;
//        try {
//            if (fdm != null) {
//                String dm = fdm.replaceAll(ONLYDIGITAL, "");
//                if (dm != null) {
//                    if (dm.startsWith("9") || dm.startsWith("5")) i = 3;
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return i;
//    }

//    /**
//     * 截取时间
//     *
//     * @param time       时间字符串
//     * @param keepHour   true 保留时
//     * @param keepMinute true 保留分
//     * @param keepSecond true 保留秒
//     * @return
//     */
//    public static String cutStringTime(String time, boolean keepHour, boolean keepMinute, boolean keepSecond) {
//        StringBuffer buffer = new StringBuffer();
//        if (time == null) return "";
//        String[] per = time.split(":");
//        if (per == null || per.length < 3) return "";
//        if (keepHour) buffer.append(per[0]);
//        if (keepHour && keepMinute) buffer.append(":");
//        if (keepMinute) buffer.append(per[1]);
//        if (keepMinute && keepSecond) buffer.append(":");
//        if (keepSecond) buffer.append(per[2]);
//        if (buffer.length() > 0) return buffer.toString();
//        else return "";
//    }
//
//    /**
//     * 将时间转换成整型数据（如15：01：59，转换后就变成了150159）
//     *
//     * @param time 如15：01：59
//     * @return 150159
//     */
//    public static int timeTransformationToInt(String time) {
//        if (time != null && time.replaceAll(ONLYDIGITAL, "").matches(INTMATCHES)) //
//            return Integer.parseInt(time.replaceAll(ONLYDIGITAL, ""));
//        return -1;
//    }
//
//    /**
//     * 获取买卖性质颜色
//     *
//     * @param nature S为绿,B为红,其他为白
//     * @return
//     */
//    public static int getNatureColor(String nature) {
//        if ("S".equals(nature)) return DIE;
//        else return "B".equals(nature) ? ZHANG : GRAY;
//    }
//
//    /**
//     * 总时间
//     *
//     * @param time 时间
//     * @return
//     */
//    public static int getDataTime(final String time) {
//        try {
//            String[] per = time.split(":");
//            int sumTime = 0;
//            if (per == null || per.length != 3) return 0;
//            sumTime = Integer.parseInt(per[0]) * 60 + Integer.parseInt(per[1]);
//            if (sumTime == 0) return 0;
//            if (sumTime < 570) return 0;// 9:00前
//            else if (sumTime <= 690) return sumTime - 570;// 11:30
//            else if (sumTime <= 900) return sumTime - 660;// 15:00
//            else return 240;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }

    public static float pxToSp(Resources res, float px) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, px, res.getDisplayMetrics());
    }

    public static int getRateBg(double rate) {
        if (rate > 0) return R.drawable.shape_profit_rate_zhang_bg;
        else if (rate == 0 || rate == -100d) return R.drawable.shape_profit_rate_0_bg;
        else return R.drawable.shape_profit_rate_die_bg;
    }

    /**
     * 这个是根据涨幅获得相对应的字体颜色
     *
     * @param rate
     * @return
     */
    public static int getRateTextColor(Context context, double rate) {

        if (rate > 0) {
            return ContextCompat.getColor(context, R.color.quotation_zhang_color);
        } else if (rate < 0) {
            return ContextCompat.getColor(context, R.color.quotation_die_color);
        } else {
            return ContextCompat.getColor(context, R.color.quotation_gray_color);
        }
    }

    /**
     * 这个是根据涨幅获得相对应的字体颜色,针对OlstarHomeActivity 黑色主题或者深色主题
     *
     * @param rate
     * @return
     */
    public static int getRateTextColorWithBlackBg(Context context, double rate) {

        if (rate > 0) {
            return ContextCompat.getColor(context, R.color.quotation_zhang_color);
        } else if (rate < 0) {
            return ContextCompat.getColor(context, R.color.quotation_die_color);
        } else {
            return ContextCompat.getColor(context, R.color.white);
        }
    }

    /**
     * 这个是根据涨幅获得相对应的箭头
     *
     * @param context
     * @param rate
     * @return
     */
    public static String getRateTextArrow(Context context, double rate) {

        if (rate >= 0) {
            return "▲";
        } else {
            return "▼";
        }
    }


    /**
     * 这个排名的字体颜色
     *
     * @param rank
     * @return
     */
    public static int getRankTextColor(int rank) {
        switch (rank) {
            case 0:
                return Color.parseColor("#fe0200");
            case 1:
                return Color.parseColor("#ff7200");
            case 2:
                return Color.parseColor("#ffae00");
            default:
                return Color.parseColor("black");

        }
    }

//    /**
//     * 首页 排行榜 右边有个小logo
//     * @param position
//     * @return
//     */
//    public static int getRankLogo(int position){
//        switch (position){
//            case 0:
//               return R.drawable.ic_rank_logo_1;
//            case 1:
//                return R.drawable.ic_rank_logo_2;
//            case 2:
//                return R.drawable.ic_rank_logo_3;
//            default:
//                return R.drawable.ic_rank_logo_3;
//        }
//    }

//    /**
//     * 排行榜前3名用图片显示名次
//     * @param position
//     * @return
//     */
//    public static int getRankNo(int position){
//        switch (position){
//            case 0:
//                return R.drawable.ic_rank_no_1;
//            case 1:
//                return R.drawable.ic_rank_no_2;
//            case 2:
//                return R.drawable.ic_rank_no_3;
//            default:
//                return 0;
//        }
//    }
//
//    /**
//     * 这个是行情页面OlstarHome背景,根据涨幅而变化
//     * @param rate
//     * @return
//     */
//    public static int getQuotationBgByRate(double rate){
//        if(rate>0){
//            return R.drawable.xml_name_bg_gradient_red_2;
//        }else if(rate<0){
//            return R.drawable.xml_name_bg_gradient_green_2;
//        }else{
//            return R.drawable.xml_name_bg_gradient_gray_2;
//        }
//    }


    /**
     * 只是格式化显示逗号，并且最多2位小数,用于显示详细
     *
     * @param num
     */
    public static String tradeFormatNumOnlyComma(String num) {
        return tradeFormatNumOnlyComma(Double.parseDouble(num));
    }

    public static String tradeFormatNumOnlyComma(Double num) {
//        return new DecimalFormat("###,###,###,###,###.##").format(num);
//            NumberFormat nf = NumberFormat.getNumberInstance();
//            nf.setMaximumFractionDigits(2);
//            nf.setMinimumFractionDigits(2);
//            nf.setRoundingMode(RoundingMode.HALF_UP);
//            nf.setGroupingUsed(true);//如果想输出的格式用逗号隔开，可以设置成true
//            return nf.format(num);
        try {
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            df.setMinimumFractionDigits(2);
            df.setGroupingUsed(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                df.setRoundingMode(RoundingMode.HALF_UP);
            }
            Log.d("currHoldRunnable", "df.format(num)==" + df.format(num));

            return df.format(num);
        } catch (Exception e) {
        }
        return String.valueOf(num);

    }

}
