package com.procoin.module.myhome.entity;

/**
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 现金订单
 */
public class OrderCash implements TaojinluType {



    public long orderCashId;//
    public long handleUid;// 负责处理此订单的服务人员id
    public long receiptId;// 用户选择的支付方式ID
    public long userId;// 账户ID
    public String symbol;// 数字货币符号

    public String balanceCny;// 委托买入数字货币的金额￥
    public String amount;// 买入卖出数量
    public String priceCny;// 买入价￥或卖出价￥

    public int buySell;// 买卖方向

//    wait_pay(0, "待付款"), //
//    mark_pay(1, "已标记付款"), //
//    done(2, "已完成"), // 完成收款，生成正式订单
//    expire(-1, "已过期"), //
//    cancel(-2, "已撤销"), //
//    unpay_cancel(-3, "系统撤销");//
    public int state;// 订单状态

    public String stateDesc;
    public long createTime;// 创建时间
    public long doneTime;// 完成时间
    public long expireTime;

    public Receipt receipt;

}
