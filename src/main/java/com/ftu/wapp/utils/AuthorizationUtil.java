package com.ftu.wapp.utils;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ftu.admin.consumer.entity.PrivilegeEntity;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.admin.consumer.utils.ApplicationUtil;
import com.ftu.utils.StringUtil;

@ManagedBean
@ViewScoped
public class AuthorizationUtil implements Serializable {

	private static final long serialVersionUID = 1L;



	public static boolean hasPrivilege(SiteMapEntity sitemap, String privilegeCode) {
		for (PrivilegeEntity pe : sitemap.getPrivileges()) {
			if (privilegeCode.equals(pe.getCode())) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkPrivilege(String resourceCode, String privilegeCode) {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("username");
		for (SiteMapEntity sitemap : sessionData.getSitemaps()) {
			if (sitemap.getStrCode().equals(resourceCode)) {
				for (PrivilegeEntity pe : sitemap.getPrivileges()) {
					if (pe.getCode().equals(privilegeCode)) {
						return true;
					}
				}
			}
			boolean isChild = checkPrivilegeChildren(sitemap, resourceCode, privilegeCode);
			if (isChild) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean checkPrivilegeChildren(SiteMapEntity parent, String resourceCode, String privilegeCode) {
		for (SiteMapEntity sitemap : parent.getChildrent()) {
			if (sitemap.getStrCode().equals(resourceCode)) {
				for (PrivilegeEntity pe : sitemap.getPrivileges()) {
					if (pe.getCode().equals(privilegeCode)) {
						return true;
					}
				}
			}
			boolean isChild = checkPrivilegeChildren(sitemap, resourceCode, privilegeCode);
			if (isChild) {
				return true;
			}
		}
		return false;
	}
	
	

	public static boolean checkOutComeLink(SiteMapEntity sitemap) {
		if (StringUtil.stringIsNullOrEmty(ApplicationUtil.getAttributeValueByKey(sitemap, "SITEMAP_URL"))) {
			return false;
		}
		return true;
	}

}
