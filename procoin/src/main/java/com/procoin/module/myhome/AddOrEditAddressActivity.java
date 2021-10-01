//package com.cropyme.module.myhome;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.TextView;
//
////import com.cropyme.http.tjrcpt.RedzHttpServiceManager;
//import VHttpServiceManager;
//import NotificationsUtil;
//import com.cropyme.http.widget.dialog.ui.TjrBaseDialog;
//import BaseAsyncTask;
//import com.cropyme.R;
//import TJRBaseToolBarSwipeBackActivity;
//import CommonConst;
//import ResultData;
//import com.cropyme.module.home.dialog.CitySelectDialogFragment;
//import MyAddress;
//import CommonUtil;
//import MyCallBack;
//import PageJumpUtil;
//
//import okhttp3.ResponseBody;
//import retrofit2.Call;
//
///**
// * Created by zhengmj on 18-3-15.
// */
//public class AddOrEditAddressActivity extends TJRBaseToolBarSwipeBackActivity implements View.OnClickListener {
//
//    private EditText etName;
//    private EditText etPhone;
//    private EditText etDetailAddress;
//    private CheckBox cbIsDefault;
//
//    private TextView tvCity;
//
//    private TextView tvAdd;
//    private TextView tvEditSave;
//    private TextView tvDelete;
//
//    //    private PopupWindow citypw;
//    private CitySelectDialogFragment citySelectDialogFragment;
//
//
//    private Call<ResponseBody> addRessCall;
//    private Call<ResponseBody> deleteAddressCall;
//    private Call<ResponseBody> updateAddressCall;
//
//    private TjrBaseDialog delDialog;
//
//    private MyAddress address;//address不为空就是编辑,否则就是添加
//    private int pos;
//
//
//    private String province;
//    private String city;
//
//    @Override
//    protected int setLayoutId() {
//        return R.layout.add_address;
//    }
//
//    @Override
//    protected String getActivityTitle() {
//        return getString(R.string.add_delivery_address);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getIntent().getExtras() != null) {
//            address = getIntent().getExtras().getParcelable("address");
//            pos = getIntent().getExtras().getInt(CommonConst.POS);
//        }
//        if (savedInstanceState != null && address == null) {
//            address = savedInstanceState.getParcelable("address");
//            pos = savedInstanceState.getInt(CommonConst.POS);
//        }
////        setContentView(R.layout.add_address);
//        etName = (EditText) findViewById(R.id.etName);
//        etPhone = (EditText) findViewById(R.id.etPhone);
//        etDetailAddress = (EditText) findViewById(R.id.etDetailAddress);
//
//        cbIsDefault = (CheckBox) findViewById(R.id.cbIsDefault);
//
//        tvCity = (TextView) findViewById(R.id.tvCity);
//        tvAdd = (TextView) findViewById(R.id.tvAdd);
//        tvEditSave = (TextView) findViewById(R.id.tvEditSave);
//
//        tvDelete = (TextView) findViewById(R.id.tvDelete);
//
//        tvCity.setOnClickListener(this);
//        tvAdd.setOnClickListener(this);
//        tvDelete.setOnClickListener(this);
//        tvEditSave.setOnClickListener(this);
//
//        if (address != null) {
//            etName.setText(address.name);
//            etPhone.setText(address.phone);
//            tvCity.setText((TextUtils.isEmpty(address.province) ? "" : address.province) + address.city);
//            etDetailAddress.setText(address.street);
//            mActionBar.setTitle(getString(R.string.edit_address));
//            cbIsDefault.setChecked(address.isDefault == 1);
//
//            province = address.province;
//            city = address.city;
//
//            tvDelete.setVisibility(View.VISIBLE);
//            tvEditSave.setVisibility(View.VISIBLE);
//            tvAdd.setVisibility(View.GONE);
//
//        } else {
//            mActionBar.setTitle(getString(R.string.add_delivery_address));
//            tvDelete.setVisibility(View.GONE);
//            tvEditSave.setVisibility(View.GONE);
//            tvAdd.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        if (address != null) {
//            outState.putParcelable(CommonConst.ADDRESS, address);
//            outState.putInt(CommonConst.POS, pos);
//        }
//        super.onSaveInstanceState(outState, outPersistentState);
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.tvCity:
//                showCityPicker();
//                break;
//            case R.id.tvAdd:
//                invalidateAndTask(0);
//                break;
//            case R.id.tvDelete:
//                showDelDialog();
//                break;
//            case R.id.tvEditSave:
//                invalidateAndTask(1);
//                break;
//        }
//    }
//
//    private void showDelDialog() {
//        if (delDialog == null) {
//            delDialog = new TjrBaseDialog(this) {
//                @Override
//                public void setDownProgress(int progress) {
//                }
//
//                @Override
//                public void onclickOk() {
//                    delDialog.dismiss();
//                    if (address != null) startdelAddressTask(address.addrId);
//                }
//
//                @Override
//                public void onclickClose() {
//                    delDialog.dismiss();
//                }
//            };
//            delDialog.setTvTitle("提示");
//            delDialog.setMessage("是否删除该地址?");
//            delDialog.setBtnColseText("取消");
//            delDialog.setBtnOkText("删除");
//        }
//        if (!isFinishing() && !delDialog.isShowing()) delDialog.show();
//    }
//
//    /**
//     * @param type 0添加  1更新
//     * @return
//     */
//    private void invalidateAndTask(int type) {
//        String name = etName.getText().toString().trim();
//        if (TextUtils.isEmpty(name)) {
//            CommonUtil.showmessage("请输入收件人姓名", AddOrEditAddressActivity.this);
//            return;
//        }
//        if (name.length() < 2) {
//            CommonUtil.showmessage("收件人姓名最少2位", AddOrEditAddressActivity.this);
//            return;
//        }
//
//        if (name.length() > 10) {
//            CommonUtil.showmessage("收件人姓名太长", AddOrEditAddressActivity.this);
//            return;
//        }
//
//        String phone = etPhone.getText().toString().trim();
//        if (TextUtils.isEmpty(phone)) {
//            CommonUtil.showmessage("请输入收件人电话", AddOrEditAddressActivity.this);
//            return;
//        }
//        if (!phone.matches("^(1)\\d{10}$")) {
//            CommonUtil.showmessage("请输入正确手机号码!", this);
//            return;
//        }
//        String citytext = tvCity.getText().toString().trim();
//        if (TextUtils.isEmpty(citytext)) {
//            CommonUtil.showmessage("请选择省市", AddOrEditAddressActivity.this);
//            return;
//        }
//
//        String detailAddress = etDetailAddress.getText().toString().trim();
//        if (TextUtils.isEmpty(detailAddress)) {
//            CommonUtil.showmessage("请输入详细地址", AddOrEditAddressActivity.this);
//            return;
//        }
//        if (detailAddress.length() < 5) {
//            CommonUtil.showmessage("详细地址长度不能低于5位", AddOrEditAddressActivity.this);
//            return;
//        }
//        if (detailAddress.length() > 60) {
//            CommonUtil.showmessage("详细地址长度不能超过60位", AddOrEditAddressActivity.this);
//            return;
//        }
//        if (type == 0) {
//            startAddAddrTask(name, phone, province, city, detailAddress, cbIsDefault.isChecked() ? 1 : 0, "");
//        } else {
//            if (address != null)
//                startEditAddressTask(address.addrId, name, phone, province, city, detailAddress, cbIsDefault.isChecked() ? 1 : 0, "");
//        }
//
//    }
//
//
//    private void showCityPicker() {
//        if (citySelectDialogFragment == null) {
//            citySelectDialogFragment = CitySelectDialogFragment.newInstance(new CitySelectDialogFragment.OnButtonClickListener() {
//                @Override
//                public void onSureClick(String province, String city) {
//                    AddOrEditAddressActivity.this.province=province;
//                    AddOrEditAddressActivity.this.city=city;
//                    tvCity.setText(province + city);
//                    etDetailAddress.setText("");
//                }
//
//            });
//        }
//        citySelectDialogFragment.show(getSupportFragmentManager(), "");
//
//    }
//
////    public void showCityPop(View v) {
////        ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(AddOrEditAddressActivity.this.getCurrentFocus().getWindowToken(), 0);
////        if (citypw == null) {
////            View view = InflaterUtils.inflateView(this, R.layout.city_setting_layout);
////            final WheelView wv_sheng = (WheelView) view.findViewById(R.id.sheng);
////            wv_sheng.setAdapter(new CityWheelAdapter(CityUtils.ARR_SHENG));// 设置"省份"的显示数据
////            wv_sheng.setCyclic(true);// 可循环滚动
////            wv_sheng.setCurrentItem(0);//
////
////            final WheelView wv_city = (WheelView) view.findViewById(R.id.city);
////            wv_city.setAdapter(new CityWheelAdapter(CityUtils.ARR_CITY[0]));
////            wv_city.setCyclic(true);
////            wv_city.setCurrentItem(0);
////
////            OnWheelChangedListener wheelListener_sheng = new OnWheelChangedListener() {
////                public void onChanged(WheelView wheel, int oldValue, final int newValue) {
////                    wv_city.postDelayed(new Runnable() {
////
////                        @Override
////                        public void run() {
////                            wv_city.setAdapter(new CityWheelAdapter(CityUtils.ARR_CITY[newValue]));
////                            wv_city.setCurrentItem(0);
////                        }
////                    }, 200);
////
////                }
////            };
////            wv_sheng.addChangingListener(wheelListener_sheng);
////
////            int textSize = 20;
////            textSize = DensityUtil.dip2px(this, textSize);
////            wv_sheng.TEXT_SIZE = textSize;
////            wv_city.TEXT_SIZE = textSize;
////
////            Button btn_sure = (Button) view.findViewById(R.id.btn_datetime_sure);
////            Button btn_cancel = (Button) view.findViewById(R.id.btn_datetime_cancel);
////            btn_sure.setOnClickListener(new View.OnClickListener() {
////
////                @Override
////                public void onClick(View arg0) {
////                    city = String.valueOf(wv_city.getAdapter().getItem(wv_city.getCurrentItem()));
////                    province = wv_sheng.getAdapter().getItem(wv_sheng.getCurrentItem());
////                    if (province == null || "其他".equals(province)) {
////                        province = "";
////                    }
////                    tvCity.setText(province + city);
////                    etDetailAddress.setText("");
////                    citypw.dismiss();
////                }
////            });
////            // 取消
////            btn_cancel.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View arg0) {
////                    citypw.dismiss();
////                }
////            });
////            citypw = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
////            citypw.setOutsideTouchable(true);
////            citypw.setBackgroundDrawable(new ColorDrawable());// 特别留意这个东东
////            citypw.setFocusable(true);
////            citypw.setAnimationStyle(R.style.datePickerPop);
////        }
////        citypw.showAtLocation(v, Gravity.BOTTOM, 0, 0);
////    }
//
//    private void startAddAddrTask(String name, String phone, String province, String city, String street, int is_default, String addressCode) {
//        showProgressDialog();
//        addRessCall = VHttpServiceManager.getInstance().getVService().addAddress(name, phone, province, city, street, is_default, addressCode);
//        addRessCall.enqueue(new MyCallBack(AddOrEditAddressActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                if (resultData.isSuccess()) {
//                    dismissProgressDialog();
//                    if (!TextUtils.isEmpty(resultData.msg))
//                        CommonUtil.showmessage(resultData.msg, AddOrEditAddressActivity.this);
//                    setResult(0x123);
//                    PageJumpUtil.finishCurr(AddOrEditAddressActivity.this);
//                }
//
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
//
//    private void startEditAddressTask(long addr_id, final String name, final String phone, final String province, final String city, final String street, final int is_default, final String addressCode) {
//        showProgressDialog();
//        CommonUtil.cancelCall(updateAddressCall);
//        updateAddressCall = VHttpServiceManager.getInstance().getVService().updateAddress(addr_id, name, phone, province, city, street, is_default, addressCode);
//        updateAddressCall.enqueue(new MyCallBack(AddOrEditAddressActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                dismissProgressDialog();
//                if (resultData.isSuccess()) {
//                    if (!TextUtils.isEmpty(resultData.msg))
//                        CommonUtil.showmessage(resultData.msg, AddOrEditAddressActivity.this);
//                    Intent intent = new Intent();
//                    if (address != null) {
//                        Bundle bundle = new Bundle();
//                        address.name = name;
//                        address.phone = phone;
//                        address.province = province;
//                        address.city = city;
//                        address.street = street;
//                        address.isDefault = is_default;
//                        address.addressCode = addressCode;
//                        bundle.putParcelable("address", address);
//                        intent.putExtras(bundle);
//                    }
//                    setResult(0x789, intent);
//                    PageJumpUtil.finishCurr(AddOrEditAddressActivity.this);
//                }
//
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
//    class EditAddressTask extends BaseAsyncTask<Void, Void, Boolean> {
//
//        long addr_id;
//        String name;
//        String phone;
//        String province;
//        String city;
//        String street;
//        int is_default;
//
//        Exception exception;
//        String msg;
//
//        public EditAddressTask(long addr_id, String name, String phone, String province, String city, String street, int is_default) {
//            this.addr_id = addr_id;
//            this.name = name;
//            this.phone = phone;
//            this.province = province;
//            this.city = city;
//            this.street = street;
//            this.is_default = is_default;
//
//        }
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//            showProgressDialog();
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            try {
////                String result = PervalHttp.getInstance().updateAddr(getUserId(), addr_id, name, phone, province, city, street, is_default);
////                Log.d("result", "result==" + result);
////                if (!TextUtils.isEmpty(result)) {
////                    boolean ret = false;
////                    JSONObject jsonObject = new JSONObject(result);
////                    if (JsonParserUtils.hasAndNotNull(jsonObject, "success")) {
////                        if (ret = jsonObject.getBoolean("success")) {
////                            msg = jsonObject.getString("msg");
////                        }
////
////                    }
////
////                    return ret;
////                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return false;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean aBoolean) {
//            super.onPostExecute(aBoolean);
//            dismissProgressDialog();
//            if (aBoolean) {
//                if (!TextUtils.isEmpty(msg))
//                    CommonUtil.showmessage(msg, AddOrEditAddressActivity.this);
//                Intent intent = new Intent();
//                if (address != null) {
//                    Bundle bundle = new Bundle();
//                    address.name = name;
//                    address.phone = phone;
//                    address.province = province;
//                    address.city = city;
//                    address.street = street;
//                    address.isDefault = is_default;
//                    bundle.putParcelable("address", address);
//                    intent.putExtras(bundle);
//                }
//                setResult(0x789, intent);
//                PageJumpUtil.finishCurr(AddOrEditAddressActivity.this);
//            } else {
//                if (exception != null) {
//                    NotificationsUtil.ToastReasonForFailure(AddOrEditAddressActivity.this, exception);
//                } else {
//                    if (!TextUtils.isEmpty(msg))
//                        CommonUtil.showmessage(msg, AddOrEditAddressActivity.this);
//                }
//            }
//        }
//    }
//
//
//    private void startdelAddressTask(long addr_id) {
//        showProgressDialog();
//        CommonUtil.cancelCall(deleteAddressCall);
//        deleteAddressCall = RedzHttpServiceManager.getInstance().getRedzService().deleteAddress(addr_id);
//        deleteAddressCall.enqueue(new MyCallBack(AddOrEditAddressActivity.this) {
//            @Override
//            protected void callBack(ResultData resultData) {
//                dismissProgressDialog();
//                if (resultData.isSuccess()) {
//                    if (!TextUtils.isEmpty(resultData.msg))
//                        CommonUtil.showmessage(resultData.msg, AddOrEditAddressActivity.this);
//                    Intent intent = new Intent();
//                    intent.putExtra(CommonConst.POS, pos);
//                    setResult(0x456, intent);
//                    PageJumpUtil.finishCurr(AddOrEditAddressActivity.this);
//                }
//
//            }
//
//            @Override
//            protected void handleError(Call<ResponseBody> call) {
//                super.handleError(call);
//                dismissProgressDialog();
//            }
//        });
//
//    }
//
//}