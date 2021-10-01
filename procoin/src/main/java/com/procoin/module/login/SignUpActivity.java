package com.procoin.module.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.InputType;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;

/**
 * Created by zhengmj on 18-10-10.
 */

public class SignUpActivity extends TJRBaseToolBarSwipeBackActivity implements TextView.OnEditorActionListener, View.OnClickListener {
    private final String TAG = "SignUpActivity";

    private TextView tvCountryName;
    private TextView tvCountryCode;
    private TextView etInviteCode;
    //    private CheckBox tv_sign;
    private TextView tvProtocol;
    private TextView tv_signin;
    private EditText et_phone;
    private EditText et_passowrd;
    private EditText et_confirmPassword;


    private ImageView ivShowOrHidePsw;
    private ImageView ivShowOrHidePsw2;

    //    private RedzVerifyActivity.CountDownReceiver receiver;
    public static final String phoneParam = "PHONE";
    public static final String locationParam = "LOCATION";
    public static final String keyParam = "KEY";
    public static final String UserPass = "UP";
    public static final String CUserPass = "DOWN";
    public static final String CountryCode = "COUNTRYCODE";
    public static final String INVITECODE = "InviteCode";
//    private boolean phoneOk;
//    private boolean passwordOk;
//    private boolean cPasswordOk;
//    private boolean isCounting;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_signup;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        receiver = new RedzVerifyActivity.CountDownReceiver() {
//            @Override
//            public void doReceive(Intent intent) {
//                if (intent.getExtras()!=null){
//                    long time = intent.getExtras().getLong("time");
//                    isCounting = time != -1;
//                }
//            }
//        };
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("GetCountDownMsg");
//        registerReceiver(receiver,filter);
//        tv_sign = (CheckBox) findViewById(R.id.cbSign);
        tvProtocol = (TextView) findViewById(R.id.tvProtocol);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString normalText = new SpannableString("注册即代表你已同意并接受");
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        SpannableString clickText = new SpannableString("《W.W.C.T用户协议》");

        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(SignUpActivity.this, CommonConst.USER_PROTOCOL);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(SignUpActivity.this, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString clickText2 = new SpannableString("《W.W.C.T 隐私条款》");
        clickText2.setSpan(new ForegroundColorSpan(Color.parseColor("#376eb8")), 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(SignUpActivity.this, CommonConst.PRIVACY_PROTOCOL);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(SignUpActivity.this, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(clickText);
        spannableStringBuilder.append("和");
        spannableStringBuilder.append(clickText2);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_passowrd = (EditText) findViewById(R.id.et_password);
        et_confirmPassword = (EditText) findViewById(R.id.et_confirmpassword);
        tv_signin = (TextView) findViewById(R.id.tv_signin);
        tv_signin.setOnClickListener(this);
        tv_signin.setEnabled(true);


        ivShowOrHidePsw = (ImageView) findViewById(R.id.ivShowOrHidePsw);
        ivShowOrHidePsw2 = (ImageView) findViewById(R.id.ivShowOrHidePsw2);


        ivShowOrHidePsw.setSelected(true);
        ivShowOrHidePsw2.setSelected(true);

        ivShowOrHidePsw.setOnClickListener(this);
        ivShowOrHidePsw2.setOnClickListener(this);

        tvCountryCode = (TextView) findViewById(R.id.tvCountryCode);
        tvCountryCode.setOnClickListener(this);

        etInviteCode= (TextView) findViewById(R.id.etInviteCode);

        tvCountryName = (TextView) findViewById(R.id.tvCountryName);
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
//                if (!TextUtils.isEmpty(et_phone.getText()) && et_phone.getText().length() == 11) {
//                    phoneOk = true;
//                } else {
//                    phoneOk = false;
//                }
//                tv_signin.setEnabled(phoneOk && passwordOk && cPasswordOk);
//            }
//        });
//        et_passowrd.addTextChangedListener(new TextWatcher() {
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
//                if (!TextUtils.isEmpty(et_passowrd.getText()) && et_passowrd.getText().length() >= 8) {
//                    passwordOk = true;
//                } else {
//                    passwordOk = false;
//                }
//                tv_signin.setEnabled(phoneOk && passwordOk && cPasswordOk);
//            }
//        });
//        et_confirmPassword.addTextChangedListener(new TextWatcher() {
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
//                if (!TextUtils.isEmpty(et_confirmPassword.getText()) && passwordOk) {//&& et_confirmPassword.getText().length() == et_passowrd.getText().length()
//                    cPasswordOk = true;
//                } else {
//                    cPasswordOk = false;
//                }
//                tv_signin.setEnabled(phoneOk && passwordOk && cPasswordOk);
//            }
//        });
        et_confirmPassword.setOnEditorActionListener(this);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_signin:
                if (!CommonUtil.invalidatePhone(et_phone.getText().toString().trim(), SignUpActivity.this)) {
                    return;
                }
                if (!CommonUtil.invalidataPsd(et_passowrd.getText().toString().trim(), et_confirmPassword.getText().toString().trim(), SignUpActivity.this)) {
                    return;
                }


                String inviteCode=etInviteCode.getText().toString();
                if(TextUtils.isEmpty(inviteCode)){
                    CommonUtil.showmessage("请输入邀请码",this);
                    return;
                }

//                if (!tv_sign.isChecked()) {
//                    CommonUtil.showmessage("请先阅读《W.W.C.T用户协议》和《W.W.C.T 隐私条款》", SignUpActivity.this);
//                    return;
//                }
                String phone = et_phone.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString(phoneParam, phone);
                bundle.putString(UserPass, et_passowrd.getText().toString());
                bundle.putString(CUserPass, et_confirmPassword.getText().toString());
                bundle.putString(CountryCode, tvCountryCode.getText().toString());
                bundle.putString(INVITECODE, inviteCode);
                PageJumpUtil.pageJumpResult(SignUpActivity.this, VerifyActivity.class, bundle);
                break;
            case R.id.ivShowOrHidePsw:
                if (ivShowOrHidePsw.isSelected()) {
                    et_passowrd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowOrHidePsw.setSelected(false);
                } else {
                    et_passowrd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowOrHidePsw.setSelected(true);
                }
                et_passowrd.setSelection(et_passowrd.getText().length());
                break;
            case R.id.ivShowOrHidePsw2:
                if (ivShowOrHidePsw2.isSelected()) {
                    et_confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    ivShowOrHidePsw2.setSelected(false);
                } else {
                    et_confirmPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    ivShowOrHidePsw2.setSelected(true);
                }
                et_confirmPassword.setSelection(et_confirmPassword.getText().length());
                break;
            case R.id.tvCountryCode:
            case R.id.tvCountryName:
                PageJumpUtil.pageJumpResult(SignUpActivity.this, AllCountryCodeListActivity.class, new Intent(), 0x123);
                break;
        }

    }


    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            closeKeyBoard();
        }
        return true;
    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_confirmPassword.getWindowToken(), 0);
    }
}
