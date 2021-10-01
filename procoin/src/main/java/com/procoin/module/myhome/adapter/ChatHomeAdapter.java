package com.procoin.module.myhome.adapter;

import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.procoin.data.sharedpreferences.CircleSharedPreferences;
import com.procoin.module.chat.ChatRoomActivity;
import com.procoin.common.base.adapter.BaseImageLoaderRecycleAdapter;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.module.chat.entity.ChatHomeEntity;
import com.procoin.util.DateUtils;
import com.procoin.widgets.BadgeView;
import com.procoin.module.myhome.ChatHomeActivity;
import com.procoin.util.CircleChatSetTextUtils;
import com.procoin.http.widget.dialog.ui.TjrBaseDialog;
import com.procoin.R;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by kechenng on 16-12-1.
 */

public class ChatHomeAdapter extends BaseImageLoaderRecycleAdapter<ChatHomeEntity> {

    private ChatHomeActivity context;
    private long userId;


    public ChatHomeAdapter(ChatHomeActivity context, long userId) {
        super(R.drawable.ic_common_mic);
        this.context = context;
        this.userId = userId;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).setData(getItem(position));
    }

    //    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//        if (convertView == null) {
//            convertView = View.inflate(context, R.layout.item_my_chat, null);
//            holder = new ViewHolder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.setData(getItem(position));
//        return convertView;
//    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivChatLogo;
        ImageView ivNoNotify;
        TextView tvName;
        TextView tvlatestMsg;
        TextView tvTime;
        BadgeView bvNewsNum;

        LinearLayout ll2ChatRoom;


        ViewHolder(View view) {
            super(view);
            ivChatLogo = (ImageView) view.findViewById(R.id.ivChatLogo);
            ivNoNotify = (ImageView) view.findViewById(R.id.ivNoNotify);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvlatestMsg = (TextView) view.findViewById(R.id.tvlatestMsg);
            tvTime = (TextView) view.findViewById(R.id.tvTime);
            bvNewsNum = new BadgeView(context, tvName);
            bvNewsNum.setTextSize(TypedValue.COMPLEX_UNIT_SP, 9);
            bvNewsNum.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
            ll2ChatRoom = (LinearLayout) view.findViewById(R.id.ll2ChatRoom);
        }

        public void setData(final ChatHomeEntity entity) {
            displayImageForHead(entity.headurl, ivChatLogo);
            tvName.setText(entity.name);
            if (entity.isDel) {
                tvlatestMsg.setText(null);
            } else {
                tvlatestMsg.setText(CircleChatSetTextUtils.formatText("", entity.latestMsg));
            }
            tvTime.setText(DateUtils.getChatTimeFormat3(DateUtils.strdate2Date(String.valueOf(entity.chatCreateTime))));
            int chatNum = PrivateChatSharedPreferences.getPriChatRecordNum(context, entity.chatTopic, userId);
            if (chatNum > 0) {
                if (chatNum > 99) {
                    bvNewsNum.setText("99+");
                } else {
                    bvNewsNum.setText(String.valueOf(chatNum));
                }
                bvNewsNum.show();
            } else {
                bvNewsNum.hide();
            }
            entity.notify = CircleSharedPreferences.getCircleSettingRemind(context, userId, entity.chatTopic);
            Log.d("ddd", "name : " + entity.name + "  notify : " + entity.notify);
            if (entity.notify) {
                ivNoNotify.setVisibility(View.INVISIBLE);
            } else {
                ivNoNotify.setVisibility(View.VISIBLE);
            }

            ll2ChatRoom.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Bundle b = new Bundle();
//                    b.putString(CommonConst.CHAT_TOPIC, entity.chatTopic);
//                    b.putLong(CommonConst.TAUSERID, entity.taUserId);
////                    PrivateChatSharedPreferences.clearPriChatRecordNum(ChatHomeActivity.this, item.chatTopic, getUserId());
//                    notifyDataSetChanged();   //使消息数量提醒消失
//                    PageJumpUtil.pageJump(context, ChatRoomActivity.class, b);
                    ChatRoomActivity.pageJump(context, entity.chatTopic, entity.name, entity.headurl);
                }
            });
            ll2ChatRoom.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (entity != null) {
                        showDelChatTipsDialog(entity);
                    }
                    return true;
                }
            });
        }
    }

    TjrBaseDialog delChatTipsDialog;

    private void showDelChatTipsDialog(final ChatHomeEntity entity) {
        delChatTipsDialog = new TjrBaseDialog(context) {
            @Override
            public void onclickOk() {
                dismiss();
                context.startClearChatInfoTask(userId, entity.chatTopic);
            }

            @Override
            public void onclickClose() {
                dismiss();
            }

            @Override
            public void setDownProgress(int progress) {

            }
        };
        delChatTipsDialog.setMessage("删除后,将清空该聊天的消息记录");
        delChatTipsDialog.setBtnOkText("删除");
        delChatTipsDialog.setTitleVisibility(View.GONE);
        delChatTipsDialog.show();
    }

    final Comparator comparator = new Comparator<ChatHomeEntity>() {
        @Override
        public int compare(ChatHomeEntity lhs, ChatHomeEntity rhs) {
            long chatCreateTime0 = lhs.chatCreateTime;
            long chatCreateTime1 = rhs.chatCreateTime;
            int result = (int) (chatCreateTime1 - chatCreateTime0);
            return result;

        }
    };

    public void notifyDataSetChangedWithComparator() {
        if (getItemCount() > 1) {
            Collections.sort(group, comparator);
        }
        notifyDataSetChanged();
    }

//    @Override
//    public void notifyDataSetChanged() {
//        if (getItemCount() > 1) {
//            Collections.sort(group, comparator);
//        }
//        super.notifyDataSetChanged();
//    }
}
