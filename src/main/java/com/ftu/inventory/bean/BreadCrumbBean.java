/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.language.LanguageBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class BreadCrumbBean implements Serializable {

    private MenuModel model;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    List<SiteMapEntity> listAction = new ArrayList<>();
    List<SiteMapEntity> breadcum;

    @PostConstruct
    public void init() {
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                .getSession(false);
        SessionData sd = (SessionData) session.getAttribute("username");
        if (sd != null) {
            listAction = sd.getSitemaps();
        }
    }

    public void getLsBreadcum(String url) {
        breadcum = new ArrayList<>();
        for (SiteMapEntity d : listAction) {
            addBreadcum(d, url);
            if (!breadcum.isEmpty()) {
                break;
            }
        }
    }

    public void addBreadcum(SiteMapEntity d, String url) {
        String url2 = "";
        if (d.getStrUrl() != null) {
            String s[] = d.getStrUrl().split("/");
            url2 = s[s.length - 1].replace(".jsf", "").replace(".xhtml", "");
        }
        if (url2.equals(url) && d.getStrUrl() != null) {
            breadcum.add(d);
            if (d.getIntParentId() != null) {
                addParentBreadcum(d.getIntParentId());
            }
        } else {
            for (SiteMapEntity itm : d.getChildrent()) {
                addBreadcum(itm, url);
                if (!breadcum.isEmpty()) {
                    break;
                }
            }
        }
    }

    public void addParentBreadcum(Integer id) {
        for (SiteMapEntity d : listAction) {
            if (id.equals(d.getIntParentId())) {
                breadcum.add(d);
                if (d.getIntParentId() != null) {
                    addParentBreadcum(d.getIntParentId());
                }
                break;
            } else {
                addParentChild(d, id);
            }
        }
    }

    public void addParentChild(SiteMapEntity d, Integer id) {
        for (SiteMapEntity sd : d.getChildrent()) {
            if (id.equals(sd.getIntParentId())) {
                breadcum.add(d);
                if (d.getIntParentId() != null) {
                    addParentBreadcum(d.getIntParentId());
                }
                break;
            } else {
                addParentChild(sd, id);
            }
        }
    }

    /**
     * @return the model
     */
    public MenuModel getModel() {
        breadcum = new ArrayList<>();
        HttpServletRequest origRequest = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        if (origRequest != null);
        {
            String[] s = origRequest.getRequestURL().toString().split("/");
            if (s.length > 0) {
                String url = s[s.length - 1].replace(".jsf", "").replace(".xhtml", "");
                getLsBreadcum(url);
            }
            if (!breadcum.isEmpty()) {
                model = new DefaultMenuModel();
                DefaultMenuItem home = new DefaultMenuItem(languageBean.getBundle().getString("home"));
                home.setUrl("/index.jsf");
                model.addElement(home);
                for (int i = breadcum.size() - 1; i >= 0; i--) {
                    SiteMapEntity d = breadcum.get(i);
                    DefaultMenuItem item = new DefaultMenuItem(d.getStrName());
                    if (i > 0) {
                        item.setUrl(d.getStrUrl());
                    }
                    model.addElement(item);
                }
            }
        }
        return model;
    }

    /**
     * @param model the model to set
     */
    public void setModel(MenuModel model) {
        this.model = model;
    }

    /**
     * @return the sessionBean
     */
    public SessionBean getSessionBean() {
        return sessionBean;
    }

    /**
     * @param sessionBean the sessionBean to set
     */
    public void setSessionBean(SessionBean sessionBean) {
        this.sessionBean = sessionBean;
    }

    /**
     * @return the languageBean
     */
    public LanguageBean getLanguageBean() {
        return languageBean;
    }

    /**
     * @param languageBean the languageBean to set
     */
    public void setLanguageBean(LanguageBean languageBean) {
        this.languageBean = languageBean;
    }
}
