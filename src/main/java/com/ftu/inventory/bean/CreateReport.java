/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.language.LanguageBean;
import com.ftu.inventory.bo.StockTransaction;
import com.ftu.inventory.bo.StockTransactionSerial;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.java.bo.SerialA;
import com.ftu.staff.bo.ApDomain;
import com.ftu.staff.bo.Shop;
import com.ftu.utils.AnalysisSerial;
import com.ftu.utils.ComponentUtils;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.LazyDataModel;

/**
 *
 * @author E5420
 */
@ManagedBean
@SessionScoped
public class CreateReport implements Serializable {

    private LazyDataModel<TransactionAction> listTrans;
    private List<TransactionActionDetail> listTransDetail;
    private String orderCode;
    private Date fromCreateDate;
    private Date toCreateDate;
    private TransactionAction trans;
    private TransactionActionDetail transDetail = new TransactionActionDetail();
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    private List<Shop> listShop;
    private Long createShopId;
    private List<ApDomain> listTransStatus;
    private Long typeId;
    private List<ApDomain> listTransType;
    private Long status;
    private String fromSerial;
    private String toSerial;
    private List<StockTransactionSerial> lsData;
    private List<SerialA> lsAnalsData;
    StockTransaction st;
    boolean fist = true;
    private Long goodsStatus;
    private List<ApDomain> listGoodsStatus;
    private List<ApDomain> listReason = new ArrayList<>();
    private Long reasonId;

    @PostConstruct
    public void init() {
    	sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        listShop = new ArrayList<>();
//        if (sessionBean.getShop().getShopParentId() != null) {
//            listShop.add(sessionBean.getService().getShopChildById(sessionBean.getShop().getShopParentId()));
//        }
        _getListShop(sessionBean.getService().getAllShop(sessionBean.getShop().getShopId()));
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

    }

    public List<String> completeOrder(String oder) {
        orderCode = oder;
        List<String> rs = new ArrayList<>();
        TransactionAction tas = new TransactionAction();
        tas.setTransactionActionCode(orderCode);
        List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, listTransType, listTransStatus, 0, 10);
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
        toCreateDate=null;
        reasonId = 0L;
        typeId = 0L;
        createShopId = 0L;
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
        if (createShopId == null && !listShop.isEmpty()) {
            createShopId = listShop.get(0).getShopId();
        }
        TransactionAction tas = new TransactionAction();
        tas.setTransactionActionCode(orderCode);
        tas.setTransactionType(typeId == null ? null : typeId.toString());
        tas.setStatus(status == null ? null : status.toString());
        tas.setCreateShopId(createShopId);
        tas.setFromDate(fromCreateDate);
        tas.setToDate(toCreateDate);
        tas.setReasonId(reasonId);
        if (typeId != null && typeId > 0) {
            listReason = sessionBean.getService().getListReason(typeId.toString());
        } else {
            listReason = sessionBean.getService().getListReason(null);
        }
        listTrans = new LazyTransActionModel(tas, listShop, listTransStatus, listTransType, null, listReason);
        trans = null;
        transDetail = null;
        lsData = null;
        listTransDetail = null;
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
            listReason = sessionBean.getService().getListReason(null);
        }
    }

    public void viewDetail() {
        st = sessionBean.getService().getLastByTransaction(trans.getTransactionActionId());
        if (st != null) {
            SimpleDateFormat fomat = new SimpleDateFormat("dd/MM/yyyy");
            trans.setImportDate(st.getImportStaffId() != null ? fomat.format(st.getFinishDatetime()) : "");
            trans.setExportDate(st.getExportStaffId() != null ? fomat.format(st.getStockTransactionDatetime()) : "");
            trans.setImportStaff(sessionBean.getService().getStaffById(st.getImportStaffId()).getStaffName());
            trans.setExportStaff(sessionBean.getService().getStaffById(st.getExportStaffId()).getStaffName());
        }
        listTransDetail = sessionBean.getService().getTranActionDetailsByTransId(trans.getTransactionActionId());
        lsData = null;
    }

     public void viewSerial(SelectEvent event) {
        transDetail = (TransactionActionDetail) event.getObject();
 
        lsData = null;
        // st = sessionBean.getService().getLastByTransaction(trans.getTransactionActionId());
        if (st == null) {
            languageBean.showMeseage("Info", "transNoSerial");
            return;
        }
        lsData = sessionBean.getService().searchTransSearch(null, null, transDetail.getProviderId(), transDetail.getGoodsId(), null, trans.getTransactionActionId(), st.getStockTransactionId(), null,null, null);

        fromSerial = "";
        FacesContext fc = FacesContext.getCurrentInstance();
        NavigationHandler nh = fc.getApplication().getNavigationHandler();
        nh.handleNavigation(fc, null, "/search-transaction-sub.xhtml?faces-redirect=false");
    }

    public void searchSerial() {
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
        if (transDetail == null || st == null) {
            return;
        }
        lsData = sessionBean.getService().searchTransSearch(fromSerial, toSerial, transDetail.getProviderId(), transDetail.getGoodsId(), goodsStatus, trans.getTransactionActionId(), st.getStockTransactionId(), null, null, null);
    }

    /**
     * @return the listTrans
     */
    public LazyDataModel<TransactionAction> getListTrans() {
        return listTrans;
    }

    /**
     * @param listTrans the listTrans to set
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
     * @param orderCode the orderCode to set
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }


    /**
     * @return the trans
     */
    public TransactionAction getTrans() {
        return trans;
    }

    /**
     * @param trans the trans to set
     */
    public void setTrans(TransactionAction trans) {
        this.trans = trans;
    }

    /**
     * @return the transDetail
     */
    public TransactionActionDetail getTransDetail() {
        return transDetail;
    }

    /**
     * @param transDetail the transDetail to set
     */
    public void setTransDetail(TransactionActionDetail transDetail) {
        this.transDetail = transDetail;
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
     * @return the listShop
     */
    public List<Shop> getListShop() {
        return listShop;
    }

    /**
     * @param listShop the listShop to set
     */
    public void setListShop(List<Shop> listShop) {
        this.listShop = listShop;
    }

    /**
     * @return the createShopId
     */
    public Long getCreateShopId() {
        return createShopId;
    }

    /**
     * @param createShopId the createShopId to set
     */
    public void setCreateShopId(Long createShopId) {
        this.createShopId = createShopId;
    }

    /**
     * @return the listTransStatus
     */
    public List<ApDomain> getListTransStatus() {
        return listTransStatus;
    }

    /**
     * @param listTransStatus the listTransStatus to set
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
     * @param reasonId the reasonId to set
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
     * @param listTransType the listTransType to set
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
     * @param status the status to set
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
     * @param fromSerial the fromSerial to set
     */
    public void setFromSerial(String fromSerial) {
        this.fromSerial = fromSerial;
    }

    /**
     * @return the lsData
     */
    public List<StockTransactionSerial> getLsData() {
        return lsData;
    }

    /**
     * @param lsData the lsData to set
     */
    public void setLsData(List<StockTransactionSerial> lsData) {
        this.lsData = lsData;
    }

    /**
     * @return the listTransDetail
     */
    public List<TransactionActionDetail> getListTransDetail() {
        return listTransDetail;
    }

    /**
     * @param listTransDetail the listTransDetail to set
     */
    public void setListTransDetail(List<TransactionActionDetail> listTransDetail) {
        this.listTransDetail = listTransDetail;
    }

    /**
     * @return the listGoodsStatus
     */
    public List<ApDomain> getListGoodsStatus() {
        return listGoodsStatus;
    }

    /**
     * @param listGoodsStatus the listGoodsStatus to set
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
     * @param goodsStatus the goodsStatus to set
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
     * @param listReason the listReason to set
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
     * @param typeId the typeId to set
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
     * @param toSerial the toSerial to set
     */
    public void setToSerial(String toSerial) {
        this.toSerial = toSerial;
    }

    /**
     * @return the lsAnalsData
     */
    public List<SerialA> getLsAnalsData() {
        lsAnalsData = new ArrayList<>();
        if (lsData != null && !lsData.isEmpty()) {
            AnalysisSerial ana = new AnalysisSerial(lsData, null);
            lsAnalsData = ana.analysis();
        }
        return lsAnalsData;
    }

    /**
     * @param lsAnalsData the lsAnalsData to set
     */
    public void setLsAnalsData(List<SerialA> lsAnalsData) {
        this.lsAnalsData = lsAnalsData;
    }

    /**
     * @return the fromCreateDate
     */
    public Date getFromCreateDate() {
        return fromCreateDate;
    }

    /**
     * @param fromCreateDate the fromCreateDate to set
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
     * @param toCreateDate the toCreateDate to set
     */
    public void setToCreateDate(Date toCreateDate) {
        this.toCreateDate = toCreateDate;
    }

}
