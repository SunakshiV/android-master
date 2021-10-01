package com.procoin.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.constant.AnimatorCommonConst;
import com.procoin.R;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhengmj on 16-5-11.
 */
public class LinearLayoutMarkMenu extends LinearLayout implements View.OnClickListener {

    private TextView tvCurr, tvFs, tvDayKline, tvWeekKline, tvMonthKline;

    private boolean toggle;

    private ObjectAnimator translateY_game = null;
    private ObjectAnimator alpha_game = null;
    private ObjectAnimator scaleY_game = null;
    private ObjectAnimator scaleX_game = null;

    private List<TextView> textViewList = new ArrayList<>();

    OnMenuItemClick onMenuItemClick;


    public LinearLayoutMarkMenu(Context context) {
        super(context);
        init();
    }

    public LinearLayoutMarkMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @SuppressLint("NewApi")
    public LinearLayoutMarkMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @SuppressLint("NewApi")
    public LinearLayoutMarkMenu(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }


    private void init() {
        View home = LayoutInflater.from(getContext()).inflate(R.layout.market_menu, this);

        tvCurr = home.findViewById(R.id.tvCurr);
        tvFs = home.findViewById(R.id.tvFs);
        tvDayKline = home.findViewById(R.id.tvDayKline);
        tvWeekKline = home.findViewById(R.id.tvWeekKline);
        tvMonthKline = home.findViewById(R.id.tvMonthKline);

        textViewList.add(tvFs);
        textViewList.add(tvDayKline);
        textViewList.add(tvWeekKline);
        textViewList.add(tvMonthKline);

        tvCurr.setOnClickListener(this);
        tvFs.setOnClickListener(this);
        tvDayKline.setOnClickListener(this);
        tvWeekKline.setOnClickListener(this);
        tvMonthKline.setOnClickListener(this);

        initAnim();
    }


    private void initAnim() {
        int duration = 200;
        translateY_game = ObjectAnimator.ofFloat(tvFs, AnimatorCommonConst.TRANSLATION_Y, -130f, 0f).setDuration(duration);
        alpha_game = ObjectAnimator.ofFloat(tvFs, AnimatorCommonConst.ALPHA, 0.2f, 1f).setDuration(duration);
        scaleY_game = ObjectAnimator.ofFloat(tvFs, AnimatorCommonConst.SCALE_Y, 0.2f, 1f).setDuration(duration);
        scaleX_game = ObjectAnimator.ofFloat(tvFs, AnimatorCommonConst.SCALE_X, 0.2f, 1f).setDuration(duration);

    }

    public void setOnMenuItemClick(OnMenuItemClick onMenuItemClick) {
        this.onMenuItemClick = onMenuItemClick;
    }

    public void setTvCurrText(String text){
        if(tvCurr!=null)tvCurr.setText(text);
    }

    private void opemMenu() {

        for (TextView tv : textViewList) {
//            if (tv.getText().toString().equals(tvCurr.getText().toString())) {
//                tv.setVisibility(GONE);
//            } else {
                tv.setVisibility(VISIBLE);
                ViewHelper.setAlpha(tv, 0f);
//            }
        }
        AnimatorSet animatorSet_fs = new AnimatorSet();
        animatorSet_fs.playTogether(translateY_game, alpha_game, scaleY_game, scaleX_game);
//        AnimatorSet anim = null;
        for (TextView tv : textViewList) {
            if(tv.getVisibility()==View.VISIBLE){
                AnimatorSet anim = animatorSet_fs.clone();
                anim.setTarget(tv);
                anim.start();
            }
        }
//        tvFs.setVisibility(VISIBLE);
//        tvDayKline.setVisibility(VISIBLE);
//        tvWeekKline.setVisibility(VISIBLE);
//        tvMonthKline.setVisibility(VISIBLE);
//
//        ViewHelper.setAlpha(tvFs, 0f);
//        ViewHelper.setAlpha(tvDayKline, 0f);
//        ViewHelper.setAlpha(tvWeekKline, 0f);
//        ViewHelper.setAlpha(tvMonthKline, 0f);

//        AnimatorSet animatorSet_fs = new AnimatorSet();
//        animatorSet_fs.playTogether(translateY_game, alpha_game, scaleY_game, scaleX_game);
//
//        AnimatorSet animatorSet_day = animatorSet_fs.clone();
//        animatorSet_day.setTarget(tvDayKline);
////        animatorSet_day.setStartDelay(30);
//
//
//        AnimatorSet animatorSet_week = animatorSet_day.clone();
//        animatorSet_week.setTarget(tvWeekKline);
////        animatorSet_week.setStartDelay(60);
//
//
//        AnimatorSet animatorSet_month = animatorSet_week.clone();
//        animatorSet_month.setTarget(tvMonthKline);
////        animatorSet_month.setStartDelay(90);
//
//        animatorSet_fs.start();
//        animatorSet_day.start();
//        animatorSet_week.start();
//        animatorSet_month.start();
        toggle = true;


    }

    public void closeMenu(String title) {
        if (toggle) {
            tvFs.setVisibility(GONE);
            tvDayKline.setVisibility(GONE);
            tvWeekKline.setVisibility(GONE);
            tvMonthKline.setVisibility(GONE);
            toggle = false;
            if (!TextUtils.isEmpty(title)) {
                tvCurr.setText(title);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCurr:
                if (toggle) {
                    closeMenu("");
                    if (onMenuItemClick != null) {
                        onMenuItemClick.onMenuClose();
                    }
                } else {
                    opemMenu();
                    if (onMenuItemClick != null) {
                        onMenuItemClick.onMenuOpen();
                    }
                }
                break;
            case R.id.tvFs:
                closeMenu(tvFs.getText().toString());
                if (onMenuItemClick != null) {
                    onMenuItemClick.onFsClick();
                }
//                tvFs.setVisibility(GONE);
                break;
            case R.id.tvDayKline:
                closeMenu(tvDayKline.getText().toString());
                if (onMenuItemClick != null) {
                    onMenuItemClick.onDayKlineClick();
//                    setFlowNewsCount(0);
                }
                break;
            case R.id.tvWeekKline:
                closeMenu(tvWeekKline.getText().toString());
                if (onMenuItemClick != null) {
                    onMenuItemClick.onWeekKlineClick();
//                    setGameNewsCount(0);
                }
                break;
            case R.id.tvMonthKline:
                closeMenu(tvMonthKline.getText().toString());
                if (onMenuItemClick != null) {
                    onMenuItemClick.onMonthKlineClick();
                }
                break;
        }
    }


    public interface OnMenuItemClick {

        void onFsClick();

        void onDayKlineClick();

        void onWeekKlineClick();

        void onMonthKlineClick();

        void onMenuOpen();

        void onMenuClose();


    }
}

