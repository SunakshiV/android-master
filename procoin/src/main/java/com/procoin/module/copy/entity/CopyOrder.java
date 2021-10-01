package com.procoin.module.copy.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 跟单
 */
public class CopyOrder implements TaojinluType {




    public long tradeId;
    public long copyOrderId;// 对应跟单的ID
    public long orderId;// 对应委托订单的ID
    public long userId;// 账户ID
    public String symbol;// 数字货币符号
    public String costPrice;// 价格(USDT)
    public String price;// 价格(USDT)，

    public String balance;// 买：表示消耗USDT的数量（乘以10^8）
    public String amount;// 卖：表示卖出币的数量（乘以10^8）

    public String usdtRate;// 当时下单usdt汇率

    public String dealBalance;// 已成交金额（乘以10^8）
    public String dealAmount;// 已成交数量（乘以10^8）
    public String dealFee;// 手续费（乘以10^8）
    public String dealAvgPrice;//成交均价
    public int state;// 订单状态
    public String stateDesc;
    public int buySell;// 买卖类型
    public int isLimit;// 0：非限价单，1：限价单
    public String createTime;// 创建时间
    public String doneTime;// 完成时间

    public String profit;// 最终的用户盈利==deal_balance-deal_fee-(deal_amount*cost_price)-profit_share
    public String profitShare;// 盈利分成={(deal_balance-deal_fee-(deal_amount*cost_price))>0}*分成比例
    public double profitScale;// 用户跟高手时赢利部分的分成比例








}
