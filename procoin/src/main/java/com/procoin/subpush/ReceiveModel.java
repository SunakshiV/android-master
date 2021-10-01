package com.procoin.subpush;

public class ReceiveModel {
	public int type;
	public boolean success;
	public Object obj;

	public int pageSize;

	public ReceiveModel(int type, Object obj) {
		this(type,obj,0);
	}
	public ReceiveModel(int type, Object obj,int pageSize) {
		this.type = type;
		this.obj = obj;
		this.pageSize=pageSize;
	}
	public ReceiveModel() {

	}

}
