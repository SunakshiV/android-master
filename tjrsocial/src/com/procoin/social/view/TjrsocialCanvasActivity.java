package com.procoin.social.view;

import com.procoin.social.util.CommonUtil;
import com.procoin.social.baseui.AbstractBaseActivity;

public class TjrsocialCanvasActivity extends AbstractBaseActivity {
//	private Bundle bundle;
//	private Bitmap viewBitmap;
////	private ChatDragpicDoodle chatDragpicDoodle;
//	private String activityName;// 要去的activity
//	private int resultType;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		getdata(this.getIntent().getExtras());
//
//	}
//
//	@Override
//	protected void onNewIntent(Intent intent) {
//		super.onNewIntent(intent);
//		getdata(intent.getExtras());
//	}
//
//	private void getdata(Bundle ibundle) {
//		bundle = ibundle;
//		if (bundle != null) {
//			viewBitmap = CommonUtil.BytesToBitmap(bundle.getByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP));
//			activityName = bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY);
//			resultType = bundle.getInt(TjrSocialShareConfig.KEY_EXTRAS_RESULTTYPE, 0);
//		}
//
//		if (chatDragpicDoodle == null) {
//			chatDragpicDoodle = new ChatDragpicDoodle(this, viewBitmap, true);
//			chatDragpicDoodle.showView();
//			chatDragpicDoodle.setCallback(new ChatDragpicDoodleCallback() {
//				@Override
//				public void doodlesendBitmap(Bitmap bitmap) {
//					// TODO sd卡判断
//					// String fileName =
//					// VeDate.getyyyyMMddHHmmss(VeDate.getNow()) +
//					// "_share.png.bm";
//					if (bitmap != null) {
//						if (CommonUtil.isSdCard()) {
//							try {
//								// CommonUtil.writeFile(CommonUtil.GetWeiboFile(fileName),
//								// bitmap, false);
//								// TODO 这个
//								bundle.remove(TjrSocialShareConfig.KEY_EXTRAS_BITMAP);
//								bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, CommonUtil.bitmapToBytes(bitmap));
//								switch (resultType) {
//								case 0:
//									if (activityName != null && !"".equals(activityName)) {// 跳转到相应的页面
//										Class<?> activityClass = CommonUtil.getClass(activityName);
//										CommonUtil.pageJumpToData(TjrsocialCanvasActivity.this, activityClass, false, true, bundle);
//									} else {
//										CommonUtil.showToast(TjrsocialCanvasActivity.this, "请重新进入", Gravity.BOTTOM);
//										goback();
//									}
//									break;
//								case 1:
//									Intent intent = new Intent();
//									intent.putExtras(bundle);
//									setResult(0x520, intent);
//									goback();
//									break;
//								default:
//									break;
//								}
//							} catch (Exception e) {
//								CommonUtil.showToast(TjrsocialCanvasActivity.this, "发送失败,原因你可能取出sd card了,请检查", Gravity.CENTER);
//								chatDragpicDoodle.setSend(false);// 发送完成后请设置未false
//								goback();
//							}
//						}
//					}
//				}
//
//				@Override
//				public void doodleGoback() {
//					goback();
//				}
//			});
//		}
//
//		setContentView(chatDragpicDoodle);
//	}

    @Override
    public void goback() {
        CommonUtil.pageJump(this, null, true, true);
    }

}
