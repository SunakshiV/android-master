package com.procoin.widgets.quotitian;

import android.content.Context;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.procoin.widgets.quotitian.adapter.StarDetailSellAdapter;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.module.home.OnItemClick;
import com.procoin.util.InflaterUtils;
import com.procoin.widgets.quotitian.adapter.StarDetailBuyAdapter;
import com.procoin.widgets.quotitian.entity.StarArkBidBean;
import com.procoin.widgets.quotitian.entity.jsonparser.StarArkBidBeanParser;

import org.json.JSONArray;


/**
 * 这个类的作用是 买卖那边出现的,这个是单独使用N挡
 * Created by zhengmj on 17-2-22.
 */

public class StarDetailPriceView_N extends LinearLayout {
    public View view;

    private RecyclerView rvSellList, rvBuyList;
    private StarDetailSellAdapter starDetailSellAdapter;
    private StarDetailBuyAdapter starDetailBuyAdapter;

    private StarArkBidBeanParser parser;

    public StarDetailPriceVIewCallback lister;
//    private short entrust_bs;//买的时候显示

    public StarDetailPriceView_N(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public StarDetailPriceView_N(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public StarDetailPriceView_N(Context context) {
        super(context);
        initView();
    }


    private void initView() {
        View view = InflaterUtils.inflateView(getContext(), R.layout.minute_detail_n);
        rvBuyList = view.findViewById(R.id.rvBuyList);
        rvSellList = view.findViewById(R.id.rvSellList);

        starDetailBuyAdapter = new StarDetailBuyAdapter(getContext());
        rvBuyList.setLayoutManager(new LinearLayoutManager(getContext()));
        rvBuyList.setAdapter(starDetailBuyAdapter);

        starDetailSellAdapter = new StarDetailSellAdapter(getContext());
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
     * @param buyReverse  true代表buys需要倒序
     * @param sellReverse true代表sells需要倒序
     * @昨结
     */
    public void updateDateDetail(String buys, boolean buyReverse, String sells, boolean sellReverse) {

        Group<StarArkBidBean> buysList = new Group<>();//买
        Group<StarArkBidBean> sellsList = new Group<>();//卖

        try {
            if (!TextUtils.isEmpty(buys)) {
                JSONArray jsbid = new JSONArray(buys);
                if (buyReverse) {
                    buysList = parser.parseReverse(jsbid);
                } else {
                    buysList = parser.parse(jsbid,1);
                }
            }
            if (!TextUtils.isEmpty(sells)) {
                JSONArray jsark = new JSONArray(sells);
                if (sellReverse) {
                    sellsList = parser.parseReverse(jsark);
                } else {
                    sellsList = parser.parse(jsark,-1);
                }

            }

            starDetailBuyAdapter.setGroup(buysList);
            starDetailSellAdapter.setGroup(sellsList);

        } catch (Exception e) {

        }


    }

    public interface StarDetailPriceVIewCallback {
        public void getMinute(double price, int bsType);
    }

}
