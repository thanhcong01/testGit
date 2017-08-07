package com.ftu.admin.consumer.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LogInputParams implements java.io.Serializable {
	private String tableName = "";
	private String primaryKeyName = "";
	private String primaryKeyValue = "";
	private String moduleName = "";
	private String ipAddress = "";
	private LogRelatedEntity vObjectRelated; // Vector<Vector<String>>

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getPrimaryKeyName() {
		return primaryKeyName;
	}

	public void setPrimaryKeyName(String primaryKeyName) {
		this.primaryKeyName = primaryKeyName;
	}

	public String getPrimaryKeyValue() {
		return primaryKeyValue;
	}

	public void setPrimaryKeyValue(String primaryKeyValue) {
		this.primaryKeyValue = primaryKeyValue;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LogRelatedEntity getvObjectRelated() {
		return vObjectRelated;
	}

	public void setvObjectRelated(LogRelatedEntity vObjectRelated) {
		this.vObjectRelated = vObjectRelated;
	}
}
