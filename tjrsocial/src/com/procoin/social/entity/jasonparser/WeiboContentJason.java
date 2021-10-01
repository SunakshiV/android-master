package com.procoin.social.entity.jasonparser;

import org.json.JSONException;
import org.json.JSONObject;

import com.procoin.social.entity.WeiboContent;
import com.procoin.http.base.AbstractParser;

public class WeiboContentJason extends AbstractParser<WeiboContent> {

	@Override
	public WeiboContent parse(JSONObject json) throws JSONException {
		// TODO Auto-generated method stub
		WeiboContent content = new WeiboContent();
		if (hasAndNotNull(json, "content")) content.content = json.getString("content");// 文字
		if (hasAndNotNull(json, "hint")) content.hint = json.getString("hint");// 这个不清楚
		if (hasAndNotNull(json, "image")) content.image = json.getString("image");// 图片路径

		return content;
	}

}
