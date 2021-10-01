package com.procoin.subpush.notify;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.subpush.Consts;

import java.util.Calendar;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TPushSettingManager {
	private static TPushSettingManager instance;
	private volatile boolean isTurnOff;//是否关闭push提醒
    private volatile int ring;//是否响铃0代表不响铃,1代表响铃
    private volatile int vibrate;//是否振动0代表不振动,1代表振动
    private volatile boolean isSilent;//静默时间
    private volatile int sStartTime;//静默时间开始
    private volatile int sEndTime;//静默时间结束
    private boolean isReset;
	private final ConcurrentMap<String,Boolean> remindeMap = new ConcurrentHashMap<String,Boolean>();
	private final ConcurrentMap<String,String> circleNameMap = new ConcurrentHashMap<String,String>();

    private TPushSettingManager(){
    	
    }
    
    public static TPushSettingManager getInstance() {
        if (instance == null) {
            synchronized (TPushSettingManager.class) {
				Log.d("NotifyManager", "==TPushSettingManager=====myPid=" + android.os.Process.myPid());
				instance = new TPushSettingManager();
            }
        }
        return instance;
    }
    
    /**
     * 设置当前用户数据
     * @param userId
     * @param context
     */
    public void resetUserId(String userId,Context context){
    	if(userId == null || "0".equals(userId))return;
    	SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
    	isTurnOff = sp.getBoolean(Consts.PUSH_IS_TURNOFF, true);
    	ring = sp.getInt(Consts.PUSH_RING, 1);
    	vibrate = sp.getInt(Consts.PUSH_VIBRATE, 1);
    	isSilent = sp.getBoolean(Consts.PUSH_IS_SILENT, false);
    	sStartTime = sp.getInt(Consts.PUSH_STARTTIME, 0);
    	sEndTime = sp.getInt(Consts.PUSH_ENDTIME, 0);
		isReset = true;
    }

	/**
	 * 全局判断是否push
	 * @return
	 */
	public boolean isTjrPush(Context context,long userId){
		if(userId == 0)return false;
		if(!isReset)resetUserId(String.valueOf(userId),context);
		if (!isTurnOff) return false;
		if (isSilent) {
			if (sStartTime != sEndTime) {
				Calendar c = Calendar.getInstance();
				int h = c.get(Calendar.HOUR_OF_DAY);
				if (sStartTime > sEndTime) {
					if (sStartTime <= h || h <= sEndTime) return false;
				} else {
					if (sStartTime <= h && h <= sEndTime) return false;
				}
			}
		}
		return true;
	}

    
	public boolean isTurnOff() {
		return isTurnOff;
	}

	public void setTurnOff(String userId,boolean isTurnOff,Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.isTurnOff = isTurnOff;
		Editor editor = sp.edit();
		editor.putBoolean(Consts.PUSH_IS_TURNOFF, isTurnOff).commit();
	}

	public boolean isSilent() {
		return isSilent;
	}

	public void setSilent(String userId,boolean isSilent,Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.isSilent = isSilent;
		Editor editor = sp.edit();
		editor.putBoolean(Consts.PUSH_IS_SILENT, isSilent).commit();
	}

	public int getsStartTime() {
		return sStartTime;
	}

	public void setsStartTime(String userId,int sStartTime,Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.sStartTime = sStartTime;
		Editor editor = sp.edit();
		editor.putInt(Consts.PUSH_STARTTIME, sStartTime).commit();
	}

	public int getsEndTime() {
		return sEndTime;
	}

	public void setsEndTime(String userId,int sEndTime,Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.sEndTime = sEndTime;
		Editor editor = sp.edit();
		editor.putInt(Consts.PUSH_ENDTIME, sEndTime).commit();
	}
	
	/**
	 * 设置免打扰时间段
	 * @param userId
	 * @param sStartTime
	 * @param sEndTime
	 * @param context
	 */
	public void setSilentTime(String userId,int sStartTime,int sEndTime,Context context){
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.sStartTime = sStartTime;
		this.sEndTime = sEndTime;
		Editor editor = sp.edit();
		editor.putInt(Consts.PUSH_STARTTIME, sStartTime).putInt(Consts.PUSH_ENDTIME, sEndTime).commit();
	}
	
	public int getRing() {
		return ring;
	}

	public void setRing(String userId,int ring,Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.ring = ring;
		Editor editor = sp.edit();
		editor.putInt(Consts.PUSH_RING, ring).commit();
	}

	public int getVibrate() {
		return vibrate;
	}

	public void setVibrate(String userId,int vibrate,Context context) {
		SharedPreferences sp = context.getSharedPreferences(Consts.PUSH_SETTING+"_"+userId, Context.MODE_PRIVATE);
		this.vibrate = vibrate;
		Editor editor = sp.edit();
		editor.putInt(Consts.PUSH_VIBRATE, vibrate).commit();
	}

//	public void saveChatReminde(long userId, String circleNum,boolean reminde){
//		remindeMap.put("chat_" + circleNum + userId, reminde);
//	}
//
//	public void saveInfoReminde(long userId, String circleNum,boolean reminde){
//		remindeMap.put("info_"+circleNum + userId, reminde);
//	}
//
//	public void savePartyReminde(long userId, String circleNum,boolean reminde){
//		remindeMap.put("party_"+circleNum + userId, reminde);
//	}
//
//	public void saveGameReminde(long userId, String circleNum,boolean reminde){
//		remindeMap.put("game_"+circleNum + userId, reminde);
//	}

		public void saveCircleReminde(long userId, String circleNum,boolean reminde){
		remindeMap.put("circle_"+circleNum + userId, reminde);
	}

	public boolean getCircleReminde(Context context,long userId, String circleNum){
		String key = "circle_"+circleNum + userId;
		if(remindeMap.containsKey(key)){
			return remindeMap.get(key);
		}else{
			boolean re = CircleSharedPreferences.getCircleSettingRemind(context, userId, circleNum);
			remindeMap.put(key, re);
			return re;
		}
	}

//	public boolean getChatReminde(Context context,long userId, String circleNum){
//		String key = "chat_"+circleNum + userId;
//		if(remindeMap.containsKey(key)){
//			return remindeMap.get(key);
//		}else{
//			boolean re = CircleSharedPreferences.getCircleSettingChatRemind(context, userId, circleNum);
//			remindeMap.put(key, re);
//			return re;
//		}
//	}

//	public boolean getInfoReminde(Context context,long userId, String circleNum){
//		String key = "info_"+circleNum + userId;
//		if(remindeMap.containsKey(key)){
//			return remindeMap.get(key);
//		}else{
//			boolean re = CircleSharedPreferences.getCircleSettingInfoRemind(context, userId, circleNum);
//			remindeMap.put(key, re);
//			return re;
//		}
//	}

//	public boolean getPartyReminde(Context context,long userId, String circleNum){
//		String key = "party_"+circleNum + userId;
//		if(remindeMap.containsKey(key)){
//			return remindeMap.get(key);
//		}else{
//			boolean re = CircleSharedPreferences.getCircleSettingPartyRemind(context, userId, circleNum);
//			remindeMap.put(key, re);
//			return re;
//		}
//	}

//	public boolean getGameReminde(Context context,long userId, String circleNum){
//		String key = "game_"+circleNum + userId;
//		if(remindeMap.containsKey(key)){
//			return remindeMap.get(key);
//		}else{
//			boolean re = CircleSharedPreferences.getCircleSettingGameRemind(context, userId, circleNum);
//			remindeMap.put(key, re);
//			return re;
//		}
//	}

	public void resetCircleName(String circleNum,String circleName){
		circleNameMap.put(circleNum,circleName);
	}

	public String getCircleName(String circleNum){
		return circleNameMap.get(circleNum);
	}
}
