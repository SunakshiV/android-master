package com.procoin.module.legal;

import android.os.Bundle;
import androidx.appcompat.widget.LinearLayoutManager;
import androidx.appcompat.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.procoin.R;
import com.procoin.common.base.TJRBaseToolBarSwipeBackActivity;
import com.procoin.common.entity.ResultData;
import com.procoin.http.base.Group;
import com.procoin.http.tjrcpt.VHttpServiceManager;
import com.procoin.http.util.CommonUtil;
import com.procoin.module.legal.adapter.OptionalListAdapter;
import com.procoin.module.legal.entity.OptionalOrder;
import com.procoin.util.MyCallBack;
import com.procoin.util.PageJumpUtil;
import com.procoin.widgets.SimpleRecycleDivider;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 我的广告列表
 * <p>
 * Created by zhengmj on 18-10-10.
 */

public class MyAdListActivity extends TJRBaseToolBarSwipeBackActivity {

    @BindView(R.id.rvAdList)
    RecyclerView rvAdList;
    @BindView(R.id.tvAdd)
    TextView tvAdd;
    @BindView(R.id.llNodata)
    LinearLayout llNodata;

    private Call<ResponseBody> getOtcFindMyAdListCall;
    private OptionalListAdapter optionalListAdapter;

    @Override
    protected int setLayoutId() {
        return R.layout.my_ad_list;
    }

    @Override
    protected String getActivityTitle() {
        return "我的广告";
    }


//    public static void pageJump(Context context, int type) {
//        Bundle bundle = new Bundle();
//        bundle.putInt(CommonConst.JUMPTYPE, type);
////        bundle.putString(CommonConst.ENTRUSTAMOUNT, entrustAmount);
//        PageJumpUtil.pageJump(context, MyAdListActivity.class, bundle);
//
//
//    }


    @Override
    protected void onResume() {
        super.onResume();
        startGetReceipts();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.payment_trem);

        ButterKnife.bind(this);
        optionalListAdapter = new OptionalListAdapter(this, null, "", true);
        rvAdList.setLayoutManager(new LinearLayoutManager(this));
        rvAdList.addItemDecoration(new SimpleRecycleDivider(this));
        rvAdList.setAdapter(optionalListAdapter);

        tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PageJumpUtil.pageJump(MyAdListActivity.this, AddAdActivity.class);
            }
        });


    }

    private void startGetReceipts() {
        CommonUtil.cancelCall(getOtcFindMyAdListCall);
        getOtcFindMyAdListCall = VHttpServiceManager.getInstance().getVService().otcFindMyAdList();
        getOtcFindMyAdListCall.enqueue(new MyCallBack(this) {
            @Override
            protected void callBack(ResultData resultData) {
                if (resultData.isSuccess()) {
                    Group<OptionalOrder> groupMarket = resultData.getGroup("myAdList", new TypeToken<Group<OptionalOrder>>() {
                    }.getType());
                    if (groupMarket != null && groupMarket.size() > 0) {
                        rvAdList.setVisibility(View.VISIBLE);
                        llNodata.setVisibility(View.GONE);
                        optionalListAdapter.setGroup(groupMarket);
                    } else {
                        rvAdList.setVisibility(View.GONE);
                        llNodata.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }


}
