//package com.tjr.imredz.module.myhome.entity.parser;
//
//import AbstractParser;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * Created by zhengmj on 18-3-24.
// */
//
//public class MyFavoriteEntityParser extends AbstractParser<MyFavoriteEntity> {
//    @Override
//    public MyFavoriteEntity parse(JSONObject json) throws JSONException {
//        MyFavoriteEntity entity = new MyFavoriteEntity();
//        if (hasAndNotNull(json,"favor_id")) entity.favor_id = json.getString("favor_id");
//        if (hasAndNotNull(json,"article_id")) entity.article_id = json.getString("article_id");
//        if (hasAndNotNull(json,"user_id")) entity.user_id = json.getString("user_id");
//        if (hasAndNotNull(json,"create_time")) entity.create_time = json.getString("create_time");
//        if (hasAndNotNull(json,"article")){
//            HomeSubArticleParser parser = new HomeSubArticleParser();
//            entity.article = parser.parse(json.getJSONObject("article"));
//        }
//        return entity;
//    }
//
//}
