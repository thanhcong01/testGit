package com.ftu.admin.component;

import com.ftu.utils.ResourceBundleUtil;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;

public class FM extends FacesMessage {

    private static final long serialVersionUID = 1L;
    public static FacesMessage facesMessage;

    public static FacesMessage setMessage(String summary, String detail) {
        return new FacesMessage(summary, detail);
    }

    public static void addMessage(Severity severity, String detail) {
        FacesMessage msg = new FacesMessage(severity, detail, null);
        FacesContext.getCurrentInstance().addMessage(detail, msg);
    }

    public static void addGrowlMessage(Severity severity, String summary, String detail) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, summary, detail));
    }

    public static void showInfoMessage(String summary, String detail) {
        FacesMessage message = new FacesMessage(summary, detail);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public static void showInfoMessageInDialog(String summary, String detail) {
        facesMessage = new FacesMessage(SEVERITY_INFO, summary, detail);

        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
    }

    public static void showFatalMessageInDialog(String summary, String detail) {
        facesMessage = new FacesMessage(SEVERITY_FATAL, summary, detail);
        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
    }

    public static void showErrorMessageInDialog(String summary, String detail) {
        facesMessage = new FacesMessage(SEVERITY_ERROR, summary, detail);
        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
    }

    public static void showWarnMessageInDialog(String summary, String detail) {
        facesMessage = new FacesMessage(SEVERITY_WARN, summary, detail);
        RequestContext.getCurrentInstance().showMessageInDialog(facesMessage);
    }
    public static void showInfoGrowlMessage(String summary, String detail) {
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(SEVERITY_INFO, summary, detail));
	}
}
