package com.ftu.admin.consumer.entity;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class LogRelatedEntity implements java.io.Serializable {
	private String value;
	private List<LogRelatedEntity> children = new ArrayList<LogRelatedEntity>();

	public LogRelatedEntity() {
		super();
	}

	public LogRelatedEntity(String value) {
		super();
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<LogRelatedEntity> getChildren() {
		return children;
	}

	public void setChildren(List<LogRelatedEntity> children) {
		this.children = children;
	}
}
