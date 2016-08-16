package com.atm.chatonline.usermsg.bean;

public class ReplyMessage {

	private String nickName;
	private String repliedNickName;
	private String replyTime;
	private String replyContent;
	
	public ReplyMessage(String nickName, String repliedNickName,
			String replyTime, String replyContent) {
		super();
		this.nickName = nickName;
		this.repliedNickName = repliedNickName;
		this.replyTime = replyTime;
		this.replyContent = replyContent;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getRepliedNickName() {
		return repliedNickName;
	}
	public void setRepliedNickName(String repliedNickName) {
		this.repliedNickName = repliedNickName;
	}
	public String getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(String replyTime) {
		this.replyTime = replyTime;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	
	
	
}
