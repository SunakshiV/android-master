package com.procoin.module.myhome.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.common.constant.CommonConst;
import com.procoin.module.myhome.entity.MyMessage;
import com.procoin.util.DateUtils;
import com.procoin.util.JsonParserUtils;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.CircleImageView;
import com.procoin.R;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的消息
 */

public class MyMessageAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<MyMessage> {

    private Context context;

    public MyMessageAdapter(Context c) {
        super(c, R.drawable.ic_common_mic);
        this.context = c;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }

    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_message, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        Log.d("Mymessage", "onBindViewViewHolderWithoutFoot==");
        MyMessage e = getItem(position);
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setData(e);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHead)
        CircleImageView ivHead;
        @BindView(R.id.tvOfficial)
        TextView tvOfficial;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvTitle)
        TextView tvTitle;
        @BindView(R.id.tvTime)
        TextView tvTime;
        @BindView(R.id.tvContent)
        TextView tvContent;
        @BindView(R.id.tvExtra)
        TextView tvExtra;
        @BindView(R.id.ll_whole)
        LinearLayout llWhole;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final MyMessage e) {
            displayImageForHead(e.headUrl, ivHead);
            if (e.isVip == 0) {
                tvOfficial.setVisibility(View.GONE);
            } else {
                tvOfficial.setVisibility(View.VISIBLE);
            }
            tvName.setText(e.userName);
            tvTitle.setText(e.title);
            if (!TextUtils.isEmpty(e.createTime))
                tvTime.setText(DateUtils.getStringDateOfString2(e.createTime, DateUtils.TEMPLATE_yyyyMMdd_HHmm));
//                tvTime.setText(DateUtils.getPastTime(e.createTime));
            tvContent.setText(e.content);
            ivHead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            if (!TextUtils.isEmpty(e.extra)) {
                try {
                    JSONObject jsonObject = new JSONObject(e.extra);
                    String pkg = "";
                    String cls = "";
                    String params = "";
                    String title = "";
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "pkg")) {
                        pkg = jsonObject.getString("pkg");
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "cls")) {
                        cls = jsonObject.getString("cls");
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "params")) {
                        params = jsonObject.getString("params");
                    }
                    if (JsonParserUtils.hasAndNotNull(jsonObject, "title")) {
                        title = jsonObject.getString("title");
                    }
                    if (!TextUtils.isEmpty(title)) {
                        tvExtra.setVisibility(View.VISIBLE);
                        tvExtra.setText(title);
                    } else {
                        tvExtra.setVisibility(View.GONE);
                    }

                    if (!TextUtils.isEmpty(pkg) && !TextUtils.isEmpty(cls)) {
                        final String pkg2 = pkg;
                        final String cls2 = cls;
                        final String params2 = params;
                        llWhole.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle = new Bundle();
                                bundle.putString(CommonConst.MSG_PARAMS, params2);
                                Intent intent = new Intent();
                                intent.setClassName(pkg2, cls2);
                                intent.putExtras(bundle);
                                PageJumpUtil.pageJump(context, intent);
                            }
                        });

                    }
                } catch (JSONException exception) {
                }
            } else {
                tvExtra.setVisibility(View.GONE);
            }

        }
    }

}
