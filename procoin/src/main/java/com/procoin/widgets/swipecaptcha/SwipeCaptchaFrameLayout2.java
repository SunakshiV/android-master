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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.procoin.util.entity.OutDragImg;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.R;

/**
 * Created by zhengmj on 18-9-30.
 * <p>
 * 滑动验证，后端验证
 */

public class SwipeCaptchaFrameLayout2 extends FrameLayout {
    //    SwipeCaptchaView mSwipeCaptchaView;
    private LinearLayout llSwipe;
    private SeekBar mSeekBar;
    private TextView refresh;
    private Context ctx;
    private int width = 0;
    private TextView tvHightLight;

    private FrameLayout flBg;
    private ImageView ivDrag;
    private ImageView ivOutImg;

    private TextView tvSwich;




    private TjrImageLoaderUtil tjrImageLoaderUtil;

    private OutDragImg outDragImg;
    private CheckCallback checkCallback;

    private FrameLayout.LayoutParams lpDrag;

    private double scale = 1;

    private LinearLayout llCheckSuccess;//这个暂时不用
    private TextView tvCheckSuccess;//用这个
    private boolean isLoading = false;
    private boolean isChecking = false;


    public SwipeCaptchaFrameLayout2(@NonNull Context context) {
        super(context);
        init(context);
    }


    public SwipeCaptchaFrameLayout2(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

    }

    public SwipeCaptchaFrameLayout2(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void setCheckCallback(CheckCallback checkCallback) {
        this.checkCallback = checkCallback;

    }

    public void setSeekbarEnable(Boolean enable) {
        mSeekBar.setEnabled(enable);
    }

    /**
     * 设置初始状态
     */
    public void onloading() {
        mSeekBar.setEnabled(false);
        llCheckSuccess.setVisibility(INVISIBLE);//设为gone第一次获取不到heigt
        tvCheckSuccess.setVisibility(GONE);
        refresh.setVisibility(VISIBLE);
        refresh.setText("加载中...");
        ivDrag.setVisibility(GONE);
        ivOutImg.setImageBitmap(null);
        isLoading = true;

    }

    public void onChecking() {
        isChecking = true;
        refresh.setVisibility(VISIBLE);
        refresh.setText("验证中...");
    }

    public void setLoadFail() {
        refresh.setVisibility(VISIBLE);
        refresh.setText("加载失败,点击图片刷新");
        ivDrag.setVisibility(GONE);
        ivOutImg.setImageBitmap(null);
        isLoading = false;
        isChecking = false;

    }

    public void setProgress(int progress) {
        mSeekBar.setProgress(progress);
    }


    private void init(Context context) {
        this.ctx = context;
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic);
        View home = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.swipe_captcha2, this);
//        mSwipeCaptchaView = home.findViewById(R.id.swipeCaptchaView);
        llSwipe = home.findViewById(R.id.llSwipe);
        mSeekBar = home.findViewById(R.id.dragBar);
        flBg = home.findViewById(R.id.flBg);
        ivOutImg = home.findViewById(R.id.ivOutImg);
        ivDrag = home.findViewById(R.id.ivDrag);
        tvHightLight = home.findViewById(R.id.tvHightLight);
        lpDrag = (FrameLayout.LayoutParams) ivDrag.getLayoutParams();
        llCheckSuccess = home.findViewById(R.id.llCheckSuccess);
        tvCheckSuccess= home.findViewById(R.id.tvCheckSuccess);

        tvSwich= home.findViewById(R.id.tvSwich);

//        width = DensityUtil.dip2px(ctx, 300);
        width = llSwipe.getLayoutParams().width;
        Log.d("SwipeCaptcha", "width====" + width);

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                lpDrag.leftMargin = progress;
                ivDrag.setLayoutParams(lpDrag);

                if (progress == 0) {
                    tvHightLight.setVisibility(VISIBLE);
                } else {
                    tvHightLight.setVisibility(GONE);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //随便放这里是因为控件
//                mSeekBar.setMax(mSwipeCaptchaView.getMaxSwipeValue());

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Log.d("zxt", "onStopTrackingTouch() called with: seekBar = [" + seekBar + "]");
                if (checkCallback != null && outDragImg != null) {
                    mSeekBar.setEnabled(false);
                    checkCallback.check(outDragImg.dragImgKey, (int) (seekBar.getProgress() / scale));
                }
//                mSwipeCaptchaView.matchCaptcha();
            }
        });

        refresh = findViewById(R.id.refresh);
        refresh.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                mSwipeCaptchaView.createCaptcha();
//                mSeekBar.setEnabled(true);
//                mSeekBar.setProgress(0);
                if (isLoading || isChecking) return;
                if (checkCallback != null) {
                    checkCallback.refresh();
                }
            }
        });
        tvSwich.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkCallback != null) {
                    checkCallback.refresh();
                }
            }
        });
    }

    //验证成功，执行动画(加多这一步只是为了加个动画)
    public void checkSuccess(final String dragImgKey, final int locationx) {
        isChecking = false;
        refresh.setVisibility(GONE);
        tvCheckSuccess.setVisibility(VISIBLE);
        mSeekBar.setProgressDrawable(ContextCompat.getDrawable(ctx,R.drawable.drag_progess_success_bg));
        //mSeekBar.setThumb()是有问题的，所以用selector的方式
        mSeekBar.setSelected(true);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (checkCallback != null) {
                    checkCallback.checkSuccess(dragImgKey, locationx);
                }
            }
        }, 1200);
//        if (llCheckSuccess != null) {
//            llCheckSuccess.setVisibility(VISIBLE);
//            Log.d("checkSuccess", "getHeight==" + llCheckSuccess.getHeight());
//            ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(llCheckSuccess, "translationY", llCheckSuccess.getMeasuredHeight(), 0);
//            objectAnimator.setDuration(400);
//            objectAnimator.addListener(new Animator.AnimatorListener() {
//                @Override
//                public void onAnimationStart(Animator animation) {
//                }
//
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    new android.os.Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (checkCallback != null) {
//                                checkCallback.checkSuccess(dragImgKey, locationx);
//                            }
//                        }
//                    }, 400);
//                }
//
//                @Override
//                public void onAnimationCancel(Animator animation) {
//
//                }
//
//                @Override
//                public void onAnimationRepeat(Animator animation) {
//                }
//            });
//            objectAnimator.start();
//        }

    }


    //loadSuccess
    public void setData(OutDragImg outDragImg) {
        ivDrag.setVisibility(VISIBLE);
        refresh.setVisibility(GONE);
        tvCheckSuccess.setVisibility(GONE);
        isLoading = false;
        this.outDragImg = outDragImg;
        mSeekBar.setEnabled(true);
        mSeekBar.setProgress(0);
        scale = width / Double.parseDouble(outDragImg.sourceImgWidth);
        FrameLayout.LayoutParams ivOutImgLp = (FrameLayout.LayoutParams) ivOutImg.getLayoutParams();
        ivOutImgLp.width = width;
        ivOutImgLp.height = (int) (Integer.parseInt(outDragImg.sourceImgHeight) * scale);
        ivOutImg.setLayoutParams(ivOutImgLp);


        FrameLayout.LayoutParams ivDragImgLp = (FrameLayout.LayoutParams) ivDrag.getLayoutParams();
        ivDragImgLp.topMargin = (int) (Integer.parseInt(outDragImg.locationy) * scale);
        ivDragImgLp.width = (int) (Integer.parseInt(outDragImg.smallImgWidth) * scale);
        ivDragImgLp.height = (int) (Integer.parseInt(outDragImg.smallImgHeight) * scale);
        ivDrag.setLayoutParams(ivDragImgLp);

        tjrImageLoaderUtil.displayImage(outDragImg.bigImgName, ivOutImg);
        tjrImageLoaderUtil.displayImage(outDragImg.smallImgName, ivDrag);

        mSeekBar.setMax(width - ivDragImgLp.width);


    }


    public interface CheckCallback {
        void check(String dragImgKey, int locationx);

        void refresh();

        void checkSuccess(String dragImgKey, int locationx);//验证成功，然后执行相对应的操作，比如关闭

    }
}
