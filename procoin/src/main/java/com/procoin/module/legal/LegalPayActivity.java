package com.procoin.module.legal;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.procoin.R;
import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.photo.ViewPagerPhotoViewActivity;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.home.trade.dialog.InoutMarkPayDialog;
import com.procoin.module.home.trade.entity.ChatStaff;
import com.procoin.module.legal.adapter.ShowUserTipAdapter;
import com.procoin.module.legal.dialog.OtcOrderExitPayDialog;
import com.procoin.module.legal.entity.OtcOrderToPayResult;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.BadgeView;
import com.procoin.widgets.CircleImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 向卖家付款页面
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class LegalPayActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {


    @BindView(R.id.llChat)
    LinearLayout llChat;
    @BindView(R.id.tvTolCny)
    TextView tvTolCny;
    @BindView(R.id.llCopyTolCny)
    LinearLayout llCopyTolCny;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvSellerName)
    TextView tvSellerName;
    //    @BindView(R.id.tvShowUserTip)
//    TextView tvShowUserTip;
    @BindView(R.id.llCopyName)
    LinearLayout llCopyName;
    @BindView(R.id.tvAccountType)
    TextView tvAccountType;
    @BindView(R.id.tvAccount)
    TextView tvAccount;
    @BindView(R.id.tvOrOrBank)
    TextView tvOrOrBank;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.llBankName)
    LinearLayout llBankName;
    @BindView(R.id.llQrOrBank)
    LinearLayout llQrOrBank;
    @BindView(R.id.tvAlertTip)
    TextView tvAlertTip;
    @BindView(R.id.tvReceivedConfirm)
    TextView tvReceivedConfirm;
    @BindView(R.id.llAccount)
    LinearLayout llAccount;
    @BindView(R.id.rvShowUserTip)
    RecyclerView rvShowUserTip;
    @BindView(R.id.llSeller)
    LinearLayout llSeller;


    private InoutMarkPayDialog inoutMarkPayDialog;
    private OtcOrderExitPayDialog otcOrderExitPayDialog;


    private Call<ResponseBody> getInoutMarkPayCall;
    private Call<ResponseBody> orderCancelCall;
    private Call<ResponseBody> getOrderCashDetailCall;

    private OtcOrderToPayResult otcOrderToPayResult;

    private long orderCashId;
    private long showUserId;
    private long showPaymentId;

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

    public static void pageJump(Context context, long orderCashId, long showUserId, long showPaymentId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        bundle.putLong(CommonConst.SHOWUSERID, showUserId);
        bundle.putLong(CommonConst.SHOWPAYMENTID, showPaymentId);
        PageJumpUtil.pageJump(context, LegalPayActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.legal_pay;
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
        immersionBar.statusBarDarkFont(true, CommonConst.STATUSBAR_ALPHA).init();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }

            if (bundle.containsKey(CommonConst.SHOWUSERID)) {
                showUserId = bundle.getLong(CommonConst.SHOWUSERID, 0l);
            }

            if (bundle.containsKey(CommonConst.SHOWPAYMENTID)) {
                showPaymentId = bundle.getLong(CommonConst.SHOWPAYMENTID, 0l);
            }
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }

        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
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

        if (otcOrderToPayResult != null) {
            tvTolCny.setText("¥ " + otcOrderToPayResult.tolPrice);
            startCountDownTime();

            tvDesc.setText(otcOrderToPayResult.payTip);
            tjrImageLoaderUtil.displayImage(otcOrderToPayResult.showUserLogo, ivHead);
            tvUserName.setText(otcOrderToPayResult.showUserName);
            if (!TextUtils.isEmpty(otcOrderToPayResult.showUserTip)) {
                ShowUserTipAdapter showUserTipAdapter = new ShowUserTipAdapter(this);
                rvShowUserTip.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                rvShowUserTip.setAdapter(showUserTipAdapter);
                showUserTipAdapter.setData(otcOrderToPayResult.showUserTip.split("，"));
            }
            tvSellerName.setText(otcOrderToPayResult.showRealName);
            tvAccount.setText(otcOrderToPayResult.receiptNo);

            if (otcOrderToPayResult.receiptType == 1 || otcOrderToPayResult.receiptType == 2) {
                tvOrOrBank.setText("收款码");
                tvAccountType.setText(otcOrderToPayResult.receiptTypeValue + "账号");
                tjrImageLoaderUtil.displayImage(otcOrderToPayResult.qrCode, ivQr);
                llBankName.setVisibility(View.GONE);
            } else if (otcOrderToPayResult.receiptType == 3) {
                tvOrOrBank.setText("开户银行");
                tvAccountType.setText(otcOrderToPayResult.receiptTypeValue + "号");
                llBankName.setVisibility(View.VISIBLE);
                tvBankName.setText(otcOrderToPayResult.bankName);
            }
            tvAlertTip.setText(otcOrderToPayResult.alertTip);
            llQrOrBank.setOnClickListener(this);
            llAccount.setOnClickListener(this);
            llCopyName.setOnClickListener(this);

            llCopyTolCny.setOnClickListener(this);
            tvReceivedConfirm.setOnClickListener(this);

            llChat.setOnClickListener(this);
            llSeller.setOnClickListener(this);
        }


    }

    private void startCountDownTime() {
        if (otcOrderToPayResult == null) return;
        if (otcOrderToPayResult.paySecondTime > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(otcOrderToPayResult.paySecondTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = com.procoin.util.VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[2] + ":" + time[3]);
                        if (otcOrderExitPayDialog != null && otcOrderExitPayDialog.isShowing()) {
                            otcOrderExitPayDialog.setTime(time[2] + ":" + time[3]);
                        }
                    }
                }

                public void onFinish() {
                    tvTime.setText("00:00");
                    com.procoin.util.CommonUtil.showmessage("订单已经超时", LegalPayActivity.this);

                }
            };
            timer.start();
        } else {
            tvTime.setText("00:00");
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
                    if (otcOrderToPayResult != null) {
                        startInoutMarkPay();
                    }
                }
            };
        }
        inoutMarkPayDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            otcOrderToPayResult = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCopyName:
                com.procoin.util.CommonUtil.copyText(this, tvSellerName.getText().toString());
                break;
            case R.id.llChat:
            case R.id.llSeller:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;
            case R.id.llQrOrBank:
                if (otcOrderToPayResult != null) {
                    if (otcOrderToPayResult.receiptType == 1 || otcOrderToPayResult.receiptType == 2) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(CommonConst.PAGETYPE, 6);
                        bundle.putString(CommonConst.SINGLEPICSTRING, otcOrderToPayResult.qrCode);
                        PageJumpUtil.pageJumpToData(LegalPayActivity.this, ViewPagerPhotoViewActivity.class, bundle);
                    } else if (otcOrderToPayResult.receiptType == 3) {
                        com.procoin.util.CommonUtil.copyText(this, tvBankName.getText().toString());
                    }
                }
                break;
            case R.id.llAccount:
                com.procoin.util.CommonUtil.copyText(this, tvAccount.getText().toString());
                break;
            case R.id.llCopyTolCny:
                if (otcOrderToPayResult != null)
                    com.procoin.util.CommonUtil.copyText(this, otcOrderToPayResult.tolPrice);
                break;
            case R.id.tvReceivedConfirm:
                showInOutMarkDialog();
                break;

        }
    }

    private void showExitTipsDialog() {
        otcOrderExitPayDialog = new OtcOrderExitPayDialog(this) {
            @Override
            public void onclickOk() {
                PageJumpUtil.finishCurr(LegalPayActivity.this);
            }
        };
        otcOrderExitPayDialog.show();
    }


    @Override
    public void onBackPressed() {
        showExitTipsDialog();
    }

    private void startInoutMarkPay() {
        CommonUtil.cancelCall(getInoutMarkPayCall);
        getInoutMarkPayCall = VHttpServiceManager.getInstance().getVService().otcToMarkPayOrderSuccess(orderCashId);
        getInoutMarkPayCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, LegalPayActivity.this);
                    PageJumpUtil.finishCurr(LegalPayActivity.this);

                }
            }
        });
    }


    private void startTradeCashOrderDetail() {
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().otcToPayOrder(orderCashId, showUserId, showPaymentId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    chatStaff = resultData.getObject("chatStaff", ChatStaff.class);
                    otcOrderToPayResult = resultData.getObject("orderToPayResult", OtcOrderToPayResult.class);
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


}
