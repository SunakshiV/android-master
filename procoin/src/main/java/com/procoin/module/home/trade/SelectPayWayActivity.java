package com.procoin.module.home.trade;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.adapter.SelectPayWayAdapter;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 选择支付方式(已经废弃了)
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class SelectPayWayActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.rv_list)
    RecyclerView rvList;


    private SelectPayWayAdapter selectPayWayAdapter;
    private Call<ResponseBody> getReceiptsCall;
    private Call<ResponseBody> getInoutCreateCall;

    //    private int type=-1;//type==0 充值USDT  type==1现金支付订单
    private String entrustAmount = "0.00";
    private String symbol = "";
    private String unitPrice = "0.00";
    private int isLimit;
    private int buySell;//1：买/充值，-1：卖/提现

//    /**
//     * 充值usdt专用
//     *
//     * @param context
//     * @param entrustAmount
//     */
//    public static void pageJump(Context context, String entrustAmount, String symbol) {
//        Bundle bundle = new Bundle();
//        bundle.putString(CommonConst.ENTRUSTAMOUNT, entrustAmount);
//        bundle.putString(CommonConst.SYMBOL, symbol);
////        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, 0);
//        PageJumpUtil.pageJump(context, SelectPayWayActivity.class, bundle);
//    }


    /**
     * 买币余额不足时候用到
     *
     * @param context
     * @param entrustAmount
     * @param symbol
     * @param isLimit
     */
    public static void pageJump(Context context, String entrustAmount, String symbol, String unitPrice, int isLimit, int buySell) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.ENTRUSTAMOUNT, entrustAmount);
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putString(CommonConst.UNITPRICE, unitPrice);
        bundle.putInt(CommonConst.ISLIMIT, isLimit);
        bundle.putInt(CommonConst.BUYSELL, buySell);
//        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, 1);
        PageJumpUtil.pageJump(context, SelectPayWayActivity.class, bundle);
    }


    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return "选择支付方式";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.simple_recycleview_2);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ENTRUSTAMOUNT)) {
                entrustAmount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                symbol = bundle.getString(CommonConst.SYMBOL, "");
            }
            if (bundle.containsKey(CommonConst.UNITPRICE)) {
                unitPrice = bundle.getString(CommonConst.UNITPRICE, "0.00");
            }
            if (bundle.containsKey(CommonConst.BUYSELL)) {
                buySell = bundle.getInt(CommonConst.BUYSELL, 1);
            }
//            if (bundle.containsKey(CommonConst.KEY_EXTRAS_TYPE)) {
//                type = bundle.getInt(CommonConst.KEY_EXTRAS_TYPE, 0);
//            }
            if (bundle.containsKey(CommonConst.ISLIMIT)) {
                isLimit = bundle.getInt(CommonConst.ISLIMIT, 0);
            }

        }
        selectPayWayAdapter = new SelectPayWayAdapter(this);
        rvList.setLayoutManager(new LinearLayoutManager(this));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this, true));
        rvList.setAdapter(selectPayWayAdapter);

        selectPayWayAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                Receipt receipt = ((Receipt) t);
                startTradeCashOrder(receipt.paymentId, receipt.receiptType);
//                if (type == 0) {
//                    startInoutCreate(receipt.receiptId, receipt.receiptType);
//                } else if (type == 1) {
//                    startTradeCashUsersubmit(receipt.receiptId, receipt.receiptType);
//                } else {
//                    CommonUtil.showmessage("参数错误，请重新进入", SelectPayWayActivity.this);
//                }

            }
        });

        startGetReceipts();
    }

    private void startGetReceipts() {
        CommonUtil.cancelCall(getReceiptsCall);
        getReceiptsCall = VHttpServiceManager.getInstance().getVService().receiptsForPay();
        getReceiptsCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Receipt> group = resultData.getGroup("receiptList", new TypeToken<Group<Receipt>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        if (group.size() == 1) {
                            selectPayWayAdapter.setDefaultSelectedOnSize1();
                        }
                        selectPayWayAdapter.setGroup(group);
                    }
                }
            }
        });
    }

//    private void startInoutCreate(long receiptId,final int type) {
//        CommonUtil.cancelCall(getInoutCreateCall);
//        getInoutCreateCall = VHttpServiceManager.getInstance().getVService().inoutCreate(entrustAmount, receiptId, 1);
//        getInoutCreateCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, SelectPayWayActivity.this);
//                    OrderCash unPaind = resultData.getObject("order", OrderCash.class);
//                    if (unPaind != null) {
//                        getApplicationContext().unPaid = unPaind;
//                        String title="";
//                        if(type==3){
//                            title="现金充值"+symbol+"订单";
//                        }else{
//                            title="扫码充值"+symbol+"订单";
//                        }
//                        setSwipeBackEnable(false);
//                        setOverrideExitAniamtion(false);
//                        OrderCashInfoActivity.pageJump(SelectPayWayActivity.this, title);
//                        PageJumpUtil.finishCurr(SelectPayWayActivity.this);
//                        overridePendingTransition(R.anim.login_enter, R.anim.login_out);
////                        PageJumpUtil.pageJump(SelectPayWayActivity.this,OrderCashInfoActivity.class);
//                    }
//                }
//            }
//        });
//    }

    private void startTradeCashOrder(long receiptId, final int type) {
        CommonUtil.cancelCall(getInoutCreateCall);
//        getInoutCreateCall = VHttpServiceManager.getInstance().getVService().tradeCashOrder(symbol, entrustAmount, unitPrice, receiptId, isLimit,buySell);
//        getInoutCreateCall.enqueue(new MyCallBack(this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    CommonUtil.showmessage(resultData.msg, SelectPayWayActivity.this);
//                    OrderCash orderCash = resultData.getObject("order", OrderCash.class);
//                    if (orderCash != null) {
//                        String title = "";
//                        if (type == 3) {
//                            title = "现金支付" + symbol + "订单";
//                        } else {
//                            title = "扫码支付" + symbol + "订单";
//                        }
//                        setSwipeBackEnable(false);
//                        setOverrideExitAniamtion(false);
//                        OrderCashInfoActivity.pageJump(SelectPayWayActivity.this, title,orderCash.orderCashId);
//                        PageJumpUtil.finishCurr(SelectPayWayActivity.this);
//                        overridePendingTransition(R.anim.login_enter, R.anim.login_out);
////                        PageJumpUtil.pageJump(SelectPayWayActivity.this,OrderCashInfoActivity.class);
//                    }
//                }
//            }
//        });
    }

}
