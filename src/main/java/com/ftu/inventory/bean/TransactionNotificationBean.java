/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SiteMapEntity;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionNotification;
import com.ftu.language.LanguageBean;
import com.ftu.login.AccessController;
import com.ftu.staff.bo.ApDomain;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

/**
 *
 * @author Cao Cuong
 */
@ManagedBean(name = "transactionNotificationBean", eager = true)
@SessionScoped
public class TransactionNotificationBean implements Serializable {

	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean ;//= new SessionBean();
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean = new LanguageBean();
	private List<TransactionNotification> listNotification;
	private Long total;

	@PostConstruct
	public void init() {
//		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		setTotal(0L);
		List<String> lsUrl = new ArrayList<>();
		if(languageBean.getListAction()!=null){
//			System.out.println("======================================"+languageBean.getListAction().size());
			for (SiteMapEntity sm : languageBean.getListAction()) {
				if (sm.getChildrent() != null) {
					lsUrl.addAll(getBySiteMap(sm));
				}
				else if(sm.getStrUrl().contains(InventoryConstanst.TransactionNotification.searchTransaction))
				{
					lsUrl.add(sm.getStrUrl());
				}
			}
		}
		//        lsUrl.add("/approve-goods.xhtml");
		//        lsUrl.add("/ex-pshop-approve.xhtml");
		//        lsUrl.add("/ex-subshop-ex-goods.xhtml");
		//        lsUrl.add("/ex-pshop-im-goods.xhtml");
		//        lsUrl.add("/im-pshop-approve.xhtml");
		//        lsUrl.add("/im-pshop-ex-goods.xhtml");
		//        lsUrl.add("/im-subshop-im-goods.xhtml");
		listNotification = sessionBean.getService().getListNotification(lsUrl);
//		System.out.println("======================================"+listNotification.size());
		if (listNotification != null) {
			List<TransactionNotification> tempLisNoti = new ArrayList<>();//Huy: neu count == 0 ~> khong hien thi
			for (TransactionNotification tn : getListNotification()) {
				if (tn.getTypes() != null && tn.getTypes().length > 0) {
					tn.setCount(sessionBean.getService().searchTransactionActionSize(sessionBean.getShop().getShopId(), tn.getIsImportStock().equals(1L), tn.getTypes(), tn.getTransactionStatus()));
					total += tn.getCount();
				}

				if((tn.getCount()!=null&&tn.getCount() != 0)||tn.getTransactionStatus().equals(InventoryConstanst.TransactionStatus.CANCEL)){
					tempLisNoti.add(tn);
				}
			}
			listNotification = tempLisNoti;
		} else {
			listNotification = new ArrayList<>();
		}
	}

	public String action(String url, String type) {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		sessionBean.setNotificationTransType(type.isEmpty()?"0":type);
		init();
		if (url.equals(InventoryConstanst.TransactionNotification.imPshopExGoods)) {
			ImPShopExGoods imPShopExGoods
			= (ImPShopExGoods) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "imPShopExGoods");
			if (imPShopExGoods != null) {
				imPShopExGoods.init();
			}
		} else if (url.equals(InventoryConstanst.TransactionNotification.imSubShopImGoods)) {
			ImSubShopImGoods imSubShopImGoods
			= (ImSubShopImGoods) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "imSubShopImGoods");
			if (imSubShopImGoods != null) {
				imSubShopImGoods.init();
			}
		} else if (url.equals(InventoryConstanst.TransactionNotification.exPShopApprove)) {
			ExPShopApprove exPShopApprove
			= (ExPShopApprove) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "exPShopApprove");
			if (exPShopApprove != null) {
				exPShopApprove.init();
			}
		} else if (url.equals(InventoryConstanst.TransactionNotification.exSubShopExGoods)) {
			ExSubShopExGoods exSubShopExGoods
			= (ExSubShopExGoods) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "exSubShopExGoods");
			if (exSubShopExGoods != null) {
				exSubShopExGoods.init();
			}
		} else if (url.equals(InventoryConstanst.TransactionNotification.imSubShopImGoods)) {
			ImSubShopImGoods imSubShopImGoods
			= (ImSubShopImGoods) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "imSubShopImGoods");
			if (imSubShopImGoods != null) {
				imSubShopImGoods.init();
			}
		}else if (url.equals(InventoryConstanst.TransactionNotification.exPshopImGoods)) {
			ExPShopImGoods exPshopImGoods
			= (ExPShopImGoods) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "exPShopImGoods");
			if (exPshopImGoods != null) {
				exPshopImGoods.init();
			}
		}
		else if (url.equals(InventoryConstanst.TransactionNotification.searchTransaction)) {
			SearchTransaction searchTransaction
			= (SearchTransaction) facesContext.getApplication()
			.getVariableResolver().resolveVariable(facesContext, "searchTransaction");
			if (searchTransaction != null) {           
				searchTransaction.init();
			}
		}
		return url + "?faces-redirect=true";
	}

	private List<String> getBySiteMap(SiteMapEntity sm) {
		List<String> rs = new ArrayList<>();
		if ((sm.getChildrent() == null || sm.getChildrent().isEmpty())&&(sm.getStrUrl()!=null)) {
			rs.add(sm.getStrUrl());
		} else {
			for (SiteMapEntity child : sm.getChildrent()) {
				rs.addAll(getBySiteMap(child));
			}
		}
		return rs;
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

	/**
	 * @return the listNotification
	 */
	public List<TransactionNotification> getListNotification() {
		return listNotification;
	}

	/**
	 * @param listNotification the listNotification to set
	 */
	public void setListNotification(List<TransactionNotification> listNotification) {
		this.listNotification = listNotification;
	}

	/**
	 * @return the total
	 */
	public Long getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Long total) {
		this.total = total;
	}
}
