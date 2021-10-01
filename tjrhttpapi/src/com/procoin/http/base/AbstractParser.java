package com.procoin.http.base;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractParser<T extends TaojinluType> implements Parser<T> {
	private static final String INTMATCHES = "[-]?[0-9E]+$";// int型匹配
	private static final String FLOATMATCHES = "[-]?[0-9.E]+$";// 小数型匹配
	/**
	 * All derived parsers must implement parsing a JSONObject instance of
	 * themselves.
	 */
	public abstract T parse(JSONObject json) throws JSONException;

	public Group<T> parseGroup(JSONArray array) throws JSONException{
		if (array == null) return null;
		Group<T> group = new Group<>();
		for (int i = 0; i < array.length(); i++) {
			group.add(parse(array.getJSONObject(i)));
		}
		return group;
	}

	/**
	 * json中是否有该字段,并且该字段不为空
	 * 
	 * @param json
	 * @param name
	 *            字段名
	 * @return
	 * @throws JSONException
	 */
	public boolean hasAndNotNull(JSONObject json, String name) throws JSONException {
		if (json != null && name != null) {
			return json.has(name) && !json.isNull(name) && //
					json.getString(name) != null && !"".equals(json.getString(name));
		} else return false;
	}

	/**
	 * json中是否有该字段,该字段不为空,并且为全数字 Long 类型可以使用
	 * 
	 * @param json
	 * @param name
	 *            字段名
	 * @return
	 * @throws JSONException
	 */
	public boolean hasNotNullAndIsIntOrLong(JSONObject json, String name) throws JSONException {
		if (json != null && name != null) {
			return json.has(name) && !json.isNull(name) && //
					json.getString(name) != null && !"".equals(json.getString(name)) && //
					json.getString(name).matches(INTMATCHES);
		} else return false;
	}

	/**
	 * json中是否有该字段,该字段不为空,并且为小数
	 * 
	 * @param json
	 * @param name
	 *            字段名
	 * @return
	 * @throws JSONException
	 */
	public boolean hasNotNullAndIsDouble(JSONObject json, String name) throws JSONException {
		if (json != null && name != null) {
			return json.has(name) && !json.isNull(name) && //
					json.getString(name) != null && !"".equals(json.getString(name)) && //
					json.getString(name).matches(FLOATMATCHES);
		} else return false;
	}
}