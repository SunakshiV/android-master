package com.procoin.module.myhome.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * 
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import com.procoin.http.base.TaojinluType;

/**
 * 一级GridView中每个item的数据模型
 * 
 * @author likebamboo
 */
public class ImageSelectGroup implements TaojinluType {


	private String pathStr;
	private boolean isCheck;//这个才是相册页面是否已经选中

	private boolean isSelected;//这个是预览页面用到是否选中状态

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean selected) {
		isSelected = selected;
	}



	public ImageSelectGroup(String pathStr){
		this.pathStr=pathStr;
	}
	public ImageSelectGroup(){
	}

	public String getPathStr() {
		return pathStr;
	}

	public void setPathStr(String pathStr) {
		this.pathStr = pathStr;
	}

	public boolean isCheck() {
		return isCheck;
	}

	public void setCheck(boolean isCheck) {
		this.isCheck = isCheck;
	}

}
