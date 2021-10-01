package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 16-1-19.
 */
public class CircleNews implements TaojinluType {


//    {\"applyNews\":0,\"chatRe\":0,\"circleNum\":10992,\"evalNews\":0,\"gameNews\":0,\"gameRe\":0,\"infoNews\":0,\"infoRe\":0,
// \"partyNews\":0,\"partyRe\":0,\"role\":10,\"sysNews\":0,\"sysPartyNews\":0,\"userId\":92}","success":true,"type":212}




    public int applyNews;//成员申请数量
    public String circleNum;//
    public int infoNews;//资讯数量
    public int partyNews;//聚会数量
    public int role;
    public int sysNews;//圈系统消息
    public int sysPartyNews;//圈聚会消息
    public long userId;
    public int evalNews;//评价消息
    public int gameNews;//比赛消息

    //下面3个值android不用，因为提醒android是记录在前端
//    public int chatRe;
//    public int infoRe;
//    public int partyRe;
//    public int gameRe;

}
