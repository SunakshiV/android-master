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
 * 首页存币宝列表数据
 */
public class Store implements TaojinluType {


    public String amount;
    public String amountSymbol;
    public String amountTip;
    public String profit;
    public String profitSymbol;
    public String profitTip;
    public String storeSymbol;

    public String frozenAmount;
    public String holdAmount;


}
