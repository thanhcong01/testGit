/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.admin.consumer.utils;

import com.ftu.admin.consumer.AuthorizationConsumer;
import com.ftu.admin.consumer.entity.AttributeEntity;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.admin.consumer.entity.SiteMapOutput;
import com.ftu.admin.controller.CommonFacesContext;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.utils.ResourceBundleUtil;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Cao Cuong
 */
public class ApplicationUtil {

    public static List<SiteMapEntity> getSitemapForSession(String roleKey) {
        SiteMapOutput sitemapTreeRole;

        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false);
        SessionData data = (SessionData) session.getAttribute("username");
        try {
            sitemapTreeRole = AuthorizationConsumer.getTreeSiteMapForRolePrl(data == null ? null : data.getTransEntity(),
                    ResourceBundleUtil.getString("consumer.app"),roleKey);
            return sitemapTreeRole.getSitemaps();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getAttributeValueByKey(SiteMapEntity sitemap, String key) {
		for (AttributeEntity ae : sitemap.getAttributes()) {
			if (key.equals(ae.getCode())) {
				return ae.getValue();
			}
		}
		return null;
	}
}
