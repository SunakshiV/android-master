package com.procoin.http.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

/**
 * 这个是圆角的图片, 如果clickable false 就需要重写onclick ， 不然就是使用原来的监听，
 *
 * @author zhengmj
 *
 */
public class RoundAngleImageView extends ImageView {

	private Paint paint;
	private Paint paint2;
	// private int roundWidth = 5;
	// private int roundHeight = 5;
	private float density;
	private int raidsTopLeft = 5;
	private int raidsTopRight = 5;
	private int raidsButtomLeft = 5;
	private int raidsButtomRight = 5;

	//

	// public void setRadius(int radius) {
	// this.roundWidth = radius;
	// this.roundHeight = radius;
	// }

	public RoundAngleImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public RoundAngleImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RoundAngleImageView(Context context) {
		super(context);
		init(context, null);
	}

	public void setradis(int topLeft, int topRight, int buttomLeft, int buttomRight) {
		raidsTopLeft = (int) (topLeft * density);
		raidsTopRight = (int) (topRight * density);
		raidsButtomLeft = (int) (buttomLeft * density);
		raidsButtomRight = (int) (buttomRight * density);
	}

	private void init(Context context, AttributeSet attrs) {
		density = context.getResources().getDisplayMetrics().density;
		// if (attrs != null) {
		// TypedArray a = context.obtainStyledAttributes(attrs,
		// R.styleable.RoundAngleImageView);
		// roundWidth =
		// a.getDimensionPixelSize(R.styleable.RoundAngleImageView_radius,
		// roundWidth);
		// roundHeight = roundWidth;
		// } else {
		setradis(5, 5, 5, 5);
		// roundWidth = (int) (roundWidth * density);
		// roundHeight = (int) (roundHeight * density);
		// }
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

		paint2 = new Paint();
		paint2.setXfermode(null);
	}

	@Override
	public void draw(Canvas canvas) {
		int width = getWidth();
		int height = getHeight();
		if (width <= 0) width = 60;
		if (height <= 0) height = 60;
		Bitmap bitmap = null;
		try {
			bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
			Canvas canvas2 = new Canvas(bitmap);
			super.draw(canvas2);
			drawLiftUp(canvas2);
			drawRightUp(canvas2);
			drawLiftDown(canvas2);
			drawRightDown(canvas2);
			canvas.drawBitmap(bitmap, 0, 0, paint2);
			if (!bitmap.isRecycled()) bitmap.recycle();
		} catch (Exception e) {

		} catch (OutOfMemoryError e) {
			if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
		}
	}

	private void drawLiftUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(0, raidsTopLeft);
		path.lineTo(0, 0);
		path.lineTo(raidsTopLeft, 0);
		path.arcTo(new RectF(0, 0, raidsTopLeft * 2, raidsTopLeft * 2), -90, -90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawLiftDown(Canvas canvas) {
		Log.d("RoundAngleImageView","raidsButtomLeft=="+raidsButtomLeft);
		Path path = new Path();
		path.moveTo(0, getHeight() - raidsButtomLeft);
		path.lineTo(0, getHeight());
		path.lineTo(raidsButtomLeft, getHeight());
		path.arcTo(new RectF(0, getHeight() - raidsButtomLeft * 2, raidsButtomLeft * 2, getHeight()), 90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}


	private void drawRightDown(Canvas canvas) {
		Log.d("RoundAngleImageView","raidsButtomRight=="+raidsButtomRight);
		Path path = new Path();
		path.moveTo(getWidth() - raidsButtomRight, getHeight());
		path.lineTo(getWidth(), getHeight());
		path.lineTo(getWidth(), getHeight() - raidsButtomRight);
		path.arcTo(new RectF(getWidth() - raidsButtomRight * 2, getHeight() - raidsButtomRight * 2, getWidth(), getHeight()), 0, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

	private void drawRightUp(Canvas canvas) {
		Path path = new Path();
		path.moveTo(getWidth(), raidsTopRight);
		path.lineTo(getWidth(), 0);
		path.lineTo(getWidth() - raidsTopRight, 0);
		path.arcTo(new RectF(getWidth() - raidsTopRight * 2, 0, getWidth(), 0 + raidsTopRight * 2), -90, 90);
		path.close();
		canvas.drawPath(path, paint);
	}

}