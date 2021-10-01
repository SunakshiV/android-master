package com.procoin.social.entity.jasonparser;

import com.procoin.http.base.AbstractParser;
import com.procoin.social.entity.WxBuidler;

import org.json.JSONException;
import org.json.JSONObject;

public class WxBuidlerParser extends AbstractParser<WxBuidler> {

    @Override
    public WxBuidler parse(JSONObject json) throws JSONException {
        // TODO Auto-generated method stub
        WxBuidler content = new WxBuidler();
        if (hasAndNotNull(json, "content")) content.content = json.getString("content");// 文字
        //分享的数据
        if (hasAndNotNull(json, "shareContent")) {
            content.shareContent = json.getString("shareContent");
        }
        if (hasAndNotNull(json, "shareLogo")) {
            content.shareLogo = json.getString("shareLogo");
        }
        if (hasAndNotNull(json, "shareTitle")) {
            content.shareTitle = json.getString("shareTitle");
        }
        return content;
    }

    public WxBuidler parse(JSONObject json, String shareType) throws JSONException {
        WxBuidler content = parse(json);
        content.shareType = shareType;
        return content;
    }


}
