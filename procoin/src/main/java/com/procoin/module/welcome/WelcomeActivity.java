package com.procoin.module.welcome;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.SysShareData;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.data.sharedpreferences.NormalShareData;
import com.procoin.nsk.TjrStarNSKManager;
import com.procoin.util.PageJumpUtil;
import com.procoin.MainApplication;
import com.procoin.common.entity.BootPage;
import com.procoin.module.home.HomeActivity;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.procoin.http.TjrBaseApi;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.social.TjrSocialMTAUtil;
import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class WelcomeActivity extends TJRBaseToolBarActivity implements View.OnClickListener {
    @BindView(R.id.ivADvert)
    ImageView ivADvert;
    @BindView(R.id.tvJump)
    TextView tvJump;
    private Handler mHandler = new Handler();
    //    private ImageView ivADvert;
//    private TextView tvJump;
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private boolean isPageJump;//是否已经点击了
    private boolean isNewversion;//判断是不是新版本
    private BootPage bootPage;
    private String userId = "";
    private MainApplication application;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            jumpNextPage();
        }
    };
    private Call<ResponseBody> bootPageCall, getDnsCall;

    /**
     *
     */
    private void jumpNextPage() {
        if (isNewversion) {
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConst.JUMPTYPE, 1);
            PageJumpUtil.pageJump(WelcomeActivity.this, GuideActivity.class, bundle);
        } else {
//            if (application.getUser() == null || application.getUser().getUserId() == 0) {
//                PageJumpUtil.pageJump(WelcomeActivity.this, LoginActivity.class, null);
//            } else {
//                PageJumpUtil.pageJump(WelcomeActivity.this, HomeActivity.class, null);
//            }
            PageJumpUtil.pageJump(WelcomeActivity.this, HomeActivity.class, null);
        }
        overridePendingTransition(R.anim.alpha_from0_to1, R.anim.alpha_from1_to0);
        WelcomeActivity.this.finish();
    }


    /**
     * 这个先判断广告
     */
    private Runnable advetRunnable = new Runnable() {
        @Override
        public void run() {
            if (bootPage != null && !TextUtils.isEmpty(bootPage.imgUrl)) {
                tjrImageLoaderUtil.displayImageWithListener(bootPage.imgUrl, ivADvert, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        tvJump.setVisibility(View.VISIBLE);
                        ivADvert.setOnClickListener(WelcomeActivity.this);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
                mHandler.postDelayed(runnable, 3000);


            } else {
                mHandler.post(runnable);
            }
        }

    };

    @Override
    protected int setLayoutId() {
        return R.layout.welcome_main;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    // android:theme="@android:style/Theme.Black.NoTitleBar"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        TjrSocialMTAUtil.initMTAConfig(TjrBaseApi.isDebug, this);
//        mActionBar.hide();
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        //之前经常碰到有时候按了home键之后app会重启,就是因为WelcomeActivity经常重新生成实例导致的
        if (!this.isTaskRoot()) { // 判断当前activity是不是所在任务栈的根
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        immersionBar.navigationBarColor(R.color.white).init();
        application = (MainApplication) getApplicationContext();
//        setContentView(R.layout.welcome_main);
        ButterKnife.bind(this);
        String version = SysShareData.getVersion(this);
        if (!version.equals(((MainApplication) getApplicationContext()).getmVersion()) && CommonConst.ISSHOWNEWVER) {
            isNewversion = true;
        }
//        isNewversion = true;
        User user = ((MainApplication) getApplicationContext()).getUser();
        if (user != null) userId = String.valueOf(user.getUserId());
        tjrImageLoaderUtil = new TjrImageLoaderUtil(R.drawable.ic_common_transparent);
//        new BootAdvertTask().executeParams();
        getDns();
//        BootAdvertTaskStart();
        mHandler.postDelayed(advetRunnable, 1500);//先展示2秒
        tvJump = (TextView) findViewById(R.id.tvJump);
        tvJump.setOnClickListener(WelcomeActivity.this);
        ivADvert = (ImageView) findViewById(R.id.ivADvert);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isPageJump) {
            tvJump.setOnClickListener(null);
            ivADvert.setOnClickListener(null);
            jumpNextPage();
            isPageJump = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvJump:
                mHandler.removeCallbacksAndMessages(null);
                tvJump.setVisibility(View.GONE);
                jumpNextPage();
                break;
            case R.id.ivADvert://广告页
                if (adVertClickable()) {
//                    if (application.getUser() == null || application.getUser().getUserId() == 0) {
//                        PageJumpUtil.pageJump(WelcomeActivity.this, LoginActivity.class, null);
//                        overridePendingTransition(R.anim.alpha_from0_to1, R.anim.alpha_from1_to0);
//                        WelcomeActivity.this.finish();
//                    } else {
                    isPageJump = true;
                    mHandler.removeCallbacksAndMessages(null);//成功跳转过去了才取消callback
                    PageJumpUtil.jumpByPkg(WelcomeActivity.this, bootPage.pkg, bootPage.cls, bootPage.params);
//                    }
                }
                break;
        }
    }

    private boolean adVertClickable() {//广告是否可以点击,必须要判断
        return bootPage != null && !TextUtils.isEmpty(bootPage.pkg) && !TextUtils.isEmpty(bootPage.cls) && !TextUtils.isEmpty(bootPage.params);
    }

    private void BootAdvertTaskStart() {
        CommonUtil.cancelCall(bootPageCall);
        bootPageCall = VHttpServiceManager.getInstance().getVService().bootPage();
        bootPageCall.enqueue(new MyCallBack(WelcomeActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.data != null) {
                    bootPage = resultData.getObject(BootPage.class);
                }
            }
        });
    }

    private void getDns() {
        CommonUtil.cancelCall(getDnsCall);
        getDnsCall = VHttpServiceManager.getInstance().getFileUploadService().getDns();
        getDnsCall.enqueue(new MyCallBack(WelcomeActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
//                    String quoteSocket = resultData.getItem("quoteSocket", String.class);
//                    String pushSocket = resultData.getItem("pushSocket", String.class);
//                    String api = resultData.getItem("api", String.class);
//
//                    TjrBaseApi.stockHomeUri.setUri(quoteSocket);
//                    TjrBaseApi.mApiSubPushUrl.setUri(pushSocket);
//                    TjrBaseApi.mApiCropymeBaseUri.setUri(api);

//                    NormalShareData.saveDnsInfo(WelcomeActivity.this,  quoteSocket,  pushSocket,  api);
                    //http
                    VHttpServiceManager.getInstance().resetService();
                    //socket quote
                    TjrStarNSKManager.getInstance().resetInit();
                    Log.d("ReceivedManager", String.format("[quoteSocket:%s] [pushSocket:%s] [api:%s]", TjrBaseApi.stockHomeUri.uri(),TjrBaseApi.mApiSubPushUrl.uri(),TjrBaseApi.mApiCropymeBaseUri.uri()));
                    BootAdvertTaskStart();
                }

            }
        });
    }
}
