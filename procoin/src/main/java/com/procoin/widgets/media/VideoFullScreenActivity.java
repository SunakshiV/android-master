//package com.tjr.imredz.widgets.media;
//
//import android.app.Activity;
//import android.content.pm.ActivityInfo;
//import android.content.res.Configuration;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Window;
//import android.view.WindowManager;
//
//import com.tjr.imredz.util.CommonUtil;
//
///**
// * Created by zhengmj on 18-10-9.
// */
//
//public class VideoFullScreenActivity extends Activity {
//
//    private String mVideoUrl;
//    private String mPreviewImgUrl;
//    private int mPreviewImgRes=-1;
//    private VideoViewManager videoViewManager;
//    private Handler handler;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
////        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        Bundle bundle = null;
//        if ((bundle = getIntent().getExtras()) != null) {
//            if (bundle.containsKey("previewImgUrl")) {
//                mPreviewImgUrl = bundle.getString("previewImgUrl");
//            }
//            if (bundle.containsKey("mPreviewImgRes")) {
//                mPreviewImgRes = bundle.getInt("mPreviewImgRes",-1);
//            }
//            if (bundle.containsKey("videoUrl")) {
//                mVideoUrl = bundle.getString("videoUrl");
//            }
//        }
//        if (TextUtils.isEmpty(mVideoUrl)) {
//            CommonUtil.showmessage("参数错误", this);
//            finish();
//        }
//        videoViewManager = new VideoViewManager(this);
//        setContentView(videoViewManager.getView());
//        videoViewManager.setUrl(mPreviewImgUrl, mVideoUrl);
//
//        if(mPreviewImgRes!=-1)videoViewManager.setmPreviewImgUrl(mPreviewImgRes);
//
//        videoViewManager.setBtnFullScreenVisible(false);
//        handler=new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                videoViewManager.clickPlay();
//            }
//        },1000);
//
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (videoViewManager != null) {
//            videoViewManager.release();
//        }
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        Log.d("VideoFullScreen", "newConfig==" + newConfig.orientation);
//        if (videoViewManager != null) {//
//            videoViewManager.setIvPreViewHeight(newConfig.orientation);
//        }
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (videoViewManager != null) {
//            videoViewManager.pause();
//        }
//    }
//
////    @Override
////    public void onBackPressed() {
////        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
////            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
////        } else {
////            super.onBackPressed();
////        }
////    }
//}
