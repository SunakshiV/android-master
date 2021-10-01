package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

public class RadarChart implements TaojinluType {

    public long userId;
    public long refreshTime;// 刷新时间
    public String tolIncomeRate;// 高手收益率（+7.8%）：反映的是大V最近一段时间内的收益走势情况，数值越高，代表最近大V个人盈利能力越强。
    public String profitShare;// 盈利占比（92.3%）:跟随某大V的所有人次中，盈利人次占总跟单人次的比例。数字越高，代表，跟随该大V盈利的可能性就越大。
    public int copyNum;// 在途跟单人数(102):某大V的跟单人数，数值越大，代表该段时间内跟随者越多，大V人气热度越高。
    public int copyNumRank;// 全局排名名次
    public String copyRate;// 用户跟单收益率（+6.7%）：跟随某大V所有用户的平均收益率，数值越高，代表跟随该大V用户收益越好。
    public String copyBalance;// 跟单盈亏累计金额（￥120.32）:代表过往某一段时间，跟随某大V的用户，营收的总金额。数值越大，代表，该时间段内，跟随用户赚的钱越多，但不能反映用户收益率越高。
    public int copyBalanceRank;// 全局排名名次




    public ScoreData scoreData;


    public String score;// 最后综合分数


}
