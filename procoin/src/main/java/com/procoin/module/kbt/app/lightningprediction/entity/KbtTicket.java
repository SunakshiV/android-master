package com.procoin.module.kbt.app.lightningprediction.entity;

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
public class KbtTicket implements TaojinluType {



    public String name;
    public String price;
    public int type;



    @Override
    public String toString() {
        return "name==" + name + "  price==" + price + " type==" + type ;
    }
}
