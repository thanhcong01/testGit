/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ftu.inventory.bean;

import com.ftu.java.business.*;
import com.ftu.java.business.ImportGoods;
import com.ftu.language.LanguageBean;
import com.ftu.constanst.InventoryConstanst;
import com.ftu.staff.bo.Staff;
import com.ftu.inventory.bo.StockTransactionSerial;
import com.ftu.inventory.bo.TransactionAction;
import com.ftu.inventory.bo.TransactionActionDetail;
import com.ftu.java.bo.LazyStockSerialModel;
import com.ftu.java.bo.LazyTransActionModel;
import com.ftu.staff.bo.ApDomain;
import com.ftu.utils.ComponentUtils;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

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
@ManagedBean(name = "approveGoods", eager = true)
@ViewScoped
public class ApproveGoods implements Serializable {

    private String oderCode;
    private Long reasonId;
    private Date createDate;
    private Long staffId;
    private Long status;
    private List<Staff> users;
    private List<ApDomain> ListReason;
    private List<ApDomain> listStatus;
    private LazyDataModel<TransactionAction> lsTa;
    private List<TransactionActionDetail> lsTad;
    private String fromSerial;
    private String toSerial;
    private TransactionAction ta;
    private TransactionActionDetail tad = new TransactionActionDetail();
    private LazyDataModel<StockTransactionSerial> lsData;
    @ManagedProperty(value = "#{sessionBean}")
    private SessionBean sessionBean;
    @ManagedProperty(value = "#{languageBean}")
    private LanguageBean languageBean;
    @ManagedProperty(value = "#{transactionNotificationBean}")
    private TransactionNotificationBean transactionNotificationBean;
    private boolean disableApp;
    private boolean disableDel;
    Staff staff;
    boolean fist = true;
    private Long goodsStatus;
    private List<ApDomain> listGoodsStatus;
    List<ApDomain> listType;
    private String ACTION_CODE;
    private String confirm;
    private Long handleType;// = handleType=InventoryConstanst.ImportType.importGood;
    private String transactionType;// = InventoryConstanst.TransactionType.IM;
    private Long handtype;// = Long.parseLong(InventoryConstanst.TransactionStatus.CREATE);
    @PostConstruct
    public void init() {
        staff = sessionBean.getStaff();
        users = sessionBean.getService().getListStaffByShop(null);
        sessionBean.loadData(sessionBean.getStaff(), sessionBean.getShop());
        disableApp = true;
        disableDel = true;
//        setListReason(sessionBean.getService().getListReason(transactionType));
//        listStatus = sessionBean.getService().getApproveStatus();
//        
//        if (listStatus != null) {
//            status = listStatus.get(0).getValue();
//        }
        listGoodsStatus = sessionBean.getListStatus();
//        transactionNotificationBean.init();
//        search();
    }

    public List<String> completeOrder(String oder) {
        oderCode = oder;
        List<String> rs = new ArrayList<>();
        TransactionAction tas = new TransactionAction();
        tas.setTransactionActionCode(oderCode);
        tas.setTransactionType(transactionType);
        tas.setCreateStaffId(staffId);
        tas.setCreateShopId(sessionBean.getShop().getShopId());
        List<TransactionAction> lsTaa = sessionBean.getService().searchTransactionAction(tas, listType, listStatus, 0, 10);
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
        staffId = 0L;
        reasonId = 0L;
    }

    public void searchSerial() {
        ComponentUtils cu = new ComponentUtils();
        DataTable dt = (DataTable) cu.findComponent("dtOrdersSerial");
        if (dt != null) {
			dt.setFirst(0);
		}
        if (tad == null || tad == null) {
            return;
        }
        lsData = null;
//        if (fromSerial != null && !fromSerial.trim().isEmpty() && !fromSerial.matches("^[0-9A-Fa-f]+$")) {
//
//            languageBean.showMeseage("Info", "fromSerialReq");
//            return;
//
//        }
//        if (toSerial != null && !toSerial.trim().isEmpty() && !toSerial.matches("^[0-9A-Fa-f]+$")) {
//
//            languageBean.showMeseage("Info", "toSerialReq");
//            return;
//
//        }
        lsData = new LazyStockSerialModel(fromSerial, toSerial, getTa().getTransactionActionId(), null, tad.getProviderId(), tad.getGoodsId(), goodsStatus);

    }

    public void save() throws ParseException {
        if (getTa() == null || !(ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE) ||
                ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE_WARRANTY))) {
            return;
        }
        RequestContext context = RequestContext.getCurrentInstance();
        setACTION_CODE(InventoryConstanst.RoleKey.APPROVE);
//        action();
        confirm = languageBean.getBundle().getString("confirmApp.appro");
        context.execute("PF('cfImNCC').show();");
    }

    public void action() {

        RequestContext context = RequestContext.getCurrentInstance();
        try {
//            com.ftu.java.business.ApproveGoods.handleType = handleType;
            getTa().setAssessmentStaffId(staff.getStaffId());
            boolean k = false;
            if (getACTION_CODE().equals(InventoryConstanst.RoleKey.REJECT)) {
                k = sessionBean.getService().approveReject(getTa());
            } else if (getACTION_CODE().equals(InventoryConstanst.RoleKey.APPROVE)) {
                k = sessionBean.getService().approveApprove(getTa());
            }
            if (k) {
                getLanguageBean().showMeseage("success", "succesProces");
            } else {
                getLanguageBean().showMeseage("error", "errorValidProcess");
            }
            transactionNotificationBean.init();
            search();
            context.execute("PF('cfApp').hide();");
        } catch (Exception ex) {
            ex.printStackTrace();
            getLanguageBean().showMeseage("error", "errorProcess");
        }

    }

    public void reject() throws ParseException {
        if (getTa() == null || !(ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE) ||
                            ta.getStatus().equals(InventoryConstanst.TransactionStatus.CREATE_WARRANTY) )) {
            return;
        }
        confirm = languageBean.getBundle().getString("confirmRej.cancel");
        RequestContext context = RequestContext.getCurrentInstance();
        setACTION_CODE(InventoryConstanst.RoleKey.REJECT);
        context.execute("PF('cfImNCC').show();");
//        action();

    }

    private void refresh() {
        lsTad = null;
        setTa(new TransactionAction());
        tad = null;
        lsData = null;
        fromSerial = "";
        toSerial = "";
        disableApp = true;
        disableDel = true;
    }

    private void refreshDetail() {
        lsData = null;
        fromSerial = "";
        toSerial = "";
    }

    public void search() {
        if (!fist) {
            ComponentUtils cu = new ComponentUtils();
            DataTable dt = (DataTable) cu.findComponent("dtOrders");
            if (dt != null) {
				dt.setFirst(0);
			}
        }
        fist = false;
        TransactionAction tas = new TransactionAction();
        tas.setTransactionActionCode(oderCode);
        tas.setTransactionType(transactionType);
        tas.setReasonId(reasonId);	
        tas.setStatus(status == null ? null : status.toString());
        tas.setCreateStaffId(staffId);
        tas.setCreateShopId(sessionBean.getShop().getShopId());
        tas.setCreateDatetime(createDate);
        lsTa = new LazyTransActionModel(tas,null, listStatus, listType, users, ListReason);
        refresh();
    }

    public void viewDetail() {
        tad = null;
        lsData = null;
        fromSerial = "";
        toSerial = "";

        lsTad = sessionBean.getService().getTranActionDetailsByTransId(getTa().getTransactionActionId());
        if (lsTad != null) {

            for (TransactionActionDetail obj : lsTad) {
                obj.setTotalErr(sessionBean.getService().getStockTransactionSerialSize(obj.getProviderId(), obj.getGoodsId(), obj.getTransactionActionId(), InventoryConstanst.GoodsStatus.ERROR));
//                obj.setTotal(sessionBean.getService().getStockTransactionSerialSize(obj.getProviderId(), obj.getGoodsId(), obj.getTransactionActionId(), InventoryConstanst.GoodsStatus.NOMAL));
                obj.setTotal(obj.getQuantity());
            }
        }
        getTa().setLsDetail(lsTad);
        disableApp = true;
        disableDel = true;
        if (getTa().getStatus().equals(InventoryConstanst.TransactionStatus.CREATE)
                || getTa().getStatus().equals(InventoryConstanst.TransactionStatus.CREATE_WARRANTY)) {
            disableDel = false;
        }
        if ((getTa().getStatus().equals(InventoryConstanst.TransactionStatus.CREATE)
                || getTa().getStatus().equals(InventoryConstanst.TransactionStatus.CREATE_WARRANTY)) && lsTad != null) {
            Long k = 0L;//sessionBean.getService().checkApproveGoods(getTa().getTransactionActionId());
            if (k > 0) {
                languageBean.showMeseage("Info", "errorApprove", new Object[]{k});
                disableApp = true;
            } else {
                disableApp = false;
            }
        }
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:dtOrdersDetail");
        if (dataTable != null) {
            dataTable.reset();
        }
    }

    public void viewSerial() {
        refreshDetail();
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frm:dtOrdersDetail");
        if (dataTable != null) {
            dataTable.setSortBy(null);
            String order = dataTable.getSortOrder();
            Object obj = dataTable.getSortBy();
            dataTable.reset();
            dataTable.setSortBy(obj);
            dataTable.setSortOrder(order);
        }
        lsData = new LazyStockSerialModel(null, null, getTa().getTransactionActionId(), null, tad.getProviderId(), tad.getGoodsId(), null);
    }

    /**
     * @return the oderCode
     */
    public String getOderCode() {
        return oderCode;
    }

    /**
     * @param oderCode the oderCode to set
     */
    public void setOderCode(String oderCode) {
        this.oderCode = oderCode;
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
     * @return the ta
     */
    public TransactionAction getTa() {
        return ta == null ? new TransactionAction() : ta;
    }

    /**
     * @param ta the ta to set
     */
    public void setTa(TransactionAction ta) {
        if(ta == null) return;
        this.ta = ta;
    }

    /**
     * @return the tad
     */
    public TransactionActionDetail getTad() {
        return tad;
    }

    /**
     * @param tad the tad to set
     */
    public void setTad(TransactionActionDetail tad) {
        this.tad = tad;
    }

    /**
     * @return the lsTad
     */
    public List<TransactionActionDetail> getLsTad() {
        return lsTad;
    }

    /**
     * @param lsTad the lsTad to set
     */
    public void setLsTad(List<TransactionActionDetail> lsTad) {
        this.lsTad = lsTad;
    }

    /**
     * @return the status
     */
    public Long getStatus() {
        return status;
    }

    /**
     * @return the lsTa
     */
    public LazyDataModel<TransactionAction> getLsTa() {
        return lsTa;
    }

    /**
     * @param lsTa the lsTa to set
     */
    public void setLsTa(LazyDataModel<TransactionAction> lsTa) {
        this.lsTa = lsTa;
    }

    /**
     * @return the users
     */
    public List<Staff> getUsers() {
        return users;
    }

    /**
     * @param users the users to set
     */
    public void setUsers(List<Staff> users) {
        this.users = users;
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
     * @return the lsData
     */
    public LazyDataModel<StockTransactionSerial> getLsData() {
        return lsData;
    }

    /**
     * @param lsData the lsData to set
     */
    public void setLsData(LazyDataModel<StockTransactionSerial> lsData) {
        this.lsData = lsData;
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
     * @return the disableDel
     */
    public boolean isDisableDel() {
        return disableDel;
    }

    /**
     * @param disableDel the disableDel to set
     */
    public void setDisableDel(boolean disableDel) {
        this.disableDel = disableDel;
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
     * @return the ListReason
     */
    public List<ApDomain> getListReason() {
        return ListReason;
    }

    /**
     * @param ListReason the ListReason to set
     */
    public void setListReason(List<ApDomain> ListReason) {
        this.ListReason = ListReason;
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
     * @return the ACTION_CODE
     */
    public String getACTION_CODE() {
        return ACTION_CODE;
    }

    /**
     * @param ACTION_CODE the ACTION_CODE to set
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
     * @param confirm the confirm to set
     */
    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public Long getHandleType() {
        return handleType;
    }

    public void setHandleType(Long handleType) {
        this.handleType = handleType;
//        transactionType = InventoryConstanst.TransactionType.IM;
        if(handleType.equals(InventoryConstanst.ImportType.importKTV)){
            transactionType = InventoryConstanst.TransactionType.IM_STAFF;
            listStatus = sessionBean.getService().getApproveStatusStaff();
        }else {
            if(handleType.equals(InventoryConstanst.ImportType.importGood)){
                transactionType = InventoryConstanst.TransactionType.IM;
                listStatus = sessionBean.getService().getApproveStatus();
            }else {
                transactionType = InventoryConstanst.TransactionType.IM_WARRANTY;
                listStatus = sessionBean.getService().getApproveStatusWarranty();
            }
        }
        listType = sessionBean.getService().getApproveType(transactionType);
        setListReason(sessionBean.getService().getListReason(transactionType));
        
        
        if (listStatus != null && !listStatus.isEmpty()) {
            status = listStatus.get(0).getValue();
        }
        search();
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
}
