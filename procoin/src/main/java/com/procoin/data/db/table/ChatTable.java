package com.procoin.data.db.table;

import android.database.Cursor;
import android.util.Log;

import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.http.base.Group;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是新的，聊天记录表(私聊)
 */
public class ChatTable {

    public static final String TABLENAME = "chat_record_data";
    public static final String CHATTOPIC = "chat_topic";
    public static final String CHATID = "chat_id";
    public static final String USERID = "chat_user_id";
    public static final String TOUSERID = "chat_to_user_id";
    public static final String CREATETIME = "chat_create_time";
    public static final String SAY = "say";
    public static final String CHAT_MARK = "chat_mark";
    public static final String IS_DEL = "is_del";
    public static final String MYUSERID = "my_user_id";
    public static final String KEYID = "key_id";
    public static final String TYPE = "type";
    public static final String VOICEISRED = "voiceIsRed"; //声音是否读过

    public static final String CREATETABLE = "create table if not exists " + TABLENAME + " ("
            + CHATTOPIC + " varchar(16),"
            + CHATID + " varchar(16),"
            + USERID + " varchar(16),"
            + TOUSERID + " varchar(16),"
            + CREATETIME + "  varchar(16),"
            + SAY + " text," + CHAT_MARK + " int DEFAULT 0 , "
            + IS_DEL + " int DEFAULT 0, "
            + MYUSERID + " varchar(16),"
            + VOICEISRED + " int DEFAULT 0,"
            + TYPE + " int DEFAULT 0,constraint "
            + KEYID + " primary key(" + MYUSERID + "," + CHATTOPIC + "," + CHAT_MARK + "))";


    /**
     * @param cursor
     * @return
     */
    public static Group<CircleChatEntity> convertToCircleChatEntity(Cursor cursor) {
        try {
            if (null == cursor) return null;
            int resultCounts = cursor.getCount();
            if (resultCounts == 0 || !cursor.moveToFirst()) return null;
            CircleChatEntity circleChatEntity = null;
            Group<CircleChatEntity> group = new Group<CircleChatEntity>();
            for (int i = 0; i < resultCounts; i++) {
                circleChatEntity = new CircleChatEntity();
                circleChatEntity.chatTopic = cursor.getString(cursor.getColumnIndex(CHATTOPIC));
                circleChatEntity.chatId = Long.parseLong(cursor.getString(cursor.getColumnIndex(CHATID)));
                circleChatEntity.userId = Long.parseLong(cursor.getString(cursor.getColumnIndex(USERID)));
                circleChatEntity.toUid = Long.parseLong(cursor.getString(cursor.getColumnIndex(TOUSERID)));
                circleChatEntity.createTime = cursor.getString(cursor.getColumnIndex(CREATETIME));
                circleChatEntity.say = cursor.getString(cursor.getColumnIndex(SAY));
                circleChatEntity.chatMark = cursor.getInt(cursor.getColumnIndex(CHAT_MARK));
                circleChatEntity.name = cursor.getString(cursor.getColumnIndex(UserInfoTable.NAME));
                circleChatEntity.headurl = cursor.getString(cursor.getColumnIndex(UserInfoTable.HEADER_URL));
                circleChatEntity.type = cursor.getInt(cursor.getColumnIndex(TYPE));
//                circleChatEntity.voiceIsRed = cursor.getInt(cursor.getColumnIndex(VOICEISRED));
//                circleChatEntity.roleName = cursor.getString(cursor.getColumnIndex(ROLENAME));
                group.add(0, circleChatEntity);// 倒过来
                Log.d("circleChat", "userId==" + circleChatEntity.userId + "  user_name==" + circleChatEntity.name + "  headurl==" + circleChatEntity.headurl + "  say==" + circleChatEntity.say);
                cursor.moveToNext();
            }
            return group;
        } catch (Exception e) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


    /**
     * 查询所有图片类型
     *
     * @param cursor
     * @return
     */
    public static List<CircleChatEntity> convertToCircleChatPic(Cursor cursor) {
        try {
            if (null == cursor) return null;
            int resultCounts = cursor.getCount();
            if (resultCounts == 0 || !cursor.moveToFirst()) return null;
            CircleChatEntity circleChatEntity = null;
            List<CircleChatEntity> group = new ArrayList<CircleChatEntity>();
            for (int i = 0; i < resultCounts; i++) {
                circleChatEntity = new CircleChatEntity();
                circleChatEntity.say = cursor.getString(cursor.getColumnIndex(SAY));
                circleChatEntity.chatMark = cursor.getInt(cursor.getColumnIndex(CHAT_MARK));
                group.add(circleChatEntity);
                cursor.moveToNext();
            }
            return group;
        } catch (Exception e) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


}
