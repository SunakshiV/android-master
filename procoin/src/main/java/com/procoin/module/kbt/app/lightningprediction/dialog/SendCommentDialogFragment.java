package com.procoin.module.kbt.app.lightningprediction.dialog;

import android.app.Activity;
import android.content.Context;
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

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.util.CommonUtil;
import com.procoin.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SendCommentDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvCancel)
    TextView tvCancel;
    @BindView(R.id.etComment)
    EditText etComment;
    @BindView(R.id.tvSend)
    TextView tvSend;
    private TJRBaseToolBarActivity mActivity;


    private OnSendCommentCallBack onSendCommentCallBack;

    public void setOnSendCommentCallBack(OnSendCommentCallBack onSendCommentCallBack) {
        this.onSendCommentCallBack = onSendCommentCallBack;
    }


    /**
     * @return
     */
    public static SendCommentDialogFragment newInstance() {
        SendCommentDialogFragment dialog = new SendCommentDialogFragment();
//        Bundle bundle = new Bundle();
//        dialog.setArguments(bundle);
        return dialog;
    }

    public void showDialog(FragmentManager manager, String tag) {
        this.show(manager, tag);
    }

    @Override
    public void onAttach(Activity mActivity) {
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment                      ---> onAttach ");
        super.onAttach(mActivity);
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
//        type=b.getInt(CommonConst.KEY_EXTRAS_TYPE);

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
        View v = inflater.inflate(R.layout.lp_send_comment_dialog, container, false);


        ButterKnife.bind(this, v);

        tvCancel.setOnClickListener(this);
        tvSend.setOnClickListener(this);
        return v;
    }

    public void clearText() {
        if (etComment != null) {
            etComment.setText("");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvCancel:
                if (onSendCommentCallBack != null) {
                    onSendCommentCallBack.onDialogDismiss();
                }
                break;
            case R.id.tvSend:
                String comment = etComment.getText().toString().trim();
                if (TextUtils.isEmpty(comment)) {
                    CommonUtil.showmessage("请输入评论", getContext());
                    return;
                }
                if (comment.length() > 140) {
                    CommonUtil.showmessage("文字太长", getContext());
                    return;
                }
                colseKeybord();
                if (onSendCommentCallBack != null) {
                    onSendCommentCallBack.onSendComment(comment);
                }
                break;

            default:
                break;
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->            onResume   isVisible = " + getUserVisibleHint());

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


    public interface OnSendCommentCallBack {
        void onDialogDismiss();

        void onSendComment(String comment);
    }


    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
