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
 * 关注
 */
public class Attention implements TaojinluType {


    public String correctRate;
    public String days;
    public String headUrl;
    public String monthProfit;
    public String totalProfit;
    public long userId;
    public String userName;

    public String entryTime;
    public String expireTime;//到期时间戳
    public int subIsFee;//订阅是否要收费 0不用 1收费
    public int isExpireTime;//是否订阅过期 0没 1过期



}
