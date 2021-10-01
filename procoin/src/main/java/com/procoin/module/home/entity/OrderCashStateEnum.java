package com.procoin.module.home.entity;

public enum OrderCashStateEnum {

	wait_pay(0, "待付款"), //
    mark_pay(1, "已标记付款"), //
	done(2, "已完成"), // 完成收款，生成正式订单
	expire(-1, "已过期"), //
	cancel(-2, "已撤销"), //
	unpay_cancel(-3, "系统撤销");//

	private int state;
	private String stateDesc;

	OrderCashStateEnum(int state, String stateDesc) {
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

	public void setStateDesc(String stateDesc) {
		this.stateDesc = stateDesc;
	}

	//根据state判断是否还可以撤销
	public static boolean isCancelEnable(int state) {
		return state==wait_pay.state||state==mark_pay.state;
	}

//	public static String getDesc(int state, int buySell) {
//		for (OrderCashStateEnum stateEnum : values()) {
//			if (stateEnum.getState() == state) {
//				if (buySell == BuySellEnum.sell.getBuySell()) {
//					if (state == wait_pay.getState()) {
//						return "待平台放款";
//					}
//				} else {
//					return stateEnum.getStateDesc();
//				}
//			}
//		}
//		return null;
//	}

}