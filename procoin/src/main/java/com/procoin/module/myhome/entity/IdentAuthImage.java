package com.procoin.module.myhome.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 一级GridView中每个item的数据模型
 *
 * @author likebamboo
 */
public class IdentAuthImage implements TaojinluType {

    public String frontImgUrl; // 身份证正面照
    public String backImgUrl; // 身份证背面照
    public String holdImgUrl; // 手持证件照

}
