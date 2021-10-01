package com.procoin.module.circle.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

import com.procoin.MainApplication;
import com.procoin.http.resource.BaseRemoteResourceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.util.DynamicPermission;
import com.procoin.util.PermissionUtils;
import com.uraroji.garage.android.mp3recvoice.RecMicToMp3;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

@SuppressLint("HandlerLeak")
public class RecordButton extends AppCompatImageButton {

    private Context context;
    private RecMicToMp3 mRecMicToMp3;
    private BaseRemoteResourceManager remoteResourceManagerTalk;
    // private final String TEMPMP3 = "tempFile.mp3";
    private String mp3Path = "";// 文件全路径
    private String realVoiceName = "";// 文件名
    private static final int MAXTIME = 60 * 1000;// 最大6os
    private static final int MINTIME = 2;// 最少2s
    private int time = 60 * 1000;
    private static final int TIMEWARN = 10 * 1000;// 录音10s警告
    private static final int mSampleRate = 8000;
    private int recordTime;// 录了多长时间

    private int mediaPlayProgress;// 播放进度
    private int mediaPlayMaxProgress;// 播放的持续时间

    private RecordState state = RecordState.getDefault();

    private TextView tvTimeWarn;// 剩下时间警告
    private TextView tvRecordState;// 文字状态
    private TextView tvRecordTime;// 录音时间
    private RoundProgressBar rpb;
    private Button btnRecordAgain;// 重录按钮

    private long systemCurrTime;

    private Timer timer;

    private MediaPlayer mpTryLinten;// 试听的

    private static final String TRYLISTENACTION = "com.procoin.mediaPlay.tryListen";
    private static final String WHENRECORDSTARTACTION = "com.procoin.recordStart.stop";

    private static final String WHENMEDIASTOPACTION = "com.procoin.mediaPlay.stop";

    private BmplitudeText tvBmplitude_left_max, tvBmplitude_left_mid, tvBmplitude_left_min, tvBmplitude_right_min, tvBmplitude_right_mid, tvBmplitude_right_max;

    private Button btnSend;

    private MediaPlayStopCastReceiver mediaPlayStopCastReceiver;

    private long userId;
    private RecordStateListener recordStateListen;


    private DynamicPermission dynamicPermission;
    boolean permission=false;//代表没权限

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                if (msg.obj instanceof RecordState) {
                    setState((RecordState) msg.obj);
                } else if (msg.obj instanceof Integer) {
                    setAmplitude((Integer) msg.obj);
                }
            } else {
                if (msg.what == 9) {// 正在录音
                    if (state.getState() == 1) {
                        time -= 100;
                        if (time < TIMEWARN) {
                            tvTimeWarn.setVisibility(View.VISIBLE);
                            tvTimeWarn.setText(time / 1000 + 1 + "''");
                        }
                        recordTime = MAXTIME - time;
                        // Log.d("time", "time==" + time);
                        if (time < 0) time = 0;
                        rpb.setProgress(time);
                        if (time == 0) {
                            whenRecordEnd();
                        }
                    } else {
                        cancelTimer();
                        // setState(RecordState.INITIALISE);
                    }
                } else if (msg.what == 10) {// 正在播放
                    if (state == RecordState.PLAYING) {
                        mediaPlayProgress += 10;
                        // Log.d("time", "time==" + time);
                        if (mediaPlayProgress >= mediaPlayMaxProgress) {
                            mediaPlayProgress = 0;
                            cancelTimer();
                        }
                        // Log.d("mediaPlayProgress", "mediaPlayProgress=="+
                        // mediaPlayProgress);
                        rpb.setProgress(mediaPlayProgress);
                    }
                }
//                else if (msg.what == 11) {
//                    setState(RecordState.RECORDING);
//                }
                else{
                    // 下面是录音过程当中发送的消息
                }
            }
        }

    };

    public void setRecordStateListener(RecordStateListener recordStateListen) {
        this.recordStateListen = recordStateListen;
    }

    public void setTvBmplitude_left_max(BmplitudeText tvBmplitude_left_max) {
        this.tvBmplitude_left_max = tvBmplitude_left_max;
    }

    protected void setAmplitude(int amplitude) {
        if (state.getState() != 1) {// 已经结束
            amplitude = 0;
        }
        tvBmplitude_left_max.setAmplitude(amplitude);
        tvBmplitude_left_mid.setAmplitude(amplitude);
        tvBmplitude_left_min.setAmplitude(amplitude);

        tvBmplitude_right_max.setAmplitude(amplitude);
        tvBmplitude_right_mid.setAmplitude(amplitude);
        tvBmplitude_right_min.setAmplitude(amplitude);
    }

    public void setBtnRecordAgain(Button btnRecordAgain) {
        this.btnRecordAgain = btnRecordAgain;
        this.btnRecordAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context).setTitle("提示").setMessage("重录会删除刚才的录音").setPositiveButton("重录", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setState(RecordState.INITIALISE);// 不用删除

                    }
                }).setNegativeButton("取消", null).create().show();
                setState(RecordState.RECORDEND);//
            }
        });
    }

    public void setTvBmplitude_left_mid(BmplitudeText tvBmplitude_left_mid) {
        this.tvBmplitude_left_mid = tvBmplitude_left_mid;
    }

    public void setTvBmplitude_left_min(BmplitudeText tvBmplitude_left_min) {
        this.tvBmplitude_left_min = tvBmplitude_left_min;
    }

    public void setTvBmplitude_right_min(BmplitudeText tvBmplitude_right_min) {
        this.tvBmplitude_right_min = tvBmplitude_right_min;
    }

    public void setTvBmplitude_right_mid(BmplitudeText tvBmplitude_right_mid) {
        this.tvBmplitude_right_mid = tvBmplitude_right_mid;
    }

    public void setTvBmplitude_right_max(BmplitudeText tvBmplitude_right_max) {
        this.tvBmplitude_right_max = tvBmplitude_right_max;
    }

    public void setTvTimeWarn(TextView tvTimeWarn) {
        this.tvTimeWarn = tvTimeWarn;
    }

    public void setTvRecordState(TextView tvRecordState) {
        this.tvRecordState = tvRecordState;
    }

    public void setTvRecordTime(TextView tvRecordTime) {
        this.tvRecordTime = tvRecordTime;
    }

    public void setBtnSend(Button btnSend) {
        this.btnSend = btnSend;
    }

    public void setRpb(RoundProgressBar rpb) {
        this.rpb = rpb;
    }

    public String getMp3Path() {
        return mp3Path;
    }

    public int getRecordTime() {
        return recordTime / 1000;
    }

    // public String getTempmp3() {
    // return TEMPMP3;
    // }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getRealVoiceName() {
        return realVoiceName;
    }

    public RecordButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public RecordButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RecordButton(Context context) {
        this(context, null);
    }

    private void init(final Context context) {
        this.context = context;
        // setState(RecordState.getDefault());
        // sendMessage();
        remoteResourceManagerTalk = getApplicatonContext().getRemoteResourceChatManager();
        mediaPlayStopCastReceiver = new MediaPlayStopCastReceiver();
        mediaPlayStopCastReceiver.register();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        if(!permission){
                            initDynamicPermission();
                            dynamicPermission.checkSelfPermission(PermissionUtils.RECORDAUDIO_EXTERNALSTORAGE,100);
                        }
                        if (state.getState() != 0) {
                            return false;
                        }
                        // Log.d("ACTION_DOWN", "ACTION_DOWN////////   state=="+
                        // state.getState());
                        if (systemCurrTime > 0) {
                            if (System.currentTimeMillis() - systemCurrTime <= 1000) {
                                CommonUtil.showToast(context, "点击太频繁了", Gravity.CENTER);
                                return true;
                            }
                        }
                        systemCurrTime = System.currentTimeMillis();
                        whenRecordStart();
                        break;
                    case MotionEvent.ACTION_UP:
                        // Log.d("ACTION_UP", "ACTION_UP////////////");
                        if (mRecMicToMp3 != null && mRecMicToMp3.isRecording()) whenRecordEnd();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(dynamicPermission!=null)dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
    private void initDynamicPermission() {
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission((AppCompatActivity) context, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    permission=true;
                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {

                }
            });
        }
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
         Log.d("RecordButton", "state=="+state);
        if (state == RecordState.RECORDEND) {
            playerRecord();
        } else if (state == RecordState.PLAYING) {
            stopPlay();
            setState(RecordState.RECORDEND);
        }
        return true;
    }

    public RecordState getState() {
        return state;
    }

    public void setState(RecordState state2) {
        // Log.d("setState", "setState=="+state2);
        this.state = state2;
        switch (this.state.getState()) {
            case 0:
                setImageLevel(0);
                rpb.setProgress(0);
                btnRecordAgain.setVisibility(View.GONE);
                setAmplitudeIsVivible(View.VISIBLE);
                if (btnSend != null) btnSend.setEnabled(false);
                if (recordStateListen != null) recordStateListen.onRecordInitialise();
                stopPlay();
                recordTime = 0;
                tvRecordState.setVisibility(View.VISIBLE);
                tvRecordState.setText("按下录音");
                tvRecordTime.setVisibility(View.GONE);
                break;
            case 1:
                // Log.d("recording", "recording正在录音/////////////");
                setImageLevel(1);
                rpb.setMax(MAXTIME);
                rpb.setProgress(MAXTIME);
                if (btnSend != null) btnSend.setEnabled(false);
                if (recordStateListen != null) recordStateListen.onRecording();
                tvRecordState.setText("正在录音");
                break;
            case 2:
                // Log.d("recording", "recording录音结束/////////////");
                setImageLevel(2);
                if (btnSend != null) btnSend.setEnabled(true);
                if (recordStateListen != null)
                    recordStateListen.onRecordEnd(mp3Path, realVoiceName, recordTime / 1000);
                stopPlay();
                btnRecordAgain.setVisibility(View.VISIBLE);
                setAmplitudeIsVivible(View.GONE);
                mediaPlayProgress = 0;// 播放的进度清零
                rpb.setProgress(0);// 录音完成时进度条清零
                setAmplitude(0);
                tvRecordTime.setVisibility(View.VISIBLE);
                tvRecordTime.setText(recordTime / 1000 + "''");
                tvRecordState.setVisibility(View.VISIBLE);
                tvRecordState.setText("点击播放");
                tvTimeWarn.setVisibility(View.GONE);
                break;
            case 3:
                setImageLevel(3);
                if (btnSend != null) btnSend.setEnabled(true);
                if (recordStateListen != null) recordStateListen.onPlaying();
                tvRecordState.setVisibility(View.VISIBLE);
                tvRecordState.setText("点击停止");
                break;

            default:
                break;
        }
    }

    private void stopPlay() {
        if (mpTryLinten != null && mpTryLinten.isPlaying()) {
            mpTryLinten.stop();
            mpTryLinten.reset();
        }
    }

    /**
     * 1 显示 0 隐藏
     *
     * @param i
     */
    private void setAmplitudeIsVivible(int i) {
        tvBmplitude_left_max.setVisibility(i);
        tvBmplitude_left_mid.setVisibility(i);
        tvBmplitude_left_min.setVisibility(i);

        tvBmplitude_right_max.setVisibility(i);
        tvBmplitude_right_mid.setVisibility(i);
        tvBmplitude_right_min.setVisibility(i);
    }

    private void cancelTimer() {// 当用户快速点击按钮的时候
        if (timer != null) {// 取消定时器
            timer.cancel();
            timer = null;
        }

    }

    protected void whenRecordStart() {
        if(!permission)return;
        if (getApplicatonContext().isSDCard()) {
            if (mRecMicToMp3 == null) {
                mRecMicToMp3 = new RecMicToMp3(mSampleRate);
                mRecMicToMp3.setHandle(handler);
            }
            realVoiceName = System.currentTimeMillis() + "" + userId + ".mp3";
            mp3Path = remoteResourceManagerTalk.getFile(realVoiceName).getAbsolutePath();
            mRecMicToMp3.setFilePath(mp3Path);
            mRecMicToMp3.start();// 开始录音
            while (true) {
                Log.d("RecordButton", "mRecMicToMp3.isRecording()=="+mRecMicToMp3.isRecording());
                if (!mRecMicToMp3.isRecording()) continue;// mRecMicToMp3初始化需要一些时间，初始化完成之后才开始
                Intent intent = new Intent();
                intent.setAction(WHENRECORDSTARTACTION);
                context.sendBroadcast(intent);
                time = MAXTIME;
                setState(RecordState.RECORDING);
                if (timer == null) timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(9);
                    }
                }, 1000, 100);
//                handler.sendEmptyMessage(11);

                break;
            }
        } else {
            CommonUtil.showToast(context, "请确定有插入sd卡", Gravity.CENTER);
        }

    }

    // private void sendMessage(RecordState state) {
    // Message message = new Message();
    // message.obj = state;
    // handler.sendMessage(message);
    //
    // }

    protected void whenRecordEnd() {
        if (mRecMicToMp3 == null || !mRecMicToMp3.isRecording()) return;
        mRecMicToMp3.stop();
        cancelTimer();
        if (recordTime / 1000 < MINTIME) {
            setState(RecordState.INITIALISE);
            CommonUtil.showToast(context, "录音时间太短", Gravity.CENTER);
        } else {
            setState(RecordState.RECORDEND);
        }

        // sendMessage();
    }

    private void playerRecord() {
        if (!new File(mp3Path).exists()) return;// 文件上传成功之后，名字已经改了

        if (mp3Path != null && !"".equals(mp3Path)) {
            Intent intent = new Intent();
            intent.setAction(TRYLISTENACTION);
            context.sendBroadcast(intent);
            if (mpTryLinten == null) {
                mpTryLinten = new MediaPlayer();
                mpTryLinten.setOnErrorListener(new OnErrorListener() {
                    @Override
                    public boolean onError(MediaPlayer mp, int what, int extra) {
                        CommonUtil.showToast(context, "播放异常", Gravity.CENTER);
                        mp.reset();
                        return false;
                    }
                });
                mpTryLinten.setOnCompletionListener(new OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.reset();
                        cancelTimer();
                        setState(RecordState.RECORDEND);
                    }
                });
            }
            try {
                mpTryLinten.setDataSource(mp3Path);
                mpTryLinten.prepare();
                mediaPlayMaxProgress = mpTryLinten.getDuration();
                rpb.setMax(mediaPlayMaxProgress);
                mpTryLinten.start();
                if (timer == null) {
                    timer = new Timer();
                    timer.scheduleAtFixedRate(new TimerTask() {
                        @Override
                        public void run() {
                            handler.sendEmptyMessage(10);
                        }
                    }, 0, 10);
                }
                setState(RecordState.PLAYING);
            } catch (Exception e) {
                // Log.d("Exception", "e==" + e);
                CommonUtil.showToast(context, "播放异常", Gravity.CENTER);
            }
        }
    }

    private MainApplication getApplicatonContext() {
        return (MainApplication) context.getApplicationContext();
    }

    public static enum RecordState {
        INITIALISE(0), RECORDING(1), RECORDEND(2), PLAYING(3);

        private int state;

        public int getState() {
            return state;
        }

        RecordState(int state) {
            this.state = state;
        }

        public static RecordState getDefault() {
            return INITIALISE;
        }
    }

    private class MediaPlayStopCastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (WHENMEDIASTOPACTION.equals(intent.getAction())) {
                // Log.d("broadcase", "需要暂停、、、、、、、");
                if (state == RecordState.PLAYING) {
                    setState(RecordState.RECORDEND);
                }
            }
        }

        public void register() {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(WHENMEDIASTOPACTION);
            context.registerReceiver(this, intentFilter);
        }
    }

    public void stop() {
        if (state == RecordState.RECORDING) {
            mRecMicToMp3.stop();
            cancelTimer();
            setState(RecordState.INITIALISE);
        } else if (state == RecordState.PLAYING) {
            setState(RecordState.RECORDEND);
        }
    }

    public void release() {
        if (mpTryLinten != null) {
            mpTryLinten.release();
            mpTryLinten = null;
        }
        try {
            context.unregisterReceiver(mediaPlayStopCastReceiver);
        } catch (Exception e) {
        }

    }
//	INITIALISE(0), RECORDING(1), RECORDEND(2), PLAYING(3);

    /**
     * 监听录音状态，以便做其他操作
     *
     * @author zhengmj
     */
    public interface RecordStateListener {
        public void onRecordInitialise();

        public void onRecording();

        public void onRecordEnd(String mp3Path, String fileName, int recordTime);

        public void onPlaying();
    }
}
