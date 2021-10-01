package com.procoin.module.legal.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 *
 */
public class OtcCertification implements TaojinluType {





    public long userId;// 认证用户ID
    public String securityDeposit;// 保证金USDT
    public int state;// 认证状态，0未认证，1认证中，2认证通过，3申请取消认证，4取消认证通过，-1认证失败
    public String reason;// 原因
    public long createTime;

    public int orderNum;// 完成订单数量
    public double limitRate;// 完成速度比率，格式化0.00，前端要*100转化%显示

    public String realName;// 身份证姓名
    public String certNo;// 身份证号码
    public int idCertify;// 实名身份认证，0没认证，1认证



}
