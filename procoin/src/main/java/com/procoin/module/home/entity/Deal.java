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
 * 成交
 */
public class Deal implements TaojinluType {


    public String symbol;
    public int buySell;
    public String timestamp;

    public String dealAmount;
    public String  dealFee;
    public String dealPrice;




}
