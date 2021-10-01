package com.procoin.subpush.notify;

import com.procoin.http.base.AbstractParser;
import org.json.JSONException;
import org.json.JSONObject;


public class NotifyModelParser extends AbstractParser<NotifyModel> {

    @Override
    public NotifyModel parse(JSONObject json) throws JSONException {
        NotifyModel notifyModel = new NotifyModel();
        if (hasNotNullAndIsIntOrLong(json, "pid")) notifyModel.setPid(json.getInt("pid"));
        if (hasAndNotNull(json, "head")) notifyModel.setHead(json.getString("head"));
        if (hasAndNotNull(json, "body")) notifyModel.setBody(json.getString("body"));
        if (hasAndNotNull(json, "t")) notifyModel.setT(json.getString("t"));
        if (hasAndNotNull(json, "pkg")) notifyModel.setPkg(json.getString("pkg"));
        if (hasAndNotNull(json, "cls")) notifyModel.setCls(json.getString("cls"));
        if (hasAndNotNull(json, "p")) notifyModel.setP(json.getString("p"));
        if (hasNotNullAndIsIntOrLong(json, "time")) notifyModel.setTime(json.getLong("time"));
        return notifyModel;
    }
}
