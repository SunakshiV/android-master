package com.procoin.module.myhome.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
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
import com.procoin.common.entity.ResultData;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.util.CommonUtil;
import com.procoin.util.DateUtils;
import com.procoin.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 订阅/续费
 */

public class AttentionDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvSubTitle)
    TextView tvSubTitle;
    @BindView(R.id.etBalance)
    EditText etBalance;
    @BindView(R.id.tvUnit)
    TextView tvUnit;
    @BindView(R.id.tvPrice)
    TextView tvPrice;
    @BindView(R.id.tvAmount)
    TextView tvAmount;
    @BindView(R.id.tvTips)
    TextView tvTips;
    @BindView(R.id.tvTolMoney)
    TextView tvTolMoney;
    @BindView(R.id.tvBuy)
    TextView tvBuy;
    private TJRBaseToolBarActivity mActivity;
    private Call<ResponseBody> outHoldAmountCall;

    private AttentionListen attentionListen;


    private String subFeeTypeName;
    private String subFeeTypeUnit;
    private String subNotice;
    private double subFee;

    private long expireTime;//过期时间戳
    private int isExpireTime;//是否订阅过期 0没 1过期
    private int myIsAttention;

    private int num;


    public void setAttentionListen(AttentionListen attentionListen) {
        this.attentionListen = attentionListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static AttentionDialogFragment newInstance(String subFeeTypeName, String subFeeTypeUnit, String subNotice, double subFee, int myIsAttention, int isExpireTime, long expireTime) {
        AttentionDialogFragment dialog = new AttentionDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("subFeeTypeName", subFeeTypeName);
        bundle.putString("subFeeTypeUnit", subFeeTypeUnit);
        bundle.putString("subNotice", subNotice);
        bundle.putDouble("subFee", subFee);
        bundle.putInt("myIsAttention", myIsAttention);
        bundle.putInt("isExpireTime", isExpireTime);
        bundle.putLong("expireTime", expireTime);
        dialog.setArguments(bundle);
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
        View v = inflater.inflate(R.layout.attention, container, false);
        ButterKnife.bind(this, v);
        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey("subFeeTypeName")) {
                subFeeTypeName = bundle.getString("subFeeTypeName", "");
                subFeeTypeUnit = bundle.getString("subFeeTypeUnit", "");
                subNotice = bundle.getString("subNotice", "");
                subFee = bundle.getDouble("subFee", 0.0);
                expireTime = bundle.getLong("expireTime", 0);
                isExpireTime = bundle.getInt("isExpireTime", 0);
                myIsAttention = bundle.getInt("myIsAttention", 0);

            }
        }
        if (myIsAttention == 0) {
            tvTitle.setText("订阅");
            tvBuy.setText("订阅");
        } else {
            tvTitle.setText("续费");
            tvBuy.setText("续费");
            if (isExpireTime == 1 && expireTime > 0) {//底部文字：订阅过期
                tvSubTitle.setText("订阅已过期");
                tvSubTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.red));
            } else if (isExpireTime == 0 && expireTime > 0) {//底部文字：到期：2020-12-12
                tvSubTitle.setText(DateUtils.getStringDateOfString2(String.valueOf(expireTime), DateUtils.TEMPLATE_yyyyMMdd_divide) + "到期,续费后有效期延长");
                tvSubTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.c1d3155));
            }
        }

        tvUnit.setText(subFeeTypeUnit);
        tvPrice.setText(subFeeTypeName);
        tvTips.setText(subNotice);

        tvBuy.setOnClickListener(this);

        etBalance.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                num = 0;
                if (!TextUtils.isEmpty(s.toString())) {
                    num = Integer.parseInt(s.toString());
                }
                tvTolMoney.setText("合计：" + num * subFee + " USDT");


            }
        });
        startOutHoldAmountCall();
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void startOutHoldAmountCall() {
        CommonUtil.cancelCall(outHoldAmountCall);
        outHoldAmountCall = VHttpServiceManager.getInstance().getVService().outHoldAmount("balance");
        outHoldAmountCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    String holdAmount = resultData.getItem("holdAmount", String.class);
                    tvAmount.setText("余额账户可用: " + holdAmount + " USDT");
                }
            }
        });
    }


    private void setAmount() {

    }

    @Override
    public void onResume() {
        super.onResume();
        CommonUtil.LogLa(2, "OLStarHomeBuyFragment--->            onResume   isVisible = " + getUserVisibleHint());
//        if(etBalance!=null){
//            etBalance.post(new Runnable(){
//                @Override
//                public void run()
//                {
//                    etBalance.requestFocus();
//                    InputMethodManager imm =
//                            (InputMethodManager)etBalance.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                    if (imm != null)
//                        imm.showSoftInput(etBalance, InputMethodManager.SHOW_IMPLICIT);
//                }
//            });
//        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {

//        InputMethodManager imm =
//                (InputMethodManager)etBalance.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//        Log.d("onDismiss","imm.isActive()=="+imm.isActive()+"  imm.isActive(etBalance)=="+imm.isActive(etBalance));
//        if (imm.isActive())
//            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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

            case R.id.tvBuy:
                colseKeybord();
                if (attentionListen != null) {
                    attentionListen.addAttention(String.valueOf(num));
                }
                break;
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

    public interface AttentionListen {
        void addAttention(String num);
    }
}
