package com.procoin.module.kbt.app.lightningprediction.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

import java.util.List;

/**
 *
 */
public class PreGame implements TaojinluType {

    public String symbol;
    public String startTime;// 本场开始时间:格式化：00:00:00
    public String midTime;// 本场中单时间（停止投注时间）:格式化：00:02:00
    public String endTime;// 本场结束时间:格式化：00:04:00
    public int tolIndex;// 总根数，如：4*60
    public int stopIndex;// 当前索引达到停止索引
    public int curIndex;// 本场索引(可用来计时器)
    public boolean isVote;// 本场我是否可以投票，false停止投票，true可以投票
    public double votePrice;// 猜这个值多少分钟后是否超过投票的值
    public List<Minute> minutes;// 分时数据
    public List<Integer> hisVotes;// 往期预测结果：-1不超，1超过
    public int myVote;// 你本场投票结果：0没参，-1不超，1超过

    public int clearChart;//是否清除图表数据重新画,0不用,1重新画

    public long tolAbilityHigh;//买超过的刻豆
    public long tolAbilityLow;//买不超过的刻豆
    public long tolAbilityValue;// 本场累计总刻豆


    @Override
    public String toString() {
        return "minutes=="+minutes.toString()+" hisVotes=="+hisVotes;
    }
}
