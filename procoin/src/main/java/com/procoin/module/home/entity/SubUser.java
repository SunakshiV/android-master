package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

public class SubUser implements TaojinluType {


    public long userId;// 大V的UID
    public String userName;//大V的名称
    public String headUrl;//大V头像
    public double totalProfit;// 大V总收益额
    public double monthProfit; // 大V月收益额
    public double correctRate;// 大V准确率(%)
    public int attentionNum; // 大V的关注人数
    public int myIsAttention; // 当前用户是否关注该大V
    public int myIsFollow; // 当前用户是否绑定该大V，0没绑定，1绑定，
    public int fanNum; // 大V的粉丝人数
    public double tolRadarWeight = 100;// 雷达图的总分数
    public double radarProfitRate; // 我的盈利能力(%)
    public double radarProfitRateWeight; // (雷达图)我的盈利能力(%)占比
    public int radarFollowNum; // 带单人气
    public double radarFollowNumWeight; // (雷达图)带单人气占比
    public double radarFollowWinRate; // 带单胜率(%)
    public double radarFollowWinRateWeight; // (雷达图)带单胜率(%)占比
    public double radarFollowProfitRate; // 带单收益率(%)
    public double radarFollowProfitRateWeight; // (雷达图)带单收益率(%)占比
    public double radarFollowBalance; // 带单盈利额
    public double radarFollowBalanceWeight; // (雷达图)带单盈利额占比
    public long entryTime;// (前端不用理)入驻时间戳
    public long days;// 入驻天数,如：入驻12天
    public int isOnline;// (前端不用理)是否上线显示，0下线，1上线显示
    public String describes; // 个人简介描述
    public String recommend; // 推荐描述
    public long realFollowCount;// (前端不用理)真实跟单人数
    public long expireTime;//过期时间戳
    public int isExpireTime;//是否订阅过期 0没 1过期

    public String subFeeTypeName;
    public String subFeeTypeUnit;
    public double subFee;
    public int subIsFee;// 订阅是否要收费，0不用，1代表要

    public String subNotice;//订阅提示
    public String followNotice;//申请绑定提示


    //这2个字段是搜索用到
    public int type;
    public String url;
    public long sortTime;

    public SubUser(){}

    public SubUser(long userId, String userName, String headUrl,long sortTime) {
        this.userId = userId;
        this.userName = userName;
        this.headUrl = headUrl;
        this.sortTime=sortTime;
    }


}
