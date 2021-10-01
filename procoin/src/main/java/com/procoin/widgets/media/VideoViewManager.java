//package com.tjr.imredz.widgets.media;
//
//import android.animation.ObjectAnimator;
//import android.app.Activity;
//import android.content.Context;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.SeekBar;
//import android.widget.Toast;
//
//import TjrImageLoaderUtil;
//import com.tjr.imredz.R;
//import com.tjr.imredz.util.DensityUtil;
//import com.tjr.imredz.util.PageJumpUtil;
//
//import tv.danmaku.ijk.media.player.IMediaPlayer;
//import tv.danmaku.ijk.media.player.IjkMediaPlayer;
//
///**
// * Created by zhengmj on 18-10-9.
// */
//
//public class VideoViewManager implements View.OnClickListener {
//
//
//    private View view;
//    private ImageView btnPlay;
//    private ImageView btnFullScreen;
//    private ImageView ivPreView;
//    private ImageView ivCenterPlay;
//    private SeekBar mProgress;
//    private IjkVideoView mPlayer;
//    private LinearLayout llMediaController;
//    private ProgressBar pbLoad;
//    private RelativeLayout rlPlay;
//    private RelativeLayout rlFullScreen;
//
//    private boolean playerSupport;
//    private boolean isfirstPlay = true;
//    private boolean isControllerShow = false;  //媒体控制器是否显示
//    private String mVideoUrl;
//    private String mPreviewImgUrl;
//    private int mPreviewImgRes;
//    private Handler handler = new Handler(Looper.getMainLooper());
//    private TjrImageLoaderUtil imageLoaderUtil;
//
//    private Context context;
//
//    //视频的宽高比就是按这个比例
//    public static final int VIDEOWIDTH = 640;
//    public static final int VIDEOHEIGHT = 368;
//
//
//    public boolean mediaController;//OlstarHomeActivity才用到这个,因为挡住了控制条,为true  首页HomeActivity为false
//
//    public VideoViewManager(Context context, boolean mediaController) {
//        Log.d("MEDIACONTROLLER","mediaController=="+mediaController);
//        this.context = context;
//        this.mediaController=mediaController;
//        view = LayoutInflater.from(context).inflate(R.layout.common_videoview_manager, null);
//        btnPlay = (ImageView) view.findViewById(R.id.btnPlay);
//        btnFullScreen = (ImageView) view.findViewById(R.id.btnFullScreen);
//        ivCenterPlay = (ImageView) view.findViewById(R.id.ivCenterPlay);
//        ivPreView = (ImageView) view.findViewById(R.id.ivPreView);
////        ivPreView.getLayoutParams().height=getScreenWidth()*VIDEOHEIGHT/VIDEOWIDTH;
//        mProgress = (SeekBar) view.findViewById(R.id.seekBar);
//        mPlayer = (IjkVideoView) view.findViewById(R.id.mPlayer);
//        llMediaController = (LinearLayout) view.findViewById(R.id.llMediaController);
//        pbLoad = (ProgressBar) view.findViewById(R.id.pbLoad);
//        rlFullScreen = (RelativeLayout) view.findViewById(R.id.rlFullScreen);
//        rlPlay = (RelativeLayout) view.findViewById(R.id.rlPlay);
//        rlFullScreen.setOnClickListener(this);
//        rlPlay.setOnClickListener(this);
//        ivCenterPlay.setOnClickListener(this);
//        imageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_mic);
//        if(mediaController)clearMediaControllerBottom();
//        if(!mediaController)setRlFullScreenMarginRight();
//        init();
//    }
//
//    public VideoViewManager(Context context) {
//        this(context, false);
//    }
//
//
//    private void clearMediaControllerBottom(){//OlstarHomeActivity挡住了整个控制条
//        RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams)llMediaController.getLayoutParams();
//        lp.bottomMargin=0;
//        lp.leftMargin=lp.rightMargin= DensityUtil.dip2px(context,20);
////        lp.addRule(RelativeLayout.ALIGN_BOTTOM,0);
//    }
//
//    private void setRlFullScreenMarginRight(){//首页的页码挡住全屏按钮
//        LinearLayout.LayoutParams lp=(LinearLayout.LayoutParams)rlFullScreen.getLayoutParams();
//        lp.rightMargin=DensityUtil.dip2px(context,40);
//    }
//
//
//    public boolean isPlay() {
//        return mPlayer != null && mPlayer.isPlaying();
//    }
//
//    public int getScreenWidth() {
//        DisplayMetrics dm = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        return dm.widthPixels;
//    }
//
//    public int getScreenHeight() {
//        DisplayMetrics dm = new DisplayMetrics();
//        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
//        return dm.heightPixels;
//    }
//
//    /**
//     * 这里是设置默认图片的宽高比
//     *
//     * @param o
//     */
//    public void setIvPreViewHeight(int o) {
//        if (ivPreView != null) {
//            if (o == 2) {
//                ivPreView.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;
//                ivPreView.getLayoutParams().width = getScreenHeight() * VIDEOWIDTH / VIDEOHEIGHT;
//            } else {
//                ivPreView.getLayoutParams().width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                ivPreView.getLayoutParams().height = getScreenWidth() * VIDEOHEIGHT / VIDEOWIDTH;
//            }
//        }
//
//    }
//
//    /**
//     * 退出一定要调用此方法释放资源
//     */
//    public void release() {
//        handler.removeCallbacks(updateProgressRunnable);
//        mPlayer.stopPlayback();
//        mPlayer.release(true);
//        IjkMediaPlayer.native_profileEnd();
//    }
//
//
//    /**
//     * 3秒隐藏控制条
//     */
//    Runnable hideRunnable = new Runnable() {
//        @Override
//        public void run() {
//            handleMediaController(false);
//        }
//    };
//
//
//    /**
//     * 每隔1秒更新播放进度
//     */
//    Runnable updateProgressRunnable = new Runnable() {
//        @Override
//        public void run() {
//            int position = mPlayer.getCurrentPosition();
//            int duration = mPlayer.getDuration();
//            Log.d("play", "duration:" + duration);
//            Log.d("play", "updateProgress position:" + position);
//            if (duration > 0) {
//                long pos = 1000L * position / duration;  //use long to avoid overflow
//                mProgress.setProgress((int) pos);
//                int percent = mPlayer.getBufferPercentage();
//                mProgress.setSecondaryProgress(percent * 10);
//                Log.d("play", "updateProgress seekbar Progress:" + pos);
//                Log.d("play", "updateProgress seekbar SecondaryProgress:" + percent);
//            } else {
//                handler.removeCallbacks(updateProgressRunnable);
//            }
//            handler.postDelayed(updateProgressRunnable, 1000);
//        }
//    };
//
//    private void handleMediaController(boolean show) {
//        if (show) {
//            isControllerShow = true;
//            ObjectAnimator animator = ObjectAnimator.ofFloat(llMediaController, "translationY", -llMediaController.getHeight());
//            animator.setDuration(500);
//            animator.start();
//            handler.removeCallbacks(hideRunnable);
//            handler.postDelayed(hideRunnable, 3000);
//        } else {
//            if (!mPlayer.isPlaying()) return;
//            isControllerShow = false;
//            ObjectAnimator animator = ObjectAnimator.ofFloat(llMediaController, "translationY", 0);
//            animator.setDuration(500);
//            animator.start();
//        }
//    }
//
//
//    public void setUrl(String previewImgUrl, String videoUrl) {
//        Log.d("setUrl", "previewImgUrl==" + previewImgUrl + " videoUrl==" + videoUrl + "  mVideoUrl==" + mVideoUrl);
//        if (TextUtils.isEmpty(videoUrl)) return;
//        if(!videoUrl.equals(this.mVideoUrl)){
//            this.mPreviewImgUrl = previewImgUrl;
//            this.mVideoUrl = videoUrl;
//            if(!TextUtils.isEmpty(mPreviewImgUrl)){
//                imageLoaderUtil.displayImage(mPreviewImgUrl, ivPreView);
//            }else{
//                if(mPreviewImgRes!=-1){
//                    ivPreView.setImageResource(mPreviewImgRes);
//                }
//            }
//        }
//    }
//
//    public void setmPreviewImgUrl(int res){
//        this.mPreviewImgRes=res;
//    }
//
//    public View getView() {
//        return this.view;
//    }
//
//    public void setBtnFullScreenVisible(boolean visible) {
//        if (visible) {
//            btnFullScreen.setVisibility(View.VISIBLE);
//        } else {
//            btnFullScreen.setVisibility(View.GONE);
//        }
//    }
//
//    private void init() {
//        //VideoView初始化
//        try {
//            IjkMediaPlayer.loadLibrariesOnce(null);
//            IjkMediaPlayer.native_profileBegin("libijkplayer.so");
//            playerSupport = true;
//        } catch (Throwable e) {
//            Log.e("GiraffePlayer", "loadLibraries error", e);
//        }
//        if (!playerSupport) {
//            Toast.makeText(context, "不支持播放", Toast.LENGTH_LONG).show();
//        }
//
//        mPlayer.setBackgroundColor(0);
//        mPlayer.setAspectRatio(IRenderView.AR_ASPECT_FILL_PARENT);
//        mPlayer.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (isControllerShow) {
//                        handleMediaController(false);
//                    } else {
//                        handleMediaController(true);
//                    }
//                }
//                return true;
//            }
//
//        });
//        mPlayer.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
//            @Override
//            public void onCompletion(IMediaPlayer iMediaPlayer) {
//                btnPlay.setImageResource(R.drawable.ic_common_play);
//                mPlayer.seekTo(0);
//                mProgress.setProgress(0);
//                handler.removeCallbacks(hideRunnable);
//                handler.removeCallbacks(updateProgressRunnable);
//                handleMediaController(true);
//            }
//        });
//        mPlayer.setOnInfoListener(new IMediaPlayer.OnInfoListener() {
//            @Override
//            public boolean onInfo(IMediaPlayer mp, int what, int extra) {
//                switch (what) {
//                    case IMediaPlayer.MEDIA_INFO_BUFFERING_START: //开始缓存
//                        Log.d("play", "buffer start");
//                        if (!isfirstPlay) {
//                            pbLoad.setVisibility(View.VISIBLE);
//                        }
//                        break;
//                    case IMediaPlayer.MEDIA_INFO_BUFFERING_END: //结束缓存
//                        Log.d("play", "buffer end");
//                        pbLoad.setVisibility(View.GONE);
//                        int percent = mPlayer.getBufferPercentage();
//                        mProgress.setSecondaryProgress(percent * 10);
//                        break;
//                    case IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH:
//                        break;
//                    case IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
//                        break;
//                }
//                return false;
//            }
//        });
//        //进度条初始化
//        mProgress.setMax(1000);
//        mProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                if (!fromUser) return;  //根据播放进度设置进度条时,防止发生设置循环
//                if (TextUtils.isEmpty(mVideoUrl)) return;
//                long duration = mPlayer.getDuration();
//                double newposition = (duration * progress) / 1000L;
//                mPlayer.seekTo((int) newposition);
//                Log.d("play", "onProgressChanged seekbar progress:" + progress);
//                Log.d("play", "onProgressChanged player newPosition:" + newposition);
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//                handler.removeCallbacks(updateProgressRunnable);
//                handler.removeCallbacks(hideRunnable);
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//                if (mPlayer.isPlaying()) {
//                    handler.post(updateProgressRunnable);
//                    handler.postDelayed(hideRunnable, 3000);
//                }
//            }
//        });
//        mPlayer.setEnabled(false);
//    }
//
//    private void playOrPauseVideo() {
//        if (TextUtils.isEmpty(mVideoUrl)) return;
//        if (mPlayer.isPlaying()) {
//            pause();
//        } else {
//            play();
//        }
//
//    }
//
//    public void pause() {
//        mPlayer.pause();
//        btnPlay.setImageResource(R.drawable.ic_common_play);
//        handler.removeCallbacks(hideRunnable);
//        handler.removeCallbacks(updateProgressRunnable);
//        if (!isfirstPlay) {  //第一次播放之前不显示控制条(显示中间的播放按钮),第一次播放后暂停时显示控制条
//            handleMediaController(true);
//        }
//    }
//
//    public void play() {
//        Log.d("play", "....");
//        if(isfirstPlay) mPlayer.setVideoPath(mVideoUrl);
//        mPlayer.start();
//        btnPlay.setImageResource(R.drawable.ic_common_pause);
//        handler.post(updateProgressRunnable);
//        handler.postDelayed(hideRunnable, 3000);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.rlPlay:
//                playOrPauseVideo();
//                break;
//            case R.id.rlFullScreen:
//                playOrPauseVideo();
//                Bundle bundle = new Bundle();
//                bundle.putString("previewImgUrl", mPreviewImgUrl);
//                bundle.putString("videoUrl", mVideoUrl);
//                PageJumpUtil.pageJump(context, VideoFullScreenActivity.class, bundle);
//                if (mPlayer.isPlaying()) {
//                    pause();
//                }
//                break;
//
//            case R.id.ivCenterPlay:
//                clickPlay();
//                break;
//        }
//    }
//
//    public void clickPlay(){
//        mPlayer.setEnabled(true);
//        ivCenterPlay.setVisibility(View.GONE);
//        ivPreView.setVisibility(View.GONE);
//        playOrPauseVideo();
//        isfirstPlay = false;
//    }
//}
