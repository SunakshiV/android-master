package com.procoin.module.circle.adapter;

import android.content.Context;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.base.adapter.BaseLoadMoreImageLoaderRecycleAdapter;
import com.procoin.common.entity.ResultData;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.module.circle.entity.CircleMemberUser;
import com.procoin.module.circle.entity.CircleRoleEnum;
import com.procoin.module.dialog.ManageCircleFragment;
import com.procoin.module.myhome.PersonalHomepageActivity;
import com.procoin.util.MyCallBack;
import com.procoin.widgets.CircleImageView;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 18-10-26.
 */

public class CircleMemberAdapter extends BaseLoadMoreImageLoaderRecycleAdapter<CircleMemberUser> {

    private Context context;
    private int userRole;
    private long userId;
    private String circleId;
    private Call<ResponseBody> updateRoleCall;
    private Call<ResponseBody> removeMemberCall;

    public CircleMemberAdapter(Context context, String circleId, int userRole, long userId) {
        super(context, R.drawable.ic_common_mic);
        this.context = context;
        this.userRole = userRole;
        this.userId = userId;
        this.circleId = circleId;
    }

    @Override
    protected int getItemType(int position) {
        return 0;
    }


    @Override
    protected RecyclerView.ViewHolder onCreateViewHolderWithoutFoot(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.circle_member_list_item, parent, false));
    }

    @Override
    protected void onBindViewViewHolderWithoutFoot(RecyclerView.ViewHolder holder, int position) {
        ViewHolder holder1 = (ViewHolder) holder;
        holder1.setData(getItem(position));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivHeader)
        CircleImageView ivHeader;
        @BindView(R.id.tvUsername)
        TextView tvUsername;
        @BindView(R.id.tvCircleRoleName)
        TextView tvCircleRoleName;
        @BindView(R.id.llHome)
        LinearLayout llHome;
        @BindView(R.id.ivPoint)
        ImageView ivPoint;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(final CircleMemberUser data) {
            displayImage(data.headUrl, ivHeader);
            tvUsername.setText(data.userName);
            if (CircleRoleEnum.isRootOrAdmin(data.role)) {
                tvCircleRoleName.setVisibility(View.VISIBLE);
                tvCircleRoleName.setText(CircleRoleEnum.getRoleName(data.role));
                if (CircleRoleEnum.isRoot(data.role)) {
                    tvCircleRoleName.setBackgroundResource(R.drawable.shape_circle_role_root);
                    tvCircleRoleName.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else {
                    tvCircleRoleName.setBackgroundResource(R.drawable.shape_circle_role_admin);
                    tvCircleRoleName.setTextColor(ContextCompat.getColor(context, R.color.white));
                }
            } else {
                tvCircleRoleName.setVisibility(View.GONE);
            }
            if (userId == data.userId) {
                ivPoint.setVisibility(View.GONE);
            } else {
                ivPoint.setVisibility(View.VISIBLE);
            }
            llHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    if (CircleRoleEnum.isllegal(data.role)) return;
                    if (userId == data.userId) return;
                    ManageCircleFragment manageCircleFragment = ManageCircleFragment.newInstance(userRole, data.role);
                    manageCircleFragment.setOnManagerCircleMember(new ManageCircleFragment.OnManagerCircleMember() {
                        @Override
                        public void onclick(int type) {
                            if (type == 1) {//移除圈子
                                startRemoveMember(data, data.userId, false);
                            } else if (type == 2) {//移除圈子并拉黑
                                startRemoveMember(data, data.userId, true);
                            } else if (type == 3) {//取消管理员
                                startGetCircleInfo(data, CircleRoleEnum.member.role(), data.userId);
                            } else if (type == 4) {//设置管理员
                                startGetCircleInfo(data, CircleRoleEnum.admin.role(), data.userId);
                            } else if (type == 5) {//查看资料
                                PersonalHomepageActivity.pageJumpThis(context, data.userId);
                            } else if (type == 6) {//私聊
                                ChatRoomActivity.pageJump(context,
                                        com.procoin.util.CommonUtil.getChatTop(userId, data.userId), data.userName, data.headUrl);
                            }

                        }
                    });
                    manageCircleFragment.show(((TJRBaseToolBarActivity) context).getSupportFragmentManager(), "");
                }
            });

        }


        private void startGetCircleInfo(final CircleMemberUser data, final int role, long targetUid) {
            CommonUtil.cancelCall(updateRoleCall);
            updateRoleCall = VHttpServiceManager.getInstance().getVService().updateRole(circleId, role, targetUid);
            updateRoleCall.enqueue(new MyCallBack(context) {
                @Override
                protected void callBack(ResultData resultData) {
                    if (resultData.isSuccess()) {
                        CommonUtil.showmessage(resultData.msg, context);
                        data.role = role;
                        notifyDataSetChanged();
                    }
                }

            });
        }

        private void startRemoveMember(final CircleMemberUser data, long targetUid, boolean addToBlackList) {
            CommonUtil.cancelCall(removeMemberCall);
            removeMemberCall = VHttpServiceManager.getInstance().getVService().removeMember(circleId, targetUid, addToBlackList);
            removeMemberCall.enqueue(new MyCallBack(context) {
                @Override
                protected void callBack(ResultData resultData) {
                    if (resultData.isSuccess()) {
                        removeItem(data);
                        notifyDataSetChanged();
                    }
                }

            });
        }


    }
}
