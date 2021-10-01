package com.procoin.module.home.entity;

public enum TimeTypeEnum {

    WEEK(-1, "week"),
    MONTH(1, "month"),
    MONTH_3(3, "month3"),
    MONTH_6(6, "month6"),
    YEAR(12, "year");

    private int type;
    private final String timeType;

    TimeTypeEnum(int type, String timeType) {
        this.type = type;
        this.timeType = timeType;
    }

    public String getTimeType() {
        return timeType;
    }
}