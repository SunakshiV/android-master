package com.procoin.widgets.quotitian.entity.jsonparser;

import com.procoin.http.base.AbstractParser;
import com.procoin.widgets.quotitian.entity.LineTimeEntity;

import org.json.JSONException;
import org.json.JSONObject;

public class LineTimeEntityParser extends AbstractParser<LineTimeEntity> {
    @Override
    public LineTimeEntity parse(JSONObject json) throws JSONException {
        LineTimeEntity lineTimeEntity = new LineTimeEntity();
        if (hasNotNullAndIsDouble(json, "last")) lineTimeEntity.price = json.getDouble("last");
        if (hasNotNullAndIsDouble(json, "amount")) lineTimeEntity.amount = json.getDouble("amount");
        if (hasNotNullAndIsDouble(json, "balance")) lineTimeEntity.balance = json.getDouble("balance");
        if (hasNotNullAndIsIntOrLong(json, "timestamp")) lineTimeEntity.time = json.getLong("timestamp");

        return lineTimeEntity;
    }

    public LineTimeEntity parse(JSONObject json, String key) throws JSONException {
        if (json == null || key == null) return null;
        if (hasAndNotNull(json, key)) {
            return parse(json.getJSONObject(key));
        }
        return null;
    }
}
