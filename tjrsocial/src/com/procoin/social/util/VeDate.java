package com.procoin.social.util;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类
 *
 * @version 1.0
 * @date
 */
public class VeDate {

    /**
     * 分析时间数据
     *
     * @param stocktime 时间格式09:29:30
     * @return
     */
    public static boolean isStartingRunDate(String stocktime) {
        try {
            if (stocktime == null) return true;
            String[] st = stocktime.split(":");
            if (st.length != 3) return true;
            if (Integer.parseInt(st[0] + st[1] + st[2]) < 83000) return false;
            if (Integer.parseInt(st[0] + st[1] + st[2]) > 150500) return false;
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * 将时间转换成整型数据（如15：01：59，转换后就变成了150159）
     *
     * @param time 如15：01：59
     * @return 150159
     */
    public static int timeTransformationToInt(String time) {
        if (time != null) {
            String temp[] = time.split(":");
            if (temp.length == 3) {
                return Integer.parseInt(temp[0] + temp[1] + temp[2]);
            }
        }
        return -1;
    }

    /**
     * 截取时间
     *
     * @param time       时间字符串
     * @param keepHour   true 保留时
     * @param keepMinute true 保留分
     * @param keepSecond true 保留秒
     * @return
     */
    public static String cutStringTime(String time, boolean keepHour, boolean keepMinute, boolean keepSecond) {
        String result = null;
        if (time != null) {
            String[] per = time.split(":");
            if (per != null && per.length >= 3) {
                if (keepHour) {
                    result = per[0];
                }
                if (keepMinute) {
                    if (result != null) {
                        result += ":" + per[1];
                    } else {
                        result = per[1];
                    }
                }
                if (keepSecond) {
                    if (result != null) {
                        result += ":" + per[2];
                    } else {
                        result = per[2];
                    }
                }
            }
        } else {
            return "";
        }
        return result;
    }

    /**
     * 得到现在手机时间
     *
     * @return
     */
    public static Date getNow() {
        Date currentTime = new Date();
        return currentTime;
    }

    /**
     * 将yyyyMMddHHmmss格式字符串转换为时间
     *
     * @param strDate
     * @return
     */
    public static Date strLongToDate(String longtime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            ParsePosition pos = new ParsePosition(0);
            return formatter.parse(longtime, pos);
        } catch (Exception e) {
            // TODO: handle exception
            return getNow();
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回yyyyMMddHHmmss
     */
    public static String getyyyyMMddHHmmss(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(date);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getDateTYYYYHHmm(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            return formatter.format(getNow());
        }
    }

    /**
     * 2012-12-12 12:12
     *
     * @param strLongToDate
     * @return
     */
    public static String getStockBarTime(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            return formatter.format(getNow());
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy-MM-dd
     */
    public static String getDateYYYYMMDD(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            return formatter.format(getNow());
        }
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int getTwoDays(Date nDate, Date hDate) {
        try {
            return (int) getDays(VeDate.getDateTYYYYHHmm(nDate), VeDate.getDateTYYYYHHmm(hDate));
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 两个时间之间的天数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static long getDays(String date1, String date2) {
        if (date1 == null || date1.equals("")) return 0;
        if (date2 == null || date2.equals("")) return 0;
        // 转换为标准时间
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = null;
        java.util.Date mydate = null;
        try {
            date = myFormatter.parse(date1);
            mydate = myFormatter.parse(date2);
        } catch (Exception e) {
            return 0;
        }
        long day = (date.getTime() - mydate.getTime()) / (24 * 60 * 60 * 1000);
        return day;
    }

    /**
     * 获取时间 小时:分 HH:mm
     *
     * @return
     */
    public static String getDateToHHmm(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    /**
     * 获取时间 小时:分 HH:mm:ss
     *
     * @return
     */
    public static String getDateToHHmmss(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    /**
     * 获取时间 MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateToMMddHHmm(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    /**
     * 获取时间 MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getDateToMMddHHmmss(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm:ss");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式MM月dd日
     */
    public static String getStringDateMMddYY(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 ");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式MM月dd日
     */
    public static String getStringDateMMddformsg(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日 HH:mm");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    // /**
    // * 格式化群聊主页面时间
    // *
    // * @param hDate
    // * @return
    // */
    // public static String getChatTimeFormat(Date hDate) {
    // try {
    // String dmessge = "";
    // int num = VeDate.getTwoDay(VeDate.getNow(), hDate);
    // Log.d("t", "。。。。。。。。。。。。。。。。。。天数num="+num);
    // if (num == 0) dmessge = "今天 " + VeDate.getDateToHHmm(hDate);
    // else if (num == 1) dmessge = "昨天 " + VeDate.getDateToHHmm(hDate);
    // else dmessge = VeDate.getStringDateMMddYY(hDate);
    // return dmessge;
    // } catch (Exception e) {
    // // TODO: handle exception
    // return "";
    // }
    // }

    /**
     * 判断是不是3天内 ,true 是3天内，false 是3天外
     *
     * @param hDate 时间
     * @return
     */
    public static boolean getThreeday(Date hDate) {
        if (hDate != null) {
            long chattime = getNow().getTime() - hDate.getTime();
            long day = 60 * 60 * 24 * 1000;
            if (chattime <= day * 3) {

                // Log.i("lv", "3天内" + chattime);
                return true;
            } else {
                // Log.i("lv", "3天外");
                return false;
            }
        } else {
            return false;
        }

    }

    /**
     * 半天内是true ，半天外是false
     *
     * @param oDate oldTime
     * @param tDate newTime
     * @return
     */
    public static boolean halfday(Date oDate, Date tDate) {
        if (oDate != null && tDate != null) {
            long halfday = 60 * 60 * 12 * 1000;
            if (tDate.getTime() - oDate.getTime() < halfday) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 这个判断五分钟之内,还是在五分钟外
     *
     * @param oDate
     * @param tDate
     * @return
     */
    public static boolean fivemin(Date oDate, Date tDate) {
        if (oDate != null && tDate != null) {
            long fivemin = 60 * 5 * 1000;
            if (tDate.getTime() - oDate.getTime() < fivemin) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // /**
    // * 格式化时间
    // *
    // * @param hDate
    // * @return
    // */
    // public static String getChatTimeFormat(Date hDate) {
    // String dmessge = "";
    // if (hDate == null) return dmessge;
    // // long hours = 60 * 60 * 1000;
    // long day = 60 * 60 * 24 * 1000;
    // long chattime = VeDate.getChatRoomNow().getTimeInMillis() -
    // hDate.getTime();
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(hDate);
    // if (chattime < day) {
    // dmessge = getChatRoomAMPM(calendar) + VeDate.getDateToHHmm(hDate);
    // } else if (chattime < (day * 8)) {
    // int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    // int day_now = VeDate.getChatRoomNow().get(Calendar.DAY_OF_WEEK) - 1;
    // int subday = day_now - day_of_week;
    // switch (subday) {
    // case 0:
    // dmessge = getChatRoomAMPM(calendar) + VeDate.getDateToHHmm(hDate);
    // return dmessge;
    // case 1:
    // dmessge = "昨天 " + VeDate.getDateToHHmm(hDate);
    // return dmessge;
    // case 2:
    // dmessge = "前天 " + VeDate.getDateToHHmm(hDate);
    // return dmessge;
    // default:
    // break;
    // }
    // String weakday;
    // switch (day_of_week) {
    // case 0:
    // weakday = "周日 ";
    // break;
    // case 1:
    // weakday = "周一 ";
    // break;
    // case 2:
    // weakday = "周二 ";
    // break;
    // case 3:
    // weakday = "周三 ";
    // break;
    // case 4:
    // weakday = "周四 ";
    // break;
    // case 5:
    // weakday = "周五 ";
    // break;
    // case 6:
    // weakday = "周六 ";
    // break;
    // default:
    // weakday = "周日 ";
    // break;
    // }
    // dmessge = weakday + VeDate.getDateToHHmm(hDate);
    // } else {
    // try {
    // int num = VeDate.getTwoDays(VeDate.getNow(), hDate);
    // if (num >= 365) {
    // dmessge = VeDate.getStringDateMMddYY(hDate);
    // } else {
    // dmessge = VeDate.getDateToMMddHHmmss(hDate);
    // }
    // } catch (Exception e) {
    // return dmessge;
    // }
    // }
    // return dmessge;
    // }

    // /**
    // * 格式化时间
    // *
    // * @param hDate
    // * @return
    // */
    // public static String getChatRoomTimeFormat(Date hDate) {
    // String dmessge = "";
    // if (hDate == null)
    // return dmessge;
    // // long hours = 60 * 60 * 1000;
    // long day = 60 * 60 * 24 * 1000;
    // long chattime = VeDate.getChatRoomNow().getTimeInMillis()
    // - hDate.getTime();
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(hDate);
    // if (chattime < day) {
    // dmessge = getChatRoomAMPM(calendar) + VeDate.getDateToHHmm(hDate);
    // } else if (chattime < (day * 8)) {
    // int day_of_week = calendar.get(Calendar.DAY_OF_WEEK) - 1;
    // int day_now = VeDate.getChatRoomNow().get(Calendar.DAY_OF_WEEK) - 1;
    // int subday = day_now - day_of_week;
    // switch (subday) {
    // case 0:
    // dmessge = getChatRoomAMPM(calendar)
    // + VeDate.getDateToHHmm(hDate);
    // return dmessge;
    // case 1:
    // dmessge = "昨天 " + VeDate.getDateToHHmm(hDate);
    // return dmessge;
    // case 2:
    // dmessge = "前天 " + VeDate.getDateToHHmm(hDate);
    // return dmessge;
    // default:
    // break;
    // }
    // String weakday;
    // switch (day_of_week) {
    // case 0:
    // weakday = "周日 ";
    // break;
    // case 1:
    // weakday = "周一 ";
    // break;
    // case 2:
    // weakday = "周二 ";
    // break;
    // case 3:
    // weakday = "周三 ";
    // break;
    // case 4:
    // weakday = "周四 ";
    // break;
    // case 5:
    // weakday = "周五 ";
    // break;
    // case 6:
    // weakday = "周六 ";
    // break;
    // default:
    // weakday = "周日 ";
    // break;
    // }
    // dmessge = weakday + VeDate.getDateToHHmm(hDate);
    // } else {
    // try {
    // int num = VeDate.getTwoDay(VeDate.getNow(), hDate);
    // if (num >= 365) {
    // dmessge = VeDate.getStringDateMMddYY(hDate);
    // } else {
    // dmessge = VeDate.getDateToMMddHHmm(hDate);
    // }
    // } catch (Exception e) {
    // return dmessge;
    // }
    // }
    // return dmessge;
    // }

    /**
     * 获取现在时分秒时间合并成int
     *
     * @param date 格式:HHmmss
     * @return int
     */
    public static int getStringHHMMSSToInt(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("HHmmss");
        String dateString = formatter.format(date);
        return Integer.parseInt(dateString);
    }

    /**
     * 根据一个日期，返回是星期几的字符串
     *
     * @param sdate
     * @return
     */
    public static String getWeek(Date date) {
        // 再转换为时间
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE").format(c.getTime());
    }

    public static String getWeekStr(Date date) {
        String str = VeDate.getWeek(date);
        if (str != null) {
            return str.replaceAll("星期", "周");
        } else {
            return "";
        }
    }

    /**
     * 格式化时间
     *
     * @param hDate
     * @return
     */
    public static String getChatTimeFormat(Date hDate) {
        String timeFormat = "";
        if (hDate == null) return timeFormat;
        Date nDate = VeDate.getNow();
        int days = VeDate.getTwoDays(nDate, hDate);
        // int k = VeDate.getStringHHMMSSToInt(hDate);
        if (days < 0) {
            timeFormat = VeDate.getWeekStr(hDate);
            timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
        } else if (days == 0) { // 今天
            timeFormat = "今天";
            timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
        } else if (days == 1) { // 昨天
            timeFormat = "昨天";
            timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
        } else if (days == 2) { // 前天
            timeFormat = "前天";
            timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
        } else if (days >= 365) {
            timeFormat = VeDate.getStringDateMMddYY(hDate);
        } else {
            timeFormat = VeDate.getDateToMMddHHmmss(hDate);
        }
        return timeFormat;
    }

    /**
     * 格式化时间
     *
     * @param hDate
     * @return
     */
    public static String getChatTimeFormatforMsg(Date hDate) {
        String timeFormat = "";
        if (hDate == null) return timeFormat;
        timeFormat = VeDate.getStringDateMMddformsg(hDate);
        return timeFormat;
    }

    /**
     * 获取现在的时间
     *
     * @return
     */
    public static Calendar getChatRoomNow() {
        Calendar rightNow = Calendar.getInstance();
        return rightNow;
    }

    /**
     * 获取是早上还是下午
     *
     * @param calendar
     * @return
     */
    public static String getChatRoomAMPM(Calendar calendar) {
        int r = calendar.get(Calendar.AM_PM);
        if (r == Calendar.AM) {
            return "早上 ";
        } else {
            return "下午 ";
        }
    }

    /**
     * 1天内是true ，1天外是false
     *
     * @param oDate
     * @param tDate
     * @return
     */
    public static boolean twohalfday(Date oDate, Date tDate) {
        long halfday = 60 * 60 * 24 * 1000;
        if (tDate.getTime() - oDate.getTime() < halfday) {
            return true;
        } else {
            return false;

        }

    }

    /**
     * 格式化时间
     *
     * @param hDate
     * @return
     */
    public static String getChatRoomTimeFormat(Date hDate) {
        String timeFormat = "";
        if (hDate == null) return timeFormat;
        Date nDate = VeDate.getNow();
        int days = VeDate.getTwoDays(nDate, hDate);
        // int k = VeDate.getStringHHMMSSToInt(hDate);
        if (days < 0) {
            timeFormat = VeDate.getWeekStr(hDate);
            timeFormat = timeFormat + VeDate.getDateToHHmm(hDate);
        } else if (days == 0) { // 今天
            timeFormat = "今天";
            timeFormat = timeFormat + VeDate.getDateToHHmm(hDate);
        } else if (days == 1) { // 昨天
            timeFormat = "昨天";
            timeFormat = timeFormat + VeDate.getDateToHHmm(hDate);
        } else if (days == 2) { // 前天
            timeFormat = "前天";
            timeFormat = timeFormat + VeDate.getDateToHHmm(hDate);
        } else if (days >= 365) {
            timeFormat = VeDate.getStringDateMMddYY(hDate);
        } else {
            timeFormat = VeDate.getDateToMMddHHmmss(hDate);
        }
        return timeFormat;
    }

    /**
     * 将yyyyMMdd格式字符串转换为时间
     *
     * @param strDate
     * @return
     */
    public static Date intToDate(int longtime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            ParsePosition pos = new ParsePosition(0);
            return formatter.parse(String.valueOf(longtime), pos);
        } catch (Exception e) {
            // TODO: handle exception
            return getNow();
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式MM月dd日
     */
    public static String getStringDateMMddforKlineSes(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MM月dd日");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            // TODO: handle exception
            return formatter.format(getNow());
        }
    }

    /**
     * 将yyyyMMdd格式字符串转换为时间
     *
     * @param strDate
     * @return
     */
    public static Date stringToDate(String longtime) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            ParsePosition pos = new ParsePosition(0);
            return formatter.parse(longtime, pos);
        } catch (Exception e) {
            return getNow();
        }
    }

    /**
     * 获取现在时间
     *
     * @return 返回短时间字符串格式yyyy年MM月dd日
     */
    public static String getStringDateyyyyMMdd(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 ");
        try {
            return formatter.format(date);
        } catch (Exception e) {
            return formatter.format(getNow());
        }
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
                timeFormat = "今天";
                timeFormat = timeFormat + " " + VeDate.getDateToHHmm(hDate);
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

    public static String getWeekOfDate(Date date) {
        String[] weekDaysName = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int intWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        return weekDaysName[intWeek];
    }


}
