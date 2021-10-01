package com.procoin.social.baseui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.social.util.CommonUtil;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class AbstractBaseActivity extends AppCompatActivity {
	private ProgressDialog progressDialog;
	private boolean destroyed = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}

	@Override
	protected void onResume() {
		super.onResume();

		CommonUtil.LogLa(2, "********  Class is *******" + this.getClass().getName());
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destroyed = true;
		System.gc();
	}

	@Override
	public synchronized boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下手机物理返回键

		if (event.getRepeatCount() == 0) {

			switch (keyCode) {
			case KeyEvent.KEYCODE_BACK:
				goback();
				break;

			default:
				break;
			}
		}

		return super.onKeyDown(keyCode, event);
	}

	public void showLoadingProgressDialog(CharSequence message) {
		try {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setIndeterminate(true);
				progressDialog.setOwnerActivity(this);
				progressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						// 取消Dialog时，调用dialogCancel()
						dialogCancel();
					}
				});
			}
			progressDialog.setMessage(message);
			progressDialog.show();
		} catch (Exception e) {
			Log.e("Exception", "showProgressDialog  " + e.toString());
		}
	}

	public void dismissProgressDialog() {
		if (progressDialog != null && !destroyed) {
			progressDialog.dismiss();
		}
	}

	public int getScreenWidth() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Log.d("t", "........ScreenWidth()=" + display.getWidth());
		return display.getWidth();
	}

	public int getScreenHeight() {
		WindowManager windowManager = getWindowManager();
		Display display = windowManager.getDefaultDisplay();
		Log.d("t", "........ScreenHeight()=" + display.getHeight());
		return display.getHeight();
	}

	public void setProgressDialogCancled(boolean show) {
		if (progressDialog != null) {
			progressDialog.setCancelable(show);
			progressDialog.setCanceledOnTouchOutside(show);
		}
	}

	public void showProgressDialog(CharSequence message) {
		try {
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(this);
				progressDialog.setIndeterminate(true);
				progressDialog.setOwnerActivity(this);
				progressDialog.setOnCancelListener(new OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						// 取消Dialog时，调用dialogCancel()
						dialogCancel();
					}
				});
			}
			progressDialog.setMessage(message);
			progressDialog.show();
		} catch (Exception e) {
			Log.e("Exception", "showProgressDialog  " + e.toString());
		}
	}

	/**
	 * 返回
	 */
	public abstract void goback();

	/**
	 * 加载页面
	 * 
	 * @param resource
	 * @param content
	 * @return
	 */
	public View inflateView(int resource) {
		return ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(resource, null);
	}

	/**
	 * json中是否有该字段,并且该字段不为空
	 * 
	 * @param json
	 * @param name
	 *            字段名
	 * @return
	 * @throws JSONException
	 */
	public boolean hasAndNotNull(JSONObject json, String name) throws JSONException {
		if (json != null && name != null) {
			return json.has(name) && !json.isNull(name) && //
					json.getString(name) != null && !"".equals(json.getString(name));
		} else return false;
	}

	/**
	 * 等待圈圈被用户取消时调用,如果有需要的话.在子类里重就可以了.
	 */
	protected void dialogCancel() {
		// 当等待圈圈被用户取消时的操作
	}

}
