package com.procoin.module.legal.entity;

import android.text.TextUtils;

/**
 * Created by zhengmj on 16-3-23.
 */
public enum OtcOrderStateEnum {
    wait(0), //买家：取消、去付款、我已付款成功，卖家：我确认已收到付款（此态暂不可用）
    mark(1), //买家：从“我已付款成功”点击后->“申诉”，卖家：申诉、我确认已收到付款
    done(2), //买家：完成，卖家：从“我确认已收到付款”点击后->完成
    appeal(3), //申诉，mark->appeal
    expire(-1), //已过期，订单过期不处理
    cancel(-2), //已撤销，
    admin_cancel(-3);// 系统撤销

    public final int state;

    OtcOrderStateEnum(int state) {
        this.state = state;
    }

}

