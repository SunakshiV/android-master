package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;

import java.util.List;

public class AccountInfo implements TaojinluType {

    public String accountType;// 账户类型
    public String holdAmount;// 持有数量(余额)
    public String frozenAmount;// 冻结数量
    public Group<Position> openList;// 当前开仓记录
    public Group<Position> entrustList;// 当前委托记录
    public String assets;// 账户权益(总资产)＝余额+未实现盈亏
    public String assetsCny;// 账户权益(总资产)＝余额+未实现盈亏
    public String profit;// 未实现盈亏＝（最新价－开仓价）*合约数量*每张约定数量
    public String riskRate;// 风险率＝（余额30%/账户权益）*100%＝100.00%强平爆仓
    public String openBail;// 持仓保证金＝所有开仓的订单保证金累加
    public String frozenBail;// 冻结保证金＝所有委托的订单保证金累加
    public String eableBail;// 可用保证金＝账户权益－持仓保证金－冻结保证金（当可用保证金为0系统撤销用户委托订单）



    public AccountInfo() {
    }



}
