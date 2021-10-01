package com.procoin.module.home.trade.dialog;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.procoin.common.base.TJRBaseToolBarActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.home.adapter.SelectPayWayAdapter2;
import com.procoin.module.home.trade.entity.OtcEntity;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;
import com.procoin.R;
import com.procoin.http.base.TaojinluType;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.trade.OrderCashInfoActivity;
import com.procoin.module.myhome.entity.OrderCash;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 选择支付方式
 */

public class SelectPayWayDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.pb)
    ProgressBar pb;
    @BindView(R.id.tvReLoad)
    TextView tvReLoad;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private TJRBaseToolBarActivity mActivity;
    private Call<ResponseBody> getReceiptsCall;
    private Call<ResponseBody> tradeCashOrderBuyCall;

    private SelectPayWayAdapter2 selectPayWayAdapter;

    private String cny;
    private PayListen payListen;

    private OtcEntity otcEntity;

    public void setPayListen(PayListen payListen) {
        this.payListen = payListen;
    }

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static SelectPayWayDialogFragment newInstance(String cny, OtcEntity otcEntity) {
        SelectPayWayDialogFragment dialog = new SelectPayWayDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConst.RECHARGE, cny);
        dialog.setArguments(bundle);
        dialog.otcEntity = otcEntity;
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

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.RECHARGE)) {
                cny = bundle.getString(CommonConst.RECHARGE, "");
            }
        }

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
        View v = inflater.inflate(R.layout.select_pay_way_dialog, container, false);
        ButterKnife.bind(this, v);
        selectPayWayAdapter = new SelectPayWayAdapter2(getActivity());
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvList.addItemDecoration(new SimpleRecycleDivider(this, true));
        rvList.setAdapter(selectPayWayAdapter);
        selectPayWayAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onItemClickListen(int pos, TaojinluType t) {
                if (t == null) return;
                Receipt data = (Receipt) t;
                startTradeCashOrder(cny, data.paymentId);
            }
        });
        tvReLoad.setOnClickListener(this);
        if (otcEntity != null) {
            onLoadComplete();
            Log.d("SelectPayWayDialog","otcEntity.receiptList=="+otcEntity.receiptList);
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
                    selectPayWayAdapter.setGroup(otcEntity.receiptList);

                    Log.d("SelectPayWayDialog","getItemCount=="+selectPayWayAdapter.getItemCount());
//                }
//            }, 500);

        }
//        startGetReceipts();
        return v;
    }


//    private void startGetReceipts() {
//        CommonUtil.cancelCall(getReceiptsCall);
//        onLoading();
//        getReceiptsCall = VHttpServiceManager.getInstance().getVService().receiptsForPay();
//        getReceiptsCall.enqueue(new MyCallBack(getActivity()) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                onLoadComplete();
//                if (resultData.isSuccess()) {
//                    Group<Receipt> group = resultData.getGroup("receiptList", new TypeToken<Group<Receipt>>() {
//                    }.getType());
//                    if (group != null && group.size() > 0) {
//                        selectPayWayAdapter.setGroup(group);
//                    }
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                onLoadFail();
//            }
//        });
//    }

    private void startTradeCashOrder(String cny, long receiptId) {
        com.procoin.http.util.CommonUtil.cancelCall(tradeCashOrderBuyCall);
        mActivity.showProgressDialog();
        tradeCashOrderBuyCall = VHttpServiceManager.getInstance().getVService().tradeCashOrderBuy(cny, receiptId);
        tradeCashOrderBuyCall.enqueue(new MyCallBack(mActivity) {
            @Override
            protected void callBack(ResultData resultData) {
                mActivity.dismissProgressDialog();
                if (resultData.isSuccess()) {
                    com.procoin.http.util.CommonUtil.showmessage(resultData.msg, mActivity);
                    OrderCash orderCash = resultData.getObject("order", OrderCash.class);
                    if (orderCash != null) {
                        if (payListen != null) payListen.onPaySuccess();
                        OrderCashInfoActivity.pageJump(mActivity, orderCash.orderCashId);
                    }
                }

            }

            @Override
            protected void handleError(Call<ResponseBody> call) {
                super.handleError(call);
                mActivity.dismissProgressDialog();
            }
        });
    }


    private void onLoading() {
        tvReLoad.setVisibility(View.GONE);
        rvList.setVisibility(View.GONE);
        pb.setVisibility(View.VISIBLE);

    }

    private void onLoadFail() {
        tvReLoad.setVisibility(View.VISIBLE);
        rvList.setVisibility(View.GONE);
        pb.setVisibility(View.GONE);
    }

    private void onLoadComplete() {
        tvReLoad.setVisibility(View.GONE);
        rvList.setVisibility(View.VISIBLE);
        pb.setVisibility(View.GONE);
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
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvReLoad:
//                startGetReceipts();
                break;
            default:
                break;
        }
    }


    public interface PayListen {
        void onPaySuccess();
    }

}
