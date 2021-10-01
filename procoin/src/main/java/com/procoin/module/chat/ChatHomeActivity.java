package com.procoin.module.chat;

import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;

import com.procoin.common.base.TJRBaseActionBarSwipeBackObserverActivity;
import com.procoin.data.db.TaoJinLuDatabase;
import com.procoin.module.chat.adapter.ChatHomeAdapter;
import com.procoin.module.chat.entity.ChatHomeEntity;
import com.procoin.module.circle.entity.CircleChatEntity;
import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.widgets.SimpleRecycleDivider;
import com.procoin.data.sharedpreferences.PrivateChatSharedPreferences;
import com.procoin.http.util.CommonUtil;
import com.procoin.task.BaseAsyncTask;
import com.procoin.R;


/**
 * 我的私聊列表
 */
public class ChatHomeActivity extends TJRBaseActionBarSwipeBackObserverActivity {
//    private int backType;// 1代表是从通知点击进来的
    private FrameLayout fl_no_content;
    private TaoJinLuDatabase mDb;
    protected RecyclerView listView;
    private ChatHomeAdapter chatHomeAdapter;
    private ClearChatInfoTask clearChatInfoTask;

    public long myUserId;

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return "聊天";
    }

    @Override
        protected void handlerMsg(ReceiveModel model) {
        switch (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type)) {
            case private_chat_record:  //收到一条新信息

                if (model.obj instanceof CircleChatEntity) {
                    CircleChatEntity newEntity = (CircleChatEntity) model.obj;
                    if (newEntity != null) {
                        boolean newChat = true;   //是否为对方创建的新对话
                        for (int i = 0; i < chatHomeAdapter.getItemCount(); i++) {
                            ChatHomeEntity oldEntity = chatHomeAdapter.getItem(i);
                            Log.d("200","ChatHomeEntity handlerMsg == "+oldEntity.toString());
                            if (oldEntity != null) {
                                if (oldEntity.chatTopic.equals(newEntity.chatTopic)) {
                                    if (newEntity.userId != myUserId) {
                                        oldEntity.name = newEntity.name;
                                        oldEntity.headurl = newEntity.headurl;
                                    }
                                    oldEntity.latestMsg = newEntity.say;
                                    oldEntity.chatCreateTime = Long.parseLong(newEntity.createTime);
                                    oldEntity.notify = newEntity.isPush;
                                    newChat = false;
                                }
                            }
                        }
                        if (newChat) {
                            chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
                        } else {
                            chatHomeAdapter.notifyDataSetChangedWithComparator();

                        }
                    }

                    }

                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fl_no_content = (FrameLayout) findViewById(R.id.fl_no_content);
        mDb = TaoJinLuDatabase.getInstance(this.getApplicationContext());
        myUserId = Long.parseLong(getUserId());
        listView = (RecyclerView) findViewById(R.id.rv_list);
        chatHomeAdapter = new ChatHomeAdapter(this, Long.parseLong(getUserId()));
        listView.setAdapter(chatHomeAdapter);
        listView.setLayoutManager(new LinearLayoutManager(this));
        listView.addItemDecoration(new SimpleRecycleDivider(this));
        listView.setBackgroundColor(ContextCompat.getColor(this,R.color.pageBackground));
    }

    @Override
    protected void onResume() {
        super.onResume();
        chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
        for (ChatHomeEntity entity : mDb.getChatHomeList(myUserId)){
            Log.d("200","ChatHomeEntity == "+entity.toString());
        }
    }


    public void startClearChatInfoTask(long myUserId, String chatTopic) {
        CommonUtil.cancelAsyncTask(clearChatInfoTask);
        clearChatInfoTask = (ClearChatInfoTask) new ClearChatInfoTask(myUserId, chatTopic).executeParams();
    }

    class ClearChatInfoTask extends BaseAsyncTask<Void, Void, Boolean> {

        long myUserId;
        String chatTopic;


        public ClearChatInfoTask(long myUserId, String chatTopic) {
            this.myUserId = myUserId;
            this.chatTopic = chatTopic;
        }


        @Override
        protected void onPreExecute() {
            showProgressDialog();
        }

        @Override

        protected Boolean doInBackground(Void... params) {
            int clearInfoNums = mDb.deleteChatHomeRecord(String.valueOf(myUserId), chatTopic);//清空与莫个人的私聊房间
            int clearChatNums = mDb.clearPrivateChat(myUserId, chatTopic);//清空与莫个人的私聊所有聊天记录
            PrivateChatSharedPreferences.clearPriChatRecordNum(ChatHomeActivity.this, chatTopic, myUserId);//清除消息数量
            if (clearInfoNums >= 0 && clearChatNums >= 0) {
                return true;
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            dismissProgressDialog();
            if (aBoolean) {
                CommonUtil.showToast(ChatHomeActivity.this, "删除成功", Gravity.CENTER);
            } else {
                CommonUtil.showToast(ChatHomeActivity.this, "删除失败", Gravity.CENTER);
            }
            chatHomeAdapter.setGroup(mDb.getChatHomeList(myUserId));
//            if (chatHomeAdapter.getItemCount()>0){
//                fl_no_content.setVisibility(View.GONE);
//            }else {
//                fl_no_content.setVisibility(View.VISIBLE);
//            }
        }
    }

}
