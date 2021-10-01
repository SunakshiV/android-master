package com.procoin.util;

import com.procoin.http.tjrcpt.VHttpServiceManager;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by zhengmj on 19-2-28.
 */

public class TjrLikeOrDislikeUtil {
    private long roomId;
    private long msgId;
    private Callback<ResponseBody> callback;
    private Call<ResponseBody> call;
    public void setHttpCallback(Callback<ResponseBody> callback){
        this.callback = callback;
    }
    public void start(){
        doLikeOrDislike();
    }
    public TjrLikeOrDislikeUtil(long roomId,long msgId){
        this.roomId = roomId;
        this.msgId = msgId;
    }
    private void doLikeOrDislike(){
        CommonUtil.cancelCall(call);
        call = VHttpServiceManager.getInstance().getVService().likeOrDislikeLive(roomId,msgId);
        if (call!=null)call.enqueue(callback);
    }
}
