//package com.cropyme.http.widget.dialog;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Html;
//import android.view.View;
//import android.webkit.URLUtil;
//
//import Components;
//import com.cropyme.http.widget.dialog.ui.TjrBaseDialog;
//import TjrBaseDownDialog;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class DownLoadDialogManager {
//
//    private TjrBaseDownDialog tjrBaseDownDialog;
//    private TjrBaseDialog tjrcheckDownDialog; // 先提示
//
//    // 提示语
//    protected final String updateMsg = "有新版本，赶快下载吧~";// 默认内容
//
//    protected String pakname = "去链儿"; // 组件默认名字
//    private Thread downLoadThread;// 下载淘金路线程
//
//    private static final int DOWN_UPDATE = 1;
//
//    private static final int DOWN_OVER = 2;
//    private File apkFile; // 文件的名字
//
//    private int progress; // 下载的
//
//    private boolean interceptFlag = false;// 判断下载的状态
//
//    private DownLoadDialogManagerInterface downLoadDialogManagerInterface;
//
//    public void setDownLoadDialogManagerInterface(DownLoadDialogManagerInterface downLoadDialogManagerInterface) {
//        this.downLoadDialogManagerInterface = downLoadDialogManagerInterface;
//    }
//
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DOWN_UPDATE:
//                    if (tjrBaseDownDialog != null) tjrBaseDownDialog.setProgressSource(progress);
//                    break;
//                case DOWN_OVER:
//                    if (tjrBaseDownDialog != null) tjrBaseDownDialog.dismiss();
//                    if (downLoadDialogManagerInterface != null){
//                        downLoadDialogManagerInterface.checkNoticeDialog();
//                    }
//                    installApk(apkFile);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        ;
//    };
//    protected Activity mActivity; // 页面的内容
//
//    protected Components components;// 组件信息
//
//    public DownLoadDialogManager(Activity activity, Components components) {
//        this.mActivity = activity;
//        this.components = components;
//        if (this.components != null && this.components.app_name != null)
//            pakname = this.components.app_name;
//    }
//
//    public void setUpdateManager(Activity activity, Components components) {
//        this.mActivity = activity;
//        this.components = components;
//        if (this.components != null && this.components.app_name != null)
//            pakname = this.components.app_name;
//    }
//
//    /**
//     * 获取组件名字
//     *
//     * @return
//     */
//    public String getPakname() {
//        return pakname;
//    }
//
//    public void setPakname(String pakname) {
//        this.pakname = pakname;
//    }
//
//    /**
//     * 获取标题信息
//     *
//     * @return
//     */
//    public CharSequence getStrTitle() {
//        return (pakname + "版本更新");
//    }
//
//    /**
//     * 获取内容信息
//     *
//     * @return
//     */
//    public CharSequence getStrMessage() {
//        if (components != null && components.bug_msg != null && !"".equals(components.bug_msg)) {
//            return Html.fromHtml(components.bug_msg);
//        } else {
//            return (pakname + updateMsg);
//        }
//    }
//
//    private Runnable mdownApkRunnable = new Runnable() {
//        @Override
//        public void run() {
//            try {
//                if (components == null) return;
//                if (components.app_name == null || components.download_url == null || !URLUtil.isNetworkUrl(components.download_url)) {
//                    return;
//                }
//                URL url = new URL(components.download_url);
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.connect();
//                int length = conn.getContentLength();
//                InputStream is = conn.getInputStream();
//
//                File baseDirectory = new File(Environment.getExternalStorageDirectory(), "qulianr");
//                apkFile = new File(baseDirectory, "qulianr.apk");
//                delFile(apkFile.getPath());
//                FileOutputStream fos = new FileOutputStream(apkFile);
//
//                int count = 0;
//                byte buf[] = new byte[1024];
//
//                do {
//                    int numread = is.read(buf);
//                    count += numread;
//                    progress = (int) (((float) count / length) * 100);
//                    // 更新进度
//                    mHandler.sendEmptyMessage(DOWN_UPDATE);
//                    if (numread <= 0) {
//                        // 下载完成通知安装
//                        mHandler.sendEmptyMessage(DOWN_OVER);
//                        break;
//                    }
//                    fos.write(buf, 0, numread);
//                } while (!interceptFlag);// 点击取消就停止下载.
//
//                fos.close();
//                is.close();
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//        }
//    };
//
//    /**
//     * 下载apk
//     */
//
//    public void downloadApk() {
//        downLoadThread = new Thread(mdownApkRunnable);
//        downLoadThread.start();
//    }
//
//    /**
//     * 停止下载
//     */
//    public void stopdownloadApk() {
//        interceptFlag = true;
//    }
//
//    /**
//     * 安装apk
//     */
//    private void installApk(File f) {
//        Intent intent = new Intent();
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        String type = getMIMEType(f);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
//            intent.setDataAndType(Uri.fromFile(f), type);
//        } else {
//            intent.setDataAndType(FileProvider.getUriForFile(mActivity, "com.apkinstall.demo.file_provider", f), type);
//            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        }
////        intent.setDataAndType(Uri.fromFile(f), type);
//        mActivity.startActivity(intent);
//
//    }
//
//    public void delFile(String filepath) {
//        File myFile = new File(filepath);
//        if (myFile.exists()) {
//            myFile.delete();
//        }
//    }
//
//    /**
//     * 判断打开的文件
//     *
//     * @param f
//     * @return
//     */
//    private String getMIMEType(File f) {
//        String type = "";
//        String fName = f.getName();
//        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();
//        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
//            type = "audio";
//        } else if (end.equals("3gp") || end.equals("mp4")) {
//            type = "video";
//        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
//            type = "image";
//        } else if (end.equals("apk")) {
//            type = "application/vnd.android.package-archive";
//        } else {
//            type = "*";
//        }
//        if (end.equals("apk")) {
//        } else {
//            type += "/*";
//        }
//        return type;
//    }
//
//    public interface DownLoadDialogActionInterface {
//        public void updateProgress(int progress);// 更新进度条
//
//        public void endProgress();// 结束
//    }
//
//    private void showtjrDownloadDialog() {
//        tjrBaseDownDialog = new TjrBaseDownDialog(mActivity) {
//
//            @Override
//            public void setDownProgress(int progress) {
//
//            }
//
//            @Override
//            public void onclickOk() {
//                // 无
//            }
//
//            @Override
//            public void onclickClose() {
//                //
//                if (downLoadDialogManagerInterface != null)
//                    downLoadDialogManagerInterface.checkNoticeDialog();
//
//            }
//        };
//        tjrBaseDownDialog.setCancelable(false);// 禁止关闭
//        tjrBaseDownDialog.setTitle("软件版本更新");
//        tjrBaseDownDialog.setBtnColseText("关闭");
//        if (!mActivity.isFinishing() && !tjrBaseDownDialog.isShowing()) {
//            tjrBaseDownDialog.show();
//            if (downLoadThread != null && downLoadThread.isAlive()) {
//            } else {
//                downloadApk();
//            }
//
//        }
//    }
//
//    /**
//     *
//     */
//    private void initcheckDownDialog() {
//        if (components == null) return;
//        tjrcheckDownDialog = new TjrBaseDialog(mActivity) {
//
//            @Override
//            public void setDownProgress(int progress) {
//                // 不需要操作
//
//            }
//
//            @Override
//            public void onclickOk() {
//                showtjrDownloadDialog();// 下载
//            }
//
//            @Override
//            public void onclickClose() {
//                if (downLoadDialogManagerInterface != null)
//                    downLoadDialogManagerInterface.checkNoticeDialog();
//            }
//        };
//
//        tjrcheckDownDialog.setTvTitle(getStrTitle());
//        tjrcheckDownDialog.setMessage(getStrMessage());
//        tjrcheckDownDialog.setBtnColseText("取消");
//        tjrcheckDownDialog.setBtnOkText("更新");
//        if (components.is_force == 1) {
//            tjrcheckDownDialog.setCancelable(false);
//            tjrcheckDownDialog.setBtnColseVisibility(View.GONE);
//        }
//        if (!mActivity.isFinishing() && !tjrcheckDownDialog.isShowing()) tjrcheckDownDialog.show();
//    }
//
//    /**
//     * 显示需要显示的按钮
//     */
//    public void checkUpdateDialog() {
//        initcheckDownDialog();
//    }
//
//    public interface DownLoadDialogManagerInterface {
//        /**
//         * 显示弹出对话框
//         */
//        public void checkNoticeDialog();
//
//    }
//
//    public void dismissDialog() {
//        if (tjrcheckDownDialog != null) tjrcheckDownDialog.dismiss();
//    }
//
//}
