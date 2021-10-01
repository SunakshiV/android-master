package com.procoin.http.util;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.procoin.http.R;

public class RoadProgressBar extends FrameLayout {
	private ImageView imageView;

	public RoadProgressBar(Context context) {
		super(context);
		initView();
	}

	public RoadProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView();
	}

	public RoadProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public void initView() {
		View view = View.inflate(this.getContext(), R.layout.progress_hud, null);
		this.addView(view);
		imageView = (ImageView) findViewById(R.id.spinnerImageView);
	}

	// TODO
	@Override
	public void setVisibility(int visibility) {
		switch (visibility) {
		case View.VISIBLE:
			show();
			break;

		default:
			break;
		}
		super.setVisibility(visibility);
	}

	private void show() {
		AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
		if (!spinner.isRunning()) {
			spinner.start();
		}
	}

	public void dissmis() {
		this.setVisibility(View.GONE);
	}

}
