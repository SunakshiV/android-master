package com.procoin.common.base;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog.Builder;

import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.jsonparser.ResultDataParser;
import com.procoin.http.util.ProgressHUD;
import com.procoin.social.TjrSocialMTAUtil;
import com.procoin.social.util.CommonUtil;

import org.json.JSONObject;

public class TJRBaseActionBarActivity extends BaseBarActivity implements OnCancelListener, ActionBarProgressBarListener {
    private volatile ProgressHUD mProgressHUD;
    protected ActionBar mActionBar;
    private Builder alertBaseDialog;
    protected MenuItem refreshItem;

    protected ResultDataParser resultDataParser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "activity user_name is =========== " + this.getClass().getName());

        super.onCreate(savedInstanceState);
//        supportRequestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        mActionBar = getSupportActionBar();
//        if(mActionBar==null){
//            Toolbar toorToolbar=(Toolbar)findViewById(R.id.toolbar);
//            if(toorToolbar!=null){
//                setSupportActionBar(toorToolbar);
//                mActionBar = getSupportActionBar();
//            }
//        }
        mActionBar.setElevation(0);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowCustomEnabled(true);
        setSupportProgressBarIndeterminateVisibility(false);

        resultDataParser = new ResultDataParser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        TjrSocialMTAUtil.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        TjrSocialMTAUtil.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unregisterReceiver(mLoggedOutReceiver);
        dismissProgressDialog();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();// ?????????????????????????????????????????????????????????????????????????????????????????????acionbar???????????????????????????
        // finish();
        return true;
    }

    public void showProgressDialog() {
        showProgressDialog("");
    }

    public void showProgressDialog(CharSequence message) {
        if (isFinishing()) return;
        if (mProgressHUD == null)
            mProgressHUD = ProgressHUD.show(TJRBaseActionBarActivity.this, message, true, true, this);
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
//        // ?????????
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
//                case -1: // ??????
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
//     * ???????????????????????????
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
//                    } else {//?????????????????????????????????????????????????????????????????????
//                        if ("com.cropyme.kandapan".equals(pkg)) {
//                            PackageInfo pi = getPackageManager().getPackageInfo(pkg, 0);
//                            CommonUtil.LogLa(2, "com.cropyme.kandapan versioncode is " + pi.versionCode);
//                            if (pi != null && pi.versionCode <= 15) {//????????????????????????????????????
//                                JSONObject json = null;
//                                if (params == null) {
//                                    json = new JSONObject();
//                                } else {
//                                    json = new JSONObject(params);
//                                }
//                                json.put("mjneedinstall", true);//????????????????????????????????????????????? ??????????????????????????????
//                                AppJc.jumpToComponetDetail(TJRBaseActionBarActivity.this, pkg, cls, json.toString());
//                                return;
//                            }
//                        }
//                        AppJc.jumpToComponetAppNotTry(this, pkg, cls, params);
//                    }
//                } else {
//                    //????????????????????????,????????????????????????
//                    AppJc.jumpToComponetDetail(TJRBaseActionBarActivity.this, pkg, cls, params);
//                }
//            }
//        } catch (Exception e) {
//            if (!TextUtils.isEmpty(pkg) && !"com.cropyme".equals(pkg)) {//????????????????????????????????????
//                AppJc.jumpToComponetDetail(TJRBaseActionBarActivity.this, pkg, cls, null);
//                CommonUtil.showmessage("????????????????????????????????????????????????", TJRBaseActionBarActivity.this);
//            } else {
//                CommonUtil.showmessage("???????????????", TJRBaseActionBarActivity.this);
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
}
