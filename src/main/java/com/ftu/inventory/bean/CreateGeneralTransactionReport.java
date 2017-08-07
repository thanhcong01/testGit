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
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.StockTransaction;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyTransActionDetailModel;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.ComponentUtils;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class CreateGeneralTransactionReport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LazyDataModel<TransactionAction> listTrans;
	private LazyDataModel<TransactionActionDetail> listTransDetail;
	private LazyDataModel<TransactionActionDetail> listTransDetailEX;
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
	
	StockTransaction st;
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

	@PostConstruct
	public void init() {
		sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
//		listShop = new ArrayList<>();
//		_getListShop(sessionBean.getService().getAllShop(sessionBean.getShop().getShopId()));
        listShop = sessionBean.getService().getTreeShop(sessionBean.getShop().getShopId());
		listTransStatus = sessionBean.getService().getListTransStatus();
		listTransType = sessionBean.getService().getListTransType();
		listGoodsStatus = sessionBean.getListStatus();
		search();
	}

	public void _getListShop(Shop s) {
		listShop.add(s);
		if (s.getChildShops() != null) {
			for (Shop sub : s.getChildShops()) {
				_getListShop(sub);
			}
		}
		
		if(listShop != null && !listShop.isEmpty()){
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
	
	public List<String> CompleteFromShop(String query){
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
		if((fromShop == null) || fromShop.equals("")){
			fromShopId = null;
				languageBean.showMeseage("Info", "inven.shop.name.just.not.empty");
				return;
		}
		if((listTypeId == null) || (listTypeId.isEmpty())){
			listTypeId = new ArrayList<>();
			for(ApDomain ap: listTransType){
				listTypeId.add(ap.getValue());
			}
		}
		listTransDetail = new LazyTransActionDetailModel(listTransType, fromShopId, fromCreateDate, toCreateDate,listTypeId , InventoryConstanst.TransactionTypeCompact.IM);
		listTransDetailEX = new LazyTransActionDetailModel(listTransType, fromShopId, fromCreateDate, toCreateDate,listTypeId , InventoryConstanst.TransactionTypeCompact.EX);
		sumIM = null;
		sumEX = null;
		if(sessionBean.getService().searchSumToShop(fromShopId, fromCreateDate, toCreateDate, listTypeId, null) != null){
			sumIM = sessionBean.getService().searchSumToShop(fromShopId, fromCreateDate, toCreateDate, listTypeId, null);
		}
		if(sessionBean.getService().searchSumFromShop(fromShopId, fromCreateDate, toCreateDate, listTypeId, null) != null){
			sumEX = sessionBean.getService().searchSumFromShop(fromShopId, fromCreateDate, toCreateDate, listTypeId, null);
		}
		
		disableExportButton = true;
		if((sumIM != null) || (sumEX != null)){
			disableExportButton = false;
		}
		
		tempShopId = fromShopId;
		tempListType = listTypeId;
		tempFromDate = fromCreateDate;
		tempToDate = toCreateDate;
		
		selectedTransactionAction = new ArrayList<>();
	}

	/**
	 * Báo cáo tổng hợp xuất nhập kho
	 * 
	 * @throws IOException
	 */
	public void exportExcel() throws IOException {
		try {
			Long sumIMForExport = null;
			Long sumEXForExport = null;
			
			int dataSizeIM = sessionBean.getService().searchSizeToShop(orderCode,tempShopId, tempFromDate, tempToDate, tempListType, null).intValue();
			List<TransactionActionDetail> listIM = sessionBean.getService().searchToShop(orderCode,tempShopId, tempFromDate, tempToDate, tempListType, null, 0, dataSizeIM);
			int dataSizeEX = sessionBean.getService().searchSizeFromShop(orderCode,tempShopId, tempFromDate, tempToDate, tempListType, null).intValue();
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
	
	private void setAttribute(List<TransactionActionDetail> list){
		SimpleDateFormat sf = new SimpleDateFormat("dd/MM/yyyy");
		if(list != null){
			int index=0;
			for(TransactionActionDetail ta: list){
				
				if(ta.getTransactionAction() != null){
					ta.setIndex(++index);
					ta.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(ta.getGoodsId().toString()));
					ta.setGoodsCode(sessionBean.getEquipProfileCode(ta.getGoodsId().toString()));
					ta.setGoodsName(sessionBean.getEquipProfileName(ta.getGoodsId().toString()));
					//ta.getTransactionAction().set("a test");
					ta.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(ta.getGoodsStatus()));
					ta.setStrCreateDatetime(sf.format(ta.getCreateDatetime()));
					ta.getTransactionAction().setTypeName(getTransType(Long.parseLong(ta.getTransactionAction().getTransactionType())));
				}
//				else
//				{
//					//System.out.println("aaa");
//				}
				//System.out.println(ta.getIndex() + " - " + ta.getRowkey() + " - " + ta.getTransactionAction().getTransactionActionCode());
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

	public LazyDataModel<TransactionActionDetail> getListTransDetail() {
		return listTransDetail;
	}

	public void setListTransDetail(LazyDataModel<TransactionActionDetail> listTransDetail) {
		this.listTransDetail = listTransDetail;
	}

	public LazyDataModel<TransactionActionDetail> getListTransDetailEX() {
		return listTransDetailEX;
	}

	public void setListTransDetailEX(LazyDataModel<TransactionActionDetail> listTransDetailEX) {
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
	
	
	
}
