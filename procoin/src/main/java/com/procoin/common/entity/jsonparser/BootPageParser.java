//package com.cropyme.common.entity.jsonparser;
//
//
//import AbstractParser;
//import BootPage;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//public class BootPageParser extends AbstractParser<BootPage> {
//    @Override
//    public BootPage parse(JSONObject json) throws JSONException {
//
//        BootPage bootPage = new BootPage();
//        if (hasAndNotNull(json, "cls")) {
//            bootPage.cls = json.getString("cls");
//        }
//        if (hasAndNotNull(json, "pkg")) {
//            bootPage.pkg = json.getString("pkg");
//        }
//        if (hasAndNotNull(json, "params")) {
//            bootPage.params = json.getString("params");
//        }
//        if (hasAndNotNull(json, "img_url")) {
//            bootPage.img_url = json.getString("img_url");
//        }
//        return bootPage;
//    }
//
//}
