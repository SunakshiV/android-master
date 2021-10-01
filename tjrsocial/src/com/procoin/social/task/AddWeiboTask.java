//package com.cropyme.social.task;
//
//import java.io.File;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
//import android.graphics.Bitmap;
//
//import TaojinluHttp;
//import NotificationsUtil;
//import com.cropyme.social.R;
//import AbstractBaseActivity;
//import CommonUtil;
//import BaseAsyncTask;
//
//public class AddWeiboTask extends BaseAsyncTask<Void, Void, String> {
//	private Exception exception;
//	private int weiboType;
//	private String text;
//	private Bitmap bmp;
//	private String voteTitle;
//	private int voteType;
//	private String option;
//	private AbstractBaseActivity activity;
//	private long userId;
//	private String jsonStr;
//	private String fileName, fileUrl;// 文件的路径
//
//	public AddWeiboTask(AbstractBaseActivity activity, int weiboType, String text, Bitmap bmp, String voteTitle, int voteType, String option, long userId, String jsonStr) {
//		this.weiboType = weiboType;
//		this.text = text;
//		this.bmp = bmp;
//		this.voteTitle = voteTitle;
//		this.voteType = voteType;
//		this.option = option;
//		this.activity = activity;
//		this.userId = userId;
//		this.jsonStr = jsonStr;
//	}
//
//	@Override
//	protected void onPreExecute() {
//		activity.showLoadingProgressDialog(activity.getResources().getString(R.string.loading_date_message));
//	}
//
//	@Override
//	protected String doInBackground(Void... params) {
//		try {
//			int cType = 1, isFile = 0;// cType=1纯文字,cType=2图片,cType=3当json
//			if (bmp != null) {
//				isFile = 1;
//				cType = 2;
//				createFileInfo(bmp);// 创建图片
//			}
//			if (jsonStr != null) {
//				cType = 3;
//				isFile = 0;
//			}
//			JSONArray contenjson = new JSONArray();
//			if (cType == 3) {
//				contenjson.put(new JSONObject().put("cType", cType).put("content", jsonStr).put("isFile", isFile));
//			} else if (cType == 2) {
//				contenjson.put(new JSONObject().put("cType", cType).put("content", fileName).put("isFile", isFile));
//			} else {
//				contenjson.put(new JSONObject().put("cType", cType).put("content", "").put("isFile", isFile));
//			}
//			String json = TaojinluHttp.getInstance().addWeiboAndOption(weiboType, text, contenjson.toString(), voteTitle, voteType, option, userId, fileName, fileUrl, isFile);
//			CommonUtil.LogLa(4, "json=" + json);
//			return "1";
//		} catch (Exception e) {
//			exception = e;
//			return null;
//		}
//	}
//
//	/**
//	 * 创建图片
//	 *
//	 * @param bmp
//	 */
//	private void createFileInfo(Bitmap bmp) throws Exception {
//		if (fileName == null) fileName = CommonUtil.getFileName(userId);
//		File file = CommonUtil.GetWeiboFile(fileName);
//		fileUrl = file.getPath();
//		CommonUtil.LogLa(2, "    filename=" + fileName + " fileUrl=" + fileUrl);
//		CommonUtil.writeFile(file, bmp, false);
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		activity.dismissProgressDialog();
//		if (exception != null) {
//			NotificationsUtil.ToastReasonForFailure(activity, exception);
//		} else {
//
//		}
//	}
//
//}