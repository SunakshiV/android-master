package com.procoin.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.procoin.data.db.table.ChatInfoTable;
import com.procoin.data.db.table.ChatTable;
import com.procoin.data.db.table.CircleChatTable;
import com.procoin.data.db.table.CircleInfoTable;
import com.procoin.module.chat.entity.ChatHomeEntity;
import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.module.circle.entity.CircleRel;
import com.procoin.util.VeDate;
import com.procoin.data.db.table.CircleRelTable;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;

import com.procoin.data.db.table.UserInfoTable;

import com.procoin.module.circle.entity.CircleInfo;

import java.util.List;
import java.util.Map;

public class TaoJinLuDatabase {
    private static final String TAG = TaoJinLuDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "encropy.db";
    private static final int DATABASE_VERSION = 1;//
    private volatile static TaoJinLuDatabase instance = null;
    private DatabaseHelper mOpenHelper = null;

    // private Context mContext = null;

    /**
     * SQLiteOpenHelper
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        // Construct
        public DatabaseHelper(Context context, String name, CursorFactory factory, int version) {
            super(context, name, factory, version);
            Log.d("TaoJinLuDatabase", "DatabaseHelper");
            // Log.d("TalkieCommentTable", TalkieCommentTable.CREATETABLE);
            // Log.d("TalkieCommentTable", TalkieGoodTable.CREATETABLE);
            // Log.d("TalkieCommentTable", TalkieRecordTable.CREATETABLE);
        }

        public DatabaseHelper(Context context) {
            this(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("TaoJinLuDatabase", "onCreate==");
            dropAllTables(db, 0);
        }

        @Override
        public synchronized void close() {
            super.close();
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            dropAllTables(db, oldVersion);
        }

        private void dropAllTables(SQLiteDatabase db, int oldVersion) {
            // 版本更新时,把新版本的数据库要创建的表放到oldVersion里执行.如果是没有任何数据库的话,就从0开始.把所有要生成的表都跑一遍.
            Log.d("TaoJinLuDatabase", "oldVersion==" + oldVersion);
            switch (oldVersion) {
                case 0:
                    Log.d("TaoJinLuDatabase", "0000000" );

                    db.execSQL(ChatInfoTable.CREATETABLE); //创建私聊info表
                    db.execSQL(ChatTable.CREATETABLE);//聊天记录
                    db.execSQL(UserInfoTable.CREATE_TABLE);// 增加记录所有用户信息记录表

                    db.execSQL(CircleInfoTable.CREATETABLE);//圈子信息表
                    db.execSQL(CircleChatTable.CREATETABLE);//圈子私聊表
                    db.execSQL(CircleRelTable.CREATETABLE);//圈子关系表，记录我有哪些圈子

                case 1:
                    Log.d("TaoJinLuDatabase", "11111111" );
                    break;

            }
        }
    }

    private TaoJinLuDatabase(Context context) {
        // mContext = context;
        mOpenHelper = new DatabaseHelper(context);
    }

    public static TaoJinLuDatabase getInstance(Context context) {
        if (instance == null) {
            synchronized (TaoJinLuDatabase.class) {
                if (instance == null) {
                    instance = new TaoJinLuDatabase(context);
                }
            }
        }
        return instance;
    }

    public void close() {
        if (null != instance) {
            mOpenHelper.close();
            instance = null;
        }
    }

    /**
     * 获取数据读写操作方式
     *
     * @param writeable
     * @return
     */
    public SQLiteDatabase getDbOppType(boolean writeable) {
        if (writeable) {
            return mOpenHelper.getWritableDatabase();
        } else {
            return mOpenHelper.getReadableDatabase();
        }
    }


//    /**
//     * 关系表,我有哪些圈子
//     * @param circleId
//     * @param userId
//     * @return
//     */
//    public long insertCircleRel(String circleId, long userId,int role) {
//        if (circleId == null || userId == 0) return 0l;
//        SQLiteDatabase db = getDbOppType(true);
//        String sql = "select count(*) AS maxCount from " + CircleRelTable.TABLENAME + " where " + CircleRelTable.CIRCLE_ID + "=? and " + CircleRelTable.MYUSERID + "=?";
//        Cursor cursor = null;
//        try {
//            cursor = db.rawQuery(sql, new String[]{circleId, String.valueOf(userId)});
//            if (cursor != null && cursor.moveToFirst()) {
//                int c = cursor.getInt(cursor.getColumnIndex("maxCount"));
//                if (c > 0) return 0;
//            }
//            ContentValues contentValues = new ContentValues();
//            contentValues.put(CircleRelTable.CIRCLE_ID, circleId);
//            contentValues.put(CircleRelTable.MYUSERID, userId);
//            contentValues.put(CircleRelTable.ROLE, role);
//            return db.replace(CircleRelTable.TABLENAME, null, contentValues);
//        } catch (Exception e) {
//        } finally {
//            if (cursor != null) cursor.close();
//        }
//        return 0;
//    }

    //更新我所在圈子的角色
    public void insertAndUpdateCircleRel(String circleId, long userId, int role) {
        if (circleId == null || userId == 0) return;
        SQLiteDatabase db = getDbOppType(true);
        //这里不能用replace 因为会把 recentTime 清掉

        CircleRel circleRel = getCircleRel(circleId, userId);
        ContentValues cv = new ContentValues();
        cv.put(CircleRelTable.CIRCLE_ID, circleId);
        cv.put(CircleRelTable.MYUSERID, userId);
        cv.put(CircleRelTable.ROLE, role);
        if (circleRel != null) {//更新
            db.update(CircleRelTable.TABLENAME, cv, CircleRelTable.CIRCLE_ID + "=? and " + CircleRelTable.MYUSERID + "=?", new String[]{circleId, String.valueOf(userId)});
        } else {//插入
            db.insert(CircleRelTable.TABLENAME, null, cv);
        }
    }

    public CircleRel getCircleRel(String circleNum, long userId) {
        SQLiteDatabase db = getDbOppType(true);
        Cursor cursor = db.query(CircleRelTable.TABLENAME, null, CircleRelTable.CIRCLE_ID + "=? and " + CircleRelTable.MYUSERID + "=?", new String[]{circleNum, String.valueOf(userId)}, null, null, null);
        return CircleRelTable.convertToCircleRel(cursor);
    }

    public int delCircleRel(String circleId, long userId) {
        SQLiteDatabase db = getDbOppType(true);
        return db.delete(CircleRelTable.TABLENAME, CircleRelTable.CIRCLE_ID + "=? and " + CircleRelTable.MYUSERID + "=?", new String[]{circleId, String.valueOf(userId)});
    }

    //获取我在当前圈子的角色
    public int getRole(String circleId, long myUserId) {
        SQLiteDatabase db = getDbOppType(false);
        Cursor cursor = db.query(CircleRelTable.TABLENAME, null, CircleRelTable.CIRCLE_ID + "=? and " + CircleRelTable.MYUSERID + "=?", new String[]{circleId, String.valueOf(myUserId)}, null, null, null);
        return CircleRelTable.getRole(cursor);
    }

    /**
     * 插入一条圈子记录 ，没有就插入，有就更新
     *
     * @return
     */
    public long insertMyCircle(CircleInfo circleInfo) {
        if (circleInfo == null) return 0l;
        SQLiteDatabase db = getDbOppType(true);
        ContentValues contentValues = new ContentValues();
        contentValues.put(CircleInfoTable.CIRCLE_ID, circleInfo.circleId);
        contentValues.put(CircleInfoTable.CIRCLE_NAME, circleInfo.circleName);
        contentValues.put(CircleInfoTable.CIRCLE_LOGO, circleInfo.circleLogo);
        contentValues.put(CircleInfoTable.CREATETIME, circleInfo.createTime);
        Log.d(TAG, "insertMyCircle()------->插入一条我的圈子记录 ，没有就插入，有就更新");
        return db.replace(CircleInfoTable.TABLENAME, null, contentValues);
    }

    /**
     * 获取我的圈子 包括我创建的和我加入的
     * circle_info a
     * circle_chat c
     * user_info u
     * circle_rel q
     *
     * @param userId
     * @return
     */
    public Group<CircleInfo> getMyCircle(long userId) {
//        String sql = "SELECT a.*,u.*,c.say,c.chat_create_time,q.role ,MAX(c.chat_mark) FROM (circle_info a LEFT JOIN circle_chat c ON a.circle_num = c.circle_num AND c.my_user_id=?) LEFT JOIN user_info u ON u.info_user_id=c.chat_user_id LEFT JOIN circle_rel q ON a.circle_num=q.circle_num WHERE q.my_user_id=? GROUP BY a.circle_num";
        String sql = "SELECT a.*,u.*,c.say,q.recent_time,q.role ,MAX(c.chat_mark) FROM (circle_info a LEFT JOIN circle_chat c ON a.circle_id = c.circle_id AND c.my_user_id=?) LEFT JOIN user_info u ON u.info_user_id=c.chat_user_id LEFT JOIN circle_rel q ON a.circle_id=q.circle_id WHERE q.my_user_id=? GROUP BY a.circle_id";
        SQLiteDatabase db = getDbOppType(false);
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId), String.valueOf(userId)});
        Log.d("CircleInfoTable","Cursor=="+c);
        return CircleInfoTable.convertToCircleInfo(c);
    }

    public Group<CircleInfo> getMyCircleOnBG(long userId) {
        String sql = "SELECT c.* FROM circle_rel r left join circle_info c ON r.circle_id = c.circle_id WHERE r.my_user_id=?";
        SQLiteDatabase db = getDbOppType(false);
        Cursor c = db.rawQuery(sql, new String[]{String.valueOf(userId)});
        return CircleInfoTable.convertToCircleBG(c);
    }

    /**
     * 获取圈子私聊记录
     *
     */
    public Group<CircleChatEntity> getCircleChat(long myUserId, String circleId, int mark, int num) {
        SQLiteDatabase db = getDbOppType(false);
        String sql = "";
        String[] selectionArgs = null;
        if (mark > 0) {// 大于0 查历史
            sql = "SELECT *  FROM (circle_chat c left join user_info u on u.info_user_id=c.chat_user_id)  WHERE c.circle_id=? and c.chat_mark<? and c.my_user_id=? and c.is_del= 0 order by chat_mark desc limit ?";
            selectionArgs = new String[]{circleId, String.valueOf(mark), String.valueOf(myUserId), String.valueOf(num)};
        } else {// mark==0就查最新
            sql = "SELECT *  FROM (circle_chat c left join user_info u on u.info_user_id=c.chat_user_id)  WHERE c.circle_id=? and c.my_user_id=? and c.is_del= 0 order by chat_mark desc limit ?";
            selectionArgs = new String[]{circleId, String.valueOf(myUserId), String.valueOf(num)};
        }
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return CircleChatTable.convertToCircleChatEntity(cursor);
    }
    //清除圈子私聊
    public int clearCircleChat(String circleId) {
        SQLiteDatabase db = getDbOppType(true);
        return db.delete(CircleChatTable.TABLENAME, CircleChatTable.CIRCLE_ID + "=?", new String[]{circleId});
    }

    /**
     * 获取圈子所有的图片
     * @param myUserId
     * @param circleNum
     * @return
     */
    public List<CircleChatEntity> getCirclePicChat(long myUserId, String circleNum) {
        SQLiteDatabase db = getDbOppType(false);
//		String	sql = "SELECT c.say  FROM circle_chat c  WHERE c.circle_num=? and c.is_del= 0 and c.type=1 and c.my_user_id=? order by chat_mark asc";
        String sql = "SELECT *  FROM circle_chat c WHERE c.circle_id=? and c.is_del= 0 and c.type=1 and c.my_user_id=? order by chat_mark asc";
        String[] selectionArgs = new String[]{circleNum, String.valueOf(myUserId)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return ChatTable.convertToCircleChatPic(cursor);
    }

    //更新real表的最近时间
    public int updateCircleRelTableRecent_time(String userId, String circleId, String recentTime) {
        Log.d("realTime", VeDate.getStringDateOfString(recentTime, "MM/dd HH:mm ss") + "  userId=" + userId + " circleId==" + circleId);
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(CircleRelTable.RECENT_TIME, recentTime);
        int result = db.update(CircleRelTable.TABLENAME, cv, CircleRelTable.CIRCLE_ID + "=? AND " + CircleRelTable.MYUSERID + "=? AND (" + CircleRelTable.RECENT_TIME + "<? OR " + CircleRelTable.RECENT_TIME + " IS NULL )", new String[]{circleId, userId, recentTime});
        Log.d("realTime", "result==" + result);
        return result;
    }

    /**
     *
     *
     * 圈子私聊插入一条数据
     *
     * 私聊不用加myUserid 因为不同用户登录可以共用私聊数据
     *
     * @param entity
     */
    public long insertCircleChat(long myUserId, CircleChatEntity entity) {
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(CircleChatTable.MYUSERID, myUserId);
        cv.put(CircleChatTable.CIRCLE_ID, entity.chatTopic);
        cv.put(CircleChatTable.CHAT_ID, entity.chatId);
        cv.put(CircleChatTable.USER_ID, entity.userId);
        cv.put(CircleChatTable.CREATETIME, entity.createTime);
        cv.put(CircleChatTable.SAY, entity.say);
        cv.put(CircleChatTable.IS_DEL, 0);
        cv.put(CircleChatTable.CHAT_MARK, entity.chatMark);
        cv.put(CircleChatTable.TYPE, entity.type);
        cv.put(CircleChatTable.VOICEISRED, entity.voiceIsRed);
        cv.put(CircleChatTable.ROLENAME, entity.roleName);
        return db.replace(CircleChatTable.TABLENAME, null, cv);
    }

    /**
     * 删除圈子私聊数据
     * @param myUserId
     * @param circleId
     * @param charMark
     * @return
     */
    public int delCircleChat(long myUserId, String circleId, int charMark) {
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(CircleChatTable.IS_DEL, 1);
        return db.update(CircleChatTable.TABLENAME, cv, CircleChatTable.CIRCLE_ID + "=? and " + CircleChatTable.MYUSERID + "=? and " + CircleChatTable.CHAT_MARK + "=?", new String[]{circleId, String.valueOf(myUserId), String.valueOf(charMark)});
    }

    //私聊清除一条记录
    public int clearPrivateChatVoiceRed(long myUserId, String circleId, int mark) {
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(ChatTable.VOICEISRED, 0);
        return db.update(ChatTable.TABLENAME, cv, ChatTable.MYUSERID + "=? and " + ChatTable.CHATTOPIC + "=? and " + ChatTable.CHAT_MARK + "=?", new String[]{String.valueOf(myUserId), circleId, String.valueOf(mark)});

    }
    //圈子清除一条记录
    public int clearCircleChatVoiceRed(long myUserId, String circleId, int mark) {
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(ChatTable.VOICEISRED, 0);
        return db.update(CircleChatTable.TABLENAME, cv, CircleChatTable.MYUSERID + "=? and " + CircleChatTable.CIRCLE_ID + "=? and " + CircleChatTable.CHAT_MARK + "=?", new String[]{String.valueOf(myUserId), circleId, String.valueOf(mark)});

    }
    // 圈子结束
    // userInfo表 开始

    /**
     * 查询userinfo表中的一条记录根据userId
     */
    public User getUserInfo(long userId) {
        SQLiteDatabase db = getDbOppType(false);
        Cursor c = db.query(UserInfoTable.TABLE_NAME, null, UserInfoTable.USER_ID + "=?", new String[]{String.valueOf(userId)}, null, null, null);
        return UserInfoTable.convertToUser(c);
    }

    public Group<User> findUserInfo(String in) {
        String sql = "SELECT * from user_info u WHERE u.info_user_id IN (" + in + ")";
        SQLiteDatabase db = getDbOppType(false);
        Cursor c = db.rawQuery(sql, null);
        return UserInfoTable.convertToGroupUser(c);
    }

    public Map<String, User> findUserInfoToMap(String in) {
        String sql = "SELECT * from user_info u WHERE u.info_user_id IN (" + in + ")";
        SQLiteDatabase db = getDbOppType(false);
        Cursor c = db.rawQuery(sql, null);
        return UserInfoTable.convertToMapUser(c);
    }

    /**
     * 更新userInfo表中的一条记录
     *
     * @param userId
     * @return
     */
    public long saveAndUpdateUserInfo(long userId, String name, String headurl) {
        return saveAndUpdateUserInfo(userId, name, headurl, -1);
    }

    public long saveAndUpdateUserInfo(long userId, String name, String headurl, int isVip) {
        if (userId == 0) return 0;
        User user = getUserInfo(userId);
        if (user != null) {
            boolean needUpdate = false;// 记录是否需要更新
            if (!TextUtils.isEmpty(name) && !name.equals(user.getUserName())) {
                user.setUserName(name);
                needUpdate = true;
            }
            if (!TextUtils.isEmpty(headurl) && !headurl.equals(user.getHeadUrl())) {
                user.setHeadUrl(headurl);
                needUpdate = true;
            }
//            if (isVip != -1) user.setIsVip(isVip);
            if (needUpdate) {
                SQLiteDatabase db = getDbOppType(true);
                ContentValues cv = new ContentValues();
                cv.put(UserInfoTable.NAME, user.getUserName());
                cv.put(UserInfoTable.HEADER_URL, user.getHeadUrl());
//                cv.put(UserInfoTable.USER_LEVEL, user.getIsVip());
                return db.update(UserInfoTable.TABLE_NAME, cv, UserInfoTable.USER_ID + "=?", new String[]{String.valueOf(user.getUserId())});
            }
            return 0;
        } else {
            if (isVip == -1) isVip = 0;
            return insertUserInfo(userId, name, headurl, isVip);
        }
    }

    public long insertUserInfo(long userId, String name, String headurl, int isVip) {
        if (userId == 0) return 0;
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(UserInfoTable.USER_ID, userId);
        cv.put(UserInfoTable.NAME, name);
        cv.put(UserInfoTable.HEADER_URL, headurl);
        cv.put(UserInfoTable.USER_LEVEL, isVip);
        return db.insert(UserInfoTable.TABLE_NAME, null, cv);
    }


    // userInfo表 结束


// privateChat 私聊开始

    /**
     * 获取private私聊记录
     *
     * @param chatTopic
     * @param num
     */
    public Group<CircleChatEntity> getPrivateChat(long myUserId, String chatTopic, int mark, int num) {
        SQLiteDatabase db = getDbOppType(false);
        String sql = "";
        String[] selectionArgs = null;
        if (mark > 0) {// 大于0 查历史
            sql = "SELECT *  FROM (" + ChatTable.TABLENAME + " c left join user_info u on u.info_user_id=c.chat_user_id)  WHERE c." + ChatTable.CHATTOPIC + "=? and c.chat_mark<? and c.my_user_id=? and c.is_del= 0 order by chat_mark desc limit ?";
            selectionArgs = new String[]{chatTopic, String.valueOf(mark), String.valueOf(myUserId), String.valueOf(num)};
        } else {// mark==0就查最新
            sql = "SELECT *  FROM (" + ChatTable.TABLENAME + " c left join user_info u on u.info_user_id=c.chat_user_id)  WHERE c." + ChatTable.CHATTOPIC + "=? and c.my_user_id=? and c.is_del= 0 order by chat_mark desc limit ?";
            selectionArgs = new String[]{chatTopic, String.valueOf(myUserId), String.valueOf(num)};
        }

        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return ChatTable.convertToCircleChatEntity(cursor);
    }

    /**
     * 获取私聊列表
     *
     * @param myUserId
     * @return
     */

    public Group<ChatHomeEntity> getChatHomeList(long myUserId) {
        String sql = "select info.*, chat.*, max(chat.chat_mark) from (chat_info as info left join chat_record_data as chat on info.chat_topic = chat.chat_topic) where (info.my_user_id=? and chat.say is not null) group by info.chat_topic order by chat.chat_create_time desc";
        SQLiteDatabase db = getDbOppType(false);
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(myUserId)});
        Group<ChatHomeEntity> entities = ChatInfoTable.convertToChatHomeEntity(cursor);
        return entities;
    }

    /**
     * 删除私聊列表的一条记录
     *
     * @param chatTopic 主键
     */
    public int deleteChatHomeRecord(String myUserId, String chatTopic) {
        SQLiteDatabase db = getDbOppType(true);
        return db.delete(ChatInfoTable.TABLENAME, ChatInfoTable.USERID + "=? and " + ChatInfoTable.CHATTOPIC + "=?", new String[]{myUserId, chatTopic});
    }

    public int clearPrivateChat(long myUserId, String chatTopic) {
        SQLiteDatabase db = getDbOppType(true);
        Group<CircleChatEntity> list = getPrivateChat(myUserId, chatTopic, 0, 1);
        //如果有记录 那么就把状态变为isdel 变为 1
        if (list != null && list.size() > 0) {
            ContentValues cv = new ContentValues();
            cv.put(ChatTable.IS_DEL, 1);
            db.delete(ChatTable.TABLENAME, ChatTable.CHATTOPIC + "=? and " + ChatTable.MYUSERID + "=? and " + ChatTable.CHAT_MARK + "  <?", new String[]{chatTopic, String.valueOf(myUserId), String.valueOf(list.get(0).chatMark)});
            return db.update(ChatTable.TABLENAME, cv, ChatTable.CHATTOPIC + "=? and " + ChatTable.MYUSERID + "=? and " + ChatTable.CHAT_MARK + "=?", new String[]{chatTopic, String.valueOf(myUserId), String.valueOf(list.get(0).chatMark)});
        } else {
            //如果没记录那么就按这样子的情况清空
            return db.delete(ChatTable.TABLENAME, ChatTable.CHATTOPIC + "=? and " + ChatTable.MYUSERID + "=?", new String[]{chatTopic, String.valueOf(myUserId),});
        }
    }

    /**
     * @param entity
     */
    public long insertPrivateChat(long myUserId, CircleChatEntity entity) {
        SQLiteDatabase db = getDbOppType(true);
        ContentValues cv = new ContentValues();
        cv.put(ChatTable.MYUSERID, myUserId);
        cv.put(ChatTable.CHATTOPIC, entity.chatTopic);
        cv.put(ChatTable.CHATID, entity.chatId);
        cv.put(ChatTable.USERID, entity.userId);
        cv.put(ChatTable.TOUSERID, entity.toUid);
        cv.put(ChatTable.CREATETIME, entity.createTime);
        cv.put(ChatTable.SAY, entity.say);
        cv.put(ChatTable.IS_DEL, 0);
        cv.put(ChatTable.CHAT_MARK, entity.chatMark);
        cv.put(ChatTable.TYPE, entity.type);
        cv.put(ChatTable.VOICEISRED, entity.voiceIsRed);
//        cv.put(CircleChatTable.ROLENAME, entity.roleName);
        return db.replace(ChatTable.TABLENAME, null, cv);
    }

    public List<CircleChatEntity> getPrivateChatPicChat(long myUserId, String chatTopic) {
        SQLiteDatabase db = getDbOppType(false);
//		String	sql = "SELECT c.say  FROM circle_chat c  WHERE c.circle_num=? and c.is_del= 0 and c.type=1 and c.my_user_id=? order by chat_mark asc";
        String sql = "SELECT *  FROM " + ChatTable.TABLENAME + " c WHERE c." + ChatTable.CHATTOPIC + "=? and c.is_del= 0 and c.type=1 and c.my_user_id=? order by chat_mark asc";
        String[] selectionArgs = new String[]{chatTopic, String.valueOf(myUserId)};
        Cursor cursor = db.rawQuery(sql, selectionArgs);
        return ChatTable.convertToCircleChatPic(cursor);

    }


    public long saveOrUpdateChatInfo(long my_user_id, long ta_userId, String chat_logo, String chat_name, String chat_topic) {
        if (my_user_id == 0) return 0;
        boolean isNeedUpdate = false;
        ChatHomeEntity chatHomeEntity = getChatInfo(String.valueOf(my_user_id), chat_topic);
        if (chatHomeEntity != null) {
            if (!TextUtils.isEmpty(chat_name) && !chat_name.equals(chatHomeEntity.name)) {
                isNeedUpdate = true;
            }
            if (!TextUtils.isEmpty(chat_logo) && !chat_logo.equals(chatHomeEntity.headurl)) {
                isNeedUpdate = true;
            }
        } else {
            isNeedUpdate = true;
        }
        if (isNeedUpdate) {
            SQLiteDatabase db = getDbOppType(true);
            ContentValues cv = new ContentValues();
            cv.put(ChatInfoTable.USERID, my_user_id);
            cv.put(ChatInfoTable.TAUSERID, ta_userId);
            cv.put(ChatInfoTable.CHATLOGO, chat_logo);
            cv.put(ChatInfoTable.CHATNAME, chat_name);
            cv.put(ChatInfoTable.CHATTOPIC, chat_topic);
            return db.replace(ChatInfoTable.TABLENAME, null, cv);
        } else {
            return 0;
        }


    }

    /**
     * 查询userinfo表中的一条记录根据userId
     */
    public ChatHomeEntity getChatInfo(String userId, String chatTopic) {
        SQLiteDatabase db = getDbOppType(false);

        String sql = "select info.*, chat.*, max(chat.chat_mark) from (chat_info as info left join chat_record_data as chat on info.chat_topic = chat.chat_topic) where (info.my_user_id=? and info.chat_topic =? ) group by info.chat_topic order by chat.chat_create_time desc";
//        String sql = " select * from " + ChatInfoTable.TABLENAME + " where  " + ChatInfoTable.USERID + "=? and " + ChatInfoTable.CHATTOPIC + "=? ";
        Cursor c = db.rawQuery(sql, new String[]{userId, chatTopic});
        Group<ChatHomeEntity> group = ChatInfoTable.convertToChatHomeEntity(c);
        ChatHomeEntity chatHomeEntity = null;
        if (group != null && group.size() > 0) {
            chatHomeEntity = group.get(0);
            Log.d("ddd", "entity: getChatInfo " + chatHomeEntity.toString());
        } else {
            Log.d("ddd", "entity: getChatInfo null");
        }

        return chatHomeEntity;
    }

    /**
     * @param myUserId
     * @param chatTopic
     * @param charMark
     * @return
     */
    public int delPrivateChat(long myUserId, String chatTopic, int charMark) {
        SQLiteDatabase db = getDbOppType(true);
        return db.delete(ChatTable.TABLENAME, ChatTable.CHATTOPIC + "=? and " + ChatTable.MYUSERID + "=? and " + ChatTable.CHAT_MARK + "=?", new String[]{chatTopic, String.valueOf(myUserId), String.valueOf(charMark)});
    }


}
