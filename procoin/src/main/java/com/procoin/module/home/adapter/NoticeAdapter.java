package com.procoin.module.home.adapter;

import android.content.Context;

import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.module.home.entity.Notice;
import com.procoin.util.DateUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 公告
 */

public class NoticeAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<Notice> {

    private Context context;

    public NoticeAdapter(Context c) {
        super(c, R.drawable.ic_common_mic);
        this.context = c;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        Log.d("Mymessage", "onBindViewViewHolderWithoutFoot==");
        Notice e = getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(e);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.ll_whole)
        LinearLayout llWhole;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final Notice e) {
            tvTitle.setText(e.title);
            if (!TextUtils.isEmpty(e.createTime))
                tvTime.setText(DateUtils.getStringDateOfString2(e.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));
            llWhole.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(context,e.url);
                }
            });
        }
    }

}
