package com.procoin.module.kbt.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.GridLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.web.CommonWebViewActivity;
import com.procoin.module.kbt.adapter.RepoAmountAdapter;
import com.procoin.module.kbt.entity.KbtPool;
import com.procoin.util.CommonUtil;
import com.procoin.R;
import com.procoin.widgets.SimpleSpaceItemDecoration;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 回购
 */
public class KbtBuyBackDialogFragment extends DialogFragment implements View.OnClickListener {


    @BindView(R.id.tvSubmit)
    TextView tvSubmit;


    @BindView(R.id.tvTitle)
    TextView tvTitle;

    @BindView(R.id.tvRepoAmount)
    TextView tvRepoAmount;

    @BindView(R.id.tvHoldAmount)
    TextView tvHoldAmount;

    @BindView(R.id.tvLockGradleInfo)
    TextView tvLockGradleInfo;

    @BindView(R.id.rvRepoAmount)
    RecyclerView rvRepoAmount;

    @BindView(R.id.tvGrade)
    TextView tvGrade;

    @BindView(R.id.tvGradeHint)
    TextView tvGradeHint;


    RepoAmountAdapter repoAmountAdapter;


//    @BindView(R.id.etMoney)
//    EditText etMoney;
    //    @BindView(R.id.tvRepoAmountText)
//    TextView tvRepoAmountText;
    //    @BindView(R.id.tvAll)
//    TextView tvAll;
//    @BindView(R.id.tvSymbol)
//    TextView tvSymbol;


    private int decimalCount = 8;//小数点数量,金额小数点2位，数量小数点是4位.


    private TJRBaseToolBarActivity mActivity;

    private KbtPool kbtPool;
    //    private String repoAmount;//剩余可回购KBT数量
    private String holdAmount;//持有KBT数量

    private String myEquityLevel = "";//你目前锁仓权益等级V
    private String myEquityTip = "";
    private String subUrl;


    private OnShareDialogCallBack onShareDialogCallBack;

    private String amount = "0.00";

    public void setOnShareDialogCallBack(OnShareDialogCallBack onShareDialogCallBack) {
        this.onShareDialogCallBack = onShareDialogCallBack;
    }

    /**
     * @return
     */
    public static KbtBuyBackDialogFragment newInstance(KbtPool kbtPool, String holdAmount, String myEquityLevel, String myEquityTip, String subUrl) {
        KbtBuyBackDialogFragment dialog = new KbtBuyBackDialogFragment();
//        dialog.user = user;
        Bundle bundle = new Bundle();
        bundle.putString("holdAmount", holdAmount);
        bundle.putString("myEquityLevel", myEquityLevel);
        bundle.putString("myEquityTip", myEquityTip);
        bundle.putString("subUrl", subUrl);
        dialog.setArguments(bundle);
        dialog.kbtPool = kbtPool;
        return dialog;
    }


    public void updateUSDT(String holdAmount) {
        this.holdAmount = holdAmount;
        if (tvHoldAmount == null) return;
        tvHoldAmount.setText("持有USDT数量： " + holdAmount);
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
//        symbol = b.getString(CommonConst.SYMBOL, "");
//        repoAmount = b.getString("repoAmount", "0.00");
        holdAmount = b.getString("holdAmount", "0.00");

        myEquityLevel = b.getString("myEquityLevel", "");
        myEquityTip = b.getString("myEquityTip", "");
        subUrl = b.getString("subUrl", "");


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
        View v = inflater.inflate(R.layout.kbt_buy_back_dialog, container, false);

        ButterKnife.bind(this, v);
//        tvRepoAmountText.setText("剩余可认购" + symbol + "数量");
//        tvRepoAmount.setText("认购数量");


        repoAmountAdapter = new RepoAmountAdapter(getActivity());
        rvRepoAmount.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        rvRepoAmount.addItemDecoration(new SimpleSpaceItemDecoration(getActivity(), 0, 10, 5, 5));
        rvRepoAmount.setAdapter(repoAmountAdapter);
        repoAmountAdapter.setOnItemclickListen(new RepoAmountAdapter.onItemclickListen() {
            @Override
            public void onItemclick(String amountType) {
                amount = amountType;
                if (kbtPool != null) {
                    tvRepoAmount.setText("认购金额: " + getAmount(amountType, kbtPool.price));
                }
            }
        });

        tvHoldAmount.setText("持有USDT数量： " + holdAmount);
        if (!TextUtils.isEmpty(myEquityLevel)) {
            tvGrade.setVisibility(View.VISIBLE);
            tvGrade.setText(myEquityLevel);
        } else {
            tvGrade.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(myEquityTip)) {
            tvGradeHint.setVisibility(View.VISIBLE);
            tvGradeHint.setText(myEquityTip);
        } else {
            tvGradeHint.setVisibility(View.GONE);
        }

        if (kbtPool != null) {
            tvTitle.setText("选择认购" + kbtPool.symbol + "数量");
            if (kbtPool.amountList != null && kbtPool.amountList.length > 0) {
                repoAmountAdapter.setData(kbtPool.amountList);
                amount = kbtPool.amountList[0];
                repoAmountAdapter.setSelected(amount);
                tvRepoAmount.setText("认购金额: " + getAmount(kbtPool.amountList[0], kbtPool.price));
            }

        }

        tvSubmit.setOnClickListener(this);
        tvLockGradleInfo.setOnClickListener(this);
        return v;
    }

    private String getAmount(String amount, String price) {
        BigDecimal tolCnyBd = new BigDecimal(amount).multiply(new BigDecimal(price)).setScale(2, BigDecimal.ROUND_FLOOR);
        return tolCnyBd.toPlainString() + "USDT";
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
                if (Double.parseDouble(amount) == 0) {
//                    CommonUtil.showmessage("请输入认购" + symbol + "数量", mActivity);
                    return;
                }
//                if (kbt > Double.parseDouble(repoAmount)) {
//                    CommonUtil.showmessage("认购数量不能大于剩余可认购" + symbol + "数量", mActivity);
//                    return;
//                }
//                if (!cbSign.isChecked()) {
//                    CommonUtil.showmessage("请先阅读《风险提示》和《跟单规则》", mActivity);
//                    return;
//                }
                colseKeybord();
                if (onShareDialogCallBack != null) {
                    onShareDialogCallBack.onSubmit(amount);
                }

                break;
//            case R.id.tvAll:
//                etMoney.setText(repoAmount);
//                etMoney.setSelection(etMoney.getText().length());
//                break;
            case R.id.tvLockGradleInfo:
                if (!TextUtils.isEmpty(subUrl)) {
                    CommonWebViewActivity.pageJumpCommonWebViewActivity(getActivity(), subUrl);
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


    public interface OnShareDialogCallBack {
        void onDialogDismiss();

        void onSubmit(String amount);
    }


    private void colseKeybord() {
        Log.d("colseKeybord", "getDialog()==" + getDialog() + " getDialog().getCurrentFocus()==" + getDialog().getCurrentFocus() + " getDialog().getCurrentFocus().getWindowToken()==" + getDialog().getCurrentFocus().getWindowToken());
        if (getDialog() != null && getDialog().getCurrentFocus() != null && getDialog().getCurrentFocus().getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getDialog().getCurrentFocus().getWindowToken(), 0);
        }
    }
}
