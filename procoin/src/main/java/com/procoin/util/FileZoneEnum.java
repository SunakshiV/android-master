package com.procoin.util;

/**
 * Created by zhengmj on 18-11-7.
 */

public enum FileZoneEnum {

    FILE_COMMON(0, "/imredz/common/", "公用路径"), //
    VIDEO(1, "/imredz/video/", "视频"), //
    VOICE(2, "/imredz/voice/", "音频"), //
    IMAGE_HEAD(3, "/imredz/head/", "图片->头像"), //
    IMAGE_CHAT(4, "/imredz/chat/", "图片->私聊图片"), //
    IMAGE_DYNAMIC(5, "/imredz/dynamic/", "图片->动态路径"), //
    IMAGE_TOKA(6, "/imredz/toka/", "图片->通卡路径"), //
    IMAGE_PROJECT(7, "/imredz/project/", "图片->项目路径"), //
    IMAGE_IDENTITY(8, "/imredz/identity/", "图片->实名认证"), //
    IMAGE_BANNER(9, "/imredz/banner/", "图片->banner"), //
    IMAGE_OTC_APPEAL(10, "/imredz/otc/appeal/", "图片->OTC申诉"),
    IMAGE_PERSONAL_BACKGROUND(11,"/imtoka/userbackground/","图片->用户背景图片"); //

    public final int type;
    public final String path;
    public final String hint;

    FileZoneEnum(int type, String path, String hint) {
        this.type = type;
        this.path = path;
        this.hint = hint;
    }


}
