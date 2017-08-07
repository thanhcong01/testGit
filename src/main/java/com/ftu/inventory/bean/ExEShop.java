/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.java.business.EquipmentsProfileService;
import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.DateTimeUtils;
import com.ftu.utils.StringUtils;

import java.io.FileInputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

/**
 *
 * @author E5420
 */
@ManagedBean(eager = true)
@SessionScoped
public class ExEShop implements Serializable {

	SimpleDateFormat sf = new SimpleDateFormat(".yy.MM.dd.");
	private String orderCode;
	private List<ApDomain> listReason = new ArrayList<>();
	private List<ApDomain> listDomainGoodsGroup = new ArrayList<>();
	private Long reasonId;
	private Long typeId;
	private List<ApDomain> listTransType;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	private Shop shop;
	private Staff staff;
	private Long goodsId;
	private Long goodStatus;
	private String fromSerial;
	private String toSerial;
	private String quantity;
	private List<StockGoodsInvSerialDTO> lsData;
	private StockTransactionDetail stDetail = new StockTransactionDetail();
	private StockTransaction st;
	private TransactionAction ta;
	private String goodsName;
	private String goodsStatusName;
	private Boolean disableReason;
	private Boolean disableAdd;
	private EquipmentsProfileService equipmentsProfileService = new EquipmentsProfileService();
	private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
	private Boolean disablePrint;
	private boolean disableApp;
	private String refreshString;
	private Long goodsGroupId;
	private Long domainGroupId;
	private List<EquipmentsProfile> lsEquipments;
	List<EquipmentsGroup> lsGoodsGroup;
	private String goods;
	private int sizers;
	private List<SerialA> setAnaSerial;
	private List<SerialA> setLsData;
	private List<SerialA> setLsDataSelect;
	private String description;
	private Boolean disableViewDetail = true;
	private List<StockGoodsInvSerialDTO> listSerial;
	private List<StockGoodsInvSerialDTO> setSerial;
	private List<SerialA> listAnaSerial;
	private List<SerialA> listAnaSerialSelect;
	private Boolean disableSub = true;
	private StockTransactionDetail stockTransactionDetailSelected;
	List<EquipmentsGroup> lsgGroup = new ArrayList<>();

	private Long subStaffId;
	private String subStaffName;
	private Long subShopId;
	private String subShop;
	private List<Staff> childShop;
	private String note;
	private Date exportDate;
	private Long statusIdFrm;
	private  String referenceCode;
	
	private transient StreamedContent fileExport;

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
		exportDate = new Date();
		disableAdd = true;
		disablePrint = true;
		setShop(getSessionBean().getShop());
		setStaff(getSessionBean().getStaff());
		//setChildShop(getSessionBean().getService().getChildEShop(shop.getShopId()));
		setChildShop(getSessionBean().getService().getListStaffByShop(shop.getShopId()));
		setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(),
				InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.SUCCESS,
				InventoryConstanst.StockTransactionDeliveryType.TRANS));
		listTransType = getSessionBean().getService().getImReasonsNoPlanTD();
		setListDomainGoodsGroup(getSessionBean().getLsgDomainGroup());
		lsGoodsGroup = sessionBean.getLsgGroupActive();
		changeType();
		changeGoodsGroup();
		initCbb();
	}

	public void SubShopSelect() {
		subStaffId = null;
		subStaffName ="";
		subShopId = null;
		if (childShop != null) {
			for (Staff staff : childShop) {
				String filter = staff.getStaffCode() +" | "+ staff.getStaffName();
				if (filter.equals(subShop)) {
					subStaffId = staff.getStaffId();
					subStaffName = staff.getStaffName();
					subShopId = staff.getShopId();
					getSt().setImportStaffId(subStaffId);
					getSt().setToShopId(subShopId);

					break;
				}
			}
		}
		if(subStaffId==null){
			disablePrint = true;
			return;
		}
		if(getSt() == null || getSt().getLsDetail() == null || getSt().getLsDetail().size() < 1){
			disablePrint = true;
			return;
		}
		if(subStaffId!=null &&getSt().getLsDetail().size() > 0){
			disablePrint = false;
			return;
		}
	}
	public void saveSerial() {
		if (listAnaSerialSelect == null || listAnaSerialSelect.isEmpty()) {
			languageBean.showMeseage("Info", "chooseReq", new Object[] { "Serial" });
			return;
		}
		List<StockGoodsInvSerialDTO> lsg = new ArrayList<>();

		for (SerialA s : listAnaSerialSelect) {
			for (StockGoodsInvSerialDTO sg : listSerial) {
				if(sg.getEquipmentProfileId().equals(s.getGoodsId())&& sg.getSerial().equals(s.getFromSerial())){
//					sg.setReasonName(ta.get());
					//lan bao hanh gan nhat
					if(sg.getSerial()!=null && !sg.getSerial().isEmpty()){
						EquipmentsDetail obj = equipmentsDetailService.findBySerial(sg.getSerial());
						if(obj!=null){
							sg.setLastMaintenDate(obj.getLastUsedDateStr());
							sg.setReasonName(obj.getWarrantyReason());
//							sg.setLastMaintenDate(getLastMaintenDate(sg.getSerial(),sg.getEquimentDetailId()));
						}
					}


					sg.setProviderId(s.getProviderId());
					lsg.add(sg);
				}
			}
		}
		getSetSerial().addAll(lsg);
		listSerial = new ArrayList<>();

		stDetail.setSetSerial(setSerial);
		searchSerial();
//		setSerial = null;
	}
	public void approSerialSelected(){
		int size = 0;
		if(stDetail.getSetSerial()!=null){
			size = stDetail.getSetSerial().size();
		}
		if (stDetail.getQuantity() != size ) {
			languageBean.showMeseage("Info", "quantity.max.size", new Object[] { stDetail.getQuantity()});
//			setSerial = new HashSet<>();
			return;
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/ex-eshop.xhtml?faces-redirect=false");
	}

	public void removeSerialSelected(){
		int size = 0;
		if(stDetail.getSetSerial()!=null){
			stDetail.getSetSerial().clear();
		}
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/ex-eshop.xhtml?faces-redirect=false");
	}
	public void clickStd() {
		RequestContext context = RequestContext.getCurrentInstance();
		context.execute("PF('cfImport').show();");
	}
	public String getLastMaintenDate(String serial, Long equipmentId){
		List<StockGoodsInvSerialDTO> obj = getSessionBean().getService().searchMainTen(serial, equipmentId, null, null);
		if(obj!=null &&! obj.isEmpty()){
			return obj.get(0).getActionDateStr();
		}
		return "";
	}

	public List<String> CompleteSubShop(String query) {
		try {
			setChildShop(getSessionBean().getService().getListStaffByShop(shop.getShopId()));
			List<String> filteredThemes = new ArrayList<String>();
			Date dateCurrent = DateTimeUtils.convertDateTimeToDate(new Date());
			for (int i = 0; i < childShop.size(); i++) {
				Staff skin = childShop.get(i);
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(skin.getStaffStatus()))
				{
					continue;
				}
				if(skin != null && skin.getEndDate()!= null
						&& dateCurrent.compareTo(skin.getEndDate()) > 0){
					continue;
				}
				String filter = skin.getStaffCode() +" | "+ skin.getStaffName();
				if (filter.toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(filter);
				}
				if (filter.toLowerCase().equals(query.toLowerCase())) {
					subShopId = skin.getShopId();
					subStaffId = skin.getStaffId();
					getSt().setImportStaffId(subStaffId);
					getSt().setToShopId(subShopId);
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	public void searchSerial() {
		Integer quan = null;
		if (stDetail == null) {
			return;
		}
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO(fromSerial, stDetail.getGoodsStatus(),
				InventoryConstanst.StockStatus.INSTOCK, sessionBean.getShop().getShopId(), null, stDetail.getGoodsId());
//		sgis.setShopId(std.getSho);
//		sgis.setGroupId(sessionBean.getE(std.getGoodsId()).getEquipmentsGroupId());
		sgis.setEquipmentProfileStatus(statusIdFrm);
		sgis.setNotSerial(true);
		listSerial = sessionBean.getService().searchStockEquipmentsInvSerial(fromSerial, toSerial, sgis, null, 0, quan);
		setSerial = stDetail.getSetSerial();
		if (setSerial != null)
			listSerial.removeAll(setSerial);

		listAnaSerial = new ArrayList<>();
		if (listSerial != null && !listSerial.isEmpty()) {
			AnalysisSerial as = new AnalysisSerial(null, listSerial);
			listAnaSerial = as.analysisInvSerial();
		}
	}
	public void removeSerial(StockGoodsInvSerialDTO sgi) {
		stDetail.getSetSerial().remove(sgi);
		setSerial = stDetail.getSetSerial();
		searchSerial();
	}
	public void changeGoodsGroup() {
		if (lsGoodsGroup != null && !lsGoodsGroup.isEmpty() && (goodsGroupId == null || goodsGroupId == 0L)) {
			setGoodsGroupId(lsGoodsGroup.get(0).getEquipmentsGroupId());
		}
		goods = "";
		goodsName = "";
		goodsId = null;
		if (getGoodsGroupId() != null && getGoodsGroupId() > 0) {
			List<EquipmentsProfile> lsg = sessionBean.getLsEquipments();
			lsEquipments = new ArrayList<>();
			if (lsg != null && !lsg.isEmpty()) {
				for (EquipmentsProfile g : lsg) {
					if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
						continue;
					}
					if (goodsGroupId.equals(g.getEquipmentsGroupId())) {
						lsEquipments.add(g);
					}
				}
			}
		}
	}

	public void goodsSelect() {
		goodsId = null;
		if (lsEquipments != null) {
			for (EquipmentsProfile g : lsEquipments) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
					continue;
				}
				if ((g.getProfileCode() + " | " + g.getProfileName()).equals(goods)) {
					goodsId = g.getProfileId();
					goodsName = g.getProfileName();
					break;
				}
			}
		}
	}

	public List<String> completeGoods(String gs) {
		goods = gs;
		List<String> rs = new ArrayList<>();
		if (lsEquipments != null && !lsEquipments.isEmpty()) {
			for (EquipmentsProfile g : lsEquipments) {
				if(InventoryConstanst.ProviderStatus.IN_ACTIVE.equals(g.getEquipProfileStatus())){
					continue;
				}
				String search =g.getProfileCode() + " | " + g.getProfileName();
				if (gs == null || gs.isEmpty() || search.toLowerCase().contains(goods.toLowerCase())) {
					rs.add(g.getProfileCode() + " | " + g.getProfileName());
				}
			}
		}
		goodsSelect();
		return rs;
	}

	public void changeType() {
		if (listTransType != null && !listTransType.isEmpty()) {
			if (typeId == null || typeId == 0L) {
				typeId = listTransType.get(0).getValue();
			}
		}
		typeId  =Long.parseLong(InventoryConstanst.TransactionType.EX_STAFF);
		if (typeId != null && typeId > 0) {
			goodStatus = typeId.toString().equals(InventoryConstanst.TransactionType.EX_PARENT_ERR)
					? InventoryConstanst.GoodsStatus.ERROR : InventoryConstanst.GoodsStatus.NOMAL;
			orderCode = "EMEX_" + typeId + sf.format(new Date())
					+ sessionBean.getService().getTransactionActionCount(InventoryConstanst.TransactionType.EX_STAFF).toString();
//					+ sessionBean.getService().getTransactionActionCount(typeId.toString()).toString();
			listReason = sessionBean.getService().getListReason(typeId.toString());
			if (listReason != null) {
				reasonId = listReason.get(0).getValue();
			}
		} else {
			goodStatus = 0L;
			orderCode = "";
		}
		lsData = null;
	}

	public void refreshStd() {
		if (stDetail != null) {
			stDetail.setSetSerial(new ArrayList<StockGoodsInvSerialDTO>());
		}
	}

	public void search() {
		Integer quan = null;
		if (listTransType == null || listTransType.isEmpty() || sessionBean.getLsEquipments() == null
				|| sessionBean.getLsEquipments().isEmpty()) {
			return;
		}
		typeId = (typeId == null || typeId < 1) ? listTransType.get(0).getValue() : typeId;
		changeType();
		if (fromSerial != null && !fromSerial.trim().isEmpty() && !fromSerial.matches("^[0-9A-Fa-f]+$")) {

			languageBean.showMeseage("Info", "fromSerialReq");
			return;

		}
		if (toSerial != null && !toSerial.trim().isEmpty() && !toSerial.matches("^[0-9A-Fa-f]+$")) {

			languageBean.showMeseage("Info", "toSerialReq");
			return;

		}
		if (quantity == null || quantity.trim().isEmpty()) {
			languageBean.showMeseage("Info", "qttReqErr");
			return;
		} else {
			if (!quantity.matches("^[0-9]+$")) {

				languageBean.showMeseage("Info", "qttReqErr2");
				return;

			} else {
				quan = Integer.parseInt(getQuantity());
				if (quan <= 0) {
					languageBean.showMeseage("Info", "qttReqErr2");
					return;
				}
			}
		}
		StockGoodsInvSerialDTO sgis = new StockGoodsInvSerialDTO("", getGoodStatus(), InventoryConstanst.StockStatus.INSTOCK,
				shop.getShopId(), null, getGoodsId());
		setLsData(
				sessionBean.getService().searchStockEquipmentsInvSerial(fromSerial, toSerial, sgis, goodsGroupId, 0, quan));
		if (lsData == null || lsData.isEmpty()) {
			languageBean.showMeseage("Info", "noRecordFound");
		}
		lsData.removeAll(stDetail.getSetSerial());
	}

	public StockTransactionDetail getByGoodsId(Long gId, Long gsts, Long quantityCurrent) {
		for (StockTransactionDetail std : getSt().getLsDetail()) {
			if (std.getGoodsId().equals(goodsId) && std.getGoodsStatus().equals(gsts)) {
				std.setQuantity(std.getQuantity()+quantityCurrent);
//				std.setQuantity(quantityCurrent);
				return std;
			}
		}
		StockTransactionDetail std = new StockTransactionDetail(quantityCurrent, gId, gsts);
		EquipmentsProfile g = sessionBean.getGoods(gId.toString());
		if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(g.getManagementType())){
			List<EquipmentsDetail> details = equipmentsDetailService.findByProfile(gId);
			if(details!=null && !details.isEmpty()){
				std.setProviderId(details.get(0).getProviderId());
			}
		}

		st.getLsDetail().add(std);
		return std;
	}

	public void add() {
		if(domainGroupId==null){
			languageBean.showMeseage("Info", "choose.type.group");
			return;
		}
		if(goodsGroupId==null){
			languageBean.showMeseage("Info", "choose.type.group");
			return;
		}
		if(goodsId==null){
			languageBean.showMeseage("Info", "choose.goodsId");
			return;
		}
		if(quantity==null || quantity.trim().isEmpty()){
			languageBean.showMeseage("Info", "input.quantity");
			return;
		}else if(!StringUtils.isNumberLong̣̣(quantity.trim()) || Long.parseLong(quantity.trim()) < 1) {
			languageBean.showMeseage("Info", "input.quantity.not.number");
			return;
		}
		if(showMsgQuantity()){
			return;
		}
		stDetail = getByGoodsId(goodsId, InventoryConstanst.GoodsStatus.NOMAL, Long.parseLong(quantity.trim()));
		quantity ="";
		if(stDetail != null){
			disablePrint = false;
		}
		if(subStaffId==null){
			disablePrint = true;
		}
//		stDetail.getSetSerial().addAll(lsg);
//		lsData = new ArrayList<>();
//		Long qt = 0L + stDetail.getSetSerial().size();
//		stDetail.setQuantity(qt);
		// lsData = new ArrayList<>();
//		fromSerial = "";
//		toSerial = "";


	}
	public Boolean showMsgQuantity(){
		try {
			List<StockGoods> stds = sessionBean.getService().getStockGoodsNotStatusError(goodsId,
					sessionBean.getShop().getShopId());
			if(stds==null || stds.isEmpty()){
				languageBean.showMeseage("Info", "input.stock.not.quantity.shop");
				return true;
			}else {
				Long quan =  0L;
				Long avail =  0L;
				Long quanInput = Long.parseLong(quantity.trim());
				for (StockTransactionDetail std : getSt().getLsDetail()) {
					if (std.getGoodsId().equals(goodsId)
							&& std.getGoodsStatus().equals(InventoryConstanst.GoodsStatus.NOMAL)) {
						quanInput += std.getQuantity();
						break;
					}
				}
				for (StockGoods stg:stds) {
					if(stg.getAvailableQuantity()!=null){
						avail+=stg.getAvailableQuantity();
					}
					if(stg.getQuantity()!=null){
						quan+= stg.getQuantity();
					}
				}
//				if(quanInput > quan || quanInput > avail){
//					languageBean.showMeseage("Info", "input.stock.not.quantity.ktv",
//							new Object[] {avail < quan ? avail:quan });
//					return true;
//				}
				if(quanInput > avail){
					languageBean.showMeseage("Info", "input.stock.not.quantity.ktv",
							new Object[] {avail});
					return true;
				}
			}

		}catch (Exception ex){

			ex.printStackTrace();
			return true;
		}
		return false;
	}

	public List<EquipmentsGroup>  getGoodsGroupByType(Long equipType) {
		List<EquipmentsGroup> objs = new ArrayList<>();
		List<EquipmentsGroup> lsAll = sessionBean.getLsgGroupActive();
		if ( lsAll!= null && equipType != null) {
			for (EquipmentsGroup g : lsAll) {
				if (equipType.equals(g.getEquipmentsGroupType())) {
					objs.add(g);
				}
			}
		}
		return objs;
	}
	public void initCbb(){
		if(sessionBean.getLsgDomainGroup()!=null && !sessionBean.getLsgDomainGroup().isEmpty()){
			domainGroupId = sessionBean.getLsgDomainGroup().get(0).getValue();
		}
		selectEquipType();
	}
	public void selectEquipType() {
		lsgGroup.clear();
		if(domainGroupId!=null){
			lsgGroup = getGoodsGroupByType(domainGroupId);
			goodsGroupId = null;
			if(lsgGroup!=null && !lsgGroup.isEmpty()){
				goodsGroupId = lsgGroup.get(0).getEquipmentsGroupId();
			}
			goods = "";
			goodsName = "";
			goodsId = 0L;
			changeGoodsGroup();
		}
	}

	public void detailInfo() {
		changeType();
		if (stDetail == null || stDetail.getGoodsId() == null) {
			languageBean.showMeseage("Info", "export.not.goodid");
			return;
		}
//		stDetail = getByGoodsId(goodsId, goodStatus);
//		fromSerial = "";
//		toSerial = "";
//		quantity = "10000";
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/ex-eshop-sub.xhtml?faces-redirect=false");
	}

	public void viewClick(SelectEvent event) {
		stockTransactionDetailSelected.getGoodsId();
		stDetail = (StockTransactionDetail) event.getObject();
		if(stDetail!=null&&stDetail.getGoodsId()!=null){
			for (EquipmentsProfile profile : sessionBean.getLsEquipments()) {
				if(profile.getProfileId().equals(stDetail.getGoodsId())
						&& InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
					disableViewDetail = false;
					return;
				}
			}
		}

		disableViewDetail = true;
		fromSerial = "";
	}

	public void refresh() {
		disablePrint = true;
		disableAdd = true;
		disableViewDetail = true;
		setSt(new StockTransaction(null, staff.getShopId(), null, shop.getShopId(),
				InventoryConstanst.StockTransactionType.TRANS, InventoryConstanst.StockTransactionStatus.SUCCESS,
				InventoryConstanst.StockTransactionDeliveryType.TRANS));
		getSt().setLsDetail(new ArrayList<StockTransactionDetail>());
		stDetail = new StockTransactionDetail();
		changeType();
	}

	public String exitSub() {
		if (stDetail.getSetSerial().isEmpty()) {
			st.getLsDetail().remove(stDetail);
		}
		checkSerial();
		stDetail = new StockTransactionDetail();
		return "ex-eshop";
	}

	public void removeRs(StockGoodsInvSerial gs) {
		getStDetail().getSetSerial().remove(gs);
		stDetail.setQuantity(stDetail.getQuantity() - 1);
	}

	public void removeDtRs(StockTransactionDetail gs) {
		getSt().getLsDetail().remove(gs);
		if (stDetail != null && gs.getRowkey() == stDetail.getRowkey()) {
			stDetail = null;
		}
		if(getSt().getLsDetail().size() < 1){
			disablePrint = true;
		}
	}

	public String checkSerial() {
//		if (getSt() == null || getSt().getLsDetail().isEmpty()) {
//			disableAdd = true;
//			return "Stock detail is empty !";
//		}
//		for (StockTransactionDetail item : getSt().getLsDetail()) {
//			if (item.getSetSerial() == null || item.getSetSerial().isEmpty()) {
//				disableAdd = true;
//				return "Detail " + item.getProfileName() + " has no Serial";
//			}
//		}
//		disableAdd = false;
		return null;
	}

	public void action() {
		changeType();
		if (getSt().getLsDetail().isEmpty()) {
			return;
		} else {
			String s = checkSerial();
			if (s != null) {
				return;
			}
			if (typeId < 1L) {
				return;
			}
		}
		try {
			StockTransaction strs = new StockTransaction(sessionBean.getStaff().getStaffId(),
					sessionBean.getShop().getShopId(), subStaffId, subShopId, typeId.toString(),
					InventoryConstanst.StockTransactionStatus.SUCCESS,
					InventoryConstanst.StockTransactionDeliveryType.TRANS);
			strs.setLsDetail(st.getLsDetail());
			sessionBean.getService().exportSaveKTV(typeId, orderCode, strs, reasonId, referenceCode, note);
			getLanguageBean().showMeseage("success", "succesProces");


//			boolean k = sessionBean.getService().exEShop(ta, getSt(), staff.getStaffId());
//			if (k) {
//				getLanguageBean().showMeseage("success", "succesProces");
//			} else {
//				getLanguageBean().showMeseage("Info", "errorProcess");
//			}
			
			disablePrint = false;
			disableAdd = true;
			note = "";
			referenceCode = "";

			refresh();
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfExSReq').hide();");
		} catch (Exception ex) {
			ex.printStackTrace();
			getLanguageBean().showMeseage("error", "errorProcess");
		}
	}

	public void save() {
		changeType();
		if (getSt().getLsDetail().isEmpty()) {
			languageBean.showMeseage("Info", "not.infor.equipment");
			return;
		} else {
			String s = checkSerial();
			if (s != null) {
				languageBean.showMeseage_("Info", s);
				return;
			}
//			if (typeId < 1L || reasonId == null || reasonId < 1L) {
//				languageBean.showMeseage("Info", "exrequireReason");
//				return;
//			}
			//todo: nhan vien nhan
			if (subStaffId == null || subStaffId < 1L) {
				languageBean.showMeseage("Info", "exrequire.nv.import");
				return;
			}
			description = "";
			ta = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(),
					InventoryConstanst.TransactionStatus.EX_STAFF, shop.getShopId(), subShopId);
			ta.setAssessmentStaffId(subStaffId);
			ta.setToShopId(subShopId);

			ta.setAssessmentStaffId(staff.getStaffId());
			ta.setReasonId(reasonId);
			ta.setReasonName(getReasonName(reasonId));
			ta.setDescription(note != null && !note.trim().isEmpty()
					? staff.getUserName() + ": " + note : "");
			ta.setReferenceCode(referenceCode);
			RequestContext context = RequestContext.getCurrentInstance();
			context.execute("PF('cfExSReq').show();");
		}
	}

	public void export(){
		setFileExport(null);
		RequestContext context = RequestContext.getCurrentInstance();
//		if (subStaffId == null || subStaffId < 1L || subShopId ==null || subShopId < 1L) {
//			languageBean.showMeseage("Info", "exrequireToShop");
//			context.update("frm:messages");
//			return;
//		}
//		if (getSt().getLsDetail().isEmpty()) {
//			languageBean.showMeseage("Info", "NoDetailReq");
//			context.update("frm:messages");
//			return;
//		}
    	try {
				List<EquipmentsProfile> lstEquipmentProfile = sessionBean.getLsEquipments();
    			Long sumQuantity = 0L;
    			int index = 0;
				List<TransactionActionDetail> lsDetailExport = new ArrayList<>();
    			for (StockTransactionDetail a : getSt().getLsDetail()) {
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
							lsDetailExport.add(detailAdd);
							detailAdd.setIndex(++index);
							sumQuantity += detailAdd.getQuantity();
						}
					}else{
						sumQuantity += a.getQuantity();
						TransactionActionDetail detailAdd = new TransactionActionDetail();
						detailAdd.setIndex(++index);
						detailAdd.setQuantity(a.getQuantity());
						detailAdd.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
						detailAdd.setGoodsCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
						detailAdd.setGoodsName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
						detailAdd.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(a.getGoodsStatus()));
						detailAdd.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
						lsDetailExport.add(detailAdd);
					}

//    				sumQuantity += a.getQuantity();
//					a.setIndex(++index);
//					a.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
//					a.setProfileCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
//					a.setProfileName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
//					a.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(a.getGoodsStatus()));
    			}
    			TransactionAction tmp = new TransactionAction(orderCode, typeId.toString(), staff.getStaffId(), staff.getShopId(),
    					InventoryConstanst.TransactionStatus.EX_STAFF, shop.getShopId(), subShopId);

    			tmp.setAssessmentStaffId(staff.getStaffId());
    			tmp.setAssStaffName(sessionBean.getStaff().getStaffName());
				tmp.setStaffName(subStaffName);
    			tmp.setReasonId(reasonId);
    			tmp.setReasonName(getReasonName(reasonId));
    			tmp.setDescription(note != null && !note.trim().isEmpty() ? staff.getUserName() + ": " + note : "");
    			tmp.setCreateDatetime(new Date());
    			tmp.setAssessmentDatetime(new Date());
    			Shop shopSub = sessionBean.getShopById(subShopId);
    			String shopNameEX = "";
    			if(shopSub!=null){
					shopNameEX =shopSub.getShopName();
				}
//			ExportExcel ee = new ExportExcel();
//			String s = ee.printStockTransactionDetailKTV(shop.getShopName(),shopNameEX, getSt().getLsDetail(), tmp, sumQuantity);
			ExportExcel ee = new ExportExcel();
			String toShop = "";//sessionBean.getService().getShopNameById(ta.getToShopId());
			String createShop = sessionBean.getService().getShopById().getShopName();
			String s = ee.printTransactionActionDetail(createShop, toShop, lsDetailExport, tmp,
					sumQuantity);

			setFileExport(new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_xuat_kho.xlsx"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

	private String getReasonName(Long reasonId) {
		if (listReason != null) {
			for (ApDomain d : listReason) {
				if (d.getValue().equals(reasonId)) {
					return d.getName();
				}
			}
		}
		return "";
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
	 * @return the goodsId
	 */
	public Long getGoodsId() {
		return goodsId;
	}

	/**
	 * @param goodsId
	 *            the goodsId to set
	 */
	public void setGoodsId(Long goodsId) {
		this.goodsId = goodsId;
	}

	/**
	 * @return the goodStatus
	 */
	public Long getGoodStatus() {
		return goodStatus;
	}

	/**
	 * @param goodStatus
	 *            the goodStatus to set
	 */
	public void setGoodStatus(Long goodStatus) {
		this.goodStatus = goodStatus;
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
	 * @return the lsData
	 */
	public List<StockGoodsInvSerialDTO> getLsData() {
		return lsData;
	}

	/**
	 * @param lsData
	 *            the lsData to set
	 */
	public void setLsData(List<StockGoodsInvSerialDTO> lsData) {
		this.lsData = lsData;
	}

	/**
	 * @return the st
	 */
	public StockTransaction getSt() {
//		if (st != null) {
//			for (StockTransactionDetail std : st.getLsDetail()) {
//				if (std.getSetSerial().isEmpty() && !std.getGoodsId().equals(stDetail.getGoodsId())) {
//					st.getLsDetail().remove(std);
//				}
//			}
//		}
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
	 * @return the goodsName
	 */
	public String getGoodsName() {
		return goodsName;
	}

	/**
	 * @param goodsName
	 *            the goodsName to set
	 */
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	/**
	 * @return the goodsStatusName
	 */
	public String getGoodsStatusName() {
		return goodsStatusName;
	}

	/**
	 * @param goodsStatusName
	 *            the goodsStatusName to set
	 */
	public void setGoodsStatusName(String goodsStatusName) {
		this.goodsStatusName = goodsStatusName;
	}

	/**
	 * @return the disableReason
	 */
	public Boolean getDisableReason() {
		if (getSt() == null || getSt().getLsDetail() == null || getSt().getLsDetail().isEmpty()) {
			disableReason = false;
		} else {
			disableReason = true;
		}
		return disableReason;
	}

	/**
	 * @param disableReason
	 *            the disableReason to set
	 */
	public void setDisableReason(Boolean disableReason) {
		this.disableReason = disableReason;
	}

	/**
	 * @return the lsGoods
	 */
	public List<EquipmentsProfile> getLsEquipments() {
		return lsEquipments;
	}

	/**
	 * @param lsGoods
	 *            the lsGoods to set
	 */
	public void setLsGoods(List<EquipmentsProfile> lsGoods) {
		this.lsEquipments = lsGoods;
	}

	/**
	 * @return the goodsGroupId
	 */
	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	/**
	 * @param goodsGroupId
	 *            the goodsGroupId to set
	 */
	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

	/**
	 * @return the disableAdd
	 */
	public Boolean getDisableAdd() {

		return disableAdd;//checkSerial() != null;
	}

	/**
	 * @param disableAdd
	 *            the disableAdd to set
	 */
	public void setDisableAdd(Boolean disableAdd) {
		this.disableAdd = disableAdd;
	}

	public int getSizers() {
		if (stDetail == null || stDetail.getSetSerial().isEmpty()) {
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

	public List<SerialA> getSetAnaSerial() {
		setAnaSerial = new ArrayList<>();
		if (stDetail != null && !stDetail.getSetSerial().isEmpty()) {
			AnalysisSerial an = new AnalysisSerial(null, new ArrayList<>(stDetail.getSetSerial()));
			setAnaSerial = an.analysisInvSerial();
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
	 * @return the setLsData
	 */
	public List<SerialA> getSetLsData() {
		setLsData = new ArrayList<>();
		if (lsData != null && !lsData.isEmpty()) {
			AnalysisSerial an = new AnalysisSerial(null, new ArrayList<>(lsData));
			setLsData = an.analysisInvSerial();
		}
		return setLsData;
	}

	/**
	 * @param setLsData
	 *            the setLsData to set
	 */
	public void setSetLsData(List<SerialA> setLsData) {
		this.setLsData = setLsData;
	}

	/**
	 * @return the goods
	 */
	public String getGoods() {
		return goods;
	}

	/**
	 * @param goods
	 *            the goods to set
	 */
	public void setGoods(String goods) {
		this.goods = goods;
	}

	/**
	 * @return the setLsDataSelect
	 */
	public List<SerialA> getSetLsDataSelect() {
		return setLsDataSelect;
	}

	/**
	 * @param setLsDataSelect
	 *            the setLsDataSelect to set
	 */
	public void setSetLsDataSelect(List<SerialA> setLsDataSelect) {
		this.setLsDataSelect = setLsDataSelect;
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

	public Long getSubStaffId() {
		return subStaffId;
	}

	public void setSubStaffId(Long subStaffId) {
		this.subStaffId = subStaffId;
	}

	public String getSubShop() {
		return subShop;
	}

	public void setSubShop(String subShop) {
		this.subShop = subShop;
	}

	public List<Staff> getChildShop() {
		return childShop;
	}

	public void setChildShop(List<Staff> childShop) {
		this.childShop = childShop;
	}

	public StreamedContent getFileExport() {
		return fileExport;
	}

	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
	}

	public Boolean getDisablePrint() {
		return disablePrint;
	}

	public void setDisablePrint(Boolean disablePrint) {
		this.disablePrint = disablePrint;
	}

	public String getRefreshString() {
		return disablePrint?"Xoá":"Tạo đơn hàng mới";
	}

	public void setRefreshString(String refreshString) {
		this.refreshString = refreshString;
	}

	/**
	 * @return the exportDate
	 */
	public Date getExportDate() {
		return exportDate;
	}

	/**
	 * @param exportDate the exportDate to set
	 */
	public void setExportDate(Date exportDate) {
		this.exportDate = exportDate;
	}

	/**
	 * @return the domainGroupId
	 */
	public Long getDomainGroupId() {
		return domainGroupId;
	}

	/**
	 * @param domainGroupId the domainGroupId to set
	 */
	public void setDomainGroupId(Long domainGroupId) {
		this.domainGroupId = domainGroupId;
	}

	/**
	 * @return the listDomainGoodsGroup
	 */
	public List<ApDomain> getListDomainGoodsGroup() {
		return listDomainGoodsGroup;
	}

	/**
	 * @param listDomainGoodsGroup the listDomainGoodsGroup to set
	 */
	public void setListDomainGoodsGroup(List<ApDomain> listDomainGoodsGroup) {
		this.listDomainGoodsGroup = listDomainGoodsGroup;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Boolean getDisableViewDetail() {
		return disableViewDetail;
	}

	public void setDisableViewDetail(Boolean disableViewDetail) {
		this.disableViewDetail = disableViewDetail;
	}

	public Long getStatusIdFrm() {
		return statusIdFrm;
	}

	public void setStatusIdFrm(Long statusIdFrm) {
		this.statusIdFrm = statusIdFrm;
	}

	public List<StockGoodsInvSerialDTO> getListSerial() {
		return listSerial;
	}

	public void setListSerial(List<StockGoodsInvSerialDTO> listSerial) {
		this.listSerial = listSerial;
	}

	public List<StockGoodsInvSerialDTO> getSetSerial() {
		return setSerial;
	}

	public void setSetSerial(List<StockGoodsInvSerialDTO> setSerial) {
		this.setSerial = setSerial;
	}

	public List<SerialA> getListAnaSerial() {

		return listAnaSerial;
	}

	public void setListAnaSerial(List<SerialA> listAnaSerial) {
		this.listAnaSerial = listAnaSerial;
	}

	public List<SerialA> getListAnaSerialSelect() {
		return listAnaSerialSelect;
	}

	public void setListAnaSerialSelect(List<SerialA> listAnaSerialSelect) {
		this.listAnaSerialSelect = listAnaSerialSelect;
	}

	public Boolean getDisableSub() {
		return disableSub;
	}

	public void setDisableSub(Boolean disableSub) {
		this.disableSub = disableSub;
	}

	public void hanleLsDataSelect() {

	}

	public boolean isDisableApp() {
		try {
			disableApp = false;
			if (st != null) {
				if (st != null && !st.getLsDetail().isEmpty()) {
					for (StockTransactionDetail stds : st.getLsDetail()) {
						EquipmentsProfile profile = equipmentsProfileService.findByProfileId(stds.getGoodsId());
						if(InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
							if (stds.getQuantity() > stds.getSetSerial().size()
									|| stds.getQuantity() < stds.getSetSerial().size()) {
								disableApp = true;
//								languageBean.showMeseage("Info", "input.stock.not.quantity.ktv");
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
								StockGoods stockGood = sessionBean.getService().getStockGoodQuantity(stds.getGoodsId(),status, sessionBean.getShop().getShopId());
								if(stockGood==null || stockGood.getQuantity() < quantity || stockGood.getAvailableQuantity() <  quantity){
									disableApp = true;
//									languageBean.showMeseage("Info", "input.stock.not.quantity.ktv");
									return disableApp;
								}
							}
						}else {
							StockGoods stockGood = sessionBean.getService().getStockGoodQuantity(stds.getGoodsId(),InventoryConstanst.GoodsStatus.NOMAL, st.getFromShopId());
							if(stockGood==null || stockGood.getQuantity() < stds.getQuantity() || stockGood.getAvailableQuantity() < stds.getQuantity()){
								disableApp = true;
//								languageBean.showMeseage("Info", "input.stock.not.quantity.ktv");
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

	public void setDisableApp(boolean disableApp) {
		this.disableApp = disableApp;
	}

	public StockTransactionDetail getStockTransactionDetailSelected() {
		return stockTransactionDetailSelected;
	}

	public void setStockTransactionDetailSelected(StockTransactionDetail stockTransactionDetailSelected) {
		this.stockTransactionDetailSelected = stockTransactionDetailSelected;
	}

	public List<EquipmentsGroup> getLsgGroup() {
		return lsgGroup;
	}

	public void setLsgGroup(List<EquipmentsGroup> lsgGroup) {
		this.lsgGroup = lsgGroup;
	}

	public String getReferenceCode() {
		return referenceCode;
	}

	public void setReferenceCode(String referenceCode) {
		this.referenceCode = referenceCode;
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
