package com.procoin.widgets.quotitian.entity;

import com.procoin.http.base.TaojinluType;

public class LineTimeEntity implements TaojinluType {
    public long time;
    public double price; //最新成交价
    public double amount; //成交量
    public double balance;  //交易额

    public LineTimeEntity(){}
    public LineTimeEntity(long time, double price, double amount, double balance) {
        this.time = time;
        this.price = price;
        this.amount = amount;
        this.balance = balance;
    }

    public LineTimeEntity(long time, double price, double amount) {
        this.time = time;
        this.price = price;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "price=="+price+"  amount=="+amount+"  balance=="+balance+"  time=="+time;
    }
}
