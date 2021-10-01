package com.procoin.module.myhome.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 18-11-20.
 */

public class PhotoEntity implements TaojinluType ,Parcelable{
    public String path;
    public boolean isSelect;
    public int position;//position in list
    public boolean isInsideSelect;
    public PhotoEntity(){}

    protected PhotoEntity(Parcel in) {
        path = in.readString();
        isSelect = in.readByte() != 0;
        position = in.readInt();
        isInsideSelect = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeByte((byte) (isSelect ? 1 : 0));
        dest.writeInt(position);
        dest.writeByte((byte) (isInsideSelect ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhotoEntity> CREATOR = new Creator<PhotoEntity>() {
        @Override
        public PhotoEntity createFromParcel(Parcel in) {
            return new PhotoEntity(in);
        }

        @Override
        public PhotoEntity[] newArray(int size) {
            return new PhotoEntity[size];
        }
    };
}
