package com.procoin.module.circle.entity;


import com.procoin.http.base.TaojinluType;

/**
 * Created by kechenng on 16-9-6.
 */
public class PinnedMentionUser implements TaojinluType {
    public int type;    // 0 表示用户项 ，1 表示字段项（# A B ...）
    public CircleMemberUser user;

    public PinnedMentionUser(int type, CircleMemberUser user) {
        this.type = type;
        this.user = user;
    }
}
