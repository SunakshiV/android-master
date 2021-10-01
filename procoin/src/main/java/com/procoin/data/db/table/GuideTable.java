package com.procoin.data.db.table;

import android.content.ContentValues;
import android.database.Cursor;

public class GuideTable {
	public static final String TABLE_NAME = "guide";
	public static final String ID = "id";
	public static final String GUIDE_KEY = "guide_key";
	public static final String VALUE = "value";
	public static final String CREATE_TABLE = "create table " + TABLE_NAME + " (" + ID + " integer primary key autoincrement," + GUIDE_KEY + " int, " + VALUE + " int" + ")";

	public static ContentValues getContentValues(int key, int value) {
		ContentValues values = new ContentValues();
		values.put(GUIDE_KEY, key);
		values.put(VALUE, value);
		return values;
	}

	public static String queryGuide() {
		return "select * from " + TABLE_NAME + " order by " + GUIDE_KEY + " asc";
	}

	public static int[] getGuideValue(Cursor cursor) {
		try {
			if (null == cursor) return null;
			if (!cursor.moveToFirst()) return null;
			int[] value = new int[cursor.getCount()];
			int index = 0;
			do {
				value[index] = cursor.getInt(cursor.getColumnIndex(VALUE));
				index++;
			} while (cursor.moveToNext());
			return value;
		} catch (Exception e) {
			return null;
		} finally {
			if (cursor != null) cursor.close();
		}
	}

}
