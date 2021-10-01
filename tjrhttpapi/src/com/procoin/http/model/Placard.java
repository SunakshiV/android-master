package com.procoin.http.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.procoin.http.base.TaojinluType;

public class Placard implements TaojinluType, Parcelable {

	public Placard() {
		super();
	}

	private String content;// 内容
	private String noticeTime;// 时间
	private String title;// 标题


	protected Placard(Parcel in) {
		content = in.readString();
		noticeTime = in.readString();
		title = in.readString();
	}

	public static final Creator<Placard> CREATOR = new Creator<Placard>() {
		@Override
		public Placard createFromParcel(Parcel in) {
			return new Placard(in);
		}

		@Override
		public Placard[] newArray(int size) {
			return new Placard[size];
		}
	};

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPlacardTime() {
		return noticeTime;
	}

	public void setPlacardTime(String placardTime) {
		this.noticeTime = placardTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub

		arg0.writeString(content);
		arg0.writeString(noticeTime);
		arg0.writeString(title);
	}

	@Override
	public String toString() {
		return "Placard{" +
				"content='" + content + '\'' +
				", noticeTime='" + noticeTime + '\'' +
				", title='" + title + '\'' +
				'}';
	}
}
