package com.procoin.module.circle.entity.parser;

import com.procoin.module.circle.entity.CircleMsg;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CircleMsgParser extends AbstractParser<CircleMsg> {


	@Override
	public CircleMsg parse(JSONObject json) throws JSONException {
		CircleMsg circleMsg = new CircleMsg();
		if (hasAndNotNull(json, "circleNum")) circleMsg.circleNum = json.getString("circleNum");
		if (hasAndNotNull(json, "describes")) circleMsg.content = json.getString("describes");
		if (hasAndNotNull(json, "createTime")) circleMsg.createTime = json.getString("createTime");
		if (hasAndNotNull(json, "expTime")) circleMsg.expTime = json.getString("expTime");
		if (hasAndNotNull(json, "msgNo")) circleMsg.msgNo = json.getString("msgNo");

		if (hasNotNullAndIsIntOrLong(json, "msgId")) circleMsg.msgId = json.getLong("msgId");
		if (hasNotNullAndIsIntOrLong(json, "userId")) circleMsg.userId = json.getLong("userId");
		if (hasNotNullAndIsIntOrLong(json, "msgType")) circleMsg.msgType = json.getInt("msgType");
		if (hasNotNullAndIsIntOrLong(json, "sysFlag")) circleMsg.sysFlag = json.getInt("sysFlag");


		if (hasNotNullAndIsIntOrLong(json, "isDone")) circleMsg.isDone = json.getInt("isDone");
		if (hasNotNullAndIsIntOrLong(json, "jf")) circleMsg.jf = json.getInt("jf");
		if (hasNotNullAndIsIntOrLong(json, "type")) circleMsg.type = json.getInt("type");

		if (hasAndNotNull(json, "cls")) circleMsg.cls = json.getString("cls");
		if (hasAndNotNull(json, "params")) circleMsg.params = json.getString("params");
		if (hasAndNotNull(json, "pkg")) circleMsg.pkg = json.getString("pkg");
		if (hasAndNotNull(json, "tip")) circleMsg.tip = json.getString("tip");

		if (hasNotNullAndIsIntOrLong(json, "itemBg")) circleMsg.itemBg = json.getInt("itemBg");

		return circleMsg;
	}

}
