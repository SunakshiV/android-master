package com.procoin.module.logout;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.procoin.common.base.TJRBaseActionBarSwipeBackActivity;
import com.procoin.http.common.TJRFilterConf;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.R;
import com.procoin.module.login.LoginActivity;

public class ReLoginRemindActivity extends TJRBaseActionBarSwipeBackActivity {
    // 对话框
    private TjrBaseDialog tjrBeanDialog;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (tjrBeanDialog != null) tjrBeanDialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
//        (new LogoutClearUser()).logoutClearUser(ReLoginRemindActivity.this, null);
        mActionBar.hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            // 有些情况下需要先清除透明flag
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }
        String msg = getIntent().getStringExtra(TJRFilterConf.RESPONSE_MSG);
        int code = getIntent().getIntExtra(TJRFilterConf.RESPONSE_CODE, 0);

        if (tjrBeanDialog == null) {
            tjrBeanDialog = new TjrBaseDialog(ReLoginRemindActivity.this) {

                @Override
                public void setDownProgress(int progress) {

                }

                @Override
                public void onclickOk() {
                    (new LogoutClearUser()).logoutClearUser(ReLoginRemindActivity.this, LoginActivity.class);
//                    try {
//                        ApplicationInfo info = CommonUtil.checkApkExist(ReLoginRemindActivity.this, CommonConst.PKG_HOME);
//                        if (info != null) {
//                            Log.d("200","ConfigTjrInfo info not null");
//                            Intent mIntent = new Intent();
//                            ComponentName comp = new ComponentName(CommonConst.PKG_HOME,
//                                    "LoginActivity");
//                            mIntent.setComponent(comp);
//                            mIntent.putExtras(new Bundle());
//                            mIntent.setAction("android.intent.action.VIEW");
//                            ReLoginRemindActivity.this.startActivity(mIntent);
//                        }else {
//                            Log.d("200","ConfigTjrInfo info is null");
//                        }
//                    } catch (Exception e) {
//                        Log.d("200","ConfigTjrInfo info catch error");
//                    }
                    finish();
                }

                @Override
                public void onclickClose() {
                    finish();
                }
            };
            tjrBeanDialog.setCloseVisibility(View.GONE);
        }


        if (msg == null) msg = "您的会话已经过期,请重新登录";
        tjrBeanDialog.setTvTitle("错误提示");
        tjrBeanDialog.setMessage(msg);
        tjrBeanDialog.setCancelable(false);
        tjrBeanDialog.setBtnOkText("确定");
        if (!tjrBeanDialog.isShowing()) {
            tjrBeanDialog.show();
        }

    }

}
