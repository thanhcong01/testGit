package com.ftu.admin.consumer.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class HeaderEntity {
	String userId;
	String userName;

	public String getUserId() {
		return userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
