package com.procoin.http.popui;

import java.lang.ref.SoftReference;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.http.R;

public class CommPopupWindow {

	private int itemTextSize = 18;

	private ImageView iv;// 箭头
	private Bitmap bitmap;
	private int llbg;
	private int pointBg;
	private int pointPosition = 2;// 默认右边
	private int itemBg = R.drawable.xml_pop_item_bg;
	private String[] content;
	private int[] pic;
	private int picPosition = 0;// 图片默认在左边
	private int textColor = -1;// -1代表为白色
	private int itemLineColor = -1;// -1代表为白色
	private int textGravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;

	private int animationStyle = -1;// pop动画，默认没有
	private LinearLayout linearLayout;
	private int textLeftAndRight = 0;// 左右边距

	private int textTopAndBottom = 0;// 上下边距

	private Context context;

	private PopupWindow pw;

	public PopupWindow getPw() {
		return pw;
	}

	public void setLinearLayoutHeight(int height) {
		if (linearLayout != null) {
			ViewGroup.LayoutParams lp = linearLayout.getLayoutParams();
			lp.height = height;
			linearLayout.setLayoutParams(lp);
		}
	}

	private CallBack callBack;
	private int popMaxHeight = WindowManager.LayoutParams.WRAP_CONTENT;// pop高的最大值，当选项过多时，页面显示不完，就需要设置这个值

	private int dpToPx(Resources res, int dp) {
		return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
	}

	public void setPopMaxHeight(int popMaxHeight) {
		this.popMaxHeight = popMaxHeight;
	}

	public void setTextGravity(int textGravity) {
		this.textGravity = textGravity;
	}

	public void setTextLeftAndRight(int textLeftAndRight) {
		this.textLeftAndRight = dpToPx(context.getResources(), textLeftAndRight);
		;
	}

	public void setTextTopAndBottom(int textTopAndBottom) {
		this.textTopAndBottom = dpToPx(context.getResources(), textTopAndBottom);
	}

	public void setItemTextSize(int itemTextSize) {
		this.itemTextSize = itemTextSize;
	}

	public void setPointPosition(int pointPosition) {
		this.pointPosition = pointPosition;
	}

	public void setItemBg(int itemBg) {
		this.itemBg = itemBg;
	}

	public void setPicPosition(int picPosition) {
		this.picPosition = picPosition;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public void setItemLineColor(int itemLineColor) {
		this.itemLineColor = itemLineColor;
	}

	public void setAnimationStyle(int animationStyle) {
		this.animationStyle = animationStyle;
	}

	/**
	 * 
	 * 如果想改变默认的值 在init（）之前调用set方法
	 * 
	 * 
	 * @param llbg
	 *            线性布局的背景
	 * @param pointBg
	 * 
	 * @param pointPosition
	 *            箭头位置 012分别代表左中右 箭头 -1代表没有
	 * @param itemBg
	 *            每个item的背景 可为selector
	 * @param content
	 *            每个item的文字
	 * @param pic每个item图片
	 * @param picPosition每个item前面的图片的位置0123分别代表左上右下
	 * @param textColor每个item的文字颜色
	 *            白色值为-1
	 * @param itemLineColor
	 *            每个item之间线的颜色
	 * @param context
	 */
	public CommPopupWindow(int llbg, int pointBg, String[] content, int[] pic, Context context, CallBack callBack) {
		this.llbg = llbg;
		this.pointBg = pointBg;
		this.content = content;
		this.pic = pic;
		this.context = context;
		this.callBack = callBack;
		itemLineColor = context.getResources().getColor(R.color.c5a5a5a);// 默认灰色
		textLeftAndRight = dpToPx(context.getResources(), 15);
		textTopAndBottom = dpToPx(context.getResources(), 8);
		// init();
	}

	/**
	 * 图标旋转的角度
	 * 
	 * @param rotate
	 */
	public void setPointRotate(float rotate) {
		if (pointBg != -1) {
			Matrix matrix = new Matrix();
			matrix.setRotate(rotate);
			if (iv != null) {
				bitmap = BitmapFactory.decodeResource(context.getResources(), pointBg);
				bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
				iv.setImageBitmap(bitmap);
			}
		}
	};

	public void setPointMarignBttom(int dp) {
		if (iv != null) {
			LayoutParams lp = (LayoutParams) iv.getLayoutParams();
			lp.bottomMargin = dpToPx(context.getResources(), dp);
			iv.setLayoutParams(lp);
		}

	}

	// public CommPopupWindow(String[] content, int[] pic,Context
	// context,CallBack callBack) {
	// this.llbg = R.drawable.ic_marixmenu_bg;
	// this.pointBg = R.drawable.ic_quotation_menu_triangle;
	// this.pointPosition = 2;
	// this.itemBg = -1;
	// this.content = content;
	// this.pic = pic;
	// this.picPosition = 0;
	// this.textColor = context.getResources().getColor(R.color.white);
	// this.context = context;
	// this.callBack = callBack;
	// init();
	// }

	/**
	 * @param pointPosition
	 *            箭头的位置
	 * @param itemBg
	 *            每个item单击的背景
	 * @param picPosition
	 *            每个item前面的图片的位置0123分别代表左上右下
	 * @param textColor
	 *            每个item的文字颜色 白色值为-1
	 * @param itemLineColor
	 *            每个item之间线的颜色
	 * @param itemTextSize
	 *            每个item文字的大小
	 */

	public void init() {
		if (content == null) return;
		LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		linearLayout = (LinearLayout) inf.inflate(R.layout.pop_layout, null);
		LinearLayout ll = (LinearLayout) linearLayout.findViewById(R.id.ll);
		// ll.setPadding(linearLayoutLeft, 0, linearLayoutRight, 0);
		ll.setBackgroundResource(llbg);

		iv = (ImageView) linearLayout.findViewById(R.id.ivPoint);
		iv.setScaleType(ScaleType.MATRIX);
		if (pointBg == -1) {
			iv.setVisibility(View.GONE);
		} else {
			iv.setImageResource(pointBg);
			// TODO设置位于左中右
			LayoutParams lp = (LayoutParams) iv.getLayoutParams();
			switch (pointPosition) {
			case 0:
				lp.gravity = Gravity.LEFT;
				break;
			case 1:
				lp.gravity = Gravity.CENTER;
				break;
			case 2:
				lp.gravity = Gravity.RIGHT;
				break;
			default:
				break;
			}

			iv.setLayoutParams(lp);
		}
		if (content.length > 0) {
			TextView textView = null;
			ImageView imageView = null;
			LayoutParams tvlp = null;
			LayoutParams ivlp = null;
			for (int i = 0; i < content.length; i++) {
				textView = new TextView(context);
				textView.setPadding(textLeftAndRight, textTopAndBottom, textLeftAndRight, textTopAndBottom);
				textView.setTextColor(textColor);
				textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, itemTextSize);
				if (tvlp == null) tvlp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				textView.setLayoutParams(tvlp);
				textView.setGravity(textGravity);
				textView.setBackgroundResource(itemBg);
				if (pic != null && pic.length > 0) {
					if (pic[i] != -1) {
						setCompoundDrawables(context, textView, pic[i], picPosition);
					}
				}
				textView.setText(content[i]);
				textView.setClickable(true);
				textView.setOnClickListener(new Onclick(i));
				ll.addView(textView);
				if (ivlp == null) ivlp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, 1);

				imageView = new ImageView(context);
				imageView.setLayoutParams(ivlp);
				imageView.setBackgroundColor(itemLineColor);
				if (i < (content.length - 1)) ll.addView(imageView);

			}

		}
		pw = new PopupWindow(linearLayout, WindowManager.LayoutParams.WRAP_CONTENT, popMaxHeight);
		pw.setOutsideTouchable(false);
		pw.setBackgroundDrawable(new BitmapDrawable());// 特别留意这个东东
		pw.setFocusable(true);// 如果不加这个，Grid不会响应ItemClick
		if (animationStyle != -1) pw.setAnimationStyle(animationStyle);
	}

	class Onclick implements View.OnClickListener {
		private int item;

		@Override
		public void onClick(View v) {
			pw.dismiss();
			if (callBack != null) callBack.onclick(item);
		}

		public Onclick(int item) {
			this.item = item;
		}
	}

	public void showPop(View v, int xoff, int yoff) {
		if (pw != null) pw.showAsDropDown(v, xoff, yoff);
	}

	public void showPopAtLocation(View v, int gravity, int xoff, int yoff) {
		if (pw != null)
		// pw.showAsDropDown(v, xoff, yoff);
		pw.showAtLocation(v, gravity, xoff, yoff);
	}

	/**
	 * 下面菜单的图片
	 * 
	 * @param context
	 * @param view
	 *            Button
	 * @param resource
	 * @param alignsize
	 *            0 left , 1 top ,2 right ,3 buttom
	 */
	public static void setCompoundDrawables(Context context, TextView view, int resource, int alignsize) {
		SoftReference<Drawable> soft = new SoftReference<Drawable>(context.getResources().getDrawable(resource));
		Drawable drawable = soft.get();
		switch (alignsize) {
		case 0:
			view.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
			break;
		case 1:
			view.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			break;
		case 2:
			view.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
			break;
		case 3:
			view.setCompoundDrawablesWithIntrinsicBounds(null, null, null, drawable);
			break;
		default:
			break;
		}
		view.setCompoundDrawablePadding(10);
	}

	public interface CallBack {
		public void onclick(int item);
	}
}
