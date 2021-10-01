package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

public class Market implements TaojinluType {

    public String symbol;
    public String name;
    public String rate;
    public String price;
    public String priceCny;
    public String amount;
    public String tip;
    public String marketType;

    //这2个字段是搜索用到
    public int type;
    public String url;

    public long sortTime;//保存历史记录时排序用到


    public boolean checked;//排序页面 删除用到


    public Market() {
    }

    public Market(String symbol,String name, long sortTime) {
        this.symbol = symbol;
        this.name = name;
        this.sortTime = sortTime;
    }

    public Market(String symbol,long sortTime) {
        this.symbol = symbol;
        this.sortTime = sortTime;
    }

}
