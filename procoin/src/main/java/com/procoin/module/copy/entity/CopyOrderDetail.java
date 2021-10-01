package com.procoin.module.copy.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.module.home.entity.Position;

/**
 * 跟单详情
 */
public class CopyOrderDetail implements TaojinluType {

    public long orderId;// 跟单ID
    public long userId;// 用户ID
    public String profitCash;// 持有盈亏额=最新持有市值+余额-投入总金额
    public String profitRate;// 持有盈亏额%
    public String tolBalance;// 总额
    public String balance;// 余额

    public double maxCopyBalance;// 每次跟单最大金额￥
    public double stopWin;// 设置止盈的百分比，如：0.2，0不设置
    public double stopLoss;// 设置止损的百分比，如：-0.2，0不设置

    public String stopWinValue;// 设置止盈的百分比，如：0.2，0不设置
    public String stopLossValue;// 设置止损的百分比，如：-0.2，0不设置

    public int isDone;// 订单状态，0运行中，1已取消
    public double doneDegree;

    public long copyUid;// 被跟用户的ID
    public String copyName;// 高手名称
    public String copyHeadUrl;// 高手头像
    public long fansCount;// 粉丝数量，365个关注
    public String score;// 综合评分


    public Group<Position> holdList;
    public Group<CopyOrderPieChart> holdDistribute;


}
