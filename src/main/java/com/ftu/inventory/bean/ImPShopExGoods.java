/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.staff.bussiness.StaffService;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.ComponentUtils;
import com.ftu.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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
@ManagedBean(eager = true)
@SessionScoped
public class ImPShopExGoods implements Serializable {

	private String orderCode;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	@ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
	private LazyDataModel<TransactionAction> lsData;
	private TransactionAction transactionActionSelected;
	private List<Shop> childShop;
	private List<ApDomain> listReason = new ArrayList<>();
	private Long reasonId;
	private Long subShopId;
	private TransactionAction ta;
	private Long status;
	private List<ApDomain> listStatus;
	private TransactionActionDetail std;
	private String fromSerial;
	private String toSerial;
	private String quantity;
	private List<StockGoodsInvSerialDTO> listSerial;
	private List<SerialA> listAnaSerial;
	private List<SerialA> listAnaSerialSelect;
	private List<StockGoodsInvSerialDTO> setSerial;
	private List<SerialA> setAnaSerial;
	private int sizers;
	private int sizers_;
	private boolean disableAdd;
	private boolean disableApp;
	boolean fist = true;
	private Date assDate;
	List<Staff> listStaff;
	private List<ApDomain> listTransType;
	private Long typeId;
	private Boolean disableSub;
	private String usernameFrm;
	private Long staffIdFrm;
	private Long statusIdFrm;

	// huy
	private int sumDataSelected = 0;
	private int rightSumDataSelected = 0;
	private String subShop;
	private transient StreamedContent fileExport;
	TransactionAction transactionActionTemp;
	List<TransactionActionDetail> listTADTemp = new ArrayList<>();
	private boolean disableFileDownloadButton = true;
	List<Staff> lstStaff = new ArrayList<>();
	private StaffService staffService = new StaffService();
	private TransactionActionDetail transactionActionDetailSelected;
	List<EquipmentsProfile> lstEquipmentProfile ;
	private boolean disableViewDetail = true;
	List<EquipmentsDetail> lstAllEquipmentsDetail = new ArrayList<>();
	List<ApDomain> listWarantytatus;
	private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
	private EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		disableApp = true;
		disableSub = true;
		setListStatus(getSessionBean().getService().getImExGoodsStatus(false));
		setListTransType(getSessionBean().getService().getImReasonsNoPlanNC());
		setChildShop(getSessionBean().getService().getChildShop());
		listStaff = sessionBean.getService().getListStaffByShop(null);
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
		lstStaff = staffService.findAll();
		lstEquipmentProfile = sessionBean.getLsEquipments();
		search();
		lstAllEquipmentsDetail = equipmentsDetailService.getAll();
		listWarantytatus = sessionBean.getService().getListWarantytatus();
	}
	public void initWaranty(){
		lstAllEquipmentsDetail = equipmentsDetailService.getAll();
		listWarantytatus = sessionBean.getService().getListWarantytatus();
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

	public void hanleLsDataSelect() {
		sumDataSelected = 0;
		for (SerialA temp : listAnaSerialSelect) {
			sumDataSelected += temp.getCount();
		}
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
		tas.setTransactionType(getTypeId() == null ? null : getTypeId().toString());
		tas.setReasonId(reasonId);
		tas.setStatus(status == null ? null : status.toString());
		tas.setCreateShopId(subShopId);
		tas.setFromShopId(sessionBean.getShop().getShopId());
		tas.setToShopId(subShopId);
		tas.setCreateDatetime(assDate);
		changeType();
		lsData = new LazyTransActionModel(tas, childShop, listStatus, getListTransType(), listStaff, listReason);
		ta = null;
		std = null;
		disableApp = true;
		setSerial = null;
		sumDataSelected = 0;
	}

	public void changeReason() {
		if (reasonId > 0) {
			typeId = Long.parseLong(sessionBean.getService().getReasonByValue(reasonId).getCode());
			listReason = sessionBean.getService().getListReason(typeId.toString());
		}
	}

	public void changeType() {
		reasonId = 0L;
		listReason = new ArrayList<>();
		if (getTypeId() != null && getTypeId() > 0) {
			listReason = sessionBean.getService().getListReason(getTypeId().toString());
		} else {
			for (ApDomain d : getListTransType()) {
				listReason.addAll(sessionBean.getService().getListReason(d.getValue().toString()));
			}
		}
	}

	public void refresh() {
		ta = null;
		std = null;
		setSerial = null;
	}

	public List<String> completeOrder(String oder) {
		orderCode = oder;
		List<String> rs = new ArrayList<>();
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		tas.setFromShopId(sessionBean.getShop().getShopId());
		List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, getListTransType(),
				listStatus, 0, 10);
		ta = null;
		std = null;
		disableApp = true;
		setSerial = null;
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
		assDate = null;
		reasonId = 0L;
		setTypeId(0L);
		subShopId = 0L;
	}
	
	public void view(SelectEvent event) {
		disableFileDownloadButton = true;
		disableViewDetail = true;
		transactionActionDetailSelected = null;
		std = null;
		disableApp = true;
		disableSub = true;
		ta = (TransactionAction) event.getObject();
		ta.setLsDetail(getSessionBean().getService().getTranActionDetailsByTransId(ta.getTransactionActionId()));
		List<TransactionActionDetail> listTAD = ta.getLsDetail();
		for (TransactionActionDetail std : listTAD) {
			if (ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
				Long k = sessionBean.getService().getStockSizeByTaId(ta.getTransactionActionId(),
						InventoryConstanst.StockStatus.BLOCK_NC, std.getGoodsId(), InventoryConstanst.GoodsStatus.NOMAL);
				std.setSetSize(k.intValue());
			} else {
				std.setSetSize(std.getSetSerial().size());
			}
			listTADTemp = listTAD;// to export
			transactionActionTemp = ta;
			if((listTADTemp != null) && (!listTADTemp.isEmpty())){
				disableFileDownloadButton = false;
			}
		}

		if (!InventoryConstanst.TransactionStatus.EX_R.equals(ta.getStatus())) {
			disableApp = false;
			disableSub = false;
		}
	}
	

	public void export() {

		try {
			lstEquipmentProfile = sessionBean.getLsEquipments();
			Long sumQuantity = 0L;
			int index = 0;
			List<TransactionActionDetail> lsDetail = ta.getLsDetail();
			List<TransactionActionDetail> lsDetailExport = new ArrayList<>();

			for (TransactionActionDetail a : lsDetail) {
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
					if(a.getSetSerial() == null) continue;
					for (StockGoodsInvSerialDTO dto:a.getSetSerial()){
						TransactionActionDetail detailAdd = new TransactionActionDetail();
						detailAdd.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
						detailAdd.setGoodsCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
						detailAdd.setGoodsName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
						detailAdd.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(dto.getEquipmentProfileStatus()));
						detailAdd.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
						detailAdd.setQuantity(1L);
						detailAdd.setSerial(dto.getSerial());
						detailAdd.setDescription(ta.getDescription());
						lsDetailExport.add(detailAdd);
						detailAdd.setIndex(++index);
						sumQuantity += detailAdd.getQuantity();
					}
				}else{
					sumQuantity += a.getQuantity();
					a.setIndex(++index);
					a.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
					a.setGoodsCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
					a.setGoodsName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
					a.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(a.getGoodsStatus()));
					a.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
					a.setDescription(ta.getDescription());
					lsDetailExport.add(a);
				}
			}
			ExportExcel ee = new ExportExcel();
			String toShop = sessionBean.getService().getShopNameById(ta.getToShopId());
			String createShop = sessionBean.getService().getShopById().getShopName();
			String s = ee.printTransactionActionAssign(createShop, toShop, lsDetailExport, transactionActionTemp,
					sumQuantity);
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_dieu_chuyen.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String checkSerial() {
		if (ta == null || ta.getLsDetail().isEmpty()) {
			return "Trans detail is empty !";
		}
//		for (TransactionActionDetail item : ta.getLsDetail()) {
//			if (item.getSetSerial() == null || item.getSetSerial().isEmpty()) {
//				return "Detail " + item.getProfileName() + " has no Serial";
//			}
//		}
		return null;
	}

	public void action() {
		try {
			if (ta == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX)|| ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
				return;
			}
			String s = checkSerial();
			if (s != null) {
				return;
			}
			boolean k = sessionBean.getService().imExApprove(ta, null, InventoryConstanst.StockStatus.BLOCK_NC);
			if (k) {
				getLanguageBean().showMeseage("success", "succesProces");
			} else {
				getLanguageBean().showMeseage("error", "errorValidProcess");
			}
			search();
			transactionNotificationBean.init();
			disableFileDownloadButton = true;
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfImPEx').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorValidProcess");
		}
	}

	public void save() {
		if (ta == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX) || ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
			return;
		}
		String s = checkSerial();
		if (s != null) {
			languageBean.showMeseage_("Info", s);
			return;
		}
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfImPEx').show();");
	}

	public void viewSerial(SelectEvent event) throws IOException {
		std = (TransactionActionDetail) event.getObject();
		if(std!=null&&std.getGoodsId()!=null){
			for (EquipmentsProfile profile:lstEquipmentProfile) {
				if(profile.getProfileId().equals(std.getGoodsId())
						&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
					disableViewDetail = false;
					return;
				}
			}
		}
		disableViewDetail = true;
//		std = (TransactionActionDetail) event.getObject();
//		if (ta.getEquipmentStatus().equals(InventoryConstanst.TransactionStatus.EX)|| ta.getEquipmentStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
//			StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", std.getGoodsStatus(),
//					InventoryConstanst.StockStatus.BLOCK, null, null, std.getGoodsId());
//			sgis.setCurrentTaId(ta.getTransactionActionId());
//
//			List<StockGoodsInvSerialDTO> ls = sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null,
//					null, null);
//
//			std.getSetSerial().addAll(ls);
//		}
//		quantity = std.getQuantity().toString();
//		setSerial = new HashSet<>(std.getSetSerial());
//		listSerial = new ArrayList<>();
//		listAnaSerial = new ArrayList<>();
//		fromSerial = "";
//		toSerial = "";
//		FacesContext fc = FacesContext.getCurrentInstance();
//		NavigationHandler nh = fc.getApplication().getNavigationHandler();
//		nh.handleNavigation(fc, null, "/im-pshop-ex-goods-sub.xhtml?faces-redirect=false");
	}

	public void viewDetailSerial() throws IOException {
		std = transactionActionDetailSelected;
		if (ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX)|| ta.getStatus().equals(InventoryConstanst.TransactionStatus.EX_R)) {
			StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", std.getGoodsStatus(),
					InventoryConstanst.StockStatus.BLOCK_NC, null, null, std.getGoodsId());
			sgis.setCurrentTaId(ta.getTransactionActionId());
			sgis.setNotSerial(true);
			List<StockGoodsInvSerialDTO> ls = sessionBean.getService().searchStockEquipmentsInvSerial(null, null, sgis, null,
					null, null);

			std.getSetSerial().addAll(ls);
		}
		quantity = std.getQuantity().toString();
		setSerial = std.getSetSerial();
		listSerial = new ArrayList<>();
		listAnaSerial = new ArrayList<>();
		fromSerial = "";
		toSerial = "";
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/im-pshop-ex-goods-sub.xhtml?faces-redirect=false");
	}
	public void searchSerial() {
		Integer quan = null;
		if (ta == null || std == null) {
			return;
		}
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, std.getGoodsStatus(),
				InventoryConstanst.StockStatus.INSTOCK, sessionBean.getShop().getShopId(), null, std.getGoodsId());
		sgis.setNotSerial(true);
		sgis.setEquipmentProfileStatus(statusIdFrm);
		listSerial = sessionBean.getService().searchStockEquipmentsInvSerial(fromSerial, toSerial, sgis, null, 0, quan);
		setSerial = std.getSetSerial();
		if (setSerial != null)
			listSerial.removeAll(setSerial);
//editSort
		listAnaSerial = new ArrayList<>();
		if (listSerial != null && !listSerial.isEmpty()) {
			AnalysisSerial as = new AnalysisSerial(null, listSerial);
			listAnaSerial = as.analysisInvSerial();
		}
	}

	public void addAllSerialtoStd() {
		if (listSerial == null || listSerial.isEmpty()) {
			languageBean.showMeseage("Info", "chooseReq", new Object[] { "Serial" });
			return;
		}
		getSetSerial().addAll(listSerial);
		listSerial = new ArrayList<>();
		sumDataSelected = 0;
	}

	public void addSerialtoStd() {
		if (listAnaSerialSelect == null || listAnaSerialSelect.isEmpty()) {
			languageBean.showMeseage("Info", "chooseReq", new Object[] { "Serial" });
			return;
		}
		List<StockGoodsInvSerialDTO> lsg = new ArrayList<>();

		for (SerialA s : listAnaSerialSelect) {
			for (StockGoodsInvSerialDTO sg : listSerial) {
				if (sg.getProviderId().equals(s.getProviderId())
						&& StringUtils.compareHexStrings(sg.getSerialSearch(), s.getFromSerialSearch())
						&& StringUtils.compareHexStrings(s.getToSerialSearch(), sg.getSerialSearch())) {
					lsg.add(sg);
				}
			}
		}
		getSetSerial().addAll(lsg);
		listSerial = new ArrayList<>();
		sumDataSelected = 0;
	}

	public void removeRs(StockGoodsInvSerial sgi) {
		getSetSerial().remove(sgi);
	}

	public void removeRs_(StockGoodsInvSerial sgi) {
		std.getSetSerial().remove(sgi);
		std.setQuantity(std.getQuantity() - 1);
	}

	public void saveSerial() {
//		if (std.getQuantity() > setSerial.size() || std.getQuantity() < setSerial.size()) {
//			languageBean.showMeseage("Info", "validQuanImEx");
//			setSerial = new HashSet<>();
//			return;
//		}
//		std.setSetSerial(setSerial);
//		setSerial = null;
		if (listAnaSerialSelect == null || listAnaSerialSelect.isEmpty()) {
			languageBean.showMeseage("Info", "chooseReq", new Object[] { "Serial" });
			return;
		}
		List<StockGoodsInvSerialDTO> lsg = new ArrayList<>();

		for (SerialA s : listAnaSerialSelect) {
			for (StockGoodsInvSerialDTO sg : listSerial) {
				if(sg.getEquipmentProfileId().equals(s.getGoodsId())&& sg.getSerial().equals(s.getFromSerial())){
					sg.setReasonName(ta.getReasonName());
					sg.setLastMaintenDate(getLastMaintenDate(sg.getSerial(),sg.getEquimentDetailId()));
					sg.setProviderId(s.getProviderId());
					lsg.add(sg);
				}
			}
		}
		getSetSerial().addAll(lsg);
		listSerial = new ArrayList<>();
		sumDataSelected = 0;

		std.setSetSerial(setSerial);
		searchSerial();
	}
	public String getLastMaintenDate(String serial, Long equipmentId){
		List<StockGoodsInvSerialDTO> obj = getSessionBean().getService().searchMainTen(serial, equipmentId, null, null);
		if(obj!=null &&! obj.isEmpty()){
			return obj.get(0).getActionDateStr();
		}
		return "";
	}
	public void refDialog() {
		std.setSetSerial(null);
		setAnaSerial = new ArrayList<>();
		setSerial = null;
		searchSerial();
	}

	public void close() {

		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('serialDlg').hide();");
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
	 * @return the std
	 */
	public TransactionActionDetail getStd() {
		if (ta != null && std != null) {
			if (ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE)
					|| ta.getStatus().equals(InventoryConstanst.TransactionStatus.APPROVE_R)) {
				std.setSetSize(std.getSetSerial().size());
			}
		}
		return std;
	}

	/**
	 * @param std
	 *            the std to set
	 */
	public void setStd(TransactionActionDetail std) {
		this.std = std;
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
	 * @return the setSerial
	 */
	public List<StockGoodsInvSerialDTO> getSetSerial() {
		if (setSerial == null) {
			setSerial = new ArrayList<>();
		}
		return setSerial;
	}

	/**
	 * @param setSerial
	 *            the setSerial to set
	 */
	public void setSetSerial(List<StockGoodsInvSerialDTO> setSerial) {
		this.setSerial = setSerial;
	}

	/**
	 * @return the sizers
	 */
	public int getSizers() {
		if (setSerial == null || setSerial.isEmpty()) {
			sizers = 0;
		} else {
			sizers = setSerial.size();
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
	 * @return the disableAdd
	 */
	public boolean isDisableAdd() {
		disableAdd = true;
		if (ta != null) {
			disableAdd = InventoryConstanst.TransactionStatus.EX.equals(ta.getStatus());
		}
		return disableAdd;
	}

	/**
	 * @param disableAdd
	 *            the disableAdd to set
	 */
	public void setDisableAdd(boolean disableAdd) {
		this.disableAdd = disableAdd;
	}

	/**
	 * @return the sizers_
	 */
	public int getSizers_() {
		if (std == null || std.getSetSerial() == null || std.getSetSerial().isEmpty()) {
			sizers_ = 0;
		} else {
			sizers_ = std.getSetSerial().size();
		}
		return sizers_;
	}

	/**
	 * @param sizers_
	 *            the sizers_ to set
	 */
	public void setSizers_(int sizers_) {
		this.sizers_ = sizers_;
	}

	/**
	 * @return the disableApp
	 */
	public boolean isDisableApp() {
		try {
			disableApp = false;
			if (ta != null && InventoryConstanst.TransactionStatus.APPROVE_R.equals(ta.getStatus())) {
				if (ta != null && !ta.getLsDetail().isEmpty()) {
					for (TransactionActionDetail stds : ta.getLsDetail()) {
						EquipmentsProfile profile = equipmentsProfileService.findByProfileId(stds.getGoodsId());
						if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
							if (stds.getQuantity() > stds.getSetSerial().size()
									|| stds.getQuantity() < stds.getSetSerial().size()) {
								disableApp = true;
								break;
							}
							HashMap<Long, Long> mapSerrial = new HashMap<>();
							List<Long> key  = new ArrayList<>();
							for (StockGoodsInvSerialDTO serial:stds.getSetSerial()) {
								Long quanTy = mapSerrial.get(serial.getEquipmentProfileStatus());
								if(quanTy==null){
									key.add(serial.getEquipmentProfileStatus());
									quanTy = 1L;
									mapSerrial.put(serial.getEquipmentProfileStatus(),quanTy);
								}else {
									quanTy++;
									mapSerrial.put(serial.getEquipmentProfileStatus(),quanTy);
								}
							}
							for (Long status:key) {
								Long quantity = mapSerrial.get(status);
								StockGoods stockGood = sessionBean.getService().getStockGoodQuantity(stds.getGoodsId(),status, ta.getFromShopId());
								if(stockGood==null || stockGood.getQuantity() < quantity || stockGood.getAvailableQuantity() <  quantity){
									disableApp = true;
									return disableApp;
								}
							}

						}else {
							StockGoods stockGood = sessionBean.getService().getStockGoodQuantity(stds.getGoodsId(),InventoryConstanst.GoodsStatus.NOMAL, ta.getFromShopId());
							if(stockGood==null || stockGood.getQuantity() < stds.getQuantity() || stockGood.getAvailableQuantity() < stds.getQuantity()){
								disableApp = true;
								break;
							}
						}
					}
				} else {
					disableApp = true;
				}
			} else {
				disableApp = true;
			}

		}catch (Exception ex){
			ex.printStackTrace();
		}

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
	 * @return the assDate
	 */
	public Date getAssDate() {
		return assDate;
	}

	/**
	 * @param assDate
	 *            the assDate to set
	 */
	public void setAssDate(Date assDate) {
		this.assDate = assDate;
	}

	/**
	 * @return the listAnaSerial
	 */
	public List<SerialA> getListAnaSerial() {
		return listAnaSerial;
	}

	/**
	 * @param listAnaSerial
	 *            the listAnaSerial to set
	 */
	public void setListAnaSerial(List<SerialA> listAnaSerial) {

		this.listAnaSerial = listAnaSerial;
	}

	/**
	 * @return the setAnaSerial
	 */
	public List<SerialA> getSetAnaSerial() {
		setAnaSerial = new ArrayList<>();
		if (setSerial != null && !setSerial.isEmpty()) {
			AnalysisSerial as = new AnalysisSerial(null, new ArrayList<StockGoodsInvSerialDTO>(setSerial));
			setAnaSerial = as.analysisInvSerial();
		}

		rightSumDataSelected = 0;
		for (SerialA temp : setAnaSerial) {
			rightSumDataSelected += temp.getCount();
		}

		return setAnaSerial;
	}

	/**
	 * @param setAnaSerial
	 *            the setAnaSerial to set
	 */
	public void setSetAnaSerial(List<SerialA> setAnaSerial) {

		this.setAnaSerial = setAnaSerial;
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
	 * @return the disableSub
	 */
	public Boolean getDisableSub() {
		return disableSub;
	}

	/**
	 * @param disableSub
	 *            the disableSub to set
	 */
	public void setDisableSub(Boolean disableSub) {
		this.disableSub = disableSub;
	}

	/**
	 * @return the listAnaSerialSelect
	 */
	public List<SerialA> getListAnaSerialSelect() {
		return listAnaSerialSelect;
	}

	/**
	 * @param listAnaSerialSelect
	 *            the listAnaSerialSelect to set
	 */
	public void setListAnaSerialSelect(List<SerialA> listAnaSerialSelect) {
		this.listAnaSerialSelect = listAnaSerialSelect;
	}

	/**
	 * @return the quantity
	 */
	public String getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public int getSumDataSelected() {
		return sumDataSelected;
	}

	public void setSumDataSelected(int sumDataSelected) {
		this.sumDataSelected = sumDataSelected;
	}

	public int getRightSumDataSelected() {
		return rightSumDataSelected;
	}

	public void setRightSumDataSelected(int rightSumDataSelected) {
		this.rightSumDataSelected = rightSumDataSelected;
	}

	public String getSubShop() {
		return subShop;
	}

	public void setSubShop(String subShop) {
		this.subShop = subShop;
	}

	public StreamedContent getFileExport() {
		return fileExport;
	}

	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
	}

	public boolean isDisableFileDownloadButton() {
		return disableApp;//disableFileDownloadButton;
	}

	public void setDisableFileDownloadButton(boolean disableFileDownloadButton) {
		this.disableFileDownloadButton = disableFileDownloadButton;
	}

	public String getUsernameFrm() {
		return usernameFrm;
	}

	public void setUsernameFrm(String usernameFrm) {
		this.usernameFrm = usernameFrm;
	}

	public Long getStaffIdFrm() {
		return staffIdFrm;
	}

	public void setStaffIdFrm(Long staffIdFrm) {
		this.staffIdFrm = staffIdFrm;
	}

	public TransactionActionDetail getTransactionActionDetailSelected() {
		return transactionActionDetailSelected;
	}

	public void setTransactionActionDetailSelected(TransactionActionDetail transactionActionDetailSelected) {
		this.transactionActionDetailSelected = transactionActionDetailSelected;
	}

	public boolean isDisableViewDetail() {
		return disableViewDetail;
	}

	public void setDisableViewDetail(boolean disableViewDetail) {
		this.disableViewDetail = disableViewDetail;
	}

	public Long getStatusIdFrm() {
		return statusIdFrm;
	}

	public void setStatusIdFrm(Long statusIdFrm) {
		this.statusIdFrm = statusIdFrm;
	}

	public List<String> autoCompleteUsername(String query) {
		try {
			List<String> filteredThemes = new ArrayList<>();
			if(lstStaff==null||lstStaff.isEmpty()) return filteredThemes;
			for (int i = 0; i < lstStaff.size(); i++) {
				Staff obj = lstStaff.get(i);
				String value  = obj.getStaffCode() + " | "+ obj.getStaffName();
				if (value.toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(value);
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public void usernameSelect() {
		staffIdFrm = null;
		if (lstStaff != null) {
			for (Staff obj : lstStaff) {
				String value  = obj.getStaffCode() + " | "+ obj.getStaffName();
				if (value.equals(usernameFrm)) {
					staffIdFrm = obj.getStaffId();
					break;
				}
			}
		}
	}
	public String getWarranStatus(Long profileId, String serial){
		if(lstAllEquipmentsDetail==null || profileId==null) return "";
		for (EquipmentsDetail obj:lstAllEquipmentsDetail) {
//			System.out.println("obj.getEquipmentsProfileId():"+ obj.getEquipmentsProfileId() +"profileId: "+ profileId+ " s:"+ serial);
			if(obj.getEquipmentsProfileId()!=null&&obj.getEquipmentsProfileId().equals(profileId)){
				if((serial==null && obj.getSerial() == null) || (serial.isEmpty() && obj.getSerial().isEmpty()) ||
						(obj.getSerial()!=null&&obj.getSerial().equals(serial))){
					for (ApDomain ap : listWarantytatus) {
						if (ap.getValue().equals(obj.getWarantyStatus())) {
							return ap.getName();
						}
					}
				}
			}
		}
		return "";
	}
	public String getWarranExpriDate(Long profileId, String serial){
		if(lstAllEquipmentsDetail==null || profileId==null) return "";
		for (EquipmentsDetail obj:lstAllEquipmentsDetail) {
			if(obj.getEquipmentsProfileId()!=null&&obj.getEquipmentsProfileId().equals(profileId)) {
				if ((serial == null && obj.getSerial() == null) || (serial.isEmpty() && obj.getSerial().isEmpty()) ||
						(obj.getSerial() != null && obj.getSerial().equals(serial))) {
					if (obj.getWarantyExpiredDate() != null) {
						return new SimpleDateFormat("dd/MM/yyyy").format(obj.getWarantyExpiredDate());
					}
				}
			}
		}
		return "";
	}
	public String getWarranExpriDate2(Long profileId, String serial){
		if(lstAllEquipmentsDetail==null || profileId==null) return "";
		for (EquipmentsDetail obj:lstAllEquipmentsDetail) {
			if(obj.getEquipmentsProfileId()!=null&&obj.getEquipmentsProfileId().equals(profileId)) {
				if ((serial == null && obj.getSerial() == null) || (serial.isEmpty() && obj.getSerial().isEmpty()) ||
						(obj.getSerial() != null && obj.getSerial().equals(serial))) {
					if (obj.getWarantyExpiredDate() != null) {
						return new SimpleDateFormat("yyyy/MM/dd").format(obj.getWarantyExpiredDate());
					}
				}
			}
		}
		return "";
	}
	public String getWarrantyReason(Long profileId, String serial){
		if(lstAllEquipmentsDetail==null || profileId==null) return "";
		for (EquipmentsDetail obj:lstAllEquipmentsDetail) {
			if(obj.getEquipmentsProfileId()!=null&&obj.getEquipmentsProfileId().equals(profileId)) {
				if ((serial == null && obj.getSerial() == null) || (serial.isEmpty() && obj.getSerial().isEmpty()) ||
						(obj.getSerial() != null && obj.getSerial().equals(serial))) {
					if (obj.getWarrantyReason() != null) {
						return obj.getWarrantyReason();
					}
				}
			}
		}
		return "";
	}
	public void removeSerial(StockGoodsInvSerialDTO sgi) {
		std.getSetSerial().remove(sgi);
		setSerial = std.getSetSerial();
		searchSerial();
	}
	public void approSerialSelected(){
		int size = 0;
		if(std.getSetSerial()!=null){
			size = std.getSetSerial().size();
		}
		if (std.getQuantity() != size ) {
			languageBean.showMeseage("Info", "quantity.max.size", new Object[] { std.getQuantity()});
//			setSerial = new HashSet<>();
			return;
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/im-pshop-ex-goods.xhtml?faces-redirect=false");
	}
	public void removeSerialSelected(){
		int size = 0;
		if(std!=null && std.getSetSerial()!=null){
			std.getSetSerial().clear();
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/im-pshop-ex-goods.xhtml?faces-redirect=false");
	}
	public void clickStd() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfImport').show();");
	}

	public TransactionAction getTransactionActionSelected() {
		return transactionActionSelected;
	}

	public void setTransactionActionSelected(TransactionAction transactionActionSelected) {
		this.transactionActionSelected = transactionActionSelected;
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
