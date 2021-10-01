package com.procoin.module.copy.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class HoldCoinSymbolAdapter extends RecyclerView.Adapter {

    private Context context;
    private String[] data;

    private onItemclickListen onItemclickListen;

    public void setOnItemclickListen(HoldCoinSymbolAdapter.onItemclickListen onItemclickListen) {
        this.onItemclickListen = onItemclickListen;
    }

    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }


    public HoldCoinSymbolAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.hold_coin_symbol_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvSymbol)
        TextView tvSymbol;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String symbol) {
            tvSymbol.setText(symbol);
            if (selectedSymbol.equals(symbol)) {
                tvSymbol.setSelected(true);
            } else {
                tvSymbol.setSelected(false);
            }
            tvSymbol.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(symbol);
                    if (onItemclickListen != null) {
                        onItemclickListen.onItemclick(symbol);
                    }
                }
            });
        }
    }

    private String selectedSymbol = "";

    public void setSelected(String symbol) {
        if (!this.selectedSymbol.equals(symbol)) {
            this.selectedSymbol = symbol;
            notifyDataSetChanged();
        }

    }


    public interface onItemclickListen {
        public void onItemclick(String symbol);
    }

}
