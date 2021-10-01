package com.procoin.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class InflaterUtils {
	public static View inflateView(Context context,int resource) {
		return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
	}
}
