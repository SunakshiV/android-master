package com.procoin.module.legal;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.legal.entity.OtcCertification;
import com.procoin.module.myhome.IdentityAuthenActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

public class AuthenticationActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvAuthenState)
    TextView tvAuthenState;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvCardNo)
    TextView tvCardNo;
    @BindView(R.id.tvBond)
    TextView tvBond;
    @BindView(R.id.llCb)
    LinearLayout llCb;
    @BindView(R.id.cbSign)
    CheckBox cbSign;
    @BindView(R.id.tvProtocol)
    TextView tvProtocol;
    @BindView(R.id.tvApply)
    TextView tvApply;
    @BindView(R.id.tvApplyCancel)
    TextView tvApplyCancel;
    @BindView(R.id.tvReason)
    TextView tvReason;
    @BindView(R.id.llReason)
    LinearLayout llReason;

    private Handler handler = new Handler();


    private Call<ResponseBody> otcGetCertificationInfoCall;
    private Call<ResponseBody> otcAuthenticateCall;
    private Call<ResponseBody> otcApplyForCancellationCall;


    @Override
    protected int setLayoutId() {
        return R.layout.authentication;
    }

    @Override
    protected String getActivityTitle() {
        return "商家认证";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);


        cbSign.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                tvApply.setEnabled(isChecked);
            }
        });

        tvApply.setOnClickListener(this);
        tvApplyCancel.setOnClickListener(this);

        startGetotcCertificationInfoCall();
    }


    TjrBaseDialog TipsDialog;

    private void showTipsDialog() {
        TipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                PageJumpUtil.pageJump(AuthenticationActivity.this, IdentityAuthenActivity.class);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        PageJumpUtil.finishCurr(AuthenticationActivity.this);
                    }
                }, 500);
            }

            @Override
            public void onclickClose() {
                dismiss();
                PageJumpUtil.finishCurr(AuthenticationActivity.this);
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        TipsDialog.setTvTitle("未实名不能申请商家认证");
        TipsDialog.setMessage("未实名不能申请商家认证，请实名认证后，在提交商家认证申请。");
        TipsDialog.setBtnOkText("前往实名");
        TipsDialog.setBtnColseText("取消");
        TipsDialog.show();
    }

    private void setProtocol(String bond) {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString normalText = new SpannableString("同意冻结");
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#3d3a50")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString normalText2 = new SpannableString(bond + " USDT");
        normalText2.setSpan(new ForegroundColorSpan(Color.parseColor("#6175ae")), 0, normalText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString normalText3 = new SpannableString("作为广告方保证资产，且同意");
        normalText3.setSpan(new ForegroundColorSpan(Color.parseColor("#3d3a50")), 0, normalText3.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString clickText = new SpannableString("《商家服务协议》");
        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#6175ae")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(AuthenticationActivity.this, CommonConst.USER_PROTOCOL);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(AuthenticationActivity.this, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(normalText2);
        spannableStringBuilder.append(normalText3);
        spannableStringBuilder.append(clickText);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);
    }


    private void startGetotcCertificationInfoCall() {
        CommonUtil.cancelCall(otcGetCertificationInfoCall);
        otcGetCertificationInfoCall = VHttpServiceManager.getInstance().getVService().otcGetCertificationInfo();
        otcGetCertificationInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    OtcCertification otcCertification = resultData.getObject("otcCertification", OtcCertification.class);
                    if (otcCertification.idCertify == 0) {
                        tvAuthenState.setText("未实名");
                        tvApply.setVisibility(View.GONE);
                        tvApplyCancel.setVisibility(View.GONE);
                        llCb.setVisibility(View.GONE);

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                showTipsDialog();
                            }
                        }, 500);

                    } else {
                        tvAuthenState.setText("已实名");
                        setState(otcCertification);
                        setProtocol(otcCertification.securityDeposit);

                        if (!TextUtils.isEmpty(otcCertification.realName)) {
                            tvName.setText(otcCertification.realName);
                        }
                        if (!TextUtils.isEmpty(otcCertification.certNo)) {
                            tvCardNo.setText(otcCertification.certNo);
                        }
                        if (!TextUtils.isEmpty(otcCertification.securityDeposit)) {
                            tvBond.setText(otcCertification.securityDeposit + " USDT");
                        }

                    }


                }
            }

        });
    }


    private void startOtcAuthenticateCall() {
        CommonUtil.cancelCall(otcAuthenticateCall);
        otcAuthenticateCall = VHttpServiceManager.getInstance().getVService().otcAuthenticate();
        otcAuthenticateCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AuthenticationActivity.this);
                    startGetotcCertificationInfoCall();
                }
            }

        });
    }


    private void startOtcApplyForCancellationCall() {
        CommonUtil.cancelCall(otcApplyForCancellationCall);
        otcApplyForCancellationCall = VHttpServiceManager.getInstance().getVService().otcApplyForCancellation();
        otcApplyForCancellationCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AuthenticationActivity.this);
                    startGetotcCertificationInfoCall();
                }
            }

        });
    }

    //0未认证，1认证中，2认证通过，3申请取消认证，4取消认证通过，-1认证失败
    private String setState(OtcCertification otcCertification) {
        switch (otcCertification.state) {
            case 0:
            case 4:
            case -1:
                tvApply.setVisibility(View.VISIBLE);
                tvApplyCancel.setVisibility(View.GONE);
                tvApply.setText("申请商家认证");
                llCb.setVisibility(View.VISIBLE);
                tvApply.setEnabled(cbSign.isChecked());
                if (otcCertification.state == -1 && !TextUtils.isEmpty(otcCertification.reason)) {
                    llReason.setVisibility(View.VISIBLE);
                    tvReason.setText(otcCertification.reason);
                }
                break;
            case 1:
                llReason.setVisibility(View.GONE);
                tvApply.setVisibility(View.VISIBLE);
                tvApplyCancel.setVisibility(View.GONE);
                tvApply.setText("认证中");
                llCb.setVisibility(View.GONE);
                tvApply.setEnabled(false);
                break;
            case 2:
                llReason.setVisibility(View.GONE);
                tvApply.setVisibility(View.GONE);
                tvApplyCancel.setVisibility(View.VISIBLE);
                tvApplyCancel.setText("申请取消商家认证");
                llCb.setVisibility(View.GONE);
                tvApplyCancel.setEnabled(true);
                break;
            case 3:
                llReason.setVisibility(View.GONE);
                tvApply.setVisibility(View.GONE);
                tvApplyCancel.setVisibility(View.VISIBLE);
                tvApplyCancel.setText("正在申请取消商家认证");
                llCb.setVisibility(View.GONE);
                tvApplyCancel.setEnabled(false);
                break;
            default:
        }
        return "";
    }

    TjrBaseDialog cancelDialog;

    private void showCancelDialog() {
        cancelDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                startOtcApplyForCancellationCall();
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        cancelDialog.setMessage("确定申请取消商家认证？");
        cancelDialog.setBtnOkText("确定");
        cancelDialog.setBtnColseText("取消");
        cancelDialog.setTitleVisibility(View.GONE);
        cancelDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvApply:
                startOtcAuthenticateCall();
                break;
            case R.id.tvApplyCancel:
                showCancelDialog();
                break;
        }
    }
}
