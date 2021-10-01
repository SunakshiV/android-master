package com.procoin.module.publish;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.cjt2325.cameralibrary.JCameraView;
import com.cjt2325.cameralibrary.listener.ClickListener;
import com.cjt2325.cameralibrary.listener.ErrorListener;
import com.cjt2325.cameralibrary.listener.JCameraListener;
import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.http.resource.BaseRemoteResourceManager;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;

/**
 * Created by zhengmj on 18-11-13.
 */

public class RecordVideo2Activity extends AppCompatActivity {
    JCameraView jCameraView;

    private BaseRemoteResourceManager mVedioResourceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("iv_change_camera", "onCreate、、、、、、、");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.record_vedio2);

        mVedioResourceManager = ((MainApplication) getApplicationContext()).getmVedioResourceManager();
        Log.d("mVedioResourceManager", "mVedioResourceManager==" + mVedioResourceManager);
        String path=mVedioResourceManager.getFile("").getAbsolutePath();
        jCameraView = (JCameraView) findViewById(R.id.jcameraview);
//        path=Environment.getExternalStorageDirectory().getPath() + File.separator + "JCamera";
//设置视频保存路径
        jCameraView.setSaveVideoPath(path);

//设置只能录像或只能拍照或两种都可以（默认两种都可以）
        jCameraView.setFeatures(JCameraView.BUTTON_STATE_ONLY_RECORDER);

        jCameraView.setTip("长按摄像");

//设置视频质量
        jCameraView.setMediaQuality(JCameraView.MEDIA_QUALITY_MIDDLE);

//JCameraView监听
        jCameraView.setErrorLisenter(new ErrorListener() {
            @Override
            public void onError() {
                //打开Camera失败回调
                Log.i("CJT", "open camera error");
            }

            @Override
            public void AudioPermissionError() {
                //没有录取权限回调
                Log.i("CJT", "AudioPermissionError");
            }
        });

        jCameraView.setJCameraLisenter(new JCameraListener() {
            @Override
            public void captureSuccess(Bitmap bitmap) {
                //获取图片bitmap
                Log.i("JCameraView", "bitmap = " + bitmap.getWidth());
            }

            @Override
            public void recordSuccess(String url, Bitmap firstFrame) {
                //获取视频路径
                Log.i("CJT", "url = " + url);
                Intent intent = new Intent();
                intent.putExtra(CommonConst.VIDEOOUTPUTPATH, url);
                setResult(0x789, intent);
                PageJumpUtil.finishCurr(RecordVideo2Activity.this);
            }
            //@Override
            //public void quit() {
            //    (1.1.9+后用左边按钮的点击事件替换)
            //}
        });
//左边按钮点击事件
        jCameraView.setLeftClickListener(new ClickListener() {
            @Override
            public void onClick() {
                RecordVideo2Activity.this.finish();
            }
        });
//右边按钮点击事件
        jCameraView.setRightClickListener(new ClickListener() {
            @Override
            public void onClick() {
                CommonUtil.showmessage("setRightClickListener",RecordVideo2Activity.this);

            }
        });


    }
}
