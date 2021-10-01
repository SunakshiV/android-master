package com.procoin.module.wallet.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;

/**
 *
 */
public class OrderInfo implements TaojinluType {


    public long orderId;
    public long userId;// 账户ID
    public String profit;// 盈亏
    public String profitRate;// 盈亏涨幅
    public String buySell;// 开仓时看涨看跌类型，1代表看涨，-1代表看跌
    public String buySellValue;// 看涨（做多）、看跌（做空）
    public String price;// 委托价
    public String openBail;// 我的保证金(USDT)
    public int openDone;// 开仓：是否完成,0开仓中，1开仓结束
    public String openState;// 开仓成交状态值
    public String openStateDesc;// 开仓状态描述
    public String openPrice;// 开仓价
    public String openHand;// 开仓手数
    public String openFee;// 开仓：手续费，买涨：币的数量，买跌：USDT的数量
    public long openTime;// 开仓：时间

    public String stopWinPrice;// 设置止盈价，0不设置
    public String stopLossPrice;// 设置止损价，0不设置

    public int closeDone;// 平仓：是否完成,0平仓中，1平仓结束
    public String closeState;// 平仓：成交状态表示
    public String closeStateDesc;// 平仓：成交状态描述
    public String closeFee;// 平仓手续费
    public String closePrice;// 平仓成本价
    public long closeTime;// 平仓：时间

    public int multiNum;// 杠杆倍数5,10
    public String symbol;
    public String last;// symbol行情最新现价
    public String rate;// symbol行情现价涨幅

    public int priceDecimals;// 价格小数位数

    public String nowStateDesc;// 当前所在状态

    public Group<CloseDetails> closeDetails;


}
