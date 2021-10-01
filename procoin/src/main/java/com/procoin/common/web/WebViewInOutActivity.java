//package com.tjr.bee.common.web;
//
//import android.annotation.SuppressLint;
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.support.v7.app.ActionBar.LayoutParams;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.webkit.JavascriptInterface;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageButton;
//import android.widget.ProgressBar;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.ImageLoader;
//import ConfigTjrInfo;
//import BaseRemoteResourceManager;
//import com.cropyme.http.tjrcpt.PervalPayHttp;
//import NotificationsUtil;
//import com.cropyme.social.aliapi.AliPay;
//import BaseAsyncTask;
//import com.tjr.bee.R;
//import TJRBaseActionBarSwipeBackActivity;
//import TJRBaseToolBarSwipeBackActivity;
//import CommonConst;
//import ResultData;
//import ResultDataParser;
//import MyhomeSelectImageActivity;
//import SubmitTabOnClickTask;
//import CommonUtil;
//import DynamicPermission;
//import InflaterUtils;
//import JsonParserUtils;
//import PageJumpUtil;
//import PermissionUtils;
//import WeChatShare;
//
//import org.jboss.netty.handler.codec.http.QueryStringDecoder;
//import org.json.JSONObject;
//
//import java.io.File;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//import java.util.Map;
//
//import static PageJumpUtil.getParameter;
//
///**
// * 新闻或者交易都用这个 不处理Json参数
// * <p>
// * 这里请注意 这个是用来充值的
// *
// * @author zhengmj
// */
//@SuppressLint({"NewApi", "SetJavaScriptEnabled"})
//public class WebViewInOutActivity extends TJRBaseToolBarSwipeBackActivity implements OnClickListener {
//
//
//    private final static String ALIPAY_TYPE = "pay.alipay.app";
//    private final static String WXPAY_TYPE = "pay.weixin.app";
//
//    private WebView mWebView;
//    private String url;
//    //    private ViewGroup view;
//    private ProgressBar pb;
//    private String title;
//    private Uri imageUri;
//    private final int PHOTOHRAPH = 0x111;// 拍照
//    private final String IMAGE_UNSPECIFIED = "image/*";
//    private static final int IMAGE_SELECT = 1;
//    private TextView tvTitle; // 标题
//    private ImageButton ibColse; // 关闭
//    private String description;//描述
//    private WeChatShare weChatShare;
//    //    private String wxUrl;
//    private SubmitTabOnClickTask mSubmitTabOnClickTask;
//    private PayTask mPayTask;
//    private AliPay alipay;
//
//
//    private ValueCallback<Uri> mUploadMessage;
//    private ValueCallback<Uri[]> valueCallback5Plus;
//
//    private Intent mSourceIntent;
//    private static final int REQUEST_CODE_PICK_IMAGE = 0;
//    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
//    private static final int PHOTORESOULT = 2;
//
//    private BaseRemoteResourceManager baseRemoteResourceManager;
//
//    private boolean selFromAlbums=false;//认证只能拍照不能从相册中选取  true代表可以从相册中选取,因为这个页面是充值页面所以默认false，不能从相册中选取
//
//    private String capturePath;//拍照的图片路径
//
//    private DynamicPermission dynamicPermission;
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.hotnews_webview;
//    }
//
//    @Override
//    protected String getActivityTitle() {
//        return "";
//    }
//
//
//    public static void jumpThisPage(Context context, String cashOppUrl){
//        if (!TextUtils.isEmpty(cashOppUrl)) {
//            Bundle bundle = new Bundle();
//            bundle.putString(CommonConst.URLS, cashOppUrl);
//            PageJumpUtil.pageJump(context, WebViewInOutActivity.class, bundle);
//        }else{
//            CommonUtil.showmessage("参数错误",context);
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
////        mActionBar.setTitle(null);
//        baseRemoteResourceManager = getApplicationContext().getRemoteResourceChatManager();
////        addCustomView();
//        if (this.getIntent().getExtras() != null) {
//            Bundle bundle = this.getIntent().getExtras();
//            parserParamsBack(bundle, new ParamsBack() {
//                @Override
//                public void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception {
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "webURL")) {//iphone 消息统一使用 webURL
//                        bundle.putString(CommonConst.URLS, jsonObject.getString("webURL"));
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "webUrl")) {
//                        bundle.putString(CommonConst.URLS, jsonObject.getString("webUrl"));
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, "urls")) {
//                        bundle.putString(CommonConst.URLS, jsonObject.getString("urls"));
//                    }
//                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.TITLE)) {
//                        bundle.putString(CommonConst.TITLE, jsonObject.getString(CommonConst.TITLE));
//                    }
//                }
//            });
//            url = bundle.getString(CommonConst.URLS);
//            title = bundle.getString(CommonConst.TITLE);
//            if (!TextUtils.isEmpty(title)) {
//                tvTitle.setText(title);
//            }
//        }
//        if (TextUtils.isEmpty(url)) {
//            CommonUtil.showmessage("参数错误", WebViewInOutActivity.this);
//            finish();
//            return;
//        }
//        CommonUtil.LogLa(2, "WebViewInOutActivity 11--->url = " + url);
//        if (url.contains("user_id=%s"))
//            url = url.replace("user_id=%s", "user_id=" + getApplicationContext().getUser().getUserId());
//        if (url.contains("token=%s"))
//            url = url.replace("token=%s", "token=" + ConfigTjrInfo.getInstance().getSessionid());
//        //isHit = true   =false
//        CommonUtil.LogLa(2, "WebViewInOutActivity 22--->url = " + url);
//        if (url.contains("selFromAlbums=true")) {
//            selFromAlbums = true;
//        }
////        setContentView(showView());
//        showView();
//        alipay = new AliPay(this);
//        weChatShare = new WeChatShare(this);
//    }
//
//
//    //当用户更改了系统字体，webView不会生效
//    @Override
//    public Resources getResources() {
//        Resources res = super.getResources();
//        Configuration config=new Configuration();
//        config.setToDefaults();
//        res.updateConfiguration(config,res.getDisplayMetrics() );
//        return res;
//    }
//
//
////    /**
////     *
////     */
////    private void addCustomView() {
////        View view = InflaterUtils.inflateView(this, R.layout.web_custom_title);
////        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
////        ibColse = (ImageButton) view.findViewById(R.id.ibColse);
////        ibColse.setOnClickListener(new View.OnClickListener() {
////
////            @Override
////            public void onClick(View v) {
//////                onBackPressed();
////                finish();
////            }
////
////        });
////        LayoutParams mCustomViewLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
////        mCustomViewLayoutParams.gravity = Gravity.LEFT;
////        mActionBar.setCustomView(view, mCustomViewLayoutParams);
////        Toolbar parent = (Toolbar) view.getParent();
////        parent.setContentInsetStartWithNavigation(5);
////
////    }
//
//    // @Override
//    // public boolean onOptionsItemSelected(MenuItem item) {
//    // switch (item.getItemId()) {
//    // case android.R.id.home:
//    // finish();
//    // return true;
//    //
//    // default:
//    // break;
//    // }
//    // return super.onOptionsItemSelected(item);
//    // }
//
//    // private final static int FILECHOOSER_RESULTCODE = 1;
//    @SuppressLint("SetJavaScriptEnabled")
//    @SuppressWarnings("deprecation")
//    private void showView() {
////        view = (ViewGroup) InflaterUtils.inflateView(WebViewInOutActivity.this, R.layout.hotnews_webview);
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        ibColse = (ImageButton) findViewById(R.id.ibColse);
//        ibColse.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
////                onBackPressed();
//                finish();
//            }
//
//        });
//        pb = (ProgressBar) findViewById(R.id.pb);
//        mWebView = (WebView) findViewById(R.id.wv);
//        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setSaveFormData(false);
//        mWebView.getSettings().setSavePassword(false);
//        // mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setUseWideViewPort(false);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setBuiltInZoomControls(false);
//        mWebView.getSettings().setSupportZoom(false);
////        mWebView.getSettings().setAllowFileAccess(true);
//
//        mWebView.addJavascriptInterface(new WebViewContent(), "handler");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
//
//        mWebView.setWebChromeClient(new WebChromeClient() {
//
//            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//
//                mUploadMessage = uploadMsg;
//                showAlertDialog();
//
//            }
//
//            // For Android 3.0+
//            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
//                mUploadMessage = uploadMsg;
//                showAlertDialog();
//            }
//
//            // For Android 4.1
//            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                mUploadMessage = uploadMsg;
//                showAlertDialog();
//
//            }
//
//            // For Android 5.0++
//            @Override
//            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//                Log.d("setWebChromeClient","onShowFileChooser......555");
//                valueCallback5Plus = filePathCallback;
//                showAlertDialog();
//                return true;
////                return super.onShowFileChooser(webView, filePathCallback,
////                        fileChooserParams);
//            }
//
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                if (tvTitle != null && !TextUtils.isEmpty(title)) tvTitle.setText(title);// 这个主要是
//            }
//
//
//            @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                Log.d("onProgressChanged", "newProgress==" + newProgress);
//                if (newProgress == 100) {
//                    pb.setVisibility(View.GONE);
//                } else {
//                    if (pb.getVisibility() == View.GONE) pb.setVisibility(View.VISIBLE);
//                    pb.setProgress(newProgress);
//                }
//                super.onProgressChanged(view, newProgress);
//            }
//
//        });
//
//        mWebView.setWebViewClient(new WebViewClient() {
//
////            @Override
////            public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
////                Log.d("shouldLoading", "onReceivedHttpAuthRequest request===" + realm);
////                super.onReceivedHttpAuthRequest(view, handler, host, realm);
////            }
////
////            @Override
////            public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
////                Log.d("shouldLoading", "shouldInterceptRequest request===" + request);
////
////                return super.shouldInterceptRequest(view, request);
////
////            }
//
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
////                CommonUtil.LogLa(2, url);
//                Log.d("shouldLoading", "onPageFinished url===" + url);
////                wxUrl = url;
//                if (tvTitle != null) tvTitle.setText(view.getTitle());// 这个主要是返回的时候改变title。2个地方都设置
//                if (mWebView != null && mWebView.canGoBack()) {
//                    if (ibColse != null) ibColse.setVisibility(View.VISIBLE);
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_common_back_white);
//                } else {
//                    if (ibColse != null) ibColse.setVisibility(View.GONE);
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_close_white);
//                }
//
//                view.loadUrl("javascript:window.handler.getContent(''+document.querySelector('meta[name=\"Description\"]').getAttribute('content')+'');");
//                view.loadUrl("javascript:window.handler.getContent(''+document.querySelector('meta[name=\"description\"]').getAttribute('content')+'');");
//            }
//
//            @Override
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                CommonUtil.LogLa(2, "shouldOverrideUrlLoading url===" + url);
//                if (url.startsWith("tel:")) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                    startActivity(intent);
//                    return true;
//                }
//                if (url.endsWith(".apk")) {
//                    Uri uri = Uri.parse(url);
//                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
//                    WebViewInOutActivity.this.startActivity(viewIntent);
//                    return true;
//                }
////                if (url.startsWith(TjrBaseApi.mApiPayUri.uri() + "/?money=")) {
//                Log.d("shouldLoading", "shouldOverrideUrlLoading startsWith " + url);
//                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(url);//
//                Map<String, List<String>> parameters = queryStringDecoder.getParameters();
//                String money = getParameter("money", parameters);
//                String pay_type = getParameter("pay_type", parameters);
//                if (ALIPAY_TYPE.equals(pay_type) || WXPAY_TYPE.equals(pay_type)) {
//                    startTask(money, pay_type);
//                    return true;
//                }
////                }
////                if (url.endsWith(".pdf")) {
//////                    mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + url);
////                    Bundle bundle = new Bundle();
////                    bundle.putString(CommonConst.URLS, url);
////                    PageJumpUtil.pageJump(CommonWebViewActivity.this, PDFWebViewActivity.class, bundle);
////                    return true;
////                }
//
//
//                if (PageJumpUtil.pageJumpUrl(WebViewInOutActivity.this, url)) return true;
//
//                if (!url.startsWith("http://") && !url.startsWith("https://")) {
//                    return true;
//                }
//                return super.shouldOverrideUrlLoading(view, url);
//            }
//
////            @Override
////            public void onPageStarted(WebView view, String url, Bitmap favicon) {
////                Log.d("shouldLoading", "onPageStarted url===" + url);
////                super.onPageStarted(view, url, favicon);
////            }
//
////            @Override
////            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
////                Log.d("shouldLoading", "onReceivedError failingUrl===" + failingUrl);
////                super.onReceivedError(view, errorCode, description, failingUrl);
////            }
////
////            @Override
////            public void onLoadResource(WebView view, String url) {
////                Log.d("shouldLoading", "onLoadResource failingUrl===" + url);
////                super.onLoadResource(view, url);
////            }
//        });
////                            mWebView.setDownloadListener(new DownloadListener() {
////                                @Override
////                                public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
////                                    Log.d("onDownloadStart", "onDownloadStart=========>开始下载 url =" + url);
////                                    System.out.println("=========>开始下载 url =" + url);
////                                    Uri uri = Uri.parse(url);
////                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////                                    startActivity(intent);
////                                }
////                            });
//
//
//        mWebView.loadUrl(url);
////        return view;
//    }
//
////    class Listener implements  DownloadListener{
////        @Override
////        public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
////            Log.d("onDownloadStart", "onDownloadStart=========>开始下载 url =" + url);
////            System.out.println("=========>开始下载 url =" + url);
////            Uri uri = Uri.parse(url);
////            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
////            startActivity(intent);
////        }
////
////    }
//
//
//    protected void showAlertDialog() {
//        String[] items = null;
//        if (selFromAlbums) {
//            items = new String[]{"拍照", "从相册中选取"};
//        } else {
//            items = new String[]{"拍照"};
//
//        }
//        new AlertDialog.Builder(this).setTitle("图片选择").setItems(items, this).setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                // Log.d("showAlertDialog", "onCancel///////////");
//                // 取消对话框一定设置为null，否则点击第二次会没有反应
//                releaseUploadMessage();
//            }
//        }).create().show();
//    }
//
//    private void releaseUploadMessage() {
//        if (mUploadMessage != null) {//如果有异常，就取消否则第二次点击会没有反映
//            mUploadMessage.onReceiveValue(null);
//            mUploadMessage = null;
//        }
//        if (valueCallback5Plus != null) {
//            valueCallback5Plus.onReceiveValue(null);//
//            valueCallback5Plus = null;
//        }
//    }
//
//    private void initDynamicPermission() {
//        if (dynamicPermission == null) {
//            dynamicPermission = new DynamicPermission(this, new DynamicPermission.RequestPermissionsCallBack() {
//                @Override
//                public void onRequestSuccess(String[] permissions, int requestCode) {
//                    if (requestCode == 100) {
//                        capturePath = ImageUtil.getNewPhotoPath();
//                        Intent intent = ImageUtil.takeBigPicture(WebViewInOutActivity.this, capturePath);
//                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
//                    }
//
//                }
//
//                @Override
//                public void onRequestFail(String[] permissions, int requestCode) {
//                    releaseUploadMessage();
//                }
//            });
//        }
//    }
//
//    @Override
//    public void onClick(DialogInterface dialog, int which) {
//        switch (which) {
////            case 0:
////                try {
////                    imageUri = getOutputMediaFile();
////                } catch (Exception e) {
////                    e.printStackTrace();
////                }
////                if (imageUri == null) return;
////                try {
////                    Intent caintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
////                    caintent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
////                    PageJumpUtil.pageJumpResult(WebViewInOutActivity.this, caintent, PHOTOHRAPH);
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    CommonUtil.showmessage("打开相机失败", WebViewInOutActivity.this);
////                }
////                break;
////            case 1:
////                try {
////                    Intent intent = new Intent(Intent.ACTION_PICK, null);
////                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
////                    PageJumpUtil.pageJumpResult(WebViewInOutActivity.this, intent, IMAGE_SELECT);
////                } catch (Exception e) {
////                    CommonUtil.showmessage("该系统获取相册失败", WebViewInOutActivity.this);
////                }
////                break;
//
//            case 0:
//                initDynamicPermission();
//                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 100);
////                mSourceIntent = ImageUtil.takeBigPicture(WebViewInOutActivity.this,"");
////                startActivityForResult(mSourceIntent, REQUEST_CODE_IMAGE_CAPTURE);
//                break;
//            case 1:
//                Intent intent = new Intent();
//                intent.putExtra(CommonConst.KEY_EXTRAS_TYPE, "sendPic");
//                intent.putExtra(CommonConst.BUTTONTEXT, "确定");
//                intent.putExtra(CommonConst.SHOWCAREMA, false);
//                PageJumpUtil.pageJumpResult(WebViewInOutActivity.this, MyhomeSelectImageActivity.class, intent, PHOTORESOULT);
//
////                initDynamicPermission();
////                dynamicPermission.checkSelfPermission(PermissionUtils.EXTERNAL_STORAGE, 101);
//
////                mSourceIntent = ImageUtil.choosePicture();
////                startActivityForResult(mSourceIntent, REQUEST_CODE_PICK_IMAGE);
//                break;
//
//            default:
//                break;
//        }
//
//    }
//
//    @SuppressLint("SimpleDateFormat")
//    @SuppressWarnings("deprecation")
//    private Uri getOutputMediaFile() throws Exception {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        File mediaFile = ImageLoader.getInstance().getDiskCache().get("IMG_TEMP.jpg");
//        Uri mediaUri = Uri.fromFile(mediaFile);
//        return mediaUri;
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        Log.d("onActivityResult","requestCode=="+requestCode+" resultCode=="+resultCode);
//        if(resultCode==0){//取消之后resultCode==0,这里在清除
//            releaseUploadMessage();
//            return;
//        }
//        switch (requestCode) {
//            case REQUEST_CODE_IMAGE_CAPTURE:
//                try {
//                    if (mUploadMessage == null && valueCallback5Plus == null) {
//                        return;
//                    }
//                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(capturePath, true);
//                    File f = saveBitmapToFile(bitmapOfFile);
//                    Uri uri = Uri.fromFile(f);
////                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmapOfFile, null, null));//
//                    if (mUploadMessage != null) {
//                        mUploadMessage.onReceiveValue(uri);
//                        Log.d("sourcePath", "onReceiveValue uri==" + uri);
//                    } else {
//                        valueCallback5Plus.onReceiveValue(new Uri[]{uri});
//                        valueCallback5Plus = null;
//                    }
//                } catch (Exception e) {
//                    releaseUploadMessage();
//                }
//                break;
//            case PHOTORESOULT:
//                if (resultCode == 0x147) {
//                    if (data != null && data.getExtras() != null) {
//                        String uri = data.getExtras().getString("uri");
//                        String path = Uri.parse(uri).getPath();
//                        try {
//                            if (!TextUtils.isEmpty(uri)) {
//                                Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(path, true);
//                                File f = saveBitmapToFile(bitmapOfFile);
//                                Log.d("absolutePath", "absolutePath==" + f.getAbsolutePath());
//                                Uri uri2 = Uri.fromFile(f);
////                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmapOfFile, null, null));
//                                if (mUploadMessage != null) {
//                                    mUploadMessage.onReceiveValue(uri2);
////                        mUploadMessage = null;
//                                    Log.d("sourcePath", "onReceiveValue uri==" + uri2);
//                                } else {
//                                    valueCallback5Plus.onReceiveValue(new Uri[]{uri2});
//                                    valueCallback5Plus = null;
//                                }
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            releaseUploadMessage();
//                        }
//                    }
//                }
//                break;
////            case REQUEST_CODE_PICK_IMAGE:
////                try {
////                    if (mUploadMessage == null && valueCallback5Plus == null) {
////                        return;
////                    }
////                    String sourcePath = ImageUtil.retrievePath(this, mSourceIntent, intent);
////
////
////                    Bitmap bitmapOfFile = CommonUtil.getSmallBitmap(sourcePath, true);
////                    File f = saveBitmapToFile(bitmapOfFile);
//////                    Uri uri= Uri.parse(f.getAbsolutePath());
////                    Uri uri= Uri.fromFile(f);
////                    Log.d("sourcePath","uri=="+uri.toString()+"  length=="+f.length()+" f.path=="+f.getAbsolutePath());
////
////                    if (mUploadMessage != null) {
////                        mUploadMessage.onReceiveValue(uri);
//////                        mUploadMessage = null;
////
////                        Log.d("sourcePath", "onReceiveValue uri=="+uri);
////                    } else {
////                        valueCallback5Plus.onReceiveValue(new Uri[]{uri});
////                        valueCallback5Plus = null;
////                    }
////                } catch (Exception e) {
////                    e.printStackTrace();
////                    Log.d("sourcePath", "onReceiveValue e=="+e.toString());
////                    releaseUploadMessage();//如果有异常，就取消否则第二次点击会没有反映
////                }
////                break;
//        }
//    }
//
//    private File saveBitmapToFile(Bitmap bitmap) throws Exception {
//        if (bitmap == null) return null;
//        String fileName2 = System.currentTimeMillis() + "" + getUserId() + ".jpg";
//        File file2 = baseRemoteResourceManager.getFile(fileName2);
//        baseRemoteResourceManager.writeFile(file2, bitmap, true);
//        if (file2 != null && file2.exists()) {
//            return file2;
//        }
//        return null;
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (mWebView != null) {
//            ((ViewGroup)mWebView.getParent()).removeView(mWebView);
//            mWebView.clearCache(true);
//            mWebView.clearHistory();
//            mWebView.clearFormData();
//            mWebView.removeAllViews();
//            mWebView.destroy();
//            mWebView = null;// 清空掉，不然会报nullpoint
//        }
//        super.onDestroy();
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        if (!isHideMenu) getMenuInflater().inflate(R.menu.webview_share, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        if (TextUtils.isEmpty(description)) description = "来源：淘金路网页分享";
////        String title = tvTitle.getText().toString();
////        if (!TextUtils.isEmpty(title)) {
////            TjrSocialMTAUtil.trackCustomKVEvent(CommonWebViewActivity.this, TjrSocialMTAUtil.PROP_CLICKTYPE, title, TjrSocialMTAUtil.MTAWEIXINWEBSHARE);
////        }
////        if (TextUtils.isEmpty(url)) {
////            CommonUtil.showmessage("没有获取到网页地址，请重新进入页面", CommonWebViewActivity.this);
////            return super.onOptionsItemSelected(item);
////        }
////        if (TextUtils.isEmpty(mWebView.getUrl())) {
////            return super.onOptionsItemSelected(item);
////        }
////        switch (item.getItemId()) {
////            case R.id.action_share:
////                CommonUtil.gotoShareActivity(this, ShareTypeEnum.WEBSHARE.type(), 0x456);
////                break;
////            default:
////                break;
////        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    public void onBackPressed() {
//        if (mWebView != null && mWebView.canGoBack()) {
//            mWebView.goBack();
//            return;
//        }
//        startSubmitTabOnClickTask(url, 4);//关闭的广告
//        super.onBackPressed();
//    }
//
//    public class WebViewContent {
//        @JavascriptInterface
//        public void getContent(String shareData) {
//            if (null != shareData && description == null) {
//                description = shareData;
//                Log.d("shouldLoading", "url description is " + description);
//            }
//        }
//    }
//
//    public void startSubmitTabOnClickTask(String modelId, int type) {
//        long userId = 0;
//        if (getApplicationContext().getUser() != null) {//这里不太确定能有userId
//            userId = getApplicationContext().getUser().getUserId();
//        }
//        if (userId > 0) {
//            CommonUtil.cancelAsyncTask(mSubmitTabOnClickTask);
//            mSubmitTabOnClickTask = (SubmitTabOnClickTask) new SubmitTabOnClickTask(userId, modelId, type).executeParams();
//        }
//    }
//
//    public void startTask(String money, String pay_type) {
//        CommonUtil.cancelAsyncTask(mPayTask);
//        mPayTask = (PayTask) new PayTask(money, pay_type).executeParams();
//    }
//
//    private class PayTask extends BaseAsyncTask<Void, Void, Boolean> {
//        //        private String payUrl;
//        private ResultData resultData;
//        private Exception e;
//        private String pay_type;
//        private String money;
//        //阿里支付
//        private String aliOrder;
//        // 微信支付需要支付的字段
//        private String noncestr;
//        private String packageValue;
//        private String partnerid;
//        private String prepayid;
//        private String sign;
//        private String timestamp;
//
//        public PayTask(String money, String pay_type) {
//            this.money = money;
//            this.pay_type = pay_type;
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            try {
////                QueryStringDecoder queryStringDecoder = new QueryStringDecoder(payUrl);//
////                Map<String, List<String>> parameters = queryStringDecoder.getParameters();
////                money = getParameter("money", parameters);
////                pay_type = getParameter("pay_type", parameters);
//                String result = PervalPayHttp.getInstance().payCashOppRechargeUrl(String.valueOf(getApplicationContext().getUser().getUserId()), pay_type, money);
//                if (!TextUtils.isEmpty(result)) {
//                    resultData = new ResultDataParser().parse(new JSONObject(result));
//                    if (resultData.isSuccess()) {
//                        JSONObject jsonResult = resultData.returnJSONObject();
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "order")) {
//                            aliOrder = jsonResult.getString("order");
//                        }
//                        //微信
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "noncestr")) {
//                            noncestr = jsonResult.getString("noncestr");
//                        }
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "package")) {
//                            packageValue = jsonResult.getString("package");
//                        }
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "partnerid")) {
//                            partnerid = jsonResult.getString("partnerid");
//                        }
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "prepayid")) {
//                            prepayid = jsonResult.getString("prepayid");
//                        }
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "sign")) {
//                            sign = jsonResult.getString("sign");
//                        }
//                        if (JsonParserUtils.hasAndNotNull(jsonResult, "timestamp")) {
//                            timestamp = jsonResult.getString("timestamp");
//                        }
//                    }
//                    return resultData.isSuccess();
//
//                }
//            } catch (Exception e) {
//                this.e = e;
//                e.printStackTrace();
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            dismissProgressDialog();
//            if (resultData != null) {
//                if (aBoolean) {
//                    if (ALIPAY_TYPE.equals(pay_type)) {
//                        Log.d("shouldLoading", "payTask alipay " + pay_type);
//                        if (!TextUtils.isEmpty(aliOrder)) if (alipay != null) {
//                            alipay.pay(aliOrder);
//                            Log.d("shouldLoading", "payTask alipay ");
//                        }
//                    } else if (WXPAY_TYPE.equals(pay_type)) {
//                        if (noncestr != null && packageValue != null && partnerid != null && prepayid != null && sign != null && timestamp != null)
//                            if (weChatShare != null) {
//                                weChatShare.sendAuthPay(prepayid, packageValue, partnerid, noncestr, timestamp, sign);
//                                Log.d("shouldLoading", "payTask alipay ");
//                            }
//                    }
//                } else {
//                    if (!TextUtils.isEmpty(resultData.msg)) {
//                        CommonUtil.showmessage(resultData.msg, getApplicationContext());
//                    }
//                }
//            }
//            if (e != null) {
//                NotificationsUtil.ToastReasonForFailure(getApplicationContext(), e);
//            }
//
//        }
//    }
//
//
//}
