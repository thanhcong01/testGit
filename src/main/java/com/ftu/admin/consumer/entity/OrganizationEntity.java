package com.ftu.admin.consumer.entity;

import java.beans.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @version 08/10/2015
 * @author haitx3
 */

@XmlRootElement
public class OrganizationEntity implements Serializable {

	private int orgId;
	private Integer parentId;
	private int locationId;
	private int orgTypeId;
	private String code;
	private String name;
	private String status;
	private List<OrganizationEntity> childrent = new ArrayList<OrganizationEntity>();
	private List<AttributeEntity> attributes = new ArrayList<AttributeEntity>();

	public OrganizationEntity() {
	}

	public int getOrgId() {
		return orgId;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public int getOrgTypeId() {
		return orgTypeId;
	}

	public void setOrgTypeId(int orgTypeId) {
		this.orgTypeId = orgTypeId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Transient
	public Integer getParentId() {
		return parentId;
	}

	@Transient
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public List<OrganizationEntity> getChildrent() {
		return childrent;
	}

	public void setChildrent(List<OrganizationEntity> childrent) {
		this.childrent = childrent;
	}

	public List<AttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeEntity> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String toString() {
		return getName();
	}

	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}
		OrganizationEntity obj2 = (OrganizationEntity) obj;
		return getOrgId() == obj2.getOrgId();
	}

}
