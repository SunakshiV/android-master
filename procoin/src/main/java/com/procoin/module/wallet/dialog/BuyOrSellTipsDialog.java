package com.procoin.module.wallet.dialog;

import android.content.Context;
import android.graphics.Color;
import androidx.core.content.ContextCompat;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.widget.dialog.base.AbstractBaseDialog;

/**
 * Created by zhengmj on 19-7-26.
 */

public abstract class BuyOrSellTipsDialog extends AbstractBaseDialog implements View.OnClickListener {


    private TextView tvProtocol;
    private TextView tvTitle;
    private TextView tvMessage;
    private TextView btnClose;
    private TextView btnOk;
    private int buySell;
    private String bail="";//保证金
    private String hand = "";//手数
    private String leverMultiple = "";
    private Context context;

    public BuyOrSellTipsDialog(final Context context,String bail, final String hand,final String leverMultiple,final int buySell) {
        super(context);
        this.context = context;
        this.bail=bail;
        this.hand=hand;
        this.leverMultiple=leverMultiple;
        this.buySell = buySell;
        setContentView(R.layout.lever_buy_sell_tips_dialog);
        tvProtocol = findViewById(R.id.tvProtocol);
        tvTitle = findViewById(R.id.tvTitle);
        tvMessage= findViewById(R.id.tvMessage);
        btnClose = findViewById(R.id.btnClose);
        btnOk = findViewById(R.id.btnOk);

        btnOk.setOnClickListener(this);
        btnClose.setOnClickListener(this);

        String bs="";
        if (buySell == 1) {
            bs="看涨(做多)";
        } else {
            bs="看跌(做空)";
        }
        tvTitle.setText(bs+"概要");
//        tvMessage.setText("你将以"+bail+"保证金开"+leverMultiple+"倍杠杆开"+hand+"手"+bs);
        tvMessage.setText("是否确定下单?");

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();

        SpannableString normalText = new SpannableString("确定即代表你已同意并接受");
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//        SpannableString clickText = new SpannableString("《数字资产借贷服务合同》");
//
//        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        clickText.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//                CommonWebViewActivity.pageJumpCommonWebViewActivity(context, CommonConst.PASSGEDETAIL_25);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setColor(ContextCompat.getColor(context, R.color.beebarBlue));
//                ds.setUnderlineText(true);
//            }
//        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


        SpannableString clickText2 = new SpannableString("《交易服务协议》");
        clickText2.setSpan(new ForegroundColorSpan(Color.parseColor("#376eb8")), 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(context, CommonConst.PASSGEDETAIL_26);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(context, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(normalText);
//        spannableStringBuilder.append(clickText);
//        spannableStringBuilder.append("和");
        spannableStringBuilder.append(clickText2);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                onclickClose();
                break;
            case R.id.btnOk:
                dismiss();
                onclickOk();
                break;
        }
    }

    @Override
    public void onclickClose() {
        dismiss();
    }

    @Override
    public void setDownProgress(int progress) {

    }
}
