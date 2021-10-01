package com.procoin.module.guide;

import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import androidx.collection.ArrayMap;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseActionBarActivity;
import com.procoin.util.InflaterUtils;
import com.procoin.social.util.CommonUtil;
import com.procoin.R;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public class GuidePopupWindow {
    private PopupWindow guide;
    private ImageView ivGuide;
    // private Button btnTop, btnBottom;
    private TJRBaseActionBarActivity activity;
    private int key;
    private Map<Integer, SoftReference<Bitmap>> mCache;
    private int lookCount;
    private boolean isCanClick;
    private FrameLayout flAll;
    private FrameLayout flToolTip;
    private FrameLayoutWithGuide flGuide;
    private View[] viewParams;
    private static Handler mHandler = new Handler();

    public GuidePopupWindow(final TJRBaseActionBarActivity activity) {
        if (mCache == null) mCache = new ArrayMap<>();
        this.activity = activity;
        if (guide == null) {
            View view = InflaterUtils.inflateView(activity, R.layout.guide_activity);

            flAll = (FrameLayout) view.findViewById(R.id.flAll);
            flToolTip = (FrameLayout) view.findViewById(R.id.flToolTip);
            ivGuide = (ImageView) view.findViewById(R.id.ivGride);
            ivGuide.setScaleType(ImageView.ScaleType.FIT_XY);
            guide = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
            guide.setOutsideTouchable(false);
            guide.setBackgroundDrawable(new BitmapDrawable());// 特别留意这个东东
        }
    }

    public GuidePopupWindow(final TJRBaseActionBarActivity activity, final int key) {
        if (mCache == null) mCache = new HashMap<Integer, SoftReference<Bitmap>>();
        this.activity = activity;
        this.key = key;
        if (guide == null) {
            final View view = InflaterUtils.inflateView(activity, R.layout.guide_activity);
            ivGuide = (ImageView) view.findViewById(R.id.ivGride);
            ivGuide.setScaleType(ImageView.ScaleType.FIT_XY);
            flAll = (FrameLayout) view.findViewById(R.id.flAll);
            flToolTip = (FrameLayout) view.findViewById(R.id.flToolTip);
            guide = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT, true);
            guide.setOutsideTouchable(false);
            guide.setTouchInterceptor(new OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (isCanClick) return false;
                    //TODO
                    switch (key) {

                    }

                    return false;
                }
            });
            guide.setBackgroundDrawable(new BitmapDrawable());// 特别留意这个东东
        }
    }


    // /**
    // * 是否显示全图
    // *
    // * @param isShowBmp
    // */
    // public void setShowBmp(boolean isShowBmp) {
    // this.isShowBmp = isShowBmp;
    // }

    public void setKey(int key) {
        this.key = key;
    }

    // private class OnClick implements OnClickListener {
    //
    // @Override
    // public void onClick(View v) {
    // activity.getApplicationContext().updataGuide(key);
    // dismiss();
    // }
    // }

    public Bitmap get(int key) throws IOException {
        SoftReference<Bitmap> ref;
        Bitmap bitmap;
        synchronized (this) {
            ref = mCache.get(key);
        }
        if (ref != null) {
            bitmap = ref.get();
            if (bitmap != null) {
                return bitmap;
            }
        }
        bitmap = CommonUtil.readBitmap(activity, key);
        if (bitmap != null) {
            synchronized (this) {
                mCache.put(key, new SoftReference<Bitmap>(bitmap));
            }
            return bitmap;
        }
        return null;
    }

    private void showGuideBitmap(final int resKey) {

        mHandler.post(new Runnable() {

            @Override
            public void run() {
                try {
                    Bitmap bitmap = get(resKey);
                    if (bitmap != null) {
                        ivGuide.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), bitmap));
                    }
                } catch (Exception e) {
                } catch (OutOfMemoryError e) {
                    // CommonUtil.LogLa(2, "..guide..OutOfMemoryError." +
                    // e.getMessage());
                }
            }
        });

    }

    /**
     * 这个是显示的view
     *
     * @param params
     */
    public void setviewParams(View... params) {
        this.viewParams = params;
    }

    /**
     * 进入页面增加引导布局
     *
     * @param guideToolTip
     */
    public void showFrameLayoutWithGuide(final GuideToolTip guideToolTip) {
        if (viewParams != null) {
            flGuide = new FrameLayoutWithGuide(activity);
            flGuide.setViewHole(viewParams[0]);
            flAll.addView(flGuide, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            flGuide.postInvalidate();
            flToolTip.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (viewParams != null) {
                        setupToolTip(guideToolTip, viewParams[0]);
                    }
                }
            }, 100);
        }
    }


    public void dismiss() {
        if (guide != null && guide.isShowing()) guide.dismiss();
    }

    private void setupToolTip(GuideToolTip toolTip, View lightView) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        // layoutParams.setGravity = Gravity.BOTTOM;
        flToolTip.removeAllViews();
        if (toolTip != null) {
            View mToolTipViewGroup = InflaterUtils.inflateView(activity, R.layout.guide_tooltip_activity);
            View toolTipContainer = mToolTipViewGroup.findViewById(R.id.toolTip_container);
            TextView toolTipTitleTV = (TextView) mToolTipViewGroup.findViewById(R.id.title);
            TextView toolTipDescriptionTV = (TextView) mToolTipViewGroup.findViewById(R.id.description);

            toolTipContainer.setBackgroundColor(toolTip.mBackgroundColor);
            toolTipTitleTV.setText(toolTip.mTitle);
            toolTipDescriptionTV.setText(toolTip.mDescription);
            toolTipTitleTV.setTextColor(toolTip.mTextColor);
            toolTipDescriptionTV.setTextColor(toolTip.mTextColor);
            // TODO mToolTipViewGroup.startAnimation(toolTip.mEnterAnimation);

            mToolTipViewGroup.measure(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            int width = mToolTipViewGroup.getMeasuredWidth();
            int height = mToolTipViewGroup.getMeasuredHeight();

            Point point = null;
            if (width > flToolTip.getWidth()) {
                point = getXYForToolTip(toolTip.mGravity, flToolTip.getWidth(), height, lightView);
            } else {
                point = getXYForToolTip(toolTip.mGravity, width, height, lightView);
            }
            layoutParams.setMargins(point.x, point.y, 0, 0);
            /* add setShadow if it's turned on */
            // if (mToolTip.mShadow) {
            // mToolTipViewGroup.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.drop_shadow));
            // }
            // ((ViewGroup)
            // mActivity.getWindow().getDecorView().findViewById(android.R.id.describes)).addView(mToolTipViewGroup,
            // layoutParams);
            flToolTip.addView(mToolTipViewGroup, layoutParams);
            if (width > flToolTip.getWidth()) {
                mToolTipViewGroup.getLayoutParams().width = flToolTip.getWidth();
            }
        }

    }

    private Point getXYForToolTip(int gravity, int width, int height, View lightView) {
        Point point = new Point();
        if (lightView == null) return point;
        int[] pos = new int[2];
        lightView.getLocationOnScreen(pos);
        int x = pos[0];
        int y = pos[1];
        float density = activity.getResources().getDisplayMetrics().density;
        float adjustment = 10 * density;
        // x calculation
        if ((gravity & Gravity.LEFT) == Gravity.LEFT) {
            point.x = x - width + (int) adjustment;
        } else if ((gravity & Gravity.RIGHT) == Gravity.RIGHT) {
            point.x = x + lightView.getWidth() - (int) adjustment;
        } else {
            point.x = x + lightView.getWidth() / 2 - width / 2;
        }

        // y calculation
        if ((gravity & Gravity.TOP) == Gravity.TOP) {

            if (((gravity & Gravity.LEFT) == Gravity.LEFT) || ((gravity & Gravity.RIGHT) == Gravity.RIGHT)) {
                point.y = y - height + (int) adjustment;
            } else {
                point.y = y - height - (int) adjustment;
            }
        } else { // this is center
            if (((gravity & Gravity.LEFT) == Gravity.LEFT) || ((gravity & Gravity.RIGHT) == Gravity.RIGHT)) {
                point.y = y + lightView.getHeight() - (int) adjustment;
            } else {
                point.y = y + lightView.getHeight() + (int) adjustment;
            }
        }
        return point;
    }

}
