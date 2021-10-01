package com.procoin.module.legal;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.legal.adapter.MultiSelectQuickPayWayAdapter;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.module.myhome.AddReceiptTermActivity;
import com.procoin.module.myhome.entity.AddPaymentTern;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.SimpleSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 添加广告
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class AddAdActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.etMinCny)
    EditText etMinCny;
    @BindView(R.id.etMaxCny)
    EditText etMaxCny;
    @BindView(R.id.rvType)
    RecyclerView rvType;
    //    @BindView(R.id.flAddPayMent)
//    FrameLayout flAddPayMent;
    @BindView(R.id.etContent)
    EditText etContent;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvBestPrice)
    TextView tvBestPrice;
    @BindView(R.id.tvBestPriceHint)
    TextView tvBestPriceHint;
    @BindView(R.id.tvIntoBestPrice)
    TextView tvIntoBestPrice;

    private int decimalCount = 2;//价格小数点

    private int amountDecimalCount = 4;//数量小数点

    private long adId;//>0就是编辑
    private OptionalOrder optionalOrder;

    private Call<ResponseBody> otcFindMyPaymentListCall, otcAddMyAdCall, otcUpdateMyAdCall, otcGetMyAdInfoCall, otcGetAdPriceCall;

    private MultiSelectQuickPayWayAdapter multiSelectQuickPayWayAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.add_ad;
    }

    @Override
    protected String getActivityTitle() {
        return "添加广告";
    }


    public static void pageJump(Context context, long adId) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.ADID, adId);
//        bundle.putString(CommonConst.ENTRUSTAMOUNT, entrustAmount);
        PageJumpUtil.pageJump(context, AddAdActivity.class, bundle);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        startOtcFindMyPaymentList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_trem);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.ADID)) {
                adId = bundle.getLong(CommonConst.ADID, 0);
            }
        }
        multiSelectQuickPayWayAdapter = new MultiSelectQuickPayWayAdapter(this);
        rvType.setLayoutManager(new GridLayoutManager(this, 3));
        rvType.addItemDecoration(new SimpleSpaceItemDecoration(this, 3, 3, 3, 3));
        rvType.setAdapter(multiSelectQuickPayWayAdapter);
//        flAddPayMent.setOnClickListener(this);

        tvBuy.setSelected(true);
        tvSell.setSelected(false);

        tvBuy.setOnClickListener(this);
        tvSell.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvIntoBestPrice.setOnClickListener(this);

        etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > decimalCount) {//最多2位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
            }
        });
        etAmount.addTextChangedListener(amountTextWatcher);
//        etMinCny.addTextChangedListener(amountTextWatcher);
//        etMaxCny.addTextChangedListener(amountTextWatcher);
        if (adId > 0) {
            mActionBar.setTitle("编辑广告");
            startOtcGetMyAdInfo(adId);
        } else {
            mActionBar.setTitle("添加广告");
            startOtcFindMyPaymentList();
            startOtcGetAdPrice();
        }
    }


    TextWatcher amountTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            int posDot = s.toString().indexOf(".");
            if (0 == posDot) {//去除首位的"."
                s.delete(0, 1);
            } else if (posDot > 0) {
                if (s.length() - 1 - posDot > amountDecimalCount) {//最多2位小数
                    s.delete(posDot + (amountDecimalCount + 1), posDot + (amountDecimalCount + 2));
                }
            }
        }
    };

    private void startOtcGetAdPrice() {
        CommonUtil.cancelCall(otcGetAdPriceCall);
        final String buySell = tvBuy.isSelected() ? "buy" : "sell";
        otcGetAdPriceCall = VHttpServiceManager.getInstance().getVService().otcGetAdPrice(buySell);
        otcGetAdPriceCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String bestPrice = resultData.getItem("price", String.class);
                    tvBestPrice.setText(bestPrice);
                    Log.d("otcGetAdPrice","buySell=="+buySell);
                    if ("buy".equals(buySell)) {
                        tvBestPriceHint.setText("当前购买最高价");
                    } else {
                        tvBestPriceHint.setText("当前出售最低价");
                    }

                }
            }
        });
    }

    private void startOtcFindMyPaymentList() {
        CommonUtil.cancelCall(otcFindMyPaymentListCall);
        otcFindMyPaymentListCall = VHttpServiceManager.getInstance().getVService().otcFindMyPaymentList();
        otcFindMyPaymentListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Receipt> groupMarket = resultData.getGroup("myPaymentList", new TypeToken<Group<Receipt>>() {
                    }.getType());
                    if (groupMarket != null && groupMarket.size() > 0) {
//                        rvType.setVisibility(View.VISIBLE);
//                        flAddPayMent.setVisibility(groupMarket.size() > 2 ? View.GONE : View.VISIBLE);
                        multiSelectQuickPayWayAdapter.setGroup(groupMarket);

                        if (adId > 0 && optionalOrder != null) {
                            Group<AddPaymentTern> payWay = new Gson().fromJson(optionalOrder.payWay, new TypeToken<Group<AddPaymentTern>>() {
                            }.getType());
                            for (AddPaymentTern addPaymentTern : payWay) {
                                multiSelectQuickPayWayAdapter.addSelectPayway(addPaymentTern.paymentId);
                            }
                            multiSelectQuickPayWayAdapter.notifyDataSetChanged();
                        }
                    } else {
//                        rvType.setVisibility(View.GONE);
//                        flAddPayMent.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    private void startOtcGetMyAdInfo(long adId) {
        CommonUtil.cancelCall(otcGetMyAdInfoCall);
        otcGetMyAdInfoCall = VHttpServiceManager.getInstance().getVService().otcGetMyAdInfo(adId);
        otcGetMyAdInfoCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    optionalOrder = resultData.getObject(OptionalOrder.class);
                    setData(optionalOrder);
                    startOtcFindMyPaymentList();
                    startOtcGetAdPrice();//如果是编辑， startOtcGetAdPrice必须调用在otcGetMyAdInfo接口之后
                }
            }
        });
    }

    private void setData(OptionalOrder optionalOrder) {
        if (optionalOrder != null) {
            if ("buy".equals(optionalOrder.buySell)) {
                tvBuy.setSelected(true);
                tvSell.setSelected(false);
            } else {
                tvBuy.setSelected(false);
                tvSell.setSelected(true);
            }
            tvBuy.setEnabled(false);
            tvSell.setEnabled(false);
            etPrice.setText(optionalOrder.price);
            etMinCny.setText(optionalOrder.minCny);
            etMaxCny.setText(optionalOrder.maxCny);
            etAmount.setText(optionalOrder.amount);
            etContent.setText(optionalOrder.content);


        }
    }

    private void startOtcUpdateMyAd(long adId, String buySell, String price, String minCny, String maxCny, String amount, String payWay, String content) {
        CommonUtil.cancelCall(otcUpdateMyAdCall);
        otcUpdateMyAdCall = VHttpServiceManager.getInstance().getVService().otcUpdateMyAd(adId, buySell, price, minCny, maxCny, amount, payWay, content);
        otcUpdateMyAdCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AddAdActivity.this);
                    PageJumpUtil.finishCurr(AddAdActivity.this);
                }
            }
        });
    }

    private void startOtcAddMyAd(String buySell, String price, String minCny, String maxCny, String amount, String payWay, String content) {
        CommonUtil.cancelCall(otcAddMyAdCall);
        otcAddMyAdCall = VHttpServiceManager.getInstance().getVService().otcAddMyAd(buySell, price, minCny, maxCny, amount, payWay, content);
        otcAddMyAdCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, AddAdActivity.this);
                    PageJumpUtil.finishCurr(AddAdActivity.this);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flAddPayMent:
                AddReceiptTermActivity.pageJump(AddAdActivity.this, 1);
                break;
            case R.id.tvBuy:
                tvBuy.setSelected(true);
                tvSell.setSelected(false);
                startOtcGetAdPrice();
                break;
            case R.id.tvSell:
                tvBuy.setSelected(false);
                tvSell.setSelected(true);
                startOtcGetAdPrice();
                break;
            case R.id.tvIntoBestPrice:
                etPrice.setText(tvBestPrice.getText().toString());
                etPrice.setSelection(etPrice.getText().length());
                break;
            case R.id.tvSubmit:
                String buySell = tvBuy.isSelected() ? "buy" : "sell";
                String price = etPrice.getText().toString().trim();
                if (TextUtils.isEmpty(price)) {
                    CommonUtil.showmessage("请输入价格", AddAdActivity.this);
                    return;
                }

                String amount = etAmount.getText().toString().trim();
                if (TextUtils.isEmpty(amount)) {
                    CommonUtil.showmessage("请输入数量", AddAdActivity.this);
                    return;
                }


                String minCny = etMinCny.getText().toString().trim();
                if (TextUtils.isEmpty(minCny)) {
                    CommonUtil.showmessage("请输入最小限额", AddAdActivity.this);
                    return;
                }
                String maxCny = etMaxCny.getText().toString().trim();
                if (TextUtils.isEmpty(maxCny)) {
                    CommonUtil.showmessage("请输入最大限额", AddAdActivity.this);
                    return;
                }


                String ids = multiSelectQuickPayWayAdapter.getSelectSet();
                if (TextUtils.isEmpty(ids)) {
                    CommonUtil.showmessage("请选择收付款方式", AddAdActivity.this);
                    return;
                }
                Log.d("startOtcAddMyAd", "ids==" + ids);

                String content = etContent.getText().toString().trim();
//                if (TextUtils.isEmpty(content)) {
//                    CommonUtil.showmessage("请输入留言", AddAdActivity.this);
//                    return;
//                }
                if (etContent.length() > 140) {
                    CommonUtil.showmessage("留言长度不能大于140个字符", AddAdActivity.this);
                    return;
                }
                if (adId > 0) {
                    startOtcUpdateMyAd(adId, buySell, price, minCny, maxCny, amount, ids, content);
                } else {
                    startOtcAddMyAd(buySell, price, minCny, maxCny, amount, ids, content);

                }
                break;
        }
    }
}
