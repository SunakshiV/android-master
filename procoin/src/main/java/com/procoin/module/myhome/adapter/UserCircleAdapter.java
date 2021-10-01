package com.procoin.module.myhome.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.circle.CircleInfoActivity;
import com.procoin.module.circle.entity.CircleRoleEnum;
import com.procoin.R;
import com.procoin.http.widget.view.RoundAngleImageView;
import com.procoin.module.circle.entity.CircleInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhengmj on 18-10-26.
 */

public class UserCircleAdapter extends BaseImageLoaderRecycleAdapter<CircleInfo> {


    private Context context;

    public UserCircleAdapter(Context context) {
        super(R.drawable.ic_common_mic);
        this.context = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.user_circle_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_head)
        RoundAngleImageView ivHead;
        @BindView(R.id.tvCircleName)
        TextView tvCircleName;
//        @BindView(R.id.tvCircleId)
//        TextView tvCircleId;
        @BindView(R.id.tvMemberAmount)
        TextView tvMemberAmount;
        @BindView(R.id.tvCircleRoleName)
        TextView tvCircleRoleName;
        @BindView(R.id.home)
        LinearLayout home;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CircleInfo data) {
            displayImage(data.circleLogo, ivHead);
            tvCircleName.setText(data.circleName);
//            tvCircleId.setText("(ID: "+data.circleId+")");
            tvMemberAmount.setText("会员: "+data.memberAmount+"位");
            tvCircleRoleName.setText(CircleRoleEnum.getRoleName(data.role));
            if (CircleRoleEnum.isRoot(data.role)) {
                tvCircleRoleName.setBackgroundResource(R.drawable.shape_circle_role_root);
                tvCircleRoleName.setTextColor(ContextCompat.getColor(context,R.color.black));
            } else {
                tvCircleRoleName.setBackgroundResource(R.drawable.shape_circle_role_admin);
                tvCircleRoleName.setTextColor(ContextCompat.getColor(context,R.color.white));
            }
            home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CircleInfoActivity.pageJumpThis(context,data.circleId);
                }
            });
        }

    }
}
