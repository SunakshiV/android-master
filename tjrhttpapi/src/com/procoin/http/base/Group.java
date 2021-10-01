package com.procoin.http.base;

import java.util.ArrayList;
import java.util.Collection;

public class Group<T extends TaojinluType> extends ArrayList<T> implements TaojinluType {

	private static final long serialVersionUID = 1L;

	private String mType;

	public Group() {
		super();
	}
	public Group(int i){
		super(i);
	}

	public Group(Collection<T> collection) {
		super(collection);
	}
	

	public void setType(String type) {
		mType = type;
	}

	public String getType() {
		return mType;
	}
}