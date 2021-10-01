package com.procoin.subpush.notify;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;
import com.procoin.http.util.ParcelUtils;

/**
 * Created by zhengmj on 16-2-24.
 */
public class NotifyModel implements TaojinluType, Parcelable {

    private int pid;
    private String head;
    private String body;
    private String t; // 消息类型  lp:文章，qa：问题，re：评论
    private String pkg; // anroid
    private String cls; // anroid
    private String p; // 相当于params
    private long time; // 消息产生时间
    private int ring; // 1响铃,代表是否响铃
    private int vibrate; // 1震动,代表是否震动

    private String circleNum;//圈子消息需要的参数
    private String circleName;




    public NotifyModel() {

    }

    public NotifyModel(int pid, String head, String body, String t, String pkg, String cls, String p, long time, int ring, int vibrate,String circleNum,String circleName) {
        this.pid = pid;
        this.head = head;
        this.body = body;
        this.t = t;
        this.pkg = pkg;
        this.cls = cls;
        this.p = p;
        this.time = time;
        this.ring = ring;
        this.vibrate = vibrate;
        this.circleNum=circleNum;
        this.circleName=circleName;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getT() {
        return t;
    }

    public void setT(String t) {
        this.t = t;
    }

    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public String getP() {
        return p;
    }

    public void setP(String p) {
        this.p = p;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getRing() {
        return ring;
    }

    public void setRing(int ring) {
        this.ring = ring;
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    public String getCircleNum() {
        return circleNum;
    }

    public void setCircleNum(String circleNum) {
        this.circleNum = circleNum;
    }

    public String getCircleName() {
        return circleName;
    }

    public void setCircleName(String circleName) {
        this.circleName = circleName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    private NotifyModel(Parcel in) {
        pid = in.readInt();
        head = ParcelUtils.readStringFromParcel(in);
        body = ParcelUtils.readStringFromParcel(in);
        t = ParcelUtils.readStringFromParcel(in);
        pkg = ParcelUtils.readStringFromParcel(in);
        cls = ParcelUtils.readStringFromParcel(in);
        p = ParcelUtils.readStringFromParcel(in);
        time = in.readLong();
        ring = in.readInt();
        vibrate = in.readInt();
        circleNum=ParcelUtils.readStringFromParcel(in);
        circleName=ParcelUtils.readStringFromParcel(in);
    }

    public static final Creator<NotifyModel> CREATOR = new Creator<NotifyModel>() {
        public NotifyModel createFromParcel(Parcel in) {
            return new NotifyModel(in);
        }

        @Override
        public NotifyModel[] newArray(int size) {
            return new NotifyModel[size];
        }
    };

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(pid);
        ParcelUtils.writeStringToParcel(out, head);
        ParcelUtils.writeStringToParcel(out, body);
        ParcelUtils.writeStringToParcel(out, t);
        ParcelUtils.writeStringToParcel(out, pkg);
        ParcelUtils.writeStringToParcel(out, cls);
        ParcelUtils.writeStringToParcel(out, p);
        out.writeLong(time);
        out.writeInt(ring);
        out.writeInt(vibrate);
        ParcelUtils.writeStringToParcel(out, circleNum);
        ParcelUtils.writeStringToParcel(out, circleName);
    }
}
