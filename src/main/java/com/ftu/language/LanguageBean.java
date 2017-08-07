/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.language;

import com.ftu.admin.component.FM;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.ResourceBundleUtil;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.http.HttpSession;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class LanguageBean implements Serializable {

    private MenuModel model;
    private List<SiteMapEntity> listAction = new ArrayList<>();
    private Long selectId;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean = new SessionBean();
    protected static final String BUNDLE_NAME = "com/ftu/language/messages";
    protected static final String BUNDLE_EXTENSION = "properties";
    private Boolean change = true;
    private transient ResourceBundle bundle;

    @PostConstruct
    public void init() {
        locale = new Locale(InventoryConstanst.local);
        FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
        _getDefaultBulder();
        HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
        SessionData sd = (SessionData) session.getAttribute("username");
        if (sd != null) {
            setListAction(sd.getSitemaps());
        }
    }

    public DefaultSubMenu addActionModel(SiteMapEntity s, DefaultSubMenu m) {
        for (SiteMapEntity d : s.getChildrent()) {
            if (d.getPrivileges() != null && !d.getPrivileges().isEmpty()) {
                if (d.getChildrent().isEmpty()) {
                    DefaultMenuItem item = new DefaultMenuItem(d.getStrName());
                    item.setUrl(d.getStrUrl());
                    item.setIcon(d.getStrIcon());
                    item.setId("sm_forms");
                    m.addElement(item);
                } else {
                    DefaultSubMenu subAction = new DefaultSubMenu(d.getStrName());
                    subAction = addActionModel(d, subAction);
                    subAction.setIcon(d.getStrIcon());
                    m.addElement(subAction);
                }
            }
        }
        return m;
    }

    public void _getModel() {
        if(listAction==null || listAction.isEmpty()){
            init();
        }
        change = false;
        model = new DefaultMenuModel();
        DefaultMenuItem home = new DefaultMenuItem(getBundle().getString("home"));
        home.setUrl("/index.jsf");
        home.setIcon(ResourceBundleUtil.getString("icon.home"));
        home.setId("sm_dashboard");
        home.setContainerStyleClass("layout-menubar-active");
        model.addElement(home);
        DefaultSubMenu listActionMenu = new DefaultSubMenu(getBundle().getString("actions"));
        for (SiteMapEntity d : getListAction()) {
            if (d.getPrivileges() != null && !d.getPrivileges().isEmpty()) {
                if (d.getChildrent().isEmpty()) {
                    DefaultMenuItem item = new DefaultMenuItem(d.getStrName());
                    item.setUrl(d.getStrUrl());
                    item.setIcon(d.getStrIcon());
                    item.setId("sm_forms");
                    listActionMenu.addElement(item);
                } else {
                    DefaultSubMenu subAction = new DefaultSubMenu(d.getStrName());
                    subAction = addActionModel(d, subAction);
                    subAction.setIcon(d.getStrIcon());
                    listActionMenu.addElement(subAction);
                }
            }
        }
        listActionMenu.setIcon(ResourceBundleUtil.getString("icon.action0"));
        Shop shop = sessionBean.getService().getAllShop(sessionBean.getShop().getShopId());
        listActionMenu.addElement(getActionModel(shop));
        model.addElement(listActionMenu);
    }

    private DefaultSubMenu getModel(Shop s) {
        DefaultSubMenu rs = new DefaultSubMenu(s.getShopName());
        DefaultMenuItem viewStock = new DefaultMenuItem(getBundle().getString("viewStock"));
        viewStock.setIcon(ResourceBundleUtil.getString("icon.viewShop4"));
        viewStock.setId("sm_forms");
        viewStock.setAjax(true);
        viewStock.setCommand("#{viewStock.viewStock}");
        viewStock.setUpdate("@([id$=viewStock])");
        viewStock.setParam("shop", s.getShopId());
        rs.addElement(viewStock);
        if (!s.getChildShops().isEmpty()) {
            DefaultSubMenu listShop = new DefaultSubMenu(getBundle().getString("viewSubStock"));
            for (Shop schild : s.getChildShops()) {
                if (schild.getChildShops() != null && !schild.getChildShops().isEmpty()) {
                    listShop.addElement(getModel(schild));
                } else {
                    DefaultMenuItem viewSub = new DefaultMenuItem(schild.getShopName());
                    viewSub.setIcon(ResourceBundleUtil.getString("icon.viewShop5"));
                    viewSub.setId("sm_forms");
                    viewSub.setAjax(true);
                    viewSub.setCommand("#{viewStock.viewStock}");
                    viewSub.setUpdate("@([id$=dtEtagSerial]), @([id$=dtStockGoods]), @([id$=listGoods]), @([id$=pnl]");
                    viewSub.setParam("shop", schild.getShopId());
                    listShop.addElement(viewSub);
                }
            }
            rs.addElement(listShop);
        }
        return rs;
    }

    private DefaultSubMenu getActionModel(Shop s) {
        DefaultSubMenu rs = new DefaultSubMenu(getBundle().getString("viewStock"));
        rs.setIcon(ResourceBundleUtil.getString("icon.viewShop1"));
        DefaultMenuItem viewStock = new DefaultMenuItem(s.getShopName());

        viewStock.setIcon(ResourceBundleUtil.getString("icon.viewShop2"));
        viewStock.setId("sm_forms");
        viewStock.setAjax(true);
        viewStock.setCommand("#{viewStock.viewStock}");
        viewStock.setUpdate("@([id$=dtEtagSerial]), @([id$=dtStockGoods]), @([id$=listGoods]), @([id$=pnl]");
        viewStock.setParam("shop", s.getShopId());
        rs.addElement(viewStock);
        if (!s.getChildShops().isEmpty()) {
            DefaultSubMenu listShop = new DefaultSubMenu(getBundle().getString("viewSubStock"));
            for (Shop schild : s.getChildShops()) {
                if (schild.getChildShops() != null && !schild.getChildShops().isEmpty()) {
                    listShop.addElement(getModel(schild));
                } else {
                    DefaultMenuItem viewSub = new DefaultMenuItem(schild.getShopName());
                    viewSub.setIcon(ResourceBundleUtil.getString("icon.viewShop3"));
                    viewSub.setId("sm_forms");
                    viewSub.setAjax(true);
                    viewSub.setCommand("#{viewStock.viewStock}");
                    viewSub.setUpdate("@([id$=dtEtagSerial]), @([id$=dtStockGoods]), @([id$=listGoods]), @([id$=pnl]");
                    viewSub.setParam("shop", schild.getShopId());
                    listShop.addElement(viewSub);
                }
            }
            listShop.setIcon(ResourceBundleUtil.getString("icon.viewShop6"));
            rs.addElement(listShop);
        }
        return rs;
    }

    /// language
    private String localeCode;
    private static Map<String, Object> countries;
    private Locale locale;

    static {
        countries = new LinkedHashMap<String, Object>();
        countries.put("Vietnam", new Locale(InventoryConstanst.local)); // label,
        // value
        countries.put("English", Locale.ENGLISH);
    }

    public Map<String, Object> getCountriesInMap() {
        return countries;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    // value change event listener
    public void countryLocaleCodeChanged(ValueChangeEvent e) {

        String newLocaleValue = e.getNewValue().toString();

        // loop country map to compare the locale code
        for (Map.Entry<String, Object> entry : countries.entrySet()) {

            if (entry.getValue().toString().equals(newLocaleValue)) {
                change = true;
                locale = (Locale) entry.getValue();
                FacesContext.getCurrentInstance().getViewRoot().setLocale(locale);
                _getDefaultBulder();
            }
        }
    }

    public String getMessage(String type, Object[] obj, boolean arg) {
        String s = null;
        try {
            if(locale ==null){
                locale = (Locale)countries.get("Vietnam");
            }
            if (!locale.equals(FacesContext.getCurrentInstance().getViewRoot().getLocale())) {
                locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
                _getDefaultBulder();
            }
            if (arg) {
                s = MessageFormat.format(getBundle().getString(type), obj);
            } else {
                s = getBundle().getString(type);
            }

        } catch (Exception ex) {

        }
        return s;
    }

    public void showMeseage(String type, String meseage) {
        try {
            if (!locale.equals(FacesContext.getCurrentInstance().getViewRoot().getLocale()))

            {
                locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
                _getDefaultBulder();
            }

            FM.showInfoMessage(getBundle().getString(type), getBundle().getString(meseage));
            if (type.equals("error")) {
                com.ftu.hibernate.HibernateUtil.closeCurrentSessions();
            } else if (type.equals("success")) {
                com.ftu.hibernate.HibernateUtil.commitCurrentSessions();
            }
        } catch (Exception ex) {

        }
    }

    public void showMeseage_(String type, String meseage) {
        try {
            if (!locale.equals(FacesContext.getCurrentInstance().getViewRoot().getLocale()))
				;
            {
                locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
                _getDefaultBulder();
            }
            FM.showInfoMessage(getBundle().getString(type), meseage);
            if (type.equals("error")) {
                com.ftu.hibernate.HibernateUtil.closeCurrentSessions();
            } else if (type.equals("success")) {
                com.ftu.hibernate.HibernateUtil.commitCurrentSessions();
            }
        } catch (Exception ex) {

        }
    }

    public void showMeseage(String type, String meseage, Object[] obj) {
        try {
            if (!locale.equals(FacesContext.getCurrentInstance().getViewRoot().getLocale()))
				;
            {
                locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
                _getDefaultBulder();
            }
            String s = getBundle().getString(meseage);
            s = MessageFormat.format(s, obj);
            FM.showInfoMessage(getBundle().getString(type), s);
            if (type.equals("error")) {
                com.ftu.hibernate.HibernateUtil.closeCurrentSessions();
            } else if (type.equals("success")) {
                com.ftu.hibernate.HibernateUtil.commitCurrentSessions();
            }
        } catch (Exception ex) {

        }
    }

    private void _getDefaultBulder() {
        try {
            locale = FacesContext.getCurrentInstance().getViewRoot().getLocale();
            String resourceName = BUNDLE_NAME + "_" + locale.toString() + "." + BUNDLE_EXTENSION;
            InputStream stream = LanguageBean.class.getResourceAsStream(resourceName);
            if (stream == null) {
                stream = FacesContext.getCurrentInstance().getExternalContext()
                        .getResourceAsStream("/resources/" + resourceName);
            }
            if (stream == null) {
                stream = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(resourceName);
            }
            if (stream != null) {
                try {
                    setBundle(new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8")));
					// Map<String, Object> session =
                    // FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
                    // session.put("bundle", bundle);
                } finally {
                    stream.close();
                }
            }
        } catch (Exception ex) {
        }
    }

    /// language
    /**
     * @return the model
     */
    public MenuModel getModel() throws Exception {
        if (change) {
            _getModel();
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
     * @return the selectId
     */
    public Long getSelectId() {
        return selectId;
    }

    /**
     * @param selectId the selectId to set
     */
    public void setSelectId(Long selectId) {
        this.selectId = selectId;
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
     * @return the locale
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @param locale the locale to set
     */
    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    /**
     * @return the bundle
     */
    public ResourceBundle getBundle() {
        if (bundle == null) {
            _getDefaultBulder();
        }
        return bundle;
    }

    /**
     * @param bundle the bundle to set
     */
    public void setBundle(ResourceBundle bundle) {
        this.bundle = bundle;
    }

    /**
     * @return the listAction
     */
    public List<SiteMapEntity> getListAction() {
        if(listAction==null || listAction.isEmpty()){
            init();
        }
        return listAction;
    }

    /**
     * @param listAction the listAction to set
     */
    public void setListAction(List<SiteMapEntity> listAction) {
        this.listAction = listAction;
    }

    /**
     * @return the bundle
     */
}
