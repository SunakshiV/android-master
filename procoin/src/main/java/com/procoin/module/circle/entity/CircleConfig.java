package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 16-4-26.
 */
public class CircleConfig implements TaojinluType {
    public String circleId;
    public int banned;
    public int articleAmount;
    public int showAmount;
    public int newApplyAmount;//成员申请数量
    public int msgAlert;//消息免打扰 1为免打扰
    public long userId;



    @Override
    public String toString() {
        return "circleId=="+circleId
                +"  banned=="+banned
                +"  articleAmount=="+articleAmount
                +"  showAmount=="+showAmount
                +"  newApplyAmount=="+newApplyAmount
                +"  msgAlert=="+msgAlert
                ;
    }
}
