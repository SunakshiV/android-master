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
 * 订单
 */
public class Order implements TaojinluType {

    public long orderId;
    public String attach = "0";// 对应火币订单ID
    public int attachType;// 0:火币交易所
    public long userId;// 账户ID
    public String symbol;// 数字货币符号
    public String price;// 买入价或卖出价(USDT)
    public String balance;// 我的交易余额(USDT)，买：此值为标准，卖：此值不作为标准
    public String tolBalance;// 我的+跟单的总交易额，买：此值为标准，卖：此值不作为标准
    public String amount;// 委托卖出数字货币的数量，卖：此值为标准，买：此值不作为标准
    public String tolAmount;// (symbol)委托卖出数量(包括跟单)，卖：此值为标准，买：此值不作为标准
    public String myAmountRate;// 我卖出数量所占当时持仓比例
    public String dealTolBalance;// (包括跟单)已成交金额USDT（包含手续费）
    public String dealTolAmount;// (包括跟单)已成交数量币（包含手续费）
    public String dealTolFee;// (包括跟单)手续费买：币的数量，卖：USDT的数量，包括跟单
    public String dealBalance;// (我占的比例)已成交金额USDT（包含手续费）
    public String dealAmount;// (我占的比例)已成交数量币（包含手续费）
    public String dealFee;// (我占的比例)手续费买：币的数量，卖：USDT的数量，包括跟单

    public String dealAvgPrice;// 成交均价

    public String usdtRate;// 当时下单usdt汇率

    public int state;// 订单状态
    public String stateDesc;// 订单状态描述
    public int buySell;// 买卖类型
    public int isLimit;// 0：非限价单，1：限价单
    public String createTime;// 创建时间
    public String doneTime;// 完成时间
    public int openCopy;// 用户是否开放跟单权限，0没，1开
    public int isMulti;// 0代表自己订单，1代表参合跟单用户

    public String profit;//盈利

    public String profitShare;// 所有跟单(卖出)记录的赢利>0总和
    public String serviceShare;// 技术服务费=profit_share*service_scale
    public double serviceScale;// 技术服务费比例

    public String copyBalance;// 跟单委托额
    public String copyAmount;// 跟单委托数量
    public String copyDealBalance;// 跟单成交额
    public String copyDealAmount;// 跟单成交数量
    public String copyDealFee;// 跟单手续费









}
