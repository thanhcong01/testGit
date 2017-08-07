package com.ftu.admin.consumer.entity;



public class LocationEntity implements java.io.Serializable {

	private int locationId;
	private Integer parentId;
	private String code;
	private String name;
	private Integer locationType;
	private String status;

	public LocationEntity() {
	}

	public LocationEntity(int locationId) {
		this.locationId = locationId;
	}

	public int getLocationId() {
		return this.locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLocationType() {
		return this.locationType;
	}

	public void setLocationType(Integer locationType) {
		this.locationType = locationType;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
