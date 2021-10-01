package com.procoin.widgets.quotitian.entity;

import com.procoin.util.DateUtils;

public class MinTimeEntity {
    public int time;
    public double zjcj; //最新成交价
    public double vol; //成交量cjsl/100，已转化手为单位
    public double ma;  //黄线结果cjje/cjsl
    public float x; // x坐标
    public float minY; // 分时线Y坐标
    public float maY; // 黄线Y坐标
    public float volY; // 黄线Y坐标

    public double volOffset; // 当前与前一次偏移量
    public float rate; //已经格化
    public String timeFormat;

    public MinTimeEntity() {

    }

    public MinTimeEntity(int time, float zjcj) {
//        this.time = time;
        this.time=Integer.parseInt(DateUtils.getStringDateOfString2(String.valueOf(time),DateUtils.TEMPLATE_HHmm2));
//        Log.d("MinTimeEntity","time=="+this.time);
        this.zjcj = zjcj;
    }

    public MinTimeEntity(int time, float zjcj, float ma) {
//        this.time = time;
        this.time=Integer.parseInt(DateUtils.getStringDateOfString2(String.valueOf(time),DateUtils.TEMPLATE_HHmm2));
//        Log.d("MinTimeEntity","time=="+this.time);
        this.zjcj = zjcj;
        this.ma = ma;
    }

    @Override
    public String toString() {
        return "MinTimeEntity{" +
                "zjcj=" + zjcj +
                ", time=" + time +
                '}';
    }
}
