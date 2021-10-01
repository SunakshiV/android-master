package com.procoin.module.home.trade;

import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.BadgeView;
import com.procoin.R;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.trade.entity.ChatStaff;
import com.procoin.module.home.trade.entity.OrderCashStateEnum;
import com.procoin.module.myhome.entity.OrderCash;
import com.procoin.util.PageJumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提现详情页面
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class WithDrawInfoActivity extends TJRBaseActionBarSwipeBackObserverActivity implements View.OnClickListener {

    @BindView(R.id.ivState)
    ImageView ivState;
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
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvPayWayText)
    TextView tvPayWayText;
    @BindView(R.id.tvPayWay)
    TextView tvPayWay;
    @BindView(R.id.tvPayAccount)
    TextView tvPayAccount;
    @BindView(R.id.tvOrderNo)
    TextView tvOrderNo;
    @BindView(R.id.tvOrderTime)
    TextView tvOrderTime;

    @BindView(R.id.tvStateDetail)
    TextView tvStateDetail;

    @BindView(R.id.llChat)
    LinearLayout llChat;

    private Call<ResponseBody> getOrderCashDetailCall;

    private OrderCash orderCash;

    private long orderCashId;

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
        PageJumpUtil.pageJump(context, WithDrawInfoActivity.class, bundle);
    }

    @Override
    protected int setLayoutId() {
        return R.layout.with_draw_info;
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

        llCopyTolBalance.setOnClickListener(this);

        badgeChat = new BadgeView(this, llChat);
        badgeChat.setBadgeMargin(0, 0);
        badgeChat.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        badgeChat.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);

        startTradeCashOrderDetail();

    }

    @Override
    protected void showReConnection() {
    }

    private void setChatNewsCount() {
        if (chatStaff != null) {
            showPrivateChatNewsCount(chatStaff.chatTopic);
        }
    }

    public void showPrivateChatNewsCount(String chatTopic) {
        int chatCount = 0;
        if (getApplicationContext().getUser() != null) {
            chatCount = PrivateChatSharedPreferences.getPriChatRecordNum(getApplicationContext(), chatTopic, getApplicationContext().getUser().getUserId());
        }
        if (chatCount > 0) {//显示
            badgeChat.show();
            badgeChat.setBadgeText(com.procoin.util.CommonUtil.setNewsCount(chatCount));
        } else {//不显示
            badgeChat.hide();
        }


    }


    @Override
    protected void onResume() {
        super.onResume();
        setChatNewsCount();

    }

    private void setData() {
        setChatNewsCount();
        if (orderCash != null && orderCash.orderCashId > 0 && orderCash.receipt != null) {
            tvTolBalance.setText("¥ " + orderCash.balanceCny);
            tvPrice.setText("¥ " + orderCash.priceCny);
            tvNum.setText(orderCash.amount + " USDT");

            tvPayWay.setText(orderCash.receipt.receiptTypeValue);
            tvName.setText(orderCash.receipt.receiptName);
            tvPayAccount.setText(orderCash.receipt.receiptNo);
            tvOrderNo.setText(String.valueOf(orderCash.orderCashId));
            tvOrderTime.setText(DateUtils.getStringDateOfString2(String.valueOf(orderCash.createTime), DateUtils.TEMPLATE_yyyyMMdd_HHmmss));

            if (orderCash.receipt.receiptType == 1 || orderCash.receipt.receiptType == 2) {
                if (orderCash.receipt.receiptType == 1) {
                    tvPayWayText.setText("支付宝账号");
                } else {
                    tvPayWayText.setText("微信账号");
                }

            } else {
                tvPayWayText.setText("银行卡号");
            }

            OrderCashStateEnum orderCashStateEnum = OrderCashStateEnum.getOrderCashState(orderCash.state);
            ivState.setImageResource(orderCashStateEnum.getIcon());
            tvState.setText(orderCash.stateDesc);

            if (orderCash.state == OrderCashStateEnum.wait_pay.getState()) {
                tvStateDetail.setText("提现申请已提交，等待平台放款");
            } else {
                tvStateDetail.setText("提现已完成");
            }

            llChat.setOnClickListener(this);

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
            case R.id.llChat:
                if (chatStaff != null) {
                    ChatRoomActivity.pageJump(this, chatStaff.chatTopic, chatStaff.userName, chatStaff.headUrl);
                }
                break;


        }
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
    }


}
