package com.procoin.module.legal.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.module.myhome.entity.AddPaymentTern;
import com.procoin.module.myhome.entity.Receipt;

/**
 *
 */
public class OptionalOrder implements TaojinluType {

    public long adId;//
    public long userId;// 用户ID
    public String userName;
    public String userLogo;
    public String buySell;// buy购买，sell出售
    public String price;// 价格cny
    public String minCny;// 最小限额cny
    public String maxCny;// 最大限额cny
    public String amount;// 委托数量usdt，当buySell=-1将会冻结余额账户的数量USDT
    public String dealAmount;// 成交数量usdt，本次广告一直成交的完成的数量
    public String frozenAmount;// 冻结数量usdt，只有出售时usdt有效
//    public Group<AddPaymentTern> payWay;// 显示支付方式，json格式数组
    public String payWay;// 显示支付方式，json格式数组

    public int timeLimit;// 交易时限(秒)，300秒
    public String content;// 留言内容
    public long createTime;// 创建时间
    public long updateTime;// 更新时间
    public int isOnline;// 上架状态，0下架，1上架

    public int orderNum;// 完成订单数量
    public String limitRate;// 完成速度比率

    public int isPayAli;// 是否支持支付定，0不支持，1支持
    public int isPayWx;// 是否支持微信，0不支持，1支持
    public int isPayBank;// 是否支持银行卡，0不支持，1支持

    public int synVersion;// 同步版本



}
