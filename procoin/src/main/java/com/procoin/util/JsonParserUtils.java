package com.procoin.util;

import com.procoin.common.constant.CommonConst;

import org.json.JSONException;
import org.json.JSONObject;


public class JsonParserUtils {

	/**
	 * json中是否有该字段,并且该字段不为空
	 * 
	 * @param json
	 * @param name
	 *            字段名
	 * @return
	 * @throws JSONException
	 */
	public static boolean hasAndNotNull(JSONObject json, String name) throws JSONException {
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
	public static boolean hasNotNullAndIsIntOrLong(JSONObject json, String name) throws JSONException {
		if (json != null && name != null) {
			return json.has(name) && !json.isNull(name) && //
					json.getString(name) != null && !"".equals(json.getString(name)) && //
					json.getString(name).matches(CommonConst.INTMATCHES);
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
	public static boolean hasNotNullAndIsDouble(JSONObject json, String name) throws JSONException {
		if (json != null && name != null) {
			return json.has(name) && !json.isNull(name) && //
					json.getString(name) != null && !"".equals(json.getString(name)) && //
					json.getString(name).matches(CommonConst.FLOATMATCHES);
		} else return false;
	}

}
