package com.procoin.widgets;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

/**
 * 这个类是chat键盘打开和关闭
 *
 * @author zhengmj
 */
@SuppressWarnings("ResourceType")
public class ResizeLayout extends RelativeLayout {
    private static final int SOFTKEYPAD_MIN_HEIGHT = 50;
    private Handler uiHandler = new Handler();
    private showOrHideViewListen listen;
    public ResizeLayoutSize resizeLayoutSize; //为了减少方法数
    private int width; // 更新宽度
    private int height; // 更新高度
    private int nowHeight;// 现在的高度
    private int nowWidth;// 现在的宽度

    public void setListen(showOrHideViewListen listen) {
        this.listen = listen;
    }

    public ResizeLayout(Context context) {
        super(context);
    }

    public ResizeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, final int h, int oldw, final int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        nowHeight = h;
        nowWidth = w;
        if (listen != null) {
            uiHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (oldh - h > SOFTKEYPAD_MIN_HEIGHT) {
                        listen.showOrHideView(false);// 隐藏
                    } else {
                        listen.showOrHideView(true);
                    }
                }
            });
        } else if (resizeLayoutSize != null) {
            resizeLayoutSize.onSizeChanged(w, h, oldw, oldh);
        }
        sizeHaveChanged(w, h);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }

    public interface showOrHideViewListen {
        public void showOrHideView(boolean show);
    }

    /**
     * 这个是把view注入到这个布局里，而不影响原来的布局
     *
     * @param context
     * @param target
     */
    public void applyTo(Context context, View target, View footView) {
        ViewGroup.LayoutParams lp = target.getLayoutParams();
        ViewParent parent = target.getParent();
        FrameLayout container = new FrameLayout(context);
        container.setId(1);
        ViewGroup group = (ViewGroup) parent;
        int index = group.indexOfChild(target);
        group.removeView(target);
        group.addView(this, index, lp);
        // this.setBackgroundColor(Color.ZHANG);
        // container.setBackgroundColor(Color.GRAY);
        container.addView(target);
        this.addView(container, new LayoutParams(lp));
        if (footView != null && footView.getLayoutParams() != null) {
            LayoutParams footlp = new LayoutParams(footView.getLayoutParams());
            footlp.addRule(ResizeLayout.ALIGN_PARENT_BOTTOM);
            this.addView(footView, footlp);
            footView.setId(2);
            LayoutParams containerlp = (LayoutParams) container.getLayoutParams();
            containerlp.addRule(ResizeLayout.ABOVE, footView.getId());
            container.setLayoutParams(containerlp);
        }
        group.invalidate();
    }

    /**
     * 更新高度，才会使底部像微信一样发送一样平滑
     *
     * @param w
     * @param h
     */
    public void sizeHaveChanged(int w, int h) {
        measure(this.width - w + getWidth(), this.height - h + getHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        this.width = widthMeasureSpec;
        this.height = heightMeasureSpec;
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    public interface ResizeLayoutSize {
        public void onSizeChanged(int w, int h, int oldw, int oldh);
    }


}
