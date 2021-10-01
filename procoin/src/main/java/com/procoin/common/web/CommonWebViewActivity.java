package com.procoin.common.web;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.http.resource.BaseRemoteResourceManager;
import com.procoin.module.myhome.MyhomeMultiSelectImageActivity;
import com.procoin.social.TjrSocialMTAUtil;
import com.procoin.util.CommonUtil;
import com.procoin.util.DynamicPermission;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.PermissionUtils;
import com.procoin.util.VeDate;
import com.tencent.smtt.sdk.ValueCallback;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;
import com.tencent.smtt.sdk.WebViewClient;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;
//import com.cropyme.task.SubmitTabOnClickTask;
//import WeChatShare;

/**
 * 已经使用X5内核  https://x5.tencent.com/
 * <p>
 * 这里请注意， 如果需要解析 http://localhost?cls=xxx&pkg=xxxparams=xxx 请直接调用方法
 *
 * @author zhengmj
 */
@SuppressLint({"NewApi", "SetJavaScriptEnabled"})
public class CommonWebViewActivity extends TJRBaseToolBarSwipeBackActivity implements OnClickListener {
    private com.tencent.smtt.sdk.WebView mWebView;
    private String url;
    private ProgressBar pb;
    private String title;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> valueCallback5Plus;
    private TextView tvTitle; // 标题
    private ImageButton ibColse; // 关闭
    private ImageButton ibBack;

    private String description;//描述
    // TODO: 19-4-17 分享暂时删除 原因：将旧的代码删除的时候这里受到关联，所以注释
//    private WeChatShare weChatShare;

    private boolean isHideBtn = true;//是否隐藏分享按钮  true代表隐藏  false代表显示
    private String target = "";//当isHideBtn为false的时候，显示分享按钮，如果target为空就分享原网页，否则需要调用接口获取需要分享的链接
//    private ShareFragment shareFragment;

//    private SubmitTabOnClickTask mSubmitTabOnClickTask;

    private BaseRemoteResourceManager baseRemoteResourceManager;
    private static final int REQUEST_CODE_IMAGE_CAPTURE = 1;
    private static final int PHOTORESOULT = 2;

    private String shareUrl;//分享出去的url不能带和token信息

    private boolean selFromAlbums = true;//认证只能拍照不能从相册中选取  true代表可以从相册中选取

    private String capturePath;//拍照的图片路径

    private DynamicPermission dynamicPermission;

    private boolean webHasBack = true;////网页是否有返回按钮
    private String picUrlForLongPress = "";


    public static void pageJumpCommonWebViewActivity(Context context, String url) {
        pageJumpCommonWebViewActivity(context, url, true);
    }


    /**
     * @param context
     * @param url
     * @param webHasBack 是否有返回按钮
     */
    public static void pageJumpCommonWebViewActivity(Context context, String url, boolean webHasBack) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.URLS, url);
        bundle.putBoolean(CommonConst.WEBHASBACK, webHasBack);
        PageJumpUtil.pageJump(context, CommonWebViewActivity.class, bundle);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.hotnews_webview;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //网页中的视频，上屏幕的时候，可能出现闪烁的情况，需要如下设置：Activity在onCreate时需要设置:（这个对宿主没什么影响，建议声明）
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        try {
            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11) {
                getWindow()
                        .setFlags(
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
                                android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
            }
        } catch (Exception e) {
        }
        baseRemoteResourceManager = getApplicationContext().getRemoteResourceChatManager();
        mActionBar.setDisplayHomeAsUpEnabled(false);
//        mActionBar.setTitle(null);
//        addCustomView();
        if (this.getIntent().getExtras() != null) {
            Bundle bundle = this.getIntent().getExtras();
            parserParamsBack(bundle, new ParamsBack() {
                @Override
                public void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception {
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "webURL")) {//iphone 消息统一使用 webURL
                        bundle.putString(CommonConst.URLS, jsonObject.getString("webURL"));
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "webUrl")) {
                        bundle.putString(CommonConst.URLS, jsonObject.getString("webUrl"));
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "urls")) {
                        bundle.putString(CommonConst.URLS, jsonObject.getString("urls"));
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.TITLE)) {
                        bundle.putString(CommonConst.TITLE, jsonObject.getString(CommonConst.TITLE));
                    }
                }
            });
            url = bundle.getString(CommonConst.URLS);
            title = bundle.getString(CommonConst.TITLE);
            webHasBack = bundle.getBoolean(CommonConst.WEBHASBACK, true);
//            auth= bundle.getBoolean(CommonConst.AUTH);
            if (!TextUtils.isEmpty(title)) {
                tvTitle.setText(title);
            }
        }
        if (TextUtils.isEmpty(url)) {
            CommonUtil.showmessage("参数错误", CommonWebViewActivity.this);
            finish();
            return;
        }
        if (url.contains("userId=%s")) {
            url = url.replace("userId=%s", "userId=" + getUserId());
        }
        shareUrl = url;
        if (url.contains("token=%s")) {
            url = url.replace("token=%s", "token=" + ConfigTjrInfo.getInstance().getSessionid());
            shareUrl = shareUrl.replace("token=%s", "token=");
        }
        Uri uri = Uri.parse(url);
        Set<String> set = uri.getQueryParameterNames();
        for (String key : set) {
            String param = uri.getQueryParameter(key);
            if (key.equals("isHideBtn")) {
                if (param.equals("false")) {
                    isHideBtn = false;
                } else {
                    isHideBtn = true;
                }
            } else if (key.equals("target")) {
                target = param;
            } else if (key.equals("selFromAlbums") && param.equals("false")) {
                selFromAlbums = false;
            }
            Log.d("webViewParams", "s==" + key + "   param==" + param);
        }

        com.procoin.social.util.CommonUtil.LogLa(2, "CommonWebViewActivity--->url = " + url);
//        setContentView(showView());
        showView();
        fixDirPath();
    }

    private void fixDirPath() {
        String path = ImageUtil.getDirPath();
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @SuppressWarnings("deprecation")
    private void showView() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        ibColse = (ImageButton) findViewById(R.id.ibColse);
        ibBack = (ImageButton) findViewById(R.id.ibBack);
        ibColse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//                onBackPressed();
                finish();
            }
        });
        if (webHasBack) {
            ibBack.setVisibility(View.VISIBLE);
            ibBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        } else {
            ibBack.setVisibility(View.GONE);
        }

        pb = (ProgressBar) findViewById(R.id.pb);
        mWebView = (WebView) findViewById(R.id.wv);
        mWebView.getSettings().setDomStorageEnabled(true);
//        mWebView.getSettings().setSaveFormData(false);
//        mWebView.getSettings().setSavePassword(false);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setBuiltInZoomControls(false);
        mWebView.getSettings().setSupportZoom(false);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.getSettings().setAllowFileAccess(true);
        mWebView.getSettings().setAllowContentAccess(true);
        mWebView.getSettings().setUserAgentString(mWebView.getSettings().getUserAgentString() + "TOKA");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
//        }
        mWebView.addJavascriptInterface(new WebViewContent(), "handler");
        mWebView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                WebView.HitTestResult result = mWebView.getHitTestResult();
                if (result == null) return false;
                int type = result.getType();
                if (type == WebView.HitTestResult.UNKNOWN_TYPE) return false;
                Log.d("mWebView", "type==" + type);
                if (type == WebView.HitTestResult.IMAGE_TYPE) {
                    picUrlForLongPress = result.getExtra();
                    Log.d("mWebView", "url==" + url);
                    showSavePicDialog();
                }
                return true;
            }
        });
//        mWebView.setDownloadListener(new Listener());
        mWebView.setWebChromeClient(new WebChromeClient() {
            // The undocumented magic method override
            // Eclipse will swear at you if you try to put @Override here
            // For Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                Log.d("setWebChromeClient", "openFileChooser......2222");
                mUploadMessage = uploadMsg;
                showAlertDialog();
            }

            // For Android 3.0+
            public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
                Log.d("setWebChromeClient", "openFileChooser......3333");
                mUploadMessage = uploadMsg;
                showAlertDialog();
            }

            // For Android 4.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                Log.d("setWebChromeClient", "openFileChooser......444");
                mUploadMessage = uploadMsg;
                showAlertDialog();

            }

            // For Android 5.0++
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                Log.d("setWebChromeClient", "onShowFileChooser......555");
                valueCallback5Plus = filePathCallback;
                showAlertDialog();
                return true;
            }


            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (tvTitle != null && !TextUtils.isEmpty(title)) tvTitle.setText(title);// 这个主要是
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                Log.d("onProgressChanged", "newProgress==" + newProgress);
                if (newProgress == 100) {
                    pb.setVisibility(View.GONE);
                } else {
                    if (pb.getVisibility() == View.GONE) pb.setVisibility(View.VISIBLE);
                    pb.setProgress(newProgress);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("shouldLoading", "onPageFinished url===" + url + "  mWebView.canGoBack()==" + mWebView.canGoBack());
                if (tvTitle != null) tvTitle.setText(view.getTitle());// 这个主要是返回的时候改变title。2个地方都设置
//                if (mWebView != null && mWebView.canGoBack()) {
//                    if (ibColse != null) ibColse.setVisibility(View.VISIBLE);
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_common_back_gray);
//                } else {
//                    if (ibColse != null) ibColse.setVisibility(View.GONE);
//                    getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_webview_close);
//                }
                view.loadUrl("javascript:window.handler.getContent(''+document.querySelector('meta[name=\"Description\"]').getAttribute('content')+'');");
                view.loadUrl("javascript:window.handler.getContent(''+document.querySelector('meta[name=\"description\"]').getAttribute('content')+'');");
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.d("shouldLoading", "shouldOverrideUrlLoading url===" + url);
                if (url.endsWith("?opp=close")) {
                    PageJumpUtil.finishCurr(CommonWebViewActivity.this);
                    return true;
                }
                if (url.startsWith("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                if (url.endsWith(".apk")) {
                    Uri uri = Uri.parse(url);
                    Intent viewIntent = new Intent(Intent.ACTION_VIEW, uri);
                    CommonWebViewActivity.this.startActivity(viewIntent);
                    return true;
                }
                if (openApp(url)) {//广告跳转其对应的app
                    return true;
                }
                if (PageJumpUtil.pageJumpUrl(CommonWebViewActivity.this, url)) return true;

                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                Log.d("shouldLoading", "onPageStarted url===" + url);
                super.onPageStarted(view, url, favicon);
                // showProgressDialog();
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Log.d("shouldLoading", "onReceivedError failingUrl===" + failingUrl);
                super.onReceivedError(view, errorCode, description, failingUrl);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.d("shouldLoading", "onLoadResource failingUrl===" + url);
                super.onLoadResource(view, url);
            }
        });
        mWebView.loadUrl(url);
    }


    private boolean isInstall(Intent intent) {
        return getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    //打开app
    private boolean openApp(String url) {
        if (TextUtils.isEmpty(url)) return false;
        try {
            if (!url.startsWith("http") && !url.startsWith("https") && !url.startsWith("ftp")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                if (isInstall(intent)) {
                    startActivity(intent);
//                     showOpenAtherAppTipDialog(intent);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }


    protected void showAlertDialog() {//, "从相册中选取"
        String[] items = null;
        if (selFromAlbums) {
            items = new String[]{"拍照", "从相册中选取"};
        } else {
            items = new String[]{"拍照"};
        }
        new AlertDialog.Builder(this).setTitle("图片选择").setItems(items, this).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // 取消对话框一定设置为null，否则点击第二次会没有反应
                releaseUploadMessage();

            }
        }).create().show();
    }


    protected void showSavePicDialog() {//, 长按保存图片
        if (TextUtils.isEmpty(picUrlForLongPress)) return;
        String[] items = new String[]{"保存图片"};
        new AlertDialog.Builder(this).setTitle("操作").setItems(items, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        initDynamicPermission();
                        dynamicPermission.checkSelfPermission(PermissionUtils.EXTERNAL_STORAGE, 200);
                        break;
                }
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
            }
        }).create().show();
    }

    private void initDynamicPermission() {
        if (dynamicPermission == null) {
            dynamicPermission = new DynamicPermission(this, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    if (requestCode == 100) {
                        capturePath = ImageUtil.getNewPhotoPath();
                        Intent intent = ImageUtil.takeBigPicture(CommonWebViewActivity.this, capturePath);
                        startActivityForResult(intent, REQUEST_CODE_IMAGE_CAPTURE);
                    } else if (requestCode == 200) {
                        savePicForLongPress(picUrlForLongPress);
                    }
                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {
                    releaseUploadMessage();
                }
            });
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        switch (which) {
            case 0:
                initDynamicPermission();
                dynamicPermission.checkSelfPermission(PermissionUtils.CAMERA_EXTERNAL_STORAGE, 100);
                break;
            case 1:
                //这里只能1张1张上传，因为删除图片后,前端接受不到回调
                MyhomeMultiSelectImageActivity.pageJumpThis(CommonWebViewActivity.this, 1, false, false, CommonWebViewActivity.class.getName());
                break;

            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (dynamicPermission != null)
            dynamicPermission.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {//从相册选择图片的回调
            ArrayList<String> flist = intent.getStringArrayListExtra(CommonConst.PICLIST);
            if (flist == null || flist.size() == 0) return;

            if (mUploadMessage != null) {
                for (String f : flist) {
                    mUploadMessage.onReceiveValue(Uri.fromFile(new File(f)));
//                        mUploadMessage = null;
                    Log.d("sourcePath", "onReceiveValue uri==" + f);
                }
            } else {
                Uri[] uris = new Uri[flist.size()];
                for (int i = 0, m = uris.length; i < m; i++) {
                    uris[i] = Uri.fromFile(new File(flist.get(i)));
                }
                valueCallback5Plus.onReceiveValue(uris);
                valueCallback5Plus = null;
            }
//            currPicSize=currPicSize+flist.size();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == 0) {//取消之后resultCode==0,这里在清除
            releaseUploadMessage();
            return;
        }
        switch (requestCode) {
            case REQUEST_CODE_IMAGE_CAPTURE:
                try {
                    if (mUploadMessage == null && valueCallback5Plus == null) {
                        return;
                    }
                    Bitmap bitmapOfFile = com.procoin.http.util.CommonUtil.getSmallBitmap(capturePath, true);
                    File f = saveBitmapToFile(bitmapOfFile);
                    Uri uri = Uri.fromFile(f);
//                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmapOfFile, null, null));//
                    if (mUploadMessage != null) {
                        mUploadMessage.onReceiveValue(uri);
                        Log.d("sourcePath", "onReceiveValue uri==" + uri);
                    } else {
                        valueCallback5Plus.onReceiveValue(new Uri[]{uri});
                        valueCallback5Plus = null;
                    }
//                    currPicSize++;
                } catch (Exception e) {
                    releaseUploadMessage();
                }
                break;
        }
        // TODO: 19-4-17 分享暂时删除 原因：将旧的代码删除的时候这里受到关联，所以注释
//        if (requestCode == 0x456 && resultCode == 0x789) {//分享
//            if (data != null) {
//                int type = data.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1);
//                switch (type) {
//                    case 0:
//                        if (weChatShare == null) {
//                            weChatShare = new WeChatShare(CommonWebViewActivity.this);
//                        }
//                        weChatShare.setTimeline(false);
//                        weChatShare.SendReqURL(shareUrl, tvTitle.getText().toString(), description, BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
//                        break;
//                    case 1:
//                        if (weChatShare == null) {
//                            weChatShare = new WeChatShare(CommonWebViewActivity.this);
//                        }
//                        weChatShare.setTimeline(true);
//                        weChatShare.SendReqURL(shareUrl, tvTitle.getText().toString(), description, BitmapFactory.decodeResource(getResources(), R.drawable.app_logo));
//                        break;
//                }
//            }
//
//        }

    }

    private void releaseUploadMessage() {
        if (mUploadMessage != null) {//如果有异常，就取消否则第二次点击会没有反映
            mUploadMessage.onReceiveValue(null);
            mUploadMessage = null;
        }
        if (valueCallback5Plus != null) {
            valueCallback5Plus.onReceiveValue(null);//
            valueCallback5Plus = null;
        }
    }

    /**
     * 保存图片到淘金目录
     *
     * @param bitmap
     * @return
     * @throws Exception
     */
    private File saveBitmapToFile(Bitmap bitmap) throws Exception {
        if (bitmap == null) return null;
        String fileName2 = System.currentTimeMillis() + "" + getUserId() + ".jpg";
        File file2 = baseRemoteResourceManager.getFile(fileName2);
        baseRemoteResourceManager.writeFile(file2, bitmap, true);
        if (file2 != null && file2.exists()) {
            return file2;
        }
        return null;
    }


    //当用户更改了系统字体，webView不会生效
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null) {
            ((ViewGroup) mWebView.getParent()).removeView(mWebView);
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.clearFormData();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;// 清空掉，不然会报nullpoint
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!isHideBtn) getMenuInflater().inflate(R.menu.webview_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (TextUtils.isEmpty(description)) description = "来源：W.W.C.T交易平台跟单网页分享";
        if (tvTitle == null) return true;//如果url为null，tvTitle这里有可能报nullpoint
        String title = tvTitle.getText().toString();
        if (!TextUtils.isEmpty(title)) {
            TjrSocialMTAUtil.trackCustomKVEvent(CommonWebViewActivity.this, TjrSocialMTAUtil.PROP_CLICKTYPE, title, TjrSocialMTAUtil.MTAWEIXINWEBSHARE);
        }
        if (TextUtils.isEmpty(url)) {
            CommonUtil.showmessage("没有获取到网页地址，请重新进入页面", CommonWebViewActivity.this);
            return super.onOptionsItemSelected(item);
        }
        if (TextUtils.isEmpty(mWebView.getUrl())) {
            return super.onOptionsItemSelected(item);
        }
        // TODO: 19-4-17 分享暂时删除 原因：将旧的代码删除的时候这里受到关联，所以注释
//        switch (item.getItemId()) {
//            case R.id.action_share:
//                if (TextUtils.isEmpty(target)) {
//                    ShareEntity shareEntity = new ShareEntity();
//                    shareEntity.title = tvTitle.getText().toString();
//                    shareEntity.content = description;
//                    showShareFragment(shareEntity);
//                } else {
//                    new GetShareUrlTask(3, target, new MyCallBack(CommonWebViewActivity.this) {
//                        @Override
//                        protected void callBack(ResultData resultData) {
//                            ShareEntity shareEntity = resultData.getObject(ShareEntity.class);
//                            if (shareEntity != null) showShareFragment(shareEntity);
//                        }
//                    });
//                }
//                break;
//            default:
//                break;
//        }
        return super.onOptionsItemSelected(item);
    }
// TODO: 19-4-17 分享暂时删除 原因：将旧的代码删除的时候这里受到关联，所以注释
//    private void showShareFragment(ShareEntity shareEntity) {
//        if (shareFragment == null) {
//            shareFragment = ShareFragment.newInstance(shareEntity);
//        } else {
//            shareFragment.setShareEntity(shareEntity);
//        }
//        shareFragment.show(getSupportFragmentManager(), "");
//    }

    @Override
    public void onBackPressed() {
        if (mWebView != null && mWebView.canGoBack()) {
            mWebView.goBack();
            return;
        }
//        startSubmitTabOnClickTask(url, 4);//关闭的广告
        super.onBackPressed();
    }

    public class WebViewContent {
        @JavascriptInterface
        public void getContent(String shareData) {
            if (null != shareData && description == null) {
                description = shareData;
                Log.d("shouldLoading", "url description is " + description);
            }
        }
    }

//    public void startSubmitTabOnClickTask(String modelId, int type) {
//        long userId = 0;
//        if (getApplicationContext().getUser() != null) {//这里不太确定能有userId
//            userId = getApplicationContext().getUser().getUserId();
//        }
//        if (userId > 0) {
//            com.cropyme.http.util.CommonUtil.cancelAsyncTask(mSubmitTabOnClickTask);
//            mSubmitTabOnClickTask = (SubmitTabOnClickTask) new SubmitTabOnClickTask(userId, modelId, type).executeParams();
//        }
//    }

    //长按保存图片
    public void savePicForLongPress(String url) {
        if (TextUtils.isEmpty(url)) return;
        ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String s, View view) {

            }

            @Override
            public void onLoadingFailed(String s, View view, FailReason failReason) {
                CommonUtil.showmessage("图片保存失败", CommonWebViewActivity.this);
            }

            @Override
            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                if (bitmap == null) {
                    CommonUtil.showmessage("图片保存失败", CommonWebViewActivity.this);
                    return;
                }
                try {
                    String fileName = VeDate.getyyyyMMddHHmmss(VeDate.getNow()) + ".png";
                    File file = getApplicationContext().getmDCIMRemoteResourceManager().getFile(fileName);
                    getApplicationContext().getmDCIMRemoteResourceManager().writeFile(file, bitmap, false);
                    if ((getApplicationContext()).isSDCard()) {
                        // 最后通知图库更新
                        CommonWebViewActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                    }
                    CommonUtil.showmessage("保存图片到" + file.getParent(), CommonWebViewActivity.this);
                } catch (Exception e) {
                    e.printStackTrace();
                    CommonUtil.showmessage("图片保存失败", CommonWebViewActivity.this);
                }
            }

            @Override
            public void onLoadingCancelled(String s, View view) {

            }
        });


    }


}
