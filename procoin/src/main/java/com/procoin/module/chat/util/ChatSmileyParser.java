package com.procoin.module.chat.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.util.Log;

import com.procoin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//TJRFace_
public class ChatSmileyParser {
    private Context mContext;
    private Pattern mPattern;
    private HashMap<String, Integer> mSmileyToRes;
    private ArrayList<String> mSmileyList;

    private static ChatSmileyParser instance;

    public static ChatSmileyParser getInstance(Context context) {
        if (instance == null) {
            synchronized (ChatSmileyParser.class) {
                if (instance == null) instance = new ChatSmileyParser(context);
            }
        }
        return instance;
    }

    public ArrayList<String> getmSmileyList() {
        return mSmileyList;
    }

    public HashMap<String, Integer> getmSmileyToRes() {
        return mSmileyToRes;
    }

    private ChatSmileyParser(Context context) {
        mContext = context;
        mSmileyList = new ArrayList<String>();
        mSmileyToRes = buildSmileyToRes();
        mPattern = buildPattern();
    }

    private HashMap<String, Integer> buildSmileyToRes() {

        Map<String, String> temp = ResourceUtils.getHashMapResource(mContext, R.xml.face);
        int len = temp.size();
        HashMap<String, Integer> smileyToRes = new HashMap<String, Integer>(len);
        for (String str : temp.keySet()) {
            smileyToRes.put(str, getResource(temp.get(str)));
            mSmileyList.add(str);
        }
        return smileyToRes;
    }

    private Pattern buildPattern() {
        StringBuilder patternString = new StringBuilder(mSmileyToRes.size() * 3);
        patternString.append('(');
        for (String s : mSmileyToRes.keySet()) {
            patternString.append(Pattern.quote(s));
            patternString.append('|');
        }
        patternString.replace(patternString.length() - 1, patternString.length(), ")");
//		Log.i("ddd", "patternString " + patternString);
        return Pattern.compile(patternString.toString());
    }

    public CharSequence replace(CharSequence text) {
        if (text == null) return "";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            // Drawable drawable = mContext.getResources().getDrawable(resId);
            // drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth()),
            // (int) (drawable.getIntrinsicHeight() * 1.5));
            // ImageSpan localImageSpan = new ImageSpan(drawable, 1);
            builder.setSpan(new ImageSpan(mContext, resId), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    /**
     * 股友圈用到
     *
     * @param text
     * @param size 为原来的size倍
     * @return
     */
    public CharSequence replaceSamll(CharSequence text, double size) {
//        if (text == null) return "";
//        SpannableStringBuilder builder = new SpannableStringBuilder(text);
//        Matcher matcher = mPattern.matcher(text);
//        while (matcher.find()) {
//            int resId = mSmileyToRes.get(matcher.group());
//            Drawable drawable = mContext.getResources().getDrawable(resId);
//            drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * size), (int) (drawable.getIntrinsicHeight() * size));
//            ImageSpan localImageSpan = new ImageSpan(drawable, 1);
//            builder.setSpan(localImageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        return builder;
        return replaceSmallWithPadding(text,size,0);
    }

    public CharSequence replaceSmallWithPadding(CharSequence text,double size,int padding){
        if (text == null) return "";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(padding, padding, (int) (drawable.getIntrinsicWidth() * size), (int) (drawable.getIntrinsicHeight() * size));
            ImageSpan localImageSpan = new ImageSpan(drawable, 1);
            builder.setSpan(localImageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
    public CharSequence replaceSmallAutoFill(CharSequence text, TextPaint paint){
        if (text == null) return "";
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();

        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            float height = fontMetrics.bottom - fontMetrics.top;
            float top = -fontMetrics.ascent+fontMetrics.descent;
            int padding = (int) ((top - height)/2);
            double scale = top/drawable.getIntrinsicHeight();
            Log.d("120","height == "+height +" top == "+top+" scale == "+scale+" Descent == "+fontMetrics.descent);
            drawable.setBounds(0, (int) (fontMetrics.descent), (int)(drawable.getIntrinsicWidth()*scale) ,(int) (drawable.getIntrinsicHeight()*scale) );
            ImageSpan localImageSpan = new ImageSpan(drawable, 1);
            builder.setSpan(localImageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }
    /**
     * 这个方法是圈子私聊加了@功能用到的，因为@别人的时候用到了SpannableString ，所以表情也要重新替换，否则@别人之后表情就变成的文字，所以这里传了个SpannableString过来
     *
     * @param text
     * @param size 为原来的size倍
     * @return
     */
    public SpannableString replaceSamll(SpannableString text, double size) {
        if (text == null) return null;
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * size), (int) (drawable.getIntrinsicHeight() * size));
            ImageSpan localImageSpan = new ImageSpan(drawable, 1);
            text.setSpan(localImageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return text;
    }

    /**
     * 股友圈用到
     *
     * @param text      文本
     * @param cFontSize 传入字体的大小
     * @return
     */
    public CharSequence replace(CharSequence text, float cFontSize) {
        if (text == null) return "";
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        Matcher matcher = mPattern.matcher(text);
        while (matcher.find()) {
            int resId = mSmileyToRes.get(matcher.group());
            Drawable drawable = mContext.getResources().getDrawable(resId);
            drawable.setBounds(0, 0, (int) (drawable.getIntrinsicWidth() * (cFontSize / 48.0)), (int) (drawable.getIntrinsicHeight() * (cFontSize / 48.0)));
            ImageSpan localImageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            // new ImageSpan(mContext, resId)
            builder.setSpan(localImageSpan, matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return builder;
    }

    /**
     * 获取图片名称获取图片的资源id的方法
     *
     * @param imageName
     * @return
     */
//    public int getResourceByReflect(String imageName) {
//        Class<drawable> drawable = R.drawable.class;
//        Field field = null;
//        int r_id;
//        try {
//            field = drawable.getField("tjr_face_" + imageName);
//            r_id = field.getInt(field.getName());
//        } catch (Exception e) {
//            r_id = R.drawable.tjr_face_1;
//            Log.e("ERROR", "PICTURE NOT　FOUND！");
//        }
//        return r_id;
//    }

    public int getResource(String imageName) {
        int resId = mContext.getResources().getIdentifier("tjr_face_" + imageName, "drawable", mContext.getPackageName());
        return resId;
    }
}
