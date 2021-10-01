package com.procoin.module.pulltorefresh;

public interface IAutoLoadMoreLayout {
	void setFootBackGroundColor(int resId);
	void setFootTexColor(int color);
	/**
	 * 
	 * @param enable    因为有些listView无法显示footDivider，所以把footDivider添加到了布局文件，由此方法控制是否显示
	 */
	void setListFootDivederEnable(boolean enable);
	void setListFootDivederDrawableRes(int res);
}
