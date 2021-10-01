package com.procoin.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kechenng on 17-5-11.
 */

public class DateUtils {

    private static SimpleDateFormat formater = new SimpleDateFormat();

    public static final String TEMPLATE_yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String TEMPLATE_yyyyMMdd_HHmmss = "yyyy-MM-dd HH:mm:ss";
    public static final String TEMPLATE_yyyyMMdd_HHmm = "yyyy-MM-dd HH:mm";
    public static final String TEMPLATE_yyyyMMdd_HHmm2 = "yyyy-MM-dd\nHH:mm";

    public static final String TEMPLATE_HHMMMMDD = "HH:mm MM/dd";

    public static final String TEMPLATE_yyyyMMdd = "yyyyMMdd";
    public static final String TEMPLATE_yyyyMMddHHmm = "yyyyMMddHHmm";
    public static final String TEMPLATE_yyyyMMdd_divide = "yyyy-MM-dd";

    public static final String TEMPLATE_MMdd = "MM.dd";

    public static final String TEMPLATE_MMdd_HHmm = "MM-dd HH:mm";
    public static final String TEMPLATE_MMdd_HHmm_CN = "MM月dd日 HH:mm";

    public static final String TEMPLATE_HHmm = "HH:mm";
    public static final String TEMPLATE_HHmm2 = "HHmm";

    public static final String TEMPLATE_yyyyMMdd_HHmm_CN = "yyyy年MM月dd日 HH:mm";


    public static final String TEMPLATE_YYYYMM = "yyyy年MM月";

    public static final String TEMPLATE_MM = "MM月";

    public static final String TEMPLATE_yyyyMM = "yyyyMM";

    public static final String TEMPLATE_MM_dd = "MM.dd";
    public static final String TEMPLATE_MM_dd2 = "MM/dd";
    public static final String TEMPLATE_MM_dd3 = "MM-dd";


    public static final String TEMPLATE_HHmmss = "HH:mm:ss";

    /**
     * 时间字符串转换为Date
     * String originStrdate -> Class Date
     * originStrdate 对应的模板为:yyyyMMddHHmmss
     *
     * @param originStrdate 时间字符串
     * @return
     */
    public static Date strdate2Date(String originStrdate) {
        formater.applyPattern(TEMPLATE_yyyyMMddHHmmss);
        try {
            return formater.parse(originStrdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 时间字符串转换为Date,带模板
     * String originStrdate -> Class Date
     *
     * @param originStrdate         原始时间字符串
     * @param originStrdateTemplate 原始时间字符串对应的模板(如 20170511 就要对应 yyyyMMdd)
     * @return
     */
    public static Date strdate2Date(String originStrdate, String originStrdateTemplate) {
        formater.applyPattern(originStrdateTemplate);
        try {
            return formater.parse(originStrdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Date转换为时间字符串
     *
     * @param originStrdate
     * @param destTemplate
     * @return
     */
    public static String date2strdate(Date originStrdate, String destTemplate) {
        formater.applyPattern(destTemplate);
        return formater.format(originStrdate);
    }

    /**
     * 这个方法比较全能,这个不是时间戳
     *
     * @param dateString
     * @param parserFormat 要解析的类型
     * @param format       解析成什么样的类型
     * @return
     */
    public static String getStringDateOfString(String dateString, String parserFormat, String format) {

        try {
            SimpleDateFormat formatterParser = new SimpleDateFormat(parserFormat);
            ParsePosition pos = new ParsePosition(0);
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(formatterParser.parse(dateString, pos));
        } catch (Exception e) {
        }
        return "";
    }

    public static String getStringDateOfString(String dateString, String format) {
        return getStringDateOfString(dateString, TEMPLATE_yyyyMMddHHmmss, format);

    }

    /**
     * 这个方法比较全能,这个是时间戳的格式
     *
     * @param dateString 时间戳的格式
     * @param format     解析成什么样的类型
     * @return
     */
    public static String getStringDateOfString2(String dateString, String format) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(format);
            return formatter.format(new Date(Long.parseLong(dateString) * 1000));//时间戳是秒，所以要乘以1000
        } catch (Exception e) {
        }
        return "";
    }




    /**
     * 时间字符串样式格式化
     * String originStrdate -> String destStrdate
     *
     * @param originStrdate 原始时间字符串(格式必须为yyyyMMddHHmmss)
     * @param destTemplate  目标时间字符串模板
     * @return
     */
    public static String strdateFormat(String originStrdate, String destTemplate) {
        Date date = strdate2Date(originStrdate);
        formater.applyPattern(destTemplate);
        String result = "--";
        try {
            result = formater.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 动态时间 :  几分钟前  几小时前  昨天  前天  具体日期
     *
     * @param lastDate
     * @param f
     * @return
     */
    public static String formatDynmicTime(String lastDate, String f) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(f);
            Date date = format.parse(lastDate);
            long delta = new Date().getTime() - date.getTime();
            if (delta < 0)
//                return lastDate;// 防止有些时间不对显示的负数
                return "1分钟内";
            if (delta >= 3600000L * 24) {//
//                return delta / 3600000L / 24 + "天前";
                return getChatTimeFormat4(date);
            } else {
                long result = delta / 1000L / 60L;//算出分钟数
                if (result / 60 > 0) {
                    return result / 60 + "小时前";
                } else {
                    if (result == 0) {
                        return "1分钟内";
                    } else {
                        return result + "分钟前";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lastDate;
    }

    /**
     *
     * @param time  时间戳格式
     * @return
     */
    public static String getPastTime(String time) {
        try {
//            Date date = strdate2Date(time);
            Date date = new Date(Long.parseLong(time) * 1000);
            long delta = new Date().getTime() - date.getTime();
            if (delta < 0) {
                return "刚刚";
            }
            if (delta >= 3600000L * 24) {
                return getChatTimeFormat5(date);
            } else {
                long result = delta / 1000L / 60L;
                if (result / 60 > 0) {
                    return result / 60 + "小时前";
                } else {
                    if (result == 0) {
                        return "刚刚";
                    } else {
                        return result + "分钟前";
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static int getDeltaDays(String time) {
        try {
            Date date = strdate2Date(time);
            int nowDate = VeDate.getNow().getDate();
            int inputDate = date.getDate();
            if (VeDate.getNow().getMonth() == date.getMonth()) {
                return inputDate - nowDate;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -100;
    }

    public static String getThreeDaysText(String time) {
        try {
            Date date = strdate2Date(time);
            int nowDate = VeDate.getNow().getDate();
            int updateDate = date.getDate();
//            if (VeDate.getNow().getMonth() == date.getMonth()){ //因为后台加了判断是这三天内的，如果出错，请将这段注释还原成代码
            int delta = nowDate - updateDate;
            if (delta == 0) {
                return "今天";
            } else if (delta == 1) {
                return "昨天";
            } else {
                return "前天";
            }
//            } //还有这里


//            long delta = new Date().getTime() - date.getTime();
//            if (delta < 3600000L * 24 ){
//                return "今天";
//            }else {
//                Date nDate = VeDate.getNow();
//                int days = VeDate.getTwoDays(nDate,date);
//                if (days == 1) return "昨天";
//                if (days == 2) return "前天";
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getChatTimeFormat5(Date hDate) {
        String timeFormat = "";
        if (hDate == null) return timeFormat;
        Date nDate = VeDate.getNow();
        int days = VeDate.getTwoDays(nDate, hDate);
        if (days > 7) {
            int weeks = days / 7;
            if (weeks > 4) {
                int months = weeks / 4;
                timeFormat = months + "个月前";
                if (months > 12) {
//                    int years = months/12;
                    timeFormat = "一年前";
                }
            } else {
                timeFormat = weeks + "周前";
            }
        } else {
            if (days == 0) { // 今天
                timeFormat = VeDate.getDateToHHmm(hDate);
            } else if (days == 1) { // 昨天
                timeFormat = "昨天";
//                timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
            } else if (days == 2) { // 前天
                timeFormat = "前天";
//                timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
            } else {
                timeFormat = "一周内";
            }
        }
        return timeFormat;
    }

    /**
     * 最近7天  前3天用 今天 昨天 前天  其他用周几显示
     *
     * @param hDate
     * @return
     */
    public static String getChatTimeFormat3(Date hDate) {
        String timeFormat = "";
        if (hDate == null) return timeFormat;
        Date nDate = VeDate.getNow();
        int days = VeDate.getTwoDays(nDate, hDate);
        if (days > 6) {
            timeFormat = VeDate.getDateToMMddHHmm(hDate);
        } else {
            if (days == 0) { // 今天
                timeFormat = VeDate.getDateToHHmm(hDate);
            } else if (days == 1) { // 昨天
                timeFormat = "昨天";
                timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
            } else if (days == 2) { // 前天
                timeFormat = "前天";
                timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
            } else {
                timeFormat = getWeekOfDate(hDate);
                timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
            }
        }
        return timeFormat;
    }


    public static String getChatTimeFormat4(Date hDate) {
        String timeFormat = "";
        if (hDate == null) return timeFormat;
        Date nDate = VeDate.getNow();
        int days = VeDate.getTwoDays(nDate, hDate);
        if (days == 0) { // 今天
            timeFormat = VeDate.getDateToHHmm(hDate);
        } else if (days == 1) { // 昨天
            timeFormat = "昨天";
            timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
        } else if (days == 2) { // 前天
            timeFormat = "前天";
            timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
        } else {
            timeFormat = VeDate.getStockBarTime(hDate);
        }
        return timeFormat;
    }

    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }


    /**
     * 圈子私聊是否显示时间  >3分钟就显示 否则不显示
     *
     * @param lastDate
     * @param currDate
     * @return
     */
    public static boolean isShowTime(String lastDate, String currDate) {
        try {
            SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
            Date begin = dfs.parse(lastDate);
            Date end = dfs.parse(currDate);
            long between = (end.getTime() - begin.getTime()) / 1000;//除以1000是为了转换成秒
//			long minute1=between%3600/60;
//			Log.d("isShowTime","minute1=="+minute1);
            return between > 180;
//			long day1=between/(24*3600);
//			long hour1=between%(24*3600)/3600;
//			long minute1=between%3600/60;
//			long second1=between%60/60;
        } catch (Exception e) {
        }
        return true;
    }

    /**
     * 获取与当前系统时间的差值毫秒秒数
     * @param dateString   时间戳格式
     * @return  返回毫秒秒数
     */
    public static long getDifference(String dateString){

        return (new Date(Long.parseLong(dateString)*1000).getTime()-new Date().getTime());

    }

}
