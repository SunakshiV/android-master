package com.procoin.common.base;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.taojin.swipback.SwipeBackLayout;
import com.taojin.swipback.SwipeBackLayout.OnPageScrollEndCallBack;
import com.taojin.swipback.Utils;
import com.taojin.swipback.app.SwipeBackActivityBase;
import com.taojin.swipback.app.SwipeBackActivityHelper;

public class TJRBaseActionBarSwipeBackActivity extends TJRBaseActionBarActivity implements SwipeBackActivityBase {
	private SwipeBackActivityHelper mHelper;
	private boolean mOverrideExitAniamtion = true;
	private boolean mIsFinishing;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHelper = new SwipeBackActivityHelper(this);
		mHelper.onActivityCreate();
		// mHelper.setOnPageScrollEndCallBack(new OnPageScrollEndCallBack() {
		// @Override
		// public void onPageScrollEnd() {
		// if(!isFinishing()){
		// onBackPressed();
		// }
		// }
		// });
		// 默认全屏拖动 ，并且灵敏度要降低
		// setEdgeSize(getScreenWidth());
		// setSensitivity(0.4f);
	}

	public void setOnPageScrollEndCallBack(OnPageScrollEndCallBack onPageScrollEndCallBack) {
		mHelper.setOnPageScrollEndCallBack(onPageScrollEndCallBack);
	}

	// public void setEdgeSize(int size){
	// getSwipeBackLayout().setEdgeSize(size);
	// }
	// public void setSensitivity(float sensitivity){
	// getSwipeBackLayout().setSensitivity(this, sensitivity);
	// }

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mHelper.onPostCreate();
	}

	@Override
	public View findViewById(int id) {
		View v = super.findViewById(id);
		if (v == null && mHelper != null) return mHelper.findViewById(id);
		return v;
	}

	@Override
	public SwipeBackLayout getSwipeBackLayout() {
		return mHelper.getSwipeBackLayout();
	}

	@Override
	public void setSwipeBackEnable(boolean enable) {
		if (getSwipeBackLayout() != null) {
			getSwipeBackLayout().setEnableGesture(enable);
		}
	}

	@Override
	public void scrollToFinishActivity() {
		Utils.convertActivityToTranslucent(this);
		getSwipeBackLayout().scrollToFinishActivity();
	}

	/**
	 * Override Exit Animation
	 * 
	 * @param override
	 */
	public void setOverrideExitAniamtion(boolean override) {
		mOverrideExitAniamtion = override;
	}

	@Override
	public void finish() {
		View rootView = ((ViewGroup) (getWindow().getDecorView().findViewById(android.R.id.content))).getChildAt(0);
//		Log.d("SwipeBack","finish////////////////// mOverrideExitAniamtion=="+mOverrideExitAniamtion+" mIsFinishing=="+mIsFinishing+"  rootView=="+(rootView==null?"null:":"不为null"));
		// 当进入一个页面参数错误的时候，要finish，但还没有设置contentView
		// 所以页面无法完成滚动，也就没法完成回调，所以这里要判断rootView不为空
		if (mOverrideExitAniamtion && !mIsFinishing && rootView != null) {
			mIsFinishing = true;
			scrollToFinishActivity();
			return;
		}
		mIsFinishing = false;
		super.finish();
		//防止有些手机页面关闭后还有默认的动画
		overridePendingTransition(0, 0);
	}


	@Override
	protected void onResume() {
		super.onResume();

//		CommonUtil.LogLa(2, "activity user_name is =========== " + this.getClass().getName());
	}
}
