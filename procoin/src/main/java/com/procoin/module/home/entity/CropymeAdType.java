package com.procoin.module.home.entity;

/**
 * Created by zhengmj on 17-8-11.
 */

public enum CropymeAdType {

    imge(0, "图片"), // 图片
    video(1, "视频"), // 视频
//    wh_room(2, "直播间"), // 视频
//    card(3, "网红卡"), // 网红卡
//    constitution(4, "红人章程"),
    adv(5, "广告");

    private int type;
    private String typeTitle;

    CropymeAdType(int type, String typeTitle) {
        this.type = type;
        this.typeTitle = typeTitle;
    }

    public static boolean isVideo(int type) {
        if (type == video.type) {
            return true;
        }
        return false;
    }
}
