package com.procoin.module.myhome;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

// TODO: 18-10-10 注意，清理旧文件时将layout删掉了，之后再用时要注意把layout添加进去！！！！
public class AboutActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.iv_web_logo)
    ImageView ivWebLogo;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tvVersion)
    TextView tvVersion;
    @BindView(R.id.llAdvertAndCooperate)
    LinearLayout llAdvertAndCooperate;
    @BindView(R.id.llHelp)
    LinearLayout llHelp;
    @BindView(R.id.tvTaojinNetWork)
    TextView tvTaojinNetWork;
    @BindView(R.id.llProtoco)
    LinearLayout llProtoco;


    public void goback() {
        PageJumpUtil.finishCurr(this);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.myhome_aboutme;
    }

    @Override
    protected String getActivityTitle() {
        return "关于我们";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.myhome_aboutme);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
//        if (view == null) {
        OnClick onClick = new OnClick();
//            view = View.inflate(this, R.layout.myhome_aboutme, null);
//            llFankui = (LinearLayout) findViewById(R.id.llFankui);
//            llMarketRate = (LinearLayout) findViewById(R.id.llMarketRate);
//            llGuide = (LinearLayout) findViewById(R.id.llGuide);
//            llHelp= (LinearLayout) findViewById(R.id.llHelp);

//            llAdvertAndCooperate = (LinearLayout) findViewById(R.id.llAdvertAndCooperate);
//            flVideo = (FrameLayout) findViewById(R.id.flVideo);

//            tvVersion = (TextView) findViewById(R.id.tvVersion);
//            llVersion = (LinearLayout) findViewById(R.id.llVersion);
        tvVersion.setText("当前版本: " + getApplicationContext().getmVersionName());
//            btnInstall = (TextView) findViewById(R.id.btnInstall);
//            if (getApplicationContext().isNewVersion()) {
//                btnInstall.setVisibility(View.VISIBLE);
//                llVersion.setVisibility(View.GONE);
//            } else {
//                btnInstall.setVisibility(View.GONE);
//                llVersion.setVisibility(View.VISIBLE);
//            }
//            btnInstall.setOnClickListener(onClick);
//            llFankui.setOnClickListener(onClick);
//            llMarketRate.setOnClickListener(onClick);
//            llGuide.setOnClickListener(onClick);
//            llHelp.setOnClickListener(onClick);
        llAdvertAndCooperate.setOnClickListener(onClick);
        llProtoco.setOnClickListener(onClick);
//            findViewById(R.id.llProtoco).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Bundle bundle = new Bundle();
//                    String url = TjrBaseApi.mApiBeebarUri.uri()+"/protocol/bbs_protocol.jsp";
//                    bundle.putString(CommonConst.URLS,url);
//                    PageJumpUtil.pageJumpToData(AuthenticationActivity.this, CommonWebViewActivity.class, bundle);
//                }
//            });
//            flVideo.setOnClickListener(onClick);
//        }
    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.btnInstall:
//                    getApplicationContext().showDownLoadAppWithDialog(AuthenticationActivity.this);
//                    break;
                // case R.id.btnReview:
                // CommonUtil.pageJump(HomeAboutActivity.this,
                // FeedbackActivity.class, false, false);
                // break;
//                case R.id.llMarketRate:
//                    try {
//                        Uri uri = Uri.parse("market://details?id=" + getPackageName());
//                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(intent);
//                    } catch (Exception e) {
//                        CommonUtil.showmessage("参数错误", AuthenticationActivity.this);
//                    }
//                    break;
//                case R.id.llGuide:
//                    PageJumpUtil.pageJump(AuthenticationActivity.this, GuideActivity.class, null);
//                    break;
//                case R.id.llFankui:
//                    PageJumpUtil.pageJump(AuthenticationActivity.this, FeedbackActivity.class, null);
//                    break;
//                case R.id.llAdvertAndCooperate:
//                    PageJumpUtil.pageJump(AuthenticationActivity.this, AdvertAndCooperateActivity.class, null);
//                    break;
//                case R.id.llshareTjr:
//                    PageJumpUtil.pageJump(AuthenticationActivity.this, InviteFriendActivity.class, null);
//                    break;
//                case R.id.llHelp:
//                    Bundle bundle = new Bundle();
//                    bundle.putString(CommonConst.URLS, help_url);
//                    PageJumpUtil.pageJump(AuthenticationActivity.this, CommonWebViewActivity.class, bundle);
//                    break;
//                case R.id.flVideo:
//
//                    Bundle bundle2 = new Bundle();
//                    bundle2.putInt("mPreviewImgRes", R.drawable.ic_video_img_bg);
//                    bundle2.putString("videoUrl", video_url);
////                    PageJumpUtil.pageJump(AuthenticationActivity.this, VideoFullScreenActivity.class, bundle2);
//                    break;
                case R.id.llProtoco:
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(AboutActivity.this, CommonConst.ABOUTCROPYME);
                    break;
                default:
                    break;
            }

        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (data == null) return;
//        int type = data.getIntExtra(CommonConst.KEY_EXTRAS_TYPE, -1);
//
//
//        //0微信好友、1微信朋友圈、2新浪微博
//        switch (type) {
//            case 0:// 微信
//                TjrSocialShare.getInstance().shareToWeiXinNoshow(this, "淘金路", "淘金路,股票的游乐园", true, null, "http://www.tajinroad.com");
//                break;
//            case 1:// 微信好友圈
//                TjrSocialShare.getInstance().shareToWeiXinNoshow(this, "淘金路", "淘金路,股票的游乐园", false, null, "http://www.tajinroad.com");
//                break;
//            case 2:// 分享到微薄
//                // TODO 分享到微薄
//                TjrSocialShare.getInstance().shareToWeiboWithCanvas(this, "淘金路,股民的游乐园 http://www.taojinroad.com", null, null, getApplicationContext().getUser(), false);
//                break;
//            default:
//                break;
//        }
//
//    }

}
