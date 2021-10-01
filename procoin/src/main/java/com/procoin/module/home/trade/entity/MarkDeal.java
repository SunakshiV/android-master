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
public class MarkDeal implements TaojinluType {


    public String amount;
    public String buySell;
    public String price;
    public String time;

    @Override
    public String toString() {
        return "amount=="+amount+" "+"buySell=="+buySell+" "+"price=="+price+" "+"time=="+time;
    }
}
