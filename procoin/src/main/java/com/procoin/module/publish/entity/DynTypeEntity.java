package com.procoin.module.publish.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 18-11-13.
 */

public class DynTypeEntity implements TaojinluType,Parcelable{
    public long projectId;
    public String name;
    public String logo;
    public DynTypeEntity(){}
    protected DynTypeEntity(Parcel in) {
        projectId = in.readLong();
        name = in.readString();
        logo = in.readString();
    }

    public static final Creator<DynTypeEntity> CREATOR = new Creator<DynTypeEntity>() {
        @Override
        public DynTypeEntity createFromParcel(Parcel in) {
            return new DynTypeEntity(in);
        }

        @Override
        public DynTypeEntity[] newArray(int size) {
            return new DynTypeEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(projectId);
        parcel.writeString(name);
        parcel.writeString(logo);
    }
}
