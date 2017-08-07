/*package com.ftu.wapp.utils;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.ftu.admin.api.cache.CacheData;
import com.ftu.admin.consumer.AuthorizationConsumer;
import com.ftu.admin.consumer.entity.AttributeEntity;
import com.ftu.admin.consumer.entity.PrivilegeEntity;
import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.admin.consumer.entity.SiteMapOutput;
import com.ftu.admin.consumer.utils.ConsumerPropeties;
import com.ftu.admin.consumer.utils.StaticData;
import com.ftu.admin.jpa.persistence.Action;
import com.ftu.admin.jpa.persistence.Privilege;
import com.ftu.admin.jpa.persistence.SecObject;
import com.ftu.admin.service.ActionService;
import com.ftu.admin.service.ObjectService;
import com.ftu.admin.service.PrivilegeService;
import com.ftu.admin.service.impl.ActionServiceImpl;
import com.ftu.admin.service.impl.ObjectServiceImpl;
import com.ftu.admin.service.impl.PrivilegeServiceImpl;
import com.ftu.utils.StringUtil;
import com.ftu.wapp.context.CommonFacesContext;
import com.ftu.wapp.session.SessionBean;

public class ApplicationUtil {

	public static List<SiteMapEntity> getSitemapForSession() {
		SiteMapOutput sitemapTreeRole;
		try {
			sitemapTreeRole = AuthorizationConsumer.getTreeSiteMapForRole(SessionBean.getTransEntity(),
					ConsumerPropeties.getProperty("consumer.app"));
			return sitemapTreeRole.getSitemaps();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void putToMapViewAllowAccess(List<SiteMapEntity> lstSiteMap) {
		for (SiteMapEntity siteMapEntity : lstSiteMap) {
			boolean isAccess = false;
			for (PrivilegeEntity pe : siteMapEntity.getPrivileges()) {
				if ("ACCESS".equals(pe.getCode())) {
					isAccess = true;
				}
			}
			if (isAccess) {
				CommonFacesContext.getCurrentInstance().getAttributes().put(siteMapEntity.getStrUrl(),
						siteMapEntity.getStrCode());
			}
		}
	}

	public static String getRequestSite() {
		HttpServletRequest request = CommonFacesContext.getRequest();
		String strRequestURI = request.getRequestURI();
		String[] items = strRequestURI.split("/");
		String strSiteName = "";
		if (items.length > 0) {
			strSiteName = items[items.length - 1];
		}

		return strSiteName;
	}

	public static String getRequestSiteName() {
		HttpServletRequest request = CommonFacesContext.getRequest();
		try {
			String strRequestURI = request.getRequestURI();
			String[] items = strRequestURI.split("/");
			String strSiteName = "";
			if (items.length > 0) {
				strSiteName = items[items.length - 1];
				String[] str = strSiteName.split(".");
				return str[0];
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}

	}

	public static String getRequestSite(String url) {
		String[] items = url.split("/");
		String strSiteName = "";
		if (items.length > 0) {
			strSiteName = items[items.length - 1];
		}
		return strSiteName;
	}

	public static String getRequestSiteName(String url) {
		try {
			String[] items = url.split("/");
			String strSiteName = "";
			if (items.length > 0) {
				strSiteName = items[items.length - 1];
				String[] str = strSiteName.split(".");
				return str[0];
			} else {
				return "";
			}
		} catch (Exception e) {
			return "";
		}

	}

	public static String getAttributeValueByKey(SiteMapEntity sitemap, String key) {
		for (AttributeEntity ae : sitemap.getAttributes()) {
			if (key.equals(ae.getCode())) {
				return ae.getValue();
			}
		}
		return null;
	}

	public static String getOutComeLink(SiteMapEntity sitemap, String attrCode) {
		if (sitemap.getAttributes() != null) {
			for (AttributeEntity attributeEntity : sitemap.getAttributes()) {
				if (attrCode.equals(attributeEntity.getCode())) {
					return attributeEntity.getValue();
				}
			}
		}
		return "";
	}

	public static String getAppAttribute(String strKey) {
		// if (CacheData.mapAppAttributes.size() <= 0) {
		// InitResourceData.initAppAttributeData();
		// }
		// return CacheData.mapAppAttributes.get(strKey);

		if (!CommonFacesContext.getCurrentInstance().getAttributes().containsKey("INIT_APP_ATTRIBUTES")) {
			InitResourceData.initAppAttributeData();
		}
		if (CommonFacesContext.getCurrentInstance().getAttributes().containsKey(strKey)) {
			return CommonFacesContext.getCurrentInstance().getAttributes().get(strKey).toString();
		}
		return null;

	}

	public static SiteMapEntity getSiteMapByCode(String strCode) {
		for (SiteMapEntity siteMapEntity : getListSiteMap()) {
			if (strCode.equals(siteMapEntity.getStrCode())) {
				return siteMapEntity;
			}
		}
		return null;
	}

	public static List<SiteMapEntity> getTreeSiteMapForLeaves(List<SiteMapEntity> lstSitemap, int parentId) {
		for (SiteMapEntity siteMapEntity : getListSiteMap()) {
			if (parentId == siteMapEntity.getId()) {
				lstSitemap.add(0, siteMapEntity);
				if (siteMapEntity.getIntParentId() != null) {
					getTreeSiteMapForLeaves(lstSitemap, siteMapEntity.getIntParentId());
				}
			}
		}
		return lstSitemap;
	}

	public SiteMapEntity getSiteMapEnittyByCode(SiteMapEntity siteMapEntity, String strCode) {
		if (strCode.equals(siteMapEntity.getStrCode())) {
			return siteMapEntity;
		} else if (siteMapEntity.getChildrent().size() > 0) {
			return getSiteMapEnittyByCode(siteMapEntity, siteMapEntity.getStrCode());
		}
		return null;
	}

	public static List<SiteMapEntity> getListSiteMap() {
		List<SiteMapEntity> lst = new ArrayList<SiteMapEntity>();
		for (SiteMapEntity siteMapEntity : SessionBean.getSitemaps()) {
			lst.add(siteMapEntity);
			getChildrenSiteMapByParent(lst, siteMapEntity);
		}
		return lst;
	}

	public static List<SiteMapEntity> getChildrenSiteMapByParent(List<SiteMapEntity> lst, SiteMapEntity siteMapEntity) {
		for (SiteMapEntity sm : siteMapEntity.getChildrent()) {
			lst.add(sm);
			getChildrenSiteMapByParent(lst, sm);
		}
		return lst;
	}

	public static boolean autoConfigPrivilege(String objCode, String referObjId) {
		boolean result = false;
		try {
			PrivilegeService privilegeService = new PrivilegeServiceImpl();
			ActionService actionService = new ActionServiceImpl();
			ObjectService objService = new ObjectServiceImpl();
			// insert default action for sitemap
			// get default privilege
			String priCodes = "";
			SecObject siemapObj = CacheData.mapObject.get(objCode);
			Object object = ConvertUtil.getAttributeValueByCode(siemapObj.getAttributeEntities(),
					"SEC_AUTOCONFIG_PRIVILEGE");
			String defaultPrivilege = "";
			if (object != null) {
				defaultPrivilege = object.toString();

			}
			String[] stringArr = defaultPrivilege.split(";");
			int stringArrLength = stringArr.length;

			if (stringArrLength > 0) {
				for (int i = 0; i < stringArrLength; i++) {
					if (StringUtil.stringIsNullOrEmty(priCodes)) {
						priCodes = "'" + stringArr[i] + "'";
					} else {
						priCodes += ",'" + stringArr[i] + "'";
					}
				}
			}

			List<Privilege> listPrivilege = privilegeService.getAllByCodes(priCodes, "1");
			List<Action> actInsertList = new ArrayList<Action>();
			SecObject objectCondition = new SecObject();
			objectCondition.setCode(objCode);
			// List<SecObject> listObject =
			// objService.getAllWithParams(objectCondition, null, true, 0, 0,
			// true);
			String assetObj = objService.getObjIdByCode(objCode);

			if (!StringUtil.stringIsNullOrEmty(assetObj)) {
				for (Privilege privilege : listPrivilege) {
					Action action = new Action();
					action.setAssetObj(assetObj);
					action.setAssetId(Long.parseLong(referObjId));
					action.setPrivilegeId(privilege.getPrivilegeId());
					action.setCode(privilege.getCode());
					action.setName(privilege.getName());

					actInsertList.add(action);
				}
				// insert default privilege
				actionService.insertBatch(actInsertList);
				result = true;
			} else {
				result = false;
			}

		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}
}
*/