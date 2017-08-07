/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.admin.consumer.entity.TransEntity;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.EquipmentsGroup;
import com.ftu.inventory.bo.Provider;
import com.ftu.inventory.dao.EquipmentsProfileDAO;
import com.ftu.inventory.dao.ProviderDAO;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.ShopDAO;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.ws.BusinessService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class SessionBean implements Serializable {
	public static final String SESSION_KEY = "username";

	//	@ManagedProperty(value = "#{accessController}")
	//    private AccessController accessController = new AccessController();

	private List<EquipmentsProfile> lsEquipments = new ArrayList<>();
	private List<EquipmentsProfile> lsEquipmentsKTV = new ArrayList<>();
	private List<Provider> lsProvider = new ArrayList<>();
	private List<ApDomain> listUnit = new ArrayList<>();
	private List<Provider> lsProviderStockSerial = new ArrayList<>();
	private Shop shop;
	private Staff staff;
	private List<ApDomain> listStatus;
	private List<ApDomain> listStatusNotError = new ArrayList<>();
	private List<ApDomain> listActionType;
	private List<ApDomain> lsgDomainGroup;
	private List<ApDomain> lsOrigin;
	private List<EquipmentsGroup> lsgGroup;
	private List<EquipmentsGroup> lsgGroupActive;
	private BusinessService service;
	private String notificationTransType = "";
	private List<ApDomain> listEquipmentStatus;
	private List<ApDomain> lsFieldNameActionAudit;
	@PostConstruct
	public void init() {
		if (staff != null && shop != null) {
			loadData(staff, shop);
		}
	}

	public void loadData(Staff staff, Shop shop) {
		setStaff(staff);
		setShop(shop);
		setService( new BusinessService(shop.getShopId(), staff.getStaffId()));
		setListStatus( getService().getListGoodsStatus());
		setListActionType(getService().getListActionType());
		setListUnit(getService().getListUnitType());
		setListEquipmentStatus( getService().getListGoodsStatus());
		setLsgGroup( getService().getAllEquipmentsGroup());
		setLsEquipments(getService().getAllGoods());
		setLsProvider(getService().getAllProvider());
		setLsProviderStockSerial(getService().getProviderByStockSerial());
		setLsgDomainGroup( getService().getDomainGoodsGroup());
		setLsOrigin(getService().getListOrigin());
		lsFieldNameActionAudit = getService().getFieldNameActionAudit();
		listStatusNotError.clear();
		for (ApDomain ap:listStatus) {
			if(!InventoryConstanst.GoodsStatus.ERROR.equals(ap.getValue())){
				listStatusNotError.add(ap);
			}

		}
	}

	/**
	 * @return the lsEquipments
	 */
	public List<EquipmentsProfile> getLsEquipments() {
		return lsEquipments;
	}

	/**
	 * @param aLsGoods the lsEquipments to set
	 */
	public void setLsEquipments(List<EquipmentsProfile> aLsGoods) {
		lsEquipments = aLsGoods;
	}

	/**
	 * @return the lsProvider
	 */
	public List<Provider> getLsProvider() {
		if(lsProvider==null || lsProvider.isEmpty()){
			ProviderDAO pvDAO = new ProviderDAO();
			setLsProvider(pvDAO.getAllProviders());
		}
		return lsProvider;
	}

	/**
	 * @param aLsProvider the lsProvider to set
	 */
	public void setLsProvider(List<Provider> aLsProvider) {
		lsProvider = aLsProvider;
	}

	/**
	 * @return the shop
	 */
	public Shop getShop() {
		return shop;
	}

	/**
	 * @param aShop the shop to set
	 */
	public void setShop(Shop aShop) {
		shop = aShop;
	}

	/**
	 * @return the staff
	 */
	public Staff getStaff() {
		return staff;
	}

	/**
	 * @param aStaff the staff to set
	 */
	public void setStaff(Staff aStaff) {
		staff = aStaff;
	}

	//    /**
	//     * @return the accessController
	//     */
	//    public AccessController getAccessController() {
	//        return accessController;
	//    }
	//
	//    /**
	//     * @param accessController the accessController to set
	//     */
	//    public void setAccessController(AccessController accessController) {
	//        this.accessController = accessController;
	//    }
	public static TransEntity getTransEntity() {
		SessionData output = getSessionData();
		if (output != null) {
			return output.getTransEntity();
		}
		return null;
	}

	public static SessionData getSessionData() {
		SessionData sessionData = null;
		if (FacesContext.getCurrentInstance() != null) {
			HttpSession session = getSession();
			if (session != null && session.getAttribute(SESSION_KEY) != null) {
				return (SessionData) session.getAttribute(SESSION_KEY);
			}
		}
		return sessionData;
	}

	public static HttpSession getSession() {
		if (FacesContext.getCurrentInstance() != null) {
			return (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		} else {
			return null;
		}
	}

	public boolean checkProfileHasSerial(Long gId)
	{
		return getGoods(gId.toString()).getSerial();
	}
	public EquipmentsGroup getGoodsGroup(Long gId) {
		if (lsgGroup != null && gId != null) {
			for (EquipmentsGroup g : lsgGroup) {
				if (gId.equals(g.getEquipmentsGroupId())) {
					return g;
				}
			}
		}
		return null;
	}


	public String getGoodsGroupNameByGoods(String gId) {
		EquipmentsProfile g = getGoods(gId);
		if (g == null || g.getEquipmentsGroupId() == null) {
			return "";
		}
		return getGoodsGroupName(g.getEquipmentsGroupId().toString());
	}

	public String getGoodsGroupName(String gId) {
		if (lsgGroup != null && gId != null) {
			for (EquipmentsGroup g : lsgGroup) {
				if (gId.equals(g.getEquipmentsGroupId().toString())) {
					return g.getEquipmentsGroupName();
				}
			}
		}
		return "";
	}

	public EquipmentsProfile getGoods(String gId) {
		EquipmentsProfile rs = new EquipmentsProfile();
		if (getLsEquipments() != null && gId != null) {
			for (EquipmentsProfile g : getLsEquipments()) {
				if (gId.equals(g.getProfileId().toString())) {
					return g;
				}
			}
		}
		return rs;
	}

	public Provider getProvider(String gId) {
		Provider rs = new Provider();
		if (getLsProvider() != null && gId != null) {
			for (Provider g : getLsProvider()) {
				if (gId.equals(g.getProviderId().toString())) {
					return g;
				}
			}
		}
		return rs;
	}

	public String getEquipProfileName(String gId) {
		if (getLsEquipments() != null && gId != null) {
			for (EquipmentsProfile g : getLsEquipments()) {
				if (gId.equals(g.getProfileId().toString())) {
					return g.getProfileName();
				}
			}
		}
		return "";
	}

	public String getEquipProfileCode(String gId) {
		if (getLsEquipments() != null && gId != null) {
			for (EquipmentsProfile g : getLsEquipments()) {
				if (gId.equals(g.getProfileId().toString())) {
					return g.getProfileCode();
				}
			}
		}
		return "";
	}
	public String getEquipmentSpecification(String gId) {
		if (getLsEquipments() != null && gId != null) {
			for (EquipmentsProfile g : getLsEquipments()) {
				if (gId.equals(g.getProfileId().toString())) {
					return g.getSpecification();
				}
			}
		}
		return "";
	}
	public String getEquipProfileUnit(String goodId) {
		if(goodId == null || goodId.isEmpty())  return "";
		EquipmentsProfileDAO dao = new EquipmentsProfileDAO();
		EquipmentsProfile profile = dao.findByProfileId(Long.parseLong(goodId));
		if(profile == null) return "";
		if (profile.getUnit() != null && getListUnit() != null) {
			for (ApDomain a : getListUnit()) {
				if(a.getValue()==null) continue;
				if (profile.getUnit().equals(a.getValue().toString())) {
					return a.getName();
				}
			}
		}
		return "";
	}
	public String getUnitByUnitName(String statusId) {
		if(statusId == null || statusId.isEmpty())  return "";
		if (getListUnit() != null) {
			for (ApDomain a : getListUnit()) {
				if(a.getValue() == null) continue;

				if (statusId.equals(a.getValue().toString())) {
					return a.getName();
				}
			}
		}
		return "";
	}
	
	public String getEquipProfileOrigin(String gId) {
		if (getLsEquipments() != null && gId != null) {
			for (EquipmentsProfile g : getLsEquipments()) {
				if (gId.equals(g.getProfileId().toString())) {
					return g.getOrigin();
				}
			}
		}
		return "";
	}
	
	public String getOrigin(String originId) {
		if (getLsOrigin() != null && originId != null) {
			for (ApDomain g : getLsOrigin()) {
				if(g.getValue()==null) continue;

				if (originId.equals(g.getValue().toString())) {
					return g.getName();
				}
			}
		}
		return "";
	}


	public String getProviderName(String gId) {
		if (getLsProvider() != null && gId != null) {
			for (Provider g : getLsProvider()) {
				if (gId.equals(g.getProviderId().toString())) {
					return g.getProviderName();
				}
			}
		}
		return "";
	}


	public Shop getShopById(Long sId){
		ShopDAO dao = new ShopDAO();
		if(dao.findById(sId) == null){
			return null;
		} else {
			return dao.findById(sId);
		}
	}
	public String getShopNameById(Long sId){
		ShopDAO dao = new ShopDAO();
		Shop s =  dao.findById(sId);
		if(s == null){
			return "";
		} else {
			return s.getShopName();
		}
	}
	public Staff getStaff(Long staffId){
		StaffDAO dao = new StaffDAO();
		Staff staff = dao.findById(staffId);
		if(staff == null){
			return null;
		} else {
			return staff;
		}
	}

	public String getStockStatusName(String stockStsId) {
		List<ApDomain> listStockStatus = service.getListSearchStocktatus();
		if (stockStsId != null) {
			for (ApDomain d : listStockStatus) {
				if(d.getValue()==null) continue;
				if (d.getValue().toString().equals(stockStsId)) {
					return d.getName();
				}
			}
		}
		return "";
	}
	public String getEquipStatusName(String statusId) {
		if (statusId != null) {
			for (ApDomain d : listEquipmentStatus) {
				if(d.getValue()==null) continue;
				if (d.getValue().toString().equals(statusId)) {
					return d.getName();
				}
			}
		}
		return "";
	}
	public String getFieldNameActionAudit(String fieldId) {
		if (fieldId != null) {
			for (ApDomain d : lsFieldNameActionAudit) {
				if(d.getValue()==null) continue;
				if (d.getValue().toString().equals(fieldId)) {
					return d.getName();
				}
			}
		}
		return "";
	}


	/**
	 * @return the listStatus
	 */
	public List<ApDomain> getListStatus() {
		return listStatus;
	}

	/**
	 * @param listStatus the listStatus to set
	 */
	public void setListStatus(List<ApDomain> listStatus) {
		this.listStatus = listStatus;
	}

	/**
	 * @return the lsgGroup
	 */
	public List<EquipmentsGroup> getLsgGroup() {
		lsgGroup = getService().getAllEquipmentsGroup();
		return lsgGroup;
	}
	public List<EquipmentsGroup> getLsgGroupOrder() {
		lsgGroup = getService().getAllEquipmentsGroupOrderCode();
		return lsgGroup;
	}

	public List<EquipmentsGroup> getLsgGroupActive() {
		lsgGroupActive = getService().getAllEquipmentsGroupActive();
		return lsgGroupActive;
	}

	public void setLsgGroupActive(List<EquipmentsGroup> lsgGroupActive) {
		this.lsgGroupActive = lsgGroupActive;
	}

	/**
	 * @param lsgGroup the lsgGroup to set
	 */
	public void setLsgGroup(List<EquipmentsGroup> lsgGroup) {
		this.lsgGroup = lsgGroup;
	}

	/**
	 * @return the service
	 */
	public BusinessService getService() {
		return service;
	}

	/**
	 * @param service the service to set
	 */
	public void setService(BusinessService service) {
		this.service = service;
	}

	/**
	 * @return the notificationTransType
	 */
	public String getNotificationTransType() {
		return notificationTransType;
	}

	/**
	 * @param notificationTransType the notificationTransType to set
	 */
	public void setNotificationTransType(String notificationTransType) {
		this.notificationTransType = notificationTransType;
	}

	public List<ApDomain> getListActionType() {
		return listActionType;
	}
	public List<ApDomain> getListActionTypeSetUp() {
		List<ApDomain> listUpdate = new ArrayList<>();
		for (ApDomain ap:listActionType) {
			if(ap==null||ap.getValue()==null) continue;
			if(ap.getValue().equals(InventoryConstanst.ACTION_TYPE.TYPE_1L)
					|| ap.getValue().equals(InventoryConstanst.ACTION_TYPE.TYPE_2L)
					|| ap.getValue().equals(InventoryConstanst.ACTION_TYPE.TYPE_3L)
					|| ap.getValue().equals(InventoryConstanst.ACTION_TYPE.TYPE_4L)
					|| ap.getValue().equals(InventoryConstanst.ACTION_TYPE.TYPE_5L)){
				listUpdate.add(ap);
			}
		}
		return listUpdate;
	}

	public void setListActionType(List<ApDomain> listActionType) {
		this.listActionType = listActionType;
	}

	public List<ApDomain> getListEquipmentStatus() {
		return listEquipmentStatus;
	}

	public void setListEquipmentStatus(List<ApDomain> listEquipmentStatus) {
		this.listEquipmentStatus = listEquipmentStatus;
	}

	public List<Provider> getLsProviderStockSerial() {
		return lsProviderStockSerial;
	}

	public void setLsProviderStockSerial(List<Provider> lsProviderStockSerial) {
		this.lsProviderStockSerial = lsProviderStockSerial;
	}

	/**
	 * @return the lsgDomainGroup
	 */
	public List<ApDomain> getLsgDomainGroup() {
		return lsgDomainGroup;
	}

	/**
	 * @param lsgDomainGroup the lsgDomainGroup to set
	 */
	public void setLsgDomainGroup(List<ApDomain> lsgDomainGroup) {
		this.lsgDomainGroup = lsgDomainGroup;
	}

	/**
	 * @return the listUnit
	 */
	public List<ApDomain> getListUnit() {
		return listUnit;
	}

	/**
	 * @param listUnit the listUnit to set
	 */
	public void setListUnit(List<ApDomain> listUnit) {
		this.listUnit = listUnit;
	}

	/**
	 * @return the lsgOrigin
	 */
	public List<ApDomain> getLsOrigin() {
		return lsOrigin;
	}

	/**
	 * @param lsgOrigin the lsgOrigin to set
	 */
	public void setLsOrigin(List<ApDomain> lsgOrigin) {
		this.lsOrigin = lsgOrigin;
	}

	public List<ApDomain> getLsFieldNameActionAudit() {
		return lsFieldNameActionAudit;
	}

	public void setLsFieldNameActionAudit(List<ApDomain> lsFieldNameActionAudit) {
		this.lsFieldNameActionAudit = lsFieldNameActionAudit;
	}

	public List<ApDomain> getListStatusNotError() {
		return listStatusNotError;
	}

	public void setListStatusNotError(List<ApDomain> listStatusNotError) {
		this.listStatusNotError = listStatusNotError;
	}

	public List<EquipmentsProfile> getLsEquipmentsKTV() {
		lsEquipmentsKTV = getService().getAllGoods();
		return lsEquipmentsKTV;
	}

	public void setLsEquipmentsKTV(List<EquipmentsProfile> lsEquipmentsKTV) {
		this.lsEquipmentsKTV = lsEquipmentsKTV;
	}
}
