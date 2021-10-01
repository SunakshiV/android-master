package com.procoin.module.myhome.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;

/**
 * Created by zhengmj on 18-11-15.
 */

public class VideoEntity implements TaojinluType ,Parcelable{
    public String coverPath;
    public String videoPath;
    public long duration;
    public long size;

    protected VideoEntity(Parcel in) {
        coverPath = in.readString();
        videoPath = in.readString();
        duration = in.readLong();
    }
    public VideoEntity(){}
    public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
        @Override
        public VideoEntity createFromParcel(Parcel in) {
            return new VideoEntity(in);
        }

        @Override
        public VideoEntity[] newArray(int size) {
            return new VideoEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(coverPath);
        parcel.writeString(videoPath);
        parcel.writeLong(duration);
    }
}
