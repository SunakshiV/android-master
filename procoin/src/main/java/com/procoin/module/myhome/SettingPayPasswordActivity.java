package com.procoin.module.myhome;

import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.entity.ResultData;
import com.procoin.util.MyCallBack;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.transactionpassword.GridPasswordView;
import com.procoin.http.model.User;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.MD5;
import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 设置支付密码，或者修改支付密码
 */
public class SettingPayPasswordActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvStep1)
    TextView tvStep1;
    @BindView(R.id.ivStep1)
    ImageView ivStep1;
    @BindView(R.id.tvStep2)
    TextView tvStep2;
    @BindView(R.id.ivStep2)
    ImageView ivStep2;
    @BindView(R.id.tvStep3)
    TextView tvStep3;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.tv_verify)
    TextView tvVerify;
    @BindView(R.id.llModifyPayPass)
    LinearLayout llModifyPayPass;
    @BindView(R.id.tvNext1)
    TextView tvNext1;
    @BindView(R.id.llStep1)
    LinearLayout llStep1;
    @BindView(R.id.gpdPayPass)
    GridPasswordView gpdPayPass;
    @BindView(R.id.tvNext2)
    TextView tvNext2;
    @BindView(R.id.llStep2)
    LinearLayout llStep2;
    @BindView(R.id.gpdPayPassAgain)
    GridPasswordView gpdPayPassAgain;
    @BindView(R.id.tvComplete)
    TextView tvComplete;
    @BindView(R.id.llStep3)
    LinearLayout llStep3;

    private Call<ResponseBody> smsCall;
    private Call<ResponseBody> checkIdentityCall;
    private Call<ResponseBody> setPayPassCall;
    private CountDownTimer countDownTimer;

    private User user;
    private String dragImgKey = "";
    private int locationx = 0;
    private String configPayPass = "";
    private String payPass = "";
    private String smsCode = "";

    @Override
    protected int setLayoutId() {
        return R.layout.modify_pay_password;
    }

    @Override
    protected String getActivityTitle() {
        user = getApplicationContext().getUser();
        if (user != null && !TextUtils.isEmpty(user.getPayPass())) {
            return getString(R.string.modifyPayPassword);
        } else {
            return getString(R.string.setPayPassword);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setStep(1);
        tvNext1.setOnClickListener(this);
        tvNext2.setOnClickListener(this);
        tvVerify.setOnClickListener(this);
        tvComplete.setOnClickListener(this);
        if (user == null) user = getApplicationContext().getUser();
        tvAccount.setText(formatPhone(user.phone));
        etVerify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                tvNext1.setEnabled(!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() >= 4);
            }
        });
        gpdPayPass.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                payPass = MD5.getMessageDigest(psw);
                tvNext2.setEnabled(true);
            }
        });
        gpdPayPassAgain.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
            }

            @Override
            public void onInputFinish(String psw) {
                configPayPass = MD5.getMessageDigest(psw);
                tvComplete.setEnabled(true);
            }
        });


    }

    private String formatPhone(String phone) {
        Log.d("formatPhone", "phone==" + phone);
        if (phone != null && phone.length() > 10) {
            StringBuilder sb = new StringBuilder(phone);
            phone = sb.replace(3, 7, "****").toString();
            Log.d("formatPhone", "phone==" + phone);
        }
        return phone;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNext1:
                checkIdentity(dragImgKey, locationx);
                break;
            case R.id.tvNext2:
                setStep(3);
                break;
            case R.id.tv_verify:
                startGetSms("", 0);
                break;
            case R.id.tvComplete:
                setPayPass(dragImgKey, locationx);
                break;
        }
    }

    private void setStep(int step) {
        if (step == 1) {
            tvStep1.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep1.setImageResource(R.drawable.ic_pay_pass_2);

            tvStep2.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            ivStep2.setImageResource(R.drawable.ic_pay_pass_3);

            tvStep3.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            llStep1.setVisibility(View.VISIBLE);
            llStep2.setVisibility(View.GONE);
            llStep3.setVisibility(View.GONE);


            tvNext1.setEnabled(false);
        } else if (step == 2) {
            tvStep1.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep1.setImageResource(R.drawable.ic_pay_pass_1);

            tvStep2.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep2.setImageResource(R.drawable.ic_pay_pass_2);

            tvStep3.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            llStep1.setVisibility(View.GONE);
            llStep2.setVisibility(View.VISIBLE);
            llStep3.setVisibility(View.GONE);
            gpdPayPass.forceInputViewGetFocus();
            tvNext2.setEnabled(false);
        } else if (step == 3) {
            tvStep1.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep1.setImageResource(R.drawable.ic_pay_pass_1);

            tvStep2.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep2.setImageResource(R.drawable.ic_pay_pass_1);

            tvStep3.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));

            llStep1.setVisibility(View.GONE);
            llStep2.setVisibility(View.GONE);
            llStep3.setVisibility(View.VISIBLE);

            gpdPayPassAgain.forceInputViewGetFocus();
            tvComplete.setEnabled(false);
        }


    }


    public void startGetSms(String dragImgKey, int locationx) {
        CommonUtil.cancelCall(smsCall);
        smsCall = VHttpServiceManager.getInstance().getVService().getSms(user.getPhone(), user.getCountryCode(), dragImgKey, locationx);
        smsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage("短信验证码已经发送至" + formatPhone(user.getPhone()), SettingPayPasswordActivity.this);
                    Counting();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                SettingPayPasswordActivity.this.dragImgKey = dragImgKey;
                SettingPayPasswordActivity.this.locationx = locationx;
                startGetSms(dragImgKey, locationx);
            }
        });
    }

    public void checkIdentity(String dragImgKey, int locationx) {
        smsCode = etVerify.getText().toString().trim();
        CommonUtil.cancelCall(checkIdentityCall);
        checkIdentityCall = VHttpServiceManager.getInstance().getVService().checkIdentity(user.getPhone(), smsCode, dragImgKey, locationx);
        checkIdentityCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, SettingPayPasswordActivity.this);
                    setStep(2);
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                SettingPayPasswordActivity.this.dragImgKey = dragImgKey;
                SettingPayPasswordActivity.this.locationx = locationx;
                checkIdentity(dragImgKey, locationx);
            }
        });
    }

    public void setPayPass(String dragImgKey, int locationx) {
        CommonUtil.cancelCall(setPayPassCall);
        showProgressDialog();
        setPayPassCall = VHttpServiceManager.getInstance().getVService().setPayPass(user.getPhone(), smsCode, dragImgKey, locationx, payPass, configPayPass);
        setPayPassCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, SettingPayPasswordActivity.this);
                    PageJumpUtil.finishCurr(SettingPayPasswordActivity.this);
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                SettingPayPasswordActivity.this.dragImgKey = dragImgKey;
                SettingPayPasswordActivity.this.locationx = locationx;
                setPayPass(dragImgKey, locationx);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


    private void Counting() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                tvVerify.setEnabled(false);
                tvVerify.setText("剩余" + l / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                tvVerify.setEnabled(true);
                tvVerify.setText("重新获取");
            }
        };
        countDownTimer.start();
    }

}
