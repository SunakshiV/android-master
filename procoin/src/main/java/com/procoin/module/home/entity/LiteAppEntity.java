package com.procoin.module.home.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 小应用
 */
public class LiteAppEntity implements TaojinluType {

    public long appId;
    public String pkg;
    public String cls;
    public String logo;
    public String name;
    public String params;
    public int sortNum;
    public int state;
    public int type;


}
