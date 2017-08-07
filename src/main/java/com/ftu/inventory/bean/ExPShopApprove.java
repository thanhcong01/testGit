/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.language.LanguageBean;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.ComponentUtils;
import com.ftu.utils.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class ExPShopApprove implements Serializable {

	private String orderCode;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
	private LazyDataModel<TransactionAction> lsData;
	private List<Shop> childShop;
	private List<ApDomain> listReason = new ArrayList<>();
	private Long reasonId;
	private Long typeId;
	private List<ApDomain> listTransType;
	private StockTransaction st;
	private Long subShopId;
	private TransactionAction ta = new TransactionAction();
	private StockTransactionDetail stDetail;
	private Long status;
	private List<ApDomain> listStatus;
	private boolean disableApp;
	private boolean disableDel;
	private int sizers;
	boolean fist = true;
	private Date createDate;
	private Long goodStatus;
	private String fromSerial;
	private String toSerial;
	private List<StockGoodsInvSerialDTO> listSerial = new ArrayList<>();
	private List<StockGoodsInvSerialDTO> setLsDataSearch = new ArrayList<>();
	private List<StockGoodsInvSerialDTO> setLsDataSelect = new ArrayList<>();
	private List<SerialA> setAnaSerial;
	private String ACTION_CODE;
	private String confirm;
	private String description;
	private boolean disableViewDetail;

	//huy
	private String subShop;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		setDisableApp(true);
		disableDel = true;
		listStatus = sessionBean.getService().getImApproveStatus(true);
		listTransType = getSessionBean().getService().getListExSubShopReasons();
		setChildShop(getSessionBean().getService().getChildShop());
		if (listStatus != null && !listStatus.isEmpty()) {
			status = listStatus.get(0).getValue();
		}
		String type = sessionBean.getNotificationTransType();
		if (type != null && !type.isEmpty()) {
			for (ApDomain a : listTransType) {
				if (a.getValue().toString().equals(type)) {
					typeId = a.getValue();
					break;
				}
			}
			sessionBean.setNotificationTransType("");
		}
		search();
	}

	public void SubShopSelect() {
		if (childShop != null) {
			for (Shop shop : childShop) {
				if (shop.getShopName().equals(subShop)) {
					subShopId = shop.getShopId();
					break;
				}
			}
		}
	}

	public List<String> CompleteSubShop(String query) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData data = (SessionData) session.getAttribute("username");
			List<String> filteredThemes = new ArrayList<String>();

			for (int i = 0; i < childShop.size(); i++) {
				Shop skin = childShop.get(i);
				if (skin.getShopName().toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(skin.getShopName());
				}
				if (skin.getShopName().toLowerCase().equals(query.toLowerCase())) {
					subShopId = skin.getShopId();
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void search() {
		if((subShop == null) || (subShop.equals(""))){
			subShopId = null;
		}
		if (!fist) {
			ComponentUtils cu = new ComponentUtils();
			DataTable dt = (DataTable) cu.findComponent("dtta");
			if (dt != null) {
				dt.setFirst(0);
			}
		}
		fist = false;
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setTransactionType(getTypeId() == null ? null : getTypeId().toString());
		tas.setStatus(status == null ? null : status.toString());
		tas.setCreateShopId(subShopId);
		tas.setFromShopId(subShopId);
		tas.setToShopId(sessionBean.getShop().getShopId());
		tas.setCreateDatetime(createDate);
		tas.setReasonId(reasonId);
		changeType();
		lsData = new LazyTransActionModel(tas,childShop, listStatus, getListTransType(), null, listReason);
		ta = new TransactionAction();
		st = null;
		stDetail = null;
		disableApp = true;
		disableDel = true;
	}

	public void changeReason() {
		if (reasonId > 0) {
			typeId = Long.parseLong(sessionBean.getService().getReasonByValue(reasonId).getCode());
			listReason = sessionBean.getService().getListReason(typeId.toString());
		}
	}

	public void changeType() {
		reasonId = 0L;
		if (getTypeId() != null && getTypeId() > 0) {
			listReason = sessionBean.getService().getListReason(getTypeId().toString());
		} else {
			listReason = new ArrayList<>();
			for (ApDomain d : getListTransType()) {
				listReason.addAll(sessionBean.getService().getListReason(d.getValue().toString()));
			}
		}
	}

	public List<String> completeOrder(String oder) {
		orderCode = oder;
		List<String> rs = new ArrayList<>();
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setToShopId(sessionBean.getShop().getShopId());
		List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, getListTransType(),
				listStatus, 0, 10);
		refresh();
		if (lsTaa != null) {
			for (TransactionAction t : lsTaa) {
				rs.add(t.getTransactionActionCode());
			}
		}
		return rs;
	}

	public void oderSelect(SelectEvent event) {
		search();
		status = 0L;
		createDate = null;
		reasonId = 0L;
		subShopId = 0L;
		setTypeId((Long) 0L);
	}

	public void refresh() {
		ta = new TransactionAction();
		st = null;
		lsData = null;
		stDetail = null;
	}

	public void view() {
		disableApp = true;
		disableDel = true;
		disableViewDetail = true;
		stDetail = null;
		setSt(getSessionBean().getService().getLastByTransaction(ta.getTransactionActionId()));
		if (st != null) {
			getSt().setLsDetail(getSessionBean().getService().getDetailsByStock(getSt().getStockTransactionId()));
		}
		if (!InventoryConstanst.TransactionStatus.APPROVE.equals(ta.getStatus()) && st != null
				&& !st.getLsDetail().isEmpty()) {
			disableApp = false;
		}
		if (!InventoryConstanst.TransactionStatus.APPROVE.equals(ta.getStatus())) {
			disableDel = false;
		}
		if (InventoryConstanst.TransactionStatus.CANCEL.equals(ta.getStatus())) {
			disableDel = true;
			disableApp = true;
		}

	}

	public void viewClick(/*SelectEvent event*/) {
		//stDetail = (StockTransactionDetail) event.getObject();
		
		if (stDetail != null && sessionBean.getGoods(stDetail.getGoodsId().toString()).getSerial()) {
			StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", stDetail.getGoodsStatus(),
					InventoryConstanst.StockStatus.BLOCK_XT, null, null, stDetail.getGoodsId());
			sgis.setCurrentTaId(ta.getTransactionActionId());
			setListSerial(sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null, null, null));
			if (listSerial != null) {
				stDetail.getSetSerial().addAll(getListSerial());
				searchSerial();
			}
			fromSerial = "";
			toSerial = "";
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "/ex-pshop-approve-sub.xhtml?faces-redirect=false");
		}
	}

	public void searchSerial() {
		if (ta == null || stDetail == null) {
			return;
		}
		setLsDataSearch = new ArrayList<>();
		setLsDataSelect = new ArrayList<>();
		if (fromSerial == null || fromSerial.trim().isEmpty() ) {

			//languageBean.showMeseage("Info", "fromSerialReq");
			setLsDataSearch.addAll(listSerial);
			return;

		}
		if (stDetail != null && !stDetail.getSetSerial().isEmpty()) {
			for (StockGoodsInvSerialDTO ss : stDetail.getSetSerial()) {
				if (ss.getSerial().contains(fromSerial)) 
				{
					setLsDataSearch.add(ss);
				}
			}
		}

		fromSerial = "";
	}

	public void save() {
		RequestContext context = RequestContext.getCurrentInstance();
		setACTION_CODE(InventoryConstanst.RoleKey.APPROVE);
		confirm = languageBean.getBundle().getString("confirmApp");
		description = "";
		context.execute("PF('cfExPApp').show();");

	}

	public void action() {
		RequestContext context = RequestContext.getCurrentInstance();
		try {
			boolean k = false;
			ta.setDescription(ta.getDescription() == null ? ""
					: ta.getDescription() + "\n" + sessionBean.getStaff().getUserName() + ": " + description);
			if (getACTION_CODE().equals(InventoryConstanst.RoleKey.APPROVE)) {
				ta.setAssessmentStaffId(sessionBean.getStaff().getStaffId());
				k = sessionBean.getService().exAppApprove(ta, st, InventoryConstanst.StockStatus.BLOCK_XT);
			} else if (getACTION_CODE().equals(InventoryConstanst.RoleKey.REJECT)) {
				ta.setAssessmentStaffId(sessionBean.getStaff().getStaffId());
				k = sessionBean.getService().exReject(ta, st, InventoryConstanst.StockStatus.BLOCK_XT);
			}
			if (k) {
				getLanguageBean().showMeseage("success", "succesProces");
			} else {
				getLanguageBean().showMeseage("error", "errorValidProcess");
			}
			search();
			transactionNotificationBean.init();
			context.execute("PF('cfExPApp').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorValidProcess");
		}

	}

	public void reject() {
		RequestContext context = RequestContext.getCurrentInstance();
		setACTION_CODE(InventoryConstanst.RoleKey.REJECT);
		description = "";
		confirm = languageBean.getBundle().getString("confirmRej");
		context.execute("PF('cfExPApp').show();");
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode
	 *            the orderCode to set
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * @return the sessionBean
	 */
	public SessionBean getSessionBean() {
		return sessionBean;
	}

	/**
	 * @param sessionBean
	 *            the sessionBean to set
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
	 * @param languageBean
	 *            the languageBean to set
	 */
	public void setLanguageBean(LanguageBean languageBean) {
		this.languageBean = languageBean;
	}

	/**
	 * @return the transactionNotificationBean
	 */
	public TransactionNotificationBean getTransactionNotificationBean() {
		return transactionNotificationBean;
	}

	/**
	 * @param transactionNotificationBean the transactionNotificationBean to set
	 */
	public void setTransactionNotificationBean(TransactionNotificationBean transactionNotificationBean) {
		this.transactionNotificationBean = transactionNotificationBean;
	}

	/**
	 * @return the reasonId
	 */
	public Long getReasonId() {
		return reasonId;
	}

	/**
	 * @param reasonId
	 *            the reasonId to set
	 */
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	/**
	 * @return the lsData
	 */
	public LazyDataModel<TransactionAction> getLsData() {
		return lsData;
	}

	/**
	 * @param lsData
	 *            the lsData to set
	 */
	public void setLsData(LazyDataModel<TransactionAction> lsData) {
		this.lsData = lsData;
	}

	/**
	 * @return the childShop
	 */
	public List<Shop> getChildShop() {
		return childShop;
	}

	/**
	 * @param childShop
	 *            the childShop to set
	 */
	public void setChildShop(List<Shop> childShop) {
		this.childShop = childShop;
	}

	/**
	 * @return the listReason
	 */
	public List<ApDomain> getListReason() {
		return listReason;
	}

	/**
	 * @param listReason
	 *            the listReason to set
	 */
	public void setListReason(List<ApDomain> listReason) {
		this.listReason = listReason;
	}

	/**
	 * @return the st
	 */
	public StockTransaction getSt() {
		return st;
	}

	/**
	 * @param st
	 *            the st to set
	 */
	public void setSt(StockTransaction st) {
		this.st = st;
	}

	/**
	 * @return the subShopId
	 */
	public Long getSubShopId() {
		return subShopId;
	}

	/**
	 * @param subShopId
	 *            the subShopId to set
	 */
	public void setSubShopId(Long subShopId) {
		this.subShopId = subShopId;
	}

	/**
	 * @return the ta
	 */
	public TransactionAction getTa() {
		return ta != null ? ta : new TransactionAction();
	}

	/**
	 * @param ta
	 *            the ta to set
	 */
	public void setTa(TransactionAction ta) {
		this.ta = ta;
	}

	/**
	 * @return the status
	 */
	public Long getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(Long status) {
		this.status = status;
	}

	/**
	 * @return the listStatus
	 */
	public List<ApDomain> getListStatus() {
		return listStatus;
	}

	/**
	 * @param listStatus
	 *            the listStatus to set
	 */
	public void setListStatus(List<ApDomain> listStatus) {
		this.listStatus = listStatus;
	}

	/**
	 * @return the disableApp
	 */
	public boolean isDisableApp() {
		return disableApp;
	}

	/**
	 * @param disableApp
	 *            the disableApp to set
	 */
	public void setDisableApp(boolean disableApp) {
		this.disableApp = disableApp;
	}

	/**
	 * @return the stDetail
	 */
	public StockTransactionDetail getStDetail() {
		return stDetail;
	}

	/**
	 * @param stDetail
	 *            the stDetail to set
	 */
	public void setStDetail(StockTransactionDetail stDetail) {
		this.stDetail = stDetail;
	}

	/**
	 * @return the sizers
	 */
	public int getSizers() {
		if (stDetail == null || stDetail.getSetSerial() == null || stDetail.getSetSerial().isEmpty()) {
			sizers = 0;
		} else {
			sizers = stDetail.getSetSerial().size();
		}
		return sizers;
	}

	/**
	 * @param sizers
	 *            the sizers to set
	 */
	public void setSizers(int sizers) {
		this.sizers = sizers;
	}

	/**
	 * @return the listSerial
	 */
	public List<StockGoodsInvSerialDTO> getListSerial() {
		return listSerial;
	}

	/**
	 * @param listSerial
	 *            the listSerial to set
	 */
	public void setListSerial(List<StockGoodsInvSerialDTO> listSerial) {
		this.listSerial = listSerial;
	}

	/**
	 * @return the createDate
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * @param createDate
	 *            the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the disableDel
	 */
	public boolean isDisableDel() {
		return disableDel;
	}

	/**
	 * @param disableDel
	 *            the disableDel to set
	 */
	public void setDisableDel(boolean disableDel) {
		this.disableDel = disableDel;
	}

	/**
	 * @return the fromSerial
	 */
	public String getFromSerial() {
		return fromSerial;
	}

	/**
	 * @param fromSerial
	 *            the fromSerial to set
	 */
	public void setFromSerial(String fromSerial) {
		this.fromSerial = fromSerial;
	}

	/**
	 * @return the toSerial
	 */
	public String getToSerial() {
		return toSerial;
	}

	/**
	 * @param toSerial
	 *            the toSerial to set
	 */
	public void setToSerial(String toSerial) {
		this.toSerial = toSerial;
	}

	/**
	 * @return the setAnaSerial
	 */
	public List<SerialA> getSetAnaSerial() {
		setAnaSerial = new ArrayList<>();
		if (getListSerial() != null && !listSerial.isEmpty()) {
			AnalysisSerial as = new AnalysisSerial(null, getListSerial());
			setAnaSerial = as.analysisInvSerial();
		}
		return setAnaSerial;
	}
	public void viewSerial(SelectEvent event) throws IOException {
		StockTransactionDetail std = (StockTransactionDetail) event.getObject();
		List<EquipmentsProfile> lstEquipmentProfile= sessionBean.getLsEquipments();
		if (std != null && std.getGoodsId() != null) {
			for (EquipmentsProfile profile : lstEquipmentProfile) {
				if (profile.getProfileId().equals(std.getGoodsId())
						&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())) {
					disableViewDetail = false;
					return;
				}
			}
		}
		disableViewDetail = true;
	}
	/**
	 * @param setAnaSerial
	 *            the setAnaSerial to set
	 */
	public void setSetAnaSerial(List<SerialA> setAnaSerial) {
		this.setAnaSerial = setAnaSerial;
	}

	/**
	 * @return the listTransType
	 */
	public List<ApDomain> getListTransType() {
		return listTransType;
	}

	/**
	 * @param listTransType
	 *            the listTransType to set
	 */
	public void setListTransType(List<ApDomain> listTransType) {
		this.listTransType = listTransType;
	}

	/**
	 * @return the typeId
	 */
	public Long getTypeId() {
		return typeId;
	}

	/**
	 * @param typeId
	 *            the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	/**
	 * @return the ACTION_CODE
	 */
	public String getACTION_CODE() {
		return ACTION_CODE;
	}

	/**
	 * @param ACTION_CODE
	 *            the ACTION_CODE to set
	 */
	public void setACTION_CODE(String ACTION_CODE) {
		this.ACTION_CODE = ACTION_CODE;
	}

	/**
	 * @return the confirm
	 */
	public String getConfirm() {
		return confirm;
	}

	/**
	 * @param confirm
	 *            the confirm to set
	 */
	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	public String getSubShop() {
		return subShop;
	}

	public void setSubShop(String subShop) {
		this.subShop = subShop;
	}

	/**
	 * @return the setLsDataSearch
	 */
	public List<StockGoodsInvSerialDTO> getSetLsDataSearch() {
		return setLsDataSearch;
	}

	/**
	 * @param setLsDataSearch the setLsDataSearch to set
	 */
	public void setSetLsDataSearch(List<StockGoodsInvSerialDTO> setLsDataSearch) {
		this.setLsDataSearch = setLsDataSearch;
	}

	/**
	 * @return the setLsDataSelect
	 */
	public List<StockGoodsInvSerialDTO> getSetLsDataSelect() {
		return setLsDataSelect;
	}

	/**
	 * @param setLsDataSelect the setLsDataSelect to set
	 */
	public void setSetLsDataSelect(List<StockGoodsInvSerialDTO> setLsDataSelect) {
		this.setLsDataSelect = setLsDataSelect;
	}

	public boolean isDisableViewDetail() {
		return disableViewDetail;
	}

	public void setDisableViewDetail(boolean disableViewDetail) {
		this.disableViewDetail = disableViewDetail;
	}

	private List<Boolean> visibleTable = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag(ToggleEvent e) {
		visibleTable.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable() {
		return visibleTable;
	}

	public void setVisibleTable(List<Boolean> visibleTable) {
		this.visibleTable = visibleTable;
	}


	private List<Boolean> visibleTable2 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag2(ToggleEvent e) {
		visibleTable2.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable2() {
		return visibleTable2;
	}

	public void setVisibleTable2(List<Boolean> visibleTable2) {
		this.visibleTable2 = visibleTable2;
	}


	private List<Boolean> visibleTable3 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag3(ToggleEvent e) {
		visibleTable3.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable3() {
		return visibleTable3;
	}

	public void setVisibleTable3(List<Boolean> visibleTable3) {
		this.visibleTable3 = visibleTable3;
	}



	private List<Boolean> visibleTable4 = Arrays.asList(true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true,
			true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true);
	public void onToggleActiveEtag4(ToggleEvent e) {
		visibleTable4.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
	}

	public List<Boolean> getVisibleTable4() {
		return visibleTable4;
	}

	public void setVisibleTable4(List<Boolean> visibleTable4) {
		this.visibleTable4 = visibleTable4;
	}
}
