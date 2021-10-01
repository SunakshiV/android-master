package com.procoin.module.guide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MotionEventCompat;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.AnimatorSet;

import java.util.ArrayList;

/**
 * TODO: document your custom view class.
 */
public class FrameLayoutWithGuide extends FrameLayout {
	private TextPaint mTextPaint;
	private AppCompatActivity mActivity;
	// private GuideActionType mMotionType;
	private Paint mEraser;

	Bitmap mEraserBitmap;
	private Canvas mEraserCanvas;
	private Paint mPaint;
	private Paint transparentPaint;
	// private View mViewHole; // This is the targeted view to be highlighted,
	// where the hole should be placed
	private int mRadius;
	private int[] mPos;
	private int mHeight;
	private int mWidth;

	private float mDensity;
	private GuideOverlay mOverlay;

	private ArrayList<AnimatorSet> mAnimatorSetArrayList;

	// /**
	// * This describes the allowable motion, for example if you want the users
	// to
	// * learn about clicking, but want to stop them from swiping, then use
	// * ClickOnly
	// */
	// public enum GuideActionType {
	// AllowAll, ClickOnly, SwipeOnly
	// }
	public void setmOverlay(GuideOverlay mOverlay) {
		this.mOverlay = mOverlay;
	}

	public void setViewHole(View mViewHole) {
		// mViewHole = view;
		// enforceMotionType();
		if (mViewHole != null) {
			int[] pos = new int[2];
			mViewHole.getLocationOnScreen(pos);
//			mViewHole.getLocationInWindow();
			setViewHole(pos, mViewHole.getHeight(), mViewHole.getWidth(), 0);
		}

	}

	public void setViewHole(int[] pos, int height, int width, int radius) {
		mPos = pos;
		mHeight = height;
		mWidth = width;
		mRadius = radius;
		mDensity = mActivity.getResources().getDisplayMetrics().density;
		int padding = (int) (20 * mDensity);
		if (height > width) {
			mRadius = height / 2 + padding;
		} else {
			mRadius = width / 2 + padding;
		}
//		CommonUtil.LogLa(2, "setViewHole is "+ mPos +" height is "+mHeight +" width is "+mWidth +" mRadius is "+mRadius);

	}

	public void addAnimatorSet(AnimatorSet animatorSet) {
		if (mAnimatorSetArrayList == null) {
			mAnimatorSetArrayList = new ArrayList<AnimatorSet>();
		}
		mAnimatorSetArrayList.add(animatorSet);
	}

	// private void enforceMotionType() {
	// Log.d("tourguide", "enforceMotionType 1");
	// if (mViewHole != null) {
	// Log.d("tourguide", "enforceMotionType 2");
	// if (mMotionType != null && mMotionType == GuideActionType.ClickOnly) {
	// Log.d("tourguide", "enforceMotionType 3");
	// Log.d("tourguide", "only Swiping");
	// mViewHole.setOnTouchListener(new OnTouchListener() {
	// @Override
	// public boolean onTouch(View view, MotionEvent motionEvent) {
	// mViewHole.getParent().requestDisallowInterceptTouchEvent(true);
	// return false;
	// }
	// });
	// } else if (mMotionType != null && mMotionType ==
	// GuideActionType.SwipeOnly) {
	// Log.d("tourguide", "enforceMotionType 4");
	// Log.d("tourguide", "only Swiping");
	// mViewHole.setClickable(false);
	// }
	// }
	// }

	public FrameLayoutWithGuide(AppCompatActivity context) {
		this(context, null, new GuideOverlay());
	}

	public FrameLayoutWithGuide(AppCompatActivity context, View view, GuideOverlay overlay) {
		super(context);
		mActivity = context;
		init(null, 0);
		// enforceMotionType();
		mOverlay = overlay;
		setViewHole(view);
		// mMotionType = motionType;
	}

	private void init(AttributeSet attrs, int defStyle) {
		// Load attributes
		// final TypedArray a = getContext().obtainStyledAttributes(
		// attrs, FrameLayoutWithHole, defStyle, 0);
		//
		//
		// a.recycle();
		setWillNotDraw(false);
		// Set up a default TextPaint object
		mTextPaint = new TextPaint();
		mTextPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mTextPaint.setTextAlign(Paint.Align.LEFT);

		Point size = new Point();
		size.x = mActivity.getWindowManager().getDefaultDisplay().getWidth();
		size.y = mActivity.getWindowManager().getDefaultDisplay().getHeight();

		mEraserBitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.ARGB_8888);
		mEraserCanvas = new Canvas(mEraserBitmap);

		mPaint = new Paint();
		mPaint.setColor(0xcc000000);
		mPaint.setAntiAlias(true); // 去掉锯齿
		transparentPaint = new Paint();
		transparentPaint.setColor(getResources().getColor(android.R.color.transparent));
		transparentPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		transparentPaint.setAntiAlias(true); // 去掉锯齿
		mEraser = new Paint();
		mEraser.setColor(0xFFFFFFFF);
		mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
		mEraser.setAntiAlias(true); // 去掉锯齿
		Log.d("tourguide", "getHeight: " + size.y);
		Log.d("tourguide", "getWidth: " + size.x);

	}

	private boolean mCleanUpLock = false;

	protected void cleanUp() {
		if (getParent() != null) {
			if (mOverlay != null && mOverlay.mExitAnimation != null) {
				performOverlayExitAnimation();
			} else {
				((ViewGroup) this.getParent()).removeView(this);
			}
		}
	}

	private void performOverlayExitAnimation() {
		if (!mCleanUpLock) {
			final FrameLayout _pointerToFrameLayout = this;
			mCleanUpLock = true;
			Log.d("tourguide", "GuideOverlay exit animation listener is overwritten...");
			if (mOverlay.mExitAnimation != null) {
				mOverlay.mExitAnimation.addListener(new AnimatorListener() {

					@Override
					public void onAnimationStart(Animator arg0) {

					}

					@Override
					public void onAnimationRepeat(Animator arg0) {

					}

					@Override
					public void onAnimationEnd(Animator arg0) {
						((ViewGroup) _pointerToFrameLayout.getParent()).removeView(_pointerToFrameLayout);
					}

					@Override
					public void onAnimationCancel(Animator arg0) {

					}
				});
				// setAnimationListener(new Animation.AnimationListener() {
				// @Override
				// public void onAnimationStart(Animation animation) {
				// }
				//
				// @Override
				// public void onAnimationRepeat(Animation animation) {
				// }
				//
				// @Override
				// public void onAnimationEnd(Animation animation) {
				//
				// }
				// });
				// this.startAnimation(mOverlay.mExitAnimation);
				mOverlay.mExitAnimation.setTarget(this);
				mOverlay.mExitAnimation.start();
			}
		}
	}

	/* comment this whole method to cause a memory leak */
	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		/* cleanup reference to prevent memory leak */
		mEraserCanvas.setBitmap(null);
		mEraserBitmap = null;

		if (mAnimatorSetArrayList != null && mAnimatorSetArrayList.size() > 0) {
			for (int i = 0; i < mAnimatorSetArrayList.size(); i++) {
				mAnimatorSetArrayList.get(i).end();
				mAnimatorSetArrayList.get(i).removeAllListeners();
			}
		}
	}

	/** Show an event in the LogCat view, for debugging */
	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_").append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN || actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid ").append(action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")");
		}
		sb.append("[");
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#").append(i);
			sb.append("(pid ").append(event.getPointerId(i));
			sb.append(")=").append((int) event.getX(i));
			sb.append(",").append((int) event.getY(i));
			if (i + 1 < event.getPointerCount()) sb.append(";");
		}
		sb.append("]");
		Log.d("tourguide", sb.toString());
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// first check if the location button should handle the touch event
		dumpEvent(ev);
		int action = MotionEventCompat.getActionMasked(ev);

		if (mPos != null) {
			// int[] pos = new int[2];
			// mViewHole.getLocationOnScreen(pos);
			Log.d("tourguide", "[dispatchTouchEvent] mViewHole.getHeight(): " + mHeight);
			Log.d("tourguide", "[dispatchTouchEvent] mViewHole.getWidth(): " + mWidth);

			Log.d("tourguide", "[dispatchTouchEvent] Touch X(): " + ev.getRawX());
			Log.d("tourguide", "[dispatchTouchEvent] Touch Y(): " + ev.getRawY());

			// Log.d("tourguide", "[dispatchTouchEvent] X of image: "+pos[0]);
			// Log.d("tourguide", "[dispatchTouchEvent] Y of image: "+pos[1]);

			Log.d("tourguide", "[dispatchTouchEvent] X lower bound: " + mPos[0]);
			Log.d("tourguide", "[dispatchTouchEvent] X higher bound: " + (mPos[0] + mWidth));

			Log.d("tourguide", "[dispatchTouchEvent] Y lower bound: " + mPos[1]);
			Log.d("tourguide", "[dispatchTouchEvent] Y higher bound: " + (mPos[1] + mHeight));

			if (ev.getRawY() >= mPos[1] && ev.getRawY() <= (mPos[1] + mHeight) && ev.getRawX() >= mPos[0] && ev.getRawX() <= (mPos[0] + mWidth)) { // location
																																					// button
																																					// event
				Log.d("tourguide", "to the BOTTOM!");
				Log.d("tourguide", "" + ev.getAction());

				// switch(action) {
				// case (MotionEvent.ACTION_DOWN) :
				// Log.d("tourguide","Action was DOWN");
				// return false;
				// case (MotionEvent.ACTION_MOVE) :
				// Log.d("tourguide","Action was MOVE");
				// return true;
				// case (MotionEvent.ACTION_UP) :
				// Log.d("tourguide","Action was UP");
				// //
				// ev.setAction(MotionEvent.ACTION_DOWN|MotionEvent.ACTION_UP);
				// // return super.dispatchTouchEvent(ev);
				// return false;
				// case (MotionEvent.ACTION_CANCEL) :
				// Log.d("tourguide","Action was CANCEL");
				// return true;
				// case (MotionEvent.ACTION_OUTSIDE) :
				// Log.d("tourguide","Movement occurred outside bounds " +
				// "of current screen element");
				// return true;
				// default :
				// return super.dispatchTouchEvent(ev);
				// }
				// return mViewHole.onTouchEvent(ev);

				return false;
			}
		}
		return super.dispatchTouchEvent(ev);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		mEraserBitmap.eraseColor(Color.TRANSPARENT);
//		CommonUtil.LogLa(2, "fl onDraw ");
		if (mOverlay != null && mPos != null) {
//			CommonUtil.LogLa(2, " mPos is " + mPos);
			mEraserCanvas.drawColor(mOverlay.mBackgroundColor);
			int padding = (int) (10 * mDensity);
			if (mOverlay.mStyle == GuideOverlay.Style.Rectangle) {
				mEraserCanvas.drawRect(mPos[0] - padding, mPos[1] - padding, mPos[0] + mWidth + padding, mPos[1] + mHeight + padding, mEraser);
			} else {
				mEraserCanvas.drawCircle(mPos[0] + mWidth / 2, mPos[1] + mHeight / 2, mRadius, mEraser);
			}
		}
		canvas.drawBitmap(mEraserBitmap, 0, 0, null);

	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		if (mOverlay != null && mOverlay.mEnterAnimation != null) {
			mOverlay.mEnterAnimation.setTarget(this);
			mOverlay.mEnterAnimation.start();
			// this.startAnimation(mOverlay.mEnterAnimation);
		}
	}

	/**
	 * 
	 * Convenient method to obtain screen width in pixel
	 * 
	 * @param activity
	 * @return screen width in pixel
	 */
	public int getScreenWidth(AppCompatActivity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getWidth();
	}

	/**
	 * 
	 * Convenient method to obtain screen height in pixel
	 * 
	 * @param activity
	 * @return screen width in pixel
	 */
	public int getScreenHeight(AppCompatActivity activity) {
		Display display = activity.getWindowManager().getDefaultDisplay();
		return display.getHeight();
	}
}
