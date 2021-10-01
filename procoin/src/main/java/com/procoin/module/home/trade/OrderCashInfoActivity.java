package com.procoin.module.home.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.photo.ViewPagerPhotoViewActivity;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.copy.dialog.PayOrderTipsDialog;
import com.procoin.module.home.trade.dialog.CancelOrderDialog;
import com.procoin.module.home.trade.dialog.InoutMarkPayDialog;
import com.procoin.module.home.trade.entity.ChatStaff;
import com.procoin.module.home.trade.entity.OrderCashStateEnum;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.DateUtils;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.BadgeView;
import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.util.VeDate;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 现金待支付订单（二维码付款页面，付款完标记完成）
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class OrderCashInfoActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {

    @BindView(R.id.ivState)
    ImageView ivState;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.llCopyTolBalance)
    LinearLayout llCopyTolBalance;
    @BindView(R.id.tvTolBalance)
    TextView tvTolBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.ivPayLogo)
    ImageView ivPayLogo;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvPayWayText)
    TextView tvPayWayText;
    @BindView(R.id.tvPayAccount)
    TextView tvPayAccount;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;

    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.llBottomBtn)
    LinearLayout llBottomBtn;
    @BindView(R.id.llQr)
    LinearLayout llQr;
    @BindView(R.id.llCopyName)
    LinearLayout llCopyName;
    @BindView(R.id.llCopyAccount)
    LinearLayout llCopyAccount;
    @BindView(R.id.llCopyOrderNo)
    LinearLayout llCopyOrderNo;
    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.tvStateDesc)
    TextView tvStateDesc;
    @BindView(R.id.tvGoPay)
    TextView tvGoPay;

    @BindView(R.id.llChat)
    LinearLayout llChat;


    private InoutMarkPayDialog inoutMarkPayDialog;
    private CancelOrderDialog cancelOrderDialog;
    private PayOrderTipsDialog payOrderTipsDialog;


    private Call<ResponseBody> getInoutMarkPayCall;
    private Call<ResponseBody> orderCancelCall;
    private Call<ResponseBody> getOrderCashDetailCall;

    private OrderCash orderCash;

    private long orderCashId;

    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private CountDownTimer timer;

    private ChatStaff chatStaff;//私聊用到


    private BadgeView badgeChat;


    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //收到一条新信息
                setChatNewsCount();
                break;

            default:
                break;
        }
    }

    public static void pageJump(Context context, long orderCashId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        PageJumpUtil.pageJump(context, OrderCashInfoActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.unpaid_info;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }


    @Override
    protected void showReConnection() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unpaid_info);
        ButterKnife.bind(this);
        immersionBar.statusBarDarkFont(false, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }


        llCopyName.setOnClickListener(this);
        llCopyAccount.setOnClickListener(this);
        llCopyOrderNo.setOnClickListener(this);
        llCopyTolBalance.setOnClickListener(this);


        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(0, 0);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        startTradeCashOrderDetail();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("setChatNewsCount", "onResume==");
        setChatNewsCount();

    }

    public void showPrivateChatNewsCount(String chatTopic) {
        int chatCount = 0;
        if (getApplicationContext().getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(getApplicationContext(), chatTopic, getApplicationContext().getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.procoin.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }


    }


    private void setData() {
        setChatNewsCount();

        if (orderCash != null && orderCash.orderCashId > 0 && orderCash.receipt != null) {
            tvTolBalance.setText("¥ " + orderCash.balanceCny);
            tvPrice.setText("¥ " + orderCash.priceCny);
            tvNum.setText(orderCash.amount + " USDT");

            tjrImageLoaderUtil.displayImageRound(orderCash.receipt.receiptLogo, ivPayLogo);
            tvPayWay.setText(orderCash.receipt.receiptTypeValue);
            tvName.setText(orderCash.receipt.receiptName);
            tvPayAccount.setText(orderCash.receipt.receiptNo);
            tvOrderNo.setText(String.valueOf(orderCash.orderCashId));
            tvOrderTime.setText(DateUtils.getStringDateOfString2(String.valueOf(orderCash.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));

            tvGoPay.setVisibility(View.GONE);
            if (orderCash.receipt.receiptType == 1 || orderCash.receipt.receiptType == 2) {
                llQr.setVisibility(View.VISIBLE);
                tjrImageLoaderUtil.displayImage(orderCash.receipt.qrCode, ivQr);
                if (orderCash.receipt.receiptType == 1) {
                    tvPayWayText.setText("支付宝账号");
                    if (!TextUtils.isEmpty(orderCash.receipt.qrContent)) {//qrContent不为空的时候才显示去支付
                        tvGoPay.setVisibility(View.VISIBLE);
                    }
                } else {
                    tvPayWayText.setText("微信账号");
                }

            } else {
                llQr.setVisibility(View.GONE);
                tvPayWayText.setText("银行卡号");
            }
            OrderCashStateEnum orderCashStateEnum = OrderCashStateEnum.getOrderCashState(orderCash.state);
            ivState.setImageResource(orderCashStateEnum.getIcon());
            tvState.setText(orderCash.stateDesc);
//            LinearLayout llTime;
//            TextView tvStateDesc;
            if (orderCash.state == OrderCashStateEnum.wait_pay.getState()) {
                llTime.setVisibility(View.VISIBLE);
                tvStateDesc.setVisibility(View.GONE);
                startCountDownTime();

                llBottomBtn.setVisibility(View.VISIBLE);
                tvSubmit.setEnabled(true);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isFinishing()) {
                            showPayOrderTipsDialog(orderCash.balanceCny);
                        }
                    }
                }, 500);


            } else if (orderCash.state == OrderCashStateEnum.mark_pay.getState()) {
                llTime.setVisibility(View.GONE);
                tvStateDesc.setVisibility(View.VISIBLE);
                tvStateDesc.setText("请耐心等候对方放币");

                llBottomBtn.setVisibility(View.VISIBLE);
                tvGoPay.setVisibility(View.GONE);
                tvSubmit.setEnabled(false);
                tvSubmit.setText("待放行");

            } else if (orderCash.state == OrderCashStateEnum.done.getState()) {
                llTime.setVisibility(View.GONE);
                tvStateDesc.setVisibility(View.VISIBLE);
                tvStateDesc.setText("订单已完成");


                llBottomBtn.setVisibility(View.GONE);
            } else if (orderCash.state == OrderCashStateEnum.expire.getState()) {
                llTime.setVisibility(View.GONE);
                tvStateDesc.setVisibility(View.VISIBLE);
                tvStateDesc.setText("订单已过期");

                llBottomBtn.setVisibility(View.GONE);
            } else if (orderCash.state == OrderCashStateEnum.cancel.getState()) {
                llTime.setVisibility(View.GONE);
                tvStateDesc.setVisibility(View.VISIBLE);
                tvStateDesc.setText("订单已撤销");

                llBottomBtn.setVisibility(View.GONE);
            } else if (orderCash.state == OrderCashStateEnum.unpay_cancel.getState()) {
                llTime.setVisibility(View.GONE);
                tvStateDesc.setVisibility(View.VISIBLE);
                tvStateDesc.setText("订单已被系统撤销");

                llBottomBtn.setVisibility(View.GONE);

            }

            tvSubmit.setOnClickListener(this);
            tvGoPay.setOnClickListener(this);
            tvCancel.setOnClickListener(this);
            llChat.setOnClickListener(this);
            llQr.setOnClickListener(this);
        }


    }

    private void setChatNewsCount() {
        Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff==" + chatStaff);

        if (chatStaff != null) {
            Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff.chatTopic==" + chatStaff.chatTopic);
            showPrivateChatNewsCount(chatStaff.chatTopic);
        }
    }

    private void showInOutMarkDialog() {
        if (inoutMarkPayDialog == null) {
            inoutMarkPayDialog = new InoutMarkPayDialog(this) {
                @Override
                public void onclickOk() {
                    if (orderCash != null) {
                        startInoutMarkPay(orderCash.orderCashId);
                    }
                }
            };
        }
        inoutMarkPayDialog.show();
    }


    private void showCancelOrderkDialog() {
        if (cancelOrderDialog == null) {
            cancelOrderDialog = new CancelOrderDialog(this) {
                @Override
                public void onclickOk() {
                    if (orderCash != null) {
                        startOrderCancel(orderCash.orderCashId);
                    }
                }
            };
        }
        cancelOrderDialog.show();
    }


    private void showPayOrderTipsDialog(String balanceCny) {
        if (payOrderTipsDialog == null) {
            payOrderTipsDialog = new PayOrderTipsDialog(this, balanceCny) {
                @Override
                public void onclickOk() {
                }
            };
        }
        payOrderTipsDialog.show();
    }


    private void startCountDownTime() {
        if (orderCash == null) return;
//        Long ss = VeDate.strLongToDate(String.valueOf(orderCash.expireTime)).getTime() - VeDate.strLongToDate(String.valueOf(orderCash.createTime)).getTime();
        Long ss = orderCash.expireTime - System.currentTimeMillis() / 1000;
        Log.d("startCountDownTime", "orderCash.expireTime==" + orderCash.expireTime);
        Log.d("startCountDownTime", "System.currentTimeMillis()==" + System.currentTimeMillis());
        Log.d("startCountDownTime", "time==" + ss);
        if (ss > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(ss * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[2] + ":" + time[3]);
                    }
                }

                public void onFinish() {
                    tvTime.setText("00:00");
                    tvSubmit.setText("订单已经超时");
                    tvSubmit.setEnabled(false);
                }
            };
            timer.start();
        } else {
            tvSubmit.setText("订单已经超时");
            tvSubmit.setEnabled(false);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            orderCash = null;
        }
    }

    TjrBaseDialog submitDialog;

    private void showSubmitDialog() {
        submitDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                if (orderCash != null) {
                    startInoutMarkPay(orderCash.orderCashId);
                }
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        submitDialog.setTvTitle("温馨提示");
        submitDialog.setMessage("请确认已付款，否则订单将在30分钟后自动取消。每日3次自动取消将影响信用，恶意行为将被冻结现金支付功能24小时。");
        submitDialog.setBtnOkText("标记为已支付");
        submitDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCopyName:
                com.procoin.util.CommonUtil.copyText(this, tvName.getText().toString());
                break;
            case R.id.llCopyAccount:
                com.procoin.util.CommonUtil.copyText(this, tvPayAccount.getText().toString());
                break;
            case R.id.llCopyTolBalance:
                if (orderCash != null)
                    com.procoin.util.CommonUtil.copyText(this, orderCash.balanceCny);
                break;
            case R.id.llCopyOrderNo:
                com.procoin.util.CommonUtil.copyText(this, tvOrderNo.getText().toString());
                break;
            case R.id.tvSubmit:
                showInOutMarkDialog();
                break;
            case R.id.tvCancel:
                showCancelOrderkDialog();
                break;
            case R.id.llChat:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;

            case R.id.llQr:
                if (orderCash != null && orderCash.receipt != null && !TextUtils.isEmpty(orderCash.receipt.qrCode)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConst.PAGETYPE, 6);
                    bundle.putString(CommonConst.SINGLEPICSTRING, orderCash.receipt.qrCode);
                    PageJumpUtil.pageJumpToData(OrderCashInfoActivity.this, ViewPagerPhotoViewActivity.class, bundle);
                }
                break;
            case R.id.tvGoPay:
                if (orderCash != null) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(OrderCashInfoActivity.this, orderCash.receipt.qrContent);
                }

                break;

        }
    }


    private void startInoutMarkPay(long inOutId) {
        CommonUtil.cancelCall(getInoutMarkPayCall);
        getInoutMarkPayCall = VHttpServiceManager.getInstance().getVService().inoutMarkPay(inOutId);
        getInoutMarkPayCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OrderCashInfoActivity.this);
                    if (orderCash != null) {
                        orderCash.state = OrderCashStateEnum.mark_pay.getState();
                        orderCash.stateDesc = "已标记付款";
                        setData();
                    }
//                    tvSubmit.setText("已标记付款");
//                    tvSubmit.setEnabled(false);
//                    llTime.setVisibility(View.GONE);

                }
            }
        });
    }


    private void startOrderCancel(long inOutId) {
        CommonUtil.cancelCall(orderCancelCall);
        orderCancelCall = VHttpServiceManager.getInstance().getVService().orderCancel(inOutId);
        orderCancelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OrderCashInfoActivity.this);
                    if (orderCash != null) {
                        orderCash.state = OrderCashStateEnum.cancel.getState();
                        orderCash.stateDesc = "订单已撤销";
                        setData();
                    }

                }
            }
        });
    }


    private void startTradeCashOrderDetail() {
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderDetail(orderCashId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    chatStaff = resultData.getObject("chatStaff", ChatStaff.class);
                    orderCash = resultData.getObject("order", OrderCash.class);
                    setData();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean saveToSdcard(String uri) {
        if (TextUtils.isEmpty(uri)) return false;
        // TODO remove 东西
        File file = null;
        try {
//            String uri = getUrl(imgUrls.get(veiePhotoView.getCurrentItem()));
            Log.d("saveToSdcard", "uri==" + uri);
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri);
            if (bitmap == null) {
                com.procoin.social.util.CommonUtil.showToast(this, "没有获取到图片", Gravity.BOTTOM);
                return false;
            }

            String fileName = com.procoin.social.util.VeDate.getyyyyMMddHHmmss(com.procoin.social.util.VeDate.getNow())
                    + ".png";
            file = ((MainApplication) getApplicationContext())
                    .getmDCIMRemoteResourceManager().getFile(fileName);
            ((MainApplication) getApplicationContext())
                    .getmDCIMRemoteResourceManager().writeFile(file, bitmap,
                    false);
            if (((MainApplication) getApplicationContext()).isSDCard()) {
                // 其次把文件插入到系统图库
//                try {
//                    MediaStore.Images.Media.insertImage(this.getContentResolver(),
//                            file.getAbsolutePath(), fileName, null);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                // 最后通知图库更新

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("saveToSdcard", "e==" + e);
            com.procoin.social.util.CommonUtil.showToast(this, "保存图片出错", Gravity.BOTTOM);
            return false;
        }
        CommonUtil.showmessage("二维码保存成功", this);
        return true;
    }


    PopupWindow pop;

    private void showPopupMenu(View parent, int width, int height) {
        if (pop == null) {
            pop = new PopupWindow(InflaterUtils.inflateView(this, R.layout.money_mark), width, height);//
//            pop.setOutsideTouchable(false);
//            pop.setFocusable(false);// 如果不加这个，Grid不会响应ItemClick
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));// 特别留意这个东东

//            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                }
//            });
        }
        if (pop != null && !pop.isShowing()) {
//            pop.showAsDropDown(parent);
            pop.showAsDropDown(parent, 0, 20);
//            pop.showAtLocation(parent,Gravity.CENTER,50,50);
        }
    }

    private void dimissPop() {
        if (pop != null && pop.isShowing()) {
            pop.dismiss();
        }
    }


}
