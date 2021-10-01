package com.procoin.module.myhome;

import android.os.Bundle;
import androidx.appcompat.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.module.myhome.dialog.Time2SelectDialogFragment;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.subpush.notify.TPushSettingManager;
import com.procoin.R;

public class PushSettingsActivity extends TJRBaseToolBarSwipeBackActivity {
    private SwitchCompat toggleView;// 消息推送开关
    private SwitchCompat toggleView2;// 勿扰模式开关
    private SwitchCompat toggleView3;// 声音开关
    private SwitchCompat toggleView4;// 振动开关
    //	private View vLine;
    // private LinearLayout llSetTime;
    private LinearLayout llDisturbMode;
    private LinearLayout llTime;
    private TextView tvTime;
//	private PopupWindow pw;

    private Time2SelectDialogFragment time2SelectDialogFragment;


    private int currStartTime;
    private int currEndTime;


    @Override
    protected int setLayoutId() {
        return R.layout.push_settings;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.pushSettings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		setContentView(R.layout.push_settings);
        llDisturbMode = (LinearLayout) this.findViewById(R.id.llDisturbMode);
        llTime = (LinearLayout) this.findViewById(R.id.llTime);
//		vLine = this.findViewById(R.id.vLine);
        // llSetTime=(LinearLayout) this.findViewById(R.id.llSetTime);
        // llSetTime.setOnClickListener(onclick);
        tvTime = (TextView) this.findViewById(R.id.tvTime);
        OnClick onclick = new OnClick();
        llTime.setOnClickListener(onclick);
        currStartTime = TPushSettingManager.getInstance().getsStartTime();
        currEndTime = TPushSettingManager.getInstance().getsEndTime();
        setPushTime(currStartTime, currEndTime);

        // 消息推送
        boolean toggle = TPushSettingManager.getInstance().isTurnOff();
        toggleView = (SwitchCompat) this.findViewById(R.id.toggleView);
        toggleView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    llDisturbMode.setVisibility(View.VISIBLE);
                } else {
                    llDisturbMode.setVisibility(View.GONE);
                }
                saveState(isChecked);
            }
        });
        toggleView.setChecked(toggle);

        // 勿扰
        boolean pushDisturbmodeToggle = TPushSettingManager.getInstance().isSilent();// 默认关
        toggleView2 = (SwitchCompat) this.findViewById(R.id.toggleView2);
        toggleView2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("toggleView2","onCheckedChanged=="+isChecked);
                if (isChecked) {
                    llTime.setVisibility(View.VISIBLE);
                } else {
                    llTime.setVisibility(View.GONE);

                }
                saveDisturbmode(isChecked);
            }
        });
        Log.d("toggleView2","pushDisturbmodeToggle=="+pushDisturbmodeToggle);
        toggleView2.setChecked(pushDisturbmodeToggle);

        // 声音
        int pushVoiceToggle = TPushSettingManager.getInstance().getRing();
        toggleView3 = (SwitchCompat) this.findViewById(R.id.toggleView3);
        toggleView3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePushVoiceToggle(isChecked);
            }
        });
        toggleView3.setChecked(pushVoiceToggle == 1);

        // 振动
        int pushVibrateToggle = TPushSettingManager.getInstance().getVibrate();
        toggleView4 = (SwitchCompat) this.findViewById(R.id.toggleView4);

        toggleView4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                savePushVibrateToggle(isChecked);
            }
        });
        toggleView4.setChecked(pushVibrateToggle == 1);

    }

    protected void savePushVibrateToggle(boolean flag) {
        // SharedPreferences
        // sp=getSharedPreferences(CommonConst.PUSHTOGGLE+"_"+getApplicationContext().getUser().getUserId(),
        // Context.MODE_PRIVATE);
        // Editor editor=sp.edit();
        // editor.putInt(CommonConst.PUSHVIBRATETOGGLE, flag?1:0).commit();
        TPushSettingManager.getInstance().setVibrate(String.valueOf(getApplicationContext().getUser().getUserId()), flag ? 1 : 0, PushSettingsActivity.this);
    }

    protected void savePushVoiceToggle(boolean flag) {
        // SharedPreferences
        // sp=getSharedPreferences(CommonConst.PUSHTOGGLE+"_"+getApplicationContext().getUser().getUserId(),
        // Context.MODE_PRIVATE);
        // Editor editor=sp.edit();
        // editor.putInt(CommonConst.PUSHVOICETOGGLE, flag?1:0).commit();
        TPushSettingManager.getInstance().setRing(String.valueOf(getApplicationContext().getUser().getUserId()), flag ? 1 : 0, PushSettingsActivity.this);
    }

    private void setPushTime(int currStartTime, int currEndTime) {
        tvTime.setText(getFormatTimer(currStartTime) + "-" + (currEndTime - currStartTime < 0 ? "次日" : "") + getFormatTimer(currEndTime));
    }

    private String getFormatTimer(int time) {
        String format = "";
        if (time < 24 && time >= 18) {
            format = "晚上";
        } else if (time < 18 && time > 12) {
            format = "下午";
        } else if (time < 12 && time >= 6) {
            format = "上午";
        } else if (time < 6) {
            format = "凌晨";
        } else if (time == 12) {
            format = "中午";
        } else if (time == 24) {
            return "晚上23:59";
        }
        return format + time + ":00";
    }

    protected void saveState(boolean flag) {
        TPushSettingManager.getInstance().setTurnOff(String.valueOf(getApplicationContext().getUser().getUserId()), flag, PushSettingsActivity.this);
    }

    protected void saveDisturbmode(boolean flag) {
        TPushSettingManager.getInstance().setSilent(String.valueOf(getApplicationContext().getUser().getUserId()), flag, PushSettingsActivity.this);
    }

    public void showDateTimePicker() {

        time2SelectDialogFragment = Time2SelectDialogFragment.newInstance(new Time2SelectDialogFragment.OnDateButtonClickListener() {
            @Override
            public void onSureClick(int currStartTime, int currEndTime) {
                savePushTime(currStartTime, currEndTime);// 保存静默时间
                setPushTime(currStartTime, currEndTime);// 设值
            }
        });
        time2SelectDialogFragment.show(getSupportFragmentManager(), "");

//		if (pw == null) {
//
//			View view = InflaterUtils.inflateView(this, R.layout.push_time_setting_layout);
//
//			final WheelView wv_startTime = (WheelView) view.findViewById(R.id.startTime);
//			NumericWheelAdapter startTimeAdapter = new NumericWheelAdapter(START_START_TIME, START_END_TIME);
//			wv_startTime.setAdapter(startTimeAdapter);// 设置"年"的显示数据
//			wv_startTime.setCyclic(true);// 可循环滚动
//			wv_startTime.setLabel(":00");// 添加文字
//			wv_startTime.setCurrentItem(currStartTime);//
//
//			final WheelView wv_endTime = (WheelView) view.findViewById(R.id.endTime);
//			wv_endTime.setAdapter(new NumericWheelAdapter(END_START_TIME, END_END_TIME));
//			wv_endTime.setCyclic(true);
//			wv_endTime.setLabel(":00");
//			wv_endTime.setCurrentItem(currEndTime - 1);
//
//			int textSize = 20;
//			textSize = DensityUtil.dip2px(this, textSize);
//
//			wv_startTime.TEXT_SIZE = textSize;
//			wv_endTime.TEXT_SIZE = textSize;
//
//			Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
//			Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);
//			btn_sure.setOnClickListener(new OnClickListener() {
//
//				@Override
//				public void onClick(View arg0) {
//					currStartTime = wv_startTime.getCurrentItem();
//					currEndTime = wv_endTime.getCurrentItem() + 1;
//					if (currStartTime == currEndTime) {
//						CommonUtil.showmessage("设置无效", PushSettingsActivity.this);
//						return;
//					}
//					savePushTime(currStartTime, currEndTime);// 保存静默时间
//					setPushTime(currStartTime, currEndTime);// 设值
//					pw.dismiss();
//				}
//			});
//			// 取消
//			btn_cancel.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					pw.dismiss();
//				}
//			});
//
//			pw = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//			pw.setOutsideTouchable(true);
//			pw.setBackgroundDrawable(new ColorDrawable());// 特别留意这个东东
//			pw.setFocusable(true);
//			pw.setAnimationStyle(R.style.datePickerPop);
//		}
//		pw.showAtLocation(toggleView, Gravity.BOTTOM, 0, 0);
    }

    protected void savePushTime(int currStartTime2, int currEndTime2) {
        TPushSettingManager.getInstance().setSilentTime(String.valueOf(getApplicationContext().getUser().getUserId()), currStartTime2, currEndTime2, PushSettingsActivity.this);
    }

    protected int getDurationTime(int currStartTime2, int currEndTime2) {
        int durationTime = currEndTime2 - currStartTime2;
        if (durationTime < 0) {
            durationTime = durationTime + 24;
        }
        return durationTime;
    }

    private class OnClick implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.llTime:
                    showDateTimePicker();
                    break;

                default:
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
