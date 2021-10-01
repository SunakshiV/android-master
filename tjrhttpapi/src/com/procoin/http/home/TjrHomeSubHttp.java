//package com.cropyme.http.home;
//
//import com.cropyme.http.TjrBaseApi;
//import com.cropyme.http.HttpApi;
//import com.cropyme.http.error.TaojinluException;
//
//import org.apache.http.message.BasicNameValuePair;
//
//import java.io.IOException;
//
//public class TjrHomeSubHttp {
//    private static TjrHomeSubHttp instance;
//    private final HttpApi mHttpApi;
//    private final String mApiSub1Uri; //主页消息
//    private final String mApiSub2Uri; // 主要对消息接口
//    private final String mApiSub3Uri; // 主要对键盘数据
//    private final String mApiSub4Uri; // 主要对键盘数据
//
//    private TjrHomeSubHttp() {
//        mHttpApi = new HttpApi();
//        mApiSub1Uri = TjrBaseApi.mApiSub1Uri.uri();
//        mApiSub2Uri = TjrBaseApi.mApiSub2Uri.uri();
//        mApiSub3Uri = TjrBaseApi.mApiSub3Uri.uri();
//        mApiSub4Uri = TjrBaseApi.mApiSub4Uri.uri();
//    }
//
//    public static TjrHomeSubHttp getInstance() {
//        if (instance == null) {
//            synchronized (TjrHomeSubHttp.class) {
//                if (instance == null) instance = new TjrHomeSubHttp();
//            }
//        }
//        return instance;
//    }
//
////    public TjrHomeSubHttpApi getTjrHomeSubHttpApi() {
////        return tjrHomeSubHttpApi;
////    }
////
////
////    public String queryHomeList(Long userId, int updown, String msgTime) throws TaojinluException, IOException {
////        return getTjrHomeSubHttpApi().queryHomeList(userId, updown, msgTime);
////    }
////
////    /**
////     */
////    public String unSubscribe(Long userId, String msgType, int msgLevel, String params, int subType) throws TaojinluException, IOException {
////        return getTjrHomeSubHttpApi().unSubscribe(userId, msgType, msgLevel, params, subType);
////    }
////
////
////    public String queryRecommendList(Long userId, int updown, String msgTime) throws TaojinluException, IOException {
////        return getTjrHomeSubHttpApi().queryRecommendList(userId, updown, msgTime);
////    }
////
////    /**
////     */
////    public String subscribe(Long userId, String msgType, int msgLevel, String params, int subType) throws TaojinluException, IOException {
////        return getTjrHomeSubHttpApi().subscribe(userId, msgType, msgLevel, params, subType);
////    }
//
//
//    public String queryHomeList(Long userId, int updown, String msgTime) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "queryHomeList"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("updown", String.valueOf(updown)),//
//                new BasicNameValuePair("msgTime", msgTime));
//    }
//
//    public String queryRecommendList(Long userId, int updown, String msgTime) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "queryRecommendList"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("updown", String.valueOf(updown)),//
//                new BasicNameValuePair("msgTime", msgTime));
//    }
//
//
//    public String subscribe(Long userId, String msgType, int msgLevel, String params, int subType) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "subscribe"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("msgType", msgType),//
//                new BasicNameValuePair("msgLevel", String.valueOf(msgLevel)),//
//                new BasicNameValuePair("params", params),
//                new BasicNameValuePair("subType", String.valueOf(subType)));
//    }
//
//    public String unSubscribe(Long userId, String msgType, int msgLevel, String params, int subType) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "unSubscribe"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("msgType", msgType),//
//                new BasicNameValuePair("msgLevel", String.valueOf(msgLevel)),//
//                new BasicNameValuePair("params", params), new BasicNameValuePair("subType", String.valueOf(subType)));
//    }
//
//
//    //	/**
////	 * 新版首页
////	 */
////	public String getHomeSubList(Long userId, int updown,String msgTime) throws TaojinluException, IOException {
////		return getTjrHomeSubHttpApi().getHomeSubList(userId, updown,msgTime);
////	}
//    //	public String getRecommendList(Long userId, int updown,String msgTime) throws TaojinluException, IOException {
////		return getTjrHomeSubHttpApi().getRecommendList(userId, updown,msgTime);
////	}
//
//
//    /********************************Msg开始 ********************/
//
//
//    /**
//     * 获取关于我的消息，好友动态，私聊是否有新 (已改)
//     *
//     * @param userId
//     * @param myMsgTime
//     * @param myDyTime
//     * @param chatTopicAndTime 格式：21,20110808080808;3,20110808080808
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String getMsgCountIsNewFromServer(Long userId, String myMsgTime, String myDyTime, String chatTopicAndTime, String msgTime, String squareTime, String squareAtTime) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
//                new BasicNameValuePair("method", "getMsgIsNew"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("myMsgTime", myMsgTime), //
//                new BasicNameValuePair("myDyTime", myDyTime),//
//                new BasicNameValuePair("chatTopicAndTime", chatTopicAndTime), //
//                new BasicNameValuePair("squareTime", squareTime), //
//                new BasicNameValuePair("squareAtTime", squareAtTime),//
//                new BasicNameValuePair("msgTime", msgTime));//
//    }
//
//    /**
//     * 修改了方式 （已改）
//     * 获取属于我的查询股票记录
//     *
//     * @return
//     */
//    public String getQueryStockByUserId(String userId, String msgTime, String targetUid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
//                new BasicNameValuePair("method", "getQueryStockByUserId"),//
//                new BasicNameValuePair("targetUid", targetUid),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("msgTime", msgTime));//
//    }
//
//    /**
//     * 我的消息  (已改)
//     *
//     * @return
//     */
//    public String getAllListMsgFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
//                new BasicNameValuePair("method", "findMyMsgAllList"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("msgTime", msgTime));//
//    }
//
//    /**
//     * 好友圈 (已改)
//     **/
//    // 获取最新好友圈数据
//    public String findMyFriendsCircleList(long userId, String msgTime, String reqCode, long targetUid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
//                new BasicNameValuePair("method", "findMyFriendsCircleList"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("msgTime", msgTime),//
//                new BasicNameValuePair("targetUid", String.valueOf(targetUid)),//
//                new BasicNameValuePair("reqCode", reqCode));//
//    }
//
//    // 获取更多好友圈历史数据  (已改)
//    public String findMyFriendsCircleMoreList(long userId, String msgTime, String reqCode, long targetUid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
//                new BasicNameValuePair("method", "findMyFriendsCircleMoreList"),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("targetUid", String.valueOf(targetUid)),//
//                new BasicNameValuePair("msgTime", msgTime), //
//                new BasicNameValuePair("reqCode", reqCode));//
//    }
//
//    /**
//     * 获取更多 我的消息  //getMoreMyMsg===findMoreMyMsgHis  （已改）
//     *
//     * @return
//     */
//    public String getMoreMsgFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
//                new BasicNameValuePair("method", "findMoreMyMsgHis"),//
//                new BasicNameValuePair("msgTime", msgTime),//
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
//
////    /**
////     * home主页面 消息
////     */
////    public String getHomeNewListFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiMSgUrl, //
////                new BasicNameValuePair("method", "getHomeNewList"),//
////                new BasicNameValuePair("userId", String.valueOf(userId)),//
////                new BasicNameValuePair("msgTime", msgTime));//
////    }
//
////    /**
////     * home 加载更多 历史主页面 消息
////     *
////     * @return
////     */
////    public String getHisHomeNewListFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiMSgUrl, //
////                new BasicNameValuePair("method", "getMoreHisHomeList"),//
////                new BasicNameValuePair("userId", String.valueOf(userId)),//
////                new BasicNameValuePair("msgTime", msgTime));//
////    }
//
////    /**
////     * 我的好友动态消息获取
////     *
////     * @return
////     */
////    public String getAllMsgDynamicFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiMSgUrl, //
////                new BasicNameValuePair("method", "getMyMsgDynamicAllList"),//
////                new BasicNameValuePair("userId", String.valueOf(userId)),//
////                new BasicNameValuePair("msgTime", msgTime));// (好友动态)根据好友搜索股票的记录，生成的好友动态
////    }
//
////    /**
////     * 获取更多 我的好友动态消息获取
////     *
////     * @return
////     */
////    public String getMoreMsgDynamicFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiMSgUrl, //
////                new BasicNameValuePair("getMoreMyMsgDynamic", userId + "~" + msgTime));//
////    }
//
////    /**
////     * 获取个人动态消息
////     *
////     * @return
////     */
////    public String getMsgDynamicByUserIdFromServer(Long userId, String msgTime) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiMSgUrl, //
////                new BasicNameValuePair("getMsgDynamicByUserId", userId + "~" + msgTime));//
////    }
//
//
//    /**
//     * 获取属于我的删除或添加股票记录
//     *
//     * @return
//     */
//    public String getAddOrDelStockByUserId(String userId, String msgTime, String targetUid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub2Uri, //
////                new BasicNameValuePair("getAddOrDelStockByUserId", userId + "~" + msgTime));//
//                new BasicNameValuePair("method", "getAddOrDelStockByUserId"),//
//                new BasicNameValuePair("targetUid", targetUid),//
//                new BasicNameValuePair("userId", String.valueOf(userId)),//
//                new BasicNameValuePair("msgTime", msgTime));//
//
//    }
//
//
//    /********************************Msg结束 ********************/
//
//
//    /***************     ********************/
//
//
//    /**
//     * 获取某个应用信息 getAppInfo=com.cropyme (已改)
//     */
//    public String getClientInfo(String type, String version, String pkg, String placardTime, String apiCallType) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub3Uri + "client_info/", //
//                new BasicNameValuePair("method", "getClientInfo"),//
//                new BasicNameValuePair("type", type),//
//                new BasicNameValuePair("pkg", pkg),//
//                new BasicNameValuePair("version", version),//
//                new BasicNameValuePair("apiCallType", apiCallType),//
//                new BasicNameValuePair("placardTime", placardTime));
//    }
//
//
//    /**
//     * 上传我查找过的自选股 （已改）
//     *
//     * @param stocks 如sh600001,sh600004
//     */
//    public String addSearchStock(String stocks, String userId) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub3Uri, //
//                new BasicNameValuePair("method", "addSearchStock"),
//                new BasicNameValuePair("fdms", stocks),
//                new BasicNameValuePair("userId", userId));
//    }
//
//
//    /**
//     * 好友的股票价格
//     *
//     * @param userId 自己的Id  (已改)
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String findMyFrStockIndex(long userId, long targetUid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub3Uri, //
//                new BasicNameValuePair("method", "findMyFrStockIndex"),
//                new BasicNameValuePair("targetUid", String.valueOf(targetUid)),
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
//    /**
//     * 获取个人的指数 （已改）
//     *
//     * @param userId
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String getMyStockIndex(String userId, String targetUid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub3Uri, //
//                new BasicNameValuePair("method", "getMyStockIndex"),
//                new BasicNameValuePair("targetUid", String.valueOf(targetUid)),
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
//
//    /**
//     * 上传信息 home页面的第二个广告信息
//     *
//     * @param userId
//     * @param type   0  首页点击   1 6个广告位
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String submitTabTwoOnClick(String userId, String modelId, int type) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "submitTabTwoOnClick"),
//                new BasicNameValuePair("modelId", String.valueOf(modelId)),
//                new BasicNameValuePair("type", String.valueOf(type)),
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
//    /**
//     * 获取广告信息
//     *
//     * @param userId
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String getBannerImage(String userId) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "getBannerImage"),
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
//    /**
//     * 新版首页卡片接口
//     * 这个接口需要的东西
//     * （devicew：设备宽度， deviceh：设备高度） android( imei：手机固件识别码， mac：手机mac地址， androidid：android	id)
//     *
//     * @param userId
//     * @return
//     * @throws TaojinluException
//     * @throws IOException
//     */
//    public String queryHomeSub(String userId, int isReset, String devicew, String deviceh, String imei, String mac, String androidid) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub1Uri, //
//                new BasicNameValuePair("method", "queryHomeSub"),
//                new BasicNameValuePair("devicew", devicew),
//                new BasicNameValuePair("deviceh", deviceh),
//                new BasicNameValuePair("imei", imei),
//                new BasicNameValuePair("mac", mac),
//                new BasicNameValuePair("androidid", androidid),
//                new BasicNameValuePair("isReset", String.valueOf(isReset)),
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
//    /**
//     * 键盘获取最hot的股票
//     * <p>
//     * 192.168.0.223:8883/v1/h83/?method=findHotStock&userId=156
//     */
//    public String findHotStock(String userId) throws TaojinluException, IOException {
//        return mHttpApi.doHttpGet(mApiSub4Uri, //
//                new BasicNameValuePair("method", "findHotStock"),
//                new BasicNameValuePair("userId", String.valueOf(userId)));
//    }
//
////    /**
////     * 获取广告信息
////     *
////     * @param userId
////     * @param type     type=home(首页)\paper(报纸)\timeline(日程表)\radio(微电台)\square(股友吧)
////     * @return
////     * @throws TaojinluException
////     * @throws IOException
////     */
////    public String getBannerImage(String userId, String type) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiSub1Uri, //
////                new BasicNameValuePair("method", "getBannerImage"),
////                new BasicNameValuePair("userId", String.valueOf(userId)),
////                new BasicNameValuePair("type", type));
////    }
//
//
////    /**
////     * 发送
////     */
////    public String sendTjrAuthTask(String info, String aid, String sid) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiKeyboard, //
////                new BasicNameValuePair("method", "sendTjrAuth"),//
////                new BasicNameValuePair("info", info), new BasicNameValuePair("aid", aid), new BasicNameValuePair("sid", sid));//
////    }
////    /**
////     * 获取某个应用信息 getAppInfo=com.cropyme
////     */
////    public String getAppInfo(String pkg) throws TaojinluException, IOException {
////        return mHttpApi.doHttpGet(mApiKeyboard, new BasicNameValuePair("getAppInfo", pkg));
////    }
//
//    /************        *******************/
//
//}
