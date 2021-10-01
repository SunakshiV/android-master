package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

import java.util.List;

public class UserFollow implements TaojinluType {

    public long dvUid;// 绑定跟随大V的UID
    public long userId;// 用户UID
    public int isOpen;//是否开通跟单，0关闭，1开通
    public int multiple;// 跟单倍数
    public String updateTime;// 主要计算用户跟大V停留时间
    public String dvUserName;// 绑定跟随大V的名称
    public String dvHeadUrl;// 绑定跟随大的头像


    public UserFollow() {
    }


}
