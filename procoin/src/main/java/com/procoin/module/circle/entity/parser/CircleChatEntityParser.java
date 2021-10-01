package com.procoin.module.circle.entity.parser;

import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.util.JsonParserUtils;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CircleChatEntityParser extends AbstractParser<CircleChatEntity> {
    @Override
    public CircleChatEntity parse(JSONObject json) throws JSONException {
        CircleChatEntity circleChatEntity = new CircleChatEntity();
        if (hasAndNotNull(json, "circleNum"))
            circleChatEntity.chatTopic = json.getString("chatTopic");
        if (hasAndNotNull(json, "createTime"))
            circleChatEntity.createTime = json.getString("createTime");
        if (hasAndNotNull(json, "say")) circleChatEntity.say = json.getString("say");
        if (hasAndNotNull(json, "name")) circleChatEntity.name = json.getString("name");
        if (hasAndNotNull(json, "headUrl")) circleChatEntity.headurl = json.getString("headUrl");
        if (hasNotNullAndIsIntOrLong(json, "chatId"))
            circleChatEntity.chatId = json.getLong("chatId");
        if (hasNotNullAndIsIntOrLong(json, "userId"))
            circleChatEntity.userId = json.getLong("userId");
        if (hasNotNullAndIsIntOrLong(json, "toUid"))
            circleChatEntity.toUid = json.getLong("toUid");
        if (hasNotNullAndIsIntOrLong(json, "mark")) circleChatEntity.chatMark = json.getInt("mark");
        if (hasAndNotNull(json, "verify")) circleChatEntity.verify = json.getString("verify");
        if (hasNotNullAndIsIntOrLong(json, "isVip")) circleChatEntity.isVip = json.getInt("isVip");

        if (JsonParserUtils.hasAndNotNull(json, "push")) {
            circleChatEntity.isPush = json.getBoolean("push");
        }
        if (JsonParserUtils.hasAndNotNull(json, "roleName")) {
            circleChatEntity.roleName = json.getString("roleName");
        }
        return circleChatEntity;
    }


    public CircleChatEntity parsePrivateChat(JSONObject json) throws JSONException {
        CircleChatEntity circleChatEntity = parse(json);
        if (hasAndNotNull(json, "chatTopic"))
            circleChatEntity.chatTopic = json.getString("chatTopic");
        return circleChatEntity;
    }


}
