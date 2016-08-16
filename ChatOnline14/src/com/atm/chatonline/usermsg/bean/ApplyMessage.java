package com.atm.chatonline.usermsg.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public class ApplyMessage implements Serializable{

	private String nickName;
	private String appliedNickName;
	private String applyTime;
	private String applyContent;
	
	public ApplyMessage(String nickName, String appliedNickName,
			String applyTime, String applyContent) {
		super();
		this.nickName = nickName;
		this.appliedNickName = appliedNickName;
		this.applyTime = applyTime;
		this.applyContent = applyContent;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getAppliedNickName() {
		return appliedNickName;
	}
	public void setAppliedNickName(String appliedNickName) {
		this.appliedNickName = appliedNickName;
	}
	public String getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	public String getApplyContent() {
		return applyContent;
	}
	public void setApplyContent(String applyContent) {
		this.applyContent = applyContent;
	}
	
	
}
