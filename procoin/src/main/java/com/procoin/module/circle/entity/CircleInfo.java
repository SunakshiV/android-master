package com.procoin.module.circle.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;

/**
 * 圈子基本信息
 */
public class CircleInfo implements TaojinluType,Parcelable {

    public long userId;
    public String circleId;
    public String circleName;
    public String circleLogo;
    public String circleBg;
    public String brief;
    public String createTime;
    public int joinMode;//0默认需要验证  1无需验证
    public int memberAmount;
    public int speakStatus;//0允许发言 1禁止发言

    public int synMark;//同步标记

    public String say;
    public String lastName;
    public String recent_time;//圈子tab的列表排序依据

    public int role;//他的圈子用到
    public int  reviewState;//0审核中 1通过  2不通过




    protected CircleInfo(Parcel in) {
        userId=in.readLong();
        circleId = in.readString();
        circleName = in.readString();
        circleLogo = in.readString();

        circleBg = in.readString();
        brief = in.readString();
        createTime = in.readString();

        joinMode = in.readInt();
        memberAmount = in.readInt();

        synMark = in.readInt();

        say = in.readString();
        lastName = in.readString();
        recent_time = in.readString();
    }
    public CircleInfo(){}
    public static final Creator<CircleInfo> CREATOR = new Creator<CircleInfo>() {
        @Override
        public CircleInfo createFromParcel(Parcel in) {
            return new CircleInfo(in);
        }

        @Override
        public CircleInfo[] newArray(int size) {
            return new CircleInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(userId);
        parcel.writeString(circleId);
        parcel.writeString(circleName);
        parcel.writeString(circleLogo);

        parcel.writeString(circleBg);
        parcel.writeString(brief);
        parcel.writeString(createTime);

        parcel.writeInt(joinMode);
        parcel.writeInt(memberAmount);

        parcel.writeInt(synMark);

        parcel.writeString(say);
        parcel.writeString(lastName);
        parcel.writeString(recent_time);
    }



    @Override
    public String toString() {
        return "userId==" + userId
                + "  circleId==" + circleId
                + "  circleName==" + circleName
                + "  circleLogo==" + circleLogo
                + "  circleBg==" + circleBg
                + "  brief==" + brief
                + "  createTime==" + createTime
                + "  synMark==" + synMark
                + "  say==" + say
                + "  lastName==" + lastName
                + "  recent_time==" + recent_time;
    }
}
