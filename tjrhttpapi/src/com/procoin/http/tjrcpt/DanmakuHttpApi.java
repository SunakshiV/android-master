//package com.cropyme.http.tjrcpt;
//
//import java.io.IOException;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import com.cropyme.http.HttpApi;
//import com.cropyme.http.TjrBaseApi;
//import com.cropyme.http.error.TaojinluException;
//
//public class DanmakuHttpApi {
//	private static DanmakuHttpApi instance;
//	private final HttpApi mHttpApi;
//	private final String mApiDanmakuUrl; //
//	private final String danmakuUrlRoot = "/";
//	private final String url_say_do = "tjrdanmaku/say.do";
//
//	private DanmakuHttpApi() {
//		mHttpApi = new HttpApi();
//		// TODO 记得加:7730
//		mApiDanmakuUrl = "http://" + TjrBaseApi.dammakuUri.uri() + ":7730/";
//	}
//
//	public static DanmakuHttpApi getInstance() {
//		if (instance == null) {
//			synchronized (TjrStockHttp.class) {
//				if (instance == null) instance = new DanmakuHttpApi();
//			}
//		}
//		return instance;
//	}
//
//	public HttpApi getmHttpApi() {
//		return mHttpApi;
//	}
//
//	/**
//	 * 订阅
//	 *
//	 * @param userId
//	 * @param fdm
//	 * @return
//	 * @throws Exception
//	 */
//	public String sub(String userId, String fdm) {
//		return mHttpApi.createUrl(danmakuUrlRoot,//
//				new BasicNameValuePair("method", "sub"),//
//				new BasicNameValuePair("userId", userId),//
//				new BasicNameValuePair("fdm", fdm));
//	}
//
//	/**
//	 * 发送
//	 *
//	 * @param userId
//	 * @param fdm
//	 * @param say
//	 * @return
//	 * @throws Exception
//	 */
//	public String send(String userId, String fdm, String say) {
//		return mHttpApi.createUrl(danmakuUrlRoot,//
//				new BasicNameValuePair("method", "send"),//
//				new BasicNameValuePair("userId", userId), //
//				new BasicNameValuePair("fdm", fdm),//
//				new BasicNameValuePair("say", say));
//	}
//
//	/**
//	 *
//	 * @param userId
//	 * @param fdm
//	 * @param say
//	 * @param updown
//	 *            0 上拉刷新， 1 下拉刷新
//	 * @param time
//	 * @param id
//	 * @param date
//	 * @return
//	 * @throws IOException
//	 * @throws TaojinluException
//	 */
//	public String findSayRecord(String userId, String fdm, String updown, String time, String id, String date) throws TaojinluException, IOException {
//		return mHttpApi.doHttpPost(mApiDanmakuUrl + url_say_do, //
//				new BasicNameValuePair("method", "findSayRecord"),//
//				new BasicNameValuePair("userId", userId),//
//				new BasicNameValuePair("fdm", fdm),//
//				// &date=0&id=0&time=0&updown=0
//				new BasicNameValuePair("updown", updown),//
//				new BasicNameValuePair("time", time),//
//				new BasicNameValuePair("id", id),//
//				new BasicNameValuePair("date", date));
//	}
//
//}
