package com.procoin.module.myhome.entity;

import com.procoin.http.base.TaojinluType;


public class CoinTradeCount implements TaojinluType {

    public String symbol;
    public String num;
    public long dayTime;
    public String profit;

    public CoinTradeCount(String symbol, String count) {
        this.symbol = symbol;
        this.num = count;
    }
}
