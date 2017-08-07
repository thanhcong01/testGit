/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.admin.consumer.entity.SessionData;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.inventory.bo.*;
import com.ftu.inventory.bussiness.EquipmentsDetailService;
import com.ftu.inventory.dto.StockGoodsInvSerialDTO;
import com.ftu.inventory.exportExcel.ExportExcel;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.java.bo.SerialA;
import com.ftu.language.LanguageBean;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.staff.bo.Staff;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.ComponentUtils;

import org.apache.commons.lang.SerializationUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.ToggleEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.Visibility;

import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import java.io.FileInputStream;
import java.io.IOException;
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
@SessionScoped
public class ImShopImGoods implements Serializable {

    private String orderCode;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    @ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
    private Long reasonId;
    private LazyDataModel<TransactionAction> lsData;
    private List<ApDomain> listReason = new ArrayList<>();
    private StockTransaction st;
    private TransactionAction ta;
    private StockTransactionDetail std;
    private Long status;
    private Shop shop;
    private List<ApDomain> listStatus;
    private boolean disableApp;
    private int sizers;
    boolean fist = true;
    private Date createDate;
    List<Staff> listStaff;
    private String fromSerial;
    private String toSerial;
    private List<StockTransactionSerial> listSerial = new ArrayList<>();
    private List<SerialA> setAnaSerial;
    private Long typeId;
    private List<ApDomain> listTransType;
    private transient StreamedContent fileExport;
    private List<Shop> childShop;
    private Long subShopId;
    //huy
    TransactionAction transactionActionTemp;
    List<StockTransactionDetail> listSDTTemp = new ArrayList<>();
    private boolean disableFileDownloadButton = true;
    private String subShop;
    private String usernameFrm;
    private Long staffIdFrm;
    List<EquipmentsProfile> lstEquipmentProfile ;
    private StockTransactionDetail stockTransactionDetailSelected;
    private boolean disableViewDetail = true;
    private SerialA serialASelected;
    private List<StockTransactionSerial> stockSelectedMaintain;

    private EquipmentsDetailService equipmentsDetailService =  new EquipmentsDetailService();
    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        disableApp = true;
        setListTransType(getSessionBean().getService().getImReasonsNoPlanTD());
        setListStatus(getSessionBean().getService().getImImGoodsStatus(true));
        listStaff = sessionBean.getService().getListStaffByShop(null);
        shop = sessionBean.getShop();
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
        setChildShop(getSessionBean().getService().getChildShop());
        lstEquipmentProfile = sessionBean.getLsEquipments();
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
        TransactionAction tas = new TransactionAction();
        tas.setTransactionActionCode(orderCode);
        tas.setTransactionType(typeId == null ? null : typeId.toString());
        tas.setStatus(status == null ? null : status.toString());
        tas.setCreateShopId(shop.getShopId());
        tas.setReasonId(reasonId);
        //tas.setFromShopId(sessionBean.getShop().getShopParentId());
        tas.setToShopId(subShopId);
        tas.setCreateStaffId(staffIdFrm);
//        tas.setToShopId(shop.getShopId());
        tas.setCreateDatetime(createDate);

        changeType();
        lsData = new LazyTransActionModel(tas, null, listStatus, getListTransType(), listStaff, listReason);
        refresh();
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
            for (ApDomain d : getListTransType()) {
                listReason.addAll(sessionBean.getService().getListReason(d.getValue().toString()));
            }
        }
    }

    public void refresh() {
        ta = null;
        st = null;
        std = null;
        disableApp = true;
    }

    public void searchSerial() {
        if (ta == null || std == null) {
            return;
        }
        if ( std.getLsSerial() != null && listSerial!=null) {
            setAnaSerial.clear();
                AnalysisSerial as = new AnalysisSerial(getListSerial(), null);
                setAnaSerial = as.analysis();
                List<SerialA> lsRemove = new ArrayList<>();
            for (SerialA obj: setAnaSerial) {
                if(!obj.getFromSerial().contains(fromSerial.trim())){
                    lsRemove.add(obj);
                }
            }
            setAnaSerial.removeAll(lsRemove);

        }

//        listSerial = new ArrayList<>();
//        if (std != null && !std.getLsSerial().isEmpty()) {
//            for (StockTransactionSerial ss : std.getLsSerial()) {
//                if(ss.getSerial().contains(fromSerial.trim())){
//                    getListSerial().add(ss);
//                }
//            }
//        }
//        if (getListSerial() != null && !listSerial.isEmpty()) {
//            AnalysisSerial as = new AnalysisSerial(getListSerial(), null);
//            setAnaSerial = as.analysis();
//        }
//        fromSerial = "";
//        toSerial = "";
    }

    public List<String> completeOrder(String oder) {
        orderCode = oder;
        List<String> rs = new ArrayList<>();
        TransactionAction tas = new TransactionAction();
        tas.setTransactionActionCode(orderCode);
        tas.setCreateShopId(sessionBean.getShop().getShopId());
        tas.setFromShopId(sessionBean.getShop().getShopParentId());
        tas.setToShopId(sessionBean.getShop().getShopId());
        List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, getListTransType(), listStatus, 0, 10);
        ta = null;
        st = null;
        std = null;
        disableApp = true;
        if (lsTaa != null) {
            for (TransactionAction t : lsTaa) {
                rs.add(t.getTransactionActionCode());
            }
        }
        return rs;
    }

    public void oderSelect(SelectEvent event) {
//        search();
        status = 0L;
        createDate = null;
        reasonId = 0L;
        typeId = 0L;
    }

    public void action() {
        try {
            if(ta==null) {
                ta = transactionActionTemp;
            }
            if (ta == null || st == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.IM)) {
                return;
            }

            boolean k = sessionBean.getService().imImApprove(ta, InventoryConstanst.ACTION_TYPE.TYPE_7L, InventoryConstanst.StockStatus.BLOCK_TD);
            if (k) {
                getLanguageBean().showMeseage("success", "succesProces");
            } else {
                getLanguageBean().showMeseage("error", "errorValidProcess");
            }
            search();
            transactionNotificationBean.init();
            disableFileDownloadButton = true;

            RequestContext context = RequestContext.getCurrentInstance();
            context.execute("PF('cfImSIM').hide();");
        } catch (Exception ex) {
            ex.printStackTrace();
            getLanguageBean().showMeseage("error", "errorProcess");
        }
    }

    public void save() {
        if(ta==null){
             ta = transactionActionTemp;
        }
        if (ta == null || st == null || ta.getStatus().equals(InventoryConstanst.TransactionStatus.IM)) {
            return;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("PF('cfImSIM').show();");
    }

    public void view() {
    	disableFileDownloadButton = true;
        disableApp = true;
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
				disableFileDownloadButton = false;
			}
        }
        if (InventoryConstanst.TransactionStatus.EX.equals(ta.getStatus())) {
            disableApp = false;
        }
        disableViewDetail = true;
    }
    public void viewDetailEquipSerial(SelectEvent event) {
        SerialA select = (SerialA) event.getObject();
        stockSelectedMaintain = new ArrayList<>();
        for (StockTransactionSerial obj:listSerial) {
            if(obj.getEquipmentProfileId().equals(select.getGoodsId())&& obj.getSerial().equals(select.getFromSerial())){
                obj.setReasonName(transactionActionTemp.getReasonName());
                obj.setLastMaintenDate(getLastMaintenDate(obj.getSerial(),obj.getEquipmentDetail().getId()==null?null:obj.getEquipmentDetail().getId()));
                obj.setProviderId(select.getProviderId());
                stockSelectedMaintain.add(obj);
            }
        }

    }
    public String getLastMaintenDate(String serial, Long equipmentId){
        List<StockGoodsInvSerialDTO> obj = getSessionBean().getService().searchMainTen(serial, equipmentId, null, null);
        if(obj!=null &&! obj.isEmpty()){
            return obj.get(0).getActionDateStr();
        }
        return "";
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
            lstEquipmentProfile = sessionBean.getLsEquipments();
    		Long sumQuantity = 0L;
    		int index = 0;
    		List<StockTransactionDetail> lsDetail = st.getLsDetail();//new ArrayList<StockTransactionDetail>();
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
                    List<SerialA> lsSerialA = getDetailSerial(a);
                    for (SerialA s : lsSerialA) {
                        StockTransactionDetail detailAdd = new StockTransactionDetail();
                        detailAdd.setGoodsGroupName(sessionBean.getGoodsGroupNameByGoods(a.getGoodsId().toString()));
                        detailAdd.setGoodsCode(sessionBean.getEquipProfileCode(a.getGoodsId().toString()));
                        detailAdd.setGoodsName(sessionBean.getEquipProfileName(a.getGoodsId().toString()));
                        detailAdd.setGoodsStatusName(sessionBean.getService().getEquipsStatusName(s.getStatus()));
                        detailAdd.setUnitType(sessionBean.getUnitByUnitName(profileSeleted.getUnit()));
                        detailAdd.setQuantity(1L);
                        detailAdd.setSerial(s.getFromSerial());
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

//                sumQuantity += a.getQuantity();
//                a.setIndex(++index);

    		}
			ExportExcel ee = new ExportExcel();
			String toShop = sessionBean.getService().getShopNameById(ta.getToShopId());
			String createShop = sessionBean.getService().getShopById().getShopName();
			String s = ee.printStockTracsactionDetail(createShop,toShop, lsDetailExport, transactionActionTemp, sumQuantity);
			fileExport = new DefaultStreamedContent(new FileInputStream(s), "xlsx", "phieu_nhap_kho.xlsx");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
    }

    public void viewSerial(SelectEvent event) {
        std = (StockTransactionDetail) event.getObject();
        if(std!=null&&std.getGoodsId()!=null){
            lstEquipmentProfile = sessionBean.getLsEquipments();
            for (EquipmentsProfile profile:lstEquipmentProfile) {
                if(profile.getProfileId().equals(std.getGoodsId())
                        && InventoryConstanst.MANAGEMENT_TYPE.MANTENANCE_TYPE_3L.toString().equals(profile.getManagementType())){
                    disableViewDetail = false;
                    return;
                }
            }
        }
        disableViewDetail = true;
    }

    public void viewDetailSerial() throws IOException {
        std = stockTransactionDetailSelected;

        std.setLsSerial(new ArrayList<StockTransactionSerial>());
        List<StockTransactionSerial> ls = new ArrayList<>();
        ls = sessionBean.getService().getByDetail(std.getStockTransactionDetailId());
        if (ls != null) {
            std.getLsSerial().addAll(ls);
            setListSerial(std.getLsSerial());
            if (std.getLsSerial() != null && !std.getLsSerial().isEmpty()) {
                AnalysisSerial as = new AnalysisSerial(std.getLsSerial(), null);
                setAnaSerial = as.analysis();
            }
        }
        fromSerial = "";
        toSerial = "";
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "/im-shop-im-goods-sub.xhtml?faces-redirect=false");
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
     * @return the std
     */
    public StockTransactionDetail getStd() {
        return std;
    }

    /**
     * @param std the std to set
     */
    public void setStd(StockTransactionDetail std) {
        this.std = std;
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
     * @return the shop
     */
    public Shop getShop() {
        return shop;
    }

    /**
     * @param shop the shop to set
     */
    public void setShop(Shop shop) {
        this.shop = shop;
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
     * @return the sizers
     */
    public int getSizers() {
        sizers = 0;
        if (listSerial != null) {
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
        return setAnaSerial;
    }

    /**
     * @param setAnaSerial the setAnaSerial to set
     */
    public void setSetAnaSerial(List<SerialA> setAnaSerial) {
        this.setAnaSerial = setAnaSerial;
    }

    /**
     * @return the listSerial
     */
    public List<StockTransactionSerial> getListSerial() {
        return listSerial;
    }

    /**
     * @param listSerial the listSerial to set
     */
    public void setListSerial(List<StockTransactionSerial> listSerial) {
        this.listSerial = listSerial;
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

    public String getSubShop() {
        return subShop;
    }

    public void setSubShop(String subShop) {
        this.subShop = subShop;
    }


    public List<Shop> getChildShop() {
        return childShop;
    }
    public void setChildShop(List<Shop> childShop) {
        this.childShop = childShop;
    }

    public Long getSubShopId() {
        return subShopId;
    }

    public void setSubShopId(Long subShopId) {
        this.subShopId = subShopId;
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

    public StockTransactionDetail getStockTransactionDetailSelected() {
        return stockTransactionDetailSelected;
    }

    public void setStockTransactionDetailSelected(StockTransactionDetail stockTransactionDetailSelected) {
        this.stockTransactionDetailSelected = stockTransactionDetailSelected;
    }

    public boolean isDisableViewDetail() {
        return disableViewDetail;
    }

    public void setDisableViewDetail(boolean disableViewDetail) {
        this.disableViewDetail = disableViewDetail;
    }

    public List<String> CompleteSubShop(String query) {
        try {
            HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext()
                    .getSession(false);
            SessionData data = (SessionData) session.getAttribute("username");
            List<String> filteredThemes = new ArrayList<String>();
            filteredThemes.add(" ");
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
    public void SubShopSelect() {
        if (childShop != null&&!childShop.isEmpty()) {
            for (Shop shop : childShop) {
                if (shop.getShopName().equals(subShop)) {
                    subShopId = shop.getShopId();
                    break;
                }
            }
        }else {
            subShopId = null;
        }
    }
    public List<String> autoCompleteUsername(String query) {
        try {
            List<String> filteredThemes = new ArrayList<>();
            if(listStaff==null||listStaff.isEmpty()) return filteredThemes;
            for (int i = 0; i < listStaff.size(); i++) {
                Staff obj = listStaff.get(i);
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
        if (listStaff != null) {
            for (Staff obj : listStaff) {
                String value  = obj.getStaffCode() + " | "+ obj.getStaffName();
                if (value.equals(usernameFrm)) {
                    staffIdFrm = obj.getStaffId();
                    break;
                }
            }
        }
    }

    public SerialA getSerialASelected() {
        return serialASelected;
    }

    public void setSerialASelected(SerialA serialASelected) {
        this.serialASelected = serialASelected;
    }

    public List<StockTransactionSerial> getStockSelectedMaintain() {
        return stockSelectedMaintain;
    }

    public void setStockSelectedMaintain(List<StockTransactionSerial> stockSelectedMaintain) {
        this.stockSelectedMaintain = stockSelectedMaintain;
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
        visibleTable.set((Integer) e.getData(), e.getVisibility() == Visibility.VISIBLE);
    }

    public List<Boolean> getVisibleTable3() {
        return visibleTable3;
    }

    public void setVisibleTable3(List<Boolean> visibleTable) {
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
