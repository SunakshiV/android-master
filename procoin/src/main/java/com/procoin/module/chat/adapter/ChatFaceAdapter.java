package com.procoin.module.chat.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


import com.procoin.module.chat.util.ChatSmileyParser;
import com.procoin.R;

import java.util.ArrayList;
import java.util.List;

public class ChatFaceAdapter extends BaseAdapter {
	private List<String> group;
	Context context;
	// private int page;
	// private int pageSize; // 每页显示的Item个数
	private ChatSmileyParser chatSmileyParser;
	private LayoutInflater inflater;
	private float cSize;

	public ChatFaceAdapter(Context context, List<String> g, int page, int pageSize, ChatSmileyParser chatSmileyParser) {
		this.context = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// this.page = page;
		// this.pageSize = pageSize;
		this.chatSmileyParser = chatSmileyParser;
		group = new ArrayList<String>();
		int i = page * pageSize;
		int iEnd = i + pageSize;
		while ((i < g.size()) && (i < iEnd)) {
			group.add(g.get(i));
			i++;
		}

	}

	public void setcFaceSize(float cSize) {
		this.cSize = cSize;
	}

	public List<String> getGroup() {
		return group;
	}

	public void setGroup(List<String> group) {
		this.group = group;
	}

	@Override
	public int getCount() {
		if (group != null) return group.size();
		else return 0;
	}

	@Override
	public Object getItem(int position) {
		if (group == null) return null;
		return group.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.tjrchat_face_item, null);
			// convertView.setLayoutParams(new GridView.LayoutParams(50, 50));
			convertView.setPadding(5, 5, 5, 5);
			convertView.setLayoutParams(new GridView.LayoutParams(dpToPx(context.getResources(), 32), dpToPx(context.getResources(), 32)));
		}

		Drawable drawable = context.getResources().getDrawable(chatSmileyParser.getmSmileyToRes().get(getItem(position)));
		// if (cSize > 0) drawable.setBounds(0, 0, (int)
		// (drawable.getIntrinsicWidth() * (cSize / 48.0)), (int)
		// (drawable.getIntrinsicHeight() * (cSize / 48.0)));
		((ImageView) convertView.findViewById(R.id.ivFace)).setImageDrawable(drawable);
		return convertView;
	}

	private int dpToPx(Resources res, float dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

}
