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

public class Notice implements TaojinluType {

    public long articleId;
    public String createTime;
    public int isTop;
    public String title;
    public int type;
    public String url;


    public Notice() {
    }



}
