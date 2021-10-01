package com.procoin.util;

import android.graphics.Paint;

/**
 * 依据text内容的长度来自动调整text 字体的大小
 */
public class DynamicTextSizeUtil {

    /**
     * 根据字符串的长度动态获取字体的大小（以最小size为标准进行缩放，maxSize限制字体最大的size）
     *
     * @param text    传入的字符串
     * @param mDipSize 字体的 dip size
     * @param mLength 字体大小为 mDipSize 时允许的最大长度
     * @return 经过计算的字体size 单位是 sp
     */
    public static float getDynamicSpSize(String text, float mDipSize, int mLength) {
        float dynamicSpSize = (mDipSize * mLength) / text.length();
        if (dynamicSpSize > mDipSize) {//文本长度较短，获得的dynamicSize 太大时。
            dynamicSpSize = mDipSize;
        }

        return dynamicSpSize;
    }

    /**
     * 根据字符串的长度动态获取字体的大小 使用对应TextView 的Paint 可能比较准确
     * @param mTextPaint
     * @param text  mSpSize 所允许的最长字符串
     * @param mSpSize 字体最小size
     * @param realText 真正的 TextView
     * @return
     */
    public static float getDynamicSpSizeByPaint(Paint mTextPaint, String text, float mSpSize, String realText) {
        if (null == mTextPaint || null == text || null == realText) return 10;//默认
        float dynamicSpSize = mTextPaint.measureText(text) * mSpSize/ mTextPaint.measureText(realText);
        if (dynamicSpSize > mSpSize) {//文本长度较短，获得的dynamicSize 太大时。
            dynamicSpSize = mSpSize;
        }
        return dynamicSpSize;
    }


    /**
     *
     * 这个方法比上面方法好用
     * @param mTextPaint
     * @param maxText 所允许的最长字符串
     * @param minSpSize 字体最小size
     * @param maxSpSize 字体最大size
     * @param realText 真正的text
     * @return
     */
    public static float getDynamicSpSizeByPaint(Paint mTextPaint, String maxText, float minSpSize,float maxSpSize, String realText) {
        if (null == mTextPaint || null == maxText || null == realText) return 10;//默认
        float dynamicSpSize = mTextPaint.measureText(maxText) * minSpSize/ mTextPaint.measureText(realText);
        if (dynamicSpSize > maxSpSize) {//文本长度较短，获得的dynamicSize 太大时。
            dynamicSpSize = maxSpSize;
        }
        return dynamicSpSize;
    }

}
