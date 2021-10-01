package com.procoin.http.util;

import android.text.TextUtils;

/**
 *
 * 包括了所有的交易所
 * 微盘的类型,放这里是因为http里面也有用到,很多地方也是根据这个区判断的
 * Created by zhengmj on 17-2-16.
 */

public enum WpProjectname {


    //外网
    GG("weipan",  "广东省贵金属交易中心", "粤贵","http://weipan.taojinroad.com/","weipan.taojinroad.com",8908,"/jd_cz/check","G"), //广贵 https://weipan.taojinroad.com/
    HG("hgweipan",  "哈尔滨贵金属交易中心", "哈贵","http://hgweipan.taojinroad.com/","hgweipan.taojinroad.com" ,8909,"/recharge/check","H");//哈贵http://192.168.0.191:8081/


//    内网
//    GG("weipan",  "广东省贵金属交易中心", "粤贵","https://weipan.taojinroad.com/","weipan.taojinroad.com",8908,"/jd_cz/check","G"), //广贵 https://weipan.taojinroad.com/
//    HG("hg-weipan",  "哈尔滨贵金属交易中心", "哈贵","http://192.168.0.191:8081/","192.168.0.191" ,8909,"/recharge/check","H");//哈贵http://192.168.0.191:8081/

    private String name;
    private String fullName;
    private String jc;
    private String mApiTjrWeipanUri;//
    private String socketHost;
    private int port;//主要webSocket用到
    private String recharge;//这个是充值路径,不同的交易所充值的路径可能不一样
    private String simpleName;//这个主要是跟单推送的时候用到的,用来区分是哪个交易所的,H代表哈贵(hgweipan)   G代表广贵(weipan)

    public static boolean openHG=true;//是否开放哈贵


    WpProjectname(String name, String fullName,String jc,String mApiTjrWeipanUri,String socketHost,int port, String recharge,String simpleName) {
        this.name = name;
        this.fullName = fullName;
        this.jc = jc;
        this.mApiTjrWeipanUri=mApiTjrWeipanUri;
        this.socketHost=socketHost;
        this.port=port;
        this.recharge=recharge;
        this.simpleName=simpleName;
    }

    public String getName() {

        return name;
    }

    public String getFullName() {
        return fullName;
    }

    public String getJc() {
        return jc;
    }

    public static WpProjectname getDefault(){
//        return openHG?HG:GG;
        return HG;//
    }


    public static String getmApiTjrWeipanUriByName(String name){
        if (TextUtils.isEmpty(name)) return getDefault().mApiTjrWeipanUri;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.name.equals(name)) {
                return wpProjectname.mApiTjrWeipanUri;
            }
        }
        return getDefault().mApiTjrWeipanUri;
    }

    public static String getSocketHostByName(String name){
        if (TextUtils.isEmpty(name)) return getDefault().socketHost;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.name.equals(name)) {
                return wpProjectname.socketHost;
            }
        }
        return getDefault().socketHost;
    }

    public static int getPortByName(String name){
        if (TextUtils.isEmpty(name)) return getDefault().port;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.name.equals(name)) {
                return wpProjectname.port;
            }
        }
        return getDefault().port;
    }

    public static String getFullNameByName(String name) {
        if (TextUtils.isEmpty(name)) return getDefault().fullName;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.name.equals(name)) {
                return wpProjectname.fullName;
            }
        }
        return getDefault().fullName;
    }

    public static String getNameByFullName(String fullName) {
        if (TextUtils.isEmpty(fullName)) return getDefault().name;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.fullName.equals(fullName)) {
                return wpProjectname.name;
            }
        }
        return getDefault().name;
    }
    public static String getJcByName(String name) {
        if (TextUtils.isEmpty(name)) return getDefault().jc;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.name.equals(name)) {
                return wpProjectname.jc;
            }
        }
        return getDefault().jc;
    }

    public static String[] getFullNames() {
        String[] subNames = new String[values().length];
        for (int i = 0, m = values().length; i < m; i++) {
            subNames[i] = values()[i].getFullName();
        }
        return subNames;
    }
    public static String getRechargeByName(String name) {
        if (TextUtils.isEmpty(name)) return getDefault().recharge;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.name.equals(name)) {
                return wpProjectname.recharge;
            }
        }
        return getDefault().recharge;
    }


    public static String getNameBySimpleName(String simpleName) {
        if (TextUtils.isEmpty(simpleName)) return getDefault().name;
        for (WpProjectname wpProjectname : values()) {
            if (wpProjectname.simpleName.equals(simpleName)) {
                return wpProjectname.name;
            }
        }
        return getDefault().name;
    }

}
