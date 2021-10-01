package com.procoin.module.circle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.module.circle.entity.CircleRoleEnum;
import com.procoin.util.DensityUtil;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.BackgroundImageView;
import com.procoin.widgets.BadgeView;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.module.circle.entity.CircleConfig;
import com.procoin.util.PageJumpUtil;
import com.procoin.module.home.HomeActivity;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.http.widget.view.RoundAngleImageView;
import com.procoin.module.circle.entity.CircleInfo;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 圈子详情
 * Created by zhengmj on 18-11-14.
 */

public class CircleInfoActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    private static final String TAG = CircleInfoActivity.class.getSimpleName();

    @BindView(R.id.ivCircleBg)
    BackgroundImageView ivCircleBg;
    @BindView(R.id.ivHead)
    RoundAngleImageView ivHead;
    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.tvCircleName)
    TextView tvCircleName;
    @BindView(R.id.tvCircleId)
    TextView tvCircleId;
    @BindView(R.id.tvReviewState)
    TextView tvReviewState;
    @BindView(R.id.tvCircleBrief)
    TextView tvCircleBrief;
    @BindView(R.id.iv1)
    ImageView iv1;
    @BindView(R.id.tvTitleArticle)
    TextView tvTitleArticle;
    @BindView(R.id.tvArticleAmount)
    TextView tvArticleAmount;
    @BindView(R.id.iv2)
    ImageView iv2;
    @BindView(R.id.tvTitleProgram)
    TextView tvTitleProgram;
    @BindView(R.id.tvShowAmount)
    TextView tvShowAmount;
    @BindView(R.id.llAllMember)
    LinearLayout llAllMember;
    @BindView(R.id.tvMemberAmount)
    TextView tvMemberAmount;
    @BindView(R.id.tvInvate)
    TextView tvInvate;
    @BindView(R.id.llSetInCricleWay)
    LinearLayout llSetInCricleWay;
    @BindView(R.id.llVip)
    LinearLayout llVip;
    @BindView(R.id.toggleViewNews)
    SwitchCompat toggleViewNews;
    @BindView(R.id.toggleViewSpeak)
    SwitchCompat toggleViewSpeak;

    @BindView(R.id.vlineSetInCricleWay)
    View vlineSetInCricleWay;
    @BindView(R.id.vlineVip)
    View vlineVip;
    @BindView(R.id.llContent)
    LinearLayout llContent;
    @BindView(R.id.vLineNews)
    View vLineNews;
    @BindView(R.id.llNews)
    LinearLayout llNews;
    @BindView(R.id.vLineSpeak)
    View vLineSpeak;
    @BindView(R.id.llSpeak)
    LinearLayout llSpeak;
    @BindView(R.id.llApply)
    LinearLayout llApply;
    @BindView(R.id.vLineClearChat)
    View vLineClearChat;
    @BindView(R.id.llClearChat)
    LinearLayout llClearChat;
    @BindView(R.id.llExitCircle)
    LinearLayout llExitCircle;
    @BindView(R.id.vlineApplyJoin)
    View vlineApplyJoin;
    @BindView(R.id.llApplyJoin)
    LinearLayout llApplyJoin;
    @BindView(R.id.tvApplyText)
    TextView tvApplyText;
    @BindView(R.id.vLineBlacklist)
    View vLineBlacklist;
    @BindView(R.id.llBlacklist)
    LinearLayout llBlacklist;


    private String circleId = "";
    private CircleConfig circleConfig;
    private CircleInfo circleInfo;
    private int role;
    private Call<ResponseBody> uploadCall;
    private Call<ResponseBody> getCircleInfoCall;
    private Call<ResponseBody> setMsgAlertCall;


    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private TjrBaseDialog delChatTipsDialog;
    private TjrBaseDialog exitCircleTipsDialog;

    private Call<ResponseBody> applyJoinCirlceCall;
    private TjrBaseDialog applySucTipsDialog;

    private BadgeView badgeApply;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ActionBar mActionBar;
    private Toolbar toolbar;
//    private ShareFragment shareFragment;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_circle_info;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    public static void pageJumpThis(Context context, String circleId) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.CIRCLEID, circleId);
        PageJumpUtil.pageJump(context, CircleInfoActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            circleId = bundle.getString(CommonConst.CIRCLEID);
        }
        if (TextUtils.isEmpty(circleId)) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        ButterKnife.bind(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.getBackground().setAlpha(0);
        immersionBar
                .keyboardEnable(false)
                .statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA)
                .flymeOSStatusBarFontColor(R.color.white)
                .init();
        mActionBar = getSupportActionBar();
        mActionBar.setElevation(0);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setHomeAsUpIndicator(R.drawable.ic_svg_back_white);
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        mCollapsingToolbar.setTitleEnabled(false);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("onOffsetChanged", "verticalOffset==" + verticalOffset);
                int alpha = Math.min(Math.abs(verticalOffset) / 1, 255);
                toolbar.getBackground().setAlpha(alpha);
                Log.d("onOffsetChanged", "alpha==" + alpha);
                if (alpha >= 150) {
                    if (TextUtils.isEmpty(mActionBar.getTitle())) {//要判断不然会一直调用，影响性能
                        if (circleInfo != null) mActionBar.setTitle(circleInfo.circleName);
                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_common_back_gray);
                        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.black).init();
                        tvMenu.setTextColor(Color.BLACK);
                    }
                } else {
                    if (!TextUtils.isEmpty(mActionBar.getTitle())) {
                        mActionBar.setTitle("");
                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_svg_back_white);
                        tvMenu.setTextColor(Color.WHITE);
                        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
                    }

                }
            }
        });
        tjrImageLoaderUtil = new TjrImageLoaderUtil(com.procoin.http.R.drawable.ic_common_mic, com.procoin.http.R.drawable.ic_default_head, DensityUtil.dip2px(this, 5));
        // 消息免打扰
        boolean partyRemind = CircleSharedPreferences.getCircleSettingRemind(this, getUserIdLong(), circleId);
        toggleViewNews.setChecked(partyRemind);
        toggleViewNews.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("partyRemind", "onCheckedChanged==" + "  isChecked==" + isChecked);
                startSetMsgAlertTask(isChecked ? 1 : 0);
//                CircleSharedPreferences.saveCircleSettingRemind(CircleInfoActivity.this, getUserIdLong(), circleId, isChecked);
            }
        });


        badgeApply = new BadgeView(this, tvApplyText);
        badgeApply.setBadgeMargin(0, 0);
        badgeApply.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        tvMenu.setTextColor(Color.WHITE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startGetCircleInfo(circleId);
    }

    private void startGetCircleInfo(String circleId) {
        CommonUtil.cancelCall(getCircleInfoCall);
//        showProgressDialog();
        getCircleInfoCall = VHttpServiceManager.getInstance().getVService().getCircleInfo(circleId);
        getCircleInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
//                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    circleConfig = resultData.getObject("circleConfig", CircleConfig.class);
                    circleInfo = resultData.getObject("circle", CircleInfo.class);
                    try {
                        JSONObject jsonObject = resultData.returnJSONObject();
                        if (JsonParserUtils.hasAndNotNull(jsonObject, "circleRole")) {
                            JSONObject jsonCircleRole = jsonObject.getJSONObject("circleRole");
                            if (JsonParserUtils.hasNotNullAndIsIntOrLong(jsonCircleRole, "role")) {
                                role = jsonCircleRole.getInt("role");
                            }
                        }
                    } catch (Exception e) {
                    }
                    Log.d(TAG, "circleConfig==" + circleConfig);
                    Log.d(TAG, "circleInfo==" + circleInfo);
                    Log.d(TAG, "role==" + role);

                    setCircleData();

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
//                dismissProgressDialog();
            }
        });
    }

    private void startSetMsgAlertTask(int msgAlert) {
        CommonUtil.cancelCall(setMsgAlertCall);
        setMsgAlertCall = VHttpServiceManager.getInstance().getVService().setMsgAlert(circleId, msgAlert);
        setMsgAlertCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    int msgAlert = resultData.getItem("msgAlert", Integer.class);
                    CircleSharedPreferences.saveCircleSettingRemind(CircleInfoActivity.this, getUserIdLong(), circleId, msgAlert == 1);
                }
            }

        });
    }

    private void setSpeakStatus() {
        toggleViewSpeak.setOnCheckedChangeListener(null);
        toggleViewSpeak.setChecked(circleInfo.speakStatus == 1);
        toggleViewSpeak.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("setSpeakStatus", "onCheckedChanged==" + "  isChecked==" + isChecked);
                startSetSpeakStatusTask(isChecked ? 1 : 0);
            }
        });
    }

    private void startSetSpeakStatusTask(int status) {
        CommonUtil.cancelCall(getCircleInfoCall);
        getCircleInfoCall = VHttpServiceManager.getInstance().getVService().setSpeakStatus(circleId, status);
        getCircleInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                }
            }

            @Override
            protected void handleSuccessFalse(ResultData resultData) {
                super.handleSuccessFalse(resultData);
                toggleViewSpeak.setChecked(circleInfo.speakStatus == 1);//这里有可能没有权限操作会失败
            }
        });
    }

    private String getReviewState(int reviewState) {
        if (reviewState == 0) {
            return "审核中";
        } else if (reviewState == 2) {
            return "不通过";
        } else {
            return "";
        }
    }

    private void setCircleData() {
        if (circleConfig != null && circleInfo != null) {
            if (!TextUtils.isEmpty(circleInfo.circleBg) && !circleInfo.circleBg.equals(ivCircleBg.getTag())) {
                tjrImageLoaderUtil.displayImage(circleInfo.circleBg, ivCircleBg);
                ivCircleBg.setTag(circleInfo.circleBg);
            }
            if (!TextUtils.isEmpty(circleInfo.circleLogo) && !circleInfo.circleLogo.equals(ivHead.getTag())) {
                tjrImageLoaderUtil.displayImage(circleInfo.circleLogo, ivHead);
                ivHead.setTag(circleInfo.circleLogo);
            }
            tvCircleName.setText(circleInfo.circleName);
            tvCircleId.setText("  (圈子ID: " + circleInfo.circleId + ")");
            tvReviewState.setText(getReviewState(circleInfo.reviewState));
            tvCircleBrief.setText("简介: " + circleInfo.brief);
            tvMemberAmount.setText("共" + circleInfo.memberAmount + "名成员");
            tvArticleAmount.setText("共" + circleConfig.articleAmount + "篇");
            tvShowAmount.setText("共" + circleConfig.showAmount + "个");
            llAllMember.setOnClickListener(this);
            setSpeakStatus();


            if (CircleRoleEnum.isRoot(role)) {
                tvMenu.setVisibility(View.VISIBLE);
                tvMenu.setText("编辑");
                tvMenu.setOnClickListener(this);
            } else {
                tvMenu.setVisibility(View.GONE);

            }
            if (CircleRoleEnum.isllegal(role)) {//是游客
                llExitCircle.setVisibility(View.GONE);
                llClearChat.setVisibility(View.GONE);
                vLineClearChat.setVisibility(View.GONE);
                llApply.setVisibility(View.VISIBLE);
                llApply.setOnClickListener(this);
                llContent.setVisibility(View.GONE);
                tvInvate.setVisibility(View.GONE);
                vlineSetInCricleWay.setVisibility(View.GONE);
                llSetInCricleWay.setVisibility(View.GONE);
                vlineVip.setVisibility(View.GONE);
                llVip.setVisibility(View.GONE);
                vLineNews.setVisibility(View.GONE);
                llNews.setVisibility(View.GONE);
                vLineSpeak.setVisibility(View.GONE);//禁言这一项除了游客都显示，只是只有圈主才能有权限操作
                llSpeak.setVisibility(View.GONE);

                vlineApplyJoin.setVisibility(View.GONE);
                llApplyJoin.setVisibility(View.GONE);

                vLineBlacklist.setVisibility(View.GONE);
                llBlacklist.setVisibility(View.GONE);

            } else if (CircleRoleEnum.isRoot(role)) {//圈主
                llExitCircle.setVisibility(View.GONE);
                llApply.setVisibility(View.GONE);
                vLineClearChat.setVisibility(View.VISIBLE);
                llClearChat.setVisibility(View.VISIBLE);
                llClearChat.setOnClickListener(this);
//                llContent.setVisibility(View.VISIBLE);//先不显示
                llContent.setVisibility(View.GONE);
                tvInvate.setVisibility(View.VISIBLE);
                tvInvate.setOnClickListener(this);

                vlineSetInCricleWay.setVisibility(View.VISIBLE);
                llSetInCricleWay.setVisibility(View.VISIBLE);
                llSetInCricleWay.setOnClickListener(this);

                vlineVip.setVisibility(View.GONE);
                llVip.setVisibility(View.GONE);

                vLineNews.setVisibility(View.VISIBLE);
                llNews.setVisibility(View.VISIBLE);

                vLineSpeak.setVisibility(View.VISIBLE);//禁言这一项除了游客都显示，只是只有圈主才能有权限操作
                llSpeak.setVisibility(View.VISIBLE);

                vlineApplyJoin.setVisibility(View.VISIBLE);
                llApplyJoin.setVisibility(View.VISIBLE);
                llApplyJoin.setOnClickListener(this);
                setApplyNews(circleConfig.newApplyAmount);


                vLineBlacklist.setVisibility(View.VISIBLE);
                llBlacklist.setVisibility(View.VISIBLE);
                llBlacklist.setOnClickListener(this);
            } else {//普通成员或者管理员
                llExitCircle.setVisibility(View.VISIBLE);
                llExitCircle.setOnClickListener(this);
                llApply.setVisibility(View.GONE);
                llClearChat.setVisibility(View.VISIBLE);
                llClearChat.setOnClickListener(this);
                vLineClearChat.setVisibility(View.VISIBLE);

//                llContent.setVisibility(View.VISIBLE);//先不显示
                llContent.setVisibility(View.GONE);
                tvInvate.setVisibility(View.VISIBLE);
                tvInvate.setOnClickListener(this);

                vlineSetInCricleWay.setVisibility(View.GONE);
                llSetInCricleWay.setVisibility(View.GONE);

                //不要Vip了
//                vlineVip.setVisibility(View.VISIBLE);
//                llVip.setVisibility(View.VISIBLE);
//                llVip.setOnClickListener(this);
                vlineVip.setVisibility(View.GONE);
                llVip.setVisibility(View.GONE);

                vLineNews.setVisibility(View.VISIBLE);
                llNews.setVisibility(View.VISIBLE);

                llSpeak.setVisibility(View.VISIBLE);//禁言这一项除了游客都显示，只是只有圈主才能有权限操作
                vLineSpeak.setVisibility(View.VISIBLE);

                if (CircleRoleEnum.isRootOrAdmin(role)) {//管理员显示新申请加入

                    vlineApplyJoin.setVisibility(View.VISIBLE);
                    llApplyJoin.setVisibility(View.VISIBLE);
                    llApplyJoin.setOnClickListener(this);
                    setApplyNews(circleConfig.newApplyAmount);

                    vLineBlacklist.setVisibility(View.VISIBLE);
                    llBlacklist.setVisibility(View.VISIBLE);
                    llBlacklist.setOnClickListener(this);

                } else {
                    vlineApplyJoin.setVisibility(View.GONE);
                    llApplyJoin.setVisibility(View.GONE);
                    vLineBlacklist.setVisibility(View.GONE);
                    llBlacklist.setVisibility(View.GONE);

                }
            }


        }
    }

    private void setApplyNews(int newApplyAmount) {
        if (newApplyAmount > 0) {
            badgeApply.show();
            badgeApply.setText(String.valueOf(newApplyAmount));
        } else {
            badgeApply.hide();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x123) {//编辑回来
            if (data != null) {
                CircleInfo info = data.getParcelableExtra(CommonConst.CIRCLEINFO);
                if (info != null) {
                    circleInfo = info;
                    tvCircleName.setText(circleInfo.circleName);
                    tvCircleBrief.setText("简介: " + circleInfo.brief);
                    tjrImageLoaderUtil.displayImage(circleInfo.circleBg, ivCircleBg);
                    tjrImageLoaderUtil.displayImage(circleInfo.circleLogo, ivHead);
                }
            }
        } else if (resultCode == 0x456) {//设置完加入方式回来
            if (data != null) {
                int mode = data.getIntExtra(CommonConst.CIRCLEJOINMODE, 0);
                if (circleInfo != null) circleInfo.joinMode = mode;
            }
        }
    }


    private void showDelChatTipsDialog() {
        delChatTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                int clearChatNums = getApplicationContext().getmDb().clearCircleChat(circleId);
                if (clearChatNums >= 0) {
                    CommonUtil.showmessage("清除成功", CircleInfoActivity.this);
                    Intent intent = new Intent(CommonConst.CLEAR_CIRCLE_CHAT);
                    intent.putExtra(CommonConst.CIRCLEID, circleId);
                    sendBroadcast(intent);
                }
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delChatTipsDialog.setMessage("删除后,将清空该聊天的消息记录");
        delChatTipsDialog.setBtnOkText("删除");
        delChatTipsDialog.setTitleVisibility(View.GONE);
        delChatTipsDialog.show();
    }


    private void showExitCircleTipsDialog() {
        exitCircleTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startExitCircle();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        exitCircleTipsDialog.setMessage("是否退出圈子?");
        exitCircleTipsDialog.setBtnOkText("退出");
        exitCircleTipsDialog.setTitleVisibility(View.GONE);
        exitCircleTipsDialog.show();
    }

    private void startExitCircle() {
        CommonUtil.cancelCall(getCircleInfoCall);
        showProgressDialog();
        getCircleInfoCall = VHttpServiceManager.getInstance().getVService().exitCircle(circleId);
        getCircleInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage("已退出", CircleInfoActivity.this);
                    int del = getApplicationContext().getmDb().delCircleRel(circleId, getUserIdLong());
                    Log.d(TAG, "delCircleRel==" + del);
                    PageJumpUtil.pageJump(CircleInfoActivity.this, HomeActivity.class);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void startApplyJoinCirlceTask(String circleId, String reason) {
        CommonUtil.cancelCall(applyJoinCirlceCall);
        showProgressDialog();
        applyJoinCirlceCall = VHttpServiceManager.getInstance().getVService().applyJoinCirlce(circleId, reason);
        applyJoinCirlceCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    showTipsDialog(resultData.msg);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void showTipsDialog(String msg) {
        applySucTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startGetCircleInfo(circleId);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        applySucTipsDialog.setMessage(msg);
        applySucTipsDialog.setBtnOkText("确定");
        applySucTipsDialog.setCloseVisibility(View.GONE);
        applySucTipsDialog.setTitleVisibility(View.GONE);
        applySucTipsDialog.show();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.llApply:
                if (circleInfo == null) return;
                if (circleInfo.joinMode == 1) {
                    startApplyJoinCirlceTask(circleId, "");
                } else if (circleInfo.joinMode == 0) {
                    ApplyJoinCircleActivity.pageJumpThis(CircleInfoActivity.this, circleId);
                }
                break;
            case R.id.llExitCircle:
                showExitCircleTipsDialog();
                break;
            case R.id.llClearChat:
                showDelChatTipsDialog();
                break;
            case R.id.llAllMember:
                if (CircleRoleEnum.isllegal(role)) {//是游客
                    CommonUtil.showmessage("你还不是圈子成员,无法查看", CircleInfoActivity.this);
                    return;
                }
                CircleAllMembersActivity.pageJumpThis(CircleInfoActivity.this, circleId, role);
                break;
            case R.id.llApplyJoin:
                CircleApplyListActivity.pageJumpThis(CircleInfoActivity.this, circleId);
                break;
            case R.id.tvMenu:
                if (circleInfo == null) return;
                CreateOrEidtCircleActivity.pageJumpThis(CircleInfoActivity.this, circleInfo);
                break;
            case R.id.llSetInCricleWay:
                SetJoinCircleWayActivity.pageJumpThisForResult(CircleInfoActivity.this, circleId, circleInfo.joinMode);
                break;
            case R.id.llBlacklist:
                CircleBlackListActivity.pageJumpThis(CircleInfoActivity.this, circleId);
                break;
            case R.id.tvInvate:
//                CommonUtil.showmessage("暂未开放!", CircleInfoActivity.this);
                if (!TextUtils.isEmpty(circleId)){
                    // TODO: 19-4-17 分享暂时删除 原因：将旧的代码删除的时候这里受到关联，所以注释
//    try{
//        new GetShareUrlTask(1, Long.parseLong(circleId), new MyCallBack(CircleInfoActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                ShareEntity shareEntity = resultData.getObject(ShareEntity.class);
//                if (shareEntity != null) showShareFragment(shareEntity);
//            }
//        });
//    }catch (ClassCastException e){
//    }
                }
                break;
        }
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
}
