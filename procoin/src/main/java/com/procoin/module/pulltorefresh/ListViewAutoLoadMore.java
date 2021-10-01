package com.procoin.module.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;


/**
 *  自动加载更多的listVIew  新闻详细页 专用
 *
 * @author zt
 *
 */
public class ListViewAutoLoadMore extends ListView {
	//	private FootMode currMode = FootMode.getDefault();
//	private boolean footLoading;
	private boolean footDividerEnable;
	private AutoLoadMoreLayout autoLoadMoreLayout;
	private FootLoadTask footLoadTask;
	private Onscroll onscroll;
	private FullClickCallback fullClickCallback;
	public void setFullClickCallback(FullClickCallback fullClickCallback){
		this.fullClickCallback = fullClickCallback;
	}
	public ListViewAutoLoadMore(Context context, AttributeSet attrs,
								int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	public ListViewAutoLoadMore(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public ListViewAutoLoadMore(Context context) {
		super(context);
		init();
	}

	public void setFootDividerEnable(boolean footDividerEnable) {
		this.footDividerEnable = footDividerEnable;
		autoLoadMoreLayout.setListFootDivederEnable(footDividerEnable);
	}

	public void setFootTextColor(int color){
		if(autoLoadMoreLayout!=null)autoLoadMoreLayout.setFootTexColor(color);
	}

	public void setOnscroll(Onscroll onscroll) {
		this.onscroll = onscroll;
	}

	public void setFootLoadTask(FootLoadTask footLoadTask) {
		this.footLoadTask = footLoadTask;
	}

	private void init() {
		setOnScrollListener(new OnScrollListener() {
			//			int scrollState;
			boolean mLastItemVisible ;

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				this.scrollState = scrollState;
//				int lastVisibleItem = firstVisibleItem + visibleItemCount - 1; // 可视的最后一个列表项
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && null != footLoadTask && mLastItemVisible) {
					if (canLoad()) {
						Log.d("startFootLoadTask","startFootLoadTask////////");
						autoLoadMoreLayout.setValueByState(AutoLoadMoreLayout.FootMode.LOADING);
						footLoadTask.startFootLoadTask();
//						footLoading = true;
					}

				}
				if(scrollState==OnScrollListener.SCROLL_STATE_TOUCH_SCROLL&&onscroll!=null)onscroll.scroll();

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
				mLastItemVisible= (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount - 1);
			}
		});
	}

	public void onFootReset() {
		if (autoLoadMoreLayout != null) {
			autoLoadMoreLayout.setValueByState(AutoLoadMoreLayout.FootMode.INITIALISE);
		}
	}
	private boolean canLoad() {
		if(autoLoadMoreLayout!=null){
			return autoLoadMoreLayout.canLoad();
		}
		return false;
	}

	public void onFootLoadComplete(boolean isSuccess, boolean isComplete) {
//		footLoading = false;
		if (autoLoadMoreLayout != null) {
			if (!isSuccess) {
				autoLoadMoreLayout
						.setValueByState(AutoLoadMoreLayout.FootMode.FAIL);
			} else {
				autoLoadMoreLayout
						.setValueByState(isComplete ? AutoLoadMoreLayout.FootMode.COMPLETE
								: AutoLoadMoreLayout.FootMode.NORMAL);

			}

		}
	}
	public void onArrayFull(boolean isSuccess,boolean isComplete){
		if (autoLoadMoreLayout != null) {
			if (!isSuccess) {
				autoLoadMoreLayout
						.setValueByState(AutoLoadMoreLayout.FootMode.FAIL);
			} else {
//				autoLoadMoreLayout
//						.setValueByState(isComplete ? AutoLoadMoreLayout.FootMode.COMPLETE
//								: AutoLoadMoreLayout.FootMode.NORMAL);
				if (isComplete){
					setFullStatus();
				}else {
					autoLoadMoreLayout.setValueByState(AutoLoadMoreLayout.FootMode.COMPLETE);
				}
			}

		}
	}
	public void setFullStatus(){
		autoLoadMoreLayout.setValueByState(AutoLoadMoreLayout.FootMode.FULL);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (autoLoadMoreLayout == null) {
			autoLoadMoreLayout = new AutoLoadMoreLayout(getContext(),
					new AutoLoadMoreLayout.OnLoadFailClick() {
						@Override
						public void onClickToReLoad() {
							if (footLoadTask != null)
								footLoadTask.startFootLoadTask();
						}
					}, AutoLoadMoreLayout.FootMode.INITIALISE,footDividerEnable);
			addFooterView(autoLoadMoreLayout);
		}
		autoLoadMoreLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (autoLoadMoreLayout.getCurrMode() == AutoLoadMoreLayout.FootMode.FULL&&fullClickCallback!=null){
					fullClickCallback.onClick();
				}
			}
		});
		super.setAdapter(adapter);
	}
	public interface FullClickCallback{
		void onClick();
	}
	public static interface FootLoadTask {
		public void startFootLoadTask();
	}
	public static interface Onscroll{
		public void scroll();
	}

}
