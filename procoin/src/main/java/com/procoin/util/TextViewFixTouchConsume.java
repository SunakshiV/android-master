package com.procoin.util;

import android.content.Context;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class TextViewFixTouchConsume extends AppCompatTextView {
	boolean dontConsumeNonUrlClicks = true;
	boolean linkHit;

	public TextViewFixTouchConsume(Context context) {
		super(context);
	}

	public TextViewFixTouchConsume(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TextViewFixTouchConsume(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		MovementMethod mm = getMovementMethod();
//		mm.onTouchEvent(this, (Spannable) getText(), event);
//		if (linkHit) {
//			if (event.getAction() == MotionEvent.ACTION_UP) {
//				return super.onTouchEvent(event);
//			}
//		}
		linkHit = false;
		boolean res = super.onTouchEvent(event);
		if (dontConsumeNonUrlClicks) {
			if (!linkHit) {
				if (isLongClickable()) {
					return true;
				}
			}
			return linkHit;
		}
		return res;

	}

	public static class LocalLinkMovementMethod extends LinkMovementMethod {
		static LocalLinkMovementMethod sInstance;

		public static LocalLinkMovementMethod getInstance() {
			if (sInstance == null) sInstance = new LocalLinkMovementMethod();

			return sInstance;
		}

		// float RawY=0;
		@Override
		public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
			// super.onTouchEvent(widget, buffer, event)

			int action = event.getAction();
			if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
				// if(action ==
				// MotionEvent.ACTION_DOWN){//不加这句点击的时候会触发onLongClick
				// RawY=event.getRawX();
				// try {
				// Thread.sleep(200);
				// } catch (InterruptedException e) {
				// }
				// }
				int x = (int) event.getX();
				int y = (int) event.getY();

				x -= widget.getTotalPaddingLeft();
				y -= widget.getTotalPaddingTop();

				x += widget.getScrollX();
				y += widget.getScrollY();

				Layout layout = widget.getLayout();
				int line = layout.getLineForVertical(y);
				int off = layout.getOffsetForHorizontal(line, x);
				ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
				// Log.d("onTouchEvent", "link.length==" + link.length);
				if (link.length != 0) {
					if (action == MotionEvent.ACTION_UP) {
						link[0].onClick(widget);
					} else if (action == MotionEvent.ACTION_DOWN) {
						Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
					}

					if (widget instanceof TextViewFixTouchConsume) {
						((TextViewFixTouchConsume) widget).linkHit = true;
					}
					return true;
				} else {
					Selection.removeSelection(buffer);
					Touch.onTouchEvent(widget, buffer, event);
					return false;
				}
			}
			return Touch.onTouchEvent(widget, buffer, event);
		}
	}

	@Override
	public boolean hasFocusable() {
		return false;
	}

}
