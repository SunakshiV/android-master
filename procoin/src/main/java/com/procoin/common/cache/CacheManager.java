package com.procoin.common.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheManager {
	private Map<Object, Object> baseCache;
	private volatile static CacheManager instance;
	
	public static CacheManager getInstance() {
		if (instance == null) {
			synchronized (CacheManager.class) {
				if (instance == null) {
					instance = new CacheManager();
				}
			}
		}
		return instance;
	}

	// private static CacheManager instance;
	// private static Object lock = new Object();

	public CacheManager() {
		baseCache = new ConcurrentHashMap<Object, Object>();
	}

	// /**
	// * 单例化一个CacheManager
	// *
	// * @return CacheManager对象
	// */
	// public static CacheManager getInstance() {
	// if (instance == null) {
	// synchronized (lock) {
	// if (instance == null) {
	// instance = new CacheManager();
	// }
	// }
	// }
	// return instance;
	// }

	/**
	 * 将股票对象存入缓存中
	 * 
	 * @param news
	 *            ：股票对象
	 */
	public void putCache(String key, Object entity) {
		baseCache.put(key, entity);
	}

	/**
	 * 根据股票代码将缓存中的股票对象移除
	 * 
	 * @param newsID
	 *            ：股票代码
	 */
	public void removeCache(String key) {
		if (baseCache.containsKey(key)) {
			baseCache.remove(key);
		}
	}

	/**
	 * 根据股票代码从缓存中提取对应的股票对象
	 */
	public Object getCache(String key) {
		try {
			if (baseCache.containsKey(key)) return baseCache.get(key);
			return null;
		} catch (Exception e) {
			// logger.debug("在缓存中取不到相应的"+key+"，原因:"+e.getMessage());
			return null;
		}
	}

	/**
	 * 删除缓存中所有的对象
	 */
	public void removeAll() {
		baseCache.clear();
	}
}
