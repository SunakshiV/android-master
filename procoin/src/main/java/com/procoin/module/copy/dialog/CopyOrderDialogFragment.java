package com.procoin.module.copy.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
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
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


public class CopyOrderDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvAll)
    TextView tvAll;
    @BindView(R.id.tvBalanceText)
    TextView tvBalanceText;
    //    @BindView(R.id.cbSign)
//    CheckBox cbSign;
    @BindView(R.id.tvProtocol)
    TextView tvProtocol;
    @BindView(R.id.tvSubmit)
    TextView tvSubmit;

    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvRemark)
    TextView tvRemark;


    private int decimalCount = 4;//小数点数量,金额小数点2位，数量小数点是4位.
    private String holdUsdt = "0.00";//持有USDT
    private double usdtRate = 0.00;//usdt市价

    private String copyFeeTip = "";//备注

    private String minCopyBalance = "";//最小跟单金额（跟单）
    private String minCopyAppendBalance = "";//最小跟单金额（追加）


    private double cash;

    private TJRBaseToolBarActivity mActivity;


    private Call<ResponseBody> getTradeConfigCall;
    private Call<ResponseBody> copySlaveOrderCall;


    private OnShareDialogCallBack onShareDialogCallBack;

    private int type;//0跟单  1追加金额
    private long targetUid;//大V用户UID

    public void setOnShareDialogCallBack(OnShareDialogCallBack onShareDialogCallBack) {
        this.onShareDialogCallBack = onShareDialogCallBack;
    }

    /**
     * @return
     */
    public static CopyOrderDialogFragment newInstance(long copyUid, int type) {
        CopyOrderDialogFragment dialog = new CopyOrderDialogFragment();
//        dialog.user = user;
        Bundle bundle = new Bundle();
        bundle.putLong(CommonConst.TAUSERID, copyUid);
        bundle.putInt(CommonConst.KEY_EXTRAS_TYPE, type);
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
        type = b.getInt(CommonConst.KEY_EXTRAS_TYPE);
        targetUid = b.getLong(CommonConst.TAUSERID);
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
        View v = inflater.inflate(R.layout.copy_order_dialog, container, false);

        ButterKnife.bind(this, v);

        if (type == 0) {
            tvTitle.setText("跟单投入");
//            etMoney.setHint("请输入跟单金额");
            tvSubmit.setText("立即跟单");
        } else {
            tvTitle.setText("追加投入");
//            etMoney.setHint("请输入追加金额");
            tvSubmit.setText("确定");
        }

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
                cash = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    cash = Double.parseDouble(s.toString());
                } else {
                    cash = 0;
                }
            }
        });

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString normalText = new SpannableString("跟单即代表你同意并接受");
        normalText.setSpan(new ForegroundColorSpan(Color.parseColor("#bebebe")), 0, normalText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString clickText = new SpannableString("《风险提示》");
        clickText.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(mActivity, CommonConst.LIABILITY_PROTOCOL);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(mActivity, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(normalText);
        spannableStringBuilder.append(clickText);
        spannableStringBuilder.append("和");

        SpannableString clickText2 = new SpannableString("《跟单规则》");
        clickText2.setSpan(new ForegroundColorSpan(Color.parseColor("#f08c42")), 0, clickText2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        clickText2.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                CommonWebViewActivity.pageJumpCommonWebViewActivity(mActivity, CommonConst.FOLLOWRULE);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setColor(ContextCompat.getColor(mActivity, R.color.beebarBlue));
                ds.setUnderlineText(true);
            }
        }, 0, clickText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        spannableStringBuilder.append(clickText2);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
        tvProtocol.setText(spannableStringBuilder);
//        tvExpends.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvAll.setOnClickListener(this);
        startGetTradeConfig();

        return v;
    }


    private void startGetTradeConfig() {
        com.procoin.http.util.CommonUtil.cancelCall(getTradeConfigCall);
        getTradeConfigCall = VHttpServiceManager.getInstance().getVService().tradeConfig("",String.valueOf(targetUid));
        getTradeConfigCall.enqueue(new MyCallBack(mActivity) {
            @Override
            protected void callBack(ResultData resultData) {
                if (!isAdded()) {
                    return;//不加这句有些手机会报错
                }
                if (resultData.isSuccess()) {
                    holdUsdt = resultData.getItem("holdUsdt", String.class);
                    usdtRate = resultData.getItem("usdtRate", Double.class);
                    copyFeeTip = resultData.getItem("copyFeeTip", String.class);

                    minCopyBalance = resultData.getItem("minCopyBalance", String.class);
                    minCopyAppendBalance = resultData.getItem("minCopyAppendBalance", String.class);
                    tvBalanceText.setText("可用USDT:" + holdUsdt);
                    tvRemark.setText(copyFeeTip);

                    if (type == 0) {
                        etMoney.setHint("最小跟单金额: " + minCopyBalance);
                    } else {
                        etMoney.setHint("最小追加金额: " + minCopyAppendBalance);
                    }
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                if (onShareDialogCallBack != null) {
                    onShareDialogCallBack.onDialogDismiss();
                }

                break;
            case R.id.tvSubmit:
                if (cash == 0) {
                    CommonUtil.showmessage("请输入跟单金额", mActivity);
                    return;
                }
//                if (!cbSign.isChecked()) {
//                    CommonUtil.showmessage("请先阅读《风险提示》和《跟单规则》", mActivity);
//                    return;
//                }
                colseKeybord();
                if (onShareDialogCallBack != null) {
                    onShareDialogCallBack.onSubmit(cash);
                }

                break;
            case R.id.tvAll:
                etMoney.setText(holdUsdt);
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
