package com.procoin.util;

/**
 * Created by zhengmj on 17-5-22.
 */

public class MatchesUtil {
    /**
     * 匹配正常的整形数
     *
     * @param str
     * @return
     */
    public static boolean isMatchesIntOrLong(String str) {
        if (str != null && str.matches("[-]?[0-9]+$")) return true;
        return false;
    }

    /**
     * 匹配double的页面
     *
     * @param
     * @return
     */
    public static boolean isMatchesDoubleOrFloat(String str) {
        if (str != null && str.matches("[-]?[0-9.E]+$")) return true;
        return false;
    }

}
