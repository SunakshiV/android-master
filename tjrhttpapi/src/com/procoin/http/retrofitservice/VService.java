package com.procoin.http.retrofitservice;

//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;

import com.procoin.http.tjrcpt.VHttpServiceManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by zhengmj on 19-2-16.
 */

public interface VService {

    @FormUrlEncoded
    @POST(VHttpServiceManager.URL_API_HOME)
    Call<ResponseBody> homeGet(@Field("noticeTime") String notice_time);

    //广告页接口
    @POST(VHttpServiceManager.URL_API_BOOTPAGE)
    Call<ResponseBody> bootPage();

    //国家代码
    @POST(VHttpServiceManager.CONFIG_COUNTRYCODEINFOLIST)
    Call<ResponseBody> countrycodeinfolist();


    //获得节目列表
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SHOW_LIST)
    Call<ResponseBody> getShowList(@Field("circleId") long circleId, @Field("pageNo") int pageNo);

    @POST(VHttpServiceManager.V_HOME_ACCOUNT)
    Call<ResponseBody> homeAccount();


    //创建节目
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SHOW_CREATE)
    Call<ResponseBody> createShow(@Field("circleId") long circleId, @Field("title") String title, @Field("content") String content, @Field("needPay") int needPay);

    //创建社区
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_CREATE)
    Call<ResponseBody> createCommunity(@Field("circleName") String circleName, @Field("brief") String brief, @Field("circleLogo") String circleLogo);

    //编辑社区
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_UPDATE)
    Call<ResponseBody> updateCommunity(@Field("circleId") String circleId, @Field("circleName") String circleName, @Field("brief") String brief, @Field("circleLogo") String circleLogo, @Field("circleBg") String circleBg);


    //获取圈子信息
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_GET)
    Call<ResponseBody> getCircleInfo(@Field("circleId") String circleId);

    //搜索圈子
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_SEARC)
    Call<ResponseBody> searchCircle(@Field("circleId") String circleId);

    //申请进入圈子
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_APPLYJOINCIRLCE)
    Call<ResponseBody> applyJoinCirlce(@Field("circleId") String circleId, @Field("reason") String reason);

    //圈子成员列表
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_GETMEMBERLIST)
    Call<ResponseBody> getMemberList(@Field("circleId") String circleId, @Field("pageNo") int pageNo);


    //获取某个圈子的申请进圈记录
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_FINDAPPLIES)
    Call<ResponseBody> findApplies(@Field("circleId") String circleId, @Field("pageNo") int pageNo);

    //审批入圈申请 int status(-1 拒绝，1允许入圈)
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_HANDLEAPPLY)
    Call<ResponseBody> approveApply(@Field("circleId") String circleId, @Field("status") int status, @Field("applyId") long applyId);

    //设置管理员 role：0：普通用户，10：管理员
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_UPDATEROLE)
    Call<ResponseBody> updateRole(@Field("circleId") String circleId, @Field("role") int role, @Field("targetUid") long targetUid);

    //移除圈子成员
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_REMOVEMEMBER)
    Call<ResponseBody> removeMember(@Field("circleId") String circleId, @Field("targetUid") long targetUid, @Field("addToBlackList") boolean addToBlackList);

    //圈子黑名单列表
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_BLACKLIST)
    Call<ResponseBody> getBlackList(@Field("circleId") String circleId, @Field("pageNo") int pageNo);

    //拉黑/取消拉黑 int status(-1拉黑,1取消拉黑),
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_HANDLEBLACK)
    Call<ResponseBody> handleBlack(@Field("circleId") String circleId, @Field("blackUserId") long blackUserId, @Field("status") int status);

    //设置入圈方式
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_SETUPJOINMODE)
    Call<ResponseBody> setupJoinMode(@Field("circleId") String circleId, @Field("joinMode") int joinMode);

    //退出圈子
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_EXIT)
    Call<ResponseBody> exitCircle(@Field("circleId") String circleId);

    //圈子禁言
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_SETSPEAKSTATUS)
    Call<ResponseBody> setSpeakStatus(@Field("circleId") String circleId, @Field("speakStatus") int speakStatus);

    //圈子免打扰
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CIRCLE_OPP_SETMSGALERT)
    Call<ResponseBody> setMsgAlert(@Field("circleId") String circleId, @Field("msgAlert") int msgAlert);


    //节目详情
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SHOW_DETAIL)
    Call<ResponseBody> getShowDetail(@Field("circleId") long circleId, @Field("showId") long showId);

    //节目评论
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SHOW_COMMENT)
    Call<ResponseBody> getShowCommentList(@Field("circleId") long circleId, @Field("showId") long showId, @Field("pageNo") int pageNo);

    //发表节目评论
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SHOW_COMMENT_SEND)
    Call<ResponseBody> sendShowComment(@Field("circleId") long circleId, @Field("showId") long showId, @Field("atUid") long atUid, @Field("content") String content);

    //点赞或取消点赞节目
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SHOW_LIKE_OR_NOT)
    Call<ResponseBody> likeOrDisLikeShow(@Field("circleId") long circleId, @Field("showId") long showId);


    //直播间发现页
    @POST(VHttpServiceManager.V_LIVE_DISCOVER)
    Call<ResponseBody> getDiscoverList();

    //直播间列表
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_LIVE_ROOMLIST)
    Call<ResponseBody> getRoomList(@Field("roomId") long roomId, @Field("type") int type, @Field("pageNo") int pageNo);

    //直播间信息
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_LIVE_ROOMINFO)
    Call<ResponseBody> getRoomInfo(@Field("roomId") long roomId);

    //点赞或取消点赞
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_LIVE_LOD)
    Call<ResponseBody> likeOrDislikeLive(@Field("roomId") long roomId, @Field("msgId") long msgId);

    //发送直播间评论
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_LIVE_SEND)
    Call<ResponseBody> sendLiveComment(@Field("atMsgId") long atMsgId, @Field("roomId") long roomId, @Field("content") String content, @Field("fileUrl") String fileUrl);

    //编辑直播间信息
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_LIVE_EDIT)
    Call<ResponseBody> updateLiveRoom(@Field("roomId") long roomId, @Field("roomName") String roomName, @Field("roomDesc") String roomDesc);

    //删除直播间信息
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_LIVE_DELETE)
    Call<ResponseBody> deleteComment(@Field("msgId") long msgId, @Field("roomId") long roomId);


    //充币/提币 记录接口
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_COIN_LIST)
    Call<ResponseBody> getCoinHistoryList(@Field("pageNo") int pageNo);

    //充币接口
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_COIN_TOIN)
    Call<ResponseBody> coinToIn(@Field("payPass") String payPass);

    //快捷充币接口
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_COIN_TOFASTIN)
    Call<ResponseBody> coinToFastIn(@Field("payPass") String payPass);

    //提币接口
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_COIN_TOOUT)
    Call<ResponseBody> coinToOut(@Field("balance") String balance, @Field("payPass") String payPass);

    //获取我的学分
    @POST(VHttpServiceManager.USER_MYCOIN_INFO)
    Call<ResponseBody> getMyCoin();


    //获取短信验证码
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_GET_SMS)
    Call<ResponseBody> getSms(@Field("phone") String phone,
                              @Field("countryCode") String countryCode,
                              @Field("dragImgKey") String key,
                              @Field("locationx") int locationx);


    //设置支付密码
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_SETPAYPASS)
    Call<ResponseBody> setPayPass(@Field("oldPhone") String phone, @Field("oldSmsCode") String smsCode, @Field("dragImgKey") String dragImgKey, @Field("locationx") int locationx, @Field("payPass") String payPass, @Field("configPayPass") String configPayPass);

    //更新用户信息
    @FormUrlEncoded
    @POST(VHttpServiceManager.USER_UPDATE_INFO)
    Call<ResponseBody> updateUserInfo(@Field("userName") String userName,
                                      @Field("sex") String sex,
                                      @Field("birthday") String birthday,
                                      @Field("describes") String describes,
                                      @Field("headUrl") String headUrl,
                                      @Field("bgUrl") String bgUrl);

    //首页活动页
    @POST(VHttpServiceManager.V_HOME_ACTIVITY)
    Call<ResponseBody> getActivityList();

    //首页活动页签到
    @POST(VHttpServiceManager.V_ACTIVITY_SIGN)
    Call<ResponseBody> doSign();

    //获取我的消息
    @FormUrlEncoded
    @POST(VHttpServiceManager.MESSAGE_FIND)
    Call<ResponseBody> findMyMessageList(@Field("pageNo") int pageNo);


    //修改密码
    @FormUrlEncoded
    @POST(VHttpServiceManager.USER_UPDATE_USERPASS)
    Call<ResponseBody> updatePass(@Field("oldUserPass") String oldUserPass, @Field("newUserPass") String newUserPass, @Field("configUserPass") String configUserPass);

    //退出登录
    @POST(VHttpServiceManager.V_LOGOUT)
    Call<ResponseBody> doLogout();

    //修改手机
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CHANGE_PHONE)
    Call<ResponseBody> changePhone(@Field("newCountryCode") String newCountryCode,
                                   @Field("newPhone") String newPhone,
                                   @Field("newSmsCode") String newSmsCode,
                                   @Field("oldPhone") String oldPhone,
                                   @Field("oldSmsCode") String oldSmsCode,
                                   @Field("dragImgKey") String dragImgKet,
                                   @Field("locationx") int locationx);

    //验证身份
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_CHECKIDENTITY)
    Call<ResponseBody> checkIdentity(@Field("phone") String phone, @Field("smsCode") String smsCode, @Field("dragImgKey") String dragImgKey, @Field("locationx") int locationx);

    //意见反馈获取小秘书chatTopic
    @POST(VHttpServiceManager.V_CHAT_GETSERVICECHATTOPIC)
    Call<ResponseBody> createChatTopic();

    //个人主页
    @FormUrlEncoded
    @POST(VHttpServiceManager.V_USER_HOMEPAGE)
    Call<ResponseBody> homepage(@Field("targetUid") long targetUid);


    //获取图片验证码
    @POST(VHttpServiceManager.DRAG_IMG)
    Call<ResponseBody> getDragImg();

    //验证图片验证码
    @FormUrlEncoded
    @POST(VHttpServiceManager.CHECK_DRAG)
    Call<ResponseBody> checkDrag(@Field("dragImgKey") String dragImgKey, @Field("locationx") int locationx);

    //获取实名认证信息
    @POST(VHttpServiceManager.IDENTITY_GET)
    Call<ResponseBody> getIdentityAuthen();

    //提交实名认证
    @FormUrlEncoded
    @POST(VHttpServiceManager.IDENTITY_SUBMIT)
    Call<ResponseBody> submitIdentityAuthen(@Field("name") String name, @Field("certNo") String certNo, @Field("frontImgUrl") String frontImgUrl, @Field("backImgUrl") String backImgUrl);

    //登录
    @FormUrlEncoded
    @POST(VHttpServiceManager.LOGIN)
    Call<ResponseBody> doLogin(@Field("phone") String phone,
                               @Field("userPass") String pass,
                               @Field("smsCode") String smsCode,
                               @Field("dragImgKey") String dragImgKey,
                               @Field("locationx") int locationx);

    //注册
    @FormUrlEncoded
    @POST(VHttpServiceManager.REGISTE)
    Call<ResponseBody> doRegiste(@Field("phone") String phone,
                                 @Field("countryCode") String countryCode,
                                 @Field("userName") String userName,
                                 @Field("userPass") String userPass,
                                 @Field("configUserPass") String configUserPass,
                                 @Field("inviteCode") String inviteCode,
                                 @Field("headUrl") String headUrl,
                                 @Field("sex") int sex,
                                 @Field("describes") String describes,
                                 @Field("smsCode") String smsCode,
                                 @Field("dragImgKey") String dragImgKey,
                                 @Field("locationx") int locationx);

    //找回密码
    @FormUrlEncoded
    @POST(VHttpServiceManager.RECEIVE)
    Call<ResponseBody> doReceive(@Field("phone") String phone,
                                 @Field("smsCode") String smsCode,
                                 @Field("userPass") String userPass,
                                 @Field("dragImgKey") String dragImgKey,
                                 @Field("locationx") int locationx);

//    //分享接口，shareType = 1(圈子分享),2(直播分享);1 -> params = circleId,2 -> params = roomId;
//    @FormUrlEncoded
//    @POST(VHttpServiceManager.SHARE_GETSHAREINFO)
//    Call<ResponseBody> getShareInfo(@Field("shareType") int shareType, @Field("params") String params);

    ///////////////////////////////////////////下面是新的

    //关注页
    @POST(VHttpServiceManager.FOLLOW)
    Call<ResponseBody> follow();

    //行情
    @POST(VHttpServiceManager.MARKET)
    Call<ResponseBody> market();

    //CROPYME页
    @POST(VHttpServiceManager.CROPYME)
    Call<ResponseBody> cropyme();

    //获取USDT汇率、随机尾数  有些地方需要穿targetUserid
    @FormUrlEncoded
    @POST(VHttpServiceManager.CONFIG)
    Call<ResponseBody> tradeConfig(@Field("symbol") String symbol, @Field("targetUid") String targetUid);

    //获取USDT汇率、随机尾数,这个是多了一个参数,表示持有usdt数量返回的小数点数量,上面方法就不传默认是8位
    @FormUrlEncoded
    @POST(VHttpServiceManager.CONFIG)
    Call<ResponseBody> tradeConfig(@Field("symbol") String symbol, @Field("usdtDecimals") int usdtDecimals);


    ///////////下面是充值提现

//    /**
//     * 充值/提现
//     *
//     * @param entrustAmount 充值：人民币金额，提现：USDT数量
//     * @param receiptId     获取随机值返回的id，后台用于区分
//     * @param inOut         1：充值，-1：提现
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(VHttpServiceManager.INOUTCREATE)
//    Call<ResponseBody> inoutCreate(@Field("entrustAmount") String entrustAmount, @Field("receiptId") long receiptId, @Field("inOut") int inOut);

    /**
     * 用户标记付款
     *
     * @param orderCashId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.INOUTMARKPAY)
    Call<ResponseBody> inoutMarkPay(@Field("orderCashId") long orderCashId);

    @FormUrlEncoded
    @POST(VHttpServiceManager.ORDERCANCEL)
    Call<ResponseBody> orderCancel(@Field("orderCashId") long orderCashId);


    /**
     * 用户撤销订单
     *
     * @param orderCashId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.INOUTCANCEL)
    Call<ResponseBody> inoutCancel(@Field("orderCashId") long orderCashId);

    /**
     * 服务人员放币(充值)/服务人员确认打款(提现)
     *
     * @param inOutId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.INOUTADMINDONE)
    Call<ResponseBody> inoutAdmindone(@Field("inOutId") long inOutId);

    /**
     * 用户未付款，服务人员撤销订单
     *
     * @param inOutId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.INOUTADMINUNPAYCANCEL)
    Call<ResponseBody> inoutAdminUnpaycancel(@Field("inOutId") long inOutId);


    ///////////下面是收款方式

    /**
     * 用户添加收款方式，获取支持的收款方式
     *
     * @return
     */
    @POST(VHttpServiceManager.RECEIPTRECEIPTSFORADD)
    Call<ResponseBody> receiptsForAdd();

    /**
     * 获取已添加的收款方式
     *
     * @return
     */
    @POST(VHttpServiceManager.RECEIPTMYRECEIPTS)
    Call<ResponseBody> myReceipts();


    /**
     * 用户买卖币，获取平台的收款方式
     *
     * @return
     */
    @POST(VHttpServiceManager.RECEIPTRECEIPTSFORPAY)
    Call<ResponseBody> receiptsForPay();


    /**
     * 添加/修改收款方式
     *
     * @param receiptType
     * @param receiptId   添加时填0
     * @param receiptName
     * @param receiptNo
     * @param bankName
     * @param bankBranch
     * @param qrCodeUrl
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.RECEIPTSAVERECEIPT)
    Call<ResponseBody> saveReceipt(@Field("receiptType") int receiptType, @Field("receiptId") long receiptId, @Field("receiptName") String receiptName, @Field("receiptNo") String receiptNo, @Field("bankName") String bankName, @Field("bankBranch") String bankBranch, @Field("qrCodeUrl") String qrCodeUrl, @Field("payPass") String payPass);

    /**
     * 删除收款方式
     *
     * @param receiptId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.RECEIPTDELETE)
    Call<ResponseBody> receiptDelete(@Field("receiptId") long receiptId, @Field("payPass") String payPass);


    /**
     * 获取默认收款方式
     *
     * @return
     */
    @POST(VHttpServiceManager.RECEIPTGETDEFAULT)
    Call<ResponseBody> receiptGetDefault();

    @FormUrlEncoded
    @POST(VHttpServiceManager.RECEIPTSETDEFAULT)
    Call<ResponseBody> receiptSetDefault(@Field("receiptId") long receiptId);


    /**
     * 下单（余额充足）
     *
     * @param symbol
     * @param price
     * @param amount
     * @param buySell 1：买，-1：卖
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_ORDER)
    Call<ResponseBody> tradeOrder(@Field("symbol") String symbol, @Field("price") String price, @Field("amount") String amount, @Field("balance") String balance, @Field("buySell") int buySell, @Field("payPass") String payPass);


    /**
     * 充值
     *
     * @param receiptId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_CASH_ORDER_BUY)
    Call<ResponseBody> tradeCashOrderBuy(@Field("cny") String cny, @Field("receiptId") long receiptId);

    /**
     * 提现
     *
     * @param receiptId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_CASH_ORDER_SELL)
    Call<ResponseBody> tradeCashOrderSell(@Field("amount") String amount, @Field("receiptId") long receiptId, @Field("payPass") String payPass);

    /**
     * 输入时实时获取跟单数量、金额、预计数量、预计金额等等
     *
     * @param symbol
     * @param amount
     * @param price
     * @param buySell
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_CHECKOUT)
    Call<ResponseBody> tradeCheckOut(@Field("symbol") String symbol, @Field("price") String price, @Field("amount") String amount, @Field("myCoin") String myCoin, @Field("buySell") int buySell);

    /**
     * 现金订单记录（包括充值提现）
     *
     * @param symbol
     * @param state  state: null:全部，0：待付款，1：已标记付款，2：已完成，-10：已失效
     * @param type   type: 1:充值提现， 2：买币(用于过滤USDT记录)
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_HISTORY)
    Call<ResponseBody> tradeHistory(@Field("symbol") String symbol, @Field("state") String state, @Field("isDone") int isDone, @Field("pageNo") int pageNo, @Field("type") int type);

    /**
     * 币币订单记录
     *
     * @param symbol
     * @param state  state: null：全部，30：全部成交，24：部分成交，44：已撤销
     * @param isDone
     * @param pageNo
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_ORDERLIST)
    Call<ResponseBody> tradeOrderList(@Field("symbol") String symbol, @Field("state") String state, @Field("isDone") int isDone, @Field("buySell") String buySell, @Field("pageNo") int pageNo);


    /**
     * 【币币】交易记录中撤销订单
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_CANCEL)
    Call<ResponseBody> tradeCancel(@Field("orderId") long orderId);


    /**
     * 【现金】撤销订单
     *
     * @param orderCashId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_CASH_ORDER_CANCEL)
    Call<ResponseBody> tradeCashOrderCancel(@Field("orderCashId") long orderCashId);

    /**
     * 现金订单详情
     *
     * @param orderCashId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_CASH_ORDER_DETAIL)
    Call<ResponseBody> tradeCashOrderDetail(@Field("orderCashId") long orderCashId);


    /**
     * 【币币】订单详情
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.TRADE_ORDER_DETAIL)
    Call<ResponseBody> tradeOrderDetail(@Field("orderId") long orderId);

    @POST(VHttpServiceManager.TRADE_INOUTHOME)
    Call<ResponseBody> tradeInoutHome();


    /**
     * 获取主页数据
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PERSONAL_HOME)
    Call<ResponseBody> personalHome(@Field("targetUid") long targetUid);

    @FormUrlEncoded
    @POST(VHttpServiceManager.PERSONAL_TRADENUM)
    Call<ResponseBody> trendChart(@Field("targetUid") long targetUid, @Field("timeType") String timeType, @Field("type") int type);

//    /**
//     * 获取个人业绩走势
//     *
//     * @param targetUid
//     * @param timeType
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(VHttpServiceManager.PERSONAL_TRENDNUM)
//    Call<ResponseBody> personalTrendNum(@Field("targetUid") long targetUid, @Field("timeType") String timeType);
//
//    /**
//     * 获取跟单人气
//     *
//     * @param targetUid
//     * @param timeType
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(VHttpServiceManager.PERSONAL_COPYNUM)
//    Call<ResponseBody> personalCopynum(@Field("targetUid") long targetUid, @Field("timeType") String timeType);
//
//    /**
//     * 获取交易次数
//     *
//     * @param targetUid
//     * @param timeType
//     * @return
//     */
//    @FormUrlEncoded
//    @POST(VHttpServiceManager.PERSONAL_TRADENUM)
//    Call<ResponseBody> personalTradenum(@Field("targetUid") long targetUid, @Field("timeType") String timeType);


    /**
     * 取消关注
     *
     * @param attentionUid
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.ATTENTION_CANCEL)
    Call<ResponseBody> followCancel(@Field("attentionUid") long attentionUid);


    /**
     * 添加关注
     *
     * @param attentionUid
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.ATTENTION_ADD)
    Call<ResponseBody> followAdd(@Field("attentionUid") long attentionUid, @Field("num") String num);


    /**
     * 提交跟单接口
     *
     * @param copyUid
     * @param balance        跟单投入，必填
     * @param maxCopyBalance 每次跟单买入上限，必填
     * @param stopWin        设置止盈的百分比，0不设置
     * @param stopLoss       设置止损的百分比，0不设置
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_ORDER)
    Call<ResponseBody> copySlaveOrder(@Field("copyUid") long copyUid, @Field("balance") String balance, @Field("maxCopyBalance") String maxCopyBalance, @Field("stopWin") double stopWin, @Field("stopLoss") double stopLoss);

    /**
     * 跟单追加金额
     *
     * @param orderId
     * @param balance
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_APPENDBALANCE)
    Call<ResponseBody> copyAppendOrderCash(@Field("orderId") long orderId, @Field("balance") String balance);


    /**
     * 跟单详情
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_ORDERDETAIL)
    Call<ResponseBody> copyOrderDetail(@Field("orderId") long orderId);


    /**
     * 跟单修改配置选项前调用
     *
     * @param orderId
     * @param stopWin
     * @param stopLoss
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_UPDATESTOPTIPS)
    Call<ResponseBody> updateStopTips(@Field("orderId") long orderId, @Field("stopWin") double stopWin, @Field("stopLoss") double stopLoss);

    /**
     * 跟单修改配置选项
     *
     * @param orderId
     * @param maxCopyBalance
     * @param stopWin
     * @param stopLoss
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_UPDATEOPTION)
    Call<ResponseBody> copyUpdateOption(@Field("orderId") long orderId, @Field("maxCopyBalance") String maxCopyBalance, @Field("stopWin") double stopWin, @Field("stopLoss") double stopLoss);


    /**
     * 停止跟单
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_CLOSEORDER)
    Call<ResponseBody> copyCloseorder(@Field("orderId") long orderId);

    /**
     * @param symbol  赛选用到
     * @param buySell 赛选用到
     * @param orderId
     * @param pageNo
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_TRADELIST)
    Call<ResponseBody> copyTradeList(@Field("symbol") String symbol, @Field("buySell") String buySell, @Field("orderId") long orderId, @Field("pageNo") int pageNo);

    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_HISTORY)
    Call<ResponseBody> copyHistoryList(@Field("pageNo") int pageNo);

    /**
     * 是否有委托单
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_SLAVE_CLOSEORDERTIPS)
    Call<ResponseBody> closeOrderTips(@Field("orderId") long orderId);


    /**
     * 跟单用户数据
     *
     * @return
     */
    @POST(VHttpServiceManager.COPY_DATA_HOME)
    Call<ResponseBody> copyDataHome();


    /**
     * 持币成本
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COPY_DATA_HOLDCOST)
    Call<ResponseBody> copyHoldCost(@Field("symbol") String symbol);

    /**
     * 跟单资金
     *
     * @return
     */
    @POST(VHttpServiceManager.COPY_DATA_COPYBALANCE)
    Call<ResponseBody> copyBalance();

    /**
     * 持币市值
     *
     * @return
     */
    @POST(VHttpServiceManager.COPY_DATA_HOLDMARKETVALUE)
    Call<ResponseBody> holdMarketValue();


    /**
     * 搜索
     *
     * @param keyValue
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.SEARCH_GET)
    Call<ResponseBody> search(@Field("keyValue") String keyValue);


    @FormUrlEncoded
    @POST(VHttpServiceManager.SEARCH_COIN)
    Call<ResponseBody> searchCoin(@Field("symbol") String symbol);

    /**
     * 提币列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.WITHDRAWCOIN_COINLIST)
    Call<ResponseBody> coinList(@Field("inOut") int inOut);


    /**
     * 获取提币或充币信息(地址、手续费、最小量等等)
     *
     * @param symbol
     * @param inOut
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.WITHDRAWCOIN_GETCOININFO)
    Call<ResponseBody> getWithdrawCoinInfo(@Field("symbol") String symbol, @Field("chainType") String chainType, @Field("inOut") int inOut);

    /**
     * 提币
     *
     * @param symbol
     * @param amount
     * @param address
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.WITHDRAWCOIN_SUBMIT)
    Call<ResponseBody> withdrawCoinSubmit(@Field("symbol") String symbol, @Field("amount") String amount, @Field("address") String address, @Field("chainType") String chainType, @Field("payPass") String payPass);

    /**
     * 撤销提币
     *
     * @param dwId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.WITHDRAWCOIN_CANCEL)
    Call<ResponseBody> withdrawCoinCancel(@Field("dwId") long dwId);

    /**
     * 提币列表
     *
     * @param pageNo
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.WITHDRAWCOIN_LIST)
    Call<ResponseBody> withdrawCoinList(@Field("pageNo") int pageNo);


    /**
     * 进入我的工作台数据
     */
    @POST(VHttpServiceManager.PERSONAL_CONSOLE)
    Call<ResponseBody> personalConsole();

    /**
     * 获取开通规则
     *
     * @return
     */
    @POST(VHttpServiceManager.CONFIG_OPENCOPYRULES)
    Call<ResponseBody> openCopyRules();

    /**
     * 开通工作台
     *
     * @return
     */
    @POST(VHttpServiceManager.USER_SECURITY_OPENCOPY)
    Call<ResponseBody> openCopy();

    /**
     * 获取币种简介
     *
     * @param symbol
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COIN_INFO)
    Call<ResponseBody> getCoinInfo(@Field("symbol") String symbol);


    /**
     * 获取分享链接
     *
     * @param shareType
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.SHARE_GETSHAREINFO)
    Call<ResponseBody> getshareinfo(@Field("shareType") int shareType, @Field("params") String params);


    /**
     * 我的
     *
     * @return
     */
    @POST(VHttpServiceManager.HOMEMY)
    Call<ResponseBody> myHome();

    @POST(VHttpServiceManager.HOME_ABILITYVALUETOAWARD)
    Call<ResponseBody> abilityValueToAward();


    /**
     * 币种资产
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COIN_ASSET)
    Call<ResponseBody> getCoinAsset(@Field("symbol") String symbol, @Field("pageNo") int pageNo);

    /**
     * 认购主页
     *
     * @return
     */
    @POST(VHttpServiceManager.COIN_INTOSUBHOME)
    Call<ResponseBody> repoHome();

    /**
     * 认购公告
     *
     * @param pageNo
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COIN_NOTICE)
    Call<ResponseBody> getCoinNotice(@Field("pageNo") int pageNo);

    /**
     * 认购
     *
     * @param subId
     * @param symbol
     * @param amount
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.COIN_SUBBUY)
    Call<ResponseBody> repo(@Field("subId") long subId, @Field("symbol") String symbol, @Field("amount") String amount);


    /**
     * YYB回购主页
     *
     * @return
     */
    @POST(VHttpServiceManager.YYB_REPOHOME)
    Call<ResponseBody> yybRepoHome();

    /**
     * yyb回购公告
     *
     * @param pageNo
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.YYB_NOTICE)
    Call<ResponseBody> getYYBNotice(@Field("pageNo") int pageNo);

    /**
     * YYB回购
     *
     * @param amount
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.YYB_REPO)
    Call<ResponseBody> yybRepo(@Field("amount") String amount);

    /**
     * 当前某个币种的持仓
     *
     * @param symbol
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.ACCOUNT_HOLD)
    Call<ResponseBody> currHold(@Field("symbol") String symbol);


    @FormUrlEncoded
    @POST(VHttpServiceManager.PREDICT_RECORD)
    Call<ResponseBody> getPredictRecord(@Field("pageNo") int pageNo);

    /**
     * 法币购买列表
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_SUPPORT_OTCLIST)
    Call<ResponseBody> otcList();

    //我的自选
    @POST(VHttpServiceManager.OPTIONAL_COIN_FIND)
    Call<ResponseBody> optionalCoinFind();

    @POST(VHttpServiceManager.OPTIONAL_COIN_FINDALL)
    Call<ResponseBody> optionalCoinFindAll();

    //添加自选
    @FormUrlEncoded
    @POST(VHttpServiceManager.OPTIONAL_COIN_ADD)
    Call<ResponseBody> optionalAdd(@Field("symbol") String symbol);

    //删除自选
    @FormUrlEncoded
    @POST(VHttpServiceManager.OPTIONAL_COIN_DEL)
    Call<ResponseBody> optionalDel(@Field("symbol") String symbol);

    //是否是自选
    @FormUrlEncoded
    @POST(VHttpServiceManager.OPTIONAL_COIN_ISOPTIONAL)
    Call<ResponseBody> isOptional(@Field("symbol") String symbol);


    @FormUrlEncoded
    @POST(VHttpServiceManager.OPTIONAL_COIN_SYNSORT)
    Call<ResponseBody> sortOptional(@Field("symbolAndSort") String symbolAndSort);


    /**
     * 存币宝->进入某个存币宝数据
     *
     * @param storeSymbol
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_STORE_ASSET)
    Call<ResponseBody> prybarStoreAsset(@Field("storeSymbol") String storeSymbol, @Field("pageNo") int pageNo);

    /**
     * 存币宝->转入转出配置信息提前获取
     *
     * @param storeSymbol
     * @param inOut       inOut=1(转入)\-1(转出)
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_STORE_CONFIG)
    Call<ResponseBody> prybarStoreConfig(@Field("storeSymbol") String storeSymbol, @Field("inOut") int inOut);


    /**
     * 转入
     *
     * @param storeSymbol
     * @param amount
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_STORE_CREATEIN)
    Call<ResponseBody> prybarStoreCreateIn(@Field("storeSymbol") String storeSymbol, @Field("amount") String amount);

    /**
     * 转出
     *
     * @param storeSymbol
     * @param amount
     * @param selectItem  selectItem=0代表选择本金转出，selectItem=1代表选择收益转出
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_STORE_CREATEOUT)
    Call<ResponseBody> prybarStoreCreateOut(@Field("storeSymbol") String storeSymbol, @Field("amount") String amount, @Field("selectItem") int selectItem);

    /**
     * 获取交易配置信息
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_CONFIG)
    Call<ResponseBody> prybarConfig(@Field("symbol") String symbol);


    /**
     * 选择保证金发生变化获取相关可视化数据
     *
     * @param symbol
     * @param price     当orderType＝market，请写死price=0
     * @param buySell   buy：看涨，sell：看跌
     * @param hand      手数，输入整型，当没值请输入0
     * @param multiNum  杠杆倍数
     * @param orderType limit：限价，market：市价
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_CHECKOUT)
    Call<ResponseBody> prybarCheckOut(@Field("symbol") String symbol, @Field("price") String price, @Field("buySell") String buySell, @Field("hand") String hand, @Field("multiNum") String multiNum, @Field("orderType") String orderType);

    //下单
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_CREATEORDER)
    Call<ResponseBody> prybarCreateOrder(@Field("symbol") String symbol, @Field("price") String price, @Field("buySell") String buySell, @Field("hand") String hand, @Field("multiNum") String multiNum, @Field("orderType") String orderType, @Field("payPass") String payPass);

    //订单详情
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_DETAIL)
    Call<ResponseBody> prybarDetail(@Field("orderId") long orderId);

    //平仓
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_CLOSEORDER)
    Call<ResponseBody> prybarCloseOrder(@Field("orderId") long orderId, @Field("closeHand") String closeHand, @Field("payPass") String payPass);

    //增加保证金
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_APPENDBAILBALANCE)
    Call<ResponseBody> prybarAppendBailBalance(@Field("orderId") long orderId, @Field("bailBalance") String bailBalance);


    //设置止盈止损
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_CHECKWINLOSS)
    Call<ResponseBody> prybarCheckWinLoss(@Field("orderId") long orderId, @Field("stopWin") String stopWin, @Field("stopLoss") String stopLoss);

    //设置止盈止损
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_UPDATEWINLOSS)
    Call<ResponseBody> prybarUpdateWinLoss(@Field("orderId") long orderId, @Field("stopWin") String stopWin, @Field("stopLoss") String stopLoss);


    /**
     * 杠杆交易记录
     *
     * @param symbol
     * @param accountType
     * @param isDone      可为空，查全部；isDone＝0当前委托，isDone＝1当前开仓，isDone＝-1历史记录
     * @param buySell
     * @param pageNo
     * @param orderState  订单状态：当isDone=-1才有效，filled（已成交），canceled（已撤销）
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_QUERYLIST)
    Call<ResponseBody> queryList(@Field("symbol") String symbol, @Field("accountType") String accountType, @Field("isDone") int isDone, @Field("buySell") String buySell, @Field("pageNo") int pageNo, @Field("orderState") String orderState);

    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_QUERYFOLLOWLIST)
    Call<ResponseBody> queryFollowList(@Field("symbol") String symbol, @Field("accountType") String accountType, @Field("isDone") int isDone, @Field("buySell") String buySell, @Field("pageNo") int pageNo, @Field("orderState") String orderState);

    //获取所有账户类型
    @POST(VHttpServiceManager.ACCOUNT_LISTACCOUNTTYPE)
    Call<ResponseBody> listAccountType();

    //获取某个账户可用数量
    @FormUrlEncoded
    @POST(VHttpServiceManager.ACCOUNT_OUTHOLDAMOUNT)
    Call<ResponseBody> outHoldAmount(@Field("accountType") String accountType);

    //划转
    @FormUrlEncoded
    @POST(VHttpServiceManager.ACCOUNT_TRANSFER)
    Call<ResponseBody> transfer(@Field("amount") String amount, @Field("fromAccountType") String fromAccountType, @Field("toAccountType") String toAccountType, @Field("payPass") String payPass);


    /**
     * 划转记录
     *
     * @param fromAccountType
     * @param toAccountType
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.ACCOUNT_QUERYTRANSFERLIST)
    Call<ResponseBody> queryTransferList(@Field("fromAccountType") String fromAccountType, @Field("toAccountType") String toAccountType, @Field("pageNo") int pageNo);


    //解绑大v
    @FormUrlEncoded
    @POST(VHttpServiceManager.FOLLOW_UNBIND)
    Call<ResponseBody> unBind(@Field("dvUid") long dvUid);

    //申请带单  绑定大V
    @FormUrlEncoded
    @POST(VHttpServiceManager.FOLLOW_APPLYFORFOLLOW)
    Call<ResponseBody> applyForFollow(@Field("dvUid") long dvUid);


    /**
     * 跟单账户－更新开通跟单权限
     *
     * @param isOpen 0关闭，1开通
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.FOLLOW_UPDATEFOROPEN)
    Call<ResponseBody> updateForOpen(@Field("isOpen") int isOpen);

    //跟单账户－更改跟单倍数
    @FormUrlEncoded
    @POST(VHttpServiceManager.FOLLOW_UPDATEMULTIPLE)
    Call<ResponseBody> updateMultiple(@Field("multiple") int multiple);


    /**
     * 杠杆 撤销订单
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_CANCEL)
    Call<ResponseBody> proOrderCancel(@Field("orderId") long orderId, @Field("payPass") String payPass);

    /**
     * 止盈
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_UPDATEWINPRICE)
    Call<ResponseBody> updateWinPrice(@Field("orderId") long orderId, @Field("stopWinPrice") String stopWinPrice, @Field("payPass") String payPass);

    /**
     * 止损
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.PRYBAR_ORDER_UPDATELOSSPRICE)
    Call<ResponseBody> updateLossPrice(@Field("orderId") long orderId, @Field("stopLossPrice") String stopLossPrice, @Field("payPass") String payPass);


    //获取公告
    @FormUrlEncoded
    @POST(VHttpServiceManager.ARTICLE_NOTICELIST)
    Call<ResponseBody> noticeList(@Field("pageNo") int pageNo);

//    /**
//     * 获取累计盈亏
//     */
//    @POST(VHttpServiceManager.PERSONAL_PROFIT)
//    Call<ResponseBody> personalProfit();


    //OTC Start


    /**
     * 市场广告列表（购买，出售）
     *
     * @param buySell      buy我想买，sell我想卖
     * @param filterPayWay 筛选支付方式：0全部，1支付宝，2微信，3银行卡
     * @param filterCny    筛选金额
     * @param pageNo
     * @param type         type=fast代表快捷区，type=optional或空代表自选区，
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_FINDADLIST)
    Call<ResponseBody> otcFindAdList(@Field("buySell") String buySell, @Field("filterPayWay") int filterPayWay, @Field("filterCny") String filterCny, @Field("pageNo") int pageNo, @Field("type") String type);

    /**
     * 下单
     *
     * @param buySell buy我要买，sell我要卖
     * @param adId    广告ID
     * @param amount  数量
     * @param price   单价
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_CREATEORDER)
    Call<ResponseBody> otcCreateOrder(@Field("buySell") String buySell, @Field("adId") long adId, @Field("amount") String amount, @Field("price") String price, @Field("showReceiptType") String showReceiptType);


    /**
     * 获取订单详情
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_GETORDERDETAIL)
    Call<ResponseBody> otcGetOrderDetail(@Field("orderId") long orderId);


    /**
     * 付款第1步：点击“去付款”按钮进入到付款页面
     *
     * @param orderId
     * @param showUserId
     * @param showPaymentId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_TOPAYORDER)
    Call<ResponseBody> otcToPayOrder(@Field("orderId") long orderId, @Field("showUserId") long showUserId, @Field("showPaymentId") long showPaymentId);

    /**
     * 付款第2步：点击“我已付款成功”按钮
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_TOMARKPAYORDERSUCCESS)
    Call<ResponseBody> otcToMarkPayOrderSuccess(@Field("orderId") long orderId);

    /**
     * 出售第2步：点击“我确认已收到付款”按钮
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_TOCONFIRMRECEIVEDPAY)
    Call<ResponseBody> otcToConfirmReceivedPay(@Field("orderId") long orderId);

    /**
     * 我的订单历史记录
     *
     * @param buySell
     * @param state
     * @param pageNo
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_FINDORDERLIST)
    Call<ResponseBody> otcFindOrderList(@Field("buySell") String buySell, @Field("state") String state, @Field("pageNo") int pageNo);

    /**
     * 取消订单
     *
     * @param orderId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_CANCELORDER)
    Call<ResponseBody> otcCancelOrder(@Field("orderId") long orderId);


    /**
     * 初始化申诉理由列表
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_GETINITAPPEALLIST)
    Call<ResponseBody> otcGetInitAppealList(@Field("orderId") long orderId);

    /**
     * 提交申诉
     * @param orderId
     * @param reason 申诉理由的话
     * @param image1Url 申诉截图链接1
     * @param image2Url 申诉截图链接2
     * @param message 申斥留言
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MAINAD_SUBMITAPPEAL)
    Call<ResponseBody> otcSubmitAppeal(@Field("orderId") long orderId, @Field("reason") String reason, @Field("image1Url") String image1Url, @Field("image2Url") String image2Url, @Field("message") String message);


    /**
     * 获取我的收款方式列表
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_PAYMENT_FINDMYPAYMENTLIST)
    Call<ResponseBody> otcFindMyPaymentList();


    /**
     * 【收款方式】可选择收款(在“添加收款方式”时调用)
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_PAYMENT_FINDPAYMENTOPTIONLIST)
    Call<ResponseBody> otcFindPaymentOptionList();


    /**
     * 添加或修改我的收款方式
     *
     * @param receiptType 收款方式，1：支付宝，2：微信，3：银行卡
     * @param paymentId   添加收款方式为0，编辑时>0
     * @param receiptName 收款开户名、微信名、支付宝账户
     * @param receiptNo   卡号、微信账号、支付宝账号
     * @param bankName    银行卡的开户行名称，receiptType=3
     * @param qrCodeUrl   收款的二维码链接(微信和支付宝)receiptType=1或receiptType=2
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_PAYMENT_SAVEPAYMENT)
    Call<ResponseBody> otcSavePayment(@Field("receiptType") int receiptType, @Field("paymentId") long paymentId, @Field("receiptName") String receiptName, @Field("receiptNo") String receiptNo, @Field("bankName") String bankName, @Field("qrCodeUrl") String qrCodeUrl);


    /**
     * 删除我的收款方式
     *
     * @param paymentId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_PAYMENT_DELETE)
    Call<ResponseBody> otcPaymentDelete(@Field("paymentId") long paymentId);

    /**
     * 获取商家认证需要信息
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_CERTIFICATION_GETCERTIFICATIONINFO)
    Call<ResponseBody> otcGetCertificationInfo();

    /**
     * 申请成为商家
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_CERTIFICATION_AUTHENTICATE)
    Call<ResponseBody> otcAuthenticate();


    /**
     * 撤销成为商家
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_CERTIFICATION_APPLYFORCANCELLATION)
    Call<ResponseBody> otcApplyForCancellation();


    /**
     * 我的广告列表
     *
     * @return
     */
    @POST(VHttpServiceManager.OTC_MYAD_FINDMYADLIST)
    Call<ResponseBody> otcFindMyAdList();

    /**
     * 我的广告详情
     *
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MYAD_GETMYADINFO)
    Call<ResponseBody> otcGetMyAdInfo(@Field("adId") long adId);


    /**
     * 我的广告设置上下架
     *
     * @param adId
     * @param online 0下架，1上架
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MYAD_SETONLINE)
    Call<ResponseBody> otcSetOnline(@Field("adId") long adId, @Field("online") int online);

    /**
     * 删除我的广告
     *
     * @param adId
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MYAD_DELMYAD)
    Call<ResponseBody> otcDelMyAd(@Field("adId") long adId);


    /**
     * 发布广告
     *
     * @param buySell buy购买，sell出售
     * @param price   单价
     * @param minCny  最小限额，>0
     * @param maxCny  最大限额，>0
     * @param amount  目标数量
     * @param payWay  支付方式集合，paymentId集合，多个用逗号隔开，如：12,23
     * @param content 留言内容
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MYAD_ADDMYAD)
    Call<ResponseBody> otcAddMyAd(@Field("buySell") String buySell, @Field("price") String price, @Field("minCny") String minCny, @Field("maxCny") String maxCny, @Field("amount") String amount, @Field("payWay") String payWay, @Field("content") String content);


    /**
     * 编辑广告
     *
     * @param adId    广告id
     * @param buySell buy购买，sell出售
     * @param price   单价
     * @param minCny  最小限额，>0
     * @param maxCny  最大限额，>0
     * @param amount  目标数量
     * @param payWay  支付方式集合，paymentId集合，多个用逗号隔开，如：12,23
     * @param content 留言内容
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MYAD_UPDATEMYAD)
    Call<ResponseBody> otcUpdateMyAd(@Field("adId") long adId, @Field("buySell") String buySell, @Field("price") String price, @Field("minCny") String minCny, @Field("maxCny") String maxCny, @Field("amount") String amount, @Field("payWay") String payWay, @Field("content") String content);


    /**
     *
     * 获取广告最高最低价格
     *
     * @param buySell buy购买，sell出售，编辑是不可更心，按原值传参
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.OTC_MYAD_GETADPRICE)
    Call<ResponseBody> otcGetAdPrice(@Field("buySell") String buySell);

    /**
     * 行情
     * @param symbols   自选才需要填 其他填空
     * @param sortField 1：symbol,2：price,3：rate
     * @param sortType
     * @param tab       optional自选、digital数字货币、stock股指期货
     * @param page      分页
     * @return
     */
    @FormUrlEncoded
    @POST(VHttpServiceManager.MARKETDATA)
    Call<ResponseBody> marketData(@Field("symbols")String symbols, @Field("sortField")int sortField, @Field("sortType")int sortType, @Field("tab")String tab,@Field("page") int page);


    //OTC End


}
