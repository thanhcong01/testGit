package com.ftu.admin.consumer.log;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The persistent class for the SEC_IDENTITY database table.
 * 
 */
@XmlRootElement
public class SecIdentity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long identityId;

	private int identityTypeId;

	private int orgId;

	private String password;

	private String status;

	private String username;

	public SecIdentity() {
	}

	public Long getIdentityId() {
		return this.identityId;
	}

	public void setIdentityId(Long identityId) {
		this.identityId = identityId;
	}

	public int getIdentityTypeId() {
		return this.identityTypeId;
	}

	public void setIdentityTypeId(int identityTypeId) {
		this.identityTypeId = identityTypeId;
	}

	public int getOrgId() {
		return this.orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}