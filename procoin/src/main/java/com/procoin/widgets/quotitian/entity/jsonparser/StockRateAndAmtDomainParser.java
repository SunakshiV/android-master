package com.procoin.widgets.quotitian.entity.jsonparser;

import com.procoin.widgets.quotitian.entity.StockRateAndAmtDomain;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class StockRateAndAmtDomainParser extends AbstractParser<StockRateAndAmtDomain> {
    @Override
    public StockRateAndAmtDomain parse(JSONObject json) throws JSONException {
        StockRateAndAmtDomain stockdomain = new StockRateAndAmtDomain();
        if (hasAndNotNull(json, "fdm")) stockdomain.fdm = json.getString("fdm");
        if (hasAndNotNull(json, "dm")) stockdomain.dm = json.getString("dm");
        if (hasAndNotNull(json, "jc")) stockdomain.jc = json.getString("jc");
        if (hasNotNullAndIsDouble(json, "zrsp")) stockdomain.zrsp = json.getDouble("zrsp");
        if (hasNotNullAndIsDouble(json, "jrkp")) stockdomain.jrkp = json.getDouble("jrkp");
        if (hasNotNullAndIsDouble(json, "zjcj")) stockdomain.zjcj = json.getDouble("zjcj");
        if (hasNotNullAndIsDouble(json, "cjsl")) stockdomain.cjsl = json.getDouble("cjsl");
        if (hasNotNullAndIsDouble(json, "cjje")) stockdomain.cjje = json.getDouble("cjje");
        if (hasNotNullAndIsDouble(json, "zgcj")) stockdomain.zgcj = json.getDouble("zgcj");
        if (hasNotNullAndIsDouble(json, "zdcj")) stockdomain.zdcj = json.getDouble("zdcj");
        if (hasNotNullAndIsDouble(json, "amt")) stockdomain.amt = json.getDouble("amt");
        if (hasNotNullAndIsDouble(json, "rate")) stockdomain.rate = json.getDouble("rate");
        if (hasAndNotNull(json, "time")) stockdomain.time = json.getString("time");
        if (hasAndNotNull(json, "date")) stockdomain.date = json.getString("date");
        return stockdomain;
    }

    public StockRateAndAmtDomain parse(JSONObject json, String key) throws JSONException {
        if (json == null || key == null) return null;
        if (hasAndNotNull(json, key)) {
            return parse(json.getJSONObject(key));
        }
        return null;
    }
}
