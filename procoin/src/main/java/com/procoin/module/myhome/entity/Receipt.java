package com.procoin.module.myhome.entity;

/**
 * <p>
 * Created by likebamboo on 2014-4-22
 * Copyright (c) 1998-2014 http://likebamboo.github.io/ All rights reserved.
 */

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;
import com.procoin.http.util.ParcelUtils;

/**
 *我的收款方式
 */
public class Receipt implements TaojinluType,Parcelable {


    public long paymentId;
    public long userId;
    public int receiptType;// 1 支付宝  2 微信  3银行卡
    public int isDefault;
    public String receiptTypeValue;
    public String  receiptLogo;
    public String  receiptName;
    public String receiptNo;
    public String receiptDesc;
    public String qrCode;
    public String qrContent;
    public String bankName;
    public String bankBranch;
    public String userName;
    public int isActive;
    public int isRecommend;

    public boolean isSelected;



    public Receipt(){}

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    private Receipt(Parcel in) {
        paymentId = in.readLong();
        userId = in.readLong();
        receiptType = in.readInt();
        isDefault= in.readInt();

        receiptTypeValue = ParcelUtils.readStringFromParcel(in);
        receiptLogo = ParcelUtils.readStringFromParcel(in);
        receiptName = ParcelUtils.readStringFromParcel(in);
        receiptNo = ParcelUtils.readStringFromParcel(in);
        receiptDesc = ParcelUtils.readStringFromParcel(in);
        qrCode = ParcelUtils.readStringFromParcel(in);
        qrContent = ParcelUtils.readStringFromParcel(in);
        bankName = ParcelUtils.readStringFromParcel(in);
        bankBranch = ParcelUtils.readStringFromParcel(in);
        userName = ParcelUtils.readStringFromParcel(in);

        isActive = in.readInt();
        isRecommend = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeLong(paymentId);
        out.writeLong(userId);
        out.writeInt(receiptType);
        out.writeInt(isDefault);

        ParcelUtils.writeStringToParcel(out, receiptTypeValue);
        ParcelUtils.writeStringToParcel(out, receiptLogo);
        ParcelUtils.writeStringToParcel(out, receiptName);
        ParcelUtils.writeStringToParcel(out, receiptNo);
        ParcelUtils.writeStringToParcel(out, receiptDesc);
        ParcelUtils.writeStringToParcel(out, qrCode);
        ParcelUtils.writeStringToParcel(out, qrContent);
        ParcelUtils.writeStringToParcel(out, bankName);
        ParcelUtils.writeStringToParcel(out, bankBranch);
        ParcelUtils.writeStringToParcel(out, userName);

        out.writeInt(isActive);
        out.writeInt(isRecommend);

    }
}
