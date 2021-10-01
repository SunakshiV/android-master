package com.procoin.module.chat.util;

import android.text.Editable;
import android.text.TextWatcher;

import java.io.UnsupportedEncodingException;

/**
 * 监听输入多少个文字
 */
public class CommentWatcher implements TextWatcher {

	private int num;
	/**
	 * 剩余字数
	 */
	private int number;
	private SurplusNum surplusNum;

	public CommentWatcher(int num, SurplusNum surplusNum) {
		this.num = num * 2;
		this.surplusNum = surplusNum;
	}

	/**
	 * 获取现在字数是否超过限制字数
	 * 
	 * @return
	 */
	public boolean getNumber() {
		return number >= 0;
	}

	/**
	 * 当前可输入中文字数
	 * 
	 * @return
	 */
	public int getNum() {
		return (num + 1) / 2;
	}

	/**
	 * 设置中文字数（为英文字符的两倍，不设置默认为140个中文，280个英文）
	 * 
	 * @param num
	 */
	public void setNum(int num) {
		this.num = num * 2;

	}

	public interface SurplusNum {
		public void callSurplusNum(int num);
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void afterTextChanged(Editable s) {
		try {
			number = (num - String.valueOf(s.toString()).getBytes("GBK").length) / 2;
			if (surplusNum != null) surplusNum.callSurplusNum(number);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
};