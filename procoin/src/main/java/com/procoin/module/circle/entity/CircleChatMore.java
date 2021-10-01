package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 16-10-26.
 */
public class CircleChatMore implements TaojinluType {

    public String title;
    public int imageRes;
    public int pos;//点击用到

    public CircleChatMore(String title, int imageRes,int pos) {
        this.title = title;
        this.imageRes = imageRes;
        this.pos=pos;
    }
}
