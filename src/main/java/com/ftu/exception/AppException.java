/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.exception;

import com.ftu.admin.component.FM;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.hibernate.HibernateUtil;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.MessageFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.faces.context.FacesContext;

/**
 *
 * @author E5420
 */
public final class AppException extends Exception {

    private String errorCode;
    private String description;
    private transient ResourceBundle bulder;
    protected static final String BUNDLE_NAME = "com/ftu/exception/messages";
    protected static final String BUNDLE_EXTENSION = "properties";

    /**
     * @return the errorCode
     */
    @Override
    public String getMessage() {
        return description;
    }

    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param aErrorCode the errorCode to set
     */
    public void setErrorCode(String aErrorCode) {
        errorCode = aErrorCode;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param aDescription the description to set
     */
    public void setDescription(String aDescription) {
        description = aDescription;
    }

    /**
     * @return the errorCode
     */
    public AppException(String code, Object[] parameter, Locale locale) {
        try {
            bulder = _getBulder(locale);
            this.errorCode = code;
            String ms = bulder.getString(code);
            if (parameter.length > 0) {
                this.description = MessageFormat.format(ms, parameter);
            } else {
                this.description = ms;
            }

            FM.showInfoMessage("Thông báo", description);

            HibernateUtil.closeCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public AppException() {
        super();

    }

    public AppException(String code, Locale locale) {
        try {
            HibernateUtil.closeCurrentSessions();
            bulder = _getBulder(locale);
            setErrorCode(code);
            setDescription(bulder.getString(code));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public AppException(String code) {
        try {
            bulder = _getBulder(new Locale(InventoryConstanst.local));
            setErrorCode(code);
            setDescription(bulder.getString(code));
            HibernateUtil.closeCurrentSessions();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private ResourceBundle _getBulder(Locale locale) {
        ResourceBundle rs = null;
        try {

            String resourceName = BUNDLE_NAME + "_" + locale.toString() + "." + BUNDLE_EXTENSION;
            InputStream stream = AppException.class.getResourceAsStream(resourceName);
            if (stream == null) {
                stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/" + resourceName);
            }
            if (stream == null) {
                stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    rs = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rs;
    }
}
