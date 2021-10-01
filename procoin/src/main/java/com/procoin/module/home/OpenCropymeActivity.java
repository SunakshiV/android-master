package com.procoin.module.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 开通操作台
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class OpenCropymeActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.llOpenCropy)
    LinearLayout llOpenCropy;
    @BindView(R.id.tvUsdtAmount)
    TextView tvUsdtAmount;
    @BindView(R.id.tvRules)
    TextView tvRules;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private ActionBar mActionBar;
    private Toolbar toolbar;

    private String openCopyBalance;
    private String openCopyRules;

    private Call<ResponseBody> openCopyRulesCall;
    private Call<ResponseBody> openCopyCall;
    private TjrBaseDialog tjrBaseDialog;

//    private CheckBox tv_sign;
    private TextView  tvProtocol;

    @Override
    protected int setLayoutId() {
        return R.layout.open_cropyme;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(0);
        immersionBar
                .keyboardEnable(false)
                .titleBar(toolbar, false)
                .statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA)
                .flymeOSStatusBarFontColor(R.color.black)
                .init();

        mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_common_back_gray);


        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitleEnabled(false);

        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);


//        builder = new StringBuilder("#ffffffff");

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {


            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                Log.d("onOffsetChanged", "verticalOffset==" + verticalOffset);
                int alpha = Math.min(Math.abs(verticalOffset) / 1, 255);
                toolbar.getBackground().setAlpha(alpha);
//                if(alpha>16){//至少大于16，否则会报错
//                    builder.replace(1,3,Integer.toHexString(alpha));
//                    toolbar.setBackgroundColor(Color.parseColor(builder.toString()));
//                }else{
//                    toolbar.setBackgroundColor(Color.parseColor("#00ffffff"));
//                }
                Log.d("onOffsetChanged", "alpha==" + alpha);
//                toolbar.getBackground().setAlpha(alpha);
                if (alpha >= 150) {
                    if (TextUtils.isEmpty(mActionBar.getTitle())) {//要判断不然会一直调用，影响性能
                        mActionBar.setTitle("开通须知");
//                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_common_back_gray);
//                        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.black).init();
                    }

                } else {
                    if (!TextUtils.isEmpty(mActionBar.getTitle())) {
                        mActionBar.setTitle("");
//                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_common_back_white);
//                        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
                    }

                }
            }
        });

//        tv_sign = (CheckBox) findViewById(R.id.cbSign);
        tvProtocol= (TextView) findViewById(R.id.tvProtocol);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString normalText = new SpannableString("开通即代表你已阅读");
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString clickText = new SpannableString("《W.W.C.T带单功能服务协议》");

        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(OpenCropymeActivity.this, CommonConst.SERVICE_PROTOCOL);
            }
            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(OpenCropymeActivity.this, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(clickText);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);

        llOpenCropy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (!tv_sign.isChecked()) {
//                    CommonUtil.showmessage("请先阅读《W.W.C.T带单功能服务协议》",OpenCropymeActivity.this);
//                    return;
//                }
                showTipsDialog();
            }
        });
        startOpenCopyRules();
    }



    private void startOpenCopyRules() {
        CommonUtil.cancelCall(openCopyRulesCall);
        openCopyRulesCall = VHttpServiceManager.getInstance().getVService().openCopyRules();
        openCopyRulesCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    openCopyBalance = resultData.getItem("openCopyBalance", String.class);
                    openCopyRules = resultData.getItem("openCopyRules", String.class);
                    tvUsdtAmount.setText("开通条件：总资产不低于" + openCopyBalance + "USDT");
                    tvRules.setText(openCopyRules);
                }
            }

        });
    }



    private void startopenCopy() {
        CommonUtil.cancelCall(openCopyCall);
        openCopyCall = VHttpServiceManager.getInstance().getVService().openCopy();
        openCopyCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OpenCropymeActivity.this);
//                    PageJumpUtil.pageJump(OpenCropymeActivity.this, CropyUserDataActivity.class);
                    PageJumpUtil.finishCurr(OpenCropymeActivity.this);
                }
            }

        });
    }


    private void showTipsDialog() {
        tjrBaseDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startopenCopy();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        tjrBaseDialog.setTvTitle("提示");
        tjrBaseDialog.setMessage("是否确定开通带单功能？");
        tjrBaseDialog.setBtnOkText("确定");
        tjrBaseDialog.show();
    }

}
