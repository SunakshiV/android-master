package com.procoin.module.legal.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.legal.adapter.SelectReceiptTermAdapter;
import com.procoin.module.myhome.AddReceiptTermActivity;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.CommonUtil;
import com.procoin.util.MyCallBack;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 选择收款方式
 */

public class SelectReceiptTermDialogFragment extends DialogFragment implements View.OnClickListener {

    @BindView(R.id.rvType)
    RecyclerView rvType;
    @BindView(R.id.llAdd)
    LinearLayout llAdd;
    private SelectReceiptTermAdapter selectPayWayAdapter;


    private OnItemClick onItemClick;
    private Call<ResponseBody> otcFindMyPaymentListCall;

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static SelectReceiptTermDialogFragment newInstance(OnItemClick onItemClick) {
        SelectReceiptTermDialogFragment dialog = new SelectReceiptTermDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(CommonConst.RECHARGE, recharge);
//        dialog.setArguments(bundle);
        dialog.onItemClick=onItemClick;
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
        View v = inflater.inflate(R.layout.select_receipt_term_dialog, container, false);
        ButterKnife.bind(this, v);

        Bundle bundle = getArguments();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.RECHARGE)) {
//                money = bundle.getInt(CommonConst.RECHARGE, 0);
            }
        }
        selectPayWayAdapter = new SelectReceiptTermAdapter(getActivity());
        rvType.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvType.setAdapter(selectPayWayAdapter);
        selectPayWayAdapter.setOnItemClick(onItemClick);

//        Group<Receipt> selectPayWayGroup = new Group<>();
//        Receipt selectPayWay = null;
//        for (int i = 0; i < 3; i++) {
//            selectPayWay = new Receipt();
//            selectPayWay.receiptTypeValue = "中国建设银行" + i;
//            selectPayWay.paymentId = i;
//            selectPayWayGroup.add(selectPayWay);
//        }

        llAdd.setOnClickListener(this);
        startOtcFindMyPaymentList();
        return v;
    }

    public void startOtcFindMyPaymentList() {
        com.procoin.http.util.CommonUtil.cancelCall(otcFindMyPaymentListCall);
        otcFindMyPaymentListCall = VHttpServiceManager.getInstance().getVService().otcFindMyPaymentList();
        otcFindMyPaymentListCall.enqueue(new MyCallBack(getActivity()) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<Receipt> groupMarket = resultData.getGroup("myPaymentList", new TypeToken<Group<Receipt>>() {
                    }.getType());
                    if (groupMarket != null && groupMarket.size() > 0) {
                        selectPayWayAdapter.setGroup(groupMarket);
                    }
                    llAdd.setVisibility((groupMarket != null && groupMarket.size() >=3)?View.GONE:View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

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
            case R.id.llAdd:
                AddReceiptTermActivity.pageJump(getActivity(),2);
                break;
            default:
                break;
        }
    }

}
