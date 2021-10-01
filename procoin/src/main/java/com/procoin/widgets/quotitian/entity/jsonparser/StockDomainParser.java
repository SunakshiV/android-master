package com.procoin.widgets.quotitian.entity.jsonparser;

import com.procoin.http.base.AbstractParser;
import com.procoin.widgets.quotitian.entity.StockDomain;

import org.json.JSONException;
import org.json.JSONObject;

public class StockDomainParser extends AbstractParser<StockDomain> {
	@Override
	public StockDomain parse(JSONObject json) throws JSONException {
		StockDomain stockdomain = new StockDomain();

		if (hasAndNotNull(json, "fdm")) stockdomain.fdm=json.getString("fdm");
		if (hasAndNotNull(json, "dm")) stockdomain.dm=json.getString("dm");
		if (hasAndNotNull(json, "jc")) stockdomain.jc=json.getString("jc");
		if (hasNotNullAndIsDouble(json, "zrsp")) stockdomain.zrsp=json.getDouble("zrsp");
		if (hasNotNullAndIsDouble(json, "jrkp")) stockdomain.jrkp=json.getDouble("jrkp");
		if (hasNotNullAndIsDouble(json, "zjcj")) stockdomain.zjcj=json.getDouble("zjcj");
		if (hasNotNullAndIsDouble(json, "cjsl")) stockdomain.cjsl=json.getDouble("cjsl");
		if (hasNotNullAndIsDouble(json, "cjje")) stockdomain.cjje=json.getDouble("cjje");
		if (hasNotNullAndIsDouble(json, "cjbs")) stockdomain.cjbs=json.getDouble("cjbs");
		if (hasNotNullAndIsDouble(json, "zgcj")) stockdomain.zgcj=json.getDouble("zgcj");
		if (hasNotNullAndIsDouble(json, "zdcj")) stockdomain.zdcj=json.getDouble("zdcj");
		if (hasAndNotNull(json, "syl1")) stockdomain.syl1=json.getString("syl1");
		if (hasAndNotNull(json, "syl2")) stockdomain.syl2=json.getString("syl2");
		if (hasNotNullAndIsDouble(json, "sjw5")) stockdomain.sjw5=json.getDouble("sjw5");
		if (hasNotNullAndIsDouble(json, "ssl5")) stockdomain.ssl5=json.getDouble("ssl5");
		if (hasNotNullAndIsDouble(json, "sjw4")) stockdomain.sjw4=json.getDouble("sjw4");
		if (hasNotNullAndIsDouble(json, "ssl4")) stockdomain.ssl4=json.getDouble("ssl4");
		if (hasNotNullAndIsDouble(json, "sjw3")) stockdomain.sjw3=json.getDouble("sjw3");
		if (hasNotNullAndIsDouble(json, "ssl3")) stockdomain.ssl3=json.getDouble("ssl3");
		if (hasNotNullAndIsDouble(json, "sjw2")) stockdomain.sjw2=json.getDouble("sjw2");
		if (hasNotNullAndIsDouble(json, "ssl2")) stockdomain.ssl2=json.getDouble("ssl2");
		if (hasNotNullAndIsDouble(json, "sjw1")) stockdomain.sjw1=json.getDouble("sjw1");
		if (hasNotNullAndIsDouble(json, "ssl1")) stockdomain.ssl1=json.getDouble("ssl1");
		if (hasNotNullAndIsDouble(json, "bjw1")) stockdomain.bjw1=json.getDouble("bjw1");
		if (hasNotNullAndIsDouble(json, "bsl1")) stockdomain.bsl1=json.getDouble("bsl1");
		if (hasNotNullAndIsDouble(json, "bjw2")) stockdomain.bjw2=json.getDouble("bjw2");
		if (hasNotNullAndIsDouble(json, "bsl2")) stockdomain.bsl2=json.getDouble("bsl2");
		if (hasNotNullAndIsDouble(json, "bjw3")) stockdomain.bjw3=json.getDouble("bjw3");
		if (hasNotNullAndIsDouble(json, "bsl3")) stockdomain.bsl3=json.getDouble("bsl3");
		if (hasNotNullAndIsDouble(json, "bjw4")) stockdomain.bjw4=json.getDouble("bjw4");
		if (hasNotNullAndIsDouble(json, "bsl4")) stockdomain.bsl4=json.getDouble("bsl4");
		if (hasNotNullAndIsDouble(json, "bjw5")) stockdomain.bjw5=json.getDouble("bjw5");
		if (hasNotNullAndIsDouble(json, "bsl5")) stockdomain.bsl5=json.getDouble("bsl5");
		if (hasAndNotNull(json, "date")) stockdomain.date=json.getString("date");
		if (hasAndNotNull(json, "time")) stockdomain.time=json.getString("time");
		return stockdomain;
	}
}
