package com.procoin.module.home.trade.fragment;

import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.fragment.UserBaseFragment;
import com.procoin.module.home.trade.TradeLeverActivity;
import com.procoin.module.home.trade.TransferCoinActivity;
import com.procoin.module.home.trade.adapter.LeverTypeAdapter;
import com.procoin.module.home.trade.adapter.MultiNumAdapter;
import com.procoin.module.wallet.dialog.BuyOrSellTipsDialog;
import com.procoin.util.CommonUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.SimpleSpaceItemDecoration;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 看涨-
 * Created by zhengmj on 19-3-8.
 */

public class TradeLeverFragment extends UserBaseFragment implements View.OnClickListener {

    @BindView(R.id.llMenu)
    FrameLayout llMenu;
    @BindView(R.id.rvHand)
    RecyclerView rvHand;
    @BindView(R.id.tvMultNum)
    TextView tvMultNum;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    @BindView(R.id.tvSell)
    TextView tvSell;
    @BindView(R.id.tvOrderType)
    TextView tvOrderType;
    @BindView(R.id.llOrderType)
    FrameLayout llOrderType;
    @BindView(R.id.tvMarketHint)
    TextView tvMarketHint;
    @BindView(R.id.etPrice)
    EditText etPrice;
    @BindView(R.id.tvMinus)
    TextView tvMinus;
    @BindView(R.id.tvPlus)
    TextView tvPlus;
    @BindView(R.id.llLimitHint)
    FrameLayout llLimitHint;
    @BindView(R.id.etHand)
    EditText etHand;
    @BindView(R.id.tvMaxHand)
    TextView tvMaxHand;
    @BindView(R.id.tvBail)
    TextView tvBail;
    @BindView(R.id.tvTransfer)
    TextView tvTransfer;
    private Call<ResponseBody> getPrybarConfigCall;
    private Call<ResponseBody> getPrybarCheckOutCall;
    private Call<ResponseBody> getPrybarCreateOrderCall;

    private String symbol;
    private boolean setDefaultFlag = false;


    private BuyOrSellTipsDialog buyOrSellTipsDialog;
    private Call<ResponseBody> getTradeOrderCall;

    private LeverTypeAdapter leverTypeAdapter;
    private MultiNumAdapter multiNumAdapter;
    private String hand = "0";//手数
    private int buySell = 1;
    private String holdUsdt = "0.0";
    private String[] multiNumList;
    private String[] initHandList;
    private String leverMultiple = "";

    private int priceDecimals = 2;//价格的小数点数量
    private int amountDecimals = 4;//数量的小数点数量
    private String openFeeScale = "";
    private String openBail = "";//开仓保证金

    private Handler handler = new Handler();


    public static TradeLeverFragment newInstance(String symbol, int buySell) {
        TradeLeverFragment fragment = new TradeLeverFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.SYMBOL, symbol);
        bundle.putInt(CommonConst.BUYSELL, buySell);
        fragment.setArguments(bundle);
        return fragment;
    }

    public void setDecimals(int priceDecimals, int amountDecimals) {
        this.priceDecimals = priceDecimals;
        this.amountDecimals = amountDecimals;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle b = getArguments();
        if (null == b) return;
        symbol = b.getString(CommonConst.SYMBOL, "");
        buySell = b.getInt(CommonConst.BUYSELL, 1);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        Log.d("TradeLeverActivity", "TradeBuyFragment setUserVisibleHint==" + isVisibleToUser + " getActivity()==" + getActivity());
        if (getActivity() == null) return;
        if (isVisibleToUser) {
            startPrybarConfig();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.trade_buy_lever, container, false);
        ButterKnife.bind(this, view);

        leverTypeAdapter = new LeverTypeAdapter(getActivity());
        rvHand.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rvHand.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 10, 0, 5, 5));
        rvHand.setAdapter(leverTypeAdapter);
//        leverTypeAdapter.setData(new String[]{"18", "20", "30", "50", "80", "100",});
        leverTypeAdapter.setOnItemclickListen(new LeverTypeAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String leverType) {
                hand = leverType;
                etHand.setText(hand);
//                startCheckOut();
//                startGetCoinInfo();//改变键类型要刷新
            }
        });
        llMenu.setOnClickListener(this);
        llOrderType.setOnClickListener(this);

        tvPlus.setOnClickListener(this);
        tvMinus.setOnClickListener(this);

        tvTransfer.setOnClickListener(this);

        if (buySell == 1) {
            tvBuy.setVisibility(View.VISIBLE);
            tvSell.setVisibility(View.GONE);
            tvBuy.setOnClickListener(this);
            etPrice.setBackgroundResource(R.drawable.xml_et_bg_2);
            etHand.setBackgroundResource(R.drawable.xml_et_bg_2);
        } else {
            tvBuy.setVisibility(View.GONE);
            tvSell.setVisibility(View.VISIBLE);
            tvSell.setOnClickListener(this);
            etPrice.setBackgroundResource(R.drawable.xml_et_bg_3);
            etHand.setBackgroundResource(R.drawable.xml_et_bg_3);
        }

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
                    if (s.length() - 1 - posDot > priceDecimals) {//最多4位小数
                        s.delete(posDot + (priceDecimals + 1), posDot + (priceDecimals + 2));
                    }
                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }
        });

        etHand.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    leverTypeAdapter.setSelected(s.toString());
                    hand = s.toString().trim();
                } else {
                    hand = "0";
                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 500);
            }
        });
        switchMarketPrice();
        return view;
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            startCheckOut();
        }
    };

    private void startPrybarConfig() {
        CommonUtil.cancelCall(getPrybarConfigCall);
        getPrybarConfigCall = VHttpServiceManager.getInstance().getVService().prybarConfig(symbol);
        getPrybarConfigCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    multiNumList = resultData.getStringArray("multiNumList");
                    initHandList = resultData.getStringArray("initHandList");

                    priceDecimals = resultData.getItem("priceDecimals", Integer.class);
                    openFeeScale = resultData.getItem("openFeeScale", String.class);

                    String accountType = resultData.getItem("accountType", String.class);

                    if (getActivity() != null && getActivity() instanceof TradeLeverActivity) {
                        TradeLeverActivity tradeLeverActivity = (TradeLeverActivity) getActivity();
                        tradeLeverActivity.setAccountType(accountType);
                    }

//                    tvEnableBalance.setText("可用" + holdUsdt + "USDT");
                    leverTypeAdapter.setData(initHandList);

                    if (TextUtils.isEmpty(leverMultiple)) {//第一次为null
                        if (multiNumList != null && multiNumList.length > 0) {
                            leverMultiple = multiNumList[0];
                            tvMultNum.setText(leverMultiple + "X");
                        }
                        if (initHandList != null && initHandList.length > 0) {
                            hand = initHandList[0];
                            leverTypeAdapter.setSelected(hand);
                            etHand.setText(hand);
                        }

                    }
                    startCheckOut();
                }
            }
        });
    }

    /**
     * 设置默认价格，只设置一次就好
     */
    public void setDefaultLast(double last) {
        if (getActivity() == null) return;
        if (setDefaultFlag) return;
        if (etPrice != null && TextUtils.isEmpty(etPrice.getText().toString().trim())) {
            etPrice.setText(String.valueOf(last));
            setDefaultFlag = true;
        }
    }

    private String getPrice() {
        if (orderType.equals("limit")) {
            String p = etPrice.getText().toString().trim();
            if (TextUtils.isEmpty(p)) {
                return "0";
            } else {
                return p;
            }

        } else if (orderType.equals("market")) {
            return "0";
        }
        return "0";
    }

    public void startCheckOut() {
        CommonUtil.cancelCall(getPrybarCheckOutCall);
        getPrybarCheckOutCall = VHttpServiceManager.getInstance().getVService().prybarCheckOut(symbol, getPrice(), buySell == 1 ? "buy" : "sell", hand, leverMultiple, orderType);
        getPrybarCheckOutCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String maxHand = resultData.getItem("maxHand", String.class);
                    openBail = resultData.getItem("openBail", String.class);
                    tvMaxHand.setText("可开 " + maxHand + " 手");
                    tvBail.setText("开仓保证金 " + openBail);

                }
            }
        });
    }


    private void startCreateOrder(String payPass) {
        CommonUtil.cancelCall(getPrybarCreateOrderCall);

        getPrybarCreateOrderCall = VHttpServiceManager.getInstance().getVService().prybarCreateOrder(symbol, getPrice(), buySell == 1 ? "buy" : "sell", hand, leverMultiple, orderType, payPass);
        getPrybarCreateOrderCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, getActivity());
                    startCheckOut();
                    if (getActivity() != null && getActivity() instanceof TradeLeverActivity) {
                        ((TradeLeverActivity) getActivity()).refreshOnCreateOrder();
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }

            @Override
            protected void onPassWordFinsh(String pwString) {
                super.onPassWordFinsh(pwString);
                startCreateOrder(pwString);
            }

        });
    }


    PopupWindow pop;
    RecyclerView rvMultiNumList;

    private void showPopupMenu(View parent) {
        if (pop == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.lever_menu);

            rvMultiNumList = view.findViewById(R.id.rvMultiNumList);

            multiNumAdapter = new MultiNumAdapter(getActivity());
            rvMultiNumList.setLayoutManager(new LinearLayoutManager(getActivity()));
            rvMultiNumList.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 0, 0, 0, 0));
            rvMultiNumList.setAdapter(multiNumAdapter);
            multiNumAdapter.setData(multiNumList);
            multiNumAdapter.setSelected(leverMultiple);
            multiNumAdapter.setOnItemclickListen(new MultiNumAdapter.onItemclickListen() {
                @Override
                public void onItemclick(String multnum) {
                    leverMultiple = multnum;
                    tvMultNum.setText(leverMultiple + "X");
                    startCheckOut();
                    dissPop();
                }
            });
            pop = new PopupWindow(view, parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);//
            pop.setOutsideTouchable(false);
            pop.setFocusable(false);//
            pop.setOutsideTouchable(true);
            pop.setFocusable(true);
            pop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (pop != null && !pop.isShowing()) {
            pop.showAsDropDown(parent, 0, 5);
        }
    }

    private void dissPop() {
        if (pop != null & pop.isShowing()) {
            pop.dismiss();
        }
    }


    PopupWindow orderTypePop;

    TextView tvMarketPrice, tvLimitPrice;

    private void showOrderTypePopupMenu(View parent) {
        if (orderTypePop == null) {
            View view = InflaterUtils.inflateView(getActivity(), R.layout.lever_order_type_menu);

            tvMarketPrice = view.findViewById(R.id.tvMarketPrice);
            tvLimitPrice = view.findViewById(R.id.tvLimitPrice);
            tvMarketPrice.setOnClickListener(this);
            tvLimitPrice.setOnClickListener(this);
            tvMarketPrice.setSelected(true);

            orderTypePop = new PopupWindow(view, parent.getWidth(), ViewGroup.LayoutParams.WRAP_CONTENT);//
            orderTypePop.setOutsideTouchable(false);
            orderTypePop.setFocusable(false);//
            orderTypePop.setOutsideTouchable(true);
            orderTypePop.setFocusable(true);
            orderTypePop.setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.transparent)));
            orderTypePop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                }
            });

        }
        if (orderTypePop != null && !orderTypePop.isShowing()) {
            orderTypePop.showAsDropDown(parent, 0, 5);
        }
    }

    private void dissOrderTypePop() {
        if (orderTypePop != null & orderTypePop.isShowing()) {
            orderTypePop.dismiss();
        }
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startPrybarConfig();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private String orderType = "market";//limit：限价，market：市价

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvBuy:
            case R.id.tvSell:
                if (orderType.equals("limit") && TextUtils.isEmpty(getPrice())) {//如果是限价，但是还没有输入价格就不刷新
                    CommonUtil.showmessage("请输入价格", getActivity());
                    return;
                }
                showSubmitTipsDialog();
                break;
            case R.id.llMenu:
                showPopupMenu(llMenu);
                break;
            case R.id.llOrderType:
                showOrderTypePopupMenu(llOrderType);
                break;
            case R.id.tvMarketPrice:
                tvMarketPrice.setSelected(true);
                tvLimitPrice.setSelected(false);
                switchMarketPrice();
                startCheckOut();
                dissOrderTypePop();
                break;
            case R.id.tvLimitPrice:
                tvMarketPrice.setSelected(false);
                tvLimitPrice.setSelected(true);
                switchLimitPrice();
                startCheckOut();
                dissOrderTypePop();
                break;
            case R.id.tvPlus:
                plusOrMinus(true);
                break;
            case R.id.tvMinus:
                plusOrMinus(false);
                break;
            case R.id.tvTransfer:
                PageJumpUtil.pageJump(getActivity(), TransferCoinActivity.class);
                break;

        }
    }

    private void plusOrMinus(boolean isPlus) {
        String price = etPrice.getText().toString().trim();
        if (TextUtils.isEmpty(price)) {
            return;
        }
        double p = Double.parseDouble(price);
        double unit = 1 / Math.pow(10, priceDecimals);
        if (isPlus) {
            p += unit;
        } else {
            p -= unit;
        }
        String ret = StockChartUtil.formatNumber(priceDecimals, p);
        etPrice.setText(ret);
        etPrice.setSelection(etPrice.getText().length());
    }


    private void switchMarketPrice() {
        tvMarketHint.setVisibility(View.VISIBLE);
        llLimitHint.setVisibility(View.GONE);
        orderType = "market";
        tvOrderType.setText("市价委托");
    }

    private void switchLimitPrice() {
        tvMarketHint.setVisibility(View.GONE);
        llLimitHint.setVisibility(View.VISIBLE);
        orderType = "limit";
        tvOrderType.setText("限价委托");
    }

    ValueAnimator textSizeAnimator;

    public void onClickPrice(String price) {
        if (etPrice != null) {
            etPrice.setText(price);
            etPrice.setSelection(etPrice.getText().length());
            if (textSizeAnimator == null) {
                textSizeAnimator = ValueAnimator.ofFloat(14f, 15f, 16f, 17f, 18f, 17f, 16f, 15f, 14f);
                textSizeAnimator.setDuration(400);
                textSizeAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
                textSizeAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                    public void onAnimationUpdate(ValueAnimator animation) {
                        etPrice.setTextSize(TypedValue.COMPLEX_UNIT_SP, (Float) animation.getAnimatedValue());
                    }
                });
            }
            textSizeAnimator.start();
        }
    }

    private void showSubmitTipsDialog() {
        buyOrSellTipsDialog = new BuyOrSellTipsDialog(getActivity(), openBail, hand, leverMultiple, buySell) {
            @Override
            public void onclickOk() {
                startCreateOrder("");
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        buyOrSellTipsDialog.show();
    }


//

}
