package com.procoin.module.copy;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 跟单 废弃
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class CropyOrderActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {


    @BindView(R.id.tvBalanceText)
    TextView tvBalanceText;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvExpends)
    TextView tvExpends;
    @BindView(R.id.etMaxMoney)
    EditText etMaxMoney;
    @BindView(R.id.tvStopWin)
    TextView tvStopWin;
    @BindView(R.id.sbStopWin)
    SeekBar sbStopWin;
    @BindView(R.id.tvStopLoss)
    TextView tvStopLoss;
    @BindView(R.id.sbStopLoss)
    SeekBar sbStopLoss;
    @BindView(R.id.llMore)
    LinearLayout llMore;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @BindView(R.id.tvTolCny)
    TextView tvTolCny;
    @BindView(R.id.tvMaxCny)
    TextView tvMaxCny;

    private long copyUid;
    private int decimalCount = 4;//小数点数量,金额小数点2位，数量小数点是4位.

    private double stopWin = 0.00;
    private double stopLoss = 0.00;

    private double cash;
    private double atMaxCash;

    private String holdUsdt = "0.00";//持有USDT
    private double usdtRate = 0.00;//usdt市价

    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> copySlaveOrderCall;


//    private CheckBox tv_sign;
    private TextView tvProtocol;


    @Override
    protected int setLayoutId() {
        return R.layout.copy_order;
    }


    public static void pageJump(Context context, long copyUid) {
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.TARGETUID, copyUid);
        PageJumpUtil.pageJump(context, CropyOrderActivity.class, bundle);
    }

    @Override
    protected String getActivityTitle() {
        return "跟单";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.TARGETUID)) {
                copyUid = bundle.getLong(CommonConst.TARGETUID, 0);
            }
        }
//        llMore.setVisibility(View.GONE);
        sbStopWin.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stopWin = (double) progress / 100;
                if (progress == 0) {
                    tvStopWin.setText("止盈: " + "无设置");
                } else {
                    tvStopWin.setText("止盈: " + progress + "%");
                }
                Log.d("CropyOrderActivity", "stopWin==" + stopWin);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        sbStopLoss.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                stopLoss = (double) progress / 100;
                if (progress == 0) {
                    tvStopLoss.setText("止损: " + "无设置");
                } else {
                    tvStopLoss.setText("止损: " + progress + "%");
                }

                Log.d("CropyOrderActivity", "stopLoss==" + stopLoss);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        etMoney.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > decimalCount) {//最多4位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                cash = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    cash = Double.parseDouble(s.toString());
                } else {
                    cash = 0;
                }
                BigDecimal tolCnyBd = BigDecimal.valueOf(usdtRate).multiply(BigDecimal.valueOf(cash)).setScale(2, BigDecimal.ROUND_FLOOR);
                tvTolCny.setText("≈¥ " + tolCnyBd.toPlainString());

            }
        });

        etMaxMoney.addTextChangedListener(new TextWatcher() {
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
                    if (s.length() - 1 - posDot > decimalCount) {//最多6位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                atMaxCash = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    atMaxCash = Double.parseDouble(s.toString());
                } else {
                    atMaxCash = 0;
                }
                BigDecimal tolMaxBd = BigDecimal.valueOf(usdtRate).multiply(BigDecimal.valueOf(atMaxCash)).setScale(2, BigDecimal.ROUND_FLOOR);
                tvMaxCny.setText("≈¥ " + tolMaxBd.toPlainString());

            }
        });

//        tv_sign = (CheckBox) findViewById(R.id.cbSign);
        tvProtocol = (TextView) findViewById(R.id.tvProtocol);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString normalText = new SpannableString("跟单即代表你同意并接受");

        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString clickText = new SpannableString("《风险提示》");

        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(CropyOrderActivity.this, CommonConst.LIABILITY_PROTOCOL);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(CropyOrderActivity.this, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(clickText);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);
//        tvExpends.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);

        startGetTradeConfig();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvExpends:
                if (llMore.getVisibility() == View.GONE) {
                    tvExpends.setText("跟单设置 ▲");
                    llMore.setVisibility(View.VISIBLE);
                } else {
                    tvExpends.setText("跟单设置 ▼");
                    llMore.setVisibility(View.GONE);
                }
                break;
            case R.id.tvSubmit:

                if (cash == 0) {
                    CommonUtil.showmessage("请输入跟单总金额", this);
                    return;
                }
                if (atMaxCash == 0) {
                    CommonUtil.showmessage("请输入最大金额", this);
                    return;
                }
                if (atMaxCash > cash) {
                    CommonUtil.showmessage("最大金额不能大于跟单总金额", this);
                    return;
                }
//                if (!tv_sign.isChecked()) {
//                    CommonUtil.showmessage("请先阅读《风险提示》", CropyOrderActivity.this);
//                    return;
//                }
                startCopySlaveOrder();
                break;
        }
    }


    private void startGetTradeConfig() {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("","");
        getTradeConfigCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    usdtRate = resultData.getItem("usdtRate", Double.class);
                    tvBalanceText.setText("可用USDT:" + holdUsdt);
                }
            }
        });
    }

    private void startCopySlaveOrder() {
        CommonUtil.cancelCall(copySlaveOrderCall);
        copySlaveOrderCall = VHttpServiceManager.getInstance().getVService().copySlaveOrder(copyUid, String.valueOf(cash), String.valueOf(atMaxCash), stopWin, stopLoss);
        copySlaveOrderCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    CommonUtil.showmessage(resultData.msg, CropyOrderActivity.this);
                    String orderId = resultData.getItem("orderId", String.class);
                    if (!TextUtils.isEmpty(orderId) && Long.parseLong(orderId) > 0) {
                        CropyOrderInfoActivity.pageJump(CropyOrderActivity.this, Long.parseLong(orderId));
                        setSwipeBackEnable(false);
                        setOverrideExitAniamtion(false);
                        PageJumpUtil.finishCurr(CropyOrderActivity.this);
                        overridePendingTransition(R.anim.login_enter, R.anim.login_out);
                    }
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
            }
        });
    }
}
