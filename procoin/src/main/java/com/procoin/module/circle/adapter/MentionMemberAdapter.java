package com.procoin.module.circle.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.procoin.R;
import com.procoin.http.base.baseadapter.BaseImageLoaderAdapter;
import com.procoin.module.circle.entity.CircleMemberUser;
import com.procoin.module.circle.entity.PinnedMentionUser;
import com.procoin.widgets.PinnedSectionListView;

public class MentionMemberAdapter extends BaseImageLoaderAdapter<PinnedMentionUser> implements PinnedSectionListView.PinnedSectionListAdapter {
    private int mLayoutToInflate;
    private AppCompatActivity activity;

    public MentionMemberAdapter(AppCompatActivity activity) {
        super(R.drawable.ic_head_default_photo);
        this.activity = activity;
        mLayoutToInflate = R.layout.friend_list_item_mention;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseViewHolder holder = null;
        if (convertView == null) {
            switch (getItemViewType(position)) {
                case 0:
                    convertView = View.inflate(activity, mLayoutToInflate, null);
                    holder = new FriendViewHolder(convertView);
                    convertView.setTag(holder);
                    break;
                case 1:
                    convertView = View.inflate(activity, R.layout.friend_pinnedselect_item, null);
                    holder = new FriendSelectViewHolder(convertView);
                    convertView.setTag(holder);
                    break;
                default:
                    break;
            }
        } else {
            holder = (BaseViewHolder) convertView.getTag();
        }
        if (holder != null) holder.setValue(position);
        return convertView;
    }

    private class FriendViewHolder extends BaseViewHolder {
        ImageView photo;
        TextView name;
        LinearLayout llRole;
        ImageView ivCircleRoleLogo;
        TextView tvCircleRoleName;

        public FriendViewHolder(View view) {
            photo = (ImageView) view.findViewById(R.id.ivPhone);
            name = (TextView) view.findViewById(R.id.friend_list_item_name);
            tvCircleRoleName = (TextView) view.findViewById(R.id.tvCircleRoleName);
            llRole = (LinearLayout) view.findViewById(R.id.llRole);
            ivCircleRoleLogo = (ImageView) view.findViewById(R.id.ivCircleRoleLogo);
        }

        @Override
        public void setValue(int position) {
            PinnedMentionUser puser = getItem(position);
            if (puser != null) {
                CircleMemberUser user = puser.user;
                if (user != null) {
                    displayImage(user.headUrl, photo);
                    name.setText(user.userName != null && !"".equals(user.userName.trim()) ? user.userName : "[注册中...]");
                }
                if (user.role == 0) {
                    llRole.setVisibility(View.GONE);
                } else if (user.role == 5) {
                    llRole.setVisibility(View.VISIBLE);
                    ivCircleRoleLogo.setImageResource(R.drawable.ic_circle_circle_manager_logo);
                    tvCircleRoleName.setText("管理员");
                } else if (user.role == 10) {
                    llRole.setVisibility(View.VISIBLE);
                    ivCircleRoleLogo.setImageResource(R.drawable.ic_circle_circle_creater_logo);
                    tvCircleRoleName.setText("圈主");
                }
            }
        }
    }

    private class FriendSelectViewHolder extends BaseViewHolder {
        private TextView indexTv;

        public FriendSelectViewHolder(View view) {
            indexTv = (TextView) view.findViewById(R.id.indexTv);
            indexTv.setEnabled(false);
        }

        @Override
        public void setValue(int position) {
            PinnedMentionUser puser = getItem(position);
            if (puser != null && puser.user != null) {
                CircleMemberUser user = puser.user;
                indexTv.setText(user.shiftKey != null && !"".equals(user.shiftKey.trim()) ? user.shiftKey : "#");
            }
        }

    }
    public abstract class BaseViewHolder {

        public abstract void setValue(int position);

    }

    @Override
    public boolean isItemViewTypePinned(int viewType) {
        return false;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) != null ? getItem(position).type : 0;
    }

}
