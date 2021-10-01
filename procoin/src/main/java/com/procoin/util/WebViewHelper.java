package com.procoin.util;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.photo.ViewPagerPhotoViewActivity;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.task.BaseAsyncTask;
import com.procoin.widgets.ViewPagerPhotoViewUI;

import org.jboss.netty.handler.codec.http.QueryStringDecoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author pengtao.du@downjoy.com
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class WebViewHelper {

    private WebView mWebView;
    private Context mContext;
    // private String TAG = "WebViewHelper";
    private WebProgress wp;
    private ArrayList<String> urlStrs;
    private ArrayList<String> urlStrsSd;
    private DownloadWebImgTask downloadTask;
    private DisplayImageOptions options;

    public WebViewHelper(Context context) {
        mContext = context;
    }

    public void execute(WebView webView, final Parser parser, final WebProgress wp) {
        mWebView = webView;
        this.wp = wp;
        setupWebView();
        parser.loadData();
        options = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisk(true).considerExifParams(false).build();
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                if (((Activity) mContext).isFinishing()) return;
                downloadTask = new DownloadWebImgTask();
                urlStrs = parser.getImgUrls();
                urlStrsSd = parser.getImgUrlsSd();
                String urlStrArray[] = new String[urlStrs.size() + 1];
                urlStrs.toArray(urlStrArray);
                downloadTask.executeParams(urlStrArray);
                if (wp != null) {
                    wp.onPageFinished();
                    mWebView.getSettings().setBlockNetworkImage(false);// 少了这句，android4.4.2显示不了图片
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (PageJumpUtil.pageJumpUrl(mContext, url)) {
                    // 这里是头条跳转组件的统计 按照iphone的来计算
                    String pview = null;
                    QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);//
                    Map<String, List<String>> parameters = queryStringDecoder.getParameters();
                    pview = PageJumpUtil.getParameter("pview", parameters);
                    if (TextUtils.isEmpty(pview)) {
                        pview = " ";
                    }
//                    TjrSocialMTAUtil.trackCustomKVEvent(mContext, pview, TjrSocialMTAUtil.MTAHOTSPOTTOCLICK);
                    return true;
                }
                Bundle bundle = new Bundle();
                bundle.putString(CommonConst.URLS, url);
                PageJumpUtil.pageJump(mContext, CommonWebViewActivity.class, bundle);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (wp != null) {
                    wp.onPageStarted();
                }
            }
        });
    }

    public void cancelAsyncTask() {
        CommonUtil.cancelAsyncTask(downloadTask);
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        if (mWebView != null) {
            mWebView.addJavascriptInterface(new Js2JavaInterface(), Parser.Js2JavaInterfaceName);
            mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
            mWebView.getSettings().setJavaScriptEnabled(true);
            mWebView.getSettings().setBlockNetworkImage(true);
            mWebView.getSettings().setUseWideViewPort(true);
            mWebView.getSettings().setLoadWithOverviewMode(true);
            mWebView.getSettings().setBuiltInZoomControls(false);
            mWebView.getSettings().setSupportZoom(false);
            // mWebView.getSettings().setRenderPriority(RenderPriority.HIGH);
            mWebView.setBackgroundColor(ContextCompat.getColor(mContext,R.color.white));
            // ViewCompat.setLayerType(mWebView, View.LAYER_TYPE_HARDWARE);
        }
    }

    public WebProgress getWp() {
        return wp;
    }

    public void setWp(WebProgress wp) {
        this.wp = wp;
    }

    public class Js2JavaInterface {
        @JavascriptInterface
        public void setImgSrc(String imgSrc) {// 上面的@JavascriptInterface
            // 去掉之后有些机子（4.2或者以上）不会回调此方法
            // TODO: handle exception
//            Log.d("154","setImgSrc");
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConst.PAGETYPE, 1);
            bundle.putInt(CommonConst.DEFAULTPOS, getIndexByImgSrc(imgSrc));
            bundle.putStringArrayList(ViewPagerPhotoViewUI.IMAGEURLS, urlStrs);
            bundle.putString(ViewPagerPhotoViewUI.IMGSRC, imgSrc);
//			CommonUtil.pageJumpToData((Activity) mContext, ViewPagerPhotoViewActivity.class, false, false, bundle);
            PageJumpUtil.pageJumpToData((AppCompatActivity) mContext, ViewPagerPhotoViewActivity.class, bundle);
            // Log.e(TAG, "setImgSrc : " + imgSrc);
            // Dialog dialog = new Dialog(mContext, R.style.Dialog_Fullscreen);
            // TouchImageView touch = new TouchImageView(mContext);
            // try {
            // touch.setImageBitmap(BitmapFactory
            // .decodeStream(new FileInputStream(imgSrc)));
            // } catch (FileNotFoundException e) {
            // e.printStackTrace();
            // }
            // touch.setMaxZoom(4f);
            // dialog.setContentView(touch);
            // dialog.show();
        }
    }

    public class DownloadWebImgTask extends BaseAsyncTask<String, String, Void> {

        public static final String TAG = "DownloadWebImgTask";

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            if (values == null || values.length == 0) return;
            if (mWebView != null)
                mWebView.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); " + "for(var i=0;i<objs.length;i++)  " + "{" + "    var imgSrc = objs[i].getAttribute(\"src_link\"); " + "    var imgOriSrc = objs[i].getAttribute(\"ori_link\"); " + " if(imgOriSrc == \"" + values[0] + "\"){ " + "    objs[i].setAttribute(\"src\",imgSrc);}" + "}" + "})()");
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mWebView != null)
                mWebView.loadUrl("javascript:(function(){" + "var objs = document.getElementsByTagName(\"img\"); " + "for(var i=0;i<objs.length;i++)  " + "{" + "    var imgSrc = objs[i].getAttribute(\"src_link\"); " + "    objs[i].setAttribute(\"src\",imgSrc);" + "}" + "})()");
            super.onPostExecute(result);
        }

        @Override
        protected Void doInBackground(String... params) {
            if (params == null || params.length == 0) return null;
            try {
                final CountDownLatch latch = new CountDownLatch(params.length);
                for (String urlStr : params) {
                    if (urlStr == null || "".equals(urlStr)) {
                        latch.countDown();
                    } else {
                        ImageLoader.getInstance().loadImage(urlStr, options, new ImageLoadingListener() {
                            volatile boolean isDown = false;

                            @Override
                            public void onLoadingStarted(String imageUri, View view) {
                                // TODO Auto-generated method stub
                            }

                            @Override
                            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                                // TODO Auto-generated method stub
                                if (!isDown) {
                                    isDown = true;
                                    latch.countDown();
                                }
                            }

                            @Override
                            public void onLoadingComplete(String imageUri, View view, Bitmap bitmap) {
                                // TODO Auto-generated method stub
                                if (!isDown) {
                                    isDown = true;
                                    if (imageUri != null) publishProgress(imageUri);
                                    latch.countDown();
                                }
                            }

                            @Override
                            public void onLoadingCancelled(String imageUri, View view) {
                                // TODO Auto-generated method stub
                                if (!isDown) {
                                    isDown = true;
                                    latch.countDown();
                                }
                            }
                        });
                    }
                }
                latch.await();// 等待所有工人完成工作
            } catch (InterruptedException e) {
                // TODO: handle exception
            }
            return null;

            // URL url = null;
            // InputStream inputStream = null;
            // OutputStream outputStream = null;
            // HttpURLConnection urlCon = null;
            // if (params.length == 0) return null;
            // for (String urlStr : params) {
            // if (urlStr == null) {
            // break;
            // }
            // ImageLoader.getInstance().loadImageSync(urlStr);
            //
            // File file = ImageLoader.getInstance().getDiskCache().get(urlStr);
            // if (file.exists()) {
            // publishProgress(file.getPath());
            // }
            //
            // // try {
            // // // File tempFile = new File(urlStr);
            // // // int index = urlStr.lastIndexOf("/");
            // // // String fileName = urlStr.substring(index + 1,
            // // // urlStr.length());
            // // // Log.i(TAG, "file name : " + fileName
            // // // + " , tempFile name : " + tempFile.getName());
            // // // Log.i(TAG, " url : " + urlStr);
            // // File file =
            // // ImageLoader.getInstance().getDiskCache().get(urlStr);
            // // Log.i("file", "" + file.exists() + " " + file.length());
            // // if (file.exists()) {
            // // continue;
            // // }
            // // try {
            // // file.createNewFile();
            // // } catch (IOException e) {
            // // e.printStackTrace();
            // // }
            // //
            // // url = new URL(urlStr);
            // // urlCon = (HttpURLConnection) url.openConnection();
            // // urlCon.setRequestMethod("GET");
            // // urlCon.setDoInput(true);
            // // urlCon.connect();
            // //
            // // inputStream = urlCon.getInputStream();
            // // outputStream = new FileOutputStream(file);
            // // byte buffer[] = new byte[1024];
            // // int bufferLength = 0;
            // // while ((bufferLength = inputStream.read(buffer)) > 0) {
            // // outputStream.write(buffer, 0, bufferLength);
            // // }
            // // outputStream.flush();
            // // publishProgress(urlStr);
            // // } catch (MalformedURLException e) {
            // // e.printStackTrace();
            // // } catch (IOException e) {
            // // e.printStackTrace();
            // // } finally {
            // //
            // // try {
            // // if (inputStream != null) {
            // // inputStream.close();
            // // }
            // // } catch (IOException e1) {
            // // e1.printStackTrace();
            // // }
            // // try {
            // // if (outputStream != null) {
            // // outputStream.close();
            // // }
            // // } catch (IOException e) {
            // // e.printStackTrace();
            // // }
            // // }
            // }
            // return null;
        }
    }

    public interface WebProgress {
        void onPageStarted();

        void onPageFinished();
    }

    public int getIndexByImgSrc(String imgSrc) {
        int index = -1;
        for (int i = 0; i < urlStrs.size(); i++) {
            if (imgSrc.equals(urlStrs.get(i))) {
                index = i;
                break;
            }
        }
        if (index == -1) {
            for (int i = 0; i < urlStrsSd.size(); i++) {
                if (imgSrc.equals(urlStrsSd.get(i))) {
                    index = i;
                    break;
                }
            }
        }
        return Math.max(index, 0);
    }
}
