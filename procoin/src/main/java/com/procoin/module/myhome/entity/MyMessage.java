package com.procoin.module.myhome.entity;

import com.procoin.http.base.TaojinluType;

/**
 * 我的消息
 */

public class MyMessage implements TaojinluType{


    public long msgId;
    public long userId;
    public long fromUid;

    public String title;
    public String content;
    public String createTime;
    public String userName;
    public String extra;
    public String headUrl;


    public int isVip;//1官方 0非官方


//    public String pkg;
//    public String cls;
//    public String params;





}
