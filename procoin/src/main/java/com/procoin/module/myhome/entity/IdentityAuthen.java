package com.procoin.module.myhome.entity;

import com.procoin.http.base.TaojinluType;


public class IdentityAuthen implements TaojinluType{


    public long id;
    public long userId;
    public String userName;
    public String transactionId; // 对接芝麻信用业务号
    public String name;
    public String certType;
    public String certNo;
//    public IdentAuthImage images;
    public int state;// 审核状态， 0：审核中 1：审核通过，2：审核未通过
    public String stateDesc; // 与status对应的状态描述
    public String checkMsg;
    public long createTime;

    public String backImgUrl;
    public String frontImgUrl;


}
