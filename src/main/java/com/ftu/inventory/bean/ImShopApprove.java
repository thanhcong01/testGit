/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.dao.StaffDAO;
import com.ftu.utils.ComponentUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.Visibility;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 *
 * @author E5420
 */
@ManagedBean(eager = true)
@ViewScoped
//@ManagedBean(name = "imShopApprove", eager = true)
//@ViewScoped
public class ImShopApprove implements Serializable {

	private String orderCode;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
	private LazyDataModel<TransactionAction> lsData;
	private List<Shop> childShop;
	private List<Staff> listStaff;
	private List<ApDomain> listReason = new ArrayList<>();
	private Long reasonId;
	private Long staffId;
	private Long typeId;
	private List<ApDomain> listTransType;
	private Long subShopId;
	private TransactionAction ta = new TransactionAction();
	private Long status;
	private List<ApDomain> listStatus;
	private boolean disableApp;
	private boolean disableDel;
	boolean fist = true;
	private Date createDate;
	private String ACTION_CODE;
	private String confirm;
	private String description;

	// huy
	private String subShop;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		setDisableApp(true);
		setDisableDel(true);
		setListStatus(sessionBean.getService().getImApproveStatus(true));
		setListTransType(getSessionBean().getService().getImReasons());
		setChildShop(getSessionBean().getService().getChildShop());
		setListStaff(getSessionBean().getService().getListStaffByShop(null));
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
		if (!fist) {
			ComponentUtils cu = new ComponentUtils();
			DataTable dt = (DataTable) cu.findComponent("dtta");
			if (dt != null) {
				dt.setFirst(0);
			}
		}
		if ((subShop == null) || (subShop.equals(""))) {
			subShopId = null;
		}
		
		
		fist = false;
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setTransactionType(typeId == null ? null : typeId.toString());
		tas.setReasonId(reasonId);
		tas.setStatus(status == null ? null : status.toString());
		tas.setCreateShopId(subShopId);
		tas.setCreateStaffId(staffId);
		tas.setFromShopId(sessionBean.getShop().getShopId());
		tas.setToShopId(subShopId);
		tas.setCreateDatetime(createDate);
		changeType();
		lsData = new LazyTransActionModel(tas, childShop, listStatus, listTransType, null, listReason);
		setTa(new TransactionAction());
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
		if (typeId != null && typeId > 0) {
			listReason = sessionBean.getService().getListReason(typeId.toString());
		} else {
			listReason = new ArrayList<>();
			for (ApDomain d : listTransType) {
				listReason.addAll(sessionBean.getService().getListReason(d.getValue().toString()));
			}
		}
	}

	public List<String> completeOrder(String oder) {
		orderCode = oder;
		List<String> rs = new ArrayList<>();
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setFromShopId(sessionBean.getShop().getShopId());
		List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, listTransType, listStatus,
				0, 10);
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
	}

	private void refresh() {
		setTa(new TransactionAction());
		lsData = null;
		disableApp = true;
		setDisableDel(true);
	}

	public void view() {
		disableApp = true;
		setDisableDel(true);

		ta.setLsDetail(getSessionBean().getService().getTranActionDetailsByTransId(ta.getTransactionActionId()));
		for (TransactionActionDetail std : ta.getLsDetail()) {
			EquipmentsProfile g = sessionBean.getGoods(std.getGoodsId().toString());
			std.setGoodsName(g.getProfileName());
			std.setGoodsCode(g.getProfileCode());
			std.setSpecification(g.getSpecification());
			std.setGoodsStatus(InventoryConstanst.GoodsStatus.NOMAL);
			std.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(std.getGoodsId().toString()));
			std.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(std.getGoodsStatus()));
		}

		if (!InventoryConstanst.TransactionStatus.APPROVE.equals(ta.getStatus()) && ta != null
				&& !ta.getLsDetail().isEmpty()) {
			disableApp = false;
		}
		if (!InventoryConstanst.TransactionStatus.APPROVE.equals(ta.getStatus())) {
			disableDel = false;
		}
		// setDisableDel(false);
		if (ta == null || ta.getLsDetail().isEmpty()) {
			languageBean.showMeseage("info", "NoDetailReq");
		}
		if (InventoryConstanst.TransactionStatus.CANCEL.equals(ta.getStatus())) {
			disableDel = true;
			disableApp = true;
		}
	}

	public void action() {
		if (getTa() == null || getTa().getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
			return;
		}
		try {
			boolean k = false;
			ta.setDescription(ta.getDescription() == null ? sessionBean.getStaff().getUserName() + ": " + description
					: ta.getDescription() + "\n" + sessionBean.getStaff().getUserName() + ": " + description);
			if (getACTION_CODE().equals(InventoryConstanst.RoleKey.APPROVE)) {
                getTa().setAssessmentStaffId(sessionBean.getStaff().getStaffId());
				k = sessionBean.getService().imApproveApprove(getTa(), null, InventoryConstanst.StockStatus.BLOCK_TD);
			} else if (getACTION_CODE().equals(InventoryConstanst.RoleKey.REJECT)) {
                getTa().setAssessmentStaffId(sessionBean.getStaff().getStaffId());
				k = sessionBean.getService().imApproveReject(getTa(), null, InventoryConstanst.StockStatus.BLOCK_TD);
			}
			if (k) {
				getLanguageBean().showMeseage("success", "succesProces");
			} else {
				getLanguageBean().showMeseage("error", "errorValidProcess");
			}
			search();
			transactionNotificationBean.init();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfImPApp').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorValidProcess");
		}
	}

	public void save() {
		if (getTa() == null || getTa().getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
			return;
		}
		description = "";
		setACTION_CODE(InventoryConstanst.RoleKey.APPROVE);
		confirm = languageBean.getBundle().getString("confirmApp");
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfImPApp').show();");

	}

	public void cancel() {
		if (getTa() == null || getTa().getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)) {
			return;
		}
		description = "";
		setACTION_CODE(InventoryConstanst.RoleKey.REJECT);
		confirm = languageBean.getBundle().getString("confirmRej");
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfImPApp').show();");
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
	 * @return the ta
	 */
	public TransactionAction getTa() {
		return ta;
	}

	/**
	 * @param ta
	 *            the ta to set
	 */
	public void setTa(TransactionAction ta) {
		this.ta = ta;
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
	 * @return the listStaff
	 */
	public List<Staff> getListStaff() {
		return listStaff;
	}

	/**
	 * @param listStaff the listStaff to set
	 */
	public void setListStaff(List<Staff> listStaff) {
		this.listStaff = listStaff;
	}

	/**
	 * @return the staffId
	 */
	public Long getStaffId() {
		return staffId;
	}

	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(Long staffId) {
		this.staffId = staffId;
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

}
