package com.procoin.module.wallet.entity;

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
public class StoreHistory implements TaojinluType {




    public String amount;
    public String createTime;
    public String storeSymbol;
    public String storeType;
    public String targetSymbol;
    public int inOut;
    public int isDone;
    public long recordId;
    public String remark;


}
