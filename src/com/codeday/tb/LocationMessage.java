package com.codeday.tb;

/**
 * 位置信息对象 <xml> <ToUserName><![CDATA[toUser]]></ToUserName>
 * <FromUserName><![CDATA[fromUser]]></FromUserName>
 * <CreateTime>1351776360</CreateTime> <MsgType><![CDATA[location]]></MsgType>
 * <Location_X>23.134521</Location_X> <Location_Y>113.358803</Location_Y>
 * <Scale>20</Scale> <Label><![CDATA[位置信息]]></Label>
 * <MsgId>1234567890123456</MsgId> </xml>
 * 
 * @author liuxf
 * 
 */
public class LocationMessage {

	/**
	 * 参数 描述 ToUserName 开发者微信号 FromUserName 发送方帐号（一个OpenID） CreateTime 消息创建时间
	 * （整型） MsgType location Location_X 地理位置维度 Location_Y 地理位置经度 Scale 地图缩放大小
	 * Label 地理位置信息 MsgId 消息id，64位整型
	 */
	private String ToUserName;
	private String FromUserName;
	private long CreateTime;
	private String MsgType;
	private String Location_X;
	private String Location_Y;
	private String Scale;
	private String Label;
	private String MsgId;

	public String getToUserName() {
		return ToUserName;
	}

	public void setToUserName(String toUserName) {
		ToUserName = toUserName;
	}

	public String getFromUserName() {
		return FromUserName;
	}

	public void setFromUserName(String fromUserName) {
		FromUserName = fromUserName;
	}

	public long getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(long createTime) {
		CreateTime = createTime;
	}

	public String getMsgType() {
		return MsgType;
	}

	public void setMsgType(String msgType) {
		MsgType = msgType;
	}

	public String getLocation_X() {
		return Location_X;
	}

	public void setLocation_X(String location_X) {
		Location_X = location_X;
	}

	public String getLocation_Y() {
		return Location_Y;
	}

	public void setLocation_Y(String location_Y) {
		Location_Y = location_Y;
	}

	public String getScale() {
		return Scale;
	}

	public void setScale(String scale) {
		Scale = scale;
	}

	public String getLabel() {
		return Label;
	}

	public void setLabel(String label) {
		Label = label;
	}

	public String getMsgId() {
		return MsgId;
	}

	public void setMsgId(String msgId) {
		MsgId = msgId;
	}

}
