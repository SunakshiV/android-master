package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.module.home.MarketActivity;
import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.http.base.Group;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.entity.Market;

import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class SearchCoinAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Market> {


    private Context context;

    public SearchCoinAdapter(Context context) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
    }

    private OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }


    @Override
    public void setGroup(Group g) {
        if (g != null && g.size() > 0) {
            Collections.sort(g, comparator);
        }
        super.setGroup(g);
    }

    Comparator comparator = new Comparator<Market>() {
        @Override
        public int compare(Market o1, Market o2) {
            int ret = 0;
            if (o2.sortTime > o1.sortTime) {
                ret = 1;
            } else if (o2.sortTime < o1.sortTime) {
                ret = -1;
            } else {
                ret = 0;
            }
            return ret;
        }
    };

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.search_coin_item, parent, false));
    }

    @Override
    public void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvSymbol)
        TextView tvSymbol;
        @BindView(R.id.tvName)
        TextView tvName;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Market data, final int pos) {
            tvSymbol.setText(data.symbol);
            tvName.setText(data.name);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (data.type == 0) {
                        MarketActivity.pageJump(context,data.symbol,1);
//                        MarketActivity.pageJump(context, data.symbol);
                        if (onItemClick != null) {
                            onItemClick.onItemClickListen(pos, data);
                        }
                    } else if (data.type == 1){
                        if (!TextUtils.isEmpty(data.url))
                            CommonWebViewActivity.pageJumpCommonWebViewActivity(context, data.url);
                    }

                }
            });

        }
    }


}
