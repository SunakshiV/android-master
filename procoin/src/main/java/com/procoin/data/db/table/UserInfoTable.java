package com.procoin.data.db.table;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.procoin.data.db.TaoJinLuDatabase;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;

import java.util.HashMap;
import java.util.Map;


public class UserInfoTable {
	public static final String TABLE_NAME = "user_info";
	
	public static final String TABLE_ID = "info_id";
	public static final String USER_ID = "info_user_id";              //用户id
	public static final String NAME = "info_name";                    //用户名称
	public static final String USER_LEVEL = "info_user_level";        //0代表正常,1代表加v
	public static final String HEADER_URL = "info_header_url";        //头像

	public static final String[] TABLE_COLUMNS = new String[] { TABLE_ID,USER_ID,NAME,USER_LEVEL,HEADER_URL};

	public static final String CREATE_TABLE = "create table if not exists " + TABLE_NAME + " ("+ 
			USER_ID +" varchar(50)," +
			NAME +" varchar(50)," +
			USER_LEVEL +" integer DEFAULT 0," +
			HEADER_URL +" text,constraint "+TABLE_ID+" primary key ("+USER_ID+"))"; 

	/**
	 * 更改我的信息
	 * @param database
	 * @param userId
	 * @param userLevel
	 * @param name 可以为null
	 * @param headerUrl 可以为null
	 * @return
	 */
	public static long saveuOrUpateUserInfo(TaoJinLuDatabase database , long userId, int userLevel, String name, String headerUrl) {
		if (userId == 0 || database == null) return 0;
		SQLiteDatabase db = database.getDbOppType(true);
		ContentValues values = new ContentValues();
		values.put(USER_ID, userId);
		if(name != null || !"".equals(name) )values.put(NAME, name);
		values.put(USER_LEVEL, userLevel);
		if(headerUrl != null || !"".equals(headerUrl) )values.put(HEADER_URL, headerUrl);
		return db.replace(TABLE_NAME, null, values);
	}

	public static long saveuOrUpateUserInfo(SQLiteDatabase db ,long userId,int userLevel,String name,String headerUrl) {
		if (userId == 0 || db == null) return 0;
		ContentValues values = new ContentValues();
		values.put(USER_ID, userId);
		if(name != null || !"".equals(name) )values.put(NAME, name);
		values.put(USER_LEVEL, userLevel);
		if(headerUrl != null || !"".equals(headerUrl) )values.put(HEADER_URL, headerUrl);
		return db.replace(TABLE_NAME, null, values);
	}
	
	public static User convertToUser(Cursor cursor) {
		try {
			if (null == cursor) return null;
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) return null;
			User user=new User();
			user.setUserId(cursor.getLong(cursor.getColumnIndex(USER_ID)));
			user.setUserName(cursor.getString(cursor.getColumnIndex(NAME)));
			user.setHeadUrl(cursor.getString(cursor.getColumnIndex(HEADER_URL)));
//			user.setIsVip(cursor.getInt(cursor.getColumnIndex(USER_LEVEL)));
			return user;
		} catch (Exception e) {
		} finally {
			if (cursor != null) cursor.close();
		}
		return null;
	}

	/**
	 * @param cursor
	 * @return
	 */
	public static Group<User> convertToGroupUser(Cursor cursor){
		try {
			if(null == cursor)return null;
			int resultCounts = cursor.getCount();
			if(resultCounts ==0 || !cursor.moveToFirst())return null;
			Group<User> group=new Group<User>();
			for (int i = 0; i <cursor.getCount() ; i++) {
				User user = new User();
				user.setUserId(cursor.getLong(cursor.getColumnIndex(USER_ID)));
				user.setUserName(cursor.getString(cursor.getColumnIndex(NAME)));
				user.setHeadUrl(cursor.getString(cursor.getColumnIndex(HEADER_URL)));
//				user.setIsVip(cursor.getInt(cursor.getColumnIndex(USER_LEVEL)));
				group.add(user);
				cursor.moveToNext();
			}
			return group;
		} catch (Exception e) {
		}finally{
			if(cursor != null)cursor.close();
		}
		return null;
	}

	/**
	 * @param cursor
	 * @return
	 */
	public static Map<String,User> convertToMapUser(Cursor cursor){
		try {
			if(null == cursor)return null;
			int resultCounts = cursor.getCount();
			if(resultCounts ==0 || !cursor.moveToFirst())return null;
			Map<String,User> map=new HashMap<String,User>();
			for (int i = 0; i <cursor.getCount() ; i++) {
				User user = new User();
				user.setUserId(cursor.getLong(cursor.getColumnIndex(USER_ID)));
				user.setUserName(cursor.getString(cursor.getColumnIndex(NAME)));
				user.setHeadUrl(cursor.getString(cursor.getColumnIndex(HEADER_URL)));
//				user.setIsVip(cursor.getInt(cursor.getColumnIndex(USER_LEVEL)));
				map.put(String.valueOf(user.getUserId()),user);
				cursor.moveToNext();
			}
			return map;
		} catch (Exception e) {
		}finally{
			if(cursor != null)cursor.close();
		}
		return null;
	}
}
