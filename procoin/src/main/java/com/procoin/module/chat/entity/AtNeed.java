package com.procoin.module.chat.entity;

import com.alibaba.fastjson.JSON;
import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 16-6-8.
 */
public class AtNeed implements TaojinluType {
    public int type;//0不用跳转,1跳到用户信息
//    public String color;
    public String params; // 要求带的参数值，如2，也可以json格式

    public AtNeed() {
        super();
    }


    public AtNeed(int type, String params) {
        super();
        this.type = type;
//        this.color = color;
        this.params = params;


    }

    public String toAtNeed(String say) {
        return "@(" + say + ")「" + JSON.toJSONString(this) + "」";
    }

//    public static void main(String[] args) {
//        AtNeed atNeed = new AtNeed(1, 7, "#568981",null);
//        System.out.println(atNeed.toAtNeed("吕林青"));
//    }
}
