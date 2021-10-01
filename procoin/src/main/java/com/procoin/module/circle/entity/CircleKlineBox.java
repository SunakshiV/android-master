package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 16-4-27.
 */
public class CircleKlineBox implements TaojinluType{
    public String cls;
    public String content;
    public String params;
    public String pkg;
    public String title;
    public String logo;
    public String time;

    @Override
    public String toString() {
        return "CircleKlineBox{" +
                "cls='" + cls + '\'' +
                ", describes='" + content + '\'' +
                ", params='" + params + '\'' +
                ", pkg='" + pkg + '\'' +
                ", title='" + title + '\'' +
                ", logo='" + logo + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
