package com.procoin.updatedialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.procoin.http.model.Components;
import com.procoin.http.model.Placard;
import com.procoin.http.widget.dialog.NoticeDialogManager;

public class DownAndNoticeDialogManager implements DownLoadDialogManager.DownLoadDialogManagerInterface {

	private NoticeDialogManager noticedialogManager; // 通知管理

	private DownLoadDialogManager downLoadDialogManager;//下载更新

	public DownAndNoticeDialogManager(AppCompatActivity activity, Components components, Placard placard) {
		downLoadDialogManager = new DownLoadDialogManager(activity, components);
		downLoadDialogManager.setDownLoadDialogManagerInterface(this);
		noticedialogManager = new NoticeDialogManager(activity, components, placard);
	}

	public void setUpdateManager(AppCompatActivity activity, Components components, Placard placard) {
		noticedialogManager.setUpdateManager(activity, components, placard);
		downLoadDialogManager.setUpdateManager(activity, components);
	}

	/**
	 * 检查是否显示通知
	 */
//	public void checkNoticeDialog() {
//		if (noticedialogManager != null) noticedialogManager.checkNoticeDialog();
//	}

	@Override
	public void checkNoticeDialog() {
		if (noticedialogManager != null) noticedialogManager.checkNoticeDialog();
	}
	public void checkUpdateDialog() {
		if(downLoadDialogManager!=null)downLoadDialogManager.checkUpdateDialog();
	}

	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(downLoadDialogManager!=null)downLoadDialogManager.onRequestPermissionsResult(requestCode,permissions,grantResults);
	}

	public void onActivityResult(int requestCode) {
		if(downLoadDialogManager!=null)downLoadDialogManager.onActivityResult(requestCode);
	}



}
