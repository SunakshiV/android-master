package com.procoin.social.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.social.R;

public class ShareFaceAdapter extends BaseAdapter {
	private AppCompatActivity activity;
	public static final int[] DEFAULT_SMILEY_RES_IDS = { R.drawable.weibo_001, R.drawable.weibo_002, R.drawable.weibo_003, R.drawable.weibo_004, R.drawable.weibo_005, R.drawable.weibo_006, R.drawable.weibo_007, R.drawable.weibo_008, R.drawable.weibo_009, R.drawable.weibo_010, R.drawable.weibo_011, R.drawable.weibo_012, R.drawable.weibo_013, R.drawable.weibo_014, R.drawable.weibo_015, R.drawable.weibo_016, R.drawable.weibo_017, R.drawable.weibo_018, R.drawable.weibo_019, R.drawable.weibo_020, R.drawable.weibo_021, R.drawable.weibo_022, R.drawable.weibo_023, R.drawable.weibo_024, R.drawable.weibo_025, R.drawable.weibo_026, R.drawable.weibo_027, R.drawable.weibo_028, R.drawable.weibo_029, R.drawable.weibo_030, R.drawable.weibo_031, R.drawable.weibo_032, R.drawable.weibo_033,
			R.drawable.weibo_034, R.drawable.weibo_035, R.drawable.weibo_036, R.drawable.weibo_037, R.drawable.weibo_038, R.drawable.weibo_039, R.drawable.weibo_040, R.drawable.weibo_041, R.drawable.weibo_042, R.drawable.weibo_043, R.drawable.weibo_044, R.drawable.weibo_045, R.drawable.weibo_046, R.drawable.weibo_047, R.drawable.weibo_048, R.drawable.weibo_049, R.drawable.weibo_050, R.drawable.weibo_051, R.drawable.weibo_052, R.drawable.weibo_053, R.drawable.weibo_054, R.drawable.weibo_055, R.drawable.weibo_056, R.drawable.weibo_057, R.drawable.weibo_058, R.drawable.weibo_059, R.drawable.weibo_060, R.drawable.weibo_061, R.drawable.weibo_062, R.drawable.weibo_063, R.drawable.weibo_064, R.drawable.weibo_065, R.drawable.weibo_066, R.drawable.weibo_067, R.drawable.weibo_068,
			R.drawable.weibo_069, R.drawable.weibo_070, R.drawable.weibo_071, R.drawable.weibo_072, R.drawable.weibo_073, R.drawable.weibo_074, R.drawable.weibo_075, R.drawable.weibo_076, R.drawable.weibo_077, R.drawable.weibo_078, R.drawable.weibo_079, R.drawable.weibo_080, R.drawable.weibo_081, R.drawable.weibo_082, R.drawable.weibo_083, R.drawable.weibo_084, R.drawable.weibo_085, R.drawable.weibo_086, R.drawable.weibo_087, R.drawable.weibo_088, R.drawable.weibo_089, R.drawable.weibo_090, R.drawable.weibo_091, R.drawable.weibo_092, R.drawable.weibo_093, R.drawable.weibo_094, R.drawable.weibo_095, R.drawable.weibo_096, R.drawable.weibo_097, R.drawable.weibo_098, R.drawable.weibo_099, R.drawable.weibo_100, R.drawable.weibo_101, R.drawable.weibo_102, R.drawable.weibo_103,
			R.drawable.weibo_104 };

	// private String[] mSmileyTexts;

	public ShareFaceAdapter(AppCompatActivity activity) {
		this.activity = activity;
		// mSmileyTexts =
		// activity.getResources().getStringArray(R.array.weibo_face_values);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewholder;
		if (convertView == null) {
			convertView = activity.getLayoutInflater().inflate(R.layout.tjr_social_main_face_item, null);
			viewholder = new ViewHolder(convertView);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
		}
		viewholder.setData(DEFAULT_SMILEY_RES_IDS[position]);
		return convertView;
	}

	@Override
	public int getCount() {
		return DEFAULT_SMILEY_RES_IDS.length;
	}

	@Override
	public Object getItem(int position) {
		return DEFAULT_SMILEY_RES_IDS[position];
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	class ViewHolder {
		private ImageView btnface;

		public ViewHolder(View view) {
			btnface = (ImageView) view.findViewById(R.id.btnface);
		}

		public void setData(int src) {
			btnface.setImageDrawable(activity.getResources().getDrawable(src));
		}
	}
}
