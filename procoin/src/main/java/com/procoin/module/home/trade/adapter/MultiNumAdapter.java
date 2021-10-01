package com.procoin.module.home.trade.adapter;

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

public class MultiNumAdapter extends RecyclerView.Adapter {

    private Context context;
    private String[] data;

    private String multNum = "";

    private onItemclickListen onItemclickListen;

    public void setOnItemclickListen(MultiNumAdapter.onItemclickListen onItemclickListen) {
        this.onItemclickListen = onItemclickListen;
    }

    public void setData(String[] data) {
        this.data = data;
        notifyDataSetChanged();
    }


    public void clearAllItem() {
        data = null;
        multNum = "";
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.length;
    }


    public MultiNumAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.multnum_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(data[position]);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvNum)
        TextView tvNum;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final String num) {
            tvNum.setText(num+"X");
            if (multNum.equals(num)) {
                tvNum.setSelected(true);
            } else {
                tvNum.setSelected(false);
            }
            tvNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelected(num);
                    if (onItemclickListen != null) {
                        onItemclickListen.onItemclick(num);
                    }
                }
            });
        }
    }


    public void setSelected(String keyType) {
        if (!this.multNum.equals(keyType)) {
            this.multNum = keyType;
            notifyDataSetChanged();
        }

    }


    public interface onItemclickListen {
        public void onItemclick(String multnum);
    }

}
