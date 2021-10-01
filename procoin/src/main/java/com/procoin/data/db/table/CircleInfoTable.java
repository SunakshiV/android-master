package com.procoin.data.db.table;

import android.database.Cursor;
import android.util.Log;

import com.procoin.http.base.Group;
import com.procoin.module.circle.entity.CircleInfo;

/**
 * 圈子详细信息
 */
public class CircleInfoTable {
	
	public static final String TABLENAME="circle_info";

	public static final String CIRCLE_ID="circle_id";
	public static final String CIRCLE_NAME="circle_name";
	public static final String CIRCLE_LOGO="circle_logo";
	public static final String CREATETIME="create_time";
	public static final String SYN_MARK="syn_mark";//同步标记

	public static final String KEYID="key_id";


	public static final String CREATETABLE = "create table if not exists " + TABLENAME +" ("
			+CIRCLE_ID +" varchar(16),"
			+CIRCLE_NAME+" varchar(32),"
			+CIRCLE_LOGO+" varchar(256),"
			+CREATETIME+" varchar(16),"
			+SYN_MARK+" int DEFAULT 0 ,"
			+"constraint "+KEYID+" primary key("+CIRCLE_ID+"))";


	/**
	 * @param cursor
	 * @return
	 */
	public static Group<CircleInfo> convertToCircleInfo(Cursor cursor) {
		try {
			if (null == cursor) return null;
			Log.d("CircleInfoTable","11111");
			int resultCounts = cursor.getCount();
			Log.d("CircleInfoTable","22222222   resultCounts=="+resultCounts);
			if (resultCounts == 0 || !cursor.moveToFirst()) return null;
			Log.d("CircleInfoTable","33333333");
			CircleInfo circleInfo = null;
			Group<CircleInfo> group = new Group<CircleInfo>();
			for (int i = 0; i < resultCounts; i++) {
				circleInfo = new CircleInfo();
				circleInfo.circleId=cursor.getString(cursor.getColumnIndex(CIRCLE_ID));
				circleInfo.circleName=cursor.getString(cursor.getColumnIndex(CIRCLE_NAME));
				circleInfo.circleLogo=cursor.getString(cursor.getColumnIndex(CIRCLE_LOGO));
				circleInfo.createTime=cursor.getString(cursor.getColumnIndex(CREATETIME));
				circleInfo.synMark=cursor.getInt(cursor.getColumnIndex(SYN_MARK));

				circleInfo.say=cursor.getString(cursor.getColumnIndex(CircleChatTable.SAY));
				circleInfo.lastName=cursor.getString(cursor.getColumnIndex(UserInfoTable.NAME));
				circleInfo.recent_time=cursor.getString(cursor.getColumnIndex(CircleRelTable.RECENT_TIME));//排序依据

				Log.d("CircleInfoTable","circleInfo=="+circleInfo);
				group.add(circleInfo);
				cursor.moveToNext();
			}
			return group;
		} catch (Exception e) {
			Log.d("CircleInfoTable","Exception=="+e);
		} finally {
			if (cursor != null) cursor.close();
		}
		return null;
	}

	public static Group<CircleInfo> convertToCircleBG(Cursor cursor) {
		try {
			if (null == cursor) return null;
			int resultCounts = cursor.getCount();
			if (resultCounts == 0 || !cursor.moveToFirst()) return null;
			CircleInfo circleInfo = null;
			Group<CircleInfo> group = new Group<CircleInfo>();
			for (int i = 0; i < resultCounts; i++) {
				circleInfo = new CircleInfo();
				circleInfo.circleId=cursor.getString(cursor.getColumnIndex(CIRCLE_ID));
				circleInfo.circleName=cursor.getString(cursor.getColumnIndex(CIRCLE_NAME));
				circleInfo.circleLogo=cursor.getString(cursor.getColumnIndex(CIRCLE_LOGO));
				circleInfo.synMark=cursor.getInt(cursor.getColumnIndex(SYN_MARK));

				Log.d("CircleInfoTable","circleInfo=="+circleInfo);
				group.add(circleInfo);
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
