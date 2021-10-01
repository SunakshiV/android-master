package com.procoin.widgets.quotitian;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.util.CommonUtil;
import com.procoin.util.InflaterUtils;
import com.procoin.widgets.quotitian.entity.StarArkBidBean;
import com.procoin.widgets.quotitian.entity.jsonparser.StarArkBidBeanParser;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.social.util.AvoidMultiClick;
import com.procoin.util.MatchesUtil;

import org.json.JSONArray;


/**
 * 这个类的作用是 买卖那边出现的,这个是单独使用5挡
 * Created by zhengmj on 17-2-22.
 */

public class StarDetailPriceView extends LinearLayout {
    public View view;

    private TextView tvSellPrice3;
    private TextView tvSellPrice2;
    private TextView tvSellPrice1;
    private TextView tvBuyPrice1;
    private TextView tvBuyPrice2;
    private TextView tvBuyPrice3;


    private TextView tvSellVolume3;
    private TextView tvSellVolume2;
    private TextView tvSellVolume1;
    private TextView tvBuyVolume1;
    private TextView tvBuyVolume2;
    private TextView tvBuyVolume3;

    public StarDetailPriceVIewCallback lister;
//    private short entrust_bs;//买的时候显示

    public StarDetailPriceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public StarDetailPriceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StarDetailPriceView(Context context) {
        super(context);
        initView();
    }

    public void setPriceTextColor(int color){
        tvSellPrice3.setTextColor(color);
        tvSellPrice2.setTextColor(color);
        tvSellPrice1.setTextColor(color);
        tvBuyPrice1.setTextColor(color);
        tvBuyPrice2.setTextColor(color);
        tvBuyPrice3.setTextColor(color);
    }

    private void initView() {
        Onclick onclick = new Onclick();
        View view = InflaterUtils.inflateView(getContext(), R.layout.minute_detail);
//        llSjw3 = (LinearLayout) view.findViewById(R.id.llSjw3);
//        llSjw2 = (LinearLayout) view.findViewById(R.id.llSjw2);
//        llSjw1 = (LinearLayout) view.findViewById(R.id.llSjw1);
//
//        llBjw1 = (LinearLayout) view.findViewById(R.id.llBjw1);
//        llBjw2 = (LinearLayout) view.findViewById(R.id.llBjw2);
//        llBjw3 = (LinearLayout) view.findViewById(R.id.llBjw3);
//
//        llSjw3.setOnClickListener(onclick);
//        llSjw2.setOnClickListener(onclick);
//        llSjw1.setOnClickListener(onclick);
//
//        llBjw1.setOnClickListener(onclick);
//        llBjw2.setOnClickListener(onclick);
//        llBjw3.setOnClickListener(onclick);


        tvSellPrice3 = (TextView) view.findViewById(R.id.tvSellPrice3);
        tvSellPrice2 = (TextView) view.findViewById(R.id.tvSellPrice2);
        tvSellPrice1 = (TextView) view.findViewById(R.id.tvSellPrice1);
        tvBuyPrice1 = (TextView) view.findViewById(R.id.tvBuyPrice1);
        tvBuyPrice2 = (TextView) view.findViewById(R.id.tvBuyPrice2);
        tvBuyPrice3 = (TextView) view.findViewById(R.id.tvBuyPrice3);

        tvSellVolume3 = (TextView) view.findViewById(R.id.tvSellVolume3);
        tvSellVolume2 = (TextView) view.findViewById(R.id.tvSellVolume2);
        tvSellVolume1 = (TextView) view.findViewById(R.id.tvSellVolume1);
        tvBuyVolume1 = (TextView) view.findViewById(R.id.tvBuyVolume1);
        tvBuyVolume2 = (TextView) view.findViewById(R.id.tvBuyVolume2);
        tvBuyVolume3 = (TextView) view.findViewById(R.id.tvBuyVolume3);



        tvSellPrice3.setOnClickListener(onclick);
        tvSellPrice2.setOnClickListener(onclick);
        tvSellPrice1.setOnClickListener(onclick);
        tvBuyPrice1.setOnClickListener(onclick);
        tvBuyPrice2.setOnClickListener(onclick);
        tvBuyPrice3.setOnClickListener(onclick);

        tvSellVolume3.setOnClickListener(onclick);
        tvSellVolume2.setOnClickListener(onclick);
        tvSellVolume1.setOnClickListener(onclick);
        tvBuyVolume1.setOnClickListener(onclick);
        tvBuyVolume2.setOnClickListener(onclick);
        tvBuyVolume3.setOnClickListener(onclick);








//        flBuyZD = (FrameLayout) view.findViewById(R.id.flBuyZD);
//        flSellZD = (FrameLayout) view.findViewById(R.id.flSellZD);


        this.addView(view, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private class Onclick extends AvoidMultiClick {

        @Override
        public void click(View v) {
            switch (v.getId()) {

                case R.id.tvSellPrice3://这个是卖 , 买是1
                case R.id.tvSellVolume3://这个是卖 , 买是1
                    String str3 = tvSellPrice3.getText().toString();
                    if (MatchesUtil.isMatchesDoubleOrFloat(str3) || MatchesUtil.isMatchesIntOrLong(str3)) {
                        getPriceOnclick(str3, -1);
                    } else {
                        CommonUtil.showmessage("当前没有最优报价", getContext());
                    }
                    break;
                case R.id.tvSellPrice2://这个是卖 , 买是1
                case R.id.tvSellVolume2://这个是卖 , 买是1
                    String str = tvSellPrice2.getText().toString();
                    if (MatchesUtil.isMatchesDoubleOrFloat(str) || MatchesUtil.isMatchesIntOrLong(str)) {
                        getPriceOnclick(str, -1);
                    } else {
                        CommonUtil.showmessage("当前没有最优报价", getContext());
                    }
                    break;
                case R.id.tvSellPrice1://这个是卖 , 买是1
                case R.id.tvSellVolume1://这个是卖 , 买是1
                    String str1 = tvSellPrice1.getText().toString();
                    if (MatchesUtil.isMatchesDoubleOrFloat(str1) || MatchesUtil.isMatchesIntOrLong(str1)) {
                        getPriceOnclick(str1, -1);
                    } else {
                        CommonUtil.showmessage("当前没有最优报价", getContext());
                    }
                    break;
                case R.id.tvBuyPrice1://
                case R.id.tvBuyVolume1://
                    String buyPrice1 = tvBuyPrice1.getText().toString();
                    if (MatchesUtil.isMatchesDoubleOrFloat(buyPrice1) || MatchesUtil.isMatchesIntOrLong(buyPrice1)) {
                        getPriceOnclick(buyPrice1, 1);
                    } else {
                        CommonUtil.showmessage("当前没有最优报价", getContext());
                    }
                    break;
                case R.id.tvBuyPrice2://
                case R.id.tvBuyVolume2://
                    String buyPrice2 = tvBuyPrice2.getText().toString();
                    if (MatchesUtil.isMatchesDoubleOrFloat(buyPrice2) || MatchesUtil.isMatchesIntOrLong(buyPrice2)) {
                        getPriceOnclick(buyPrice2, 1);
                    } else {
                        CommonUtil.showmessage("当前没有最优报价", getContext());
                    }
                    break;
                case R.id.tvBuyPrice3://
                case R.id.tvBuyVolume3://
                    String buyPrice3 = tvBuyPrice3.getText().toString();
                    if (MatchesUtil.isMatchesDoubleOrFloat(buyPrice3) || MatchesUtil.isMatchesIntOrLong(buyPrice3)) {
                        getPriceOnclick(buyPrice3, 1);
                    } else {
                        CommonUtil.showmessage("当前没有最优报价", getContext());
                    }
                    break;
            }
        }
    }

    private void getPriceOnclick(String text, int bsType) {
        double p = 0;
        try {
            p = Double.parseDouble(text);
        } catch (Exception e) {

        }
        if (p > 0)
            if (lister != null) lister.getMinute(p, bsType);
    }


//    /**
//     * 获取第一优先的价格
//     *
//     * @param itemEntrust_bs
//     * @return
//     */
//    public double getFirstPrice(short itemEntrust_bs) {
//        double p = 0;
//        try {
//            String text = "0";
//            if (itemEntrust_bs == OLStarHomeBuyFragment.SELL) {
//                text = tvDetailPrice3.getText().toString();
//            } else if (itemEntrust_bs == OLStarHomeBuyFragment.BUY) {
//                text = tvDetailPrice4.getText().toString();
//            }
//            p = Double.parseDouble(text);
//        } catch (Exception e) {
//
//        }
//        return p;
//    }


    /**
     * @param decimal 小数点
     *                更新明细
     * @昨结
     */
    public void updateDateDetail(String bid_str, String ask_str, int decimal) {
//        this.entrust_bs = entrust_bs;
        // 1- 3 是卖的
        tvSellPrice3.setText(getResources().getString(R.string.novalue));
        tvSellPrice2.setText(getResources().getString(R.string.novalue));
        tvSellPrice1.setText(getResources().getString(R.string.novalue));
        //4 - 6 是买的
        tvBuyPrice1.setText(getResources().getString(R.string.novalue));
        tvBuyPrice2.setText(getResources().getString(R.string.novalue));
        tvBuyPrice3.setText(getResources().getString(R.string.novalue));

        tvSellVolume3.setText(getResources().getString(R.string.novalue));
        tvSellVolume2.setText(getResources().getString(R.string.novalue));
        tvSellVolume1.setText(getResources().getString(R.string.novalue));

        tvBuyVolume1.setText(getResources().getString(R.string.novalue));
        tvBuyVolume2.setText(getResources().getString(R.string.novalue));
        tvBuyVolume3.setText(getResources().getString(R.string.novalue));
//        flSellZD.setVisibility(GONE);
//        flBuyZD.setVisibility(GONE);

        Group<StarArkBidBean> arkList = new Group<>();//卖
        Group<StarArkBidBean> bidList = new Group<>();//买
        StarArkBidBeanParser parser = new StarArkBidBeanParser();
        try {
            if (!TextUtils.isEmpty(bid_str)) {
                JSONArray jsbid = new JSONArray(bid_str);
                bidList = parser.parse(jsbid,-1);
            }
            if (!TextUtils.isEmpty(ask_str)) {
                JSONArray jsark = new JSONArray(ask_str);
                arkList = parser.parse(jsark,1);
            }
        } catch (Exception e) {

        }

        for (int i = 0; i < bidList.size(); i++) {
            StarArkBidBean entity = bidList.get(i);
            switch (i) {
//                case 0:
//                    tvBuyPrice1.setText(StockChartUtil.formatNumber(decimal, entity.price));
//                    tvBuyVolume1.setText(StockChartUtil.formatMoney(entity.amount, 0, 2));
//                    break;
//                case 1:
//                    tvBuyPrice2.setText(StockChartUtil.formatNumber(decimal, entity.price));
//                    tvBuyVolume2.setText(StockChartUtil.formatMoney(entity.amount, 0, 2));
//                    break;
//                case 2:
//                    tvBuyPrice3.setText(StockChartUtil.formatNumber(decimal, entity.price));
//                    tvBuyVolume3.setText(StockChartUtil.formatMoney(entity.amount, 0, 2));
//
//                    break;
            }
        }
        for (int i = 0; i < arkList.size(); i++) {
            StarArkBidBean entity = arkList.get(i);
            switch (i) {
//                case 2:
//                    tvSellPrice3.setText(StockChartUtil.formatNumber(decimal, entity.price));
//                    tvSellVolume3.setText(StockChartUtil.formatMoney(entity.amount, 0, 2));
//                    break;
//                case 1:
//                    tvSellPrice2.setText(StockChartUtil.formatNumber(decimal, entity.price));
//                    tvSellVolume2.setText(StockChartUtil.formatMoney(entity.amount, 0, 2));
//                    break;
//                case 0:
//                    tvSellPrice1.setText(StockChartUtil.formatNumber(decimal, entity.price));
//                    tvSellVolume1.setText(StockChartUtil.formatMoney(entity.amount, 0, 2));
//                    break;
            }
        }
    }

    public interface StarDetailPriceVIewCallback {
        public void getMinute(double price, int bsType);
    }

}
