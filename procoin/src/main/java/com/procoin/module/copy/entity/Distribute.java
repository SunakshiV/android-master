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
public class Distribute implements TaojinluType {

    public String amount;
    public String fromPirce;
    public String price;
    public String toPirce;
    public String rate;
    public float x;
    public float y;

    @Override
    public String toString() {
        return "Distribute{" +
                "amount='" + amount + '\'' +
                ", fromPirce='" + fromPirce + '\'' +
                ", price='" + price + '\'' +
                ", toPirce='" + toPirce + '\'' +
                ", rate='" + rate + '\'' +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
