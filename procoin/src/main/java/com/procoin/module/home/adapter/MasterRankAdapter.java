package com.procoin.module.home.adapter;

import android.content.Context;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.module.myhome.UserHomeActivity;
import com.procoin.widgets.CircleImageView;
import com.procoin.R;
import com.procoin.module.home.entity.SubUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-10-26.
 */

public class MasterRankAdapter extends BaseImageLoaderRecycleAdapter<SubUser> {

    private Context context;
    private Call<ResponseBody> followAddCall;
    private Call<ResponseBody> followCancelCall;
    private boolean isRequest;
    private String myUserId;


    public MasterRankAdapter(Context context, String myUserId) {
//        super(context, R.drawable.ic_common_mic);
        this.context = context;
        this.myUserId = myUserId;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.master_rank_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position), position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvRank)
        TextView tvRank;
        @BindView(R.id.ivRank)
        ImageView ivRank;

        @BindView(R.id.ivhead)
        CircleImageView ivhead;
        @BindView(R.id.tvName)
        TextView tvName;
        @BindView(R.id.tvFollowCount)
        TextView tvFollowCount;
        @BindView(R.id.tvGrade)
        TextView tvGrade;
        //        @BindView(R.id.tvFollow)
//        TextView tvFollow;
        @BindView(R.id.llItem)
        LinearLayout llItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final SubUser data, final int pos) {
            if (pos <= 2) {
                tvRank.setVisibility(View.GONE);
                ivRank.setVisibility(View.VISIBLE);
                if (pos == 0) {
                    ivRank.setImageResource(R.drawable.ic_rank_1);
                } else if (pos == 1) {
                    ivRank.setImageResource(R.drawable.ic_rank_2);
                } else if (pos == 2) {
                    ivRank.setImageResource(R.drawable.ic_rank_3);
                }
            } else {
                tvRank.setVisibility(View.VISIBLE);
                ivRank.setVisibility(View.GONE);
                tvRank.setText(String.valueOf(pos + 1));
            }
            displayImageForHead(data.headUrl, ivhead);
            tvName.setText(data.userName);
            tvFollowCount.setText(data.fanNum + "个关注");
//            tvGrade.setText(data.score);
//            if(String.valueOf(data.userId).equals(myUserId)){
//                tvFollow.setVisibility(View.INVISIBLE);
//            }else{
//                tvFollow.setVisibility(View.VISIBLE);
//                if (data.isFollow == 1) {
//                    tvFollow.setSelected(true);
//                    tvFollow.setText("已关注");
//                } else {
//                    tvFollow.setSelected(false);
//                    tvFollow.setText("关注");
//                }
//                tvFollow.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (data.isFollow == 1) {
//                            startFollowCancel(data, pos);
//                        } else {
//                            startFollowAdd(data, pos);
//                        }
//                    }
//                });
//            }

            llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserHomeActivity.pageJump(context, data.userId);
                }
            });

        }
    }


//    private void startFollowAdd(final SubUser data, final int pos) {
//        if (isRequest) return;
//        isRequest = true;
//        CommonUtil.cancelCall(followAddCall);
//        followAddCall = VHttpServiceManager.getInstance().getVService().followAdd(data.userId);
//        followAddCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                isRequest = false;
//                if (resultData.isSuccess()) {
//                    data.isFollow = 1;
//                    notifyItemChanged(pos);
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                isRequest = false;
//            }
//        });
//    }
//
//    private void startFollowCancel(final SubUser data, final int pos) {
//        if (isRequest) return;
//        isRequest = true;
//        CommonUtil.cancelCall(followCancelCall);
//        followCancelCall = VHttpServiceManager.getInstance().getVService().followCancel(data.userId);
//        followCancelCall.enqueue(new MyCallBack(context) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                isRequest = false;
//                if (resultData.isSuccess()) {
//                    data.isFollow = 0;
//                    notifyItemChanged(pos);
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                isRequest = false;
//            }
//        });
//    }

}
