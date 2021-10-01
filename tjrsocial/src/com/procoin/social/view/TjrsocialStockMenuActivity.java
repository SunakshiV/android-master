package com.procoin.social.view;

import com.procoin.social.baseui.AbstractBaseActivity;
import com.procoin.social.util.CommonUtil;

public class TjrsocialStockMenuActivity extends AbstractBaseActivity {
    //
//    private StockKeyboard keyboard;
//    private ChatStockmenu menu;
//    private Bundle bundle;
//    private int resultType;
//    private long userId;
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (keyboard != null) {
//            keyboard.dismissPopuWindow();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        bundle = this.getIntent().getExtras();
//        menu = new ChatStockmenu(this);
//        if (bundle != null) {
//            menu.setType(bundle.getInt(TjrSocialShareConfig.KEY_EXTRAS_TYPE, 0));
//            resultType = bundle.getInt(TjrSocialShareConfig.KEY_EXTRAS_RESULTTYPE, 0);
//            userId = bundle.getLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, 0);
//        }
//
//        setContentView(menu.showView());
//        menu.setCallBack(new ChatStockmenuCallBack() {
//
//            @Override
//            public void showKeyboard(View v) {
//                if (keyboard != null) {
//                    keyboard.dismissPopuWindow();
//                }
//                showStockKeyboard(v);
//            }
//
//            @Override
//            public void onClick(int id) {
//                Bitmap bitmap = new SoftReference<Bitmap>(ImageViewUtil.createViewBitmap(menu.getLlImage())).get();
//                if (bitmap == null) {
//                    CommonUtil.showToast(TjrsocialStockMenuActivity.this, "没有获取到图片，请重新点击", Gravity.BOTTOM);
//                    return;
//                }
//                bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, CommonUtil.bitmapToBytes(bitmap));
//                switch (resultType) {
//                    case 0:
//                        CommonUtil.pageJumpToData(TjrsocialStockMenuActivity.this, TjrsocialCanvasActivity.class, false, true, bundle);
//                        break;
//                    case 1:
//                        Intent intent = new Intent();
//                        intent.putExtras(bundle);
//                        CommonUtil.pageJumpResult(TjrsocialStockMenuActivity.this, TjrsocialCanvasActivity.class, intent, false);
//                        break;
//                    default:
//                        break;
//                }
//
//            }
//
//            @Override
//            public void ChatStockmenuGoback() {
//                goback();
//            }
//        });
//
//    }
//
//    /**
//     * 显示键盘
//     *
//     * @param v
//     */
//    private void showStockKeyboard(View v) {
//        if (keyboard == null) {
//            keyboard = new StockKeyboard(this, false, this, true, true, true, userId);
//            keyboard.initPopupMenu(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
//            keyboard.setQueryLimit(keyboard.getNormalStock(), new String[]{"sh000001"});
//        }
//        keyboard.showPopupMenu(v, Gravity.BOTTOM, 0, 0);
//    }
//
//    @Override
//    public void selectStockResult(StockInformation information) {
//        // TODO Auto-generated method stub
//        menu.chooseResult(information.getFdm(), information.getJc());
//    }
//
    @Override
    public void goback() {
        CommonUtil.pageJump(this, null, true, true);
    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        setResult(resultCode, data);
//        goback();
//    }

}
