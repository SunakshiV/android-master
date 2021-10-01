package com.procoin.http.model;

import com.procoin.http.base.TaojinluType;

public class Components implements TaojinluType {

    public String appName;//:"红跳蚤安卓",
    public String platform;
    public String identifyCode;
    public String downloadUrl; //更新链接
    public String bugMsg;      //更新提示
    public int version;
    public int isForce;        //是否强制更新
    public String updateTime;


    @Override
    public String toString() {
        return "Components{" +
                "app_name='" + appName + '\'' +
                ", download_url='" + downloadUrl + '\'' +
                ", bug_msg='" + bugMsg + '\'' +
                ", version=" + version +
                '}';
    }
}
