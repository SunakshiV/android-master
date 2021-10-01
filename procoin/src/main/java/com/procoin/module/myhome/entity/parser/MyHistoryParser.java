//package com.tjr.imredz.module.myhome.entity.parser;
//
//import android.text.TextUtils;
//
//import AbstractParser;
//import com.tjr.imredz.util.JsonParserUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//
///**
// * Created by zhengmj on 18-6-20.
// */
//
//public class MyHistoryParser extends AbstractParser<HistoryEntity> {
//    @Override
//    public HistoryEntity parse(JSONObject json) throws JSONException {
//        HistoryEntity entity = new HistoryEntity();
//        if (hasAndNotNull(json,"is_like")) entity.is_like = json.getBoolean("is_like");
//        if (hasAndNotNull(json,"is_dislike")) entity.is_dislike = json.getBoolean("is_dislike");
//        if (hasAndNotNull(json,"simple_name")) entity.simple_name = json.getString("simple_name");
//        if (hasAndNotNull(json,"history_id")) entity.history_id = json.getLong("history_id");
//        if (hasAndNotNull(json,"history_update_time")) entity.history_update_time = json.getString("history_update_time");
//        if (hasAndNotNull(json,"history_create_time")) entity.history_create_time = json.getString("history_create_time");
//        if (hasAndNotNull(json,"history_article_id")) entity.history_article_id = json.getLong("history_article_id");
//        if (hasAndNotNull(json,"history_user_id")) entity.history_user_id = json.getLong("history_user_id");
//        if (hasAndNotNull(json,"article_id")) entity.article_id = json.getLong("article_id");
//        if (hasAndNotNull(json,"title")) entity.title = json.getString("title");
//        if (hasAndNotNull(json,"content")) entity.content = json.getString("content");
//        if (hasAndNotNull(json,"article_type")) entity.article_type = json.getInt("article_type");
//        if (hasAndNotNull(json,"user_id")) entity.user_id = json.getLong("user_id");
//        if (hasAndNotNull(json,"update_time")) entity.update_time = json.getString("update_time");
//        if (hasAndNotNull(json,"create_time")) entity.create_time= json.getString("create_time");
//        if (hasAndNotNull(json,"end_time")) entity.end_time = json.getString("end_time");
//        if (hasAndNotNull(json,"coin_id")) entity.coin_id = json.getLong("coin_id");
//        if (hasAndNotNull(json,"question_coin")) entity.question_coin= json.getDouble("question_coin");
//        if (hasAndNotNull(json,"weight")) entity.weight = json.getInt("weight");
//        if (hasAndNotNull(json,"anwser_users")) entity.anwser_users = json.getString("anwser_users");
//        if (hasAndNotNull(json, "pic_files")) {
//            entity.pic_files = json.getString("pic_files");
//            if (!TextUtils.isEmpty(entity.pic_files)) {
//                JSONObject jsonObject = new JSONObject(entity.pic_files);
//                if (JsonParserUtils.hasAndNotNull(jsonObject, "imgList")) {
//                    JSONArray jaMin = jsonObject.getJSONArray("imgList");
//                    ArrayList<String> list = new ArrayList<>();
//                    for (int i = 0, m = jaMin.length(); i < m; i++) {
//                        list.add(jaMin.getString(i));
//                    }
//                    entity.imgList = list;
//                }
//            }
//        }
//        if (hasAndNotNull(json,"comment_num")) entity.comment_num = json.getLong("comment_num");
//        if (hasAndNotNull(json,"like_num")) entity.like_num = json.getLong("like_num");
//        if (hasAndNotNull(json,"dislike_num")) entity.dislike_num = json.getLong("dislike_num");
//        if (hasAndNotNull(json,"is_recommend")) entity.is_recommend = json.getBoolean("is_recommend");
//        if (hasAndNotNull(json,"is_delete")) entity.is_delete  = json.getBoolean("is_delete");
//        if (hasAndNotNull(json,"reading")) entity.reading = json.getLong("reading");
//        if (hasAndNotNull(json,"user_name")) entity.user_name = json.getString("user_name");
//        if (hasAndNotNull(json,"head_url")) entity.head_url = json.getString("head_url");
//        if (hasAndNotNull(json,"lead")) entity.lead = json.getString("lead");
//        if (hasAndNotNull(json, "reviewList")) {
//            entity.homeHotAnswers =new HomeHotAnswerParser().parseGroup(json.getJSONArray("reviewList"));
//        }
//        return entity;
//    }
//}
