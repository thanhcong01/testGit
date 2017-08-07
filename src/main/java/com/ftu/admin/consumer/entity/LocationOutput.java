package com.ftu.admin.consumer.entity;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LocationOutput implements java.io.Serializable {

	private String errorCode;
	private String errorMessage;
	private String errorCause;
	private String errorRecommend;
	private List<LocationEntity> locations;
	private String status;

	public LocationOutput() {

		super();
	}

	public String getErrorCode() {
		return errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getErrorCause() {
		return errorCause;
	}

	public String getErrorRecommend() {
		return errorRecommend;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setErrorCause(String errorCause) {
		this.errorCause = errorCause;
	}

	public void setErrorRecommend(String errorRecommend) {
		this.errorRecommend = errorRecommend;
	}

	public List<LocationEntity> getLocations() {
		return locations;
	}

	public void setLocations(List<LocationEntity> locations) {
		this.locations = locations;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
