package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

/**
 *
 * 圈子空间页面包括  私聊的消息数   party的消息数量   资讯的消息数量
 * Created by zhengmj on 15-12-22.
 */
public class CircleRoomDetail implements TaojinluType {
    public int applyNews;
    public int chatNews;
    public int infoNews;
    public int partyNews;
    public int sysNews;
    public String circleNum;
    public int role;//用户角色,0普通用户,10是圈主,5是管理员
    public long userId;

    public double curjf;
    public double ljjf;
    public double zrjf;



}
