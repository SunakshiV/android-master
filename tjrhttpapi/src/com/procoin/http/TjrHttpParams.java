//package com.cropyme.http;
//
//import java.util.HashMap;
//import java.util.Map;
//
//
//public class TjrHttpParams {
//	private static TjrHttpParams instance;
//	private static Map<Object, String> map = new HashMap<Object, String>();
//	static {
////        String[] entities = {"user_0","addcontact_1","findknowfriends_2","accurate_3"
////        		,"find_4","msgOpp_5","addfriend_6","login_7"
////        		,"friend_8","mbregist_9","stock_10","mystock_11"
////        		,"company_12","chat_13","mychat_14","chatsend_15"
////        		,"chatopp_16","invite_17","sendinvite_18","components_19"
////        		,"component_20","newsPaper_21","stockPK_22","helpPick_23"
////        		,"stockbar_24","QAServlet_25","otherAccount_26","weibo_27"
////        		,"millionaire_28","kline_29","arena_30","userId_31","method_32"
////        		,"fullcodes_33","userAccount_34","newsPaperOpp_35"};
//		String[] entities ={"c5c36892e5ed528800c8","6164a1a007fce4ff548e00c9","66696e646b6e6fb2a51afbf5f0538900ca",
//				"a4a00be7e2ff439f00cb","c5c36892f6f7599e00cc","c5c305e1f7d1478a00cd",
//				"61a1a70ee0f9fb599e00ce","c5c368fefff95e9400cf","c5c30ee0f9fb599e00c0",
//				"a8a11af7f7f7448e00c1","c5c31be6fffd5ca56ec8","a8ba1be6fffd5ca56ec9",
//				"a6ac05e2f1f04ea56eca","c5c368f1f8ff43a56ecb","c5ae11f1f8ff43a56ecc",
//				"63ada21ce1f5f053a56ecd","a6ab09e6ffee47a56ece","c5aa06e4f9ea52a56ecf",
//				"73656ea1aa06e4f9ea52a56ec0","636f6db5ac06f7feea44a56ec1","636fa8b307fcf5f043a56dc8",
//				"6e65b2b038f3e0fb45a56dc9","b6b707f1fbce7ca56dca","68a0af18c2f9fd5ca56dcb",
//				"73b1ac0bf9f2ff45a56dcc","514196a61ae4fcfb43a56dcd","6f7468657284a00bfde5f043a56dce",
//				"c5c31ff7f9fc58a56dcf","6d696c6cacac06f3f9ec52a56dc0","c5c303fef9f052a56dc1",
//				"c5c309e0f5f056a56cc8","c5b61bf7e2d753a56cc9","c5ae0de6f8f153a56cca",
//				"6675a9af0bfdf4fb44a56ccb","7573657284a00bfde5f043a56ccc","6e65777350a4b30de0dfee47a56ccd"};
//        String[] a;
//		for (String entity : entities) {
//			a = Encryption.decrypt(entity);
//			map.put(Integer.parseInt(a[1]), a[0]);
//		}
//	}
//
//	private TjrHttpParams() {
//		map.put(10000, "//");
//		map.put(10001, ".");
//		map.put(10002, "/");
//		map.put(10003, ":");
//	}
//
//	public static TjrHttpParams getInstance() {
//		if (instance == null) {
//			synchronized (TjrHttpParams.class) {
//				if (instance == null) instance = new TjrHttpParams();
//			}
//		}
//		return instance;
//	}
//
//	private String fullParams(final String... params){
//		String p = "";
//		for (String param:params) {
//			p = p +  map.get(10002)+param;
//		}
//		return p;
//	}
//	//"/user";
//	public String getUser(){
//		return fullParams(map.get(0));
//	}
//	//"/user/addcontact"; // 上传通信录接口
//	public String getUserAddcontact(){
//		return fullParams(map.get(0),map.get(1));
//	}
//	//"/user/findknowfriends"; // 查找好友在淘金路上
//	public String getUserFindknowfriends(){
//		return fullParams(map.get(0),map.get(2));
//	}
//    //"/user/msgOpp";// 发送好友动态
//	public String getUserMsgOpp(){
//		return fullParams(map.get(0),map.get(5));
//	}
//	//"/user/addfriend"; // 加好友接口
//	public String getUserAddfriend(){
//		return fullParams(map.get(0),map.get(6));
//	}
//	//"/user/login";用户登录
//	public String getUserLogin(){
//		return fullParams(map.get(0),map.get(7));
//	}
//	//"/accurate/find"; // 查找接口
//	public String getAccurateFind(){
//		return fullParams(map.get(3),map.get(4));
//	}
//	//"/user/friend";
//	public String getUserFriend(){
//		return fullParams(map.get(0),map.get(8));
//	}
//	//"/user/mbregist"; // 手机端注册
//	public String getUserMbregist(){
//		return fullParams(map.get(0),map.get(9));
//	}
//	//"/stock/mystock";我的自选
//	public String getStockMystock(){
//		return fullParams(map.get(10),map.get(11));
//	}
//	//"/stock";我的自选
//	public String getStock(){
//		return fullParams(map.get(10));
//	}
//	//"/company";
//	public String getCompany(){
//		return fullParams(map.get(12));
//	}
//	//"/chat/mychat"; // 私聊
//	public String getChatMychat(){
//		return fullParams(map.get(13),map.get(14));
//	}
//	//"/chat/chatsend"; // 私聊发送接口
//	public String getChatChatsend(){
//		return fullParams(map.get(13),map.get(15));
//	}
//	//"/chat/chatopp"; // 私聊加好友接口
//	public String getChatChatopp(){
//		return fullParams(map.get(13),map.get(16));
//	}
//	//"/invite/sendinvite"; // 手机发送邀请接口
//	public String getinvitesendinvite(){
//		return fullParams(map.get(17),map.get(18));
//	}
//	//"/components";
//	public String getcomponents(){
//		return fullParams(map.get(19));
//	}
//	//"/component/newsPaper"; // 报纸
//	public String getcomponentnewsPaper(){
//		return fullParams(map.get(20),map.get(21));
//	}
//	//"component/newsPaperOpp"; // 报纸
//	public String getcomponentnewsPaperOpp(){
//		return fullParams(map.get(20),map.get(35));
//	}
//	//"/component/stockPK"; // 战股擂台
//	public String getcomponentstockPK(){
//		return fullParams(map.get(20),map.get(22));
//	}
//	//"/component/helpPick"; // 帮忙挑股
//	public String getcomponenthelpPick(){
//		return fullParams(map.get(20),map.get(23));
//	}
//	//"/component/stockbar"; // 股吧
//	public String getcomponentstockbar(){
//		return fullParams(map.get(20),map.get(24));
//	}
//	//"/QAServlet"; // 语音问答
//	public String getQAServlet(){
//		return fullParams(map.get(25));
//	}
//	//"/user/otherAccount"; // 其他账号
//	public String getuserotherAccount(){
//		return fullParams(map.get(0),map.get(26));
//	}
//	//"/weibo";
//	public String getweibo(){
//		return fullParams(map.get(27));
//	}
//	//"/millionaire"; 百W
//	public String getmillionaire(){
//		return fullParams(map.get(28));
//	}
//	//"/kline/arena"; K线角斗
//	public String getklinearena(){
//		return fullParams(map.get(29),map.get(30));
//	}
//
//
//	public String getParam(final Integer key){
//		if (map != null && map.containsKey(key)) {
//			return map.get(key);
//		}
//		return null;
//	}
//
//}
