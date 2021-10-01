package com.procoin.module.myhome;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.module.myhome.adapter.UserCircleAdapter;
import com.procoin.module.myhome.entity.KdpInfo;
import com.procoin.util.DensityUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.common.constant.CommonConst;
import com.procoin.module.circle.entity.CircleInfo;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.widgets.CircleImageView;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 *
 * 废弃
 * 个人主页
 * Created by zhengmj on 18-11-14.
 */

public class PersonalHomepageActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    private static final String TAG = PersonalHomepageActivity.class.getSimpleName();
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvBrief)
    TextView tvBrief;
    @BindView(R.id.rlCircleOfJoin)
    RecyclerView rlCircleOfJoin;
    @BindView(R.id.llJoin)
    LinearLayout llJoin;
    @BindView(R.id.ivKdpLogo)
    ImageView ivKdpLogo;
    @BindView(R.id.tvDesigName)
    TextView tvDesigName;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvTimes)
    TextView tvTimes;
    @BindView(R.id.llPredictionTape)
    LinearLayout llPredictionTape;
//    @BindView(R.id.rlCircleOfCreate)
//    RecyclerView rlCircleOfCreate;
//    @BindView(R.id.llCreate)
//    LinearLayout llCreate;

    private Call<ResponseBody> getCircleInfoCall;
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private UserCircleAdapter joinCircleAdapter;
    //    private UserCircleAdapter createCircleAdapter;
    private long targetUid;

    private CollapsingToolbarLayout mCollapsingToolbar;
    private ActionBar mActionBar;
    private Toolbar toolbar;

    private User user;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_personal_home;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    public static void pageJumpThis(Context context, long targetUid) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.TARGETUID, targetUid);
        PageJumpUtil.pageJump(context, PersonalHomepageActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            targetUid = bundle.getLong(CommonConst.TARGETUID, 0l);
        }
        if (targetUid == 0l) {
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
                        if (user != null) mActionBar.setTitle(user.getUserName());
                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_common_back_gray);
                        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.black).init();
                    }
                } else {
                    if (!TextUtils.isEmpty(mActionBar.getTitle())) {
                        mActionBar.setTitle("");
                        mActionBar.setHomeAsUpIndicator(R.drawable.ic_svg_back_white);
                        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).flymeOSStatusBarFontColor(R.color.white).init();
                    }

                }
            }
        });
        tjrImageLoaderUtil = new TjrImageLoaderUtil(com.procoin.http.R.drawable.ic_common_mic, com.procoin.http.R.drawable.ic_default_head, DensityUtil.dip2px(this, 5));
        startGetHomepage();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void startGetHomepage() {
        CommonUtil.cancelCall(getCircleInfoCall);
//        showProgressDialog();
        getCircleInfoCall = VHttpServiceManager.getInstance().getVService().homepage(targetUid);
        getCircleInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
//                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    user = resultData.getObject("user", User.class);
                    KdpInfo kdpInfo = resultData.getObject("kdpInfo", KdpInfo.class);
//                    Group<CircleInfo> groupCreateCircleList = resultData.getGroup("createCircleList", new TypeToken<Group<CircleInfo>>() {
//                    }.getType());
                    Group<CircleInfo> groupJoinCircleList = resultData.getGroup("joinCircleList", new TypeToken<Group<CircleInfo>>() {
                    }.getType());
                    Log.d("homepage", "groupJoinCircleList==" + (groupJoinCircleList == null ? "0" : groupJoinCircleList.size()));
                    if (user != null) {
                        tvName.setText(user.getUserName());
                        tvId.setText("(ID: " + user.userId + ")");
                        tvBrief.setText(user.getDescribes());
                        tjrImageLoaderUtil.displayImage(user.headUrl, ivHead);
                    }
                    if (kdpInfo != null) {
                        llPredictionTape.setVisibility(View.VISIBLE);
                        tvDesigName.setText(kdpInfo.desigName);
                        tvRate.setText(kdpInfo.winRateValue);
                        tvTimes.setText(String.valueOf(kdpInfo.loseCount + kdpInfo.winCount));
                        tjrImageLoaderUtil.displayImage(kdpInfo.desigLogo, ivKdpLogo);
                    } else {
                        llPredictionTape.setVisibility(View.GONE);
                    }
//                    if (groupCreateCircleList != null && groupCreateCircleList.size() > 0) {
//                        llCreate.setVisibility(View.VISIBLE);
//                        createCircleAdapter = new UserCircleAdapter(PersonalHomepageActivity.this);
//                        rlCircleOfCreate.setLayoutManager(new LinearLayoutManager(PersonalHomepageActivity.this));
//                        rlCircleOfCreate.addItemDecoration(new SimpleRecycleDivider(PersonalHomepageActivity.this, 15,15, ContextCompat.getColor(PersonalHomepageActivity.this, R.color.dividerColor)));
//                        rlCircleOfCreate.setAdapter(createCircleAdapter);
//                        createCircleAdapter.setGroup(groupCreateCircleList);
//                    } else {
//                        llCreate.setVisibility(View.GONE);
//                    }
                    if (groupJoinCircleList != null && groupJoinCircleList.size() > 0) {
                        llJoin.setVisibility(View.VISIBLE);
                        joinCircleAdapter = new UserCircleAdapter(PersonalHomepageActivity.this);
                        rlCircleOfJoin.setLayoutManager(new LinearLayoutManager(PersonalHomepageActivity.this));
                        rlCircleOfJoin.addItemDecoration(new SimpleRecycleDivider(PersonalHomepageActivity.this, 15, 15, ContextCompat.getColor(PersonalHomepageActivity.this, R.color.dividerColor)));
                        rlCircleOfJoin.setAdapter(joinCircleAdapter);
                        joinCircleAdapter.setGroup(groupJoinCircleList);
                        Log.d("homepage", "==========");

                    } else {
                        llJoin.setVisibility(View.GONE);
                    }


                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
//                dismissProgressDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        }
    }
}
