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
public class CoinDesc implements TaojinluType {

    public String desc;
    public String maxWithdrawDecimals;
    public String blockUrl;
    public String circulateAmount;
    public String crowdfundPrice;
    public String issueAmount;
    public String issueDate;
    public String officialWebUrl;
    public String whitePaperUrl;

    public String name;
    public String symbol;

    public int isTrade;//是否可交易 0不可交易  1可以交易


}
