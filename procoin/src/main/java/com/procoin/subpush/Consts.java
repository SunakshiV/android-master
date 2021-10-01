package com.procoin.subpush;

import java.util.Random;

public class Consts {
	public static final String CONNECTION_ERROR = "connection_error";
	public static final String REQDO_CONNECT = "{\"reqDo\":\"/connect\"}";
	public static final String REQDO_LOGIN = "{\"reqDo\":\"/login\"}";//game
	public static final String REQDO_OK = "{\"reqDo\":\"/ok\"}";
	public static final String REQDO_GETDATA = "{\"reqDo\":\"/getData\"}";
	public static final String PING = "ping"; //
	public static final String PONG = "pong"; //
	public static final String R_N = "\r\n"; //
//	public static final String ACTION_CHECK_CONNECT = "com.cropyme.subpush.action.CHECK_CONNECT";
	public static final String ACTION_NOTIFY_MESSAGE = "com.cropyme.notify.action.MESSAGE_ARRIVED";
	public static final String NOTIFY_MODEL_JSON = "notify_model_json";
	public static final String ACTION_SUBPUSH_RESTART = "com.cropyme.subpush.action.SUBPUSH_RESTART";
//	public static final String ACTION_ONCREATE_ONMSGSERVICE = "com.cropyme.subpush.action.CHECK_ONMSGSERVICE";

	public static final String PUSH_SETTING = "push_setting_";//push设置
	public static final String PUSH_IS_TURNOFF = "push_is_turnoff";//push是否开启
	public static final String PUSH_RING = "push_ring";//声音开关
	public static final String PUSH_VIBRATE = "push_vibrate";//振动开关
	public static final String PUSH_IS_SILENT = "push_is_silent";//勿扰模式开关
	public static final String PUSH_STARTTIME = "push_starttime";
	public static final String PUSH_ENDTIME = "push_endtime";


	//push的pid  官方的是10
	public static final int PUSH_ID_CHAT = 11;//这个是以前的圈子push
	public static final int PUSH_ID_PRIVATECHAT = 14;//这个是私聊的push



	public static String getRandomVerifi(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		int l = base.length();
		StringBuffer sb = new StringBuffer();
		sb.append(System.currentTimeMillis());
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(l);
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}
	
}
