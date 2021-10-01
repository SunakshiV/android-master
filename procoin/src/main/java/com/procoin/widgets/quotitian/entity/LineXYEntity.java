package com.procoin.widgets.quotitian.entity;

public class LineXYEntity {
    public long time;
    public double price; //最新成交价
    public double amount; //成交量
    public double balance;  //交易额
    public double ma;  //黄线结果cjje/cjsl
    public float coordinateX; // x坐标
    public float lineY; // 分时线Y坐标
    public float volY; // 黄线Y坐标

    public float rate; //已经格化
    public boolean isShowTime;

    public LineXYEntity(long time, double price, double amount, double balance) {
        this.time = time;
        this.price = price;
        this.amount = amount;
        this.balance = balance;
    }
}
