package com.procoin.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.common.entity.ResultData;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.MD5;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;

//import com.tjrv.http.tjrcpt.RedzHttpServiceManager;

/**
 * 忘记密码
 * Created by zhengmj on 18-10-12.
 */

public class ReceiveActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
    private TextView tvCountryName;
    private TextView tvCountryCode;
    private EditText et_phone;
    private EditText et_verify;
    private EditText et_password;
    private EditText et_confirmpassword;
    private TextView tv_verify;
    private TextView tv_receive;

    private ImageView ivShowOrHidePsw;
    private ImageView ivShowOrHidePsw2;

    private Call<ResponseBody> receiveCall;
    private Call<ResponseBody> smsCall;
    private CountDownTimer countDownTimer;
    private static boolean isCounting;
    //    private boolean phoneOk;
//    private boolean vCodeOk;
//    private boolean passOK;
//    private boolean vPassOk;
    private String dragImgKey = "";
    private int location = 0;

    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_receive;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvCountryName = (TextView) findViewById(R.id.tvCountryName);
        tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_verify = (EditText) findViewById(R.id.et_verify);
        et_password = (EditText) findViewById(R.id.et_password);
        et_confirmpassword = (EditText) findViewById(R.id.et_confirmpassword);
        tv_verify = (TextView) findViewById(R.id.tv_verify);

        tv_receive = (TextView) findViewById(R.id.tv_receive);

        ivShowOrHidePsw = (ImageView) findViewById(R.id.ivShowOrHidePsw);
        ivShowOrHidePsw2 = (ImageView) findViewById(R.id.ivShowOrHidePsw2);

        ivShowOrHidePsw.setSelected(true);
        ivShowOrHidePsw2.setSelected(true);

        ivShowOrHidePsw.setOnClickListener(this);
        ivShowOrHidePsw2.setOnClickListener(this);

        tvCountryCode.setOnClickListener(this);
        tvCountryName.setOnClickListener(this);

        tvCountryCode.setText("+86");//默认中国
        tvCountryName.setText("中国");//默认中国


//        et_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                phoneOk = !TextUtils.isEmpty(et_phone.getText())&&et_phone.getText().length() == 11;
//                tv_receive.setEnabled(phoneOk&&vCodeOk&&passOK&&vPassOk);
//            }
//        });
//        et_verify.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                vCodeOk = !TextUtils.isEmpty(et_verify.getText())&&et_verify.getText().length() == 6;
//                tv_receive.setEnabled(phoneOk&&vCodeOk&&passOK&&vPassOk);
//            }
//        });
//        et_password.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                passOK = !TextUtils.isEmpty(et_password.getText())&&(et_password.getText().length()>=6||et_password.getText().length()<=18);
//                tv_receive.setEnabled(phoneOk&&vCodeOk&&passOK&&vPassOk);
//            }
//        });
//        et_confirmpassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                vPassOk = et_confirmpassword.getText().length() == et_password.getText().length();
//                tv_receive.setEnabled(phoneOk&&vCodeOk&&passOK&&vPassOk);
//            }
//        });
        tv_receive.setOnClickListener(this);
        tv_receive.setEnabled(true);
        tv_verify.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_verify:
                if (!CommonUtil.invalidatePhone(et_phone.getText().toString().trim(), this)) {
                    return;
                }
                startGetSms(et_phone.getText().toString(), tvCountryCode.getText().toString(), dragImgKey, location);
                break;
            case R.id.tv_receive:
                if (!CommonUtil.invalidatePhone(et_phone.getText().toString().trim(), this)) {
                    return;
                }
                String verify = et_verify.getText().toString().trim();
                if (TextUtils.isEmpty(verify)) {
                    CommonUtil.showmessage("请输入验证码", ReceiveActivity.this);
                    return;
                }
                if (!CommonUtil.invalidataPsd(et_password.getText().toString().trim(), et_confirmpassword.getText().toString().trim(), this)) {
                    return;
                }
                startReceive(dragImgKey, location);
                break;
            case R.id.ivShowOrHidePsw:
                if (ivShowOrHidePsw.isSelected()) {
                    et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowOrHidePsw.setSelected(false);
                } else {
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowOrHidePsw.setSelected(true);
                }
                et_password.setSelection(et_password.getText().length());
                break;
            case R.id.ivShowOrHidePsw2:
                if (ivShowOrHidePsw2.isSelected()) {
                    et_confirmpassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowOrHidePsw2.setSelected(false);
                } else {
                    et_confirmpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowOrHidePsw2.setSelected(true);
                }
                et_confirmpassword.setSelection(et_confirmpassword.getText().length());
                break;
            case R.id.tvCountryCode:
            case R.id.tvCountryName:
                PageJumpUtil.pageJumpResult(ReceiveActivity.this, AllCountryCodeListActivity.class, new Intent(), 0x123);
                break;
        }
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

    public void startGetSms(final String phone, final String code, String key, int location) {
        CommonUtil.cancelCall(smsCall);
        smsCall = VHttpServiceManager.getInstance().getVService().getSms(phone, code, key, location);
        smsCall.enqueue(new MyCallBack(ReceiveActivity.this, 2) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();

                if (resultData.isSuccess()) {
                    Counting();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                if (!TextUtils.isEmpty(et_phone.getText())) {
                    startGetSms(et_phone.getText().toString(), code, dragImgKey, locationx);
                } else {
                    CommonUtil.showmessage("请输入手机号码", ReceiveActivity.this);
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    private void Counting() {
        countDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                tv_verify.setEnabled(false);
                tv_verify.setText("剩余" + l / 1000 + "秒");
                isCounting = true;
            }

            @Override
            public void onFinish() {
                isCounting = false;
                tv_verify.setEnabled(true);
                tv_verify.setText("重新获取");
            }
        }.start();
    }

    private void startReceive(String key, int location) {
        CommonUtil.cancelCall(receiveCall);
        receiveCall = VHttpServiceManager.
                getInstance().
                getVService().
                doReceive(et_phone.getText().toString(),
                        et_verify.getText().toString(),
                        MD5.getMessageDigest(et_confirmpassword.getText().toString()).toUpperCase(), key, location);
        receiveCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, ReceiveActivity.this);
                    PageJumpUtil.finishCurr(ReceiveActivity.this);
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                startReceive(dragImgKey, locationx);
            }
        });
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
        super.onDestroy();

    }
}
