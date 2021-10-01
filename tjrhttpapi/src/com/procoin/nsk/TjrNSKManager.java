//package com.cropyme.nsk;
//
//import android.util.Log;
//
//import com.cropyme.http.TjrBaseApi;
//import com.cropyme.nsk.exceptions.TjrNSKConnectionException;
//
//public class TjrNSKManager {
//	private TjrNSKPool pool;
//	private volatile static TjrNSKManager instance;
//	public TjrNSKManager() {
//		TjrNSKPoolConfig config = new TjrNSKPoolConfig();
//		// 最大分配的对象数
//		config.setMaxActive(10);
//		// 允许最大空闲对象数
//		config.setMaxIdle(5);
//		//允许最小空闲对象数
//		config.setMinIdle(1);
//		// 当池内没有返回对象时，最大等待时间
//		config.setMaxWait(1000);
//		// 当调用borrow Object方法时，是否进行有效性检查
//		config.setTestOnBorrow(false);
//		// 当调用return Object方法时，是否进行有效性检查
//		config.setTestOnReturn(false);
//		pool = new TjrNSKPool(config,TjrBaseApi.stockHomeUri.uri(),8220,4000,8000);
//	}
//
//	/**
//	 * 单例化一个CacheManager
//	 *
//	 * @return CacheManager对象
//	 */
//	public static TjrNSKManager getInstance() {
//		if (instance == null) {
//			synchronized (TjrNSKManager.class) {
//				if (instance == null) {
//					instance = new TjrNSKManager();
//				}
//			}
//		}
//		return instance;
//	}
//
//	public String sendCommand(String cmd) {
//		String obj = null;
//		if (cmd == null)return obj;
//		TjrNSK tjrnsk = null;
//		try {
//			tjrnsk = pool.getResource();
//			if(TjrBaseApi.isDebug)Log.d("Protocol", "==1==sendCommand...tjrnsk="+tjrnsk+"  send="+cmd);
//			return tjrnsk.sendCommand(cmd);
//		} catch (Exception e) {
//			if(TjrBaseApi.isDebug)Log.d("Protocol", "==4==sendCommand...Exception:"+e.getMessage());
//			throw new TjrNSKConnectionException(e);
//		} finally {
//			if(tjrnsk != null)pool.returnResource(tjrnsk);
//		}
//	}
//
//	public void clearPool(){
//		if(TjrBaseApi.isDebug)Log.d("Protocol", "====clearPool:");
//		if(pool != null)pool.clear();
//	}
//}
