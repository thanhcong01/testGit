package com.ftu.admin.consumer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SecAccessLogOutput extends ResponseOutput implements Serializable {
	private List<SecAccessLogEntity> lstSecAccess = new ArrayList<SecAccessLogEntity>();

	public SecAccessLogOutput() {
		super();
	}

	public List<SecAccessLogEntity> getLstSecAccess() {
		return lstSecAccess;
	}

	public void setLstSecAccess(List<SecAccessLogEntity> lstSecAccess) {
		this.lstSecAccess = lstSecAccess;
	}

}
