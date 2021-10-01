package com.procoin.module.myhome.entity;

/**
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 添加收款方式
 */
public class AddPaymentTern implements TaojinluType {


    public long paymentId;
    public int receiptType;
    public String receiptTypeValue;
    public String receiptLogo;


}
