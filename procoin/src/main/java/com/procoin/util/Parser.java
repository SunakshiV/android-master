package com.procoin.util;

import android.util.Log;
import android.webkit.WebView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author pengtao.du@downjoy.com
 *
 */
public class Parser {

	private WebView webView;
	public static String Js2JavaInterfaceName = "JsUseJava";
	public ArrayList<String> imgUrls = new ArrayList<String>();
	private ArrayList<String> imgUrlsSd = new ArrayList<String>();//Sd卡的路径
	private String html;

	public Parser(WebView webView, String html, int type) {
		this.webView = webView;
		this.html = html;
	}

	public ArrayList<String> getImgUrls() {
		return imgUrls;
	}
	public ArrayList<String> getImgUrlsSd() {
		return imgUrlsSd;
	}

	public void loadData() {
			Document doc = String2Document(html);
//			 Document doc = Jsoup.parse(html);
			imgUrls.clear();
			imgUrlsSd.clear();
			Elements es = doc.getElementsByTag("img");
			for (Element e : es) {
				String imgUrl = e.attr("src");
				imgUrls.add(imgUrl);
				String imgName;
				File file = new File(imgUrl);
				imgName = file.getName();
				if (imgName.endsWith(".gif")) {
					File dir = ImageLoader.getInstance().getDiskCache().get(imgUrl);
					imgUrlsSd.add(dir.getPath());
					e.attr("src", "file:///android_asset/ic_web_image_logo2.png");
					e.attr("src_link", "file://" + dir.getPath());
					e.attr("ori_link", imgUrl);
					e.attr("width","100%");
					String str = "window." + Js2JavaInterfaceName + ".setImgSrc('" + dir.getPath() + "')";
					e.attr("onclick", str);
//					e.remove();
				} else {
					// TODO 图片缓存
					// File dir = new File(Environment.getExternalStorageDirectory()
					// + "/test/"+ URLEncoder.encode(imgUrl));
					// Log.i("file", "exists is "+dir.exists());
					// String filePath = "file:///mnt/sdcard/test/" +
					// URLEncoder.encode(imgUrl);
					File dir = ImageLoader.getInstance().getDiskCache().get(imgUrl);
					imgUrlsSd.add(dir.getPath());
					Log.i("file", "exists is " + dir.exists());
					e.attr("src", "file:///android_asset/ic_web_image_logo2.png");
					e.attr("src_link", "file://" + dir.getPath());
					e.attr("ori_link", imgUrl);
					 //mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);4.4.2这个属性已经无效
					e.attr("width","100%");
					String str = "window." + Js2JavaInterfaceName + ".setImgSrc('" + dir.getPath() + "')";
					e.attr("onclick", str);
				}
			}
			// Log.d("html", doc.html());
			String data = doc.html();
//			Log.d("time", "loadDataWithBaseURL start==" + System.currentTimeMillis());
			webView.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);
//			webView.loadUrl(" file:///android_asset/index.html ");
//			Log.d("time", "loadDataWithBaseURL end==" + System.currentTimeMillis());

	}


	public static Document String2Document(String html){
		return Jsoup.parse("<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no\" /></head><body>" + html + "</body></html>");
	}
}
