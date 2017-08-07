/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.admin.consumer.log;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


/**
 *
 * @author haitx3
 */
@XmlRootElement
public class SecTableLogEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long changeId;
	private Long logId;
	private String tableName;
	private String rowId;
	private String actionType;
	private List<SecColumnLogEntity> secColumnLogEntitys = new ArrayList<SecColumnLogEntity>();

	public SecTableLogEntity() {
		super();
	}

	public SecTableLogEntity(Long changeId) {
		this.changeId = changeId;
	}

	public SecTableLogEntity(Long changeId, Long logId, String tableName, String rowId) {
		this.changeId = changeId;
		this.logId = logId;
		this.tableName = tableName;
		this.rowId = rowId;
	}

	public Long getChangeId() {
		return changeId;
	}

	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}

	public Long getLogId() {
		return logId;
	}

	public void setLogId(Long logId) {
		this.logId = logId;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getRowId() {
		return rowId;
	}

	public void setRowId(String rowId) {
		this.rowId = rowId;
	}

	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public List<SecColumnLogEntity> getSecColumnLogEntitys() {
		return secColumnLogEntitys;
	}

	public void setSecColumnLogEntitys(List<SecColumnLogEntity> secColumnLogEntitys) {
		this.secColumnLogEntitys = secColumnLogEntitys;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (changeId != null ? changeId.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are
		// not set
		if (!(object instanceof SecTableLogEntity)) {
			return false;
		}
		SecTableLogEntity other = (SecTableLogEntity) object;
		if ((this.changeId == null && other.changeId != null)
				|| (this.changeId != null && !this.changeId.equals(other.changeId))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.ftu.admin.entity.OldSecTableLog[ changeId=" + changeId + " ]";
	}

}
