package com.procoin.module.login;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.common.TJrLoginTypeEnum;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.MD5;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * Created by zhengmj on 18-10-11.
 */

public class VerifyActivity extends TJRBaseToolBarSwipeBackActivity {
    private final String TAG = "VerifyActivity";
    private TextView tv_login;
    private TextView tv_sign;
    private TextView tv_verify;
    private EditText et_verify;

    //    private SharedPreferences sharedata;
    private CountDownTimer countDownTimer;
    public static boolean isCounting;
    private Call<ResponseBody> smsCall;
    private Call<ResponseBody> registeCall;
    private long COUNT = 60000;
    //    private String from;
    private String phone;
    private String countryCode;
    private String key;
    private String pass;
    private String cpass;
    private String inviteCode;
    private boolean isFail;
    private int location;


    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_verify;
    }

    @Override
    protected String getActivityTitle() {
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            phone = bundle.getString(SignUpActivity.phoneParam);
//            key = bundle.getString(SignUpActivity.keyParam);
//            location = bundle.getInt(SignUpActivity.locationParam);
//            if (bundle.containsKey("from")) from = bundle.getString("from");
            if (bundle.containsKey(SignUpActivity.UserPass))
                pass = bundle.getString(SignUpActivity.UserPass);
            if (bundle.containsKey(SignUpActivity.CUserPass))
                cpass = bundle.getString(SignUpActivity.CUserPass);
            if (bundle.containsKey(SignUpActivity.CountryCode))
                countryCode = bundle.getString(SignUpActivity.CountryCode);
            if (bundle.containsKey(SignUpActivity.INVITECODE))
                inviteCode = bundle.getString(SignUpActivity.INVITECODE);

//            if (bundle.containsKey(inviteCode))invite = bundle.getString(inviteCode);
            Log.d("200", "登录 bundle\n phone: " + phone + "\npass: " + pass + "\ncpass: " + cpass);
        }

        tv_login = (TextView) findViewById(R.id.tv_login);
//        if (!TextUtils.isEmpty(from) && from.equals("login")){
//            tv_login.setText("登录");
//        }
        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                test();
//                if (!TextUtils.isEmpty(from) && from.equals("login")){
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("Code",et_verify.getText().toString());
//                    intent.putExtras(bundle);
//                    setResult(RESULT_OK,intent);
//                    PageJumpUtil.finishCurr(VerifyActivity.this);
//                }else {
                startRegiste(key, location);
//                }
            }
        });
        tv_login.setEnabled(false);
        et_verify = (EditText) findViewById(R.id.et_verify);
        et_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!TextUtils.isEmpty(et_verify.getText()) && et_verify.getText().length() == 6) {
                    tv_login.setEnabled(true);
                } else {
                    tv_login.setEnabled(false);
                }
            }
        });
        tv_sign = (TextView) findViewById(R.id.tv_sign);
        tv_verify = (TextView) findViewById(R.id.tv_verify);
        et_verify = (EditText) findViewById(R.id.et_verify);
        et_verify.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                tv_verify.setEnabled(!TextUtils.isEmpty(et_verify.getText()));
            }
        });
        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isFail) {
                    if (isCounting) {
                        Counting();
                    } else {
//                        DragFragment dragFragment = DragFragment.newInstance(new DragFragment.OnSuccessCallback() {
//                            @Override
//                            public void onSuccess(String dragImgKey, int locationx) {
//                                key = dragImgKey;
//                                location = locationx;
                        startGetSms(key, location);
//                            }
//                        });
//                        dragFragment.show(getSupportFragmentManager(),"");
                    }
                } else {
                    if (isCounting) {
                        Counting();
                    } else {
                        startGetSms(key, location);
                    }
                }
            }
        });
    }

    public void startGetSms(String key, int location) {
        CommonUtil.cancelCall(smsCall);
        Log.d("154", "phone == " + phone + " key == " + key + " location == " + location);
        smsCall = VHttpServiceManager.getInstance().getVService().getSms(phone, countryCode, key, location);
        smsCall.enqueue(new MyCallBack(VerifyActivity.this, 2) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (!TextUtils.isEmpty(phone)) {
                        StringBuilder stringBuilder = new StringBuilder(phone);
                        stringBuilder.replace(3, 7, "****");
                        tv_sign.setText("短信验证码已经发送至" + stringBuilder.toString());
                    }
                    Counting();
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                CommonUtil.showmessage("获取成功", VerifyActivity.this);
                VerifyActivity.this.key = dragImgKey;
                VerifyActivity.this.location = locationx;
                startGetSms(dragImgKey, locationx);
            }
        });
    }

    private void test() {
        Bundle testBundle = new Bundle();
        testBundle.putString(CommonConst.USERACCOUNT, "123123");
        testBundle.putString(CommonConst.PASSWORD, "qweasd");// password
        testBundle.putString(CommonConst.LOGIN_TYPE, TJrLoginTypeEnum.mb.type());
        testBundle.putBoolean(CommonConst.IS_LOGIN, true);
        testBundle.putString(CommonConst.MYINFO, "myInfo");
        PageJumpUtil.pageJump(VerifyActivity.this, LoginActivity.class, testBundle);
    }

    public void startRegiste(String key, int location) {
        CommonUtil.cancelCall(registeCall);
        registeCall = VHttpServiceManager.getInstance().getVService().doRegiste(phone, countryCode, "", MD5.getMessageDigest(pass).toUpperCase(), MD5.getMessageDigest(cpass).toUpperCase(), inviteCode,"", 0, "", et_verify.getText().toString(), key, location);
        registeCall.enqueue(new MyCallBack(VerifyActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, VerifyActivity.this);
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonConst.USERACCOUNT, phone);
                    bundle.putString(CommonConst.PASSWORD, pass);// password
                    bundle.putString(CommonConst.LOGIN_TYPE, TJrLoginTypeEnum.mb.type());
                    bundle.putBoolean(CommonConst.IS_LOGIN, true);
                    bundle.putString(CommonConst.MYINFO, getRawResult());
                    PageJumpUtil.pageJump(VerifyActivity.this, LoginActivity.class, bundle);
                } else {
                    isFail = true;
                    if (resultData.code == 40016) {
                        CommonUtil.showmessage("请重新获取验证码", VerifyActivity.this);
                    }
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                VerifyActivity.this.key = dragImgKey;
                VerifyActivity.this.location = locationx;
                startRegiste(dragImgKey, locationx);
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_login, menu);
//        return true;
//    }
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        MenuItem item = menu.findItem(R.id.action_title);
//        item.setTitle("注册");
//        return super.onPrepareOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.action_title){
////            PageJumpUtil.pageJump(VerifyActivity.this,SignUpActivity.class);
//        }
//        return super.onOptionsItemSelected(item);
//    }

}
