package com.procoin.widgets.quotitian.entity;

import com.procoin.http.base.TaojinluType;

import java.io.Serializable;

/**
 * Created by zhengmj on 17-5-19.
 */

public class StarArkBidBean implements TaojinluType, Serializable {
    public String price; //交易价格
    public String amount; //交易数量
    public int depthRate;//深度占比,最大100

    public boolean isEmpty;


}
