package com.procoin.module.circle.entity.parser;

import com.procoin.module.circle.entity.CircleKlineBox;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class CircleKlineBoxParser extends AbstractParser<CircleKlineBox> {
    @Override
    public CircleKlineBox parse(JSONObject json) throws JSONException {
        CircleKlineBox circleKlineBox = new CircleKlineBox();
        if (hasAndNotNull(json, "cls")) circleKlineBox.cls = json.getString("cls");
        if (hasAndNotNull(json, "describes")) circleKlineBox.content = json.getString("describes");
        if (hasAndNotNull(json, "params")) circleKlineBox.params = json.getString("params");
        if (hasAndNotNull(json, "pkg")) circleKlineBox.pkg = json.getString("pkg");
        if (hasAndNotNull(json, "title")) circleKlineBox.title = json.getString("title");

        if (hasAndNotNull(json, "logo")) circleKlineBox.logo = json.getString("logo");
        if (hasAndNotNull(json, "time")) circleKlineBox.time = json.getString("time");

        return circleKlineBox;
    }

}
