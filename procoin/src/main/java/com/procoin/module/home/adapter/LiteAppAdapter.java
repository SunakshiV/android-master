package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.home.entity.LiteAppEntity;
import com.procoin.util.PageJumpUtil;
import com.procoin.R;
import com.procoin.module.login.LoginActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class LiteAppAdapter extends BaseImageLoaderRecycleAdapter<LiteAppEntity> {


    private Context context;

    public LiteAppAdapter(Context context) {
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.lite_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivLiteAppIcon)
        ImageView ivLiteAppIcon;
        @BindView(R.id.tvLiteAppName)
        TextView tvLiteAppName;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final LiteAppEntity liteAppEntity) {
            displayImage(liteAppEntity.logo, ivLiteAppIcon);
            tvLiteAppName.setText(liteAppEntity.name);
            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (((TJRBaseToolBarActivity) context).isLogin()) {
                        PageJumpUtil.jumpByPkg(context, liteAppEntity.pkg, liteAppEntity.cls, liteAppEntity.params);
                    } else {
                        LoginActivity.login((TJRBaseToolBarActivity) context);
                    }
                }
            });
        }
    }


}
