package com.procoin.http;

import android.text.TextUtils;

public enum TjrBaseApi {

    //    mApiBaseUri("https://api.cropyme.com/crpm", "http://192.168.1.40/tjrapi", "http://192.168.1.66/tjrapi"), // 主要对用户关系系统http://www.redflea.com/tjr
    stockHomeUri("market.xincp11.com", "192.168.1.68", "192.168.1.66"), // 主要对行情sokcet:stock.redflea.com
    stockHomeUriHttp("http://market.xincp11.com", "http://192.168.1.68", "http://192.168.1.66"),//retrofit的baseurl必须以"/"结束否则报错

    mApiSubPushUrl("api.xincp11.com", "192.168.1.68", "192.168.1.66"),//push的socket
    mApiCropymeBaseUri("http://api.xincp11.com", "http://192.168.1.68", "http://192.168.1.66"),//retrofit的baseurl必须以"/"结束否则报错
    gamePredictSocket("predict.xincp11.com", "192.168.1.68", "192.168.1.66"), // 主要对行情sokcet:stock.redflea.com
    gamePredictHttp("http://predict.xincp11.com", "http://192.168.1.68", "http://192.168.1.66"), // 主要对行情sokcet:stock.redflea.com
    mApiCropymeBaseUploadFile("http://upload.xincp11.com", "http://192.168.1.68", "http://192.168.1.66");//retrofit的baseurl必须以"/"结束否则报错


    // 出外网一定要修改成false
    public static final boolean isDebug;
    public static final int debugType;//当debug为true时候才有效, debugType==0 连接青爷  debugType==1 连接科城
    public static final boolean isLog;

    static {
        isDebug = false;
        debugType =0;//debugType==0 连接青爷  debugType==1 连接科城
        isLog = false;
    }

    private String uri;
    private String debugUri;
    private String debugUri2;

    TjrBaseApi(String uri, String debugUri, String debugUri2) {
        this.uri = uri;
        this.debugUri = debugUri;
        this.debugUri2 = debugUri2;
    }

    public void setUri(String uri) {
        if (!TextUtils.isEmpty(uri)) {
            if (isDebug) {
                if (debugType == 0) {
                    this.debugUri = uri;
                } else {
                    this.debugUri2 = uri;
                }
            } else {
                this.uri = uri;
            }
        }
    }

    public String uri() {
        if (isDebug) {
            if (debugType == 0) {
                return debugUri;
            } else {
                return debugUri2;
            }
        } else {
            return uri;
        }
    }

}