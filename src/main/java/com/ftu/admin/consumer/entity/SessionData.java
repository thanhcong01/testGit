/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ftu.admin.consumer.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Cao Cuong
 */
public class SessionData implements Serializable {
	private IdentityEntity identityEntity;
	private TransEntity transEntity;
	public List<SiteMapEntity> sitemaps = new ArrayList<SiteMapEntity>();
	public IdentityEntity getIdentityEntity() {
		return identityEntity;
	}

	public void setIdentityEntity(IdentityEntity identityEntity) {
		this.identityEntity = identityEntity;
	}

	public TransEntity getTransEntity() {
		return transEntity;
	}

	public void setTransEntity(TransEntity transEntity) {
		this.transEntity = transEntity;
	}

	public List<SiteMapEntity> getSitemaps() {
		return sitemaps;
	}

	public void setSitemaps(List<SiteMapEntity> sitemaps) {
		this.sitemaps = sitemaps;
	}


}

