//package com.cropyme.social.task;
//
//import com.cropyme.http.TaojinluHttp;
//import com.cropyme.http.util.CommonUtil;
//import com.cropyme.task.BaseAsyncTask;
//
//public class ShareAddSquareTask {
//
//	private static ShareAddSquareTask mShareAddSquareTaskInstance = null;
//
//	public synchronized static ShareAddSquareTask getInstance() {
//		if (mShareAddSquareTaskInstance == null) {
//			mShareAddSquareTaskInstance = new ShareAddSquareTask();
//		}
//		return mShareAddSquareTaskInstance;
//	}
//
//	private AddSquareTask addSquareTask;
//
//	public void startAddTalkieTask(long userId, String say, String stype, String title, String content, String params, String pkg, String cls, String pview) {
//		CommonUtil.cancelAsyncTask(addSquareTask);
//		addSquareTask = (AddSquareTask) new AddSquareTask(userId, stype, say, title, content, params, pkg, cls, pview).executeParams();
//	}
//
//	class AddSquareTask extends BaseAsyncTask<Void, Void, Boolean> {
//		private long userId;
//		private String say;
//
//		private String stype;
//		private String title;
//		private String content;
//		private String params;
//		private String pkg;
//		private String cls;
//		private String pview;
//
//		public AddSquareTask(long userId, String stype, String say, String title, String content, String params, String pkg, String cls, String pview) {
//			this.say = say;
//			this.stype = stype;
//			this.title = title;
//			this.content = content;
//			this.params = params;
//			this.pkg = pkg;
//			this.cls = cls;
//			this.pview = pview;
//			this.userId = userId;
//		}
//
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//		}
//
//		@Override
//		protected Boolean doInBackground(Void... ps) {
//			Boolean ret = false;
//			try {
//				String result = TaojinluHttp.getInstance().addSquare(0, userId, "share", say, stype, title, content, params, null, pkg, cls, pview);
//				if (result != null) {
//
//				}
//				return ret;
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			return false;
//		}
//
//		@Override
//		protected void onPostExecute(Boolean result) {
//			super.onPostExecute(result);
//		}
//	}
//}
