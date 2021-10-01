package com.procoin.module.chat.entity;

import com.procoin.http.base.TaojinluType;

/**
 * Created by kechenng on 16-12-1.
 */

public class ChatHomeEntity implements TaojinluType {

    public long taUserId;
    public long chatCreateTime;
    public long chatMark;
    public String name;
    public String headurl;
    public String latestMsg;
    public String chatTopic;
    public boolean notify;
    public boolean isDel;//say里的记录是否被删除,如果是删除的,那么不显示

    @Override
    public String toString() {
        return "ChatHomeEntity{" +
                "taUserId=" + taUserId +
                ", chatCreateTime=" + chatCreateTime +
                ", chatMark=" + chatMark +
                ", user_name='" + name + '\'' +
                ", headurl='" + headurl + '\'' +
                ", latestMsg='" + latestMsg + '\'' +
                ", chatTopic='" + chatTopic + '\'' +
                ", notify=" + notify +
                '}';
    }
}
