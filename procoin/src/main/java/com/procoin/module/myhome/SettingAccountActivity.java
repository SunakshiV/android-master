package com.procoin.module.myhome;

import android.content.Intent;
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

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.module.login.AllCountryCodeListActivity;
import com.procoin.module.login.LoginActivity;
import com.procoin.module.logout.LogoutClearUser;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;

/**
 * Created by zhengmj on 18-11-27.
 */

public class SettingAccountActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
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
    @BindView(R.id.et_newPhone)
    EditText et_newPhone;
    @BindView(R.id.et_newSms)
    EditText et_newSms;
    @BindView(R.id.tv_verify)
    TextView tvVerify;
    @BindView(R.id.tv_newVerfiy)
    TextView tv_newVerfiy;

    @BindView(R.id.tvNext1)
    TextView tvNext1;
    @BindView(R.id.llStep1)
    LinearLayout llStep1;
    @BindView(R.id.tvNext2)
    TextView tvNext2;
    @BindView(R.id.llStep2)
    LinearLayout llStep2;
    @BindView(R.id.tvCountryName)
    TextView tvCountryName;
    @BindView(R.id.tvCountryCode)
    TextView tvCountryCode;
    //    @BindView(R.id.tvComplete)
//    TextView tvComplete;
//    @BindView(R.id.llStep3)
//    LinearLayout llStep3;
    private Call<ResponseBody> checkIdentityCall;
    private Call<ResponseBody> smsCall;
    private CountDownTimer countDownTimer;
    private Call<ResponseBody> changeCall;
    private String oldSms;
    private boolean npOK;
    private boolean nsOK;
    private int currentState;
    private TjrBaseDialog clearUserDialog;
    private Call<ResponseBody> loginOutCall;

    @Override
    protected int setLayoutId() {
        return R.layout.modify_account_phone;
    }

    @Override
    protected String getActivityTitle() {
        return "更换手机号码";
    }

    private User user;
    private String dragImgKey = "";
    private int locationx = 0;
    private String configPayPass = "";
    private String payPass = "";
    private String smsCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setStep(1);
        tvNext1.setOnClickListener(this);
        tvNext2.setOnClickListener(this);
        tvVerify.setOnClickListener(this);
        tv_newVerfiy.setOnClickListener(this);
        tvCountryName.setOnClickListener(this);
        tvCountryCode.setOnClickListener(this);
        tvCountryCode.setText("+86");//默认中国
        tvCountryName.setText("中国");//默认中国
//        tvComplete.setOnClickListener(this);
        if (user == null) user = getApplicationContext().getUser();
        tvAccount.setText(formatPhone(user.phone));
        et_newPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(et_newPhone.getText()) ) {//&& et_newPhone.getText().length() == 11
                    npOK = true;
                } else {
                    npOK = false;
                }
                tvNext2.setEnabled(npOK && nsOK);
            }
        });
        et_newSms.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(et_newSms.getText()) && et_newSms.getText().length() == 6) {
                    nsOK = true;
                } else {
                    nsOK = false;
                }
                tvNext2.setEnabled(npOK && nsOK);
            }
        });
    }

    private void setStep(int step) {
        if (step == 1) {
            currentState = 1;
            tvStep1.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep1.setImageResource(R.drawable.ic_pay_pass_2);

            tvStep2.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            ivStep2.setImageResource(R.drawable.ic_pay_pass_3);

            tvStep3.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

            llStep1.setVisibility(View.VISIBLE);
            llStep2.setVisibility(View.GONE);
//            llStep3.setVisibility(View.GONE);


            tvNext1.setEnabled(true);
        } else if (step == 2) {
            if (countDownTimer != null) {
                countDownTimer.cancel();
                tv_newVerfiy.setText("获取验证码");
                tv_newVerfiy.setEnabled(true);
            }
            currentState = 2;
            tvStep1.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep1.setImageResource(R.drawable.ic_pay_pass_1);

            tvStep2.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
            ivStep2.setImageResource(R.drawable.ic_pay_pass_2);

            tvStep3.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            llStep1.setVisibility(View.GONE);
            llStep2.setVisibility(View.VISIBLE);
//            llStep3.setVisibility(View.GONE);
            tvNext2.setEnabled(false);
        } else if (step == 3) {
//                    CommonUtil.cancelAsyncTask(logOutTask);
//                    logOutTask = (LogOutTask) new LogOutTask().executeParams();
            CommonUtil.cancelCall(loginOutCall);
            loginOutCall = VHttpServiceManager.getInstance().getVService().doLogout();
            loginOutCall.enqueue(new MyCallBack(SettingAccountActivity.this) {
                @Override
                protected void callBack(ResultData resultData) {
                    if (resultData.isSuccess()) {
                        dismissProgressDialog();
                        if (!TextUtils.isEmpty(resultData.msg))
                            CommonUtil.showmessage("请重新登录", SettingAccountActivity.this);
                        (new LogoutClearUser()).logoutClearUser(SettingAccountActivity.this, LoginActivity.class);
                        PageJumpUtil.finishCurr(SettingAccountActivity.this);
                    }
                }
            });
//            tvStep1.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
//            ivStep1.setImageResource(R.drawable.ic_pay_pass_1);
//
//            tvStep2.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
//            ivStep2.setImageResource(R.drawable.ic_pay_pass_1);
//
//            tvStep3.setBackgroundColor(ContextCompat.getColor(this, R.color.ceeeeee));
//
//            llStep1.setVisibility(View.GONE);
//            llStep2.setVisibility(View.GONE);
//            llStep3.setVisibility(View.VISIBLE);
//
//            tvComplete.setEnabled(false);
        }


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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 0x456) {
            if (data != null) {
                Bundle bundle = data.getExtras();
                String code = bundle.getString("code");
                String name = bundle.getString("name");

                if (!TextUtils.isEmpty(code)) {
                    tvCountryCode.setText(code);
                }
                if (!TextUtils.isEmpty(name)) {
                    tvCountryName.setText(name);
                }
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvNext1:
                checkIdentity("", 0);
//                setStep(2);
                break;
            case R.id.tvNext2:
                changePhone("", 0);
//                setStep(3);
//                startGetSms("", 0);
                break;
            case R.id.tv_verify:
                startGetSms("", 0);
                break;
            case R.id.tv_newVerfiy:
                startGetSms("", 0);
                break;
            case R.id.tvCountryCode:
            case R.id.tvCountryName:
                PageJumpUtil.pageJumpResult(SettingAccountActivity.this, AllCountryCodeListActivity.class, new Intent(), 0x123);
                break;
//            case R.id.tvComplete:
////                setPayPass();
//                break;
        }
    }

    public void checkIdentity(String dragImgKey, int locationx) {
        this.dragImgKey = dragImgKey;
        this.locationx = locationx;
        smsCode = etVerify.getText().toString().trim();
        CommonUtil.cancelCall(checkIdentityCall);
        checkIdentityCall = VHttpServiceManager.getInstance().getVService().checkIdentity(user.getPhone(), smsCode, dragImgKey, locationx);
        checkIdentityCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, SettingAccountActivity.this);
                    setStep(2);
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                checkIdentity(dragImgKey,locationx);
            }
        });
    }

    public void changePhone(String key, int location) {
        CommonUtil.cancelCall(changeCall);
        changeCall = VHttpServiceManager.getInstance().getVService().changePhone(
                tvCountryCode.getText().toString(),
                et_newPhone.getText().toString(),
                et_newSms.getText().toString(),
                user.getPhone(),
                oldSms,
                key,
                location);
        changeCall.enqueue(new MyCallBack(SettingAccountActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, SettingAccountActivity.this);
                    setStep(3);
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                changePhone(dragImgKey, locationx);
            }
        });
    }

    public void startGetSms(String dragImgKey, int locationx) {
        if (currentState == 2 && TextUtils.isEmpty(et_newPhone.getText())) {
            CommonUtil.showmessage("请输入手机", SettingAccountActivity.this);
            return;
        }
        this.dragImgKey = dragImgKey;
        this.locationx = locationx;
        CommonUtil.cancelCall(smsCall);
        smsCall = VHttpServiceManager.getInstance().getVService().getSms(currentState == 1 ? user.getPhone() : et_newPhone.getText().toString(), tvCountryCode.getText().toString(), dragImgKey, locationx);
        smsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    oldSms = etVerify.getText().toString();
                    CommonUtil.showmessage("短信验证码已经发送至" + formatPhone(currentState == 1 ? user.getPhone() : et_newPhone.getText().toString()), SettingAccountActivity.this);
                    Counting();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                startGetSms(dragImgKey, locationx);
            }
        });
    }

    private void Counting() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                if (currentState == 1) {
                    tvVerify.setEnabled(false);
                    tvVerify.setText("剩余" + l / 1000 + "秒");
                } else if (currentState == 2) {
                    tv_newVerfiy.setEnabled(false);
                    tv_newVerfiy.setText("剩余" + l / 1000 + "秒");
                }

            }

            @Override
            public void onFinish() {
                if (currentState == 1) {
                    tvVerify.setEnabled(true);
                    tvVerify.setText("重新获取");
                } else if (currentState == 2) {
                    tv_newVerfiy.setEnabled(true);
                    tv_newVerfiy.setText("重新获取");
                }
            }
        };
        countDownTimer.start();
    }
}
