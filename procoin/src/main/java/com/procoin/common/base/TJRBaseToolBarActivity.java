package com.procoin.common.base;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import com.procoin.common.constant.CommonConst;
import com.procoin.module.home.trade.dialog.RechargeUsdtDialogFragment;
import com.procoin.MainApplication;
import com.procoin.common.entity.jsonparser.ResultDataParser;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.trade.dialog.SelectPayWayDialogFragment;
import com.procoin.module.home.trade.entity.OtcEntity;
import com.gyf.barlibrary.ImmersionBar;
import com.procoin.http.util.ProgressHUD;
import com.procoin.social.TjrSocialMTAUtil;
import com.procoin.R;

import org.json.JSONObject;

public abstract class TJRBaseToolBarActivity extends BaseBarActivity implements OnCancelListener, ActionBarProgressBarListener {
    private volatile ProgressHUD mProgressHUD;
    protected MenuItem refreshItem;
    protected ResultDataParser resultDataParser;
    protected ImmersionBar immersionBar;
    protected Toolbar titleBar;
    protected ActionBar mActionBar;

    //这2个dialog是充值流程，放到父类是方便其他地方调用
    protected RechargeUsdtDialogFragment rechargeUsdtDialogFragment;
    protected SelectPayWayDialogFragment selectPayWayDialogFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "activity user_name is =========== " + this.getClass().getName());
        super.onCreate(savedInstanceState);
        setContentView(setLayoutId());//一定要先setContentView,在初始化沉浸式,否则下面的导航栏会出问题
        resultDataParser = new ResultDataParser();
        titleBar = (Toolbar) findViewById(R.id.toolbar);

        //初始化沉浸式
        if (isImmersionBarEnabled()) {
            initImmersionBar();
        }
    }

    protected abstract int setLayoutId();

    protected abstract String getActivityTitle();

    protected void initImmersionBar() {
        //在BaseActivity里初始化
        immersionBar = ImmersionBar.with(this);
        if (titleBar != null) {
            setSupportActionBar(titleBar);
            mActionBar = getSupportActionBar();
            mActionBar.setTitle(getActivityTitle());//设置Activit的Title
            immersionBar.titleBar(titleBar, true);
        }
        //设为true之后会把键盘模式 设置为这个WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        immersionBar.keyboardEnable(true);
        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA);
        immersionBar.flymeOSStatusBarFontColor(R.color.black);
        immersionBar.init();
//        CommonUtil.showmessage(immersionBar.isSupportStatusBarDarkFont()+"",this);
//        Log.d("initImmersionBar","isSupportStatusBarDarkFont=="+immersionBar.isSupportStatusBarDarkFont());
    }


    /**
     * 是否可以使用沉浸式
     * Is immersion bar enabled boolean.
     *
     * @return the boolean
     */
    protected boolean isImmersionBarEnabled() {
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        TjrSocialMTAUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            if (immersionBar != null)
                immersionBar.destroy();
        }
        TjrSocialMTAUtil.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(mLoggedOutReceiver);
        dismissProgressDialog();

    }

    public boolean isLogin() {
        MainApplication application = getApplicationContext();
        return application != null && application.getUser() != null && application.getUser().getUserId() > 0;
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();// 统一相当于按返回键，这样在关闭页面的时候不管是按物理返回键还是acionbar返回按钮都是统一的
        // finish();
        return true;
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(boolean disableFocus) {
        //true的话，Dialog不会抢夺Activity的焦点，但是Dialog的动画会失效
        if (isFinishing()) return;
        if (mProgressHUD == null)
            mProgressHUD = ProgressHUD.show(TJRBaseToolBarActivity.this, true, "", true, true, this);
    }

    public void showProgressDialog(CharSequence message) {
        if (isFinishing()) return;
        if (mProgressHUD == null)
            mProgressHUD = ProgressHUD.show(TJRBaseToolBarActivity.this, message, true, true, this);
    }

    public void dismissProgressDialog() {
        if (mProgressHUD != null && mProgressHUD.isShowing()) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        // TODO Auto-generated method stub
        if (isFinishing()) return;
        if (mProgressHUD != null) {
            mProgressHUD.dismiss();
            mProgressHUD = null;
        }
    }


//    public void showAlterBaseDialog(String title, String confirm, String cancel, String callbackType, CallbackFunction callbackFunction, Object object) {
//        // 对话框
//        if (alertBaseDialog == null) {
//            alertBaseDialog = new Builder(this);
//        }
//        ExitOnClick onClick = new ExitOnClick(callbackType, callbackFunction, object);
//        alertBaseDialog.setTitle(title).setIcon(android.R.drawable.ic_dialog_info);
//        alertBaseDialog.setPositiveButton(confirm, onClick);
//        alertBaseDialog.setNegativeButton(cancel, onClick);
//        alertBaseDialog.show();
//    }
//
//    private class ExitOnClick implements DialogInterface.OnClickListener {
//        private String callbackType;
//        private CallbackFunction callbackFunction;
//        private Object object;
//
//        public ExitOnClick(String callbackType, CallbackFunction callbackFunction, Object object) {
//            super();
//            this.callbackType = callbackType;
//            this.callbackFunction = callbackFunction;
//            this.object = object;
//        }
//
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            switch (which) {
//                case -1: // 确定
//                    callbackFunction.callbackFunction(callbackType, object);
//                    break;
//                case -2:
//                    dialog.cancel();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }


    protected void parserParamsBack(Bundle bundle, ParamsBack paramsBack) {
        if (bundle == null) return;
        try {
            if (bundle.containsKey(CommonConst.PARAMS)) {
                JSONObject jsonObject = new JSONObject(bundle.getString(CommonConst.PARAMS));
                if (paramsBack != null) paramsBack.paramsBack(bundle, jsonObject);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    protected interface ParamsBack {
        void paramsBack(Bundle bundle, JSONObject jsonObject) throws Exception;
    }

    @Override
    public void progressBarShow() {
        // TODO Auto-generated method stub
        if (refreshItem != null) refreshItem.setVisible(false);
        setSupportProgressBarIndeterminateVisibility(true);
    }

    @Override
    public void progressBarDismiss() {
        // TODO Auto-generated method stub
        setSupportProgressBarIndeterminateVisibility(false);
        if (refreshItem != null) refreshItem.setVisible(true);
    }

    public int getWidthPixels() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    public int getHeightPixels() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

//    /**
//     * 中转到组件页面方法
//     *
//     * @param pkg
//     * @param cls
//     * @param params
//     */
//    public void jumpToComponet(final String pkg, final String cls, final String params) {
//        try {
//            if (pkg != null && !"".equals(pkg)) {
//                ApplicationInfo info = CommonUtil.checkApkExist(TJRBaseActionBarActivity.this, pkg);
//                if (info != null) {
//                    if (ComponentWebViewV1Activity.class.getCanonicalName().equals(cls) || ComponentWebViewActivity.class.getCanonicalName().equals(cls)) {
//                        String jc = null;
//                        if (params != null) {
//                            JSONObject json = new JSONObject(params);
//                            if (JsonParserUtils.hasAndNotNull(json, MyComponentsTable.COMPONENT_JC)) {
//                                jc = json.getString(MyComponentsTable.COMPONENT_JC);
//                            }
//                        }
//                        if ("financialmarket".equals(jc)) {
//                            if (clausUtil == null) {
//                                clausUtil = new ClausUtil(this);
//                                clausUtil.type = clausUtil.agreeLicai;
//                                clausUtil.cls = cls;
//                                clausUtil.pkg = pkg;
//                                clausUtil.params = params;
//                                clausUtil.callback = new ClausUtilCallBack() {
//
//                                    @Override
//                                    public void pagejump() {
//                                        AppJc.jumpToComponetApp(TJRBaseActionBarActivity.this, pkg, cls, params);
//                                    }
//                                };
//                            }
//                            clausUtil.getClause();
//                        } else {
//                            AppJc.jumpToComponetApp(this, pkg, cls, params);
//                        }
//                    } else {//如果不是网页类型，就需要获取异常来进入组件市场
//                        if ("com.cropyme.kandapan".equals(pkg)) {
//                            PackageInfo pi = getPackageManager().getPackageInfo(pkg, 0);
//                            CommonUtil.LogLa(2, "com.cropyme.kandapan versioncode is " + pi.versionCode);
//                            if (pi != null && pi.versionCode <= 15) {//在这个版本前需要强制安装
//                                JSONObject json = null;
//                                if (params == null) {
//                                    json = new JSONObject();
//                                } else {
//                                    json = new JSONObject(params);
//                                }
//                                json.put("mjneedinstall", true);//需要强制安装，名字定的变态点， 防止后台返回同样的值
//                                AppJc.jumpToComponetDetail(TJRBaseActionBarActivity.this, pkg, cls, json.toString());
//                                return;
//                            }
//                        }
//                        AppJc.jumpToComponetAppNotTry(this, pkg, cls, params);
//                    }
//                } else {
//                    //该组件还没有安装,请到组件市场下载
//                    AppJc.jumpToComponetDetail(TJRBaseActionBarActivity.this, pkg, cls, params);
//                }
//            }
//        } catch (Exception e) {
//            if (!TextUtils.isEmpty(pkg) && !"com.cropyme".equals(pkg)) {//如果是主程序就不提示更新
//                AppJc.jumpToComponetDetail(TJRBaseActionBarActivity.this, pkg, cls, null);
//                CommonUtil.showmessage("请点击打开按钮，进入组件更新程序", TJRBaseActionBarActivity.this);
//            } else {
//                CommonUtil.showmessage("请更新程序", TJRBaseActionBarActivity.this);
//            }
//        }
//    }

    public String getDeviceId() {
        return getApplicationContext().getDeviceId();
    }

    public String getUserId() {
        if (getApplicationContext().getUser() != null && getApplicationContext().getUser().getUserId() > 0) {
            return String.valueOf(getApplicationContext().getUser().getUserId());
        }
        return "";
    }

    public long getUserIdLong() {
        if (getApplicationContext().getUser() != null && getApplicationContext().getUser().getUserId() > 0) {
            return getApplicationContext().getUser().getUserId();
        }
        return 0;
    }

    public String getCountryCode() {
        if (getApplicationContext().getUser() != null ) {
            return getApplicationContext().getUser().getCountryCode();
        }
        return "";
    }



    public void showRechargeUsdtDialogFragment(int recharge,final OtcEntity otcEntity) {
        rechargeUsdtDialogFragment = RechargeUsdtDialogFragment.newInstance(recharge);
        rechargeUsdtDialogFragment.setRechargeListen(new RechargeUsdtDialogFragment.RechargeListen() {
            @Override
            public void goSelectPayWay(String cny) {
                showSelectPayWayDialogFragment(cny,otcEntity);
            }
        });
        rechargeUsdtDialogFragment.showDialog(getSupportFragmentManager(), "");
    }


    public void showSelectPayWayDialogFragment(String cny,OtcEntity otcEntity) {
        selectPayWayDialogFragment = SelectPayWayDialogFragment.newInstance(cny,otcEntity);
        selectPayWayDialogFragment.setPayListen(new SelectPayWayDialogFragment.PayListen() {
            @Override
            public void onPaySuccess() {
                if (rechargeUsdtDialogFragment != null) rechargeUsdtDialogFragment.dismiss();
                if (selectPayWayDialogFragment != null) selectPayWayDialogFragment.dismiss();
            }
        });
        selectPayWayDialogFragment.showDialog(getSupportFragmentManager(), "");
    }
}
