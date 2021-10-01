package com.procoin.http.resource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;

public class NullDiskCache implements DiskCache {

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub

	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean exists(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public File getFile(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getInputStream(String key) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void invalidate(String key) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean store(String key, InputStream is) {
		return false;
		// TODO Auto-generated method stub
	}

	@Override
	public void writeFile(File file, Bitmap bitmap) {
		// TODO Auto-generated method stub
	}

	@Override
	public void writeFile(File file, Bitmap bitmap, boolean recycle) {
		// TODO Auto-generated method stub

	}

}
