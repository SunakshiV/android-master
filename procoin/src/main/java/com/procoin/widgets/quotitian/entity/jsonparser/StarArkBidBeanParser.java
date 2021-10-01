package com.procoin.widgets.quotitian.entity.jsonparser;


import com.procoin.widgets.quotitian.entity.StarArkBidBean;
import com.procoin.http.base.AbstractParser;
import com.procoin.http.base.Group;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StarArkBidBeanParser extends AbstractParser<StarArkBidBean> {

    @Override
    public StarArkBidBean parse(JSONObject json) throws JSONException {

        StarArkBidBean a = new StarArkBidBean();
        if (hasAndNotNull(json, "price")) {
            a.price = json.getString("price");
        }
        if (hasAndNotNull(json, "amount")) {
            a.amount = json.getString("amount");
        }
        if (hasNotNullAndIsIntOrLong(json, "depthRate")) {
            a.depthRate = json.getInt("depthRate");
        }
        return a;
    }

    public Group<StarArkBidBean> parse(JSONArray array, int buySell) throws JSONException {
        return parse(array, buySell, 5);//默认5挡
    }


    public Group<StarArkBidBean> getEmptyList() {
        return getEmptyList(5);//默认5挡
    }

    public Group<StarArkBidBean> getEmptyList(int size) {
        Group<StarArkBidBean> group = new Group<>();
        StarArkBidBean starArkBidBean = null;
        for (int i = 0; i < size; i++) {
            starArkBidBean = new StarArkBidBean();
            starArkBidBean.isEmpty = true;
            group.add(starArkBidBean);
        }
        return group;

    }


    public Group<StarArkBidBean> parse(JSONArray array, int buySell, int size) throws JSONException {
        if (array == null) return null;
        Group<StarArkBidBean> group = new Group<>();
        for (int i = 0; i < array.length(); i++) {
            group.add(parse(array.getJSONObject(i)));
        }
        if (group.size() < size) {//添加空数据
            StarArkBidBean starArkBidBean = null;
            for (int i = 0, m = size - group.size(); i < m; i++) {
                starArkBidBean = new StarArkBidBean();
                starArkBidBean.isEmpty = true;
                if (buySell == 1) {//买入往后面塞
                    group.add(starArkBidBean);
                } else {//卖出往前面塞
                    group.add(0, starArkBidBean);
                }

            }
        }
        return group;
    }


    //需要倒序就调用这个
    public Group<StarArkBidBean> parseReverse(JSONArray array) throws JSONException {
        if (array == null) return null;
        Group<StarArkBidBean> group = new Group<>();
        for (int i = array.length() - 1; i >= 0; i--) {
            group.add(parse(array.getJSONObject(i)));
        }
        return group;
    }

}
