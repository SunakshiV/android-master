package com.procoin.common.base;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.procoin.subpush.ReceiveModel;
import com.procoin.subpush.ReceiveModelTypeEnum;
import com.procoin.module.circle.entity.SocketFail;
import com.procoin.subpush.ReceivedManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.subpush.IRemoteService;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by zhengmj on 15-12-24.
 * <p/>
 * <p/>
 * 这个主要是socket用到
 */
public abstract class TJRBaseActionBarSwipeBackObserverActivity extends TJRBaseToolBarSwipeBackActivity implements Observer {
//private  String title;
    protected static final String CONNECTION_ERROR="连接中...";
    protected IRemoteService iRemoteService;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.obj != null) {
                if (msg.obj instanceof ReceiveModel) {
                    ReceiveModel model = (ReceiveModel) msg.obj;
                    if (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type) == ReceiveModelTypeEnum.connection_error) {//显示重连状态
//                        mActionBar.setTitle(CONNECTION_ERROR);
                        showReConnection();
                    } else if (ReceiveModelTypeEnum.getReceiveModelTypeEnum(model.type) == ReceiveModelTypeEnum.connection_suc) {//连接成功
//                        mActionBar.setTitle(title);
                        hideReConnection();
                    } else {
                        if (model.obj != null && model.obj instanceof SocketFail) {//请求数据失败
                            SocketFail socketFail = (SocketFail) model.obj;
                            CommonUtil.showmessage(socketFail.msg, TJRBaseActionBarSwipeBackObserverActivity.this);
                            scoketError(socketFail);
                        } else {//正常返回数据
                            handlerMsg(model);
                        }
                    }

                }
            }
        }
    };

    /**
     * 用来处理错误,不然没办法消去圈圈
     */
    protected void scoketError(SocketFail socketFail) {

    }


    protected abstract void handlerMsg(ReceiveModel model);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReceivedManager.getInstance(this).addObserver(this);
        iRemoteService = ReceivedManager.getInstance(this).getiRemoteService();
//        title= TextUtils.isEmpty(mActionBar.getTitle())?"":mActionBar.getTitle().toString();
//        mActionBar.setTitle("TITLE");
//        Log.d("onCreate_title","title=="+title);
    }

    @Override
    protected void onDestroy() {
        ReceivedManager.getInstance(this).deleteObserver(this);
        super.onDestroy();
    }

    @Override
    public void update(Observable observable, Object data) {
        Message message = new Message();
        message.obj = data;
        handler.sendMessage(message);
    }

    protected CharSequence mActionBarTitle;

    protected void showReConnection() {
        if (mActionBarTitle == null) mActionBarTitle = mActionBar.getTitle();//因为重连状态会一直调用，所以要判null
        Log.d("showReConnection", "title==" + mActionBarTitle);
        mActionBar.setTitle(CONNECTION_ERROR);
    }
    protected void hideReConnection() {
        if(mActionBarTitle != null && mActionBarTitle.length()>0 && !CONNECTION_ERROR.equals(mActionBarTitle))mActionBar.setTitle(mActionBarTitle);
        mActionBarTitle = null;
    }


    //TODO Test
    @Override
    protected void onResume() {
        super.onResume();

        com.procoin.social.util.CommonUtil.LogLa(2, "Class user_name is =========== " + this.getClass().getName());
    }
}
