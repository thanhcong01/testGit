/**
 * 
 */
package com.ftu.admin.consumer.entity;

import java.io.Serializable;

/**
 * @author haitx
 */

public class PermissionEntity implements Serializable {

	private String granteeObj;
	private String granteeRefId;
	private String granteeRefCode;
	private String grantedObj;
	private String grantedRefId;
	private String grantedRefCode;
	private String privilegeCode;
	private boolean alow;

	public PermissionEntity() {
	}

	public String getGranteeObj() {
		return granteeObj;
	}

	public void setGranteeObj(String granteeObj) {
		this.granteeObj = granteeObj;
	}

	public String getGranteeRefId() {
		return granteeRefId;
	}

	public void setGranteeRefId(String granteeRefId) {
		this.granteeRefId = granteeRefId;
	}

	public String getGranteeRefCode() {
		return granteeRefCode;
	}

	public void setGranteeRefCode(String granteeRefCode) {
		this.granteeRefCode = granteeRefCode;
	}

	public String getGrantedObj() {
		return grantedObj;
	}

	public void setGrantedObj(String grantedObj) {
		this.grantedObj = grantedObj;
	}

	public String getGrantedRefId() {
		return grantedRefId;
	}

	public void setGrantedRefId(String grantedRefId) {
		this.grantedRefId = grantedRefId;
	}

	public String getGrantedRefCode() {
		return grantedRefCode;
	}

	public void setGrantedRefCode(String grantedRefCode) {
		this.grantedRefCode = grantedRefCode;
	}

	public String getPrivilegeCode() {
		return privilegeCode;
	}

	public void setPrivilegeCode(String privilegeCode) {
		this.privilegeCode = privilegeCode;
	}

	public boolean isAlow() {
		return alow;
	}

	public void setAlow(boolean alow) {
		this.alow = alow;
	}

	public String getPVKey() {
		return getGrantedObj() + "-" + getPrivilegeCode();
	}

	public String getGrantedKey() {
		return getGrantedObj() + "-" + getGrantedRefCode();
	}

	public String getGranteeKey() {
		return getGranteeObj() + "-" + getGranteeRefCode();
	}

}
