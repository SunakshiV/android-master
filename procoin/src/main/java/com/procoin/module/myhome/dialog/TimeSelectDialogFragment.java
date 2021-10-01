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

import com.procoin.http.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.DensityUtil;
import com.procoin.widgets.wheelview.NumericWheelAdapter;
import com.procoin.widgets.wheelview.OnWheelChangedListener;
import com.procoin.widgets.wheelview.WheelView;
import com.procoin.R;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by zhengmj on 19-4-17.
 */

public class TimeSelectDialogFragment extends DialogFragment implements View.OnClickListener {
    private View view;
    private WheelView wv_year;
    private WheelView wv_month;
    private WheelView wv_day;
    private Button btn_sure;
    private Button btn_cancel;

    public final static int START_YEAR = 1960;//最早起始年份
    private int END_YEAR;//最晚年份,一般是当前年份。但可根据需求改变
    private Date curDate;//当前日期
    private SimpleDateFormat formatter;
    private List<String> list_big;
    private List<String> list_little;


    private Date showDate = new Date();//默认显示日期
    private OnDateButtonClickListener onDateButtonClickListener;


    public static TimeSelectDialogFragment newInstance(Date date, OnDateButtonClickListener onDateButtonClickListener) {
        TimeSelectDialogFragment timeSelectDialogFragment = new TimeSelectDialogFragment();
        timeSelectDialogFragment.onDateButtonClickListener = onDateButtonClickListener;
        timeSelectDialogFragment.showDate = date;
        return timeSelectDialogFragment;
    }
    public static TimeSelectDialogFragment newInstance(OnDateButtonClickListener onDateButtonClickListener) {
        TimeSelectDialogFragment timeSelectDialogFragment = new TimeSelectDialogFragment();
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

    public void setShowDate(Date showDate) {
        this.showDate = showDate;
    }

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
        view = inflater.inflate(R.layout.wheelview_setting_layout, container, false);
        wv_year = view.findViewById(R.id.year);
        wv_month = view.findViewById(R.id.month);
        wv_day = view.findViewById(R.id.day);
        btn_sure = view.findViewById(R.id.btn_datetime_sure);
        btn_cancel = view.findViewById(R.id.btn_datetime_cancel);

        Calendar c = Calendar.getInstance(Locale.getDefault());
        curDate = DateUtils.strdate2Date(DateUtils.date2strdate(c.getTime(), DateUtils.TEMPLATE_yyyyMMdd), DateUtils.TEMPLATE_yyyyMMdd); //为了去掉时分秒
        END_YEAR = Integer.parseInt(date2Str(curDate, "yyyy"));

        String[] months_big = new String[]{"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = new String[]{"4", "6", "9", "11"};
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);

        int textSize = 20;
        textSize = DensityUtil.dip2px(getActivity(), textSize);

        //年
        wv_year.setAdapter(new NumericWheelAdapter(START_YEAR, END_YEAR));// 设置"年"的显示数据
        wv_year.setCyclic(true);// 可循环滚动
        wv_year.setLabel("年");// 添加文字
        wv_year.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int year_num = newValue + START_YEAR;
                int currpos = wv_day.getCurrentItem();// 记录当前索引
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    else wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                }
                wv_day.setCurrentItem(currpos);
            }
        });
        wv_year.TEXT_SIZE = textSize;

        //月
        wv_month.setAdapter(new NumericWheelAdapter(1, 12));
        wv_month.setCyclic(true);
        wv_month.setLabel("月");
        wv_month.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int month_num = newValue + 1;
                int currpos = wv_day.getCurrentItem();// 记录当前索引
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 31));
                } else if (list_little.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(new NumericWheelAdapter(1, 30));
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0 && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0) || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 29));
                    } else {
                        wv_day.setAdapter(new NumericWheelAdapter(1, 28));
                    }
                }
                wv_day.setCurrentItem(currpos);
            }
        });
        wv_month.TEXT_SIZE = textSize;

        //日
        wv_day.setCyclic(true);
        wv_day.setLabel("日");
        wv_day.TEXT_SIZE = textSize;

        btn_cancel.setOnClickListener(this);
        btn_sure.setOnClickListener(this);


        updateViewData(showDate);

        return view;
    }

    /**
     * 跟新日期选择 popupWindow 弹出时显示的时间
     *
     * @param d 要更改日期的 TextView 中的日期
     */
    public void updateViewData(Date d) {
        int year = Integer.parseInt(date2Str(d, "yyyy"));
        int month = Integer.parseInt(date2Str(d, "MM"));
        int day = Integer.parseInt(date2Str(d, "dd"));

        //年
        wv_year.setCurrentItem(year - START_YEAR);
        //月
        wv_month.setCurrentItem(month - 1);
        if (list_big.contains(String.valueOf(month))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 31));
        } else if (list_little.contains(String.valueOf(month))) {
            wv_day.setAdapter(new NumericWheelAdapter(1, 30));
        } else {
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(new NumericWheelAdapter(1, 29));
            else wv_day.setAdapter(new NumericWheelAdapter(1, 28));
        }
        //日
        wv_day.setCurrentItem(day - 1);
    }

    private String date2Str(Date date, String format) {
        if (null == formatter) {
            formatter = new SimpleDateFormat();
        }
        formatter.applyPattern(format);

        return formatter.format(date);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_datetime_cancel:
                dismiss();
                break;
            case R.id.btn_datetime_sure:
                String search_year = String.valueOf(wv_year.getCurrentItem() + START_YEAR);
                String search_month = String.format("%02d", wv_month.getCurrentItem() + 1);
                String search_day = String.format("%02d", wv_day.getCurrentItem() + 1);
                String selectTime = search_year + search_month + search_day; //当前选择的时间(没有限制范围的时间)

                //判断日期的合法性
                if (DateUtils.strdate2Date(selectTime, DateUtils.TEMPLATE_yyyyMMdd).after(curDate)) {
                    CommonUtil.showToast(getActivity(), "日期不能超过今天", Gravity.CENTER);
                    return;
                }
                onDateButtonClickListener.onSureClick(DateUtils.strdate2Date(selectTime, DateUtils.TEMPLATE_yyyyMMdd),search_year,search_month,search_day);
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
        void onSureClick(Date result, String year, String month, String day);

    }
}
