package com.procoin.util.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.procoin.common.entity.ResultData;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.util.entity.OutDragImg;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.R;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.widgets.swipecaptcha.SwipeCaptchaFrameLayout2;
import com.google.gson.Gson;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Created by zhengmj on 19-4-17.
 */

public class DragFragment extends DialogFragment {
    private OnSuccessCallback callback;
    private TextView tv_sign;
    private TextView tv_close;
    private Call<ResponseBody> getDragImgCall;
    private Call<ResponseBody> checkDragCall;

    public final static String TEXT = "text";
    private SwipeCaptchaFrameLayout2 swipeCaptchaFrameLayout;
    public static DragFragment newInstance(OnSuccessCallback onSuccessCallback){
        DragFragment dragFragment = new DragFragment();
        dragFragment.setOnSuccessCallback(onSuccessCallback);
        return dragFragment;
    }

    private void setOnSuccessCallback(OnSuccessCallback callback){
        this.callback = callback;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(params);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_drag,container,false);
        tv_close = view.findViewById(R.id.tv_close);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        String text = null;
        if (getArguments()!=null){
            int titletype = getArguments().getInt(TEXT);
            switch (titletype){
                case 1:
                    text = "完成验证后登录";
                    break;
                case 2:
                    text = "完成验证后获取验证码";
                    break;
            }
        }
//        tv_sign = view.findViewById(R.id.tv_sign);
//        if (!TextUtils.isEmpty(text)){
//            tv_sign.setText(text);
//        }
        swipeCaptchaFrameLayout=view.findViewById(R.id.swipeCaptchaFrameLayout);
        getOutDragImgTask();
        swipeCaptchaFrameLayout.setCheckCallback(new SwipeCaptchaFrameLayout2.CheckCallback() {
            @Override
            public void check(String dragImgKey, int locationx) {
                startCheckTask(dragImgKey, locationx);
            }

            @Override
            public void refresh() {
                getOutDragImgTask();
            }

            @Override
            public void checkSuccess(String dragImgKey, int locationx) {
//                CommonUtil.showmessage("验证成功", getActivity());
                dismiss();
                if (callback!=null)callback.onSuccess(dragImgKey,locationx);

            }
        });
//        swipeCaptchaFrameLayout.setOnCaptchaMatchCallback(new SwipeCaptchaView.OnCaptchaMatchCallback() {
//            @Override
//            public void matchSuccess(SwipeCaptchaView swipeCaptchaView) {
//                Toast.makeText(getActivity(), "恭喜你啊 验证成功 可以搞事情了", Toast.LENGTH_SHORT).show();
//                //swipeCaptcha.createCaptcha();
//                swipeCaptchaFrameLayout.setSeekbarEnable(false);
//                swipeCaptchaFrameLayout.setSuccessDrawable();
//                if (callback != null){
//                    callback.onSuccess();
//                }
//            }
//
//            @Override
//            public void matchFailed(SwipeCaptchaView swipeCaptchaView) {
//                Log.d("zxt", "matchFailed() called with: swipeCaptchaView = [" + swipeCaptchaView + "]");
//                Toast.makeText(getActivity(), "你有80%的可能是机器人，现在走还来得及", Toast.LENGTH_SHORT).show();
//                swipeCaptchaView.resetCaptcha();
//                swipeCaptchaFrameLayout.setProgress(0);
//            }
//        });
        return view;
    }
    private void getOutDragImgTask() {
        CommonUtil.cancelCall(getDragImgCall);

        try{
            TJRBaseToolBarSwipeBackActivity activity = (TJRBaseToolBarSwipeBackActivity) getActivity();
            activity.showProgressDialog();
        }catch (ClassCastException e){

        }
        swipeCaptchaFrameLayout.onloading();//数据请求回来之前先设置false，不让拖
        getDragImgCall = VHttpServiceManager.getInstance().getFileUploadService().getDragImg();
        getDragImgCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                try{
                    TJRBaseToolBarSwipeBackActivity activity = (TJRBaseToolBarSwipeBackActivity) getActivity();
                    activity.dismissProgressDialog();
                }catch (ClassCastException e){

                }
                if (resultData.isSuccess()) {
                    OutDragImg outDragImg = new Gson().fromJson(resultData.data, OutDragImg.class);
                    Log.d("outDragImg", outDragImg.toString());
                    if (outDragImg != null) {
                        if (swipeCaptchaFrameLayout.getVisibility() == View.GONE){
                            swipeCaptchaFrameLayout.setVisibility(View.VISIBLE);
                        }
                        swipeCaptchaFrameLayout.setData(outDragImg);

                    }
                }else{
                    swipeCaptchaFrameLayout.setLoadFail();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                try{
                    TJRBaseToolBarSwipeBackActivity activity = (TJRBaseToolBarSwipeBackActivity) getActivity();
                    activity.dismissProgressDialog();
                }catch (ClassCastException e){

                }
                swipeCaptchaFrameLayout.setLoadFail();
            }
        });
    }
    private void startCheckTask(final String dragImgKey, final int locationx) {
        CommonUtil.cancelCall(checkDragCall);
        swipeCaptchaFrameLayout.onChecking();//数据请求回来之前先设置false，不让拖
        checkDragCall = VHttpServiceManager.getInstance().getVService().checkDrag(dragImgKey, locationx);
        checkDragCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    if(swipeCaptchaFrameLayout!=null){
                        swipeCaptchaFrameLayout.checkSuccess(dragImgKey,locationx);
                    }
                } else {
                    getOutDragImgTask();
                }
            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                swipeCaptchaFrameLayout.setLoadFail();
            }
        });
    }
    public interface OnSuccessCallback{
        void onSuccess(String dragImgKey, int locationx);
    }
}
