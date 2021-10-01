package com.procoin.module.circle.entity.parser;

import com.procoin.module.circle.entity.InfoFlowListItemEntity;
import com.procoin.http.base.AbstractParser;

import org.json.JSONException;
import org.json.JSONObject;

public class InfoFlowListItemEntityParser extends AbstractParser<InfoFlowListItemEntity> {

    @Override
    public InfoFlowListItemEntity parse(JSONObject json) throws JSONException {
        InfoFlowListItemEntity ilie = new InfoFlowListItemEntity();

//        public long circleNum; // 圈号,唯一
//        public long userId; // 即user_id
//        public String user_name; // 用户名
//        public String headurl; // 头像路径
//        public int isVip; // 0代表不是v，1代表是v
//        public String title;// 标题
//        public String describes;// 内容
//        public String picUrl;// 图片路径,json格式
//        public String fileUrl;// 文件路径,json
//        public String pkg; // 哪个包的下 如com.cropyme
//        public String cls; // 跳到哪个activity
//        public String pview; // iphone跳转页面名称
//        public String params; // 要求json格式
//        public int infoType;// 资讯类型
//        public int isDelete;// 是否已删除
//        public long createTime;

        if (hasAndNotNull(json, "circleNum")) ilie.circleNum = json.getString("circleNum");
        if (hasAndNotNull(json, "cls")) ilie.cls = json.getString("cls");
        if (hasAndNotNull(json, "createTime")) ilie.createTime = json.getString("createTime");
        if (hasAndNotNull(json, "headurl")) ilie.headurl = json.getString("headurl");
        if (hasAndNotNull(json, "infoId")) ilie.infoId = json.getLong("infoId");
        if (hasAndNotNull(json, "infoType")) ilie.infoType = json.getInt("infoType");
        if (hasAndNotNull(json, "isDelete")) ilie.isDelete = json.getInt("isDelete");
        if (hasAndNotNull(json, "isVip")) ilie.isVip = json.getInt("isVip");
        if (hasAndNotNull(json, "user_name")) ilie.name = json.getString("user_name");
        if (hasAndNotNull(json, "params")) ilie.params = json.getString("params");
        if (hasAndNotNull(json, "picUrl")) ilie.picUrl = json.getString("picUrl");
        if (hasAndNotNull(json, "pkg")) ilie.pkg = json.getString("pkg");
        if (hasAndNotNull(json, "pview")) ilie.pview = json.getString("pview");
        if (hasAndNotNull(json, "title")) ilie.title = json.getString("title");
        if (hasAndNotNull(json, "userId")) ilie.userId = json.getLong("userId");
        if (hasAndNotNull(json, "picUrl")) ilie.picUrl = json.getString("picUrl");
        if (hasAndNotNull(json, "fileUrl")) ilie.fileUrl = json.getString("fileUrl");
        if (hasAndNotNull(json, "isPush")) ilie.isPush = json.getBoolean("isPush");
        return ilie;
    }

}
