package com.procoin.http.common;

import android.content.Context;
import android.util.Log;

public class ConfigTjrInfo {
	private static ConfigTjrInfo instance;
	private volatile String version;
	private volatile String packageName;
	private volatile String sessionid="";
	private volatile String userId="";
	private Context context;

	public static ConfigTjrInfo getInstance() {
		if (instance == null) {
			synchronized (ConfigTjrInfo.class) {
				if (instance == null){
					instance = new ConfigTjrInfo();
				}
			}
		}
		return instance;
	}
	public void initAndroidInfo(Context context,String mVersion,String packageName){
		initAndroidInfo(context,mVersion,packageName,"","");
	}
	public void initAndroidInfo(Context context,String mVersion,String packageName,String sessionid,String userId){
		this.context = context;
		this.version = mVersion;
		this.packageName = packageName;
		this.sessionid = sessionid;
		this.userId=userId;
	}

	public String getVersion() {
		return version;
	}

	public String getUserId() {
		Log.d("ConfigTjrInfo","getUserId=="+userId);
		return userId;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getSessionid() {
		return sessionid;
	}


	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		Log.d("ConfigTjrInfo","setUserId=="+userId);
	}

	public void clearUser(){//用户注销时清楚sessionid和userid
		this.sessionid="";
		this.userId="";
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public void clear(){
		this.sessionid = null;
	}


}
