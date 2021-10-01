package com.procoin.widgets.quotitian.entity.jsonparser;


import com.procoin.http.base.Group;
import com.procoin.module.home.trade.entity.MarkDeal;
import com.procoin.module.home.trade.entity.MarkDealParser;
import com.procoin.widgets.quotitian.entity.StarProData;
import com.procoin.http.base.AbstractParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StarProDataParser extends AbstractParser<StarProData> {

    @Override
    public StarProData parse(JSONObject json) throws JSONException {

        StarProData resultData = new StarProData();
        if (hasAndNotNull(json, "symbol")) {
            resultData.symbol = json.getString("symbol");
        }

        if (hasAndNotNull(json, "marketType")) {
            resultData.marketType = json.getString("marketType");
        }

        if (hasAndNotNull(json, "prefixType")) {
            resultData.prefixType = json.getString("prefixType");
        }

        if (hasAndNotNull(json, "amt")) {
            resultData.amt = json.getString("amt");
        }
        if (hasNotNullAndIsDouble(json, "rate")) {
            resultData.rate = json.getDouble("rate");
        }
        if (hasAndNotNull(json, "amount")) {
            resultData.amount = json.getString("amount");
        }

        if (hasNotNullAndIsDouble(json, "balance")) {
            resultData.balance = json.getDouble("balance");
        }
        if (hasAndNotNull(json, "high")) {
            resultData.high = json.getString("high");
        }

        if (hasNotNullAndIsDouble(json, "last")) {
            resultData.last = json.getDouble("last");
        }
        if (hasAndNotNull(json, "lastCny")) {
            resultData.lastCny = json.getString("lastCny");
        }
        if (hasAndNotNull(json, "low")) {
            resultData.low = json.getString("low");
        }
        if (hasNotNullAndIsDouble(json, "open")) {
            resultData.open = json.getDouble("open");
        }
        if (hasNotNullAndIsDouble(json, "preClose")) {
            resultData.preClose = json.getDouble("preClose");
        }

        if (hasNotNullAndIsDouble(json, "yesClose")) {
            resultData.yesClose = json.getDouble("yesClose");
        }

        if (hasAndNotNull(json, "sells")) {
            resultData.sells = json.getString("sells");
        }
        if (hasAndNotNull(json, "buys")) {
            resultData.buys = json.getString("buys");
        }
        if (hasAndNotNull(json, "date")) {
            resultData.date = json.getString("date");
        }
        if (hasAndNotNull(json, "time")) {
            resultData.time = json.getString("time");
        }
        if (hasNotNullAndIsDouble(json, "ratio")) {
            resultData.ratio = json.getDouble("ratio");
        }
        if (hasNotNullAndIsIntOrLong(json, "priceDecimals")) {
            resultData.priceDecimals = json.getInt("priceDecimals");
        }
        if (hasNotNullAndIsIntOrLong(json, "amountDecimals")) {
            resultData.amountDecimals = json.getInt("amountDecimals");
        }
        if (hasAndNotNull(json, "tip")) {
            resultData.tip = json.getString("tip");
        }
        if (hasAndNotNull(json, "openMarketStr")) {
            resultData.openMarketStr = json.getString("openMarketStr");
        }
        if (hasAndNotNull(json, "currency")) {
            resultData.currency = json.getString("currency");
        }

        if (hasNotNullAndIsIntOrLong(json, "isTrade")) {
            resultData.isTrade = json.getInt("isTrade");
        }
        if (hasAndNotNull(json, "dealList")) {
            JSONArray ja = json.getJSONArray("dealList");
            MarkDealParser markDealParser = new MarkDealParser();
            resultData.dealList = markDealParser.parseGroup(ja);
        }

        return resultData;
    }

}
