package com.procoin.module.home.trade.entity;

import com.procoin.http.base.AbstractParser;
import com.procoin.http.base.Group;
import com.procoin.widgets.quotitian.entity.LineTimeEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MarkDealParser extends AbstractParser<MarkDeal> {
    public String amount;
    public String buySell;
    public String price;
    public String time;

    @Override
    public MarkDeal parse(JSONObject json) throws JSONException {
        MarkDeal markDeal = new MarkDeal();
        if (hasAndNotNull(json, "amount")) markDeal.amount = json.getString("amount");
        if (hasAndNotNull(json, "buySell")) markDeal.buySell = json.getString("buySell");
        if (hasAndNotNull(json, "price")) markDeal.price = json.getString("price");
        if (hasAndNotNull(json, "time")) markDeal.time = json.getString("time");

        return markDeal;
    }

    public Group<MarkDeal> parseGroup(JSONArray ja) throws JSONException {
        if (ja == null) return null;
        Group<MarkDeal> group = new Group<>();
        for (int i = 0, m = ja.length(); i < m; i++) {
            group.add(parse(ja.getJSONObject(i)));
        }
        return group;
    }
}
