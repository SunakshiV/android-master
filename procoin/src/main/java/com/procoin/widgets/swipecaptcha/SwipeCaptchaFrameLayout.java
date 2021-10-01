package com.procoin.widgets.swipecaptcha;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.procoin.R;

/**
 * Created by zhengmj on 18-9-30.
 *
 * 滑动验证，前端验证
 */

public class SwipeCaptchaFrameLayout extends FrameLayout {
    SwipeCaptchaView mSwipeCaptchaView;
    SeekBar mSeekBar;
    Context ctx;
    TextView tvHightLight;


    public SwipeCaptchaFrameLayout(@NonNull Context context) {
        super(context);
        init(context);
    }


    public SwipeCaptchaFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public SwipeCaptchaFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setOnCaptchaMatchCallback(SwipeCaptchaView.OnCaptchaMatchCallback onCaptchaMatchCallback) {
        mSwipeCaptchaView.setOnCaptchaMatchCallback(onCaptchaMatchCallback);

    }

    public void setSeekbarEnable(Boolean enable) {
        mSeekBar.setEnabled(enable);
    }

    public void setProgress(int progress) {
        mSeekBar.setProgress(progress);
    }
    public void setSuccessDrawable(){
        mSeekBar.setThumb(ContextCompat.getDrawable(ctx, R.drawable.ic_thumb_success_bg));
        mSeekBar.setProgressDrawable(ContextCompat.getDrawable(ctx,R.drawable.drag_progess_success_bg));
    }

    private void init(Context context) {
        this.ctx = context;
        View home = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.swipe_captcha, this);
        mSwipeCaptchaView = home.findViewById(R.id.swipeCaptchaView);
        mSeekBar = home.findViewById(R.id.dragBar);
        tvHightLight = home.findViewById(R.id.tvHightLight);
        final ImageView ivDrag= home.findViewById(R.id.ivDrag);
        final FrameLayout.LayoutParams lp=(FrameLayout.LayoutParams)ivDrag.getLayoutParams();

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                lp.leftMargin=progress;
                ivDrag.setLayoutParams(lp);


                mSwipeCaptchaView.setCurrentSwipeValue(progress);
                if (progress == 0) {
                    tvHightLight.setVisibility(VISIBLE);
                } else {
                    tvHightLight.setVisibility(GONE);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                mSwipeCaptchaView.matchCaptcha();
            }
        });
        findViewById(R.id.refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSwipeCaptchaView.createCaptcha();
                mSeekBar.setEnabled(true);
                mSeekBar.setProgress(0);
            }
        });
    }


}
