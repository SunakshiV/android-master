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
public class CoinResult implements TaojinluType {

    public String amount;
    public String inOutTip;
    public int maxWithdrawDecimals;
    public String minWithdrawAmt;
    public String symbol;
    public String withdrawFee;
    public String[] chainTypes;
    public String address;


}
