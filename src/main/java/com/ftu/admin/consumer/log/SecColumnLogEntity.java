package com.ftu.admin.consumer.log;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SecColumnLogEntity {
	private Long changeId;
	private String columnName;
	private String oldValue;
	private String newValue;

	public SecColumnLogEntity() {
		super();
	}

	public SecColumnLogEntity(Long changeId, String columnName, String oldValue, String newValue) {
		super();
		this.changeId = changeId;
		this.columnName = columnName;
		this.oldValue = oldValue;
		this.newValue = newValue;
	}

	public Long getChangeId() {
		return changeId;
	}

	public void setChangeId(Long changeId) {
		this.changeId = changeId;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String oldValue) {
		this.oldValue = oldValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

}
