package com.procoin.module.home.entity;

public enum OrderStateEnum {

    submitted(0, "正在受理"), // 进入火币下单队列
    binding(10, "正在下单"), // 向火币下单（内存中间态，此状态对数据库不可见）
    binded(11, "下单成功"), // 火币下单成功，绑定火币订单ID
    error(4, "下单失败"), // 火币下单失败（终态）
    partial_filled(20, "部分成交"), // 部分成交（中间态）
    partial_canceled(24, "部分撤销"), // 部分撤销（终态）
    filled(30, "全部成交"), // 全部成交（终态）
    canceled(44, "已撤销"); // 已撤销（终态）

    private int state;
    private final String stateDesc;


    OrderStateEnum(int state, String stateDesc) {
        this.state = state;
        this.stateDesc = stateDesc;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateDesc() {
        return stateDesc;
    }

    public static String getStateDesc(int state) {
        for (OrderStateEnum stateEnum : values()) {
            if (stateEnum.getState() == state) {
                return stateEnum.getStateDesc();
            }
        }
        return null;
    }

    //根据state判断是否还可以撤销
    public static boolean isCancelEnable(int state) {
        return state==submitted.state||state==binding.state||state==binded.state||state==partial_filled.state;
    }

}