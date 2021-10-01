package com.procoin.module.circle.entity.parser;

import com.procoin.module.circle.entity.CircleRoomDetail;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CircleRoomDetailParser extends AbstractParser<CircleRoomDetail> {
	@Override
	public CircleRoomDetail parse(JSONObject json) throws JSONException {
		CircleRoomDetail circleChatEntity = new CircleRoomDetail();

		if (hasNotNullAndIsIntOrLong(json, "applyNews")) circleChatEntity.applyNews = json.getInt("applyNews");
		if (hasNotNullAndIsIntOrLong(json, "chatNews")) circleChatEntity.chatNews = json.getInt("chatNews");
		if (hasNotNullAndIsIntOrLong(json, "infoNews")) circleChatEntity.infoNews = json.getInt("infoNews");
		if (hasNotNullAndIsIntOrLong(json, "partyNews")) circleChatEntity.partyNews = json.getInt("partyNews");
		if (hasNotNullAndIsIntOrLong(json, "role")) circleChatEntity.role = json.getInt("role");
		if (hasNotNullAndIsIntOrLong(json, "sysNews")) circleChatEntity.sysNews = json.getInt("sysNews");

		if (hasNotNullAndIsIntOrLong(json, "userId")) circleChatEntity.userId = json.getLong("userId");
		if (hasAndNotNull(json, "circleNum")) circleChatEntity.circleNum = json.getString("circleNum");

		if (hasNotNullAndIsDouble(json, "curjf")) circleChatEntity.curjf = json.getDouble("curjf");
		if (hasNotNullAndIsDouble(json, "ljjf")) circleChatEntity.ljjf = json.getDouble("ljjf");
		if (hasNotNullAndIsDouble(json, "zrjf")) circleChatEntity.zrjf = json.getDouble("zrjf");

		return circleChatEntity;
	}

}
