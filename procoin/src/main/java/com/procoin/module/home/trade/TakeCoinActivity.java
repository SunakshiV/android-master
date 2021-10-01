package com.procoin.module.home.trade;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.trade.adapter.KeyTypeAdapter;
import com.procoin.module.home.trade.dialog.TakeCoinSelectFragment;
import com.procoin.module.home.trade.entity.CoinResult;
import com.procoin.module.home.trade.entity.TakeCoin;
import com.procoin.module.home.trade.history.TakeCoinHistoryActivity;
import com.procoin.util.DensityUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.widgets.SimpleSpaceItemDecoration;
import com.google.gson.reflect.TypeToken;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 提币
 */
public class TakeCoinActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvSymbol)
    TextView tvSymbol;
    @BindView(R.id.llSelectCoin)
    LinearLayout llSelectCoin;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etAmount)
    EditText etAmount;
    @BindView(R.id.tvSymbol2)
    TextView tvSymbol2;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.etFee)
    TextView etFee;
    @BindView(R.id.tvSymbol3)
    TextView tvSymbol3;
    @BindView(R.id.tvScope)
    TextView tvScope;
    @BindView(R.id.tvLastAmount)
    TextView tvLastAmount;
    @BindView(R.id.tvTakeCoin)
    TextView tvTakeCoin;
    @BindView(R.id.tvMinWithdrawAmt)
    TextView tvMinWithdrawAmt;
    @BindView(R.id.tvEnableAmount)
    TextView tvEnableAmount;

    @BindView(R.id.tvMenu)
    TextView tvMenu;
    @BindView(R.id.llKeyType)
    LinearLayout llKeyType;
    @BindView(R.id.rvKeyType)
    RecyclerView rvKeyType;

    private KeyTypeAdapter keyTypeAdapter;

    private Group<TakeCoin> coinGroup;

    private Call<ResponseBody> getCoinListCall;
    private Call<ResponseBody> getCoinInfo;
    private Call<ResponseBody> withdrawCoinCall;


    private String currSymbol = "";
    private CoinResult currCoin;
    private String keyType = "";


    private int amount_decimalcount = 8;//数量的小数点数量

    @Override
    protected int setLayoutId() {
        return R.layout.take_coin;
    }

    public static void pageJump(Context context, String defaultSymbol) {
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, defaultSymbol);
        PageJumpUtil.pageJump(context, TakeCoinActivity.class, bundle);
    }

    @Override
    protected String getActivityTitle() {
        return "提币";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        String defaultSymbol = "";
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.SYMBOL)) {
                defaultSymbol = bundle.getString(CommonConst.SYMBOL, "");
            }
        }

        etAmount.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > amount_decimalcount) {
                        s.delete(posDot + (amount_decimalcount + 1), posDot + (amount_decimalcount + 2));
                    }
                }
                if (currCoin == null) return;
                setLastAmountText(s.toString());
            }
        });
        llSelectCoin.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        tvTakeCoin.setOnClickListener(this);
        tvMenu.setText("记录");
        tvMenu.setOnClickListener(this);
        keyTypeAdapter = new KeyTypeAdapter(this);
        rvKeyType.setLayoutManager(new GridLayoutManager(this, 4));
        rvKeyType.addItemDecoration(new SimpleSpaceItemDecoration(this, 0, 10, 5, 5));
        rvKeyType.setAdapter(keyTypeAdapter);
        keyTypeAdapter.setOnItemclickListen(new KeyTypeAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String keyType) {
                TakeCoinActivity.this.keyType = keyType;
                startGetCoinInfo();//改变键类型要刷新
            }
        });


        setSymbol(defaultSymbol);
        startGetCoinList();
    }


    private void setSymbol(String symbol) {
        if (TextUtils.isEmpty(symbol)) return;
        currSymbol = symbol;
        currCoin = null;
        keyType = "";
        etAmount.setText("");
        keyTypeAdapter.clearAllItem();
        tvSymbol.setText(symbol);
        tvSymbol2.setText(symbol);
        tvSymbol3.setText(symbol);
        startGetCoinInfo();
    }

    private void startGetCoinList() {
        CommonUtil.cancelCall(getCoinListCall);
        getCoinListCall = VHttpServiceManager.getInstance().getVService().coinList(-1);
        getCoinListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    coinGroup = resultData.getGroup("coinList", new TypeToken<Group<TakeCoin>>() {
                    }.getType());
                }
            }
        });
    }

    private void startGetCoinInfo() {
        CommonUtil.cancelCall(getCoinInfo);
        getCoinInfo = VHttpServiceManager.getInstance().getVService().getWithdrawCoinInfo(currSymbol, keyType, -1);
        getCoinInfo.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    currCoin = resultData.getObject("coinResult", CoinResult.class);

                    setData();
                }
            }
        });
    }

    private void setData() {
        if (currCoin == null) return;
        Log.d("startGetCoinInfo", "currCoin.chainTypes==" + currCoin.chainTypes);
        if (currCoin.chainTypes != null && currCoin.chainTypes.length > 0) {
            llKeyType.setVisibility(View.VISIBLE);
//            currCoin.chainTypes=new String[]{"aaa","bbb","ccc","ddd","eee","fff"};
            keyTypeAdapter.setData(currCoin.chainTypes);
            if (TextUtils.isEmpty(keyType)) {
                keyType = currCoin.chainTypes[0];
                keyTypeAdapter.setSelected(keyType);
            }
        } else {
            llKeyType.setVisibility(View.GONE);
        }
        amount_decimalcount = currCoin.maxWithdrawDecimals;
        tvEnableAmount.setText("可提币数量：" + currCoin.amount + currCoin.symbol);
        etFee.setText(currCoin.withdrawFee);
//        tvMinWithdrawAmt.setText("最小提币数量为：" + currCoin.minWithdrawAmt + currCoin.symbol);
//        tvMinWithdrawAmt.setText(CommonUtil.fromHtml(currCoin.inOutTip));
        tvMinWithdrawAmt.setText(currCoin.inOutTip);
        setLastAmountText(etAmount.getText().toString());

    }

    private void startWithdrawCoinSubmit(final String symbol, final String amount, final String address, final String chainType, String payPass) {
        CommonUtil.cancelCall(withdrawCoinCall);
        Log.d("CoinSubmit", "222address==" + address);
        withdrawCoinCall = VHttpServiceManager.getInstance().getVService().withdrawCoinSubmit(symbol, amount, address, chainType, payPass);
        withdrawCoinCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, TakeCoinActivity.this);
                    BigDecimal bigDecimal = new BigDecimal(currCoin.amount).subtract(new BigDecimal(amount)).setScale(amount_decimalcount, BigDecimal.ROUND_FLOOR);
                    currCoin.amount = bigDecimal.toPlainString();
                    tvEnableAmount.setText("可提币数量：" + bigDecimal.toPlainString() + currCoin.symbol);
                    startGetCoinList();
                }
            }


            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startWithdrawCoinSubmit(symbol, amount, address, chainType, pwString);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llSelectCoin:
                if (coinGroup == null) {
                    startGetCoinList();
                } else {
                    showTakeCoinSelectFragment();
                }
                break;
            case R.id.tvAll:
                if (currCoin != null) {
                    etAmount.setText(currCoin.amount);
                    etAmount.setSelection(etAmount.getText().length());
                }
                break;
            case R.id.tvMenu:
                PageJumpUtil.pageJump(TakeCoinActivity.this, TakeCoinHistoryActivity.class);
                break;
            case R.id.tvTakeCoin:
                if (TextUtils.isEmpty(currSymbol)) {
                    CommonUtil.showmessage("请选择提现币种", this);
                    return;
                }
                if (currCoin == null) {
                    CommonUtil.showmessage("获取信息失败", this);
                    return;
                }
                if (currCoin.chainTypes != null && currCoin.chainTypes.length > 0) {
                    if (TextUtils.isEmpty(keyType)) {
                        CommonUtil.showmessage("请选择键类型", this);
                        return;
                    }
                }
                String address = etAddress.getText().toString();
                if (TextUtils.isEmpty(address)) {
                    CommonUtil.showmessage("请填写提币地址", this);
                    return;
                }

                String amount = etAmount.getText().toString();
                if (TextUtils.isEmpty(amount)) {
                    CommonUtil.showmessage("请输入提币数量", this);
                    return;
                }

                if (Double.parseDouble(amount) > Double.parseDouble(currCoin.amount)) {
                    CommonUtil.showmessage("提币数量不足", this);
                    return;
                }
                showTipsDialog(currCoin.symbol, amount,address, keyType);
                break;
        }
    }

    private void setLastAmountText(String amountText) {
        if (!TextUtils.isEmpty(amountText)) {
            double amount = Double.parseDouble(amountText);
            if (amount > 0) {
                BigDecimal bigDecimal = BigDecimal.valueOf(amount).subtract(new BigDecimal(currCoin.withdrawFee)).setScale(amount_decimalcount, BigDecimal.ROUND_FLOOR);
                if (bigDecimal.doubleValue() >= 0) {
                    tvLastAmount.setText(bigDecimal.toPlainString() + currCoin.symbol);
                }
            }
        } else {
            tvLastAmount.setText("0.00000000" + currCoin.symbol);
        }
    }

    private void showTakeCoinSelectFragment() {
        TakeCoinSelectFragment manageCircleFragment = TakeCoinSelectFragment.newInstance(coinGroup);
        manageCircleFragment.setOnSelectCoinListen(new TakeCoinSelectFragment.OnSelectCoinListen() {
            @Override
            public void onclick(TakeCoin takeCoin) {
//                amount_decimalcount = takeCoin.maxWithdrawDecimals;
                setSymbol(takeCoin.symbol);
//                currCoin = takeCoin;
//                if (takeCoin.symbol.equals("USDT")) {
//                    llKeyType.setVisibility(View.VISIBLE);
//                } else {
//                    llKeyType.setVisibility(View.GONE);
//                }
//                tvSymbol.setText(takeCoin.symbol);
//                tvSymbol2.setText(takeCoin.symbol);
//                tvSymbol3.setText(takeCoin.symbol);
//                tvEnableAmount.setText("可提币数量：" + takeCoin.amount + takeCoin.symbol);
//                etFee.setText(takeCoin.withdrawFee);
//                tvMinWithdrawAmt.setText("最小提币数量为：" + takeCoin.minWithdrawAmt + takeCoin.symbol);
//                setLastAmountText(etAmount.getText().toString());
            }
        });
        manageCircleFragment.show(getSupportFragmentManager(), "");
    }


    TjrBaseDialog TipsDialog;

    private void showTipsDialog(final String symbol, final String amount, final String address, final String chainType) {
        TipsDialog = new TjrBaseDialog(this) {
            @Override
            public void onclickOk() {
                dismiss();
                Log.d("CoinSubmit", "address==" + address);
                startWithdrawCoinSubmit(symbol, amount, address, chainType, "");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        TipsDialog.setTitleVisibility(View.GONE);
        TipsDialog.setMessage("提币币种:" + symbol + "\n提币数量:" + amount + "\n\n" + "确认前请仔细核对提币地址信息，以避免造成不必要的财产损失。");
        TipsDialog.setBtnOkText("确定");
        TipsDialog.show();
    }

    private class SpaceItemDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = DensityUtil.dip2px(TakeCoinActivity.this, 5);
            outRect.right = DensityUtil.dip2px(TakeCoinActivity.this, 5);
            outRect.top = DensityUtil.dip2px(TakeCoinActivity.this, 5);
            outRect.bottom = DensityUtil.dip2px(TakeCoinActivity.this, 5);

        }
    }
}
