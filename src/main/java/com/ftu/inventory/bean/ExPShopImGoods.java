/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.language.LanguageBean;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
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
public class ExPShopImGoods implements Serializable {

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
	private TransactionAction ta;
	private StockTransactionDetail stDetail;
	private Long status;
	private List<ApDomain> listStatus;
	private boolean disableApp;
	private List<StockGoodsInvSerialDTO> listSerial = new ArrayList<>();
	private List<StockGoodsInvSerialDTO> setLsDataSearch = new ArrayList<>();
	private List<StockGoodsInvSerialDTO> setLsDataSelect = new ArrayList<>();
	private int sizers;
	boolean fist = true;
	private Date createDate;
	List<Staff> listStaff;
	private String fromSerial;
	private String toSerial;
	private List<SerialA> setAnaSerial;
	private boolean disableViewDetail = true;

	//huy
	private String subShop;
	TransactionAction transactionActionTemp;
	List<StockTransactionDetail> listSDTTemp = new ArrayList<>();
	private boolean disableFileDownloadButton = true;
	private transient StreamedContent fileExport;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		setDisableApp(true);
		listStatus = sessionBean.getService().getImImGoodsStatus(true);
		listTransType = getSessionBean().getService().getListExSubShopReasons();
		setChildShop(getSessionBean().getService().getChildShop());
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
		if((subShop == null) || (subShop.equals(""))){
			subShopId = null;
		}

		fist = false;
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setTransactionType(typeId == null ? null : typeId.toString());
		tas.setReasonId(reasonId);
		tas.setStatus(status == null ? null : status.toString());
		tas.setCreateShopId(subShopId);
		tas.setFromShopId(subShopId);
		tas.setToShopId(sessionBean.getShop().getShopId());
		tas.setCreateDatetime(createDate);
		changeType();
		lsData = new LazyTransActionModel(tas, childShop, listStatus, listTransType, listStaff, listReason);
		ta = null;
		st = null;
		stDetail = null;
		disableApp = true;
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
		List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, listTransType, listStatus, 0, 10);
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
		typeId = 0L;
	}

	public void refresh() {
		ta = null;
		st = null;
		search();
		stDetail = null;
	}

	public void view() {
		disableApp = true;
		disableViewDetail = true;
		stDetail =null;
		setSt(getSessionBean().getService().getLastByTransaction(ta.getTransactionActionId()));
		getSt().setLsDetail(getSessionBean().getService().getDetailsByStock(getSt().getStockTransactionId()));
		if (st != null) {
			List<StockTransactionDetail> listSDT = st.getLsDetail();
			for (StockTransactionDetail std : listSDT) {
				Long k = sessionBean.getService().getStockSerialSizeByDetail(std.getStockTransactionDetailId());
				std.setSetSize(k.intValue());
			}
			transactionActionTemp = ta;//save to export
			listSDTTemp = listSDT;
			if((listSDTTemp != null) && (!listSDTTemp.isEmpty())){
				setDisableFileDownloadButton(false);
			}
		}
		if (!InventoryConstanst.TransactionStatus.IM.equals(ta.getStatus())) {
			disableApp = false;
		}
		if (InventoryConstanst.TransactionStatus.IM.equals(ta.getStatus())) {
			disableFileDownloadButton = true;
		}
	}

	public void viewClick(/*SelectEvent event*/) {
		//stDetail = (StockTransactionDetail) event.getObject();
		if (stDetail != null) {
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

			nh.handleNavigation(fc,
					null, "/ex-pshop-im-goods-sub.xhtml?faces-redirect=false");
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

	public void action() {
		try {
			RequestContext context = RequestContext.getCurrentInstance();
			boolean k = sessionBean.getService().exImApprove(ta, st, InventoryConstanst.StockStatus.BLOCK_XT);
			if (k) {
				getLanguageBean().showMeseage("success", "succesProces");
			} else {
				getLanguageBean().showMeseage("error", "errorValidProcess");
			}
			search();
			transactionNotificationBean.init();
			disableFileDownloadButton = true;

			context.execute("PF('cfExPIm').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorProcess");
		}
	}

	public void save() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfExPIm').show();");
	}

	public List<SerialA> getDetailSerial(StockTransactionDetail stockTransactionDetail)  {
		List<SerialA> lstSerialA = new ArrayList<>();
		List<StockTransactionSerial> ls = new ArrayList<>();
		ls = sessionBean.getService().getByDetail(stockTransactionDetail.getStockTransactionDetailId());
		if (ls != null) {
			if (ls != null && !ls.isEmpty()) {
				AnalysisSerial as = new AnalysisSerial(ls, null);
				lstSerialA = as.analysis();
			}
		}
		return lstSerialA;
	}
	public void export(){
		if((listSDTTemp == null) || (listSDTTemp.isEmpty())){
			languageBean.showMeseage("Info", "inve.transaction.not.selected");
			return;
		}
		try {
			List<EquipmentsProfile> lstEquipmentProfile = sessionBean.getLsEquipments();
			Long sumQuantity = 0L;
			int index = 0;
			List<StockTransactionDetail> lsDetail =st.getLsDetail();// new ArrayList<StockTransactionDetail>();
			List<StockTransactionDetail> lsDetailExport = new ArrayList<>();//new ArrayList<StockTransactionDetail>();
			for(StockTransactionDetail a : lsDetail) {
				EquipmentsProfile profileSeleted = null;
				if(a.getGoodsId()!=null){
					for (EquipmentsProfile pro:lstEquipmentProfile) {
						if(pro.getProfileId().equals(a.getGoodsId())){
							profileSeleted = pro;
							break;
						}
					}
				}
				if(profileSeleted!=null
						&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profileSeleted.getManagementType())){
					List<SerialA> lsSerialA = new ArrayList<>();
					StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", a.getGoodsStatus(), InventoryConstanst.StockStatus.BLOCK_XT, null, null, a.getGoodsId());
					sgis.setCurrentTaId(ta.getTransactionActionId());
//					sgis.setNotSerial(true);
					a.getSetSerial().clear();
					a.getSetSerial().addAll(sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null, null, null));
					for (StockGoodsInvSerialDTO dto:a.getSetSerial()) {
						StockTransactionDetail detailAdd = new StockTransactionDetail();
						detailAdd.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
						detailAdd.setGoodsCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
						detailAdd.setGoodsName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
						detailAdd.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(dto.getEquipmentProfileStatus()));
						detailAdd.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
						detailAdd.setQuantity(1L);
						detailAdd.setSerial(dto.getSerial());
						sumQuantity += detailAdd.getQuantity();
						detailAdd.setIndex(++index);
						lsDetailExport.add(detailAdd);
					}

				}else {
					a.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
					a.setGoodsCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
					a.setGoodsName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
					a.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(a.getGoodsStatus()));
					a.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
					sumQuantity += a.getQuantity();
					a.setIndex(++index);
					lsDetailExport.add(a);
				}


//				sumQuantity += a.getQuantity();
//				a.setIndex(++index);
//				a.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
//				a.setProfileCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
//				a.setProfileName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
//				a.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(a.getGoodsStatus()));
			}
			ExportExcel ee = new ExportExcel();
			String fromShop = sessionBean.getService().getShopNameById(ta.getFromShopId());
			String createShop = sessionBean.getService().getShopById().getShopName();
			String s = ee.printStockTracsactionDetail(createShop,fromShop, lsDetailExport, transactionActionTemp, sumQuantity);
//			listSDTTemp.clear();
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_nhap_kho.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
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
	 * @return the childShop
	 */
	public List<Shop> getChildShop() {
		return childShop;
	}

	/**
	 * @param childShop the childShop to set
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
	 * @return the subShopId
	 */
	public Long getSubShopId() {
		return subShopId;
	}

	/**
	 * @param subShopId the subShopId to set
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
		if (listSerial != null && !listSerial.isEmpty()) {
			AnalysisSerial as = new AnalysisSerial(null,listSerial);
			setAnaSerial = as.analysis();
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

	public String getSubShop() {
		return subShop;
	}

	public void setSubShop(String subShop) {
		this.subShop = subShop;
	}

	public boolean isDisableFileDownloadButton() {
		return disableFileDownloadButton;
	}

	public void setDisableFileDownloadButton(boolean disableFileDownloadButton) {
		this.disableFileDownloadButton = disableFileDownloadButton;
	}

	public StreamedContent getFileExport() {
		return fileExport;
	}

	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
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
