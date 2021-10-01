package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 币币订单记录
 */
public class CoinTradeHistory implements TaojinluType {

    public long orderId;
    public String attach = "0";// 对应火币订单ID
    public int attachType;// 0:火币交易所
    public long userId;// 账户ID
    public String symbol;// 数字货币符号
    public String price;// (USDT)委托价格
    public String priceCash;// (￥)委托价格
    public String balance;// (USDT)我的委托买入金额
    public String balanceCash;// (￥)我的委托买入金额
    public String tolBalance;// (USDT)委托买入总金额(包括跟单)
    public String tolBalanceCash;// (￥)委托买入总金额(包括跟单)
    public String tolAmount;// (symbol)委托卖出数量(包括跟单)
    public String amount;// (symbol)我的委托卖出数量
    public String dealTolBalance;// (包括跟单)已成交金额USDT（包含手续费）
    public String dealTolAmount;// (包括跟单)已成交数量币（包含手续费）
    public String dealTolFee;// (包括跟单)手续费买：币的数量，卖：USDT的数量，包括跟单
    public String dealBalance;// (我占的比例)已成交金额USDT（包含手续费）
    public String dealAmount;// (我占的比例)已成交数量币（包含手续费）
    public String dealFee;// (我占的比例)手续费买：币的数量，卖：USDT的数量，包括跟单

    public String usdtRate;// 当时下单usdt汇率

    public int state;// 订单状态
    public String stateDesc;// 订单状态描述
    public int buySell;// 买卖类型
    public int isLimit;// 0：非限价单，1：限价单
    public long createTime;// 创建时间
    public long doneTime;// 完成时间
    public int isMulti;// 0代表自己订单，1代表参合跟单用户

    public String copyBalance;// 跟单的金额或的数量
    public String predictBalance;// 预计数量或金额



}
