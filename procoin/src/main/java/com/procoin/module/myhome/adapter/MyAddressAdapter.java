//package com.cropyme.module.myhome.adapter;
//
//import android.content.Context;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import AMBaseAdapter;
//import com.cropyme.R;
//import CommonConst;
//import com.cropyme.module.myhome.AddOrEditAddressActivity;
//import com.cropyme.module.myhome.ManagerAddressActivity;
//import MyAddress;
//import PageJumpUtil;
//
//
///**
// *
// */
//
//public class MyAddressAdapter extends AMBaseAdapter<MyAddress> {
//    private Context context;
//
//    public MyAddressAdapter(Context c) {
//        this.context = c;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        Holder holder = null;
//        if (null == convertView) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.myaddress_item, parent, false);
//            holder = new Holder(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (Holder) convertView.getTag();
//        }
//        if (null != holder) holder.setView(position);
//
//        return convertView;
//    }
//
//    private class Holder {
//        private TextView tvAddress;
//        private TextView tvName;
//        private TextView tvPhone;
//
//        private TextView tvEdit;
//
//        private TextView tvDefault;
//
//        Holder(View v) {
//            tvAddress = (TextView) v.findViewById(R.id.tvAddress);
//            tvName = (TextView) v.findViewById(R.id.tvName);
//            tvPhone = (TextView) v.findViewById(R.id.tvPhone);
//
//            tvEdit = (TextView) v.findViewById(R.id.tvEdit);
//
//            tvDefault = (TextView) v.findViewById(R.id.tvDefault);
//        }
//
//        void setView(int pos) {
//            MyAddress e = getItem(pos);
//            if (null == e) return;
//            tvAddress.setText((TextUtils.isEmpty(e.province)?"":e.province) + e.city + e.street);
//            tvName.setText(e.name);
//            tvPhone.setText(e.phone);
//            Log.d("setView","is_default="+e.isDefault);
//            tvDefault.setVisibility(e.isDefault == 1 ? View.VISIBLE : View.GONE);
//            tvEdit.setOnClickListener(new MyOnclick(pos));
//
//        }
//
//
//        class MyOnclick implements View.OnClickListener {
//            MyAddress myAddress;
//            int pos;
//
//            public MyOnclick(int pos) {
//                this.pos = pos;
//                this.myAddress = getItem(pos);
//            }
//
//            @Override
//            public void onClick(View v) {
//                if (myAddress != null) {
//                    Bundle b = new Bundle();
//                    b.putParcelable(CommonConst.ADDRESS, myAddress);
//                    b.putInt(CommonConst.POS, pos);
//                    Intent intenet = new Intent();
//                    intenet.putExtras(b);
//                    PageJumpUtil.pageJumpResult((ManagerAddressActivity) context, AddOrEditAddressActivity.class, intenet, 0x789);
////                    PageJumpUtil.pageJumpToData(context, AddOrEditAddressActivity.class,b);
//                }
//
//            }
//        }
//    }
//}
