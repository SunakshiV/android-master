package com.procoin.module.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.BaseBarActivity;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.HomeActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.google.gson.Gson;
import com.procoin.R;
import com.procoin.http.common.ConfigTjrInfo;
import com.procoin.http.common.TJrLoginTypeEnum;
import com.procoin.http.model.User;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.MD5;
import com.procoin.http.util.ShareData;
import com.gyf.barlibrary.OnKeyboardListener;

import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.procoin.util.PageJumpUtil.pageJump;

//import com.tjrv.http.tjrcpt.RedzHttpServiceManager;

/**
 * Created by zhengmj on 18-10-10.
 */

public class LoginActivity extends TJRBaseToolBarSwipeBackActivity implements TextView.OnEditorActionListener, View.OnClickListener {
    private LinearLayout ll_verify;
    private TextView tv_verify;
    private TextView tv_login;
    private TextView tv_receive;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_verify;
    private ImageView ivLogo;


    private ImageView ivShowOrHidePsw;

    private CountDownTimer countDownTimer;
    private SharedPreferences sharedata;
    private Call<ResponseBody> loginCall;
    private Call<ResponseBody> smsCall;
    private static boolean isCounting;
    private String cls;
    private String pkg;
    private String params;
    //    private String smsCode = "";
//    private String userPass = "";
    //    private boolean pwOk;
//    private boolean acOk;
//    private boolean isPass = false;
    private String key = "";
    private int location = 0;


    private Handler handler = new Handler();


    @Override
    protected int setLayoutId() {
        return R.layout.activity_login_main;
    }

    @Override
    protected String getActivityTitle() {
        return "　" ;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LoginActivity", "onCreate...");
//        setSwipeBackEnable(false);
        setOverrideExitAniamtion(false);
        mActionBar.setDisplayHomeAsUpEnabled(false);
        if (this.getIntent() != null) {
            Bundle inBundle = this.getIntent().getExtras();
            if (inBundle != null) {
                parserParamsBack(inBundle, new ParamsBack() {
                    @Override
                    public void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception {
                        if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.CLS)) {
                            bundle.putString(CommonConst.CLS, jsonObject.getString(CommonConst.CLS));
                        }
                        if (JsonParserUtils.hasAndNotNull(jsonObject, CommonConst.PKG)) {
                            bundle.putString(CommonConst.PKG, jsonObject.getString(CommonConst.PKG));
                        }
                    }
                });
                cls = inBundle.getString(CommonConst.CLS);
                pkg = inBundle.getString(CommonConst.PKG);
                params = inBundle.getString(CommonConst.PARAMS);
            }
        }
        ll_verify = (LinearLayout) findViewById(R.id.ll_verify);
        ll_verify.setVisibility(View.GONE);
        tv_verify = (TextView) findViewById(R.id.tv_verify);
        tv_verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startGetSms();
            }
        });
        et_verify = (EditText) findViewById(R.id.et_verify);
        ivLogo = (ImageView) findViewById(R.id.ivLogo);
        ivShowOrHidePsw = (ImageView) findViewById(R.id.ivShowOrHidePsw);
        ivShowOrHidePsw.setSelected(true);
        ivShowOrHidePsw.setOnClickListener(this);
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
//                if (pwOk && acOk && !TextUtils.isEmpty(et_verify.getText()) && et_verify.getText().length() == 6) {
//                    smsCode = et_verify.getText().toString();
//                    tv_login.setEnabled(true);
//                } else {
//                    tv_login.setEnabled(false);
//                }
//            }
//        });
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_login = (TextView) findViewById(R.id.tv_login);
        tv_login.setEnabled(true);
        tv_login.setOnClickListener(this);
        tv_receive = (TextView) findViewById(R.id.tv_receive);
        tv_receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PageJumpUtil.pageJump(LoginActivity.this, ReceiveActivity.class);
            }
        });
        sharedata = ShareData.getUserSharedPreferences(this);
        et_password.setOnEditorActionListener(this);
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
//                    acOk = true;
//                } else {
//                    acOk = false;
//                }
//                tv_login.setEnabled(acOk && pwOk && (ll_verify.getVisibility() == View.GONE || !TextUtils.isEmpty(et_verify.getText())));
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
//                if (!TextUtils.isEmpty(et_password.getText())) {
//                    userPass = et_password.getText().toString();
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                if (!TextUtils.isEmpty(et_password.getText()) && et_password.getText().length() >= 6) {
//                    pwOk = true;
//                } else {
//                    pwOk = false;
//                }
//                tv_login.setEnabled(acOk && pwOk && (ll_verify.getVisibility() == View.GONE || !TextUtils.isEmpty(et_verify.getText())));
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
//                tv_login.setEnabled(acOk && pwOk && (ll_verify.getVisibility() == View.GONE || !TextUtils.isEmpty(et_verify.getText())));
//            }
//        });

        immersionBar.setOnKeyboardListener(new OnKeyboardListener() {
            @Override
            public void onKeyboardChange(boolean isPopup, int keyboardHeight) {
                LoginActivity.this.isPopup = isPopup;
                handler.removeCallbacks(null);
                handler.postDelayed(runnable, 200);


            }
        });
    }

    boolean isPopup;
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (isPopup) {
                ivLogo.setVisibility(View.GONE);
            } else {
                ivLogo.setVisibility(View.VISIBLE);
            }
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LoginActivity", "onResume...");
        if (isLogin(LoginActivity.this)) {
            PageJumpUtil.finishCurr(LoginActivity.this);
        }
    }

    @Override
    public void finish() {
        closeKeyBoard();
        super.finish();
        overridePendingTransition(0, R.anim.login_out_top_to_bottom);
        Log.d("LoginActivity", "finish...");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processInputParams(intent);
    }

    private void processInputParams(Intent intent) {
        if (intent != null) {//
            Bundle inBundle = intent.getExtras();
            if (inBundle != null) {
                boolean isLogin = inBundle.getBoolean(CommonConst.IS_LOGIN, false);
                String bAcount = inBundle.getString(CommonConst.USERACCOUNT);
                String bPassword = inBundle.getString(CommonConst.PASSWORD);
                String bLoginType = inBundle.getString(CommonConst.LOGIN_TYPE);
                String bjson = inBundle.getString(CommonConst.MYINFO);
                if (isLogin) {
                    successLoginTo(bjson, bAcount, bPassword, bLoginType);
                }
            }
        }
    }

    private void successLoginTo(String json, String userAccount, String password, String loginType) {
        Log.d("154", "successLoginTo == " + json);
        com.procoin.social.util.CommonUtil.LogLa(2, "LoginActivity--->        successLoginTo = " + json);
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (JsonParserUtils.hasAndNotNull(jsonObject, "msg")) {
                CommonUtil.showmessage(jsonObject.getString("msg"), LoginActivity.this);
            }
            if (JsonParserUtils.hasNotNullAndIsIntOrLong(jsonObject, "code")) {
                int code = jsonObject.getInt("code");
                if (code == 40015) {//需要输入验证码了
//                    llCode.setVisibility(View.VISIBLE);
//                    edtCode.setText("");
                }

            }
            if (JsonParserUtils.hasAndNotNull(jsonObject, "success")) {
                if (jsonObject.getBoolean("success")) {
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "data")) {
                        JSONObject dataJsonObj = jsonObject.getJSONObject("data");
                        if (JsonParserUtils.hasAndNotNull(dataJsonObj, "token")) {
                            String sessionid = dataJsonObj.getString("token");// 与userid绑定验证的
                            ConfigTjrInfo.getInstance().setSessionid(sessionid);
                            ConfigTjrInfo.getInstance().setVersion(getApplicationContext().getmVersion());
                        }
                        if (JsonParserUtils.hasAndNotNull(dataJsonObj, "user")) {
//                            UserJsonParser userJsonParser = new UserJsonParser();
//                            User user = userJsonParser.parse(dataJsonObj.getJSONObject("user"));
                            User user = new Gson().fromJson(dataJsonObj.getString("user"), User.class);
                            if (user != null && ConfigTjrInfo.getInstance().getSessionid() != null) {
                                getApplicationContext().setUser(user);
                                ShareData.saveUser(sharedata, userAccount, password, loginType, ConfigTjrInfo.getInstance().getSessionid(), getApplicationContext().getUser());
                                if (!TextUtils.isEmpty(cls) && !TextUtils.isEmpty(pkg)) {
                                    Intent intent = new Intent();
                                    intent.setClassName(pkg, cls);
                                    Bundle pBundle = new Bundle();
                                    if (!TextUtils.isEmpty(params)) {
                                        pBundle.putString(CommonConst.PARAMS, params);
                                    }
                                    intent.putExtras(pBundle);
                                    PageJumpUtil.pageJump(LoginActivity.this, intent);
                                    PageJumpUtil.finishCurr(LoginActivity.this);
                                    getApplicationContext().startSubPushService();
                                } else {
//                                    if (countDownUtil!=null)countDownUtil.clear();
                                    getApplicationContext().startSubPushService();
                                    Intent intent = new Intent();
                                    intent.setClass(LoginActivity.this, HomeActivity.class);
                                    LoginActivity.this.startActivity(intent);
                                    PageJumpUtil.finishCurr(LoginActivity.this);
//                                    overridePendingTransition(R.anim.login_enter, R.anim.login_out);
                                    overridePendingTransition(0, R.anim.login_out_top_to_bottom);

                                }
                            }
                        }
                    }

                    //登录后清空 所有 本地"关注"数据, 因为不支持同一账户多点登录
//                    FollowSharedPreference.clearAll(this);
                } else {

                }
            }
        } catch (Exception e) {
            com.procoin.http.util.CommonUtil.showmessage("登录失败", LoginActivity.this);
        }
    }

    private void startLogin(String key, final int location) {
        showProgressDialog();
        CommonUtil.cancelCall(loginCall);
        String smsCode = "";
        if (ll_verify.getVisibility() == View.VISIBLE) {
            smsCode = et_verify.getText().toString().trim();
        }
        final String userPass = et_password.getText().toString().trim();
        loginCall = VHttpServiceManager.getInstance().getVService().doLogin(et_phone.getText().toString(), MD5.getMessageDigest(userPass), smsCode, key, location);
        loginCall.enqueue(new MyCallBack(this, 1) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    successLoginTo(getRawResult(), et_phone.getText().toString(), MD5.getMessageDigest(userPass), TJrLoginTypeEnum.mb.type());
                } else {
                    if (resultData.code == 40015) {
//                        isPass = true;
                        ll_verify.setVisibility(View.VISIBLE);
//                        tv_login.setEnabled(false);
                    }
//                    if (ll_verify.getVisibility() == View.VISIBLE) {
//                        et_verify.setText("");
//                    }
//                    et_password.setText("");
//                    et_password.setFocusable(true);
//                    et_password.setFocusableInTouchMode(true);
//                    et_password.requestFocus();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.showSoftInput(et_password,0);
                }
            }

            @Override
            protected void onDragSuccessCallback(String dragImgKey, int locationx) {
                super.onDragSuccessCallback(dragImgKey, locationx);
                LoginActivity.this.key = dragImgKey;
                LoginActivity.this.location = locationx;
                startLogin(dragImgKey, locationx);
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }

    public static boolean isLogin(final BaseBarActivity activity) {
        if (activity.getApplicationContext().getUser() == null || activity.getApplicationContext().getUser().getUserId() > 0) {
            Log.d("154", "successLoginTo isLogin is false");
            return false;
        }
        return true;
    }

    public static void login(final BaseBarActivity activity) {
        login(activity, null, null, null);
    }

    public static void login(final BaseBarActivity activity, final String pkg, final String cls, final String jsonParams) {
        if (activity == null) return;
        Bundle b = new Bundle();
        if (!TextUtils.isEmpty(pkg)) b.putString(CommonConst.PKG, pkg);
        if (!TextUtils.isEmpty(cls)) b.putString(CommonConst.CLS, cls);

        if (!TextUtils.isEmpty(jsonParams)) {
            b.putString(CommonConst.PARAMS, jsonParams);
        }
        if (!isLogin(activity)) {  //需要登录
//            pageJump(activity, LoginActivity.class, b);
            PageJumpUtil.pageJump(activity, LoginActivity.class, b);
            activity.overridePendingTransition(R.anim.login_in_bottom_to_top, 0);
        } else {
            //如果是已经登录过的那么直接
            Intent intent = new Intent();
            intent.setClassName(pkg, cls);
            intent.putExtras(b);
            PageJumpUtil.pageJump(activity, intent);
        }

    }

    private void closeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_password.getWindowToken(), 0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_title);
        item.setTitle("注册账户");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_title) {
            PageJumpUtil.pageJump(LoginActivity.this, SignUpActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            closeKeyBoard();
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            case R.id.tv_login:
                if (!CommonUtil.invalidatePhone(et_phone.getText().toString().trim(), LoginActivity.this)) {
                    return;
                }
                String psd = et_password.getText().toString().trim();
                if (TextUtils.isEmpty(psd)) {
                    CommonUtil.showmessage("请输入密码", LoginActivity.this);
                    return;
                }
                if (ll_verify.getVisibility() == View.VISIBLE) {//如果需要验证码的话
                    String verify = et_verify.getText().toString().trim();
                    if (TextUtils.isEmpty(verify)) {
                        CommonUtil.showmessage("请输入验证码", LoginActivity.this);
                        return;
                    }
                }
                startLogin(key, location);
                break;
        }
    }

    public void startGetSms() {
        showProgressDialog();
        CommonUtil.cancelCall(smsCall);
        smsCall = VHttpServiceManager.getInstance().getVService().getSms(et_phone.getText().toString(),"" , key, location);
        smsCall.enqueue(new MyCallBack(LoginActivity.this, 2) {
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
                key = dragImgKey;
                location = locationx;
                startGetSms();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK){
//            if (data!=null&&data.getExtras()!=null){
//                Bundle bundle = data.getExtras();
//                smsCode = bundle.getString("Code");
//            }
//            startLogin(key,location);
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }

}
