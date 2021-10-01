package com.procoin.module.pulltorefresh;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.R;


public class AutoLoadMoreLayout extends FrameLayout implements IAutoLoadMoreLayout {
	public Context context;
	private FootMode currMode;
	private ProgressBar pb;
	private TextView tvText;
	private View viewFootDivider;
	private OnLoadFailClick onLoadFailClick;

	public AutoLoadMoreLayout(Context context) {
		this(context, null);
	}

	public AutoLoadMoreLayout(Context context, OnLoadFailClick onLoadFailClick) {
		this(context, onLoadFailClick, null);
	}

	public AutoLoadMoreLayout(Context context, OnLoadFailClick onLoadFailClick,
							  FootMode mode) {
		this(context, onLoadFailClick, mode, true);
	}

	public AutoLoadMoreLayout(Context context, OnLoadFailClick onLoadFailClick,
							  FootMode mode, boolean footDividerEnable) {
		super(context);
		this.context = context;
		this.currMode = mode;
		this.onLoadFailClick = onLoadFailClick;
		init(context);
		setListFootDivederEnable(footDividerEnable);
	}
	public FootMode getCurrMode() {
		return currMode;
	}
	private void init(Context context) {
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.load_foot, this);
		pb = (ProgressBar) findViewById(R.id.pb);
		tvText = (TextView) findViewById(R.id.tvText);
		viewFootDivider = findViewById(R.id.ViewFootDivider);
		setValueByState(currMode);
		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (currMode) {
					case FAIL:
						if (onLoadFailClick != null) {
							setValueByState(FootMode.LOADING);
							onLoadFailClick.onClickToReLoad();
						}
						break;

					default:
						break;
				}
			}
		});
	}
	/**
	 *
	 * @param mode2
	 *            是否是第一次赋值
	 */
	public void setValueByState(FootMode mode2) {
		currMode = mode2 == null ? FootMode.INITIALISE : mode2;
		if (null != tvText) {
			tvText.setText(mode2.resText == 0 ? "" : context
					.getString(mode2.resText));
		}
		if (getVisibility() == View.GONE)
			setVisibility(View.VISIBLE);
		switch (mode2) {
			case INITIALISE:
				setVisibility(View.GONE);
				break;
			case NORMAL:
				pb.setVisibility(View.GONE);
				break;
			case LOADING:
				pb.setVisibility(View.VISIBLE);
				break;
			case FAIL:
				pb.setVisibility(View.GONE);
				break;
			case COMPLETE:
				pb.setVisibility(View.GONE);
				break;
			case FULL:
				pb.setVisibility(View.GONE);
				break;
			default:
				break;
		}
		this.currMode = mode2;
	}

	@Override
	public void setFootBackGroundColor(int resId) {
		setBackgroundResource(resId);
	}

	@Override
	public void setFootTexColor(int color) {
		if (null != tvText) {
			tvText.setTextColor(color);
		}
	}

	public interface OnLoadFailClick {
		void onClickToReLoad();
	}

	public static enum FootMode {
		INITIALISE(0x1, 0), // 初始化状态，刚加进来为隐藏
		NORMAL(0x2, R.string.auto_load_more_initialise_label), // 正常状态，文字为松手加载
		LOADING(0x3, R.string.auto_load_more_loading_label), // 正在加载
		FAIL(0x4, R.string.auto_load_more_fail_label), // 加载时失败状态，可点击重新加载
		COMPLETE(0x5, R.string.auto_load_more_complete_label),// 已加载全部
		NODATA(0x6,R.string.auto_load_more_nodata_label),//无数据
		FULL(0x7,R.string.auto_load_more_full);

		int value;
		int resText;

		FootMode(int value, int resText) {
			this.value = value;
			this.resText = resText;
		}

		static FootMode getDefault() {
			return INITIALISE;
		}

		public int getResText() {
			return resText;
		}

		public int getValue() {
			return value;
		}
	}

	@Override
	public void setListFootDivederEnable(boolean enable) {
		if (viewFootDivider != null)
			viewFootDivider.setVisibility(enable ? View.VISIBLE : View.GONE);
	}

	public boolean canLoad() {
		return currMode!=FootMode.COMPLETE&&currMode!=FootMode.LOADING;
	}

	public boolean isFootLoadComplete() {
		return currMode==FootMode.COMPLETE;
	}

	@Override
	public void setListFootDivederDrawableRes(int res) {
		if (viewFootDivider != null)
			viewFootDivider.setBackgroundResource(res);
	}

}
