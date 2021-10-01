package com.procoin.widgets.quotitian.entity;


import com.procoin.http.base.TaojinluType;

/**
 * 币种产品基类
 * Created by zhengmj on 17-5-19.
 */
public class StarBaseProData implements TaojinluType {

    public String symbol; //代码 wh000001
    public String amt;//这里是涨跌幅
    public double rate; //涨幅
}