package com.procoin.module.home.trade;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.MainApplication;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.photo.ViewPagerPhotoViewActivity;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.VeDate;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.util.TjrImageLoaderUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
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

public class OrderCashInfoActivityBak extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.llTime)
    LinearLayout llTime;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.ivQr)
    ImageView ivQr;
    @BindView(R.id.tvGoPay)
    TextView tvGoPay;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvBankNo)
    TextView tvBankNo;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.llBank)
    LinearLayout llBank;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.llQuestionMark)
    LinearLayout llQuestionMark;
    @BindView(R.id.tvAliPayAccount)
    TextView tvAliPayAccount;
    @BindView(R.id.tvAliUserName)
    TextView tvAliUserName;
    @BindView(R.id.llAliPay)
    LinearLayout llAliPay;
    @BindView(R.id.tvWechatAccount)
    TextView tvWechatAccount;
    @BindView(R.id.tvWechatUserName)
    TextView tvWechatUserName;
    @BindView(R.id.llWechatPay)
    LinearLayout llWechatPay;
    @BindView(R.id.tvSaveQr)
    TextView tvSaveQr;
    @BindView(R.id.ivCopyBankAccount)
    ImageView ivCopyBankAccount;
    @BindView(R.id.ivCopyName)
    ImageView ivCopyName;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvOrderState)
    TextView tvOrderState;
    @BindView(R.id.llOrderState)
    LinearLayout llOrderState;

    @BindView(R.id.llBottomBtn)
    LinearLayout llBottomBtn;

    @BindView(R.id.llExplain)
    LinearLayout llExplain;


    private Call<ResponseBody> getInoutMarkPayCall;

    private Call<ResponseBody> getOrderCashDetailCall;

    private OrderCash orderCash;
    private String title = "";

    private long orderCashId;


    private TjrImageLoaderUtil tjrImageLoaderUtil;
    private CountDownTimer timer;


    public static void pageJump(Context context, String title, long orderCashId) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.TITLE, title);
        bundle.putLong(CommonConst.ORDERCASHID, orderCashId);
        PageJumpUtil.pageJump(context, OrderCashInfoActivityBak.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.unpaid_info_bak;
    }

    @Override
    protected String getActivityTitle() {
        return "";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.unpaid_info);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.TITLE)) {
                title = bundle.getString(CommonConst.TITLE, "");
            }

            if (bundle.containsKey(CommonConst.ORDERCASHID)) {
                orderCashId = bundle.getLong(CommonConst.ORDERCASHID, 0l);
            }
        }
        if (orderCashId == 0l) {
            CommonUtil.showmessage("参数错误", this);
            finish();
            return;
        }
        llQuestionMark.setOnClickListener(this);
//        llQuestionMark.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        showPopupMenu(llQuestionMark,getScreenWidth()*2/3, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                    case MotionEvent.ACTION_CANCEL:
//                        dimissPop();
//                }
//                return true;
//            }
//        });
        tjrImageLoaderUtil = new TjrImageLoaderUtil();
        startTradeCashOrderDetail();

    }

    private void setData() {
        if (orderCash != null && orderCash.orderCashId > 0 && orderCash.receipt != null) {
            tvMoney.setText(String.valueOf(orderCash.balanceCny));
            tvMoney.setOnClickListener(this);
            mActionBar.setTitle(title);
            tvOrderNo.setText(String.valueOf(orderCash.orderCashId));


            if (orderCash.receipt.receiptType == 1 || orderCash.receipt.receiptType == 2) {
                llBank.setVisibility(View.GONE);
                ivQr.setVisibility(View.VISIBLE);
                tjrImageLoaderUtil.displayImage(orderCash.receipt.qrCode, ivQr);
                ivQr.setOnClickListener(this);

                if (orderCash.receipt.receiptType == 1) {
                    llAliPay.setVisibility(View.VISIBLE);
                    llWechatPay.setVisibility(View.GONE);
                    tvAliPayAccount.setText(orderCash.receipt.receiptNo);
                    tvAliUserName.setText(orderCash.receipt.receiptName);

                } else {
                    llAliPay.setVisibility(View.GONE);
                    llWechatPay.setVisibility(View.VISIBLE);
                    tvWechatAccount.setText(orderCash.receipt.receiptNo);
                    tvWechatUserName.setText(orderCash.receipt.receiptName);


                }


            } else if (orderCash.receipt.receiptType == 3) {
                llBank.setVisibility(View.VISIBLE);
                ivQr.setVisibility(View.GONE);

                tvName.setText(orderCash.receipt.receiptName);
                tvBankNo.setText(orderCash.receipt.receiptNo);
                tvBankName.setText(orderCash.receipt.bankName);

                tvGoPay.setVisibility(View.GONE);
                tvSaveQr.setVisibility(View.GONE);

                ivCopyBankAccount.setOnClickListener(this);
                ivCopyName.setOnClickListener(this);

            }

            if (orderCash.state == 0) {
                llBottomBtn.setVisibility(View.VISIBLE);
                llExplain.setVisibility(View.VISIBLE);
                tvSubmit.setOnClickListener(this);
                llTime.setVisibility(View.VISIBLE);
                startCountDownTime();
                tvSubmit.setText("标记为已支付");
                tvSubmit.setEnabled(true);
                llOrderState.setVisibility(View.GONE);
                if (orderCash.receipt.receiptType == 1) {
                    tvGoPay.setVisibility(View.VISIBLE);
                    tvGoPay.setOnClickListener(this);
                    tvSaveQr.setVisibility(View.GONE);
                } else if (orderCash.receipt.receiptType == 2) {
                    tvGoPay.setVisibility(View.GONE);
                    tvSaveQr.setVisibility(View.VISIBLE);
                    tvSaveQr.setOnClickListener(this);
                } else if (orderCash.receipt.receiptType == 3) {
                    tvGoPay.setVisibility(View.GONE);
                    tvSaveQr.setVisibility(View.GONE);
                }
            } else {
                llBottomBtn.setVisibility(View.GONE);
                llExplain.setVisibility(View.GONE);
                llTime.setVisibility(View.GONE);
                llOrderState.setVisibility(View.VISIBLE);
                tvOrderState.setText(orderCash.stateDesc);
            }
//            if (orderCash.state == 0) {
//                tvSubmit.setText("标记为已支付");
//                tvSubmit.setEnabled(true);
//                startCountDownTime();
//            } else {
//                tvSubmit.setText("已标记付款");
//                tvSubmit.setEnabled(false);
//            }
        }


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
//            llTime.setVisibility(View.VISIBLE);
            timer = new CountDownTimer(ss * 1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    String[] time = VeDate.formatSecToTime(millisUntilFinished / 1000);
                    if (time != null && time.length == 4) {
                        tvTime.setText("剩余" + time[2] + "分" + time[3] + "秒");
                    }
                }

                public void onFinish() {
                    tvTime.setText("剩余00分00秒");
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
            case R.id.tvSubmit:
                if (orderCash != null) {
                    showSubmitDialog();
                }
                break;
            case R.id.tvGoPay:
                if (orderCash != null) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(OrderCashInfoActivityBak.this, orderCash.receipt.qrContent);
                }

                break;
            case R.id.tvSaveQr:
                if (orderCash == null || orderCash.receipt == null) return;
                saveToSdcard(orderCash.receipt.qrCode);
                break;
            case R.id.ivCopyBankAccount:
                if (orderCash != null && orderCash.receipt != null)
                    com.procoin.util.CommonUtil.copyText(OrderCashInfoActivityBak.this, orderCash.receipt.receiptNo);
                break;

            case R.id.ivCopyName:
                if (orderCash != null && orderCash.receipt != null)
                    com.procoin.util.CommonUtil.copyText(OrderCashInfoActivityBak.this, orderCash.receipt.receiptName);
                break;
            case R.id.llQuestionMark:
                showPopupMenu(llQuestionMark, getScreenWidth() * 2 / 3, ViewGroup.LayoutParams.WRAP_CONTENT);
                break;
            case R.id.ivQr:
                if (orderCash != null &&orderCash.receipt!=null&& !TextUtils.isEmpty(orderCash.receipt.qrCode)) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(CommonConst.PAGETYPE, 0);
                    bundle.putString(CommonConst.SINGLEPICSTRING, orderCash.receipt.qrCode);
                    PageJumpUtil.pageJumpToData(OrderCashInfoActivityBak.this, ViewPagerPhotoViewActivity.class, bundle);
                }
                break;
            case R.id.tvMoney:
                com.procoin.util.CommonUtil.copyText(OrderCashInfoActivityBak.this,tvMoney.getText());
                break;

        }
    }

//                    ivCopyBankAccount.setOnClickListener(this);
//                ivCopyName.setOnClickListener(this);

    private void startInoutMarkPay(long inOutId) {
        CommonUtil.cancelCall(getInoutMarkPayCall);
        getInoutMarkPayCall = VHttpServiceManager.getInstance().getVService().inoutMarkPay(inOutId);
        getInoutMarkPayCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, OrderCashInfoActivityBak.this);
                    if (orderCash != null) {
                        orderCash.state = 1;
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

    private void startTradeCashOrderDetail() {
        CommonUtil.cancelCall(getOrderCashDetailCall);
        getOrderCashDetailCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderDetail(orderCashId);
        getOrderCashDetailCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
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
