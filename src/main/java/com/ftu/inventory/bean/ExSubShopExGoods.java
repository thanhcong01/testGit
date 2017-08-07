/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.dao.EquipmentsProfileDAO;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.exception.AppException;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.ComponentUtils;
import com.ftu.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.lang.SerializationUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class ExSubShopExGoods implements Serializable {

	private String orderCode;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
	private LazyDataModel<TransactionAction> lsData;
	private List<ApDomain> listReason = new ArrayList<>();
	private Long reasonId;
	private Long typeId;
	private List<ApDomain> listTransType;
	private StockTransaction st;
	private TransactionAction ta;
	private StockTransactionDetail stDetail;
	private Long status;
	private List<ApDomain> listStatus;
	private boolean disableApp;
	private int sizers;
	private String fromSerial;
	private String toSerial;
	private List<StockGoodsInvSerialDTO> listSerial = new ArrayList<>();
	private List<StockGoodsInvSerialDTO> setLsDataSearch = new ArrayList<>();
	private List<StockGoodsInvSerialDTO> setLsDataSelect = new ArrayList<>();
	private List<SerialA> setAnaSerial;

	Shop shop;
	boolean fist = true;
	private Date createDate;
	List<Staff> listStaff;


	private transient StreamedContent fileExport;
	TransactionAction transactionActionTemp;
	List<StockTransactionDetail> listTADTemp = new ArrayList<>();
	private boolean disableFileDownloadButton = true;
	private boolean disableViewDetail = true;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		setDisableApp(true);
		shop = sessionBean.getShop();
		listStatus = sessionBean.getService().getImExGoodsStatus(true);
		setListTransType(getSessionBean().getService().getListExSubShopReasons());
		if (listStatus != null && !listStatus.isEmpty()) {
			status = listStatus.get(0).getValue();
		}
		listStaff = sessionBean.getService().getListStaffByShop(null);
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

	public void search() {
		if (!fist) {
			ComponentUtils cu = new ComponentUtils();
			DataTable dt = (DataTable) cu.findComponent("dtta");
			if (dt != null) {
				dt.setFirst(0);
			}
		}
		fist = false;
		setSetLsDataSelect(new ArrayList<StockGoodsInvSerialDTO>());
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setTransactionType(getTypeId() == null ? null : getTypeId().toString());
		tas.setReasonId(reasonId);
		tas.setStatus(status == null ? null : status.toString());
		tas.setCreateShopId(sessionBean.getShop().getShopId());
		tas.setFromShopId(sessionBean.getShop().getShopId());
		tas.setToShopId(sessionBean.getShop().getShopParentId());
		tas.setCreateDatetime(createDate);
		changeType();
		lsData = new LazyTransActionModel(tas, null, listStatus, getListTransType(), listStaff, listReason);
		ta = null;
		st = null;
		stDetail = null;
		disableApp = true;
	}

	public void changeReason() {
		if (reasonId > 0) {
			typeId = Long.parseLong(sessionBean.getService().getReasonByValue(reasonId).getCode());
			listReason = sessionBean.getService().getListReason(getTypeId().toString());
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
		tas.setCreateShopId(sessionBean.getShop().getShopId());
		tas.setFromShopId(sessionBean.getShop().getShopId());
		tas.setToShopId(sessionBean.getShop().getShopParentId());
		List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, getListTransType(), listStatus, 0, 10);
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
		setTypeId((Long) 0L);
	}

	public void refresh() {
		ta = null;
		st = null;
		search();
		stDetail = null;
	}

	public void view() {
		disableApp = true;
		stDetail = null;
		disableViewDetail = true;
		setSt(getSessionBean().getService().getByTransaction(ta.getTransactionActionId(), InventoryConstanst.StockTransactionStatus.WAIT));
		getSt().setLsDetail(getSessionBean().getService().getDetailsByStock(getSt().getStockTransactionId()));
		List<StockTransactionDetail> listTAD = getSt().getLsDetail();
		for (StockTransactionDetail std : listTAD) {
			if (ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX)) {
				Long k = sessionBean.getService().getStockSizeByTaId(ta.getTransactionActionId(),
						InventoryConstanst.StockStatus.BLOCK_XT, std.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL);
				std.setSetSize(k.intValue());
			} else {
				std.setSetSize(std.getSetSerial().size());
			}
			listTADTemp = listTAD;// to export
			transactionActionTemp = ta;
			//			System.out.println(listTADTemp);
			if((listTADTemp != null) && (!listTADTemp.isEmpty())){
				disableFileDownloadButton = false;
			}
		}
		if (!InventoryConstanst.TransactionStatus.EX.equals(ta.getStatus())) {
			disableApp = false;
		}
		if (InventoryConstanst.TransactionStatus.EX.equals(ta.getStatus())) {
			disableFileDownloadButton = true;
		}
	}

	public void viewClick(/*SelectEvent event*/) {
		//stDetail = (StockTransactionDetail) event.getObject();

		if (stDetail != null){
			StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", stDetail.getGoodsStatus(), InventoryConstanst.StockStatus.BLOCK_XT, null, null, stDetail.getGoodsId());
			sgis.setCurrentTaId(ta.getTransactionActionId());
			listSerial = sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null, null, null);
			if (listSerial != null) {
				stDetail.getSetSerial().addAll(listSerial);
				searchSerial();
			}
			setFromSerial("");
			setToSerial("");
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "/ex-subshop-ex-goods-sub.xhtml?faces-redirect=false");
		}
	}

	public void viewSerialTable(SelectEvent event) throws IOException {
		StockTransactionDetail std = (StockTransactionDetail) event.getObject();
		if (std != null && std.getGoodsId() != null) {
			List<EquipmentsProfile> lstEquipmentProfile = sessionBean.getLsEquipments();
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
	
	public void viewSerial(SelectEvent event) {
		//stDetail = (StockTransactionDetail) event.getObject();

		if (stDetail != null){
			StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", stDetail.getGoodsStatus(), InventoryConstanst.StockStatus.BLOCK_XT, null, null, stDetail.getGoodsId());
			sgis.setCurrentTaId(ta.getTransactionActionId());
			sgis.setNotSerial(true);
			listSerial = sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null, null, null);

			if (listSerial != null) {
				stDetail.getSetSerial().addAll(listSerial);
				
			}
			setFromSerial("");
			setToSerial("");
			FacesContext fc = FacesContext.getCurrentInstance();
			NavigationHandler nh = fc.getApplication().getNavigationHandler();
			nh.handleNavigation(fc, null, "/ex-subshop-ex-goods-sub.xhtml?faces-redirect=false");
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

	public void action() throws Exception {
		try {
			boolean k = sessionBean.getService().exExApprove(ta, st, InventoryConstanst.StockStatus.BLOCK_XT);
			if (k) {
				getLanguageBean().showMeseage("success", "succesProces");
			} else {
				getLanguageBean().showMeseage("error", "errorValidProcess");
			}
			search();
			transactionNotificationBean.init();
			disableFileDownloadButton = true;

			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfExPEx').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorValidProcess");
		}
	}

	public void save() throws Exception {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfExPEx').show();");
	}

	public void export() {
		try {
			List<EquipmentsProfile> lstEquipmentProfile = sessionBean.getLsEquipments();
			Long sumQuantity = 0L;
			int index = 0;
			List<StockTransactionDetail> lsDetail = getSt().getLsDetail();//new ArrayList<TransactionActionDetail>();
			List<TransactionActionDetail> lstDetail = new ArrayList<TransactionActionDetail>();
			for (StockTransactionDetail std : lsDetail) {
				EquipmentsProfile profileSeleted = null;
				if(std.getGoodsId()!=null){
					for (EquipmentsProfile pro:lstEquipmentProfile) {
						if(pro.getProfileId().equals(std.getGoodsId())){
							profileSeleted = pro;
							break;
						}
					}
				}
				if(profileSeleted!=null
						&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profileSeleted.getManagementType())){
					StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", std.getGoodsStatus(), InventoryConstanst.StockStatus.BLOCK_XT, null, null, std.getGoodsId());
					sgis.setCurrentTaId(ta.getTransactionActionId());
//					sgis.setNotSerial(true);
					std.getSetSerial().clear();
					std.getSetSerial().addAll(sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null, null, null));
					if(std.getSetSerial() == null) continue;
					for (StockGoodsInvSerialDTO dto:std.getSetSerial()){
						TransactionActionDetail detailAdd = new TransactionActionDetail();
						detailAdd.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(std.getGoodsId().toString()));
						detailAdd.setGoodsCode(sessionBean.getEquipProfileCode(std.getGoodsId().toString()));
						detailAdd.setGoodsName(sessionBean.getEquipProfileName(std.getGoodsId().toString()));
						detailAdd.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(dto.getEquipmentProfileStatus()));
						detailAdd.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
						detailAdd.setQuantity(1L);
						detailAdd.setSerial(dto.getSerial());
						lstDetail.add(detailAdd);
						detailAdd.setIndex(++index);
						sumQuantity += detailAdd.getQuantity();
					}
				}else{
					TransactionActionDetail tad = new TransactionActionDetail(std.getGoodsId(), std.getProviderId());
					tad.setDescription(std.getDescription());

					tad.setGoodsCode(sessionBean.getEquipProfileCode(std.getGoodsId().toString()));
					tad.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(std.getGoodsId().toString()));
					tad.setGoodsName(sessionBean.getEquipProfileName(std.getGoodsId().toString()));
					tad.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(std.getGoodsStatus()));
					tad.setQuantity(std.getQuantity());
					tad.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
					lstDetail.add(tad);
					tad.setIndex(++index);
					sumQuantity += std.getQuantity();

				}
			}
			ExportExcel ee = new ExportExcel();
			String fromShop = sessionBean.getService().getShopNameById(ta.getFromShopId());
			String createShop = sessionBean.getService().getShopById().getShopName();
//			List<TransactionActionDetail> lstDetail = new ArrayList<TransactionActionDetail>();
//			lstDetail.addAll(mapDetail.values());
			String s = ee.printTransactionActionDetail(createShop, fromShop, lstDetail, transactionActionTemp,
					sumQuantity);
//			listTADTemp.clear();
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_xuat_kho.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode to set
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
	 * @param reasonId the reasonId to set
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
	 * @param lsData the lsData to set
	 */
	public void setLsData(LazyDataModel<TransactionAction> lsData) {
		this.lsData = lsData;
	}

	/**
	 * @return the listReason
	 */
	public List<ApDomain> getListReason() {
		return listReason;
	}

	/**
	 * @param listReason the listReason to set
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
	 * @param st the st to set
	 */
	public void setSt(StockTransaction st) {
		this.st = st;
	}

	/**
	 * @return the ta
	 */
	public TransactionAction getTa() {
		return ta != null ? ta : new TransactionAction();
	}

	/**
	 * @param ta the ta to set
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
	 * @param status the status to set
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
	 * @param listStatus the listStatus to set
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
	 * @param disableApp the disableApp to set
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
	 * @param stDetail the stDetail to set
	 */
	public void setStDetail(StockTransactionDetail stDetail) {
		this.stDetail = stDetail;
	}

	/**
	 * @return the sizers
	 */
	public int getSizers() {
		if (listSerial == null || listSerial.isEmpty()) {
			sizers = 0;
		} else {
			sizers = listSerial.size();
		}
		return sizers;
	}

	/**
	 * @param sizers the sizers to set
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
	 * @param listSerial the listSerial to set
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
	 * @param createDate the createDate to set
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * @return the fromSerial
	 */
	public String getFromSerial() {
		return fromSerial;
	}

	/**
	 * @param fromSerial the fromSerial to set
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
	 * @param toSerial the toSerial to set
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

	/**
	 * @param setAnaSerial the setAnaSerial to set
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
	 * @param listTransType the listTransType to set
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
	 * @param typeId the typeId to set
	 */
	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public StreamedContent getFileExport() {
		return fileExport;
	}

	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
	}

	public boolean isDisableFileDownloadButton() {
		return disableFileDownloadButton;
	}

	public void setDisableFileDownloadButton(boolean disableFileDownloadButton) {
		this.disableFileDownloadButton = disableFileDownloadButton;
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
