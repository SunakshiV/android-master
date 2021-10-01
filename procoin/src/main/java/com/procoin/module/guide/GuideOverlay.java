package com.procoin.module.guide;

import android.graphics.Color;

import com.nineoldandroids.animation.Animator;

/**
 * Created by zhengmengjia on 6/20/15.
 */
public class GuideOverlay {
	public int mBackgroundColor;
	public boolean mDisableClick;
	public Style mStyle;
	public Animator mEnterAnimation, mExitAnimation;

	public enum Style {
		Circle, Rectangle
	}

	public GuideOverlay() {
		this(true, Color.parseColor("#8C000000"), Style.Circle);
	}

	public GuideOverlay(boolean disableClick, int backgroundColor, Style style) {
		mDisableClick = disableClick;
		mBackgroundColor = backgroundColor;
		mStyle = style;
	}

	/**
	 * Set background color
	 * 
	 * @param backgroundColor
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideOverlay setBackgroundColor(int backgroundColor) {
		mBackgroundColor = backgroundColor;
		return this;
	}

	/**
	 * Set to true if you want to block all user input to pass through this
	 * overlay, set to false if you want to allow user input under the overlay
	 * 
	 * @param yes_no
	 * @return return GuideOverlay instance for chaining purpose
	 */
	public GuideOverlay disableClick(boolean yes_no) {
		mDisableClick = yes_no;
		return this;
	}

	public GuideOverlay setStyle(Style style) {
		mStyle = style;
		return this;
	}

	/**
	 * Set enter animation
	 * 
	 * @param enterAnimation
	 * @return return GuideOverlay instance for chaining purpose
	 */
	public GuideOverlay setEnterAnimation(Animator enterAnimation) {
		mEnterAnimation = enterAnimation;
		return this;
	}

	/**
	 * Set exit animation
	 * 
	 * @param exitAnimation
	 * @return return GuideOverlay instance for chaining purpose
	 */
	public GuideOverlay setExitAnimation(Animator exitAnimation) {
		mExitAnimation = exitAnimation;
		return this;
	}
}
