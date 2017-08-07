/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.login;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import com.ftu.utils.DateTimeUtils;
import org.primefaces.context.RequestContext;

import com.ftu.admin.component.FM;
import com.ftu.admin.consumer.AuthenticationConsumer;
import com.ftu.admin.consumer.entity.AttributeEntity;
import com.ftu.admin.consumer.entity.AuthenticationOutput;
import com.ftu.admin.consumer.entity.IdentityEntity;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.admin.consumer.utils.ApplicationUtil;
import com.ftu.admin.consumer.utils.ConsumerPropeties;
import com.ftu.admin.consumer.utils.StaticData;
import com.ftu.admin.consumer.utils.StringUtil;
import com.ftu.admin.controller.CommonFacesContext;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bean.SessionBean;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.utils.ResourceBundleUtil;
import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;

@ManagedBean(name = "accessController")
@SessionScoped
public class AccessController implements Serializable {
	public IdentityEntity identityEntity = new IdentityEntity();
	private boolean rememberPasswordLg = false;
	private String strPassword = null;
	private Staff staff;
	private Shop shop;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;

	public SessionBean getSessionBean() {
		return sessionBean;
	}

	public void setSessionBean(SessionBean sessionBean) {
		this.sessionBean = sessionBean;
	}

	@PostConstruct
	public void init() {
	}
	public AccessController() {
		getRememberPassword();
	}
	public void rememberPasswordLogin(String strUsername, String strPassword) {

		Cookie ckIdentity = new Cookie("IDENTITY", strUsername);
		Cookie ckIdentityPassword = new Cookie("IDENTITY_PASSWORD", strPassword);
		if (rememberPasswordLg) {
			CommonFacesContext.getHttpServletResponse().addCookie(ckIdentity);
			CommonFacesContext.getHttpServletResponse().addCookie(ckIdentityPassword);
		} else {
			Map<String, Object> cookieProperties = new HashMap<>();
			cookieProperties.put("maxAge", new Integer(0));
			CommonFacesContext.getExternalContext().addResponseCookie("IDENTITY", "", cookieProperties);
			CommonFacesContext.getExternalContext().addResponseCookie("IDENTITY_PASSWORD", "", cookieProperties);
		}
	}
	public void getRememberPassword() {
		rememberPasswordLg = false;
		Cookie[] cookiesArr = CommonFacesContext.getHttpServletRequest().getCookies();
		if (cookiesArr != null) {
			for (Cookie cookie : cookiesArr) {
				if ("IDENTITY".equals(cookie.getName())) {
					identityEntity.setUsername(cookie.getValue());
//					rememberPasswordLg = true;
				}
				if ("IDENTITY_PASSWORD".equals(cookie.getName())) {
					strPassword = cookie.getValue();
					identityEntity.setPassword(strPassword);
//					rememberPasswordLg = true;
				}
			}
		}
	}


	public IdentityEntity getIdentityEntity() {
		return identityEntity;
	}

	public void setIdentityEntity(IdentityEntity identityEntity) {
		this.identityEntity = identityEntity;
	}

	public void login() {
		try {
			if (StringUtil.stringIsNullOrEmty((identityEntity.getUsername()))) {
				FM.showInfoMessage("", ResourceBundleUtil.getString("userNamReq"));
				HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
						.getSession(false);
				session.invalidate();
			} else {
				StaffDAO stDAO = new StaffDAO();
				setStaff(stDAO.getByUserName(identityEntity.getUsername()));
				Date dateCurrent = DateTimeUtils.convertDateTimeToDate(new Date());
				if (getStaff() != null) {
					if(getStaff()!=null && InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(getStaff().getStaffStatus())){
						FM.showInfoMessage("", ResourceBundleUtil.getString("in_active.in.system"));
						HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
								.getSession(false);
						session.invalidate();
						return;
					}else if(getStaff() != null && getStaff().getEndDate()!= null
							&& dateCurrent.compareTo(getStaff().getEndDate()) > 0){
						FM.showInfoMessage("", ResourceBundleUtil.getString("in_active.in.system.out.date"));
						HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
								.getSession(false);
						session.invalidate();
						return;
					} else if(getStaff() != null && getStaff().getStartDate()!= null
							&& dateCurrent.compareTo(getStaff().getStartDate()) < 0){
						FM.showInfoMessage("", ResourceBundleUtil.getString("in_active.in.system.out.date"));
						HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
								.getSession(false);
						session.invalidate();
						return;
					} else{
						ShopDAO sDAO = new ShopDAO();
						Shop s = (sDAO.findById(staff.getShopId()));
						if(s!=null && InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(s.getShopStatus())){
							FM.showInfoMessage("", ResourceBundleUtil.getString("in_active.in.system"));
							HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
									.getSession(false);
							session.invalidate();
							return;
						}
					}
					// Nologin
					// HttpSession session = (HttpSession)
					// FacesContext.getCurrentInstance().getExternalContext()
					// .getSession(false);
					// ShopDAO sDAO = new ShopDAO();
					// setShop(sDAO.findById(staff.getShopId()));
					// sessionBean.loadData(staff, shop);
					// SessionData sessionData = new SessionData();
					// sessionData.setSitemaps(new ArrayList<SiteMapEntity>());
					// session.setAttribute("username", new SessionData());
					// FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
					//
					String password = StringUtil.stringIsNullOrEmty(identityEntity.getPassword()) ? null
							: StringUtil.encrypt(identityEntity.getPassword(),
									ResourceBundleUtil.getString("conf.encrypt.algorithm"));

					TransEntity transEntity = new TransEntity();
					transEntity.setRemoteAddr(CommonFacesContext.getRemoteAddr());
					AuthenticationOutput output = AuthenticationConsumer.login(transEntity,
							identityEntity.getUsername(), password, ResourceBundleUtil.getString("consumer.app"));
					if (StaticData.RESPONSE_STATUS_SUCCESS.equals(output.getStatus())) {
						if (rememberPasswordLg) {
							rememberPasswordLogin(identityEntity.getUsername(), identityEntity.getPassword());
						} else {
//                            rememberPasswordLogin(null, null);
						}
						HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
								.getSession(false);
						SessionData sessionData = new SessionData();
						sessionData.setIdentityEntity(output.getIdentityEntity());
						sessionData.setTransEntity(output.getTransEntity());
						if ((output.getTransEntity().getInterval() > 0)) {
							session.setMaxInactiveInterval(output.getTransEntity().getInterval());
						}
						session.setAttribute("username", sessionData);
						sessionData.getTransEntity().setChanglePass(false);
						sessionData
								.setSitemaps(ApplicationUtil.getSitemapForSession(InventoryConstanst.RoleKey.ACCESS));
						if (sessionData.getSitemaps() == null) {
							FM.showInfoMessage("", ResourceBundleUtil.getString("wrong.pm"));
							return;
						}
						String s = null;
						if (session.getAttribute("urlBlock") != null) {
							s = session.getAttribute("urlBlock").toString();
						}
						ShopDAO sDAO = new ShopDAO();
						setShop(sDAO.findById(staff.getShopId()));
						if (shop == null) {
							FacesContext.getCurrentInstance().getExternalContext().redirect("block-access.xhtml");
						}
						sessionBean.loadData(staff, shop);
						if (s != null && !s.isEmpty()) {
							if (ValidateUser.checkPermission(sessionData.getSitemaps(), s)) {
								FacesContext.getCurrentInstance().getExternalContext().redirect(s);
							} else {
								FacesContext.getCurrentInstance().getExternalContext().redirect("block-access.xhtml");
							}
						} else {
							AttributeEntity attributeEntity = output.getIdentityEntity()
									.getAttributeEntityByCode("IDENTITY_CHANGE_PASS_FIRST_LOGIN");
							if (attributeEntity != null && "1".equals(attributeEntity.getValue())) {
								RequestContext.getCurrentInstance().update("frmChangePwd:dlgPanelPwdInfo");
								RequestContext.getCurrentInstance().execute("PF('dlgChangePassword').show();");
								sessionData.getTransEntity().setChanglePass(true);
							} else {
								sessionData.getTransEntity().setChanglePass(false);
								FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
							}
							// FacesContext.getCurrentInstance().getExternalContext().redirect("index.xhtml");
						}
					} else {
						FM.showInfoMessage("",output.getErrorMessage());// ResourceBundleUtil.getString("wrong.us"));//
						HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
								.getSession(false);
						session.invalidate();
					}
				} else {
					FM.showInfoMessage("", ResourceBundleUtil.getString("not.in.system"));
					HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
							.getSession(false);
					session.invalidate();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			FM.showInfoMessage("", ResourceBundleUtil.getString("logIn.err"));
		}
	}

	// logout event, invalidate session
	public String logout() throws Exception {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		SessionData sessionData = (SessionData) session.getAttribute("username");
		if (sessionData != null) {
			boolean rs = AuthenticationConsumer.logout(sessionData.getTransEntity());
			if (rs) {
				identityEntity = new IdentityEntity();
				// HttpSession session = (HttpSession)
				// FacesContext.getCurrentInstance().getExternalContext()
				// .getSession(false);
				session.invalidate();
				// TransData.transId = null;
				return "login?faces-redirect=true";
			} else {
				FM.showInfoMessage("", ResourceBundleUtil.getString("logOut.err"));
				return null;
			}
		}
		return null;
	}

	/**
	 * @return the staff
	 */
	public Staff getStaff() {
		return staff;
	}

	/**
	 * @param staff
	 *            the staff to set
	 */
	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	/**
	 * @return the shop
	 */
	public Shop getShop() {
		return shop;
	}

	/**
	 * @param shop
	 *            the shop to set
	 */
	public void setShop(Shop shop) {
		this.shop = shop;
	}

	// --
	public String getTransChannel() {
		String transIdEncrypted;
		try {
			transIdEncrypted = SessionBean.getTransEntity().getTransId().replaceAll("-", "");
			transIdEncrypted = transIdEncrypted.replaceAll("[0-5]+", "a");
			return transIdEncrypted;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

	public String getPushServerUrl() {
		return ConsumerPropeties.getProperty("resource.eim.push.url");
	}
	public boolean isRememberPasswordLg() {
		return rememberPasswordLg;
	}

	public void setRememberPasswordLg(boolean rememberPasswordLg) {
		this.rememberPasswordLg = rememberPasswordLg;
	}

	public String getStrPassword() {
		return strPassword;
	}

	public void setStrPassword(String strPassword) {
		this.strPassword = strPassword;
	}
}
