package com.ftu.wapp.context;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlBody;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonFacesContext {
	public static String getRemoteAddr() {
		String ipAddress = null;
		if (FacesContext.getCurrentInstance() != null) {
			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			ipAddress = request.getHeader("X-FORWARDED-FOR");
			if (ipAddress == null) {
				ipAddress = request.getRemoteAddr();
			}
		}
		return ipAddress;
	}

	public static HttpServletRequest getRequest() {
		if (FacesContext.getCurrentInstance() != null) {
			return (HttpServletRequest) getExternalContext().getRequest();
		} else {
			return null;
		}

	}

	public static ExternalContext getExternalContext() {
		if (FacesContext.getCurrentInstance() != null) {
			return FacesContext.getCurrentInstance().getExternalContext();
		} else {
			return null;
		}
	}

	public static FacesContext getCurrentInstance() {
		return FacesContext.getCurrentInstance();
	}

	public static String getViewAttributeByKey(String key) {
		if (getCurrentInstance() != null) {
			List<UIComponent> lstComs = getCurrentInstance().getViewRoot().getChildren();
			for (UIComponent uiComponent : lstComs) {
				if (uiComponent instanceof HtmlBody) {
					if (uiComponent.getAttributes().containsKey(key)) {
						return uiComponent.getAttributes().get(key).toString();
					}
				}
			}
		}
		return null;
	}

	public static String getCompAttributeByKey(UIComponent component, String key) {
		if (component.getAttributes().containsKey(key)) {
			return component.getAttributes().get(key).toString();
		}
		return null;
	}

	public static HttpServletResponse getHttpServletResponse() {
		return (HttpServletResponse) getExternalContext().getResponse();
	}

	public static HttpServletRequest getHttpServletRequest() {
		return (HttpServletRequest) getExternalContext().getRequest();
	}
}
