package com.ftu.wapp.component;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.primefaces.component.menu.Menu;

import com.ftu.utils.StringUtil;
import com.ftu.wapp.context.CommonFacesContext;
import com.ftu.wapp.utils.AuthorizationUtil;

/**
 * @version n/a
 * @author haitx3
 */
public class CommonRenderer {

	public CommonRenderer() {
	}

	public static boolean isDisable(FacesContext context, UIComponent component) throws IOException {
		String privilegeCode = CommonFacesContext.getCompAttributeByKey(component, "PRIVILEGE");
		String resourceCode = CommonFacesContext.getCompAttributeByKey(component, "RESOURCE");
		if (!StringUtil.stringIsNullOrEmty(privilegeCode)) {
			if (StringUtil.stringIsNullOrEmty(resourceCode)) {
				resourceCode = CommonFacesContext.getViewAttributeByKey("RESOURCE");
			}
			if (AuthorizationUtil.checkPrivilege(resourceCode, privilegeCode)) {
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean isDisable(FacesContext context, Menu component) throws IOException {
		String privilegeCode = CommonFacesContext.getCompAttributeByKey(component, "PRIVILEGE");
		String resourceCode = CommonFacesContext.getCompAttributeByKey(component, "RESOURCE");
		if (!StringUtil.stringIsNullOrEmty(privilegeCode)) {
			if (StringUtil.stringIsNullOrEmty(resourceCode)) {
				resourceCode = CommonFacesContext.getViewAttributeByKey("RESOURCE");
			}
			if (AuthorizationUtil.checkPrivilege(resourceCode, privilegeCode)) {
				return false;
			}
			return true;
		}
		return false;
	}

}
