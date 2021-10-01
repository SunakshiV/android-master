package com.procoin.social.ui;

import java.util.Arrays;
import java.util.List;

import android.R.color;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.procoin.social.R;
import com.procoin.social.ui.adapter.WeiboShareFaceAdapter;
import com.procoin.social.util.ScrollLayoutFace;
import com.procoin.social.util.ScrollLayoutFace.PageListener;

public class WeiboShareFace extends RelativeLayout {
	private View home;
	private ScrollLayoutFace GvFace;
	private LinearLayout layoutBottom;
	private Context context;

	private int allPageCount;// 记录当前页面的总页数
	// private int pageCurrent;
	private static final int PAGE_SIZE = 14;
	private static final int NUMCOLUMNS = 7;
	private GridView gv;

	private ImageView imgCur;
	private String[] faceTexts;
	private ClickFace clickFace;

	public void setClickFace(ClickFace clickFace) {
		this.clickFace = clickFace;
	}

	public WeiboShareFace(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(context);
	}

	public WeiboShareFace(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context);
	}

	public WeiboShareFace(Context context) {
		super(context);
		this.context = context;
		init(context);
	}

	private void init(Context context) {
		faceTexts = getResources().getStringArray(R.array.weibo_face_values);
		home = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.tjr_social_shareface, this);
		layoutBottom = (LinearLayout) home.findViewById(R.id.llCurPoint);
		GvFace = (ScrollLayoutFace) home.findViewById(R.id.GvFace);
		GvFace.setPageListener(new PageListener() {

			@Override
			public void page(int page) {
				setCurPage(page);
			}
		});
		List<String> group = Arrays.asList(faceTexts);
		int size = group.size();
		allPageCount = size % PAGE_SIZE == 0 ? size / PAGE_SIZE : size / PAGE_SIZE + 1;
		setCurPage(0);
		WeiboShareFaceAdapter faceAdapter;
		for (int i = 0; i < allPageCount; i++) {
			gv = new GridView(context);
			gv.setNumColumns(NUMCOLUMNS);
			faceAdapter = new WeiboShareFaceAdapter(context, group, i);
			gv.setAdapter(faceAdapter);
			gv.setSelector(color.transparent);
			gv.setVerticalSpacing(10);
			gv.setPadding(5, 5, 5, 5);
			gv.setOnItemClickListener(gridListener);
			GvFace.addView(gv);
		}
	}

	/**
	 * GridView的监听事件
	 */
	public OnItemClickListener gridListener = new OnItemClickListener() {

		public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
			String faceStr = (String) arg0.getAdapter().getItem(position);
			if (clickFace != null) {
				clickFace.onClickItemFace(faceStr);
			}
		}
	};

	/**
	 * 更新当前页码
	 */
	public void setCurPage(int page) {
		if (layoutBottom.getChildCount() > 0) {
			layoutBottom.removeAllViews();
			layoutBottom.invalidate();
		}
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);
		params.setMargins(10, 10, 10, 10);
		for (int i = 0; i < allPageCount; i++) {
			imgCur = new ImageView(context);
			imgCur.setLayoutParams(params);
			// 判断当前页码来更新
			if (i == page) {
				imgCur.setBackgroundResource(R.drawable.ic_social_curpage);
			} else {
				imgCur.setBackgroundResource(R.drawable.ic_social_page);
			}
			layoutBottom.addView(imgCur);
		}
	}

	public interface ClickFace {
		public void onClickItemFace(String face);
	}

}
