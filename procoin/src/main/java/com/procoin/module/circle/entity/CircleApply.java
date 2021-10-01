package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

public class CircleApply implements TaojinluType {

    public long applyId;
    public String circleId;
    public long userId;
    public long handleUid;
    public String reason;
    public String createTime;
    public int status;// 处理状态, 0未处理，1已通过，-1已拒绝
    public String userName;
    public String handleUserName;
    public String headUrl;


}
