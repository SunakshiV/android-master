//package com.tjr.bee.common;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.tjr.bee.R;
//import TJRBaseToolBarSwipeBackActivity;
//import CommonConst;
//import CommonWebViewActivity;
//import GetShareUrlTask;
//import CommonUtil;
//import DateUtils;
//import PageJumpUtil;
//import Parser;
//import WebViewHelper;
//import WeChatShare;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
///**
// * 红人卡公告详情页面
// * Created by kechenng on 17-5-11.
// */
//
//public class CommonNewsDetailActivity extends TJRBaseToolBarSwipeBackActivity implements WebViewHelper.WebProgress {
//
//    private TextView tvTitle;
//    private TextView tvTime;
//    private WebView mWebView;
//    private LinearLayout llHead;
//    private WebViewHelper webViewHelper;
//    private String content;
//    private String create_time;
//    private String title;
//
//
//    private String share_url;
//    private String share_title;
//    private String share_content;
//    private String share_logo;
//
//
//    private String id;
//    private String prod_code;
//
//    private String params;
//
//    private WeChatShare weChatShare;
//
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.common_webview_news_detail;
//    }
//
//    @Override
//    protected String getActivityTitle() {
//        return "公告";
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        Bundle bundle = null;
//        if ((bundle = getIntent().getExtras()) != null) {
//            if (bundle.containsKey("title")) {
//                title = bundle.getString("title");
//            }
//            if (bundle.containsKey("question_coin")) {
//                create_time = bundle.getString("question_coin");
//            }
//            if (bundle.containsKey("describes")) {
//                content = bundle.getString("describes");
//            }
//
//            if (bundle.containsKey("id")) {
//                id = bundle.getString("id");
//            }
//            if (bundle.containsKey(CommonConst.PROD_CODE)) {
//                prod_code = bundle.getString(CommonConst.PROD_CODE);
//            }
//
//            android.util.Log.d("params2JsonStr","33id=="+id+"  prod_code=="+prod_code);
//        } else {
//            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(create_time) || TextUtils.isEmpty(content)) {
//                CommonUtil.showmessage("参数错误", this);
//                finish();
//            }
//        }
////        setContentView(R.layout.common_webview_news_detail);
//        tvTitle = (TextView) findViewById(R.id.tvTitle);
//        tvTime = (TextView) findViewById(R.id.tvTime);
//        mWebView = (WebView) findViewById(R.id.webView);
//        llHead = (LinearLayout) findViewById(R.id.llHead);
//
//        mWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setBuiltInZoomControls(false);
//        mWebView.getSettings().setSupportZoom(false);
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.webview_share, menu);
//        return true;
//    }
//
//    private String params2JsonStr() {
//        if (TextUtils.isEmpty(params)) {
//            JSONObject j = new JSONObject();
//            try {
//                j.put("prod_code", prod_code);
//                j.put("id", id);
//                params = j.toString();
//                android.util.Log.d("params2JsonStr","id=="+id+"  prod_code=="+prod_code+"  params=="+params);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        return params;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.action_share:
//                if (TextUtils.isEmpty(share_url)) {
//                    try {
//                        new GetShareUrlTask("prod_news", params2JsonStr()) {
//                            @Override
//                            protected void onPreExecute() {
//                                super.onPreExecute();
//                                showProgressDialog();
//                            }
//
//                            @Override
//                            protected void onPostExecute(Boolean aBoolean) {
//                                super.onPostExecute(aBoolean);
//                                dismissProgressDialog();
//                                if (aBoolean) {
//                                    CommonNewsDetailActivity.this.share_title = this.title;
//                                    CommonNewsDetailActivity.this.share_content = this.content;
//                                    CommonNewsDetailActivity.this.share_logo = this.logo;
//                                    CommonNewsDetailActivity.this.share_url = this.share_url;
//                                    CommonUtil.gotoShareActivity(CommonNewsDetailActivity.this, ShareActivity.ShareTypeEnum.WECHAT.type(), 0x456);
//                                }
//                            }
//                        }.executeParams();
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    CommonUtil.gotoShareActivity(CommonNewsDetailActivity.this, ShareActivity.ShareTypeEnum.WECHAT.type(), 0x456);
//                }
////                com.tjr.perval.util.CommonUtil.gotoShareActivity(this, ShareActivity.ShareTypeEnum.WECHAT.type(), 0x456);
//                break;
//            default:
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == 0x789 && requestCode == 0x456) {
//            if (data != null) {
//                int type = data.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1);
//                if (TextUtils.isEmpty(share_url)) {
//                    CommonUtil.showmessage("参数错误", CommonNewsDetailActivity.this);
//                    return;
//                }
//                Bitmap bitmap = TextUtils.isEmpty(share_logo) ? BitmapFactory.decodeResource(getResources(), R.mipmap.ic_applogo) : ImageLoader.getInstance().loadImageSync(share_logo);
//                switch (type) {
//                    case 0:
//                        if (weChatShare == null) {
//                            weChatShare = new WeChatShare(CommonNewsDetailActivity.this);
//                        }
//                        weChatShare.setTimeline(false);
//                        weChatShare.SendReqURL(share_url, share_title, share_content, bitmap);
//                        break;
//                    case 1:
//                        if (weChatShare == null) {
//                            weChatShare = new WeChatShare(CommonNewsDetailActivity.this);
//                        }
//                        weChatShare.setTimeline(true);
//                        weChatShare.SendReqURL(share_url, share_title, share_content, bitmap);
//                        break;
//                }
//            }
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (content != null) {
//            if (!TextUtils.isEmpty(content)) {
//                if (content.startsWith("http")) {  //加载URL网页
//                    llHead.setVisibility(View.GONE);
//                    mWebView.setWebViewClient(new WebViewClient() {
//                        @Override
//                        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//                            super.onPageStarted(view, url, favicon);
//                            showProgressDialog();
//                        }
//
//                        @Override
//                        public void onPageFinished(WebView view, String url) {
//                            super.onPageFinished(view, url);
//                            if (!isFinishing()) dismissProgressDialog();
//                        }
//
//                        @Override
//                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            Bundle bundle = new Bundle();
//                            bundle.putString(CommonConst.URLS, url);
//                            PageJumpUtil.pageJump(CommonNewsDetailActivity.this, CommonWebViewActivity.class, bundle);
//                            finish();
//                            return true;
//                        }
//
//                    });
//                    mWebView.loadUrl(content);
//                } else {
//                    llHead.setVisibility(View.VISIBLE);
//                    tvTitle.setText(title);
//                    tvTime.setText(DateUtils.strdateFormat(create_time, DateUtils.TEMPLATE_yyyyMMdd_HHmm));
//                    webViewHelper = new WebViewHelper(CommonNewsDetailActivity.this);
//                    webViewHelper.execute(mWebView, new Parser(mWebView, content, 0), CommonNewsDetailActivity.this);
//                }
//            }
//        }
//    }
//
//
//    @Override
//    protected void onDestroy() {
//        if (mWebView != null) {
//            mWebView.clearCache(true);
//            mWebView.clearHistory();
//            mWebView.clearFormData();
//            mWebView.removeAllViews();
//            mWebView.destroy();
//            mWebView = null;
//        }
//        super.onDestroy();
//    }
//
//
//    @Override
//    public void onPageStarted() {
//
//    }
//
//    @Override
//    public void onPageFinished() {
//
//    }
//
//
//}
