package com.procoin.social.entity;

import com.procoin.http.base.TaojinluType;

/**
 * 这个类是用来解析分享的
 */
public class WxBuidler implements TaojinluType {

    public String shareType;//分享的类型  weixin-kline
    public String content; //这个参数是 json数据或者文本，现在目前是json多一些
    public String shareLogo;//: "http://share.taojinroad.com:9997/app/image/kline_lg.png",
    public String shareTitle;//: "我正在玩K线角斗场",
    public String shareContent; //"内容"
}
