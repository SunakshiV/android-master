package com.procoin.util;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.common.constant.CommonConst;

import org.json.JSONObject;

public class MyClickableSpan extends ClickableSpan {

    private final int mTime = 1000;// 两次点击时间间隔
    private String mFDM;// 股票代码fullcode
    private String mJC;// 股票名称
    private int color;
    private boolean isUnderline;
    private static long lastClickTime;
    private final String type; //type=$,type=#,type=@

    private String topic;
    private String json;
    private int isPageJump;//是否跳转 0不用跳转,1跳到用户信息  这个字段就是json中的type字段
    private String params;


    public MyClickableSpan(String type, String mJC, String mFDM) {
        this(type, mJC, mFDM, -1);
    }

    public MyClickableSpan(String type, String mJC, String mFDM, int color) {
        this(type, mJC, mFDM, color, true);
    }
    //这个是代码
    public MyClickableSpan(String type, String mJC, String mFDM, int color, boolean isUnderline) {
        this.mFDM = mFDM;
        this.type = type;
        this.mJC = mJC;
        this.color = color;
        this.isUnderline = isUnderline;
    }


    //网址和话题都用这个
    public MyClickableSpan(String type, String topicOrUrl, int color, boolean isUnderline) {
        this.type = type;
        this.topic = topicOrUrl;
        this.color = color;
        this.isUnderline = isUnderline;
    }


    //这个是@
    public MyClickableSpan(String type, String json, int color) {
        this.type = type;
        this.json = json;
        this.color = color;
        if (!TextUtils.isEmpty(json)) {
            try {
                JSONObject jsonObject = new JSONObject(json);
                if (JsonParserUtils.hasAndNotNull(jsonObject, "params")) {
                    params = jsonObject.getString("params");
                }
                if (JsonParserUtils.hasAndNotNull(jsonObject, "type")) {
                    isPageJump = jsonObject.getInt("type");
                }


//                if (isPageJump == 1 && !TextUtils.isEmpty(params)) {
//                    CommonUtil.jump2UserInfo(context, params);
//                }

            } catch (Exception e) {

            }
        }
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if (ds!=null){
            ds.setColor(color);
            ds.setUnderlineText(false);
        }
    }
    @Override
    public void onClick(View widget) {
        // TODO Auto-generated method stub
        if (isFastDoubleClick()) return;
        Activity activity=getActivityFromView(widget);//直接这样(Activity) context强转会报错
        Log.d("MyClickableSpan","activity=="+activity);
        if(activity==null)return;
        // 原来的是如果长按的话弹出复制。删除框，但是依然会执行ClickableSpan跳转页面，这是不对的，弹框之后hasWindowFocus=false，就return。这里采用了取巧的方式
        if (!activity.hasWindowFocus()) return;
//        if (context instanceof Activity) {
            Intent mIntent = new Intent();
            Bundle bundle = new Bundle();
            ComponentName comp = null;
//            if ("$".equals(type)) {
//                bundle.putString(CommonConst.STOCKNAME, mJC);
//                bundle.putString(CommonConst.FULLCODE, mFDM);
//                if (mFDM.equals(CommonConst.SH000001) || mFDM.equals(CommonConst.SZ399001) || mFDM.equals(CommonConst.SZ399005) || mFDM.equals(CommonConst.SZ399006)) {//4个指数
//                    comp = new ComponentName("com.cropyme", "com.cropyme.quotation.index.IndexActivity");//
//                } else {
//                    bundle.putBoolean(CommonConst.ISARROW, false);
//                    comp = new ComponentName("com.cropyme", "com.cropyme.quotation.stock.StockActivity");//
//                }
//            } else if ("#".equals(type)) {
//                bundle.putString(CommonConst.TOPICNAME, topic);
//                comp = new ComponentName("com.cropyme", "com.cropyme.square.SearchTopicActivity");//
//            } else

            if ("@".equals(type)) {
                if (isPageJump == 1 && !TextUtils.isEmpty(params)) {
//TODO                    CommonUtil.jump2UserInfo(context, params);
                } else if (isPageJump == 2 && !TextUtils.isEmpty(params)) {
                    comp = new ComponentName("com.coingo", "com.coingo.circle.LuckyBoxDetailActivity");//
                    mIntent.setComponent(comp);
                    mIntent.putExtra(CommonConst.PARAMS, params);
                    PageJumpUtil.pageJump(activity, mIntent);
                }
                return;
            }else if("web".equals(type)){
//                bundle.putString(CommonConst.URLS, topic);//topic为网址或者话题
//                comp = new ComponentName("com.tjr.perval", "com.tjr.perval.common.web.CommonWebViewActivity");//
                //私聊里面的链接统一跳转系统的浏览器

                mIntent.setAction(Intent.ACTION_VIEW);
                try{
                    mIntent.setData(Uri.parse(topic));
                    activity.startActivity(mIntent);
                    return;
                }catch (Exception e){
                    if(!topic.startsWith("http")&&!topic.startsWith("https://")){
                        mIntent.setData(Uri.parse("http://"+topic));
                        activity.startActivity(mIntent);
                        return;
                    }

                }

            }
            mIntent.putExtras(bundle);
            mIntent.setComponent(comp);
            mIntent.setAction("android.intent.action.VIEW");
//			((Activity) context).startActivity(mIntent);
//			((Activity) context).overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);// 进入动画
            PageJumpUtil.pageJump(activity, mIntent);
//        }
    }

    public AppCompatActivity getActivityFromView(View view) {
        if(view==null)return null;
        Context context = view.getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof AppCompatActivity) {
                return (AppCompatActivity) context;
            }
            context = ((ContextWrapper) context).getBaseContext();
        }
        return null;
    }
//    @Override
//    public void updateDrawState(TextPaint ds) {
//        // TODO Auto-generated method stub
//        if (color == -1) {
//            ds.setColor(ds.linkColor);
//        } else {
//            ds.setColor(color);
//        }
//        ds.setUnderlineText(isUnderline);
//    }

    private boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < mTime) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
}
