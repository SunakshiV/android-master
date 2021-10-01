package com.procoin.module.myhome;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.photo.ViewPagerPhotoViewActivity;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.copy.CropyOrderInfoActivity;
import com.procoin.module.copy.dialog.CopyOrderDialogFragment;
import com.procoin.module.home.RadarDetailsActivity;
import com.procoin.module.home.entity.SubUser;
import com.procoin.module.home.fragment.ShareDialogFragment;
import com.procoin.module.login.LoginActivity;
import com.procoin.module.myhome.dialog.AttentionDialogFragment;
import com.procoin.module.myhome.fragment.TrendFragment;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.CircleImageView;
import com.procoin.widgets.RadarView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 个人主页
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class UserHomeActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.ivhead)
    CircleImageView ivhead;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvUserBrief)
    TextView tvUserBrief;

    @BindView(R.id.llBtn)
    LinearLayout llBtn;
    @BindView(R.id.tvFollow)
    TextView tvFollow;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvGrade)
    TextView tvGrade;
    @BindView(R.id.llCopy)
    TextView llCopy;
    @BindView(R.id.rdv)
    RadarView rdv;
    @BindView(R.id.tvA)
    TextView tvA;
    @BindView(R.id.tvB)
    TextView tvB;
    @BindView(R.id.tvC)
    TextView tvC;
    @BindView(R.id.tvD)
    TextView tvD;
    @BindView(R.id.tvE)
    TextView tvE;
    @BindView(R.id.tvTab1)
    TextView tvTab1;
    @BindView(R.id.tvTab2)
    TextView tvTab2;
    @BindView(R.id.tvTab3)
    TextView tvTab3;
    @BindView(R.id.llRadar)
    LinearLayout llRadar;

    @BindView(R.id.llShare)
    LinearLayout llShare;
    @BindView(R.id.tvId)
    TextView tvId;
    @BindView(R.id.tvDays)
    TextView tvDays;
    @BindView(R.id.tvAttentionNum)
    TextView tvAttentionNum;

    @BindView(R.id.tvRadarDetails)
    TextView tvRadarDetails;

    //    @BindView(R.id.userHeadList)
//    UserHeadHorizontalList userHeadList;
    @BindView(R.id.tvCopyCount)
    TextView tvCopyCount;
    @BindView(R.id.ivRating1)
    ImageView ivRating1;
    @BindView(R.id.ivRating2)
    ImageView ivRating2;
    @BindView(R.id.ivRating3)
    ImageView ivRating3;
    @BindView(R.id.ivRating4)
    ImageView ivRating4;
    @BindView(R.id.ivRating5)
    ImageView ivRating5;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;
    @BindView(R.id.llPie)
    LinearLayout llPie;
    @BindView(R.id.tvAccuracyRate)
    TextView tvAccuracyRate;
    @BindView(R.id.tvTotalProfit)
    TextView tvTotalProfit;
    @BindView(R.id.tvLastMonthProfit)
    TextView tvLastMonthProfit;
    @BindView(R.id.tvRecommend)
    TextView tvRecommend;


    private Call<ResponseBody> personalHomeCall;
    private SubUser subUser;
    private long tarUserId;

    @BindView(R.id.vp_content)
    ViewPager vpContent;
    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private Call<ResponseBody> followAddCall;
    private Call<ResponseBody> followCancelCall;
    private Call<ResponseBody> copySlaveOrderCall;
    private Call<ResponseBody> unBindViewCall;
    private Call<ResponseBody> applyForFollowCall;

    private boolean isRequest;

    private MyPagerAdapter adapter;

//    private long copyOrderId;//>0代表已经跟单


    public static void pageJump(Context context, long userId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.USERID, userId);
        PageJumpUtil.pageJump(context, UserHomeActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.user_home;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.my_cropyme);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.USERID)) {
                tarUserId = bundle.getLong(CommonConst.USERID, 0);
            }
        }

        if (tarUserId == 0) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        tjrImageLoaderUtil = new TjrImageLoaderUtil();

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(adapter);
        vpContent.setOffscreenPageLimit(adapter.getCount() - 1);
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                Log.d("slide", "onPageSelected  i==" + i);
                slideTab(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        tvTab1.setOnClickListener(this);
        tvTab2.setOnClickListener(this);
        tvTab3.setOnClickListener(this);
        llRadar.setOnClickListener(this);
        tvRadarDetails.setOnClickListener(this);
        llShare.setOnClickListener(this);


        slideTab(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        startPersonalHome();
    }

    private void startPersonalHome() {
        CommonUtil.cancelCall(personalHomeCall);
        personalHomeCall = VHttpServiceManager.getInstance().getVService().personalHome(tarUserId);
        personalHomeCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    subUser = resultData.getObject("userRadar", SubUser.class);
                    setData();
                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

//    private void setAttentionData() {
//        if (subUser != null) {
//            if (subUser.myIsAttention == 1) {
////                tvFollow.setSelected(false);
//                tvFollow.setText("已关注");
//            } else {
////                tvFollow.setSelected(true);
//                tvFollow.setText("关注");
//            }
//        }
//    }

    private void setFollowData() {//申请绑定
        if (subUser != null) {
            if (subUser.myIsFollow == 1) {
                llCopy.setText("已绑定");
            } else {
                llCopy.setText("申请绑定");
            }
        }
    }

    private void setData() {
        if (subUser != null) {

            tvName.setText(subUser.userName);
            tvId.setText("ID: " + String.valueOf(subUser.userId));
            tvDays.setText("已入驻 " + subUser.days + "天");
            tjrImageLoaderUtil.displayImageForHead(subUser.headUrl, ivhead);
            ivhead.setOnClickListener(this);
            tvUserBrief.setText(subUser.describes);

            tvAccuracyRate.setText(subUser.correctRate + "%");
            tvTotalProfit.setText(String.valueOf(subUser.totalProfit));
            tvLastMonthProfit.setText(String.valueOf(subUser.monthProfit));

            if (!TextUtils.isEmpty(subUser.recommend)) {
                tvRecommend.setVisibility(View.VISIBLE);
//                tvRecommend.setText(CommonUtil.fromHtml(subUser.recommend));
                tvRecommend.setText(subUser.recommend);
            } else {
                tvRecommend.setVisibility(View.GONE);
            }
//            tvGrade.setText(subUser.score);
            if (subUser.userId == getUserIdLong()) {
                llBtn.setVisibility(View.GONE);
            } else {
                llBtn.setVisibility(View.VISIBLE);
                if (subUser.myIsAttention == 0) {
                    if(subUser.subIsFee==0){
                        tvFollow.setText("免费订阅");
                    }else{
                        tvFollow.setText("订阅");
                    }
                    tvTime.setVisibility(View.GONE);
                } else {
                    if(subUser.subIsFee==0){
                        tvFollow.setText("已订阅");
                        tvTime.setVisibility(View.GONE);
                    }else{
                        tvFollow.setText("续费");
                        tvTime.setVisibility(View.VISIBLE);
                        if (subUser.isExpireTime == 1 && subUser.expireTime > 0) {//底部文字：订阅过期
                            tvTime.setText("订阅已过期");
                            tvTime.setTextColor(ContextCompat.getColor(this, R.color.red));
                        } else if (subUser.isExpireTime == 0 && subUser.expireTime > 0) {//底部文字：到期：2020-12-12
                            tvTime.setText("到期：" + DateUtils.getStringDateOfString2(String.valueOf(subUser.expireTime), DateUtils.TEMPLATE_yyyyMMdd_divide));
                            tvTime.setTextColor(ContextCompat.getColor(this, R.color.c1d3155));
                        }
                    }

                }
                tvFollow.setOnClickListener(this);
                llCopy.setOnClickListener(this);

            }
            tvAttentionNum.setText(String.valueOf(subUser.attentionNum));
            tvCopyCount.setText(String.valueOf(subUser.radarFollowNum));

//            setAttentionData();
            setFollowData();


            List<Double> data = new ArrayList<>();
            data.add(subUser.radarFollowBalanceWeight);
            data.add(subUser.radarProfitRateWeight);
            data.add(subUser.radarFollowProfitRateWeight);
            data.add(subUser.radarFollowWinRateWeight);
            data.add(subUser.radarFollowNumWeight);
            rdv.setData(data);


            ArrayList<String> titles = new ArrayList<>();
            titles.add("跟单盈利额\n");
            titles.add("盈利能力\n");
            titles.add("跟单收益率\n");
            titles.add("跟单胜率\n");
            titles.add("人气指数\n");

            ArrayList<String> titleValues = new ArrayList<>();
            titleValues.add(String.valueOf(subUser.radarFollowBalance));
            titleValues.add(subUser.radarProfitRate + "%");
            titleValues.add(subUser.radarFollowProfitRate + "%");
            titleValues.add(subUser.radarFollowWinRate + "%");
            titleValues.add(String.valueOf(subUser.radarFollowNum));


            rdv.setTitles(titles, titleValues);


        }
    }


    private void setRatingBarData(String score) {
        int rating = (int) (Double.parseDouble(score) / 20);
        if (rating == 0) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 1) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 2) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 3) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_off);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 4) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_off);
        } else if (rating == 5) {
            ivRating1.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating2.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating3.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating4.setImageResource(R.drawable.ic_svg_ratingbar_on);
            ivRating5.setImageResource(R.drawable.ic_svg_ratingbar_on);
        }
    }
    AttentionDialogFragment attentionDialogFragment;

    public void showAttentionDialogFragment(String subFeeTypeName, String subFeeTypeUnit, String subNotice, double subFee,int myIsAttention,int isExpireTime,long expireTime) {
        attentionDialogFragment = AttentionDialogFragment.newInstance(subFeeTypeName,subFeeTypeUnit,subNotice,subFee,myIsAttention,isExpireTime,expireTime);
        attentionDialogFragment.setAttentionListen(new AttentionDialogFragment.AttentionListen() {
            @Override
            public void addAttention(String num) {
                startFollowAdd(num);
            }
        });
        attentionDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvFollow:
                if (isLogin()) {
                    if (subUser == null) return;

                    if(subUser.subIsFee==0){//免费的
                        if(subUser.myIsAttention==0){
                            showSubFeeDialog();
                        }else{
                            CommonUtil.showmessage("你已经订阅",UserHomeActivity.this);
                        }
                    }else{
                        showAttentionDialogFragment(subUser.subFeeTypeName,subUser.subFeeTypeUnit,subUser.subNotice,subUser.subFee,subUser.myIsAttention,subUser.isExpireTime,subUser.expireTime);
                    }

//                    showAttentionDialogFragment(subUser.subFeeTypeName,subUser.subFeeTypeUnit,subUser.subNotice,subUser.subFee,subUser.myIsAttention,subUser.isExpireTime,subUser.expireTime);
//                    if (subUser.myIsAttention == 1) {
////                        startFollowCancel();
//                    } else {
//                        startFollowAdd();
//                    }
                } else {
                    LoginActivity.login(UserHomeActivity.this);
                }

                break;
            case R.id.tvTab1:
                if (vpContent.getCurrentItem() != 0) {
                    vpContent.setCurrentItem(0, false);
                }
                break;
            case R.id.tvTab2:
                if (vpContent.getCurrentItem() != 1) {
                    vpContent.setCurrentItem(1, false);
                }
                break;
            case R.id.tvTab3:
                if (vpContent.getCurrentItem() != 2) {
                    vpContent.setCurrentItem(2, false);
                }
                break;
            case R.id.llCopy:
                if (isLogin()) {
                    if (subUser == null) return;
                    if (subUser.myIsFollow == 1) {
                        startUnBindViewCall(subUser.userId);
                    } else {
                        showCopyTipsDialog(subUser.followNotice);
                    }
                } else {
                    LoginActivity.login(UserHomeActivity.this);
                }

                break;
            case R.id.tvRadarDetails:
            case R.id.llRadar:
                if (subUser != null) {
                    getApplicationContext().subUser = subUser;
                }
                PageJumpUtil.pageJump(UserHomeActivity.this, RadarDetailsActivity.class);
                break;
            case R.id.llShare:
                if (isLogin()) {
                    if (TextUtils.isEmpty(shareUrl)) {
                        startGetShareCall();
                    } else {
                        showShareDialogFragment();
                    }
                } else {
                    LoginActivity.login(UserHomeActivity.this);
                }
                break;
            case R.id.ivhead:
                if (subUser != null && !TextUtils.isEmpty(subUser.headUrl)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConst.PAGETYPE, 0);
                    bundle.putString(CommonConst.SINGLEPICSTRING, subUser.headUrl);
                    PageJumpUtil.pageJumpToData(UserHomeActivity.this, ViewPagerPhotoViewActivity.class, bundle);
                }
                break;
        }
    }



    TjrBaseDialog copyTipsDialog;

    private void showCopyTipsDialog(String tips) {
        copyTipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                if(subUser!=null)startApplyForFollowCallCall(subUser.userId);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        copyTipsDialog.setTvTitle("申请绑定须知");
        copyTipsDialog.setMessage(tips);
        copyTipsDialog.show();
    }



    TjrBaseDialog subFeeDialog;//免费订阅提示

    private void showSubFeeDialog() {
        subFeeDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startFollowAdd("0");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        subFeeDialog.setTvTitle("提示");
        subFeeDialog.setMessage("是否订阅该用户?");
        subFeeDialog.setBtnOkText("订阅");
        subFeeDialog.show();
    }


    CopyOrderDialogFragment copyOrderDialogFragment;

    private void showCopyOrderDialogFragment() {
        copyOrderDialogFragment = CopyOrderDialogFragment.newInstance(tarUserId, 0);
        copyOrderDialogFragment.setOnShareDialogCallBack(new CopyOrderDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                copyOrderDialogFragment.dismiss();
            }

            @Override
            public void onSubmit(double cash) {
                startCopySlaveOrder(cash);
            }
        });
        copyOrderDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


    private void startCopySlaveOrder(double cash) {
        CommonUtil.cancelCall(copySlaveOrderCall);
        copySlaveOrderCall = VHttpServiceManager.getInstance().getVService().copySlaveOrder(tarUserId, String.valueOf(cash), "", 0, 0);
        copySlaveOrderCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, UserHomeActivity.this);
                    if (copyOrderDialogFragment != null) {
                        copyOrderDialogFragment.dismiss();
                    }
                    String orderId = resultData.getItem("orderId", String.class);
                    if (!TextUtils.isEmpty(orderId) && Long.parseLong(orderId) > 0) {
                        CropyOrderInfoActivity.pageJump(UserHomeActivity.this, Long.parseLong(orderId));

                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }


    ShareDialogFragment shareDialogFragment;


    private Call<ResponseBody> getShareCall;
    private String shareUrl;

    private void startGetShareCall() {
        CommonUtil.cancelCall(getShareCall);
        getShareCall = VHttpServiceManager.getInstance().getVService().getshareinfo(1, "");
        getShareCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    shareUrl = resultData.getItem("shareUrl", String.class);
                    showShareDialogFragment();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }

    private void showShareDialogFragment() {
        if (subUser == null) return;
        shareDialogFragment = ShareDialogFragment.newInstance(new User(subUser.userId, subUser.userName, subUser.headUrl), shareUrl);
        shareDialogFragment.setOnShareDialogCallBack(new ShareDialogFragment.OnShareDialogCallBack() {
            @Override
            public void onDialogDismiss() {
                shareDialogFragment.dismiss();
            }
        });
        shareDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


    private void slideTab(int arg0) {
//        currentTab = arg0;
        switch (arg0) {
            case 0:
                tvTab1.setSelected(true);
                tvTab2.setSelected(false);
                tvTab3.setSelected(false);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case 1:
                tvTab1.setSelected(false);
                tvTab2.setSelected(true);
                tvTab3.setSelected(false);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                break;
            case 2:
                tvTab1.setSelected(false);
                tvTab2.setSelected(false);
                tvTab3.setSelected(true);
                tvTab1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                tvTab3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                break;
        }
    }


    private void startFollowAdd(String num) {
        if (isRequest) return;
        isRequest = true;
        CommonUtil.cancelCall(followAddCall);
        followAddCall = VHttpServiceManager.getInstance().getVService().followAdd(tarUserId,num);
        followAddCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                isRequest = false;
                if (resultData.isSuccess()) {
                    subUser.myIsAttention = 1;
                    CommonUtil.showmessage(resultData.msg,UserHomeActivity.this);
                    if(attentionDialogFragment!=null)attentionDialogFragment.dismiss();
                    startPersonalHome();
//                    setAttentionData();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                isRequest = false;
            }
        });
    }

//    private void startFollowCancel() {
//        if (isRequest) return;
//        isRequest = true;
//        CommonUtil.cancelCall(followCancelCall);
//        followCancelCall = VHttpServiceManager.getInstance().getVService().followCancel(tarUserId);
//        followCancelCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                isRequest = false;
//                if (resultData.isSuccess()) {
//                    subUser.myIsAttention = 0;
//                    setAttentionData();
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                isRequest = false;
//            }
//        });
//    }


    private void startUnBindViewCall(long userId) {
        if (isRequest) return;
        isRequest = true;
        com.procoin.http.util.CommonUtil.cancelCall(unBindViewCall);
        unBindViewCall = VHttpServiceManager.getInstance().getVService().unBind(userId);
        unBindViewCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                isRequest = false;
                if (resultData.isSuccess()) {
                    com.procoin.http.util.CommonUtil.showmessage(resultData.msg, UserHomeActivity.this);
                    if (subUser != null) {
                        subUser.myIsFollow = 0;
                        setFollowData();
                    }

                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                isRequest = false;
            }
        });
    }

    private void startApplyForFollowCallCall(long userId) {
        if (isRequest) return;
        isRequest = true;
        com.procoin.http.util.CommonUtil.cancelCall(applyForFollowCall);
        applyForFollowCall = VHttpServiceManager.getInstance().getVService().applyForFollow(userId);
        applyForFollowCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                isRequest = false;
                if (resultData.isSuccess()) {
                    com.procoin.http.util.CommonUtil.showmessage(resultData.msg, UserHomeActivity.this);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                isRequest = false;
            }
        });
    }


    class MyPagerAdapter extends FragmentPagerAdapter {


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return 3;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            return position == 0 ? "　出售中　" : "　未上架　";
//        }

        @Override
        public Fragment getItem(int i) {
            return TrendFragment.newInstance(tarUserId, i + 1);

        }
    }

}
