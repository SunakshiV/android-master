package com.procoin.module.circle.entity;

import com.procoin.http.base.TaojinluType;

public class CircleChatEntity implements TaojinluType {

	public String chatTopic;//chat_topic 也是circleId;
	public long chatId;
	public long userId;
	public long toUid;
	public String createTime;
	public String say;
	public String name;
	public String headurl;
	public int chatMark;
	public String verify;
	public int isVip;
	public int type;//0纯文字   1图片    2语音  3股票  4提示 5k线宝箱 详情见这个类CircleChatTypeEnum
	public boolean isPush;//收到消息时是否需要push

	public int state;//0 发送成功(从数据库查询出来默认是0 显示成功状态)    1 为正在发送      -1 发送失败

	public boolean showTime;//私聊的时间3分钟才显示一次

	public int voiceIsRed;//语音用到 ，是否未读
	public int overtime;
	public String roleName;




//	public String typeOnSelfSend;//自己发的东西没有类型用这个判断


	@Override
	public String toString() {
		return "CircleChatEntity{" +
				"chatId=" + chatId +
				", chatTopic='" + chatTopic + '\'' +
				", userId=" + userId +
				", createTime='" + createTime + '\'' +
				", say='" + say + '\'' +
				", user_name='" + name + '\'' +
				", headurl='" + headurl + '\'' +
				", chatMark=" + chatMark +
				", verify='" + verify + '\'' +
				", isVip=" + isVip +
				", type=" + type +
				", isPush=" + isPush +
				", state=" + state +
				", showTime=" + showTime +
				", voiceIsRed=" + voiceIsRed +
				", overtime=" + overtime +
				", roleName='" + roleName + '\'' +
				'}';
	}
}
