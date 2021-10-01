package com.procoin.module.myhome.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;
import com.procoin.http.util.ParcelUtils;

/**
 * 我的地址
 */

public class MyAddress implements TaojinluType, Parcelable {


    public long addrId;
    public long createTime;
    public int isDefault;
    public String name;
    public String phone;
    public String province;
    public String city;
    public String street;
    public String addressCode;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        ParcelUtils.writeStringToParcel(out, name);
        ParcelUtils.writeStringToParcel(out, phone);
        ParcelUtils.writeStringToParcel(out, province);
        ParcelUtils.writeStringToParcel(out, city);
        ParcelUtils.writeStringToParcel(out, street);
        ParcelUtils.writeStringToParcel(out, addressCode);

        out.writeLong(addrId);
        out.writeLong(createTime);
        out.writeInt(isDefault);
    }

    public MyAddress() {
    }

    private MyAddress(Parcel in) {
        name = ParcelUtils.readStringFromParcel(in);
        phone = ParcelUtils.readStringFromParcel(in);
        province = ParcelUtils.readStringFromParcel(in);
        city = ParcelUtils.readStringFromParcel(in);
        street = ParcelUtils.readStringFromParcel(in);
        addressCode = ParcelUtils.readStringFromParcel(in);

        addrId = in.readLong();
        createTime = in.readLong();
        isDefault = in.readInt();
    }

    public static MyAddress.Creator<MyAddress> getCreator() {
        return CREATOR;
    }

    public static final MyAddress.Creator<MyAddress> CREATOR = new Parcelable.Creator<MyAddress>() {
        public MyAddress createFromParcel(Parcel in) {
            return new MyAddress(in);
        }

        @Override
        public MyAddress[] newArray(int size) {
            return new MyAddress[size];
        }
    };
}
