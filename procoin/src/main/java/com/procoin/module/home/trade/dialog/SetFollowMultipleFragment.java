package com.procoin.module.home.trade.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 设置跟单倍数
 */

public class SetFollowMultipleFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.etMultiple)
    EditText etMultiple;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    private TJRBaseToolBarActivity mActivity;
    private Call<ResponseBody> getTradeConfigCall;

    private SetMultipleListen setMultipleListen;


    public void setSetMultipleListen(SetMultipleListen setMultipleListen) {
        this.setMultipleListen = setMultipleListen;
    }


    /**
     * 非摘单 入参
     *
     * @return
     */
    public static SetFollowMultipleFragment newInstance() {
        SetFollowMultipleFragment dialog = new SetFollowMultipleFragment();
        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreate ");
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog);
        mActivity = (TJRBaseToolBarActivity) getActivity();
        //入参处理
        Bundle b = getArguments();
        if (null == b) return;

    }

    @Override
    public void onStart() {
        CommonUtil.LogLa(2, "OLStarHomeDialogFragment                      ---> onStart ");
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onCreateView ");
        View v = inflater.inflate(R.layout.set_follow_multiple, container, false);
        ButterKnife.bind(this, v);
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(etMultiple.getText().toString())) {
                    com.procoin.http.util.CommonUtil.showmessage("请输入倍数", getActivity());
                    return;
                }
                colseKeybord();
                int multiple = Integer.parseInt(etMultiple.getText().toString());
                if (multiple > 0) {
                    if (setMultipleListen != null) {
                        setMultipleListen.setMultiple(multiple);
                    }
                } else {
                    CommonUtil.showmessage("倍数不能为0", getActivity());
                }

            }
        });
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->            onResume   isVisible = " + getUserVisibleHint());
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->           setUserVisibleHint  isVisibleToUser = " + isVisibleToUser);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }

    public interface SetMultipleListen {
        void setMultiple(int multiple);
    }
}
