package com.procoin.updatedialog;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.procoin.R;
import com.procoin.http.model.Components;
import com.procoin.http.widget.dialog.ui.TjrBaseDownDialog;
import com.procoin.module.dialog.TjrUpdateDialog;
import com.procoin.util.CommonUtil;
import com.procoin.util.DynamicPermission;
import com.procoin.util.PermissionUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DownLoadDialogManager {

    private TjrBaseDownDialog tjrBaseDownDialog;
    private TjrUpdateDialog tjrcheckDownDialog; // 先提示
    //    private TjrBaseDialog installDialog; // 开启安装权限提示
    protected AppCompatActivity mActivity; // 页面的内容
    protected Components components;// 组件信息
    // 提示语
    protected final String updateMsg = "有新版本，赶快下载吧~";// 默认内容
    private String apkName = "W.W.C.T"; // 组件默认名字
    private static final String fileDir = "cropyme";
    private static final String fileApk = "cropyme.apk";
    private Thread downLoadThread;// 下载刻币跟单线程

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private File apkFile; // 文件的名字
    private int progress; // 下载的
    private boolean interceptFlag = false;// 判断下载的状态

    private DynamicPermission dynamicPermissionStorage;
//    private DynamicPermission dynamicPermissionInstall;

    private DownLoadDialogManagerInterface downLoadDialogManagerInterface;

    public void setDownLoadDialogManagerInterface(DownLoadDialogManagerInterface downLoadDialogManagerInterface) {
        this.downLoadDialogManagerInterface = downLoadDialogManagerInterface;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    if (tjrBaseDownDialog != null) tjrBaseDownDialog.setProgressSource(progress);
                    break;
                case DOWN_OVER:
                    if (tjrBaseDownDialog != null) tjrBaseDownDialog.dismiss();
                    if (downLoadDialogManagerInterface != null) {
                        downLoadDialogManagerInterface.checkNoticeDialog();
                    }

//                    installApk(apkFile);
                    checkInstallPermission();
                    break;
                default:
                    break;
            }
        }

        ;
    };


    public DownLoadDialogManager(AppCompatActivity activity, Components components) {
        this.mActivity = activity;
        this.components = components;
        if (this.components != null && !TextUtils.isEmpty(this.components.appName))
            apkName = this.components.appName;
    }

    public void setUpdateManager(AppCompatActivity activity, Components components) {
        this.mActivity = activity;
        this.components = components;
        if (this.components != null && !TextUtils.isEmpty(this.components.appName))
            apkName = this.components.appName;
    }

    /**
     * 获取组件名字
     *
     * @return
     */
    public String getApkname() {
        return apkName;
    }

    public void setApkname(String apkName) {
        this.apkName = apkName;
    }

    /**
     * 获取标题信息
     *
     * @return
     */
    public CharSequence getStrTitle() {
//        return (apkName + "版本更新");
        return "版本更新";
    }

    /**
     * 获取内容信息
     *
     * @return
     */
    public CharSequence getStrMessage() {
        if (components != null && components.bugMsg != null && !"".equals(components.bugMsg)) {
            return Html.fromHtml(components.bugMsg);
        } else {
            return (apkName + updateMsg);
        }

    }

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                Log.d("mdownApkRunnable", "components.download_url==" + components.downloadUrl);
                if (components == null) return;
                if (components.appName == null || components.downloadUrl == null || !URLUtil.isNetworkUrl(components.downloadUrl)) {
                    return;
                }
                URL url = new URL(components.downloadUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File baseDirectory = new File(Environment.getExternalStorageDirectory(), fileDir);
                apkFile = new File(baseDirectory, fileApk);
                delFile(apkFile.getPath());
                FileOutputStream fos = new FileOutputStream(apkFile);

                int count = 0;
                byte buf[] = new byte[1024];

                do {
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    // 更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        // 下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);// 点击取消就停止下载.

                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                Log.d("mdownApkRunnable", "MalformedURLException==" + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("mdownApkRunnable", "IOException==" + e.getMessage());
            }

        }
    };

    /**
     * 下载apk
     */

    public void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 停止下载
     */
    public void stopdownloadApk() {
        interceptFlag = true;
    }


    /**
     * 安装apk
     */
    private void installApk(File f) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(f);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            intent.setDataAndType(Uri.fromFile(f), type);
        } else {
            Uri uri = FileProvider.getUriForFile(mActivity, mActivity.getString(R.string.fileprovider_authority), f);
            Log.d("mdownApkRunnable", "uri==" + uri);
            intent.setDataAndType(uri, type);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
//        intent.setDataAndType(Uri.fromFile(f), type);
        mActivity.startActivity(intent);

    }

    public void delFile(String filepath) {
        File myFile = new File(filepath);
        if (myFile.exists()) {
            myFile.delete();
        } else {
            boolean mkdirs = myFile.getParentFile().mkdirs();
            Log.d("mdownApkRunnable", "mkdirs==" + mkdirs);
        }
    }

    /**
     * 判断打开的文件
     *
     * @param f
     * @return
     */
    private String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    public interface DownLoadDialogActionInterface {
        public void updateProgress(int progress);// 更新进度条

        public void endProgress();// 结束
    }

    private void showtjrDownloadDialog() {
        tjrBaseDownDialog = new TjrBaseDownDialog(mActivity) {

            @Override
            public void setDownProgress(int progress) {

            }

            @Override
            public void onclickOk() {
                // 无
            }

            @Override
            public void onclickClose() {
                stopdownloadApk();
                //
                if (downLoadDialogManagerInterface != null)
                    downLoadDialogManagerInterface.checkNoticeDialog();

            }
        };
        tjrBaseDownDialog.setCancelable(false);// 禁止关闭
        tjrBaseDownDialog.setTitle("软件版本更新");
        tjrBaseDownDialog.setBtnColseText("关闭");
        if (!mActivity.isFinishing() && !tjrBaseDownDialog.isShowing()) {
            tjrBaseDownDialog.show();
            if (downLoadThread != null && downLoadThread.isAlive()) {
            } else {
                downloadApk();
            }

        }
    }


    private void initDynamicPermissionStorage() {//如果更新app需要申请存储权限，否则无法创建文件
        if (dynamicPermissionStorage == null) {
            dynamicPermissionStorage = new DynamicPermission(mActivity, new DynamicPermission.RequestPermissionsCallBack() {
                @Override
                public void onRequestSuccess(String[] permissions, int requestCode) {
                    if (requestCode == 100) {
                        showtjrDownloadDialog();// 下载
                    }
                }

                @Override
                public void onRequestFail(String[] permissions, int requestCode) {
                }
            });
        }
        dynamicPermissionStorage.checkSelfPermission(PermissionUtils.EXTERNAL_STORAGE, 100);
    }

    private void checkInstallPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (mActivity.getPackageManager().canRequestPackageInstalls()) {
                installApk(apkFile);
            } else {
                goOpenInstall();
            }
        } else {
            installApk(apkFile);
        }
    }

    private void goOpenInstall() {//手动开启未知来源安装

//        ActivityCompat.requestPermissions(mActivity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},200);
        Uri uri = Uri.parse("package:" + mActivity.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri);
        mActivity.startActivityForResult(intent, 0x123);
    }

//    private void openInstallTipsDialog() {
//        installDialog = new TjrBaseDialog(mActivity) {
//
//            @Override
//            public void setDownProgress(int progress) {
//
//            }
//
//            @Override
//            public void onclickOk() {
//                goOpenInstall();
//            }
//
//            @Override
//            public void onclickClose() {
//            }
//        };
//
//        installDialog.setTvTitle("提示");
//        installDialog.setMessage("允许来自此来源的应用");
//        installDialog.setBtnColseText("取消");
//        installDialog.setBtnOkText("去开启");
//        if (!mActivity.isFinishing() && !installDialog.isShowing()) installDialog.show();
//    }
//
//    private void initDynamicPermissionInstall() {//安装apk权限
//        if (dynamicPermissionInstall == null) {
//            dynamicPermissionInstall = new DynamicPermission(mActivity, new DynamicPermission.RequestPermissionsCallBack() {
//                @Override
//                public void onRequestSuccess(String[] permissions, int requestCode) {
//                    if (requestCode == 200) {
//                        installApk(apkFile);
//                    }
//                }
//
//                @Override
//                public void onRequestFail(String[] permissions, int requestCode) {
//                }
//            });
//        }
//        dynamicPermissionInstall.checkSelfPermission(PermissionUtils.INSTALL_PACKAGES, 200);
//    }


    /**
     *
     */
    private void initcheckDownDialog() {
        if (components == null) return;
        tjrcheckDownDialog = new TjrUpdateDialog(mActivity) {

            @Override
            public void setDownProgress(int progress) {
                // 不需要操作

            }

            @Override
            public void onclickOk() {
                initDynamicPermissionStorage();
            }

            @Override
            public void onclickClose() {
                if (downLoadDialogManagerInterface != null)
                    downLoadDialogManagerInterface.checkNoticeDialog();
            }
        };

        tjrcheckDownDialog.setTvTitle(getStrTitle());
        tjrcheckDownDialog.setMessage(getStrMessage());
        tjrcheckDownDialog.setBtnColseText("稍后再说");
        tjrcheckDownDialog.setBtnOkText("立即升级");
        if (components.isForce == 1) {
            tjrcheckDownDialog.setCancelable(false);
            tjrcheckDownDialog.setBtnColseVisibility(View.GONE);
        }
        if (!mActivity.isFinishing() && !tjrcheckDownDialog.isShowing()) tjrcheckDownDialog.show();
    }

    /**
     * 显示需要显示的按钮
     */
    public void checkUpdateDialog() {
        initcheckDownDialog();
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (dynamicPermissionStorage != null)
            dynamicPermissionStorage.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void onActivityResult(int resultCode) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            installApk(apkFile);
        } else {
            CommonUtil.showmessage("允许来自此来源的应用后才能更新", mActivity);
        }
    }

    public interface DownLoadDialogManagerInterface {
        /**
         * 显示弹出对话框
         */
        public void checkNoticeDialog();

    }

    public void dismissDialog() {
        if (tjrcheckDownDialog != null) tjrcheckDownDialog.dismiss();
    }

}
