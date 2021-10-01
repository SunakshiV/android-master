package com.procoin.module.myhome;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;

import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.constant.CommonConst;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.myhome.adapter.AddReceiptAdapter;
import com.procoin.module.myhome.entity.AddPaymentTern;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 增加账户类型
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class AddReceiptTermActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.rv_list)
    RecyclerView rv_list;

    private Call<ResponseBody> getPaymentOptionListCall;
    private AddReceiptAdapter addReceiptAdapter;
    private int from;//0 收款管理过来 1 添加广告过来 2快捷购买那里

    @Override
    protected int setLayoutId() {
        return R.layout.simple_recycleview_2;
    }

    @Override
    protected String getActivityTitle() {
        return "添加账户类型";
    }


    public static void pageJump(Context context, int from) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConst.JUMPTYPE, from);
        PageJumpUtil.pageJump(context, AddReceiptTermActivity.class, bundle);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(CommonConst.JUMPTYPE)) {
                from = bundle.getInt(CommonConst.JUMPTYPE, 0);
//                entrustAmount = bundle.getString(CommonConst.ENTRUSTAMOUNT, "0.00");
            }
        }

//        setContentView(R.layout.payment_trem);
        ButterKnife.bind(this);
        addReceiptAdapter = new AddReceiptAdapter(this,from);
        rv_list.setLayoutManager(new LinearLayoutManager(this));
//        rv_list.addItemDecoration(new SimpleRecycleDivider(this, true));
        rv_list.setAdapter(addReceiptAdapter);

//        Group<AddPaymentTern> selectPayWayGroup = new Group<>();
//        AddPaymentTern selectPayWay = null;
//        for (int i = 0; i < 4; i++) {
//            selectPayWay = new AddPaymentTern();
//            selectPayWay.receiptTypeValue = "中国建设银行" + i;
//
//            selectPayWay.receiptType=i;
//            selectPayWayGroup.add(selectPayWay);
//        }
//        addReceiptAdapter.setGroup(selectPayWayGroup);
        startOtcFindPaymentOptionList();

    }

    private void startOtcFindPaymentOptionList() {
        CommonUtil.cancelCall(getPaymentOptionListCall);
        getPaymentOptionListCall = VHttpServiceManager.getInstance().getVService().otcFindPaymentOptionList();
        getPaymentOptionListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<AddPaymentTern> group = resultData.getGroup("paymentOptionList", new TypeToken<Group<AddPaymentTern>>() {
                    }.getType());
                    if (group != null && group.size() > 0) {
                        addReceiptAdapter.setGroup(group);
                    }
                }
            }
        });
    }

}
