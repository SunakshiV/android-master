package com.procoin.widgets.index;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;


import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;

import java.util.HashMap;

public class FriendIndexUI {
	private Context context;
	private IndexUIView lvIndex;
	private TextView tvPop;
	private HashMap<String, Integer> indexMap = new HashMap<String, Integer>();

	private IndexUpdate inUpdate;
	private IndexComparator indexComparator;
	// private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {

		@Override
		public void run() {
			tvPop.setVisibility(View.GONE);
		}
	};

	public FriendIndexUI(Context context, IndexUpdate inUpdate) {
		this.context = context;
		this.inUpdate = inUpdate;
		indexComparator = new IndexComparator();
	}

	public void setIndexMap(HashMap<String, Integer> indexMap) {
		this.indexMap = indexMap;
	}

	/**
	 * 加载页面
	 * 
	 * @param resource
	 * @return
	 */
	private View inflateView(int resource, Context context) {
		return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
	}

	public View showView() {
		View view = inflateView(R.layout.tjrfriend_friend_index, context);

		lvIndex = (IndexUIView) view.findViewById(R.id.lvIndex);
		lvIndex.setOnTouchingLetterChangedListener(new IndexUIView.OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// if (indexMap.containsKey(s)) {
				updateindex(s);
				// }
			}

			@Override
			public void onTouchUp() {
				tvPop.setVisibility(View.GONE);
			}
		});
		tvPop = (TextView) view.findViewById(R.id.tvPop);
		TextPaint tp = tvPop.getPaint();
		tp.setFakeBoldText(true);
		tvPop.setVisibility(View.GONE);
		return view;
	}

	private void updateindex(String s) {
		tvPop.setVisibility(View.VISIBLE);
		tvPop.setText(s);
		// handler.removeCallbacks(runnable);
		// handler.postDelayed(runnable, 200);
		if (inUpdate != null) {
			if (indexMap.containsKey(s)) {
				inUpdate.indexUpdate(indexMap.get(s));
			}
		}
	}

	public void setDate(Group<User> group) {
		if (group != null) {
			lvIndex.ClearChoose();
			indexMap.clear();
			// 把data集合里的所有包含中文的字符串转为英文后，存入另一个新的list集合
			int i = 0;
			for (User user : group) {
				String str = (user.getUserName() == null ? "" : user.getUserName());
				String newStr = getPingYinSpell(str);
				if (newStr == null || newStr.length() == 0) newStr = "#";
				char startChar = newStr.charAt(0);
				int startInt = (int) startChar;
				// 如果字符以A-Z a-z开头，则存入索引集合和position集合
				if ((startInt > 64 && startInt < 91) || (startInt > 96 && startInt < 123)) {
					// 如果索引值和当前字符串的首字母相同（忽略大小写）则不做任何处理
					if (indexMap.containsKey(newStr)) {

					} else {
						indexMap.put(newStr, i);
					}
				} else {
					if (!indexMap.containsKey(newStr)) {
						indexMap.put("#", i);
					}
				}
				i++;
			}
		}
	}

	public interface IndexUpdate {
		public void indexUpdate(int position);
	}

	/**
	 * 获取 中文姓 名的首字母
	 * 
	 *            中文的名字
	 * @return
	 */
	public String getPingYinSpell(String src) {
		// if (src == null || "".equals(src) || src.length() == 0) return "#";
		return indexComparator.getPingYinSpell(src);
	}
}
