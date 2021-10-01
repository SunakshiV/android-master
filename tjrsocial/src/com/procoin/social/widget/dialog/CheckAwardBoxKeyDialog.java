//package com.cropyme.social.widget.dialog;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.content.ComponentName;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.graphics.PointF;
//import android.os.Bundle;
//import android.support.v4.view.ViewCompat;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationSet;
//import android.view.animation.AnimationUtils;
//import android.view.animation.BounceInterpolator;
//import android.view.animation.RotateAnimation;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.nineoldandroids.animation.Animator;
//import com.nineoldandroids.animation.ValueAnimator;
//import com.cropyme.http.tjrcpt.TjrPayHttp;
//import CommonUtil;
//import NotificationsUtil;
//import ParserJsonUtils;
//import TjrSocialMTAUtil;
//import AvoidMultiClick;
//
//import com.cropyme.social.R;
//import QuadraticBezier;
//import MatrixButton;
//import BaseAsyncTask;
//
//import org.json.JSONObject;
//
///**
// * 弹出宝箱的对话框
// */
//public class CheckAwardBoxKeyDialog extends AvoidMultiClick {
//
//    //宝箱布局
////    private RelativeLayout rlBox;//整个宝箱布局
//    private ImageView ivLight;//发光的背景图
//    private LinearLayout llTwoBtn;
//    private Button btnIKnow;
//    private Button btnLook;
//    private Button btnOpenBox;
//    private ImageView ivClosedBox;
//    private TextView tvTip;//提示
//    private Button btnCancel;
//
//    private Activity activity;
//    private ProgressBar progressBarWait;
//    private Dialog dialog;
//    private CheckAwardBoxKeyTask mCheckAwardBoxKeyTask;
//    private boolean isFirstTime = true;
//    private String oldawardBoxKey;
//    public long userId;//
//    public String awardBoxKey;
//    private String cls;//
//    private String pkg;//
//    private String strparams;//参数
//    private int boxType;// 宝箱的类型 0 没有获奖，1豆 ，2 是卷
//
//    public CheckAwardBoxKeyDialog(Context context) {
//        this.activity = (Activity) context;
//        initDialog();
//        setDialog();
//    }
//
//    private void initDialog() {
//        //宝箱布局
//        View view = LayoutInflater.from(activity).inflate(R.layout.tjr_social_dialog_checkawardbox_box, null);
//        tvTip = (TextView) view.findViewById(R.id.tvTip);
//        progressBarWait = (ProgressBar) view.findViewById(R.id.progressBarWait);
//        progressBarWait.setVisibility(View.GONE);
////        rlBox = (RelativeLayout) view.findViewById(R.id.rlBox);
//        ivLight = (ImageView) view.findViewById(R.id.ivLight);
//        llTwoBtn = (LinearLayout) view.findViewById(R.id.llTwoBtn);
//        btnIKnow = (MatrixButton) view.findViewById(R.id.btnIKnow);
//        btnLook = (MatrixButton) view.findViewById(R.id.btnLook);
//        btnOpenBox = (MatrixButton) view.findViewById(R.id.btnOpenBox);
//        btnCancel = (Button) view.findViewById(R.id.btnCancel);
//        //贝塞尔曲线对应的宝箱 ImageView
//        ivClosedBox = (ImageView) view.findViewById(R.id.ivClosedBox);
//
//        dialog = new Dialog(activity, R.style.checkawardboxdialog) {
//            @Override
//            public void onWindowFocusChanged(boolean hasFocus) {
//                super.onWindowFocusChanged(hasFocus);
//
//                if (isFirstTime) {
//                    getBezierAnimator().start();
//                    isFirstTime = false;
//                }
//            }
//
////            @Override
////            public void setOnDismissListener(OnDismissListener listener) {
////                cls = null;
////                pkg = null;
////                strparams = null;
////                isFirstTime = true;
////                super.setOnDismissListener(listener);
////            }
//
//            @Override
//            public void dismiss() {
//                cls = null;
//                pkg = null;
//                strparams = null;
//                isFirstTime = true;
//                resetDialog();
//                super.dismiss();
//            }
//
//            @Override
//            protected void onStop() {
//                super.onStop();
//            }
//        };
//        dialog.setContentView(view);
//        dialog.setCancelable(true);
////        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
////            @Override
////            public void onCancel(DialogInterface dialog) {
////                isFirstTime = true;
////            }
////        });
//        Window window = dialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        window.setGravity(Gravity.CENTER);
//    }
//
//    private void setDialog() {
//        resetDialog();
//
//        btnOpenBox.setOnClickListener(this);
//        btnIKnow.setOnClickListener(this);
//        btnLook.setOnClickListener(this);
//        btnCancel.setOnClickListener(this);
//    }
//
//    private void resetDialog() {
//        /*重置Dialog */
//        ivClosedBox.clearAnimation();
//        ivLight.clearAnimation();
//        ivLight.setVisibility(View.INVISIBLE);
//        tvTip.setVisibility(View.INVISIBLE);//隐藏
//        btnOpenBox.setVisibility(View.INVISIBLE);
//        llTwoBtn.setVisibility(View.INVISIBLE);
//        btnCancel.setVisibility(View.INVISIBLE);
//        ivClosedBox.setVisibility(View.INVISIBLE);
//        ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_closed_box));
//    }
//
//    private ValueAnimator getBezierAnimator() {
//        /*重置Dialog */
////        ivClosedBox.clearAnimation();
////        ivLight.clearAnimation();
////        ivLight.setVisibility(View.INVISIBLE);
////        tvTip.setVisibility(View.INVISIBLE);//隐藏
////        btnOpenBox.setVisibility(View.INVISIBLE);
////        llTwoBtn.setVisibility(View.INVISIBLE);
////        ivClosedBox.setVisibility(View.VISIBLE);
////        ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_closed_box));
//
////        resetDialog();
//        ivClosedBox.setVisibility(View.VISIBLE);
//
//
//        /*初始化 3 个点*/
//        int[] location = new int[2];
//        //起点
//        tvTip.getLocationInWindow(location);
//        location[0] += tvTip.getWidth() / 2 - ivClosedBox.getWidth() / 2;
//        location[1] += tvTip.getHeight() / 2 - ivClosedBox.getHeight() / 2;
//        PointF pointStart = new PointF(location[0], location[1]);//起点坐标（是绝对坐标）
//        //终点和辅助点
//        ivLight.getLocationInWindow(location);
//        location[0] += ivLight.getWidth() / 2 - ivClosedBox.getWidth() / 2;
//        location[1] += ivLight.getHeight() / 2 - ivClosedBox.getHeight() / 2;
////        CommonUtil.LogLa(2, "location[0] = " + location[0] + "  location[1] = " + location[1] + "   IndexPkActivity--->ivClosedBox.getWidth() / 2 = " + ivClosedBox.getWidth() / 2 + "  ivClosedBox.getHeight() / 2 = " + ivClosedBox.getHeight() / 2);//ivClosedBox.getWidth() / 2 = 128  ivClosedBox.getHeight() / 2 = 154
//        PointF pointEnd = new PointF(location[0], location[1]);//终点坐标
//        PointF pointMid = new PointF(location[0], location[1] - 200);//中间的辅助点
//
//
//        /*获取 ValueAnimator */
//        QuadraticBezier bezierEvaluator = new QuadraticBezier(pointMid);
//        ValueAnimator valueAnimator = com.nineoldandroids.animation.ValueAnimator.ofObject(bezierEvaluator, pointStart, pointEnd);
//        valueAnimator.setDuration(1000);
////        valueAnimator.setInterpolator(new AccelerateInterpolator());//差值器
//        BounceInterpolator bounceInterpolator = new BounceInterpolator();
//        valueAnimator.setInterpolator(bounceInterpolator);//动画结束时弹起
//
//        // 给动画添加一个动画的进度监听;在动画执行的过程中动态的改变view的位置;
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//
//                PointF pointF = (PointF) animation.getAnimatedValue();
//                ViewCompat.setX(ivClosedBox, pointF.x);
//                ViewCompat.setY(ivClosedBox, pointF.y);
//
//                ViewCompat.setScaleX(ivClosedBox, animation.getAnimatedFraction());
//                ViewCompat.setScaleY(ivClosedBox, animation.getAnimatedFraction());
//                // 设置view的透明度,达到动画执行过程view逐渐透明效果;
////                view.setAlpha(1 - animation.getAnimatedFraction());
//            }
//        });
//
//        valueAnimator.addListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animator) {
//                ivClosedBox.clearAnimation();
//                btnOpenBox.setVisibility(View.VISIBLE);
//                ivLight.setVisibility(View.VISIBLE);
//                AnimationSet as = (AnimationSet) AnimationUtils.loadAnimation(activity,
//                        R.anim.box_rotate_alpha_infinite);
//                ivLight.setAnimation(as);
//                as.start();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animator) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animator) {
//
//            }
//        });
//
//        return valueAnimator;
//    }
//
//
//    public void showDialog() {
//        if (!activity.isFinishing() && null != dialog && !dialog.isShowing()) {
//            dialog.show();
//        }
//    }
//
//    public void dissmissDialog() {
//        if (null != dialog && dialog.isShowing()) {
//            dialog.dismiss();
//        }
//    }
//
//    public boolean isDialogShowing() {
//        return !(activity.isFinishing() || null == dialog || !dialog.isShowing());
//    }
//
//    @Override
//    public void click(View v) {
//        int i = v.getId();
//        if (i == R.id.btnOpenBox) {//开启宝箱
//            CommonUtil.cancelAsyncTask(mCheckAwardBoxKeyTask);
//            mCheckAwardBoxKeyTask = (CheckAwardBoxKeyTask) new CheckAwardBoxKeyTask().executeParams();
//        } else if (i == R.id.btnIKnow) {
//            if (boxType == 2) {
//                if (activity != null)
//                    TjrSocialMTAUtil.trackCustomKVEvent(activity, TjrSocialMTAUtil.PROP_CLICKTYPE, "宝箱微盘劵取消", TjrSocialMTAUtil.MTAGAMEBOXWEIPANCANCEL);
//            }
//
//            ivLight.clearAnimation();
////            ivLight.setVisibility(View.INVISIBLE);
////            ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_closed_box));
////            llTwoBtn.setVisibility(View.INVISIBLE);
////            dialog.dismiss();
//            dissmissDialog();
//
//
//        } else if (i == R.id.btnLook) {
//            if (boxType == 2) {
//                if (activity != null)
//                    TjrSocialMTAUtil.trackCustomKVEvent(activity, TjrSocialMTAUtil.PROP_CLICKTYPE, "宝箱微盘劵确定", TjrSocialMTAUtil.MTAGAMEBOXWEIPANTOGO);
//            }
//            ivLight.clearAnimation();
////            ivLight.setVisibility(View.INVISIBLE);
////            ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_closed_box));
////            llTwoBtn.setVisibility(View.INVISIBLE);
////            dialog.dismiss();
//            try {
//                if (!TextUtils.isEmpty(cls) && !TextUtils.isEmpty(pkg) && activity != null) {
//                    Intent mIntent = new Intent();
//                    ComponentName comp = new ComponentName(pkg,
//                            cls);
//                    mIntent.setComponent(comp);
//                    Bundle bundle = new Bundle();
//                    if (strparams != null) {//有params就
//                        bundle.putString("params", strparams);
//                    }
//                    mIntent.putExtras(bundle);
//                    mIntent.setAction("android.intent.action.VIEW");
//                    activity.startActivity(mIntent);
//                }
//            } catch (Exception e) {
//                if (activity != null) CommonUtil.showToast(activity, "请更新主程序", Gravity.BOTTOM);
//            }
//            dissmissDialog();
//
//        } else if (i == R.id.btnCancel) {
//            dissmissDialog();
//        }
//    }
//
//    private class CheckAwardBoxKeyTask extends BaseAsyncTask<Void, Void, Boolean> {
//        private Exception e;
//        private String tip; //提示
//        private String msg;//toast提示
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            progressBarWait.setVisibility(View.VISIBLE);
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            String result = null;
//            try {
//                //{"awardBox":{"boxId":0,"boxType":0,"couponId":0,"probability":0,"tip":"差一点点就可以获得神秘大奖了哟！","tjrBean":0},"code":20000,"msg":"请求成功","success":true}
//                //{"awardBox":{"boxId":5,"boxType":2,"cls":"com.cropyme.weipan.WeipanHomeActivity","couponId":7,"params":"{}","pkg":"com.cropyme","probability":0.38,"pview":"MicromarketIndexController","tip":"恭喜你！获得了8元微盘本金券","tjrBean":0},"code":20000,"msg":"请求成功","success":true}
//                result = TjrPayHttp.getInstance().checkAwardBoxKey(String.valueOf(userId), awardBoxKey);
//                if (!TextUtils.isEmpty(result)) {
//                    JSONObject allJson = new JSONObject(result);
//                    if (ParserJsonUtils.hasAndNotNull(allJson, "msg")) {
//                        msg = allJson.getString("msg");
//                    }
//                    if (ParserJsonUtils.hasAndNotNull(allJson, "success")) {
//                        if (allJson.getBoolean("success")) {
//                            if (ParserJsonUtils.hasAndNotNull(allJson, "awardBox")) {
//                                JSONObject awardBoxJson = new JSONObject(allJson.getString("awardBox"));
//                                if (ParserJsonUtils.hasAndNotNull(awardBoxJson, "boxType")) {
//                                    boxType = awardBoxJson.getInt("boxType");
//                                }
//                                if (ParserJsonUtils.hasAndNotNull(awardBoxJson, "tip")) {
//                                    tip = awardBoxJson.getString("tip");
//                                }
//                                if (ParserJsonUtils.hasAndNotNull(awardBoxJson, "cls")) {
//                                    cls = awardBoxJson.getString("cls");
//                                }
//                                if (ParserJsonUtils.hasAndNotNull(awardBoxJson, "pkg")) {
//                                    pkg = awardBoxJson.getString("pkg");
//                                }
//                                if (ParserJsonUtils.hasAndNotNull(awardBoxJson, "params")) {
//                                    strparams = awardBoxJson.getString("params");
//                                }
//                            }
//                            return true;
//                        }
//                    }
//
//                }
//            } catch (Exception e) {
//                this.e = e;
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aVoid) {
//            super.onPostExecute(aVoid);
//            progressBarWait.setVisibility(View.GONE);
//            if (aVoid != null && aVoid) {
//                RotateAnimation as = (RotateAnimation) AnimationUtils.loadAnimation(activity, R.anim.box_rotate);
//                as.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        ivClosedBox.clearAnimation();
//                        switch (boxType) {
//                            case 1:
//                                ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_bean_open_box));
//                                break;
//                            case 2:
//                                ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_ticket_open_box));
//                                break;
//                            default:
//                                ivClosedBox.setImageDrawable(activity.getResources().getDrawable(R.drawable.ic_box_empty_open_box));
//                                break;
//                        }
//
//                        llTwoBtn.setVisibility(View.VISIBLE);
//                        btnOpenBox.setVisibility(View.INVISIBLE);
//                        if (!TextUtils.isEmpty(cls) && !TextUtils.isEmpty(tip)) {
//                            btnLook.setVisibility(View.VISIBLE);
//                        } else {
//                            btnLook.setVisibility(View.GONE);
//                        }
//                        if (!TextUtils.isEmpty(tip)) {
//                            tvTip.setVisibility(View.VISIBLE);
//                            tvTip.setText(tip);//显示
//                        }
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//                    }
//                });
//                ivClosedBox.startAnimation(as);
//            } else {
//                if (!TextUtils.isEmpty(msg) && activity != null)
//                    CommonUtil.showToast(activity, msg, Gravity.BOTTOM);
//                if (e != null && activity != null) {
//                    btnCancel.setVisibility(View.VISIBLE);
//                    NotificationsUtil.ToastReasonForFailure(activity, e);
//                }
//
//            }
//        }
//    }
//}
