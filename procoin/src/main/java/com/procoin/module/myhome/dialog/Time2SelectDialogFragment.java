package com.procoin.module.myhome.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.procoin.util.CommonUtil;
import com.procoin.util.DensityUtil;
import com.procoin.widgets.wheelview.NumericWheelAdapter;
import com.procoin.widgets.wheelview.WheelView;
import com.procoin.R;
import com.procoin.subpush.notify.TPushSettingManager;

/**
 * Created by zhengmj on 19-4-17.
 */

public class Time2SelectDialogFragment extends DialogFragment implements View.OnClickListener {
    private WheelView wv_startTime;
    private WheelView wv_endTime;
    private static final int START_START_TIME = 0;
    private static final int START_END_TIME = 23;

    private static final int END_START_TIME = 1;
    private static final int END_END_TIME = 24;

    private int currStartTime;
    private int currEndTime;

    private OnDateButtonClickListener onDateButtonClickListener;

//
//    public static Time2SelectDialogFragment newInstance(Date date, OnDateButtonClickListener onDateButtonClickListener) {
//        Time2SelectDialogFragment timeSelectDialogFragment = new Time2SelectDialogFragment();
//        timeSelectDialogFragment.onDateButtonClickListener = onDateButtonClickListener;
//        timeSelectDialogFragment.showDate = date;
//        return timeSelectDialogFragment;
//    }

    public static Time2SelectDialogFragment newInstance(OnDateButtonClickListener onDateButtonClickListener) {
        Time2SelectDialogFragment timeSelectDialogFragment = new Time2SelectDialogFragment();
        timeSelectDialogFragment.onDateButtonClickListener = onDateButtonClickListener;
        return timeSelectDialogFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    public void setOnDateButtonClickListener(OnDateButtonClickListener onDateButtonClickListener) {
        this.onDateButtonClickListener = onDateButtonClickListener;
    }

//    public void setShowDate(Date showDate) {
//        this.showDate = showDate;
//    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currStartTime = TPushSettingManager.getInstance().getsStartTime();
        currEndTime = TPushSettingManager.getInstance().getsEndTime();

        View view = inflater.inflate(R.layout.push_time_setting_layout, container, false);
        wv_startTime = (WheelView) view.findViewById(R.id.startTime);
        NumericWheelAdapter startTimeAdapter = new NumericWheelAdapter(START_START_TIME, START_END_TIME);
        wv_startTime.setAdapter(startTimeAdapter);// 设置"年"的显示数据
        wv_startTime.setCyclic(true);// 可循环滚动
        wv_startTime.setLabel(":00");// 添加文字
        wv_startTime.setCurrentItem(currStartTime);//

        wv_endTime = (WheelView) view.findViewById(R.id.endTime);
        wv_endTime.setAdapter(new NumericWheelAdapter(END_START_TIME, END_END_TIME));
        wv_endTime.setCyclic(true);
        wv_endTime.setLabel(":00");
        wv_endTime.setCurrentItem(currEndTime - 1);

        int textSize = 20;
        textSize = DensityUtil.dip2px(getActivity(), textSize);

        wv_startTime.TEXT_SIZE = textSize;
        wv_endTime.TEXT_SIZE = textSize;

        Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
        Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);

        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);


        return view;
    }

//    protected void savePushTime(int currStartTime2, int currEndTime2) {
//        long userId = ((MainApplication) getActivity().getApplicationContext()).getUser().getUserId();
//        TPushSettingManager.getInstance().setSilentTime(String.valueOf(userId), currStartTime2, currEndTime2, getActivity());
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_datetime_cancel:
                dismiss();
                break;
            case R.id.btn_datetime_sure:
                currStartTime = wv_startTime.getCurrentItem();
                currEndTime = wv_endTime.getCurrentItem() + 1;
                if (currStartTime == currEndTime) {
                    CommonUtil.showmessage("设置无效", getActivity());
                    return;
                }
//                savePushTime(currStartTime, currEndTime);// 保存静默时间
//                setPushTime(currStartTime, currEndTime);// 设值
                if(onDateButtonClickListener!=null){
                    onDateButtonClickListener.onSureClick(currStartTime,currEndTime);
                }
                dismiss();
                break;
            default:
                break;
        }
    }


    /**
     * 监听器:监听日期的选择并传出结果
     */
    public interface OnDateButtonClickListener {
        void onSureClick(int currStartTime, int currEndTime);

    }
}
