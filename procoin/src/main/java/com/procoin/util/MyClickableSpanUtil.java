package com.procoin.util;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.widget.TextView;

import com.procoin.R;

import java.util.List;
import java.util.Map;


public class MyClickableSpanUtil{
	private static final String TYPE_STOCK = "$";
	private static final String TYPE_TOPIC = "#";
	private static final String TYPE_AT = "@";
	private static final String TYPE_WEB = "web";
	
	/**
	 * 设置text的内容有股票名称可点击
	 * @param textView
	 * @param content
	 * @param map 如map<fmd,jc>格式:map<sh600000,中国平安>
	 */
	public static void setCustomText(TextView textView,String content,Map<String, String> map,Context context){
		if (textView == null || content == null)return;
		SpannableString ss = new SpannableString(content);
		if (map != null && map.size() >0) {
			for (Map.Entry<String, String> entry:map.entrySet()) {
				if(entry.getValue() != null){
					int vlen = entry.getValue().length();
					int in = content.indexOf(entry.getValue());
					if (in != -1 && vlen >0) {
						ss.setSpan(new MyClickableSpan(TYPE_STOCK,entry.getValue(),entry.getKey(),context.getResources().getColor(R.color.c2593d0)), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		textView.setText(ss);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public static void setCustomText(TextView textView,String content,Map<String, String> map){
		if (textView == null || content == null)return;
		SpannableString ss = new SpannableString(content);
		if (map != null && map.size() >0) {
			for (Map.Entry<String, String> entry:map.entrySet()) {
				if(entry.getValue() != null){
					int vlen = entry.getValue().length();
					int in = content.indexOf(entry.getValue());
					if (in != -1 && vlen >0) {
						ss.setSpan(new MyClickableSpan(TYPE_STOCK,entry.getValue(),entry.getKey()), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		textView.setText(ss);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	public static void setCustomText(TextView textView,String content,Map<String, String> map,int color){
		if (textView == null || content == null)return;
		SpannableString ss = new SpannableString(content);
		if (map != null && map.size() >0) {
			for (Map.Entry<String, String> entry:map.entrySet()) {
				if(entry.getValue() != null){
					int vlen = entry.getValue().length();
					int in = content.indexOf(entry.getValue());
					if (in != -1 && vlen >0) {
						ss.setSpan(new MyClickableSpan(TYPE_STOCK,entry.getValue(),entry.getKey(),color), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					}
				}
			}
		}
		textView.setText(ss);
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}
	
	/**
	 * 包括股票$，话题#
	 * @param content
	 * @param map
	 * @param isUnderline
	 * @param context
	 * @return
	 */
	public static CharSequence getCustomText(String content,List<StockTextUtils.MatchEntity> map,boolean isUnderline,Context context){

		Log.d("getCustomText","describes=="+content+"  map.size=="+map.size());
		if (content == null)return "";
		SpannableStringBuilder ss = new SpannableStringBuilder(content);
		try{
			if (map != null && map.size() >0) {

				//这个是@用到
//				int at_replaceFromStart=0;
				int at_setSpanFromStart=0;//因为可能@同一个人2次，比如@阿青 @阿青 ，这种情况要把第二个阿青成功替换掉，就要有个索引，String.indexOf(subString,fromStart)以下同理

				//这个是stock用到
				int stock_FromStart=0;

				//这个是话题用到
				int topic_FromStart=0;


				//这个是web
				int web_FromStart=0;



				for (StockTextUtils.MatchEntity entry:map) {
						if(entry.getValue() != null){
//							if(entry.getValue().startsWith("#")){
//								int vlen = entry.getValue().length();
//								int in = ss.toString().indexOf(entry.getValue(),topic_FromStart);
//								topic_FromStart=in+vlen;
//								if (in != -1 && vlen >0) {
//									ss.setSpan(new MyClickableSpan(TYPE_TOPIC,entry.getValue(),context.getResources().getColor(R.color.circle_chat_at_color),isUnderline), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//								}
//								ss.replace(in, in+vlen, entry.getValue());
//							}else if(entry.getValue().startsWith("$")) {
//								Log.d("matchStock", "key==" +entry.getKey());
//								Log.d("matchStock", "value==" +entry.getValue());
////								String oldChar ="$"+entry.getValue()+"("+entry.getKey().toUpperCase()+")$";
//								int vlen = entry.getValue().length();
//								int in = ss.toString().indexOf(entry.getValue(),stock_FromStart);
//								stock_FromStart=in+vlen;
//								Log.d("matchStock", "vlen==" +vlen+"  in=="+in+"  stock_FromStart=="+stock_FromStart);
//								String stockKey=entry.getKey();
//								String stockKeys[]=stockKey.split("_");
//								if (in != -1 && vlen >0) {
//									ss.setSpan(new MyClickableSpan(TYPE_STOCK,stockKeys[0],stockKeys[1].toLowerCase(),context.getResources().getColor(R.color.circle_chat_at_color),isUnderline), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//								}
//							}else

							if(entry.getValue().startsWith("@")||entry.getKey().startsWith("@")){

							//ma3.group(0)==@(@潘民升)「{"color":"568981","params":"{\"userId\":14}","type":1}」 ma3.group(1)==@潘民升 ma3.group(2)=={"color":"568981","params":"{\"userId\":14}","type":1}

								int vlen = entry.getKey().length();
								int in = ss.toString().indexOf(entry.getKey(),at_setSpanFromStart);
								String value=entry.getValue();
								String[] values=value.split("_");
								ss.replace(in, in + vlen, values[0]);
								if (in != -1 && vlen >0) {
									ss.setSpan(new MyClickableSpan(TYPE_AT,values[1],context.getResources().getColor(R.color.circle_chat_at_color)), in, in+values[0].length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									at_setSpanFromStart=in+values[0].length();
								}
							}else if(entry.getValue().startsWith("web")){
								int vlen = entry.getKey().length();
								int in = ss.toString().indexOf(entry.getKey(),web_FromStart);
								if (in != -1 && vlen >0) {
									ss.setSpan(new MyClickableSpan(TYPE_WEB,entry.getKey(),context.getResources().getColor(R.color.circle_chat_at_color),true), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
									web_FromStart=in+vlen;
								}
							}
						}
				}
			}
		}catch (Exception e){

		}

		return ss;
	}
	/**
	 * 包括股票$，话题#
	 * @param content
	 * @param map
	 * @param isUnderline
	 * @param context
	 * @return
	 */
	public static CharSequence getCustomText(CharSequence content,List<StockTextUtils.MatchEntity> map,boolean isUnderline,Context context){

		Log.d("getCustomText","describes=="+content+"  map.size=="+map.size());
		if (content == null)return "";
		SpannableStringBuilder ss = new SpannableStringBuilder(content);
		try{
			if (map != null && map.size() >0) {

				//这个是@用到
//				int at_replaceFromStart=0;
				int at_setSpanFromStart=0;//因为可能@同一个人2次，比如@阿青 @阿青 ，这种情况要把第二个阿青成功替换掉，就要有个索引，String.indexOf(subString,fromStart)以下同理

				//这个是stock用到
				int stock_FromStart=0;

				//这个是话题用到
				int topic_FromStart=0;


				//这个是web
				int web_FromStart=0;



				for (StockTextUtils.MatchEntity entry:map) {
					if(entry.getValue() != null){
//							if(entry.getValue().startsWith("#")){
//								int vlen = entry.getValue().length();
//								int in = ss.toString().indexOf(entry.getValue(),topic_FromStart);
//								topic_FromStart=in+vlen;
//								if (in != -1 && vlen >0) {
//									ss.setSpan(new MyClickableSpan(TYPE_TOPIC,entry.getValue(),context.getResources().getColor(R.color.circle_chat_at_color),isUnderline), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//								}
//								ss.replace(in, in+vlen, entry.getValue());
//							}else if(entry.getValue().startsWith("$")) {
//								Log.d("matchStock", "key==" +entry.getKey());
//								Log.d("matchStock", "value==" +entry.getValue());
////								String oldChar ="$"+entry.getValue()+"("+entry.getKey().toUpperCase()+")$";
//								int vlen = entry.getValue().length();
//								int in = ss.toString().indexOf(entry.getValue(),stock_FromStart);
//								stock_FromStart=in+vlen;
//								Log.d("matchStock", "vlen==" +vlen+"  in=="+in+"  stock_FromStart=="+stock_FromStart);
//								String stockKey=entry.getKey();
//								String stockKeys[]=stockKey.split("_");
//								if (in != -1 && vlen >0) {
//									ss.setSpan(new MyClickableSpan(TYPE_STOCK,stockKeys[0],stockKeys[1].toLowerCase(),context.getResources().getColor(R.color.circle_chat_at_color),isUnderline), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//								}
//							}else

						if(entry.getValue().startsWith("@")||entry.getKey().startsWith("@")){

							//ma3.group(0)==@(@潘民升)「{"color":"568981","params":"{\"userId\":14}","type":1}」 ma3.group(1)==@潘民升 ma3.group(2)=={"color":"568981","params":"{\"userId\":14}","type":1}

							int vlen = entry.getKey().length();
							int in = ss.toString().indexOf(entry.getKey(),at_setSpanFromStart);
							String value=entry.getValue();
							String[] values=value.split("_");
							ss.replace(in, in + vlen, values[0]);
							if (in != -1 && vlen >0) {
								ss.setSpan(new MyClickableSpan(TYPE_AT,values[1],context.getResources().getColor(R.color.circle_chat_at_color)), in, in+values[0].length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								at_setSpanFromStart=in+values[0].length();
							}
						}else if(entry.getValue().startsWith("web")){
							int vlen = entry.getKey().length();
							int in = ss.toString().indexOf(entry.getKey(),web_FromStart);
							if (in != -1 && vlen >0) {
								ss.setSpan(new MyClickableSpan(TYPE_WEB,entry.getKey(),context.getResources().getColor(R.color.circle_chat_at_color),true), in, in+vlen,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
								web_FromStart=in+vlen;
							}
						}
					}
				}
			}
		}catch (Exception e){

		}

		return ss;
	}
	
}
