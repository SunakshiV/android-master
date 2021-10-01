//package com.cropyme.social;
//
//import android.app.Activity;
//import android.content.ComponentName;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.View;
//import android.widget.Toast;
//
//import User;
//import TjrSocialShareConfig;
//import com.cropyme.social.task.OldThirdShareWXTask;
//import com.cropyme.social.task.ShareToCircleTask;
//import com.cropyme.social.task.ThirdShareWXTask;
//import CommonUtil;
//import ImageViewUtil;
//
//public class TjrSocialShare {
//    private static TjrSocialShare mTjrSocialShareInstance = null;
//    private ThirdShareWXTask thirdShareWxTask;
//    private OldThirdShareWXTask mOldThirdShareWXTask;
//    private ShareToCircleTask mShareToCircleTask;
//
//    public synchronized static TjrSocialShare getInstance() {
//        if (mTjrSocialShareInstance == null) {
//            mTjrSocialShareInstance = new TjrSocialShare();
//        }
//        return mTjrSocialShareInstance;
//    }
//
//    /**
//     * 没有图片 分享成功之后有点击事件，例如文章、报纸等等 发表成功之后jumpType==1弹框，jumpType==0不弹出框
//     *
//     * @param activity
//     * @param stype
//     * @param title
//     * @param content
//     * @param params
//     * @param iconUrl
//     * @param squareOrFriendCircle 0 股友吧 1 好友圈
//     */
//    public void shareToSquareOrFriendCircle(Activity activity, String stype, String title, String content, String params, String iconUrl, int squareOrFriendCircle, String pkg, String cls, String pview) {
//
//        // ComponentName comp = new ComponentName("com.cropyme",
//        // "com.cropyme.talkie.share.TalkieShareActivity");
//        Bundle bundle = new Bundle();
//        bundle.putString(TjrSocialShareConfig.STYPE, stype);
//        bundle.putString(TjrSocialShareConfig.TITLE, title);
//        bundle.putString(TjrSocialShareConfig.CONTENT, content);
//        bundle.putString(TjrSocialShareConfig.PARAMS, params);
//        bundle.putString(TjrSocialShareConfig.ICONURL, iconUrl);
//        bundle.putInt(TjrSocialShareConfig.KEY_EXTRAS_TYPE, 1);// 分享过去都要弹框
//        bundle.putInt(TjrSocialShareConfig.SQUAREORFRIENDCIRCLE, squareOrFriendCircle);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_PKG, pkg);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_CLS, cls);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_PVIEW, pview);
//        goToActivity(activity, bundle, "com.cropyme.square.SquareOrFriendCircleShareActivity", null);
//    }
//
//    /**
//     * 带张图片或文字，分享成功之后没有点击事件，分享到股友吧
//     * <p/>
//     * 截屏分享到股友圈，好友圈 修改了分享的类名， 增加了jumpType 发表成功之后jumpType==1弹框，jumpType==0不弹出框
//     *
//     * @param activity
//     * @param view
//     * @param text
//     */
//    public void sharePicOrTextToSquare(Activity activity, View view, String text) {
//        Bundle bundle = new Bundle();
//        if (view != null) {
//            Bitmap bitmap = ImageViewUtil.createDecorViewBitmap(activity, view);
//            byte[] b = CommonUtil.bitmapToBytes(bitmap);
//            bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        }
//        bundle.putInt(TjrSocialShareConfig.KEY_EXTRAS_TYPE, 1);// 分享过去都要弹框
//        bundle.putString(TjrSocialShareConfig.CONTENT, text);
//        // intent.putExtras(bundle);
//        // intent.setComponent(comp);
//        // intent.setAction("android.intent.action.VIEW");
//        // activity.startActivity(intent);
//        // activity.overridePendingTransition(R.anim.in_left_to_right,
//        // R.anim.in_right_to_left);
//        goToActivity(activity, bundle, "com.cropyme.square.SquareSendActivity", null);
//    }
//
//    /**
//     * 带张图片或文字，分享成功之后没有点击事件，分享到好友圈
//     * <p/>
//     * 截屏分享到股友圈，好友圈 修改了分享的类名， 增加了jumpType 发表成功之后jumpType==1弹框，jumpType==0不弹出框
//     *
//     * @param activity
//     * @param view
//     * @param text
//     */
//    public void sharePicOrTextToFriendCircle(Activity activity, View view, String text) {
//        Bundle bundle = new Bundle();
//        if (view != null) {
//            Bitmap bitmap = ImageViewUtil.createDecorViewBitmap(activity, view);
//            byte[] b = CommonUtil.bitmapToBytes(bitmap);
//            bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        }
//        bundle.putInt(TjrSocialShareConfig.KEY_EXTRAS_TYPE, 0);// 分享过去都要弹框
//        bundle.putString(TjrSocialShareConfig.CONTENT, text);
//        goToActivity(activity, bundle, "com.cropyme.friendscircle.FriendCircleSendActivity", null);
//    }
//
//    /**
//     * 這個是主程序進行分享,这个默认需要调用后台的url
//     *
//     * @param activity
//     * @param text
//     * @param view
//     * @param jsonStr
//     * @param user
//     */
//    public void shareToWeiboWithCanvas(Activity activity, String text, View view, String jsonStr, User user) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.weibo.TjrSocialShareWeiboActivity");
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, text);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISQUESTION,
//        // isQuestion);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISOTHEARIMAGE,
//        // isOtherImage);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        sharegotoStyle(activity, bundle, view, null, null, null);
//        // if (view != null) {
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // goToCanvas(activity, bundle, null);
//        // } else {
//        // goToActivity(activity, bundle,
//        // bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY));
//        // }
//    }
//
//    /**
//     * 这个是主程序com.cropyme进行分享
//     *
//     * @param activity
//     * @param text
//     * @param view
//     * @param jsonStr
//     * @param user
//     * @param isneedUrl
//     */
//    public void shareToWeiboWithCanvas(Activity activity, String text, View view, String jsonStr, User user, boolean isneedUrl) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.weibo.TjrSocialShareWeiboActivity");
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, text);
//        bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISNEEDURL, isneedUrl);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        sharegotoStyle(activity, bundle, view, null, null, null);
//        // if (view != null) {
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // goToCanvas(activity, bundle, null);
//        // } else {
//        // goToActivity(activity, bundle,
//        // bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY));
//        // }
//    }
//
//    public void shareToWeiboWithCanvas(Activity activity, String text, View view, String jsonStr, String styleType, User user) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_STYLETYPE, styleType);
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.weibo.TjrSocialShareWeiboActivity");
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, text);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISQUESTION,
//        // isQuestion);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISOTHEARIMAGE,
//        // isOtherImage);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        sharegotoStyle(activity, bundle, view, styleType, null, null);
//        // switch (styleId) {
//        // case 0:
//        // if (view != null) {
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // goToCanvas(activity, bundle, null);
//        // } else {
//        // goToActivity(activity, bundle,
//        // bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY));
//        // }
//        // break;
//        // case 1:
//        // if (view != null) {
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // }
//        // goToActivity(activity, bundle,
//        // bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY));
//        // break;
//        //
//        // default:
//        // break;
//        // }
//    }
//
//    /**
//     * 这个是为了统一跳转的样式 当style太多的时候只需要改这里就可以了
//     * 使用这个方法时,请记得TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY 必须要先添加
//     *
//     * @param activity
//     * @param bundle
//     * @param view
//     * @param mainPackage
//     * @param defalutclass 要调转的结果默认页.没有图片的时候就直接跳过去
//     */
//    private void sharegotoStyle(Activity activity, Bundle bundle, View view, String styleType, String mainPackage, String defalutclass) {
//
//
//        if (view != null) {
//            Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//            byte[] b = CommonUtil.bitmapToBytes(bitmap);
//            bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//            goToCanvas(activity, bundle, mainPackage);
//        } else {
//            if (defalutclass != null) {// 判斷是不是去微信或者微薄
//                goToActivity(activity, bundle, defalutclass, mainPackage);
//            } else {
//                goToActivity(activity, bundle, bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY), mainPackage);
//            }
//
//        }
//
////        switch (styleId) {
////            case 0:
////
////                break;
////            case 1:
////                if (view != null) {
////                    Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
////                    byte[] b = CommonUtil.bitmapToBytes(bitmap);
////                    bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
////                }
////                goToActivity(activity, bundle, bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY), mainPackage);
////                break;
////
////            default:
////                break;
////        }
//    }
//
//    /**
//     * 這個是進入画图页面
//     *
//     * @param activity
//     * @param bundle   ，调用这个方法，必须清楚的，是bundle需要要有user,而且跳转的地方也需要自己写
//     */
//    private void goToCanvas(Activity activity, Bundle bundle, String mainPackage) {
//        try {
//            if (bundle != null) {
//                // if
//                // (!bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USER))
//                // CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入",
//                // Gravity.BOTTOM);
//                Intent intent = new Intent();
//                if (mainPackage == null) mainPackage = "com.cropyme";
//                ComponentName comp = new ComponentName(mainPackage, "TjrsocialCanvasActivity");
//                intent.putExtras(bundle);
//                intent.setComponent(comp);
//                intent.setAction("android.intent.action.VIEW");
//                activity.startActivity(intent);
//                activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);
//            }
//
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            Toast.makeText(activity, "请更新版淘金路", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 這個是進入画图页面
//     *
//     * @param activity
//     * @param bundle       ，调用这个方法，必须清楚的，是bundle需要要有user,而且跳转的地方也需要自己写
//     * @param mainPackeage
//     */
//    private void goToActivity(Activity activity, Bundle bundle, String className, String mainPackeage) {
//        try {
//            if (bundle != null) {
//                // if
//                // (!bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USER))
//                // CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入",
//                // Gravity.BOTTOM);
//                Intent intent = new Intent();
//                if (mainPackeage == null) mainPackeage = "com.cropyme";
//                ComponentName comp = new ComponentName(mainPackeage, className);
//                intent.putExtras(bundle);
//                intent.setComponent(comp);
//                intent.setAction("android.intent.action.VIEW");
//                activity.startActivity(intent);
//                activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            Toast.makeText(activity, "请更新版淘金路", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 这个方法可以
//     *
//     * @param activity
//     * @param textContent paper 记得加上 "《" + 文章名 + "》";
//     * @param view
//     * @param user
//     */
//    public void shareToWeiXin(Activity activity, String textContent, String textTitle, boolean istimeline, int iconId, View view, String jsonStr, User user) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        bundle.putString(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE, textTitle);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, textContent);
//        bundle.putBoolean(TjrSocialShareConfig.QQ_WECHAT_TIMELINE, istimeline);//
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISQUESTION,
//        // isQuestion);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISOTHEARIMAGE,
//        // isOtherImg);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        // if (user != null)
//        // bundle.putParcelable(TjrSocialShareConfig.KEY_EXTRAS_USER, user);
//        if (iconId != 0) {
//            Bitmap mbitmap2 = BitmapFactory.decodeResource(activity.getResources(), iconId);
//            bundle.putByteArray(TjrSocialShareConfig.QQ_WECHAT_ICON, CommonUtil.bitmapToBytes(mbitmap2));
//        }
//        sharegotoStyle(activity, bundle, view, null, null, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        // if (view != null) {// 有图就跳进行编辑页面
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // goToCanvas(activity, bundle, null);
//        // } else {// 没有直接调转到wx分享页面
//        // goToActivity(activity, bundle,
//        // "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        // }
//    }
//
//    /**
//     * 这个是主程序有样式的时候使用的
//     *
//     * @param activity
//     * @param textContent
//     * @param textTitle
//     * @param istimeline
//     * @param iconId
//     * @param view
//     * @param jsonStr
//     * @param user
//     */
//    public void shareToWeiXin(Activity activity, String textContent, String textTitle, boolean istimeline, int iconId, View view, String jsonStr, String styleType, User user) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_STYLETYPE, styleType);
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        bundle.putString(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE, textTitle);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, textContent);
//        bundle.putBoolean(TjrSocialShareConfig.QQ_WECHAT_TIMELINE, istimeline);//
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISQUESTION,
//        // isQuestion);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISOTHEARIMAGE,
//        // isOtherImg);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        // if (user != null)
//        // bundle.putParcelable(TjrSocialShareConfig.KEY_EXTRAS_USER, user);
//        if (iconId != 0) {
//            Bitmap mbitmap2 = BitmapFactory.decodeResource(activity.getResources(), iconId);
//            bundle.putByteArray(TjrSocialShareConfig.QQ_WECHAT_ICON, CommonUtil.bitmapToBytes(mbitmap2));
//        }
//        sharegotoStyle(activity, bundle, view, styleType, null, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        // switch (styleId) {
//        // case 0:
//        // if (view != null) {// 有图就跳进行编辑页面
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // goToCanvas(activity, bundle, null);
//        // } else {// 没有直接调转到wx分享页面
//        // goToActivity(activity, bundle,
//        // "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        // }
//        // break;
//        // case 1:
//        // if (view != null) {
//        // Bitmap bitmap = ImageViewUtil.createViewBitmap(view);
//        // byte[] b = CommonUtil.bitmapToBytes(bitmap);
//        // bundle.putByteArray(TjrSocialShareConfig.KEY_EXTRAS_BITMAP, b);
//        // }
//        // goToActivity(activity, bundle,
//        // bundle.getString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY));
//        // break;
//        // default:
//        // break;
//        // }
//    }
//
//    /**
//     * 这个方法可以
//     *
//     * @param activity
//     * @param textContent paper 记得加上 "《" + 文章名 + "》";
//     */
//    public void shareToWeiXinNoshow(Activity activity, String textContent, String textTitle, boolean istimeline, Bitmap mbitmap, String url) {
//        Bundle bundle = new Bundle();
////        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        bundle.putString(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE, textTitle);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, textContent);
//        bundle.putBoolean(TjrSocialShareConfig.QQ_WECHAT_TIMELINE, istimeline);//
//        bundle.putString(TjrSocialShareConfig.QQ_WECHAT_SENDURL, url);//
//
//        // if (user != null)
//        // bundle.putParcelable(TjrSocialShareConfig.KEY_EXTRAS_USER, user);
//        if (mbitmap != null) {
////            Bitmap mbitmap2 = BitmapFactory.decodeResource(activity.getResources(), iconId);
//            bundle.putByteArray(TjrSocialShareConfig.QQ_WECHAT_ICON, CommonUtil.bitmapToBytes(mbitmap));
//        }
//        goToActivity(activity, bundle, "com.cropyme.social.wxapi.WXNoshowActivity", null);
//    }
//
//    /**
//     * 这个是分享到私聊，所有分享到私聊都用这个方法
//     *
//     * @param activity
//     * @param ，调用这个方法，必须清楚的，是bundle需要要有user,而且跳转的地方也需要自己写
//     */
//    public void goToResultChatShare(Activity activity, String jsonStr, Bundle mbundle, String cls, String pkg, String fileName) {
//        try {
//            if (jsonStr != null) {
//                // if
//                // (!bundle.containsKey(TjrSocialShareConfig.KEY_EXTRAS_USER))
//                // CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入",
//                // Gravity.BOTTOM);
//                Intent intent = new Intent();
//                ComponentName comp = new ComponentName("com.cropyme", "com.cropyme.chat.ChatShareActivity");
//                Bundle bundle = new Bundle();
//                if (mbundle != null)
//                    bundle.putBundle(TjrSocialShareConfig.KEY_EXTRAS_COMMONBUNDLE, mbundle);
//                if (cls != null) bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_CLS, cls);
//                if (pkg != null) bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_PKG, pkg);
//                if (fileName != null) {
//                    bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, fileName);
//                }
//                bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//                intent.putExtras(bundle);
//                intent.setComponent(comp);
//                intent.setAction("android.intent.action.VIEW");
//                activity.startActivityForResult(intent, 0x123);
//                activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);
//            }
//        } catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            Toast.makeText(activity, "请更新版淘金路", Toast.LENGTH_SHORT).show();
//        } catch (OutOfMemoryError e) {
//        }
//    }
//
//    /**
//     * 这个是其他软件调用分享
//     *
//     * @param activity
//     * @param textContent
//     * @param textTitle
//     * @param istimeline
//     * @param iconId
//     * @param view
//     * @param jsonStr
//     * @param user
//     * @param appId
//     * @param mainPackage 其他軟件的Package ,
//     */
//    public void shareToWeiXin(Activity activity, String textContent, String textTitle, boolean istimeline, int iconId, View view, String jsonStr, String styleType, User user, String appId, String mainPackage) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_STYLETYPE, styleType);
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//        bundle.putString(TjrSocialShareConfig.QQ_WECHAT_WEBTITLE, textTitle);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, textContent);
//        bundle.putBoolean(TjrSocialShareConfig.QQ_WECHAT_TIMELINE, istimeline);//
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISQUESTION,
//        // isQuestion);
//        // bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISOTHEARIMAGE,
//        // isOtherImg);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID, appId);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_PACKAPE, mainPackage);
//        // if (user != null)
//        // bundle.putParcelable(TjrSocialShareConfig.KEY_EXTRAS_USER, user);
//        if (iconId != 0) {
//            Bitmap mbitmap2 = BitmapFactory.decodeResource(activity.getResources(), iconId);
//            bundle.putByteArray(TjrSocialShareConfig.QQ_WECHAT_ICON, CommonUtil.bitmapToBytes(mbitmap2));
//        }
//        sharegotoStyle(activity, bundle, view, styleType, mainPackage, "com.cropyme.social.wxapi.TjrSocialShareWXActivity");
//    }
//
//    /**
//     * 這是其他軟件进行分享微薄
//     *
//     * @param activity
//     * @param text
//     * @param view
//     * @param jsonStr
//     * @param user
//     * @param mainPackeage
//     * @param appId
//     */
//    public void shareToWeiboWithCanvas(Activity activity, String text, View view, String jsonStr, User user, boolean isneedUrl, String styleType, String appId, String mainPackeage) {
//        if (user == null || user.getUserId() == null) {
//            CommonUtil.showToast(activity, "没有获取到用户信息，请重新进入", Gravity.BOTTOM);
//            return;
//        }
//        Bundle bundle = new Bundle();
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_STYLETYPE, styleType);
//        bundle.putLong(TjrSocialShareConfig.KEY_EXTRAS_USERID, user.getUserId());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERNAME, user.getName());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_USERHEADURL, user.getHeadurl());
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_TOACTIVITY, "com.cropyme.social.weibo.TjrSocialShareWeiboActivity");
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_KEY_CONTENT, text);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_APPID, appId);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_SOCIAL_PACKAPE, mainPackeage);
//        bundle.putString(TjrSocialShareConfig.KEY_EXTRAS_JSONSTR, jsonStr);
//        bundle.putBoolean(TjrSocialShareConfig.KEY_EXTRAS_ISNEEDURL, isneedUrl);
//        sharegotoStyle(activity, bundle, view, styleType, mainPackeage, null);
//    }
//
//    /**
//     * 分享类型
//     *
//     * @param activity
//     * @param shareType
//     * @param sharetitle
//     * @param shareContent
//     * @param content
//     * @param params
//     * @param istimeline
//     * @param iconBitmap
//     * @param userId
//     * @param viewBitmap
//     */
//    public void shareToWeixinNoGui(final Activity activity, String shareType, final String sharetitle, final String shareContent, String content, String params, final boolean istimeline, final Bitmap iconBitmap, final long userId, Bitmap viewBitmap) {
//        CommonUtil.cancelAsyncTask(thirdShareWxTask);
//        thirdShareWxTask = (ThirdShareWXTask) new ThirdShareWXTask(activity, shareType, content, params, userId, viewBitmap) {
//            @Override
//            public void sendUrl(String url) {
//                TjrSocialShare.this.shareToWeiXinNoshow(activity, shareContent, sharetitle, istimeline, iconBitmap, url);
//            }
//        };
//        thirdShareWxTask.executeParams();
//    }
//
//    /**
//     * 旧版主程序的分享，params需要处理
//     *
//     * @param activity
//     * @param shareType
//     * @param title
//     * @param content
//     * @param params
//     * @param istimeline
//     * @param iconBitmap
//     * @param userId
//     */
//    public void shareToWeixinNoGui(Activity activity, String shareType, String title, String content, String params, boolean istimeline, Bitmap iconBitmap, long userId) {
//        CommonUtil.cancelAsyncTask(thirdShareWxTask);
//        // BitmapFactory.decodeResource(activity.getResources(), iconId);
//        mOldThirdShareWXTask = (OldThirdShareWXTask) new OldThirdShareWXTask(activity, shareType, title, content, params, userId, iconBitmap);
//        mOldThirdShareWXTask.isTimeline = istimeline;
//        mOldThirdShareWXTask.executeParams();
//    }
//
//    /**
//     * 这个是分享到Circle，所有分享到私聊都用这个方法
//     *
//     * @param ，调用这个方法，必须清楚的，是bundle需要要有user,而且跳转的地方也需要自己写
//     */
//    public void shareToCircle(final Activity activity, final long userId, String title, String content, String params, String shareType) {
//        CommonUtil.cancelAsyncTask(mShareToCircleTask);
//        mShareToCircleTask = (ShareToCircleTask) new ShareToCircleTask(activity, userId, title, content, params, shareType) {
//            @Override
//            public void getContentFinish(String shareContent) {
//                Bundle bundle = new Bundle();
//                bundle.putLong("shareUserId", userId);
//                if (!TextUtils.isEmpty(shareContent)) {
//                    bundle.putString("shareContent", shareContent);
////                    shareContent === {"cls":"com.cropyme.quotation.stock.f10.F10DetailsActivity","content":"原标题：险资三季末持仓曝光：新进107股增持56股随着上市公司三季报逐步披露，险资2016年三季末持","logo":"http://share.taojinroad.com:9997/app/image/info_share.png","params":"{\"title\":\"险资三季末持仓曝光：新进107股 增持56股\",\"fullCode\":\"sz000506\",\"stockName\":\"中润资源\",\"urls\":\"http:\\/\\/info.taojinroad.com:8090\\/company\\/news\\/2016-10-27\\/sz0005068526241.html\",\"brief\":\"原标题：险资三季末持仓曝光：新进107股增持56股随着上市公司三季报逐步披露，险资2016年三季末持仓情况曝光。证券时报股市大数据新媒体“数据宝”统计，截至10\",\"f10TypeNumber\":0}","pkg":"com.cropyme","pview":"F10DetailsViewController","time":20161028173334,"title":"险资三季末持仓曝光：新进107股 增持56股"}
//                }
//                ComponentName componentName = new ComponentName("com.cropyme", "com.cropyme.circle.CircleShareActivity");
//                Intent intent = new Intent();
//                intent.putExtras(bundle);
//                intent.setComponent(componentName);
//                intent.setAction("com.cropyme.circle.CircleShareActivity");
////                activity.startActivityForResult(intent, 0x123);
////                activity.overridePendingTransition(R.anim.in_left_to_right, R.anim.in_right_to_left);
//                activity.startActivity(intent);
//            }
//        }.executeParams();
//    }
//
//
//    /**
//     * 分享(文本)到私聊
//     *
//     * @param activity
//     * @param userId
//     * @param title
//     * @param content
//     * @param params
//     * @param shareType
//     */
//    public void shareToPrivateChat(final Activity activity, final long userId, String title, String content, String params, final String shareType) {
//        CommonUtil.cancelAsyncTask(mShareToCircleTask);
//        mShareToCircleTask = (ShareToCircleTask) new ShareToCircleTask(activity, userId, title, content, params, shareType) {
//            @Override
//            public void getContentFinish(String shareContent) {
//                if (!TextUtils.isEmpty(shareContent)) {
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putString("shareContent", shareContent);
//                    bundle.putString("shareType", shareType);
//                    ComponentName componentName = new ComponentName("com.cropyme", "com.cropyme.chat.ChatShareActivity");
//                    intent.putExtras(bundle);
//                    intent.setComponent(componentName);
////                    intent.setAction("com.cropyme.circle.CircleShareActivity");
//                    activity.startActivity(intent);
//                } else {
//                    CommonUtil.showToast(activity, "获取数据失败", Gravity.CENTER);
//                }
//
//            }
//        }.executeParams();
//    }
//
//
//}
