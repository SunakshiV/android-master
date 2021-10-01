package com.procoin.task;

import com.procoin.util.MyCallBack;
import com.procoin.http.tjrcpt.VHttpServiceManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 17-9-27.
 */

public class GetShareUrlTask {

    /**
     *
     *
     * @param shareType  1圈子分享，2直播分享;
     * @param id
     * @param myCallBack
     */
    public  GetShareUrlTask(int shareType, long id, MyCallBack myCallBack) {
        String params = "";
        try {
            JSONObject jsonObject = new JSONObject();
            if (shareType == 1) {
                jsonObject.put("circleId", id);
            } else if (shareType == 2){
                jsonObject.put("roomId", id);
            }
            params = jsonObject.toString();
        } catch (JSONException e) {

        }
        startGetInfo(shareType, params, myCallBack);
    }

    /**
     * @param shareType  3 网页分享专用
     * @param target
     * @param myCallBack
     */
    public  GetShareUrlTask(int shareType, String target, MyCallBack myCallBack) {
        String params = "";
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("target", target);
            params = jsonObject.toString();
        } catch (JSONException e) {

        }
        startGetInfo(shareType, params, myCallBack);
    }

    private void startGetInfo(int share_type, String params, MyCallBack myCallBack) {
        Call<ResponseBody> callBack = VHttpServiceManager.getInstance().getVService().getshareinfo(share_type, params);
        if (callBack != null)
            callBack.enqueue(myCallBack);
    }
}
