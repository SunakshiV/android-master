package com.procoin.module.home.trade.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 *
 */
public class CheckOut implements TaojinluType {

    public String minAmount;//可买最小数量
    public String maxAmount;//可买最大数量
    public String amount;//数量
    public String myCoin;//当前buySell=1:usdt余额，buySell=-1:BTC数量，初始化请传空
    public String balance;//交易额
    public String balanceCny;//交易额，折合人民币
    public String price;//价格
    public String priceCny;//价格，折合人民币
    public int openCopy;//0没有跟单 1有跟单
    public String copy;//跟单时候显示
    public String usdtRate;//usdt汇率

}
