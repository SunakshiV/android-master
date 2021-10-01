package com.procoin.util;

/**
 * 时长单位，0:小时，1:天，2:月，3:年，4:永久
 * <p>
 * Created by zhengmj on 18-11-7.
 */

public enum TimeUnitEnum {

    HOUR(0, "小时"),
    DAY(1, "天"),
    MONTH(2, "月"),
    YEAR(3, "年"),
    FOREVER(4, "永久");

    public final int type;
    public final String value;

    TimeUnitEnum(int type, String value) {
        this.type = type;
        this.value = value;
    }

    public static int getTypeByValue(String value) {
        for (TimeUnitEnum v : values()) {
            if (v.value.equals(value)) {
                return v.type;
            }
        }
        return -1;
    }

    public static String getValueByType(int type) {
        for (TimeUnitEnum v : values()) {
            if (v.type == type) {
                return v.value;
            }
        }
        return "";
    }

}
