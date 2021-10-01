package com.procoin.module.circle.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.http.resource.BaseRemoteResourceManager;
import com.procoin.module.chat.util.DownLoadVoiceTask;
import com.procoin.module.circle.entity.VoiceEntity;
import com.procoin.social.util.CommonUtil;
import com.procoin.util.DensityUtil;

import java.io.File;

/**
 * 播放声音的 view
 */
@SuppressLint("HandlerLeak")
public class PlayVoiceUtilView extends RelativeLayout {
    private VoiceEntity voiceEntity;
    private DownLoadVoiceTask downLoadTalkFileTask;
    public VoiceLoadAndPlay voiceLoadAndPlay;

    private ImageView ivPlay;
    private ProgressBar progressBar;
    private TextView tvSecond;

    private Context context;
    private BaseRemoteResourceManager mRtalk;

    private static final int maxWidth = 170;// dip
    private static final int minWidth = 60;// dip
    private static final int maxSecond = 60;

    /**
     * 0 默认状态 1 加载中 2 播放中
     */
    private int state;

    private int pos = -1;// 属于哪个item
    private int followPos = -1;// 楼中楼的索引
    private Animation animation;
    private AnimationDrawable ad;

    private boolean isBgAnimOnPalying = true;// 播放的时候改变背景颜色


    public PlayVoiceUtilView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (state == 1) return;
                if (voiceLoadAndPlay != null) {
                    voiceLoadAndPlay.stop();
                    if (state == 2) {
                        setState(0);
                        return;
                    }
                }
                try {
                    startPlay();
                } catch (Exception e) {
                }
            }
        });
    }

//	public void performClick(){
//		if (state == 1) return;
//		if (voiceLoadAndPlay != null) {
//			voiceLoadAndPlay.stop();
//			if (state == 2) {
//				setState(0);
//				return;
//			}
//		}
//		try {
//			startPlay();
//		} catch (Exception e) {
//		}
//super.performClick();
//	}


    public void setBgAnimOnPalying(boolean animOnPalying) {
        this.isBgAnimOnPalying = animOnPalying;
    }

    public PlayVoiceUtilView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlayVoiceUtilView(Context context) {
        this(context, null);
    }

    public VoiceLoadAndPlay getVoiceLoadAndPlay() {
        return voiceLoadAndPlay;
    }

    public void setVoiceLoadAndPlay(VoiceLoadAndPlay voiceLoadAndPlay) {
        this.voiceLoadAndPlay = voiceLoadAndPlay;
    }

    /**
     * @param voiceEntity      播放的声音的url 和时间长
     * @param voiceLoadAndPlay 声音加载完之后 播放的回调接口
     * @param pos              属于哪个position
     * @param followPos        属于position 项 的楼中楼的position
     * @param playingPos       正在播放的 position
     * @param playingFollowPos 正在播放的 position项 的楼中楼的position
     */
    public void setVoiceEntityAndCallBack(VoiceEntity voiceEntity,
                                          VoiceLoadAndPlay voiceLoadAndPlay, int pos, int followPos,
                                          int playingPos, int playingFollowPos) {
        this.voiceEntity = voiceEntity;
        this.voiceLoadAndPlay = voiceLoadAndPlay;
        this.pos = pos;
        this.followPos = followPos;
        mRtalk = getApplicationContext().getRemoteResourceChatManager();
        ivPlay = (ImageView) findViewById(R.id.ivPlay);
        progressBar = (ProgressBar) findViewById(R.id.pb);
        progressBar.setVisibility(View.GONE);
        tvSecond = (TextView) findViewById(R.id.tvSecond);
        tvSecond.setText(voiceEntity.voiceSecond + "''");
        if (animation == null) {
            animation = AnimationUtils.loadAnimation(context, R.anim.play_voice_alpha);
        }
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) getLayoutParams();
        lp.width = DensityUtil.dip2px(context,
                voiceEntity.voiceSecond * maxWidth / maxSecond)
                + DensityUtil.dip2px(context, minWidth);
        setLayoutParams(lp);
        setState(pos == playingPos && followPos == playingFollowPos ? 2 : 0);

    }

    public VoiceEntity getVoiceEntity() {
        return voiceEntity;
    }

    protected void startPlay() throws Exception {
        if (voiceEntity != null) {
            // Log.d("onclick", "url=="+voiceEntity.getVoiceUrl());
            if (getApplicationContext().isSDCard()) { // 判断文件是否存在
                File f = new File(voiceEntity.voiceUrl);//有可能是本地已经有了,比如私聊的时候，录完音本地就有了，点击直接播放
                if (f.exists()) {
                    if (voiceLoadAndPlay != null) {
                        voiceLoadAndPlay.play(f, pos, followPos);
                        setState(1);
                    }
                } else {
                    File file = mRtalk.getFile(voiceEntity.voiceUrl);
                    if (!file.exists()) {
                        // Log.d("downLoadTalkFileTask", "url=="+voiceEntity.getVoiceUrl());
                        CommonUtil.cancelAsyncTask(downLoadTalkFileTask);
                        downLoadTalkFileTask = (DownLoadVoiceTask) new DownLoadVoiceTask(voiceEntity.voiceUrl,
                                mRtalk) {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();
                                setState(1);
                                voiceLoadAndPlay.setWillPlay(Uri.encode(voiceEntity.voiceUrl));
                            }

                            @Override
                            protected void onPostExecute(String result) {
                                super.onPostExecute(result);
                                setState(0);
                                if (result != null) {
                                    File file = mRtalk.getFile(voiceEntity.voiceUrl);
                                    if (file.exists()) {
                                        if (voiceLoadAndPlay != null) {
                                            voiceLoadAndPlay.playAfterLoad(file,
                                                    pos, followPos);
                                            // setState(1);
                                        }
                                    } else {
                                        CommonUtil.showToast(context, "语音下载失败",
                                                Gravity.CENTER);
                                    }
                                }
                            }

                        };
                        downLoadTalkFileTask.executeParams();
                    } else {
                        if (voiceLoadAndPlay != null) {
                            voiceLoadAndPlay.play(file, pos, followPos);
                            setState(1);
                        }
                    }
                }

            } else {
                CommonUtil.showToast((AppCompatActivity) context, "没有sd卡", Gravity.BOTTOM);
            }
        }
    }

    protected String getFileName(String voiceUrl) {
        if (TextUtils.isEmpty(voiceUrl))
            return "";
        try {
            return voiceUrl.split("=")[1];
        } catch (Exception e) {
        }
        return "";
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        switch (state) {
            case 0:
                ivPlay.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                stopAnim();
                break;
            case 1:
                ivPlay.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                break;
            default:
                ivPlay.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                startAnim();
                break;
        }
//		postInvalidate();// 少了这句楼中楼不会播放动画
    }

    private void stopAnim() {
        if (ad == null) {
            ad = getAnimationDrawable();
        }
        if (ad != null && ad.isRunning()) {
            ad.stop();
//			Log.d("anim", "animStop///////////////");
            ad.selectDrawable(0);
            postInvalidate();
        }
//		Drawable drawable = ivPlay.getDrawable();
//		if (drawable != null && drawable instanceof AnimationDrawable) {
//			AnimationDrawable ad = ((AnimationDrawable) drawable);
//		
//		}
        if (isBgAnimOnPalying) {
            setBackGroundColor(Color.WHITE);
            getBackground().setAlpha(255);
            clearAnimation();
        }


    }

    private void startAnim() {
        if (ad == null) {
            ad = getAnimationDrawable();
        }
        if (ad != null && !ad.isRunning()) {
            if (state == 2) {
                ad.start();
                Log.d("anim", "animStart///////////////");
            }
        }
//		if (bg_anim)
//			return;
        if (isBgAnimOnPalying) {
            setBackGroundColor(Color.rgb(140, 221, 255));
            startAnimation(animation);
        }

//		animation.start();
//		bg_anim = handler.postDelayed(runnable, 500);

    }

    private AnimationDrawable getAnimationDrawable() {
        Drawable drawable = ivPlay.getDrawable();
        if (drawable != null && drawable instanceof AnimationDrawable) {
            return (AnimationDrawable) drawable;
        }
        return null;
    }

    private void setBackGroundColor(int color) {// 播放的时候改变背景颜色
        GradientDrawable p = (GradientDrawable) getBackground();
        p.setColor(color);
    }

    private MainApplication getApplicationContext() {
        return (MainApplication) context.getApplicationContext();
    }

    public interface VoiceLoadAndPlay {

        /**
         * 本地有直接播放
         */
        void play(File file, int pos, int followPos);

        /**
         * 当加载完成的时候开始播放，（如果还没加载完，已经播放了其他的voice，那加载完之后不再播放这个，加载之前回调用setWillPlay，
         * 把URL传过去，播放时发现不一样就不在播放）
         */
        void playAfterLoad(File file, int pos, int followPos);

        void stop();

        void setWillPlay(String filePath);
    }

}
