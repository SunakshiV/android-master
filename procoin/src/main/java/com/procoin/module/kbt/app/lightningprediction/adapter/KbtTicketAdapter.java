package com.procoin.module.kbt.app.lightningprediction.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.module.kbt.app.lightningprediction.entity.KbtTicket;
import com.procoin.R;
import com.procoin.common.base.adapter.AMBaseRecycleAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class KbtTicketAdapter extends AMBaseRecycleAdapter<KbtTicket> {


    private Context context;
    private int type;

    private int selectedPos = 0;

    private CallBack callBack;

    public void setCallBack(CallBack callBack) {
        this.callBack = callBack;
    }

    public KbtTicketAdapter(Context context, int type) {
        this.context = context;
        this.type = type;
    }

    public KbtTicket getSelectedTicket() {
        return getItem(selectedPos);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lp_ticket_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ticketName)
        TextView ticketName;
        @BindView(R.id.ticketPrice)
        TextView ticketPrice;
        @BindView(R.id.llTicket)
        LinearLayout llTicket;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(KbtTicket kbtTicket, final int pos) {
            if (selectedPos == pos) {
                llTicket.setSelected(true);
            } else {
                llTicket.setSelected(false);
            }
            ticketName.setText(kbtTicket.name);
            ticketPrice.setText(kbtTicket.price);
            ColorStateList color = null;
            if (type == 1) {
                color = ContextCompat.getColorStateList(context, R.color.xml_lp_ticket_surpass_text_color);
            } else {
                color = ContextCompat.getColorStateList(context, R.color.xml_lp_ticket_not_surpass_text_color);
            }
            ticketName.setTextColor(color);
            ticketPrice.setTextColor(color);

            llTicket.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectedPos = pos;
                    notifyDataSetChanged();
                    if(callBack!=null)callBack.onTicketSelected();


                }
            });
        }
    }

    public interface CallBack{
        void onTicketSelected();
    }

}
