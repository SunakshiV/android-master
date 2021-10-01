package com.procoin.http.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

/**
 * 判断 sd 卡 在不在时候所做操作的接口
 * 
 * @author zhengmj
 * 
 */
public interface DiskCache {
	public boolean exists(String key);

	public File getFile(String key);

	public InputStream getInputStream(String key) throws IOException;

	public boolean store(String key, InputStream is);

	public void writeFile(File file, Bitmap bitmap);

	public void invalidate(String key);

	public void cleanup();

	public void clear();

	public void writeFile(File file, Bitmap bitmap, boolean recycle) throws Exception;
}
