package com.procoin.module.guide;

import com.nineoldandroids.animation.Animator;

import android.graphics.Color;
import android.view.Gravity;

/**
 * Created by tanjunrong on 6/17/15.
 */
public class GuideToolTip {
	public String mTitle, mDescription;
	public int mBackgroundColor, mTextColor;
	public Animator mEnterAnimation, mExitAnimation;
	public boolean mShadow;
	public int mGravity;

	public GuideToolTip() {
		/* default values */
		mTitle = "";
		mDescription = "";
		mBackgroundColor = Color.TRANSPARENT; // Color.parseColor("#3498db");
		mTextColor = Color.parseColor("#00a1f2");

		// mEnterAnimation = new AlphaAnimation(0f, 1f);
		// mEnterAnimation.setDuration(1000);
		// mEnterAnimation.setFillAfter(true);
		// mEnterAnimation.setInterpolator(new BounceInterpolator());
		mShadow = true;

		// TODO: exit animation
		mGravity = Gravity.CENTER;
	}

	/**
	 * Set title text
	 * 
	 * @param title
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setTitle(String title) {
		mTitle = title;
		return this;
	}

	/**
	 * Set description text
	 * 
	 * @param description
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setDescription(String description) {
		mDescription = description;
		return this;
	}

	/**
	 * Set background color
	 * 
	 * @param backgroundColor
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setBackgroundColor(int backgroundColor) {
		mBackgroundColor = backgroundColor;
		return this;
	}

	/**
	 * Set text color
	 * 
	 * @param textColor
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setTextColor(int textColor) {
		mTextColor = textColor;
		return this;
	}

	/**
	 * Set enter animation
	 * 
	 * @param enterAnimation
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setEnterAnimation(Animator enterAnimation) {
		mEnterAnimation = enterAnimation;
		return this;
	}

	/**
	 * Set exit animation
	 * 
	 * @param exitAnimation
	 * @return return GuideToolTip instance for chaining purpose
	 */
	// TODO:
	// public GuideToolTip setExitAnimation(Animation exitAnimation){
	// mExitAnimation = exitAnimation;
	// return this;
	// }
	/**
	 * Set the gravity, the setGravity is centered relative to the targeted
	 * button
	 * 
	 * @param gravity
	 *            Gravity.CENTER, Gravity.TOP, Gravity.BOTTOM, etc
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setGravity(int gravity) {
		mGravity = gravity;
		return this;
	}

	/**
	 * Set if you want to have setShadow
	 * 
	 * @param shadow
	 * @return return GuideToolTip instance for chaining purpose
	 */
	public GuideToolTip setShadow(boolean shadow) {
		mShadow = shadow;
		return this;
	}
}
