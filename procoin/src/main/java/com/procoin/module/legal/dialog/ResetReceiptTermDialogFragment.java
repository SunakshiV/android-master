package com.procoin.module.legal.dialog;

import android.content.Context;
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

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.http.base.Group;
import com.procoin.module.home.OnItemClick;
import com.procoin.module.legal.adapter.ResetReceiptTermAdapter;
import com.procoin.module.myhome.entity.Receipt;
import com.procoin.util.CommonUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;

/**
 * 订单详情修改收款方式
 */

public class
ResetReceiptTermDialogFragment extends DialogFragment {

    @BindView(R.id.rvType)
    RecyclerView rvType;
    private ResetReceiptTermAdapter selectPayWayAdapter;
    private OnItemClick onItemClick;
    private Group<Receipt> receipts;

    /**
     * 非摘单 入参
     *
     * @return
     */
    public static ResetReceiptTermDialogFragment newInstance(Group<Receipt> receipts, OnItemClick onItemClick) {
        ResetReceiptTermDialogFragment dialog = new ResetReceiptTermDialogFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(CommonConst.RECHARGE, recharge);
//        dialog.setArguments(bundle);
        dialog.onItemClick = onItemClick;
        dialog.receipts = receipts;
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
        View v = inflater.inflate(R.layout.reset_receipt_term_dialog, container, false);
        ButterKnife.bind(this, v);

        selectPayWayAdapter = new ResetReceiptTermAdapter(getActivity());
        rvType.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvType.setAdapter(selectPayWayAdapter);
        selectPayWayAdapter.setOnItemClick(onItemClick);
        selectPayWayAdapter.setGroup(receipts);

        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}
