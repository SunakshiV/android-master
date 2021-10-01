package com.procoin.module.circle.entity.parser;

import com.procoin.module.circle.entity.CircleNews;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CircleNewsParser extends AbstractParser<CircleNews> {
	@Override
	public CircleNews parse(JSONObject json) throws JSONException {

//		public int applyNews;
//		public int infoNews;
//		public int partyNews;
//		public int role;
//		public int sysNews;
//		public int sysPartyNews;
//		public String circleNum;
//		public long userId;
//		//下面3个值android不用，因为提醒android是记录在前端
//		public int chatRe;
//		public int infoRe;
//		public int partyRe;
		CircleNews circleNews = new CircleNews();
		if (hasNotNullAndIsIntOrLong(json, "applyNews")) circleNews.applyNews = json.getInt("applyNews");
		if (hasNotNullAndIsIntOrLong(json, "infoNews")) circleNews.infoNews = json.getInt("infoNews");
		if (hasNotNullAndIsIntOrLong(json, "partyNews")) circleNews.partyNews = json.getInt("partyNews");
		if (hasNotNullAndIsIntOrLong(json, "role")) circleNews.role = json.getInt("role");
		if (hasNotNullAndIsIntOrLong(json, "sysNews")) circleNews.sysNews = json.getInt("sysNews");
		if (hasNotNullAndIsIntOrLong(json, "sysPartyNews")) circleNews.sysPartyNews = json.getInt("sysPartyNews");
		if (hasNotNullAndIsIntOrLong(json, "userId")) circleNews.userId = json.getLong("userId");
		if (hasAndNotNull(json, "circleNum")) circleNews.circleNum = json.getString("circleNum");
		if (hasAndNotNull(json, "evalNews")) circleNews.evalNews = json.getInt("evalNews");
		if (hasAndNotNull(json, "gameNews")) circleNews.gameNews = json.getInt("gameNews");

		return circleNews;
	}

}
