package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 16-1-5.
 */
public class CircleMsg implements TaojinluType {

//    {"circleNum":115288,"cls":"cls","describes":"你获取得一次评价,马上评价","createTime":20160112112629,
//     "expTime":20160118112625,"isDone":0,"jf":0,"msgId":6,"msgNo":"14518782s1s407s29w4ilmxclxjh9w32",
//     "msgType":2,"params":"params","pkg":"pkg","pview":"pview","sysFlag":0,"type":0,"userId":91}],"pageSize":20}

    public String circleNum;
    public String content;
    public String createTime;
    public String expTime;
    public long msgId;
    public String msgNo;
    public int msgType;//0是白色  其他红色
    public int sysFlag;
    public long userId;

    public int isDone;
    public int jf;
    public int type;//没用
    public String cls;
    public String params;
    public String pkg;

    public String tip;

    public int itemBg;

}
