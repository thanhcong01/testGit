/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.language;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import javax.faces.context.FacesContext;

/**
 * @author haitx3
 */
public class UTF8ResourceBundle extends ResourceBundle {
    
    protected static final String BUNDLE_NAME = "com.ftu.language.messages";
    protected static final String BUNDLE_EXTENSION = "properties";
    protected static final Control UTF8_CONTROL = new UTF8Control();
    
    public UTF8ResourceBundle() {
        setParent(ResourceBundle.getBundle(BUNDLE_NAME, FacesContext.getCurrentInstance().getViewRoot().getLocale(),
                UTF8_CONTROL));
    }
    @Override
    protected Object handleGetObject(String key) {
        return parent.getObject(key);
    }
    
    @Override
    public Enumeration getKeys() {
        return parent.getKeys();
    }
    
    protected static class UTF8Control extends Control {
        
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader,
                boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, BUNDLE_EXTENSION);
            ResourceBundle bundle = null;
            InputStream stream = null;
            if (reload) {
                URL url = loader.getResource(resourceName);
                if (url != null) {
                    URLConnection connection = url.openConnection();
                    if (connection != null) {
                        connection.setUseCaches(false);
                        stream = connection.getInputStream();
                    }
                }
            } else {
                stream = loader.getResourceAsStream(resourceName);
                if (stream == null) {
                    stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream("/resources/" + resourceName);
                }
            }
            if (stream != null) {
                try {
                    bundle = new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                } finally {
                    stream.close();
                }
            }
            return bundle;
        }
    }
    
}
