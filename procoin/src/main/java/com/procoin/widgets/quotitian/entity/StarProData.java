package com.procoin.widgets.quotitian.entity;


import com.procoin.http.base.Group;
import com.procoin.module.home.trade.entity.MarkDeal;

import java.io.Serializable;
import java.util.List;

/**
 * 币种产品内容
 * Created by zhengmj on 17-5-19.
 */
public class StarProData extends StarBaseProData implements Serializable{

    public String marketType;//
    public double open; // 今日开盘价
    public double preClose; // 昨日收盘价
    public double yesClose; // 昨日收盘价（股票有）
    public double last; // 当前价格 相当于今日收盘
    public String lastCny;// 当前价格折合人民币

    public String high; // 今日最高价
    public String low; // 今日最低价

    public String amount; // 成交数量、总成交量6

    public double balance; // 成交金额、总成交额7
    public String sells;// 委卖价格档位List<PrSl>
    public String buys;// 委买价格档位List<PrSl>
    public String date; // 日期:20111201
    public String time; // 最后更新时间:090502
    public double ratio;// 换手率

    public int priceDecimals;// 价格小数位数
    public int amountDecimals;// 数量小数位数
    public String tip;//火币标志


    public String openMarketStr;// 格式：收市，开市
    public String currency;// 货币：USDT、CNY、HKD、USD、YPY
    public String prefixType;//
    public int isTrade;//是否可交易 0不可交易  1可以交易


    public Group<MarkDeal> dealList;

}