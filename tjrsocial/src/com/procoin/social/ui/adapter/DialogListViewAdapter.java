package com.procoin.social.ui.adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.http.base.baseadapter.AMBaseAdapter;
import com.procoin.social.R;
import com.procoin.social.entity.WeiboAtName;

@SuppressLint("ParserError")
public class DialogListViewAdapter extends AMBaseAdapter<WeiboAtName> {
	private AppCompatActivity activity;

	public DialogListViewAdapter(AppCompatActivity activity) {
		this.activity = activity;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(R.layout.tjr_social_main_dialog_item, null);
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.setDate(getItem(position));

		return convertView;
	}

	class ViewHolder {
		private TextView atweibo;

		public ViewHolder(View view) {
			atweibo = (TextView) view.findViewById(R.id.tvWeiboname);
		}

		public void setDate(WeiboAtName name) {
			atweibo.setText(name.screen_name);
		}
	}

}
