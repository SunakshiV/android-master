package com.procoin.data.db.table;

import android.database.Cursor;
import android.util.Log;

import com.procoin.module.chat.entity.ChatHomeEntity;
import com.procoin.http.base.Group;

import java.util.ArrayList;

public class ChatInfoTable {//私聊房间信息(私聊)
    public static final String TABLENAME = "chat_info";

    public static final String USERID = "my_user_id"; //
    public static final String TAUSERID = "ta_user_id"; //
    public static final String CHATTOPIC = "chat_topic";
    public static final String CHATNAME = "chat_name";
    public static final String CHATCREATETIME = "chat_create_time";
    public static final String CHAT_MARK = "chat_mark";
    public static final String KEYID = "key_id";  //
    public static final String CHATLOGO = "chat_logo";
//    public static final String LATESTMSG = "latest_msg";
//	circle = CircleInfo{advert='null', circleNum='19487', userId=1200071, circleId=1502608, circleName='东成外', brief='淘金路外网', memberList='null', longitude='113.355859', latitude='23.118778', tag='null', createTime='20160815181151', isLock=0, memberNum=1, myUserId=0, circleMark=0, chatMark=0, infoId=0, chatUserId=0, say='null', lastName='null', role=0, ageTime='17天', dist=0, isMember=false, isVip=0, user_name='三三', headurl='http://share.taojinroad.com:9997/user_head/19592333951200071.png', maxNum=100, badCount=0, goodCount=0, midCount=0, credit='null', selected=false, circleAddr='广东省广州市天河区纺联北路靠近尚合传播', recent_time='null', circleLogo='http://share.taojinroad.com:9997/user_head/19592333951200071.png'}


    public static final String CREATETABLE = "create table if not exists " + TABLENAME + " ("
            + USERID + " varchar(16),"
            + TAUSERID + " varchar(16),"
            + CHATTOPIC + " varchar(16),"
            + CHATNAME + " varchar(20),"
//            + CHATCREATETIME + " varchar(16),"      chat_create_time从chat_record_data中查询
            + CHAT_MARK + " int DEFAULT 0 ,"
            + CHATLOGO + " text ,"
//            + LATESTMSG + " text ,"
            + "constraint " + KEYID + " primary key(" + CHATTOPIC + "))";


//    public static Group<ChatMytopic> convertToCircleInfo(Cursor cursor) {
//        try {
//            if (null == cursor) return null;
//            int resultCounts = cursor.getCount();
//            if (resultCounts == 0 || !cursor.moveToFirst()) return null;
//            ChatMytopic chatMytopic = null;
//            Group<ChatMytopic> group = new Group<ChatMytopic>();
//            for (int i = 0; i < resultCounts; i++) {
//                chatMytopic = new ChatMytopic();
////                chatMytopic.tableId = cursor.getInt(cursor.getColumnIndex(ChatTopicTable.CHAT_TOPIC_KEY_ID));
//                chatMytopic.chatTopicId = cursor.getString(cursor.getColumnIndex(CHATTOPIC));
//                chatMytopic.chatContent = cursor.getString(cursor.getColumnIndex(ChatTopicTable.CHAT_RECORD_CONTENT));
////                chatMytopic.tableTime = cursor.getString(cursor.getColumnIndex(ChatTopicTable.CHAT_RECORD_CREATE_TIME));
//                chatMytopic.createTime = cursor.getString(cursor.getColumnIndex(CHATCREATETIME));
//                chatMytopic.headurl = cursor.getString(cursor.getColumnIndex(CHATLOGO));
//                chatMytopic.user_name = cursor.getString(cursor.getColumnIndex(CHATNAME));
//
//
//                group.add(chatMytopic);
//                cursor.moveToNext();
//            }
//            return group;
//        } catch (Exception e) {
//        } finally {
//            if (cursor != null) cursor.close();
//        }
//        return null;
//    }


    public static Group<ChatHomeEntity> convertToChatHomeEntity(Cursor cursor) {
        try {
            if (cursor == null) return null;
            Log.d("convertToChatHomeEntity"," cursor.getCount()=="+ cursor.getCount());
            ChatHomeEntity entity = null;
            Group<ChatHomeEntity> group = new Group<ChatHomeEntity>();
            while (cursor.moveToNext()) {
                entity = new ChatHomeEntity();
                entity.taUserId = cursor.getLong(cursor.getColumnIndex(TAUSERID));
                entity.chatCreateTime = cursor.getLong(cursor.getColumnIndex(CHATCREATETIME));
                entity.chatMark = cursor.getLong(cursor.getColumnIndex(CHAT_MARK));
                entity.name = cursor.getString(cursor.getColumnIndex(CHATNAME));
                entity.headurl = cursor.getString(cursor.getColumnIndex(CHATLOGO));
                entity.latestMsg = cursor.getString(cursor.getColumnIndex(ChatTable.SAY));
                entity.chatTopic = cursor.getString(cursor.getColumnIndex(CHATTOPIC));
                int intDel = cursor.getInt(cursor.getColumnIndex(ChatTable.IS_DEL));
                entity.isDel = intDel > 0 ? true : false;
                group.add(entity);
                Log.d("convertToChatHomeEntity", "entity: " + entity.toString());
            }
            return group;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


    public static ArrayList<String> convertToChatTopic(Cursor cursor) {
        try {
            if (cursor == null) return null;
            ArrayList<String> chatTopicList = new ArrayList<String>();
            while (cursor.moveToNext()) {
                chatTopicList.add(cursor.getString(cursor.getColumnIndex(CHATTOPIC)));
            }
            return chatTopicList;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


//    public static ChatHomeEntity convertToChatInfo(Cursor cursor) {
//        try {
//            if (null == cursor) return null;
//            int resultCounts = cursor.getCount();
//            if (resultCounts == 0 || !cursor.moveToFirst()) return null;
//            User user = new User();
//            user.setUserId(cursor.getLong(cursor.getColumnIndex(USER_ID)));
//            user.setName(cursor.getString(cursor.getColumnIndex(NAME)));
//            user.setHeadurl(cursor.getString(cursor.getColumnIndex(HEADER_URL)));
//            user.setIsVip(cursor.getInt(cursor.getColumnIndex(USER_LEVEL)));
//            return user;
//        } catch (Exception e) {
//        } finally {
//            if (cursor != null) cursor.close();
//        }
//        return null;
//    }


//    public static long saveuOrUpateChatInfo(SQLiteDatabase db , long userId, int userLevel, String user_name, String headerUrl) {
//        if (userId == 0 || db == null) return 0;
//        ContentValues values = new ContentValues();
//        values.put(USER_ID, userId);
//        if(user_name != null || !"".equals(user_name) )values.put(NAME, user_name);
//        values.put(USER_LEVEL, userLevel);
//        if(headerUrl != null || !"".equals(headerUrl) )values.put(HEADER_URL, headerUrl);
//        return db.replace(TABLE_NAME, null, values);
//    }


}
