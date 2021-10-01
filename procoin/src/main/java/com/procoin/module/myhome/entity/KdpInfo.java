package com.procoin.module.myhome.entity;

import com.procoin.http.base.TaojinluType;


public class KdpInfo implements TaojinluType{


    public long userId;
    public String userName;// 用户昵称
    public String headUrl;// 用户头像
    public int winCount; // 预测正确次数
    public int loseCount; // 预测失败次数
    public double winRate;// 预测胜率win_count/(win_count+lose_count)
    public String winRateValue;
    public long empiricValue;// 可得经验，分为：初出茅庐、草莽义士、声名鹊起、卧龙初升、威震王城、力挽狂澜、名将初成、名震八荒
    public String desigName = "初出茅庐";// 称号
    public String desigLogo;// 称号对应的logo
}
