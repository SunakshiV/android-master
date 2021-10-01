package com.procoin.module.home.trade.entity;

/**
 * ImageGroup.java
 * ImageChooser
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;
import com.procoin.http.util.ParcelUtils;
import com.procoin.module.myhome.entity.Receipt;

/**
 *
 */
public class TakeCoinHistory implements TaojinluType,Parcelable {

    public String address;
    public String amount;
    public String createTime;
    public String fee;

    public String stateDesc;
    public String symbol;
    public String chainType;
    public String realAmount;

    public int inOut;//1充币 -1提币
    public int state;
    public long userId;
    public long dwId;


    public TakeCoinHistory(){}

    public static final Creator<TakeCoinHistory> CREATOR = new Creator<TakeCoinHistory>() {
        @Override
        public TakeCoinHistory createFromParcel(Parcel in) {
            return new TakeCoinHistory(in);
        }

        @Override
        public TakeCoinHistory[] newArray(int size) {
            return new TakeCoinHistory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    private TakeCoinHistory(Parcel in) {

        address = ParcelUtils.readStringFromParcel(in);
        amount = ParcelUtils.readStringFromParcel(in);
        createTime = ParcelUtils.readStringFromParcel(in);
        fee = ParcelUtils.readStringFromParcel(in);

        stateDesc = ParcelUtils.readStringFromParcel(in);
        symbol = ParcelUtils.readStringFromParcel(in);
        chainType = ParcelUtils.readStringFromParcel(in);
        realAmount = ParcelUtils.readStringFromParcel(in);

        inOut = in.readInt();
        state= in.readInt();
        userId = in.readLong();
        dwId = in.readLong();

    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        ParcelUtils.writeStringToParcel(out, address);
        ParcelUtils.writeStringToParcel(out, amount);
        ParcelUtils.writeStringToParcel(out, createTime);
        ParcelUtils.writeStringToParcel(out, fee);

        ParcelUtils.writeStringToParcel(out, stateDesc);
        ParcelUtils.writeStringToParcel(out, symbol);
        ParcelUtils.writeStringToParcel(out, chainType);
        ParcelUtils.writeStringToParcel(out, realAmount);

        out.writeInt(inOut);
        out.writeInt(state);

        out.writeLong(userId);
        out.writeLong(dwId);



    }
}
