package com.procoin.social.util;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 這個是主要是用來跳转json时用的 大部分的方法就是用来
 * 
 * @author zhengmj
 * 
 */
public class VeJson {

	/**
	 * 头条
	 * @param articleId
	 * @param relType
	 * @return
	 */
	public static String getJsonRelId(String articleId, String relType) {
		try {
			return new JSONObject().put("relId", articleId).put("relType", relType).toString();
		} catch (JSONException e) {
		}
		return null;
	}

	/**
	 * 报纸 加上书名号
	 * 
	 * @param articleId
	 * @param relType
	 * @return
	 */
	public static String setArticleTitle(String articleTitle) {
		if (articleTitle != null && !articleTitle.trim().startsWith("《")) {
			return "《" + articleTitle.trim() + "》";
		} else {
			return null;
		}
	}

	/**
	 * 微访谈  需要talkId和issueId  才能跳转到微访谈
	 *
	 *
	 * @param talkId
	 * @param issueId
	 * @return
	 */
	public static String setMicrointerviewsParams(String talkId,String issueId) {

		try {
			return new JSONObject().put("talkId", talkId).put("issueId", issueId).toString();
		} catch (JSONException e) {
		}
		return "";
	}

}
