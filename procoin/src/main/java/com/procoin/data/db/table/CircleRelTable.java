package com.procoin.data.db.table;

import android.database.Cursor;

import com.procoin.module.circle.entity.CircleRel;

/**
 * 关系表，这个表记录我有多少个圈子
 */
public class CircleRelTable {
    public static final String TABLENAME = "circle_rel";

    public static final String CIRCLE_ID = "circle_id";
    public static final String KEYID = "key_id";
    public static final String ROLE = "role";
    public static final String MYUSERID = "my_user_id";
    public static final String RECENT_TIME = "recent_time";
    public static final String CREATETABLE = "create table if not exists " + TABLENAME + " ("
            + CIRCLE_ID + " varchar(16),"
            + ROLE + " int DEFAULT 0  ,"
            + MYUSERID + " varchar(16),"
            + RECENT_TIME + " text,"
            + "constraint " + KEYID + " primary key(" + CIRCLE_ID + "," + MYUSERID + "))";

    /**
     * @param cursor
     * @return
     */
    public static int getRole(Cursor cursor) {
        try {
            if (null == cursor) return -1;
            int resultCounts = cursor.getCount();
            if (resultCounts == 0 || !cursor.moveToFirst()) return -1;
            return cursor.getInt(cursor.getColumnIndex(CircleRelTable.ROLE));
        } catch (Exception e) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return -1;
    }


    public static CircleRel convertToCircleRel(Cursor cursor) {
        try {
            if (null == cursor) return null;
            int resultCounts = cursor.getCount();
            if (resultCounts == 0 || !cursor.moveToFirst()) return null;
            CircleRel circleRel = new CircleRel();
            circleRel.circleId = cursor.getString(cursor.getColumnIndex(CIRCLE_ID));
            circleRel.role = cursor.getInt(cursor.getColumnIndex(ROLE));
            circleRel.recentTime = cursor.getString(cursor.getColumnIndex(RECENT_TIME));
            circleRel.userId = Long.parseLong(cursor.getString(cursor.getColumnIndex(MYUSERID)));
            return circleRel;
        } catch (Exception e) {
        } finally {
            if (cursor != null) cursor.close();
        }
        return null;
    }


}
