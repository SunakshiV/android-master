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
public class AccountType implements TaojinluType {

    public String accountName;
    public String accountType;

    public AccountType(String accountName, String accountType) {
        this.accountName = accountName;
        this.accountType = accountType;
    }
}
