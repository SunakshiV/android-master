package com.procoin.module.circle.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.procoin.R;


public class BmplitudeText extends AppCompatTextView {
    private int minAmplitude;//当超过这个值的时候就把透明度设置为255 （完全不透明）

    public BmplitudeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BmplitudeText);
        minAmplitude = ta.getInt(R.styleable.BmplitudeText_minAmplitude, 1);
        ta.recycle();
    }

    public BmplitudeText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BmplitudeText(Context context) {
        this(context, null);
    }

    public void setAmplitude(int amplitude) {
//		Log.d("setAlphaSize", "alphaSize=="+alphaSize);
        if (amplitude > minAmplitude) {
            if (minAmplitude == 1) {
                if (amplitude < 10) {
                    setBackgroundResource(R.drawable.ic_amplitude_transparent);
                } else {
                    setBackgroundResource(R.drawable.ic_amplitude_blue);
                }

            } else if (minAmplitude == 30) {
                if (amplitude < 40) {
                    setBackgroundResource(R.drawable.ic_amplitude_transparent);
                } else {
                    setBackgroundResource(R.drawable.ic_amplitude_blue);
                }
            } else {
                if (amplitude < 80) {
                    setBackgroundResource(R.drawable.ic_amplitude_transparent);
                } else {
                    setBackgroundResource(R.drawable.ic_amplitude_blue);
                }
            }
        } else {
            setBackgroundResource(R.drawable.ic_amplitude_white);
        }
    }

}
