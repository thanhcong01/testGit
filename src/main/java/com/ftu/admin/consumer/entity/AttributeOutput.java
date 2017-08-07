package com.ftu.admin.consumer.entity;

import java.util.List;

public class AttributeOutput implements java.io.Serializable {
	private String errorCode;
	private String errorMessage;
	private String errorCause;
	private String errorRecommend;
	private List<AttributeEntity> attributeEntities;
	private String status;

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getErrorCause() {
		return errorCause;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}

	public String getErrorRecommend() {
		return errorRecommend;
	}

	public void setErrorRecommend(String errorRecommend) {
		this.errorRecommend = errorRecommend;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<AttributeEntity> getAttributeEntities() {
		return attributeEntities;
	}

	public void setAttributeEntities(List<AttributeEntity> attributeEntities) {
		this.attributeEntities = attributeEntities;
	}
}
