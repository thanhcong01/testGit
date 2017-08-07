package com.ftu.admin.consumer.entity;

import java.util.Date;

public class TransEntity implements java.io.Serializable {

	private String identityId;
	private String username;
	private String transId;
	private String remoteAddr;
	private int interval;
	private Date createTime;
	private Boolean changlePass = false;
	public String getIdentityId() {
		return identityId;
	}

	public void setIdentityId(String identityId) {
		this.identityId = identityId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getChanglePass() {
		return changlePass;
	}

	public void setChanglePass(Boolean changlePass) {
		this.changlePass = changlePass;
	}
}
