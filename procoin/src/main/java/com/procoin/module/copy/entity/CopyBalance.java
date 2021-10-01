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
 * 跟单资金
 */
public class CopyBalance implements TaojinluType {

    public double nextUsableBalance;
    public double profit;
    public double totalBalance;
    public double usableBalance;

    public String userId;
    public String userName;


}
