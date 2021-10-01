//package com.cropyme.module.myhome;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.google.gson.reflect.TypeToken;
//import Group;
////import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//import com.cropyme.R;
//import TJRBaseToolBarSwipeBackActivity;
//import CommonConst;
//import ResultData;
//import com.cropyme.module.myhome.adapter.MyAddressAdapter;
//
//import MyAddress;
//import CommonUtil;
//import MyCallBack;
//import PageJumpUtil;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//
//
//public class ManagerAddressActivity extends TJRBaseToolBarSwipeBackActivity {
//
//    private ListView lvAddress;
//    private TextView tvAddAddress;
//    private MyAddressAdapter myAddressAdapter;
//    private LinearLayout llNoData;
//    private TextView tvNoData;
//
//    private int type;//type==0,管理收货地址  1 是选择收货地址
//
//    private long addr_id;//上个页面已经选中的地址
//    private boolean isDel;//用于上个页面已经选中的地址,这个页面被删除了,返回时应该清除
//
//
//    private Call<ResponseBody> addressListCall;
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.manager_adress;
//    }
//
//    @Override
//    protected String getActivityTitle() {
//        return getString(R.string.manage_delivery_address);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getIntent().getExtras() != null) {
//            type = getIntent().getExtras().getInt(CommonConst.KEY_EXTRAS_TYPE);
//            addr_id = getIntent().getExtras().getLong(CommonConst.ADDRID);
//        }
////        setContentView(R.layout.manager_adress);
//        mActionBar.setTitle(type == 0 ? "管理收货地址" : "选择收货地址");
//        lvAddress = (ListView) findViewById(R.id.lvAddress);
//        myAddressAdapter = new MyAddressAdapter(this);
//        lvAddress.setAdapter(myAddressAdapter);
//
//        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (type == 0) return;
//                MyAddress myAddress = (MyAddress) parent.getItemAtPosition(position);
//                if (myAddress != null) {
//                    isDel = false;//如果点击了item项,那就不管他有没有删除了
//                    Intent intent = new Intent();
//                    Bundle bundle = new Bundle();
//                    bundle.putParcelable(CommonConst.ADDRESS, myAddress);
//                    intent.putExtras(bundle);
//                    setResult(0x123, intent);
//                    PageJumpUtil.finishCurr(ManagerAddressActivity.this);
//                }
//            }
//        });
//
//        tvAddAddress = (TextView) findViewById(R.id.tvAddAddress);
//        tvAddAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switch (v.getId()) {
//                    case R.id.tvAddAddress:
////                        PageJumpUtil.pageJump(ManagerAddressActivity.this, AddOrEditAddressActivity.class);
//                        PageJumpUtil.pageJumpResult(ManagerAddressActivity.this, AddOrEditAddressActivity.class, new Intent(), 0x123);
//
//                        break;
//
//                    default:
//                        break;
//                }
//            }
//        });
//
//
//        llNoData = (LinearLayout) findViewById(R.id.llNoData);
//        tvNoData = (TextView) findViewById(R.id.tvNoData);
//        tvNoData.setText("暂无添加地址");
//        startGetAllAddressTask();
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == 0x123 && resultCode == 0x123) {
//            Log.d("onActivityResult", "添加地址");
//            startGetAllAddressTask();
//        } else if (requestCode == 0x789) {
//            if (resultCode == 0x789) {//更新
//                if (data != null && data.getParcelableExtra("address") != null) {
//                    MyAddress address = data.getParcelableExtra("address");
//                    Log.d("onActivityResult", "更新地址");
//                    if (myAddressAdapter.getCount() > 0) {
//                        boolean ret = false;
//                        for (MyAddress myAddress : myAddressAdapter.getGroup()) {
//                            if (address.addrId == myAddress.addrId) {
//                                myAddress.name = address.name;
//                                myAddress.phone = address.phone;
//                                myAddress.province = address.province;
//                                myAddress.city = address.city;
//                                myAddress.street = address.street;
//                                myAddress.isDefault = address.isDefault;
//                                ret = true;
//                                if (address.isDefault == 0) break;
//                            } else {
//                                if (address.isDefault == 1) {
//                                    myAddress.isDefault = 0;
//                                }
//
//                            }
//                        }
//                        if (ret) myAddressAdapter.notifyDataSetChanged();
//                    }
//                } else {
//                    startGetAllAddressTask();
//                }
//
//            } else if (resultCode == 0x456) {//删除
//                if (data != null) {
//                    Log.d("onActivityResult", "删除地址 addr_id==" + addr_id);
//                    int pos = data.getIntExtra(CommonConst.POS, -1);
//                    if (pos >= 0) {
//                        if (myAddressAdapter.getItem(pos).addrId == addr_id) isDel = true;
//                        myAddressAdapter.removeItem(pos);
//                        setNoData();
//                    } else {
//                        startGetAllAddressTask();
//                    }
//
//                }
//            } else {
////                startGetAllAddressTask();
//            }
//
//        }
//    }
//
//    @Override
//    public void finish() {
//        if (isDel) setResult(0x456);
//        super.finish();
//    }
//
//    private void startGetAllAddressTask() {
//        showProgressDialog();
//        CommonUtil.cancelCall(addressListCall);
//        addressListCall = RedzHttpServiceManager.getInstance().getRedzService().getUserAddressList();
//        addressListCall.enqueue(new MyCallBack(ManagerAddressActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                dismissProgressDialog();
//                if (resultData.isSuccess()) {
//                    Group<MyAddress> group=resultData.getGroup(new TypeToken<Group<MyAddress>>() {}.getType());
//                    myAddressAdapter.setGroup(group);
//                    setNoData();
//                }
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                dismissProgressDialog();
//            }
//        });
//    }
//
//    private void setNoData() {
//        if (myAddressAdapter != null && myAddressAdapter.getCount() > 0) {
//            lvAddress.setVisibility(View.VISIBLE);
//            llNoData.setVisibility(View.GONE);
//        } else {
//            lvAddress.setVisibility(View.GONE);
//            llNoData.setVisibility(View.VISIBLE);
//        }
//    }
//
//}
