//package com.cropyme.http.widget.dialog;
//
//import android.app.Activity;
//
//import Components;
//import Placard;
//import com.cropyme.http.widget.dialog.DownLoadDialogManager.DownLoadDialogManagerInterface;
//
//public class DownAndNoticeDialogManager implements DownLoadDialogManagerInterface {
//
//	private NoticeDialogManager noticedialogManager; // 通知管理
//
//	private DownLoadDialogManager downLoadDialogManager;
//
//	public DownAndNoticeDialogManager(Activity activity, Components components, Placard placard) {
//		downLoadDialogManager = new DownLoadDialogManager(activity, components);
//		downLoadDialogManager.setDownLoadDialogManagerInterface(this);
//		noticedialogManager = new NoticeDialogManager(activity, components, placard);
//	}
//
//	public void setUpdateManager(Activity activity, Components components, Placard placard) {
//		noticedialogManager.setUpdateManager(activity, components, placard);
//		downLoadDialogManager.setUpdateManager(activity, components);
//	}
//
//	/**
//	 * 检查是否显示通知
//	 */
//	public void checkNoticeDialog() {
//		if (noticedialogManager != null) noticedialogManager.checkNoticeDialog();
//	}
//
//	public void checkUpdateDialog() {
//		downLoadDialogManager.checkUpdateDialog();
//	}
//}
