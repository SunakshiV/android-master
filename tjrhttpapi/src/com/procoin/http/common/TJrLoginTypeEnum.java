package com.procoin.http.common;

/**
 * 所有的登录类型都是用这个枚举类型
 */
public enum TJrLoginTypeEnum {
    // 登錄的類型
    mb("mb"),
    sinawb("sinawb"),
    weixin("weixin"),
    qq("qq");

    private final String type;

    TJrLoginTypeEnum(String type) {
        this.type = type;
    }

    public String type() {
        return type;
    }
}
