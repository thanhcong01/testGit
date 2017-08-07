package com.ftu.admin.consumer.entity;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SecAccessLogEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long accessId;

	private long orgId;

	private long identityId;

	private String identityName;

	private String ipAddress;

	private long assetObj;

	private String assetName;

	private String assetCode;

	private String appName;

	private String appCode;

	private Date accessDate;

	private String straccessDate;

	private String transId;

	private String status;

	private long minute;
	private String tag;

	private String json;

	private String filterColum;

	public SecAccessLogEntity() {
		super();
	}

	public long getAccessId() {
		return accessId;
	}

	public void setAccessId(long accessId) {
		this.accessId = accessId;
	}

	public long getOrgId() {
		return orgId;
	}

	public void setOrgId(long orgId) {
		this.orgId = orgId;
	}

	public long getIdentityId() {
		return identityId;
	}

	public void setIdentityId(long identityId) {
		this.identityId = identityId;
	}

	public String getIdentityName() {
		return identityName;
	}

	public void setIdentityName(String identityName) {
		this.identityName = identityName;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public long getAssetObj() {
		return assetObj;
	}

	public void setAssetObj(long assetObj) {
		this.assetObj = assetObj;
	}

	public Date getAccessDate() {
		return accessDate;
	}

	public void setAccessDate(Date accessDate) {
		this.accessDate = accessDate;
	}

	public String getTransId() {
		return transId;
	}

	public void setTransId(String transId) {
		this.transId = transId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public long getMinute() {
		return minute;
	}

	public void setMinute(long minute) {
		this.minute = minute;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAssetCode() {
		return assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	public String getFilterColum() {
		return filterColum;
	}

	public void setFilterColum(String filterColum) {
		this.filterColum = filterColum;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getJson() {
		return json;
	}

	public void setJson(String json) {
		this.json = json;
	}

	public String getStraccessDate() {
		return straccessDate;
	}

	public void setStraccessDate(String straccessDate) {
		this.straccessDate = straccessDate;
	}

}