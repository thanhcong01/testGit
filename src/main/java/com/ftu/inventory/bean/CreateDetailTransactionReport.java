/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
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

import com.ftu.java.business.EquipmentsProfileService;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.EquipmentsDetail;
import com.ftu.inventory.bo.EquipmentsProfile;
import com.ftu.inventory.bo.StockTransaction;
import com.ftu.inventory.bo.StockTransactionSerial;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyTransActionDetailModel;
import com.ftu.java.bo.SerialA;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.ComponentUtils;
import org.primefaces.model.Visibility;
import sun.java2d.cmm.Profile;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class CreateDetailTransactionReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<TransactionAction> listTrans;
	private List<TransactionActionDetail> listTransDetail;
	private List<TransactionActionDetail> listTransDetailEX;
	private String orderCode;
	private Date fromCreateDate;
	private Date toCreateDate;
	@ManagedProperty(value = "#{sessionBean}")
	private SessionBean sessionBean;
	@ManagedProperty(value = "#{languageBean}")
	private LanguageBean languageBean;
	private List<Shop> listShop;

	private List<ApDomain> listTransStatus;
	private Long typeId;
	private List<Long> listTypeId;
	private List<ApDomain> listTransType;
	private Long status;
	private String fromSerial;
	private String toSerial;
	// StockTransaction st;
	boolean fistEX = true;
	boolean fist = true;
	private Long goodsStatus;
	private List<ApDomain> listGoodsStatus;
	private List<ApDomain> listReason = new ArrayList<>();
	private Long reasonId;
	// huy
	private Long fromShopId;
	private String fromShop;
	private transient StreamedContent fileExport;
	private List<TransactionAction> selectedTransactionAction;
	private Long sumIM = 0L;
	private Long sumEX = 0L;
	private Long tempShopId;
	private List<Long> tempListType;
	private Date tempFromDate;
	private Date tempToDate;
	private boolean disableExportButton = true;
	private TransactionActionDetail transDetailSelected;
	private List<StockTransactionSerial> lsData;
	StockTransaction st;
	private TransactionAction trans;
	private List<SerialA> lsAnalsData;
	private boolean EX;
	private boolean disableDetailExportButton;
	private EquipmentsDetailService equipmentsDetailService = new EquipmentsDetailService();
	private List<EquipmentsDetail> lstAllEquipmentsDetail = new ArrayList<>();
	private List<ApDomain> listWarantytatus;
	EquipmentsProfileService profileService = new EquipmentsProfileService();
	private List<EquipmentsProfile> listAllEquipmentProfile = new ArrayList<>();
	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
//		listShop = new ArrayList<>();
//		_getListShop(sessionBean.getService().getAllShop(sessionBean.getShop().getShopId()));
        listShop = sessionBean.getService().getTreeShop(sessionBean.getShop().getShopId());
		listTransStatus = sessionBean.getService().getListTransStatus();
		listTransType = sessionBean.getService().getListTransType();
		listGoodsStatus = sessionBean.getListStatus();
		lstAllEquipmentsDetail = equipmentsDetailService.getAll();
		listWarantytatus = sessionBean.getService().getListWarantytatus();
		listAllEquipmentProfile = profileService.getAllProfile(null, true, -1, -1);
		search();

	}

	public EquipmentsProfile getProfile(Long gId){
		for (EquipmentsProfile profile:listAllEquipmentProfile) {
			if(profile.getProfileId().equals(gId)){
				return profile;
			}
		}
		return null;
	}

	public void _getListShop(Shop s) {
		listShop.add(s);
		if (s.getChildShops() != null) {
			for (Shop sub : s.getChildShops()) {
				_getListShop(sub);
			}
		}

		if (listShop != null && !listShop.isEmpty()) {
			fromShop = listShop.get(0).getShopName();
			fromShopId = listShop.get(0).getShopId();
		}

	}

	public void fromShopSelect() {
		if (listShop != null) {
			for (Shop shop : listShop) {
				if (shop.getShopName().equals(fromShop)) {
					fromShopId = shop.getShopId();
					break;
				}
			}
		}
	}

	public void viewSerialIM(SelectEvent event) {
		EX = false;
		transDetailSelected = (TransactionActionDetail) event.getObject();
		st = sessionBean.getService().getLastByTransaction(transDetailSelected.getTransactionActionId());
		lsData = null;
		// st =
		// sessionBean.getService().getLastByTransaction(trans.getTransactionActionId());
		if (st == null) {
			languageBean.showMeseage("Info", "transNoSerial");
			return;
		}
		
		EquipmentsProfile profile = sessionBean.getGoods(transDetailSelected.getGoodsId().toString());
		if (profile != null && !profile.getSerial())
		{
//			languageBean.showMeseage("Info", "serialNotRequired");
			return;
		}
//		transDetailSelected.getGoodsId(), st.getStockTransactionId(),transDetailSelected.getProviderId()
		lsData = sessionBean.getService().searchTransSearchXK(null, null, null,
				transDetailSelected.getGoodsId(), null, transDetailSelected.getTransactionActionId(),
				null, null,null, null);

		fromSerial = "";
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/create-detail-transaction-rp-sub.xhtml?faces-redirect=false");
		if(lsData != null || !lsData.isEmpty()){
			disableDetailExportButton = false;
		}
	}

	public void viewSerialEX(SelectEvent event) {
		EX = true;
		transDetailSelected = (TransactionActionDetail) event.getObject();
		st = sessionBean.getService().getLastByTransaction(transDetailSelected.getTransactionActionId());
		lsData = null;
		// st =
		// sessionBean.getService().getLastByTransaction(trans.getTransactionActionId());
		if (st == null) {
			languageBean.showMeseage("Info", "transNoSerial");
			return;
		}
		EquipmentsProfile profile = sessionBean.getGoods(transDetailSelected.getGoodsId().toString());
		if (profile != null && !profile.getSerial())
		{
//			languageBean.showMeseage("Info", "serialNotRequired");
			return;
		}
//		transDetailSelected.getGoodsId(), st.getStockTransactionId(),transDetailSelected.getProviderId()
		lsData = sessionBean.getService().searchTransSearchXK(null, null, null ,
				transDetailSelected.getGoodsId(), null, transDetailSelected.getTransactionActionId(),
				null, null,null, null);

		fromSerial = "";
		FacesContext fc = FacesContext.getCurrentInstance();
		NavigationHandler nh = fc.getApplication().getNavigationHandler();
		nh.handleNavigation(fc, null, "/create-detail-transaction-rp-sub.xhtml?faces-redirect=false");
		
		if(lsData != null || !lsData.isEmpty()){
			disableDetailExportButton = false;
		}
	}

	public void searchSerial() {
		disableDetailExportButton = true;
		if (fromSerial != null && !fromSerial.trim().isEmpty() && !fromSerial.matches("^[0-9A-Fa-f]+$")) {

			languageBean.showMeseage("Info", "fromSerialReq");
			return;

		}
		if (getToSerial() != null && !toSerial.trim().isEmpty() && !toSerial.matches("^[0-9A-Fa-f]+$")) {

			languageBean.showMeseage("Info", "toSerialReq");
			return;

		}
		ComponentUtils cu = new ComponentUtils();
		DataTable dt = (DataTable) cu.findComponent("dtOrdersSerial");
		if (dt != null) {
			dt.setFirst(0);
		}
		if (transDetailSelected == null || st == null) {
			return;
		}
//		st.getStockTransactionId()
		lsData = sessionBean.getService().searchTransSearchXK(fromSerial, toSerial, null,
				null, goodsStatus, transDetailSelected.getTransactionActionId(),
				null, null,null, null);
		tempFromSerial = fromSerial;
		tempToSerial = toSerial;
		
		if (lsData == null) return;
		if(lsData != null || !lsData.isEmpty()){
			disableDetailExportButton = false;
		}
	}

	String tempFromSerial;
	String tempToSerial;

	public List<String> CompleteFromShop(String query) {
		List<String> filteredThemes = new ArrayList<String>();

		for (int i = 0; i < listShop.size(); i++) {
			Shop skin = listShop.get(i);
			if (skin.getShopName().toLowerCase().contains(query.toLowerCase())) {
				filteredThemes.add(skin.getShopName());
			}
			if (skin.getShopName().toLowerCase().equals(query.toLowerCase())) {
				fromShopId = skin.getShopId();
			}
		}
		return filteredThemes;
	}

	public List<String> autoCompleteUsername(String query) {
		try {
			HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
					.getSession(false);
			SessionData data = (SessionData) session.getAttribute("username");
			List<String> filteredThemes = new ArrayList<String>();

			for (int i = 0; i < listShop.size(); i++) {
				Shop skin = listShop.get(i);
				if (skin.getShopName().toLowerCase().contains(query.toLowerCase())) {
					filteredThemes.add(skin.getShopName());
				}
				if (skin.getShopName().toLowerCase().equals(query.toLowerCase())) {
					fromShopId = skin.getShopId();
				}
			}
			return filteredThemes;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<String> completeOrder(String oder) {
		orderCode = oder;
		List<String> rs = new ArrayList<>();
		TransactionAction tas = new TransactionAction();
		tas.setTransactionActionCode(orderCode);
		List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, listTransType,
				listTransStatus, 0, 10);
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
		fromCreateDate = null;
		toCreateDate = null;
		reasonId = 0L;
		typeId = 0L;
	}

	public void handleSelect() {
	}


	public void search() {
		if (!fist) {
			ComponentUtils cu = new ComponentUtils();
			DataTable dt = (DataTable) cu.findComponent("dtOrdersDetail");
			if (dt != null) {
				dt.setFirst(0);
			}
		}
		if (!fistEX) {
			ComponentUtils cu = new ComponentUtils();
			DataTable dt = (DataTable) cu.findComponent("dtOrdersDetailEX");
			if (dt != null) {
				dt.setFirst(0);
			}
		}
		fistEX = false;
		fist = false;
		if ((fromShop == null) || fromShop.equals("")) {
			fromShopId = null;
			languageBean.showMeseage("Info", "inven.shop.name.just.not.empty");
			return;
		}
		if ((listTypeId == null) || (listTypeId.isEmpty())) {
			listTypeId = new ArrayList<>();
			for (ApDomain ap : listTransType) {
				listTypeId.add(ap.getValue());
			}
		}
		List<TransactionActionDetail> lstTran = new ArrayList<>();
		listTransDetail = sessionBean.getService().searchToShop(orderCode,fromShopId, fromCreateDate, toCreateDate, listTypeId, null, null,
				null);
//				new LazyTransActionDetailModel(listTransType, fromShopId, fromCreateDate, toCreateDate,
//				listTypeId, InventoryConstanst.TransactionTypeCompact.IM);

		if(listTransDetail != null){

			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			for(TransactionActionDetail ta: listTransDetail){
				if(ta.getTransactionAction() != null){
					ta.setStrCreateDatetime(sf.format(ta.getCreateDatetime()));
					ta.getTransactionAction().setTypeName(getTransType(Long.parseLong(ta.getTransactionAction().getTransactionType())));
				}
				lstTran.add(new TransactionActionDetail(ta));
			}
			listTransDetail = new ArrayList<>();
			listTransDetail.addAll(lstTran);
			lstTran = new ArrayList<>();;
			for (int i = 0; i < listTransDetail.size();i++) {
				TransactionActionDetail objAdd = listTransDetail.get(i);
				if(objAdd.getGoodsId()!=null){
					EquipmentsProfile profile = getProfile(objAdd.getGoodsId());
					if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
						lstTran.add(objAdd);
						continue;
					}
				}
				boolean dk = false;
				if(lstTran.contains(objAdd)){
					continue;
				}
				for (TransactionActionDetail obj:lstTran) {
					if(objAdd.getTransactionActionId().equals(obj.getTransactionActionId())
							&& objAdd.getGoodsId().equals(obj.getGoodsId()) ){
						dk = true;
						break;
					}
				}
				if(dk){
					continue;
				}
				for (int j = i+1; i < listTransDetail.size();j++) {
					if(j == listTransDetail.size()) break;
					TransactionActionDetail objCompare = listTransDetail.get(j);
					if(objAdd.getGoodsId()!=null){
						EquipmentsProfile profile = getProfile(objCompare.getGoodsId());
						if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
							continue;
						}
					}
					if(objAdd.getTransactionActionId().equals(objCompare.getTransactionActionId())
							&& objAdd.getGoodsId().equals(objCompare.getGoodsId()) ){
						objAdd.setQuantity(objAdd.getQuantity()+objCompare.getQuantity());
					}
				}
				lstTran.add(objAdd);
			}
			listTransDetail= new ArrayList<>();
			listTransDetail.addAll(lstTran);
			lstTran = new ArrayList<>();;
		}


		listTransDetailEX = sessionBean.getService().searchFromShop(orderCode, fromShopId, fromCreateDate, toCreateDate, listTypeId, null, null,
				null);
		if(listTransDetailEX != null){

			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			for(TransactionActionDetail ta: listTransDetailEX){
				if(ta.getTransactionAction() != null){
					ta.setStrCreateDatetime(sf.format(ta.getCreateDatetime()));
					ta.getTransactionAction().setTypeName(getTransType(Long.parseLong(ta.getTransactionAction().getTransactionType())));
				}
				lstTran.add(new TransactionActionDetail(ta));
			}
			listTransDetailEX = new ArrayList<>();
			listTransDetailEX.addAll(lstTran);
			lstTran = new ArrayList<>();;
			for (int i = 0; i < listTransDetailEX.size();i++) {
				TransactionActionDetail objAdd = listTransDetailEX.get(i);
				if(objAdd.getGoodsId()!=null){
					EquipmentsProfile profile = getProfile(objAdd.getGoodsId());
					if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
						lstTran.add(objAdd);
						continue;
					}
				}
				boolean dk = false;
				if(lstTran.contains(objAdd)){
					continue;
				}
				for (TransactionActionDetail obj:lstTran) {
					if(objAdd.getTransactionActionId().equals(obj.getTransactionActionId())
							&& objAdd.getGoodsId().equals(obj.getGoodsId()) ){
						dk = true;
						break;
					}
				}
				if(dk){
					continue;
				}
				for (int j = i+1; i < listTransDetailEX.size();j++) {
					if(j == listTransDetailEX.size()) break;
					TransactionActionDetail objCompare = listTransDetailEX.get(j);
					if(objAdd.getGoodsId()!=null){
						EquipmentsProfile profile = getProfile(objCompare.getGoodsId());
						if(!InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
							continue;
						}
					}
					if(objAdd.getTransactionActionId().equals(objCompare.getTransactionActionId())
							&& objAdd.getGoodsId().equals(objCompare.getGoodsId()) ){
						objAdd.setQuantity(objAdd.getQuantity()+objCompare.getQuantity());
					}
				}
				lstTran.add(objAdd);
			}
			listTransDetailEX= new ArrayList<>();
			listTransDetailEX.addAll(lstTran);
//			lstTran = new ArrayList<>();;
		}
		sumIM = null;
		sumEX = null;
		if (sessionBean.getService().searchSumToShop(fromShopId, fromCreateDate, toCreateDate, listTypeId,
				null) != null) {
			sumIM = sessionBean.getService().searchSumToShop(fromShopId, fromCreateDate, toCreateDate, listTypeId,
					null);
		}
		if (sessionBean.getService().searchSumFromShop(fromShopId, fromCreateDate, toCreateDate, listTypeId,
				null) != null) {
			sumEX = sessionBean.getService().searchSumFromShop(fromShopId, fromCreateDate, toCreateDate, listTypeId,
					null);
		}

		disableExportButton = true;
		if ((sumIM != null) || (sumEX != null)) {
			disableExportButton = false;
		}

		tempShopId = fromShopId;
		tempListType = listTypeId;
		tempFromDate = fromCreateDate;
		tempToDate = toCreateDate;

		selectedTransactionAction = new ArrayList<>();
	}

	/**
	 * Kết xuất chi tiết
	 * 
	 * @throws IOException
	 */
	public void detailExportExcel() throws IOException {
		try {
			SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
			int index = 0;
			for (StockTransactionSerial stockTransactionSerial : lsData) {
				stockTransactionSerial.setGoodsCode(sessionBean.getEquipProfileCode(transDetailSelected.getGoodsId().toString()));
				stockTransactionSerial.setTransactionActionCode(
						transDetailSelected.getTransactionAction().getTransactionActionCode());
				stockTransactionSerial.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(transDetailSelected.getGoodsId().toString()));
				stockTransactionSerial.setTypeName(transDetailSelected.getTransactionAction().getTypeName());
				stockTransactionSerial.setGoodsName(sessionBean.getEquipProfileName(transDetailSelected.getGoodsId().toString()));
				stockTransactionSerial.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(stockTransactionSerial.getEquipmentProfileStatus()));
				stockTransactionSerial.setIndex(++index);
				stockTransactionSerial.setWarrantyStatus(getWarranStatus(transDetailSelected.getGoodsId(), stockTransactionSerial.getSerial()));
				stockTransactionSerial.setExpireDateWarrantyStr(getWarranExpriDate(transDetailSelected.getGoodsId(), stockTransactionSerial.getSerial()));
				//stockTransactionSerial.setStrCreateDatetime(sf.format(stockTransactionSerial.getExpireDateWarranty()));
			}
			String s;
			ExportExcel ee = new ExportExcel();
			if(lsData == null || lsData.isEmpty()){
				languageBean.showMeseage("Info", "export.not.data");
				fileExport = null;
				return;
			}
				s = ee.exportDetailTransactionReport(lsData, sessionBean.getStaff().getStaffName(), sessionBean.getShopById(tempShopId).getShopName(),
						Long.parseLong(("" + lsData.size())));
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "bc_chitiet_xuatnhap_kho.xlsx");

		} catch (Exception ex) {
			ex.printStackTrace();
			languageBean.showMeseage("Info", "errorProcess");
			fileExport.getStream().close();
		}

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

//	/**
//	 * Kết xuất tất cả
//	 * 
//	 * @throws IOException
//	 */
//	public void exportExcel() throws IOException {
//		try {
//			Long sumIMForExport = null;
//			Long sumEXForExport = null;
//
//			int dataSizeIM = sessionBean.getService()
//					.searchSizeToShop(tempShopId, tempFromDate, tempToDate, tempListType, null).intValue();
//			List<TransactionActionDetail> listIMDetail = sessionBean.getService().searchToShop(tempShopId, tempFromDate,
//					tempToDate, tempListType, null, 0, dataSizeIM);
//			int dataSizeEX = sessionBean.getService()
//					.searchSizeFromShop(tempShopId, tempFromDate, tempToDate, tempListType, null).intValue();
//			List<TransactionActionDetail> listEXDetail = sessionBean.getService().searchFromShop(tempShopId,
//					tempFromDate, tempToDate, tempListType, null, 0, dataSizeEX);
//
//			// if(sessionBean.getService().searchSumToShop(tempShopId,
//			// tempFromDate, tempToDate, tempListType, null) != null){
//			// sumIMForExport =
//			// sessionBean.getService().searchSumToShop(tempShopId,
//			// tempFromDate, tempToDate, tempListType, null);
//			// }
//			// if(sessionBean.getService().searchSumFromShop(tempShopId,
//			// tempFromDate, tempToDate, tempListType, null) != null){
//			// sumEXForExport =
//			// sessionBean.getService().searchSumFromShop(tempShopId,
//			// tempFromDate, tempToDate, tempListType, null);
//			// }
//			//
//			setAttribute(listIMDetail);
//			setAttribute(listEXDetail);
//			List<StockTransactionSerial> listIMSerial = getListSerial(listIMDetail);
//			List<StockTransactionSerial> listEXSerial = getListSerial(listEXDetail);
//
//			ExportExcel ee = new ExportExcel();
//			String s = ee.exportDetailTransactionReport(null, null, getLsAnalsData(listIMSerial), getLsAnalsData(listEXSerial), tempFromDate,
//					tempToDate, sessionBean.getStaff().getStaffName(),
//					sessionBean.getShopById(tempShopId).getShopName(), sumIMForExport, sumEXForExport);
//			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "bc_chitiet_xuatnhap_kho.xlsx");
//
//		} catch (Exception ex) {
//			ex.printStackTrace();
//			languageBean.showMeseage("Info", "errorProcess");
//		}
//
//	}

	/**
	 * Báo cáo tổng hợp xuất nhập kho
	 * 
	 * @throws IOException
	 */
	public void exportGeneralExcel() throws IOException {
		try {
			Long sumIMForExport = null;
			Long sumEXForExport = null;
			
			int dataSizeIM = sessionBean.getService().searchSizeToShop(orderCode,tempShopId, tempFromDate, tempToDate, tempListType, null).intValue();
			List<TransactionActionDetail> listIM = sessionBean.getService().searchToShop(orderCode, tempShopId, tempFromDate, tempToDate, tempListType, null, 0, dataSizeIM);
			int dataSizeEX = sessionBean.getService().searchSizeFromShop(orderCode, tempShopId, tempFromDate, tempToDate, tempListType, null).intValue();
			List<TransactionActionDetail> listEX = sessionBean.getService().searchFromShop(orderCode, tempShopId, tempFromDate, tempToDate, tempListType, null, 0, dataSizeEX);
			
			if(sessionBean.getService().searchSumToShop(tempShopId, tempFromDate, tempToDate, tempListType, null) != null){
				sumIMForExport = sessionBean.getService().searchSumToShop(tempShopId, tempFromDate, tempToDate, tempListType, null);
			}
			if(sessionBean.getService().searchSumFromShop(tempShopId, tempFromDate, tempToDate, tempListType, null) != null){
				sumEXForExport = sessionBean.getService().searchSumFromShop(tempShopId, tempFromDate, tempToDate, tempListType, null);
			}
			
			setAttribute(listIM);
			setAttribute(listEX);
			 
			ExportExcel ee = new ExportExcel();
			 String s = ee.exportGeneralTransactionReport(listIM, listEX, tempFromDate, tempToDate, sessionBean.getStaff().getStaffName(), sessionBean.getShopById(tempShopId).getShopName(), sumIMForExport, sumEXForExport);
			 fileExport = new DefaultStreamedContent(new FileInputStream(s),
			 "xlsx", "bc_tonghop_xuatnhap_kho.xlsx");	
			 
		} catch (Exception ex) {
			ex.printStackTrace();
			languageBean.showMeseage("Info", "errorProcess");
		}

	}
	
	private List<StockTransactionSerial> getListSerial(List<TransactionActionDetail> listIMDetail) {

		List<StockTransactionSerial> listIMSerial = new ArrayList<>();
		for (TransactionActionDetail transDetail : listIMDetail) {
			List<StockTransactionSerial> list = sessionBean.getService().searchTransSearchXK(null, null,
					transDetail.getProviderId(), transDetail.getGoodsId(), null,
					transDetail.getTransactionActionId(), sessionBean.getService()
							.getLastByTransaction(transDetail.getTransactionActionId()).getStockTransactionId(),
					null,null, null);

			if (list != null) {
				for (StockTransactionSerial stockTransactionSerial : list) {
					stockTransactionSerial.setGoodsCode(transDetail.getGoodsCode());
					stockTransactionSerial
							.setTransactionActionCode(transDetail.getTransactionAction().getTransactionActionCode());
					stockTransactionSerial.setGoodsGroupName(transDetail.getGoodsGroupName());
					stockTransactionSerial.setTypeName(transDetail.getTransactionAction().getTypeName());
					stockTransactionSerial.setGoodsName(transDetail.getGoodsName());
				}
				listIMSerial.addAll(list);
			}
		}
		int index = 0;
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		if (listIMSerial != null) {
			for (StockTransactionSerial sts : listIMSerial) {
				sts.setIndex(++index);
				sts.setStrCreateDatetime(sf.format(sts.getStsDatetime()));
			}
		}
		return listIMSerial;
	}

	private String getTransType(Long taId) {
		if (listTransType != null) {
			for (ApDomain d : listTransType) {
				if (d.getValue().equals(taId)) {
					return d.getName();
				}
			}
		}
		return "";
	}

	private void setAttribute(List<TransactionActionDetail> list) {
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		if (list != null) {
			int index = 0;
			for (TransactionActionDetail ta : list) {
				if (ta.getTransactionAction() != null) {
					ta.setIndex(++index);
					ta.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(ta.getGoodsId().toString()));
					ta.setGoodsCode(sessionBean.getEquipProfileCode(ta.getGoodsId().toString()));
					ta.setGoodsName(sessionBean.getEquipProfileName(ta.getGoodsId().toString()));
					ta.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(ta.getGoodsStatus()));
					ta.setStrCreateDatetime(sf.format(ta.getCreateDatetime()));
					ta.getTransactionAction()
							.setTypeName(getTransType(Long.parseLong(ta.getTransactionAction().getTransactionType())));
				}
			}
		}
	}
	
	public void changeReason() {
		if (reasonId != null && reasonId > 0) {
			typeId = Long.parseLong(sessionBean.getService().getReasonByValue(reasonId).getCode());
			listReason = sessionBean.getService().getListReason(typeId.toString());
		}
	}

	public void changeType() {
		reasonId = 0L;
		if (typeId != null && typeId > 0) {
			listReason = sessionBean.getService().getListReason(typeId.toString());
		} else {
			listReason = sessionBean.getService().getListReason(null);
		}
	}

	/**
	 * @return the listTrans
	 */
	public LazyDataModel<TransactionAction> getListTrans() {
		return listTrans;
	}

	/**
	 * @param listTrans
	 *            the listTrans to set
	 */
	public void setListTrans(LazyDataModel<TransactionAction> listTrans) {
		this.listTrans = listTrans;
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
	 * @return the listShop
	 */
	public List<Shop> getListShop() {
		return listShop;
	}

	/**
	 * @param listShop
	 *            the listShop to set
	 */
	public void setListShop(List<Shop> listShop) {
		this.listShop = listShop;
	}

	/**
	 * @return the listTransStatus
	 */
	public List<ApDomain> getListTransStatus() {
		return listTransStatus;
	}

	/**
	 * @param listTransStatus
	 *            the listTransStatus to set
	 */
	public void setListTransStatus(List<ApDomain> listTransStatus) {
		this.listTransStatus = listTransStatus;
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
	 * @return the listGoodsStatus
	 */
	public List<ApDomain> getListGoodsStatus() {
		return listGoodsStatus;
	}

	/**
	 * @param listGoodsStatus
	 *            the listGoodsStatus to set
	 */
	public void setListGoodsStatus(List<ApDomain> listGoodsStatus) {
		this.listGoodsStatus = listGoodsStatus;
	}

	/**
	 * @return the goodsStatus
	 */
	public Long getGoodsStatus() {
		return goodsStatus;
	}

	/**
	 * @param goodsStatus
	 *            the goodsStatus to set
	 */
	public void setGoodsStatus(Long goodsStatus) {
		this.goodsStatus = goodsStatus;
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
	 * @return the fromCreateDate
	 */
	public Date getFromCreateDate() {
		return fromCreateDate;
	}

	/**
	 * @param fromCreateDate
	 *            the fromCreateDate to set
	 */
	public void setFromCreateDate(Date fromCreateDate) {
		this.fromCreateDate = fromCreateDate;
	}

	/**
	 * @return the toCreateDate
	 */
	public Date getToCreateDate() {
		return toCreateDate;
	}

	/**
	 * @param toCreateDate
	 *            the toCreateDate to set
	 */
	public void setToCreateDate(Date toCreateDate) {
		this.toCreateDate = toCreateDate;
	}

	public StreamedContent getFileExport() {
		return fileExport;
	}

	public void setFileExport(StreamedContent fileExport) {
		this.fileExport = fileExport;
	}

	public List<TransactionAction> getSelectedTransactionAction() {
		return selectedTransactionAction;
	}

	public void setSelectedTransactionAction(List<TransactionAction> selectedTransactionAction) {
		this.selectedTransactionAction = selectedTransactionAction;
	}

	public List<Long> getListTypeId() {
		return listTypeId;
	}

	public void setListTypeId(List<Long> listTypeId) {
		this.listTypeId = listTypeId;
	}

	public Long getFromShopId() {
		return fromShopId;
	}

	public void setFromShopId(Long fromShopId) {
		this.fromShopId = fromShopId;
	}

	public String getFromShop() {
		return fromShop;
	}

	public void setFromShop(String fromShop) {
		this.fromShop = fromShop;
	}

	public List<TransactionActionDetail> getListTransDetail() {
		return listTransDetail;
	}

	public void setListTransDetail(List<TransactionActionDetail> listTransDetail) {
		this.listTransDetail = listTransDetail;
	}

	public List<TransactionActionDetail> getListTransDetailEX() {
		return listTransDetailEX;
	}

	public void setListTransDetailEX(List<TransactionActionDetail> listTransDetailEX) {
		this.listTransDetailEX = listTransDetailEX;
	}

	public Long getSumIM() {
		return sumIM;
	}

	public void setSumIM(Long sumIM) {
		this.sumIM = sumIM;
	}

	public Long getSumEX() {
		return sumEX;
	}

	public void setSumEX(Long sumEX) {
		this.sumEX = sumEX;
	}

	public boolean isDisableExportButton() {
		return disableExportButton;
	}

	public void setDisableExportButton(boolean disableExportButton) {
		this.disableExportButton = disableExportButton;
	}

	public TransactionAction getTrans() {
		return trans;
	}

	public void setTrans(TransactionAction trans) {
		this.trans = trans;
	}

	public List<StockTransactionSerial> getLsData() {
		return lsData;
	}

	public void setLsData(List<StockTransactionSerial> lsData) {
		this.lsData = lsData;
	}

	public TransactionActionDetail getTransDetailSelected() {
		return transDetailSelected;
	}

	public void setTransDetailSelected(TransactionActionDetail transDetailSelected) {
		this.transDetailSelected = transDetailSelected;
	}

	public List<SerialA> getLsAnalsData() {
		lsAnalsData = new ArrayList<>();
		if (lsData != null && !lsData.isEmpty()) {
			AnalysisSerial ana = new AnalysisSerial(lsData, null);
			lsAnalsData = ana.analysis();
		}
		return lsAnalsData;
	}

	/**
	 * @param lsAnalsData
	 *            the lsAnalsData to set
	 */
	public void setLsAnalsData(List<SerialA> lsAnalsData) {
		this.lsAnalsData = lsAnalsData;
	}

	public boolean isDisableDetailExportButton() {
		return disableDetailExportButton;
	}

	public void setDisableDetailExportButton(boolean disableDetailExportButton) {
		this.disableDetailExportButton = disableDetailExportButton;
	}
	
	public List<SerialA> getLsAnalsData(List<StockTransactionSerial> data) {
		List<SerialA> analsData = new ArrayList<>();
		if (data != null && !data.isEmpty()) {
			AnalysisSerial ana = new AnalysisSerial(data, null);
			analsData = ana.analysis();
		}
		return analsData;
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
