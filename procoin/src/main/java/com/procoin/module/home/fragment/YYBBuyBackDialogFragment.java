package com.procoin.module.home.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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


/**
 * kbt回购
 */
public class YYBBuyBackDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;
    @BindView(R.id.tvRepoAmount)
    TextView tvRepoAmount;
    @BindView(R.id.tvHoldAmount)
    TextView tvHoldAmount;


    private int decimalCount = 8;//小数点数量,金额小数点2位，数量小数点是4位.


    private TJRBaseToolBarActivity mActivity;


    private String repoAmount;//剩余可回购YYB数量
    private String holdAmount;//持有YYB数量


    private OnShareDialogCallBack onShareDialogCallBack;

    private double kbt;

    public void setOnShareDialogCallBack(OnShareDialogCallBack onShareDialogCallBack) {
        this.onShareDialogCallBack = onShareDialogCallBack;
    }

    /**
     * @return
     */
    public static YYBBuyBackDialogFragment newInstance(String repoAmount, String holdAmount) {
        YYBBuyBackDialogFragment dialog = new YYBBuyBackDialogFragment();
//        dialog.user = user;
        Bundle bundle = new Bundle();
        bundle.putString("repoAmount", repoAmount);
        bundle.putString("holdAmount", holdAmount);
        dialog.setArguments(bundle);
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
        repoAmount = b.getString("repoAmount", "0.00");
        holdAmount = b.getString("holdAmount", "0.00");

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
        View v = inflater.inflate(R.layout.yyb_buy_back_dialog, container, false);

        ButterKnife.bind(this, v);

        tvRepoAmount.setText(repoAmount);
        tvHoldAmount.setText("持有YYB数量： " + holdAmount);

        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int posDot = s.toString().indexOf(".");
                if (0 == posDot) {//去除首位的"."
                    s.delete(0, 1);
                } else if (posDot > 0) {
                    if (s.length() - 1 - posDot > decimalCount) {//最多4位小数
                        s.delete(posDot + (decimalCount + 1), posDot + (decimalCount + 2));
                    }
                }
                kbt = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    kbt = Double.parseDouble(s.toString());
                } else {
                    kbt = 0;
                }
            }
        });

//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
//        SpannableString normalText = new SpannableString("");
//        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        SpannableString clickText = new SpannableString("《风险提示》");
//        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        clickText.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//                CommonWebViewActivity.pageJumpCommonWebViewActivity(mActivity, CommonConst.LIABILITY_PROTOCOL);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setColor(ContextCompat.getColor(mActivity, R.color.beebarBlue));
//                ds.setUnderlineText(true);
//            }
//        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        spannableStringBuilder.append(normalText);
//        spannableStringBuilder.append(clickText);
//        spannableStringBuilder.append("和");
//
//        SpannableString clickText2 = new SpannableString("《跟单规则》");
//        clickText2.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        clickText2.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View view) {
//                CommonWebViewActivity.pageJumpCommonWebViewActivity(mActivity, CommonConst.FOLLOWRULE);
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                ds.setColor(ContextCompat.getColor(mActivity, R.color.beebarBlue));
//                ds.setUnderlineText(true);
//            }
//        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        spannableStringBuilder.append(clickText2);
//        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
//        tvProtocol.setText(spannableStringBuilder);
        tvSubmit.setOnClickListener(this);
        tvAll.setOnClickListener(this);
//        startGetTradeConfig();

        return v;
    }


//    private void startGetTradeConfig() {
//        com.cropyme.http.util.CommonUtil.cancelCall(getTradeConfigCall);
//        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("");
//        getTradeConfigCall.enqueue(new MyCallBack(mActivity) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (!isAdded()) {
//                    return;//不加这句有些手机会报错
//                }
//                if (resultData.isSuccess()) {
//                    holdUsdt = resultData.getItem("holdUsdt", String.class);
//                    usdtRate = resultData.getItem("usdtRate", Double.class);
//                    tvBalanceText.setText("可用USDT:" + holdUsdt);
//                }
//            }
//        });
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (onShareDialogCallBack != null) {
                    onShareDialogCallBack.onDialogDismiss();
                }
                break;
            case R.id.tvSubmit:
                if (kbt == 0) {
                    CommonUtil.showmessage("请输入回购YYB数量", mActivity);
                    return;
                }
                if (kbt > Double.parseDouble(repoAmount)) {
                    CommonUtil.showmessage("回购数量不能大于剩余可回购YYB数量", mActivity);
                    return;
                }
//                if (!cbSign.isChecked()) {
//                    CommonUtil.showmessage("请先阅读《风险提示》和《跟单规则》", mActivity);
//                    return;
//                }
                colseKeybord();
                if (onShareDialogCallBack != null) {
                    onShareDialogCallBack.onSubmit(kbt);
                }

                break;
            case R.id.tvAll:
                etMoney.setText(holdAmount);
                etMoney.setSelection(etMoney.getText().length());
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


    public interface OnShareDialogCallBack {
        void onDialogDismiss();

        void onSubmit(double cash);
    }


    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
