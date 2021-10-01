package com.procoin.common.interfaces;

public interface BaseRequestListener {
	void requestStart();
	void requestComplete(Object result);
}
