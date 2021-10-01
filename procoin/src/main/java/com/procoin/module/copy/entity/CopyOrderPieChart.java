package com.procoin.module.copy.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 */
public class CopyOrderPieChart implements TaojinluType {


    public String amount;
    public String balance;
    public String symbol;
    public float rate;

    public CopyOrderPieChart(String symbol,float rate){
        this.symbol=symbol;
        this.rate=rate;
    }






}
