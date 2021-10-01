package com.procoin.module.chat;

import android.os.Bundle;

import com.procoin.common.base.TJRBaseActionBarSwipeBackActivity;
import com.procoin.data.db.TaoJinLuDatabase;

/**
 * Created by zhengmj on 17-7-12.
 */

public class ChatTestActivity extends TJRBaseActionBarSwipeBackActivity {
    private TaoJinLuDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = getApplicationContext().getmDb();
//        new GetChatTopicTask(getUserId(), 1).executeParams();
    }

//    /**
//     * 当收到一个新的由对方创建的对话时,需要获取对话的完整信息
//     */
//
//    class GetChatTopicTask extends BaseAsyncTask<Void, Void, Boolean> {
//        private String msg;
//        private String chatTopic;
//        private Exception e;
//        private long taUserId;
//        private long myUserId;
//        private User user;
//        private ResultDataParser resultDataParser;
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        public GetChatTopicTask(long userId, long taUserId) {
//            this.myUserId = userId;
//            this.taUserId = taUserId;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
//                String result = PervalHttp.getInstance().createChatTopic(this.myUserId, this.taUserId);
//                JSONObject joAll = new JSONObject(result);
//                ResultData data = new ResultDataParser().parse(joAll);
//                if (ParserJsonUtils.hasAndNotNull(joAll, "msg")) {
//                    msg = joAll.getString("msg");
//                }
//                JSONObject rdData = data.returnJSONObject();
//                if (ParserJsonUtils.hasAndNotNull(rdData, "chat_topic")) {
//                    chatTopic = rdData.getString("chat_topic");
//                }
//
//                if (ParserJsonUtils.hasAndNotNull(joAll, "user")) {
//                    user = new UserJsonParser().parse(joAll.getJSONObject("user"));
//                }
//
//                if (ParserJsonUtils.hasAndNotNull(joAll, "success")) {
//                    if (joAll.getBoolean("success")) {
//                        return true;
//                    }
//                }
//            } catch (Exception e) {
//                this.e = e;
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            dismissProgressDialog();
//            if (aBoolean) {
//                if (!TextUtils.isEmpty(chatTopic)) {
//                    mDb.saveOrUpdateChatInfo(myUserId, user.getUserId(), user.getHeadurl(), user.getName(), chatTopic);
//                    mDb.saveAndUpdateUserInfo(user.getUserId(), user.getName(), user.getHeadurl());
//                    Bundle b = new Bundle();
//                    b.putString(CommonConst.CHAT_TOPIC, chatTopic);
//                    b.putLong(CommonConst.TAUSERID, taUserId);
//                    PageJumpUtil.pageJump(ChatTestActivity.this, ChatRoomActivity.class, b);
//                }
//            } else {
//                if (!TextUtils.isEmpty(msg)) {
//                    CommonUtil.showToast(ChatTestActivity.this, msg, Gravity.CENTER);
//                }
//                if (e != null) {
//                    NotificationsUtil.ToastReasonForFailure(ChatTestActivity.this, e);
//                }
//            }
//        }
//
//    }
}