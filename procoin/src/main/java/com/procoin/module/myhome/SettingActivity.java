package com.procoin.module.myhome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.module.home.HomeActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
//import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.task.BaseAsyncTask;
import com.procoin.R;
import com.procoin.module.logout.LogoutClearUser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

import static com.procoin.util.PageJumpUtil.pageJump;

public class SettingActivity extends TJRBaseToolBarSwipeBackActivity {


    //    @BindView(R.id.llPaymentTerm)
//    LinearLayout llPaymentTerm;
    @BindView(R.id.llEditPassWord)
    LinearLayout llEditPassWord;
    @BindView(R.id.llAccountBindViewing)
    LinearLayout llAccountBindViewing;
    @BindView(R.id.llPayPassword)
    LinearLayout llPayPassword;
    //    @BindView(R.id.llOTCPaymentWay)
//    LinearLayout llOTCPaymentWay;
//    @BindView(R.id.llAddressManager)
//    LinearLayout llAddressManager;
    @BindView(R.id.llPassAndAddress)
    LinearLayout llPassAndAddress;
    @BindView(R.id.llPushSettings)
    LinearLayout llPushSettings;
    @BindView(R.id.cachePb)
    ProgressBar cachePb;
    @BindView(R.id.tvSize)
    TextView tvSize;
    @BindView(R.id.rlclearCache)
    LinearLayout rlclearCache;
    //    @BindView(R.id.llFeedback)
//    LinearLayout llFeedback;
    @BindView(R.id.llabout)
    LinearLayout llabout;
    @BindView(R.id.llShareApp)
    LinearLayout llShareApp;
    @BindView(R.id.llLogout)
    LinearLayout llLogout;
    @BindView(R.id.llIdentityAuthen)
    LinearLayout llIdentityAuthen;

    @BindView(R.id.llStockSettings)
    LinearLayout llStockSettings;


    private AsyncTask<Void, Void, String> mClearTask;
    private TjrBaseDialog exitDialog;// 清理缓存
    private GetCacheTask getCacheTask;
    private TjrBaseDialog clearUserDialog;// 注销用户

    private Call<ResponseBody> loginOutCall;

    private Call<ResponseBody> createChatTopicCall;//意见反馈获取小秘书id

    private String chatTopic;
    private String headUrl;
    private String userName;


    @Override
    protected int setLayoutId() {
        return R.layout.myhome_setting_info;
    }

    @Override
    protected String getActivityTitle() {
        return getString(R.string.settings);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.myhome_setting_info);
        ButterKnife.bind(this);
        OnClick onclick = new OnClick();

        llAccountBindViewing.setOnClickListener(onclick);
        llEditPassWord.setOnClickListener(onclick);
        rlclearCache.setOnClickListener(onclick);
        llPushSettings.setOnClickListener(onclick);
        llabout.setOnClickListener(onclick);
        llLogout.setOnClickListener(onclick);
//        llAddressManager.setOnClickListener(onclick);
        llPayPassword.setOnClickListener(onclick);
//        llindName.setOnClickListener(onclick);
//        llFeedback.setOnClickListener(onclick);
//        llPaymentTerm.setOnClickListener(onclick);
        llIdentityAuthen.setOnClickListener(onclick);
        llStockSettings.setOnClickListener(onclick);
        startGetCacheTask();

    }

    private class OnClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
//                case R.id.llPaymentTerm:
//                    PaymentTermActivity.pageJump(SettingActivity.this, 0);
//                    break;
                case R.id.llAccountBindViewing:
                    PageJumpUtil.pageJump(SettingActivity.this, SettingAccountActivity.class);
                    break;
                case R.id.llEditPassWord:// 修改密码
                    PageJumpUtil.pageJump(SettingActivity.this, MyPasswordActivity.class, null);
                    break;
                case R.id.rlclearCache:
                    if (exitDialog == null) {
                        exitDialog = new TjrBaseDialog(SettingActivity.this) {
                            @Override
                            public void setDownProgress(int progress) {
                                // 这个暂时不需要
                            }

                            @Override
                            public void onclickOk() {
                                exitDialog.dismiss();
                                startClearTask();
                            }

                            @Override
                            public void onclickClose() {
                                exitDialog.dismiss();
                            }
                        };
                        exitDialog.setTvTitle("提示");
                        exitDialog.setMessage("确定要清除缓存？");
                        exitDialog.setBtnColseText("暂不清理");
                        exitDialog.setBtnOkText("清理");
                    }
                    if (!SettingActivity.this.isFinishing() && !exitDialog.isShowing())
                        exitDialog.show();
                    break;

                case R.id.llPushSettings:
                    PageJumpUtil.pageJump(SettingActivity.this, PushSettingsActivity.class);
                    break;
//                case R.id.llStockSettings:
//                    PageJumpUtil.pageJump(SettingActivity.this, StockSettingActivity.class);
//                    break;
                case R.id.llabout:
                    PageJumpUtil.pageJump(SettingActivity.this, AboutActivity.class, null);
                    break;
                case R.id.llLogout:
                    showClearUserDialog();
                    break;
//                case R.id.llAddressManager:
//                    PageJumpUtil.pageJump(SettingActivity.this, ManagerAddressActivity.class);
//                    break;
                case R.id.llPayPassword:
                    PageJumpUtil.pageJump(SettingActivity.this, SettingPayPasswordActivity.class);
                    break;
//                case R.id.llindName:
//                    PageJumpUtil.pageJump(SettingActivity.this, IdentityAuthenActivity.class);
//
//                    break;
//                case R.id.llPublishRedOlstarCard:
//                    startGetIssueApplyUrlTask();
////                    PageJumpUtil.pageJump(SettingActivity.this,PublishCardActivity.class);
//                    break;

//                case R.id.llRedCardARScanning:
//                    PageJumpUtil.pageJump(SettingActivity.this,ARScanActivity.class);
//                    break;
                case R.id.llFeedback:
                    if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                        ChatRoomActivity.pageJump(SettingActivity.this, chatTopic, userName, headUrl);
                    } else {
                        startCreateChat();
                    }
                    break;
            case R.id.llIdentityAuthen:
                PageJumpUtil.pageJump(SettingActivity.this, IdentityAuthenActivity.class);
                break;
                case R.id.llStockSettings:
                    PageJumpUtil.pageJump(SettingActivity.this, StockSettingActivity.class);
                    break;
                default:
                    break;
            }

        }
    }


    private void startCreateChat() {
        CommonUtil.cancelCall(createChatTopicCall);
        showProgressDialog();
        createChatTopicCall = VHttpServiceManager.getInstance().getVService().createChatTopic();
        createChatTopicCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                dismissProgressDialog();
                if (resultData.isSuccess()) {
                    chatTopic = resultData.getItem("chatTopic", String.class);
                    headUrl = resultData.getItem("headUrl", String.class);
                    userName = resultData.getItem("userName", String.class);
                    if (!TextUtils.isEmpty(chatTopic) && !TextUtils.isEmpty(userName)) {
                        ChatRoomActivity.pageJump(SettingActivity.this, chatTopic, userName, headUrl);
                    } else {
                        CommonUtil.showmessage("未获取到客服信息", SettingActivity.this);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                dismissProgressDialog();
            }
        });
    }


    /**
     * 获取指定文件夹
     *
     * @param f
     * @return
     * @throws Exception
     */
    private long getFileSizes(File f) throws Exception {
        Log.d("cacheFile", "getFileSizes cacheFile==" + ImageLoader.getInstance().getDiskCache().getDirectory());
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }


    // /**
    // * 注销账号
    // */
    // public void logoutClearUser(final SharedPreferences sharedata) {
    // // 注销时调用后台解除个推push绑定
    // getApplicationContext().startLogoutUserTPushTask(getApplicationContext().getUser().getUserId(),
    // new BaseRequestListener() {
    //
    // @Override
    // public void requestStart() {
    // // TODO Auto-generated method stub
    // showProgressDialog("正在注销...");
    // }
    //
    // @Override
    // public void requestComplete(Object result) {
    // dismissProgressDialog();
    // if (result instanceof Boolean) {
    // boolean suc = (Boolean) result;
    // if (suc) {
    // ShareData.delUser(sharedata);
    // ShareData.delWeibo(sharedata);
    // ShareData.delQQ(sharedata);
    // ShareData.delWeiXin(sharedata);
    // SquareCacheData.clearSquareCache(HomeSettingActivity.this);//
    // 注销用户时清除股友吧的缓存数据
    // HomeCache.clearHomeCache(HomeSettingActivity.this);// 注销用户时清除home路牌的缓存数据
    // try {
    // StringBuffer buffer = (StringBuffer)
    // getApplicationContext().getCacheManager().getCache(CommonConst.MY_SEARCH_STOCK);
    // if (buffer != null && buffer.length() > 0) {
    // if (getApplicationContext().getUser() != null) {
    // TaojinluHttp.getInstance().addSearchStock(buffer.toString(),
    // String.valueOf(getApplicationContext().getUser().getUserId()));
    // }
    // buffer.setLength(0);
    // }
    // } catch (Exception e) {
    // }
    // getApplicationContext().setUser(null);// 注意不能设置为null
    // CacheManager.getInstance().removeAll();
    // // 停止后台服务
    // getApplicationContext().stopTPushService();
    // finishAndSendBroadcase();
    // // CommonUtil.pageJump(HomeSettingActivity.this,
    // // WelcomeIntroductionActivity.class, true, true);
    // } else {
    // CommonUtil.showToast(HomeSettingActivity.this, "注销失败", Gravity.CENTER);
    // }
    // } else {
    // CommonUtil.showToast(HomeSettingActivity.this, "注销失败", Gravity.CENTER);
    // }
    // }
    // });
    // }
    //
    // @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    // protected void finishAndSendBroadcase() {
    // Intent intent = new Intent(HomeSettingActivity.this,
    // WelcomeIntroductionActivity.class);
    // if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
    // Intent.FLAG_ACTIVITY_NEW_TASK);
    // } else {
    // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    // Intent broadcastIntent = new Intent();
    // broadcastIntent.setAction(CommonConst.ACTION_BROADCASERECIVER_FINISHONLOGOUT);
    // sendBroadcast(broadcastIntent);// 该函数用于发送广播
    // }
    // // PageJumpUtil.pageJump(HomeSettingActivity.this, intent);
    // // startActivity(intent);
    // // finish();
    // // PageJumpUtil.pageJump(this, intent);//这里不能用这个，会改变intent
    // setOverrideExitAniamtion(false);
    // startActivity(intent);
    // PageJumpUtil.finishCurr(this);
    // }

    private long getFileSize(File file) {
        long size = 0;
        FileInputStream fis = null;
        try {
            if (file.exists()) {
                fis = new FileInputStream(file);
                size = fis.available();
            } else {
                file.createNewFile();
                // Log.e("获取文件大小", "文件不存在!");
            }
        } catch (Exception e) {
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                }
            }
        }
        return size;
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    private String FormetFileSize(long fileS) throws Exception {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        // if (fileS < 1024) {
        // fileSizeString = df.format((double) fileS) + "B";
        // } else
        if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private void startClearTask() {
        CommonUtil.cancelAsyncTask(mClearTask);
        mClearTask = new ClearTask().executeParams();
    }

    private class ClearTask extends BaseAsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... arg0) {
            try {
                ImageLoader.getInstance().getDiskCache().clear();
                getApplicationContext().getRemoteResourceChatManager().clear();
                getApplicationContext().getmDCIMRemoteResourceManager().clear();
                getApplicationContext().getmVedioResourceManager().clear();
            } catch (Exception e) {
                // TODO: handle exception
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                tvSize.setText(FormetFileSize(getFileSizes(ImageLoader.getInstance().getDiskCache().getDirectory())));
                CommonUtil.showmessage("缓存已清理", SettingActivity.this);
            } catch (Exception e) {
                CommonUtil.showmessage("获取缓存大小错误", SettingActivity.this);
            }
            dismissProgressDialog();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialog("正在清理...");
        }
    }

    private void startGetCacheTask() {
        CommonUtil.cancelAsyncTask(getCacheTask);
        getCacheTask = (GetCacheTask) new GetCacheTask().executeParams();
    }

    class GetCacheTask extends BaseAsyncTask<Void, Void, Long> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Long doInBackground(Void... params) {
            try {
                return getFileSizes(ImageLoader.getInstance().getDiskCache().getDirectory());

            } catch (Exception e) {
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long result) {
            super.onPostExecute(result);
            try {
                tvSize.setText(FormetFileSize(result));
            } catch (Exception e) {
                CommonUtil.showmessage("获取缓存大小错误", SettingActivity.this);
            }
            tvSize.setVisibility(View.VISIBLE);
            cachePb.setVisibility(View.GONE);
        }

    }


    public void showClearUserDialog() {
        if (clearUserDialog == null) {// 只有要退出的时候才初始化
            clearUserDialog = new TjrBaseDialog(this) {
                @Override
                public void setDownProgress(int progress) {
                    // 这个暂时不需要
                }

                @Override
                public void onclickOk() {
                    clearUserDialog.dismiss();
//                    CommonUtil.cancelAsyncTask(logOutTask);
//                    logOutTask = (LogOutTask) new LogOutTask().executeParams();
                    CommonUtil.cancelCall(loginOutCall);
                    loginOutCall = VHttpServiceManager.getInstance().getVService().doLogout();
                    loginOutCall.enqueue(new MyCallBack(SettingActivity.this) {
                        @Override
                        protected void callBack(ResultData resultData) {
                            if (resultData.isSuccess()) {
                                dismissProgressDialog();
                                if (!TextUtils.isEmpty(resultData.msg))
                                    CommonUtil.showmessage(resultData.msg, SettingActivity.this);
//                                (new LogoutClearUser()).logoutClearUser(SettingActivity.this, LoginActivity.class);
                                (new LogoutClearUser()).logoutClearUser(SettingActivity.this, HomeActivity.class);
                                PageJumpUtil.finishCurr(SettingActivity.this);
                            }
                        }
                    });
                }

                @Override
                public void onclickClose() {
                    clearUserDialog.dismiss();
                }
            };
            clearUserDialog.setTvTitle("注销");
            clearUserDialog.setMessage("你确定要注销吗?");
            clearUserDialog.setBtnColseText("取消");
            clearUserDialog.setBtnOkText("注销");
        }
        if (!this.isFinishing() && !clearUserDialog.isShowing()) clearUserDialog.show();
    }

//    private class LogOutTask extends BaseAsyncTask<Void, Void, Boolean> {
//        private String msg = "";
//        private Exception exception;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                String jsonStr = BeebarHttp.getInstance().loginOut(String.valueOf(getApplicationContext().getUser().getUserId()));
//                //更新头像
//                if (!TextUtils.isEmpty(jsonStr)) {
//                    ResultData phoneImgData = new ResultDataParser().parse(new JSONObject(jsonStr));
//                    CommonUtil.LogLa(2, "ResultData " + jsonStr);
//                    if (phoneImgData != null && !phoneImgData.isSuccess()) {
//                        msg = phoneImgData.msg;
//                        CommonUtil.LogLa(2, "msg " + msg);
//                    }
//                    if (phoneImgData != null && phoneImgData.isSuccess()) {
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                exception = e;
//            }
//
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            dismissProgressDialog();
//            if (exception != null)
//                NotificationsUtil.ToastReasonForFailure(getApplicationContext(), exception);
//            if (!TextUtils.isEmpty(msg))
//                CommonUtil.showmessage(msg, SettingActivity.this);
//            (new LogoutClearUser()).logoutClearUser(SettingActivity.this, null);
//            PageJumpUtil.finishCurr(SettingActivity.this);
//        }
//    }

}
