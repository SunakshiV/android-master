package com.procoin.social.util;

/**
 * 这个类使用来显示能发送多少个文字的，140个文字
 */

import android.text.Editable;
import android.text.TextWatcher;

import java.io.UnsupportedEncodingException;

public class CommentWatcher implements TextWatcher {
    private int maxNum;
    private int leftNum;
    private int inputNum;
    private SurplusNum surplusNum;
    private CommentWatcherInterface watcherInterface;

    public void setWatcherInterface(CommentWatcherInterface watcherInterface) {
        this.watcherInterface = watcherInterface;
    }

    public CommentWatcher(int num, SurplusNum surplusNum) {
        this.maxNum = num * 2;
        this.surplusNum = surplusNum;
    }

    /**
     * 是否已经超过字数
     *
     * @return
     */
    public boolean getNumber() {
        return leftNum >= 0;
    }

    /**
     * 当前已输入中文字数
     *
     * @return
     */
    public int getInputNum() {
        return inputNum;
    }

    /**
     * 设置中文字数（为英文字符的两倍，不设置默认为140个中文，280个英文）
     *
     * @param num
     */
    public void setNum(int num) {
        this.maxNum = num * 2;

    }

    public interface SurplusNum {
        public void callSurplusNum(int leftNum);
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (watcherInterface != null) {
            watcherInterface.onTextChanged(s, start, before, count);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        try {
            leftNum = (maxNum - String.valueOf(s.toString()).getBytes("GBK").length) / 2;
            inputNum = maxNum / 2 - leftNum;

            if (surplusNum != null) surplusNum.callSurplusNum(leftNum);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public interface CommentWatcherInterface {
        public void onTextChanged(CharSequence s, int start, int before, int count);

    }

};