package com.procoin.module.myhome;

import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.common.entity.ResultData;
import com.procoin.util.MyCallBack;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.MD5;
import com.procoin.R;

import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * 这个是修改密码
 *
 * @author zhengmj
 */
public class MyPasswordActivity extends TJRBaseToolBarSwipeBackActivity {

    private EditText edtOldPassword;
    private EditText edtNewPassword;
    private EditText edtNewPassword2;
//    private EditText edtRePassword;
//    private SendPasswdTask sendPasswdTask;

    private ImageView ivShowOrHidePsw;
    private ImageView ivShowOrHidePsw2;
    private Call<ResponseBody> updatePassCall;

    @Override
    protected int setLayoutId() {
        return R.layout.home_password;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.fix_passwd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(showView());
        showView();
    }

    public void showView() {
//        View view = InflaterUtils.inflateView(this, R.layout.home_password);
        Onclcik onclcik = new Onclcik();
//        TextView btnAdd = (TextView) findViewById(R.id.btnAdd);
//        btnAdd.setOnClickListener(onclcik);
        edtOldPassword = (EditText) findViewById(R.id.edtOldPassword);
        edtNewPassword = (EditText) findViewById(R.id.edtNewPassword);
//        edtRePassword = (EditText) view.findViewById(R.id.edtRePassword);
        ivShowOrHidePsw = (ImageView) findViewById(R.id.ivShowOrHidePsw);

        edtNewPassword2 = (EditText) findViewById(R.id.edtNewPassword2);
        ivShowOrHidePsw2 = (ImageView) findViewById(R.id.ivShowOrHidePsw2);


        ivShowOrHidePsw.setSelected(true);
        ivShowOrHidePsw2.setSelected(true);

        ivShowOrHidePsw.setOnClickListener(onclcik);
        ivShowOrHidePsw2.setOnClickListener(onclcik);
//        return view;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_title);
        item.setTitle("确定");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_title) {
            String oldPsd = edtOldPassword.getText().toString().trim();
            if (TextUtils.isEmpty(oldPsd)) {//原密码不需要判断了，判空就好了
                CommonUtil.showmessage("请输入原密码", this);
                return true;
            }
            if (com.procoin.util.CommonUtil.invalidataPsd(edtNewPassword.getText().toString().trim(), edtNewPassword2.getText().toString().trim(), MyPasswordActivity.this)) {
                StartSendPasswdTask();
            }
        }
        return super.onOptionsItemSelected(item);
    }
//    private boolean cansend() {
//        boolean send = true;
//        send = editTextLength(edtOldPassword);
//        if (!send) {
//            CommonUtil.showmessage("旧密码的长度至少为6位", this);
//            return send;
//        }
//        if (edtNewPassword.getText().toString().matches("[\u4e00-\u9fa5]+$")) {
//            CommonUtil.showmessage("密码不能包含中文", MyPasswordActivity.this);
//            return false;
//        }
//
//        send = editTextLength(edtNewPassword);
//        if (!send) {
//            CommonUtil.showmessage("新密码的长度至少为6位", this);
//            return send;
//        }
//        return send;
//    }

    /**
     * 输入的长度如果是6-15 就是可以超过就不行
     *
     * @return
     */
    private boolean editTextLength(EditText editText) {
        boolean length = false;
        int len = editText.getText().toString().trim().length();
        if (len >= 6) {
            length = true;
        }
        return length;

    }

    private class Onclcik implements View.OnClickListener {

        @Override
        public void onClick(View arg0) {
            switch (arg0.getId()) {
                case R.id.btnBack:
                    finish();
                    break;
//                case R.id.btnAdd:
//                    if (cansend()) {
//                        StartSendPasswdTask();
//                    }
//                    break;
                case R.id.ivShowOrHidePsw:
                    if (ivShowOrHidePsw.isSelected()) {
                        edtNewPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        ivShowOrHidePsw.setSelected(false);
                    } else {
                        edtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ivShowOrHidePsw.setSelected(true);
                    }
                    edtNewPassword.setSelection(edtNewPassword.getText().length());
                    break;
                case R.id.ivShowOrHidePsw2:
                    if (ivShowOrHidePsw2.isSelected()) {
                        edtNewPassword2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        ivShowOrHidePsw2.setSelected(false);
                    } else {
                        edtNewPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        ivShowOrHidePsw2.setSelected(true);
                    }
                    edtNewPassword2.setSelection(edtNewPassword2.getText().length());
                    break;
                default:
                    break;
            }

        }

    }

    public void StartSendPasswdTask() {
        CommonUtil.cancelCall(updatePassCall);
        updatePassCall = VHttpServiceManager.getInstance().getVService().updatePass(MD5.getMessageDigest(edtOldPassword.getText().toString().trim()), MD5.getMessageDigest(edtNewPassword.getText().toString().trim()), MD5.getMessageDigest(edtNewPassword2.getText().toString().trim()));
        updatePassCall.enqueue(new MyCallBack(MyPasswordActivity.this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if (resultData.msg != null)
                        CommonUtil.showmessage(resultData.msg, MyPasswordActivity.this);
                    finish();
                    dismissProgressDialog();
                }
            }
        });
//        CommonUtil.cancelAsyncTask(sendPasswdTask);
//        sendPasswdTask = (SendPasswdTask) new SendPasswdTask().executeParams(edtOldPassword.getText().toString().trim(), edtNewPassword.getText().toString().trim());

    }

//    private class SendPasswdTask extends BaseAsyncTask<String, Void, Boolean> {
//
//        private Exception e;
//        private String msg;
//        private ResultData phoneImgData;
//
//
//        @Override
//        protected Boolean doInBackground(String... arg0) {
//            String result = null;
//
//            try {
//                result = BeebarHttp.getInstance().updatePass(String.valueOf(getApplicationContext().getUser().getUserId()), arg0[0], arg0[1]);
//                if (!TextUtils.isEmpty(result)) {
//                    phoneImgData = new ResultDataParser().parse(new JSONObject(result));
//                    if (phoneImgData != null) {
//                        msg = phoneImgData.msg;
//                        if (phoneImgData.isSuccess()) {
//                            return true;
//                        }
//                    }
//                }
//            } catch (Exception e) {
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            if (result != null && result) {
//                if (msg != null) CommonUtil.showmessage(msg, MyPasswordActivity.this);
//                finish();
//            } else {
//                if (e != null) {
//                    NotificationsUtil.ToastReasonForFailure(MyPasswordActivity.this, e);
//                } else {
//                    if (msg != null) CommonUtil.showmessage(msg, MyPasswordActivity.this);
//                }
//            }
//            dismissProgressDialog();
//            super.onPostExecute(result);
//        }
//
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }
//    }


}
