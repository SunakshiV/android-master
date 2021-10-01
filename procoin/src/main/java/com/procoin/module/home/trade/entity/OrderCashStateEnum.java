package com.procoin.module.home.trade.entity;

import com.procoin.R;

public enum OrderCashStateEnum {

	//如果是提现就只有 0 和 2 两种状态

	wait_pay(0, "请付款", R.drawable.ic_svg_order_pay), //
    mark_pay(1, "待放行", R.drawable.ic_svg_order_pay), //
	done(2, "已完成", R.drawable.ic_svg_order_complete), // 完成收款，生成正式订单
	expire(-1, "已过期", R.drawable.ic_svg_order_cancel), //
	cancel(-2, "已撤销", R.drawable.ic_svg_order_cancel), //
	unpay_cancel(-3, "系统撤销", R.drawable.ic_svg_order_cancel);//

	private int state;
	private String stateDesc;
	private int icon;

	OrderCashStateEnum(int state, String stateDesc,int icon) {
		this.state = state;
		this.stateDesc = stateDesc;
		this.icon = icon;
	}

	public int getState() {
		return state;
	}

	public int getIcon() {
		return icon;
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

	public static OrderCashStateEnum getOrderCashState(int state) {
		for (OrderCashStateEnum stateEnum : values()) {
			if (stateEnum.getState() == state) {
				return stateEnum;
			}
		}
		return null;
	}

}