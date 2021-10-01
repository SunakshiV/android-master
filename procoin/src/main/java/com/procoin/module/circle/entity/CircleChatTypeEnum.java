package com.procoin.module.circle.entity;

import android.text.TextUtils;

/**
 * Created by zhengmj on 16-3-23.
 */
public enum CircleChatTypeEnum {
    SAY_TEXT("[text]=", 0), // 私聊回送文字
    SAY_IMG("[img]=", 1), // 私聊回送图片
    SAY_VOICE("[voice]=", 2), // 私聊回语音
    SAY_FDM("[fdm]=", 3), // 私聊回送股票，出现股票价和涨幅，如:[fdm]=探路者,sz300005,12.25,-0.23
    SAY_TIP("[tip]=", 4), // 私聊回送提示，出现中间文字，如:[tip]=XXX邀请了XXX进来
    SAY_UNREAD_MESSAGE("[unread_message]=", 5); // 未读消息 提示 由前端生成

    //    public static final String CIRCLE_CHAT_TYPE_TEXT = "[text]=";
//    public static final String CIRCLE_CHAT_TYPE_IMG = "[img]=";
//    public static final String CIRCLE_CHAT_TYPE_VOICE = "[voice]=";


    public final String type; //存数据库时会保存类型，比如图片，我可以查看所有图片，就是根据数据库存的这个类型
    public final int tValue;//这个值主要是图片要用到，还有长按事件用这个值判断

    CircleChatTypeEnum(String type, int tValue) {
        this.type = type;
        this.tValue = tValue;
    }

    public static int gettValue(String say) {
        for (CircleChatTypeEnum c : values()) {
            if (say.startsWith(c.type)) {
                return c.tValue;
            }
        }
        return 0;
    }


    public static boolean isVoice(String say) {
        if (!TextUtils.isEmpty(say)) {
            return say.startsWith(SAY_VOICE.type);
        }
        return false;
    }

    public static boolean isText(String say) {
        if (!TextUtils.isEmpty(say)) {
            return say.startsWith(SAY_TEXT.type);
        }
        return false;
    }


    public static boolean isTip(String say) {
        if (!TextUtils.isEmpty(say)) {
            return say.startsWith(SAY_TIP.type);
        }
        return false;
    }

    public static CircleChatTypeEnum getEnum(String say) {
        if (!TextUtils.isEmpty(say)) {
            for (CircleChatTypeEnum c : values()) {
                if (say.startsWith(c.type)) {
                    return c;
                }
            }
        }
        return SAY_TEXT;
    }
}

