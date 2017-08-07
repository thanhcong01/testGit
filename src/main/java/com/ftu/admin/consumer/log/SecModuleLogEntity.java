/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.admin.consumer.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author haitx3
 */
@XmlRootElement
public class SecModuleLogEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long logId;
	private String moduleCode;
	private Long userId;
	private Date logDate;
	private String actionType;
	private String ipAddress;
	private String transId;
	private List<SecTableLogEntity> secTableLogEntitys = new ArrayList<SecTableLogEntity>();

	public SecModuleLogEntity() {
		super();
	}

	public SecModuleLogEntity(Long logId, String moduleCode, Long userId, Date logDate, String ipAddress,
			String transId) {
		super();
		this.logId = logId;
		this.moduleCode = moduleCode;
		this.userId = userId;
		this.logDate = logDate;
		this.ipAddress = ipAddress;
		this.transId = transId;
	}

	public SecModuleLogEntity(Long logId) {
		this.logId = logId;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Date getLogDate() {
		return logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public List<SecTableLogEntity> getSecTableLogEntitys() {
		return secTableLogEntitys;
	}

	public void setSecTableLogEntitys(List<SecTableLogEntity> secTableLogEntitys) {
		this.secTableLogEntitys = secTableLogEntitys;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (logId != null ? logId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SecModuleLogEntity)) {
			return false;
		}
		SecModuleLogEntity other = (SecModuleLogEntity) object;
		if ((this.logId == null && other.logId != null) || (this.logId != null && !this.logId.equals(other.logId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.ftu.admin.entity.OldSecModuleLog[ logId=" + logId + " ]";
	}

}
