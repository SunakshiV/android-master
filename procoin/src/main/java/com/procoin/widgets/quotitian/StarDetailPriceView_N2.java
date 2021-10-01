package com.procoin.widgets.quotitian;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.module.home.OnItemClick;
import com.procoin.util.InflaterUtils;
import com.procoin.util.StockChartUtil;
import com.procoin.widgets.quotitian.adapter.StarDetailBuySellAdapter;
import com.procoin.widgets.quotitian.entity.StarArkBidBean;
import com.procoin.widgets.quotitian.entity.jsonparser.StarArkBidBeanParser;

import org.json.JSONArray;


/**
 * 这个类的作用是 买卖那边出现的,这个是单独使用N挡
 * Created by zhengmj on 17-2-22.
 */

public class StarDetailPriceView_N2 extends LinearLayout {
    public View view;

    private RecyclerView rvSellList, rvBuyList;

    private TextView tvPrice;
    private TextView tvPriceCash;

    private TextView tvUnitSymbol;

    private StarDetailBuySellAdapter starDetailBuyAdapter;
    private StarDetailBuySellAdapter starDetailSellAdapter;


    private StarArkBidBeanParser parser;

    public StarDetailPriceVIewCallback lister;
//    private short entrust_bs;//买的时候显示

    public StarDetailPriceView_N2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public StarDetailPriceView_N2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StarDetailPriceView_N2(Context context) {
        super(context);
        initView();
    }


    private void initView() {
        View view = InflaterUtils.inflateView(getContext(), R.layout.minute_detail_n_2);
        rvBuyList = view.findViewById(R.id.rvBuyList);
        rvSellList = view.findViewById(R.id.rvSellList);

        tvPrice = view.findViewById(R.id.tvPrice);
        tvPriceCash = view.findViewById(R.id.tvPriceCash);

        tvUnitSymbol = view.findViewById(R.id.tvUnitSymbol);

        starDetailBuyAdapter = new StarDetailBuySellAdapter(getContext(), 1);
        rvBuyList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBuyList.setAdapter(starDetailBuyAdapter);

        starDetailSellAdapter = new StarDetailBuySellAdapter(getContext(), -1);
        rvSellList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvSellList.setAdapter(starDetailSellAdapter);
        parser = new StarArkBidBeanParser();

        this.addView(view, new LinearLayoutCompat.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        starDetailBuyAdapter.setOnItemClick(onItemClick);
        starDetailSellAdapter.setOnItemClick(onItemClick);
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


    /**
     * 更新明细
     *
     * @昨结
     */
    public void updateDateDetail(String currency, String buys, String sells, double last, String lastCny, double rate) {


        tvPrice.setText(StockChartUtil.formatNumber(last));
        tvPrice.setTextColor(StockChartUtil.getRateTextColor(getContext(), rate));

        tvUnitSymbol.setText("价格(" + currency+ ")");

        tvPriceCash.setText(lastCny);

        Log.d("updateDateDetail", "buys==" + buys);

        Group<StarArkBidBean> buysList = new Group<>();//买
        Group<StarArkBidBean> sellsList = new Group<>();//卖
        try {
            if (!TextUtils.isEmpty(buys)) {
                JSONArray jsbid = new JSONArray(buys);
                buysList = parser.parse(jsbid, 1);
            } else {
                buysList = parser.getEmptyList();
            }
            if (!TextUtils.isEmpty(sells)) {
                JSONArray jsark = new JSONArray(sells);
                sellsList = parser.parse(jsark, -1);
            } else {
                sellsList = parser.getEmptyList();
            }

            Log.d("updateDateDetail", "buysList.size==" + buysList.size() + "  sellsList.size==" + buysList.size());

            starDetailBuyAdapter.clearAllItem();
            starDetailSellAdapter.clearAllItem();

            starDetailBuyAdapter.setGroup(buysList);
            starDetailSellAdapter.setGroup(sellsList);

        } catch (Exception e) {
            Log.d("updateDateDetail", "e==" + e);
        }


    }

    public interface StarDetailPriceVIewCallback {
        public void getMinute(double price, int bsType);
    }

}
