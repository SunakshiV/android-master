package com.procoin.module.legal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.appcompat.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.procoin.MainApplication;
import com.procoin.R;
import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.trade.entity.ChatStaff;
import com.procoin.module.legal.dialog.OtcConfirmReceivedPayDialog;
import com.procoin.module.legal.dialog.OtcOrderCancelDialog;
import com.procoin.module.legal.dialog.ResetReceiptTermDialogFragment;
import com.procoin.module.legal.entity.OtcOrderInfo;
import com.procoin.module.legal.entity.OtcOrderStateEnum;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.DateUtils;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.VeDate;
import com.procoin.widgets.BadgeView;
import com.procoin.widgets.CircleImageView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * ????????????????????????????????????
 * <p>
 * <p>
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class LegalOrderInfoActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {


    @BindView(R.id.llChat)
    LinearLayout llChat;
    @BindView(R.id.tvState)
    TextView tvState;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvTimeRight)
    TextView tvTimeRight;
    @BindView(R.id.icState)
    AppCompatImageView icState;
    @BindView(R.id.tvDesc)
    TextView tvDesc;
    @BindView(R.id.tvTolCny)
    TextView tvTolCny;
    @BindView(R.id.llCopyTolBalance)
    LinearLayout llCopyTolBalance;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvNum)
    TextView tvNum;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.llCopyOrderNo)
    LinearLayout llCopyOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;
    @BindView(R.id.ivHead)
    CircleImageView ivHead;
    @BindView(R.id.tvUserName)
    TextView tvUserName;
    @BindView(R.id.tvUserRealName)
    TextView tvUserRealName;
    @BindView(R.id.llCopyName)
    LinearLayout llCopyName;
    @BindView(R.id.ivPayLogo)
    ImageView ivPayLogo;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.tvGoPay)
    TextView tvGoPay;
    @BindView(R.id.llBtn1)
    LinearLayout llBtn1;
    //    @BindView(R.id.tvSubmit)
//    TextView tvSubmit;
    @BindView(R.id.tvAppeal)
    TextView tvAppeal;
    @BindView(R.id.tvReceivedConfirm)
    TextView tvReceivedConfirm;
    @BindView(R.id.tvAppeal2)
    TextView tvAppeal2;
    @BindView(R.id.tvReceivedConfirm2)
    TextView tvReceivedConfirm2;
    @BindView(R.id.llBtn2)
    LinearLayout llBtn2;
    @BindView(R.id.tvBuyOrSellName)
    TextView tvBuyOrSellName;
    @BindView(R.id.tvBuyOrSellRealName)
    TextView tvBuyOrSellRealName;

    @BindView(R.id.llPayWay)
    LinearLayout llPayWay;
    @BindView(R.id.ivPayWay)
    ImageView ivPayWay;
    @BindView(R.id.llSeller)
    LinearLayout llSeller;

    private Call<ResponseBody> orderCancelCall;
    private Call<ResponseBody> getOrderCashDetailCall;
    private Call<ResponseBody> otcToConfirmReceivedPayCall;

    private OtcConfirmReceivedPayDialog otcConfirmReceivedPayDialog;
    private OtcOrderCancelDialog otcOrderCancelDialog;


    private OtcOrderInfo orderCash;
    private long orderCashId;

    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private CountDownTimer timer;

    private ChatStaff chatStaff;//????????????


    private BadgeView badgeChat;


    private Group<Receipt> receipts;//????????????
    private Receipt receipt;//????????????????????????
    private ResetReceiptTermDialogFragment selectReceiptTermDialogFragment;


    @Override
    protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //?????????????????????
                setChatNewsCount();
                break;

            default:
                break;
        }
    }

    public static void pageJump(Context context, long orderCashId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        PageJumpUtil.pageJump(context, LegalOrderInfoActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.legal_order_info;
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
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage("????????????", this);
            finish();
            return;
        }


        llCopyName.setOnClickListener(this);
        llCopyOrderNo.setOnClickListener(this);
        llCopyTolBalance.setOnClickListener(this);
        llChat.setOnClickListener(this);
        llSeller.setOnClickListener(this);

        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeBackgroundColor(Color.parseColor("#CCFF0000"));
        badgeChat.setBadgeMargin(15, 10);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        tjrImageLoaderUtil = new TjrImageLoaderUtil();

    }

    private void hiddenBtn() {//??????????????????
        llBtn1.setVisibility(View.GONE);
        tvAppeal.setVisibility(View.GONE);
        tvReceivedConfirm.setVisibility(View.GONE);
        llBtn2.setVisibility(View.GONE);
        ivPayWay.setVisibility(View.GONE);
        cancelTimer();
        tvTime.setText("");
    }


    @Override
    protected void onResume() {
        super.onResume();
        startOtcGetOrderDetail();
        Log.d("setChatNewsCount", "onResume==");
        setChatNewsCount();

    }

    public void showPrivateChatNewsCount(String chatTopic) {
        int chatCount = 0;
        if (getApplicationContext().getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(getApplicationContext(), chatTopic, getApplicationContext().getUser().getUserId());
        }
        Log.d("setChatNewsCount", "chatCount==" + chatCount);
        if (chatCount > 0) {//??????
            badgeChat.show();
            badgeChat.setBadgeText(com.procoin.util.CommonUtil.setNewsCount(chatCount));
        } else {//?????????
            badgeChat.hide();
        }
    }
//    wait(0), //????????????????????????????????????????????????????????????????????????????????????????????????????????????
//    mark(1), //?????????????????????????????????????????????->?????????????????????????????????????????????????????????
//    done(2), //?????????????????????????????????????????????????????????????????????->??????
//    appeal(3), //?????????mark->appeal
//    expire(-1), //?????????????????????????????????
//    cancel(-2), //????????????
//    admin_cancel(-3);// ????????????

    private void setData() {
        setChatNewsCount();
        if (orderCash != null) {
            tvState.setText(orderCash.stateValue);
            tvTips.setText(orderCash.stateTip);
            tvDesc.setText(orderCash.buySellValue);
            tvTolCny.setText("?? " + orderCash.tolPrice);
            tvPrice.setText("?? " + orderCash.price);
            tvNum.setText(orderCash.amount + " USDT");
            tvOrderNo.setText(String.valueOf(orderCash.orderId));
            tvOrderTime.setText(DateUtils.getStringDateOfString2(orderCash.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmmss));
            tvUserName.setText(orderCash.showUserName);
            tvUserRealName.setText(orderCash.showRealName);
            tjrImageLoaderUtil.displayImage(orderCash.showUserLogo, ivHead);

            receipts = new Gson().fromJson(orderCash.showPayWay, new TypeToken<Group<Receipt>>() {
            }.getType());

            if (receipts != null && receipts.size() > 0) {
                if (isBuyer()) {//????????????
                    if (receipt == null) {//???????????????????????????????????????,
                        receipt = receipts.get(0);
                    }
                }else{
                    receipt = receipts.get(0);//?????????????????????????????????
                }
                tjrImageLoaderUtil.displayImage(receipt.receiptLogo, ivPayLogo);
                tvPayWay.setText(receipt.receiptTypeValue);
            }

            if (isBuyer()) {//????????????
                tvBuyOrSellName.setText("????????????");
                tvBuyOrSellRealName.setText("????????????");
            } else {
                tvBuyOrSellName.setText("????????????");
                tvBuyOrSellRealName.setText("????????????");
            }
            llPayWay.setOnClickListener(null);
            if (orderCash.state == OtcOrderStateEnum.wait.state) {
                if (isBuyer()) {//???????????? ?????????
                    llBtn1.setVisibility(View.VISIBLE);
                    icState.setImageResource(R.drawable.ic_svg_time);
                    tvTimeRight.setVisibility(View.GONE);
                    tvCancel.setOnClickListener(this);
                    tvGoPay.setOnClickListener(this);
                    startCountDownTimeLeft();
                    if (receipts != null && receipts.size() > 1) {
                        ivPayWay.setVisibility(View.VISIBLE);
                        llPayWay.setOnClickListener(this);
                    }

                } else {//???????????? ??????????????????
                    tvReceivedConfirm.setVisibility(View.VISIBLE);
                    tvReceivedConfirm.setOnClickListener(this);
                    startCountDownTimeRight();
                }
            } else if (orderCash.state == OtcOrderStateEnum.mark.state) {
                if (isBuyer()) {//???????????? ??????????????????
                    tvAppeal.setVisibility(View.VISIBLE);
                    tvAppeal.setOnClickListener(this);
                    startCountDownTimeRight();
                } else {//???????????? ???????????????
                    icState.setImageResource(R.drawable.ic_svg_time);
                    tvTimeRight.setVisibility(View.GONE);
                    llBtn2.setVisibility(View.VISIBLE);
                    tvAppeal2.setOnClickListener(this);
                    tvReceivedConfirm2.setOnClickListener(this);
                }

            } else if (orderCash.state == OtcOrderStateEnum.done.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_success);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.appeal.state) {
                icState.setImageResource(R.drawable.ic_svg_time);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.expire.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_false);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.cancel.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_false);
                tvTimeRight.setVisibility(View.GONE);
            } else if (orderCash.state == OtcOrderStateEnum.admin_cancel.state) {
                icState.setImageResource(R.drawable.ic_svg_otc_false);
                tvTimeRight.setVisibility(View.GONE);
            }
        }

    }


    private void showLegalQuickSellDialogFragment(Group<Receipt> receipts) {
        selectReceiptTermDialogFragment = ResetReceiptTermDialogFragment.newInstance(receipts, new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                if (selectReceiptTermDialogFragment != null) {
                    selectReceiptTermDialogFragment.dismiss();
                }
                receipt = (Receipt) t;
                tjrImageLoaderUtil.displayImage(receipt.receiptLogo, ivPayLogo);
                tvPayWay.setText(receipt.receiptTypeValue);

            }
        });
        selectReceiptTermDialogFragment.showDialog(getSupportFragmentManager(), "");
    }

    private boolean isBuyer() {
        return orderCash != null && getUserId().equals(orderCash.buyUserId);
    }

    private boolean isSeller() {
        return orderCash != null && getUserId().equals(orderCash.sellUserId);
    }


    private void setChatNewsCount() {
        Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff==" + chatStaff);

        if (chatStaff != null) {
            Log.d("setChatNewsCount", "setChatNewsCount.....chatStaff.chatTopic==" + chatStaff.chatTopic);
            showPrivateChatNewsCount(chatStaff.chatTopic);
        }
    }


    private void showOtcConfirmReceivedPayDialog() {
        otcConfirmReceivedPayDialog = new OtcConfirmReceivedPayDialog(this) {
            @Override
            public void onclickOk() {
                if (orderCash != null) {
                    startOtcToConfirmReceivedPay();
                }
            }
        };
        otcConfirmReceivedPayDialog.show();
    }

    private void startOtcToConfirmReceivedPay() {
        CommonUtil.cancelCall(otcToConfirmReceivedPayCall);
        otcToConfirmReceivedPayCall = VHttpServiceManager.getInstance().getVService().otcToConfirmReceivedPay(orderCashId);
        otcToConfirmReceivedPayCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            }
        });
    }


    private void startCountDownTimeLeft() {
        if (orderCash == null) return;
//        Long ss = VeDate.strLongToDate(String.valueOf(orderCash.expireTime)).getTime() - VeDate.strLongToDate(String.valueOf(orderCash.createTime)).getTime();
        if (orderCash.paySecondTime > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(orderCash.paySecondTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTime.setText(time[2] + ":" + time[3]);
                    }
                }

                public void onFinish() {
                    tvTime.setText("00:00");
//                    llBtn1.setVisibility(View.GONE);
                    com.procoin.util.CommonUtil.showmessage("??????????????????", LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            };
            timer.start();
        } else {
            tvTime.setText("00:00");
//            llBtn1.setVisibility(View.GONE);
        }
    }


    private void startCountDownTimeRight() {
        if (orderCash == null) return;
        icState.setImageResource(R.drawable.ic_svg_otc_time_bg);
        tvTimeRight.setVisibility(View.VISIBLE);
        if (orderCash.paySecondTime > 0) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            timer = new CountDownTimer(orderCash.paySecondTime * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTimeRight.setText(time[2] + ":" + time[3]);
                    }
                }

                public void onFinish() {
                    tvTimeRight.setText("00:00");
//                    tvReceivedConfirm.setVisibility(View.GONE);
//                    tvAppeal.setVisibility(View.GONE);
                    com.procoin.util.CommonUtil.showmessage("??????????????????", LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            };
            timer.start();
        } else {
            tvTimeRight.setText("00:00");
//            tvReceivedConfirm.setVisibility(View.GONE);
//            tvAppeal.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            orderCash = null;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCopyTolBalance:
//                if (orderCash != null)
//                    com.procoin.util.CommonUtil.copyText(this, orderCash.balanceCny);
                break;
            case R.id.llCopyOrderNo:
                com.procoin.util.CommonUtil.copyText(this, tvOrderNo.getText().toString());
                break;
            case R.id.tvCancel:
                showCancleOrderDialog();
                break;
            case R.id.llChat:
            case R.id.llSeller:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;

            case R.id.llQr:
//                if (orderCash != null && orderCash.receipt != null && !TextUtils.isEmpty(orderCash.receipt.qrCode)) {
//                    Bundle bundle = new Bundle();
//                    bundle.putInt(CommonConst.PAGETYPE, 6);
//                    bundle.putString(CommonConst.SINGLEPICSTRING, orderCash.receipt.qrCode);
//                    PageJumpUtil.pageJumpToData(LegalOrderInfoActivity.this, ViewPagerPhotoViewActivity.class, bundle);
//                }
                break;
            case R.id.tvGoPay:
                if (orderCash != null && receipt != null) {
                    LegalPayActivity.pageJump(this, orderCash.orderId, orderCash.showUserId, receipt.paymentId);
                }
                break;
            case R.id.tvReceivedConfirm:
                break;
            case R.id.tvReceivedConfirm2:
                showOtcConfirmReceivedPayDialog();
                break;
            case R.id.tvAppeal:
            case R.id.tvAppeal2:
                OtcAppealActivity.pageJump(LegalOrderInfoActivity.this, orderCashId);
                break;
            case R.id.llPayWay:
                if (receipts != null && receipts.size() > 0) {
                    showLegalQuickSellDialogFragment(receipts);
                }
                break;


        }
    }

    private void showCancleOrderDialog() {
        otcOrderCancelDialog = new OtcOrderCancelDialog(this) {
            @Override
            public void onclickOk() {
                if (orderCash != null) {
                    startOrderCancel();
                }
            }
        };
        otcOrderCancelDialog.show();
    }


    private void startOrderCancel() {
        CommonUtil.cancelCall(orderCancelCall);
        orderCancelCall = VHttpServiceManager.getInstance().getVService().otcCancelOrder(orderCashId);
        orderCancelCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, LegalOrderInfoActivity.this);
                    startOtcGetOrderDetail();
                }
            }
        });
    }


    private void startOtcGetOrderDetail() {
        hiddenBtn();//?????????????????????
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().otcGetOrderDetail(orderCashId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    chatStaff = resultData.getObject("chatStaff", ChatStaff.class);
                    orderCash = resultData.getObject("order", OtcOrderInfo.class);
                    setData();
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelTimer();
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean saveToSdcard(String uri) {
        if (TextUtils.isEmpty(uri)) return false;
        // TODO remove ??????
        File file = null;
        try {
//            String uri = getUrl(imgUrls.get(veiePhotoView.getCurrentItem()));
            Log.d("saveToSdcard", "uri==" + uri);
            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(uri);
            if (bitmap == null) {
                com.procoin.social.util.CommonUtil.showToast(this, "?????????????????????", Gravity.BOTTOM);
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
                // ????????????????????????????????????
//                try {
//                    MediaStore.Images.Media.insertImage(this.getContentResolver(),
//                            file.getAbsolutePath(), fileName, null);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
                this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + file.getPath())));
                // ????????????????????????

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("saveToSdcard", "e==" + e);
            com.procoin.social.util.CommonUtil.showToast(this, "??????????????????", Gravity.BOTTOM);
            return false;
        }
        CommonUtil.showmessage("?????????????????????", this);
        return true;
    }


    PopupWindow pop;

    private void showPopupMenu(View parent, int width, int height) {
        if (pop == null) {
            pop = new PopupWindow(InflaterUtils.inflateView(this, R.layout.money_mark), width, height);//
//            pop.setOutsideTouchable(false);
//            pop.setFocusable(false);// ?????????????????????Grid????????????ItemClick
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));// ????????????????????????

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
