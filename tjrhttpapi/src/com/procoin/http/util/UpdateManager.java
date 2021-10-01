//package com.cropyme.http.util;
//
//import android.app.Activity;
//import android.app.AlertDialog;
//import android.app.AlertDialog.Builder;
//import android.app.Dialog;
//import android.content.DialogInterface;
//import android.content.DialogInterface.OnClickListener;
//import android.content.Intent;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.Message;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup.LayoutParams;
//import android.webkit.URLUtil;
//import android.widget.ProgressBar;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.cropyme.http.R;
//import Components;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class UpdateManager {
//
//    private Activity mActivity;
//
//    // 提示语
//    private final String updateMsg = "有新版本，赶快下载吧~";
//
//    private String pakname = "淘金路";
//
//    private Dialog noticeDialog;
//
//    private Dialog downloadDialog;
//
//    /* 进度条与通知ui刷新的handler和msg常量 */
//    private ProgressBar mProgress;
//
//    private static final int DOWN_UPDATE = 1;
//
//    private static final int DOWN_OVER = 2;
//
//    private int progress;
//
//    private Thread downLoadThread;
//
//    private boolean interceptFlag = false;
//
//    // private String apk;
//    //
//    // private String apkUrl;
//
//    private File apkFile;
//
//    private Components components;
//
//    private Handler mHandler = new Handler() {
//        public void handleMessage(Message msg) {
//            switch (msg.what) {
//                case DOWN_UPDATE:
//                    mProgress.setProgress(progress);
//                    break;
//                case DOWN_OVER:
//                    installApk(apkFile);
//                    break;
//                default:
//                    break;
//            }
//        }
//
//        ;
//    };
//
//    public UpdateManager(Activity activity, Components components) {
//        this.mActivity = activity;
//        this.components = components;
//        if (components != null && components.appName != null) pakname = components.appName;
//    }
//
//    public void setUpdateManager(Activity activity, Components components) {
//        this.mActivity = activity;
//        this.components = components;
//        if (components != null && components.app_name != null) pakname = components.app_name;
//    }
//
//    // 外部接口让主Activity调用
//    public void checkUpdateInfo() {
//        showNoticeDialog();
//    }
//
//    public void dismissDialog() {
//        if (noticeDialog != null) noticeDialog.dismiss();
//    }
//
//    private void showNoticeDialog() {
//        AlertDialog.Builder builder = new Builder(mActivity);
//        builder.setTitle(pakname + "版本更新");
//        if (components != null && components.bug_msg != null && !"".equals(components.bug_msg)) {
//            ScrollView scrollView = new ScrollView(mActivity);
//            scrollView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//            TextView tView = new TextView(mActivity);
//            tView.setTextSize(16);
//            tView.setTextColor(Color.WHITE);
//            tView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
//            tView.setText(Html.fromHtml(components.bug_msg));
//            scrollView.addView(tView);
//            builder.setView(scrollView);
//        } else {
//            builder.setMessage(pakname + updateMsg);
//        }
//        builder.setPositiveButton("下载", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                showDownloadDialog();
//            }
//        });
//        builder.setNegativeButton("以后再说", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        noticeDialog = builder.create();
//        noticeDialog.show();
//    }
//
//    private void showDownloadDialog() {
//        AlertDialog.Builder builder = new Builder(mActivity);
//        builder.setTitle("软件版本更新");
//
//        final LayoutInflater inflater = LayoutInflater.from(mActivity);
//        View v = inflater.inflate(R.layout.app_update_progress, null);
//        mProgress = (ProgressBar) v.findViewById(R.id.progress);
//
//        builder.setView(v);
//        builder.setNegativeButton("取消", new OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//                interceptFlag = true;
//            }
//        });
//        downloadDialog = builder.create();
//        downloadDialog.setCancelable(false);
//        downloadDialog.show();
//        downloadApk();
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
//                File baseDirectory = new File(Environment.getExternalStorageDirectory(), "perval");
//                apkFile = new File(baseDirectory, components.download_url);
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
//    private void downloadApk() {
//        downLoadThread = new Thread(mdownApkRunnable);
//        downLoadThread.start();
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
//        intent.setDataAndType(Uri.fromFile(f), type);
//        mActivity.startActivity(intent);
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
//}