package com.procoin.util;

import android.util.Log;

import com.procoin.common.constant.CommonConst;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author zt
 * 
 * 显示文字的时候自从匹配数字代码，点击跳到行情
 *
 */
public class StockTextUtils {

//	public static CharSequence formatToStockClick(Context context, CharSequence text) {
//		SpannableStringBuilder ssb=new SpannableStringBuilder();
//		KeyboardDB kdb=KeyboardDB.getInstance(context);
//		StringBuilder linkText =null;
//		for (int i = 0; i < text.length(); i++) {
//			String str = String.valueOf(text.charAt(i));
//			if (isNumeric(str)) {
//				if(linkText==null){
//					linkText = new StringBuilder();
//				}
//				linkText.append(str);
//				if(i==text.length()-1){
//					setSpannableString(linkText,ssb,"",kdb);
//				}
//			} else {
//				if (linkText!=null) {
//					setSpannableString(linkText, ssb, str,kdb);
//				} else {
//					ssb.append(str);
//				}
//
//			}
//		}
//		return ssb;
//	}
//
//
//	private static  void setSpannableString(StringBuilder linkText, SpannableStringBuilder tv,String str,KeyboardDB kdb) {
//		SpannableString sss = null;
//		if (linkText.length() == 4 || linkText.length() == 6) {
//			StockInformation stockInfo=kdb.queryStockWithDm(linkText.toString());
//			if(stockInfo!=null){
//				sss = new SpannableString(linkText);
//				sss.setSpan(new MyClickableSpan(stockInfo.getJc(), stockInfo.getFdm()), 0, sss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//				
////				sss.setSpan(new IntentSpan(new OnClickListener() {
////					@Override
////					public void onClick(View v) {
////						Log.d("click", "spanClick=="+System.currentTimeMillis());
////
////					}
////				}), 0, sss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//				tv.append(sss);
//			}else{
//				tv.append(linkText);
//			}
//		}else{
//			tv.append(linkText);
//		}
//		tv.append(str);
//		
//		linkText.delete(0, linkText.length());
//		linkText=null;
//		
//	}
//	public static boolean isNumeric(String str) {
//		Pattern pattern = Pattern.compile("[0-9]*");
//		return pattern.matcher(str).matches();
//	}
	
//	/**
//	 * 解析$$##
//	 * @param describes
//	 * @return
//	 */
//	public static Map<String, String> parserTestToStock(String describes) {
//		Map<String, String> map = new HashMap<String, String>();
//		try {
//			// $中国平安(SH600000)$
//			final String regex2 = "[$]([^$|.]*)\\([(]*([A-Z]*\\d{4,6})[)]*\\)[$]"; // 总
//			final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//			final Matcher ma2 = pa2.matcher(describes);
//			while (ma2.find()) {
//				if (ma2.groupCount() == 2) {
//					Log.d("Matcher","ma2.group(0)=="+ma2.group(0)+" ma3.group(1)=="+ma2.group(1)+" ma3.group(2)=="+ma2.group(2));
//					map.put(ma2.group(2).toLowerCase(), ma2.group(1));
//				}
//			}
//			//#莫予毒##123#ssss##1森罗万象持人伯人56#
//			final String regex1 = "(#[^#]+#)"; //
//			final Pattern pa1 = Pattern.compile(regex1, Pattern.DOTALL); //
//			final Matcher ma1 = pa1.matcher(describes);
//			while (ma1.find()) {
//				if (ma1.groupCount() == 1) {
//					map.put(ma1.group(1), ma1.group(1));
//				}
//			}
//
//			//@阿青
//			final String regex3 = CommonConst.AT_MATCHES;//
//			final Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
//			final Matcher ma3 = pa3.matcher(describes);
//			while (ma3.find()) {
//				if (ma3.groupCount() == 2) {
//					Log.d("Matcher","ma3.group(0)=="+ma3.group(0)+" ma3.group(1)=="+ma3.group(1)+" ma3.group(2)=="+ma3.group(2));
//					map.put(ma3.group(0), ma3.group(1)+"_"+ma3.group(2));
//				}
//			}
//
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		} catch (OutOfMemoryError o) {
//			// TODO: handle exception
//			o.printStackTrace();
//		}
//		return map;
//	}

	/**
	 * 解析$$##
	 *
	 *  以前用上面的方法，用hashMap 封装，因为key不能重复，所以当用到同一个的时候就会出问题，比如我重复@某一人（@阿青 @阿青）就会导致第二个@不能替换，所以用一个类封装起来
	 *
	 * @param content
	 * @return
	 */
	public static List<MatchEntity> parserTestToStock(String content) {
		List map = new ArrayList<MatchEntity>();
		try {
			// $中国平安(SH600000)$
//			final String regex2 = "[$]([^$|.]*)\\([(]*([A-Z]*\\d{4,6})[)]*\\)[$]"; // 总
//			final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//			final Matcher ma2 = pa2.matcher(describes);
//			while (ma2.find()) {
//				if (ma2.groupCount() == 2) {
//					Log.d("Matcher", "ma2.group(0)==" + ma2.group(0) + " ma3.group(1)==" + ma2.group(1) + " ma3.group(2)==" + ma2.group(2));
//					map.add(new MatchEntity(ma2.group(1)+"_"+ma2.group(2),ma2.group(0)));
//				}
//			}
			//#莫予毒##123#ssss##1森罗万象持人伯人56#
//			final String regex1 = "(#[^#]+#)"; //
//			final Pattern pa1 = Pattern.compile(regex1, Pattern.DOTALL); //
//			final Matcher ma1 = pa1.matcher(describes);
//			while (ma1.find()) {
//				if (ma1.groupCount() == 1) {
//					map.add(new MatchEntity(ma1.group(1), ma1.group(1)));
//				}
//			}

			//网址
			final String regex4 = CommonConst.WEB_MATCHES;//
			final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); //
			final Matcher ma4 = pa4.matcher(content);
			while (ma4.find()) {
				Log.d("Matcher", "ma4.group==" +ma4.group() );
					map.add(new MatchEntity(ma4.group(),"web"));
			}

			//@阿青
			final String regex3 = CommonConst.AT_MATCHES;//
			final Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
			final Matcher ma3 = pa3.matcher(content);
			while (ma3.find()) {
				if (ma3.groupCount() == 2) {
					Log.d("Matcher", "ma3.group(0)==" + ma3.group(0) + " ma3.group(1)==" + ma3.group(1) + " ma3.group(2)==" + ma3.group(2));
					map.add(new MatchEntity(ma3.group(0), ma3.group(1) + "_"+ma3.group(2)));
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
		} catch (OutOfMemoryError o) {
			// TODO: handle exception
			o.printStackTrace();
		}
		return map;
	}
	/**
	 * 解析$$##
	 *
	 *  以前用上面的方法，用hashMap 封装，因为key不能重复，所以当用到同一个的时候就会出问题，比如我重复@某一人（@阿青 @阿青）就会导致第二个@不能替换，所以用一个类封装起来
	 *
	 * @param content
	 * @return
	 */
	public static List<MatchEntity> parserTestToStock(CharSequence content) {
		List map = new ArrayList<MatchEntity>();
		try {
			// $中国平安(SH600000)$
//			final String regex2 = "[$]([^$|.]*)\\([(]*([A-Z]*\\d{4,6})[)]*\\)[$]"; // 总
//			final Pattern pa2 = Pattern.compile(regex2, Pattern.DOTALL); // 总
//			final Matcher ma2 = pa2.matcher(describes);
//			while (ma2.find()) {
//				if (ma2.groupCount() == 2) {
//					Log.d("Matcher", "ma2.group(0)==" + ma2.group(0) + " ma3.group(1)==" + ma2.group(1) + " ma3.group(2)==" + ma2.group(2));
//					map.add(new MatchEntity(ma2.group(1)+"_"+ma2.group(2),ma2.group(0)));
//				}
//			}
			//#莫予毒##123#ssss##1森罗万象持人伯人56#
//			final String regex1 = "(#[^#]+#)"; //
//			final Pattern pa1 = Pattern.compile(regex1, Pattern.DOTALL); //
//			final Matcher ma1 = pa1.matcher(describes);
//			while (ma1.find()) {
//				if (ma1.groupCount() == 1) {
//					map.add(new MatchEntity(ma1.group(1), ma1.group(1)));
//				}
//			}

			//网址
			final String regex4 = CommonConst.WEB_MATCHES;//
			final Pattern pa4 = Pattern.compile(regex4, Pattern.DOTALL); //
			final Matcher ma4 = pa4.matcher(content);
			while (ma4.find()) {
				Log.d("Matcher", "ma4.group==" +ma4.group() );
				map.add(new MatchEntity(ma4.group(),"web"));
			}

			//@阿青
			final String regex3 = CommonConst.AT_MATCHES;//
			final Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
			final Matcher ma3 = pa3.matcher(content);
			while (ma3.find()) {
				if (ma3.groupCount() == 2) {
					Log.d("Matcher", "ma3.group(0)==" + ma3.group(0) + " ma3.group(1)==" + ma3.group(1) + " ma3.group(2)==" + ma3.group(2));
					map.add(new MatchEntity(ma3.group(0), ma3.group(1) + "_"+ma3.group(2)));
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
		} catch (OutOfMemoryError o) {
			// TODO: handle exception
			o.printStackTrace();
		}
		return map;
	}

	public static String parserAtName(String content) {
		try {
			//@阿青
			final String regex3 = CommonConst.AT_MATCHES;//
			final Pattern pa3 = Pattern.compile(regex3, Pattern.DOTALL); //
			final Matcher ma3 = pa3.matcher(content);
			while (ma3.find()) {
				if (ma3.groupCount() == 2) {
//					Log.d("Matcher", "ma3.group(0)==" + ma3.group(0) + " ma3.group(1)==" + ma3.group(1) + " ma3.group(2)==" + ma3.group(2));
//					map.put(ma3.group(0), ma3.group(1)+"_"+ma3.group(2));
					return ma3.group(1);
				}
			}


		} catch (Exception e) {
			// TODO: handle exception
		} catch (OutOfMemoryError o) {
			// TODO: handle exception
			o.printStackTrace();
		}
		return "";
	}

	/**
	 *
	 */
	public static class MatchEntity {
		private String key;
		private String value;

		public String getKey() {
			return key;
		}
		public String getValue() {
			return value;
		}

		public MatchEntity(String key,String value){
			this.key=key;
			this.value=value;
		}

	}
}
