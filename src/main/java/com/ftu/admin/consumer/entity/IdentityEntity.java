package com.ftu.admin.consumer.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
// default package
// Generated Aug 12, 2015 2:25:08 PM by Hibernate Tools 3.4.0.CR1
// default package
// Generated Aug 12, 2015 2:25:08 PM by Hibernate Tools 3.4.0.CR1

/**
 * SecIdentity generated by hbm2java
 */

@XmlRootElement
public class IdentityEntity implements java.io.Serializable {

	private long id;
	private int orgId;
	private int typeId;
	private String username;
	private String password;
	private String status;
	private String appCode;

	private OrganizationEntity organization;
	private List<RoleEntity> roles;
	private List<AttributeEntity> attributes;

	public IdentityEntity() {
		organization = new OrganizationEntity();
		roles = new ArrayList<RoleEntity>();
		attributes = new ArrayList<AttributeEntity>();
	}

	public long getId() {
		return id;
	}

	public int getOrgId() {
		return orgId;
	}

	public int getTypeId() {
		return typeId;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getStatus() {
		return status;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public OrganizationEntity getOrganization() {
		return organization;
	}

	public void setOrganization(OrganizationEntity organization) {
		this.organization = organization;
	}

	public List<RoleEntity> getRoles() {
		return roles;
	}

	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	public List<AttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<AttributeEntity> attributes) {
		this.attributes = attributes;
	}

	public AttributeEntity getAttributeEntityByCode(String code) {
		if (code != null) {
			for (AttributeEntity attributeEntity : attributes) {
				if (code.equals(attributeEntity.getCode())) {
					return attributeEntity;
				}
			}
		} else {
			return null;
		}
		return null;
	}
}
