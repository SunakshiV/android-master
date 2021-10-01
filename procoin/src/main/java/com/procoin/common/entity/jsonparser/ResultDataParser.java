package com.procoin.common.entity.jsonparser;


import com.procoin.common.entity.ResultData;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class ResultDataParser extends AbstractParser<ResultData> {

    @Override
    public ResultData parse(JSONObject json) throws JSONException {

        ResultData resultData = new ResultData();
        if (hasAndNotNull(json, "msg")) {
            resultData.msg = json.getString("msg");
        }
        if (hasNotNullAndIsIntOrLong(json, "code")) {
            resultData.code = json.getInt("code");
        }
        if (hasAndNotNull(json, "success")) {
            resultData.success = json.getBoolean("success");
        }
        if (hasAndNotNull(json, "data")) {
            resultData.data = json.getString("data");
        }
        return resultData;
    }

}
