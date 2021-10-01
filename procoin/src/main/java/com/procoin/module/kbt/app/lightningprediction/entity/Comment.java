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
public class Comment implements TaojinluType {

//    {"receive":"{\"headUrl\":\"http://192.168.1.66:16678/cropyme/user/image/20190626174233963047533.png\",\"say\":\"很好玩哟\"}","success":true,"type":20003}

    public String headUrl;
    public String say;


    @Override
    public String toString() {
        return "headUrl==" + headUrl + "  say==" + say;
    }
}
